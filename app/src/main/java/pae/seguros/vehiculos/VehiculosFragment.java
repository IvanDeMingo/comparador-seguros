package pae.seguros.vehiculos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.File;

import pae.seguros.BuildConfig;
import pae.seguros.R;
import pae.seguros.databases.AppDatabase;
import pae.seguros.vehiculos.camera.OpenALRP;

public class VehiculosFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private RecyclerView mRecyclerView;
    private CarAdapter mAdapter;
    private boolean visibleFAB = true;
    private static final String FAB = "fab";

    // @param fab: if true, then show the FAB, otherwise don't show it
    public static VehiculosFragment newInstance (boolean fab) {
        Bundle args = new Bundle();
        args.putBoolean(FAB, fab);

        VehiculosFragment fragment = new VehiculosFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fabCamera,fabQr,fabManual;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Uri mImageUri;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final int CAMERA = 0;
    static final int CAM_REQUEST = 1;
    private static final int FORM = 2;
    private static final int QR_SCANNER = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.vehiculos_fragment, container, false);

        this.visibleFAB = getArguments().getBoolean(FAB);

        initLayout();

        return mView;
    }

    private void initLayout() {
        fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fabCamera = (FloatingActionButton) mView.findViewById(R.id.fabCamera);
        fabQr = (FloatingActionButton) mView.findViewById(R.id.fabQr);
        fabManual = (FloatingActionButton) mView.findViewById(R.id.fabManual);
        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fabCamera.setOnClickListener(this);
        fabQr.setOnClickListener(this);
        fabManual.setOnClickListener(this);


        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        if (!visibleFAB) {
            fab.setVisibility(View.INVISIBLE);
            mAdapter = new CarAdapter(AppDatabase.getDatabase(VehiculosFragment.this.getContext()).carDao().getAllCars(),true);
        }
        else mAdapter = new CarAdapter(AppDatabase.getDatabase(VehiculosFragment.this.getContext()).carDao().getAllCars(),false);
        mRecyclerView.setAdapter(mAdapter);
    }

    public pae.seguros.databases.Car getSelected()
    {
        return mAdapter.getSelectedItem();
    }
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fabCamera:
                int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAM_REQUEST);
                }
                //verificamos los permisos de escritura en storage
                else if (permission != PackageManager.PERMISSION_GRANTED) {
                    //no tenemos permisos, entonces se lo damos al usuario
                    ActivityCompat.requestPermissions(getActivity(),PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
                }
                else {
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //Li pasem la localitzacio del fitxer al intent
                    File file = getfile();
                    mImageUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",file);
                    camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(camera_intent, CAM_REQUEST);
                }
                break;
            case R.id.fabQr:
                // Request camera permission if it's not already granted
                if (ContextCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAM_REQUEST);
                } else {
                    // Start QR scan activity
                    Intent intent = new Intent(getContext(), QrCodeScanner.class);
                    startActivityForResult(intent, QR_SCANNER);
                }
                break;
            case R.id.fabManual:
                Intent intent = new Intent(getContext(), VehiculosFormNoSpinner.class);
                startActivityForResult(intent, FORM);
                break;
        }
    }

    /*public static boolean verifyStoragePermissions(Activity activity) {
        // comprobamos si tenemos permiso de escritura
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //no tenemos permisos, entonces se lo damos al usuario
            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
        return  true;
    }*/

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
            fabManual.startAnimation(fab_close);
            fabCamera.setClickable(false);
            fabQr.setClickable(false);
            fabManual.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            fabCamera.startAnimation(fab_open);
            fabQr.startAnimation(fab_open);
            fabManual.startAnimation(fab_open);
            fabCamera.setClickable(true);
            fabQr.setClickable(true);
            fabManual.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshList();
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAM_REQUEST) {
                //setFragment(new OpenALRP());
                Intent myIntent = new Intent(getActivity(),OpenALRP.class);
                startActivity(myIntent);
            }
        }
    }

    public void refreshList() {
        mAdapter.updateCars(AppDatabase.getDatabase(VehiculosFragment.this.getContext()).carDao().getAllCars());
    }

}
