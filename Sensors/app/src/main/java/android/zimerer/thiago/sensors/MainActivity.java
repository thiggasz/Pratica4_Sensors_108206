package android.zimerer.thiago.sensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorProx, sensorLight;
    private float proximityValue = -1, lightValue = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorProx = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (sensorProx != null)
            sensorManager.registerListener(this, sensorProx, SensorManager.SENSOR_DELAY_NORMAL);
        if (sensorLight != null)
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);

        Intent it = getIntent();
        if (it != null) {
            changeState(it);
        }
    }

    public void classifySensors(View v) {
        Intent it = new Intent("SENSOR_EVALUATION");
        Bundle values = new Bundle();
        values.putFloat("proximity", proximityValue);
        values.putFloat("light", lightValue);
        it.putExtras(values);

        startActivity(it);
    }

    public void changeState(Intent it) {
        LanternaHelper lh = new LanternaHelper(this);
        MotorHelper mh = new MotorHelper(this);

        Switch lanterSwitch = findViewById(R.id.lanterSwitch);
        Switch vibrationSwitch = findViewById(R.id.vibrationSwitch);

        boolean lightResponse = it.getBooleanExtra("lightResponse", false);
        boolean proximityResponse = it.getBooleanExtra("proximityResponse", false);

        Log.d("SensorDebug", "Proximity: " + lightResponse);
        Log.d("SensorDebug", "Light: " + proximityResponse);

        if (lightResponse) {
            lh.ligar();
            lanterSwitch.setChecked(true);
        } else {
            lh.desligar();
            lanterSwitch.setChecked(false);
        }

        if (proximityResponse) {
            mh.iniciarVibracao();
            vibrationSwitch.setChecked(true);
        } else {
            mh.pararVibracao();
            vibrationSwitch.setChecked(false);
        }
    }

    @Override
    public void onSensorChanged(android.hardware.SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximityValue = event.values[0];
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightValue = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}
