package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

/**
 * One of the main game objects. Repels the ball against the bricks.
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 350;

    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final int minDistanceFromEdge;

    /**
     * Construct a new GameObject of type Paddle.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     * @param dimensions Vector which contains width and height of Paddle.
     * @param renderable The renderable representing the Paddle.
     * @param inputListener input from user, paddle moves accordingly
     * @param windowDimensions dimensions of the main window
     * @param minDistanceFromEdge int, how close the paddle can get to the edges of the screen
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistanceFromEdge = minDistanceFromEdge;
    }

    /**
     * Should be called once per frame, checks if the user moved the paddle left or right.
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // default direction is none. if left is pressed, add LEFT direction. if right, add RIGHT direction.
        Vector2 movementDir = Vector2.ZERO;
        if(this.inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(this.inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        // apply velocity in given direction, multiplied by movement speed.
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
        // checks if the paddle is not moving too close to the screen's edges. if so, moves it back.
        Vector2 location = getTopLeftCorner();
        if (location.x() < this.minDistanceFromEdge) {
            transform().setTopLeftCornerX(this.minDistanceFromEdge);
        }
        if (location.x() > this.windowDimensions.x() - this.minDistanceFromEdge - getDimensions().x()) {
            transform().setTopLeftCornerX(this.windowDimensions.x() - this.minDistanceFromEdge - getDimensions().x());
        }
    }
}
