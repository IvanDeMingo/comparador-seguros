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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pae.seguros.R;
import pae.seguros.databases.AppDatabase;
import pae.seguros.databases.Car;
import pae.seguros.databases.Insurance;
import pae.seguros.databases.User;

public class ConsultarSeguroFragmentResumen extends ConsultarSeguroFragment {
    private Button bCalcularSeguro;
    private TextView selecteddriver, selectedvehicle, yeartext,occasionalDrivers;
    private SeekBar seek;
    private View mView;
    private RecyclerView seguros;
    private ConsultarSeguroActivity supActivity;
    private Car currentCar;
    private User currentUser;
    private Random rnd;
    private MultiSelectionSpinner spinner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        supActivity = (ConsultarSeguroActivity) getActivity();

        mView = inflater.inflate(R.layout.consultar_seguro_page3, container, false);
        selecteddriver = (TextView) mView.findViewById(R.id.selecteddriver);
        selectedvehicle = (TextView) mView.findViewById(R.id.selectedvehicle);
        occasionalDrivers = (TextView) mView.findViewById(R.id.occasionalDrivers);
        bCalcularSeguro = (Button) mView.findViewById(R.id.calcularSeguroButton);
        seek = (SeekBar) mView.findViewById(R.id.yearseekbar);
        yeartext = (TextView) mView.findViewById(R.id.yeartext);
        spinner = (MultiSelectionSpinner) mView.findViewById(R.id.mySpinner1);
        spinner.setEnabled(false);

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
                List<String> extraDriversSelected = spinner.getSelectedStrings();
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
                    currentUser = supActivity.getConductor();
                    selecteddriver.setText(supActivity.getConductor().name + " " + supActivity.getConductor().surname +
                            " " + supActivity.getConductor().lastname + " " + supActivity.getConductor().dni);
                }
                if (supActivity.getCoche() != null) {
                    currentCar = supActivity.getCoche();
                    selectedvehicle.setText(supActivity.getCoche().company + " " +
                            supActivity.getCoche().plate);
                }
                if (currentUser != null) {
                    List<String> extraDrivers = parseDrivers(AppDatabase.getDatabase(this.getContext()).userDao().getExtraDrivers(currentUser.dni));
                    if (!extraDrivers.isEmpty()) {
                        spinner.setItems(extraDrivers);
                        spinner.setSelection(Collections.<String>emptyList());
                        spinner.setEnabled(true);
                    }
                    occasionalDrivers.setText("Occasional drivers:");
                }
                else {
                    occasionalDrivers.setText("Select a main driver to be able to select occasional drivers");
                }
            }

        }
        super.setMenuVisibility(visible);
    }

    private List<String> parseDrivers(List<User> extraUser) {
        List<String> extraDrivers = new ArrayList<String>();
        for (Iterator<User> i = extraUser.iterator(); i.hasNext();) {
            User user = i.next();
            String item = user.name + " " + user.surname + " "+ user.lastname;
            extraDrivers.add(item);
        }
        return extraDrivers;
    }

}
