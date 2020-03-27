package com.example.testapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;

import com.mercadolibre.android.device.sdk.DeviceSDK;

public class DeviceDummy extends AppCompatActivity {

    TextView tvDescription;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        tvDescription = findViewById(R.id.tv_process_description);
        initAdvertisingId();
    }

    private void initAdvertisingId() {
        final Button btnTestAdvertisingId = findViewById(R.id.btn_test_device);

        final View.OnClickListener onClickListener = view -> {
            Toast.makeText(DeviceDummy.this, "Process iniciated, wait for results...", Toast.LENGTH_LONG).show();
            Map deviceMap = DeviceSDK.getInstance().getInfoAsMap();
            tvDescription.setText(deviceMap.toString());
        };
        btnTestAdvertisingId.setOnClickListener(onClickListener);
    }
}
