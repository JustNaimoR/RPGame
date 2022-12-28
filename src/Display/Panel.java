package Display;

import Entities.Entity;
import Entities.EntityManager;

import Entities.Player;
import Graphics.MyGraphics;
import Maps.MapsManager;
import Util.KeyHandler;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel implements Runnable {

    private final Thread gamingLoopThread = new Thread(this);
    private final Frame frame;

    private final EntityManager entityManager = new EntityManager(this);
    private final MapsManager mapsManager = new MapsManager(this);
    private final KeyHandler keyHandler = new KeyHandler();
    private boolean running;
    // Общий масштаб для всего
    private int overallScale;

    private boolean showFPS_UPS;
    public int FPS, UPS;

    //  ================================================================  Constructs  =============
    protected Panel(Frame frame) {
        this.frame = frame;

        setPreferredSize(new Dimension(Frame.DEFAULT_WIDTH, Frame.DEFAULT_HEIGHT));
        setFocusable(true);
        frame.setVisible(true);
    }
    //  ================================================================  Gaming Loop  ============
    public synchronized void start() {
        if (!running) {
            MyGraphics.initGraphics();
            setOverallScale(5);  // Установка начального общего масштаба
                    // Начальные координаты игрока
                    int startX = 50;
                    int startY = 50;
                    entityManager.initPlayer(startX, startY);
                    // Установка прослушиваний клавиш и мыши для работы
                    keyHandler.setPlayer((Player) entityManager.getEntityHashMap().get("Player"));
                    this.addKeyListener(keyHandler);
                    this.addMouseWheelListener(keyHandler);

                    mapsManager.setMap(MapsManager.MAPS.OVERLORD);  // Установка карты

            gamingLoopThread.start();
            running = true;
        }
    }

    public synchronized void stop() {
        if (running) {
            try {
                gamingLoopThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            running = false;
        }
    }
    @Override
    public void run() {
        /**
         *  Gaming loop (Игровая петля) - является игровым движком, который работает с методами update() и render()
         *      (методы обновления физических характеристик используемых объектов и отрисовка новой картинки на основе состояния мира).
         *       - Для создания используется отделбный поток (Thread) и реализуется в методе run() или с помощью класса Timer.
         *      Поскольку скорость постоянной обработки (речь о цикле while(running) {...}, который используется для постоянного обновления)
         *          слишком высока и тем самым загружает процессор компьютера, то для контроля частоты обновления используется отмер времени (переменные delta)
         *          Для самого замера используется метод System.nanoTime(), дающий время в наносекундах, т.е. наиболее точное время.                              */

        final int MAX_FPS = 60;  //  Ограничение, потолок допустимого количества обновления кадров (FPS - frames per second)
        final int MAX_UPS = 120;  //  Ограничение для колиества обновления игрового мира (UPS - updates per second)

        /**  optimalFPS и optimalUPS - оптимальное время для обновления кадров и состояния мира. Другими словами это то время, что должно
         *      быть в идеальном случае между каждым обновлением состояния/кадра.
         *   1_000_000_000 nanoSec == 1 Sec                                                                                              */
        final double optimalFPS = 1_000_000_000.0 / MAX_FPS;
        final double optimalUPS = 1_000_000_000.0 / MAX_UPS;

        /**  FPSDelta и UPSDelta - переменные для замера времени, пройденного между обновлением состояния/кадра.
         *   delta - дополнительная переменная для возможности вывода занчений количества FPS и UPS
         *   startTime - переменная для начала замера промежутка времени между обновлениями                     */
        double FPSDelta = 0, UPSDelta = 0, delta = System.currentTimeMillis();
        int frames = 0, updates = 0;
        long starTime = System.nanoTime();

        /**  running - поле, обозначающее работу игры (основного цикла). Оно является внешним, может измениться в
         *      процессе игры. Изменение на false будет означать, что игра цикл должен прекратить работу -> заканчивается игра   */
        while (running) {

            long currentTime = System.nanoTime();  //  Текущее время
            FPSDelta += (currentTime - starTime);  //  Добавление пройденного промежутка времени к FPSDelta
            UPSDelta += (currentTime - starTime);  //  Добавление пройденного промежутка времени к UPSDelta
            starTime = currentTime;

            /**  Обновления экрана и обновление состояния должны быть независимыми, поскольку при одинаоковом количестве
             *      на слабых компьютерах происходить действия будут медленнее, чем на более быстрых. Также обновление состояние мира
             *      может потребовать больше итераций в секунду, час количество FPS                                                   */
            if (UPSDelta >= optimalUPS) {
                /**  update(UPSDelta);  --> Поскольку при увелечении или уменьшении количества обновлений состояния мира
                 *                          может меняться скорость всего происходящаего (например мяч лети быстрее или медленнее),
                 *                          в метод обновления состояния передается и время в секундах, которое после будет умножано на скорость
                 *                          изменения (dt)                                                                                   */
                update(UPSDelta);
                updates++;               //  Вычитание из промежутка времени, поскольку только что был проведено обновления мира,
                UPSDelta -= optimalUPS;  // <-- а значит промежуток отработан.
            }
            //  Такая же процедура для обновления экрана
            if (FPSDelta >= optimalFPS) {
                repaint();
                frames++;
                FPSDelta -= optimalFPS;
            }

            /**  Данное условие создано для возможности просматривать текущее количество кадров и частоты обновления мира.
             *      Срабатывает каждую секунду также с помощью замера промежутка времени delta */
            if (System.currentTimeMillis() - delta > 1_000 && showFPS_UPS) {
                UPS = updates;
                updates = 0;
                FPS = frames;
                frames = 0;
                delta += 1000;  //  Поскольку мы уже обработали одну секунду
            }

        }
    }
    //  ===========================================================  Animations and Physics  ======
    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(frame.getBackground());
        graphics2D.fillRect(0, 0, Frame.DEFAULT_WIDTH, Frame.DEFAULT_HEIGHT);


        render(graphics2D);

        if (showFPS_UPS) MyGraphics.printBoxyFontString(FPS + " FPS", 1000, 30, 5, graphics2D);
    }

    public void update(double passedNanoSec) {
        for (java.util.Map.Entry<String, Entity> entry : entityManager.getEntityHashMap().entrySet()) {
            entry.getValue().update(passedNanoSec);
        }

        if (mapsManager.getCurrentMap() != null)
        mapsManager.getCurrentMap().update(passedNanoSec);
    }

    public void render(Graphics2D graphics2D) {
        if (mapsManager.getCurrentMap() != null)
        mapsManager.getCurrentMap().render(graphics2D);  // Отрисовка текущей карты

        for (java.util.Map.Entry<String, Entity> entry : entityManager.getEntityHashMap().entrySet()) {
            entry.getValue().render(graphics2D);  // Отрисовка сущностей на поле
        }
    }
    //  =================================================================  Additional methods  ====
    public void setShowFPS_UPS(boolean value) {
        showFPS_UPS = value;
    }
    //  ==============================================================  Getters & Setters ==========
    public EntityManager getEntityManager() {
        return entityManager;
    }
    public MapsManager getMapsManager() {
        return mapsManager;
    }

    public void setOverallScale(int overallScale) {
        this.overallScale = overallScale;

        if (entityManager.getEntityHashMap().get("Player") == null) return;
        // Изменение позиции X Y игрока на экране при изменении масштаба мира
        entityManager.getEntityHashMap().get("Player").X = (getWidth() -
                entityManager.getEntityHashMap().get("Player").getMainHitBox().width * overallScale) / 2;
        entityManager.getEntityHashMap().get("Player").Y = (getHeight() -
                entityManager.getEntityHashMap().get("Player").getMainHitBox().height * overallScale) / 2;
    }
    public int getOverallScale() {
        return overallScale;
    }
    // Инкримент и декрмиент для масштаба карты
    public void incOverallScale() {
        setOverallScale(getOverallScale() + 1);
    }
    public void decOverallScale() {
        setOverallScale(getOverallScale() - 1);
    }
}