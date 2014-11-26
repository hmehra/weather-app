package com.cs571_weatherapp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.util.Log;

public class retrieveJSON extends AsyncTask<URL,String,String>
{
	static int back;
	InputStream stream = null;
	public static String imgURL,locationCity,conditionValue,conditionText,locationRegion,unit,feed,link;
	public static JSONArray forecast;
	@Override
	protected String doInBackground(URL... url) 
	{
		String inputLine = "", jsonString = "";
		try
		{
			BufferedReader in;
			URLConnection connect = url[0].openConnection();
			try
			{
			    HttpURLConnection httpconnect =  (HttpURLConnection) connect;
			    httpconnect.setRequestMethod("GET");
			    httpconnect.connect();
			    Log.i("URL",url[0].toString());
			    if(httpconnect.getResponseCode() == HttpURLConnection.HTTP_OK)
			    {
			    	 stream = httpconnect.getInputStream();
			    	 in = new BufferedReader(new InputStreamReader(stream));
						
			    	 while ((inputLine = in.readLine()) != null)
					jsonString = jsonString + inputLine;
						
					in.close();
			    }
			    
			}
			
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			Log.e("Output json", jsonString);
			return jsonString;
		} 

		catch (IOException e)
		{
			e.printStackTrace();
			return "Error";
		}
	
	    	
	}  
	 
	 protected void onPostExecute(String jsonString) 
	 {
		 JSONObject jObject = null;
		 if(jsonString.equals(null))
		 {
			WeatherApp.TlocationCity.setText("No Information Found");
			clearall();
		 }
		 
		 else
		 {
			 try
			 {
				 jObject = new JSONObject(jsonString);
				 JSONObject weather = jObject.getJSONObject("weather");	
				 if(weather.getString("link").equals("false"))
				 {
					 WeatherApp.TlocationCity.setText("No Information Found");
					 clearall();
				 }
				
				
				 else
				 {
					 JSONObject location = weather.getJSONObject("location");
					 JSONObject condition = weather.getJSONObject("condition");
					 JSONObject units = weather.getJSONObject("units");
					 forecast = weather.getJSONArray("forecast");	
					
					 locationCity = location.getString("city");
					 locationRegion = location.getString("region")+", " + location.getString("country");
					 conditionText =  condition.getString("text");
					 conditionValue =  condition.getString("temp")+"\u00B0 " + units.getString("temperature");
					 imgURL = weather.getString("img");
					 unit = units.getString("temperature");
					 feed = weather.getString("feed");
					 link = weather.getString("link");
					 try
					 {
						 URL imageURL = new URL(imgURL);
						 new DownloadImage().execute(imageURL);
					 }
					 catch(MalformedURLException e)
					 {
						 e.printStackTrace();
					 }
											
					 WeatherApp.TlocationRegion.setText(locationRegion);
					 WeatherApp.TconditionText.setText(conditionText);
					 WeatherApp.TconditionValue.setText(conditionValue);
					 WeatherApp.TlocationCity.setText(locationCity);
					 WeatherApp.postCurrent.setText("Share Current Weather");
					 WeatherApp.postForecast.setText("Share Weather Forecast");
					 printTable(forecast,unit);
				 }		 
			 }
			 catch(JSONException e)
			 {
				 e.printStackTrace();
			 }
		 }
	}

