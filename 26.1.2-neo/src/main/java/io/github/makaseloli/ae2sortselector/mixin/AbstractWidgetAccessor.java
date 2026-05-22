package io.github.makaseloli.ae2sortselector.mixin;

import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractWidget.class)
public interface AbstractWidgetAccessor {
    @Accessor("isHovered")
    boolean ae2sortselector$isHovered();

    @Accessor("isHovered")
    void ae2sortselector$setHovered(boolean hovered);
}
