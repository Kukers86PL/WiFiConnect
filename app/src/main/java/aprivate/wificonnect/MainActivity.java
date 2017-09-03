package aprivate.wificonnect;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import com.google.zxing.Result;
import aprivate.wificonnect.QRBuilderParser;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
import aprivate.wificonnect.QRBuilderParser;
import android.content.Intent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import aprivate.wificonnect.Cache;
import aprivate.wificonnect.WiFi;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                //.addTestDevice("C04B1BFFB0774708339BC273F8A43708")
                .build();
        if ((mAdView != null) && (adRequest != null)) {
            mAdView.loadAd(adRequest);
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    public void onClickBtnGenerate(View v)
    {
        Intent intent = new Intent(this, WiFiListActivity.class);
        startActivity(intent);
    }

    public void onClickBtnScan(View v)
    {
        QRBuilderParser.initiateQRScan(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        QRBuilderParser.onActivityResult(this, requestCode, resultCode, data);
    }
}
