package uz.arena.stadium;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import uz.arena.stadium.language.Appcompat;

public class WaitActivity extends Appcompat {
    FirebaseAuth auth;
    DatabaseReference reference;
    TextView DateE, time, st_name, location, uid, date_book, item_st_uid, wait_st_uidd, wait_timeGone;
    ImageView img, img_location_wait;
    String latitude, longitude;
    String DateString, Uid;
    CardView card_wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        DateE = findViewById(R.id.wait_date);
        time = findViewById(R.id.wait_time);
        st_name = findViewById(R.id.wait_stName);
        location = findViewById(R.id.wait_location);
        uid = findViewById(R.id.wait_uid);
        date_book = findViewById(R.id.wait_date_book);
        item_st_uid = findViewById(R.id.wait_st_uid);
        img = findViewById(R.id.wait_img);
        card_wait = findViewById(R.id.card_wait);
        wait_st_uidd = findViewById(R.id.wait_st_uidd);
        wait_timeGone = findViewById(R.id.wait_timeGone);
        img_location_wait = findViewById(R.id.img_location_wait);

        String currentUserID = auth.getCurrentUser().getUid();
        reference.child("date_time").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String date = snapshot.child("dateGone").getValue().toString();
                String St_uid = snapshot.child("st_uid").getValue().toString();
                date_book.setText(date);
                item_st_uid.setText(St_uid);
                Uid = item_st_uid.getText().toString();
                DateString = date_book.getText().toString();
                Database();


                String currentUserID = auth.getCurrentUser().getUid();

                reference.child("order_status").child(currentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String status = Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                        switch (status) {
                            case "two":
                                Intent intent = new Intent(WaitActivity.this, Wait2Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                break;

                            case "block":
                                Intent intentBlock = new Intent(WaitActivity.this, BlockActivity.class);
                                intentBlock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentBlock);
                                finish();
                                break;

                            case "success": {
                                Intent intentTwo = new Intent(WaitActivity.this, HomeActivity.class);
                                intentTwo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentTwo);
                                finish();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                reference.child("order_users").child(Uid).child(DateString).child(currentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        latitude = snapshot.child("latitude").getValue().toString();
                        longitude = snapshot.child("longitude").getValue().toString();
                        if (snapshot.child("status").exists()) {
                            String status = Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                            switch (status) {
                                case "two":
                                    Intent intent = new Intent(WaitActivity.this, Wait2Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    break;

                                case "success": {
                                    Intent intentTwo = new Intent(WaitActivity.this, HomeActivity.class);
                                    intentTwo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intentTwo);
                                    finish();
                                    break;
                                }
                            }

                        }

                        String Uid_st = snapshot.child("st_uid").getValue().toString();
                        String date = snapshot.child("dateGone").getValue().toString();
                        wait_st_uidd.setText(Uid_st);
                        wait_timeGone.setText(date);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

    private void Database() {
        String currentUserID = auth.getCurrentUser().getUid();
        reference.child("order_users").child(Uid).child(DateString).child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("st_name").getValue().toString();
                String Location = snapshot.child("location").getValue().toString();
                String date = snapshot.child("date").getValue().toString();
                String Time = snapshot.child("time").getValue().toString();
                st_name.setText(name);
                location.setText(Location);
                DateE.setText(date);
                time.setText(Time);

                card_wait.setOnClickListener(v -> {
                    String Uid = wait_st_uidd.getText().toString();
                    String St_name = st_name.getText().toString();
                    String DateGone = wait_timeGone.getText().toString();
                    String Date = DateE.getText().toString();
                    String Locations = location.getText().toString();
                    Intent intent = new Intent(WaitActivity.this, TwoWaitActivity.class);
                    intent.putExtra("st_uid", Uid);
                    intent.putExtra("St_name", St_name);
                    intent.putExtra("date_gone", DateGone);
                    intent.putExtra("date", Date);
                    intent.putExtra("location", Locations);
                    startActivity(intent);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}