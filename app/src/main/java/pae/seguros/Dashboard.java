package pae.seguros;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import pae.seguros.conductores.ConductoresFragment;
import pae.seguros.seguros.SegurosFragment;
import pae.seguros.vehiculos.VehiculosFragment;

public class Dashboard extends AppCompatActivity {
    private Fragment fragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.vehiculos:
                    fragment = new VehiculosFragment();
                    break;
                case R.id.seguros:
                    fragment = new SegurosFragment();
                    break;
                case R.id.conductores:
                    fragment = new ConductoresFragment();
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

    }
}
