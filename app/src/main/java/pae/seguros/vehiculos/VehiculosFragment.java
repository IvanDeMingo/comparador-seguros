package pae.seguros.vehiculos;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import pae.seguros.R;
import pae.seguros.databases.AppDatabase;
import pae.seguros.databases.Car;

public class VehiculosFragment extends Fragment {

    private Activity mActivity;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.vehiculos_fragment, container, false);

        this.visibleFAB = getArguments().getBoolean(FAB);

        init();

        return mView;
    }

    private void init() {
        mActivity = getActivity();
        initLayout();
    }

    private void initLayout() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CarAdapter( AppDatabase.getDatabase(VehiculosFragment.this.getContext()).carDao().getAllCars());

        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        if (visibleFAB) {
            fab.setOnClickListener(new View.OnClickListener() {
                Random rand = new Random();
                @Override
                public void onClick(View view) {
                    AppDatabase.getDatabase(VehiculosFragment.this.getContext()).carDao().
                            addCar(new Car(String.valueOf(rand.nextInt()),String.valueOf(rand.nextInt()),null,null,0));
                    initLayout();
                }
            });
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
    }
}
