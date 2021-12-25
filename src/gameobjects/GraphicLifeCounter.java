package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * on screen graphic life counter which consists of widgets in the number of lives.
 * will update the number of widgets in the progress of the game.
 */
public class GraphicLifeCounter extends GameObject {
    private static final int SPACE_BETWEEN_WIDGETS = 7;

    private final GameObject[] widgets;
    private final Counter livesCounter;
    private int currLives;
    private final GameObjectCollection gameObjectCollection;

    /**
     * Construct a new GameObject of type Paddle.
     *  @param widgetTopLeftCorner Position of the object, in window coordinates (pixels).
     * @param widgetDimensions Vector which contains width and height of a single widget.
     * @param widgetRenderable The renderable representing the widget.
     * @param livesCounter A real-time counter of how many lives there is
     * @param gameObjectsCollection used to add / remove widgets from the screen
     * @param numOfLives starting number of lives.
     **/
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable, GameObjectCollection gameObjectsCollection,
                              int numOfLives) {
        // create an invisble non-interactable object that will control the logic of the counter.
        super(widgetTopLeftCorner, widgetDimensions, null);
        this.widgets = new GameObject[numOfLives];
        this.livesCounter = livesCounter;
        this.currLives = numOfLives;
        this.gameObjectCollection = gameObjectsCollection;
        // starting location of the first widget
        Vector2 location = Vector2.ZERO;
        // create widgets and add in array, each time offsets location.X by dimensions and SPACE_BETWEEN_WIDGETS.
        for (int i = 0; i < numOfLives; i++) {
            GameObject widget = new GameObject(Vector2.ZERO, widgetDimensions, widgetRenderable);
            this.widgets[i] = widget;
            widget.setCenter(widgetTopLeftCorner.add(location));
            location = new Vector2(location.x() + widgetDimensions.x() + SPACE_BETWEEN_WIDGETS, 0);
            gameObjectsCollection.addGameObject(widget, Layer.BACKGROUND);
        }
    }

    /**
     * Should be called once per frame, checks if livesCounter changed, and if so, removes widgets accordingly.
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     **/
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.livesCounter.value() < this.currLives) {
            this.currLives --;
            this.gameObjectCollection.removeGameObject(this.widgets[currLives], Layer.BACKGROUND);
        }
    }
}
