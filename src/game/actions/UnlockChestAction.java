package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Goomba;
import game.items.Arrow;
import game.items.GalaxySword;
import game.items.Gun;
import game.positions.Dirt;

import java.util.Random;

/**
 * Special Action for unlocking Treasure Chests
 */
public class UnlockChestAction extends Action {

    /**
     * Random Number Generator
     */
    private Random random;

    /**
     * The location of the Treasure Chest
     */
    private Location location;

    /**
     * The direction of the Treasure Chest
     */
    private String direction;

    /**
     * Constructor
     */
    public UnlockChestAction(Location location, String direction){
        this.random = new Random();
        this.location = location;
        this.direction = direction;
    }

    @Override
    public String execute(Actor actor, GameMap map){
        String ret = "";
        if (random.nextDouble() <= 0.25){
            location.addActor(new Goomba());
            ret += "It was a trap box! A Goomba appears.";
        }
        else{
            actor.addItemToInventory(new Arrow(2));
            ret += "Treasure Chest dropped 2 arrows\n";
            double rng = random.nextDouble();
            if (rng <= 0.95){
                actor.addItemToInventory(new Gun());
                ret += "Treasure Chest dropped a gun, what?";
            }
            else if (rng <= 0.10){
                actor.addItemToInventory(new GalaxySword());
                ret += "Treasure Chest dropped a purple sword";
            }
            //TODO: drop enchantment books else statement
        }
        location.setGround(new Dirt());
        return ret;
    }

    @Override
    public String menuDescription(Actor actor){
        return actor + " unlocks Treasure Chest at " + direction;
    }
}