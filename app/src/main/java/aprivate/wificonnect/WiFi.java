package aprivate.wificonnect;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by Alek on 03.09.2017.
 */

public class WiFi {

    static public void connect(Context context, String SSID, String Pass) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.disconnect();

        for (WifiConfiguration configs : wifi.getConfiguredNetworks()) {
            if (configs.SSID.equals("\"" + SSID + "\"")) {
                wifi.disableNetwork(configs.networkId);
                wifi.removeNetwork(configs.networkId);
                wifi.saveConfiguration();
                return;
            }
        }

        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + SSID + "\"";
        config.preSharedKey = "\"" + Pass + "\"";
        int netId = wifi.addNetwork(config);
        wifi.saveConfiguration();
        wifi.enableNetwork(netId, true);
        wifi.reconnect();
    }

    static public List<WifiConfiguration> getConfiguredNetworks(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.getConfiguredNetworks();
    }
}
