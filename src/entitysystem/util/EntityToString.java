package entitysystem.util;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import entitysystem.components.Health;
import entitysystem.components.Position;

public class EntityToString {

    public static String convert(Entity e){
        StringBuilder entityString = new StringBuilder();
        Bag<Component> components = new Bag<Component>();
        components = e.getComponents(components);

        entityString.append(e + " ");

        for (Component component : components) {

            if (component instanceof Position) {
                Position pos = (Position) component;
                entityString.append("X: " + pos.x + " Y: " + pos.y + " ");
            } else if (component instanceof Health) {
                Health health = (Health) component;
                entityString.append("Health: " + health.health + " ");
            } else {
                entityString.append(component.getClass() + " ");
            }
        }
        components.clear();
        return entityString.toString();
    }
}