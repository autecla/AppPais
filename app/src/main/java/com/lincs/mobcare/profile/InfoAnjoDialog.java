package com.lincs.mobcare.profile;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Anjo;
import com.lincs.mobcare.utils.Snapshot;

import java.util.Objects;


public class InfoAnjoDialog extends DialogFragment {

    public static InfoAnjoDialog newInstance() {
        return new InfoAnjoDialog();
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
        return inflater.inflate(R.layout.info_angel_dialog, container);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button fechar = view.findViewById(R.id.btn_fechar_info_angel_dialog);
        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        int tabIconColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white);
        ImageView icon = view.findViewById(R.id.icon_angel);
        icon.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        Anjo anjo = Snapshot.getAnjo();

        TextView name = view.findViewById(R.id.nome_info_angel_dialog);
        name.setText(anjo.nome);

        TextView date = view.findViewById(R.id.data_info_angel_dialog);
        date.setText(anjo.data_de_nascimento);

        TextView sexo = view.findViewById(R.id.sexo_info_angel_dialog);
        sexo.setText(anjo.sexo);

        sexo = view.findViewById(R.id.sexo_info_angel_dialog);
        sexo.setText(anjo.sexo);

        TextView prontuario = view.findViewById(R.id.prontuario_info_angel_dialog);
        prontuario.setText(anjo.numero_prontuario);

        TextView cidade = view.findViewById(R.id.cidade_info_angel_dialog);
        cidade.setText(anjo.dadosGeraisAnjo.cidade);

        TextView numero_cartao_sus = view.findViewById(R.id.cartao_sus_info_angel_dialog);
        numero_cartao_sus.setText(anjo.dadosGeraisAnjo.numero_cartao_sus);

        StringBuilder terapia= new StringBuilder();
        String[] tratamentos = {"Comunicação não verbal\n", "Linguagem expressiva funcional \n", "Linguagem receptiva funcional \n",  "Apraxia de fala \n", "Alteração sensorial da alimentação\n", "Percepção e Localização sonora\n", "Ecolalias\n" +
                "Comportamentos repetitivos\n", "Oscilação de humor"};

        for (String a : tratamentos){
            terapia.append(a);
        }
        TextView terapias = view.findViewById(R.id.detail_terapeuticos);
        terapias.setText(terapia.toString());

        TextView equipeMedica = view.findViewById(R.id.detail_medicas);
        StringBuilder medicas = new StringBuilder();


        String[] equipe = {"Fonoaudiólogas (Fga):\n", "    Mila Veras\n ", "   Janiely Tinôco\n", "    Valência Marinho\n", "\nPsicólogas: \n", "    Claudia Marques\n", "    Maria Carolina\n", "    Suzan Almeida\n", "    Eliane Teles\n", "\nPsicopedagoga: \n", "    Adriana Magalhães\n", "\nFisioterapeuta: \n", "    Juliane Falcão\n"};

        for (String a : equipe){
            medicas.append(a);
        }
        equipeMedica.setText(medicas.toString());



        TextView convulsoes = view.findViewById(R.id.convulsao_info_angel_dialog);
        convulsoes.setText(anjo.dadosGeraisAnjo.convulsoes);

        TextView data_primeira_consulta_fav = view.findViewById(R.id.consulta_fav_info_angel_dialog);
        data_primeira_consulta_fav.setText(anjo.dadosGeraisAnjo.data_primeira_consulta_fav);


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
    public String retorno(boolean in){
        String aux;
        if (!in){
            aux="NÃO";
        }else{
            aux="SIM";
        }
        return aux;
    }

}
