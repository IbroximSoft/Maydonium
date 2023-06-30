package uz.arena.stadium;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import uz.arena.stadium.arena_fragments.ArenaFootballFragment;
import uz.arena.stadium.fragment_top_menu.HomeFragment;
import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.more.BookingActivity;

public class SearchActivity extends Appcompat {
    FirebaseAuth auth;
    DatabaseReference reference;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton actionButton;
    TextView Name, Number;
    ImageView search_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        actionButton = findViewById(R.id.floating3);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomappbar3);
        bottomNavigationView.setSelectedItemId(R.id.mSearch);
        Name = findViewById(R.id.name_search);
        Number = findViewById(R.id.number_search);
        search_img = findViewById(R.id.search_img);

        actionButton.setOnClickListener(view -> {
            Intent intent = new Intent(SearchActivity.this, BookingActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home3, new ArenaFootballFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.mHome:
                        startActivity(new Intent(getApplicationContext()
                                ,HomeActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.mSearch:
                        return false;

                    case R.id.mPerson:
                        startActivity(new Intent(getApplicationContext()
                                , CompetitionActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.mSetting:
                        startActivity(new Intent(getApplicationContext()
                                , MoreActivity.class));
                        overridePendingTransition(0, 0);
                        return false;
                }
                return false;

            }
        });

        String currentUserID = auth.getCurrentUser().getUid();
        reference.child("User").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String lost = snapshot.child("lost").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String vs = name + " " + lost;
                Name.setText(vs);
                Number.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("User_img").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("rasim").getValue() !=null){
                    String name = Objects.requireNonNull(snapshot.child("rasim").getValue()).toString();
                    Picasso.get().load(name).into(search_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}