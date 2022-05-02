package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.positions.GameMap;
import game.*;
import game.actions.AttackAction;
import game.behaviours.AttackBehaviour;
import game.behaviours.Behaviour;
import game.behaviours.FollowBehaviour;
import game.behaviours.WanderBehaviour;
import game.reset.Resettable;

import java.util.HashMap;
import java.util.Map;

/**
 * A Mjølnir wielding teenage mutant ninja turtle
 */
public class Koopa extends Actor implements Resettable {
    private final Map<Integer, Behaviour> behaviours = new HashMap<>(); // priority, behaviour

    /**
     * A constructor for the Koopa class
     */
    public Koopa() {
        super("Koopa", 'K', 100);
        this.behaviours.put(10, new WanderBehaviour());
        this.addCapability(Status.HOSTILE_TO_PLAYER);
        this.addCapability(Status.CAN_PUNCH);
        registerInstance();
    }

    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        // it can be attacked only by the HOSTILE opponent, and this action will not attack the HOSTILE enemy back.
        if(otherActor.hasCapability(Status.HOSTILE_TO_ENEMY)) {
            actions.add(new AttackAction(this,direction));
        }
        if (otherActor instanceof Player) {
            this.behaviours.put(8, new FollowBehaviour(otherActor));
            this.behaviours.put(9, new AttackBehaviour(otherActor));
        }
        return actions;
    }


    /**
     * Figure out what to do next.
     * @see Actor#playTurn(ActionList, Action, GameMap, Display)
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (this.hasCapability(Status.RESETTABLE)) {
            map.removeActor(this); // removes this instance if reset operation is called
            this.removeCapability(Status.RESETTABLE);
        }
        else {
            if (!this.isConscious()) {
                this.addCapability(Status.DORMANT);
            }
            if (this.hasCapability(Status.DORMANT)) {
                this.setDisplayChar('D');
                return new DoNothingAction();
            }
            for (Behaviour Behaviour : behaviours.values()) {
                Action action = Behaviour.getAction(this, map);
                if (action != null)
                    return action;
            }
        }
        return new DoNothingAction();
    }

    /**
     * Adds the RESETTABLE capability to this instance
     */
    @Override
    public void resetInstance() {
        this.addCapability(Status.RESETTABLE);
    }
}