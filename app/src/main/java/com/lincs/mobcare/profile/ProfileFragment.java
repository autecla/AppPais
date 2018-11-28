package com.lincs.mobcare.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lincs.mobcare.R;
import com.lincs.mobcare.authentication.signup.SignUpAngelEditActivity;
import com.lincs.mobcare.authentication.signup.SignUpCompanionEditActivity;
import com.lincs.mobcare.daily.DailyActivity;
import com.lincs.mobcare.models.*;
import com.lincs.mobcare.symptom.SymptomReportDialog;
import com.lincs.mobcare.utils.LoadImg;
import com.lincs.mobcare.utils.Snapshot;

import java.text.ParseException;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {// Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView imageViewBaby = view.findViewById(R.id.iv_baby_profile);
        ImageView imageViewCompanion = view.findViewById(R.id.iv_companion_profile);
        ImageView imageViewEditBaby = view.findViewById(R.id.edit_icon_angel);
        ImageView imageViewEditMom = view.findViewById(R.id.edit_icon_sponsor);

        CardView cardViewAcompanhante = view.findViewById(R.id.card_acompanhante);
        cardViewAcompanhante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoAcompanhanteDialog hdf = InfoAcompanhanteDialog.newInstance();
                hdf.setTargetFragment(getTargetFragment(),view.getId());
                if (getActivity()!=null)
                hdf.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        CardView cardViewAnjo = view.findViewById(R.id.card_anjo);
        cardViewAnjo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoAnjoDialog hdf = InfoAnjoDialog.newInstance();
                hdf.setTargetFragment(getTargetFragment(),view.getId());
                if (getActivity()!=null)
                hdf.show(getActivity().getSupportFragmentManager(), "");
            }
        });


        ImageButton btnDaily = view.findViewById(R.id.btn_daily);
        btnDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DailyActivity.class);
                startActivity(intent);
            }
        });

        ImageButton buttonReport = view.findViewById(R.id.btn_report);
        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SymptomReportDialog hdf = SymptomReportDialog.newInstance();
                hdf.setTargetFragment(getTargetFragment(),view.getId());
                if (getActivity()!=null)
                hdf.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        imageViewEditBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity()!=null)
                getActivity().finish();
                Intent intent = new Intent(view.getContext(), SignUpAngelEditActivity.class);
                startActivity(intent);
            }
        });

        imageViewBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity()!=null)
                    getActivity().finish();
                Intent intent = new Intent(view.getContext(), SignUpAngelEditActivity.class);
                startActivity(intent);
            }
        });

        imageViewEditMom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (getActivity()!=null)
                   getActivity().finish();
                Intent intent = new Intent(view.getContext(), SignUpCompanionEditActivity.class);
                startActivity(intent);
            }
        });

        imageViewCompanion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity()!=null)
                    getActivity().finish();
                Intent intent = new Intent(view.getContext(), SignUpCompanionEditActivity.class);
                startActivity(intent);
            }
        });

        //Criança
          Anjo anjo = Snapshot.getAnjo();

        //Carrega imagem do anjo
        if(anjo != null){
            LoadImg.loadImage(anjo.foto,imageViewBaby,getContext());

            TextView anjoNome = view.findViewById(R.id.profile_name);
            anjoNome.setText(anjo.nome);

            TextView anjoIdade = view.findViewById(R.id.profile_type);
            String idade= null;
            try {
                idade = Snapshot.getIdade2(anjo.data_de_nascimento);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            anjoIdade.setText(idade);
        }

        //Cuidador
        Acompanhante acompanhante = Snapshot.getAcompanhante();
        if(acompanhante != null) {
            LoadImg.loadImage(acompanhante.foto, imageViewCompanion, getContext());

            TextView acompanhanteNome = view.findViewById(R.id.profile_sponsor_name);
            String acompanhante_nome = acompanhante.nome + " (" + acompanhante.parentesco + ")";
            acompanhanteNome.setText(acompanhante_nome);

            TextView acompanhanteTelefone = view.findViewById(R.id.profile_sponsor_phone);
            acompanhanteTelefone.setText(acompanhante.telefone);
        }

        
    }
}
