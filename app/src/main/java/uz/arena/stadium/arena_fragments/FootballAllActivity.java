package uz.arena.stadium.arena_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uz.arena.stadium.ArenaAllAdapter;
import uz.arena.stadium.MoreActivity;
import uz.arena.stadium.OrderAdapter;
import uz.arena.stadium.OrderItem;
import uz.arena.stadium.R;
import uz.arena.stadium.SearchActivity;
import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.more.AboutActivity;

public class FootballAllActivity extends Appcompat {
    RecyclerView recyclerView;
    ArenaAllAdapter stadiumAdapter;
    ImageView football_all_back;
    EditText edit_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_all);

        recyclerView = findViewById(R.id.rec_allFootball);
        football_all_back = findViewById(R.id.football_all_back);
        edit_search = findViewById(R.id.edit_allFootball);

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String chars = charSequence.toString();
                FirebaseData(chars);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order_arena").child("Football");

        FirebaseRecyclerOptions<OrderItem> options =
                new FirebaseRecyclerOptions.Builder<OrderItem>()
                        .setQuery(reference, OrderItem.class)
                        .build();
        stadiumAdapter = new ArenaAllAdapter(options);
        recyclerView.setAdapter(stadiumAdapter);

        football_all_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FootballAllActivity.this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                FootballAllActivity.this.finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    private void FirebaseData(String newText){

        FirebaseRecyclerOptions<OrderItem> options =
                new FirebaseRecyclerOptions.Builder<OrderItem>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("order_arena").child("Football").orderByChild("arena_name").startAt(newText.toUpperCase()).endAt(newText.toLowerCase()+"\uf8ff"), OrderItem.class)
                        .build();

        stadiumAdapter = new ArenaAllAdapter(options);
        stadiumAdapter.startListening();
        recyclerView.setAdapter(stadiumAdapter);

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