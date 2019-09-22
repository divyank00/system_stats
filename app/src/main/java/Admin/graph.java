package Admin;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.system_stats.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;

public class graph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        LineChart lineChart;

        lineChart=findViewById(R.id.lineChart);

        final ArrayList<String> xAxes=new ArrayList<>(Arrays.asList("13", "14", "15",
        "16", "17", "18",
                "19", "20", "21",
                "22"));
        final ArrayList<String> yAxes=new ArrayList<>(Arrays.asList("2.6542", "3.0948", "2.4905", "3.7311", "3.3191", "4.5125", "5.7827", "3.8885",
                "3.9115", "1.9625"));
        ArrayList<Entry> graph=new ArrayList<>();

//        final XAxis xAxis=lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                for (int i = 0 ; i < xAxes.size(); ++i) {
//                    if (yAxes.get(i).equals(value)) {
//                        return xAxes.get(i);
//                    }
//                }
//                return null;
//            }
//        });
        for(int i=0;i<xAxes.size();i++){
            graph.add(new Entry(Float.parseFloat(xAxes.get(i)), Float.parseFloat(yAxes.get(i))));
        }


        ArrayList<ILineDataSet> lineDataSets=new ArrayList<>();
        LineDataSet lineDataSets1=new LineDataSet(graph,"CPU Usage");
        lineDataSets1.setDrawCircles(false);
        lineDataSets1.setColor(Color.BLUE);

        lineDataSets.add(lineDataSets1);
        lineChart.setData(new LineData(lineDataSets));

        lineChart.setVisibleXRangeMaximum(1000);
        lineChart.setVerticalScrollBarEnabled(true);
        lineChart.fitScreen();
    }
}
