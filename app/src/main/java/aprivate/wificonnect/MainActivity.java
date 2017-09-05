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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import android.net.Uri;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-8210578461491658~5644639501");

        mAdView = (AdView) findViewById(R.id.adViewMain);

        // Release version
        mAdView.loadAd(new AdRequest.Builder().build());

        // Test version
        //mAdView.loadAd(new AdRequest.Builder()
        //        .addTestDevice("3EDAFA2C4F46E267165CB11B3C4D32C0") //Kukers phone
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        //        .build());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8210578461491658/2179574194");

        // Release vversion
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        // Test version
        //mInterstitialAd.loadAd(new AdRequest.Builder()
        //        .addTestDevice("3EDAFA2C4F46E267165CB11B3C4D32C0") //Kukers phone
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        //        .build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
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

    public void onClickBtnPro(View v)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=aprivate.wificonnectpro"));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        QRBuilderParser.onActivityResult(this, requestCode, resultCode, data);
    }
}
