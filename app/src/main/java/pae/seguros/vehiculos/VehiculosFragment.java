package pae.seguros.vehiculos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.File;
import java.util.Random;

import pae.seguros.R;
import pae.seguros.databases.AppDatabase;
import pae.seguros.databases.Car;
import pae.seguros.vehiculos.camera.Camera;
import pae.seguros.vehiculos.camera.OpenALRP;

import static android.content.ContentValues.TAG;

public class VehiculosFragment extends Fragment implements View.OnClickListener {

    private Activity mActivity;
    private View mView;
    private RecyclerView mRecyclerView;
    private CarAdapter mAdapter;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fabCamera,fabQr;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Uri mImageUri;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    static final int CAM_REQUEST = 1;

    private static final int CAMERA = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.vehiculos_fragment, container, false);

        init();

        return mView;
    }

    private void init() {
        mActivity = getActivity();
        initLayout();
    }

    private void initLayout() {
        fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fabCamera = (FloatingActionButton) mView.findViewById(R.id.fabCamera);
        fabQr = (FloatingActionButton) mView.findViewById(R.id.fabQr);
        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fabCamera.setOnClickListener(this);
        fabQr.setOnClickListener(this);


        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CarAdapter(AppDatabase.getDatabase(VehiculosFragment.this.getContext()).carDao().getAllCars());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fabCamera:
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
                startActivityForResult(camera_intent,CAM_REQUEST);
                break;
            case R.id.fabQr:
                break;
        }
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

    public void animateFAB(){

        if(isFabOpen){
            fab.startAnimation(rotate_backward);
            fabCamera.startAnimation(fab_close);
            fabQr.startAnimation(fab_close);
            fabCamera.setClickable(false);
            fabQr.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            fabCamera.startAnimation(fab_open);
            fabQr.startAnimation(fab_open);
            fabCamera.setClickable(true);
            fabQr.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            setFragment(new OpenALRP());
        }
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_content, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }




}
