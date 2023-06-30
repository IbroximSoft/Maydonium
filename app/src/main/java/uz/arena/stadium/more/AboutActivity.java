package uz.arena.stadium.more;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import uz.arena.stadium.MoreActivity;
import uz.arena.stadium.R;
import uz.arena.stadium.language.Appcompat;

public class AboutActivity extends Appcompat {
    ImageView back, facebook, instagram, telegram;
    TextView number_about, gmail_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        back = findViewById(R.id.about_back);
        facebook = findViewById(R.id.about_facebook);
        instagram = findViewById(R.id.about_instagram);
        telegram = findViewById(R.id.about_telegram);
        number_about = findViewById(R.id.number_about);
        gmail_about = findViewById(R.id.gmail_about);

        gmail_about.setOnClickListener(v -> {
            Intent emailintent = new Intent(Intent.ACTION_SEND);
            emailintent.setType("plain/text");
            emailintent.putExtra(Intent.EXTRA_EMAIL,new String[] {"ekstremal95@gmail.com" });
            emailintent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailintent.putExtra(Intent.EXTRA_TEXT,"");
            startActivity(Intent.createChooser(emailintent, "Pochta yuborish..."));
        });

        number_about.setMovementMethod(new LinkMovementMethod());
        gmail_about.setMovementMethod(new LinkMovementMethod());

        back.setOnClickListener(v -> {
            Intent intent = new Intent(AboutActivity.this, MoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            AboutActivity.this.finish();
            overridePendingTransition(0, 0);
        });

        facebook.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://fb.com/maydonium");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });

        instagram.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://istagram.com/maydonium");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });

        telegram.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://t.me/Maydonium");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });
    }
}