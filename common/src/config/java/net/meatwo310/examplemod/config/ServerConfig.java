package io.github.makaseloli.ae2sortselector.config;

import io.github.makaseloli.ae2sortselector.mdk.config.ConfigEntries;
import io.github.makaseloli.ae2sortselector.mdk.config.ConfigEntryBuilder;

public class ServerConfig {
    private static final ConfigEntryBuilder BUILDER = new ConfigEntryBuilder();

    public static final ConfigEntries ENTRIES = BUILDER.build();
}
