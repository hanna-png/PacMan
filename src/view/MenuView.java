package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class MenuView extends JFrame {
    private Map<String, JButton> buttons;

    public MenuView() {
      openMenuWindow();
    }


    private void openMenuWindow() {
        buttons = new HashMap<>();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Pacman");

        Color green = new Color(0, 255, 0);
        Image backgroundImage = new ImageIcon("resources/menu/background.jpg").getImage();
        BackgroundImagePanel backgroundPanel = new BackgroundImagePanel(backgroundImage);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setOpaque(false);

        JButton newGame = createButton("New Game", green);
        JButton highScores = createButton("High Scores", green);
        JButton exit = createButton("Exit", green);

        JLabel title = new JLabel("MENU");
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        title.setForeground(green);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        optionsPanel.add(Box.createVerticalStrut(20));
        optionsPanel.add(newGame);
        buttons.put("New Game", newGame);
        optionsPanel.add(Box.createVerticalStrut(20));
        optionsPanel.add(highScores);
        buttons.put("High Scores", highScores);
        optionsPanel.add(Box.createVerticalStrut(20));
        optionsPanel.add(exit);
        buttons.put("Exit", exit);
        optionsPanel.add(Box.createVerticalStrut(20));
        optionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel borderPanel = new JPanel();
        borderPanel.setLayout(new BoxLayout(borderPanel, BoxLayout.Y_AXIS));
        borderPanel.setBorder(BorderFactory.createLineBorder(green, 2));
        borderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        borderPanel.setMaximumSize(new Dimension(380, 280));
        borderPanel.setOpaque(false);
        borderPanel.add(optionsPanel);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setOpaque(false);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(borderPanel);

        backgroundPanel.add(mainPanel);
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

    public Map<String, JButton> getButtons() {
        return buttons;
    }

    static class BackgroundImagePanel extends JPanel {
        private final Image background;

        public BackgroundImagePanel(Image background) {
            this.background = background;
            setLayout(new GridBagLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}




