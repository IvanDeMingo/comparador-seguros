package pae.seguros.consultar_seguros;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pae.seguros.R;
import pae.seguros.conductores.AddDriver;
import pae.seguros.conductores.ConductoresFragment;

public class ConsultarSeguroFragmentConductor extends ConsultarSeguroFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consultar_seguro_page2, container, false);

        initDrivers();
        initAddButton(view);

        return view;
    }

    private void initAddButton(View view) {
        AppCompatButton buttonAdd = view.findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddDriver.class);
                startActivity(intent);
            }
        });
    }

    private void initDrivers() {
        ConductoresFragment fragment = ConductoresFragment.newInstance(false);
        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.conductores_content, fragment);
        t.commit();
    }

}
