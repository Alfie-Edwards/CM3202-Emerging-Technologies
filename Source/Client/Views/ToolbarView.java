package Client.Views;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ToolbarView extends VBox {
    private static final String BACKGROUND_COLOUR = "#fff2";

    public ToolbarView() {
        setSpacing(0);
        setStyle("-fx-background-color: " + BACKGROUND_COLOUR);
    }

    // Getters and Setters

    public void addItem(Node item) {
        getChildren().add(item);
    }

    public void removeItem(ToolbarItemView item) {
        getChildren().remove(item);
    }
}
