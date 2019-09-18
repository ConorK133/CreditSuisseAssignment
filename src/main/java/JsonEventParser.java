import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
 
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class JsonEventParser {
    
    public static void main(String [] args) {
    	String currPath = System.getProperty("user.dir");
        //create JsonReader object and pass it the json file. I use a Input stream and Gson's builder here to account for larger files
        try(
        	JsonReader jsonReader = new JsonReader(
                new InputStreamReader(
                        new FileInputStream(args[0]), StandardCharsets.UTF_8))) {
            Gson gson = new GsonBuilder().create();
            jsonReader.beginArray(); //start of json array
            int numberOfRecords = 0;
           
            while (jsonReader.hasNext()){ //next json array element
            	try {
            		EventEntry newEvent = gson.fromJson(jsonReader, EventEntry.class);
            		Connection c = DriverManager.getConnection("jdbc:hsqldb:file:" + currPath + "/hsqldb/lib/mydb", "SA", "");
            		String getExisting = "SELECT * FROM PUBLIC.Event WHERE eventId = ?;";
            		PreparedStatement getExisStatement = c.prepareStatement(getExisting);
            		getExisStatement.setString(1, newEvent.getId());
            		ResultSet result = getExisStatement.executeQuery();
            		result.next();
            		
            		//Check to see if the Event already exists in the DB, if it does not, we create it
                	if (result.getRow() == 0) {
                		String createQuery = "INSERT INTO PUBLIC.Event (eventId, eventDuration, timeStart, type, host, alert)"
                				+ " VALUES (?, ?, ?, ?, ?, ?)";
                		PreparedStatement ps = c.prepareStatement(createQuery);
                		ps.setString(1, newEvent.getId());
                		ps.setInt(2, 0);
                		ps.setInt(3, (int)newEvent.getTimestamp());
                		ps.setBoolean(6, false);
                		
                		final String typeNull = newEvent.getType() != null ? newEvent.getType() : null;
                		ps.setString(4, typeNull);
                		final String hostNull = newEvent.getHost() != null ? newEvent.getHost() : null;
                		ps.setString(5, hostNull);
                		
                		ps.executeUpdate();
                	} else { //if it does already exist calculate the duration and need for an alarm and update the DB
                		int existingTime = result.getInt("timeStart");
                		int duration = (existingTime - (int)newEvent.getTimestamp());
                		duration = duration < 0 ? duration * -1 : duration;
                		String updateQuery = "UPDATE PUBLIC.Event SET eventDuration = ?, alert = ? WHERE eventId = ?";
                		PreparedStatement ps = c.prepareStatement(updateQuery);
                		ps.setInt(1, duration);
                		final boolean needAlert = duration >= 4 ? true : false;
                		ps.setBoolean(2, needAlert);
                		ps.setString(3, newEvent.getId());
                		
                		ps.executeUpdate();
                	}
                
                	c.close();
                    numberOfRecords++;
            	}
            	catch (Exception e) {
            		e.printStackTrace();
            	}
            }
          
            jsonReader.endArray();
            System.out.println("Total Records Found : "+numberOfRecords);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
