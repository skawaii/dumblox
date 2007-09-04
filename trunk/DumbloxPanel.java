import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

public class DumbloxPanel extends JPanel implements Runnable, DumbloxConstants {
    /* Number of frames with a delay of 0 ms before the animation thread yields
    to other running threads. */
    private static final int NUM_DELAYS_PER_YIELD = 16;

    /* number of frames that can be skipped in any one animation loop
    i.e the games state is updated but not rendered */
    private static int MAX_FRAME_SKIPS = 5;
    
    private Thread animator_thread;  //animation thread
    
    private boolean running = false;   //stops the animation
    private boolean is_paused = false; //pauses the game
    private boolean game_over = false; //for game termination
    
    private long game_start_time;
    private long frames_skipped = 0;
    private long prev_stats_time; // TODO also not being used anywhere...
    
    private int loop_period; // period between screen drawing
    private int game_level;  // used to determine scoring
    private int game_speed;  // based on the lvl of the game; determines how fast blocks fall
    private int total_rows_cleared;  // for record keeping purposes
    private int total_score; // keep track of the player's score
    
    //Number of cycles since the active block has been moved down by the game (not user)
    private int periods_since_forced_move; 
 
    // TODO dx_top really doesn't seem to be of any use...
	private Dumblox dx_top;
    
    private Graphics2D db_graphics;
    private Image db_image = null;
    
    private Grid grid;
    private Timer timer;
    
    // The current and nextblock
    private Block active_block;
    private Block next_block;
    
    //A random number generator for selecting the blocks
    Random generator;
    
    HashMap<Integer, Image> backgrounds;
    
    //The pre-loaded images for squares
    public static Image RED_IMAGE;
    public static Image BLUE_IMAGE;
    public static Image GREEN_IMAGE;
    public static Image YELLOW_IMAGE;
    public static Image PURPLE_IMAGE;
    public static Image CYAN_IMAGE;
    public static Image GREY_IMAGE;
    
    public static Image BACKGROUND;
    
    /**
     * DumbloxPanel constructor that sets up some variables and configures
     * the JPanel.
     * @param dx - the Dumblox game obj
     * @param loop_period - desired time for the animation/game loops
     */
    public DumbloxPanel(Dumblox dx, int loop_period) {
        dx_top = dx;
        this.loop_period = loop_period;
        
        // Initialize some of the game variables
        game_level = 0;
        game_speed = START_SPEED;
        total_rows_cleared = 0;
        total_score = 0;
        periods_since_forced_move = 0; //The number of loop periods since the block was forced downwards
        backgrounds = new HashMap<Integer, Image>();
        
        // setup the JPanel
        setBackground(Color.black);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        
        setFocusable(true);
        requestFocus(); // JPanel now receives key events
        readyForTermination();

        // instantiate the random number generator
        generator = new Random();
        
        // add key listeners so the game knows what to do when certain keys are pressed
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!is_paused && active_block != null) {
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        active_block.move(grid, Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        active_block.move(grid, Direction.RIGHT);
                        break;
                    case KeyEvent.VK_UP: //This is for testing purposes
                        active_block.move(grid, Direction.UP);
                        break;
                    case KeyEvent.VK_ENTER: //This is for testing purposes
                        active_block = new Block(new Point(GRID_COLUMNS / 2, 0), 
                                (BlockType.values()[(active_block.getBlockType().ordinal() + 1) % BlockType.values().length]));
                    case KeyEvent.VK_DOWN:
                        moveBlockDown();
                        break;
                    case KeyEvent.VK_SPACE:
                        // drop the block and add it to the pile (which creates a new block)
                        active_block.dropBlock(grid);
                        addBlockToPile();                        
                        periods_since_forced_move = 0;
                        break;
                    case KeyEvent.VK_X:
                        // rotate the block clockwise
                        active_block.rotate(grid, RotateDirection.CLOCKWISE);
                        break;
                    case KeyEvent.VK_Z:
                        // rotate the block counter-clockwise
                        active_block.rotate(grid, RotateDirection.C_CLOCKWISE);
                        break;
                    }
                }
                
