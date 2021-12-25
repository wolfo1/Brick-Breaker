package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Ball is the main game object. It's positioned in game window as part of game initialization and given
 * initial velocity. On collision, it's velocity is updated to be reflected about the normal vector of the
 * surface it collides with.
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private final Counter ballHitsCounter;

    /**
     * Construct a new GameObject instance of type Ball.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the ball.
     * @param collisionSound the sound to be played when the ball collides with something.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.ballHitsCounter = new Counter();
    }

    /**
     * Called on the first frame of a collision. Plays a sound and flips the velocity.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // the ball doesn't hit other ball types (like pucks).
        if (!(other instanceof Ball)) {
            // if the ball hit's something, it goes the flipped direction and plays a sound.
            setVelocity(getVelocity().flipped(collision.getNormal()));
            this.collisionSound.play();
            this.ballHitsCounter.increment();
        }
    }

    /**
     * returns the value of how many times the ball was hit. can be used to calculate duration of certain effects.
     * @return int, how many times the ball was hit so far.
     */
    public int getCollisionCounter() {
        return this.ballHitsCounter.value();
    }
}
