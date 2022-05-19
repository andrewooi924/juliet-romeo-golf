package game.injectors;

import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.GameMap;
import game.GameUtilities;
import game.ImprovedNoise;
import game.map.Maps;
import game.positions.*;
import game.positions.Tree.SproutTree;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class MapInjector {

    private final HashMap<Maps, GameMap> maps;
    private final HashMap<GameMap, String> mapNames;

    public MapInjector() {
        maps = new HashMap<>();
        mapNames = new HashMap<>();
        createProceduralMap();
    }

    public HashMap<Maps, GameMap> addingMaps() {
        FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new SproutTree(), new WarpPipe(), new HealthFountain(), new PowerFountain(), new CraftingTable(), new TreasureChest());
        FancyGroundFactory lavaFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new SproutTree(), new Lava(), new WarpPipe());
        FancyGroundFactory proceduralFactory= new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new SproutTree(), new WarpPipe(), new HealthFountain(), new PowerFountain(), new CraftingTable(), new TreasureChest(), new Lava());
        GameMap lavaMap = null;
        GameMap gameMap = null;
        GameMap proceduralMap = null;
        try {
            lavaMap = new GameMap(lavaFactory, "src/game/map/Lava Zone");
            gameMap = new GameMap(groundFactory, "src/game/map/Basic Map");
            proceduralMap = new GameMap(proceduralFactory, "src/game/map/Procedural Map");
        } catch (IOException e) {
            e.printStackTrace();
        }
        maps.put(Maps.MAP_LAVA, lavaMap);
        maps.put(Maps.MAP_BASIC, gameMap);
        maps.put(Maps.MAP_PROCEDURAL, proceduralMap);
        return maps;
    }

    private void createProceduralMap() {
        final double frequency = 1;
        final double amplitude = 1;
        final double treeFrequency = 0.05;
        final double treeAmplitude= 5;
        final long lava_seed = 3523;
        final long tree_seed = 2556437;
        final long chest_seed = 5643;
        final long power_seed = 435532;
        final long heal_seed = 325456;
        final long wall_seed = 6549368;
        try {
            FileWriter myWriter = new FileWriter("src/game/map/Procedural Map", false);
            for (int y = 0; y < 30; y++) {
                for (int x = 0; x < 100; x++) {
                    double random = Math.abs(ImprovedNoise.noise((double)(x)/100 * treeFrequency, (double)(y)/30*treeFrequency, tree_seed * 10) * treeAmplitude);
                    if ( random <= 0.025) {
                        myWriter.write("+");
                        continue;
                    }
                    random = Math.abs(ImprovedNoise.noise((double)(x)/100 * frequency, (double)(y)/30*frequency, wall_seed * 10) * amplitude);
                    if (random <= 0.025) {
                        myWriter.write("#");
                        continue;
                    }
                    random = Math.abs(ImprovedNoise.noise((double)(x)/100 * frequency, (double)(y)/30*frequency, lava_seed * 10) * amplitude);
                    if (random <= 0.006) {
                        myWriter.write("L");
                        continue;
                    }
                    random = Math.abs(ImprovedNoise.noise((double)(x)/100 * frequency, (double)(y)/30*frequency, chest_seed * 10) * amplitude);
                    if (random <= 0.001) {
                        myWriter.write("X");
                        continue;
                    }
                    random = Math.abs(ImprovedNoise.noise((double)(x)/100 * frequency, (double)(y)/30*frequency, heal_seed * 10) * amplitude);
                    if (random <= 0.05) {
                        myWriter.write("H");
                        continue;
                    }
                    random = Math.abs(ImprovedNoise.noise((double)(x)/100 * frequency, (double)(y)/30*frequency, power_seed * 10) * amplitude);
                    if (random <= 0.05) {
                        myWriter.write("A");
                        continue;
                    }
                    myWriter.write(".");
                }
                myWriter.write("\n");
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<GameMap, String> addingMapNames() {
        GameMap lavaMap = maps.get(Maps.MAP_LAVA);
        GameMap gameMap = maps.get(Maps.MAP_BASIC);
        GameMap proceduralMap = maps.get(Maps.MAP_PROCEDURAL);
        mapNames.put(gameMap, "Basic Map");
        mapNames.put(lavaMap, "Lava Zone");
        mapNames.put(proceduralMap, "Procedural Map");
        return mapNames;
    }


}
