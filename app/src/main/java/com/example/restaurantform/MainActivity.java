package com.example.restaurantform;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView edt_resType;
    private ArrayList<String> typesArray;
    private ArrayAdapter<String> arrayAdapter_types;

    private DatePickerDialog datePickerDialog;
    private TextInputEditText edt_dateVisit;
    private Button btn_submit;

    private TextInputEditText txt_resName;
    private TextInputEditText txt_price;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hook();
        validateSubmit();

        arrayAdapter_types = new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,typesArray);
        edt_resType.setAdapter(arrayAdapter_types);

        edt_resType.setThreshold(1);
        edt_dateVisit.setOnClickListener(view -> pickDay());




        
    }

    private void pickDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            calendar.set(i,i1,i2);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            edt_dateVisit.setText(simpleDateFormat.format(calendar.getTime()));
        }, year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void validateSubmit(){
        btn_submit.setOnClickListener(view -> {
            String resName = Objects.requireNonNull(txt_resName.getText()).toString().trim();
            String price = Objects.requireNonNull(txt_price.getText()).toString().trim();
            String type = Objects.requireNonNull(edt_resType.getText()).toString().trim();
            String date = Objects.requireNonNull(edt_dateVisit.getText()).toString().trim();



            Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


            if (TextUtils.isEmpty(resName)) {
                TextInputLayout til_name = findViewById(R.id.layout_resName);
                til_name.setError(getString(R.string.txt_error_validate_resName));
                til_name.requestFocus();
            }

            if (TextUtils.isEmpty(price)) {
                TextInputLayout til = (TextInputLayout) findViewById(R.id.layout_averagePrice);
                til.setError(getString(R.string.txt_error_validate_resPrice));
                til.requestFocus();
            }

            if (TextUtils.isEmpty(type)) {
                TextInputLayout til = (TextInputLayout) findViewById(R.id.layout_resType);
                til.setError(getString(R.string.txt_error_validate_resType));
                til.requestFocus();
            }

            if (TextUtils.isEmpty(date)) {
                TextInputLayout til = (TextInputLayout) findViewById(R.id.layout_dateVisit);
                til.setError(getString(R.string.txt_error_validate_resDate));
                til.requestFocus();
            }

            if (TextUtils.isEmpty(resName) || TextUtils.isEmpty(type) || TextUtils.isEmpty(date) || TextUtils.isEmpty(price)) {
                Toast.makeText(MainActivity.this, getString(R.string.empty_credentials_tv), Toast.LENGTH_SHORT).show();
                vi.vibrate(400);
            }


            else{
                submit(resName,type,date,price);
            }

        });
    }

    private void submit(String resName, String type, String date, String price) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> resInfo = new HashMap<>();
        resInfo.put("resName", resName);
        resInfo.put("resType", type);
        resInfo.put("date", date);
        resInfo.put("price", price);
        db.collection("restaurant").add(resInfo).addOnSuccessListener(documentReference -> {

        });

    }


    private void hook(){
        edt_resType = findViewById(R.id.auto_com_tv_resType);
        edt_dateVisit = findViewById(R.id.edt_dateVisit);
        btn_submit = findViewById(R.id.btn_submit);
        txt_resName = findViewById(R.id.edt_resName);
        txt_price = findViewById(R.id.edt_averagePrice);

        typesArray = new ArrayList<>();
        typesArray.add("Grill");
        typesArray.add("Seafood");
        typesArray.add("Fast Food");
        typesArray.add("Italian food");
    }
}