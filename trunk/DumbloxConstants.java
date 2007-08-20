import java.awt.Color;

public interface DumbloxConstants {
    // all the possible types of blocks in Tetrix
    enum BlockType {O_BLOCK, I_BLOCK, T_BLOCK, L_BLOCK, J_BLOCK, S_BLOCK, Z_BLOCK};
    enum RotateDirection {CLOCKWISE, C_CLOCKWISE};    // possible directions of block rotation
    enum Direction {DOWN, LEFT, RIGHT, UP};
    
    enum GameState {RUNNING, PAUSED, GAME_OVER};
    
    int DEFAULT_FPS = 60;
    
    // game speed constants
    int START_SPEED = 11;
    int END_SPEED = 55;
    int SPEED_INC = 4;
    
    // number of lines needed to advance to the next level
    int ROWS_PER_LEVEL = 10;

    //Scoring array
    int[] SCORING_ARRAY = {40, 100, 300, 1200};
    
    //Panel constants
    int PANEL_WIDTH = 640;
    int PANEL_HEIGHT = 480;
    Color BACKGROUND_COLOR = Color.black;
    
    //Grid constants
    int GRID_POSITION_X = 220; // the upper left corner (px)
    int GRID_POSITION_Y = 70;
    int GRID_ROWS = 20;
    int GRID_COLUMNS = 10;
        
    //Square constants
    int SQUARE_SIZE = 20;
    int SQUARE_PADDING = 2;
    
    //Block constants (in grid coordinates)
    int ACTIVE_BLOCK_X = GRID_COLUMNS / 2;
    int ACTIVE_BLOCK_Y = 0;
    // TODO it'd be nice if the upper left corner of next block was always in the same spot
    int NEXT_BLOCK_X = 15;
    int NEXT_BLOCK_Y = 4;
    
    //Game level and score constants
    int GAME_LEVEL_X = 510;
    int GAME_LEVEL_Y = 385;
    int GAME_SCORE_X = 80;
    int GAME_SCORE_Y = 185;
    int ROWS_CLEARED_X = 80;
    int ROWS_CLEARED_Y = 385;
    
    //For row fading
    int NUM_FADE_CYCLES = 60;
    
    //Square image files
    String RED_SQUARE = "/pics/red2.bmp";
    String BLUE_SQUARE = "/pics/blue2.bmp";
    String GREEN_SQUARE = "/pics/green2.bmp";
    String YELLOW_SQUARE = "/pics/yellow2.bmp";
    String PURPLE_SQUARE = "/pics/purple2.bmp";
    String CYAN_SQUARE = "/pics/cyan2.bmp";
    String GREY_SQUARE = "/pics/grey2.bmp";
    
    String BACKGROUND0 = "/pics/background0.png";
    String BACKGROUND1 = "/pics/background1.png";
    String BACKGROUND2 = "/pics/background2.png";
    String BACKGROUND3 = "/pics/background3.png";
    String BACKGROUND4 = "/pics/background4.png";
    String BACKGROUND5 = "/pics/background5.png";
    String BACKGROUND6 = "/pics/background6.png";
    String BACKGROUND7 = "/pics/background7.png";
    String BACKGROUND8 = "/pics/background8.png";
    String BACKGROUND9 = "/pics/background9.png";
    String BACKGROUND10 = "/pics/background10.png";
    String BACKGROUND11 = "/pics/background11.png";
}
