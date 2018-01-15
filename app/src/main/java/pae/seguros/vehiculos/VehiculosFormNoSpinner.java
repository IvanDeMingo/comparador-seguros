package pae.seguros.vehiculos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pae.seguros.Dashboard;
import pae.seguros.R;
import pae.seguros.databases.AppDatabase;
import pae.seguros.databases.Car;
import pae.seguros.databases.User;

import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;

public class VehiculosFormNoSpinner extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextPlate, editTextMaker, editTextModel, editTextDateFM,
            editTextGas, editTextPower, editTextKM, editTextGarage, editTextDoor;

    private Button buttonSubmit, buttonCancel;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_form_no_spinners);

        awesomeValidation = new AwesomeValidation(COLORATION);

        editTextPlate = (EditText) findViewById(R.id.editTextPlate);
        editTextMaker = (EditText) findViewById(R.id.editTextMaker);
        editTextModel = (EditText) findViewById(R.id.editTextModel);
        editTextDateFM = (EditText) findViewById(R.id.editTextDateFM);
        editTextGas = (EditText) findViewById(R.id.editTextGas);
        editTextPower = (EditText) findViewById(R.id.editTextPower);
        editTextKM = (EditText) findViewById(R.id.editTextKM);
        editTextGarage = (EditText) findViewById(R.id.editTextGarage);
        editTextDoor = (EditText) findViewById(R.id.editTextDoor);


        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        Bundle extras = getIntent().getBundleExtra(QrCodeScanner.QR_RESULT);
        if (extras != null) {
            editTextPlate.setText(extras.getString(QrCodeScanner.LICENSE_NUMBER));
            editTextMaker.setText(extras.getString(QrCodeScanner.MAKER));
            editTextModel.setText(extras.getString(QrCodeScanner.MODEL));
            editTextDateFM.setText(extras.getString(QrCodeScanner.LICENSE_YEAR));
            editTextGas.setText(extras.getString(QrCodeScanner.GAS));
            editTextPower.setText(extras.getString(QrCodeScanner.POWER));
        }

        awesomeValidation.addValidation(this, R.id.editTextPlate, "^(([0-9]{4}[A-Za-z]]{3})|([A-Za-z]{1,2}[0-9]{4}[A-Za-z]{2}))$", R.string.plateerror);
        awesomeValidation.addValidation(this, R.id.editTextDateFM, "^(19|20)\\d{2}$", R.string.FMerror);
        awesomeValidation.addValidation(this, R.id.editTextPower, "^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$", R.string.powererror);
        awesomeValidation.addValidation(this, R.id.editTextKM, "^-?\\d{1,19}$", R.string.KMerror);
        awesomeValidation.addValidation(this, R.id.editTextDoor, "^-?\\d{1,19}$", R.string.doorerror);

        buttonSubmit.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    private void validateForm() {
        if (awesomeValidation.validate()) {
            if (!AppDatabase.getDatabase(this).carDao().getCar(editTextPlate.getText().toString()).isEmpty()) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Adding a car");
                dialog.setMessage("The car with plate " + editTextPlate.getText().toString() + " already exists, would you like to replace it? All related insurances will be removed");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        AppDatabase.getDatabase(getApplicationContext()).insuranceDao().removeInsuranceByCar(editTextPlate.getText().toString());
                        AppDatabase.getDatabase(getApplicationContext()).carDao().deleteCar(editTextPlate.getText().toString());
                        submitForm();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
            } else {
                submitForm();
            }
        }
    }

    private void submitForm(){
        AppDatabase.getDatabase(this).carDao().
                addCar(new Car(editTextPlate.getText().toString(),
                        editTextMaker.getText().toString(),
                        editTextModel.getText().toString(),null,
                        Long.parseLong(editTextDateFM.getText().toString()),null,
                        editTextGas.getText().toString(),
                        Double.parseDouble(editTextPower.getText().toString()),
                        Long.parseLong(editTextKM.getText().toString()),
                        editTextGarage.getText().toString(),
                        Long.parseLong(editTextDoor.getText().toString())
                        ));
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSubmit) {
            validateForm();
        }
        else if (view==buttonCancel) {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        }
    }
}