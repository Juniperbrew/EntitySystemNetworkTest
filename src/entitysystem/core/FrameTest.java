package entitysystem.core;

import com.artemis.*;
import entitysystem.util.EntityToString;

import java.util.Vector;

public class FrameTest extends TestAbstract<String>{

	World world;
	WorldData worldData;

	public FrameTest(){

	}

	@Override
	void initialize(){
		worldData = new WorldData("untitled.tmx");
		world = worldData.getWorld();
	}

	@Override
	void doLogic() {

		Vector<String> entitiesAsStrings = new Vector<String>();
		for(Entity e : worldData.getAllEntities()){
			entitiesAsStrings.add(EntityToString.convert(e));
		}
		infoFrame.setListItems(entitiesAsStrings);

		world.setDelta(1);
		world.process();
	}

	@Override
	void oneSecondElapsed() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String args[]){
		new FrameTest();
	}
}
