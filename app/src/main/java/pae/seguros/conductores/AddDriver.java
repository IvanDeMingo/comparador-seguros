package pae.seguros.conductores;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import pae.seguros.R;

public class AddDriver extends AppCompatActivity {

    private static final int OCR_ACTIVITY_CODE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_driver);
    }

    public void clickOCR(View view) {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { android.Manifest.permission.CAMERA },
                    0);
        } else {
            Intent intent = new Intent(this, DniOcrMain.class);
            startActivityForResult(intent, OCR_ACTIVITY_CODE);
        }
    }

    public void clickNFC(final View view) {

        if (!checkNFCAvailable()) return;
        readDNIe(view);
    }

    private void readDNIe(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final Activity activity = this;

        LayoutInflater layoutInflater = getLayoutInflater();
        final View dialog = layoutInflater.inflate(R.layout.nfc_dialog, null);

        builder.setView(dialog)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder can = new StringBuilder(
                                ((EditText) dialog.findViewById(R.id.edit_text_can_nfc))
                                        .getText().toString());
                        while (can.length() < 6)
                            can.insert(0, '0');

                        DniNfcAdapter nfcAdapter = new DniNfcAdapter(activity);
                        nfcAdapter.setCanNumber(can.toString());
                        nfcAdapter.setView(view);
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setTitle(R.string.nfc_dialog_title)
                .create().show();
    }

    private boolean checkNFCAvailable() {
        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = null;
        if (manager != null) {
            adapter = manager.getDefaultAdapter();
        }
        if (adapter != null) {
            if (!adapter.isEnabled()) {
                showErrorDialog(getString(R.string.nfc_no_activado));
                return false;
            }
        } else {
            showErrorDialog(getString(R.string.sin_nfc));
            return false;
        }
        return true;
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
}
