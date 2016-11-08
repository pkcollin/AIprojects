/**
 * Created by liberato on 9/16/14.
 */
public class Placement {
    public final String name;
    public final int width;
    public final int height;
    public final int x;
    public final int y;

    public Placement(String name, int width, int height, int x, int y) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    

    @Override
    public String toString() {
        return name + " " + width + " " + height + " " + x + " " + y;
    }
}

