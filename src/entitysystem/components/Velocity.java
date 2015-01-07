package entitysystem.components;

import com.artemis.Component;

public class Velocity extends Component{
    public float vectorX, vectorY;

    public Velocity(float vectorX, float vectorY) {
        this.vectorX = vectorX;
        this.vectorY = vectorY;
    }
}
