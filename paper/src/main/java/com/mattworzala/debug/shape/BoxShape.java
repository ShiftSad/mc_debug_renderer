package com.mattworzala.debug.shape;

import com.google.common.io.ByteArrayDataOutput;
import com.mattworzala.debug.Layer;
import io.papermc.paper.math.Position;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public record BoxShape(
        Position start,
        Position end,
        int faceColor,
        Layer faceLayer,
        int edgeColor,
        Layer edgeLayer,
        float edgeWidth
) implements Shape {

    @Override
    public int id() {
        return 3;
    }

    @Override
    public void write(@NotNull ByteArrayDataOutput out) {
        // Write the coordinates for both start and end positions
        out.writeDouble(start.x());
        out.writeDouble(start.y());
        out.writeDouble(start.z());
        out.writeDouble(end.x());
        out.writeDouble(end.y());
        out.writeDouble(end.z());

        // Write face color, layer, edge color, edge layer, edge width
        out.writeInt(faceColor);
        out.writeInt(faceLayer.ordinal());
        out.writeInt(edgeColor);
        out.writeInt(edgeLayer.ordinal());
        out.writeFloat(edgeWidth);
    }

    public static class Builder {

        private Position start;
        private Position end;
        private int faceColor = 0xFFFFFFFF;
        private Layer faceLayer = Layer.INLINE;
        private int edgeColor = 0xFFFFFFFF;
        private Layer edgeLayer = Layer.INLINE;
        private float edgeWidth = 4f;

        /**
         * The starting corner of the box. Must be set.
         *
         * @param start The position.
         * @return The builder.
         */
        public @NotNull Builder start(@NotNull Position start) {
            this.start = start;
            return this;
        }

        /**
         * The ending corner of the box. Must be set.
         *
         * @param end The position.
         * @return The builder.
         */
        public @NotNull Builder end(@NotNull Position end) {
            this.end = end;
            return this;
        }

        public @NotNull Builder faceColor(int color) {
            this.faceColor = color;
            return this;
        }

        /**
         * The {@link Layer} of the box.
         * <p>
         * Defaults to {@link Layer#INLINE} if not set.
         *
         * @param layer The layer.
         * @return The builder.
         */
        public @NotNull Builder faceLayer(@NotNull Layer layer) {
            this.faceLayer = layer;
            return this;
        }

        public @NotNull Builder edgeColor(int color) {
            this.edgeColor = color;
            return this;
        }

        public @NotNull Builder edgeLayer(@NotNull Layer layer) {
            this.edgeLayer = layer;
            return this;
        }

        public @NotNull Builder edgeWidth(float width) {
            this.edgeWidth = width;
            return this;
        }

        public @NotNull Shape build() {
            if (start == null) throw new IllegalArgumentException("start cannot be null");
            if (end == null) throw new IllegalArgumentException("end cannot be null");
            return new BoxShape(start, end, faceColor, faceLayer, edgeColor, edgeLayer, edgeWidth);
        }
    }

}
