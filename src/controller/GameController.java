package controller;

import model.*;
import model.entities.Item;
import model.entities.Score;
import model.enums.Direction;
import view.GameView;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

public class GameController {
    private GameModel model;
    private GameView view;
    private Direction inputDirection = Direction.RIGHT;
    private ScoreModel scoreModel;
    private MovePacmanDispatcher dispatcher;

    public GameController(int width, int height) {
        model = new GameModel(width, height);
        view = new GameView(model);
        scoreModel = new ScoreModel();
        view.setVisible(true);
        dispatcher = new MovePacmanDispatcher();
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(dispatcher);
            changePacmanPosition();
            changeGhostsPosition();
            animatePacman();
            animateGhosts();
            startItemChecker();
            startScoreCounter();
            startTimeCounter();
            startLiveCounter();
            startEndGameChecker();


    }




    private void startEndGameChecker() {
        Thread thread = new Thread(() -> {
            while (!model.isEnd() && !model.isReturnedToMenu()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(view, "Exception in the startEndGameChecker Thread");
                    return;
                }
            }


           if (!model.isReturnedToMenu()){
               SwingUtilities.invokeLater(() -> {
               showInputNameDialog();
               });
           }
           if (model.isReturnedToMenu()){
               if (view.isDisplayable()){
                   SwingUtilities.invokeLater(() -> {
                       view.dispose();
                   });
               }
               SwingUtilities.invokeLater(() -> {
                   KeyboardFocusManager.getCurrentKeyboardFocusManager()
                           .removeKeyEventDispatcher(dispatcher);
               });
           }


        });

