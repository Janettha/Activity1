package janettha.activity1.Models;

/**
 * Created by janeth on 22/11/2017.
 */

public class ImageUpload {
    public String feel;
    public int id;
    public String name;
    public String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getFeel() { return feel; }

    public int getId() { return id; }

    public ImageUpload(String name, String feel, int id) {
        this.name = name;
        this.feel = feel;
        this.id = id;
    }

    public ImageUpload(){}
}
