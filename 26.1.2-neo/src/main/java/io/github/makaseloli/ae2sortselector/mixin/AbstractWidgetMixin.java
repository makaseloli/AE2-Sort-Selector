package io.github.makaseloli.ae2sortselector.mixin;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import io.github.makaseloli.ae2sortselector.bridge.SortSelectorMenuBridge;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractWidget.class)
public abstract class AbstractWidgetMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void ae2sortselector$handleExpandedSortClick(MouseButtonEvent event, boolean doubleClick,
            CallbackInfoReturnable<Boolean> callbackInfo) {
        if (event.button() != 0) {
            return;
        }
        this.ae2sortselector$handleClick(event.x(), event.y(), callbackInfo);
    }

    private void ae2sortselector$handleClick(double mouseX, double mouseY,
            CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!((Object) this instanceof SortSelectorMenuBridge bridge)) {
            return;
        }

        AbstractWidget widget = (AbstractWidget) (Object) this;
        List<? extends Enum<?>> values = this.ae2sortselector$getOptions(bridge);
        if (values.isEmpty()) {
            return;
        }

        if (!bridge.ae2sortselector$isMenuOpen()) {
            if (!this.ae2sortselector$isInWidget(widget, mouseX, mouseY)) {
                return;
            }
            widget.playDownSound(Minecraft.getInstance().getSoundManager());
            bridge.ae2sortselector$setMenuOpen(true);
            callbackInfo.setReturnValue(true);
            return;
        }

        int index = this.ae2sortselector$slotIndex(widget, values.size(), mouseX, mouseY);
        if (index < 0 || index >= values.size()) {
            widget.playDownSound(Minecraft.getInstance().getSoundManager());
            bridge.ae2sortselector$setMenuOpen(false);
            callbackInfo.setReturnValue(true);
            return;
        }

        widget.playDownSound(Minecraft.getInstance().getSoundManager());
        bridge.ae2sortselector$choose(values.get(index));
        bridge.ae2sortselector$setMenuOpen(false);
        callbackInfo.setReturnValue(true);
    }

    private int ae2sortselector$slotIndex(AbstractWidget widget, int size, double mouseX, double mouseY) {
        if (mouseY < widget.getY() || mouseY >= widget.getY() + widget.getHeight()) {
            return -1;
        }
        int localX = (int) mouseX - this.ae2sortselector$menuStartX(widget, size);
        int index = localX / SortSelectorMenuBridge.ae2sortselector$SLOT_WIDTH;
        return localX < 0 ? -1 : index;
    }

    private int ae2sortselector$menuStartX(AbstractWidget widget, int size) {
        return widget.getX() - size * SortSelectorMenuBridge.ae2sortselector$SLOT_WIDTH;
    }

    private boolean ae2sortselector$isInWidget(AbstractWidget widget, double mouseX, double mouseY) {
        return mouseX >= widget.getX()
                && mouseY >= widget.getY()
                && mouseX < widget.getX() + widget.getWidth()
                && mouseY < widget.getY() + widget.getHeight();
    }

    private List<? extends Enum<?>> ae2sortselector$getOptions(SortSelectorMenuBridge bridge) {
        Enum<?> current = bridge.ae2sortselector$getCurrentValue();
        List<Enum<?>> options = new ArrayList<>();
        for (Enum<?> value : bridge.ae2sortselector$getValues()) {
            if (value != current) {
                options.add(value);
            }
        }
        return options;
    }
}
