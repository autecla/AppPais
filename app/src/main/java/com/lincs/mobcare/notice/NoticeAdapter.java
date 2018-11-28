package com.lincs.mobcare.notice;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Aviso;
import java.util.List;
import java.util.Random;



public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    // Define listener member variable

    private static NoticeAdapter.OnClickListener clickListener;
   // private int [] colors = {"#acccff", "#FFD4BA", "#F1F9CA", "#AFF0FF", "#89B2FF", "#D2AFED"};

    // Define listener member variable
    public interface OnClickListener {
        void onClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnClickListener(NoticeAdapter.OnClickListener listener) {
        clickListener = listener;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView titleTextView;
        TextView descriptionTextView;
        TextView moreTextView;
        public View item;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView, int rot) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            titleTextView = itemView.findViewById(R.id.notice_title);
            item = itemView.findViewById(R.id.item_notice);
            moreTextView=itemView.findViewById(R.id.notice_more);


            int [] BG = {R.drawable.pecs0, R.drawable.pecs1, R.drawable.pecs2, R.drawable.pecs3, R.drawable.pecs4};

            Random rand = new Random();
            int idx = rand.nextInt(4);
            LinearLayout itemCard =(LinearLayout) itemView.findViewById(R.id.notice_card);
            itemCard.setBackgroundResource(BG[rot]);



            //item.setBackground(R.drawable.ic_emoji_choro_continuo_24dp);

            // Setup the click listener
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Triggers click upwards to the adapter on click
                 /*   if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (clickListener != null && position != RecyclerView.NO_POSITION) {
                            clickListener.onClick(item, position);
                            Log.d("pos", position+"");
                        }
                    }*/
                }
            });
            moreTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (clickListener != null && position != RecyclerView.NO_POSITION) {
                            clickListener.onClick(item, position);
                            Log.d("pos", position+"");
                        }
                    }
                }
            });
        }
    }

    private List<Aviso> notices;
    private Context context;

    NoticeAdapter(List<Aviso> notices, Context context) {
        this.notices = notices;
        this.context = context;
    }

    // Easy access to the context object in the recyclerview
    public Context getContext() {
        return context;
    }
    int rot = -1;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout

        View noticeView = inflater.inflate(R.layout.item_notice, parent, false);


        // Return a new holder instance
        rot++;
        return  new ViewHolder(noticeView, rot);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
          Aviso notice = notices.get(position);

        // Set item views based on your views and data model
        holder.titleTextView.setText(notice.mensagem);

        //String shortComment = notice.mensagem.substring(0, 85) + "...";
        Log.d("OI", "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

}