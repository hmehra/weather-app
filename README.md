Weather App
===========

Android and Web app for Weather Retrieval

The app fetches weather information of worldwide cities based on name or US
Zip codes using Yahoo! Geoplanet and Yahoo! RSS Weather feed API's.

#### Program Organisation -
1. HTML is used to display the basic form and gather the relevant data.
2. Javascript is used to validate the form.
3. CSS is used to apply styling to the document.
4. PHP script is used to parse XML files that are generated using user data 
   entered in the form.

#### Working - 
1. The HTML form collects either a US ZIP code or a city name and submits this form data by POST method to the server.
2. Before the transaction to the server takes place, the form is validated using Javascript to check for empty fields and invalid entries.
3. The PHP script gets killed if invalid data was entered.
4. If valid data was entered, PHP script is executed.
5. 	Based on ZIP code or city name, an URL is constructed to query the Yahoo! Geoplanet API. This returns a XML containing unique 'Where on Earth (WOEID)' ID's. This XML is parsed using simpleXML library to extract the WOEID(s).
6. 	After the retrieval of WOEID(s), another URL is constructed comprising of WOEID to query Yahoo RSS Weather feed. The query returns the weather information for a specific WOEID.
7. 	The returned XML is parsed using xpath to display the required table. Empty fields are displayed as N/A.

