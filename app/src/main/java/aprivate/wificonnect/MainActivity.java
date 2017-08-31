package aprivate.wificonnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickBtnGenerate(View v)
    {
        Button generate_button = (Button)findViewById(R.id.buttonGenerate);
        generate_button.setText("Generate QR Clicked");
    }

    public void onClickBtnScan(View v)
    {
        Button scan_button = (Button)findViewById(R.id.buttonScan);
        scan_button.setText("Scan QR Clicked");
    }
}
