package Sub_Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.system_stats.LoginActivity;
import com.example.system_stats.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Sub_Admin.PC_info.PC_info;

public class Sub_Admin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference lab = db.collection("LAB").document("LAB");

    //    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub_Admins");

    private String UId, labNo;

    private ArrayList<sub_admin_Lab_model_class> model_classList = new ArrayList<>();

    private LabItemAdapter labItemAdapter;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            Intent intent = new Intent(Sub_Admin.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        setContentView(R.layout.activity_sub__admin);
        Toast.makeText(this, "Logged in as " + mUser.getEmail(), Toast.LENGTH_SHORT).show();
        UId = mUser.getUid();

        Sub_Admins
                .document(UId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        labNo = (String) documentSnapshot.get("labNo");
                        if (labNo != null) {
                            lab.collection(labNo)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            if (queryDocumentSnapshots != null) {
                                                model_classList.clear();
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                    model_classList.add(new sub_admin_Lab_model_class(queryDocumentSnapshot.getId()));

                                                    RecyclerView recyclerView = findViewById(R.id.rV_subAdmin);
                                                    recyclerView.setHasFixedSize(true);
                                                    LinearLayoutManager linearLayout = new LinearLayoutManager(Sub_Admin.this);
                                                    linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
                                                    recyclerView.setLayoutManager(linearLayout);

                                                    LabItemAdapter labItemAdapter = new LabItemAdapter(model_classList);

                                                    labItemAdapter.notifyDataSetChanged();
                                                    recyclerView.setAdapter(labItemAdapter);

                                                    labItemAdapter.setOnItemClickLIstener(new LabItemAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onClick(String PCno, int position) {
                                                            Intent intent = new Intent(Sub_Admin.this, PC_info.class);
                                                            intent.putExtra("Labno", labNo);
                                                            intent.putExtra("PCno", PCno);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });

//        Sub_Admins
//                .document(UId)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        labNo = (String) documentSnapshot.get("labNo");
//                        if (labNo != null) {
//                            lab.collection(labNo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                @Override
//                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
//
//                                        model_classList.add(new sub_admin_Lab_model_class(queryDocumentSnapshot.getId()));
//
//                                        RecyclerView recyclerView = findViewById(R.id.rV_subAdmin);
//                                        recyclerView.setHasFixedSize(true);
//                                        LinearLayoutManager linearLayout = new LinearLayoutManager(Sub_Admin.this);
//                                        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
//                                        recyclerView.setLayoutManager(linearLayout);
//
//                                        LabItemAdapter labItemAdapter = new LabItemAdapter(model_classList);
//
//                                        labItemAdapter.notifyDataSetChanged();
//                                        recyclerView.setAdapter(labItemAdapter);
//
//                                        labItemAdapter.setOnItemClickLIstener(new LabItemAdapter.OnItemClickListener() {
//                                            @Override
//                                            public void onClick(String PCno, int position) {
//                                                Intent intent = new Intent(Sub_Admin.this, PC_info.class);
//                                                intent.putExtra("Labno", labNo);
//                                                intent.putExtra("PCno", PCno);
//                                                startActivity(intent);
//                                            }
//                                        });
//                                    }
//                                }
//                            })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(Sub_Admin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        }
//                    }
//                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                mAuth.signOut();
                Intent intent = new Intent(Sub_Admin.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
