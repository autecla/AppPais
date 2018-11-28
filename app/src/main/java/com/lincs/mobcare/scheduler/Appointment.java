package com.lincs.mobcare.scheduler;

import java.io.Serializable;

public class Appointment implements Serializable{

    private String mLocal;
    private String mDoctor;
    private String mDate;
    private String mHour;
    private boolean mPresent;
    private String mType;

    public Appointment() {
    }

    public String getLocal() {
        return mLocal;
    }

    public void setLocal(String mLocal) {
        this.mLocal = mLocal;
    }

    public String getDoctor() {
        return mDoctor;
    }

    public void setDoctor(String mDoctor) {
        this.mDoctor = mDoctor;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getHour() {
        return mHour;
    }

    public void setHour(String mHour) {
        this.mHour = mHour;
    }

    public boolean isPresent() {
        return mPresent;
    }

    public void setPresent(boolean mPresent) {
        this.mPresent = mPresent;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

//    public static ArrayList<Appointment> createAppointmentsList(int numAppointments) {
//
//        ArrayList<Appointment> appointments = new ArrayList<>();
//
//        for (int i = numAppointments; i > 0; i--) {
//            Appointment appointment = new Appointment();
//            switch (i) {
//                case 1:
//                    appointment.setLocal("Consultório B23");
//                    appointment.setDoctor("Dr. Juliano Cabral");
//                    appointment.setDate("23/11/2016");
//                    appointment.setHour("08:30");
//                    appointment.setType("Oftalmologista");
//                    appointment.setPresent(true);
//                    break;
//
//                case 2:
//                    appointment.setLocal("Consultório A04");
//                    appointment.setDoctor("Dra. Carolina Macedo");
//                    appointment.setDate("23/12/2016");
//                    appointment.setHour("09:00");
//                    appointment.setType("Fonoaudióloga");
//                    appointment.setPresent(true);
//                    break;
//
//                case 3:
//                    appointment.setLocal("Consultório A04");
//                    appointment.setDoctor("Dra. Maria Oliveira");
//                    appointment.setDate("27/01/2017");
//                    appointment.setHour("09:00");
//                    appointment.setType("Terapeuta Ocupacional");
//                    break;
//
//                case 4:
//                    appointment.setLocal("Apoio 01");
//                    appointment.setDoctor("Contação de História");
//                    appointment.setDate("28/01/2017");
//                    appointment.setHour("09:00");
//                    appointment.setType("Oficina");
//                    break;
//                /*default:
//                    appointment.setLocal("Apoio 01");
//                    appointment.setDoctor("Contação de História");
//                    appointment.setDate("14/01/2017");
//                    appointment.setHour("09:00");
//                    appointment.setType("Oficina");*/
//            }
//            appointments.add(appointment);
//        }
//
//        return appointments;
//    }


//    public static void createEvolutionsListDB(int numAppointments, AppointmentHelper mDBHelper) {
    // Gets the data repository in write mode
//        SQLiteDatabase db = mDBHelper.getWritableDatabase();
//        ArrayList<ContentValues> values = new ArrayList<>();

    // Create a new map of values, where column names are the keys
//         values.add(createApp("Consultório B1", "Contação de História", "05/01/2017", "09:00", "Oficina", false));
//         values.add(createApp("Consultório B2", "Contação de História", "10/01/2017", "09:00", "Oficina", false));
//         values.add(createApp("Consultório B3", "Contação de História", "15/01/2017", "09:00", "Oficina", false));
//
//         values.add(createApp("Consultório B4", "Contação de História", "05/12/2016", "09:00", "Oficina", false));
//         values.add(createApp("Consultório B5", "Contação de História", "10/12/2016", "09:00", "Oficina", false));
//         values.add(createApp("Consultório B6", "Contação de História", "15/12/2016", "09:00", "Oficina", false));

//         values.add(createApp("Consultório B7", "Contação de História", "05/11/2016", "09:00", "Oficina", false));
//         values.add(createApp("Consultório B8", "Contação de História", "10/11/2016", "09:00", "Oficina", false));
//         values.add(createApp("Consultório B9", "Contação de História", "15/11/2016", "09:00", "Oficina", false));
//
//        values.add(createApp("Consultório B10", "Contação de História", "05/10/2016", "09:00", "Oficina", false));
//        values.add(createApp("Consultório B11", "Contação de História", "10/10/2016", "09:00", "Oficina", false));
//        values.add(createApp("Consultório B12", "Contação de História", "15/10/2016", "09:00", "Oficina", false));
//
//        values.add(createApp("Consultório B13", "Contação de História", "05/09/2016", "09:00", "Oficina", false));
//        values.add(createApp("Consultório B14", "Contação de História", "10/09/2016", "09:00", "Oficina", false));
//        values.add(createApp("Consultório B15", "Contação de História", "15/09/2016", "09:00", "Oficina", false));
//
//        values.add(createApp("Consultório B16", "Contação de História", "05/08/2016", "09:00", "Oficina", false));
//        values.add(createApp("Consultório B17", "Contação de História", "10/08/2016", "09:00", "Oficina", false));
//        values.add(createApp("Consultório B18", "Contação de História", "15/08/2016", "09:00", "Oficina", false));

    // Insert the new row, returning the primary key value of the new row

//        for (int i = 0; i < numAppointments; i++) {
//            db.insert(TableSchema.AppointmentEntry.TABLE_NAME, null, values.get(i));
//        }
//    }

//    public static ContentValues createApp(String local, String doctor, String date,
//                                   String hour, String type, Boolean present) {
//
//        ContentValues cv = new ContentValues();
//        cv.put(TableSchema.AppointmentEntry.COLUMN_NAME_LOCAL, local);
//        cv.put(TableSchema.AppointmentEntry.COLUMN_NAME_DOCTOR, doctor);
//        cv.put(TableSchema.AppointmentEntry.COLUMN_NAME_DATE, date);
//        cv.put(TableSchema.AppointmentEntry.COLUMN_NAME_HOUR, hour);
//        cv.put(TableSchema.AppointmentEntry.COLUMN_NAME_TYPE, type);
//        cv.put(TableSchema.AppointmentEntry.COLUMN_NAME_PRESENT, String.valueOf(present));
//
//        return cv;
//    }

}
