package com.lincs.mobcare.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.lincs.mobcare.R;
import com.lincs.mobcare.daily.DailyAdapter;
import com.lincs.mobcare.daily.SintomaDialogFragment;

/**
 * Created by Lincs on 06/02/2017.
 */

@SuppressLint("ValidFragment")
public class ListenerFAB extends Fragment implements View.OnClickListener{

    private com.github.clans.fab.FloatingActionMenu menu;
    private FragmentActivity listener;
    private FragmentManager fm;
    private DailyAdapter adapter;

    @SuppressLint("ValidFragment")
    public ListenerFAB(View view, int fabutton, FragmentManager fm) {
        this.fm = fm;
        menu = (com.github.clans.fab.FloatingActionMenu) view.findViewById(fabutton);
        this.adapter = adapter;
    }

//    @Override
    public void onClick(View view) {

        menu.close(false);

        switch (view.getId()) {

            case R.id.menu_item_alimentacao:
                SintomaDialogFragment hdf = SintomaDialogFragment.newInstance(R.layout.febre_dialog, R.id.menu_item_alimentacao);
                hdf.setTargetFragment(getTargetFragment(),view.getId());
                hdf.show(fm, "febre_dialog");

                break;

            case R.id.menu_item_sono:
                SintomaDialogFragment hdv = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_sono);
                hdv.setTargetFragment(getTargetFragment(),view.getId());
                hdv.show(fm, "choro_vomito_gripe_dialog");
                break;

            case R.id.menu_item_choro:
                SintomaDialogFragment hdc = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_choro);
                hdc.setTargetFragment(getTargetFragment(),view.getId());
                hdc.show(fm, "choro_vomito_gripe_dialog");
                break;

            case R.id.menu_item_agressividade:
                SintomaDialogFragment hde = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_agressividade);
                hde.setTargetFragment(getTargetFragment(),view.getId());
                hde.show(fm, "choro_vomito_gripe_dialog");
                break;

            case R.id.menu_item_mudanca:
                SintomaDialogFragment hdg = SintomaDialogFragment.newInstance(R.layout.choro_vomito_gripe_dialog, R.id.menu_item_mudanca);
                hdg.setTargetFragment(getTargetFragment(),view.getId());
                hdg.show(fm, "choro_vomito_gripe_dialog");
                break;
            case R.id.menu_item_outro:
                SintomaDialogFragment hdo = SintomaDialogFragment.newInstance(R.layout.outros_sintomas_dialog, R.id.menu_item_outro);
                hdo.setTargetFragment(getTargetFragment(),view.getId());
                hdo.show(fm, "outros_sintomas_dialog");
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

}