	public void printTable(JSONArray forecast, String unit) {
		
		WeatherApp.header.setText("Forecast");
		WeatherApp.row0col1.setText("Day");
		WeatherApp.row0col1.setBackgroundResource(R.drawable.headerbox);
		WeatherApp.row0col2.setText("Weather");
		WeatherApp.row0col2.setBackgroundResource(R.drawable.headerbox);
		WeatherApp.row0col3.setText("High");
		WeatherApp.row0col3.setBackgroundResource(R.drawable.headerbox);
		WeatherApp.row0col4.setText("Low");
		WeatherApp.row0col4.setBackgroundResource(R.drawable.headerbox);
			
		try
		{
			WeatherApp.row1col1.setText(forecast.getJSONObject(0).getString("day"));
			WeatherApp.row1col1.setBackgroundResource(R.drawable.rowodd);
			WeatherApp.row1col2.setText(forecast.getJSONObject(0).getString("text"));
			WeatherApp.row1col2.setBackgroundResource(R.drawable.rowodd);
			WeatherApp.row1col3.setText(forecast.getJSONObject(0).getString("high") +"\u00B0"+unit);
			WeatherApp.row1col3.setBackgroundResource(R.drawable.rowodd);
			WeatherApp.row1col4.setText(forecast.getJSONObject(0).getString("low")+"\u00B0"+unit);
			WeatherApp.row1col4.setBackgroundResource(R.drawable.rowodd);
			
			WeatherApp.row2col1.setText(forecast.getJSONObject(1).getString("day"));
			WeatherApp.row2col1.setBackgroundResource(R.drawable.roweven);
			WeatherApp.row2col2.setText(forecast.getJSONObject(1).getString("text"));
			WeatherApp.row2col2.setBackgroundResource(R.drawable.roweven);
			WeatherApp.row2col3.setText(forecast.getJSONObject(1).getString("high") +"\u00B0"+unit);
			WeatherApp.row2col3.setBackgroundResource(R.drawable.roweven);
			WeatherApp.row2col4.setText(forecast.getJSONObject(1).getString("low")+"\u00B0"+unit);
			WeatherApp.row2col4.setBackgroundResource(R.drawable.roweven);
			
			WeatherApp.row3col1.setText(forecast.getJSONObject(2).getString("day"));
			WeatherApp.row3col1.setBackgroundResource(R.drawable.rowodd);
			WeatherApp.row3col2.setText(forecast.getJSONObject(2).getString("text"));
			WeatherApp.row3col2.setBackgroundResource(R.drawable.rowodd);
			WeatherApp.row3col3.setText(forecast.getJSONObject(2).getString("high") +"\u00B0"+unit);
			WeatherApp.row3col3.setBackgroundResource(R.drawable.rowodd);
			WeatherApp.row3col4.setText(forecast.getJSONObject(2).getString("low")+"\u00B0"+unit);
			WeatherApp.row3col4.setBackgroundResource(R.drawable.rowodd);
			
			WeatherApp.row4col1.setText(forecast.getJSONObject(3).getString("day"));
			WeatherApp.row4col1.setBackgroundResource(R.drawable.roweven);
			WeatherApp.row4col2.setText(forecast.getJSONObject(3).getString("text"));
			WeatherApp.row4col2.setBackgroundResource(R.drawable.roweven);
			WeatherApp.row4col3.setText(forecast.getJSONObject(3).getString("high") +"\u00B0"+unit);
			WeatherApp.row4col3.setBackgroundResource(R.drawable.roweven);
			WeatherApp.row4col4.setText(forecast.getJSONObject(3).getString("low")+"\u00B0"+unit);
			WeatherApp.row4col4.setBackgroundResource(R.drawable.roweven);
			
			WeatherApp.row5col1.setText(forecast.getJSONObject(4).getString("day"));
			WeatherApp.row5col1.setBackgroundResource(R.drawable.rowodd);
			WeatherApp.row5col2.setText(forecast.getJSONObject(4).getString("text"));
			WeatherApp.row5col2.setBackgroundResource(R.drawable.rowodd);
			WeatherApp.row5col3.setText(forecast.getJSONObject(4).getString("high") +"\u00B0"+unit);
			WeatherApp.row5col3.setBackgroundResource(R.drawable.rowodd);
			WeatherApp.row5col4.setText(forecast.getJSONObject(4).getString("low")+"\u00B0"+unit);
			WeatherApp.row5col4.setBackgroundResource(R.drawable.rowodd);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public void clearall()
	{
		WeatherApp.TlocationRegion.setText(null);
		WeatherApp.TconditionText.setText(null);
		WeatherApp.TconditionValue.setText(null);
		WeatherApp.postCurrent.setText(null);
		WeatherApp.postForecast.setText(null);
		WeatherApp.row0col1.setText(null);
		WeatherApp.row0col1.setBackgroundResource(0);
		WeatherApp.row0col2.setText(null);
		WeatherApp.row0col2.setBackgroundResource(0);
		WeatherApp.row0col3.setText(null);
		WeatherApp.row0col3.setBackgroundResource(0);
		WeatherApp.row0col4.setText(null);
		WeatherApp.row0col4.setBackgroundResource(0);
		WeatherApp.row1col1.setBackgroundResource(0);
		WeatherApp.row1col1.setText(null);
		WeatherApp.row1col1.setBackgroundResource(0);
		WeatherApp.row1col2.setText(null);
		WeatherApp.row1col2.setBackgroundResource(0);
		WeatherApp.row1col3.setText(null);
		WeatherApp.row1col3.setBackgroundResource(0);
		WeatherApp.row1col4.setText(null);
		WeatherApp.row1col4.setBackgroundResource(0);
		
		WeatherApp.row2col1.setText(null);
		WeatherApp.row2col1.setBackgroundResource(0);
		WeatherApp.row2col2.setText(null);
		WeatherApp.row2col2.setBackgroundResource(0);
		WeatherApp.row2col3.setText(null);
		WeatherApp.row2col3.setBackgroundResource(0);
		WeatherApp.row2col4.setText(null);
		WeatherApp.row2col4.setBackgroundResource(0);
		
		WeatherApp.row3col1.setText(null);
		WeatherApp.row3col1.setBackgroundResource(0);
		WeatherApp.row3col2.setText(null);
		WeatherApp.row3col2.setBackgroundResource(0);
		WeatherApp.row3col3.setText(null);
		WeatherApp.row3col3.setBackgroundResource(0);
		WeatherApp.row3col4.setText(null);
		WeatherApp.row3col4.setBackgroundResource(0);
		
		WeatherApp.row4col1.setText(null);
		WeatherApp.row4col1.setBackgroundResource(0);
		WeatherApp.row4col2.setText(null);
		WeatherApp.row4col2.setBackgroundResource(0);
		WeatherApp.row4col3.setText(null);
		WeatherApp.row4col3.setBackgroundResource(0);
		WeatherApp.row4col4.setText(null);
		WeatherApp.row4col4.setBackgroundResource(0);
		
		WeatherApp.row5col1.setText(null);
		WeatherApp.row5col1.setBackgroundResource(0);
		WeatherApp.row5col2.setText(null);
		WeatherApp.row5col2.setBackgroundResource(0);
		WeatherApp.row5col3.setText(null);
		WeatherApp.row5col3.setBackgroundResource(0);
		WeatherApp.row5col4.setText(null);
		WeatherApp.row5col4.setBackgroundResource(0);
		WeatherApp.header.setText(null);
		WeatherApp.image.setImageBitmap(null);
	}
}
