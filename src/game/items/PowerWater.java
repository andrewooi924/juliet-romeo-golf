package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.Status;
import game.actors.IntrinsicFighter;
import game.actors.Player;

public class PowerWater extends Item implements Consumable {
    final int BASEATTACK_BOOST = 15;

    /***
     * Constructor.
     */
    public PowerWater() {
        super("Power Water", 'A', false);
    }

    @Override
    public Status effect() {
        return null;
    }

    @Override
    public String consume(Actor actor, GameMap map) {
        if (actor.hasCapability(Status.CAN_INTRINSIC_ATTACK)) {
            IntrinsicFighter fighter = (IntrinsicFighter) actor;
            fighter.setIntrinsicDamage(fighter.getIntrinsicDamage() + BASEATTACK_BOOST);
        }
        return "";
    }
}