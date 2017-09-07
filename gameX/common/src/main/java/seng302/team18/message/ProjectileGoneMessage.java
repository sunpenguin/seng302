package seng302.team18.message;

/**
 * Created by csl62 on 7/09/17.
 */
public class ProjectileGoneMessage implements MessageBody {

    private int id;


    public ProjectileGoneMessage(int id) {
        this.id = id;
    }

    @Override
    public int getType() {
        return AC35MessageType.PROJECTILE_GONE.getCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
