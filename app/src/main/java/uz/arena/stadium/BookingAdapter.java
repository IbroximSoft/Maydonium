package uz.arena.stadium;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookingAdapter extends FirebaseRecyclerAdapter<BookingItem, BookingAdapter.myViewHolder> {

    public BookingAdapter(@NonNull FirebaseRecyclerOptions<BookingItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myViewHolder holder,final int position, @NonNull BookingItem model) {
        holder.st_name.setText(model.getSt_name());
        holder.item_st_uid.setText(model.getSt_uid());
        holder.location.setText(model.getLocation());
        holder.time.setText(model.getTime());
        holder.DateE.setText(model.getDate());
        holder.date_book.setText(model.getDateGone());

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("location", model.getLocation());
        dataModel.put("arena_name", model.getSt_name());
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
        dataModel.put("uid", model.getSt_uid());
        dataModel.put("latitude", model.getLatitude());
        dataModel.put("longitude", model.getLongitude());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView DateE, time, st_name, location, uid, date_book, item_st_uid;
        ImageView img, img_wait, img_success;
        CardView booking_card;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            DateE = itemView.findViewById(R.id.item_book_date);
            time = itemView.findViewById(R.id.item_book_time);
            st_name = itemView.findViewById(R.id.item_book_stName);
            location = itemView.findViewById(R.id.item_book_location);
            img = itemView.findViewById(R.id.item_book_img);
            img_wait = itemView.findViewById(R.id.bookItem_img_wait);
            img_success = itemView.findViewById(R.id.bookItem_img_success);
            uid = itemView.findViewById(R.id.item_uid_book);
            date_book = itemView.findViewById(R.id.item_date_book);
            item_st_uid = itemView.findViewById(R.id.item_st_uid);
            booking_card = itemView.findViewById(R.id.booking_card);
        }
    }
}
