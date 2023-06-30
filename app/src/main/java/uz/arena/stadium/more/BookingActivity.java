package uz.arena.stadium.more;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import uz.arena.stadium.BookingAdapter;
import uz.arena.stadium.BookingItem;
import uz.arena.stadium.HomeActivity;
import uz.arena.stadium.MoreActivity;
import uz.arena.stadium.R;

public class BookingActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference RootRef;
    RecyclerView recyclerview;
    BookingAdapter stadiumAdapter;
    TextView booking_txt;
    ImageView img_on_info_book, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        recyclerview = findViewById(R.id.booking_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        manager.setSmoothScrollbarEnabled(true);
        manager.setReverseLayout(true);
        recyclerview.setLayoutManager(manager);
        booking_txt = findViewById(R.id.txt_on_info_book);
        img_on_info_book = findViewById(R.id.img_on_info_book);
        back = findViewById(R.id.book_back);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(BookingActivity.this, MoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            BookingActivity.this.finish();
            overridePendingTransition(0, 0);
        });

        String currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user_booking").child(currentUserID);

        FirebaseRecyclerOptions<BookingItem> options =
                new FirebaseRecyclerOptions.Builder<BookingItem>()
                        .setQuery(reference, BookingItem.class)
                        .build();
        stadiumAdapter = new BookingAdapter(options);
        recyclerview.setAdapter(stadiumAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        stadiumAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        stadiumAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BookingActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        BookingActivity.this.finish();
    }
}