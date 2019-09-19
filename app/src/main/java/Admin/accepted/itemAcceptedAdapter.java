package Admin.accepted;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.system_stats.R;

import java.util.List;

import Admin.Admin;
import Sub_Admin.sub_admin_Lab_model_class;

public class itemAcceptedAdapter extends RecyclerView.Adapter<itemAcceptedAdapter.viewHolder> {

    private List<sub_admin_Lab_model_class> model_classList;

    public itemAcceptedAdapter(List<sub_admin_Lab_model_class> model_classList) {
        this.model_classList = model_classList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_lab_layout,parent,false);
        return new itemAcceptedAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.labNo.setText(model_classList.get(position).getPCno());

    }

    @Override
    public int getItemCount() {
        return model_classList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        private TextView labNo;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            labNo=itemView.findViewById(R.id.LabNo);
        }
    }

}
