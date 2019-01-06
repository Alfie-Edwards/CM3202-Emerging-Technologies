package Client;

import Client.Controllers.*;
import Client.Models.ClientModel;
import Client.Models.ToolbarItemModel;
import Client.Views.*;
import Client.Views.Geometry.ShapeService;
import Framework.ShapeTypes;

import javafx.scene.shape.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ApplicationBuilder {

    private static final double TOOLBAR_WIDTH = 48;
    private static final double TOOLBAR_ICON_RADIUS = 10;
    private static final double TOOLBAR_MOVE_ICON_SIZE = 20;
    private static final double TOOLBAR_BUTTON_HEIGHT = TOOLBAR_WIDTH;
    private static final double SWATCH_SIZE = 30;

    public static ApplicationView buildApplication(ClientModel clientModel) {

        // Create Views
        CanvasView canvasView = new CanvasView();
        ToolbarView toolbarView = new ToolbarView();
        ApplicationView applicationView = new ApplicationView(canvasView, toolbarView);
        SwatchView swatchView = new SwatchView(TOOLBAR_WIDTH, TOOLBAR_BUTTON_HEIGHT, SWATCH_SIZE);

        // Create Canvas Controllers
        CanvasEventRouter canvasEventRouter = new CanvasEventRouter(canvasView);
        DrawingController drawingController = new DrawingController(clientModel, canvasEventRouter, canvasView);
        CanvasController canvasController = new CanvasController(clientModel, canvasView);
        CursorTrackingController cursorTrackingController = new CursorTrackingController(clientModel, canvasEventRouter, canvasView);
        SwatchController swatchController = new SwatchController(swatchView, clientModel.getCurrentUser()::setColour, clientModel.getCurrentUser().getColour());
        SelectionController selectionController = new SelectionController(clientModel, canvasEventRouter, canvasView);

        // Create Toolbar
        List<ToolbarItemController> toolbarItemControllers = createToolbarItemForEachShape(getShapeToolbarButtonCallback(drawingController, selectionController));

        // Create Move Toolbar Item
        ToolbarItemController movementItemController = createToolbarItem(
                ShapeService.CreateCursor(
                        (TOOLBAR_WIDTH - TOOLBAR_MOVE_ICON_SIZE / Math.sqrt(2)) / 2,
                        (TOOLBAR_BUTTON_HEIGHT - TOOLBAR_MOVE_ICON_SIZE) / 2,
                        TOOLBAR_MOVE_ICON_SIZE),
                "Move",
                getMoveToolbarButtonCallback(drawingController, selectionController));
        toolbarItemControllers.add(0, movementItemController);

        List<ToolbarItemModel> toolbarItemModels = new ArrayList<>();
        for (ToolbarItemController itemController : toolbarItemControllers) {
            toolbarItemModels.add(itemController.getItemModel()); // Populate model list
            toolbarView.addItem(itemController.getItemView()); // Add view to toolbar view
        }
        toolbarView.addItem(swatchView);

        ToolbarSelectionController toolbarSelectionController = new ToolbarSelectionController(toolbarItemModels);

        // Activate Controllers
        canvasEventRouter.start();
        canvasController.start();
        cursorTrackingController.start();
        toolbarSelectionController.start();
        swatchController.start();
        selectionController.start();
        for (ToolbarItemController itemController : toolbarItemControllers)
            itemController.start();

        // Return top-level view
        return applicationView;
    }



    // Toolbar Creation

    private static List<ToolbarItemController> createToolbarItemForEachShape(Consumer<ShapeTypes> onClick) {

        List<ToolbarItemController> toolbarItemControllers = new ArrayList<>();

        for (ShapeTypes type : ShapeTypes.values() ) {
            toolbarItemControllers.add(
                    createToolbarItem(
                            ShapeService.CreatePolygon(
                                    type.numSides(),
                                    TOOLBAR_WIDTH / 2,
                                    TOOLBAR_BUTTON_HEIGHT / 2,
                                    TOOLBAR_ICON_RADIUS),
                            type.name(),
                            () -> onClick.accept(type)));
        }
        return toolbarItemControllers;
    }

    private static ToolbarItemController createToolbarItem(Shape icon, String name, Runnable buttonAction) {
        ToolbarItemModel itemModel = new ToolbarItemModel();
        ToolbarItemView itemView = new ToolbarItemView(icon, name, TOOLBAR_WIDTH, TOOLBAR_BUTTON_HEIGHT);
        return new ToolbarItemController(itemModel, itemView, buttonAction);
    }

    private static Consumer<ShapeTypes> getShapeToolbarButtonCallback(DrawingController drawingController, SelectionController selectionController) {
        return (shapeTypes -> {
            drawingController.setSelectedShapeType(shapeTypes);
            drawingController.start();
            selectionController.setMoveMode(false);
        });
    }

    private static Runnable getMoveToolbarButtonCallback(DrawingController drawingController, SelectionController selectionController) {
        return () -> {
            drawingController.stop();
            selectionController.setMoveMode(true);
        };
    }
}
