
public class EventEntry {
    String id;
    int eventDuration;
    long timestamp;
    String type, host;
    boolean alert;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public int getEventDuration() {
        return eventDuration;
    }
    
    public void setEventDuration(int eventDuration) {
        this.eventDuration = eventDuration;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public boolean getAlert() {
        return alert;
    }
    
    public void setAlert(boolean alert) {
        this.alert = alert;
    }
}