import java.awt.*;

public class Square implements DumbloxConstants {
    
    private Point grid_pos;
    private Image image;
    private String image_file;
    private AlphaComposite composite; //Used for fading effect
    
    public Square(Point grid_pos, Image image, String image_file) {
        this.grid_pos = grid_pos;
        this.image = image;
        this.image_file = image_file;
        setComposite(1);
    }
    
    /**
     * Get the current position of the square.
     * @return the square's current position.
     */
    public Point getPosition() { 
        return grid_pos; 
    }
    
    /**
     * Gets the image of the square. If you couldn't figure that out then I apoligize to society for you.
     * @return the square's image
     */
    public Image getImage() {
        return image;
    }
    
    /**
     * Gets the image file name.
     * @return the image file name
     */
    public String getImageFile() {
        return image_file;
    }
    
    /**
     * Move the block down, left, or right.
     * @param direction - the direction in which the square will be moved
     */
    public void move(Direction direction) {
        switch (direction) {
        case DOWN:
            grid_pos.y++;
            break;
        case LEFT:
            grid_pos.x--;
            break;
        case RIGHT:
            grid_pos.x++;
            break;
        case UP:   //This is for testing purposes
            grid_pos.y--;
            break;
        default:
            System.out.println("Unknown direction: " + direction);
        }
    }
    
    /**
     * Set alpha composite.  For example, pass in 1.0f to have 100% opacity and
     * pass in 0.25f to have 25% opacity.
     * @param alpha the float that determines the opacity
     */
    public void setComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        composite = AlphaComposite.getInstance(type, alpha);
    }

    public AlphaComposite getComposite() {
        return composite;
    }
}
