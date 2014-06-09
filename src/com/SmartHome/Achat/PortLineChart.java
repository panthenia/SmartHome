/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.SmartHome.Achat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import com.SmartHome.DataType.EnviSensor;
import com.SmartHome.DataType.PublicState;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Average temperature demo chart.
 */
public class PortLineChart extends AbstractDemoChart {
	String envi_type;
	public PortLineChart(String type){
		super();
		envi_type=type;
	}
	
	public String getName() {
		return "环境数据";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "The average temperature in 4 Greek islands (line chart)";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public Intent execute(Context context) {
		PublicState ps = PublicState.getInstance();
		ArrayList<EnviSensor> sensor_list=ps.getSensorInfo(envi_type);

        if(sensor_list.size() == 0)//当无数据是就返回null
            return null;

		String[] titles = new String[1];
		titles[0]=ps.selected_room.name;

		List<double[]> x = new ArrayList<double[]>();
		List<double[]> values = new ArrayList<double[]>();
		double[] tx=new double[sensor_list.size()];
		double[] ty=new double[sensor_list.size()];
		for(int i=0;i<sensor_list.size();++i){
			tx[i]=i+1;
			String envi_val = sensor_list.get(i).envi_value;

            envi_val = envi_val.trim();
            if(envi_val.length()==0)
                ty[i]=0;
            else {
                boolean none_num=false;
                for(int temi=0;temi<envi_val.length();++temi)
                    if(!Character.isDigit(envi_val.charAt(temi))) {
                        none_num = true;
                    }
                if(none_num)
                    ty[i]=0;
                else ty[i]=Double.valueOf(envi_val);
            }
		}
		x.add(tx);
		values.add(ty);
		
		int[] colors = new int[] { Color.BLUE};
		// , Color.CYAN, Color.YELLOW };
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE};
		// PointStyle.TRIANGLE, PointStyle.SQUARE };
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
		}
        String e_type="";
        if(envi_type.contains("Temperature"))
            e_type="值(℃)";
        else if(envi_type.contains("Humidity"))
            e_type="值(%rh)";
        else if(envi_type.contains("Light"))
            e_type="值(LUX)";
        else e_type="";
		setChartSettings(renderer,envi_type, "时间",e_type, 0.5, 12.5,
				-10, 40, Color.LTGRAY, Color.LTGRAY);
		renderer.setXLabels(sensor_list.size());
		renderer.setYLabels(10);
		renderer.setShowGrid(true);
		renderer.setYAxisMin(0);//设置y轴最小值是0  
        renderer.setYAxisMax(40);  
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomButtonsVisible(true);
		renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
		renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		renderer.setXLabelsAlign(Align.CENTER);
		for(int i=0;i<sensor_list.size();++i)
			renderer.addXTextLabel(i+1,sensor_list.get(i).envi_time.substring(2,13));
		renderer.setXLabels(0);

		XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
		XYSeries series = dataset.getSeriesAt(0);
		//ries.addAnnotation("Vacation", 6, 30);
		Intent intent = ChartFactory.getLineChartIntent(context, dataset,
				renderer, "环境数据");
		return intent;
	}

}
