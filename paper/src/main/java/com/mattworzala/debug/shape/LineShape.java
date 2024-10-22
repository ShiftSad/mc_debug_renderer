package com.mattworzala.debug.shape;

import com.google.common.io.ByteArrayDataOutput;
import com.mattworzala.debug.Layer;
import io.papermc.paper.math.Position;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A line connected with multiple points.
 *
 * @param points    The points the line should go through.
 * @param lineWidth The thickness of the line.
 * @param color     The color of the line, in ARGB format.
 * @param layer     The layer of the line.
 */
public record LineShape(
        @NotNull Type type,
        @NotNull List<Position> points,
        int color,
        @NotNull Layer layer,
        float lineWidth
) implements Shape {

    public enum Type {
        SINGLE,
        STRIP,
        LOOP
    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public void write(@NotNull ByteArrayDataOutput out) {
        // Write line type
        out.writeInt(type.ordinal());

        // Write points
        out.writeInt(points.size());  // First write the number of points
        for (Position point : points) {
            out.writeDouble(point.x());
            out.writeDouble(point.y());
            out.writeDouble(point.z());
        }

        // Write other properties: color, layer, and line width
        out.writeInt(color);
        out.writeInt(layer.ordinal());
        out.writeFloat(lineWidth);
    }

    public static class Builder {

        private Type type = Type.SINGLE;
        private final List<Position> points = new ArrayList<>();
        private float lineWidth = 4f;
        private int color = 0xFFFFFFFF;
        private Layer layer = Layer.INLINE;

        public @NotNull Builder type(@NotNull Type type) {
            this.type = type;
            return this;
        }

        /**
         * Adds a point to the line.
         * Lines will be rendered in order from the first point to the last.
         * There must be at least 2 points to render a line.
         *
         * @param point The point.
         * @return The builder.
         */
        public @NotNull Builder point(@NotNull Position point) {
            points.add(point);
            return this;
        }

        /**
         * The color of the line in ARGB format.
         * <p>
         * Defaults to pure white if not set.
         *
         * @param color The color.
         * @return The builder.
         */
        public @NotNull Builder color(int color) {
            this.color = color;
            return this;
        }

        /**
         * The {@link Layer} of the line.
         * <p>
         * Defaults to {@link Layer#INLINE} if not set.
         *
         * @param layer The layer.
         * @return The builder.
         */
        public @NotNull Builder layer(@NotNull Layer layer) {
            this.layer = layer;
            return this;
        }

        public @NotNull Builder lineWidth(float lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }

        /**
         * @return A {@link LineShape} constructed from the builder parameters.
         */
        public @NotNull LineShape build() {
            if (points.size() < 2) {
                throw new IllegalArgumentException("Line must have at least 2 points");
            }
            return new LineShape(type, points, color, layer, lineWidth);
        }

    }

}
