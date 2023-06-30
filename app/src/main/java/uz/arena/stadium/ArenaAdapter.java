package uz.arena.stadium;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ArenaAdapter extends FirebaseRecyclerAdapter<OrderItem, ArenaAdapter.myViewHolder> {
    FirebaseAuth auth;
    DatabaseReference reference;

    public ArenaAdapter(@NonNull FirebaseRecyclerOptions<OrderItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull OrderItem model) {

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        holder.arena_name.setText(model.getArena_name());
        holder.location.setText(model.getLocation());
        holder.summa.setText(model.getSumma());
        Picasso.get().load(model.getRasim1()).into(holder.img);

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("location", model.getLocation());
        dataModel.put("arena_name", model.getArena_name());
        dataModel.put("rasim1", model.getRasim1());
        dataModel.put("rasim2", model.getRasim2());
        dataModel.put("rasim3", model.getRasim3());
        dataModel.put("rasim4", model.getRasim4());
        dataModel.put("check_1", model.getCheck_1());
        dataModel.put("check_2", model.getCheck_2());
        dataModel.put("choosing", model.getChoosing());
        dataModel.put("dimensions", model.getDimensions());
        dataModel.put("summa", model.getSumma());
        dataModel.put("radio", model.getRadio());
        dataModel.put("uid", model.getUid());
        dataModel.put("latitude", model.getLatitude());
        dataModel.put("longitude", model.getLongitude());

        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(holder.card.getContext(), DetailsActivity.class);
            intent.putExtra("datas", (Serializable) dataModel);
            holder.card.getContext().startActivity(intent);
        });

        String currentUserID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        String Uid = model.getUid();

        reference.child("favorite_users").child(currentUserID).child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("salom", Uid);
                if (snapshot.child("arena_name").getValue() == null) {
                    holder.favorite_on.setVisibility(View.GONE);
                    holder.favorite_off.setVisibility(View.VISIBLE);
                } else {
                    holder.favorite_on.setVisibility(View.VISIBLE);
                    holder.favorite_off.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.favorite_off.setOnClickListener(v -> {
            String Location = model.getLocation();
            String Arena_name = model.getArena_name();
            String Rasim1 = model.getRasim1();
            String Rasim2 = model.getRasim2();
            String Rasim3 = model.getRasim3();
            String Rasim4 = model.getRasim4();
            String Check_1 = model.getCheck_1();
            String Check_2 = model.getCheck_2();
            String Choosing = model.getChoosing();
            String Dimensions = model.getDimensions();
            String Summa = model.getSumma();
            String Radio = model.getRadio();
            String Uid1 = model.getUid();
            String Latitude = model.getLatitude();
            String Longitude = model.getLongitude();
            String currentUserID1 = auth.getCurrentUser().getUid();

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
            malumotHas.put("uid", Uid1);
            malumotHas.put("latitude", Latitude);
            malumotHas.put("longitude", Longitude);

            reference.child("favorite_users").child(currentUserID1).child(Uid1).setValue(malumotHas)
                    .addOnCompleteListener(task -> {
                        holder.favorite_on.setVisibility(View.VISIBLE);
                        holder.favorite_off.setVisibility(View.GONE);
                        Toasty.success(v.getContext(), R.string.yoqqanlari, Toasty.LENGTH_SHORT, true).show();
                    });
        });

        holder.favorite_on.setOnClickListener(v -> reference.child("favorite_users").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child(Uid).getRef().removeValue();
                Toasty.success(v.getContext(), R.string.yoqqanlari_olibTashlash, Toasty.LENGTH_SHORT, true).show();
                holder.favorite_on.setVisibility(View.GONE);
                holder.favorite_off.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }

    @NonNull
    @Override
    public ArenaAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_arena, parent, false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView arena_name, location, summa;
        ImageView img;
        CardView card, favorite_off, favorite_on;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            arena_name = itemView.findViewById(R.id.item_name);
            location = itemView.findViewById(R.id.item_location);
            summa = itemView.findViewById(R.id.item_summa);
            img = itemView.findViewById(R.id.item_img);
            card = itemView.findViewById(R.id.item_card);
            favorite_off = itemView.findViewById(R.id.arena_favorite_off);
            favorite_on = itemView.findViewById(R.id.arena_favorite_on);
        }
    }
}
