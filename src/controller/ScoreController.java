package controller;

import model.ScoreModel;
import view.ScoresView;

public class ScoreController {
    private final ScoreModel model;
    private final ScoresView view;

    public ScoreController() {
        model = new ScoreModel();
        view = new ScoresView(model);

        view.getBackButton().addActionListener(e -> {
            view.dispose();
            new MenuController();
        });

        view.handleClosing(MenuController::new);

        view.setVisible(true);
    }
}
