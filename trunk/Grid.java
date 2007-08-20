import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Grid implements DumbloxConstants {
    private Point position; //The position of the grid (specifically, the top left corner) in pixel coordinates
    private int rows;
    private int columns;
    private Graphics2D g;
    private Square[][] pile;
    private boolean game_over;
    
    private AlphaComposite non_transparent; //The default composite for drawing the graphics (non-transparent)
    
    public Grid(Point position, int rows, int columns, Graphics2D g) {
        this.position = position;
        this.rows = rows;
        this.columns = columns;
        this.g = g;
        this.game_over = false;
        
        pile = new Square[rows][columns];
        non_transparent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
    }
    
    public void setGraphics(Graphics2D g) {
        this.g = g;
    }
    
    /**
     * This is the main draw method for the grid.
     */
    public void draw() {
        g.setColor(Color.WHITE);
        g.draw3DRect(position.x, position.y, columns * SQUARE_SIZE, rows * SQUARE_SIZE, true);
        drawPile();
    }
    
    /**
     * Draws a square on the grid. This will be called within public method drawBlock.
     * @param square the square to be drawn on the grid
     * 
     * TODO This method seems out of place here in the Grid class, but I guess having it here
     * allows us to have one Graphics obj that isn't passed around, yea? - Jason
     */
    private void drawSquare(Square square) {
        // don't draw squares that are above the grid
    	if (square.getPosition().y < 0) {
    		return;
    	}
    	
    	//Translate grid coordinates into window coordinates
        int x = (square.getPosition().x * SQUARE_SIZE) + position.x;
        int y = (square.getPosition().y * SQUARE_SIZE) + position.y;
        
        g.setComposite(square.getComposite());
        g.drawImage(square.getImage(), x + SQUARE_PADDING, y + SQUARE_PADDING, null);
        g.setComposite(non_transparent);
    }
    
    /**
     * Draws a block on the grid.
     * @param block the block to be drawn on the grid
     */
    public void drawBlock(Block block) {
        for (Square square : block.getSquares()) {
            drawSquare(square);
        }
    }
        
    /**
     * Draws the pile to the grid. This method is called by draw.
     */
    private void drawPile() {
        for (int i = 0; i < rows; i++) {
            int y = position.y + (SQUARE_SIZE * i);
            for (int j = 0; j < columns; j++) {
                int x = position.x + (SQUARE_SIZE * j);
                if (pile[i][j] != null) {
                    g.setComposite(pile[i][j].getComposite());
                    g.drawImage(pile[i][j].getImage(), x + SQUARE_PADDING, y + SQUARE_PADDING, null);
                }
            }
        }
        g.setComposite(non_transparent);
    }
    
    /**
     * Adds a block to the pile and deletes any completed rows. This method assumes that the block has already
     * collided with the pile.
     * @param block the block that will be added to the pile
     * @retrun a list of the completed rows
     */
    public ArrayList<Integer> addBlockToPile(Block block) {
        for (Square square : block.getSquares()) {
            Point position = square.getPosition();
            
            // check if the game should be over
            if (position.y == 0 && pile[position.y][position.x] != null) game_over = true;
            
            // add the square to the pile
            pile[position.y][position.x] = square;
        }
        
        ArrayList<Integer> completed_rows = new ArrayList<Integer>();
        for (int i = 0; i < pile.length; i++) {
            boolean row_complete = true;
            
            for (Square square : pile[i]) {
                if (square == null) {
                    row_complete = false;
                    break;
                }
            }
            
            if (row_complete) completed_rows.add(new Integer(i));
        }
        
        return completed_rows;
    }
    
    /**
     * Deletes all of the rows specified in row_list from the pile.
     * @param row_list the list of integers representing the rows that will be deleted from the pile.
     */
    public void deleteRows(ArrayList<Integer> row_list) {
        if (!row_list.isEmpty()) {
            for (Integer i : row_list) {
                deleteRow(i.intValue());
            }
        }
    }
    
    /**
     * Deletes a row from the pile.
     * @param num the row to be deleted
     */
    private void deleteRow(int num) {
        //Create a new grid array
        Square[][] new_pile = new Square[rows][columns];

        //First, move all of the rows above the deleted row down one
        for (int i = 1; i <= num; i++) {
            for (int j = 0; j < columns; j++) {
                new_pile[i][j] = pile[i - 1][j];
            }
        }
        
        //Now copy the rest of the rows
        for (int i = num + 1; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                new_pile[i][j] = pile[i][j];
            }
        }
        
        pile = new_pile;
    }
    
    /**
     * This method determines if a given block is colliding with another square in the pile or
     * a wall of the grid. This method is used for collision detection for rotating and moving a block.
     * It is assumed that the block that is passed in is already rotated/moved in the tentative direction
     * so that this method does not need to simulate the movement/rotation itself.
     * @param block the block whose collision is being detected
     * @return true if the block collides, false otherwise
     */
    public boolean collides(Block block) {
        for (Square square : block.getSquares()) {
            Point position = square.getPosition();
            
            //Check if the square collides with a wall
            if (position.x < 0                  //collides with left wall
                    || position.x > columns - 1 //collides with right wall
                    || position.y > rows - 1) { //collides with bottom
                return true;
            }
            
            //If the square is located above the grid, then this square is not colliding.
            //This could happen when the piece first appears and the user rotates it.
            if (position.y >= 0) {
                //Check if the square collides with the pile
                if (pile[position.y][position.x] != null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Sets the alpha composite for each of the squares in each of the rows specified in the list of rows.
     * @param rows contains the row numbers that will be faded
     * @param alpha_composite the alpha composite number (between 0.0 and 1.0), which controls the transparency
     */
    public void fadeRows(ArrayList<Integer> rows, float alpha_composite) {
        for (Integer row : rows) {
            for (Square square : pile[row.intValue()]) {
                square.setComposite(alpha_composite);
            }
        }
    }
    
    /**
     * Is the game over or not? Tell me!
     * @return true if the game is over; false otherwise
     */
    public boolean isGameOver() {
    	return game_over;
    }
}
