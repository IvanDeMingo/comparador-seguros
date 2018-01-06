package pae.seguros.consultar_seguros;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pae.seguros.R;

public class ConsultarSeguroFragmentResumen extends ConsultarSeguroFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consultar_seguro_page3, container, false);



        return view;
    }

}
