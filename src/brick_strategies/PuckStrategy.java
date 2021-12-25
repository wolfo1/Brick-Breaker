package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;
import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator with the following strategy:
 * When the brick is hit, remove the brick and spawn several small mock-balls, which will help the user clear
 * the bricks.
 */
public class PuckStrategy extends RemoveBrickStrategyDecorator{
    // paths to assets
    private static final String PUCK_PATH = "assets/mockBall.png";
    private static final String COLLISION_SOUND = "assets/Bubble5_4.wav";
    // number of pucks to spawn and their speed (10% faster than the ball).
    private static final int NUM_OF_PUCKS = 1;
    private static final int PUCK_SPEED = 250;

    private final ImageRenderable puckImage;
    private final Sound collisionSound;

    /**
     * c'tor
     * @param toBeDecorated strategy to wrap
     * @param imageReader type ImageReader, used to scan image files.
     * @param soundReader type SoundReader, used to scan sound files.
     */
    public PuckStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, SoundReader soundReader) {
        super(toBeDecorated);
        this.puckImage = imageReader.readImage(PUCK_PATH, true);
        this.collisionSound = soundReader.readSound(COLLISION_SOUND);
    }

    /**
     * when the brick is hit, remove it and spawn NUM_OF_PUCKS pucks instead of it, to fly in random directions.
     * @param thisObj the brick hit
     * @param otherObj the object hitting it
     * @param counter global brick counter
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        // puck size should be a third of the size of the brick
        int puck_radius = (int) (thisObj.getDimensions().x() / 3);
        // create NUM_OF_PUCKS pucks instead of the brick
        for (int i = 0; i < NUM_OF_PUCKS; i++) {
            GameObject puck = new Puck(Vector2.ZERO, new Vector2(puck_radius, puck_radius),
                    this.puckImage, this.collisionSound);
            positionPuck(puck, thisObj.getCenter());
            getGameObjectCollection().addGameObject(puck);
        }
    }

    /**
     * gives the puck random direction and places it at the center of the brick
     */
    private void positionPuck(GameObject puck, Vector2 center) {
        int [] velMultipliers = {-1, 1};
        Random rand = new Random();
        // generate random multiplier for X and Y directions.
        int velMultX = velMultipliers[rand.nextInt(velMultipliers.length)];
        int velMultY = velMultipliers[rand.nextInt(velMultipliers.length)];
        puck.setVelocity(new Vector2(PUCK_SPEED * velMultX, PUCK_SPEED * velMultY));
        puck.setCenter(center);
    }
}
