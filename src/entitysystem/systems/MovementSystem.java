package entitysystem.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import entitysystem.components.Position;
import entitysystem.components.Velocity;

@Wire
public class MovementSystem extends EntityProcessingSystem {

    ComponentMapper<Position> pm;
    ComponentMapper<Velocity> vm;

    public MovementSystem() {
        super(Aspect.getAspectForAll(Position.class, Velocity.class));
    }

    @Override
    protected void process(Entity e) {
                System.out.println("Processing movement");
                Position position = pm.get(e);
                Velocity velocity = vm.get(e);

                if(velocity == null) {
                    return;
                }
                position.x += velocity.vectorX*world.delta;
                position.y += velocity.vectorY*world.delta;
    }
}
