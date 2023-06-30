package uz.arena.stadium;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jbvincey.nestedradiobutton.NestedConstraintRadioGroup;
import com.jbvincey.nestedradiobutton.NestedRadioButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.more.AboutActivity;

public class TwoWaitActivity extends Appcompat implements OnMapReadyCallback {
    Uri uri;
    FirebaseAuth auth;
    DatabaseReference reference;
    ImageView back, img_location_del;
    TextView name, summa, test_txt, calendar_txt, txt_calendar, radiotextid,
            txt_vaqt, location_det, dateGone_two_wait, longitude_txt, two_st_uid;
    EditText txt_tadio_time, det_maskEditTwo;
    Button btn_det, det_btn_off;
    SupportMapFragment mapFragment;
    private GoogleMap map;
    NestedConstraintRadioGroup football_radioGr;
    NestedRadioButton radio_1, radio_2, radio_3, radio_4, radio_5, radio_6, radio_7, radio_8, radio_9;

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_wait);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_football_two_wait);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        name = findViewById(R.id.arena_name_two_wait);
        btn_det = findViewById(R.id.two_wait_btn);
        det_btn_off = findViewById(R.id.two_wait_btn_off);
        summa = findViewById(R.id.arena_summa_two_wait);
        back = findViewById(R.id.two_wait_back);
        calendar_txt = findViewById(R.id.edit_date_two_wait);
        txt_calendar = findViewById(R.id.txt_calendar_two_wait);
        test_txt = findViewById(R.id.test_txt_two_wait);
        radiotextid = findViewById(R.id.radiotextid_two_wait);
        txt_vaqt = findViewById(R.id.txt_vaqtYoq_two_wait);
        location_det = findViewById(R.id.location_two_wait);
        img_location_del = findViewById(R.id.img_location_two_wait);
        dateGone_two_wait = findViewById(R.id.dateGone_two_wait);
        longitude_txt = findViewById(R.id.longitude_two_wait);
        txt_tadio_time = findViewById(R.id.txt_tadio_time_two_wait);
        two_st_uid = findViewById(R.id.two_st_uid);
        det_maskEditTwo = findViewById(R.id.det_maskEditTwo);

        two_st_uid.setText(getIntent().getStringExtra("st_uid"));
        test_txt.setText(getIntent().getStringExtra("st_uid"));
        name.setText(getIntent().getStringExtra("St_name"));
        calendar_txt.setText(getIntent().getStringExtra("date"));
        dateGone_two_wait.setText(getIntent().getStringExtra("date_gone"));
        location_det.setText(getIntent().getStringExtra("location"));

        det_btn_off.setOnClickListener(v ->
                Toasty.warning(TwoWaitActivity.this, "Bo'sh vaqtni tanlang",
                        Toasty.LENGTH_SHORT, true).show());

        location_det.setSelected(true);

        football_radioGr = findViewById(R.id.football_radioGr_two_wait);
        radio_1 = findViewById(R.id.football_radio1_two_wait);
        radio_2 = findViewById(R.id.football_radio2_two_wait);
        radio_3 = findViewById(R.id.football_radio3_two_wait);
        radio_4 = findViewById(R.id.football_radio4_two_wait);
        radio_5 = findViewById(R.id.football_radio5_two_wait);
        radio_6 = findViewById(R.id.football_radio6_two_wait);
        radio_7 = findViewById(R.id.football_radio7_two_wait);
        radio_8 = findViewById(R.id.football_radio8_two_wait);
        radio_9 = findViewById(R.id.football_radio9_two_wait);

        txt_tadio_time.addTextChangedListener(textWatcher);

        football_radioGr.setOnCheckedChangeListener((groupManager, checkedId) -> {
            int selectedId = groupManager.getCheckedId();

            switch (selectedId) {
                case R.id.football_radio1_two_wait:
                    radiotextid.setText("time");
                    txt_tadio_time.setText(radio_1.getText().toString());
                    break;
                case R.id.football_radio2_two_wait:
                    radiotextid.setText("time2");
                    txt_tadio_time.setText(radio_2.getText().toString());
                    break;
                case R.id.football_radio3_two_wait:
                    radiotextid.setText("time3");
                    txt_tadio_time.setText(radio_3.getText().toString());
                    break;
                case R.id.football_radio4_two_wait:
                    radiotextid.setText("time4");
                    txt_tadio_time.setText(radio_4.getText().toString());
                    break;
                case R.id.football_radio5_two_wait:
                    radiotextid.setText("time5");
                    txt_tadio_time.setText(radio_5.getText().toString());
                    break;
                case R.id.football_radio6_two_wait:
                    radiotextid.setText("time6");
                    txt_tadio_time.setText(radio_6.getText().toString());
                    break;
                case R.id.football_radio7_two_wait:
                    radiotextid.setText("time7");
                    txt_tadio_time.setText(radio_7.getText().toString());
                    break;
                case R.id.football_radio8_two_wait:
                    radiotextid.setText("time8");
                    txt_tadio_time.setText(radio_8.getText().toString());
                    break;
                case R.id.football_radio9_two_wait:
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
                det_maskEditTwo.setText(Text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        String mDate = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault()).format(new Date());
        txt_calendar.setText(mDate);

        btn_det.setOnClickListener(v -> {
            WaitGo();
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(TwoWaitActivity.this, WaitActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void DatabaseSet(){
        String Radiotextid = txt_tadio_time.getText().toString();
        String Uid = two_st_uid.getText().toString();
        String DateGone = dateGone_two_wait.getText().toString();
        String currentUserID = auth.getCurrentUser().getUid();
        String two = "two";

        HashMap<String, String> malumotHas = new HashMap<>();
        malumotHas.put("time", Radiotextid);
        malumotHas.put("status", two);

        reference.child("order_users").child(Uid).child(DateGone).child(currentUserID).child("2").setValue(malumotHas)
                .addOnCompleteListener(task -> {

                });

        HashMap<String, String> malumotHas2 = new HashMap<>();
        malumotHas2.put("status", two);
        reference.child("order_status").child(currentUserID).setValue(malumotHas2)
                .addOnCompleteListener(task -> {
                });

        Intent intent = new Intent(TwoWaitActivity.this, Wait2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        TwoWaitActivity.this.finish();
        overridePendingTransition(0, 0);
    }

    private void WaitGo() {
        String uid = two_st_uid.getText().toString();
        String time_string = radiotextid.getText().toString();
        String time = txt_tadio_time.getText().toString();
        String date = dateGone_two_wait.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("order_arena");
        Query dummyQuery = ref
                .child("Football")
                .child(uid)
                .orderByChild(time_string)
                .equalTo(time);

        dummyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dummySnapshot : dataSnapshot.getChildren()) {
                    if (dummySnapshot.getKey().equals(date)) {
                        dummySnapshot.child(time_string).getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        DatabaseSet();

        String Uid = two_st_uid.getText().toString();
        String DateGone = dateGone_two_wait.getText().toString();
        String currentUserID = auth.getCurrentUser().getUid();
        String filter = det_maskEditTwo.getText().toString();

        reference.child("order_users").child(Uid).child(DateGone).child(currentUserID).child("status").setValue("two");
        reference.child("order_filter").child("Football").child(date).child(uid).child(filter).getRef().removeValue();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;

        map.setMinZoomPreference(11.0f);

        String Uid = two_st_uid.getText().toString();
        reference.child("order_arena").child("Football").child(Uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double Lat = Double.parseDouble(Objects.requireNonNull(snapshot.child("latitude").getValue()).toString());
                        double Long = Double.parseDouble(Objects.requireNonNull(snapshot.child("longitude").getValue()).toString());
                        String Summa = snapshot.child("summa").getValue().toString();
                        summa.setText(Summa);
                        LatLng position = new LatLng(Lat, Long);

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14.5f));

                        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.det_stadium_icon);
                        Bitmap b = bitmapdraw.getBitmap();
                        int height = 180;
                        int width = 160;
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

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String ism1 = txt_tadio_time.getText().toString();

            if (ism1.isEmpty()) {
                det_btn_off.setVisibility(View.VISIBLE);
                btn_det.setVisibility(View.GONE);
            } else {
                det_btn_off.setVisibility(View.GONE);
                btn_det.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        String uid = test_txt.getText().toString();
        String date = dateGone_two_wait.getText().toString();

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
                        if (snapshot.getValue() != null) {
                            txt_vaqt.setVisibility(View.GONE);
                            if (snapshot.child("time").getValue() != null) {
                                String startTime1 = Objects.requireNonNull(snapshot.child("time").getValue()).toString();
                                radio_1.setVisibility(View.VISIBLE);
                                radio_1.setText(startTime1);
                            } else {
                                radio_1.setVisibility(View.GONE);
                            }
                            if (snapshot.child("time2").getValue() != null) {
                                String startTime1 = Objects.requireNonNull(snapshot.child("time2").getValue()).toString();
                                radio_2.setVisibility(View.VISIBLE);
                                radio_2.setText(startTime1);

                            } else {
                                radio_2.setVisibility(View.GONE);

                            }
                            if (snapshot.child("time3").getValue() != null) {
                                String startTime1 = Objects.requireNonNull(snapshot.child("time3").getValue()).toString();
                                radio_3.setVisibility(View.VISIBLE);
                                radio_3.setText(startTime1);

                            } else {
                                radio_3.setVisibility(View.GONE);
                            }

                            if (snapshot.child("time4").getValue() != null) {
                                String startTime1 = Objects.requireNonNull(snapshot.child("time4").getValue()).toString();
                                radio_4.setVisibility(View.VISIBLE);
                                radio_4.setText(startTime1);

                            } else {
                                radio_4.setVisibility(View.GONE);
                            }

                            if (snapshot.child("time5").getValue() != null) {
                                String startTime1 = Objects.requireNonNull(snapshot.child("time5").getValue()).toString();
                                radio_5.setVisibility(View.VISIBLE);
                                radio_5.setText(startTime1);

                            } else {
                                radio_5.setVisibility(View.GONE);
                            }

                            if (snapshot.child("time6").getValue() != null) {
                                String startTime1 = Objects.requireNonNull(snapshot.child("time6").getValue()).toString();
                                radio_6.setVisibility(View.VISIBLE);
                                radio_6.setText(startTime1);

                            } else {
                                radio_6.setVisibility(View.GONE);
                            }

                            if (snapshot.child("time7").getValue() != null) {
                                String startTime1 = Objects.requireNonNull(snapshot.child("time7").getValue()).toString();
                                radio_7.setVisibility(View.VISIBLE);
                                radio_7.setText(startTime1);

                            } else {
                                radio_7.setVisibility(View.GONE);
                            }

                            if (snapshot.child("time8").getValue() != null) {
                                String startTime1 = Objects.requireNonNull(snapshot.child("time8").getValue()).toString();
                                radio_8.setVisibility(View.VISIBLE);
                                radio_8.setText(startTime1);

                            } else {
                                radio_8.setVisibility(View.GONE);
                            }

                            if (snapshot.child("time9").getValue() != null) {
                                String startTime1 = Objects.requireNonNull(snapshot.child("time9").getValue()).toString();
                                radio_9.setVisibility(View.VISIBLE);
                                radio_9.setText(startTime1);

                            } else {
                                radio_9.setVisibility(View.GONE);
                            }
                        } else {
                            txt_vaqt.setVisibility(View.VISIBLE);
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
                if (snapshot.getValue() != null) {
                    txt_vaqt.setVisibility(View.GONE);
                    if (snapshot.child("time").getValue() != null) {
                        String startTime1 = Objects.requireNonNull(snapshot.child("time").getValue()).toString();
                        radio_1.setVisibility(View.VISIBLE);
                        radio_1.setText(startTime1);

                    } else {
                        radio_1.setVisibility(View.GONE);
                    }
                    if (snapshot.child("time2").getValue() != null) {
                        String startTime1 = Objects.requireNonNull(snapshot.child("time2").getValue()).toString();
                        radio_2.setVisibility(View.VISIBLE);
                        radio_2.setText(startTime1);

                    } else {
                        radio_2.setVisibility(View.GONE);

                    }
                    if (snapshot.child("time3").getValue() != null) {
                        String startTime1 = Objects.requireNonNull(snapshot.child("time3").getValue()).toString();
                        radio_3.setVisibility(View.VISIBLE);
                        radio_3.setText(startTime1);

                    } else {
                        radio_3.setVisibility(View.GONE);
                    }

                    if (snapshot.child("time4").getValue() != null) {
                        String startTime1 = Objects.requireNonNull(snapshot.child("time4").getValue()).toString();
                        radio_4.setVisibility(View.VISIBLE);
                        radio_4.setText(startTime1);

                    } else {
                        radio_4.setVisibility(View.GONE);
                    }

                    if (snapshot.child("time5").getValue() != null) {
                        String startTime1 = Objects.requireNonNull(snapshot.child("time5").getValue()).toString();
                        radio_5.setVisibility(View.VISIBLE);
                        radio_5.setText(startTime1);

                    } else {
                        radio_5.setVisibility(View.GONE);
                    }

                    if (snapshot.child("time6").getValue() != null) {
                        String startTime1 = Objects.requireNonNull(snapshot.child("time6").getValue()).toString();
                        radio_6.setVisibility(View.VISIBLE);
                        radio_6.setText(startTime1);

                    } else {
                        radio_6.setVisibility(View.GONE);
                    }

                    if (snapshot.child("time7").getValue() != null) {
                        String startTime1 = Objects.requireNonNull(snapshot.child("time7").getValue()).toString();
                        radio_7.setVisibility(View.VISIBLE);
                        radio_7.setText(startTime1);

                    } else {
                        radio_7.setVisibility(View.GONE);
                    }

                    if (snapshot.child("time8").getValue() != null) {
                        String startTime1 = Objects.requireNonNull(snapshot.child("time8").getValue()).toString();
                        radio_8.setVisibility(View.VISIBLE);
                        radio_8.setText(startTime1);

                    } else {
                        radio_8.setVisibility(View.GONE);
                    }

                    if (snapshot.child("time9").getValue() != null) {
                        String startTime1 = Objects.requireNonNull(snapshot.child("time9").getValue()).toString();
                        radio_9.setVisibility(View.VISIBLE);
                        radio_9.setText(startTime1);

                    } else {
                        radio_9.setVisibility(View.GONE);
                    }
                }else {
                    txt_vaqt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}