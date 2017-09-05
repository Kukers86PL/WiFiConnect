package aprivate.wificonnect;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.net.wifi.WifiConfiguration;
import android.content.Context;

import java.util.List;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.HashMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class WiFiListActivity extends AppCompatActivity {

    private String m_SSID = "";
    private String m_PASS = "";
    private AdView mAdView;

    static final String LIST_TITLE = "Known WiFi's:";

    private String removeFirstAndLastChar(String text) {
        return text.substring(1, text.length() - 1);
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
        if (QR != null) {
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
    }

    private void getPass()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        input.setId(R.id.dialog_edit_text);
        input.setText(Cache.getPassFromCache(getApplicationContext(), m_SSID), TextView.BufferType.EDITABLE);

        alert.setView(input);
        alert.setTitle(m_SSID + " password required");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.dialog_edit_text);
                m_PASS = edit.getText().toString();

                Cache.saveToCache(getApplicationContext(), m_SSID, m_PASS);

                showQR(QRBuilderParser.buildQR(m_SSID, m_PASS));

                dialog.cancel();
                }
            });
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_list);

        final ListView listview = (ListView) findViewById(R.id.wifi_list);

        final ArrayList<String> list = new ArrayList<String>();
        list.add(LIST_TITLE);

        List<WifiConfiguration> configs = WiFi.getConfiguredNetworks(getApplicationContext());

        if (configs != null) {
            for (int i = 0; i < configs.size(); i++) {
                list.add(removeFirstAndLastChar(configs.get(i).SSID));
            }
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                m_SSID = (String) parent.getItemAtPosition(position);
                if (!m_SSID.equals(LIST_TITLE)) {
                    getPass();
                }
            }
        });

        MobileAds.initialize(this, "ca-app-pub-8210578461491658~5644639501");

        mAdView = (AdView) findViewById(R.id.adViewList);

        // Release version
        //mAdView.loadAd(new AdRequest.Builder().build());

        // Test version
        mAdView.loadAd(new AdRequest.Builder()
                .addTestDevice("3EDAFA2C4F46E267165CB11B3C4D32C0") //Kukers phone
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build());
    }

}
