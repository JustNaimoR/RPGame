package Graphics;

import Entities.Entity;
import Entities.EntityManager;
import Maps.Map;
import Maps.MapsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MyGraphics {
    // Color of console's symbols
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    // Boxy font
    public static String boxyFontFilename = "Graphics/Fonts/boxy_bold_font_4.png";
    public static String boxyFontVoc = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`{|}~abcdefghijklmnopqrstuvwxyz ";
    public static HashMap<Integer, BufferedImage> boxyFont;
    // Map's tiles
    public static String mapTilesWay = "Graphics/Maps/dungeon tiles.png";
    public static HashMap<Map.TILES, BufferedImage> tiles = new HashMap<>();

    public static void initGraphics() {
        initBoxyFont();
        MyGraphics.initMapTiles();
    }
    //  =========================================================  Init Animations  ===============
    private static void initBoxyFont() {
        System.out.println("Обработка " + boxyFontFilename + "...");

        boxyFont = new HashMap<>();
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = getImage(boxyFontFilename);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "    Файл " + boxyFontFilename + " не найден!!!" + ANSI_RESET);
            System.exit(-1);
        }

            int defaultHeight = 8;
        int readableY = 0;
        int readableX = 1;
        int symbolsCount = 0;

        while (true) {
            if (readableX >= bufferedImage.getWidth()) {
                readableX = 0;
                readableY += 9;

                if (readableY >= bufferedImage.getHeight()) {
                    break;
                }

            }

            int pixelsLine = readableY;
            while (readableX < bufferedImage.getWidth() && bufferedImage.getRGB(readableX, pixelsLine) == 0x00000000) {
                for ( ; pixelsLine < readableY + 9; pixelsLine++) {
                    if (pixelsLine == bufferedImage.getHeight() || bufferedImage.getRGB(readableX, pixelsLine) != 0x00000000) {
                        break;
                    }
                }
                if (pixelsLine == readableY + 9 || pixelsLine == bufferedImage.getHeight()) {
                    pixelsLine = readableY;
                    readableX++;
                }
            }

            if (readableX >= bufferedImage.getWidth()) continue;
            // Из-за промежутка между строками после 67-го символа
            if (symbolsCount == 68) {
                defaultHeight++;
                readableY += 2;
            }

            int rightX = readableX;
            int rightY = pixelsLine;
            // right Shift
            while (true) {
                if (bufferedImage.getRGB(rightX + 1, rightY) != 0x00000000) {
                    rightX++;
                } else if (rightY - readableY == 8) {
                    break;
                } else {
                    while (rightY - readableY != 8 && bufferedImage.getRGB(rightX + 1, rightY) == 0x00000000) {
                        rightY++;
                    }
                }
            }

            boxyFont.put(symbolsCount++, bufferedImage.getSubimage(readableX, readableY, rightX - readableX + 1, defaultHeight));
            readableX += rightX - readableX + 2;
        }

        boxyFont.put(symbolsCount, bufferedImage.getSubimage( 50, 18, 7, 8));

        System.out.println(ANSI_GREEN + "    " + boxyFontFilename + " обработан!" + ANSI_RESET);
    }

    private static void initMapTiles() {
        System.out.println("Обработка " + mapTilesWay + "...");

        // (!) Закоменченные тайлы - не поддерживаемые для рисовки (!)
        tiles.put(Map.TILES.BIG_PLATE, getImage(mapTilesWay, 32, 32, 80, 92));
        tiles.put(Map.TILES.PLATE_WITH_HOLE, getImage(mapTilesWay, 116, 32, 48, 60));
        tiles.put(Map.TILES.ROW_1, getImage(mapTilesWay, 68, 128, 16, 28));
        tiles.put(Map.TILES.ROW_2, getImage(mapTilesWay, 32, 128, 32, 28));
        tiles.put(Map.TILES.ROW_3, getImage(mapTilesWay, 116, 96, 16, 44));
        tiles.put(Map.TILES.PLATE_B, getImage(mapTilesWay, 32, 160, 48, 48));
        tiles.put(Map.TILES.ROW_1_B, getImage(mapTilesWay, 68, 212, 16, 32));
        tiles.put(Map.TILES.ROW_2_B, getImage(mapTilesWay, 32, 212, 32, 32));
        tiles.put(Map.TILES.ROW_3_B, getImage(mapTilesWay, 140, 160, 16, 48));
        tiles.put(Map.TILES.PLATE_WITH_HOLE_B, getImage(mapTilesWay, 88, 160, 48, 64));
        tiles.put(Map.TILES.PLATE_C, getImage(mapTilesWay, 32, 260, 48, 56));
        tiles.put(Map.TILES.PLATE_WITH_HOLE_C, getImage(mapTilesWay, 88, 260, 48, 72));
        tiles.put(Map.TILES.ROW_1_C, getImage(mapTilesWay, 68, 320, 16, 40));
        tiles.put(Map.TILES.ROW_2_C, getImage(mapTilesWay, 32, 320, 32, 50));
        tiles.put(Map.TILES.ROW_3_C, getImage(mapTilesWay, 140, 260, 16, 56));
        tiles.put(Map.TILES.OPEN_DOOR, getImage(mapTilesWay, 140, 212, 16, 16));
        tiles.put(Map.TILES.CLOSED_DOOR, getImage(mapTilesWay, 156, 212, 16, 16));
        tiles.put(Map.TILES.CLOSED_GRID, getImage(mapTilesWay, 172, 212, 16, 16));
        tiles.put(Map.TILES.CHEST, getImage(mapTilesWay, 142, 112, 17, 16));
        tiles.put(Map.TILES.BRIDGE_X, getImage(mapTilesWay, 140, 132, 20, 15));
        tiles.put(Map.TILES.BRIDGE_Y, getImage(mapTilesWay, 165, 130, 16, 20));
        tiles.put(Map.TILES.BIG_BOX, getImage(mapTilesWay, 162, 104, 18, 24));
        tiles.put(Map.TILES.SMALL_BOX, getImage(mapTilesWay, 182, 116, 9, 12));
        tiles.put(Map.TILES.BARREL, getImage(mapTilesWay, 196, 109, 13, 16));
        tiles.put(Map.TILES.TORCH, getImage(mapTilesWay, 192, 131, 7, 20));
        tiles.put(Map.TILES.EMPTINESS, getImage(mapTilesWay, 192, 212, 16, 16));
        // tiles.put(Map.TILES.MINI_ISLAND, getImage(mapTilesWay, 288, 32, 48, 48));
        tiles.put(Map.TILES.POND, getImage(mapTilesWay, 236, 32, 48, 48));
        tiles.put(Map.TILES.POND_ISLAND, getImage(mapTilesWay, 236, 84, 48, 48));
        tiles.put(Map.TILES.GUTTER, getImage(mapTilesWay, 240, 140, 16, 16));
        tiles.put(Map.TILES.GUTTER_B, getImage(mapTilesWay, 256, 140, 16, 17));
        tiles.put(Map.TILES.CLOSED_GUTTER, getImage(mapTilesWay, 272, 140, 16, 16));
        tiles.put(Map.TILES.WATER_ROW_X, getImage(mapTilesWay, 308, 84, 32, 16));
        tiles.put(Map.TILES.WATER_ROW_Y, getImage(mapTilesWay, 288, 84, 16, 32));
        // tiles.put(Map.TILES.ISLAND_ROW_X, getImage(mapTilesWay, 288, 120, 32, 16));
        // tiles.put(Map.TILES.ISLAND_ROW_Y, getImage(mapTilesWay, 324, 104, 16, 32));
        // tiles.put(Map.TILES.ISLAND_CIRCLES, getImage(mapTilesWay, 244, 160, 48, 48));
        tiles.put(Map.TILES.STREAM_X, getImage(mapTilesWay, 296, 192, 16, 16));
        tiles.put(Map.TILES.STREAM_Y, getImage(mapTilesWay, 296, 160, 16, 29));
        tiles.put(Map.TILES.STAIRS_X, getImage(mapTilesWay, 184, 32, 32, 16));
        tiles.put(Map.TILES.STAIRS_Y, getImage(mapTilesWay, 216, 48, 16, 32));
        tiles.put(Map.TILES.CENTER, getImage(mapTilesWay, 184, 48, 32, 32));

        System.out.println(ANSI_GREEN + "    " + mapTilesWay + " обработана!" + ANSI_RESET);
    }
    //  ======================================================================  Images  ===========
    public static BufferedImage getImage(String filename) {
        return getImage(filename, 0 , 0, -1, -1);
    }

    public static BufferedImage getImage(String filename, int startX, int startY, int width, int height) {
        Image image = new ImageIcon(filename).getImage();

        assert (image != null);
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawImage(image, 0,0, null);
        graphics2D.dispose();

        if (width == -1) width = bufferedImage.getWidth();
        if (height == -1) height = bufferedImage.getHeight();

        return bufferedImage.getSubimage(startX, startY, width, height);
    }

    public static BufferedImage[] getImages(String filename, int startX, int startY, int width, int height, int count, boolean reverse) {
        /**  Метод для получения массива картинок количества count, зарисовка которых начинается с точек startX и startY.
         *      Высота и ширина записываемых картинок - height и width соответственно.
         *      В методе подсчитываются смещения границ картинки по оси X и применяется для каждой записываемой.
         *   (*) Подразумевается, что задданные значения width и height обозначают итоговые значения ширины и высоты получаемых
         *      картинок с учетом отступов, а также рсположение картинок в файле на одной высоте                      */

        BufferedImage[] bufferedImages = new BufferedImage[count];
        BufferedImage mainImage = getImage(filename);

        // Запись массива изображений
        for (int currentPicture = 0; ; currentPicture++) {
            bufferedImages[currentPicture] = getImage(filename, startX, startY, width, height);

            if (currentPicture + 1 == count) break;  // Условие конца записи

            startX += width;

            // Ищем натуральное начало картинки для подсчет натуральной ширины без учета смещения
            for (int pxLine = 0; mainImage.getRGB(startX, startY + pxLine) == 0x00000000; pxLine++) {
                if (pxLine + 1 == height) {
                    startX++;
                    pxLine = 0;
                }
            }

            // Подсчет натуральной ширины картинки
            int pictureWidth = 0;
            for (int pictureX = startX, pictureY = startY; ; pictureY++) {

                if (mainImage.getRGB(pictureX, pictureY) != 0x00000000) {
                    pictureY = startY;
                    pictureX++;

                    pictureWidth++;
                }

                if (pictureY + 1 == startY + height) {
                    break;
                }
            }

            startX -= (width - pictureWidth) / 2;
        }
        // Если требуется переверутый массив
        if (reverse) Collections.reverse(Arrays.asList(bufferedImages));

        return bufferedImages;
        /*BufferedImage[] bufferedImages = new BufferedImage[count];
        BufferedImage mainImage = getImage(filename);

        // Подсчет смещения по X
        int shiftX = 0;
        for (int pxLine = startY; pxLine < startY + height; pxLine++) {

            if (mainImage.getRGB(startX + shiftX, pxLine) != 0x00000000) {
                break;
            }

            if ((pxLine - startY) + 1 == height) {
                shiftX++;
                pxLine = startY;
            }
        }
        // Запись массива изображений
        for (int currentPicture = 0; ; currentPicture++) {
                bufferedImages[currentPicture] = getImage(filename, startX, startY, width, height);

            if (currentPicture + 1 == count) break;

            startX += width;
            boolean run = true;
            while (run) {
                startX++;

                for (int line = startY; line < startY + height; line++) {
                    if (mainImage.getRGB(startX, line) != 0x00000000) {
                        run = false;
                        break;
                    }
                }
            }

            startX -= shiftX;
        }
        // Если требуется переверутый массив
        if (reverse) Collections.reverse(Arrays.asList(bufferedImages));

        return bufferedImages;*/
    }
    //  =================================================================  Additional methods  ====
    public static void printBoxyFontString(String string, int startX, int startY, int scale, Graphics2D graphics2D) {
        int offsetX = 0;
        for (int currentLetter = 0; currentLetter < string.length(); currentLetter++) {

            int serialNumber = -1;
            for (int mark = 0; mark < boxyFontVoc.length(); mark++) {
                if (boxyFontVoc.charAt(mark) == string.charAt(currentLetter)) {
                    serialNumber = mark;
                    break;
                }
            }

            if (serialNumber != -1) graphics2D.drawImage(boxyFont.get(serialNumber), startX + offsetX, startY,
                    boxyFont.get(serialNumber).getWidth() * scale, boxyFont.get(serialNumber).getHeight() * scale, null);
            offsetX += boxyFont.get(serialNumber).getWidth() * scale + scale;
        }
    }
    //  =======================================================================  Getters ==========
    public static String getBoxyFontVoc() {
        return boxyFontVoc;
    }
}