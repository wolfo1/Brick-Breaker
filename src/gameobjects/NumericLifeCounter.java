package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.awt.*;

/**
 * on screen graphic life counter which consists of widgets in the number of lives.
 * will update the number of widgets in the progress of the game.
 */
public class NumericLifeCounter extends GameObject {
    private static final String PHRASE = " lives left";

    // global counter, holds how many lives the player has
    private final Counter livesCounter;
    // holds the number which is presented on the screen right now
    private int currLives;
    private final TextRenderable textRenderable;

    /**
     * Construct a new GameObject of type Paddle.
     * @param livesCounter A real-time counter of how many lives player has left.
     * @param topLeftCorner a Vector represents the location of the numeric counter on screen.
     * @param dimensions the dimensions of the text
     * @param gameObjectCollection used to add / remove widgets from the screen
     **/
    public NumericLifeCounter (Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                               GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.currLives = livesCounter.value();
        // create a text box, bold letters in white text, add it to the screen.
        this.textRenderable = new TextRenderable(
                this.livesCounter.value() + PHRASE,
                Font.SANS_SERIF, false, true);
        this.textRenderable.setColor(Color.WHITE);
        GameObject counterText = new GameObject(topLeftCorner, dimensions, this.textRenderable);
        gameObjectCollection.addGameObject(counterText, Layer.BACKGROUND);
    }

    /**
     * Should be called once per frame, checks if livesCounter changed, and if so, updates the text.
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     **/
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // if livesCounter is different than currLives, update text and currLives.
        if (this.livesCounter.value() != this.currLives) {
            this.textRenderable.setString(this.livesCounter.value() + PHRASE);
            this.currLives = this.livesCounter.value();
        }
    }
}
