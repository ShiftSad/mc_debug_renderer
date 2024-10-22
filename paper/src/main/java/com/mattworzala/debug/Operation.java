package com.mattworzala.debug;

import com.google.common.io.ByteArrayDataOutput;
import com.mattworzala.debug.shape.Shape;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface Operation {

    void write(@NotNull ByteArrayDataOutput output);

    record Set(
            NamespacedKey id,
            Shape shape
    ) implements Operation {
        private static final int ID = 0;

        @Override
        public void write(@NotNull ByteArrayDataOutput output) {
            output.writeInt(ID);
            output.writeUTF(id.toString());
            output.writeInt(shape.id());
            shape.write(output);
        }
    }

    record Remove(
            @NotNull NamespacedKey id
    ) implements Operation {
        private static final int ID = 1;

        @Override
        public void write(@NotNull ByteArrayDataOutput output) {
            output.writeInt(ID);
            output.writeUTF(id.toString());
        }
    }

    record ClearNS(
            @NotNull String namespace
    ) implements Operation {
        private static final int ID = 2;

        @Override
        public void write(@NotNull ByteArrayDataOutput output) {
            output.writeInt(ID);
            output.writeUTF(namespace);
        }
    }

    final class Clear implements Operation {
        private static final int ID = 3;

        @Override
        public void write(@NotNull ByteArrayDataOutput output) {
            output.writeInt(ID);
        }
    }

}
