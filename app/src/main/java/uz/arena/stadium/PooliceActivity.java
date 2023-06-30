package uz.arena.stadium;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.more.PoliceActivity;

public class PooliceActivity extends Appcompat {
    ImageView police_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poolice);

        police_back = findViewById(R.id.poolice_back);

        police_back.setOnClickListener(v -> {
            Intent intent = new Intent(PooliceActivity.this, InfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            PooliceActivity.this.finish();
            overridePendingTransition(0, 0);
        });

    }
}