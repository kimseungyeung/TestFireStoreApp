package com.example.testfirestoreapp.Activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;



import com.example.testfirestoreapp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;


import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    //    private PieChart chart;
    private BarChart chart;
    Typeface tfLight = Typeface.DEFAULT;
    Typeface tf1 = Typeface.MONOSPACE;
    String[] test = {"반려", "미결", "종결", "결제"};
    String[] test2 = {"월", "화", "수", "목","금"};
    float[] num={0f,10f,20f,30f,40f,50f};
    public static final int[] colorlist = {
            Color.rgb(219, 0, 0), Color.rgb(255, 94, 0), Color.rgb(47, 157, 39),
            Color.rgb(1, 0, 255), Color.rgb(255, 0, 127)
    };
    List<String> ss;
    //pie 차트
    private PieChart chart2;
    private CombinedChart chart3;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;
    private final int count =5;
    Button btn_pie,btn_bar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart1_activity);

        piechartset();
        setPieData(4, 100);
      //  barchartset();
        btn_bar=(Button)findViewById(R.id.btn_bar);
        btn_pie=(Button)findViewById(R.id.btn_pie);
        btn_pie.setOnClickListener(this);
        btn_bar.setOnClickListener(this);
        combinechart();
    }

        public void piechartset(){
        chart2 = findViewById(R.id.chart1);
        chart2.setUsePercentValues(true);
        chart2.getDescription().setEnabled(false);
        chart2.setExtraOffsets(5, 10, 5, 5);

        chart2.setDragDecelerationFrictionCoef(0.95f);

        chart2.setCenterTextTypeface(tfLight);
        chart2.setCenterText("사원 김승영");

        chart2.setDrawHoleEnabled(true);
        chart2.setHoleColor(Color.WHITE);

        chart2.setTransparentCircleColor(Color.WHITE);
        chart2.setTransparentCircleAlpha(110);

        chart2.setHoleRadius(58f);
       chart2.setTransparentCircleRadius(61f);

        chart2.setDrawCenterText(true);

        chart2.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart2.setRotationEnabled(true);
        chart2.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart2.setOnChartValueSelectedListener(this);



        chart2.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        com.github.mikephil.charting.components.Legend l = chart2.getLegend();
        l.setVerticalAlignment(com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart2.setEntryLabelColor(Color.WHITE);
        chart2.setEntryLabelTypeface(tf1);
        chart2.setEntryLabelTextSize(12f);
    }
    public void barchartset() {


        chart = (BarChart) findViewById(R.id.chart2);

        Description desc;
        Legend L;

        L = chart.getLegend();
        desc = chart.getDescription();
        desc.setText("사원 김승영 배당 명세 "); // this is the weirdest way to clear something!!
        L.setEnabled(false);


        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        XAxis xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new getbarvalueformat(test2));
        xAxis.setLabelCount(5);
        leftAxis.setTextSize(10f);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);

        //leftAxis.setInverted(true);

        leftAxis.setDrawLimitLinesBehindData(true);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(true);

        BarData data = new BarData(setData());

        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setDrawValueAboveBar(false);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        chart.setScaleEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setBackgroundColor(Color.rgb(255, 255, 255));
        chart.animateXY(2000, 2000);
        chart.setDrawBorders(false);
        chart.setDescription(desc);




    }

    private BarDataSet setData() {

        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0f, 17));
        entries.add(new BarEntry(1f, 20));
        entries.add(new BarEntry(2f, 40));
        entries.add(new BarEntry(3f, 5f));
        entries.add(new BarEntry(4f, 31));
        //entries.add(new BarEntry(5f, 60f));

        ArrayList<Integer> colors = new ArrayList<>();
        BarDataSet set = new BarDataSet(entries, "testtitle");
        //        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : colorlist)
            colors.add(c);
/*
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);*/

        colors.add(ColorTemplate.getHoloBlue());

        set.setColors(colors);
//        set.setColor(Color.rgb(155, 155, 155));
        set.setValueTextColor(Color.rgb(155, 155, 155));
        return set;
    }

    private void setPieData(int count, float range) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //Pieentry(값,이름,아이콘)
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
                   test[i],
                    getResources().getDrawable(R.drawable.ic_launcher_background)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "실적 명세");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : colorlist)
            colors.add(c);

    /*    for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
*/
        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart2));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tfLight);

        chart2.setData(data);

        // undo all highlights
        chart2.highlightValues(null);

        chart2.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bar:
                chart3.setVisibility(View.VISIBLE);
                chart2.setVisibility(View.GONE);

                btn_bar.setBackgroundColor(getResources().getColor(R.color.quantum_grey));
                btn_pie.setBackgroundColor(getResources().getColor(R.color.quantum_grey100));
                break;
            case R.id.btn_pie:
                chart3.setVisibility(View.GONE);
                chart2.setVisibility(View.VISIBLE);
                btn_bar.setBackgroundColor(getResources().getColor(R.color.quantum_grey100));
                btn_pie.setBackgroundColor(getResources().getColor(R.color.quantum_grey));
                break;
        }
    }


    public class getbarvalueformat extends ValueFormatter {
    private String[] labels;
    private String label;
    public getbarvalueformat(String[] d) {
        this.labels = d;
    }

    @Override
    public String getFormattedValue(float value) {
        label=labels[(int)value];
        return label;
    }
}

