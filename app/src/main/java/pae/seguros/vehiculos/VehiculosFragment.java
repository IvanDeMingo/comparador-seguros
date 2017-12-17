package pae.seguros.vehiculos;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pae.seguros.R;

public class VehiculosFragment extends Fragment {

    private Activity mActivity;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.vehiculos_fragment, container, false);

        init();

        return mView;
    }

    private void init() {
        mActivity = getActivity();
        initLayout();
    }

    private void initLayout() {
        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Se presionó el FAB vehiclos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
