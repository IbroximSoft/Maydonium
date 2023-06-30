package uz.arena.stadium;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.more.PoliceActivity;

public class InfoActivity extends Appcompat {
    EditText name, lost, city, year;
    TextView Number, info_police;
    Button button, buttonOff;
    CheckBox checkBox;
    String number;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        number = Objects.requireNonNull(auth.getCurrentUser()).getPhoneNumber();
        button = findViewById(R.id.info_btn);
        buttonOff = findViewById(R.id.info_btn_off);
        checkBox = findViewById(R.id.checkBy);
        name = findViewById(R.id.name_info);
        lost = findViewById(R.id.lost_info);
        city = findViewById(R.id.city_info);
        year = findViewById(R.id.year_info);
        Number = findViewById(R.id.txt_number_info);
        info_police = findViewById(R.id.info_police);
        Number.setText(number);

        buttonOff.setOnClickListener(v -> Toasty.warning(InfoActivity.this, getString(R.string.toast_shart), Toasty.LENGTH_SHORT, true).show());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    button.setVisibility(View.VISIBLE);
                    buttonOff.setVisibility(View.GONE);
                } else {
                    buttonOff.setVisibility(View.VISIBLE);
                    button.setVisibility(View.GONE);
                }
            }
        });

        info_police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, PooliceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        button.setOnClickListener(view ->
                SaveValue());
    }

    @SuppressLint("CheckResult")
    void SaveValue() {
        String Name = Objects.requireNonNull(name.getText()).toString();
        String Lost = Objects.requireNonNull(lost.getText()).toString();
        String Phone = Objects.requireNonNull(Number.getText()).toString();
        String City = Objects.requireNonNull(city.getText()).toString();
        String Year = Objects.requireNonNull(year.getText()).toString();
        if (TextUtils.isEmpty(Name)) {
            name.setError(getString(R.string.name));
            return;
        } else if (TextUtils.isEmpty(Lost)) {
            lost.setError(getString(R.string.lost_name));
            return;
        } else {
            String currentUserID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            HashMap<String, String> malumotHas = new HashMap<>();
            malumotHas.put("name", Name);
            malumotHas.put("lost", Lost);
            malumotHas.put("phone", Phone);
            malumotHas.put("city", City);
            malumotHas.put("year", Year);
            malumotHas.put("uid", currentUserID);
            malumotHas.put("status", "success");

            reference.child("User").child(currentUserID).setValue(malumotHas)
                    .addOnCompleteListener(task -> {
                        Toasty.success(InfoActivity.this, R.string.muvofaqiyatli, Toasty.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(InfoActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    });
        }
    }
}