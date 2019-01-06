package Client.Controllers;

import Client.Models.ToolbarItemModel;
import Client.Views.ToolbarItemView;

import javafx.scene.input.MouseEvent;

public class ToolbarItemController implements Controller {
    private final ToolbarItemModel itemModel;
    private final ToolbarItemView itemView;
    private Runnable buttonAction;

    public ToolbarItemController(ToolbarItemModel itemModel, ToolbarItemView itemView, Runnable buttonAction) {
        this.itemModel = itemModel;
        this.itemView = itemView;
        this.buttonAction = buttonAction;
    }

    @Override
    public void start() {
        itemModel.addSelectedChangedListener(this::onSelectedChanged);
        itemView.setOnMouseClicked(this::onMouseClicked);
    }

    @Override
    public void stop() {
        itemModel.removeSelectedChangedListener(this::onSelectedChanged);
        itemView.setOnMouseClicked(null);
    }

    // Getters

    public ToolbarItemModel getItemModel() {
        return itemModel;
    }

    public ToolbarItemView getItemView() {
        return itemView;
    }

    // Events

    private void onMouseClicked(MouseEvent event) {
        itemModel.setSelected(true);
    }

    private void onSelectedChanged(boolean selected) {
        itemView.setSelected(selected);
        if (selected)
            buttonAction.run();
    }
}
