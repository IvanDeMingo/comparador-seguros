package pae.seguros.consultar_seguros;


import android.support.v4.app.Fragment;

public class ConsultarSeguroFragment extends Fragment {


    public static ConsultarSeguroFragment newInstance(int page) {
        ConsultarSeguroFragment fragment;

        switch (page) {
            case 0:
                fragment = new ConsultarSeguroFragmentCoche();
                break;
            case 1:
                fragment = new ConsultarSeguroFragmentConductor();
                break;
            default:
                fragment = new ConsultarSeguroFragmentResumen();
                break;
        }

        return fragment;
    }







}
