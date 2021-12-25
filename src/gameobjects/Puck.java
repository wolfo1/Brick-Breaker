package src.gameobjects;

import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Puck extends Ball {

    /**
     * Construct a new GameObject instance of type Puck.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the ball.
     * @param collisionSound the sound to be played when the ball collides with something.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
    }


}
