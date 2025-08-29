import controller.MenuController;
import model.ShortCutInstaller;

import javax.swing.*;



public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuController controller = new MenuController();
            ShortCutInstaller shortCutInstaller = new ShortCutInstaller();
            shortCutInstaller.setMenuShortcut();

        });

    }
}