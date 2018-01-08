package pae.seguros.consultar_seguros;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pae.seguros.R;

public class ConsultarSeguroFragmentResumen extends ConsultarSeguroFragment {
    private Button bCalcularSeguro;
    private TextView selecteddriver, selectedvehicle;
    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.consultar_seguro_page3, container, false);

        initLayout();

        return mView;
    }

    private void initLayout() {
        bCalcularSeguro = (Button) mView.findViewById(R.id.bCalcularSeguro);
        selecteddriver = (TextView) mView.findViewById(R.id.selecteddriver);
        selectedvehicle = (TextView) mView.findViewById(R.id.selectedvehicle);
    }

    View.OnClickListener buttonHandler = new View.OnClickListener() {
        public void onClick(View v) {
        }
    };

}
