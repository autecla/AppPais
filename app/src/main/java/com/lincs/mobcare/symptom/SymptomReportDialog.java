package com.lincs.mobcare.symptom;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.lincs.mobcare.R;
import com.lincs.mobcare.daily.SintomaDialogFragment;

import java.util.Objects;

import static android.view.Window.FEATURE_NO_TITLE;

public class SymptomReportDialog extends DialogFragment implements View.OnClickListener{

    public static SymptomReportDialog newInstance() {
        return new SymptomReportDialog();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.report_symptom_dialog, container);
    }

    public void resizeImages(int size, View view){
        ImageView image_febre = view.findViewById(R.id.image_febre);
        image_febre.getLayoutParams().height = size;
        image_febre.getLayoutParams().width = size;
        ImageView image_vomito =view.findViewById(R.id.image_vomito);
        image_vomito.getLayoutParams().height = size;
        image_vomito.getLayoutParams().width = size;

        ImageView image_agressividade =view.findViewById(R.id.image_espasmo);
        image_agressividade.getLayoutParams().height = size;
        image_agressividade.getLayoutParams().width = size;
        ImageView image_gripe = view.findViewById(R.id.image_gripe);
        image_gripe.getLayoutParams().height = size;
        image_gripe.getLayoutParams().width = size;
        ImageView image_convulsao =view.findViewById(R.id.image_convulsao);
        image_convulsao.getLayoutParams().height = size;
        image_convulsao.getLayoutParams().width = size;
        ImageView image_doenca = view.findViewById(R.id.image_doenca);
        image_doenca.getLayoutParams().height = size;
        image_doenca.getLayoutParams().width = size;
        ImageView image_outros=view.findViewById(R.id.image_outro);
        image_outros.getLayoutParams().height = size;
        image_outros.getLayoutParams().width = size;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Recuperar

        Button cancelar = view.findViewById(R.id.btn_cancelar_dialog_activity);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        final LinearLayout layout = view.findViewById(R.id.report_symptom_dialog);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = layout.getMeasuredWidth();
                resizeImages(width/4, view);
            }
        });

        LinearLayout menu_item_alimentacao = view.findViewById(R.id.menu_item_alimentacao);

        LinearLayout menu_item_sono =  view.findViewById(R.id.menu_item_sono);


        LinearLayout menu_item_agressividade =  view.findViewById(R.id.menu_item_agressividade);

        LinearLayout menu_item_mudanca =  view.findViewById(R.id.menu_item_mudanca);

        LinearLayout menu_item_convulsao =  view.findViewById(R.id.menu_item_convulsao);

        LinearLayout menu_item_doenca =  view.findViewById(R.id.menu_item_doenca);

        LinearLayout menu_item_outros =  view.findViewById(R.id.menu_item_outro);

        menu_item_alimentacao.setOnClickListener(this);
        menu_item_sono.setOnClickListener(this);
        menu_item_agressividade.setOnClickListener(this);
        menu_item_mudanca.setOnClickListener(this);
        menu_item_convulsao.setOnClickListener(this);
        menu_item_doenca.setOnClickListener(this);
        menu_item_outros.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.menu_item_alimentacao:
                SintomaDialogFragment hdf = SintomaDialogFragment.newInstance(R.layout.febre_dialog, R.id.menu_item_alimentacao);
                hdf.setTargetFragment(getTargetFragment(),view.getId());
                assert getFragmentManager() != null;
                hdf.show(getFragmentManager(), "febre_dialog");

                break;

            case R.id.menu_item_sono:
                SintomaDialogFragment hdv = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_sono);
                hdv.setTargetFragment(getTargetFragment(),view.getId());
                assert getFragmentManager() != null;
                hdv.show(getFragmentManager(), "choro_vomito_gripe_dialog");
                break;


            case R.id.menu_item_agressividade:
                SintomaDialogFragment hde = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_agressividade);
                hde.setTargetFragment(getTargetFragment(),view.getId());
                assert getFragmentManager() != null;
                hde.show(getFragmentManager(), "choro_vomito_gripe_dialog");
                break;

            case R.id.menu_item_mudanca:
                SintomaDialogFragment hdg = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_mudanca);
                hdg.setTargetFragment(getTargetFragment(),view.getId());
                assert getFragmentManager() != null;
                hdg.show(getFragmentManager(), "choro_vomito_gripe_dialog");
                break;

            case R.id.menu_item_doenca:
                SintomaDialogFragment hden = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_doenca);
                hden.setTargetFragment(getTargetFragment(),view.getId());
                assert getFragmentManager() != null;
                hden.show(getFragmentManager(), "choro_vomito_gripe_dialog");
                break;

            case R.id.menu_item_convulsao:
                SintomaDialogFragment hdc2 = SintomaDialogFragment.newInstance(R.layout.convulsao_dialog, R.id.menu_item_convulsao);
                hdc2.setTargetFragment(getTargetFragment(),view.getId());
                assert getFragmentManager() != null;
                hdc2.show(getFragmentManager(), "convulsao_dialog");
                break;
            case R.id.menu_item_outro:
                SintomaDialogFragment hdout = SintomaDialogFragment.newInstance(R.layout.outros_sintomas_dialog, R.id.menu_item_outro);
                hdout.setTargetFragment(getTargetFragment(),view.getId());
                assert getFragmentManager() != null;
                hdout.show(getFragmentManager(), "outro_dialog");
                break;
        }
        this.dismiss();
    }
}