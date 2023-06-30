package uz.arena.stadium.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import uz.arena.stadium.MoreActivity;
import uz.arena.stadium.R;
import uz.arena.stadium.language.Appcompat;

public class NotificationActivity extends Appcompat {
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        back = findViewById(R.id.notification_back);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, MoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            NotificationActivity.this.finish();
            overridePendingTransition(0, 0);
        });
    }
}