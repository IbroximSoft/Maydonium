package uz.arena.stadium.fragment_top_menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uz.arena.stadium.R;

public class BowlingFragment extends Fragment {
    Button btn_football, btn_tennis, btn_bowling, btn_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bowling, container, false);

        btn_football = view.findViewById(R.id.btn_football_home3);
        btn_tennis = view.findViewById(R.id.btn_tennis_home3);
        btn_bowling = view.findViewById(R.id.btn_bowling_home3);
        btn_add = view.findViewById(R.id.btn_add_home3);

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

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_home, new AddFragment())
                        .commit();
            }
        });

        return view;
    }
}