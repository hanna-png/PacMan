package model;

import controller.MenuController;
import view.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ShortCutInstaller {
    public void setMenuShortcut() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
                new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getID() != KeyEvent.KEY_PRESSED) {
                            return false;
                        }
                        boolean ctrl  = (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK)  != 0;
                        boolean shift = (e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0;
                        boolean qkey  = (e.getKeyCode() == KeyEvent.VK_Q);

                        if (ctrl && shift && qkey) {

                            SwingUtilities.invokeLater(() -> {
                                Window window = KeyboardFocusManager
                                        .getCurrentKeyboardFocusManager()
                                        .getActiveWindow();
                                if (window != null) {
                                    window.dispose();
                                }

                                if (window instanceof GameView) {
                                    ((GameView) window).getGameModel().returnToMenu();
                                }

                                new MenuController();
                            });
                            return true;
                        }

                        return false;
                    }
                }
        );
    }
}
