package Maps;

import Display.Panel;
import Entities.Entity;
import Entities.Player;
import Util.Collisions;

import java.awt.*;

public class OverlordMap extends Map {

    public static final int WIDTH = 280;
    public static final int HEIGHT = 150;

    public OverlordMap(MapsManager mapsManager) {
        super(mapsManager);
        name = MapsManager.MAPS.OVERLORD;
        initMap();
    }
    //  ===========================================================  Overrides methods  ===========
    @Override
    public void render(Graphics2D graphics2D) {
        Panel usingPanel = mapsManager.getMainPanel();
        Entity player = mapsManager.getMainPanel().getEntityManager().getEntityHashMap().get("Player");

        for (Tile currentTile: map) {
            /* Для отрисовки лишь близьлежащих тайлов, относительно игрока, его расположения на экране и размеры экрана,
             *    новый "Хитбокс" персонажа увеличивается равномерно по экрану, чтобы отрисовывались тайлы заранее их появления на экране */
            // Формула отрисовки тайлов - как и отрисоква границ карты, но с добавлением координат тайла на карте
            if (Collisions.intersection(currentTile,
                    player.worldX - (mapsManager.getMainPanel().getWidth() - player.X) / 2 - 30 * usingPanel.getOverallScale(),
                    player.worldY - (mapsManager.getMainPanel().getHeight() - player.Y) / 2 - 30 * usingPanel.getOverallScale(),
                    mapsManager.getMainPanel().getWidth() + 60 * usingPanel.getOverallScale(),
                    mapsManager.getMainPanel().getHeight() + 60 * usingPanel.getOverallScale())) {
                // Отрисовка тайла в случае пересечения его с увеличенной рамкой
                graphics2D.drawImage(currentTile.image,
                        /* Отрисовка тайла на экране в зависимости от ситуации - либо экран показывает весь игровой мир, либо экран слишком мал для мира,
                         *    но всё ещё учитывается, что экран держит в центре не карту, а игрока */
                        //TODO Ещё раз проверить правильность формул для отрисовке на экране тайлов ()
                        (usingPanel.getWidth() - player.getMainHitBox().width * usingPanel.getOverallScale()) / 2 - player.worldX * usingPanel.getOverallScale() + currentTile.worldX * usingPanel.getOverallScale(),
                        (usingPanel.getHeight() - player.getMainHitBox().height * usingPanel.getOverallScale()) / 2 - player.worldY * usingPanel.getOverallScale() + currentTile.worldY * usingPanel.getOverallScale(),
                        currentTile.image.getWidth() * usingPanel.getOverallScale(),
                        currentTile.image.getHeight() * usingPanel.getOverallScale(),
                        null);
            }
        }

        dopRendering(graphics2D);
    }
    @Override
    public void update(double newPassedNanoSec) {
        // Что-то тут будет как-нибудь потом
    }

    private void dopRendering(Graphics2D graphics2D) {

        if (showMapBoarders) {
            // Отрисовка границ карты
            /* Алгоритм: Вычисление расстояния от левой части хитбокса игрока до левой границы экрана -> вычитание координат
            *   игрока по карте (также везде учитываются масштаб показываемой карты) */
            Player player = (Player) mapsManager.getMainPanel().getEntityManager().getEntityHashMap().get("Player");
            Panel panel = mapsManager.getMainPanel();
            graphics2D.setColor(Color.GREEN);
            graphics2D.drawRect( (panel.getWidth() - player.getMainHitBox().width * panel.getOverallScale()) / 2 - player.worldX * panel.getOverallScale(),
                    (panel.getHeight() - player.getMainHitBox().height * panel.getOverallScale()) / 2 - player.worldY * panel.getOverallScale(),
                    WIDTH * panel.getOverallScale(),
                    HEIGHT * panel.getOverallScale());

            // System.out.println(player.worldX + "   " + player.worldY);
        }

        for (Maps.Map.Tile currentTile: getMap()) {
            if (currentTile instanceof Doors && ((Doors) currentTile).getUsing()) {
                ((Doors) currentTile).render(graphics2D);
            }
        }
    }
    //  ==================================================================  Adding methods  =======
    @Override
    protected void initMap() {
        //TODO добавить возможность с файла получать данные по карте
        map.add(getTile(TILES.BIG_PLATE, 25, 29, this));
        map.add(getTile(TILES.PLATE_C, 25 + 80, 29 + 18, this));
        map.add(getTile(TILES.PLATE_C, 25 + 80 + 48, 29 + 18, this));
        map.add(getTile(TILES.PLATE_WITH_HOLE, 25 + 80 + 48 + 48, 29 + 10, this));
        map.add(getTile(TILES.OPEN_DOOR, 33, 37, this));
    }



}