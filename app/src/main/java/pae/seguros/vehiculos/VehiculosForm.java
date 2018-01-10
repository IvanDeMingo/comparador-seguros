package pae.seguros.vehiculos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;

import pae.seguros.R;
import pae.seguros.databases.AppDatabase;
import pae.seguros.vehiculos.camera.Camera;
import pae.seguros.vehiculos.camera.OpenALRP;

import static pae.seguros.vehiculos.QrCodeScanner.AUTONOMY;
import static pae.seguros.vehiculos.QrCodeScanner.CATEGORY;
import static pae.seguros.vehiculos.QrCodeScanner.EMISSIONS_LVL_EUR;
import static pae.seguros.vehiculos.QrCodeScanner.GAS;
import static pae.seguros.vehiculos.QrCodeScanner.LICENSE_NUMBER;
import static pae.seguros.vehiculos.QrCodeScanner.LICENSE_YEAR;
import static pae.seguros.vehiculos.QrCodeScanner.MAKER;
import static pae.seguros.vehiculos.QrCodeScanner.MODEL;
import static pae.seguros.vehiculos.QrCodeScanner.POWER;
import static pae.seguros.vehiculos.QrCodeScanner.QR_RESULT;

/**
 * Created by Gerard on 04/01/2018.
 */

public class VehiculosForm extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextMatricula, editTextModel1, editTextModel2,editTextMarca1,editTextMarca2;
    private Spinner spinnerModel, spinnerMarca;
    private Button submit;
    private Uri mImageUri;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    static final int CAM_REQUEST = 1;

    private static final int CAMERA = 0;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_form);

        editTextMatricula = (EditText) findViewById(R.id.editTextMatricula);
        spinnerMarca = (Spinner) findViewById(R.id.spinnerMarca);
        spinnerModel = (Spinner) findViewById(R.id.spinnerModel);
        submit = (Button) findViewById(R.id.buttonSubmit);


        Bundle parameters = getIntent().getExtras();

        if(parameters!=null){
            if (parameters.getString("omatricula").equals("Unknown") && parameters.getString("omodel1").equals("Unknown")
            && parameters.getString("omarca1").equals("Unknown") && parameters.getString("omodel2").equals("Unknown")
            && parameters.getString("omarca2").equals("Unknown") && parameters.getString("smatricula").equals("Unknown")
            && parameters.getString("smodel1").equals("Unknown") && parameters.getString("smarca1").equals("Unknown")
            && parameters.getString("smodel2").equals("Unknown") && parameters.getString("smarca2").equals("Unknown")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please notice that no car was detected in the photo")
                .setPositiveButton("OK", null)
                .setNegativeButton("Retry Photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAM_REQUEST);
                            }
                        }
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //verificamos los permisos de escritura en storage
                        verifyStoragePermissions(VehiculosForm.this);
                        //Li pasem la localitzacio del fitxer al intent
                        File file = getfile();
                        mImageUri = FileProvider.getUriForFile(context,
                                BuildConfig.APPLICATION_ID + ".provider",
                                file);
                        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        startActivityForResult(camera_intent,CAM_REQUEST);

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
            else {
                //Mirem tamany matricula si no es correcte posem sighthound
                if (parameters.getString("omatricula").length()< 7 && !parameters.getString("smatricula").equals("Unknown")) editTextMatricula.setText(parameters.getString("smatricula"));
                else if (!parameters.getString("omatricula").equals("Unknown"))editTextMatricula.setText(parameters.getString("omatricula"));

                //Ordenem per confiança
                ArrayList<String> listmodel = new ArrayList<>();
                if (!parameters.getString("oconfmodel1").equals("Unknown")) {
                    if (!parameters.getString("sconfmodel1").equals("Unknown")) {
                        if (Float.parseFloat(parameters.getString("oconfmodel1")) > Float.parseFloat(parameters.getString("sconfmodel1"))){
                            listmodel.add(parameters.getString("omodel1"));
                            listmodel.add(parameters.getString("smodel1"));
                        }
                        else {
                            listmodel.add(parameters.getString("smodel1"));
                            listmodel.add(parameters.getString("omodel1"));
                        }
                    }
                    //si sighthound no te model1
                    else  listmodel.add(parameters.getString("omodel1"));
                }
                //si openalpr no te model pero sighthound si
                else {
                    if (!parameters.getString("sconfmodel1").equals("Unknown"))
                        listmodel.add(parameters.getString("smodel1"));
                }

                if (!parameters.getString("oconfmodel2").equals("Unknown")) {
                    if (!parameters.getString("sconfmodel2").equals("Unknown")) {
                        if (Float.parseFloat(parameters.getString("oconfmodel2")) > Float.parseFloat(parameters.getString("sconfmodel2"))) {
                            listmodel.add(parameters.getString("omodel2"));
                            listmodel.add(parameters.getString("smodel2"));
                        } else {
                            listmodel.add(parameters.getString("smodel2"));
                            listmodel.add(parameters.getString("omodel2"));
                        }
                    }
                    //si sighthound no te model1
                    else  listmodel.add(parameters.getString("omodel2"));
                }
                //si openalpr no te model pero sighthound si
                else {
                    if (!parameters.getString("sconfmodel2").equals("Unknown"))
                        listmodel.add(parameters.getString("smodel2"));
                }

                ArrayAdapter<String> adaptermodel = new ArrayAdapter<String>(this,
                        R.layout.support_simple_spinner_dropdown_item, listmodel);
                spinnerModel.setAdapter(adaptermodel);

                ArrayList<String> listmarca = new ArrayList<>();

                if (!parameters.getString("oconfmarca1").equals("Unknown")) {
                    if (!parameters.getString("sconfmarca1").equals("Unknown")) {
                        if (Float.parseFloat(parameters.getString("oconfmarca1")) > Float.parseFloat(parameters.getString("sconfmarca1"))) {
                            listmarca.add(parameters.getString("omarca1"));
                            listmarca.add(parameters.getString("smarca1"));
                        } else {
                            listmarca.add(parameters.getString("smarca1"));
                            listmarca.add(parameters.getString("omarca1"));
                        }
                    }
                    else  listmarca.add(parameters.getString("omarca1"));
                }
                else {
                    if (!parameters.getString("sconfmarca1").equals("Unknown"))
                        listmarca.add(parameters.getString("smarca1"));
                }

                if (!parameters.getString("oconfmarca2").equals("Unknown")) {
                    if (!parameters.getString("sconfmarca2").equals("Unknown")) {
                        if (Float.parseFloat(parameters.getString("oconfmarca2")) > Float.parseFloat(parameters.getString("sconfmarca2"))) {
                            listmarca.add(parameters.getString("omarca2"));
                            listmarca.add(parameters.getString("smarca2"));
                        } else {
                            listmarca.add(parameters.getString("smarca2"));
                            listmarca.add(parameters.getString("omarca2"));
                        }
                    }
                    else  listmarca.add(parameters.getString("omarca2"));
                }
                else {
                    if (!parameters.getString("sconfmarca2").equals("Unknown"))
                        listmarca.add(parameters.getString("smarca2"));
                }


                ArrayAdapter<String> adaptermarca = new ArrayAdapter<String>(this,
                        R.layout.support_simple_spinner_dropdown_item, listmarca);
                spinnerMarca.setAdapter(adaptermarca);

                //Si cliquem el submit guardem el resultat a la BD
                submit.setOnClickListener(this);

            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            Bundle resultBundle = parseInfo();
            Intent resultIntent = new Intent(this,VehiculosFormNoSpinner.class);
            resultIntent.putExtra(String.valueOf(QR_RESULT), resultBundle);
            //Serveix per saber de lactivity don vens el CAM_REQUEST en aquest cas seria el VehiculosFormNoSpinner
            startActivityForResult(resultIntent, CAM_REQUEST);

        }
    }

    private Bundle parseInfo() {
        Bundle bundle = new Bundle();

        bundle.putString(LICENSE_YEAR, "");
        bundle.putString(LICENSE_NUMBER, editTextMatricula.getText().toString());
        bundle.putString(MAKER, spinnerMarca.getSelectedItem().toString());
        bundle.putString(MODEL, spinnerModel.getSelectedItem().toString());
        bundle.putString(GAS, "");
        bundle.putString(EMISSIONS_LVL_EUR, "");
        bundle.putString(POWER, "");
        bundle.putString(CATEGORY, "");
        bundle.putString(AUTONOMY, "");

        return bundle;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // comprobamos si tenemos permiso de escritura
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            //no tenemos permisos, entonces se lo damos al usuario
            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
    }

    private File getfile(){
        File folder = new File(Environment.getExternalStorageDirectory(),"gft_camera");
        //Comprovar si existeix la carpeta, sino crearla.
        if(!folder.exists()) folder.mkdir();
        File image_file =new File(folder,"images.jpg");
        return image_file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //setFragment(new OpenALRP());
            Intent myIntent = new Intent(VehiculosForm.this,OpenALRP.class);
            startActivity(myIntent);
        }
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_content, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_OK);
        finish();

    }
}
