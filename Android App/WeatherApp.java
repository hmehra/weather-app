package com.cs571_weatherapp;

import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.TextView;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class WeatherApp extends Activity 
{
	Button search;
	EditText textBox;
	RadioGroup rG; 
	RadioButton rF;
	String locType="",tempUnit="",input="";
	int valid = 0,selectId;
	public static TextView TlocationCity,TlocationRegion,TconditionText,TconditionValue;
	public static TextView postCurrent,postForecast;
	public static ImageView image;
	public static TextView header;
	public static TextView row0col1,row0col2,row0col3,row0col4;
	public static TextView row1col1,row1col2,row1col3,row1col4;
	public static TextView row2col1,row2col2,row2col3,row2col4;
	public static TextView row3col1,row3col2,row3col3,row3col4;
	public static TextView row4col1,row4col2,row4col3,row4col4;
	public static TextView row5col1,row5col2,row5col3,row5col4;
	public static int post;
	
    	
	protected void onCreate(Bundle savedInstanceState) 
    {      	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_app);
        
        // Get All Graphical Variables
        postCurrent = (TextView) findViewById(R.id.postCurrent);
        postForecast = (TextView) findViewById(R.id.postForecast);
        TlocationCity = (TextView) findViewById(R.id.cityName);
        TlocationRegion = (TextView) findViewById(R.id.regionName);
    	TconditionText = (TextView) findViewById(R.id.weatherType);
    	TconditionValue = (TextView) findViewById(R.id.tempValue);
    	header = (TextView) findViewById(R.id.header);
    	row0col1 = (TextView) findViewById(R.id.Row0Col1);
    	row0col2 = (TextView) findViewById(R.id.Row0Col2);
    	row0col3 = (TextView) findViewById(R.id.Row0Col3);
    	row0col4 = (TextView) findViewById(R.id.Row0Col4);
    	
    	row1col1 = (TextView) findViewById(R.id.Row1Col1);
    	row1col2 = (TextView) findViewById(R.id.Row1Col2);
    	row1col3 = (TextView) findViewById(R.id.Row1Col3);
    	row1col4 = (TextView) findViewById(R.id.Row1Col4);
    	
    	row2col1 = (TextView) findViewById(R.id.Row2Col1);
    	row2col2 = (TextView) findViewById(R.id.Row2Col2);
    	row2col3 = (TextView) findViewById(R.id.Row2Col3);
    	row2col4 = (TextView) findViewById(R.id.Row2Col4);
    	
    	row3col1 = (TextView) findViewById(R.id.Row3Col1);
    	row3col2 = (TextView) findViewById(R.id.Row3Col2);
    	row3col3 = (TextView) findViewById(R.id.Row3Col3);
    	row3col4 = (TextView) findViewById(R.id.Row3Col4);
    	
    	row4col1 = (TextView) findViewById(R.id.Row4Col1);
    	row4col2 = (TextView) findViewById(R.id.Row4Col2);
    	row4col3 = (TextView) findViewById(R.id.Row4Col3);
    	row4col4 = (TextView) findViewById(R.id.Row4Col4);
    	
    	row5col1 = (TextView) findViewById(R.id.Row5Col1);
    	row5col2 = (TextView) findViewById(R.id.Row5Col2);
    	row5col3 = (TextView) findViewById(R.id.Row5Col3);
    	row5col4 = (TextView) findViewById(R.id.Row5Col4);
    	
    	image = (ImageView) findViewById(R.id.image);
        rG = (RadioGroup)findViewById(R.id.radioGroup);
        rF = (RadioButton) findViewById(R.id.radioF);
        search = (Button) findViewById(R.id.searchButton);
        textBox = (EditText) findViewById(R.id.textBox);
        
        // Facebook Session
             
        Session session = Session.getActiveSession();

		if(session == null )
		session = new Session(WeatherApp.this);
		
		session.closeAndClearTokenInformation();
		Session.setActiveSession(session);
		
	    search.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) 
		{ 
			input = textBox.getText().toString().replaceAll("\\s+","");
			valid = validateForm();
			if(valid == 1)
			{
				selectId = rG.getCheckedRadioButtonId();
				if(selectId == rF.getId())
					tempUnit = "f";
				else
					tempUnit = "c";
				
				String urlString = "http://cs-server.usc.edu:33488/servlet/WeatherServlet?data="+input+"&identity="+locType+"&unit="+tempUnit;
				try 
				{
					URL url = new URL(urlString);
					new retrieveJSON().execute(url);
				}
				catch (MalformedURLException e) 
				{
					e.printStackTrace();
				}
				
			}	 
		}
		} );
        
        postCurrent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WeatherApp.this);
				alertDialogBuilder.setTitle("Post To Facebook");
				
				alertDialogBuilder.setPositiveButton("Post Current Weather",new DialogInterface.OnClickListener() 
				{
		            public void onClick(DialogInterface dialog,int which) 
		            {
		            	Session.openActiveSession( WeatherApp.this, true, new Session.StatusCallback(){
		            		@Override
							public void call(Session session, SessionState state, Exception exception) 
		            		{
		            			if (state.isOpened() || session.isOpened()) 
								postCurrentWeather();
		            		}					
		            	});	
		            }
		        });
				
				alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() 
				{
		            public void onClick(DialogInterface dialog,int which) 
		            {
		 		      Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
		            }
		        });
				 
				alertDialogBuilder.create().show();
			}
		});
        
        postForecast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WeatherApp.this);
				alertDialogBuilder.setTitle("Post To Facebook");
				
				alertDialogBuilder.setPositiveButton("Post Weather Forecast",new DialogInterface.OnClickListener() 
				{
		            public void onClick(DialogInterface dialog,int which) 
		            {
		            	Session.openActiveSession( WeatherApp.this, true, new Session.StatusCallback(){
		            		@Override
							public void call(Session session, SessionState state, Exception exception) 
		            		{
		            			if (state.isOpened() || session.isOpened()) 
								postWeatherForecast();
		            		}					
		            	});	
		            }
		        });
				
				alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() 
				{
		            public void onClick(DialogInterface dialog,int which) 
		            {
		 		      Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
		            }
		        });
				
				alertDialogBuilder.create().show();
			}
		});
   }
    
    public int validateForm()
    {   	
		if (input.length() == 0)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WeatherApp.this);
			alertDialogBuilder.setTitle("Error").setMessage("Please Enter Some Data!").setNeutralButton("Close", null).show();
			new retrieveJSON().clearall();
			TlocationCity.setText(null);
			return -1;
		}
		
		else if(input.matches("^\\d+$"))
    	{
    		if(input.matches("^\\d{5}$"))
    		{
    			locType = "zipcode";
    			return 1;
    		}
    		else
    		{
    			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WeatherApp.this);
    			alertDialogBuilder.setTitle("Error").setMessage("Invalid Zip Code.\nMust be Five Digits").setNeutralButton("Close", null).show();
    			new retrieveJSON().clearall();
    			TlocationCity.setText(null);
    			return -1;
    		}
    		
    	}
    	
    	else
    	{
    		String reg_city_a = "^[a-zA-Z\\'\\-\\s.]+\\,\\s*[a-zA-Z\\'\\-\\s.]+$"; 
    		String reg_city_b = "^[a-zA-Z\\'\\-\\s.]+\\,\\s*[a-zA-Z\\'\\-\\s.]+\\,\\s*[a-zA-Z\\'\\-\\s.]+$";
    		
    		if(! ( (input.matches(reg_city_a)) || (input.matches(reg_city_b))))
    		{
    			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WeatherApp.this);
    			alertDialogBuilder.setTitle("Error").setMessage("Invalid City Argument.\nMust be City Name comma seperated by Region or Country").setNeutralButton("Close", null).show();
    			new retrieveJSON().clearall();
    			TlocationCity.setText(null);
    			return -1;
    		}
   
    		else
    		{
    			locType="city";
    			return 1;
    		}
    		
    	}	
    }
    	 
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather_app, menu);
        return true;
    }
    
   
    public void postCurrentWeather()
    {
    	JSONObject prop;
		try
		{
			prop = new JSONObject("{\"Look at Details\":{\"text\":\"Here\", \"href\":\""+retrieveJSON.link+"\"}}");
			Log.e("link", retrieveJSON.link);
			Bundle params = new Bundle();
	        params.putString("name", retrieveJSON.locationCity+", "+retrieveJSON.locationRegion);
	        params.putString("caption", "The Current Condition for "+retrieveJSON.locationCity+" is "+retrieveJSON.conditionText);
	        params.putString("description", "Temperature is "+retrieveJSON.conditionValue);
	        params.putString("link", retrieveJSON.feed);
	        params.putString("properties",prop.toString());
	        params.putString("picture",retrieveJSON.imgURL);
	        
	        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(WeatherApp.this,Session.getActiveSession(),params))
					.setOnCompleteListener(new OnCompleteListener() 
					{
						@Override
						public void onComplete(Bundle values, FacebookException error) 
						{
							// TODO Auto-generated method stub
							if (error == null) 
							{
								// When the story is posted, echo the success and the post Id.
								final String postId = values.getString("post_id");
								if (postId != null) 
									Toast.makeText(WeatherApp.this,"Posted Successfully.\nID: "+postId,Toast.LENGTH_SHORT).show();
         
								else // User clicked the Cancel button 
									Toast.makeText(WeatherApp.this.getApplicationContext(),"Publish cancelled",Toast.LENGTH_SHORT).show();
							} 
    
							else if (error instanceof FacebookOperationCanceledException)  // User clicked the "x" button 
								Toast.makeText(WeatherApp.this.getApplicationContext(),"Publish cancelled",Toast.LENGTH_SHORT).show();
     
							else	// Generic, ex: network error 
								Toast.makeText(WeatherApp.this.getApplicationContext(),"Error posting story",Toast.LENGTH_SHORT).show(); 
						}
					}).build();

	        feedDialog.show();
		}
		
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
    	      
    }
    
    public void postWeatherForecast()
    {
    	JSONObject prop;
    	String des = "";
		try
		{
			JSONArray forecast = retrieveJSON.forecast;
			for(int i=0;i<forecast.length();i++)
			{
				des += forecast.getJSONObject(i).getString("day")+": "+forecast.getJSONObject(i).getString("text") + ", "
					 +forecast.getJSONObject(i).getString("high")+"/"+forecast.getJSONObject(i).getString("low")
					 +"\u00B0"+retrieveJSON.unit+"; ";
			}
			
			prop = new JSONObject("{\"Look at Details\":{\"text\":\"Here\", \"href\":\""+retrieveJSON.link+"\"}}");
			Bundle params = new Bundle();
	        params.putString("name", retrieveJSON.locationCity+", "+retrieveJSON.locationRegion);
	        params.putString("caption", "Weather Forecast for "+retrieveJSON.locationCity);
	        params.putString("description", des);
	        params.putString("link", retrieveJSON.feed);
	        params.putString("properties",prop.toString());
	        params.putString("picture","http://cs-server.usc.edu:33488/forecast.jpg");
	        
	        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(WeatherApp.this,Session.getActiveSession(),params))
					.setOnCompleteListener(new OnCompleteListener() 
					{
						@Override
						public void onComplete(Bundle values, FacebookException error) 
						{
							// TODO Auto-generated method stub
							if (error == null) 
							{
								// When the story is posted, echo the success and the post Id.
								final String postId = values.getString("post_id");
								if (postId != null) 
									Toast.makeText(WeatherApp.this,"Posted Successfully.\nID: "+postId,Toast.LENGTH_SHORT).show();
         
								else // User clicked the Cancel button 
									Toast.makeText(WeatherApp.this.getApplicationContext(),"Publish cancelled",Toast.LENGTH_SHORT).show();
							} 
    
							else if (error instanceof FacebookOperationCanceledException)  // User clicked the "x" button 
								Toast.makeText(WeatherApp.this.getApplicationContext(),"Publish cancelled",Toast.LENGTH_SHORT).show();
     
							else	// Generic, ex: network error 
								Toast.makeText(WeatherApp.this.getApplicationContext(),"Error posting story",Toast.LENGTH_SHORT).show(); 
						}
					}).build();

	        feedDialog.show();
		}
		
		catch (JSONException e) 
		{
			e.printStackTrace();
		}

    }
    

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }
    
}

