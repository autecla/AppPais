package com.lincs.mobcare.daily;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.*;
import com.lincs.mobcare.utils.Snapshot;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    public ArrayList<Realiza> checked;
   // public ArrayList<ActivityAdapter.ViewHolder> positions2;
   // public static ArrayList<CheckBox> checkboxes;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public String id;
        public long data;
        public String titulo;
        String comentario_texto,local_audio,local_video,comentario_video;
        TextView textViewType;
        ImageView imageView;


        public ViewHolder(final View itemView) {
            super(itemView);

            textViewType = itemView.findViewById(R.id.activity_type);
            imageView = itemView.findViewById(R.id.note_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                    ShowDailyDialogFragment ddf = ShowDailyDialogFragment.newInstance(id,data,titulo,comentario_texto,local_audio,local_video,comentario_video);
                    ddf.setTargetFragment(targetFragment, 1);
                    ddf.show(fm, "dialog");
                }
            });
        }
    }

    private ArrayList<  Realiza> mEvolution;
    private Context context;
    private Fragment targetFragment;


    ActivityAdapter(ArrayList<  Realiza> mEvolution, Context context, Fragment targetFragment) {
        this.mEvolution = mEvolution;
        this.context = context;
        this.targetFragment = targetFragment;
        checked = new ArrayList<>();
        //positions2 = new ArrayList<>();
        //checkboxes = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final   Realiza e = mEvolution.get(position);
          Exercicio ev = Snapshot.getExercicio().get(e.id_exercicio);

        holder.id = e.id;
        holder.data = e.data;
        holder.comentario_texto = e.comentario_texto;
        holder.local_audio = e.local_audio;
        holder.local_video = e.local_video;
        holder.comentario_video=e.comentario_video;
        if(ev != null) {
            holder.titulo = ev.titulo;
            holder.textViewType.setText(ev.titulo);
        }
    }

    @Override
    public int getItemCount() {
        return mEvolution.size();
    }
}