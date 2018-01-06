package pae.seguros.conductores;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pae.seguros.Dashboard;
import pae.seguros.R;
import pae.seguros.databases.AppDatabase;
import pae.seguros.databases.User;

import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;

public class UserForm extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextDNI, editTextName, editTextSurname, editTextLastname,
            editTextDob, editTextPlace;
    private Spinner spinnerGender;

    private Button buttonSubmit, buttonCancel;

    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_form);

        awesomeValidation = new AwesomeValidation(COLORATION);

        editTextDNI = (EditText) findViewById(R.id.editTextDNI);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSurname = (EditText) findViewById(R.id.editTextSurname);
        editTextLastname = (EditText) findViewById(R.id.editTextLastname);
        editTextDob = (EditText) findViewById(R.id.editTextDob);
        editTextPlace = (EditText) findViewById(R.id.editTextPlace);

        spinnerGender = (Spinner)findViewById(R.id.spinnerGender);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            editTextDNI.setText(extras.getString("dni"));
            editTextDob.setText(extras.getString("edad"), TextView.BufferType.EDITABLE);
            if (extras.getString("sexo").equals("M")) spinnerGender.setSelection(0);
            else spinnerGender.setSelection(1);
        }

        awesomeValidation.addValidation(this, R.id.editTextDNI, "^(([0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKET])|([XYZ][0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKET]))$", R.string.dnierror);
        awesomeValidation.addValidation(this, R.id.editTextName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editTextSurname, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.surnameerror);
        awesomeValidation.addValidation(this, R.id.editTextLastname, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.lastnameerror);
        awesomeValidation.addValidation(this, R.id.editTextDob, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerror);
        awesomeValidation.addValidation(this, R.id.editTextPlace, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.placeerror);

        buttonSubmit.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    private void validateForm() {
        if (!AppDatabase.getDatabase(this).userDao().getUser(editTextDNI.getText().toString()).isEmpty()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Adding a driver");
            dialog.setMessage("The driver with DNI "+ editTextDNI.getText().toString() +" already exists, would you like to replace it?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int id) {
                    AppDatabase.getDatabase(getApplicationContext()).userDao().deleteUser(editTextDNI.getText().toString());
                    submitForm();
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
        }
        else {
            submitForm();
        }
    }

    private void submitForm(){
        if (awesomeValidation.validate()) {
            try {
                String dateStr = editTextDob.getText().toString();
                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
                Date dateObj = curFormater.parse(dateStr);
                long unixTimestamp = dateObj.getTime() / 1000;
                AppDatabase.getDatabase(this).userDao().
                        addUser(new User(editTextDNI.getText().toString(),
                                editTextName.getText().toString(),
                                editTextSurname.getText().toString(),
                                editTextLastname.getText().toString(),null,null,
                                editTextPlace.getText().toString(),null,0,
                                unixTimestamp,
                                String.valueOf(spinnerGender.getSelectedItem()).equals("Male")));
                Intent resultIntent = new Intent(this,Dashboard.class);
                startActivity(resultIntent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSubmit) {
            validateForm();
        }
        else if (view==buttonCancel) {
            onBackPressed();
        }
    }
}