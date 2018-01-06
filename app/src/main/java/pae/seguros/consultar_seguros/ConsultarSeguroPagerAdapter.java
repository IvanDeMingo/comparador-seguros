package pae.seguros.consultar_seguros;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class ConsultarSeguroPagerAdapter extends FragmentPagerAdapter {

    public final static int NUM_PAGES = 3;

    private ArrayList<ConsultarSeguroFragment> mFragmentList;

    public ConsultarSeguroPagerAdapter(FragmentManager fm, ArrayList<ConsultarSeguroFragment> fragments) {
        super(fm);
        this.mFragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
