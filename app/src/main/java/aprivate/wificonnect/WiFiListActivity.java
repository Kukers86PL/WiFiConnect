package aprivate.wificonnect;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.content.Context;
import android.app.Activity;
import java.util.List;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.HashMap;
import android.widget.AdapterView;
import aprivate.wificonnect.QRBuilderParser;
import android.widget.EditText;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.content.DialogInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;

public class WiFiListActivity extends AppCompatActivity {

    private List<WifiConfiguration> configs;
    private String SSID = "";
    private String Pass = "";

    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 800;
    public final static int HEIGHT = 800;

    private Bitmap encodeAsBitmap(String str) throws WriterException {
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

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    private void showQR(Bitmap QR)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final ImageView input = new ImageView(this);

        input.setImageBitmap(QR);

        alert.setView(input);
        alert.setTitle("QR Code");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private void getPass()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        input.setId(R.id.dialog_edit_text);

        alert.setView(input);
        alert.setTitle("Password Required");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.dialog_edit_text);
                Pass = edit.getText().toString();

                QRBuilderParser builderParser = new QRBuilderParser();
                String QR_string = builderParser.buildQR(SSID, Pass);

                try {
                    showQR(encodeAsBitmap(QR_string));
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                dialog.cancel();
                }
            });
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_list);

        Context context = getApplicationContext();
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        configs = wifi.getConfiguredNetworks();

        if (configs != null) {

            final ListView listview = (ListView) findViewById(R.id.wifi_list);
            String[] values = new String[]{"Known WiFi's"};

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < values.length; ++i) {
                list.add(values[i]);
            }
            for (int i = 0; i < configs.size(); i++) {
                list.add(configs.get(i).SSID);
            }
            final StableArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    if (configs != null) {
                        final String item = (String) parent.getItemAtPosition(position);

                        for (int i = 0; i < configs.size(); i++) {
                            if (configs.get(i).SSID == item) {
                                SSID = item;
                                getPass();
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

}
