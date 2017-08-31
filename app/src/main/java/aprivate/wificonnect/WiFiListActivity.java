package aprivate.wificonnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class WiFiListActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_list);

        Context context = getApplicationContext();
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configs = wifi.getConfiguredNetworks();

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
                    final String item = (String) parent.getItemAtPosition(position);
                }
            });
        }
    }

}
