package pae.seguros.seguros;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import pae.seguros.R;
import pae.seguros.databases.AppDatabase;
import pae.seguros.databases.Car;
import pae.seguros.databases.Insurance;
import pae.seguros.databases.User;

public class SegurosFragment extends Fragment {

    private Activity mActivity;
    private View mView;
    private AppDatabase db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.seguros_fragment, container, false);
        mActivity = getActivity();
        initLayout();
        return mView;
    }

    private void initLayout() {
        LinearLayout layout1,layout2;

        layout1 = (LinearLayout) mView.findViewById(R.id.Layout1);
        layout2 = (LinearLayout) mView.findViewById(R.id.Layout2);

        List<Insurance> allInsurances = AppDatabase.getDatabase(this.getContext()).insuranceDao().getAllInsurances();
        if (allInsurances.isEmpty()) {
            layout1.setVisibility(LinearLayout.VISIBLE);
            layout2.setVisibility(LinearLayout.GONE);
        }
        else {
            layout1.setVisibility(LinearLayout.GONE);
            layout2.setVisibility(LinearLayout.VISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: se añade un seguro por defecto para probar la aplicacion, falta hacer toda la logica cuando se llama al fab
                AppDatabase.getDatabase(SegurosFragment.this.getContext()).userDao().
                        addUser(new User(0,null,null,null,null,null,null,null,0,0,true));
                AppDatabase.getDatabase(SegurosFragment.this.getContext()).carDao().
                        addCar(new Car("0",null,null,null,0));
                AppDatabase.getDatabase(SegurosFragment.this.getContext()).insuranceDao().
                        addInsurance(new Insurance(0,0));
                
                initLayout();
            }
        });
    }
}
