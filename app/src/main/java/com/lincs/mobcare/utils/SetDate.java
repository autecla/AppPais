package com.lincs.mobcare.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class SetDate implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private EditText editText;
    private EditText editTextT;
    private Context ctx;

    public SetDate(EditText editText, EditText editTextT, Context ctx) {
        this.editText = editText;
        this.editTextT = editTextT;
        this.editText.requestFocus();
        this.editText.setOnClickListener(this);
        this.ctx = ctx;

        String dataS = editText.getText().toString().trim();
        String[] data = dataS.substring(0,10).trim().split("/");
        new DatePickerDialog(ctx, this, Integer.valueOf(data[2]), Integer.valueOf(data[1])-1, Integer.valueOf(data[0])).show();
    }

    @Override
    public void onClick(View view) {
        String dataS = editText.getText().toString().trim();
        String[] data = dataS.substring(0,10).trim().split("/");
        new DatePickerDialog(ctx, this, Integer.valueOf(data[2]), Integer.valueOf(data[1])-1, Integer.valueOf(data[0])).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            String dataS = editText.getText().toString().trim();
            String[] data = dataS.substring(0,10).trim().split("/");
            new DatePickerDialog(ctx, this, Integer.valueOf(data[2]), Integer.valueOf(data[1])-1, Integer.valueOf(data[0])).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String horaS = editTextT.getText().toString().trim();
        String[] hora = horaS.substring(0,5).trim().split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hora[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(hora[1]));

        if (calendar.getTime().getTime() > Calendar.getInstance().getTime().getTime()) {
            Toast.makeText(ctx, "Impossível setar data e hora maior que o atual.", Toast.LENGTH_SHORT).show();
        } else {

                this.editText.setText(TimeUtils.convertDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.YEAR), 0,0).substring(0,11));
        }
    }
}