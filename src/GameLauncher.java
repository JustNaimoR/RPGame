import Display.Frame;

public class GameLauncher {

    public static void main(String[] args) {
        // Start the game
        Frame frame = new Frame();
        frame.getPanel().start();

        frame.getPanel().setShowFPS_UPS(true);
        // frame.getPanel().getEntityManager().getEntityHashMap().get("Player").showHitBox();
            //frame.getPanel().getEntityManager().getEntityHashMap().get("Player").setScale(10);
        frame.getPanel().getMapsManager().getCurrentMap().showMapBoarders();
        frame.getPanel().getEntityManager().getEntityHashMap().get("Player").showFootHitBox();

        //TODO  - Добавить новые типы тайлов для дверей, воды и т.п., обустроить их
        //      - Добавить все тайлы и их платформы
        //      - Создать первую тестовую карту и на ней продолжать тесты игры
        //      - Добавить меч в игру, анимацию его поднятия и удары с ним
    }

}