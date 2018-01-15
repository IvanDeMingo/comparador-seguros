package pae.seguros.vehiculos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;

import pae.seguros.R;
import pae.seguros.vehiculos.camera.OpenALRP;

import static pae.seguros.vehiculos.VehiculosFragment.CAM_REQUEST;


public class AddVehicle extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    static final int CAM_REQUEST = 1;
    private static final int FORM = 2;
    private static final int QR_SCANNER = 3;
    private Uri mImageUri;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);
    }

    public void clickPhoto(View view) {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAM_REQUEST);
            }
        }
        //verificamos los permisos de escritura en storage
        else if (permission != PackageManager.PERMISSION_GRANTED) {
            //no tenemos permisos, entonces se lo damos al usuario
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
        else {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Li pasem la localitzacio del fitxer al intent
            File file = getfile();
            mImageUri = FileProvider.getUriForFile(this, pae.seguros.BuildConfig.APPLICATION_ID + ".provider",file);
            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(camera_intent, CAM_REQUEST);
        }

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

    public void clickInfoPhoto(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(AddVehicle.this).create();
        alertDialog.setTitle("Vehicle recognition");
        alertDialog.setMessage("This option analyzes images of vehicles and responds with license plate data, as well as vehicle make and model type.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void clickInfoQR(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final Activity activity = AddVehicle.this;

        LayoutInflater layoutInflater = getLayoutInflater();
        final View dialog = layoutInflater.inflate(R.layout.dialog_info_qr, null);

        builder.setView(dialog)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setTitle("DGT QR sustainability sticker")
                .create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAM_REQUEST) {
                //setFragment(new OpenALRP());
                Intent myIntent = new Intent(AddVehicle.this, OpenALRP.class);
                startActivity(myIntent);
            }
            else finish();
        }
        if (requestCode == QR_SCANNER && resultCode != QrCodeScanner.BACK) {
            Log.v("QR", "Successful scan");
        }


    }

    private File getfile(){
        File folder = new File(Environment.getExternalStorageDirectory(),"gft_camera");
        //Comprovar si existeix la carpeta, sino crearla.
        if(!folder.exists()) folder.mkdir();
        File image_file =new File(folder,"images.jpg");
        return image_file;
    }

}