//컴바인차트
    public void combinechart(){
        chart3 = findViewById(R.id.chart3);
        chart3.getDescription().setEnabled(false);
        chart3.setBackgroundColor(Color.WHITE);
        chart3.setDrawGridBackground(false);
        chart3.setDrawBarShadow(false);
       // chart3.setHighlightFullBarEnabled(false);
       // chart3.animateXY(2000, 2000);
        // draw bars behind lines
        chart3.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

            //라인과 바의 분류제목 위치
        Legend l = chart3.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = chart3.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

       // rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart3.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(5);
        leftAxis.setAxisMinimum(0f); // 왼쪽바 시작순서 0,10,20 정해주기

        XAxis xAxis = chart3.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
       // xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(5,false);

      //  leftAxis.setAxisMinimum(0f);
        //xAxis.setGranularity(1f);

/*        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return week[(int)value];
            }
        });*/
        xAxis.setValueFormatter(new getbarvalueformat(test2));

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
       data.setData(generateBarData());
       // data.setData(generateBubbleData());
       // data.setData(generateScatterData());
       // data.setData(generateCandleData());
        data.setValueTypeface(tfLight);


        chart3.setData(data);
        chart3.setScaleEnabled(false);

        xAxis.setSpaceMin(0.5f);
        xAxis.setSpaceMax(0.5f);

        chart3.invalidate();
    }
    private LineData generateLineData() {

            LineData d = new LineData();

            ArrayList<Entry> entries = new ArrayList<>();

            //for (int index = 0; index < count; index++)
                entries.add(new Entry((float)0,15));
        entries.add(new Entry((float)1,34));
        entries.add(new Entry((float)2,13));
        entries.add(new Entry((float)3,23));
        entries.add(new Entry((float)4,10));

            LineDataSet set = new LineDataSet(entries, "지난 주 실적");
            set.setColor(Color.rgb(237, 76, 0));
            set.setLineWidth(2.5f);
            set.setCircleColor(Color.rgb(237, 76, 0));
            set.setCircleRadius(5f);
            set.setFillColor(Color.rgb(237, 76, 0));
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setDrawValues(true);
            set.setValueTextSize(10f);
            set.setValueTextColor(Color.rgb(237, 76, 0));

            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            d.addDataSet(set);

            return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<>();
       // ArrayList<BarEntry> entries2 = new ArrayList<>();


            entries1.add(new BarEntry( 0f, 19));
        entries1.add(new BarEntry( 1f, 37));
        entries1.add(new BarEntry( 2f, 15));
        entries1.add(new BarEntry( 3f, 23));
        entries1.add(new BarEntry( 4f, 16));
        // stacked
            //entries2.add(new BarEntry(0, new float[]{getRandom(13, 12), getRandom(13, 12)}));


        BarDataSet set1 = new BarDataSet(entries1, "이번 주 실적");
        set1.setColor(Color.rgb(0, 156, 61));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);

        //set1.setAxisDependency(YAxis.AxisDependency.LEFT);

       // BarDataSet set2 = new BarDataSet(entries2, "");
     //   set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
      //  set2.setColors(Color.rgb(61, 165, 255), Color.rgb(23, 197, 255));
      //  set2.setValueTextColor(Color.rgb(61, 165, 255));
      //  set2.setValueTextSize(10f);
       // set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        // (barSpace + barWidth) * 2 + groupSpace = 1
        float groupSpace = 0.4f;
        float barSpace = 0f; // x2 dataset
        float barWidth = 0.4f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);
        d.removeDataSet(5);
        // make this BarData object grouped
//        d.groupBars(1, groupSpace, barSpace); // start at x = 0

        return d;
    }

    private ScatterData generateScatterData() {

        ScatterData d = new ScatterData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (float index = 0; index < count; index += 0.5f)
            entries.add(new Entry(index + 0.25f, getRandom(10, 55)));

        ScatterDataSet set = new ScatterDataSet(entries, "Scatter DataSet");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setScatterShapeSize(7.5f);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        d.addDataSet(set);

        return d;
    }

    private CandleData generateCandleData() {

        CandleData d = new CandleData();

        ArrayList<CandleEntry> entries = new ArrayList<>();

        for (int index = 0; index < count; index += 2)
            entries.add(new CandleEntry(index + 1f, 90, 70, 85, 75f));

        CandleDataSet set = new CandleDataSet(entries, "Candle DataSet");
        set.setDecreasingColor(Color.rgb(142, 150, 175));
        set.setShadowColor(Color.DKGRAY);
        set.setBarSpace(0.3f);
        set.setValueTextSize(10f);
        set.setDrawValues(false);
        d.addDataSet(set);

        return d;
    }

    private BubbleData generateBubbleData() {

        BubbleData bd = new BubbleData();

        ArrayList<BubbleEntry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            float y = getRandom(10, 105);
            float size = getRandom(100, 105);
            entries.add(new BubbleEntry(index + 0.5f, y, size));
        }

        BubbleDataSet set = new BubbleDataSet(entries, "Bubble DataSet");
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.WHITE);
        set.setHighlightCircleWidth(1.5f);
        set.setDrawValues(true);
        bd.addDataSet(set);

        return bd;
    }
        @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    protected final String[] week = new String[] {
            "mon", "tue", "wen", "thur", "fri"
    };
    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }
}
