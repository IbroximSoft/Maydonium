package uz.arena.stadium.fragment_top_menu;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import uz.arena.stadium.R;

public class FilterFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Button filter_btn;
    EditText date_filter, time_edit_filter;
    TextView calendar_filter;
    ImageView filter_back;
    Spinner spinner;
    String item;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        arrayList.add(getString(R.string.vaqtni_kiriting));
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
        filter_btn = view.findViewById(R.id.filter_btn);
        date_filter = view.findViewById(R.id.date_filter);
        calendar_filter = view.findViewById(R.id.calendar_filter);
        time_edit_filter = view.findViewById(R.id.time_edit_filter);
        filter_back = view.findViewById(R.id.filter_back);
        spinner = view.findViewById(R.id.time_filter);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.my_selected_spinner, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        filter_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_home, new HomeFragment())
                        .commit();
            }
        });

        calendar_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), (view, year1, month1, day1) -> {
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


        return view;
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

    private void Test(){
        filter_btn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("date", date_filter.getText().toString());
            bundle.putString("time", time_edit_filter.getText().toString());

            HomeFragment fragment = new HomeFragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragment_home, fragment).commit();
        });
    }
}