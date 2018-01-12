package pae.seguros.consultar_seguros;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

import pae.seguros.R;
import pae.seguros.databases.AppDatabase;
import pae.seguros.databases.Car;
import pae.seguros.databases.Insurance;
import pae.seguros.databases.User;

public class ConsultarSeguroFragmentResumen extends ConsultarSeguroFragment {
    private Button bCalcularSeguro;
    private TextView selecteddriver, selectedvehicle, yeartext;
    private SeekBar seek;
    private View mView;
    private RecyclerView seguros;
    private ConsultarSeguroActivity supActivity;
    private Car currentCar;
    private User currentUser;
    private Random rnd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        supActivity = (ConsultarSeguroActivity) getActivity();

        mView = inflater.inflate(R.layout.consultar_seguro_page3, container, false);
        selecteddriver = (TextView) mView.findViewById(R.id.selecteddriver);
        selectedvehicle = (TextView) mView.findViewById(R.id.selectedvehicle);
        bCalcularSeguro = (Button) mView.findViewById(R.id.calcularSeguroButton);
        seek = (SeekBar) mView.findViewById(R.id.yearseekbar);
        yeartext = (TextView) mView.findViewById(R.id.yeartext);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                yeartext.setText(Integer.toString(progress+2018));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        bCalcularSeguro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction();
            }
        });
        return mView;
    }



    private void buttonAction()
    {


        if(supActivity!=null) {
            supActivity = (ConsultarSeguroActivity) getActivity();

            if(currentCar!=null && currentUser!=null)
            {

                AppDatabase.getDatabase(getContext()).insuranceDao().addInsurance(new Insurance(currentUser.dni, currentCar.plate));
                int seed = (currentUser.dni + currentCar.plate).hashCode();
                Intent intent = new Intent(getActivity().getApplicationContext(), BuquedaSeguros.class);
                intent.putExtra("seed",seed);
                startActivity(intent);
            }
        }
    }


    @Override
    public void setMenuVisibility(final boolean visible) {

        if(supActivity!=null) {
            if (visible) {
                supActivity = (ConsultarSeguroActivity) getActivity();
                if (supActivity.getConductor() != null) {
                    currentCar = supActivity.getCoche();
                    selecteddriver.setText(supActivity.getConductor().name + " " + supActivity.getConductor().surname +
                            " " + supActivity.getConductor().lastname + " " + supActivity.getConductor().dni);
                }
                if (supActivity.getCoche() != null) {
                    currentUser = supActivity.getConductor();
                    selectedvehicle.setText(supActivity.getCoche().company + " " +
                            supActivity.getCoche().plate);
                }
            }

        }
        super.setMenuVisibility(visible);
    }



}
