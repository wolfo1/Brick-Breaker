package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A mockPaddle is an extra paddle that can be created using AddPaddle Brick Strategy. The object behaves like
 * the paddle, but will disappear once it was hit several times.
 * only one can be present at any given moment.
 */
public class MockPaddle extends Paddle{
    // will be used to indicate if there is already an instance of Mock paddle on screen.
    public static boolean isInstantiated = false;

    private int collisionCounter;
    private final GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject of type MockPaddle.
     *  @param topLeftCorner       Position of the object, in window coordinates (pixels).
     * @param dimensions          Vector which contains width and height of Paddle.
     * @param renderable          The renderable representing the Paddle.
     * @param inputListener       input from user, paddle moves accordingly
     * @param windowDimensions    dimensions of the main window
     * @param minDistanceFromEdge The distance from the wall which Paddle will stop moving further.
     * @param numCollisionsToDisappear how many collisions until the MockPaddle disappears from the game.
     */
    public MockPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener, Vector2 windowDimensions,
                      GameObjectCollection gameObjects, int minDistanceFromEdge, int numCollisionsToDisappear) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        MockPaddle.isInstantiated = true;
        this.gameObjects = gameObjects;
        this.collisionCounter = numCollisionsToDisappear;
    }

    /**
     * When extra paddle collides with something, behave exactly like normal paddle, and decrement count
     * of how many times mock paddle was hit. if the counter hits 0, removes mock paddle from the game.
     * @param other the object hitting the paddle
     * @param collision info on the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.collisionCounter--;
        if (this.collisionCounter == 0) {
            this.gameObjects.removeGameObject(this);
            MockPaddle.isInstantiated = false;
        }
    }
}
