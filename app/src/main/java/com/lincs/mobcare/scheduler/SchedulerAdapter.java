package com.lincs.mobcare.scheduler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Consulta;
import com.lincs.mobcare.models.Profissional;
import com.lincs.mobcare.utils.Snapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SchedulerAdapter extends RecyclerView.Adapter<SchedulerAdapter.ViewHolder> {

    // Define listener member variable
    private static SchedulerAdapter.OnClickListener clickListener;

    // Define listener member variable
    public interface OnClickListener {
        void onClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnClickListener(SchedulerAdapter.OnClickListener listener) {
        clickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView localTextview;
        TextView doctorTextView;
        public TextView dateTextView;
        TextView hourTextView;
        TextView typeTextView;
        ImageView statusImageView;
        View appointment;

        public ViewHolder(final View itemView) {
            super(itemView);

            appointment = itemView.findViewById(R.id.item_appointment);
            localTextview = itemView.findViewById(R.id.appointment_local);
            doctorTextView = itemView.findViewById(R.id.appointment_doctor);
            dateTextView =itemView.findViewById(R.id.appointment_date);
            hourTextView = itemView.findViewById(R.id.appointment_hour);
            typeTextView =itemView.findViewById(R.id.appointment_type);
            statusImageView = itemView.findViewById(R.id.appointment_present);

            appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Triggers click upwards to the adapter on click
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (clickListener != null && position != RecyclerView.NO_POSITION) {
                            clickListener.onClick(appointment, position);
                        }
                    }
                }
            });
        }
    }

    private List<Consulta> appointmentList;
    private Context context;

    SchedulerAdapter(List<Consulta> appointmentList, Context context) {
        this.appointmentList = appointmentList;
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
        View appointmentView = inflater.inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(appointmentView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final   Consulta appointment = appointmentList.get(position);
          Profissional profissional = Snapshot.getProfissionais().get(appointment.id_profissional);
        if(profissional != null) {
            holder.doctorTextView.setText(profissional.nome);
            holder.typeTextView.setText(profissional.especialidade);
        }
        if(appointment.especialidade != null) {
            holder.typeTextView.setText(appointment.especialidade);
        }

        holder.localTextview.setText(appointment.local);

        String date="dd/MM/yyyy";
        String time="HH:mm";
        SimpleDateFormat sdf_data = new SimpleDateFormat(date, Locale.ROOT);
        holder.dateTextView.setText(sdf_data.format(appointment.data));
        SimpleDateFormat sdf_hora = new SimpleDateFormat(time,Locale.ROOT);
        holder.hourTextView.setText(sdf_hora.format(appointment.data));

        if (appointment.data > new Date().getTime()) {
            holder.statusImageView.setImageResource(R.mipmap.ic_schedule_black_new);
        } else {
            if (appointment.status) {
                holder.statusImageView.setImageResource(R.mipmap.ic_done_black_new);
            } else {
                holder.statusImageView.setImageResource(R.mipmap.ic_clear_black_new);
            }
        }
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public void setAppointmentList(List<  Consulta> appointmentList) {
        this.appointmentList.clear();
        this.appointmentList = appointmentList;
    }
}