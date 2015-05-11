package lza.com.lza.library.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

import lza.com.lza.library.util.AppLogger;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 2/23/15.
 */
public final class WifiAdmin {

    /**
     * 无网络连接
     */
    public static final int TYPE_NONE = -1;
    public static final String TYPE_NAME_NONE = "";

    /**
     * Wi-Fi连接状态
     */
    public static final int WIFI_CONNECTED = 0x01;
    public static final int WIFI_CONNECT_FAILED = 0x02;
    public static final int WIFI_CONNECTING = 0x013;

    private WifiManager mWifiManager;

    private WifiInfo mWifiInfo;

    private List<ScanResult> mWifiList;

    private List<WifiConfiguration> mWifiConfigurations;

    private WifiManager.WifiLock mWifiLock;

    private ConnectivityManager mConnManager;

    private WifiReceiver mWifiReceiver = new WifiReceiver();

    private Context mCtx;

    private static WifiAdmin sInstance = null;

    public static WifiAdmin getInstance(Context c) {
        if (sInstance == null) {
            sInstance = new WifiAdmin(c);
        }
        return sInstance;
    }

    private WifiAdmin(Context c) {
        mWifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        mConnManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
        mCtx = c;
    }

    /**
     * 先关闭热点再打开Wi-Fi
     *
     * @param listener
     */
    public void openWifi(OnWifiStateChange listener) {
        if (isWifiEnable()) return;
        setOnWifiStateChange(listener);
        registerWifiReceiver();
        mAction = ACTION_OPEN_WIFI;
        WifiApAdmin wifiApAdmin = WifiApAdmin.getInstance(mCtx);
        if (!wifiApAdmin.isWifiApEnable()) {
            openWifi();
        } else {
            wifiApAdmin.closeWifiAp(new WifiApAdmin.OnWifiApStateChange() {
                @Override
                public void handle(String action, int wifiApState) {
                    if (action.equals(WifiApAdmin.ACTION_CLOSE_WIFI_AP) &&
                            wifiApState == WifiApAdmin.WIFI_AP_STATE_DISABLED) {
                        openWifi();
                    }
                }
            });
        }
    }

    public boolean openWifi() {
        return mWifiManager.setWifiEnabled(true);
    }

    public boolean closeWifi(OnWifiStateChange listener) {
        if (!isWifiEnable()) return true;
        setOnWifiStateChange(listener);
        registerWifiReceiver();
        mAction = ACTION_CLOSE_WIFI;
        return closeWifi();
    }

    public boolean closeWifi() {
        return mWifiManager.setWifiEnabled(false);
    }

    public boolean isWifiEnable() {
        return mWifiManager.isWifiEnabled();
    }

    public int getWifiState() {
        return mWifiManager.getWifiState();
    }

    public String checkWifiState(int state) {
        if (state == WifiManager.WIFI_STATE_DISABLED) return "Wi-Fi已关闭";
        else if (state == WifiManager.WIFI_STATE_DISABLING) return "Wi-Fi正在关闭";
        else if (state == WifiManager.WIFI_STATE_ENABLED) return "Wi-Fi已打开";
        else if (state == WifiManager.WIFI_STATE_ENABLING) return "Wi-Fi正在打开";
        else if (state == WifiManager.WIFI_STATE_UNKNOWN) return "Wi-Fi状态未知";
        else return "未知状态";
    }

    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    public void releaseWifiLock() {
        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }

    public void createWifiLock(String key) {
        mWifiLock = mWifiManager.createWifiLock(key);
    }

