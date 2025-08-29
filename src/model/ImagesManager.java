package model;

import model.enums.Direction;
import model.enums.GhostColor;
import model.enums.ItemType;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ImagesManager {

    private final Map<String, ImageIcon> images;

    public ImagesManager() {
        images = new HashMap<>();
        loadImages();
    }

    private void loadImages() {
        images.put("pacman_closed", loadIcon("resources/game/pacman/mouthClosed.png"));
        images.put("pacman_up_2",  loadIcon("resources/game/pacman/mouthOpen_up.png"));
        images.put("pacman_down_2",loadIcon("resources/game/pacman/mouthOpen_down.png"));
        images.put("pacman_left_2",loadIcon("resources/game/pacman/mouthOpen_left.png"));
        images.put("pacman_right_2",loadIcon("resources/game/pacman/mouthOpen_right.png"));

        for (GhostColor color : GhostColor.values()) {
            String colorName = color.name().toLowerCase();
            for (Direction dir : Direction.values()) {
                String dirName = dir.name().toLowerCase();
                for (int frame = 1; frame <= 2; frame++) {
                    String key = ghostKey(colorName, dirName, frame);
                    String path = String.format(
                            "resources/game/ghosts/%s/%s_%d.png",
                            colorName, dirName, frame
                    );
                    images.put(key, loadIcon(path));
                }
            }
        }

        for (ItemType type : ItemType.values()) {
            String key = type.name().toLowerCase();
            String path = String.format(
                    "resources/game/items/%s.png",
                    key
            );
            images.put(key, loadIcon(path));

        }

        images.put("dot",  loadIcon("resources/game/textures/dot.png"));
        images.put("wall", loadIcon("resources/game/textures/wall.png"));
    }


    private String ghostKey(String color, String direction, int frame) {
        return "ghost_" + color + "_" + direction + "_" + frame;
    }


    private ImageIcon loadIcon(String path) {
        return new ImageIcon(path);
    }

    public ImageIcon getPacmanImage(Direction direction, int frame) {
        if (frame == 1) {
            return images.get("pacman_closed");
        } else {
            String key = "pacman_" + direction.name().toLowerCase() + "_2";
            return images.get(key);
        }
    }

    public ImageIcon getGhostImage(Direction direction, int frame, GhostColor color) {
        String key = ghostKey(color.name().toLowerCase(), direction.name().toLowerCase(), frame);
        return images.get(key);
    }

    public ImageIcon getDotImage() {
        return images.get("dot");
    }

    public ImageIcon getWall() {
        return images.get("wall");
    }

    public ImageIcon getItemImage(ItemType type) {
        return images.get(type.name().toLowerCase());
    }

    //image scaling

    public int getCharacterSize(int width, int height){
       return  Math.max(Math.min(width, height * 3 / 4), 12);
    }

    public int getDotSize(int width,int  height){
        return Math.max(Math.min(width, height / 4), 4);
    }

    public int getItemSize(int width, int height) {
            return  Math.max(Math.min(width , height * 4/7 ), 8);

    }

}
