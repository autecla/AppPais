package com.lincs.mobcare.notice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Aviso;

import java.util.List;

public class NoticeExpandedAdapter extends RecyclerView.Adapter<NoticeExpandedAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;

        public ViewHolder(final View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.notice_title);
            descriptionTextView = itemView.findViewById(R.id.notice_description);
        }
    }

    private List<Aviso> notices;
    private Context context;

    NoticeExpandedAdapter(List<Aviso> notices, Context context) {
        this.notices = notices;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder( inflater.inflate(R.layout.item_notice_expanded, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
          Aviso notice = notices.get(position);

        // Set item views based on your views and data model
        holder.titleTextView.setText(notice.tipo);
        holder.descriptionTextView.setText(notice.mensagem);
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

}