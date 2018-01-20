package pae.seguros.consultar_seguros;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import pae.seguros.R;
import pae.seguros.vehiculos.AddVehicle;
import pae.seguros.vehiculos.VehiculosFragment;
import pae.seguros.databases.Car;
public class ConsultarSeguroFragmentCoche extends ConsultarSeguroFragment {
    private VehiculosFragment fragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consultar_seguro_page1, container, false);

        initVehicles();
        initAddButton(view);

        return view;
    }

    private void initAddButton(View view) {
        ImageView buttonAdd = view.findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddVehicle.class);
                startActivityForResult(intent,0);
            }
        });
    }

    private void initVehicles() {
        fragment = VehiculosFragment.newInstance(false);
        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.coches_content, fragment);
        t.commit();
    }

    public Car getSelected()
    {
        return fragment.getSelected();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.refreshList();

    }
}
