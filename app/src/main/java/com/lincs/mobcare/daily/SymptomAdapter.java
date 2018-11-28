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
import com.lincs.mobcare.models.Apresenta;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.TimeUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public String id;
        String tipo;
        TextView textViewType;
        TextView textViewTime;
        ImageView imageView;

        public ViewHolder(final View itemView) {
            super(itemView);

            textViewType = itemView.findViewById(R.id.symptom_type);
            textViewTime =itemView.findViewById(R.id.symptom_time);
            imageView =itemView.findViewById(R.id.note_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //MOSTRAR DIALOG
                    FragmentManager fm = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                    InfoDialog info = InfoDialog.newInstance(id);
                    info.setTargetFragment(targetFragment, 1);
                    info.show(fm, "info_dialog");
                }
            });
        }
    }

    private ArrayList<Apresenta> mSymptom;
    private Context context;
    private Fragment targetFragment;

    SymptomAdapter(ArrayList<  Apresenta> mSymptom, Context context, Fragment targetFragment) {
        this.mSymptom = mSymptom;
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
        View view = inflater.inflate(R.layout.item_symptom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          Apresenta a = mSymptom.get(position);
        if (a != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(a.data));
            String data = TimeUtils.convertHour(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            holder.textViewType.setText(getTexto(a.id_sintoma));
            holder.textViewTime.setText(data);
            holder.imageView.setImageResource(getImagem(a.id_sintoma));
            holder.id = a.id;
            holder.tipo = a.id_sintoma;
        }
    }

    private String getTexto(String n) {
        String r;
        switch (n) {
            case "0":
                r = "Alimentacao";
                break;
            case "1":
                r = "Sono";
                break;
            case "2":
                r = "Troca de Medicacao";
                break;
            case "3":
                r = "Doenca";
                break;
            case "4":
                r = "Choro Contínuo";
                break;
            case "5":
                r = "Convulsão";
                break;
            case "6":
                r = "Agressividade";
                break;
            case "7":
                r = "Outros";
                break;
            default:
                r="Alimentacao";
                break;
        }
        return r;
    }

    private int getImagem(String n) {
        int r;
        switch (n) {
            case "0":
                r = R.mipmap.ic_febre_new;
                break;
            case "1":
                r = R.mipmap.ic_vomito_new;
                break;
            case "2":
                r = R.mipmap.ic_gripe_new;
                break;
            case "3":
                r = R.mipmap.ic_engasgo_new;
                break;
            case "4":
                r = R.mipmap.ic_choro_continuo_new;
                break;
            case "5":
                r = R.mipmap.ic_convulsao_new;
                break;
            case "6":
                r = R.mipmap.ic_espasmos_new;
                break;
            case "7":
                r =R.mipmap.ic_convulsao_new;
                break;
            default:
                r = R.mipmap.ic_espasmos_new;
                break;
        }
        return r;
    }

    @Override
    public int getItemCount() {
        return mSymptom.size();
    }
}
