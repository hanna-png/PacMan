package view;

import model.enums.Cell;
import model.GameModel;
import model.ImagesManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CellRenderer extends DefaultTableCellRenderer {
    private final GameModel model;
    private final ImagesManager imageManager;

    public CellRenderer(GameModel model) {
        this.model = model;
        imageManager = new ImagesManager();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);

        cell.setHorizontalAlignment(SwingConstants.CENTER);
        cell.setVerticalAlignment(SwingConstants.CENTER);
    //check ghosts' coordinates
        for (var ghost : model.getGhosts()) {
            if (ghost.getCurrentC() == column && ghost.getCurrentR() == row) {
                int ghostSize = imageManager.getCharacterSize(table.getColumnModel().getColumn(column).getWidth(),  table.getRowHeight());
                Image image = imageManager.getGhostImage(ghost.getCurrentDirection(), ghost.getCurrentFrame(), ghost.getGhostColor())
                        .getImage().getScaledInstance(ghostSize, ghostSize, Image.SCALE_SMOOTH);
                cell.setIcon(new ImageIcon(image));
                cell.setText("");
                cell.setBackground(Color.BLACK);
                return cell;
            }
        }



      // check pacman coordinates
        if (model.getPacman().getCurrentC() == column && model.getPacman().getCurrentR() == row) {
            int pacSize = imageManager.getCharacterSize(table.getColumnModel().getColumn(column).getWidth(),  table.getRowHeight());
            Image image = imageManager.getPacmanImage(model.getPacman().getCurrentDirection(), model.getPacman().getCurrentFrame())
                    .getImage().getScaledInstance(pacSize, pacSize, Image.SCALE_SMOOTH);
            cell.setIcon(new ImageIcon(image));
            cell.setText("");
            cell.setBackground(Color.BLACK);
            cell.setOpaque(true);
            return cell;
        }

        //check items' coordinates

        for(var item : model.getItems()) {
            if(item.getCol() == column && item.getRow() == row) {
                int itemSize = imageManager.getItemSize(table.getColumnModel().getColumn(column).getWidth(),  table.getRowHeight());
                Image image = imageManager.getItemImage(item.getType())
                        .getImage().getScaledInstance(itemSize, itemSize, Image.SCALE_SMOOTH);
                cell.setIcon(new ImageIcon(image));
                cell.setText("");
                cell.setBackground(Color.BLACK);
                return cell;
            }
        }
      //check textures
        Cell cellValue = (Cell) value;
        cell.setIcon(null);
        switch (cellValue) {
            case WALL -> {
                setBackground(Color.BLACK);
                setText("");
                Image image = imageManager.getWall().getImage().getScaledInstance(
                        table.getColumnModel().getColumn(column).getWidth(),
                        table.getRowHeight(), Image.SCALE_SMOOTH);
                cell.setIcon(new ImageIcon(image));
            }
            case DOT -> {
                setBackground(Color.BLACK);
                setText("");
                int dotSize = imageManager.getDotSize(table.getColumnModel().getColumn(column).getWidth(),  table.getRowHeight());
                Image image = imageManager.getDotImage().getImage().getScaledInstance(dotSize, dotSize, Image.SCALE_SMOOTH);
                cell.setIcon(new ImageIcon(image));
            }
            case FRAME -> {
                setBackground(Color.GREEN);
                setText("");
            }

            default -> {
                setBackground(Color.BLACK);
                setText("");
            }
        }
        cell.setOpaque(true);
        return cell;
    }

}
