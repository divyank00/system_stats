package Admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.system_stats.R;

public class graph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

//        LineChart lineChart;

//        lineChart=findViewById(R.id.lineChart);
//
//        final ArrayList<String> xAxes=new ArrayList<>(Arrays.asList("2019-9-20 3:13:13", "2019-9-20 3:14:13", "2019-9-20 3:15:13",
//        "2019-9-20 3:16:13", "2019-9-20 3:17:13", "2019-9-20 3:18:14",
//                "2019-9-20 3:19:14", "2019-9-20 3:20:16", "2019-9-20 3:21:16",
//                "2019-9-20 3:22:16"));
//        final ArrayList<String> yAxes=new ArrayList<>(Arrays.asList("2.6542", "3.0948", "2.4905", "3.7311", "3.3191", "4.5125", "5.7827", "3.8885",
//                "3.9115", "1.9625"));
//        ArrayList<Entry> graph=new ArrayList<>();
////        ArrayList<Entry> xAxes=new ArrayList<>();
//
////        for(int i=0;i<2;i++){
//
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
//        for(int i=0;i<xAxes.size();i++){
//            graph.add(new Entry(Float.parseFloat(xAxes.get(i)), Float.parseFloat(yAxes.get(i))));
//        }
//
//
//        ArrayList<ILineDataSet> lineDataSets=new ArrayList<>();
//        LineDataSet lineDataSets1=new LineDataSet(graph,"CPU Usage");
//        lineDataSets1.setDrawCircles(false);
//        lineDataSets1.setColor(Color.BLUE);
//
//        lineDataSets.add(lineDataSets1);
//        lineChart.setData(new LineData(lineDataSets));
//
//        lineChart.setVisibleXRangeMaximum(1000);
//        lineChart.setVerticalScrollBarEnabled(true);
//        lineChart.fitScreen();
    }
}
