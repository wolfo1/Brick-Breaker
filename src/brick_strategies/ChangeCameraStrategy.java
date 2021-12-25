package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;
import src.gameobjects.Puck;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator with the following strategy:
 * When a brick with ChangeCameraStrategy gets hit, remove brick from game and make the camera focous on the
 * Ball until it hits something several times, then resets game camera back to normal.
 */
public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator {
    // how many collisions until camera resets to normal
    private static final int NUM_OF_COLLISIONS = 4;

    private final BrickerGameManager gameManager;
    private final WindowController windowController;
    private BallCollisionCountdownAgent countdownAgent;

    /**
     * c'tor
     * @param toBeDecorated type CollisionStrategy, strategy to wrap.
     * @param windowController used to get the dimensions of the screen
     * @param gameManager used to change the camera settings
     */
    public ChangeCameraStrategy(CollisionStrategy toBeDecorated, WindowController windowController,
                                BrickerGameManager gameManager) {
        super(toBeDecorated);
        this.gameManager = gameManager;
        this.windowController = windowController;
    }

    /**
     * if the camera isn't already on the ball, change camera to focus on the ball.
     * @param thisObj The brick that was hit.
     * @param otherObj The object hitting it.
     * @param counter Counts how many brick were hit
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if (this.gameManager.getCamera() == null) { // only change camera if the camera is at default.
            if (!(otherObj instanceof Puck)) { // only change camera if the Ball hit the brick, not a puck.
                this.gameManager.setCamera(
                        new Camera(
                                otherObj,        //object to follow
                                Vector2.ZERO,    //follow the center of the object
                                windowController.getWindowDimensions().mult(1.2f),  //widen the frame a bit
                                windowController.getWindowDimensions())  //share the window dimensions
                );
                // create new object of type BallCollisionCountdownAgent. it will call turnOffCameraChange
                // once the ball was hit NUM_OF_COLLISIONS times.
                this.countdownAgent = new BallCollisionCountdownAgent((Ball) otherObj, this,
                        NUM_OF_COLLISIONS);
                getGameObjectCollection().addGameObject(this.countdownAgent);
            }
        }
    }

    /**
     * resets the camera back to default position.
     */
    public void turnOffCameraChange() {
        this.gameManager.setCamera(null);
        getGameObjectCollection().removeGameObject(this.countdownAgent);
    }
}
