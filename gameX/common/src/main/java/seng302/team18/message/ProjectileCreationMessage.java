package seng302.team18.message;

/**
 * Class to hold data for a projectile location message
 */
public class ProjectileCreationMessage implements MessageBody {

    private int projectile_id;

    public ProjectileCreationMessage(int projectile_id) {
        this.projectile_id = projectile_id;
    }


    @Override
    public int getType() {
        return AC35MessageType.PROJECTILE_CREATION.getCode();
    }


    public int getProjectile_id() {
        return projectile_id;
    }
}
