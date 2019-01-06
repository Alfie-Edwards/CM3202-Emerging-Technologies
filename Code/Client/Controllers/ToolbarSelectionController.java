package Client.Controllers;

import Client.Models.ToolbarItemModel;

import java.util.List;

public class ToolbarSelectionController implements Controller {

    private final List<ToolbarItemModel> toolbarItemModels;
    private ToolbarItemModel selectedItem;

    public ToolbarSelectionController(List<ToolbarItemModel> toolbarItemModels) {
        this.toolbarItemModels = toolbarItemModels;

    }

    @Override
    public void start() {
        for (ToolbarItemModel itemModel : toolbarItemModels)
            itemModel.addSelectedChangedListener(this::onSelectedChanged);
    }

    @Override
    public void stop() {
        for (ToolbarItemModel itemModel : toolbarItemModels)
            itemModel.removeSelectedChangedListener(this::onSelectedChanged);
    }

    // Events

    private void onSelectedChanged(boolean selected) {
        if (!selected) {
            selectedItem = null;
            return;
        }
        if (selectedItem != null)
            selectedItem.setSelected(false);

        for (ToolbarItemModel itemModel : toolbarItemModels) {
            if (itemModel.getSelected()) {
                selectedItem = itemModel;
                break;
            }
        }
    }
}
