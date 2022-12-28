package Util;

import Entities.Player;
import Graphics.MyGraphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class KeyHandler implements KeyListener, MouseWheelListener {

    private Player player;

    //  ================================================================  Constructs  =============
    public KeyHandler(Player player) {
        this.player = player;
    }

    public KeyHandler() {
        player = null;
    }
    //  ======================================================  Override methods  =================
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        playerCheck();
        if (e.getKeyCode() == KeyEvent.VK_W) player.MOVEMENT_DIRECTION_UP = true;
        if (e.getKeyCode() == KeyEvent.VK_D) player.MOVEMENT_DIRECTION_RIGHT = true;
        if (e.getKeyCode() == KeyEvent.VK_S) player.MOVEMENT_DIRECTION_DOWN = true;
        if (e.getKeyCode() == KeyEvent.VK_A) player.MOVEMENT_DIRECTION_LEFT = true;
        // System.out.println("Key is pressed!");
    }
    @Override
    public void keyReleased(KeyEvent e) {
        playerCheck();
        if (e.getKeyCode() == KeyEvent.VK_W) player.MOVEMENT_DIRECTION_UP = false;
        if (e.getKeyCode() == KeyEvent.VK_D) player.MOVEMENT_DIRECTION_RIGHT = false;
        if (e.getKeyCode() == KeyEvent.VK_S) player.MOVEMENT_DIRECTION_DOWN = false;
        if (e.getKeyCode() == KeyEvent.VK_A) player.MOVEMENT_DIRECTION_LEFT = false;
        // System.out.println("Key is released!");
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        playerCheck();
        if (e.getWheelRotation() == -1 && player.getMainPanel().getOverallScale() <= 10) player.getMainPanel().incOverallScale();
        if (e.getWheelRotation() == 1 && player.getMainPanel().getOverallScale() >= 3) player.getMainPanel().decOverallScale();
        // System.out.println("Mouse wheel is moved!");
    }
    //  =================================================================  Additional methods  ====
    private void playerCheck() {
        if (player == null) {
            System.out.println(MyGraphics.ANSI_RED + "Игрок не был установлен для KeyHandler!!! Прекращение работы..." + MyGraphics.ANSI_RESET);
            System.exit(-1);
        }
    }
    //  =============================================================  Getters & Setters ==========
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
}
