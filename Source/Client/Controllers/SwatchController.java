package Client.Controllers;

import Client.Views.SwatchView;
import Framework.Colour;

import com.sun.javafx.scene.control.skin.CustomColorDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class SwatchController implements Controller {
    private final SwatchView swatchView;
    private final CustomColorDialog colorDialog;
    private final Consumer<Colour> colourSelectedHandler;


    public SwatchController(SwatchView swatchView, Consumer<Colour> colourSelectedHandler, Colour initialColour) {
        this.swatchView = swatchView;
        this.colourSelectedHandler = colourSelectedHandler;
        colorDialog = new CustomColorDialog(new Stage());
        colorDialog.setShowOpacitySlider(false);
        colorDialog.setShowUseBtn(false);

        swatchView.setColour(initialColour);
        colorDialog.setCurrentColor(Color.color(initialColour.getR(), initialColour.getG(), initialColour.getB()));
    }

    @Override
    public void start() {
        swatchView.setOnMouseClicked(this::onMouseClicked);
        colorDialog.setOnSave(this::onColourPicked);
    }

    @Override
    public void stop() {
        swatchView.setOnMouseClicked(null);
        colorDialog.setOnSave(null);
    }

    private void onMouseClicked(MouseEvent event) {
        colorDialog.show();
        colorDialog.getDialog().centerOnScreen();
    }

    private void onColourPicked() {
        Color colour = colorDialog.getCustomColor();
        Colour frameworkColour = new Colour(colour.getRed(), colour.getGreen(), colour.getBlue());
        swatchView.setColour(frameworkColour);
        colourSelectedHandler.accept(frameworkColour);
    }
}
