package uz.arena.stadium;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import uz.arena.stadium.language.Appcompat;

public class Wait2Activity extends Appcompat {
    FirebaseAuth auth;
    DatabaseReference reference;
    TextView wait2_name, wait2_location, wait2_date, wait2_time,
            wait2_name2, wait2_location2, wait2_date2, wait2_time2,
            wait2_st_uid, wait2_dateGone, location_wait;
    String latitude, longitude;
    ImageView img_location_wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait2);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        wait2_name = findViewById(R.id.wait2_name);
        wait2_location = findViewById(R.id.wait2_location);
        wait2_date = findViewById(R.id.wait2_date);
        wait2_time = findViewById(R.id.wait2_time);
        wait2_name2 = findViewById(R.id.wait2_name2);
        wait2_location2 = findViewById(R.id.wait2_location2);
        wait2_date2 = findViewById(R.id.wait2_date2);
        wait2_time2 = findViewById(R.id.wait2_time2);
        wait2_st_uid = findViewById(R.id.wait2_st_uid);
        wait2_dateGone = findViewById(R.id.wait2_dateGone);
        img_location_wait = findViewById(R.id.img_location_wait2);

        String currentUserID = auth.getCurrentUser().getUid();
        reference.child("date_time").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Uid = snapshot.child("st_uid").getValue().toString();
                String Date = snapshot.child("dateGone").getValue().toString();
                wait2_st_uid.setText(Uid);
                wait2_dateGone.setText(Date);
                Database();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("order_status").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("status").exists()) {
                            String status = Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                            switch (status) {
                                case "wait":
                                    Intent intent = new Intent(Wait2Activity.this, WaitActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    break;

                                case "block":
                                    Intent intentBlock = new Intent(Wait2Activity.this, BlockActivity.class);
                                    intentBlock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intentBlock);
                                    finish();
                                    break;

                                case "success": {
                                    Intent intentTwo = new Intent(Wait2Activity.this, HomeActivity.class);
                                    intentTwo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intentTwo);
                                    finish();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void Database() {
        String Uid = wait2_st_uid.getText().toString();
        String Date = wait2_dateGone.getText().toString();
        String currentUserID = auth.getCurrentUser().getUid();
        reference.child("order_users").child(Uid).child(Date).child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                latitude = snapshot.child("latitude").getValue().toString();
                longitude = snapshot.child("longitude").getValue().toString();
                String name = snapshot.child("st_name").getValue().toString();
                String location = snapshot.child("location").getValue().toString();
                String date = snapshot.child("date").getValue().toString();
                String time = snapshot.child("time").getValue().toString();
                String time2 = snapshot.child("2").child("time").getValue().toString();
                wait2_name.setText(name);
                wait2_location.setText(location);
                wait2_date.setText(date);
                wait2_time.setText(time);
                wait2_name2.setText(name);
                wait2_location2.setText(location);
                wait2_date2.setText(date);
                wait2_time2.setText(time2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        img_location_wait.setOnClickListener(view -> {
            String https = "https://maps.google.com/maps?q=";
            String lat_long = latitude + "," + longitude;
            String https2 = "&hl=es&z=14&amp;output=embed";
            String vs = https + lat_long + https2;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Bizning maydon joylashuvi:");
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(vs));
            startActivity(Intent.createChooser(intent, "Joylashuvni ulashish:"));
        });
    }
}