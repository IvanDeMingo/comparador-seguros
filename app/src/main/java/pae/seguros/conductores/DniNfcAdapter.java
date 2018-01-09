package pae.seguros.conductores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

import de.tsenger.androsmex.mrtd.DG11;
import de.tsenger.androsmex.mrtd.DG1_Dnie;
import es.gob.jmulticard.jse.provider.DnieKeyStore;
import es.gob.jmulticard.jse.provider.DnieProvider;
import es.gob.jmulticard.jse.provider.MrtdKeyStoreImpl;
import pae.seguros.R;


public class DniNfcAdapter implements android.nfc.NfcAdapter.ReaderCallback {

    private Tag tagFromIntent;
    private String canNumber;
    private Activity mActivity;
    private View mView;
    private AlertDialog alertDialog;

    private DG1_Dnie dg1;
    private DG11 dg11;

    static private android.nfc.NfcAdapter nfcAdapter;

    public DniNfcAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        alertDialog = (new AlertDialog.Builder(mActivity)).create();

        nfcAdapter = android.nfc.NfcAdapter.getDefaultAdapter(mActivity);
        nfcAdapter.setNdefPushMessage(null, mActivity);
        nfcAdapter.setNdefPushMessageCallback(null, mActivity);

        enableReaderMode(1000);
    }

    @SuppressLint("StaticFieldLeak")
    public class ReadDNIeTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            alertDialog.setCancelable(false);
            //alertDialog.setMessage("Reading DNIe...");

            LayoutInflater layoutInflater = mActivity.getLayoutInflater();
            View dialog = layoutInflater.inflate(R.layout.progress_dialog, null);
            ((TextView) dialog.findViewById(R.id.progress_dialog_text))
                    .setText(R.string.reading_dnie);
            alertDialog.setView(dialog);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alertDialog.show();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //android.os.Debug.waitForDebugger();
            try {
                read();
            } catch (CertificateException | NoSuchAlgorithmException e) {
                showErrorDialog(mActivity.getString(R.string.error_dni_nfc));
            } catch (IOException e) {
                showErrorDialog(mActivity.getString(R.string.can_incorrecto));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (alertDialog.isShowing())
                alertDialog.dismiss();
            else
                showErrorDialog(mActivity.getString(R.string.dnie_no_detectado));
            showInfo();
            disableReaderMode();
        }
    }

    private void showErrorDialog(String message) {
        if (mView != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setTitle(R.string.error)
                    .setMessage(message)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    builder.create().show();
                }
            });
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        tagFromIntent = tag;

        ReadDNIeTask readDNIeTask = new ReadDNIeTask();
        readDNIeTask.execute();
    }

    public void setCanNumber(String canNumber) {
        this.canNumber = canNumber;
    }

    public void setView(View view) {
        mView = view;
    }

    private boolean enableReaderMode(int msDelay) {
        // Ponemos en msDelay milisegundos el tiempo de espera para comprobar presencia de lectores NFC
        Bundle options = new Bundle();
        options.putInt(android.nfc.NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, msDelay);
        nfcAdapter.enableReaderMode(mActivity,
                this,
                android.nfc.NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
                        | android.nfc.NfcAdapter.FLAG_READER_NFC_B,
                options);
        return true;
    }

    private boolean disableReaderMode() {
        // Desactivamos el modo reader de NFC
        nfcAdapter.disableReaderMode(mActivity);
        return true;
    }

    private void read() throws CertificateException, NoSuchAlgorithmException, IOException {
        Log.v("READ", "Reading DNI...");
        // Activamos el modo rápido para agilizar la carga.
        System.setProperty("es.gob.jmulticard.fastmode", "true");

        // Se instancia el proveedor y se añade
        final DnieProvider p = new DnieProvider();
        p.setProviderTag(tagFromIntent);
        p.setProviderCan(canNumber);
        Security.insertProviderAt(p, 1);

        // Cargamos certificados y keyReferences
        KeyStoreSpi ksSpi = new MrtdKeyStoreImpl();
        DnieKeyStore ksUserDNIe = new DnieKeyStore(ksSpi, p, "MRTD");

        ksUserDNIe.load(null, null);

        dg1 = ksUserDNIe.getDatagroup1();
        dg11 = ksUserDNIe.getDatagroup11();
    }

    private void showInfo() {
        if (mView != null && dg1 != null && dg11 != null) {
            Intent resultIntent = new Intent(mView.getContext(),UserForm.class);
            resultIntent.putExtra("edad", dg1.getDateOfBirth());
            resultIntent.putExtra("sexo", dg1.getSex());
            resultIntent.putExtra("dni", dg11.getPersonalNumber().split("-"));
            mView.getContext().startActivity(resultIntent);
        }
    }
}
