package Entities;

import Display.Panel;
import Util.Collisions;

import java.awt.*;
import Graphics.MyGraphics;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {
    // Дефолтные значения высоты и ширины хитбокса игрока
    public static final int DEFAULT_PLAYER_HITBOX_WIDTH = 17;
    public static final int DEFAULT_PLAYER_HITBOX_HEIGHT = 25;

    // Player's animation, ways to files
    public static HashMap<DIRECTION, BufferedImage> standardPlayerStances = new HashMap<>();
    public static HashMap<DIRECTION, BufferedImage[]> standardPlayerMovements = new HashMap<>();
    public static BufferedImage[] deathInVoidAnimation = new BufferedImage[8];

    private DIRECTION currentDirectionImages;
    //  ================================================================  Constructs  =============
    public Player(Panel panel, int worldX, int worldY, int hitBoxWidth, int hitBoxHeight) {
        super(panel, worldX, worldY);

        // Хитбоксы частей игрока (меч, главный хибокс, ноги...)
        footHitBox = new Rectangle( 6, 19, 4, 3);
        mainHitBox = new Rectangle(hitBoxWidth, hitBoxHeight);
        // Центральное расположение персонажа
        this.X = (mainPanel.getWidth() - hitBoxWidth * getMainPanel().getOverallScale()) / 2;
        this.Y = (mainPanel.getHeight() - hitBoxHeight * getMainPanel().getOverallScale()) / 2;

        initPlayerAnimation();
    }
    public Player(Panel panel, int worldX, int worldY) {
        super(panel, worldX, worldY);

        // Хитбоксы частей игрока (меч, главный хибокс, ноги...)
        mainHitBox = new Rectangle(DEFAULT_PLAYER_HITBOX_WIDTH, DEFAULT_PLAYER_HITBOX_HEIGHT);
        footHitBox = new Rectangle( 6, 19, 4, 3);
        // Центральное расположение персонажа в экране
        this.X = (mainPanel.getWidth() - mainHitBox.width * getMainPanel().getOverallScale()) / 2;
        this.Y = (mainPanel.getHeight() - mainHitBox.height * getMainPanel().getOverallScale()) / 2;

        initPlayerAnimation();
    }
    //  =======================================================================  Behaviour  =======
    boolean killed = false;
    private void killInVoid(Graphics2D graphics2D) {
        //TODO Смерть игрока в пустоте
        killed = true;

        for (int mark = 0; mark < deathInVoidAnimation.length; mark++) {
            mainPanel.paint(mainPanel.getGraphics());

            graphics2D.drawImage(deathInVoidAnimation[mark],
                    (mainPanel.getWidth() - deathInVoidAnimation[mark].getWidth() * mainPanel.getOverallScale()) / 2,
                    (mainPanel.getHeight() - deathInVoidAnimation[mark].getHeight() * mainPanel.getOverallScale()) / 2,
                    deathInVoidAnimation[mark].getWidth() * mainPanel.getOverallScale(),
                    deathInVoidAnimation[mark].getHeight() * mainPanel.getOverallScale(),
                    null);

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(1);
    }
    //  ===========================================================  Render & Update Entity  ======
    private double UPassedNanoTime = 0;
    private long lastAnimationUpdate = -1;
    private long delta = 0;

    private int currentImage;
    @Override
    public void render(Graphics2D graphics2D) {
        if (killed) return;
        if (MOVEMENT_DIRECTION_UP ^ MOVEMENT_DIRECTION_DOWN || MOVEMENT_DIRECTION_RIGHT ^ MOVEMENT_DIRECTION_LEFT) {

            if (MOVEMENT_DIRECTION_UP) currentDirectionImages = DIRECTION.UP;
            else if (MOVEMENT_DIRECTION_DOWN) currentDirectionImages = DIRECTION.DOWN;
            else if (MOVEMENT_DIRECTION_RIGHT) currentDirectionImages = DIRECTION.RIGHT;
            else if (MOVEMENT_DIRECTION_LEFT) currentDirectionImages = DIRECTION.LEFT;

            updateAnimation();
            // Отрисовка картинки анимации ровно в центра экрана не учитывая хитбокс
            BufferedImage currentUsingImage = standardPlayerMovements.get(currentDirectionImages)[currentImage];
            // System.out.println(currentDirectionImages + "   " + currentImage + " из " + standardPlayerMovements.get(currentDirectionImages).length);
            graphics2D.drawImage(currentUsingImage,
                    (mainPanel.getWidth() - currentUsingImage.getWidth() * mainPanel.getOverallScale()) / 2,
                    (mainPanel.getHeight() - currentUsingImage.getHeight() * mainPanel.getOverallScale()) / 2,
                    currentUsingImage.getWidth() * mainPanel.getOverallScale(),
                    currentUsingImage.getHeight() * mainPanel.getOverallScale(), null);
        } else {
            // Отрисовка картинки анимации ровно в центра экрана не учитывая хитбокс
            BufferedImage currentUsingImage = standardPlayerStances.get(currentDirectionImages);
            graphics2D.drawImage(currentUsingImage,
                    (mainPanel.getWidth() - currentUsingImage.getWidth() * mainPanel.getOverallScale()) / 2,
                    (mainPanel.getHeight() - currentUsingImage.getHeight() * mainPanel.getOverallScale()) / 2,
                    currentUsingImage.getWidth() * mainPanel.getOverallScale(),
                    currentUsingImage.getHeight() * mainPanel.getOverallScale(), null);

        }

        dopRendering(graphics2D);
    }
    private void updateAnimation() {
        long movementDelayMillis = 150;
        long currentAnimationUpdate = System.currentTimeMillis();
        long elapsedTime = currentAnimationUpdate - (lastAnimationUpdate != -1? lastAnimationUpdate : currentAnimationUpdate);
        lastAnimationUpdate = currentAnimationUpdate;
        delta += elapsedTime;

        while (delta >= movementDelayMillis) {
            currentImage++;

            if (currentImage >= standardPlayerMovements.get(currentDirectionImages).length) {
                currentImage = 0;
            }

            delta -= movementDelayMillis;
        }
        // Дополнительная проверка на выход ха рамки currentImage для случаев, когда цикл while сверху не совершился
        if (currentImage >= standardPlayerMovements.get(currentDirectionImages).length) {
            currentImage = 0;
        }
    }
    @Override
    public void update(double newPassedNanoSec) {
        checkLocation();
        UPassedNanoTime += newPassedNanoSec;
        double dSPEED = 1_000_000_000.0 / speed;
        int shift = (int) (UPassedNanoTime / dSPEED);

        if (!(dSPEED < UPassedNanoTime)) return;
        if (MOVEMENT_DIRECTION_UP) {
            worldY -= shift;
            //worldY -= (int) (UPassedNanoTime / dSPEED);
            if (checkCollisions()) worldY += shift;
        }
        if (MOVEMENT_DIRECTION_RIGHT) {
            worldX += shift;
            //worldX += (int) (UPassedNanoTime / dSPEED);
            if (checkCollisions()) worldX -= shift;
        }
        if (MOVEMENT_DIRECTION_DOWN) {
            worldY += shift;
            //worldY += (int) (UPassedNanoTime / dSPEED);
            if (checkCollisions()) worldY -= shift;
        }
        if (MOVEMENT_DIRECTION_LEFT) {
            worldX -= shift;
            //worldX -= (int) (UPassedNanoTime / dSPEED);
            if (checkCollisions()) worldX += shift;
        }

        UPassedNanoTime -= dSPEED /* * (int) (UPassedNanoTime / dSPEED) */;
    }
    @Override
    protected boolean checkCollisions() {
        // Метод проверки на столкновения на поле игроком
        for (Maps.Map.Tile currentTile: getMainPanel().getMapsManager().getCurrentMap().getMap()) {
            // Проверка на столкновение игрока с недопустимой частью тайла (например не двери нельзя стоять)
            if (currentTile instanceof Maps.Map.Doors && Collisions.intersection(currentTile, this)) {
                return true;
            }
        }
        // Проверка с пересечением других сущностей
        for (Map.Entry<String, Entity> entry: mainPanel.getEntityManager().getEntityHashMap().entrySet()) {
            if (entry.getValue() != this && Collisions.intersection(this, entry.getValue())) return true;
        }

        return false;
    }
    protected void checkLocation() {
        // Функция проверки местанахождения игрока на игровом поле
        boolean onTile = false;
        for (Maps.Map.Tile currentTile: getMainPanel().getMapsManager().getCurrentMap().getMap()) {
            // Проверка на нахождение игрока на том или ином тайле карты
            if (!(currentTile instanceof Maps.Map.Doors) &&  Collisions.intersection(currentTile, this)) {
                onTile = true;
                break;
            }
        }
        if (!onTile) killInVoid((Graphics2D) mainPanel.getGraphics());

        for (Maps.Map.Tile currentTile: getMainPanel().getMapsManager().getCurrentMap().getMap()) {
            // Проверка на нахождение игрока рядом с объектом, поддерживаемый некоторое действие
            if (currentTile instanceof Maps.Map.Doors && Collisions.intersection(this, ((Maps.Map.Doors) currentTile).actionsArea.x + currentTile.worldX,
                    ((Maps.Map.Doors) currentTile).actionsArea.y + currentTile.worldY, ((Maps.Map.Doors) currentTile).actionsArea.width,
                    ((Maps.Map.Doors) currentTile).actionsArea.height)) {
                ((Maps.Map.Doors) currentTile).doAction();
            }
        }
    }
    @Override
    protected void dopRendering(Graphics2D graphics2D) {
        if (showHitBox) {
            graphics2D.setColor(Color.RED);
            graphics2D.drawRect(X, Y, mainHitBox.width * mainPanel.getOverallScale(), mainHitBox.height * mainPanel.getOverallScale());
        }

        if (showFootHitBox) {
            graphics2D.setColor(Color.ORANGE);
            graphics2D.drawRect(X + footHitBox.x * mainPanel.getOverallScale(), Y + footHitBox.y * mainPanel.getOverallScale(),
                    footHitBox.width * mainPanel.getOverallScale(), footHitBox.height * mainPanel.getOverallScale());
        }
    }
    //  =======================================================================  Animations =======
    private void initPlayerAnimation() {
        // Инициализация анимаций игрока
        System.out.println("Обработка файлов анимации игрока...");
        // Стандартные стойки игрока
        standardPlayerStances.put(DIRECTION.UP, MyGraphics.getImage("Graphics/Texture atlas/Player's different textures.png",
                1, 95, 20, 26));
        standardPlayerStances.put(DIRECTION.RIGHT, MyGraphics.getImage("Graphics/Texture atlas/Player's different textures.png",
                2, 54, 20, 26));
        standardPlayerStances.put(DIRECTION.DOWN, MyGraphics.getImage("Graphics/Texture atlas/Player's different textures.png",
                88, 11, 20, 26));
        standardPlayerStances.put(DIRECTION.LEFT, MyGraphics.getImage("Graphics/Entities/Player/Necessary parts/Standard player's staying left.png",
                8, 13, 20, 26));
        // Стандартная анимация движения персонажа в разных направлениях
        standardPlayerMovements.put(Entity.DIRECTION.UP, MyGraphics.getImages("Graphics/Texture atlas/Player's different textures.png",
                28, 95, 20, 26, 6, false));
        standardPlayerMovements.put(Entity.DIRECTION.DOWN, MyGraphics.getImages("Graphics/Texture atlas/Player's different textures.png",
                120, 11, 20, 26, 7, false));
        standardPlayerMovements.put(Entity.DIRECTION.RIGHT, MyGraphics.getImages("Graphics/Texture atlas/Player's different textures.png",
                29, 53, 20, 26, 8, false));
        standardPlayerMovements.put(Entity.DIRECTION.LEFT, MyGraphics.getImages("Graphics/Entities/Player/Necessary parts/Standard player's movements left.png",
                8, 16, 20, 26, 8, true));
        //TODO Анимация смерти игрока в пустоте (ошибка - выходит за границы картинки)
        deathInVoidAnimation = MyGraphics.getImages("Graphics/Texture atlas/Player's standard movements.png",
                4, 132, 26, 25, 8, false);

        currentDirectionImages = DIRECTION.DOWN;  // Установка изначального направления просмотра у игрока

        System.out.println(MyGraphics.ANSI_GREEN + "    Файлы анимации игрока обработаны!" + MyGraphics.ANSI_RESET);
    }
    //  ==============================================================  Getters & Setters ==========

}