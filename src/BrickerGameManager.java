package src;

import danogl.components.CoordinateSpace;
import src.brick_strategies.BrickStrategyFactory;
import src.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.gameobjects.*;
import danogl.util.Counter;
import java.util.Random;

public class BrickerGameManager extends GameManager{
    // various constants to be used for objects dimensions and locations on screen
    public static final int BORDER_WIDTH = 10;
    private static final int MAIN_WINDOW_X = 700;
    private static final int MAIN_WINDOW_Y = 500;
    private static final int STARTING_LIVES = 4;
    private static final int BUFFER_FROM_WALL = 11;
    private static final int HEART_RADIUS = 25;
    private static final int HEART_X_OFFSET = 15;
    private static final int HEART_Y_OFFSET = 15;
    private static final int NUMERIC_X_OFFSET = 5;
    private static final int NUMERIC_Y_OFFSET = 50;
    private static final int NUMERIC_COUNTER_DIMENSIONS = 15;
    private static final int BALL_SPEED = 250;
    private static final int BALL_RADIUS = 20;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_Y_OFFSET = 65;
    private static final int NUM_OF_BRICKS_PER_LINE = 20;
    private static final int NUM_OF_BRICKS_LINES = 5;
    private static final int BRICK_HEIGHT = 15;
    private static final int SPACE_BETWEEN_BRICKS = 3;
    private static final int SPACE_BETWEEN_LINES = 20;
    private static final int FIRST_BRICK_LINE_Y_OFFSET = 30;
    // messages displayed to user
    private static final String WIN_PROMPT = "You Won!";
    private static final String LOSE_PROMPT = "You Lost!";
    private static final String AGAIN_PROMPT = " Play again?";
    private static final String TITLE_SCREEN = "Bricker by Omri Wolf";
    // path to assets used in the game.
    private static final String BALL_PATH = "assets/ball.png";
    private static final String PADDLE_PATH = "assets/paddle.png";
    private static final String COLLISION_SOUND = "assets/blop_cut_silenced.wav";
    private static final String BG_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String BRICK_PATH = "assets/brick.png";
    private static final String HEART_PATH = "assets/heart.png";

    private GameObject ball;
    private final Vector2 windowDimensions;
    private WindowController windowController;
    private Counter bricksCounter;
    private Counter livesCounter;

    /**
     * Creates a new window with the specified title and of the specicied dimensions
     * @param windowTitle can be null to indicate the usage of the default window title
     * @param windowDimensions dimensions in pixels. can be null to indicate a full-screen window whose size in pixels is the main screen's resolution.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
    }

    /**
     * The method will initiliaze the Bricker game and all of it's asset.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     * @param soundReader Contains a single method: readSound, which reads a wav file from disk.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether a given key is currently pressed by the user or not.
     * @param windowController Contains an array of helpful, self explanatory methods concerning the window.
     * See Also:
     * ImageReader, SoundReader, UserInputListener, WindowController
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(80);
        this.windowController = windowController;
        // will be used to count how many bricks are left
        this.bricksCounter = new Counter();
        // indicates how many lives user has.
        this.livesCounter = new Counter(STARTING_LIVES);
        // create all assets
        createBackground(imageReader);
        createWalls();
        createBall(imageReader, soundReader);
        createPaddle(imageReader, inputListener);
        BrickStrategyFactory strategyFactory = new BrickStrategyFactory(gameObjects(), this,
                imageReader, soundReader, inputListener, windowController, this.windowDimensions);
        createBricks(imageReader, strategyFactory);
        createGraphicLifeCounter(imageReader);
        createNumericLifeCounter();
    }

    /**
     * Called once per frame. checks if player has won or lost,
     * and also removes each object that is out of the screen.
     * @param deltaTime The time, in seconds, that passed since the last invocation of this method
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkWinLoseCondition();
        removeOutOfScreen();
    }

    /**
     * Method checks for win/lose condition (0 lives or all bricks destroyed). if the game ended,
     * displays play again question to the user, and then closes the game or resets the game.
     */
    private void checkWinLoseCondition() {
        String prompt = "";
        // check if ball went below the paddle.
        float ballHeight = this.ball.getCenter().y();
        if (ballHeight > this.windowDimensions.y()) {
            // if lives are left, decrement lives and reset ball. else, generate lose prompt to user.
            this.livesCounter.decrement();
            repositionBall(this.ball);
            if (this.livesCounter.value() == 0)
                prompt = LOSE_PROMPT;
        }
        // check if no bricks are left, generate win prompt to user.
        if (this.bricksCounter.value() == 0)
            prompt = WIN_PROMPT;
        // if prompt was generated, open play again dialog for user.
        if (!prompt.isEmpty()) {
            prompt += AGAIN_PROMPT;
            if (this.windowController.openYesNoDialog(prompt))
                this.windowController.resetGame();
            else
                this.windowController.closeWindow();
        }
    }

    /**
     * Method goes over all objects and removes each object that went below the screen (like pucks or Status Definers).
     */
    private void removeOutOfScreen() {
        for (GameObject object : gameObjects()) {
            float verticalLocation = object.getCenter().y();
            if (verticalLocation > this.windowDimensions.y()) {
                gameObjects().removeGameObject(object);
            }
        }
    }

