package com.lincs.mobcare.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.lincs.mobcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class LoadImg {

    public static void loadImage(String url, ImageView imageView, Context context){
        Picasso.with(context).load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).transform(new CircleTransform()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("Img","img carregada!");
            }

            @Override
            public void onError() {
                Log.d("Img","img não foi carregada!");
            }
        });
    }
}
