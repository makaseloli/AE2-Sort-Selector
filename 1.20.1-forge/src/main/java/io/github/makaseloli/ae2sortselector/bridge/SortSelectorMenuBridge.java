package io.github.makaseloli.ae2sortselector.bridge;

import java.util.List;

public interface SortSelectorMenuBridge {
    int ae2sortselector$SLOT_WIDTH = 18;

    void ae2sortselector$choose(Enum<?> value);

    List<? extends Enum<?>> ae2sortselector$getValues();

    void ae2sortselector$setDisplayValue(Enum<?> value);

    boolean ae2sortselector$isMenuOpen();

    void ae2sortselector$setMenuOpen(boolean open);

    void ae2sortselector$setHoveredIndex(int index);

    int ae2sortselector$getHoveredIndex();

    Enum<?> ae2sortselector$getCurrentValue();
}
