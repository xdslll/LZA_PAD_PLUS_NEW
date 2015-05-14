package lza.com.lza.library.db;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 14-9-18.
 */
public abstract class AbstractLoader<D> extends AsyncTaskLoader<D> {

    protected D mData;
    protected Context mContext;

    public AbstractLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public abstract D loadInBackground();

    @Override
    public void deliverResult(D data) {
        if (isReset()) {
            if (data != null) {
                onReleaseResources(data);
            }
        }
        D oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null) {
            onReleaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (mData == null) {
            forceLoad();
        }

        super.onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(D data) {
        super.onCanceled(data);
        onReleaseResources(data);
    }

    protected void onReleaseResources(D apps) {
        if (apps != null) {
            apps = null;
        }
    }
}
