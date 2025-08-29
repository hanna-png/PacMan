package view;

import model.BoardTableModel;
import model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class GameView extends JFrame {
    private static final int minimalCellSize = 10;
    private final GameModel gameModel;
    private JTable boardTable;
    private JScrollPane scrollPane;
    private JPanel infoPanel;
    private HashMap<String, JLabel> lables;

    public GameView(GameModel gameModel) {
        this.gameModel = gameModel;
        lables = new HashMap<>();
        openGameWindow();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        SwingUtilities.invokeLater(this::adjustBoard);
    }

    private void openGameWindow() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gameModel.setEnd(true);
            }
        });
        setTitle("Pacman");

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustBoard();
            }
        });

        infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        infoPanel.setBackground(Color.BLACK);
        lables.put("score", new JLabel("Score:"));
        lables.put("lives", new JLabel("Lives:"));
        lables.put("time",  new JLabel("Time:"));
        for (JLabel lbl : lables.values()) {
            lbl.setForeground(Color.WHITE);
            infoPanel.add(lbl);
            infoPanel.add(Box.createHorizontalStrut(30));
        }

        boardTable = new JTable(new BoardTableModel(gameModel));
        boardTable.setTableHeader(null);
        boardTable.setCellSelectionEnabled(false);
        boardTable.setDragEnabled(false);
        boardTable.setEnabled(false);
        boardTable.setFocusable(false);
        boardTable.setColumnSelectionAllowed(false);
        boardTable.setRowSelectionAllowed(false);
        boardTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        boardTable.setShowGrid(false);
        boardTable.setIntercellSpacing(new Dimension(0, 0));
        boardTable.setDefaultRenderer(Object.class, new CellRenderer(gameModel));


        JPanel centralizingPanel = new JPanel(new GridBagLayout());
        centralizingPanel.setBackground(Color.BLACK);
        centralizingPanel.add(boardTable);


        scrollPane = new JScrollPane(centralizingPanel);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(infoPanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void adjustBoard() {
        int totalR = gameModel.getHeight();
        int totalC = gameModel.getWidth();

        Dimension areaBox = getContentPane().getSize();
        if (areaBox.width <= 0 || areaBox.height <= 0) {
            return;
        }

        int topBarH = infoPanel.getHeight();
        int usableW = areaBox.width;
        int usableH = areaBox.height - topBarH;
        if (usableW <= 0 || usableH <= 0) {
            return;
        }

        int cellByW = usableW / totalC;
        int cellByH = usableH / totalR;
        int cellSize = (cellByW < cellByH) ? cellByW : cellByH;

        boolean needScroll = false;
        if (cellSize < minimalCellSize) {
            cellSize = minimalCellSize;
            needScroll = true;
        }

        boardTable.setRowHeight(cellSize);
        for (int colIndex = 0; colIndex < totalC; colIndex++) {
            boardTable.getColumnModel()
                    .getColumn(colIndex)
                    .setPreferredWidth(cellSize);
        }

        int actualW = cellSize * totalC;
        int actualH = cellSize * totalR;
        boardTable.setPreferredScrollableViewportSize(
                new Dimension(actualW, actualH)
        );

        if (needScroll) {
            scrollPane.setHorizontalScrollBarPolicy(
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            );
            scrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            );
        } else {
            scrollPane.setHorizontalScrollBarPolicy(
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            );
            scrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER
            );
        }

        scrollPane.revalidate();
        scrollPane.repaint();
    }



    public HashMap<String, JLabel> getLables() {
        return lables;
    }

    public JTable getBoardTable() {
        return boardTable;
    }

    public GameModel getGameModel() {
        return gameModel;
    }
}
