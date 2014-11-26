package com.cs571_weatherapp;

import java.io.InputStream;
import java.net.URL;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadImage extends AsyncTask<URL, String, Bitmap> {
	Bitmap wIcon = null;
	@Override
	protected Bitmap doInBackground(URL... url) 
	{
        try 
        {
            	InputStream in = (url[0].openStream());
            	wIcon = BitmapFactory.decodeStream(in);
        } 
        
        catch (Exception e) 
        {  
        	Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        
        return wIcon;
	}
	
	@SuppressLint("NewApi")
	protected void onPostExecute(Bitmap wIcon) 
	 {
		WeatherApp.image.setImageBitmap(wIcon);
     }
}
