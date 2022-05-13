package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.injectors.LocationInjector;
import game.injectors.MapInjector;
import game.map.Maps;

import java.util.HashMap;

public class TeleportAction extends Action {

    private final HashMap<GameMap, String> mapNames;
    private final Location newLocation;

    public TeleportAction(Location newLocation) {
        mapNames = MapInjector.getMapNames(); // getting the names of the maps
        this.newLocation = newLocation;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        if (newLocation.containsAnActor())
            map.removeActor(newLocation.getActor()); // removes the piranha plant if it is there
        map.moveActor(actor, newLocation);
        return "Mario teleports to " + mapNames.get(newLocation.map()) + " at x:" + newLocation.x() + " y:" + newLocation.y();
    }

    @Override
    public String menuDescription(Actor actor) {
        return "Teleport to " + mapNames.get(newLocation.map());
    }
}
