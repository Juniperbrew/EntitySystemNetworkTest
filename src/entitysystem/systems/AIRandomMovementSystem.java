package entitysystem.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import entitysystem.components.Position;

import java.util.Random;

@Wire
public class AIRandomMovementSystem extends EntityProcessingSystem{

    ComponentMapper<Position> pm;
    Random rng;

    public AIRandomMovementSystem() {
        super(Aspect.getAspectForAll(Position.class));
        rng = new Random();
    }

    @Override
    protected void process(Entity e) {
        System.out.println("Moving randomly");
        Position position = pm.get(e);

        position.x += ((rng.nextFloat()*5)-2.5f)*world.delta;
        position.y += ((rng.nextFloat()*5)-2.5f)*world.delta;
    }
}
