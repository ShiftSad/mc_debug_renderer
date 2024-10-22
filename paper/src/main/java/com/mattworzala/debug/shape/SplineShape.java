package com.mattworzala.debug.shape;

import com.google.common.io.ByteArrayDataOutput;
import com.mattworzala.debug.Layer;
import io.papermc.paper.math.Position;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public record SplineShape(
        @NotNull Type type,
        @NotNull List<Position> points,
        boolean loop,
        int color,
        @NotNull Layer layer,
        float lineWidth
) implements Shape {
    public enum Type {
        CATMULL_ROM,
        BEZIER,
    }

    @Override
    public int id() {
        return 1;
    }

    @Override
    public void write(@NotNull ByteArrayDataOutput out) {
        // Write the enum type (CATMULL_ROM or BEZIER)
        out.writeInt(type.ordinal());

        // Write the list of points
        out.writeInt(points.size()); // First, write the number of points
        for (Position point : points) {
            out.writeDouble(point.x());
            out.writeDouble(point.y());
            out.writeDouble(point.z());
        }

        // Write the remaining fields
        out.writeBoolean(loop);
        out.writeInt(color);
        out.writeInt(layer.ordinal());
        out.writeFloat(lineWidth);
    }

    public static class Builder {
        private Type type = Type.CATMULL_ROM;
        private final List<Position> points = new ArrayList<>();
        private boolean loop = false;
        private int color = 0xFFFFFFFF;
        private Layer layer = Layer.INLINE;
        private float lineWidth = 3.0f;

        public @NotNull Builder type(@NotNull Type type) {
            this.type = type;
            return this;
        }

        public @NotNull Builder position(@NotNull Position point) {
            this.points.add(point);
            return this;
        }

        public @NotNull Builder loop(boolean loop) {
            this.loop = loop;
            return this;
        }

        public @NotNull Builder color(int color) {
            this.color = color;
            return this;
        }

        public @NotNull Builder layer(@NotNull Layer layer) {
            this.layer = layer;
            return this;
        }

        public @NotNull Builder lineWidth(float lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }

        public @NotNull SplineShape build() {
            return new SplineShape(type, points, loop, color, layer, lineWidth);
        }
    }
}
