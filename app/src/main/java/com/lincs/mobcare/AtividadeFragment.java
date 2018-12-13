package com.lincs.mobcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lincs.mobcare.utils.ActivityCores;
import com.lincs.mobcare.utils.ActivitySeq;

public class AtividadeFragment extends Fragment {
        private Button btnCores, btnSeq;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.atividade_fragment_layout, container, false);
            btnCores = (Button) view.findViewById(R.id.btnCores);
            btnSeq = (Button) view.findViewById(R.id.btnSeq);

            return view;
        }


        @Override
        public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

            btnCores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ActivityCores.class);
                    startActivity(intent);
                }
            });

            btnSeq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ActivitySeq.class);
                    startActivity(intent);
                }
            });


        }




    }




