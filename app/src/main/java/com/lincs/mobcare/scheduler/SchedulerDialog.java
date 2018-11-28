package com.lincs.mobcare.scheduler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.*;
import com.lincs.mobcare.utils.Snapshot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SchedulerDialog extends DialogFragment {

    private int position;
    private List<Consulta> appointments;

    public static SchedulerDialog newInstance(int position, List<  Consulta> appointments) {
        SchedulerDialog fragment = new SchedulerDialog();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("appointments", (Serializable) appointments);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        position = getArguments().getInt("position");
        appointments = (List<Consulta>) getArguments().getSerializable("appointments");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.appointment_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView nome = view.findViewById(R.id.profissional_appointment_dialog);
        final TextView tipo =view.findViewById(R.id.especialidade_appointment_dialog);

          Consulta consulta = appointments.get(position);
          Profissional profissional = Snapshot.getProfissionais().get(consulta.id_profissional);
        if(profissional != null) {
            nome.setText(profissional.nome);
            tipo.setText(profissional.especialidade);
        }
        if(consulta.especialidade != null) {
            tipo.setText(consulta.especialidade);
        }
        ((TextView) view.findViewById(R.id.sala_appointment_dialog)).setText(appointments.get(position).local);

        String date="dd/MM/yyyy";
        String time="HH:mm";
        SimpleDateFormat sdf_data = new SimpleDateFormat(date, Locale.ROOT);
        SimpleDateFormat sdf_hora = new SimpleDateFormat(time,Locale.ROOT);
        ((TextView) view.findViewById(R.id.data_appointment_dialog)).setText(sdf_data.format(appointments.get(position).data));
        ((TextView) view.findViewById(R.id.hora_appointment_dialog)).setText(sdf_hora.format(appointments.get(position).data));

        Button cancelar = view.findViewById(R.id.btn_cancelar_dialog_scheduler);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
}
