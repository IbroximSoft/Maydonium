package uz.arena.stadium;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jbvincey.nestedradiobutton.NestedConstraintRadioGroup;
import com.jbvincey.nestedradiobutton.NestedRadioButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;
import uz.arena.stadium.language.Appcompat;

public class DetailsActivity extends Appcompat implements OnitemClcikListerner, OnMapReadyCallback {
    ArrayList<MyStory> myStories = new ArrayList<>();
    Uri uri;
    FirebaseAuth auth;
    DatabaseReference reference;
    ImageView back, img_card, img_card2, img_card3, img_card4, img_location_del, det_favorite, det_on_favorite;
    TextView name, bathroom, dimensions, summa, test_txt, calendar_txt, txt_calendar, radiotextid,
    txt_vaqt, location_det, latitude_txt, longitude_txt;
    EditText txt_tadio_time;
    ProgressBar progressBar, progressBar2, progressBar3, progressBar4;
    FloatingActionButton flo_btn;
    SupportMapFragment mapFragment;
    private GoogleMap map;
    EditText det_maskEdit;
    NestedConstraintRadioGroup football_radioGr;
    NestedRadioButton radio_1, radio_2, radio_3, radio_4, radio_5, radio_6, radio_7, radio_8, radio_9;

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_football_det);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        name = findViewById(R.id.arena_name_det);
        bathroom = findViewById(R.id.check1_det);
        dimensions = findViewById(R.id.dimensions_det);
        summa = findViewById(R.id.arena_summa_det);
        back = findViewById(R.id.det_back);
        flo_btn = findViewById(R.id.flo_det);

        img_card = findViewById(R.id.img_card_det);
        img_card2 = findViewById(R.id.img_card_det2);
        img_card3 = findViewById(R.id.img_card_det3);
        img_card4 = findViewById(R.id.img_card_det4);

        calendar_txt = findViewById(R.id.edit_date);
        txt_calendar = findViewById(R.id.txt_calendar);
        progressBar = findViewById(R.id.progress_det_card);
        progressBar2 = findViewById(R.id.progress_det_card2);
        progressBar3 = findViewById(R.id.progress_det_card3);
        progressBar4 = findViewById(R.id.progress_det_card4);

        test_txt = findViewById(R.id.test_txt);
        radiotextid = findViewById(R.id.radiotextid);
        txt_vaqt = findViewById(R.id.txt_vaqtYoq);
        location_det = findViewById(R.id.location_det);
        img_location_del = findViewById(R.id.img_location_del);
        latitude_txt = findViewById(R.id.latitude_det);
        longitude_txt = findViewById(R.id.longitude_det);
        txt_tadio_time = findViewById(R.id.txt_tadio_time);
        det_favorite = findViewById(R.id.det_favorite);
        det_on_favorite = findViewById(R.id.det_on_favorite);
        det_maskEdit = findViewById(R.id.det_maskEdit);

        String ism1 = txt_tadio_time.getText().toString();
        if (ism1.isEmpty()) {
            flo_btn.setOnClickListener(view -> Toasty.warning(DetailsActivity.this, getString(R.string.choose), Toasty.LENGTH_SHORT, true).show());
        }

        location_det.setSelected(true);
        Map<String, Object> Datas = (Map<String, Object>) getIntent().getSerializableExtra("datas");
        latitude_txt.setText(Objects.requireNonNull(Datas.get("latitude")).toString());
        longitude_txt.setText(Objects.requireNonNull(Datas.get("longitude")).toString());
        name.setText(Objects.requireNonNull(Datas.get("arena_name")).toString());
        dimensions.setText(Objects.requireNonNull(Datas.get("dimensions")).toString());
        summa.setText(Objects.requireNonNull(Datas.get("summa")).toString());
        test_txt.setText(Objects.requireNonNull(Datas.get("uid")).toString());
        location_det.setText(Objects.requireNonNull(Datas.get("location")).toString());
        String have_bathroom = Objects.requireNonNull(Datas.get("check_1")).toString();

        MyStory story1 = new MyStory(
                Objects.requireNonNull(Datas.get("rasim1")).toString());
        myStories.add(story1);
        MyStory story2 = new MyStory(
                Objects.requireNonNull(Datas.get("rasim2")).toString());
        myStories.add(story2);
        MyStory story3 = new MyStory(
                Objects.requireNonNull(Datas.get("rasim3")).toString());
        myStories.add(story3);
        MyStory story4 = new MyStory(
                Objects.requireNonNull(Datas.get("rasim4")).toString());
        myStories.add(story4);

        img_card.setOnClickListener(view -> {
            ImgData(Objects.requireNonNull(Datas.get("arena_name")));
        });

        img_card2.setOnClickListener(view -> {
            ImgData(Objects.requireNonNull(Datas.get("arena_name")));
        });

        img_card3.setOnClickListener(view -> {
            ImgData(Objects.requireNonNull(Datas.get("arena_name")));
        });

        img_card4.setOnClickListener(view -> {
            ImgData(Objects.requireNonNull(Datas.get("arena_name")));
        });

        if (have_bathroom.equals("1")) {
            bathroom.setText(R.string.extant);
        } else {
            bathroom.setText(R.string.no);
        }

        img_location_del.setOnClickListener(v -> {
            String latitude = latitude_txt.getText().toString();
            String longitude = longitude_txt.getText().toString();
            String https = "https://maps.google.com/maps?q=";
            String lat_long = latitude + "," + longitude;
            String https2 = "&hl=es&z=14&amp;output=embed";
            String vs = https + lat_long + https2;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Bizning maydon joylashuvi:");
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(vs));
            startActivity(Intent.createChooser(intent, "Joylashuvni ulashish:"));

        });

        football_radioGr = findViewById(R.id.football_radioGr);
        radio_1 = findViewById(R.id.football_radio1);
        radio_2 = findViewById(R.id.football_radio2);
        radio_3 = findViewById(R.id.football_radio3);
        radio_4 = findViewById(R.id.football_radio4);
        radio_5 = findViewById(R.id.football_radio5);
        radio_6 = findViewById(R.id.football_radio6);
        radio_7 = findViewById(R.id.football_radio7);
        radio_8 = findViewById(R.id.football_radio8);
        radio_9 = findViewById(R.id.football_radio9);

        txt_tadio_time.addTextChangedListener(textWatcher);

        football_radioGr.setOnCheckedChangeListener((groupManager, checkedId) -> {
            int selectedId = groupManager.getCheckedId();

            switch (selectedId) {
                case R.id.football_radio1:
                    radiotextid.setText("time");
                    txt_tadio_time.setText(radio_1.getText().toString());
                    break;
                case R.id.football_radio2:
                    radiotextid.setText("time2");
                    txt_tadio_time.setText(radio_2.getText().toString());
                    break;
                case R.id.football_radio3:
                    radiotextid.setText("time3");
                    txt_tadio_time.setText(radio_3.getText().toString());
                    break;
                case R.id.football_radio4:
                    radiotextid.setText("time4");
                    txt_tadio_time.setText(radio_4.getText().toString());
                    break;
                case R.id.football_radio5:
                    radiotextid.setText("time5");
                    txt_tadio_time.setText(radio_5.getText().toString());
                    break;
                case R.id.football_radio6:
                    radiotextid.setText("time6");
                    txt_tadio_time.setText(radio_6.getText().toString());
                    break;
                case R.id.football_radio7:
                    radiotextid.setText("time7");
                    txt_tadio_time.setText(radio_7.getText().toString());
                    break;
                case R.id.football_radio8:
                    radiotextid.setText("time8");
                    txt_tadio_time.setText(radio_8.getText().toString());
                    break;
                case R.id.football_radio9:
                    radiotextid.setText("time9");
                    txt_tadio_time.setText(radio_9.getText().toString());
                    break;
            }
        });


        txt_tadio_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String Text = txt_tadio_time.getText().toString();
                det_maskEdit.setText(Text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        calendar_txt.setText(date);

        String mDate = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault()).format(new Date());
        txt_calendar.setText(mDate);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar_txt.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    DetailsActivity.this, (view, year1, month1, day1) -> {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(year1, month1, day1);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat date1 = new SimpleDateFormat("dd.MM.yyyy");
                String dateString = dateFormat.format(calendar1.getTime());
                String dateS = date1.format(calendar1.getTime());
                calendar_txt.setText(dateS);
                txt_calendar.setText(dateString);
            }, day, month, year
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });


        uri = Uri.parse(Datas.get("rasim1").toString());

        Glide.with(this)
                .load(uri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        img_card.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(img_card);

        uri = Uri.parse(Datas.get("rasim2").toString());
        Glide.with(this)
                .load(uri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar2.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar2.setVisibility(View.GONE);
                        img_card2.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(img_card2);
        uri = Uri.parse(Datas.get("rasim3").toString());
        Picasso.get().load(uri).into(img_card3, new Callback() {
            @Override
            public void onSuccess() {
                progressBar3.setVisibility(View.GONE);
                img_card3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        uri = Uri.parse(Datas.get("rasim4").toString());
        Picasso.get().load(uri).into(img_card4, new Callback() {
            @Override
            public void onSuccess() {
                progressBar4.setVisibility(View.GONE);
                img_card4.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        det_favorite.setOnClickListener(v -> {
            FavoriteDatabase();
        });

        det_on_favorite.setOnClickListener(v -> {
            FavoriteDeleteDatabase();
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void ImgData(Object arena_name){
        new StoryView.Builder(getSupportFragmentManager())
                .setStoriesList(myStories)
                .setStoryDuration(10000)
                .setTitleText(arena_name.toString())
                .setSubtitleText("Maydon surati")
                .setTitleLogoUrl("https://firebasestorage.googleapis.com/v0/b/stadium-c39e8.appspot.com/o/template.png?alt=media&token=3820813f-6e64-40cf-9cf7-64f13f5b0e56")
                .setStoryClickListeners(new StoryClickListeners() {
                    @Override
                    public void onDescriptionClickListener(int position) {
                    }

                    @Override
                    public void onTitleIconClickListener(int position) {
                    }
                })
                .setStartingIndex(0)
                .build()
                .show();
    }

    private void FavoriteDeleteDatabase() {
        String currentUserID = auth.getCurrentUser().getUid();
        Map<String, Object> Datas = (Map<String, Object>) getIntent().getSerializableExtra("datas");
        String Uid = Datas.get("uid").toString();
        Log.i("shunda", Uid);

        reference.child("favorite_users").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child(Uid).getRef().removeValue();
                Toasty.success(DetailsActivity.this, "Yoqqanlar qatoridan olib tashlandi!", Toasty.LENGTH_SHORT, true).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FavoriteDatabase() {
        Map<String, Object> Datas = (Map<String, Object>) getIntent().getSerializableExtra("datas");
        String Location = Datas.get("location").toString();
        String Arena_name = Datas.get("arena_name").toString();
        String Rasim1 = Datas.get("rasim1").toString();
        String Rasim2 = Datas.get("rasim2").toString();
        String Rasim3 = Datas.get("rasim3").toString();
        String Rasim4 = Datas.get("rasim4").toString();
        String Check_1 = Datas.get("check_1").toString();
        String Check_2 = Datas.get("check_2").toString();
        String Choosing = Datas.get("choosing").toString();
        String Dimensions = Datas.get("dimensions").toString();
        String Summa = Datas.get("summa").toString();
        String Radio = Datas.get("radio").toString();
        String Uid = Datas.get("uid").toString();
        String Latitude = Datas.get("latitude").toString();
        String Longitude = Datas.get("longitude").toString();
        String currentUserID = auth.getCurrentUser().getUid();

        HashMap<String, String> malumotHas = new HashMap<>();
        malumotHas.put("location", Location);
        malumotHas.put("arena_name", Arena_name);
        malumotHas.put("rasim1", Rasim1);
        malumotHas.put("rasim2", Rasim2);
        malumotHas.put("rasim3", Rasim3);
        malumotHas.put("rasim4", Rasim4);
        malumotHas.put("check_1", Check_1);
        malumotHas.put("check_2", Check_2);
        malumotHas.put("choosing", Choosing);
        malumotHas.put("dimensions", Dimensions);
        malumotHas.put("summa", Summa);
        malumotHas.put("radio", Radio);
        malumotHas.put("uid", Uid);
        malumotHas.put("latitude", Latitude);
        malumotHas.put("longitude", Longitude);

        reference.child("favorite_users").child(currentUserID).child(Uid).setValue(malumotHas)
                .addOnCompleteListener(task -> {
                    det_on_favorite.setVisibility(View.VISIBLE);
                    det_favorite.setVisibility(View.GONE);
                    Toasty.success(DetailsActivity.this, "Yoqqanlar qatoriga qo'shildi!", Toasty.LENGTH_SHORT, true).show();
                });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String ism1 = txt_tadio_time.getText().toString();

            if (ism1.isEmpty()) {
                flo_btn.setOnClickListener(view -> Toasty.warning(DetailsActivity.this, "Bo'sh vaqtni tanlang",
                        Toasty.LENGTH_SHORT, true).show());
            } else {
                flo_btn.setOnClickListener(v -> {
                    String name_stadium = name.getText().toString();
                    String location = location_det.getText().toString();
                    String summa_stadium = summa.getText().toString();
                    String dateE = txt_calendar.getText().toString();
                    String time = radiotextid.getText().toString();
                    String date2 = calendar_txt.getText().toString();
                    String time2 = txt_tadio_time.getText().toString();
                    String uid = test_txt.getText().toString();
                    String Filters = det_maskEdit.getText().toString();
                    String Latitude_txt = latitude_txt.getText().toString();
                    String Longitude_txt = longitude_txt.getText().toString();
                    Intent intent = new Intent(DetailsActivity.this, LastActivity.class);
                    intent.putExtra("name_stadium", name_stadium);
                    intent.putExtra("location", location);
                    intent.putExtra("summa_stadium", summa_stadium);
                    intent.putExtra("dateE", dateE);
                    intent.putExtra("time", time);
                    intent.putExtra("date2", date2);
                    intent.putExtra("time2", time2);
                    intent.putExtra("uid", uid);
                    intent.putExtra("filter", Filters);
                    intent.putExtra("latitude", Latitude_txt);
                    intent.putExtra("longitude", Longitude_txt);
                    startActivity(intent);
                });
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        String currentUserID = auth.getCurrentUser().getUid();
        String uid = test_txt.getText().toString();
        reference.child("favorite_users").child(currentUserID).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("arena_name").getValue() == null) {
                    det_on_favorite.setVisibility(View.GONE);
                    det_favorite.setVisibility(View.VISIBLE);
                } else {
                    det_on_favorite.setVisibility(View.VISIBLE);
                    det_favorite.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String date = txt_calendar.getText().toString();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("order_arena")
                .child("Football");

        txt_calendar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String new_date = s.toString();
                reference.child(uid).child(new_date).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child("time").getValue() != null) {
                            String startTime1 = Objects.requireNonNull(snapshot.child("time").getValue()).toString();
                            radio_1.setVisibility(View.VISIBLE);
                            radio_1.setText(startTime1);
                            txt_vaqt.setVisibility(View.GONE);
                        } else {
                            radio_1.setVisibility(View.GONE);
                            txt_vaqt.setVisibility(View.VISIBLE);
                        }

                        if (snapshot.child("time2").getValue() != null) {
                            String startTime2 = Objects.requireNonNull(snapshot.child("time2").getValue()).toString();
                            radio_2.setVisibility(View.VISIBLE);
                            radio_2.setText(startTime2);
                            txt_vaqt.setVisibility(View.GONE);
                        } else {
                            radio_2.setVisibility(View.GONE);
                        }

                        if (snapshot.child("time3").getValue() != null) {
                            String startTime3 = Objects.requireNonNull(snapshot.child("time3").getValue()).toString();
                            radio_3.setVisibility(View.VISIBLE);
                            radio_3.setText(startTime3);
                            txt_vaqt.setVisibility(View.GONE);
                        } else {
                            radio_3.setVisibility(View.GONE);
                        }

                        if (snapshot.child("time4").getValue() != null) {
                            String startTime4 = Objects.requireNonNull(snapshot.child("time4").getValue()).toString();
                            radio_4.setVisibility(View.VISIBLE);
                            radio_4.setText(startTime4);
                            txt_vaqt.setVisibility(View.GONE);
                        } else {
                            radio_4.setVisibility(View.GONE);
                        }

                        if (snapshot.child("time5").getValue() != null) {
                            String startTime1 = Objects.requireNonNull(snapshot.child("time5").getValue()).toString();
                            radio_5.setVisibility(View.VISIBLE);
                            radio_5.setText(startTime1);
                            txt_vaqt.setVisibility(View.GONE);
                        } else {
                            radio_5.setVisibility(View.GONE);
                        }

                        if (snapshot.child("time6").getValue() != null) {
                            String startTime1 = Objects.requireNonNull(snapshot.child("time6").getValue()).toString();
                            radio_6.setVisibility(View.VISIBLE);
                            radio_6.setText(startTime1);
                            txt_vaqt.setVisibility(View.GONE);
                        } else {
                            radio_6.setVisibility(View.GONE);
                        }

                        if (snapshot.child("time7").getValue() != null) {
                            String startTime1 = Objects.requireNonNull(snapshot.child("time7").getValue()).toString();
                            radio_7.setVisibility(View.VISIBLE);
                            radio_7.setText(startTime1);
                            txt_vaqt.setVisibility(View.GONE);
                        } else {
                            radio_7.setVisibility(View.GONE);
                        }

                        if (snapshot.child("time8").getValue() != null) {
                            String startTime1 = Objects.requireNonNull(snapshot.child("time8").getValue()).toString();
                            radio_8.setVisibility(View.VISIBLE);
                            radio_8.setText(startTime1);
                            txt_vaqt.setVisibility(View.GONE);
                        } else {
                            radio_8.setVisibility(View.GONE);
                        }

                        if (snapshot.child("time9").getValue() != null) {
                            String startTime1 = Objects.requireNonNull(snapshot.child("time9").getValue()).toString();
                            radio_9.setVisibility(View.VISIBLE);
                            radio_9.setText(startTime1);
                            txt_vaqt.setVisibility(View.GONE);
                        } else {
                            radio_9.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reference.child(uid).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("time").getValue() != null) {
                    String startTime1 = Objects.requireNonNull(snapshot.child("time").getValue()).toString();
                    radio_1.setVisibility(View.VISIBLE);
                    radio_1.setText(startTime1);
                    txt_vaqt.setVisibility(View.GONE);
                } else {
                    radio_1.setVisibility(View.GONE);
                    txt_vaqt.setVisibility(View.VISIBLE);
                }

                if (snapshot.child("time2").getValue() != null) {
                    String startTime2 = Objects.requireNonNull(snapshot.child("time2").getValue()).toString();
                    radio_2.setVisibility(View.VISIBLE);
                    radio_2.setText(startTime2);
                    txt_vaqt.setVisibility(View.GONE);
                } else {
                    radio_2.setVisibility(View.GONE);
                }

                if (snapshot.child("time3").getValue() != null) {
                    String startTime3 = Objects.requireNonNull(snapshot.child("time3").getValue()).toString();
                    radio_3.setVisibility(View.VISIBLE);
                    radio_3.setText(startTime3);
                    txt_vaqt.setVisibility(View.GONE);
                } else {
                    radio_3.setVisibility(View.GONE);
                }

                if (snapshot.child("time4").getValue() != null) {
                    String startTime4 = Objects.requireNonNull(snapshot.child("time4").getValue()).toString();
                    radio_4.setVisibility(View.VISIBLE);
                    radio_4.setText(startTime4);
                    txt_vaqt.setVisibility(View.GONE);
                } else {
                    radio_4.setVisibility(View.GONE);
                }

                if (snapshot.child("time5").getValue() != null) {
                    String startTime1 = Objects.requireNonNull(snapshot.child("time5").getValue()).toString();
                    radio_5.setVisibility(View.VISIBLE);
                    radio_5.setText(startTime1);
                    txt_vaqt.setVisibility(View.GONE);
                } else {
                    radio_5.setVisibility(View.GONE);
                }

                if (snapshot.child("time6").getValue() != null) {
                    String startTime1 = Objects.requireNonNull(snapshot.child("time6").getValue()).toString();
                    radio_6.setVisibility(View.VISIBLE);
                    radio_6.setText(startTime1);
                    txt_vaqt.setVisibility(View.GONE);
                } else {
                    radio_6.setVisibility(View.GONE);
                }

                if (snapshot.child("time7").getValue() != null) {
                    String startTime1 = Objects.requireNonNull(snapshot.child("time7").getValue()).toString();
                    radio_7.setVisibility(View.VISIBLE);
                    radio_7.setText(startTime1);
                    txt_vaqt.setVisibility(View.GONE);
                } else {
                    radio_7.setVisibility(View.GONE);
                }

                if (snapshot.child("time8").getValue() != null) {
                    String startTime1 = Objects.requireNonNull(snapshot.child("time8").getValue()).toString();
                    radio_8.setVisibility(View.VISIBLE);
                    radio_8.setText(startTime1);
                    txt_vaqt.setVisibility(View.GONE);
                } else {
                    radio_8.setVisibility(View.GONE);
                }

                if (snapshot.child("time9").getValue() != null) {
                    String startTime1 = Objects.requireNonNull(snapshot.child("time9").getValue()).toString();
                    radio_9.setVisibility(View.VISIBLE);
                    radio_9.setText(startTime1);
                    txt_vaqt.setVisibility(View.GONE);
                } else {
                    radio_9.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void radioVisible() {
        if (radio_1.getVisibility() == View.VISIBLE || radio_2.getVisibility() == View.VISIBLE || radio_3.getVisibility() == View.VISIBLE
                || radio_4.getVisibility() == View.VISIBLE || radio_5.getVisibility() == View.VISIBLE || radio_6.getVisibility() == View.VISIBLE
                || radio_7.getVisibility() == View.VISIBLE || radio_8.getVisibility() == View.VISIBLE) {
            txt_vaqt.setVisibility(View.GONE);
        } else {
            txt_vaqt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        map.setMinZoomPreference(11.0f);


        reference.child("order_arena").child("Football")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String LatString = latitude_txt.getText().toString();
                        double Lat = Double.parseDouble(LatString);
                        String LongString = longitude_txt.getText().toString();
                        double Long = Double.parseDouble(LongString);

                        LatLng position = new LatLng(Lat, Long);

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14.5f));

                        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_location);
                        Bitmap b = bitmapdraw.getBitmap();
                        int height = 120;
                        int width = 100;
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(position)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                        Objects.requireNonNull(marker).setTag(position);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(this, "position:" + position, Toast.LENGTH_SHORT).show();
    }
}