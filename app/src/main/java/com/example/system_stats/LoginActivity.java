package com.example.system_stats;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private EditText mail, passwd, mail2, passwd2, name2, labNo2;
    private FloatingActionButton submit, submit2;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub-Admins");

//    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private ProgressDialog loginProgress, mProgress;

    private String loginmail, loginpass, name, lab_no, User_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intentThatStartedThisActivity = getIntent();
        String mail_intent = intentThatStartedThisActivity.getStringExtra("email");
        String password_intent = intentThatStartedThisActivity.getStringExtra("password");
        String toast_intent = intentThatStartedThisActivity.getStringExtra("toast");

        mProgress = new ProgressDialog(this);
        loginProgress = new ProgressDialog(this);

        mProgress.setMessage("Please Wait!");
        mProgress.show();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            User_Id = mUser.getUid();

            Admins.document(User_Id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                mProgress.dismiss();
                                Intent intent = new Intent(LoginActivity.this, Admin.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgress.dismiss();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            Sub_Admins.document(User_Id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                mProgress.dismiss();
                                Intent intent = new Intent(LoginActivity.this, Sub_Admin.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgress.dismiss();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Admins.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                mProgress.dismiss();
                                Intent intent = new Intent(LoginActivity.this, Admin_signup.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
//                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                                    Map<String,Object> data=new HashMap<>();
//                                    if(documentSnapshot.getData().get("E-mail verified")=="true"){
//                                        mProgress.dismiss();
//                                        break;
//                                    }
//                                    else{
//                                        mProgress.dismiss();
//                                        Intent intent = new Intent(LoginActivity.this, Admin_signup.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(intent);
//                                    }
//                                }
                                mProgress.dismiss();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgress.dismiss();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            mail = findViewById(R.id.eT_mail);
            passwd = findViewById(R.id.eT_passwd);
            submit = findViewById(R.id.btn_submit);

            mail2 = findViewById(R.id.eT_mail2);
            passwd2 = findViewById(R.id.eT_passwd2);
            submit2 = findViewById(R.id.btn_submit2);
            name2 = findViewById(R.id.eT_name2);
            labNo2 = findViewById(R.id.eT_lab_no2);

            if (mail_intent != null && password_intent != null && toast_intent != null) {
                mail.setText(mail_intent);
                passwd.setText(password_intent);
                Toast.makeText(LoginActivity.this, toast_intent, Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginmail = mail.getText().toString().trim();
                    loginpass = passwd.getText().toString().trim();
                    if (loginmail.isEmpty())
                        mail.setError("Mandatory field.");
                    if (loginpass.isEmpty())
                        passwd.setError("Mandatory field.");
                    else if (!loginmail.isEmpty() && !loginpass.isEmpty()) {
                        loginProgress.setMessage("Logging in...");
                        loginProgress.setCanceledOnTouchOutside(false);
                        loginProgress.show();

                        mAuth.signInWithEmailAndPassword(loginmail, loginpass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            String errorMessage = task.getException().getMessage();
                                            Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                            passwd.getText().clear();
                                            loginProgress.dismiss();
                                        } else {
                                            mUser = mAuth.getCurrentUser();
                                            if (mUser != null && mUser.isEmailVerified()) {
                                                User_Id = mUser.getUid();
                                                Admins.document(User_Id).get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                if (documentSnapshot.exists()) {
                                                                    loginProgress.dismiss();
                                                                    Intent intent = new Intent(LoginActivity.this, Admin.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                loginProgress.dismiss();
                                                            }
                                                        });
                                                Sub_Admins.document(User_Id).get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                if (documentSnapshot.exists()) {
                                                                    loginProgress.dismiss();
                                                                    Intent intent = new Intent(LoginActivity.this, Sub_Admin.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                loginProgress.dismiss();
                                                            }
                                                        });
                                            } else {
                                                loginProgress.dismiss();
                                                Toast.makeText(LoginActivity.this, "Please verify your e-mail.", Toast.LENGTH_SHORT).show();
                                                mAuth.signOut();
                                            }
                                        }
                                    }
                                });
                    }
                }
            });

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
                                            Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                            mail2.getText().clear();
                                            passwd2.getText().clear();
                                            name2.getText().clear();
                                            labNo2.getText().clear();
                                            loginProgress.dismiss();
                                        } else {
                                            mUser = mAuth.getCurrentUser();
                                            if (mUser != null)
                                                User_Id = mUser.getUid();
//                                            Map<String, Object> data = new HashMap<>();
//                                            data.put("admin", Boolean.FALSE);
//                                            data.put("name", name);
//                                            data.put("labNo.", lab_no);
//                                            Sub_Admins.document(User_Id).set(data)
//                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void aVoid) {
                                                            Map<String, Object> data2 = new HashMap<>();
                                                            data2.put("email", loginmail);
                                                            data2.put("name", name);
                                                            data2.put("labNo", lab_no);
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
                                                                            Toast.makeText(LoginActivity.this, "Please verify your mail and wait for the admin to give you access.", Toast.LENGTH_LONG).show();
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
                                                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
//                                                        }
//                                                    })
//                                                    .addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            mail2.getText().clear();
//                                                            passwd2.getText().clear();
//                                                            name2.getText().clear();
//                                                            labNo2.getText().clear();
//                                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }

    private void sendMailVerification(FirebaseUser mUser) {
        mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    loginProgress.dismiss();
                } else {
                    mAuth.signOut();
                    loginProgress.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
    }
}
