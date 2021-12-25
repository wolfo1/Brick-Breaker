package src.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

/**
 * An object of this class is instantiated on collision of ball with a brick with a change camera strategy.
 * It checks ball's collision counter every frame, and once the it finds the ball has collided countDownValue
 * times since instantiation, it calls the strategy to reset the camera to normal.
 */
public class BallCollisionCountdownAgent extends GameObject {
    private final int targetCounter;
    private final Ball ball;
    private final ChangeCameraStrategy owner;

    /**
     * c'tor
     * @param ball - Ball object whose collisions are to be counted.
     * @param owner - Object asking for countdown notification.
     * @param countDownValue - Number of ball collisions. Notify caller object that the ball collided countDownValue times since instantiation.
     */
    public BallCollisionCountdownAgent(Ball ball, ChangeCameraStrategy owner, int countDownValue) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        // sets a target, when the ball hit counter reaches target, reset camera.
        this.owner = owner;
        this.ball = ball;
        this.targetCounter = this.ball.getCollisionCounter() + countDownValue;
    }

    /**
     * checks each frame if the ball hit countDownValue times. if so, calls to strategy turn off camera change method.
     * @param deltaTime time of the frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.ball.getCollisionCounter() > this.targetCounter) {
            this.owner.turnOffCameraChange();
        }
    }
}
