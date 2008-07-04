import java.awt.*;

public class Block implements DumbloxConstants {
    private static final int MAX_SQUARES = 4;
    private BlockType block_type;   // this block's type
    private Square rotations[][];   // 2D array of all possible rotations for this block
    private int rotation_index = 0; // the index of the current rotation
    
    /**
     * Block constructor. Initialize the rotations array.
     * @param grid_position - the position of the "center" Square of the Block
     *                        (ie. the Square about which the Block rotates)
     * @param block_type - the type of Block this is
     */
    public Block (Point grid_pos, BlockType block_type) {
        this.block_type = block_type;
        
        // based on the BlockType, create the block with all of its possible rotations
        switch (block_type) {
        case O_BLOCK:
            rotations = new Square[1][4];
            /* rotations[0]
             * [0][1]
             * [2][3]
             */
            rotations[0][0] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.GREY_IMAGE, GREY_SQUARE);
            rotations[0][1] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.GREY_IMAGE, GREY_SQUARE);
            rotations[0][2] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.GREY_IMAGE, GREY_SQUARE);
            rotations[0][3] = new Square(new Point(grid_pos.x + 1, grid_pos.y + 1), DumbloxPanel.GREY_IMAGE, GREY_SQUARE);
            break;
        case I_BLOCK:
            rotations = new Square[2][4];
            /* rotations[0]
             * [0][1][2][3]
             */
            rotations[0][0] = new Square(new Point(grid_pos.x - 2, grid_pos.y), DumbloxPanel.BLUE_IMAGE, BLUE_SQUARE);
            rotations[0][1] = new Square(new Point(grid_pos.x - 1, grid_pos.y), DumbloxPanel.BLUE_IMAGE, BLUE_SQUARE);
            rotations[0][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.BLUE_IMAGE, BLUE_SQUARE);
            rotations[0][3] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.BLUE_IMAGE, BLUE_SQUARE);
            
            /* rotations[1]
             *    [0]
             *    [1]
             *    [2]
             *    [3]
             */
            rotations[1][0] = new Square(new Point(grid_pos.x, grid_pos.y - 2), DumbloxPanel.BLUE_IMAGE, BLUE_SQUARE);
            rotations[1][1] = new Square(new Point(grid_pos.x, grid_pos.y - 1), DumbloxPanel.BLUE_IMAGE, BLUE_SQUARE);
            rotations[1][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.BLUE_IMAGE, BLUE_SQUARE);
            rotations[1][3] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.BLUE_IMAGE, BLUE_SQUARE);
            break;
        case T_BLOCK:
            rotations = new Square[4][4];
            /* rotations[0]
             * [3][2][1]
             *    [0]
             */
            rotations[0][0] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[0][1] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[0][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[0][3] = new Square(new Point(grid_pos.x - 1, grid_pos.y), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            
            /* rotations[1]
             *    [3]
             * [0][2]
             *    [1]
             */
            rotations[1][0] = new Square(new Point(grid_pos.x - 1, grid_pos.y), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[1][1] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[1][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[1][3] = new Square(new Point(grid_pos.x, grid_pos.y - 1), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            
            /* rotations[2]
             *    [0]
             * [1][2][3]
             */
            rotations[2][0] = new Square(new Point(grid_pos.x, grid_pos.y - 1), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[2][1] = new Square(new Point(grid_pos.x - 1, grid_pos.y), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[2][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[2][3] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            
            /* rotations[3]
             *    [1]
             *    [2][0]
             *    [3]
             */
            rotations[3][0] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[3][1] = new Square(new Point(grid_pos.x, grid_pos.y - 1), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[3][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            rotations[3][3] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.CYAN_IMAGE, CYAN_SQUARE);
            break;
        case L_BLOCK:
            rotations = new Square[4][4];
            /* rotations[0]
             * [3][2][1]
             * [0]
             */
            rotations[0][0] = new Square(new Point(grid_pos.x - 1, grid_pos.y + 1), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[0][1] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[0][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[0][3] = new Square(new Point(grid_pos.x - 1, grid_pos.y), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            
            /* rotations[1]
             * [0][3]
             *    [2]
             *    [1]
             */
            rotations[1][0] = new Square(new Point(grid_pos.x - 1, grid_pos.y - 1), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[1][1] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[1][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[1][3] = new Square(new Point(grid_pos.x, grid_pos.y - 1), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            
            /* rotations[2]
             *       [0]
             * [1][2][3]
             */
            rotations[2][0] = new Square(new Point(grid_pos.x + 1, grid_pos.y - 1), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[2][1] = new Square(new Point(grid_pos.x - 1, grid_pos.y), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[2][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[2][3] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            
            /* rotations[3]
             *    [1]
             *    [2]
             *    [3][0]
             */
            rotations[3][0] = new Square(new Point(grid_pos.x + 1, grid_pos.y + 1), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[3][1] = new Square(new Point(grid_pos.x, grid_pos.y - 1), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[3][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            rotations[3][3] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.GREEN_IMAGE, GREEN_SQUARE);
            break;
        case J_BLOCK:
            rotations = new Square[4][4];
            /* rotations[0]
             * [3][2][1]
             *       [0]
             */
            rotations[0][0] = new Square(new Point(grid_pos.x + 1, grid_pos.y + 1), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[0][1] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[0][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[0][3] = new Square(new Point(grid_pos.x - 1, grid_pos.y), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            
            /* rotations[1]
             *    [3]
             *    [2]
             * [0][1]
             */
            rotations[1][0] = new Square(new Point(grid_pos.x - 1, grid_pos.y + 1), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[1][1] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[1][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[1][3] = new Square(new Point(grid_pos.x, grid_pos.y - 1), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            
            /* rotations[2]
             * [0]
             * [1][2][3]
             */
            rotations[2][0] = new Square(new Point(grid_pos.x - 1, grid_pos.y - 1), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[2][1] = new Square(new Point(grid_pos.x - 1, grid_pos.y), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[2][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[2][3] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            
            /* rotations[3]
             *    [1][0]
             *    [2]
             *    [3]
             */
            rotations[3][0] = new Square(new Point(grid_pos.x + 1, grid_pos.y - 1), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[3][1] = new Square(new Point(grid_pos.x, grid_pos.y - 1), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[3][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            rotations[3][3] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.PURPLE_IMAGE, PURPLE_SQUARE);
            break;
        case S_BLOCK:
            rotations = new Square[2][4];
            /* rotations[0]
             *    [0][1]
             * [2][3]
             */
            rotations[0][0] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.RED_IMAGE, RED_SQUARE);
            rotations[0][1] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.RED_IMAGE, RED_SQUARE);
            rotations[0][2] = new Square(new Point(grid_pos.x - 1, grid_pos.y + 1), DumbloxPanel.RED_IMAGE, RED_SQUARE);
            rotations[0][3] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.RED_IMAGE, RED_SQUARE);
            
            /* rotations[1]
             *    [2]
             *    [3][0]
             *       [1]
             */
            rotations[1][0] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.RED_IMAGE, RED_SQUARE);
            rotations[1][1] = new Square(new Point(grid_pos.x + 1, grid_pos.y + 1), DumbloxPanel.RED_IMAGE, RED_SQUARE);
            rotations[1][2] = new Square(new Point(grid_pos.x, grid_pos.y - 1), DumbloxPanel.RED_IMAGE, RED_SQUARE);
            rotations[1][3] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.RED_IMAGE, RED_SQUARE);
            break;
        case Z_BLOCK:
            rotations = new Square[2][4];
            /* rotations[0]
             * [0][1]
             *    [2][3]
             */
            rotations[0][0] = new Square(new Point(grid_pos.x - 1, grid_pos.y), DumbloxPanel.YELLOW_IMAGE, YELLOW_SQUARE);
            rotations[0][1] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.YELLOW_IMAGE, YELLOW_SQUARE);
            rotations[0][2] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.YELLOW_IMAGE, YELLOW_SQUARE);
            rotations[0][3] = new Square(new Point(grid_pos.x + 1, grid_pos.y + 1), DumbloxPanel.YELLOW_IMAGE, YELLOW_SQUARE);
            
            /* rotations[1]
             *       [0]
             *    [2][1]
             *    [3]
             */
            rotations[1][0] = new Square(new Point(grid_pos.x + 1, grid_pos.y - 1), DumbloxPanel.YELLOW_IMAGE, YELLOW_SQUARE);
            rotations[1][1] = new Square(new Point(grid_pos.x + 1, grid_pos.y), DumbloxPanel.YELLOW_IMAGE, YELLOW_SQUARE);
            rotations[1][2] = new Square(new Point(grid_pos.x, grid_pos.y), DumbloxPanel.YELLOW_IMAGE, YELLOW_SQUARE);
            rotations[1][3] = new Square(new Point(grid_pos.x, grid_pos.y + 1), DumbloxPanel.YELLOW_IMAGE, YELLOW_SQUARE);
            break;
        default:
            System.out.println("Random number generator picked an invalid BlockType");
        }
    }
    
    /**
     * get all the squares of the current rotation
     * @return the squares of the current rotation
     */
    public Square[] getSquares() {
        return rotations[rotation_index];
    }
    
    /**
     * rotate() simulates a block rotation merely by incrementing
     * or decrementing the index of the rotations array.
     * @param pile - the pile of Squares at the bottom
     * @param rotate_direction - the direction of rotation corresponding
     *                           to the user-pressed key
     * @return true if the rotation was successful; false otherwise
     */
    public boolean rotate(Grid grid, RotateDirection rotate_direction) {
        int index = rotation_index;
        
        switch (rotate_direction) {
        case CLOCKWISE:
            // rotate the block clockwise
            rotation_index = (rotation_index + 1) % rotations.length;
            break;
        case C_CLOCKWISE:
            // rotate the block counter-clockwise
            rotation_index = ((rotation_index - 1) + rotations.length) % rotations.length;
            break;
        default:
            System.out.println("Unknown rotation direction");
        }
        
        /* Check the Grid to see if this new rotation causes a collision with a wall or
         * the pile of Squares at the bottom. If collision, then revert the rotation_index
         * back to index and return false
         */
        if (grid.collides(this)) {
            rotation_index = index;
            return false;
        }
        
        return true;
    }
    
    /**
     * move the Block by calling move() on the Squares
     * @param grid - Grid obj containing info on the walls/pile of Squares
     * @param direction - the direction of movement corresponding to the
     *                    key the user pressed
     * @return true if the move was succesful; false otherwise
     */
    public boolean move(Grid grid, Direction direction) {
        for (int i = 0; i < rotations.length; i++) {
            for (int j = 0; j < MAX_SQUARES; j++) {
                rotations[i][j].move(direction);
            }
        }
        
        // Check the Grid for collisions and move everything back if true
        if (grid.collides(this)) {
            // undo the move
            unmove(grid, direction);
            return false;
        }
        
        return true;
    }
    
    /**
     * This method un-does the move in the direction that is passed in. This is called in the move() method
     * when a collision is detected after simulating a move.
     * @param grid Grid obj containing info on the walls/pile of Squares
     * @param direction the direction of the previous movement (which will now be reversed)
     */
    private void unmove(Grid grid, Direction direction) {
        //Change the direction accordingly
        switch (direction) {
        case DOWN:
            direction = Direction.UP;
            break;
        case UP: //for testing purposes
            direction = Direction.DOWN;
            break;
        case LEFT:
            direction = Direction.RIGHT;
            break;
        case RIGHT:
            direction = Direction.LEFT;
            break;
        }
        
        //Now move the rotations back
        for (Square[] rotation : rotations) {
            for (Square square : rotation) {
                square.move(direction);
            }
        }
    } 

    /**
     * Find out what type of block this tetrimino is
     * @return the block type
     */
    public BlockType getBlockType() {
        return block_type;
    }
    
    /**
     * Continuously call move on the block until it collides with the pile (returns false)
     * @param grid - Grid obj containing info on the pile of Squares
     */
    public void dropBlock(Grid grid) {
        while (move(grid, Direction.DOWN)) {}
    }
    
    /**
     * draw the Block by calling the drawBlock() method
     * of the Grid (which draws all the Squares)
     * @param grid - the Graphics object
     */
    public void draw(Grid grid) {
        grid.drawBlock(this);
    }
}