package aprivate.wificonnect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Alek on 31.08.2017.
 */

public class QRBuilderParser {

    static final String INIT_STRING = "*WiFiConnect*0*";

    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 800;
    public final static int HEIGHT = 800;

    static private Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    static public Bitmap buildQR(String SSID, String pass) {
        try {
            return encodeAsBitmap(INIT_STRING + SSID + "*" + pass + "*");
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseSSID(String QR) {
        if (QR.length() >= INIT_STRING.length()) {
            String init = QR.substring(0, INIT_STRING.length());
            if (init.equals(INIT_STRING)) {
                if (QR.length() > INIT_STRING.length()) {
                    int index = QR.indexOf("*", INIT_STRING.length());
                    if (index != -1) {
                        return QR.substring(INIT_STRING.length(), index);
                    }
                }
            }
        }

        return "";
    }

    private String parsePass(String QR) {
        if (QR.length() >= INIT_STRING.length()) {
            String init = QR.substring(0, INIT_STRING.length());
            if (init.equals(INIT_STRING)) {
                if (QR.length() > INIT_STRING.length()) {
                    int index1 = QR.indexOf("*", INIT_STRING.length());
                    if ((index1 != -1) && (QR.length() - index1) > 0) {
                        int index2 = QR.indexOf("*", index1 + 1);
                        if (index2 != -1) {
                            return QR.substring(index1 + 1, index2);
                        }
                    }
                }
            }
        }

        return "";
    }

    static public void initiateQRScan(AppCompatActivity activity) {
        new IntentIntegrator(activity).initiateScan();
    }

    static public void onActivityResult(AppCompatActivity activity, int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if ((result != null) && (resultCode == RESULT_OK)) {
            String QR_text = result.getContents();
            QRBuilderParser builderParses = new QRBuilderParser();
            String SSID = builderParses.parseSSID(QR_text);
            String Pass = builderParses.parsePass(QR_text);
            if (!SSID.isEmpty()) {
                Cache.saveToCache(activity.getApplicationContext(), SSID, Pass);
                WiFi.connect(activity.getApplicationContext(), SSID, Pass);
            }
        }
    }
}
