package Client.Views;

import Framework.Colour;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class SwatchView extends Pane {

    private static final Color BORDER_COLOUR = Color.gray(1, 0.8);
    private static final double SWATCH_EDGE_RADIUS = 8;

    private Colour colour;
    private Rectangle swatch;

    public SwatchView(double width, double height, double swatchSize) {
        setMinSize(width, height);

        double x = (width - swatchSize) / 2;
        double y = (height - swatchSize) / 2;

        swatch = new Rectangle(x, y, swatchSize, swatchSize);
        swatch.setArcHeight(SWATCH_EDGE_RADIUS);
        swatch.setArcWidth(SWATCH_EDGE_RADIUS);
        swatch.setStroke(BORDER_COLOUR);
        swatch.setStrokeWidth(2);

        this.getChildren().add(swatch);
    }

    public void setColour(Colour colour) {
        this.colour = colour;
        swatch.setFill(Color.color(colour.getR(), colour.getG(), colour.getB()));
    }

    public Colour getColour() {
        return colour;
    }
}
