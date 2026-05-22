package io.github.makaseloli.ae2sortselector.mdk.config;

interface ConfigElement {
    void bindTo(ConfigVisitor visitor);
}
