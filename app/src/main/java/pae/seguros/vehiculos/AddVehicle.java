package pae.seguros.vehiculos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import pae.seguros.R;


public class AddVehicle extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int QR_SCANNER = 0;
    private static final int FORM = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);
    }

    public void clickPhoto(View view) {
        Snackbar.make(view, "Foto del coche", Snackbar.LENGTH_SHORT).show();
    }

    public void clickQR(View view) {
        // Request camera permission if it's not already granted
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { android.Manifest.permission.CAMERA },
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            // Start QR scan activity
            Intent intent = new Intent(this, QrCodeScanner.class);
            startActivityForResult(intent, QR_SCANNER);
        }
    }

    public void clickManual(View view) {
        Intent intent = new Intent(this, VehiculosFormNoSpinner.class);
        startActivityForResult(intent, FORM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            finish();
        if (requestCode == QR_SCANNER && resultCode != QrCodeScanner.BACK) {
            Log.v("QR", "Successful scan");
        }
    }
}