    public void createWifiLock() {
        createWifiLock("WIFI_ADMIN");
    }

    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfigurations;
    }

    public void connectionConfiguration(int index) {
        if (index > mWifiConfigurations.size()) {
            return;
        }
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
    }

    public boolean startScan() {
        boolean flag;
        flag = mWifiManager.startScan();
        //mWifiList = mWifiManager.getScanResults();
        //mWifiConfigurations = mWifiManager.getConfiguredNetworks();
        return flag;
    }

    public List<ScanResult> getPrevWifiList() {
        return mWifiList;
    }

    public List<ScanResult> getWifiList() {
        mWifiList = mWifiManager.getScanResults();
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
        return mWifiList;
    }

    public StringBuffer lookUpScan() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mWifiList.size(); i++) {
            sb.append("Index_")
                .append(String.valueOf(i + 1))
                .append(":");
            sb.append(mWifiList.get(i))
                .append("\n");
        }
        return sb;
    }

    public String getMacAddress() {
        return mWifiInfo == null ? "NULL" : mWifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return mWifiInfo == null ? "NULL" : mWifiInfo.getBSSID();
    }

    public int getIpAddress() {
        return mWifiInfo == null ? 0 : mWifiInfo.getIpAddress();
    }

    public int getNetworkId() {
        return mWifiInfo == null ? 0 : mWifiInfo.getNetworkId();
    }

    public NetworkInfo getWifiNetworkInfo() {
        return mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }

    public boolean isWifiConnected() {
        NetworkInfo networkInfo = getWifiNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public int getWifiConnectState() {
        NetworkInfo wifiNetworkInfo = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo == null) return WIFI_CONNECT_FAILED;
        if (wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.OBTAINING_IPADDR
                || wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTING
                || wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.SCANNING
                || wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.AUTHENTICATING) {
            return WIFI_CONNECTING;
        } else if (wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
            return WIFI_CONNECTED;
        } else {
            return WIFI_CONNECT_FAILED;
        }
    }

    public boolean isNetworkConnected() {
        NetworkInfo networkInfo = mConnManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public int getNetworkType() {
        NetworkInfo networkInfo = mConnManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.getType();
        } else {
            return TYPE_NONE;
        }
    }

    public String getNetworkTypeName() {
        NetworkInfo networkInfo = mConnManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.getTypeName();
        } else {
            return TYPE_NAME_NONE;
        }
    }

    public boolean addNetwork(WifiConfiguration configuration) {
        int wcgId = mWifiManager.addNetwork(configuration);
        return mWifiManager.enableNetwork(wcgId, true);
    }

    public boolean disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        return mWifiManager.disconnect();
    }

    public DhcpInfo getDhcpInfo() {
        return mWifiManager.getDhcpInfo();
    }

    private WifiConfiguration isExists(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration config : existingConfigs) {
                if (config.SSID.equals("\"" + SSID + "\"")) {
                    return config;
                }
            }
        }
        return null;
    }

    public static final int TYPE_NO_PASSWORD = 0x11;
    public static final int TYPE_WEP = 0x12;
    public static final int TYPE_WPA = 0x13;
    public WifiConfiguration createWifiInfo(String SSID, String password, int type) {
        AppLogger.e("SSID=" + SSID + "##Password=" + password + "##Type=" + type);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = isExists(SSID);
        if (tempConfig != null)
            mWifiManager.removeNetwork(tempConfig.networkId);

        if (type == TYPE_NO_PASSWORD) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == TYPE_WEP) {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == TYPE_WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    public boolean addNetWork(String ssid, String password, int type) {
        return addNetwork(createWifiInfo(ssid, password, type));
    }

    /**
     * 用于检查Wi-Fi状态的变更
     */
    private class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                AppLogger.e("监听到Wi-Fi状态变更，当前状态：" + checkWifiState(getWifiState()));
                if (mOnWifiStateChange != null) {
                    mOnWifiStateChange.handle(mAction, getWifiState());
                }
                if (getWifiState() == WifiManager.WIFI_STATE_ENABLED &&
                        mAction.equals(ACTION_OPEN_WIFI)) {
                    unregisterWifiReceiver();
                } else if (getWifiState() == WifiManager.WIFI_STATE_DISABLED &&
                        mAction.equals(ACTION_CLOSE_WIFI)) {
                    unregisterWifiReceiver();
                }
            }
        }
    }

    public String mAction = "";
    public static final String ACTION_OPEN_WIFI = "action_open_wifi";
    public static final String ACTION_CLOSE_WIFI = "action_close_wifi";

    public interface OnWifiStateChange {
        void handle(String action, int wifiState);
    }

    private OnWifiStateChange mOnWifiStateChange;

    public void setOnWifiStateChange(OnWifiStateChange onWifiStateChange) {
        this.mOnWifiStateChange = onWifiStateChange;
    }

    private void registerWifiReceiver() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            mCtx.registerReceiver(mWifiReceiver, filter);
        } catch (Exception ex) {

        }
    }

    private void unregisterWifiReceiver() {
        try {
            mCtx.unregisterReceiver(mWifiReceiver);
        } catch (Exception ex) {

        }
    }
}
