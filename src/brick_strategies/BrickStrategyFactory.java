package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import src.BrickerGameManager;

import java.util.Random;

public class BrickStrategyFactory {
    private static final int NUM_OF_SPECIAL_STRATEGIES = 4;

    // all the different classes that controls the game, and will allow strategies to affect the game
    private final BrickerGameManager gameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener userInputListener;
    private final WindowController windowController;
    private final Vector2 windowDimensions;
    // the default strategy to wrap (will be RemoveBrickStrategy)
    private final CollisionStrategy defaultWrappedStrategy;
    // will be used to keep track of chains of strategies (double or triple decorators).
    private int makingChainCounter;

    /**
     * c'tor of a BrickStrategyFactory
     * @param gameObjectCollection includes all the objects and allows to add or remove objects
     * @param gameManager of type BrickerGameManager, allows to change camera settings
     * @param imageReader type ImageReader for scanning images files for new objects
     * @param soundReader type SoundReader, for scanning sound files for new effects.
     * @param inputListener type UserInputListener, for scanning the user input and using the info.
     * @param windowController type WindowController, allow to change dimensions of the game.
     * @param windowDimensions type Vector2, dimensions of the main window of the game.
     */
    public BrickStrategyFactory(GameObjectCollection gameObjectCollection, BrickerGameManager gameManager,
                                ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                                WindowController windowController, Vector2 windowDimensions) {
        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.userInputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
        this.defaultWrappedStrategy = new RemoveBrickStrategy(gameObjectCollection);
    }

    /**
     * method randomly selects between 5 strategies and returns one CollisionStrategy object which is a
     * RemoveBrickStrategy decorated by one of the decorator strategies,
     * or decorated by two randomly selected strategies, or decorated by one of the decorator strategies and
     * a pair of additional two decorator strategies.
     * @return CollisionStrategy, randomly selected strategy.
     */
    public CollisionStrategy getStrategy() {
        // reset making chain counter.
        this.makingChainCounter = 0;
        Random rand = new Random();
        // choice between the 4 special strategies, default brick and Double strategy.
        int choice = rand.nextInt(NUM_OF_SPECIAL_STRATEGIES + 2);
        switch (choice) {
            case (5):
                return this.defaultWrappedStrategy;
            default:
                return getSpecialStrategy(choice);
        }
    }

    /**
     * function gets a special strategy and wraps the default remove brick strategy in it.
     * @param choice the number of the strategy, see getSpecialStrategy(choice, strategyToWrap).
     * @return collision strategy that wraps removebrick.
     */
    private CollisionStrategy getSpecialStrategy(int choice) {
        return getSpecialStrategy(choice, this.defaultWrappedStrategy);
    }

    /**
     * get a special strategy, wrapping StrategyToWrap.
     * @param choice the choice (should be 0 to NUM_OF_SPECIAL_STRATEGIES).
     * @param strategyToWrap the strategy you want to decorate.
     * @return CollisionStrategy a strategy wrapping the strategy given to method
     */
    private CollisionStrategy getSpecialStrategy(int choice, CollisionStrategy strategyToWrap) {
        switch (choice) {
            case (0):
                return new ChangeTimeScaleStrategy(strategyToWrap, this.imageReader, this.windowController);
            case (1):
                return new PuckStrategy(strategyToWrap, this.imageReader, this.soundReader);
            case (2):
                return new AddPaddleStrategy(strategyToWrap, this.imageReader, this.userInputListener,
                        this.windowDimensions);
            case (3):
                return new ChangeCameraStrategy(strategyToWrap, this.windowController, this.gameManager);
            case (4):
                return createDoubleStrategy();
            default:
                return null;
        }
    }

    /**
     * Create a strategy chain (A wraps B that wraps C that wraps default RemoveBrick),
     * with length of 2 or 3.
     * @return the first strategy in the chain.
     */
    private CollisionStrategy createDoubleStrategy() {
        Random rand = new Random();
        if (this.makingChainCounter < 1) {
            this.makingChainCounter++;
            // create a new strategy. Can be a recursive call to createDoubleStrategy().
            CollisionStrategy strategyToWrap = getSpecialStrategy(rand.nextInt(NUM_OF_SPECIAL_STRATEGIES + 1));
            // create a new strategy (can't be double), that wraps the one we got before.
            return getSpecialStrategy(rand.nextInt(NUM_OF_SPECIAL_STRATEGIES), strategyToWrap);
        }
        else {
            // if createDouble was called twice recursively, return 2 strategies that will be decorated by a third.
            CollisionStrategy strategyToWrap = getSpecialStrategy(rand.nextInt(NUM_OF_SPECIAL_STRATEGIES));
            return getSpecialStrategy(rand.nextInt(NUM_OF_SPECIAL_STRATEGIES), strategyToWrap);
        }
    }
}