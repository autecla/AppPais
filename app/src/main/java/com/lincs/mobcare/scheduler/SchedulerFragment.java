package com.lincs.mobcare.scheduler;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Consulta;
import com.lincs.mobcare.utils.Snapshot;

import java.util.ArrayList;
import java.util.Objects;

public class SchedulerFragment extends Fragment {

    public SchedulerFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scheduler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView rvScheduler =  view.findViewById(R.id.rvScheduler);
        ArrayList<Consulta> appointments = new ArrayList<>(Snapshot.getConsulta().values());

        SchedulerAdapter adapter = new SchedulerAdapter(appointments, view.getContext());
        Snapshot.setConsultaAdapter(adapter);

        adapter.setOnClickListener(new SchedulerAdapter.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View itemView, int position) {
                SchedulerDialog sd = SchedulerDialog.newInstance(position, new ArrayList<>(Snapshot.getConsulta().values()));
                sd.setTargetFragment(getTargetFragment(), view.getId());
                sd.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "");
            }
        });

        rvScheduler.setAdapter(adapter);
        rvScheduler.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}
