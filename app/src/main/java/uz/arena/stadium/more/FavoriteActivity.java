package uz.arena.stadium.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import uz.arena.stadium.FavoriteAdapter;
import uz.arena.stadium.MoreActivity;
import uz.arena.stadium.OrderItem;
import uz.arena.stadium.R;
import uz.arena.stadium.language.Appcompat;

public class FavoriteActivity extends Appcompat {
    FirebaseAuth mAuth;
    DatabaseReference RootRef;
    RecyclerView recyclerview;
    FavoriteAdapter stadiumAdapter;
    TextView booking_txt;
    ImageView img_on_info_book, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        recyclerview = findViewById(R.id.recyclerview_favorite);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        img_on_info_book = findViewById(R.id.img_on_info_favorite);
        booking_txt = findViewById(R.id.txt_on_info_favorite);
        back = findViewById(R.id.favorite_back);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(FavoriteActivity.this, MoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            FavoriteActivity.this.finish();
            overridePendingTransition(0, 0);
        });

        String currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        RootRef.child("favorite_users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() ==null) {
                    booking_txt.setVisibility(View.VISIBLE);
                    img_on_info_book.setVisibility(View.VISIBLE);
                } else {
                    booking_txt.setVisibility(View.GONE);
                    img_on_info_book.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("favorite_users").child(currentUserID);

        FirebaseRecyclerOptions<OrderItem> options =
                new FirebaseRecyclerOptions.Builder<OrderItem>()
                        .setQuery(reference, OrderItem.class)
                        .build();
        stadiumAdapter = new FavoriteAdapter(options);
        recyclerview.setAdapter(stadiumAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        stadiumAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        stadiumAdapter.stopListening();
    }
}