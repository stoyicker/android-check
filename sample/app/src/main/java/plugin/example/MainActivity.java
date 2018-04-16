package plugin.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    // CR Code Review

    // TODO To Do

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final String processed = JavaLibrary.process(this);
        if (BuildConfig.DEBUG) {
            Log.d("MainActivity", "MainActivity::onCreate");
            Log.d("MainActivity.process", processed);
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    Object object = "";
                    Double value = (Double) object;
                } catch (Exception e) {

                }

                "".length();
            }
        }.start();
    }

}
