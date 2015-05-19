package lza.com.lza.library.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/4/15.
 */
public class HttpUtility {

    private Context mCtx;
    private static final int DEFAULT_TIME_OUT = 15 * 1000;
    private static final int DEFAULT_RETRY_COUNT = 2;
    private static final int DEFAULT_RETRY_TIME_OUT = 5 * 1000;
    public static final int ASYNC_HTTP_CLIENT = 1;
    public static final int SYNC_HTTP_CLIENT = 2;
    private AsyncHttpClient mHttpClient;

    public HttpUtility(Context c, int type) {
        mCtx = c;
        createHttpClient(type);
    }

    public HttpUtility(Context c, int type, String hostName, int port) {
        mCtx = c;
        createHttpClient(type);
        mHttpClient.setProxy(hostName, port);
    }

    public HttpUtility(Context c, int type, String hostName, int port, String username, String password) {
        mCtx = c;
        createHttpClient(type);
        mHttpClient.setProxy(hostName, port, username, password);
    }

    public void cancel() {
        cancel(true);
    }

    public void cancel(boolean mayInterruptIfRunning) {
        mHttpClient.cancelRequests(mCtx, mayInterruptIfRunning);
    }

    public void cancelAll() {
        cancelAll(true);
    }

    public void cancelAll(boolean mayInterruptIfRunning) {
        mHttpClient.cancelAllRequests(mayInterruptIfRunning);
    }

    public void send(String url, AsyncHttpResponseHandler responseHandler) {
        mHttpClient.get(url, responseHandler);
    }

    public void send(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mHttpClient.get(url, params, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mHttpClient.post(url, params, responseHandler);
    }

    public void post(UrlRequest request, AsyncHttpResponseHandler responseHandler) {
        mHttpClient.post(request.getUrl(), request.getRequestParams(), responseHandler);
    }

    public void sendByProxy(String url, String proxyHostName, int proxyPort,
                            String proxyUsername, String proxyPassword, AsyncHttpResponseHandler responseHandler) {
        mHttpClient.setProxy(proxyHostName, proxyPort, proxyUsername, proxyPassword);
        mHttpClient.get(url, responseHandler);
    }

    public void sendByProxy(String url, String proxyHostName, int proxyPort, AsyncHttpResponseHandler responseHandler) {
        mHttpClient.setProxy(proxyHostName, proxyPort);
        mHttpClient.get(url, responseHandler);
    }

    public AsyncHttpClient getHttpClient() {
        return mHttpClient;
    }

    private void createHttpClient(int type) {
        if (type == ASYNC_HTTP_CLIENT) {
            mHttpClient = new AsyncHttpClient();
        } else if (type == SYNC_HTTP_CLIENT) {
            mHttpClient = new SyncHttpClient();
        } else {
            throw new RuntimeException("HttpClient type was wrong!");
        }
        mHttpClient.setTimeout(DEFAULT_TIME_OUT);
        mHttpClient.setMaxRetriesAndTimeout(DEFAULT_RETRY_COUNT, DEFAULT_RETRY_TIME_OUT);
    }

}
