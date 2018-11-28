package com.lincs.mobcare.profile;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Acompanhante;
import com.lincs.mobcare.utils.Snapshot;

import java.util.Objects;

public class InfoAcompanhanteDialog extends DialogFragment {

    public static InfoAcompanhanteDialog newInstance() {
        return new InfoAcompanhanteDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.info_companion_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button cancelar = view.findViewById(R.id.btn_fechar_info_companion_dialog);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Acompanhante acompanhante = Snapshot.getAcompanhante();

        TextView name = view.findViewById(R.id.nome_info_angel_dialog);
        name.setText(acompanhante.nome);

        TextView phone = view.findViewById(R.id.data_info_angel_dialog);
        phone.setText(acompanhante.telefone);

        TextView relationship = view.findViewById(R.id.sexo_info_angel_dialog);
        relationship.setText(acompanhante.parentesco);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
}
