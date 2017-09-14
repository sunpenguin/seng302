package seng302.team18.message;

/**
 * Message that stores the information from ProjectileGone messages.
 */
public class ProjectileGoneMessage implements MessageBody {

    private int id;


    /**
     * Constructor for ProjectileGoneMessage.
     *
     * @param id of the projectile.
     */
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
