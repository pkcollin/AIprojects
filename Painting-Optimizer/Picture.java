/**
 * Created by liberato on 9/16/14.
 */
public class Picture implements Comparable {
    public final String name;
    public final int width;
    public final int height;

    @Override
    public String toString() {
        return name + " " + width + " " + height;
    }

    public Picture(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    @Override
    public int compareTo(Object o) {
        Picture other = (Picture)o;
        int area = width * height;
        int otherArea = other.width * other.height;
        if(area == otherArea){
        	return 0;
        }
        if(area > otherArea){
        	return 1;
        }
        return -1;
    }
}

