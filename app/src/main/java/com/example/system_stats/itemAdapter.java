package com.example.system_stats;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class itemAdapter extends FirestoreRecyclerAdapter<model_class, itemAdapter.itemHolder> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");

    public itemAdapter(@NonNull FirestoreRecyclerOptions<model_class> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull itemHolder holder, int position, @NonNull model_class model) {
        holder.name.setText(model.getName());
        holder.mail.setText(model.getEmail());
        holder.labNo.setText(model.getLabNo());

    }

    @NonNull
    @Override
    public itemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new itemHolder(view);
    }


    class itemHolder extends RecyclerView.ViewHolder {

        TextView name, mail, labNo;
        CheckBox yes, no;

        public itemHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            mail = itemView.findViewById(R.id.mail);
            labNo = itemView.findViewById(R.id.lab_no);
            yes = itemView.findViewById(R.id.yes);
            no = itemView.findViewById(R.id.no);

            final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setMessage("Do you wish to confirm.")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Admins.document("Sub-Admins").collection("Sub-Admins").document()
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setCancelable(false);
            if (yes.isChecked()) {
                no.setChecked(false);
            }
            else if(no.isChecked()){
                yes.setChecked(false);
            }

        }
    }
}