    /**
     * method creates all the bricks in the game.
     * @param imageReader type Image Reader, to scan image files.
     * @param strategyFactory to create collision strategies for all bricks.
     */
    private void createBricks(ImageReader imageReader, BrickStrategyFactory strategyFactory) {
        // calculate single brick length by taking window length minus buffers and divide by num of bricks.
        float brickLength = (this.windowDimensions.x() -
                (NUM_OF_BRICKS_PER_LINE * SPACE_BETWEEN_BRICKS + 2 * BUFFER_FROM_WALL)) / NUM_OF_BRICKS_PER_LINE;
        Renderable brickImage = imageReader.readImage(BRICK_PATH, false);
        // generate lines of bricks.
        for (int i = 0; i < NUM_OF_BRICKS_LINES; i ++) {
            float locate = BUFFER_FROM_WALL;
            for (int j = 0; j < NUM_OF_BRICKS_PER_LINE; j++) {
                CollisionStrategy collisionStrategy = strategyFactory.getStrategy();
                GameObject brick = new Brick(Vector2.ZERO, new Vector2(brickLength, BRICK_HEIGHT),
                        brickImage, collisionStrategy, this.bricksCounter);
                brick.setTopLeftCorner(new Vector2(locate,
                        FIRST_BRICK_LINE_Y_OFFSET + i * SPACE_BETWEEN_LINES));
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                // count how many bricks were made, to be used for win condition.
                this.bricksCounter.increment();
                locate += SPACE_BETWEEN_BRICKS + brickLength;
            }
        }
    }

    /**
     * create invisible walls around the game to stop objects from exiting (except for the floor).
     */
    private void createWalls() {
        // create right wall
        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO, new Vector2(BORDER_WIDTH, this.windowDimensions.y()), null)
        );
        // create upper wall
        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO, new Vector2(this.windowDimensions.x(), BORDER_WIDTH), null)
        );
        // create left wall
        gameObjects().addGameObject(
                new GameObject(new Vector2(this.windowDimensions.x() - BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, this.windowDimensions.y()), null)
        );
    }

    private void createBackground(ImageReader imageReader) {
        GameObject background = new GameObject(Vector2.ZERO, this.windowController.getWindowDimensions(),
                imageReader.readImage(BG_PATH, false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);

    }

    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage(BALL_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND);
        GameObject ball = new Ball(Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS),
                ballImage, collisionSound);
        this.ball = ball;
        repositionBall(this.ball);
        gameObjects().addGameObject(ball);
    }

    /**
     * method resets ball to center of the screen with a random diagonal direction.
     * @param ball the ball being reset.
     */
    public void repositionBall(GameObject ball) {
        int [] velMultipliers = {-1, 1};
        Random rand = new Random();
        // generate random multiplier for X and Y directions.
        int velMultX = velMultipliers[rand.nextInt(velMultipliers.length)];
        int velMultY = velMultipliers[rand.nextInt(velMultipliers.length)];
        ball.setVelocity(new Vector2(BALL_SPEED * velMultX, BALL_SPEED * velMultY));
        ball.setCenter(this.windowDimensions.mult(0.5F));
    }

    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_PATH, true);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                inputListener, this.windowDimensions, BUFFER_FROM_WALL);
        // set paddle to center of the X axis.
        paddle.setCenter(new Vector2(this.windowDimensions.x() / 2, this.windowDimensions.y() - PADDLE_Y_OFFSET));
        this.gameObjects().addGameObject(paddle);
    }

    private void createGraphicLifeCounter(ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage(HEART_PATH, true);
        GameObject graphicLifeCounter = new GraphicLifeCounter(
                new Vector2(HEART_X_OFFSET, this.windowDimensions.y() - HEART_Y_OFFSET),
                new Vector2(HEART_RADIUS, HEART_RADIUS), this.livesCounter, heartImage, gameObjects(),
                this.livesCounter.value());
        gameObjects().addGameObject(graphicLifeCounter, Layer.BACKGROUND);
    }

    private void createNumericLifeCounter() {
        GameObject NumericLifeCounter = new NumericLifeCounter(this.livesCounter,
                new Vector2(NUMERIC_X_OFFSET, this.windowDimensions.y() - NUMERIC_Y_OFFSET),
                new Vector2(NUMERIC_COUNTER_DIMENSIONS, NUMERIC_COUNTER_DIMENSIONS),
                gameObjects());
        gameObjects().addGameObject(NumericLifeCounter, Layer.BACKGROUND);
    }

    /**
     * Entry point for game. contains:
     * 1. An instantiation call to BrickerGameManager constructor.
     * 2. A call to run() method of instance of BrickerGameManager.
     * @param args none
     */
    public static void main(String[] args) {
        BrickerGameManager manager = new BrickerGameManager(TITLE_SCREEN,
                new Vector2(MAIN_WINDOW_X, MAIN_WINDOW_Y));
        manager.run();
    }
}
