package uz.arena.stadium.fragment_top_menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uz.arena.stadium.R;

public class AddFragment extends Fragment {
    Button btn_football, btn_tennis, btn_bowling, btn_add;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add, container, false);
        btn_football = view.findViewById(R.id.btn_football_home4);
        btn_tennis = view.findViewById(R.id.btn_tennis_home4);
        btn_bowling = view.findViewById(R.id.btn_bowling_home4);
        btn_add = view.findViewById(R.id.btn_add_home4);

        btn_football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_home, new HomeFragment())
                        .commit();
            }
        });

        btn_tennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_home, new TennisFragment())
                        .commit();
            }
        });

        btn_bowling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_home, new BowlingFragment())
                        .commit();
            }
        });

        return view;
    }
}