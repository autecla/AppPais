package com.lincs.mobcare.evolution;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lincs.mobcare.R;
import com.lincs.mobcare.utils.Firebase;
import com.lincs.mobcare.utils.Snapshot;
import com.lincs.mobcare.utils.VideoFromFirebaseActivity;
import com.lincs.mobcare.models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EvolutionAdapter extends RecyclerView.Adapter<EvolutionAdapter.ViewHolder> {

    public ArrayList<Registra> checked;
    public ArrayList<ViewHolder> positions2;

    //private static ArrayList<CheckBox> checkboxes;

    private static OnItemClickListener listener;
    private static OnCheckedChangeListener changeListener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(CompoundButton compoundButton, boolean isChecked);
    }

    /*public void setOnItemClickListener(OnItemClickListener listener) {
        EvolutionAdapter.listener = listener;
    }*/

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        changeListener = onCheckedChangeListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public String id;
        TextView nameTextView, comment, comment_2;
        public CheckBox evolutionCheckBox, evolutionCheckBox2;
        Button confirmarButton;
        ImageView videoButton, btnExpand, btnCollapse, comment_image, comment_image_2;
        public LinearLayout expanded, items, item, comment_linear_layout, comment_linear_layout_2;
        public LayoutInflater inflater;

        public ViewHolder(final View itemView) {
            super(itemView);

            nameTextView =itemView.findViewById(R.id.evolution_name);
            comment = itemView.findViewById(R.id.comment_exercise);
            comment_2 = itemView.findViewById(R.id.comment_2);
            comment_image =itemView.findViewById(R.id.comment_image);
            comment_image_2 =itemView.findViewById(R.id.comment_image_2);
            evolutionCheckBox =itemView.findViewById(R.id.evolution_check);
            evolutionCheckBox2 = itemView.findViewById(R.id.evolution_check_2);
            confirmarButton = itemView.findViewById(R.id.btn_evolution_confirmar);
            videoButton =  itemView.findViewById(R.id.btn_video);
            btnExpand = itemView.findViewById(R.id.btn_expand);
            btnCollapse =  itemView.findViewById(R.id.btn_collapse);
            expanded =  itemView.findViewById(R.id.expanded);
            items =  itemView.findViewById(R.id.items);
            comment_linear_layout =  itemView.findViewById(R.id.comment_linear_layout);
            comment_linear_layout_2 =  itemView.findViewById(R.id.comment_linear_layout_2);
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });

            evolutionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (changeListener != null) {
                        changeListener.onCheckedChange(compoundButton, isChecked);
                    }
                }
            });
        }
    }

    private List<Registra> mEvolutions;
    private Context mContext;
    private EvolutionFragment mActivity;
    private ViewGroup viewGroup;

    EvolutionAdapter(List<Registra> mEvolutions, Context mContext, EvolutionFragment mActivity) {
        this.mEvolutions = mEvolutions;
        this.mContext = mContext;
        this.mActivity = mActivity;
        checked = new ArrayList<>();
        positions2 = new ArrayList<>();
        //checkboxes = new ArrayList<>();
    }

    public Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        viewGroup=parent;
        View evolutionView = inflater.inflate(R.layout.item_evolution, parent, false);
        return new ViewHolder(evolutionView);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Registra evolution = mEvolutions.get(position);
        final Exercicio ev = Snapshot.getExercicio().get(evolution.id_exercicio);
        if (ev!=null) {
            if (ev.titulo.length() >= 21 && ev.titulo.contains(" ")) {
                String exercicio_title = ev.titulo.substring(0, 17) + "...";
                holder.nameTextView.setText(exercicio_title);
                holder.nameTextView.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());
                        alertDialogBuilder.setTitle("Exercício");
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {dialogInterface.dismiss();}
                        });

                        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        assert (getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)) != null;
                        final View view = ((LayoutInflater) Objects.requireNonNull(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                                .inflate(R.layout.exercicio_nome_dialog, viewGroup,false);
                        final TextView textView = view.findViewById(R.id.exercicio_nome);
                        String title = ev.titulo;
                        textView.setText(title);
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getContext().getResources()
                                        .getColor(R.color.cyan_500));
                            }
                        });
                        alertDialog.setView(view);//add your own xml with defied with and height of videoview
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
            } else {
                holder.nameTextView.setText(ev.titulo);
            }

            for (Realiza realiza : Firebase.getRealizaHoje()) {
                if (realiza.id_exercicio.equals(evolution.id_exercicio) && realiza.status) {
                    holder.evolutionCheckBox.setChecked(true);
                    holder.evolutionCheckBox2.setChecked(true);
                    holder.evolutionCheckBox.setEnabled(false);
                    holder.evolutionCheckBox2.setEnabled(false);
                    holder.id = realiza.id;
                }
            }

            //ao inves de texto agora é imagem
            switch (ev.especialidade) {
                case "Estimulação Auditiva":
                    holder.videoButton.setImageResource(R.mipmap.ic_ear);
                    break;
                case "Estimulação Visual":
                    holder.videoButton.setImageResource(R.mipmap.ic_eye);
                    break;
                case "Estimulação Motora":
                    holder.videoButton.setImageResource(R.mipmap.ic_body);
                    break;
                case "Estimulação Cognitiva":
                    holder.videoButton.setImageResource(R.mipmap.ic_body);
                    break;
            }

            //add video links
            for (final Video video : Snapshot.getVideoFromExercicio(ev.id)) {
                if (video != null) {
                    View view = holder.inflater.inflate(R.layout.item_evolution_row, viewGroup,false);
                    TextView text = view.findViewById(R.id.text);
                    String nome_video = "" + video.arquivo;
                    text.setText(nome_video);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (video.url_video!=null) {
                                mContext.startActivity(new Intent(mContext, VideoFromFirebaseActivity.class).
                                        putExtra("url_video", video.url_video));
                            }
                        }
                    });

                    holder.items.addView(view);
                }
            }
            Snapshot.setAdapterChanged(false);
        }
        else {

            String exercicio_title ="Carregando dados...";
            holder.nameTextView.setText(exercicio_title);
            holder.nameTextView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());
                    alertDialogBuilder.setTitle("Exercício");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });

                    final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    assert (getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)) != null;
                    final View view = ((LayoutInflater) Objects.requireNonNull(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                            .inflate(R.layout.exercicio_nome_dialog, viewGroup,false);
                    final TextView textView = view.findViewById(R.id.exercicio_nome);
                    String title ="Atualize a página!";
                    textView.setText(title);
                    alertDialog.setView(view);//add your own xml with defied with and height of videoview
                    alertDialog.setCancelable(false);
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.cyan_500));
                        }
                    });
                    alertDialog.show();

                    alertDialog.getButton(android.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                }
            });
            holder.evolutionCheckBox.setChecked(false);
            holder.evolutionCheckBox2.setChecked(false);
            holder.evolutionCheckBox.setEnabled(false);
            holder.evolutionCheckBox2.setEnabled(false);
            holder.btnExpand.setVisibility(View.GONE);
            holder.comment_linear_layout.setVisibility(View.GONE);
            holder.evolutionCheckBox.setVisibility(View.GONE);
            holder.expanded.setVisibility(View.GONE);
            Snapshot.setAdapterChanged(true);

        }
        holder.btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnExpand.setVisibility(View.GONE);
                holder.comment_linear_layout.setVisibility(View.GONE);
                holder.evolutionCheckBox.setVisibility(View.GONE);
                holder.expanded.setVisibility(View.VISIBLE);
            }
        });

        holder.btnCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnExpand.setVisibility(View.VISIBLE);
                holder.comment_linear_layout.setVisibility(View.VISIBLE);
                holder.evolutionCheckBox.setVisibility(View.VISIBLE);
                holder.expanded.setVisibility(View.GONE);
            }
        });

        holder.evolutionCheckBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.evolutionCheckBox2.isChecked()) {
                    holder.evolutionCheckBox.setChecked(true);
                    checked.add(evolution);
                    positions2.add(holder);
                } else {
                    holder.evolutionCheckBox.setChecked(false);
                    checked.remove(evolution);
                    positions2.remove(holder);
                }
            }
        });

        holder.evolutionCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.evolutionCheckBox.isChecked()) {
                    holder.evolutionCheckBox2.setChecked(true);
                    checked.add(evolution);
                    positions2.add(holder);
                } else {
                    holder.evolutionCheckBox2.setChecked(false);
                    checked.remove(evolution);
                    positions2.remove(holder);
                }
            }
        });

        final Realiza realiza = Snapshot.getRealiza().get(holder.id);

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (realiza != null) {
                    new EvolutionAlertDialogComment(mContext, mActivity, holder, realiza);
                }
                else {
                    Toast.makeText(mContext,"Clique no quadrado da atividade para comentar!",Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.comment_linear_layout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (realiza != null) {
                  new EvolutionAlertDialogComment(mContext, mActivity, holder, realiza);
                }
                else {
                    Toast.makeText(mContext,"Clique no quadrado da atividade para comentar!",Toast.LENGTH_LONG).show();
                }
            }
        });

        if(realiza!=null) {
            if (realiza.comentario_texto != null || realiza.local_audio != null || realiza.local_video != null) {
                String editComment="Editar/Remover";
                holder.comment.setText(editComment);
                holder.comment_2.setText(editComment);
                holder.comment_image.setVisibility(View.VISIBLE);
                holder.comment_image_2.setVisibility(View.VISIBLE);
            }
            if (realiza.local_video != null && realiza.comentario_video == null) {
                new EvolutionAlertDialogComment(mContext, mActivity, holder, realiza);
            }
            if (realiza.local_audio != null && realiza.comentario_audio == null) {
                new EvolutionAlertDialogComment(mContext, mActivity, holder, realiza);
            }
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mEvolutions.size();
    }

    public void setAppointmentList(List<Registra> appointmentList) {
        this.mEvolutions.clear();
        this.mEvolutions = appointmentList;
    }

}