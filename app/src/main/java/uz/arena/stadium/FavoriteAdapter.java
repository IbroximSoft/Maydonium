package uz.arena.stadium;

import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FavoriteAdapter extends FirebaseRecyclerAdapter<OrderItem, FavoriteAdapter.myViewHolder> {

    public FavoriteAdapter(@NonNull FirebaseRecyclerOptions<OrderItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull OrderItem model) {

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
            intent.putExtra("datas", (Serializable)dataModel);
            holder.card.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView arena_name, location, summa;
        ImageView img;
        CardView card;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            arena_name = itemView.findViewById(R.id.item_favorite_stName);
            location = itemView.findViewById(R.id.item_favorite_location);
            summa = itemView.findViewById(R.id.favorite_card1_summa);
            img = itemView.findViewById(R.id.item_favorite_img);
            card = itemView.findViewById(R.id.favorite_card_item);
        }
    }
}
