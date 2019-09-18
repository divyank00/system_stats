package com.example.system_stats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class Admin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub_Admins");

    private RecyclerView recyclerView;
    //        private FirestoreRecyclerOptions firestoreRecyclerOptions;
//    private FirestoreRecyclerAdapter<model_class,holder> firestoreRecyclerAdapter;
    private itemAdapter itemAdapter;

    private String User_Id;

    private Button log_out;

//    private FirestoreRecyclerOptions<model_class>firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            Intent intent = new Intent(Admin.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Logged in as " + mUser.getEmail(), Toast.LENGTH_SHORT).show();

            CollectionReference requests = Admins.document("Sub-Admins").collection("Sub-Admins");
            Query query = requests
//                .whereEqualTo("access_given", "pending".toString())
                    .orderBy("labNo", Query.Direction.ASCENDING);
//                .whereEqualTo("access_given","pending".toString());

            FirestoreRecyclerOptions<model_class> firebaseRecyclerAdapter = new FirestoreRecyclerOptions.Builder<model_class>()
                    .setQuery(query, model_class.class)
                    .build();

            itemAdapter = new itemAdapter(firebaseRecyclerAdapter);

            log_out = findViewById(R.id.log_out);
            recyclerView = findViewById(R.id.rV);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            itemAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(itemAdapter);

            itemAdapter.setOnItemClickLIstener(new itemAdapter.OnItemClickListener() {
                @Override
                public void onYesClick(final DocumentSnapshot documentSnapshot, final int position) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Admin.this);
                    builder.setTitle("Do you confirm to grant access?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                Admins.document("Sub-Admins").collection("Sub-Admins").document(documentSnapshot.getId()).update("access_given", "yes");
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("name", documentSnapshot.get("name"));
                                    data.put("email", documentSnapshot.get("email"));
                                    data.put("labNo.", documentSnapshot.get("labNo"));
                                    Sub_Admins.document(documentSnapshot.getId()).set(data);
                                    Admins.document("Sub-Admins").collection("Sub-Admins").document(documentSnapshot.getId())
                                            .delete();
//                                        .update("access_given", "yes");
                                    Toast.makeText(Admin.this, "User added as sub-admin.", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }

                @Override
                public void onNoClick(final DocumentSnapshot documentSnapshot, int position) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Admin.this);
                    builder.setTitle("Do you confirm to deny access?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("name", documentSnapshot.get("name"));
                                    data.put("email", documentSnapshot.get("email"));
                                    data.put("labNo.", documentSnapshot.get("labNo"));
                                    db.collection("Spam").document(documentSnapshot.getId()).set(data);
                                    Admins.document("Sub-Admins").collection("Sub-Admins").document(documentSnapshot.getId())
                                            .delete();
//                                        .update("access_given", "no");
                                    Toast.makeText(Admin.this, "User was reported as spam.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });

            log_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    Intent intent = new Intent(Admin.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        itemAdapter.startListening();
    }

    @Override
    public void onStop() {
        itemAdapter.stopListening();
        super.onStop();
    }
}
