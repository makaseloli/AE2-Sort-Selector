package io.github.makaseloli.ae2sortselector.mixin;

import java.util.ArrayList;
import java.util.List;

import appeng.client.gui.widgets.SettingToggleButton;
import io.github.makaseloli.ae2sortselector.bridge.SortSelectorMenuBridge;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SettingToggleButton.class)
public abstract class SettingToggleButtonMixin<T extends Enum<T>> implements SortSelectorMenuBridge {
    @Shadow(remap = false)
    public abstract T getCurrentValue();

    @Shadow(remap = false)
    private T currentValue;

    @Unique
    private T ae2sortselector$forcedNextValue;

    @Unique
    private T ae2sortselector$displayValue;

    @Unique
    private T ae2sortselector$savedCurrentValue;

    @Unique
    private boolean ae2sortselector$menuOpen;

    @Unique
    private int ae2sortselector$hoveredIndex = -1;

    @Override
    @SuppressWarnings("unchecked")
    public void ae2sortselector$choose(Enum<?> value) {
        this.ae2sortselector$forcedNextValue = (T) value;
        try {
            ((Button) (Object) this).onPress();
        } finally {
            this.ae2sortselector$forcedNextValue = null;
        }
    }

    @Override
    public List<? extends Enum<?>> ae2sortselector$getValues() {
        T current = this.ae2sortselector$getCurrentSettingValue();
        if (current == null) {
            return List.of();
        }
        T[] constants = current.getDeclaringClass().getEnumConstants();
        if (constants == null || constants.length <= 1) {
            return List.of();
        }
        List<T> values = new ArrayList<>(List.of(constants));
        values.sort(Enum::compareTo);
        return values;
    }

    @Unique
    private T ae2sortselector$getCurrentSettingValue() {
        return this.ae2sortselector$savedCurrentValue != null ? this.ae2sortselector$savedCurrentValue : this.currentValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void ae2sortselector$setDisplayValue(Enum<?> value) {
        this.ae2sortselector$displayValue = (T) value;
        if (value == null) {
            if (this.ae2sortselector$savedCurrentValue != null) {
                this.currentValue = this.ae2sortselector$savedCurrentValue;
                this.ae2sortselector$savedCurrentValue = null;
            }
        } else if (this.ae2sortselector$savedCurrentValue == null) {
            this.ae2sortselector$savedCurrentValue = this.currentValue;
            this.currentValue = (T) value;
        } else {
            this.currentValue = (T) value;
        }
    }

    @Override
    public boolean ae2sortselector$isMenuOpen() {
        return this.ae2sortselector$menuOpen;
    }

    @Override
    public void ae2sortselector$setMenuOpen(boolean open) {
        this.ae2sortselector$menuOpen = open;
        this.ae2sortselector$hoveredIndex = -1;
        if (!open) {
            this.ae2sortselector$setDisplayValue(null);
        }
    }

    @Override
    public void ae2sortselector$setHoveredIndex(int index) {
        this.ae2sortselector$hoveredIndex = index;
    }

    @Override
    public int ae2sortselector$getHoveredIndex() {
        return this.ae2sortselector$hoveredIndex;
    }

    @Override
    public Enum<?> ae2sortselector$getCurrentValue() {
        return this.ae2sortselector$getCurrentSettingValue();
    }

    @Inject(method = "getNextValue", remap = false, at = @At("HEAD"), cancellable = true)
    private void ae2sortselector$getNextValue(boolean backwards, CallbackInfoReturnable<T> callbackInfo) {
        if (this.ae2sortselector$forcedNextValue != null) {
            callbackInfo.setReturnValue(this.ae2sortselector$forcedNextValue);
        }
    }

    @Inject(method = "getCurrentValue", remap = false, at = @At("HEAD"), cancellable = true)
    private void ae2sortselector$getCurrentValue(CallbackInfoReturnable<T> callbackInfo) {
        if (this.ae2sortselector$displayValue != null) {
            callbackInfo.setReturnValue(this.ae2sortselector$displayValue);
        }
    }

    @Inject(method = "getTooltipMessage", remap = false, at = @At("HEAD"), cancellable = true)
    private void ae2sortselector$getTooltipMessage(CallbackInfoReturnable<List<Component>> callbackInfo) {
        if (this.ae2sortselector$hoveredIndex < 0) {
            return;
        }
        List<? extends Enum<?>> values = this.ae2sortselector$getValues();
        if (this.ae2sortselector$hoveredIndex >= values.size()) {
            return;
        }

        T previous = this.ae2sortselector$displayValue;
        int previousHoveredIndex = this.ae2sortselector$hoveredIndex;
        this.ae2sortselector$setDisplayValue(values.get(this.ae2sortselector$hoveredIndex));
        try {
            this.ae2sortselector$hoveredIndex = -1;
            callbackInfo.setReturnValue(((SettingToggleButton<T>) (Object) this).getTooltipMessage());
        } finally {
            this.ae2sortselector$setDisplayValue(previous);
            this.ae2sortselector$hoveredIndex = previousHoveredIndex;
        }
    }
}
