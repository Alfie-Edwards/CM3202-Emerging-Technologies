package Client.Views;

import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class ToolbarItemView extends Pane {

    private static final String NON_HOVER_BACKGROUND_COLOUR = "#0000";
    private static final String HOVER_BACKGROUND_COLOUR = "#0004";
    private static final String SELECTED_BACKGROUND_COLOUR = "#fff2";
    private static final Color ICON_COLOUR = Color.gray(0.6);
    private static final Color SELECTED_ICON_COLOUR = Color.gray(0.8);

    private final Shape icon;
    private boolean selected;
    private String nonSelectedBackgroundColour;

    public ToolbarItemView(Shape icon, String name, double width, double height) {
        super();
        this.icon = icon;
        selected = false;
        nonSelectedBackgroundColour = NON_HOVER_BACKGROUND_COLOUR;

        this.getChildren().add(icon);
        Tooltip.install(this, new Tooltip(name));

        setOnMouseEntered(this::onMouseEntered);
        setOnMouseExited(this::onMouseExited);

        setMinSize(width, height);
        updateColours();
    }

    // Setters

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateColours();
    }

    // Events

    private void onMouseEntered(MouseEvent event) {
        nonSelectedBackgroundColour = HOVER_BACKGROUND_COLOUR;
        updateColours();
    }

    private void onMouseExited(MouseEvent event) {
        nonSelectedBackgroundColour = NON_HOVER_BACKGROUND_COLOUR;
        updateColours();
    }

    private void updateColours() {
        if (this.selected) {
            setStyle("-fx-background-color: " + SELECTED_BACKGROUND_COLOUR);
            this.icon.setFill(SELECTED_ICON_COLOUR);
        }
        else {
            setStyle("-fx-background-color: " + nonSelectedBackgroundColour);
            this.icon.setFill(ICON_COLOUR);
        }
    }
}
