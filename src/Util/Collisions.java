package Util;

import Entities.Entity;
import Maps.Map;

public class Collisions {

    public static boolean intersection(Entity entity1, Entity entity2) {
        // Пересечние двух сущностей
        if (entity1.worldY + entity1.getMainHitBox().height < entity2.worldY) return false;
        if (entity1.worldY > entity2.worldY + entity2.getMainHitBox().height) return false;
        if (entity1.worldX > entity2.worldX + entity2.getMainHitBox().width) return false;
        if (entity1.worldX + entity1.getMainHitBox().width < entity2.worldX) return false;

        return true;
    }

    public static boolean intersection(int worldX1, int worldY1, int width1, int height1,
                                       int worldX2, int worldY2, int width2, int height2) {
        // Метод для получения значения пересечения двух объектов, для которых действует масштабирование(!!!)
        if (worldY1 + height1 < worldY2) return false;
        if (worldY1 > worldY2 + height2) return false;
        if (worldX1 > worldX2 + width2) return false;
        if (worldX1 + width1 < worldX2) return false;

        return true;
    }

    public static boolean intersection(Map.Tile tile, int worldX2, int worldY2, int width2, int height2) {
        // Дополнительный метод для получения значения пересечения экрана и тайла карты для отрисовки (но не только)
        if (tile.worldY + tile.getHitBox().height < worldY2) return false;
        if (tile.worldY > worldY2 + height2) return false;
        if (tile.worldX > worldX2 + width2) return false;
        if (tile.worldX + tile.getHitBox().width < worldX2) return false;

        return true;
    }

    public static boolean intersection(Entity entity, int worldX2, int worldY2, int width2, int height2) {
        if (entity.worldX + entity.getFootHitBox().x + entity.getFootHitBox().width < worldX2) return false;
        if (entity.worldY + entity.getFootHitBox().y + entity.getFootHitBox().height < worldY2) return false;
        if (entity.worldX + entity.getFootHitBox().x > worldX2 + width2) return false;
        if (entity.worldY + entity.getFootHitBox().y > worldY2 + height2) return false;

        return true;
    }

    public static boolean intersection(Map.Tile tile, Entity entity) {
        // Дополнительный метод для проверки нахождения сущности на тайле

        if (tile instanceof Map.HoleTile) {
            // Дополнительная проверка на нахождение игрока в допустимом месте тайла с пропастью
            Map.HoleTile holeTile = (Map.HoleTile) tile;
            if (holeTile.worldX + holeTile.getHoleHitBox().x < entity.worldX + entity.getFootHitBox().x &&
                    holeTile.worldX + holeTile.getHoleHitBox().x + holeTile.getHoleHitBox().width > entity.worldX + entity.getFootHitBox().x + entity.getFootHitBox().width &&
                    holeTile.worldY + holeTile.getHoleHitBox().y < entity.worldY + entity.getFootHitBox().y &&
                    holeTile.worldY + holeTile.getHoleHitBox().y + holeTile.getHoleHitBox().height > entity.worldY + entity.getFootHitBox().y + entity.getFootHitBox().height) {
                return false;
            }
        }

        if (tile.worldY + tile.getHitBox().y + tile.getHitBox().height < entity.worldY + entity.getFootHitBox().y) return false;
        if (tile.worldY + tile.getHitBox().y > entity.worldY + entity.getFootHitBox().y + entity.getFootHitBox().height) return false;
        if (tile.worldX + tile.getHitBox().x > entity.worldX + entity.getFootHitBox().x + entity.getFootHitBox().width) return false;
        if (tile.worldX + tile.getHitBox().x + tile.getHitBox().width < entity.worldX + entity.getFootHitBox().x) return false;

        return true;
    }

}