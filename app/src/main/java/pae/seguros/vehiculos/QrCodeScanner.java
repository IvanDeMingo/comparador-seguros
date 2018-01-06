package pae.seguros.vehiculos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QrCodeScanner extends Activity implements ZXingScannerView.ResultHandler {

    public static final int SCAN_OK = 0;
    public static final int SCAN_ERROR = 1;
    public static final int BACK = 2;

    private static final String QR_RESULT = "qr_result";

    private static final String LICENSE_YEAR         = "license_year";
    private static final String LICENSE_NUMBER       = "license_number";
    private static final String MAKER                = "maker";
    private static final String MODEL                = "model";
    private static final String GAS                  = "gas";
    private static final String CATEGORY             = "category";
    private static final String POWER                = "power";
    private static final String EMISSIONS_LVL_EUR    = "emissions_lvl_eur";
    private static final String AUTONOMY             = "autonomy";

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this); // Programmatically initialize the scanner view
        setContentView(mScannerView); // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera(); // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera(); // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);

        Log.v("TEXT", rawResult.getText());

        Intent resultIntent = new Intent();

        Bundle resultBundle = parseInfo(rawResult.getText());
        int resultCode = resultBundle.isEmpty() ?
                SCAN_ERROR : SCAN_OK;

        resultIntent.putExtra(QR_RESULT, resultBundle);
        setResult(resultCode, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(BACK, new Intent());
        finish();
    }

    private Bundle parseInfo(String text) {
        Bundle bundle = new Bundle();

        text += ' '; // For some QR codes, the last value is empty (autonomy)
        String[] info = text.split("/");

        if (info.length == 9) {
            bundle.putString(LICENSE_YEAR, info[0].trim());
            bundle.putString(LICENSE_NUMBER, info[1].trim());
            bundle.putString(MAKER, info[2].trim());
            bundle.putString(MODEL, info[3].trim());
            bundle.putString(GAS, info[4].trim());
            bundle.putString(EMISSIONS_LVL_EUR, info[5].trim());
            bundle.putString(POWER, info[6].trim());
            bundle.putString(CATEGORY, info[7].trim());
            bundle.putString(AUTONOMY, info[8].trim());
        }

        return bundle;
    }
}
