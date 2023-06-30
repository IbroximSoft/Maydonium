package uz.arena.stadium.arena_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uz.arena.stadium.ArenaAdapter;
import uz.arena.stadium.OrderItem;
import uz.arena.stadium.R;

public class ArenaFootballFragment extends Fragment {
    Button btn_tennis, btn_bowling, btn_add;
    RecyclerView recyclerView;
    ArenaAdapter stadiumAdapter;
    TextView all, test_1, test_2, test;
    DatabaseReference reference;

    public ArenaFootballFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_football_arena, container, false);

        recyclerView = view.findViewById(R.id.arena_football_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        all = view.findViewById(R.id.football_arena_all);
        test_1 = view.findViewById(R.id.test_1);
        test_2 = view.findViewById(R.id.test_2);
        test = view.findViewById(R.id.test);

        all.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FootballAllActivity.class);
            startActivity(intent);
        });

        reference = FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<OrderItem> options =
                new FirebaseRecyclerOptions.Builder<OrderItem>()
                        .setQuery(reference.child("order_arena").child("Football"), OrderItem.class)
                        .build();
        Log.i("keldi",options.toString());
        stadiumAdapter = new ArenaAdapter(options);
        recyclerView.setAdapter(stadiumAdapter);

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
//        OnStart();
        stadiumAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        stadiumAdapter.stopListening();
    }
}