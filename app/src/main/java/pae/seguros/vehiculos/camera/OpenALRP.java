package pae.seguros.vehiculos.camera;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import pae.seguros.R;
import pae.seguros.vehiculos.VehiculosForm;

/**
 * Created by Gerard on 04/01/2018.
 */

public class OpenALRP extends android.support.v4.app.Fragment {

    private ArrayList<String> OpenALRPRes = new ArrayList<>();
    private ArrayList<String> SighthoundRes = new ArrayList<>();
    String Omatricula = null, Smatricula = null, Oconfmatricula = null, Sconfmatricula = null;
    String Omarca1 = null, Smarca1= null, Oconfmarca1 = null, Sconfmarca1 = null;
    String Omodel1 = null, Smodel1= null, Oconfmodel1 = null, Sconfmodel1 = null;
    String Omarca2 = null, Smarca2 = null, Oconfmarca2 = null, Sconfmarca2 = null;
    String Omodel2 = null, Smodel2 = null, Oconfmodel2 = null, Sconfmodel2 = null;
    private boolean SighthoundFinished = false;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vehiculos_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Wait while loading...");
        dialog.setTitle("Loading");
        dialog.setProgressStyle(dialog.STYLE_SPINNER);
        dialog.show();

        //API Sighthound
        new NetworkAsyncTask().execute();

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


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("ERROR Openalpr");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonobj) {

                Iterator<String> iter = jsonobj.keys();
                //System.out.println("OpenALPR: "+ jsonobj);
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        if (key.equals("results")) {
                            JSONArray array = jsonobj.getJSONArray("results");
                            for (int i = 0; i < array.length(); i++) {
                                //System.out.println(array.getJSONObject(i).getString("plate"));
                                if (array.getJSONObject(i).has("plate")) {
                                    Omatricula = array.getJSONObject(i).getString("plate");
                                   // if(array.getJSONObject(i).has("confidence")) {
                                       // Oconfmatricula = array.getJSONObject(i).getString("confidence");
                                        if (array.getJSONObject(i).has("vehicle")) {
                                            if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(0).has("name")) {
                                                Omarca1 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(0).getString("name");
                                            }
                                            if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(0).has("confidence")) {
                                                Oconfmarca1 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(0).getString("confidence");
                                            }
                                            if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(0).has("name")) {
                                                Omodel1 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(0).getString("name");
                                            }
                                            if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(0).has("confidence")) {
                                                Oconfmodel1 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(0).getString("confidence");
                                            }
                                            if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(1).has("name")) {
                                                Omarca2 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(1).getString("name");
                                            }
                                            if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(1).has("confidence")) {
                                                Oconfmarca2 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make").getJSONObject(1).getString("confidence");
                                            }
                                            if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(1).has("name")) {
                                                Omodel2 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(1).getString("name");
                                            }
                                            if (array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(1).has("confidence")) {
                                                Oconfmodel2 = array.getJSONObject(i).getJSONObject("vehicle").getJSONArray("make_model").getJSONObject(1).getString("confidence");
                                            }
                                        }
                                   // }
                                }
                            }
                            if (Omatricula==null) Omatricula = "Unknown";
                            if (Omarca1==null) Omarca1 = "Unknown";
                            if (Omodel1==null) Omodel1 = "Unknown";
                            if (Omarca2==null) Omarca2 = "Unknown";
                            if (Omodel2==null) Omodel2 = "Unknown";
                            if (Oconfmatricula==null) Oconfmatricula = "Unknown";
                            if (Oconfmarca1==null) Oconfmarca1 = "Unknown";
                            if (Oconfmodel1==null) Oconfmodel1 = "Unknown";
                            if (Oconfmarca2==null) Oconfmarca2 = "Unknown";
                            if (Oconfmodel2==null) Oconfmodel2 = "Unknown";

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (Omatricula==null) Omatricula = "Unknown";
                if (Omarca1==null) Omarca1 = "Unknown";
                if (Omodel1==null) Omodel1 = "Unknown";
                if (Omarca2==null) Omarca2 = "Unknown";
                if (Omodel2==null) Omodel2 = "Unknown";
                //if (Oconfmatricula==null) Oconfmatricula = "Unknown";
                if (Oconfmarca1==null) Oconfmarca1 = "Unknown";
                if (Oconfmodel1==null) Oconfmodel1 = "Unknown";
                if (Oconfmarca2==null) Oconfmarca2 = "Unknown";
                if (Oconfmodel2==null) Oconfmodel2 = "Unknown";

                //afegim els resultats larray
                OpenALRPRes.add(Omatricula);
               // OpenALRPRes.add(Oconfmatricula);
                OpenALRPRes.add(Omarca1);
                OpenALRPRes.add(Oconfmarca1);
                OpenALRPRes.add(Omodel1);
                OpenALRPRes.add(Oconfmodel1);
                OpenALRPRes.add(Omarca2);
                OpenALRPRes.add(Oconfmarca2);
                OpenALRPRes.add(Omodel2);
                OpenALRPRes.add(Oconfmodel2);

                Intent resultIntent = new Intent(getActivity(),VehiculosForm.class);
                resultIntent.putExtra("omatricula", OpenALRPRes.get(0));
                //resultIntent.putExtra("oconfmatricula", OpenALRPRes.get(1));
                resultIntent.putExtra("omarca1", OpenALRPRes.get(1));
                resultIntent.putExtra("oconfmarca1", OpenALRPRes.get(2));
                resultIntent.putExtra("omodel1", OpenALRPRes.get(3));
                resultIntent.putExtra("oconfmodel1", OpenALRPRes.get(4));
                resultIntent.putExtra("omarca2", OpenALRPRes.get(5));
                resultIntent.putExtra("oconfmarca2", OpenALRPRes.get(6));
                resultIntent.putExtra("omodel2", OpenALRPRes.get(7));
                resultIntent.putExtra("oconfmodel2", OpenALRPRes.get(8));


                if (SighthoundFinished){
                    resultIntent.putExtra("smatricula", SighthoundRes.get(0));
                    //resultIntent.putExtra("sconfmatricula", SighthoundRes.get(1));
                    resultIntent.putExtra("smarca1", SighthoundRes.get(1));
                    resultIntent.putExtra("sconfmarca1", SighthoundRes.get(2));
                    resultIntent.putExtra("smodel1", SighthoundRes.get(3));
                    resultIntent.putExtra("sconfmodel1", SighthoundRes.get(4));
                    resultIntent.putExtra("smarca2", SighthoundRes.get(5));
                    resultIntent.putExtra("sconfmarca2", SighthoundRes.get(6));
                    resultIntent.putExtra("smodel2", SighthoundRes.get(7));
                    resultIntent.putExtra("sconfmodel2", SighthoundRes.get(8));
                }
                while (!SighthoundFinished) {
                    //System.out.println("Estic al while");
                }
                startActivity(resultIntent);
                dialog.dismiss();


            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                //System.out.println("Percentatge:"+(int)(bytesWritten*100)/totalSize);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private File getfile(){
        File folder = new File(Environment.getExternalStorageDirectory(),"gft_camera");
        //Comprovar si existeix la carpeta, sino crearla.
        if(!folder.exists()) folder.mkdir();
        File image_file =new File(folder,"images.jpg");
        return image_file;
    }

    private class NetworkAsyncTask extends AsyncTask<Void, Integer, Void> {
        //private ProgressDialog mProgress =null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* mProgress = new ProgressDialog(getContext());

            mProgress.setMessage("Please wait...");
            mProgress.setTitle("Sighthound");

            mProgress.setMax(100);
            mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgress.setIndeterminate(false);

            mProgress.show();*/
        }


        @Override
        protected Void doInBackground(Void... voids) {
            String api = "https://dev.sighthoundapi.com/v1/recognition?objectType=vehicle,licenseplate";
            String accessToken = "OAFMge5GN40wlLsyHvxpUxVkLzytbCmvNbKJ";

            ArrayList<String> res = new ArrayList<>();

            try {
                File file = getfile();
                FileInputStream fis = new FileInputStream(file);
                Bitmap bm = BitmapFactory.decodeStream(fis);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 0 , baos);
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
                //System.out.println("Estic mes que reaaaaaaaaaaaady esperant ja!!!!!!!!2"+ body.length);
                os.write(body);
                os.flush();
                int httpCode = connection.getResponseCode();

                /*for (int i=0; i < 100; ++i) {
                    publishProgress(i);
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }*/

                if (httpCode == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    JSONObject result = new JSONObject(String.valueOf(stringBuilder));
                    //System.out.println("Sighthound: "+stringBuilder);
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
                                                        Smatricula = array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("licenseplate")
                                                                .getJSONObject("attributes").getJSONObject("system").getJSONObject("string").getString("name");
                                                    }
                                                    /*if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("licenseplate").getJSONObject("attributes").getJSONObject("system").getJSONObject("string").has("confidence")){
                                                        Sconfmatricula = array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("licenseplate")
                                                                .getJSONObject("attributes").getJSONObject("system").getJSONObject("string").getString("confidence");
                                                    }*/
                                                }
                                            }
                                        }
                                    }
                                    if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").has("attributes")) {
                                        if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").has("system")) {
                                            //Mirem que a la resposta JSON estigui la marca
                                            if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").has("make")) {
                                                if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("make").has("name")) {
                                                    Smarca1 = array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("make").getString("name");
                                                }
                                                if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("make").has("confidence")) {
                                                    Sconfmarca1 = array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("make").getString("confidence");
                                                }
                                            }
                                            //Mirem que a la resposta JSON estigui el model
                                            if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").has("model")) {
                                                if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("model").has("name")) {
                                                    Smodel1 = array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("model").getString("name");
                                                }
                                                if (array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("model").has("confidence")) {
                                                    Sconfmodel1 = array.getJSONObject(0).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("model").getString("confidence");
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
                                                    Smarca2 = array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("make").getString("name");
                                                }
                                                if (array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("make").has("confidence")) {
                                                    Sconfmarca2 = array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("make").getString("confidence");
                                                }
                                            }
                                            //Mirem que a la resposta JSON estigui el model
                                            if (array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").has("model")) {
                                                if (array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("model").has("name")) {
                                                    Smodel2 = array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("model").getString("name");
                                                }
                                                if (array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").getJSONObject("system").getJSONObject("model").has("confidence")) {
                                                    Sconfmodel2 = array.getJSONObject(1).getJSONObject("vehicleAnnotation").getJSONObject("attributes").
                                                            getJSONObject("system").getJSONObject("model").getString("confidence");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                           e.printStackTrace();
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
            //En el cas de no haver reconegut cap cotxe marco els camps com Unkown i els tracto
            //a VehiculosForm
            if (Smatricula==null) Smatricula = "Unknown";
            if (Smarca1==null) Smarca1="Unknown";
            if (Smodel1==null) Smodel1="Unknown";
            if (Smarca2==null) Smarca2="Unknown";
            if (Smodel2==null) Smodel2="Unknown";
            //if (Sconfmatricula==null) Sconfmatricula = "Unknown" ;
            if (Sconfmarca1==null) Sconfmarca1="Unknown";
            if (Sconfmodel1==null) Sconfmodel1="Unknown";
            if (Sconfmarca2==null) Sconfmarca2="Unknown";
            if (Sconfmodel2==null) Sconfmodel2="Unknown";

            SighthoundRes.add(Smatricula);
            //SighthoundRes.add(Sconfmatricula);
            SighthoundRes.add(Smarca1);
            SighthoundRes.add(Sconfmarca1);
            SighthoundRes.add(Smodel1);
            SighthoundRes.add(Sconfmodel1);
            SighthoundRes.add(Smarca2);
            SighthoundRes.add(Sconfmarca2);
            SighthoundRes.add(Smodel2);
            SighthoundRes.add(Sconfmodel2);
            //Aviso de q ja pot llegir el resultat
            SighthoundFinished = true;

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //mProgress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
