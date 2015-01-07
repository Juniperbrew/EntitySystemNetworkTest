package entitysystem.core;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import entitysystem.components.Health;
import entitysystem.components.Position;
import entitysystem.systems.AIRandomMovementSystem;
import entitysystem.util.EntityToString;
import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.MapObject;
import tiled.core.ObjectGroup;
import tiled.io.TMXMapReader;

import java.util.*;

public class WorldData {

	World world;
	HashMap<String,Map> allMaps;
	Vector<Entity> entities;
	String resFolderPath;

	public WorldData(String startingMapName){
		entities = new Vector<Entity>();
		resFolderPath = (System.getProperty("user.dir") + "\\res\\");
		System.out.println("#Loading world from: " + resFolderPath);
		allMaps = new HashMap<String,Map>();
		System.out.println("#Loading mainmap: " + startingMapName);
		allMaps.put(startingMapName, loadMap(startingMapName));
		
		String subMapsString = allMaps.get(startingMapName).getProperties().getProperty("SubMaps");
		if(subMapsString != null){
			String[] subMaps = subMapsString.split(" ");
			for(String subMapName : subMaps){
				System.out.println("#Loading submap: " + subMapName);
				allMaps.put(subMapName,loadMap(subMapName));
			}
		}

		world = new World();
		setWorldManagersAndSystems();
		world.initialize();

		for(String mapName : allMaps.keySet()){
			loadObjects(mapName);
		}
		printEntities();
	}

	private void setWorldManagersAndSystems(){
		world.setManager(new GroupManager());
		world.setSystem(new AIRandomMovementSystem());
	}

	public World getWorld(){
		return world;
	}

	public Set<String> getMapList(){
		return allMaps.keySet();
	}

	public Vector<Entity> getAllEntities(){
		return entities;
	}

	public Vector<String> getAllEntitiesAsString(){
		Vector<String> entitiesAsString = new Vector<String>();
		for(Entity e : entities){
			entitiesAsString.add(EntityToString.convert(e));
		}
		return entitiesAsString;
	}

	private void printEntities(){
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
	
	private World loadObjects(String mapName){

		System.out.println("#Loading objects in map: " + mapName);
		ObjectGroup objectLayer = null;
		
		Vector<MapLayer> mapLayers = allMaps.get(mapName).getLayers();
		for(MapLayer layer: mapLayers){

			Properties layerProperties = layer.getProperties();
			System.out.println("Checking layers: " + layer.getName());
			if(layer instanceof ObjectGroup){
				objectLayer = (ObjectGroup) layer;
				System.out.println("#Found object layer#");
			}
			System.out.println(layer.getClass());
			layerProperties.list(System.out);
		}
		System.out.println();

		Iterator<MapObject> objIterator = objectLayer.getObjects();
		int objectCount = 0;
		int collissionCount = 0;
		int entityCount = 0;
		int messageCount = 0;
		int teleportCount = 0;
		int spawnCount = 0;
		int exitCount = 0;
		int noTypeCount = 0;

		System.out.println("##Listing non collission objects##");
		while(objIterator.hasNext()){
			objectCount++;
			MapObject obj = objIterator.next();
			if(obj.getType().equalsIgnoreCase("collission")){
				collissionCount++;
				//System.out.println("Name: " + obj.getName() + " Type: " + obj.getType());
				//obj.getProperties().list(System.out);
			}else if(obj.getType().equalsIgnoreCase("entity")){
				entityCount++;
				createEntity(mapName, obj);
			}else if(obj.getType().equalsIgnoreCase("message")){
				messageCount++;
				System.out.println("Name: " + obj.getName() + " Type: " + obj.getType());
				obj.getProperties().list(System.out);
				System.out.println();
			}else if(obj.getType().equalsIgnoreCase("teleport")){
				teleportCount++;
				System.out.println("Name: " + obj.getName() + " Type: " + obj.getType());
				obj.getProperties().list(System.out);
				System.out.println();
			}else if(obj.getType().equalsIgnoreCase("spawn")){
				spawnCount++;
				System.out.println("Name: " + obj.getName() + " Type: " + obj.getType());
				obj.getProperties().list(System.out);
				System.out.println();
			}else if(obj.getType().equalsIgnoreCase("exit")){
				exitCount++;
				System.out.println("Name: " + obj.getName() + " Type: " + obj.getType());
				obj.getProperties().list(System.out);
				System.out.println();
				//System.out.println("Found exit leading to map: " + obj.getProperties().getProperty("exit") + " Location: " + obj.getProperties().getProperty("location"));
			}else if(obj.getType().equalsIgnoreCase("")){
				noTypeCount++;
				System.out.println("Name: " + obj.getName() + " Type: " + obj.getType());
				obj.getProperties().list(System.out);
				System.out.println(obj.getImageSource());
				System.out.println(obj.getImage(1));
				System.out.println();
			}
		}
		System.out.println("Collission count: " + collissionCount);
		System.out.println("Entity count: " + entityCount);
		System.out.println("Message count: " + messageCount);
		System.out.println("Teleport count: " + teleportCount);
		System.out.println("Spawn count: " + spawnCount);
		System.out.println("Exit count: " + exitCount);
		System.out.println("Objects without type: " + noTypeCount);
		System.out.println("Supported objects: " + (collissionCount+entityCount+messageCount+teleportCount+spawnCount+exitCount+noTypeCount));
		System.out.println("Total object count: " + objectCount);
		System.out.println();
		
		return world;
	}

	private void createEntity(String mapName, MapObject obj){
		System.out.println("Name: " + obj.getName() + " Type: " + obj.getType());
		Properties entityProperties = obj.getProperties();
		entityProperties.list(System.out);
		System.out.println();

		Entity newEntity = world.createEntity()
				.edit()
				.add(new Position(obj.getX(), obj.getY()))
				.getEntity();
		world.getManager(GroupManager.class).add(newEntity,mapName);

		if(entityProperties.containsKey("health")){
			int health = Integer.parseInt(entityProperties.getProperty("health"));
			newEntity.edit().add(new Health(health));
		}

		entities.add(newEntity);

	}
	
	private Map loadMap(String mapName){
		

		System.out.println("#Loading map: " + mapName);
		TMXMapReader mapReader = new TMXMapReader();

		Map tiledMap = null;

		try {
			tiledMap = mapReader.readMap(resFolderPath + mapName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(tiledMap == null){
			return null;
		}

		String mapFileName = tiledMap.getFilename();
		int tileHeight = tiledMap.getTileHeight();
		int tileWidth = tiledMap.getTileWidth();
		int mapWidth = tiledMap.getWidth();
		int mapHeight = tiledMap.getHeight();

		Properties mapProperties = tiledMap.getProperties();

		System.out.println("Map file name: " + mapFileName);
		System.out.println("TileHeight: " + tileHeight);
		System.out.println("TileWidth: " + tileWidth);
		System.out.println("MapWidth: " + mapWidth);
		System.out.println("MapHeight: " + mapHeight);
		mapProperties.list(System.out);
		System.out.println();
		
		return tiledMap;
	}

	//For testing
	public static void main(String args[]){
		new WorldData("untitled.tmx");
	}
}
