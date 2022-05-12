package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.Status;
import game.actions.AttackAction;
import game.behaviours.AttackBehaviour;
import game.behaviours.Behaviour;
import game.behaviours.FollowBehaviour;
import game.reset.Resettable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PiranhaPlant extends Actor implements Resettable {
    //TODO spawn it in turn 2

    private final Map<Integer, Behaviour> behaviours = new HashMap<>(); // priority, behaviour

    public PiranhaPlant() {
        super("PiranhaPlant", 'Y', 150);
        this.addCapability(Status.HOSTILE_TO_PLAYER);
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
            this.behaviours.put(9, new AttackBehaviour(otherActor));
        }
        return actions;
    }
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        //resetting will increase alive/existing Piranha Plants hit points by an additional 50 hit points and heal to the maximum
        if (this.hasCapability(Status.RESETTABLE)) {
            this.resetMaxHp(this.getMaxHp() + 50);
        } else {
            for (game.behaviours.Behaviour Behaviour : behaviours.values()) {
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

    @Override
    protected IntrinsicWeapon getIntrinsicWeapon(){
        return new IntrinsicWeapon(90, "chomps");
    }
}
