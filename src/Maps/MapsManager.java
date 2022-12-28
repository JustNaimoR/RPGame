package Maps;

import Display.Panel;

public class MapsManager {
    // Дополнительлный масштаб для карт в случае дополнительного отдельного масштабирования
    public static int MAP_SCALE = 1;

    private Map currentMap;
    private final Panel mainPanel;

    public enum MAPS {
        OVERLORD
    }
    //  ================================================================  Constructs  =============
    public MapsManager(Panel mainPanel) {
        this.mainPanel = mainPanel;
    }
    //  ===========================================================  Adding public methods  =======
    public void setMap(MAPS map) {
        if (map == MAPS.OVERLORD) currentMap = new OverlordMap(this);
    }
    public void removeMap() {
        currentMap = null;
    }
    //  ==============================================================  Getters & Setters ==========
    public Panel getMainPanel() {
        return mainPanel;
    }
    public Map getCurrentMap() {
        return currentMap;
    }
}