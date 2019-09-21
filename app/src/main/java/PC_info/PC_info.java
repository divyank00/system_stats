package PC_info;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.pusher.pushnotifications.PushNotifications;


import java.util.Calendar;
import java.util.List;

public class PC_info extends AppCompatActivity {
    private TextView total, used, available, cpu_brand, cpu_mnf, cpu_spd, cpu_usage, mnf_brand, mnf_model, platorm, uptime, status;
    private Button shut_down;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference lab = db.collection("LAB").document("LAB");
    private DocumentReference pc;

    //    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub_Admins");

    private String a;

    private List<String> list;

    String upTime1 = null, upTime2 = null;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc_info);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        total = findViewById(R.id.tV2);
        available = findViewById(R.id.tV4);
        used = findViewById(R.id.tV6);
        cpu_brand = findViewById(R.id.tV8);
        cpu_spd = findViewById(R.id.tV10);
        cpu_usage = findViewById(R.id.tV12);
        cpu_mnf = findViewById(R.id.tV14);
        mnf_brand = findViewById(R.id.tV16);
        mnf_model = findViewById(R.id.tV18);
        platorm = findViewById(R.id.tV20);
        shut_down = findViewById(R.id.shutDown);
        uptime = findViewById(R.id.tV22);
        status = findViewById(R.id.tV24);

        Intent intentThatStartedThisActivtiy = getIntent();
        String PC = intentThatStartedThisActivtiy.getStringExtra("PCno");
        String Lab = intentThatStartedThisActivtiy.getStringExtra("Labno");
        assert Lab != null;
        Toast.makeText(this, Lab.toUpperCase() + ":  " + PC, Toast.LENGTH_SHORT).show();

        status.setText("Processing...");

        assert PC != null;
        pc = lab.collection(Lab.toLowerCase()).document(PC.toLowerCase());

        runnable.run();

        pc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                    Toast.makeText(PC_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                else {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        total.setText((String) documentSnapshot.get("Total_memory"));
                        available.setText((String) documentSnapshot.get("Available_memory"));
                        used.setText((String) documentSnapshot.get("Used_memory"));
                        cpu_brand.setText((String) documentSnapshot.get("cpu_brand"));
                        cpu_spd.setText((String) documentSnapshot.get("cpu_speed"));
                        cpu_usage.setText(String.valueOf(documentSnapshot.get("cpu_usage")));
                        cpu_mnf.setText((String) documentSnapshot.get("cpu_mnf"));
                        mnf_brand.setText((String) documentSnapshot.get("cpu_brand"));
                        mnf_model.setText((String) documentSnapshot.get("mnf_model"));
                        platorm.setText((String) documentSnapshot.get("platform"));
                        uptime.setText(String.valueOf(documentSnapshot.get("upTime")));
                    }
                }
            }
        });

//        shut_down.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pc.update("shutDown",Boolean.TRUE);
//                Toast.makeText(PC_info.this, PC+" is being shutting down.", Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Calendar cal = Calendar.getInstance();
            final String crntHr = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
            final String crntMin = String.valueOf(cal.get(Calendar.MINUTE));
            String crntSec = String.valueOf(cal.get(Calendar.SECOND));
            pc.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String sysTime = (String) documentSnapshot.get("sysTime");
                            String[] Time;
                            if (sysTime != null) {
                                Time = sysTime.split(":");
                                if (crntHr.equals(Time[0])) {
                                    if (crntMin.equals(Time[1])) {
                                        status.setText("PC is active!");
                                    } else {
                                        status.setText("PC is inactive!");
                                    }
//                                    if (Math.abs(Integer.parseInt(crntMin) - Integer.parseInt(Time[1])) <= 1) {
//                                        status.setText("PC is active!");
//                                    } else {
//                                        status.setText("PC is inactive!");
//                                    }
//                                } else if (Math.abs(Integer.valueOf(crntHr) - Integer.valueOf(Time[0])) == 1) {
//                                    if (Math.abs(Integer.parseInt(crntMin) - Integer.parseInt(Time[1])) <= 60 && Math.abs(Integer.parseInt(crntMin) + Integer.parseInt(Time[1])) >= 59) {
//                                        status.setText("PC is active!");
//                                    } else if (Math.abs(Integer.parseInt(crntMin) - Integer.parseInt(Time[1])) >= 0 && Math.abs(Integer.parseInt(crntMin) - Integer.parseInt(Time[1])) <= 1) {
//                                        status.setText("PC is active!");
//                                    } else {
//                                        status.setText("PC is inactive!");
//                                    }
//                                } else {
//                                    status.setText("PC is inactive!");
//                                }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PC_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            handler.postDelayed(this, 5000);
        }
    };

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(runnable);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        runnable.run();
    }
}

