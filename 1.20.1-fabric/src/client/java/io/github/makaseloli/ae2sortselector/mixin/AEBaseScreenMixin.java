package io.github.makaseloli.ae2sortselector.mixin;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import appeng.client.gui.AEBaseScreen;
import io.github.makaseloli.ae2sortselector.bridge.SortSelectorMenuBridge;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AEBaseScreen.class)
public abstract class AEBaseScreenMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void ae2sortselector$handleExpandedSortClick(double mouseX, double mouseY, int button,
            CallbackInfoReturnable<Boolean> callbackInfo) {
        if (button != 0) {
            return;
        }
        for (GuiEventListener child : ((Screen) (Object) this).children()) {
            if (this.ae2sortselector$handleChild(child, mouseX, mouseY)) {
                callbackInfo.setReturnValue(true);
                return;
            }
        }
    }

    private boolean ae2sortselector$handleChild(GuiEventListener child, double mouseX, double mouseY) {
        if (!(child instanceof AbstractWidget widget)
                || !(child instanceof SortSelectorMenuBridge bridge)
                || !bridge.ae2sortselector$isMenuOpen()) {
            return false;
        }

        List<? extends Enum<?>> values = this.ae2sortselector$getOptions(bridge);
        int index = this.ae2sortselector$slotIndex(widget, values.size(), mouseX, mouseY);
        if (index < 0 || index >= values.size()) {
            return false;
        }

        widget.playDownSound(Minecraft.getInstance().getSoundManager());
        bridge.ae2sortselector$choose(values.get(index));
        bridge.ae2sortselector$setMenuOpen(false);
        return true;
    }

    private int ae2sortselector$slotIndex(AbstractWidget widget, int size, double mouseX, double mouseY) {
        if (mouseY < widget.getY() || mouseY >= widget.getY() + widget.getHeight()) {
            return -1;
        }
        int startX = widget.getX() - size * SortSelectorMenuBridge.ae2sortselector$SLOT_WIDTH;
        int localX = (int) mouseX - startX;
        int index = localX / SortSelectorMenuBridge.ae2sortselector$SLOT_WIDTH;
        return localX < 0 ? -1 : index;
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
