package com.lza.pad.ui.main;

import java.io.IOException;
import java.util.Vector;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Base64;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lza.pad.R;
import com.lza.pad.db.model.EBookInfo;
import com.lza.pad.db.model.PadResource;
import com.lza.pad.db.model.ResponseData;
import com.lza.pad.helper.JsonParseHelper;
import com.lza.pad.ui.base.BaseActivity;
import com.lza.pad.zxing.camera.CameraManager;
import com.lza.pad.zxing.decoding.CaptureActivityHandler;
import com.lza.pad.zxing.decoding.InactivityTimer;
import com.lza.pad.zxing.view.ViewfinderView;

import org.apache.http.Header;

import lza.com.lza.library.http.HttpUtility;
import lza.com.lza.library.util.ToastUtils;

/**
 * Initial the camera
 */
public class MipcaActivityCapture extends BaseActivity implements Callback {
    private TextView mTitleTxt = null;

    private CaptureActivityHandler handler = null;
    private ViewfinderView viewfinderView = null;
    private boolean hasSurface = false;
    private Vector<BarcodeFormat> decodeFormats = null;
    private String characterSet = null;
    private InactivityTimer inactivityTimer = null;
    private MediaPlayer mediaPlayer = null;
    private boolean playBeep = false;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate = false;

    private PadResource mPadResource = null;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        CameraManager.init(getApplication());

        initActionBar();
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    private void initActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        log(" mActionBar = " + mActionBar);
        if (null == mActionBar)
            return;

        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.action_bar_title_layout);
        mTitleTxt = (TextView) findViewById(R.id.action_bar_title);
        mTitleTxt.setText(R.string.scan_title);

        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MipcaActivityCapture.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        String resultString = result.getText();
        if (resultString.equals("")) {
            ToastUtils.showShort(MipcaActivityCapture.this, R.string.scan_failed);
        } else {
            // 对扫码返回数据Base64解码
            String mUrl = new String(Base64.decode(resultString, Base64.DEFAULT));
            log("MipcaActivityCapture mUrl = " + mUrl);

            // 根据扫码返回URL，获取相关电子书/期刊/文献数据
            HttpUtility httpUtility = new HttpUtility(mCtx, HttpUtility.ASYNC_HTTP_CLIENT);
            httpUtility.send(mUrl, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

                }

                @Override
                public void onSuccess(int i, Header[] headers, String s) {
                    // 对书籍数据进行解析
                    ResponseData<PadResource> response = JsonParseHelper.parseResourceResponse(s);

                    if (response != null && response.getContent() != null &&
                            response.getContent().size() > 0) {
                        mPadResource = response.getContent().get(0);
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                parsePadResource();
                            }
                        });
                    }
                }
            });
        }
    }

    private void parsePadResource() {
        String source_type = mPadResource.getSource_type();
        log("MipcaActivityCapture source_type = " + source_type);
        if (source_type.equalsIgnoreCase(PadResource.RESOURCE_EBOOK)) {
            //进入电子图书详情界面
            gotoEBookDetails();
        } else if (source_type.equalsIgnoreCase(PadResource.RESOURCE_JOURNAL)) {
            //进入电子期刊详情界面
            gotoMagazineDetails();
        }
        MipcaActivityCapture.this.finish();
    }

    private void gotoEBookDetails(){
        Intent mIntent = new Intent(this , EBookDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromScan",true);
        bundle.putParcelable("book_info",mPadResource);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
    }

    private void gotoMagazineDetails(){
        Intent mIntent = new Intent(this , MagazineDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromScan",true);
        bundle.putParcelable("magazine_info",mPadResource);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}