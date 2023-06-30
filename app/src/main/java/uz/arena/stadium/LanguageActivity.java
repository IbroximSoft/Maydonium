package uz.arena.stadium;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.language.LanguageManager;
import uz.arena.stadium.more.AboutActivity;
import uz.arena.stadium.more.FavoriteActivity;

public class LanguageActivity extends Appcompat {
    ImageView btn_next;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        auth = FirebaseAuth.getInstance();
        LinearLayout btn_uz = findViewById(R.id.line_uz);
        LinearLayout btn_ru = findViewById(R.id.line_ru);
        LinearLayout btn_eng = findViewById(R.id.line_eng);
        btn_next = findViewById(R.id.lan_next);

        LanguageManager lang = new LanguageManager(this);

        btn_uz.setOnClickListener(view ->
        {
            lang.updateResource("uz");
            recreate();
            ElseToMain();
        });

        btn_ru.setOnClickListener(view -> {
            lang.updateResource("ru");
            recreate();
            ElseToMain();
        });

        btn_eng.setOnClickListener(view -> {
            lang.updateResource("eng");
            recreate();
            ElseToMain();
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        OnStart();
    }

    private void OnStart(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            
        }else{
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent intent = new Intent(LanguageActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void ElseToMain() {
        Intent intent = new Intent(LanguageActivity.this, NumberActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}