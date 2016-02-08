package zbynek.remotehouseholdcontrol;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.graphics.Color;
import android.widget.LinearLayout;
import java.io.IOException;


import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.widget.TableLayout;
import android.os.Handler;
import android.os.Message;
import zbynek.remotehouseholdcontrol.nettools.CgiScriptCaller;
import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;

import java.lang.ref.WeakReference;

public class GraphFragment extends Fragment {

//	private TableLayout table;
//	private LayoutInflater inflater;
//	private HandleDownload h = new HandleDownload(this);
	private View mChart;
	private ConnectionCredentialsManager cm;
	
	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
	  View v = i.inflate(R.layout.graph_fragment_layout, container, false);
//	  table = (TableLayout)v.findViewById(R.id.table);
//	  inflater = i;
	  
      cm = new ConnectionCredentialsManager(getActivity());
		    int sensor_id = getResources().getInteger(R.integer.sensor_OUT_temp);
			AsyncTask< Integer, Void, String> getGraphDataTask = new AsyncTask< Integer, Void, String>() {
				@Override
				protected String doInBackground( Integer ... params) {
					Integer device = params[0];
					CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
					try {
						return scriptCaller.callCGIScriptAndGetGraphData(device);
					} catch (IOException e) {return "";
											}
				}
			};
			String graph_values;
			try {
				graph_values = getGraphDataTask.execute(sensor_id).get();
				if (graph_values !="") {   
	//			Toast.makeText(getActivity(), "GraphCgiScriptCaller result: " + result, Toast.LENGTH_LONG).show();
					  openChart(v,graph_values);
					  return v;
	   			}
			} catch (Exception e) { //collect all possible exceptions
				Toast.makeText(getActivity(),"Exception GraphTask"+e, Toast.LENGTH_LONG).show();					
				graph_values = "";
  			  }	  
			  return v;
	}

	private void openChart(View v, String graph_values){
		String[] graph_data=graph_values.split("/");
		String[] data_x = graph_data[0].split(",");
		String[] data_y = graph_data[1].split(",");
		
		// Creating an XYSeries for dataX
		XYSeries dataSeries = new XYSeries("OUT sever");
		// Adding data to Series
		double min_val=0, max_val=0;
		for(int i=0;i<data_y.length;i++){
			if (min_val > Double.parseDouble(data_y[i])) {min_val = Double.parseDouble(data_y[i]);}
			if (max_val < Double.parseDouble(data_y[i])) {max_val = Double.parseDouble(data_y[i]);}
			
		dataSeries.add(i,Double.parseDouble(data_y[i]));
		}

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset(); 		// Creating a dataset to hold each series

		dataset.addSeries(dataSeries);							// Adding Income Series to the dataset
		XYSeriesRenderer dataRenderer = new XYSeriesRenderer();	// Creating XYSeriesRenderer to customize dataSeries
		dataRenderer.setColor(Color.CYAN); 						//color of the graph set to cyan		
		dataRenderer.setFillPoints(false);
		dataRenderer.setLineWidth(1f);
		dataRenderer.setDisplayChartValues(false);
		dataRenderer.setDisplayChartValuesDistance(10);			//setting chart value distance
//		dataRenderer.setPointStyle(PointStyle.CIRCLE);			//setting line graph point style to circle
		dataRenderer.setStroke(BasicStroke.SOLID);				//setting stroke of the line chart to solid

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
//		multiRenderer.setChartTitle(min_val+"/"+max_val);
		multiRenderer.setXTitle("h");
		multiRenderer.setYTitle("°C");
		 
		//***	* Customizing graphs	
		multiRenderer.setChartTitleTextSize(28); 		//setting text size of the title
		multiRenderer.setAxisTitleTextSize(24);			//setting text size of the axis title
		multiRenderer.setLabelsTextSize(24);			//setting text size of the graph lable
		multiRenderer.setZoomButtonsVisible(true);		//setting zoom buttons visiblity
		multiRenderer.setPanEnabled(false, false);		//setting pan enablity which uses graph to move on both axis
		multiRenderer.setClickEnabled(false);			//setting click false on graph
		multiRenderer.setZoomEnabled(false, false);		//setting zoom to false on both axis
		multiRenderer.setShowGridY(true);				//setting lines to display on y axis
		multiRenderer.setShowGridX(true);				//setting lines to display on x axis
		multiRenderer.setFitLegend(true);				//setting legend to fit the screen size
		multiRenderer.setShowGrid(true);				//setting displaying line on grid
		multiRenderer.setZoomEnabled(false);			//setting zoom to false
		multiRenderer.setExternalZoomEnabled(false);	//setting external zoom functions to false
		multiRenderer.setAntialiasing(true);			//setting displaying lines on graph to be formatted(like using graphics)
		multiRenderer.setInScroll(false);				//setting to in scroll to false	
		multiRenderer.setLegendHeight(30);				//setting to set legend height of the graph
		multiRenderer.setXLabelsAlign(Align.CENTER);	//setting x axis label align
		multiRenderer.setYLabelsAlign(Align.LEFT);		//setting y axis label to align
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);		//setting text style
		multiRenderer.setYLabels(10);					//setting no of values to display in y axis

		// setting y axis max value,
		// if you use dynamic values then get the max y value and set here
		multiRenderer.setYAxisMax(max_val);
		multiRenderer.setYAxisMin(min_val);
		multiRenderer.setXAxisMin(0);				//setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMax(data_x.length);		//setting used to move the graph on xaxiz to .5 to the right
		//setting bar size or space between two bars
		//multiRenderer.setBarSpacing(0.5);
		multiRenderer.setBackgroundColor(Color.TRANSPARENT);		//Setting background color of the graph to transparent
		multiRenderer.setBackgroundColor(Color.BLACK);
		multiRenderer.setMarginsColor(getResources().getColor(R.color.transparent_background));		//Setting margin color of the graph to transparent
		multiRenderer.setApplyBackgroundColor(true);
		multiRenderer.setScale(2f);
		multiRenderer.setPointSize(4f);					//setting x axis point size
		multiRenderer.setMargins(new int[]{30, 30, 30, 30});		//setting the margin size for the graph in the order top, left, bottom, right
		for(int i=0; i< data_y.length;i++){		multiRenderer.addXTextLabel(i, data_x[i]);
		}

		// Adding dataRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer should be same
		multiRenderer.addSeriesRenderer(dataRenderer);

		//this part is used to display graph on the xml
		LinearLayout chartContainer = (LinearLayout) v.findViewById(R.id.chart);
		//remove any views before u paint the chart
		chartContainer.removeAllViews();
		//drawing bar chart
		mChart = ChartFactory.getLineChartView(getActivity(), dataset, multiRenderer);
		//adding the view to the linearlayout
		chartContainer.addView(mChart);
	}
		
/*	private static class HandleDownload extends Handler {
		private final WeakReference<GraphFragment> frag;
		public HandleDownload(GraphFragment frag) {
			this.frag = new WeakReference<GraphFragment>(frag);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Data.MSG_DOWNLOADED) {
				SimpleArrayMap<String, String> m = Data.get();
				GraphFragment f = frag.get();
//				if (m != null && f != null)
//					f.displayData(m);
			}
		}
	}*/
}