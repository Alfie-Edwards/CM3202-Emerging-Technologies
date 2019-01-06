package Client.Models;

import Framework.GenericEvent;

import java.util.function.Consumer;

public class ToolbarItemModel {

    private final GenericEvent<Boolean> selectedChangedEvent;
    private boolean selected;

    public ToolbarItemModel() {
        selectedChangedEvent = new GenericEvent<>();
        selected = false;
    }

    // Getters and Setters

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (this.selected == selected) return;
        this.selected = selected;
        onSelctedChanged();
    }

    // Events

    public void addSelectedChangedListener(Consumer<Boolean> listener) {
        selectedChangedEvent.addListener(listener);
    }

    public void removeSelectedChangedListener(Consumer<Boolean> listener) {
        selectedChangedEvent.addListener(listener);
    }

    private void onSelctedChanged() {
        selectedChangedEvent.fire(selected);
    }
}
