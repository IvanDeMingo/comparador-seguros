package pae.seguros;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import pae.seguros.conductores.ConductoresFragment;
import pae.seguros.introduction.IntroActivity;
import pae.seguros.seguros.SegurosFragment;
import pae.seguros.vehiculos.VehiculosFragment;

public class Dashboard extends AppCompatActivity {
    private Fragment fragment;
    private TextView toolbarTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.vehiculos:
                    fragment = VehiculosFragment.newInstance(true);
                    toolbarTitle.setText(R.string.vehiculos);
                    break;
                case R.id.seguros:
                    fragment = new SegurosFragment();
                    toolbarTitle.setText(R.string.seguros);
                    break;
                case R.id.conductores:
                    fragment = ConductoresFragment.newInstance(true);
                    toolbarTitle.setText(R.string.conductores);
                    break;
            }
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_content, fragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.seguros);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content, new SegurosFragment());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.seguros);

        Intent introIntent = new Intent(this, IntroActivity.class);
        startActivity(introIntent);
    }
}
