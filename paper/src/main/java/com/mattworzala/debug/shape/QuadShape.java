package com.mattworzala.debug.shape;

import com.google.common.io.ByteArrayDataOutput;
import com.mattworzala.debug.Layer;
import io.papermc.paper.math.Position;
import org.jetbrains.annotations.NotNull;

public record QuadShape(
        @NotNull Position a,
        @NotNull Position b,
        @NotNull Position c,
        @NotNull Position d,
        int color,
        @NotNull Layer renderLayer
) implements Shape {

    @Override
    public int id() {
        return 2;
    }

    @Override
    public void write(@NotNull ByteArrayDataOutput out) {
        // Write positions of points a, b, c, and d
        out.writeDouble(a.x());
        out.writeDouble(a.y());
        out.writeDouble(a.z());

        out.writeDouble(b.x());
        out.writeDouble(b.y());
        out.writeDouble(b.z());

        out.writeDouble(c.x());
        out.writeDouble(c.y());
        out.writeDouble(c.z());

        out.writeDouble(d.x());
        out.writeDouble(d.y());
        out.writeDouble(d.z());

        // Write color and render layer
        out.writeInt(color);
        out.writeInt(renderLayer.ordinal());
    }

    public static class Builder {
        private Position a;
        private Position b;
        private Position c;
        private Position d;
        private int color = 0xFFFFFFFF;
        private Layer renderLayer = Layer.INLINE;

        public @NotNull Builder a(@NotNull Position a) {
            this.a = a;
            return this;
        }

        public @NotNull Builder b(@NotNull Position b) {
            this.b = b;
            return this;
        }

        public @NotNull Builder c(@NotNull Position c) {
            this.c = c;
            return this;
        }

        public @NotNull Builder d(@NotNull Position d) {
            this.d = d;
            return this;
        }

        public @NotNull Builder color(int color) {
            this.color = color;
            return this;
        }

        public @NotNull Builder renderLayer(@NotNull Layer renderLayer) {
            this.renderLayer = renderLayer;
            return this;
        }

        public @NotNull QuadShape build() {
            if (a == null || b == null || c == null || d == null) {
                throw new IllegalArgumentException("Points a, b, c, and d must not be null");
            }
            return new QuadShape(a, b, c, d, color, renderLayer);
        }
    }
}
