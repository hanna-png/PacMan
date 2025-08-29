package model;

import model.enums.Cell;

import javax.swing.table.AbstractTableModel;

public class BoardTableModel extends AbstractTableModel {
    private GameModel gameModel;

    public BoardTableModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }
    @Override
    public int getRowCount() {
        return gameModel.getBoard().length;
    }

    @Override
    public int getColumnCount() {
        return gameModel.getBoard()[0].length;
    }

    @Override
    public Cell getValueAt(int rowIndex, int columnIndex) {
        return gameModel.getBoard()[rowIndex][columnIndex];
    }
}
