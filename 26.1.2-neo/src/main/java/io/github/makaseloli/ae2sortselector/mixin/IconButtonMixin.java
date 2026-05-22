package io.github.makaseloli.ae2sortselector.mixin;

import java.util.ArrayList;
import java.util.List;

import appeng.client.gui.widgets.IconButton;
import io.github.makaseloli.ae2sortselector.bridge.SortSelectorMenuBridge;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.Rect2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IconButton.class)
public abstract class IconButtonMixin {
    @Unique
    private boolean ae2sortselector$renderingExpandedMenu;

    @Inject(method = "getTooltipArea", remap = false, at = @At("HEAD"), cancellable = true)
    private void ae2sortselector$getExpandedTooltipArea(CallbackInfoReturnable<Rect2i> callbackInfo) {
        if (!((Object) this instanceof SortSelectorMenuBridge bridge) || !bridge.ae2sortselector$isMenuOpen()) {
            return;
        }
        int index = bridge.ae2sortselector$getHoveredIndex();
        List<? extends Enum<?>> allValues = bridge.ae2sortselector$getValues();
        if (index < 0 || index >= allValues.size()) {
            return;
        }
        List<? extends Enum<?>> options = this.ae2sortselector$getOptions(bridge);
        int visualIndex = options.indexOf(allValues.get(index));
        if (visualIndex < 0) {
            return;
        }
        AbstractWidget widget = (AbstractWidget) (Object) this;
        int x = widget.getX() - (options.size() - visualIndex) * SortSelectorMenuBridge.ae2sortselector$SLOT_WIDTH;
        callbackInfo.setReturnValue(new Rect2i(x, widget.getY(), widget.getWidth(), widget.getHeight()));
    }

    @Inject(method = "extractContents", at = @At("TAIL"))
    private void ae2sortselector$renderExpandedSortMenu(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
            float partialTick,
            CallbackInfo callbackInfo) {
        if (this.ae2sortselector$renderingExpandedMenu || !((Object) this instanceof SortSelectorMenuBridge bridge)
                || !bridge.ae2sortselector$isMenuOpen()) {
            return;
        }

        AbstractWidget widget = (AbstractWidget) (Object) this;
        List<? extends Enum<?>> values = this.ae2sortselector$getOptions(bridge);
        if (values.isEmpty()) {
            return;
        }

        int originalX = widget.getX();
        int originalY = widget.getY();
        AbstractWidgetAccessor accessor = (AbstractWidgetAccessor) widget;
        boolean originalHovered = accessor.ae2sortselector$isHovered();
        boolean originalFocused = widget.isFocused();
        int startX = originalX - values.size() * SortSelectorMenuBridge.ae2sortselector$SLOT_WIDTH;

        this.ae2sortselector$renderingExpandedMenu = true;
        try {
            bridge.ae2sortselector$setHoveredIndex(-1);
            for (int i = 0; i < values.size(); i++) {
                int x = startX + i * SortSelectorMenuBridge.ae2sortselector$SLOT_WIDTH;
                widget.setX(x);
                widget.setY(originalY);
                bridge.ae2sortselector$setDisplayValue(values.get(i));
                if (mouseX >= x && mouseX < x + widget.getWidth()
                        && mouseY >= originalY && mouseY < originalY + widget.getHeight()) {
                    bridge.ae2sortselector$setHoveredIndex(bridge.ae2sortselector$getValues().indexOf(values.get(i)));
                    accessor.ae2sortselector$setHovered(true);
                    widget.setFocused(true);
                } else {
                    accessor.ae2sortselector$setHovered(false);
                    widget.setFocused(false);
                }
                ((IconButton) (Object) this).extractContents(graphics, mouseX, mouseY, partialTick);
            }
        } finally {
            bridge.ae2sortselector$setDisplayValue(null);
            widget.setX(originalX);
            widget.setY(originalY);
            accessor.ae2sortselector$setHovered(originalHovered);
            widget.setFocused(originalFocused);
            this.ae2sortselector$renderingExpandedMenu = false;
        }
    }

    @Unique
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
