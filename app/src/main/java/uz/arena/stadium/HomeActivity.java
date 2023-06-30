package uz.arena.stadium;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;
import java.util.Objects;

import uz.arena.stadium.fragment_top_menu.FilterFragment;
import uz.arena.stadium.fragment_top_menu.HomeFragment;
import uz.arena.stadium.language.Appcompat;
import uz.arena.stadium.more.BookingActivity;
import uz.arena.stadium.more.LanguageMoreActivity;

public class HomeActivity extends Appcompat {
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton actionButton;
    ProgressDialog progressDialog;
    TextView txt_date, txt_uid;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        actionButton = findViewById(R.id.floating);
        bottomNavigationView = findViewById(R.id.bottomappbar);
        bottomNavigationView.setSelectedItemId(R.id.mHome);
        txt_date = findViewById(R.id.date_home_txt);
        txt_uid = findViewById(R.id.uid_home_txt);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, new HomeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.mHome:
                    return true;

                case R.id.mSearch:
                    startActivity(new Intent(getApplicationContext()
                            , SearchActivity.class));

                    overridePendingTransition(0, 0);

                    return true;

                case R.id.mPerson:
                    startActivity(new Intent(getApplicationContext()
                            , CompetitionActivity.class));

                    overridePendingTransition(0, 0);
                    return true;

                case R.id.mSetting:
                    startActivity(new Intent(getApplicationContext()
                            , MoreActivity.class));

                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;

        });

        actionButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
        });
    }

    private void VersionTest() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(R.string.version_eski);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=uz.arena.stadium");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null) {
            progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_item);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            VerifyUserExistance();

            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.internet_layout);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations =
                        android.R.style.Animation_Dialog;
                Button button = dialog.findViewById(R.id.btn_internet);
                button.setOnClickListener(view -> recreate());
                dialog.show();
            }

        } else {
            SendUserToLoginActivity();
        }

        RootRef.child("admin_setting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentVersion = Objects.requireNonNull(snapshot.child("version").getValue()).toString();
                String version = "1.0.2";
                if (!currentVersion.equals(version)){
                    VersionTest();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void VerifyUserExistance() {
        String currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        RootRef.child("User").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("name").exists()) {
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                            DataCheck();
                        } else {
                            SendUserToSettingsActivityy();
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void DataCheck() {

        String currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        RootRef.child("User").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("status").exists()) {
                            String status = Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                            if (status.equals("block")){
                                Intent intentBlock = new Intent(HomeActivity.this, BlockActivity.class);
                                intentBlock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentBlock);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void SendUserToSettingsActivityy() {
        Intent settingsIntent = new Intent(HomeActivity.this, InfoActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(HomeActivity.this, NumberActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
}