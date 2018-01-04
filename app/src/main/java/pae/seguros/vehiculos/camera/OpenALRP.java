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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import static android.content.ContentValues.TAG;

/**
 * Created by Gerard on 04/01/2018.
 */

public class OpenALRP extends android.support.v4.app.Fragment {
/*
    Button butInformation,butPhoto;
    ImageView image;
    private Uri mImageUri;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    static final int CAM_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_openalpr,container,false);
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
                if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)
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
                startActivityForResult(camera_intent,CAM_REQUEST);
            }
        });
        butInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //API Openalpr url

                URL url = null;
                HttpURLConnection urlconnection = null;

                try {
                    url = new URL("https://api.openalpr.com/v2/recognize?");

                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }

                try {
                    urlconnection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AsyncHttpClient client = new AsyncHttpClient();

                RequestParams params = new RequestParams();
                try {
                    params.put("image", getfile());
                } catch (java.io.FileNotFoundException e) {
                    e.printStackTrace();
                }
                params.put("secret_key", "sk_e5468a7f484c80efe9599482");
                params.put("recognize_vehicle",1);
                params.put("country", "eu");
                params.put("return_image",0);
                params.put("topn",2);



                client.post("https://api.openalpr.com/v2/recognize?", params, new JsonHttpResponseHandler() {

                    String matricula = null;
                    String marca1 =null;
                    String model1=null;
                    String marca2 = null;
                    String model2 = null;

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        System.out.println("ERROR Openalpr");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonobj) {

                        Iterator<String> iter = jsonobj.keys();
                        System.out.println(jsonobj);
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {
                                if (key.equals("results")) {
                                    JSONArray array = jsonobj.getJSONArray("results");
                                    for (int i = 0; i < array.length(); i++) {
                                        //System.out.println(array.getJSONObject(i).getString("plate"));
                                        if (array.getJSONObject(i).has("plate")) {
                                            matricula = array.getJSONObject(i).getString("plate");
                                            if(array.getJSONObject(i).has("vehicle")){
                                                if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(0).has("name")) {
                                                    marca1 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(0).getString("name");
                                                }
                                                if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(0).has("name")){
                                                    model1 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(0).getString("name");
                                                }
                                                if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(1).has("name")){
                                                    marca2 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(1).getString("name");
                                                }
                                                if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(1).has("name")){
                                                    model2 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(1).getString("name");
                                                }
                                            }
                                        }
                                    }
                                    System.out.println("Resultats OpenALPR: \n");
                                    if (matricula==null) System.out.println("Unknown");
                                    System.out.println(matricula);
                                    if (marca1==null) System.out.println("Unknown");
                                    System.out.println(marca1);
                                    if (model1==null) System.out.println("Unknown");
                                    System.out.println(model1);
                                    if (marca2==null) System.out.println("Unknown");
                                    System.out.println(marca2);
                                    if (model2==null) System.out.println("Unknown");
                                    System.out.println(model2);


                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("OpenALPR Results");
                                    builder.setMessage("Licenseplate: " + matricula + "\n" + "Make1: " + marca1 + "\n" + "Model1: " + model1 + "\n" + "Make2: " + marca2 + "\n" + "Model2: " + model2);
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                }
                            } catch (JSONException e) {
                                System.out.println("Resultats OpenALPR: \n");
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
                            }
                        }
                    }
                });
            }
        });
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
        //String path = mImageUri.getPath();
        //image.setImageDrawable(Drawable.createFromPath(path));
        Log.d(TAG, mImageUri.getPath());
        Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getPath());
        image.setImageBitmap(bitmap);
    }


*/}
