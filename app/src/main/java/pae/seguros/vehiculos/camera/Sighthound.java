package pae.seguros.vehiculos.camera;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Created by Gerard on 04/01/2018.
 */

public class Sighthound extends android.support.v4.app.Fragment{
/*    Button butInformation, butPhoto;
    ImageView image;
    private Uri mImageUri;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    static final int CAM_REQUEST = 1;

    String matricula = null;
    String marca1 =null;
    String model1=null;
    String marca2 =null;
    String model2=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_sighthound, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        butInformation = (Button) getView().findViewById(R.id.btninfo);
        butPhoto = (Button) getView().findViewById(R.id.btnphoto);
        image = (ImageView) getView().findViewById(R.id.imageView);
        butPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAM_REQUEST);
                }
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //verificamos los permisos de escritura en storage
                verifyStoragePermissions(getActivity());
                //Li pasem la localitzacio del fitxer al intent
                File file = getfile();
                mImageUri = Uri.fromFile(file);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(camera_intent, CAM_REQUEST);
            }
        });

        final TextView txtmat, txtmodel1, txtmodel2, txtmarca1, txtmarca2;
        txtmat = (TextView) getView().findViewById(R.id.matricula);
        txtmodel1 = (TextView) getView().findViewById(R.id.model1);
        txtmarca1 = (TextView) getView().findViewById(R.id.maker1);
        txtmodel2 = (TextView) getView().findViewById(R.id.model2);
        txtmarca2 = (TextView) getView().findViewById(R.id.maker2);

        //vector on guardarem els resultats de consultar la info de la foto
        final ArrayList<String> resultat = new ArrayList<>();
        butInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //API Sighthound
                try {
                    new NetworkAsyncTask().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                //Part on enseño el resultat en forma de dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sighthound Results");
                builder.setMessage("Licenseplate: " + matricula + "\n" + "Make1: " + marca1 + "\n" + "Model1: " + model1 + "\n" + "Make2: " + marca2 + "\n" + "Model2: " + model2);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                //Afegeixo el resultat de la consulta a la API en el array res
                resultat.add(matricula);
                resultat.add(model1);
                resultat.add(marca1);
                resultat.add(model2);
                resultat.add(marca2);

                txtmat.setText(resultat.get(1));
                txtmodel1.setText(resultat.get(2));
            }
        });

    }


    public static void verifyStoragePermissions(Activity activity) {
        // comprobamos si tenemos permiso de escritura
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            //no tenemos permisos, entonces se lo damos al usuario
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    private File getfile() {
        File folder = new File(Environment.getExternalStorageDirectory(), "gft_camera");
        //Comprovar si existeix la carpeta, sino crearla.
        if (!folder.exists()) folder.mkdir();
        File image_file = new File(folder, "images.jpg");
        return image_file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //String path = mImageUri.getPath();
        //image.setImageDrawable(Drawable.createFromPath(path));
        Log.d(TAG, mImageUri.getPath());
        Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getPath());
        image.setImageBitmap(bitmap);
    }

    private class NetworkAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            String api = "https://dev.sighthoundapi.com/v1/recognition?objectType=vehicle,licenseplate";
            String accessToken = "OAFMge5GN40wlLsyHvxpUxVkLzytbCmvNbKJ";

            ArrayList<String> res = new ArrayList<>();

            try {
                File file = getfile();
                FileInputStream fis = new FileInputStream(file);
                Bitmap bm = BitmapFactory.decodeStream(fis);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
                byte[] bytes = baos.toByteArray();
                //String test = Base64.encodeToString(bytes, Base64.DEFAULT);


                // JSONObject jsonImage = new JSONObject(test);
                URL apiURL = new URL(api);
                HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
                connection.setRequestProperty("Content-Type", "application/octet-stream");
                connection.setRequestProperty("X-Access-Token", accessToken);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                byte[] body = bytes;
                connection.setFixedLengthStreamingMode(body.length);
                OutputStream os = connection.getOutputStream();
                os.write(body);
                os.flush();
                int httpCode = connection.getResponseCode();

                if (httpCode == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    JSONObject result = new JSONObject(String.valueOf(stringBuilder));
                    System.out.println(stringBuilder);
                    Iterator<String> iter = result.keys();

                    while (iter.hasNext()) {
                        String key = iter.next();

                        try {
                            if (key.equals("objects")) {

                                JSONArray array = result.getJSONArray("objects");
                                //First car
                                if(array.getJSONObject(0).has("vehicleAnnotation")) {
                                    //Mirem que a la resposta JSON estigui la matricula
                                    if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").has("licenseplate")){
                                        if(array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("licenseplate").has("attributes")){
                                            if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("licenseplate").getJSONObject("attributes").has("system")){
                                                if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("licenseplate").getJSONObject("attributes").getJSONObject("system").has("string")){
                                                    if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("licenseplate").getJSONObject("attributes").getJSONObject("system").getJSONObject("string").has("name")){
                                                        matricula = array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("licenseplate")
                                                                .getJSONObject("attributes").getJSONObject("system").getJSONObject("string").getString("name");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").has("attributes")) {
                                        if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").has("system")) {
                                            //Mirem que a la resposta JSON estigui la marca
                                            if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").has("make")) {
                                                if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("make").has("name")) {
                                                    marca1 = array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("make").getString("name");
                                                }
                                            }
                                            //Mirem que a la resposta JSON estigui el model
                                            if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").has("model")) {
                                                if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("model").has("name")) {
                                                    model1 = array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("model").getString("name");
                                                }
                                            }
                                        }
                                    }
                                }
                                //Second car
                                if(array.getJSONObject(1).has("vehicleAnnotation")) {
                                    //Mirem que a la resposta JSON estigui la matricula
                                    if (array.getJSONObject(1).getJSONObject("vehicleAnnotation").has("attributes")){
                                        if (array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").has("system")){
                                            //Mirem que a la resposta JSON estigui la marca
                                            if (array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").has("make")) {
                                                if (array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("make").has("name")) {
                                                    marca2 = array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("make").getString("name");
                                                }
                                            }
                                            //Mirem que a la resposta JSON estigui el model
                                            if (array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").has("model")) {
                                                if (array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("model").has("name")) {
                                                    model2 = array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("model").getString("name");
                                                }
                                            }
                                        }
                                    }
                                }
                                //System.out.println("Info Results: "+array.getJSONObject(0).getJSONObject("vehicleAnnotation"));
                                System.out.println("Resultats Sighthound: \n");
                                if (matricula==null) matricula = "Unknown";
                                //else System.out.println(matricula);
                                if (marca1==null) marca1="Unknown";
                                //else System.out.println(marca1);
                                if (model1==null) model1="Unknown";
                                //else System.out.println(model1);
                                if (marca2==null) marca2="Unknown";
                                //else System.out.println(marca2);
                                if (model2==null) model2="Unknown";
                                //else System.out.println(model2);

                                res.add(matricula);
                                res.add(marca1);
                                res.add(model1);
                                res.add(marca2);
                                res.add(model2);
                            }
                        } catch (JSONException e) {
                            System.out.println("Resultats Sighthound: \n");
                            //Matricula
                            System.out.println("Unknown");
                            //Make1
                            System.out.println("Unknown");
                            //Model1
                            System.out.println("Unknown");
                            //Make2
                            System.out.println("Unknown");
                            //Model2
                            System.out.println("Unknown");
                            res.add("Unknown");
                            res.add("Unknown");
                            res.add("Unknown");
                            res.add("Unknown");
                            res.add("Unknown");
                        }
                    }
                }    else {
                    //tractar lexepcio amb error de conexio
                    System.out.println("ERROR!");
                }
            }catch (java.io.IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return res;
        }
    }
*/}
