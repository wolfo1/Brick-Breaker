package src.gameobjects;

import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * class represents game object of a type brick, a static GameObject, which interact with the ball when
 * collided by a given collision strategy. Bricks are the target of the player, hitting all bricks wins the game.
 */
public class Brick extends GameObject {
    private final CollisionStrategy collisionStrategy;
    private final Counter counter;
    // used to make sure a brick doesn't call onCollision twice (2 hits in the same frame).
    private int timesHit = 0;

    /**
     * Construct a new GameObject of type Brick.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     * @param dimensions Vector which contains width and height of Paddle.
     * @param renderable The renderable representing the Paddle.
     * @param collisionStrategy defines the logic which the brick will behave if collided with the ball.
     * @param counter counts how many bricks is in the game (0 is win condition).
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.counter = counter;
    }

    /**
     * Called on the first frame of a collision. calls the collision strategy.
     * @param other – The GameObject with which a collision occurred.
     * @param collision – Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // make sure each brick is only calling Collision Strategy only once
        if (this.timesHit == 0) {
            this.timesHit += 1;
            this.collisionStrategy.onCollision(this, other, this.counter);
        }
    }
}
