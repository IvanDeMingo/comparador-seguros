package pae.seguros.vehiculos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import pae.seguros.R;

/**
 * Created by Gerard on 04/01/2018.
 */

public class VehiculosForm extends AppCompatActivity {
    private EditText editTextMatricula, editTextModel1, editTextModel2,editTextMarca1,editTextMarca2;
    private Spinner spinnerModel, spinnerMarca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_form);

        editTextMatricula = (EditText) findViewById(R.id.editTextMatricula);
        spinnerMarca = (Spinner) findViewById(R.id.spinnerMarca);
        spinnerModel = (Spinner) findViewById(R.id.spinnerModel);

        Bundle parameters = getIntent().getExtras();

        if(parameters!=null){
            editTextMatricula.setText(parameters.getString("omatricula"));

            ArrayList<String> listmodel = new ArrayList<>();
            listmodel.add(parameters.getString("omodel1"));
            listmodel.add(parameters.getString("omodel2"));
            listmodel.add(parameters.getString("smodel2"));
            listmodel.add(parameters.getString("smodel2"));
            ArrayAdapter<String> adaptermodel = new ArrayAdapter<String>(this,
                    R.layout.support_simple_spinner_dropdown_item,listmodel);
            spinnerModel.setAdapter(adaptermodel);

            ArrayList<String> listmarca = new ArrayList<>();
            listmarca.add(parameters.getString("omarca1"));
            listmarca.add(parameters.getString("omarca2"));
            listmarca.add(parameters.getString("smarca2"));
            listmarca.add(parameters.getString("smarca2"));
            ArrayAdapter<String> adaptermarca = new ArrayAdapter<String>(this,
                    R.layout.support_simple_spinner_dropdown_item,listmarca);
            spinnerMarca.setAdapter(adaptermarca);

        }
        else {
            //MOSTRAR DIALOG DERROR I LA OPCIÓ DE TORNAR A FER LA LECTURA DE LA IMATGE O CANCELAR

            editTextMatricula.setText("44444444D");
        }


    }
}
