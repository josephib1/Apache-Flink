```xml

# Apache Flink project 


## Installation and Setup:
### Flink:

•	First step is to download and run docker on windows using this link: https://www.docker.com/products/docker-desktop/.
•	Run “docker info” in a terminal to insure docker is installed correctly.
•	Copy the command “docker pull flink” into the command prompt, open this link for more instructions: https://hub.docker.com/_/flink .
•	Create file “docker-compose.yml” and copy this code into it:
•	version: "2.1"
•	services:
•	  jobmanager:
•	    image: ${FLINK_DOCKER_IMAGE_NAME:-flink}
•	    expose:
•	      - "6123"
•	    ports:
•	      - "8081:8081"
•	    command : jobmanager
•	    environment:
•	    - JOB_MANAGER_RPC_ADDRESS=jobmanager	
•	  taskmanager:
•	    image: ${FLINK_DOCKER_IMAGE_NAME:-flink}
•	    expose:
•	      - "6121"
•	      - "6122"
•	    depends_on:
•	      - jobmanager
•	    command : taskmanager
•	    links:
•	      - "jobmanager:jobmanager"
•	    environment:
•	      - JOB_MANAGER_RPC_ADDRESS=jobmanager
•	Navigate from the cmd to this file location and then type “docker-compose up” and that should run flink without errors.
•	Open http://localhost:8081 and your good to go.

## Project:

### Maven setup:

To become ready to run new code on flink you need first to download maven and set it up:
•	Download maven  .zip file from this link: https://maven.apache.org/download.cgi
•	Navigate to environment variables on your pc and Maven bin file to the file path, and then create new System variable called “MAVEN_HOME” and add to it the file path of the extracted maven file.
•	To test it try mvn –version.
•	Create a new folder for the project and navigate to it using the cmd.
•	Copy this command and run it in your cmd:  
mvn archetype:generate     -DarchetypeGroupId=org.apache.flink   -DarchetypeArtifactId=flink-quickstart-java -DarchetypeVersion=1.4.2
•	It should ask you for some informations enter them, and the project will be created.
•	Last Step: your good to go .

### Java Setup:

You should have jdk 8 or 11 installed on your pc.
Run the code In an Ide and add this dependency:
<!--//add dependency to create json object-->
<dependency>
   <groupId>org.json</groupId>
   <artifactId>json</artifactId>
   <version>20140107</version>
</dependency>
And make sure ide java version is same as your running jdk :
<java.version>11</java.version>


## Project Description: 
### Apache Flink Weather Data Processing:

The Apache Flink Weather Data Processing project is an innovative solution that utilizes the power of Apache Flink to fetch and process real-time weather data from a weather API. This project showcases the extensive features and capabilities of Apache Flink in handling streaming data, performing data transformations, and conducting insightful data analysis.
At the core of the project lies the ApiData class, which implements the SourceFunction interface. This class establishes a seamless connection with a WeatherAPI service and periodically retrieves accurate weather data for a specific location, in this case, Lebanon, at a regular interval of 5 seconds.

The project highlights the following key Apache Flink features:

1.	Streaming Data Processing: Leveraging Apache Flink's remarkable streaming data processing capabilities, the project demonstrates its ability to handle continuous streams of data. The ApiData class runs an infinite loop, ensuring the constant retrieval of weather data from the API.

2.	SourceFunction: By implementing the SourceFunction interface, the ApiData class leverages one of the fundamental abstractions in Apache Flink for creating data sources. This enables the project to seamlessly start and stop the streaming source while emitting data records effectively.

3.	Efficient HTTP Connection: The project expertly establishes a reliable HTTP connection with the WeatherAPI service using HttpURLConnection to efficiently fetch the desired weather data. This integration showcases Apache Flink's capability to seamlessly interact with external systems and APIs for seamless data ingestion.

4.	JSON Data Parsing: With the weather data retrieved in JSON format, the project effectively utilizes Apache Flink's flexible data parsing capabilities. By employing the JSONObject class, the JSON response is skillfully parsed to extract essential weather information, such as temperature, weather description, humidity, and wind speed.


5.	Accurate Timestamping: The project demonstrates the utilization of Apache Flink's powerful timestamping features. By skillfully utilizing the LocalDateTime and DateTimeFormatter classes, the project generates well-formatted timestamps for each data record, allowing precise tracking of the time when the weather data was collected.


6.	Streamlined Data Emission: With Apache Flink's SourceContext, the project ensures smooth and efficient emission of the extracted weather data, complete with accurate timestamps. By utilizing the collect method, the data is seamlessly sent downstream for further processing or storage, ensuring the seamless flow of information.

7.	Exception Handling: The project incorporates robust exception handling mechanisms to address potential errors during the HTTP request or JSON parsing processes. In the event of a failed API request or an unexpected response code, an exception is diligently thrown, guaranteeing the overall reliability and resilience of the application.

8.	Graceful Cancellation: The project includes a graceful cancellation mechanism through the implementation of the cancel method. By efficiently setting the isRunning flag to false, the execution of the source function can be gracefully stopped. This ensures a smooth and controlled termination of the data retrieval process.

## Conclusion:

Through the effective combination of these Apache Flink features, the project elegantly demonstrates the immense power and versatility of Apache Flink in real-time data processing scenarios. It serves as a solid foundation for the development of more intricate applications that involve streaming data from diverse sources, enabling advanced analytics and deep insights to be derived from the continuous data stream.


## Some Screenshots of the running project:

```

![11](https://github.com/josephib1/Apache-Flink/assets/105210115/b2420fb2-46ca-485a-b74e-304a03202abf)

![12](https://github.com/josephib1/Apache-Flink/assets/105210115/3d3b2803-f3b4-46bd-aa6b-ba77e492a6fa)

![13](https://github.com/josephib1/Apache-Flink/assets/105210115/5d2e0fed-ce20-49d6-be6f-de6c18dbcb38)

![14](https://github.com/josephib1/Apache-Flink/assets/105210115/728fc7e9-ca80-41df-b498-89ba9b2c5c8a)

![15](https://github.com/josephib1/Apache-Flink/assets/105210115/498873fa-a0bc-4dfa-9056-35279cf3d4d3)













































































