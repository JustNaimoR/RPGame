package Entities;

import Util.Collisions;

import Display.Panel;
import java.awt.*;
import java.util.Map;

public abstract class Entity {
    // Coordinates in the world and on the screen
    public int X;
    public int Y;
    public int worldX;
    public int worldY;
    protected int speed;
    // Main hitbox & hitbox of entity's foot
    protected Rectangle mainHitBox;
    protected Rectangle footHitBox;
    // Link of a main gaming panel
    protected final Panel mainPanel;

    public enum DIRECTION {
        UP, RIGHT, DOWN, LEFT
    }
    // Entity's direction of movement
    public boolean MOVEMENT_DIRECTION_UP;
    public boolean MOVEMENT_DIRECTION_RIGHT;
    public boolean MOVEMENT_DIRECTION_DOWN;
    public boolean MOVEMENT_DIRECTION_LEFT;
    //  ================================================================  Constructs  =============
    public Entity(Panel mainPanel, int worldX, int worldY) {
        this.mainPanel = mainPanel;  // Using Panel

        this.worldX = worldX;
        this.worldY = worldY;

        this.speed = EntityManager.DEFAULT_SPEED;
    }
    //  =================================================================  Additional methods  ====
    protected boolean showHitBox;  // Разрешение на показ хитбокса сущности
    protected boolean showFootHitBox;  // Разрешение на показ хитбокса ног существа
    public void showHitBox() {
        showHitBox = true;
    }
    public void hideHitBox() {
        showHitBox = false;
    }

    public void showFootHitBox() {
        showFootHitBox = true;
    }
    public void hideFootHitBox() {
        showFootHitBox = false;
    }

    protected boolean checkCollisions() {
        for (Map.Entry<String, Entity> entry: mainPanel.getEntityManager().getEntityHashMap().entrySet()) {
            if (entry.getValue() != this && Collisions.intersection(this, entry.getValue())) return true;
        }
        return false;
    }
    //  ===========================================================  Render & Update Entity  ======
    protected abstract void dopRendering(Graphics2D graphics2D);
    public abstract void render(Graphics2D graphics2D);
    public abstract void update(double newPassedNanoSec);
    //  ================================================================  Getters & Setters =======
    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    }
    public int getSpeed() {
        return speed;
    }

    public Panel getMainPanel() {
        return mainPanel;
    }

    public void setHitBoxWidth(int width) {
        mainHitBox.width = width;
    }
    public void setHitBoxHeight(int height) {
        mainHitBox.height = height;
    }

    public Rectangle getMainHitBox() {
        return mainHitBox;
    }
    public Rectangle getFootHitBox() {
        return footHitBox;
    }
}