package Client.Controllers;

import Client.Models.ClientModel;
import Client.Models.UserServant;
import Client.Views.CanvasView;

import javafx.scene.input.MouseEvent;

public class CursorTrackingController implements Controller {

    private final ClientModel clientModel;
    private final CanvasEventRouter canvasEventRouter;
    private final CanvasView canvasView;

    public CursorTrackingController(ClientModel clientModel, CanvasEventRouter canvasEventRouter, CanvasView canvasView) {
        this.clientModel = clientModel;
        this.canvasEventRouter = canvasEventRouter;
        this.canvasView = canvasView;
    }

    @Override
    public void start() {
        canvasEventRouter.addMouseMovedListener(this::onMouseMoved);
        canvasEventRouter.addMouseExitedListener(this::onMouseExited);
    }

    @Override
    public void stop() {
        canvasEventRouter.removeMouseMovedListener(this::onMouseMoved);
        canvasEventRouter.removeMouseExitedListener(this::onMouseExited);
        UserServant currentUser = clientModel.getCurrentUser();
        currentUser.setCursorX(-1);
        currentUser.setCursorY(-1);
    }

    // Events

    private void onMouseMoved(MouseEvent event) {
        double x = event.getX() / canvasView.getWidth();
        double y = event.getY() / canvasView.getHeight();
        UserServant currentUser = clientModel.getCurrentUser();
        currentUser.setCursorX(x);
        currentUser.setCursorY(y);
    }

    private void onMouseExited(MouseEvent event) {
        UserServant currentUser = clientModel.getCurrentUser();
        currentUser.setCursorX(-1);
        currentUser.setCursorY(-1);
    }
}
