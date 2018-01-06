package pae.seguros.conductores;

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
import pae.seguros.databases.User;

public class ConductoresFragment extends Fragment {

    private Activity mActivity;
    private View mView;
    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;
    private boolean visibleFAB = true;
    private static final String FAB = "fab";

    // @param fab: if true, then show the FAB, otherwise don't show it
    public static ConductoresFragment newInstance (boolean fab) {
        Bundle args = new Bundle();
        args.putBoolean(FAB, fab);

        ConductoresFragment fragment = new ConductoresFragment();
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

        mAdapter = new UserAdapter( AppDatabase.getDatabase(ConductoresFragment.this.getContext()).userDao().getAllUser());

        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        if (visibleFAB) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Random rand = new Random();
                    AppDatabase.getDatabase(ConductoresFragment.this.getContext()).userDao().
                            addUser(new User(rand.nextInt(),"Prueba",null,null,null,null,null,null,0,0,true));
                    initLayout();
                }
            });
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
    }
}
