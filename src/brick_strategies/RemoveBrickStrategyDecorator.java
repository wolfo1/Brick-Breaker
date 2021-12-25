package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public abstract class RemoveBrickStrategyDecorator implements CollisionStrategy{

    private final CollisionStrategy decoratedStrategy;

    /**
     * c'tor of abstract class. takes a CollisionStrategy and decorates it
     * @param toBeDecorated the strategy to wrap.
     */
    public RemoveBrickStrategyDecorator(CollisionStrategy toBeDecorated)
    {
        this.decoratedStrategy = toBeDecorated;
    }

    /**
     * returns object collection for the bricker game.
     * @return game object collection
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return this.decoratedStrategy.getGameObjectCollection();
    }

    /**
     * when the brick associated with the strategy get's hit, call the decorated strategy
     * @param thisObj the brick hit
     * @param otherObj the object hitting it
     * @param counter global brick counter
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        this.decoratedStrategy.onCollision(thisObj, otherObj, counter);
    }
}
