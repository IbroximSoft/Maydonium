package uz.arena.stadium.fragment_top_menu;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import uz.arena.stadium.HomeActivity;
import uz.arena.stadium.R;
import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.more.BookingActivity;

public class FilterActivity extends Appcompat implements AdapterView.OnItemSelectedListener{
    Button filter_btn;
    EditText date_filter, time_edit_filter;
    TextView calendar_filter;
    ImageView filter_back;
    Spinner spinner;
    String item;
    ArrayList<String> arrayList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        arrayList.add(getString(R.string.vaqtni_kiriting));
        arrayList.add("00:00");
        arrayList.add("01:00");
        arrayList.add("02:00");
        arrayList.add("03:00");
        arrayList.add("04:00");
        arrayList.add("05:00");
        arrayList.add("06:00");
        arrayList.add("07:00");
        arrayList.add("08:00");
        arrayList.add("09:00");
        arrayList.add("10:00");
        arrayList.add("11:00");
        arrayList.add("12:00");
        arrayList.add("13:00");
        arrayList.add("14:00");
        arrayList.add("15:00");
        arrayList.add("16:00");
        arrayList.add("17:00");
        arrayList.add("18:00");
        arrayList.add("19:00");
        arrayList.add("20:00");
        arrayList.add("21:00");
        arrayList.add("22:00");
        arrayList.add("23:00");
        filter_btn = findViewById(R.id.filter_btn);
        date_filter = findViewById(R.id.date_filter);
        calendar_filter = findViewById(R.id.calendar_filter);
        time_edit_filter = findViewById(R.id.time_edit_filter);
        filter_back = findViewById(R.id.filter_back);
        spinner = findViewById(R.id.time_filter);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.my_selected_spinner, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        filter_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        calendar_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        FilterActivity.this, (viewS, year1, month1, day1) -> {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(year1, month1, day1);

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat date1 = new SimpleDateFormat("dd.MM.yyyy");
                    String dateString = dateFormat.format(calendar1.getTime());
                    String dateS = date1.format(calendar1.getTime());
                    calendar_filter.setText(dateS);
                    date_filter.setText(dateString);
                }, day, month, year
                );

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        date_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TestDate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = spinner.getSelectedItem().toString();
        time_edit_filter.setText(item);
        if (item.equals(getString(R.string.vaqtni_kiriting))){
        }else {
            TestDate();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void TestDate(){
        String date = date_filter.getText().toString();
        if (date.isEmpty() || item.equals(getString(R.string.vaqtni_kiriting))){
        }else{
            Test();
        }
    }

    private void Test() {
        filter_btn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("date", date_filter.getText().toString());
            bundle.putString("time", time_edit_filter.getText().toString());

            HomeFragment fragment = new HomeFragment();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_filter, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FilterActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        FilterActivity.this.finish();
    }
}