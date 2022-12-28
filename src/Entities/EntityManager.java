package Entities;

import Display.Panel;

import java.util.HashMap;

public class EntityManager {

    private final HashMap<String, Entity> entityHashMap = new HashMap<>();
    private int entityCount;
    private Panel mainPanel;

    public static final int DEFAULT_SPEED = 50;
    //  ================================================================  Constructs  =============
    public EntityManager(Panel mainPanel) {
        this.mainPanel = mainPanel;

        entityCount = 0;
    }
    //  ===========================================================  Adding public methods  =======
    public void initPlayer(int worldX, int worldY) {
        if (entityHashMap.get("Player") != null) return;

        // Создание и запись в Hashmap
        entityHashMap.put("Player", new Player(mainPanel, worldX, worldY));
    }
    public void removePlayer() {
        if (entityHashMap.get("Player") == null) return;

        entityHashMap.remove("Player");
    }

    public void addEntity() {
        //TODO создать работающую реализацию добавления сущностей в игру
        entityCount++;


    }

    //  =======================================================================  Getters ==========
    public int getEntityCount() {
        return entityCount;
    }

    public HashMap<String, Entity> getEntityHashMap() {
        return entityHashMap;
    }
    public Player getPlayer() {
        return (Player) entityHashMap.get("Player");
    }
}