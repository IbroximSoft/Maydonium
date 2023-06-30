package uz.arena.stadium;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.more.AboutActivity;
import uz.arena.stadium.more.BookingActivity;
import uz.arena.stadium.more.FavoriteActivity;
import uz.arena.stadium.more.LanguageMoreActivity;
import uz.arena.stadium.more.NotificationActivity;
import uz.arena.stadium.more.PoliceActivity;
import uz.arena.stadium.more.ProfilActivity;

public class MoreActivity extends Appcompat {
    BottomNavigationView bottomNavigationView;
    FloatingActionButton actionButton;
    ListView listView;
    FirebaseAuth mAuth;
    ArrayList<String> arrayList = new ArrayList<>();
    int[] list_img =
            {R.drawable.more_notification, R.drawable.more_favorite,
                    R.drawable.more_booking, R.drawable.my_account, R.drawable.more_info,
                    R.drawable.more_upload, R.drawable.more_language,
                    R.drawable.more_done, R.drawable.more_logout};

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottomappbarMore);
        bottomNavigationView.setSelectedItemId(R.id.mSetting);
        actionButton = findViewById(R.id.floatingMore);
        listView = findViewById(R.id.list_more);

        arrayList.add(getString(R.string.notifications_more));
        arrayList.add(getString(R.string.saved_more));
        arrayList.add(getString(R.string.mybooking_more));
        arrayList.add(getString(R.string.myaccount_more));
        arrayList.add(getString(R.string.aboutus_more));
        arrayList.add(getString(R.string.share_more));
        arrayList.add(getString(R.string.change_language_more));
        arrayList.add(getString(R.string.police_and_security));
        arrayList.add(getString(R.string.logout_more));

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        actionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MoreActivity.this, BookingActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                startActivity(new Intent(MoreActivity.this, NotificationActivity.class));
                overridePendingTransition(0, 0);
            } else if (position == 1) {
                startActivity(new Intent(MoreActivity.this, FavoriteActivity.class));
                overridePendingTransition(0, 0);
            } else if (position == 2) {
                startActivity(new Intent(MoreActivity.this, BookingActivity.class));
                overridePendingTransition(0, 0);
            } else if (position == 3) {
                startActivity(new Intent(MoreActivity.this, ProfilActivity.class));
                overridePendingTransition(0, 0);
            } else if (position == 4) {
                startActivity(new Intent(MoreActivity.this, AboutActivity.class));
                overridePendingTransition(0, 0);
            } else if (position == 5) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String Body = "Download this App";
                String Sub = "https://play.google.com/store/apps/details?id=uz.arena.stadium";
                intent.putExtra(Intent.EXTRA_TEXT, Body);
                intent.putExtra(Intent.EXTRA_TEXT, Sub);
                startActivity(Intent.createChooser(intent, "Share using"));
            } else if (position == 6) {
                startActivity(new Intent(MoreActivity.this, LanguageMoreActivity.class));
                overridePendingTransition(0, 0);
            } else if (position == 7) {
                startActivity(new Intent(MoreActivity.this, PoliceActivity.class));
                overridePendingTransition(0, 0);
            } else if (position == 8) {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.log_out_item);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button button = dialog.findViewById(R.id.log_yes);
                Button button2 = dialog.findViewById(R.id.log_no);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth.signOut();
                        Intent loginIntent = new Intent(MoreActivity.this, LanguageActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        finish();
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.mHome:
                    Intent intent = new Intent(MoreActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    MoreActivity.this.finish();
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.mSearch:
                    startActivity(new Intent(getApplicationContext()
                            , SearchActivity.class));
                    finish();
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.mPerson:
                    startActivity(new Intent(getApplicationContext()
                            , CompetitionActivity.class));
                    finish();
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.mSetting:
                    return true;
            }
            return false;

        });
    }

    private class CustomAdapter extends BaseAdapter {
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
            @SuppressLint({"ViewHolder", "InflateParams"}) View view = getLayoutInflater().inflate(R.layout.more_item, null);
            TextView text = view.findViewById(R.id.more_item_text);
            ImageView img = view.findViewById(R.id.more_item_img);
            text.setText(arrayList.get(position));
            img.setImageResource(list_img[position]);
            return view;
        }
    }
}