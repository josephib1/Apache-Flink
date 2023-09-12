package mk.ukimi.finiki.Sources;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiData implements SourceFunction<String> {
    private double avg;
    private String HighesttmpName;
    private String LowesttmpName;
    private double Highesttmpvalue=0;
    private double Lowesttmpvalue=9999;
    private int LHum;
    private double LWind;
    private String LDes;
    private String HDes;
    private int HHum;
    private double HWind;
    private List<String> BadCountries = new ArrayList<>();
    private List<String> Sunny = new ArrayList<>();
    private static final String API_URL = "https://api.weatherapi.com/v1/current.json?key=%s&q=%s&aqi=no";
    private static final String API_KEY = "15fe195a4def4d13af191025231506";
    private static final int INTERVAL_SECONDS = 10;
    private static final List<String> COUNTRIES = Arrays.asList("Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria",
            "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan",
            "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Côte d'Ivoire", "Cabo Verde",
            "Cambodia", "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo",
            "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica",
            "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia",
            "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada",
            "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Holy See", "Honduras", "Hungary", "Iceland", "India",
            "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan",
            "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
            "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
            "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique",
            "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea",
            "North Macedonia", "Norway", "Oman", "Pakistan", "Palau", "Palestine State", "Panama", "Papua New Guinea", "Paraguay", "Peru",
            "Philippines");

    private volatile boolean isRunning = true;

    @Override
    public void run(SourceContext<String> sourceContext) throws Exception {
        while (isRunning) {
            for (String country : COUNTRIES) {
                // Construct the API URL by replacing the placeholder with the API key and country
                String apiUrl = String.format(API_URL, API_KEY, country);

                // Open a connection to the API
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Get the response code from the API request
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response data from the API
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse the JSON response and extract the desired data
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject current = jsonObject.getJSONObject("current");
                    double temp = current.getDouble("temp_c");
                    String description = current.getJSONObject("condition").getString("text");
                    int humidity = current.getInt("humidity");
                    double windSpeed = current.getDouble("wind_kph");

                    if(temp>Highesttmpvalue){
                        Highesttmpvalue=temp;
                        HighesttmpName=country;
                        HWind=windSpeed;
                        HHum=humidity;
                        HDes=description;
                    }
                    if(temp<Lowesttmpvalue) {
                        Lowesttmpvalue=temp;
                        LowesttmpName = country;
                        LWind=windSpeed;
                        LHum=humidity;
                        LDes=description;
                    }
                    if(description.equalsIgnoreCase("SUNNY")) {
                        Sunny.add(country);
                    }
                    if(windSpeed>=48.00 | humidity>=80 | temp>=35.00 | temp<=0.00)
                        BadCountries.add(country);

                    //uncomment those lines to see each country full data
                    // Format the timestamp
                    LocalDateTime timestamp = LocalDateTime.now();
                    String formattedTimestamp = timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                    // Emit the extracted data with timestamp and country
                    String data = String.format("[%s] Country: %s, Temperature: %.2f°C, Description: %s, Humidity: %d, Wind Speed: %.2f kph",
                          formattedTimestamp, country, temp, description, humidity, windSpeed);
                    sourceContext.collect(data);
                } else {
                    continue;
                }
            }
            LocalDateTime timestamp= LocalDateTime.now();
            String formattedTimestamp = timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            sourceContext.collect("-----------------------------------------------------------------");
            Thread.sleep(1 * 1000);
            String data = String.format("[%s] Highest temperature Country:%s, Temperature: %.2f°C, Description: %s, Humidity: %d, Wind Speed: %.2f kph",
                    formattedTimestamp, HighesttmpName, Highesttmpvalue, HDes, HHum, HWind);
            sourceContext.collect(data);
            Thread.sleep(1 * 1000);
            sourceContext.collect("-----------------------------------------------------------------");
            Thread.sleep(2 * 1000);
            String Lowestdata = String.format("[%s] Lowest temperature Country:%s, Temperature: %.2f°C, Description: %s, Humidity: %d, Wind Speed: %.2f kph",
                    formattedTimestamp, LowesttmpName, Lowesttmpvalue, LDes, LHum, LWind);
            sourceContext.collect(Lowestdata);
            Thread.sleep(1 * 1000);
            sourceContext.collect("-----------------------------------------------------------------");
            Thread.sleep(2 * 1000);
            sourceContext.collect("Bad Countries Weather:");
            Thread.sleep(1 * 1000);
            for(String Bad : BadCountries){
                sourceContext.collect(String.format("%s",Bad));
            }
            Thread.sleep(1 * 1000);
            sourceContext.collect("-----------------------------------------------------------------");
            Thread.sleep(2 * 1000);
            sourceContext.collect("Countries with 'Sunny' Weather:");
            Thread.sleep(1 * 1000);
            for(String sun : Sunny){
                sourceContext.collect(String.format("%s",sun));
            }
            Thread.sleep(3 * 1000);
            sourceContext.collect("-----------------------------------------------------------------");
            sourceContext.collect("-----------------------------------------------------------------");
            sourceContext.collect("-----------------------------------------------------------------");
            // Sleep for the specified interval
            Thread.sleep(INTERVAL_SECONDS * 1000);
            isRunning=false;
        }
    }

    @Override
    public void cancel() {
        // Set the flag to stop the execution of the source function
        isRunning = false;
    }
}
