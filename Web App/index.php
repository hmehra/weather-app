<?php    
$xmlElement = new SimpleXMLElement('<weather/>');
header('Content-type: text/xml');
  error_reporting(0);
  $yid = "ntYIUxTV34Fej5SZcRUw9FjdZsFnhiAxhYHlp1yqV701aBkxDfZlxs3MQfbHgvZNIv9DdmzsiQ--"; 	
  
  if($_SERVER["REQUEST_METHOD"] == "GET")
  {    	
	
	if($_GET["locationtype"] == "zipcode")
	 {        
	   $url_zip = "http://where.yahooapis.com/v1/concordance/usps/".urlencode($_GET["location"])."?appid=".$yid;
	   $xml_zip = simplexml_load_file(urlencode($url_zip));
	   
	   if($xml_zip === false)
	   {
	    $link = $xmlElement->addchild("linker","false");
		die($xmlElement->asXML());
	   }
			
	   else if(count($xml_zip->children()) == 0)	    
	   {
		$link = $xmlElement->addchild("linker","false");
		die($xmlElement->asXML());
	   }		
	   
	   else
		{
			foreach($xml_zip->children() as $child)
			{
				if($child->getname()== "woeid")
				$wid = $child;		   
			}	
			xml_info($wid);				
		}
	}	    
	
	else
		{			    
			$url_city = "http://where.yahooapis.com/v1/places\$and(.q('".urlencode($_GET["location"])."'),.type(7));start=0;count=1?appid=".$yid;
			$xml_city = simplexml_load_file(urlencode($url_city));
			
			if($xml_city === FALSE)
			{
			 $link = $xmlElement->addchild("linker","false");
			 die($xmlElement->asXML()); 
			}
			
			else if(count($xml_city->children()) == 0)				
			{
			  $link = $xmlElement->addchild("linker","false");
			  die($xmlElement->asXML());
			}
			
			else
			{
				$place = $xml_city->children();	
				foreach($place->children() as $child)
				{
					if($child->getname()== "woeid")
					$wid=$child;
				}
				xml_info($wid);
			}									
		}
	
	echo $xmlElement->asXML();
}
?>

<?php
function xml_info($wid)
{
   global $xmlElement;
   $url_wthr = "http://weather.yahooapis.com/forecastrss?w=".$wid."&u=".$_GET["temperature"];
   $feed = $xmlElement->addchild("feed",$url_wthr);
   
   $xml_wthr=simplexml_load_file($url_wthr);
   
   if($xml_wthr == NULL)
   {
	$link = $xmlElement->addchild("linker","false");
	return;
   }
   
   else
   {
   
	$city_check = $xml_wthr->xpath('/rss/channel/item/title');
    if($city_check[0] == 'City not found')
     {
	   $link = $xmlElement->addchild("linker","false");
	   return;
	 }
   
   $ylocation = $xml_wthr->xpath('/rss/channel/yweather:location');
   $lat = $xml_wthr->xpath('/rss/channel/item/geo:lat');
   $long = $xml_wthr->xpath('/rss/channel/item/geo:long');
   $description = $xml_wthr->xpath('/rss/channel/item/description');
   $ycondition = $xml_wthr->xpath('/rss/channel/item/yweather:condition');
   $yunits = $xml_wthr->xpath('/rss/channel/yweather:units');
   $yforecast = $xml_wthr->xpath('/rss/channel/item/yweather:forecast');
   
   preg_match_all("/http:\/\/(.?)[^\"']+/",$description[0],$matches);  										
   
   $img = $xmlElement->addchild("image",$matches[0][0]);
   $link = $xmlElement->addchild("linker",$matches[0][1]);
   
	foreach($ycondition[0]->attributes() as $a => $b)
	{
		if($b == "")
		$b = "NA";
		
		if($a == "text")
		{		
		  $condition = $xmlElement->addchild("condition");
		  $condition->addAttribute("text",$b);	  
		}
		if($a == "temp")
		$condition->addAttribute("temp",$b);
		
	}	
    
	foreach($yunits[0]->attributes() as $a => $b)
	{
		if($b == "")
		$b = "NA";
		
		if($a == "temperature")
		{
		 $units = $xmlElement->addchild("units");
		 $units->addAttribute("temperature",$b);
		}
	}
	
	foreach($ylocation[0]->attributes() as $a => $b)
	{
		if($b == "")
		$b = "NA";
		
		if($a == "city")
		{
		  $location = $xmlElement->addchild("location");
		  $location->addAttribute("city",$b);
		}
		
		if($a == "region")	
		$location->addAttribute("region",$b);
		
		if($a == "country")	
		$location->addAttribute("country",$b);
		
	}	   
	
	
	for($i=0;$i<count($yforecast);$i++)
	{
	  $forecast[$i] = $xmlElement->addchild("forecast");
	  foreach($yforecast[$i]->attributes() as $a => $b)
	 {
		if($b == "")
		$b = "NA";
		
		if($a == "day")	
		$forecast[$i]->addAttribute("day",$b);
		
		if($a == "low")	
		$forecast[$i]->addAttribute("low",$b);
		
		if($a == "high")	
		$forecast[$i]->addAttribute("high",$b);

		if($a == "text")	
		$forecast[$i]->addAttribute("text",$b);
	  }
	}
		
  }	
 }

?>


