package view;

import model.ScoreModel;
import model.entities.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ScoresView extends JFrame {
    private final ScoreModel scoreModel;
    private final JButton backButton;

    public ScoresView(ScoreModel model) {
        this.scoreModel = model;
        this.backButton = createButton("Back to Menu", new Color(0, 255, 0));
        System.out.println("Scores: " + scoreModel.getDefaultListModel().size());
        openScoreWindow();
    }

    private void openScoreWindow() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("High Scores");

        Color green = new Color(0, 255, 0);
        Image backgroundImage = new ImageIcon("resources/menu/background.jpg").getImage();
        MenuView.BackgroundImagePanel backgroundPanel = new MenuView.BackgroundImagePanel(backgroundImage);

        JLabel title = new JLabel("HIGH SCORES");
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        title.setForeground(green);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        DefaultListModel<Score> unsorted = scoreModel.getDefaultListModel();
        ArrayList<Score> scores = new ArrayList<>();
        for (int i = 0; i < unsorted.getSize(); i++) {
            scores.add(unsorted.getElementAt(i));
        }

        scores.sort((s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));


        DefaultListModel<Score> sorted = new DefaultListModel<>();
        for (Score sc : scores) {
            sorted.addElement(sc);
        }

        JList<Score> scoreList = new JList<>(sorted);
        scoreList.setFont(new Font("Monospaced", Font.PLAIN, 16));
        scoreList.setForeground(green);
        scoreList.setBackground(Color.BLACK);
        scoreList.setFixedCellHeight(30);
        scoreList.setSelectionBackground(Color.DARK_GRAY);

        JScrollPane scoreScrollPane = new JScrollPane(scoreList);
        scoreScrollPane.setPreferredSize(new Dimension(300, 200));
        scoreScrollPane.setMaximumSize(new Dimension(300, 200));
        scoreScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreScrollPane.setBorder(BorderFactory.createLineBorder(green, 2));
        scoreScrollPane.setOpaque(false);
        scoreScrollPane.getViewport().setOpaque(false);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setOpaque(false);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        scorePanel.add(title);
        scorePanel.add(Box.createVerticalStrut(20));
        scorePanel.add(scoreScrollPane);
        scorePanel.add(Box.createVerticalStrut(20));
        scorePanel.add(backButton);

        backgroundPanel.add(scorePanel);
        setContentPane(backgroundPanel);

        setPreferredSize(new Dimension(600, 600));
        pack();
        setLocationRelativeTo(null);
    }

    private JButton createButton(String text, Color green) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(300, 50));
        button.setMaximumSize(new Dimension(300, 50));
        button.setForeground(green);
        button.setFont(new Font("Monospaced", Font.BOLD, 18));
        button.setBackground(Color.BLACK);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createLineBorder(green, 2));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setForeground(green);
                button.setBorder(BorderFactory.createLineBorder(green, 2));
            }
        });
        return button;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void handleClosing(Runnable action) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                action.run();
            }
        });
    }
}
