import java.io.*;
import java.util.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherServlet extends HttpServlet 
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
         response.setContentType("application/json;charset=UTF-8");
		 String data = request.getParameter("data");
		 String type = request.getParameter("identity");
		 String unit = request.getParameter("unit");
		 PrintWriter out = response.getWriter(); 
		 String urlString = "http://default-environment-pnbhsnzg9h.elasticbeanstalk.com/?location="+data+"&locationtype="+type+"&temperature="+unit;
         
		 
		URL url = new URL(urlString);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine,xmlString = "";
        while ((inputLine = in.readLine()) != null)
        xmlString = xmlString + inputLine;
        in.close();
		
		SAXBuilder builder = new SAXBuilder();
		Reader read = new StringReader(xmlString);
		Document doc = new Document();
		Element weather = null;
		Element link = null;
		Element feed = null;
		Element image = null;
		Element condition = null;
		List forecast = null;
		Element units = null;
		Element location = null;
		
		try
		{
			doc = builder.build(read);
			weather = doc.getRootElement();
			link = weather.getChild("linker");
			if(! (link.getText().equals("false")))
			{
				image = weather.getChild("image");
				condition = weather.getChild("condition");
				feed = weather.getChild("feed");
				forecast = weather.getChildren("forecast");
				location = weather.getChild("location");
				units = weather.getChild("units");
			}	
		} 
		
		catch (JDOMException e)
		{
			out.println(e.getMessage());
		} 
		catch (IOException e)
		{
			out.println("IO Exception");
		} 
		catch (Exception e)
		{	
			out.println(e.getMessage());
		}
		
		JSONObject output = new JSONObject();
		JSONObject jweather = new JSONObject();
		JSONObject jcondition = new JSONObject();
		JSONObject jlocation = new JSONObject();
		JSONObject jforecast_contents_a = new JSONObject();
		JSONObject jforecast_contents_b = new JSONObject();
		JSONObject jforecast_contents_c = new JSONObject();
		JSONObject jforecast_contents_d = new JSONObject();
		JSONObject jforecast_contents_e = new JSONObject();
		JSONObject junits = new JSONObject();
		JSONArray jforecast = new JSONArray();
		
	if(! (link.getText().equals("false")))
	{
		junits.put("temperature",units.getAttributeValue("temperature"));
		
		jlocation.put("region",location.getAttributeValue("region"));
		jlocation.put("country",location.getAttributeValue("country"));
		jlocation.put("city",location.getAttributeValue("city"));
		
		jcondition.put("text",condition.getAttributeValue("text"));
		jcondition.put("temp",condition.getAttributeValue("temp"));
		
		Element a = (Element)forecast.get(0);	
		jforecast_contents_a.put("text",a.getAttributeValue("text"));
		jforecast_contents_a.put("high",a.getAttributeValue("high"));
		jforecast_contents_a.put("low",a.getAttributeValue("low"));
		jforecast_contents_a.put("day",a.getAttributeValue("day"));
		
		Element b = (Element)forecast.get(1);	
		jforecast_contents_b.put("text",b.getAttributeValue("text"));
		jforecast_contents_b.put("high",b.getAttributeValue("high"));
		jforecast_contents_b.put("low",b.getAttributeValue("low"));
		jforecast_contents_b.put("day",b.getAttributeValue("day"));
		
		Element c = (Element)forecast.get(2);	
		jforecast_contents_c.put("text",c.getAttributeValue("text"));
		jforecast_contents_c.put("high",c.getAttributeValue("high"));
		jforecast_contents_c.put("low",c.getAttributeValue("low"));
		jforecast_contents_c.put("day",c.getAttributeValue("day"));
		
		Element d = (Element)forecast.get(3);	
		jforecast_contents_d.put("text",d.getAttributeValue("text"));
		jforecast_contents_d.put("high",d.getAttributeValue("high"));
		jforecast_contents_d.put("low",d.getAttributeValue("low"));
		jforecast_contents_d.put("day",d.getAttributeValue("day"));
		
		Element e = (Element)forecast.get(4);	
		jforecast_contents_e.put("text",e.getAttributeValue("text"));
		jforecast_contents_e.put("high",e.getAttributeValue("high"));
		jforecast_contents_e.put("low",e.getAttributeValue("low"));
		jforecast_contents_e.put("day",e.getAttributeValue("day"));
		
		jforecast.put(jforecast_contents_a);
		jforecast.put(jforecast_contents_b);
		jforecast.put(jforecast_contents_c);
		jforecast.put(jforecast_contents_d);
		jforecast.put(jforecast_contents_e);
		
		jweather.put("location",jlocation);
		jweather.put("condition",jcondition);
		jweather.put("units",junits);
		jweather.put("link",link.getText());
		jweather.put("img",image.getText());
		jweather.put("feed",feed.getText());
		jweather.put("forecast",jforecast);
	}	
		jweather.put("link",link.getText());
		output.put("weather",jweather);
		String result = output.toString();
		out.write(result);
		
}   
	
	public void doPost(HttpServletRequest request, HttpServletResponse res)
    throws IOException, ServletException
    {
        Enumeration e = request.getParameterNames();
        PrintWriter out = res.getWriter();
		
    }
}