                switch(e.getKeyCode()) {
                case KeyEvent.VK_P:
                case KeyEvent.VK_PAUSE:
                    togglePause();
                }
            }
        });
        
        /**
         * Create the game components
         */
        //Create the square images so that they will already be in memory
        try {
            RED_IMAGE    = ImageIO.read(getClass().getResource(RED_SQUARE));
            BLUE_IMAGE   = ImageIO.read(getClass().getResource(BLUE_SQUARE));
            GREEN_IMAGE  = ImageIO.read(getClass().getResource(GREEN_SQUARE));
            YELLOW_IMAGE = ImageIO.read(getClass().getResource(YELLOW_SQUARE));
            PURPLE_IMAGE = ImageIO.read(getClass().getResource(PURPLE_SQUARE));
            CYAN_IMAGE   = ImageIO.read(getClass().getResource(CYAN_SQUARE));
            GREY_IMAGE   = ImageIO.read(getClass().getResource(GREY_SQUARE));
            
            backgrounds.put(0, ImageIO.read(getClass().getResource(BACKGROUND0)));
            backgrounds.put(1, ImageIO.read(getClass().getResource(BACKGROUND1)));
            backgrounds.put(2, ImageIO.read(getClass().getResource(BACKGROUND2)));
            backgrounds.put(3, ImageIO.read(getClass().getResource(BACKGROUND3)));
            backgrounds.put(4, ImageIO.read(getClass().getResource(BACKGROUND4)));
            backgrounds.put(5, ImageIO.read(getClass().getResource(BACKGROUND5)));
            backgrounds.put(6, ImageIO.read(getClass().getResource(BACKGROUND6)));
            backgrounds.put(7, ImageIO.read(getClass().getResource(BACKGROUND7)));
            backgrounds.put(8, ImageIO.read(getClass().getResource(BACKGROUND8)));
            backgrounds.put(9, ImageIO.read(getClass().getResource(BACKGROUND9)));
            backgrounds.put(10, ImageIO.read(getClass().getResource(BACKGROUND10)));
            backgrounds.put(11, ImageIO.read(getClass().getResource(BACKGROUND11)));
        } catch (IOException e) {
            System.out.println("Error reading image file: " + e.getMessage());
            System.exit(1);
        }
        
        BACKGROUND = backgrounds.get(0);
        grid = new Grid(new Point(GRID_POSITION_X, GRID_POSITION_Y), GRID_ROWS, GRID_COLUMNS, db_graphics);
        timer = new Timer();
        
        // Select new random blocks to start the game with
        BlockType new_type = BlockType.values()[generator.nextInt(BlockType.values().length)];
        active_block = new Block(new Point(ACTIVE_BLOCK_X, ACTIVE_BLOCK_Y), new_type);
        
        new_type = BlockType.values()[generator.nextInt(BlockType.values().length)];
        next_block = new Block(new Point(NEXT_BLOCK_X, NEXT_BLOCK_Y), new_type);
        
        // listen for mouse presses
        addMouseListener( new MouseAdapter () { // TODO Do we need the mouse at all?
            public void mousePressed(MouseEvent e) { 
                testPress(e.getX(), e.getY()); 
            }
        });    
    }
    
    /**
     * Repeatedly update, render, sleep
     * @see java.lang.Runnable#run()
     */
    public void run() {
        long before_time, after_time, time_diff, sleep_time;
        int over_sleep_time = 0;
        int num_delays = 0;
        int excess_time = 0;
        //Graphics g;
        
        game_start_time = System.currentTimeMillis();
        prev_stats_time = game_start_time;
        before_time = game_start_time;
        
        running = true;
        
        while (running) {
            updateGame();  // game state is updated
            renderGame();  // draw to an off-screen buffer
            paintScreen(); // draw buffer to screen
            
            after_time = System.currentTimeMillis();
            time_diff = after_time - before_time;
            sleep_time = (loop_period - time_diff) - over_sleep_time; // time left in this loop
            
            if (sleep_time > 0) { // some time left in this cycle
                try {
                    Thread.sleep(sleep_time);
                }
                catch (InterruptedException ex) {}
                over_sleep_time = (int)((System.currentTimeMillis() - after_time) - sleep_time);
            }
            else { // the frame took longer than the period
            	// add the extra time that the update/render/paint cycle took (over the desired period)
            	excess_time -= sleep_time;
                over_sleep_time = 0;
                
                if (++num_delays >= NUM_DELAYS_PER_YIELD) {
                    Thread.yield(); // give another thread a chance to run
                    num_delays = 0;
                }
            }
            
            before_time = System.currentTimeMillis();
            
            /* If frame animation is taking too long, update the game state
             without rendering it, to get the updates/sec nearer to
             the required FPS. */
            int skips = 0;
            
            while ((excess_time > loop_period) && (skips < MAX_FRAME_SKIPS)) {
                excess_time -= loop_period;
                updateGame();    // update state but don't render
                skips++;
            }
            
            frames_skipped += skips;
        }
        
        // once it kicks out of the while loop (i.e. !running), we want to exit
        System.exit(0);
    }

    /**
     * Updates the game state. This method will be run every
     * period (iteration of the run() loop).
     */
    private void updateGame() {
        timer.tick(); //Timer tick (scheduled tasks are performed here)
        
        if (!is_paused && !game_over && active_block != null) {
            //Make the active block move down every FPS-gameSpeed cycles
            if (periods_since_forced_move >= Dumblox.DEFAULT_FPS - game_speed) {
                moveBlockDown();
            }
            else {
                periods_since_forced_move++;
            }
            
            checkForGameOver();
        }
    }
    
    /**
     * Draws the current frame to an off-screen image buffer.
     */
    private void renderGame() {
        if (db_image == null) {
            /* create the off-screen image buffer.
             * NOTE: createImage is a method from Component,
             * inherited by JPanel.
             */
            db_image = createImage(PANEL_WIDTH, PANEL_HEIGHT);
            
            if (db_image == null) {
                System.out.println("dbImage is null");
                return;
            }
            else {
                // getGraphics() creates a drawing context
                db_graphics = (Graphics2D)db_image.getGraphics();
                grid.setGraphics(db_graphics);
            }
        }
        
        // draw the background
        drawBackground();
        
        // draw game elements here
        if (active_block != null && next_block != null && !is_paused) {
            active_block.draw(grid);
            next_block.draw(grid);
        }
        
        // draw the grid and score
        if (!is_paused) grid.draw();
        drawGameLevel();
        drawGameScore();
        drawRowsCleared();
        
        if (is_paused) printPauseMessage();
        if (game_over) printGameOverMessage();
    }
    
	/**
     * Actively renders the buffer image to the screen
     */
    private void paintScreen() {
        Graphics g;
        
        try {
            g = this.getGraphics(); // get the panel's graphic context
            
            if ((g != null) && (db_image != null)) {
                g.drawImage(db_image, 0, 0, null);
            }
            
            // sync the display on Linux systems
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        catch (Exception e) { 
            System.out.println("Graphics context error: " + e); 
        }
    }
    
    /**
     * Waits for key presses that signal termination (esc, q, end, ctrl-c).
     */
    private void readyForTermination() {
        addKeyListener(new KeyAdapter() {
            // listen for esc, q, end, ctrl-c
            public void keyPressed(KeyEvent e)
            {
                int keyCode = e.getKeyCode();
                if ((keyCode == KeyEvent.VK_ESCAPE) ||
                        (keyCode == KeyEvent.VK_Q) ||
                        (keyCode == KeyEvent.VK_END) ||
                        ((keyCode == KeyEvent.VK_C) && e.isControlDown())) {
                    running = false;
                }
            }
        });
    }
    
    /**
     * This method gets run when the user clicks on the mouse
     * @param x the x-coordinate position of the mouse pointer
     * @param y the y-coordinate position of the mouse pointer
     */
    private void testPress(int x, int y) {
        if (!is_paused && !game_over) {
            //do stuff
        }
    }
    
    /**
     * Wait for the JPanel to be added to the JFrame/JApplet
     * before starting. addNotify() is called automatically
     * while the DumbloxPanel is being added to its enclosing JFrame
     * @see java.awt.Component#addNotify()
     */
    public void addNotify() {
        super.addNotify(); //creates the peer
        startGame(); //start the thread
    }
    
    /**
     * Initializes and starts the thread
     */
    private void startGame() {
        if (animator_thread == null || !running) {
            animator_thread = new Thread(this);
            animator_thread.start();
        }
    }  
    
    /**
     * This method is called by the user to stop execution.
     */
    public void stopGame() { 
        running = false; 
    }
    
    /**
     * Draw the background
     */
    public void drawBackground() {
    	db_graphics.drawImage(BACKGROUND, 0, 0, null);
    }
    
    /**
     * Draw the current game level on the panel
     */
    public void drawGameLevel() {
    	db_graphics.drawString("Level: " + game_level, GAME_LEVEL_X, GAME_LEVEL_Y);
    }
    
    /**
     * Draw the current game score on the panel
     */
    public void drawGameScore() {        
    	db_graphics.drawString("Score: " + total_score, GAME_SCORE_X, GAME_SCORE_Y);
    }
    
    /**
     * Draw the total rows cleared on the panel
     */
    public void drawRowsCleared() {        
    	db_graphics.drawString("Rows: " + total_rows_cleared, ROWS_CLEARED_X, ROWS_CLEARED_Y);
    }
    
    /**
     * Adds the active block to the pile and in case of row completion schedules fading tasks and does
     * score calculation.
     */
    private void addBlockToPile() {
        ArrayList<Integer> completed_rows = grid.addBlockToPile(active_block);
        
        //If there are completed rows, then schedule fading tasks
        if (!completed_rows.isEmpty()) {
            //Stop drawing the active block
            active_block = null;
            
            ArrayList<Object> obj_list = new ArrayList<Object>();
            obj_list.add(this);
            obj_list.add(completed_rows);
            
            Task fade_task = new Task(1, NUM_FADE_CYCLES, obj_list, true) {
                public Object run(int counter, Object result) {
                    ArrayList<Object> obj_list = (ArrayList<Object>) result;
                    DumbloxPanel tp = (DumbloxPanel) obj_list.get(0);
                    ArrayList<Integer> completed_rows = (ArrayList<Integer>) obj_list.get(1);
                    
                    //Row fading logic here
                    tp.grid.fadeRows(completed_rows, 1.0f - ((1.0f / NUM_FADE_CYCLES) * (counter + 1)));
                    
                    return result;
                }
            };
            
            Task end_task = new Task(NUM_FADE_CYCLES, 1, obj_list, true) {
                public Object run(int counter, Object result) {
                    ArrayList<Object> obj_list = (ArrayList<Object>) result;
                    DumbloxPanel tp = (DumbloxPanel) obj_list.get(0);
                    ArrayList<Integer> completed_rows = (ArrayList<Integer>) obj_list.get(1);
                    
                    /* Calculate the score for this clearing and update the total score.
                     * Also update the number of cleared rows and see if it's time to go
                     * to the next level. 
                     */
                    tp.updateScore(completed_rows.size());
                    tp.checkForLevelChange(completed_rows.size());
                    
                    tp.grid.deleteRows(completed_rows);
                    tp.updateBlock();
                    return result;
                }
                
            };
            
            timer.schedule("fade", fade_task);
            timer.schedule("end_fade", end_task);
        }
        else {
            updateBlock();
        }
    }
    
    /**
     * Selects new random blocks to be the active and next blocks.
     */
    public void updateBlock() {
    	/* although we've already created the next_block, it's easier to just recreate and let the
    	 * old next_block obj get garbage collected
    	 */
        active_block = new Block(new Point(ACTIVE_BLOCK_X, ACTIVE_BLOCK_Y), next_block.getBlockType());
        
        BlockType new_type = BlockType.values()[generator.nextInt(BlockType.values().length)];
        next_block = new Block(new Point(NEXT_BLOCK_X, NEXT_BLOCK_Y), new_type);
    }
    
    /**
     * Placed in a method because the same code was getting called at least twice
     */
    private void moveBlockDown() {
    	if (!active_block.move(grid, Dumblox.Direction.DOWN)) {
            //Collision has occurred. Add active_block to pile and make a new block
            addBlockToPile();
        }
    	
        periods_since_forced_move = 0;
    }

    /**
     * The grid is keeping track of whether or not a block colliding with the
     * pile should cause the game to end, so we just ask Grid if the game is over.
     * If the game is over, then set the game_over variable accordingly.
     */
    public void checkForGameOver() {
    	if (grid.isGameOver()) game_over = true;
    }
    
    /**
     * Draws the game over message to the screen.
     */
    private void printGameOverMessage() {
    	// TODO make this msg into a graphic instead of text
        String msg = "You're an incompetent fool";
        int x = PANEL_WIDTH / 3;
        int y = PANEL_HEIGHT / 2;
        
        db_graphics.drawString(msg, x, y);
    }
    
    /**
     * When the game is paused, print "PAUSE" on the
     * screen so the player knows the game is paused.
     */
    private void printPauseMessage() {
    	// TODO make this msg into a graphic instead of text
    	String msg = "PAUSE";
        int x = PANEL_WIDTH / 2 - 10;
        int y = PANEL_HEIGHT / 2;
        
        db_graphics.drawString(msg, x, y);
    }
    
    /**
     * is_paused ? false : true
     * reverse the polarity of the is_paused variable
     */
    public void togglePause() {
        is_paused = !is_paused; 
    }

    public void pauseGame() {
        is_paused = true;
    }

    public void resumeGame() {
        is_paused = false;
    }
    
    /**
     * Calculate the score for the latest line(s) clearing and
     * update the total score
     * @param num_rows - the number of lines that were just cleared
     */
    public void updateScore(int num_rows) {
        total_score += (game_level + 1) * SCORING_ARRAY[num_rows - 1];
        //System.out.println(num_rows + " row" + (num_rows > 1 ? "s" : "") + " cleared. Score: "
        	//+ (game_level + 1) * SCORING_ARRAY[num_rows - 1] + " Total Score: " + total_score);
    }
    
    /**
     * Determine if enough rows have been cleared for the player
     * to advance to the next level.
     * @param num_rows - the number of rows that the player just cleared
     */
    public void checkForLevelChange(int num_rows) {
        // add num_rows to the total number of rows cleared
        total_rows_cleared += num_rows;
        //System.out.println("total_rows_cleared = " + total_rows_cleared);
        
        if (total_rows_cleared >= (game_level + 1) * ROWS_PER_LEVEL) {
        	game_level++;
            if (game_speed + SPEED_INC <= END_SPEED) {
            	game_speed += SPEED_INC;
            
            	// change the background
            	BACKGROUND = backgrounds.get(game_level);
            }
        }
    }
  
}
