package uz.arena.stadium;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.payment.PayInfoActivity;

public class LastActivity extends Appcompat {
    FirebaseAuth auth;
    DatabaseReference reference;
    ImageView image, back;
    Button cancel_btn, pay_btn;
    String uid_string;
    private String filter;
    TextView txt_name, txt_location, txt_summa, txt_date, txt_time, txt_date_gone, txt_time_gone,
            txt_summa2, name_last_gone, lost_last_gone, last_advance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        back = findViewById(R.id.last_back);
        image = findViewById(R.id.img_card_last);
        cancel_btn = findViewById(R.id.cancel_last);
        pay_btn = findViewById(R.id.pay_last);

        txt_name = findViewById(R.id.txt_name_last);
        txt_location = findViewById(R.id.txt_location_last);
        txt_summa = findViewById(R.id.txt_summa_last);
        txt_date = findViewById(R.id.txt_date_last);
        txt_time = findViewById(R.id.txt_time_last);
        txt_date_gone = findViewById(R.id.txt_date_last_gone);
        txt_time_gone = findViewById(R.id.txt_time_last_gone);
        txt_summa2 = findViewById(R.id.txt_summa2_last);
        name_last_gone = findViewById(R.id.name_last_gone);
        lost_last_gone = findViewById(R.id.lost_last_gone);
        last_advance = findViewById(R.id.last_advance);
        txt_location.setSelected(true);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(LastActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        cancel_btn.setOnClickListener(v -> {
            Intent intent = new Intent(LastActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        uid_string = getIntent().getStringExtra("uid");
        txt_name.setText(getIntent().getStringExtra("name_stadium"));
        txt_location.setText(getIntent().getStringExtra("location"));
        txt_summa.setText(getIntent().getStringExtra("summa_stadium"));
        txt_date.setText(getIntent().getStringExtra("date2"));
        txt_time.setText(getIntent().getStringExtra("time2"));
        txt_date_gone.setText(getIntent().getStringExtra("dateE"));
        txt_time_gone.setText(getIntent().getStringExtra("time"));
        txt_summa2.setText(getIntent().getStringExtra("summa_stadium"));
        filter = getIntent().getStringExtra("filter");

        reference.child("order_arena").child("Football").child(uid_string)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("rasim1").exists()) {
                            String imageUrl = Objects.requireNonNull(snapshot.child("rasim1").getValue()).toString();
                            Picasso.get().load(imageUrl).into(image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        reference.child("admin_setting").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String summa = Objects.requireNonNull(snapshot.child("summa").getValue()).toString();
                        String summaX = summa + " " + "so'm";
                        last_advance.setText(summaX);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        pay_btn.setOnClickListener(v -> {
            String time_string = txt_time_gone.getText().toString();
            String time = txt_time.getText().toString();
            String date = txt_date_gone.getText().toString();
            Intent intent = new Intent(LastActivity.this, PayInfoActivity.class);
            intent.putExtra("time_string", time_string);
            intent.putExtra("time", time);
            intent.putExtra("date", date);
            intent.putExtra("uid_last", uid_string);
            intent.putExtra("filter", filter);
            intent.putExtra("location", txt_location.getText().toString());
            intent.putExtra("name", txt_name.getText().toString());
            startActivity(intent);
        });
    }
}