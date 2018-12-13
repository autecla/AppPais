package com.lincs.mobcare.daily;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.*;
import com.lincs.mobcare.symptom.SymptomReportDialog;
import com.lincs.mobcare.utils.Snapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class DailyFragment extends Fragment implements AdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "DailyFragment: ";
    private ArrayList<  Realiza> mEvolution = new ArrayList<>();
    private ArrayList<  Apresenta> mSymptom;
    SwipeRefreshLayout swipeLayout;

    private ArrayList<EvolutionSymptomDay> mEvSympDay = new ArrayList<>();

    private int month;
    DailyAdapter adapter;
    AdapterCallback adapterCallback;

    public static DailyFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("month", page);
        DailyFragment fragment = new DailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        month = getArguments().getInt("month");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_daily, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.rvDaily);

        swipeLayout = view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);

        adapterCallback = this;

        mEvolution = Snapshot.getRealizaMes(month);
        mSymptom = Snapshot.getApresentaMes(month);

        mEvSympDay = new ArrayList<>();
        splitThroughDays();
        Log.d(TAG, "OnCreateView");

        if (mEvSympDay.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new DailyAdapter(mEvSympDay, view.getContext(), DailyFragment.this);

        ImageButton buttonReport = view.findViewById(R.id.btn_report);
        buttonReport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                SymptomReportDialog hdf = SymptomReportDialog.newInstance();
                hdf.setTargetFragment(DailyFragment.this,view.getId());
                hdf.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "");
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    @Override
    public void onMethodCallback() {
        mEvolution.clear();
        mEvolution = Snapshot.getRealizaMes(month);
        mSymptom.clear();
        mSymptom = Snapshot.getApresentaMes(month);

        splitThroughDays();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        onMethodCallback();
        swipeLayout.setRefreshing(false);
    }

    public class EvolutionSymptomDay implements Comparable<EvolutionSymptomDay> {

        private Date date;
        private ArrayList<Realiza> ev;
        private ArrayList<  Apresenta> symp;

        EvolutionSymptomDay(Date date) {
            this.ev = new ArrayList<>();
            this.symp = new ArrayList<>();
            this.date=date;
        }

        public Date getDate() {
            return date;
        }

        public ArrayList<  Realiza> getEv() {
            return ev;
        }

        ArrayList<  Apresenta> getSymp() {
            return symp;
        }

        @Override
        public int compareTo(@NonNull EvolutionSymptomDay o) {

            return (this.date.compareTo(o.date));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            onMethodCallback();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public int existe(ArrayList<EvolutionSymptomDay>array,Date date){
        int retorno=-1;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        for (int i=0;i<array.size();i++){
            Calendar  calendar1=Calendar.getInstance();
            calendar1.setTime(array.get(i).date);
            if (calendar.get(Calendar.DAY_OF_MONTH)==calendar1.get(Calendar.DAY_OF_MONTH)&&
                    calendar.get(Calendar.MONTH)==calendar.get(Calendar.MONTH)){
                retorno=i;
            }
        }
        return retorno;
    }

    public void splitThroughDays() {
        mEvSympDay.clear();

        if (mEvolution != null && mEvolution.size() > 0) {

            for (  Realiza e : mEvolution) {//Add dias e meses
                Date date=new Date(e.data);

                int tam=mEvSympDay.size();

                if (tam == 0) {
                    mEvSympDay.add(new EvolutionSymptomDay(date));
                    mEvSympDay.get(0).getEv().add(e);
                }
                else {
                    int existe=existe(mEvSympDay,date);
                    if (existe==-1) {
                        mEvSympDay.add(new EvolutionSymptomDay(date));
                        mEvSympDay.get(mEvSympDay.size()-1).getEv().add(e);
                    }
                    else{
                        mEvSympDay.get(existe).getEv().add(e);
                    }
                }
            }
        }

        if (mSymptom != null && mSymptom.size() > 0) {
            for (  Apresenta s : mSymptom) {
                Date date=new Date(s.data);

                int tam=mEvSympDay.size();

                if (tam == 0) {
                    mEvSympDay.add(new EvolutionSymptomDay(date));
                    mEvSympDay.get(0).getSymp().add(s);
                }
                else {
                    int existe=existe(mEvSympDay,date);
                    if (existe==-1) {
                        mEvSympDay.add(new EvolutionSymptomDay(date));
                        mEvSympDay.get(mEvSympDay.size()-1).getSymp().add(s);
                    }
                    else{
                        mEvSympDay.get(existe).getSymp().add(s);
                    }
                }
            }
        }
        Collections.sort(mEvSympDay);
        Collections.reverse(mEvSympDay);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mEvolution.clear();
        mSymptom.clear();
        mEvSympDay.clear();
    }
}
