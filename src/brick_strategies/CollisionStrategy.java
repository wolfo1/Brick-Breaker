package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Class is in charge of collision logic with bricks. Each brick is given A CollisionStrategy which dictates
 * how the game progresses when the ball hits a brick.
 */
public interface CollisionStrategy {
    /**
     * To be called on brick collision.
     * @param thisObj the brick hit
     * @param otherObj the object hitting it
     * @param counter global brick counter
     */
    void onCollision(GameObject thisObj, GameObject otherObj, Counter counter);

    /**
     * All collision strategy objects should hold a reference to the global game object collection
     * and be able to return it.
     * @return reference to GameObjectCollection of the game.
     */
    GameObjectCollection getGameObjectCollection();

}
