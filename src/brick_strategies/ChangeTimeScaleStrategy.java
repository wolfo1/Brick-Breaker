package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.ChangeTimeStatusDefiner;
import src.gameobjects.StatusDefiner;
import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator with the following strategy:
 * A brick with ChangeTimeScaleStrategy will drop a StatusDefiner, which when collected with the Paddle will
 * either quicken the game pace or make it slower (thus making the game more difficult or easier).
 */
public class ChangeTimeScaleStrategy extends RemoveBrickStrategyDecorator{
    private static final String GREEN_CLOCK_IMAGE_PATH = "assets/slow.png";
    private static final String RED_CLOCK_IMAGE_PATH = "assets/quicken.png";
    private static final int CLOCK_HEIGHT = 30;
    private static final int CLOCK_WIDTH = 55;
    private static final float SLOW_TIME = 0.9f;
    private static final float FAST_TIME = 1.1f;
    private static final float DEFAULT_TIME = 1.0f;

    private final WindowController windowController;
    private final Renderable greenClockImage;
    private final Renderable redClockImage;

    /**
     * c'tor
     * @param toBeDecorated type CollisiionStrategy, the strategy to wrap.
     * @param imageReader type ImageReader, used to scan the image of the StatusDefiners.
     * @param windowController type WindowController, used to set TimeScale of the game.
     */
    public ChangeTimeScaleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                                   WindowController windowController) {
        super(toBeDecorated);
        this.windowController = windowController;
        // scan the images of the StatusDefiners.
        this.greenClockImage = imageReader.readImage(GREEN_CLOCK_IMAGE_PATH, true);
        this.redClockImage = imageReader.readImage(RED_CLOCK_IMAGE_PATH, true);
    }

    /**
     * drop a StatusDefiner of type ChangeTimeStatusDefiner. if the game is in default scale, drop a random
     * StatusDefiner. If the game is slower, drop a quicken one, and if the game is faster drop a slower one.
     * @param thisObj the brick hit
     * @param otherObj the object hitting it
     * @param counter used to count how many bricks were destroyed
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        // arrays of both possibilities to choose from at random
        float[] timeMultipliers = {SLOW_TIME, FAST_TIME};
        Renderable[] clockImages = {this.greenClockImage, this.redClockImage};
        double currTime = windowController.getTimeScale();
        int chosenType;
        // if the time hasn't changed already, choose at random
        if (currTime == DEFAULT_TIME) {
            Random rand = new Random();
            chosenType = rand.nextInt(2);
        }
        // if time is fast, choose slow time, else choose fast time
        else if (currTime == FAST_TIME)
            chosenType = 0;
        else
            chosenType = 1;
        // create Status Definer object with the chosen image and time, place it into the game.
        StatusDefiner statusDefiner = new ChangeTimeStatusDefiner(Vector2.ZERO, new Vector2(CLOCK_WIDTH, CLOCK_HEIGHT),
                clockImages[chosenType], getGameObjectCollection(), this.windowController, timeMultipliers[chosenType]);
        statusDefiner.setCenter(thisObj.getCenter());
        getGameObjectCollection().addGameObject(statusDefiner);
    }
}
