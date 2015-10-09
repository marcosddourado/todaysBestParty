package unlv.com.bestpartytoday;

/**
 * Created by Kyle on 9/26/2015.
 */
public class Event {

    private String name;
    private String time;
    private float rating;
    private String image;

    public Event(String name, String time, float rating, String image) {
        super();
        this.name = name;
        this.time = time;
        this.rating = rating;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public float getRating() {
        return rating;
    }
}
