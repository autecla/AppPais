package com.lincs.mobcare.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SetTime implements View.OnClickListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

    private EditText editTextD;
    private EditText editText;
    private Context ctx;

    public SetTime(EditText editTextD, EditText editText, Context ctx) {
        this.editTextD = editTextD;
        this.editText = editText;
        this.editText.requestFocus();
        this.editText.setOnClickListener(this);
        this.ctx = ctx;

        String horaS = editText.getText().toString().trim();
        String[] hora = horaS.substring(0,5).trim().split(":");
        new TimePickerDialog(ctx, this, Integer.valueOf(hora[0]), Integer.valueOf(hora[1]), true).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        String dataS = editTextD.getText().toString().trim();
        String[] data = dataS.substring(0,10).trim().split("/");
        calendar.set(Integer.valueOf(data[2]),Integer.valueOf(data[1])-1,Integer.valueOf(data[0]));

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        if (calendar.getTime().getTime() > Calendar.getInstance().getTime().getTime()) {
            Toast.makeText(ctx, "Impossível setar data e hora maior que a atual.", Toast.LENGTH_SHORT).show();
        } else {
                this.editText.setText(TimeUtils.convertHour(hourOfDay, minute));
        }
    }

    @Override
    public void onClick(View view) {
        String horaS = editText.getText().toString().trim();
        String[] hora = horaS.substring(0,5).trim().split(":");
        new TimePickerDialog(ctx, this, Integer.valueOf(hora[0]), Integer.valueOf(hora[1]), true).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            String horaS = editText.getText().toString().trim();
            String[] hora = horaS.substring(0,5).trim().split(":");
            new TimePickerDialog(ctx, this, Integer.valueOf(hora[0]), Integer.valueOf(hora[1]), true).show();
        }
    }
}