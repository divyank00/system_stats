package com.example.system_stats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class Admin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser, mUser2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub_Admins");

    private String User_Id;

    private ProgressDialog mProgress, loginProgress;

//    private EditText mail;

//    private FloatingActionButton submit;

    private Button log_out;

    private String loginmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            Intent intent = new Intent(Admin.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        setContentView(R.layout.activity_admin);

        Toast.makeText(this, "Logged in as " + mUser.getEmail(), Toast.LENGTH_SHORT).show();

//        User_Id = mUser.getUid();

//        mail = findViewById(R.id.eT_mail);

//        submit = findViewById(R.id.btn_submit);

        log_out = findViewById(R.id.log_out);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(Admin.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loginmail = mail.getText().toString().trim();
//                if (loginmail.isEmpty())
//                    mail.setError("Mandatory field.");
//                else {
//                    final FirebaseAuth mAuth2 = FirebaseAuth.getInstance();
//                    loginProgress.setMessage("Signing Up...");
//                    loginProgress.show();
//                    mAuth2.createUserWithEmailAndPassword(loginmail, "123456")
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (!task.isSuccessful()) {
//                                        mAuth2.signOut();
//                                        String errorMessage = task.getException().getMessage();
//                                        Toast.makeText(Admin.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
//                                        mail.getText().clear();
//                                        loginProgress.dismiss();
//                                    } else {
//                                        mUser2 = mAuth2.getCurrentUser();
//                                        if (mUser2 != null)
//                                            User_Id2 = mUser2.getUid();
//                                        mAuth2.signOut();
//
//                                        Map<String, Object> data = new HashMap<>();
//                                        data.put("Type", "Sub-Admin");
//                                        data.put("Appointed by", mUser.getEmail());
//                                        Sub_Admins.document(User_Id2).set(data);
//                                        loginProgress.dismiss();
////                                        Toast.makeText(Admin.this, "User added as Sub-Admin!", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                }
//            }
//        });
    }

}
