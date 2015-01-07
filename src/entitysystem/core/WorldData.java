package entitysystem.core;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import entitysystem.util.EntityToString;
import tiled.core.Map;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Litude on 7.1.2015.
 */
public class WorldData {

    World world;
    HashMap<String,Map> allMaps;

    public WorldData(World world, HashMap<String, Map> allMaps) {
        this.world = world;
        this.allMaps = allMaps;
    }

    public Vector<String> getAllEntitiesAsString(){
        Vector<String> entitiesAsString = new Vector<String>();
        ImmutableBag<Entity> entities = world.getManager(GroupManager.class).getEntities("all");
        for(Entity e : entities){
            entitiesAsString.add(EntityToString.convert(e));
        }
        return entitiesAsString;
    }

    public Vector<Entity> getAllEntities(){
        Vector<Entity> entities = new Vector<Entity>();
        ImmutableBag<Entity> entitiesBag = world.getManager(GroupManager.class).getEntities("all");
        for(Entity e : entitiesBag){
            entities.add(e);
        }
        return entities;
    }

    public void printEntities(){
        for(String mapName : allMaps.keySet()){
            System.out.println(mapName + " contains following entities: ");
            ImmutableBag<Entity> entities = world.getManager(GroupManager.class).getEntities(mapName);
            Bag<Component> components = new Bag<Component>();
            for(Entity entity : entities){
                System.out.print(entity + " ");
                entity.getComponents(components);
                for(Component component : components){
                    System.out.print(component.getClass() + " ");
                }
                components.clear();
                System.out.println();
            }
        }
    }
}
