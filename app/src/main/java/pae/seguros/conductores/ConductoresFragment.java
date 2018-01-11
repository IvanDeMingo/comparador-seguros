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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import pae.seguros.R;
import pae.seguros.conductores.dni_ocr.DniOcrMain;
import pae.seguros.databases.AppDatabase;

public class ConductoresFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;
    private boolean visibleFAB = true;
    private static final String FAB = "fab";

    // @param fab: if true, then show the FAB, otherwise don't show it
    public static ConductoresFragment newInstance (boolean fab) {
        Bundle args = new Bundle();
        args.putBoolean(FAB, fab);

        ConductoresFragment fragment = new ConductoresFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fabOcr,fabNfc,fabData;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    private static final int OCR = 0;
    public static final int NFC = 1;
    private static final int FORM = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.vehiculos_fragment, container, false);

        this.visibleFAB = getArguments().getBoolean(FAB);

        mView = inflater.inflate(R.layout.user_fragment, container, false);
        initLayout();

        return mView;
    }

    private void initLayout() {
        fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fabOcr = (FloatingActionButton) mView.findViewById(R.id.fabOcr);
        fabNfc = (FloatingActionButton) mView.findViewById(R.id.fabNfc);
        fabData = (FloatingActionButton) mView.findViewById(R.id.fabData);
        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fabOcr.setOnClickListener(this);
        fabNfc.setOnClickListener(this);
        fabData.setOnClickListener(this);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        if (!visibleFAB) {
            fab.setVisibility(View.INVISIBLE);
            mAdapter = new UserAdapter(AppDatabase.getDatabase(ConductoresFragment.this.getContext()).userDao().getAllUser(),true);
        }
        else
            mAdapter = new UserAdapter(AppDatabase.getDatabase(ConductoresFragment.this.getContext()).userDao().getAllUser(),false);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fabOcr:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] { android.Manifest.permission.CAMERA },
                            0);
                } else {
                    Intent intent = new Intent(getContext(), DniOcrMain.class);
                    startActivityForResult(intent, OCR);
                }
                break;
            case R.id.fabNfc:
                if (!checkNFCAvailable()) return;
                readDNIe();
                break;
            case R.id.fabData:
                Intent intent = new Intent(getContext(), UserForm.class);
                startActivityForResult(intent, FORM);
                break;
        }
    }

    public void animateFAB(){

        if(isFabOpen){
            fab.startAnimation(rotate_backward);
            fabOcr.startAnimation(fab_close);
            fabNfc.startAnimation(fab_close);
            fabData.startAnimation(fab_close);
            fabOcr.setClickable(false);
            fabNfc.setClickable(false);
            fabData.setClickable(false);
            isFabOpen = false;

        } else {
            fab.startAnimation(rotate_forward);
            fabOcr.startAnimation(fab_open);
            fabNfc.startAnimation(fab_open);
            fabData.startAnimation(fab_open);
            fabOcr.setClickable(true);
            fabNfc.setClickable(true);
            fabData.setClickable(true);
            isFabOpen = true;
        }
    }

    public void refreshList() {
        mAdapter.updateUsers(AppDatabase.getDatabase(ConductoresFragment.this.getContext()).userDao().getAllUser());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshList();
        if (requestCode == NFC) {
            initLayout();
        }
    }

    private void readDNIe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

        final Activity activity = getActivity();

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
                        nfcAdapter.setView(mView);
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
        NfcManager manager = (NfcManager) getActivity().getSystemService(Context.NFC_SERVICE);
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
        new AlertDialog.Builder(getContext())
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
