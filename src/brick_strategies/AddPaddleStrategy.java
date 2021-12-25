package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.MockPaddle;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator with the following strategy:
 * When a brick with AddPaddleStrategy get hit, remove brick from the game and add another Paddle in the middle
 * of the screen, which lasts up to several hits before disappearing.
 * there can only be one extra paddle in any given instance.
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator{
    private static final int MOCK_PADDLE_HEIGHT = 20;
    private static final int MOCK_PADDLE_WIDTH = 100;
    private static final int COLLISIONS_TO_DISAPPEAR = 4;
    private static final int BUFFER_FROM_WALL = 11;
    private static final String MOCK_PADDLE_PATH = "assets/paddle.png";

    private final ImageRenderable mockPaddleImage;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;

    /**
     * c'tor
     * @param toBeDecorated type CollisiionStrategy, the strategy to wrap.
     * @param imageReader type ImageReader, used to scan the image of the MockPaddle.
     * @param inputListener type UserInputListener, used to get the info from user pressing keys and move mock paddle.
     * @param windowDimensions type Vector2, to calculate Mock Paddle position on screen
     */
    public AddPaddleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                             UserInputListener inputListener, Vector2 windowDimensions) {
        super(toBeDecorated);
        this.mockPaddleImage = imageReader.readImage(MOCK_PADDLE_PATH, false);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;

    }

    /**
     * when a Brick with AddPaddleStrategy collides, remove brick from game and add another Mock Paddle
     * in the middle of the screen (only if there isn't one already).
     * @param thisObj the brick that was hit.
     * @param otherObj the object hitting it.
     * @param counter used to count how many bricks were hit to indicate end of game.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        // is there isn't a Mock Paddle in the game right now, create one and add it to the game.
        if (!MockPaddle.isInstantiated) {
            // location, brick X axis, middle of the screen Y axis
            Vector2 location = new Vector2(otherObj.getCenter().x(), this.windowDimensions.mult(0.5f).y());
            Vector2 dimensions = new Vector2(MOCK_PADDLE_WIDTH, MOCK_PADDLE_HEIGHT);
            GameObject mockPaddle = new MockPaddle(location, dimensions, this.mockPaddleImage, this.inputListener,
                    this.windowDimensions, getGameObjectCollection(), BUFFER_FROM_WALL, COLLISIONS_TO_DISAPPEAR);
            getGameObjectCollection().addGameObject(mockPaddle);
        }
    }
}
