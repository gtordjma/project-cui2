import java.util.ArrayList;

public class Rectangle {
    private int height;
    private int width;
    private static ArrayList<Rectangle> instances = new ArrayList<>();
    private Boolean shape;

    public Rectangle(int height, int width) {
        this.height = height;
        this.width = width;
        this.shape = isRectSquare();
        this.instances.add(this);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    public static ArrayList<Rectangle> getAll() {
        return instances;
    }

    public boolean getShape(){
        return shape;
    }

    public boolean isRectSquare(){
        if (height == width){
            return true;
        }
        else {
            return false;
        }
    }

}