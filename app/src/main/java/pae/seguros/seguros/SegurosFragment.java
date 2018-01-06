package pae.seguros.seguros;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import pae.seguros.R;
import pae.seguros.consultar_seguros.ConsultarSeguroActivity;
import pae.seguros.databases.AppDatabase;
import pae.seguros.databases.Insurance;

public class SegurosFragment extends Fragment {

    private View mView;
    private AppDatabase db;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private TextView emptyText2;
    private ImageView emptyImage;
    private RecyclerView mRecyclerView;
    private InsuranceAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.seguros_fragment, container, false);
        initLayout();
        return mView;
    }

    private void initLayout() {

        recyclerView = (RecyclerView) mView.findViewById(R.id.rv);
        emptyText = (TextView) mView.findViewById(R.id.empty_text);
        emptyText2 = (TextView) mView.findViewById(R.id.empty_text2);
        emptyImage = (ImageView) mView.findViewById(R.id.empty_image);

        List<Insurance> allInsurances = AppDatabase.getDatabase(this.getContext()).insuranceDao().getAllInsurances();
        if (allInsurances.isEmpty()) {
            emptyText.setVisibility(LinearLayout.VISIBLE);
            emptyText2.setVisibility(LinearLayout.VISIBLE);
            emptyImage.setVisibility(LinearLayout.VISIBLE);
            recyclerView.setVisibility(LinearLayout.GONE);
        }
        else {
            emptyText.setVisibility(LinearLayout.GONE);
            emptyText2.setVisibility(LinearLayout.GONE);
            emptyImage.setVisibility(LinearLayout.GONE);
            recyclerView.setVisibility(LinearLayout.VISIBLE);
        }

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new InsuranceAdapter(AppDatabase.getDatabase(SegurosFragment.this.getContext()).insuranceDao().getAllInsurances());
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("DEBUG", "FAB listener");
                Intent intent = new Intent(getActivity(), ConsultarSeguroActivity.class);
                startActivity(intent);
                /*
                Random rand = new Random();
                String dni = String.valueOf(rand.nextInt());
                String plate = String.valueOf(rand.nextInt());
                //TODO: se añade un seguro por defecto para probar la aplicacion, falta hacer toda la logica cuando se llama al fab
                AppDatabase.getDatabase(SegurosFragment.this.getContext()).userDao().
                        addUser(new User(dni,null,null,null,null,null,null,null,0,0,true));
                AppDatabase.getDatabase(SegurosFragment.this.getContext()).carDao().
                        addCar(new Car(plate,String.valueOf(rand.nextInt()),null,null,0, ""));
                AppDatabase.getDatabase(SegurosFragment.this.getContext()).insuranceDao().
                        addInsurance(new Insurance(dni,plate));
                
                initLayout();
                */
            }
        });
    }
}
