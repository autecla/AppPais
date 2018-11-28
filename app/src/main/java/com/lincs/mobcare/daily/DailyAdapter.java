package com.lincs.mobcare.daily;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewDia;
        public LinearLayout linearLayoutNote;
        RecyclerView recView_atividades;
        RecyclerView recView_sintomas;
        public View item_separator;
        public LinearLayout text_sintomas;
        public LinearLayout text_atividades;

        public ViewHolder(final View itemView) {
            super(itemView);

            textViewDia = itemView.findViewById(R.id.textViewDia);
            linearLayoutNote = itemView.findViewById(R.id.linearLayoutNote);
            recView_atividades =itemView.findViewById(R.id.recview_atividades);
            recView_sintomas = itemView.findViewById(R.id.recview_sintomas);
            item_separator = itemView.findViewById(R.id.item_separator);
            text_atividades = itemView.findViewById(R.id.text_atividades);
            text_sintomas = itemView.findViewById(R.id.text_sintomas);
        }
    }

    private ArrayList<DailyFragment.EvolutionSymptomDay> mEvSympDay;
    private Context context;
    private Fragment targetFragment;

    DailyAdapter(ArrayList<DailyFragment.EvolutionSymptomDay> mEvSympDay, Context context, Fragment targetFragment) {
        this.mEvSympDay = mEvSympDay;
        this.context = context;
        this.targetFragment = targetFragment;
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Calendar cal = Calendar.getInstance();
        DailyFragment.EvolutionSymptomDay esd = mEvSympDay.get(position);
        Boolean ev = (esd.getEv() != null && esd.getEv().size() > 0);
        Boolean symp = (esd.getSymp() != null && esd.getSymp().size() > 0);

        if (ev) {
            holder.recView_atividades.setVisibility(View.VISIBLE);
            holder.text_atividades.setVisibility(LinearLayout.VISIBLE);
            cal.setTime(new Date(esd.getEv().get(0).data));

            ArrayList<Realiza> tmp = esd.getEv();
            Collections.reverse(tmp);

            ActivityAdapter adapter_atividades = new ActivityAdapter(tmp, context, targetFragment);
            holder.recView_atividades.setAdapter(adapter_atividades);
            holder.recView_atividades.setLayoutManager(new LinearLayoutManager(context));
        } else {
            holder.recView_atividades.setVisibility(View.GONE);
            holder.text_atividades.setVisibility(LinearLayout.GONE);
        }

        if (symp) {
            holder.recView_sintomas.setVisibility(View.VISIBLE);
            holder.text_sintomas.setVisibility(LinearLayout.VISIBLE);
            cal.setTime(new Date(esd.getSymp().get(0).data));

            ArrayList<Apresenta> tmp = esd.getSymp();
            Collections.reverse(tmp);

            SymptomAdapter adapter_sintomas = new SymptomAdapter(tmp, context, targetFragment);
            holder.recView_sintomas.setLayoutManager(new LinearLayoutManager(context));
            holder.recView_sintomas.setAdapter(adapter_sintomas);
        } else {
            holder.recView_sintomas.setVisibility(View.GONE);
            holder.text_sintomas.setVisibility(LinearLayout.GONE);
        }
        if (ev && symp) {
            holder.item_separator.setVisibility(View.VISIBLE);
        } else {
            holder.item_separator.setVisibility(View.GONE);
        }
        String dia="Dia " + cal.get(Calendar.DAY_OF_MONTH);
        holder.textViewDia.setText(dia);
    }

    @Override
    public int getItemCount() {
        return mEvSympDay.size();
    }
}
