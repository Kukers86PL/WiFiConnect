package aprivate.wificonnect;

/**
 * Created by Alek on 31.08.2017.
 */

public class QRBuilderParser {

    final String init_string = "*WiFiConnect*0*";

    public String buildQR(String SSID, String pass) {
        SSID.substring(1, SSID.length() - 1);
        return init_string + SSID + "*" + pass + "*";
    }

    public String parseSSID(String QR) {
        if (QR.length() >= init_string.length()) {
            String init = QR.substring(0, init_string.length());
            if (init.equals(init_string)) {
                if (QR.length() > init_string.length()) {
                    int index = QR.indexOf("*", init_string.length());
                    return QR.substring(init_string.length(), index);
                }
            }
        }

        return "";
    }

    public String parsePass(String QR) {
        if (QR.length() >= init_string.length()) {
            String init = QR.substring(0, init_string.length());
            if (init.equals(init_string)) {
                if (QR.length() > init_string.length()) {
                    int index1 = QR.indexOf("*", init_string.length());
                    if ((QR.length() - index1) > 0) {
                        int index2 = QR.indexOf("*", index1 + 1);
                        return QR.substring(index1 + 1, index2);
                    }
                }
            }
        }

        return "";
    }
}
