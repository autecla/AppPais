package com.lincs.mobcare.notice;

import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Aviso;
import com.lincs.mobcare.utils.Snapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoticeFragment extends Fragment {

    private ArrayList<Aviso> notices = new ArrayList<>();

    public NoticeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lookup the recyclerview in activity layout
        final RecyclerView recyclerView = view.findViewById(R.id.rvNotices);

        String [] rotinas = {"Acordar", "Tomar banho", "Trocar de roupa", "Tomar café", "Escovar os dentes"};
        notices = new ArrayList<>();
        for(String r : rotinas){
            notices.add(new Aviso("0","", ""));

        }

        NoticeAdapter adapter = new NoticeAdapter(notices, view.getContext());

        adapter.setOnClickListener(new NoticeAdapter.OnClickListener() {
            @Override
            public void onClick(View itemView, int position) {
                NoticeDialog hdf = NoticeDialog.newInstance(position, notices);
                hdf.setTargetFragment(getTargetFragment(), view.getId());
                hdf.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "");
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

}
