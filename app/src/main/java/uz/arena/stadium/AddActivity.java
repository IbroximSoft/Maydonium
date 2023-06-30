package uz.arena.stadium;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        textView = findViewById(R.id.add_text);

        String Key = getIntent().getStringExtra("key");
        textView.setText(Key);
    }
}