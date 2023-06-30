package uz.arena.stadium.more;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import uz.arena.stadium.HomeActivity;
import uz.arena.stadium.InfoActivity;
import uz.arena.stadium.MoreActivity;
import uz.arena.stadium.R;
import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.language.LanguageManager;

public class LanguageMoreActivity extends Appcompat {

    ListView language_list;
    TextView language_more_txt;
    ImageView lang_more_icon, language_back;
    String[] list_text = {"O‘zbekcha", "Русский", "English"};
    int[] list_img =
            {R.drawable.uzb_language, R.drawable.russia_language,
                    R.drawable.america_language};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_more);

        language_list = findViewById(R.id.language_list);
        LanguageAdapter customAdapter = new LanguageAdapter();
        language_list.setAdapter(customAdapter);
        LanguageManager lang = new LanguageManager(this);
        language_more_txt = findViewById(R.id.language_more_txt);
        language_back = findViewById(R.id.language_back);

        language_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LanguageMoreActivity.this, MoreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                LanguageMoreActivity.this.finish();
                overridePendingTransition(0, 0);
            }
        });

        language_list.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                lang.updateResource("uz");
                recreate();
                startActivity(new Intent(LanguageMoreActivity.this, MoreActivity.class));
                overridePendingTransition(0, 0);
            } else if (position == 1) {
                lang.updateResource("ru");
                recreate();
                startActivity(new Intent(LanguageMoreActivity.this, MoreActivity.class));
                overridePendingTransition(0, 0);
            } else if (position == 2) {
                lang.updateResource("eng");
                recreate();
                startActivity(new Intent(LanguageMoreActivity.this, MoreActivity.class));
                overridePendingTransition(0, 0);
            }
        });
    }

    private class LanguageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list_img.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint({"ViewHolder", "InflateParams"}) View view = getLayoutInflater().inflate(R.layout.language_item, null);
            TextView text = view.findViewById(R.id.language_text);
            ImageView img = view.findViewById(R.id.language_img);
            lang_more_icon = view.findViewById(R.id.lang_more_icon);
            text.setText(list_text[position]);
            img.setImageResource(list_img[position]);

            String txt_test = language_more_txt.getText().toString();

            if ((position == 0)) {

                if (txt_test.equals("Til")) {
                    lang_more_icon.setVisibility(View.VISIBLE);
                }

            } else if (position == 1) {
                if (txt_test.equals("Язык")) {
                    lang_more_icon.setVisibility(View.VISIBLE);
                }

            } else if (position == 2) {
                if (txt_test.equals("Language")) {
                    lang_more_icon.setVisibility(View.VISIBLE);
                }
            }

            return view;
        }
    }
}