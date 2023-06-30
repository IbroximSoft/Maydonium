package uz.arena.stadium.more;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import uz.arena.stadium.MoreActivity;
import uz.arena.stadium.R;
import uz.arena.stadium.language.Appcompat;

public class PoliceActivity extends Appcompat {
    ImageView police_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police);

        police_back = findViewById(R.id.police_back);

        police_back.setOnClickListener(v -> {
            Intent intent = new Intent(PoliceActivity.this, MoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            PoliceActivity.this.finish();
            overridePendingTransition(0, 0);
        });
    }
}