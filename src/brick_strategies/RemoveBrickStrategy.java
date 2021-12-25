package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class RemoveBrickStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjects;

    /**
     * default c'tor
     * @param gameObjectCollection - to allow CollisionStrategy to add / remove objects to the game.
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjects = gameObjectCollection;
    }

    /**
     * called when a ball hits a brick. So far, method will remove the brick from the game and lower the
     * brick counter.
     * @param thisObj the object that was being hit.
     * @param otherObj the object hitting
     * @param counter counter of how many bricks there are.
     */
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        // checks if onCollision was called more than 1 time on the same object
        if(gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
            counter.decrement();
        }
    }

    /**
     * Method returns the GameObjectCollection which holds all objects in the game.
     * @return GameObject Collection.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return this.gameObjects;
    }
}
