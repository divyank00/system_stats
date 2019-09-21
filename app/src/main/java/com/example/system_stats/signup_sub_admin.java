package com.example.system_stats;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup_sub_admin extends AppCompatActivity {

    private EditText mail2, passwd2, name2, labNo2;
    private FloatingActionButton submit2;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub_Admins");
    private CollectionReference requests = Admins.document("Sub-Admins").collection("Sub-Admins");

//    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private ProgressDialog loginProgress;

    private String loginmail, loginpass, name, lab_no, User_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_sub_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loginProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mail2 = findViewById(R.id.eT_mail2);
        passwd2 = findViewById(R.id.eT_passwd2);
        submit2 = findViewById(R.id.btn_submit2);
        name2 = findViewById(R.id.eT_name2);
        labNo2 = findViewById(R.id.eT_lab_no2);

        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginmail = mail2.getText().toString().trim();
                loginpass = passwd2.getText().toString().trim();
                name = name2.getText().toString().trim();
                lab_no = labNo2.getText().toString().trim();

                if (loginmail.isEmpty())
                    mail2.setError("Mandatory field.");
                if (loginpass.isEmpty())
                    passwd2.setError("Mandatory field.");
                else if (!loginmail.isEmpty() && !loginpass.isEmpty()) {
                    loginProgress.setMessage("Signing Up...");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    mAuth.createUserWithEmailAndPassword(loginmail, loginpass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(signup_sub_admin.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                        mail2.getText().clear();
                                        passwd2.getText().clear();
                                        name2.getText().clear();
                                        labNo2.getText().clear();
                                        loginProgress.dismiss();
                                    } else {
                                        mUser = mAuth.getCurrentUser();
                                        if (mUser != null)
                                            User_Id = mUser.getUid();
                                        Map<String, Object> data2 = new HashMap<>();
                                        data2.put("email", loginmail);
                                        data2.put("name", name);
                                        data2.put("labNo", "lab" + lab_no);
                                        data2.put("access_given", "pending");
                                        Admins.document("Sub-Admins").collection("Sub-Admins").document(User_Id).set(data2)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        sendMailVerification(mUser);
                                                        mail2.getText().clear();
                                                        passwd2.getText().clear();
                                                        name2.getText().clear();
                                                        labNo2.getText().clear();
                                                        loginProgress.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        mail2.getText().clear();
                                                        passwd2.getText().clear();
                                                        name2.getText().clear();
                                                        labNo2.getText().clear();
                                                        loginProgress.dismiss();
                                                        Toast.makeText(signup_sub_admin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
    }


    private void sendMailVerification(FirebaseUser mUser) {
        mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(signup_sub_admin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    loginProgress.dismiss();
                } else {
                    mAuth.signOut();
                    loginProgress.dismiss();
                    Intent intent = new Intent(signup_sub_admin.this, LoginActivity.class);
                    intent.putExtra("email", loginmail);
                    intent.putExtra("password", loginpass);
                    intent.putExtra("toast", "Please verify your mail and wait for the admin to give you access.");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}