        thread.setDaemon(true);
        thread.start();
    }

    private void showInputNameDialog() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    view,
                    "Game Over! Your score is: " + model.getScore()
            );

            while (true) {
                String name = JOptionPane.showInputDialog(
                        view,
                        "Please enter your name:",
                        "Score Saving...",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (name == null) {
                    break;
                }

                name = name.trim();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            view,
                            "Name is empty. Please enter a name, or click Cancel (your result won't be saved).",
                            "Empty Name",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;


                }

                Score score = new Score(model.getScore(), name);
                if (!scoreModel.isDuplicateRecord(score)) {
                    scoreModel.saveScore(score);
                }

                break;
            }

            KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .removeKeyEventDispatcher(dispatcher);
            view.dispose();
            new MenuController();
        });
    }



    public void startScoreCounter() {
        Thread thread = new Thread(() -> {
            while (!model.isEnd()) {
                view.getLables().get("score").setText("Score: " + model.getScore());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(view, "Exception in the startScoreCounter Thread");
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void startLiveCounter() {
        Thread thread = new Thread(() -> {
            while (!model.isEnd()) {
                view.getLables().get("lives").setText("Lives: " + model.getLives());
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void startTimeCounter() {
        Thread thread = new Thread(() -> {
            while (!model.isEnd()) {
                view.getLables().get("time").setText("Time: " + model.getTime());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(view, "Exception in the startTimeCounter Thread");
                    break;
                }
            }


        });
        thread.setDaemon(true);
        thread.start();
    }


    public void changePacmanPosition() {
        Thread thread = new Thread(() -> {
            while (!model.isEnd()) {

                int prevR = model.getPacman().getCurrentR();
                int prevC = model.getPacman().getCurrentC();
                model.movePacman(inputDirection);
                int currR = model.getPacman().getCurrentR();
                int currC = model.getPacman().getCurrentC();


                SwingUtilities.invokeLater(() -> {
                    BoardTableModel table =
                            (BoardTableModel) view.getBoardTable().getModel();
                    table.fireTableCellUpdated(prevR, prevC);
                    table.fireTableCellUpdated(currR, currC);
                });


                try {
                    Thread.sleep(100L * model.getPacman().getSpeed());
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(view, "Exception in the changePacmanPosition Thread");
                    break;
                }
            }

        });

        thread.setDaemon(true);
        thread.start();
    }

    public void changeGhostsPosition() {
        int[] prevGenerationTimes = new int[]{-1};
        for (var ghost : model.getGhosts()) {
            Thread thread = new Thread(() -> {
                while (!model.isEnd()) {

                    if (!model.isGhostsFreezed()) {
                        int prevR = ghost.getCurrentR();
                        int prevC = ghost.getCurrentC();
                        model.moveGhost(ghost);
                        int currR = ghost.getCurrentR();
                        int currC = ghost.getCurrentC();


                        if (model.getTime() % 5 == 0 && model.getTime() != prevGenerationTimes[0]) {
                            model.generateItem(prevR, prevC);
                            prevGenerationTimes[0] = getModel().getTime();
                            SwingUtilities.invokeLater(() -> {
                                BoardTableModel table =
                                        (BoardTableModel) view.getBoardTable().getModel();
                                table.fireTableCellUpdated(prevR, prevC);
                            });
                        }

                        SwingUtilities.invokeLater(() -> {
                            BoardTableModel table =
                                    (BoardTableModel) view.getBoardTable().getModel();
                            table.fireTableCellUpdated(prevR, prevC);
                            table.fireTableCellUpdated(currR, currC);
                        });


                        try {
                            Thread.sleep(100L * ghost.getSpeed());
                        } catch (InterruptedException e) {
                            JOptionPane.showMessageDialog(view, "Exception in the changeGhostsPosition Thread");
                            break;
                        }
                    }


                }


            });

            thread.setDaemon(true);
            thread.start();
        }

    }


    public void animatePacman() {
        Thread thread = new Thread(() -> {
            while (!model.isEnd()) {
                model.getPacman().changeFrame();
                SwingUtilities.invokeLater(() -> {
                    BoardTableModel table =
                            (BoardTableModel) view.getBoardTable().getModel();
                    table.fireTableCellUpdated(model.getPacman().getCurrentR(), model.getPacman().getCurrentC());
                });


                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(view, "Exception in the animatePacman Thread");
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void animateGhosts() {

        for (var ghost : model.getGhosts()) {
            Thread thread = new Thread(() -> {
                while (!model.isEnd()) {
                    int currR = ghost.getCurrentR();
                    int currC = ghost.getCurrentC();
                    ghost.changeFrame();

                    SwingUtilities.invokeLater(() -> {
                        BoardTableModel table =
                                (BoardTableModel) view.getBoardTable().getModel();
                        table.fireTableCellUpdated(currR, currC);
                    });


                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(view, "Exception in the animateGhosts Thread");
                        break;
                    }

                }


            });

            thread.setDaemon(true);
            thread.start();
        }

    }

    public void startItemChecker() {
        Thread thread = new Thread(() -> {
            while (!model.isEnd()) {
                Iterator<Item> it = model.getItems().iterator();
                while (it.hasNext()) {
                    Item i = it.next();
                    if (model.getPacman().getCurrentC() == i.getCol() &&
                            model.getPacman().getCurrentR() == i.getRow()) {

                        switch (i.getType()) {
                            case LIVE:
                                model.incrementLives(1);
                                break;
                            case FREEZE:
                                model.freeze();
                                break;
                            case PACMAN_SPEED:
                                model.speedPacman();
                                break;
                            case GHOSTS_SLOW:
                                model.slowGhosts();
                                break;
                            case SCORE:
                                model.incrementScore(10);
                                break;
                        }

                        int itemRow = i.getRow();
                        int itemCol = i.getCol();
                        it.remove();

                        SwingUtilities.invokeLater(() -> {
                            BoardTableModel table =
                                    (BoardTableModel) view.getBoardTable().getModel();
                            table.fireTableCellUpdated(itemRow, itemCol);
                        });

                        break;
                    }
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(view, "Exception in the startItemChecker Thread");
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }


    public GameModel getModel() {
        return model;
    }

    public void setModel(GameModel model) {
        this.model = model;
    }

    public GameView getView() {
        return view;
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public Direction getInputDirection() {
        return inputDirection;
    }

    public void setInputDirection(Direction inputDirection) {
        this.inputDirection = inputDirection;
    }


    private class MovePacmanDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (model.isEnd()) {
                return false;
            }
            if (e.getID() != KeyEvent.KEY_PRESSED) {
                return false;
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    inputDirection = Direction.LEFT;
                    return true;
                case KeyEvent.VK_RIGHT:
                    inputDirection = Direction.RIGHT;
                    return true;
                case KeyEvent.VK_UP:
                    inputDirection = Direction.UP;
                    return true;
                case KeyEvent.VK_DOWN:
                    inputDirection = Direction.DOWN;
                    return true;
                default:
                    return false;
            }
        }
    }


}
