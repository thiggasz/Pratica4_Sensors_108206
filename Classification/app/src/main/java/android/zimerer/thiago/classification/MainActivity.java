package android.zimerer.thiago.classification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    boolean lightResponse = false, proximityResponse = false;
    float light, proximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent it = getIntent();

        light = it.getFloatExtra("light", -1);
        proximity = it.getFloatExtra("proximity", -1);

        Log.d("SensorDebug", "Proximity: " + proximity);
        Log.d("SensorDebug", "Light: " + light);
    }

    public void evaluateValues(View v) {
        if(light < 20.0) {
            lightResponse = true;
        }

        if(proximity > 3.0) {
            proximityResponse = true;
        }

        Intent it = new Intent("SENSOR_ACTION");
        Bundle results = new Bundle();

        results.putBoolean("lightResponse", lightResponse);
        results.putBoolean("proximityResponse", proximityResponse);
        it.putExtras(results);

        startActivity(it);
    }
}