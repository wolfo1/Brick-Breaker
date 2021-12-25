package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Status Definer of type ChangeTimeStatusDefiner, which drops a object that can only interact with the paddle.
 * if the paddle "collects" the object, the Time Scale pace of the game changes to given pace in the c'tor.
 */
public class ChangeTimeStatusDefiner extends StatusDefiner{
    private final float timeMultiplier;
    private final WindowController windowController;

    /**
     * Construct a new GameObject of type Status Definer, ChangeTimeStatusDefiner.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param gameObjects collection of all the objects in the game.
     * @param windowController of type WindowController, controls the time scale of the game.
     * @param timeMultiplier the change to time scale applied if paddle collects the object.
     */
    public ChangeTimeStatusDefiner(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObjectCollection gameObjects,
                                   WindowController windowController, float timeMultiplier) {
        super(topLeftCorner, dimensions, renderable, gameObjects);
        this.timeMultiplier = timeMultiplier;
        this.windowController = windowController;
    }

    /**
     * default behaviour - if the status hits the paddle, remove it from the game.
     * in ChangeTimeStatus, also change the time scale in the game to the muiltiplier given in constructor.
     * @param other     the paddle
     * @param collision information on the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.windowController.setTimeScale(this.timeMultiplier);
    }
}
