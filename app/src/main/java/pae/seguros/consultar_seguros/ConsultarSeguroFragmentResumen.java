package pae.seguros.consultar_seguros;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pae.seguros.R;

public class ConsultarSeguroFragmentResumen extends ConsultarSeguroFragment {
    private TextView selecteddriver, selectedvehicle;
    private View mView;
    private ConsultarSeguroActivity supActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.consultar_seguro_page3, container, false);


        return mView;
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible) {
            supActivity = (ConsultarSeguroActivity) getActivity();
            if(supActivity.getConductor()!=null)
                selecteddriver.setText(supActivity.getConductor().dni);
               //Log.println(Log.DEBUG,"con",supActivity.getConductor().name);
            if(supActivity.getCoche()!=null)
                selectedvehicle.setText(supActivity.getCoche().plate);
               // Log.println(Log.DEBUG,"coc", supActivity.getCoche().plate);
        }

        super.setMenuVisibility(visible);
    }









}
