package uz.arena.stadium.more;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import uz.arena.stadium.MoreActivity;
import uz.arena.stadium.R;
import uz.arena.stadium.language.Appcompat;

public class ProfilActivity extends Appcompat {
    FirebaseAuth auth;
    DatabaseReference reference;
    ImageView profil_back;
    CircleImageView profil_image;
    LinearLayout profil_liner_camera;
    TextView profil_text, profil_phone;
    EditText profil_name_edit, profil_lost_edit;
    Button cancel_btn, save_btn;
    StorageReference mStorageRef;
    public Uri imageUri;
    StorageReference file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        profil_back = findViewById(R.id.profil_back);
        profil_image = findViewById(R.id.profil_image);
        profil_liner_camera = findViewById(R.id.profil_liner_camera);
        profil_text = findViewById(R.id.profil_text);
        profil_phone = findViewById(R.id.profil_phone);
        profil_name_edit = findViewById(R.id.profil_name_edit);
        profil_lost_edit = findViewById(R.id.profil_lost_edit);
        cancel_btn = findViewById(R.id.profil_cancel);
        save_btn = findViewById(R.id.profil_save);

        String currentUserID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        reference.child("User").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String lost = Objects.requireNonNull(snapshot.child("lost").getValue()).toString();
                String phone = Objects.requireNonNull(snapshot.child("phone").getValue()).toString();
                String vs = name + " " + lost;
                profil_name_edit.setText(name);
                profil_lost_edit.setText(lost);
                profil_phone.setText(phone);
                profil_text.setText(vs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("User_img").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("rasim").getValue() !=null){
                    String name = Objects.requireNonNull(snapshot.child("rasim").getValue()).toString();
                    Picasso.get().load(name).into(profil_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profil_liner_camera.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });

        profil_back.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, MoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            ProfilActivity.this.finish();
            overridePendingTransition(0, 0);
        });

        save_btn.setOnClickListener(v -> DatabaseProfil());

        cancel_btn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, MoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            ProfilActivity.this.finish();
            overridePendingTransition(0, 0);
        });
    }

    private void SendUserMainActivity() {
        Intent intent = new Intent(ProfilActivity.this, MoreActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ProfilActivity.this.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profil_image.setImageURI(imageUri);
            StorageReference Folder = FirebaseStorage.getInstance().getReference().child("User");
            file_name = Folder.child("file" + imageUri.getLastPathSegment());

            file_name.putFile(imageUri).addOnSuccessListener(taskSnapshot -> file_name.getDownloadUrl().addOnSuccessListener(uri -> {
                String currentUerID = auth.getCurrentUser().getUid();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("rasim", String.valueOf(uri));
                reference.child("User_img").child(currentUerID).setValue(hashMap);
            }));
            UploadPicture();
        }
    }

    private void UploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Yuklanmoqda");
        pd.show();
        pd.setCancelable(false);

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = mStorageRef.child(randomKey);


        riversRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    pd.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(ProfilActivity.this, "Hato bo'ldi", Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(snapshot -> {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) progressPercent + "%");
                });
    }

    private void DatabaseProfil() {
        String currentUserID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        String Name = profil_name_edit.getText().toString();
        String Lost = profil_lost_edit.getText().toString();
        String Number = profil_phone.getText().toString();

        if (TextUtils.isEmpty(Name)) {
            profil_name_edit.setError(getString(R.string.name));
        } else if (TextUtils.isEmpty(Lost)) {
            profil_lost_edit.setError(getString(R.string.lost_name));
        } else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", Name);
            hashMap.put("lost", Lost);
            hashMap.put("phone", Number);

            reference.child("User").child(currentUserID).setValue(hashMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            SendUserMainActivity();
                            Toasty.success(ProfilActivity.this, "Profilingiz tahrirlandi!", Toasty.LENGTH_SHORT, true).show();
                        } else {
                            String message = Objects.requireNonNull(task.getException()).toString();
                            Toasty.error(ProfilActivity.this, "Hatolik:" + message, Toasty.LENGTH_SHORT, true).show();
                        }
                    });
        }
    }
}