package unlv.com.bestpartytoday;

/**
 * Created by Kyle on 9/26/2015.
 */
public class Event {

    private String name;
    private String time;
    private float rating;
    private String image;
    private float latitude;
    private float longitude;


    public Event(String name, String time, float rating, String image, float latitude, float longitude) {
        super();
        this.name = name;
        this.time = time;
        this.rating = rating;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }

    public float getRating() {
        return rating;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
