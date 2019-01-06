package Client.Views;

import javafx.scene.layout.BorderPane;

public class ApplicationView extends BorderPane {

    private static final String BACKGROUND_COLOUR = "#222";

    private final ToolbarView toolbarView;
    private final CanvasView canvasView;

    public ApplicationView(CanvasView canvasView, ToolbarView toolbarView) {
        this.canvasView = canvasView;
        this.toolbarView = toolbarView;

        setStyle("-fx-background-color: " + BACKGROUND_COLOUR);
        leftProperty().setValue(toolbarView);
        centerProperty().setValue(canvasView);

        this.heightProperty().addListener(this::onWindowSizeChanged);
        this.widthProperty().addListener(this::onWindowSizeChanged);
    }

    // Getters

    public CanvasView getCanvasView() {
        return canvasView;
    }

    public ToolbarView getToolbarView() {
        return toolbarView;
    }

    // Events

    public void onWindowSizeChanged(Object event) {
        double toolbarWidth = toolbarView.getWidth();
        double height = heightProperty().getValue();
        double width = widthProperty().getValue();
        double canvasDimensions = Math.min(width - toolbarWidth, height);

        canvasView.widthProperty().setValue(canvasDimensions);
        canvasView.heightProperty().setValue(canvasDimensions);
    }


}
