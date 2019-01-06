package Client.Controllers;

import Client.Models.ClientModel;
import Client.Views.CanvasView;

import javafx.animation.AnimationTimer;

public class CanvasController implements Controller {

    private final ClientModel clientModel;
    private final CanvasView canvasView;
    private final AnimationTimer animationTimer;

    public CanvasController(ClientModel clientModel, CanvasView canvasView) {
        this.clientModel = clientModel;
        this.canvasView = canvasView;
        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                canvasView.redraw();
            }
        };
    }

    @Override
    public void start() {
        canvasView.updateShapes(clientModel.getShapes());
        canvasView.updateUsers(clientModel.getUsers());
        canvasView.updateSelectedShape(clientModel.getCurrentUser().getSelectedShape());
        clientModel.addShapesChangedListener(canvasView::updateShapes);
        clientModel.addUsersChangedListener(canvasView::updateUsers);
        clientModel.addCurrentUserSelectedShapeChangedListener(canvasView::updateSelectedShape);
        animationTimer.start();
    }

    @Override
    public void stop() {
        clientModel.removeShapesChangedListener(canvasView::updateShapes);
        clientModel.removeUsersChangedListener(canvasView::updateUsers);
        clientModel.addCurrentUserSelectedShapeChangedListener(canvasView::updateSelectedShape);
        animationTimer.stop();
    }
}
