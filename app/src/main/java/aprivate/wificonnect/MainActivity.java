package aprivate.wificonnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    boolean dupa = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickBtnDupa(View v)
    {
        Button dupa_button = (Button)findViewById(R.id.button);
        if (dupa) dupa_button.setText("DUPA! DUPA!"); else dupa_button.setText("NIE DUPA?!");
        dupa = !dupa;
    }
}
