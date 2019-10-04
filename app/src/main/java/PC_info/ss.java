package PC_info;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.system_stats.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ss extends AppCompatActivity {

    private ImageView ss_full;

    private PhotoViewAttacher photoViewAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_ss);

        Intent intentThatStartedThisActivity=getIntent();
        String path=intentThatStartedThisActivity.getStringExtra("path");

        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
        ss_full=findViewById(R.id.ss_full);

        Picasso.get().load(path).into(ss_full);

        photoViewAttacher=new PhotoViewAttacher(ss_full);
    }
}
