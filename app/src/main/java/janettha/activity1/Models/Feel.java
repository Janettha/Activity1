package janettha.activity1.Models;

/**
 * Created by janeth on 08/11/2017.
 */

public class Feel {
    String name;
    String color;

    public Feel(){   }

    public Feel(String n, String c){
        this.name = n;
        this.color = c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
