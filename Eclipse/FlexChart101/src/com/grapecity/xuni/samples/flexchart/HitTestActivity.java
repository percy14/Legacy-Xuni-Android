package com.grapecity.xuni.samples.flexchart;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;

import com.grapecity.xuni.core.IEventHandler;
import com.grapecity.xuni.flexchart.ChartSeries;
import com.grapecity.xuni.flexchart.FlexChart;
import com.grapecity.xuni.flexchart.FlexChartHitTestInfo;

public class HitTestActivity extends Activity
{

	private FlexChart mChart;
	private TextView mHitTestInfo;

	private String chartElement;
	private String chartElementNone;
	private String pointIndex;
	private String seriesName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hit_test);

		// initializing widgets
		mChart = (FlexChart) findViewById(R.id.flexchart);
		mHitTestInfo = (TextView) findViewById(R.id.hitTestInfo);

		mChart.getAxisY().setFormat("#.##");

		chartElement = this.getResources().getString(R.string.hitTestChartElement);
		chartElementNone = this.getResources().getString(R.string.hitTestChartElementNone);
		pointIndex = this.getResources().getString(R.string.hitTestPointIndex);
		seriesName = this.getResources().getString(R.string.hitTestSeriesName);
		
		// initialize series elements and set the binding to variables of
		// ChartPoint
		ChartSeries seriesSine = new ChartSeries(mChart, "sin(x)", "y");
		ChartSeries seriesCosine = new ChartSeries(mChart, "cos(x)", "y");

		// setup individual series item source
		int len = 40;
		List<PointF> listCosTuple = new ArrayList<PointF>();
		List<PointF> listSinTuple = new ArrayList<PointF>();

		for (int i = 0; i < len; i++)
		{
			listCosTuple.add(new PointF(i, (float) Math.cos(0.12 * i)));
			listSinTuple.add(new PointF(i, (float) Math.sin(0.12 * i)));
		}

		seriesSine.setItemsSource(listSinTuple);
		seriesCosine.setItemsSource(listCosTuple);

		// add series to list
		mChart.getSeries().add(seriesCosine);
		mChart.getSeries().add(seriesSine);

		// properties set in XML layout
		// mChart.setChartType(ChartType.LINESYMBOLS);

		// handler to override the existing call() which is called before
		// rendering each chart element and customize the required elements

		// mChart.getOnTouchListeners().add(new BaseOnFlexChartTouchListener(mChart){
		//
		// @Override
		// public void onTouch(MotionEvent event, FlexChartHitTestInfo info)
		// {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// });

		mChart.getTapped().addHandler(new IEventHandler()
		{
			@Override
			public void call(Object arg0, Object arg1)
			{
				Point p = (Point) arg1;

				FlexChartHitTestInfo info = mChart.hitTest(p.x, p.y);
				// display hitTestInfo for each touch event
				String displayText;
				if (info != null)
				{
					displayText = chartElement + (info.chartElement == null ? chartElementNone : info.chartElement.toString());
					if (info.dataPoint != null)
					{
						displayText += "\n"+seriesName + info.dataPoint.seriesName + "\n" + pointIndex + info.dataPoint.pointIndex;
						if (info.dataPoint.xValue != null && info.dataPoint.yValue != null)
						{
							displayText += "\nX : " + info.dataPoint.xValue.toString() + " Y : " + info.dataPoint.yValue.toString();
						}
					}
				}
				else
				{
					displayText = "Well, this is not happening..";
				}
				mHitTestInfo.setText(displayText);
			}
		}, this);

	}
}