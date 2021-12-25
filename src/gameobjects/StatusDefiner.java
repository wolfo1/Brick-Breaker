package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A game object which is created by hitting a certain brick. Goes down in a straight line. If collides by the
 * paddle, does an effect on the game. can only collide with the paddle.
 */
public class StatusDefiner extends GameObject {
    // the speed at which the status definer is falling down
    private static final int STATUS_DEFINER_SPEED = 100;

    private final GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject of type Status Definer.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param gameObjects collection of all game objects in the game, allows status to delete itself after
     *                   colliding with paddle.
     */
    public StatusDefiner(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                         GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.setVelocity(Vector2.DOWN.mult(STATUS_DEFINER_SPEED));
        this.gameObjects = gameObjects;
    }

    /**
     * default behaviour - if the status hits the paddle, remove it from the game.
     * @param other the paddle
     * @param collision information on the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.gameObjects.removeGameObject(this);
    }

    /**
     * a StatusDefiner should only collide with the paddle.
     * @param other the object colliding with the status definer
     * @return true if the object is a paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        // should only collide with the paddle.
        super.shouldCollideWith(other);
        return (other instanceof Paddle);
    }
}
