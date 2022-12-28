package Maps;


import Display.Panel;
import Entities.Player;
import Graphics.MyGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Map {

    protected ArrayList<Tile> map = new ArrayList<>();
    protected MapsManager.MAPS name;
    protected MapsManager mapsManager;
    //  ================================================================  Constructs  =============
    protected Map(MapsManager mapsManager) {
        this.mapsManager = mapsManager;
    }
    //  ================================================================  Adding static  ==========
    public enum TILES {
        //  Все координаты основаы на расположении объектов в файле "dungeon tiles.png"
        BIG_PLATE,                      /* Большая плита (68, 61) { <- одна из точек для нахождения на изображении } */
        PLATE_WITH_HOLE,                /* Небольшая плита с дырой посередине размером (127, 44) */
        ROW_1, ROW_2, ROW_3,            /* Ряд в 1 (77, 138), 2 (44, 137), 3 блока (123, 114) */
        PLATE_B,                        /* Небольшая плита с основанием (61, 179) */
        ROW_1_B, ROW_2_B, ROW_3_B,      /* Ряд в 1 (76, 225), 2 (51, 224), 3 (148, 174) с основанием */
        PLATE_WITH_HOLE_B,              /* Небольшая плита с дырой посередине размером с основанием (99, 180) */
        PLATE_C,                        /* Небольшая плита с смешанным основанием (58, 274) */
        PLATE_WITH_HOLE_C,              /* Небольшая плита с дырой со смешанным основанием (122, 298) */
        ROW_1_C, ROW_2_C, ROW_3_C,      /* Ряд в 1 (82, 329), 2 (59, 331), 3 (147, 272) со смешанным основанием */
        OPEN_DOOR,                      /* Небольшая открытая дверь (153, 219) */
        CLOSED_DOOR,                    /* Небольшая закрытая дверь (163, 219) */
        CLOSED_GRID,                    /* Закрытая решетка размером с небольшую дверь (180, 220) */
        CHEST,                          /* Сундук (153, 123) */
        BRIDGE_X,                       /* Деревянный мост по оси Х (155, 140) */
        BRIDGE_Y,                       /* Деревянный мост по оси Y (173, 137) */
        BIG_BOX,                        /* Большая коробка (171, 118) */
        SMALL_BOX,                      /* Небольшая коробка (187, 123) */
        BARREL,                         /* Небольшая деревянная бочка (204, 116) */
        TORCH,                          /* Факел (195, 138) */
        EMPTINESS,                      /* Пустота (203, 220) */
        MINI_ISLAND,                    /* Небольшой остров среди воды (313, 53) */
        POND,                           /* Пруд (261, 48) */
        POND_ISLAND,                    /* И пруд, и остров (266, 108) */
        GUTTER, GUTTER_B, CLOSED_GUTTER,/* Водосток (248, 146), ещё один водосток (269, 146) и закрытый водосток (281, 149) */
        WATER_ROW_Y,                    /* Ряд воды вдоль соответствущей оси (295, 98) */
        WATER_ROW_X,                    /* Ряд воды вдоль соответствущей оси (326, 90) */
        ISLAND_ROW_X,                   /* Островок вдоль соответствующей оси (305, 123) */
        ISLAND_ROW_Y,                   /* Островок вдоль соответствующей оси (337, 124) */
        ISLAND_CIRCLES,                 /* Круги дорог среди воды (276, 175) */
        STREAM_X,                       /* Поток воды по Х (304, 199) */
        STREAM_Y,                       /* Поток воды по Y (306, 171) */
        STAIRS_X,                       /* Лестница по X (209, 39) */
        STAIRS_Y,                       /* Лестница по Y (229, 58) */
        CENTER                          /* Якобы центр (196, 66) */
    }
    public static Tile getTile(TILES tile, int worldX, int worldY, Map map) {
        if (tile == null) return null;

        switch (tile) {
            case PLATE_WITH_HOLE, PLATE_WITH_HOLE_B, PLATE_WITH_HOLE_C: return new HoleTile(tile, worldX, worldY, map);
            case OPEN_DOOR, CLOSED_GRID, CLOSED_DOOR: return new Doors(tile, worldX, worldY, map);

            default: return new Tile(tile, worldX, worldY, map);
        }

    }
    //  ===========================================================  Abstracts methods  ===========
    public abstract void render(Graphics2D graphics2D);
    public abstract void update(double newPassedNanoSec);
    protected abstract void initMap();
    //  ===========================================================  Adding public methods  =======
    protected boolean showMapBoarders;  // Разрешение на показ хитбокса сущности
    public void showMapBoarders() {
        showMapBoarders = true;
    }
    public void hideMapBoarders() {
        showMapBoarders = false;
    }
    public ArrayList<Tile> getMap() {
        return map;
    }
    public MapsManager getMapsManager() {
        return mapsManager;
    }
    //  ===========================================================  Adding classes  ==============
    public static class Tile {
        /** Обычый тайл лишь с единственным хитбоксом. Может расширяться */
        protected final Map map;  // Используемая тайл карта
        public BufferedImage image;
        public TILES tile;
        public int worldX;
        public int worldY;

        // Хитбокс обычного тайла карты
        protected Rectangle hitBox;
        //  Constructors  =========================================================
        public Tile(TILES tile, int worldX, int worldY, Map map) {
            this.map = map;
            this.tile = tile;

            this.worldX = worldX;
            this.worldY = worldY;

            image = MyGraphics.tiles.get(tile);

            setAVAILABLE_AREA(tile);
        }
        //  ================================================================  Adding methods  =========
        private void setAVAILABLE_AREA(TILES tile) {
            //TODO Установка допустимого места пребывания игрока на тайле
            switch (tile) {
                case BIG_PLATE: hitBox = new Rectangle(0, 0, 80, 80); break;
                case PLATE_WITH_HOLE, PLATE_WITH_HOLE_B, PLATE_WITH_HOLE_C,
                        POND_ISLAND, POND: hitBox = new Rectangle(0, 0, 48, 48); break;
                case ROW_2, ROW_2_B, ROW_2_C,
                        WATER_ROW_X,
                            STAIRS_X: hitBox = new Rectangle(0, 0, 32, 16); break;
                case ROW_3, ROW_3_B, ROW_3_C,
                        WATER_ROW_Y,
                            STAIRS_Y: hitBox = new Rectangle(0, 0, 16, 32); break;
                case PLATE_B, PLATE_C: hitBox = new Rectangle(0, 0, 48, 32); break;
                case ROW_1, ROW_1_B, ROW_1_C,
                        STREAM_X: hitBox = new Rectangle(0, 0, 16, 16); break;
                case OPEN_DOOR, CLOSED_GRID, CLOSED_DOOR,
                        CHEST, BIG_BOX, BARREL, SMALL_BOX,
                            GUTTER, GUTTER_B, CLOSED_GUTTER: hitBox = new Rectangle(); break;
                case BRIDGE_X, BRIDGE_Y: hitBox = new Rectangle(0, 0, 17, 16); break;
                case TORCH: hitBox = new Rectangle(0, 0, 7 , 20); break;
                case STREAM_Y: hitBox = new Rectangle(0, 0, 16, 29); break;
                case CENTER: hitBox = new Rectangle(0, 0, 32, 32); break;
            }
        }

        public Rectangle getHitBox() {
            return hitBox;
        }
        public Map getMap() {
            return map;
        }
        //  ===========================================================  Overrides methods  ===========
        @Override
        public String toString() {
            return tile.name();
        }
    }

    public static class HoleTile extends Tile {
        /** Тайл с дырой, в которую можно провалиться */
        // Хитбокс дыры
        protected Rectangle holeHitBox;
        //  Constructors  =========================================================
        public HoleTile(TILES tile, int worldX, int worldY, Map map) {
            super(tile, worldX, worldY, map);

            setHoleHitBox(tile);
        }
        //  ================================================================  Adding methods  =========
        private void setHoleHitBox(TILES tile) {
            //TODO добавление большего количества тайлов и их запрещенные боксы
            switch (tile) {
                case PLATE_WITH_HOLE: holeHitBox = new Rectangle(16, 16, 16, 16); break;

            }
        }
        public Rectangle getHoleHitBox() {
            return holeHitBox;
        }
    }

    public static class Doors extends Tile {
        /** Двери-тайлы с переходом на другие локации */

        // Площадь двери, находясь в которой будет просиходить некторое действие с дверью или игроков рядом (координаты относительно заблокированной площади)
        public Rectangle actionsArea;
        // Удтверждение, что дуерь сейчас используется -> вывод надписи string
        private boolean using;
        private String string;
        //  Constructors  =========================================================
        public Doors(TILES tile, int worldX, int worldY, Map map) {
            super(tile, worldX, worldY, map);
            // В калссе двери hitbox - это область, где сущность не сможет находиться
            hitBox = new Rectangle(0, 0, image.getWidth(), image.getHeight());
            actionsArea = new Rectangle(0, hitBox.height, hitBox.width, 6);
        }

        //  ================================================================  Adding methods  =========
        public void doAction() {
            // Действия при подходу игрока к двери (табличка со словами слева от игрока на определенной высоте- <Открыть ?>)
            using = true;
            if (tile == TILES.OPEN_DOOR) {
                string = "Enter?";
            } else {
                //TODO Вывод <Open?>
            }
        }

        public void render(Graphics2D graphics2D) {
            // Метод для отрисовки надписей при подходу к двери игроком
            Panel panel = getMap().getMapsManager().getMainPanel();
            MyGraphics.printBoxyFontString(string, panel.getEntityManager().getPlayer().X - panel.getOverallScale(),
                    panel.getEntityManager().getPlayer().Y - 10 - panel.getOverallScale(), panel.getOverallScale() > 6? 3: 2, graphics2D);

        }

        public boolean getUsing() {
            return using;
        }
    }

    public static class Object extends Tile {

        public Object(TILES tile, int worldX, int worldY, Map map) {
            super(tile, worldX, worldY, map);
        }
    }

    public static class TileWithWater extends Tile {

        public TileWithWater(TILES tile, int worldX, int worldY, Map map) {
            super(tile, worldX, worldY, map);
        }
    }
}