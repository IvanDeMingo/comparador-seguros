package pae.seguros.consultar_seguros;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import pae.seguros.R;


public class ConsultarSeguroActivity extends AppCompatActivity {

    private ConsultarSeguroPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_seguro_view_pager);

        ArrayList<ConsultarSeguroFragment> fragments = new ArrayList<>();
        initFragments(fragments);

        ViewPager viewPager = findViewById(R.id.consultar_seguro_view_pager);

        mPagerAdapter = new ConsultarSeguroPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.consultar_seguro_tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    private void initFragments(ArrayList<ConsultarSeguroFragment> fragments) {
        for (int i = 0; i < ConsultarSeguroPagerAdapter.NUM_PAGES; i++) {
            fragments.add(ConsultarSeguroFragment.newInstance(i));
        }
    }
}
