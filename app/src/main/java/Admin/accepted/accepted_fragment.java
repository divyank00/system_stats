package Admin.accepted;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.system_stats.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import Admin.Admin;
import Admin.pending.itemAdapter;
import Admin.pending.model_class;
import Sub_Admin.Sub_Admin;


public class accepted_fragment extends Fragment {

    private RecyclerView recyclerView;
    private accepteditemAdapter accepteditemAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub_Admins");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accepted_fragment,null,true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView=view.findViewById(R.id.rV2);

        Query query = Sub_Admins
                .orderBy("labNo", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<model_class> firebaseRecyclerAdapter = new FirestoreRecyclerOptions.Builder<model_class>()
                .setQuery(query, model_class.class)
                .build();

        accepteditemAdapter = new accepteditemAdapter(firebaseRecyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        accepteditemAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(accepteditemAdapter);

        accepteditemAdapter.setOnItemClickLIstener(new accepteditemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(DocumentSnapshot documentSnapshot, int position) {
                Sub_Admins.document(documentSnapshot.getId()).delete();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        accepteditemAdapter.startListening();
    }

    @Override
    public void onStop() {
        accepteditemAdapter.stopListening();
        super.onStop();
    }
}
