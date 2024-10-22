package com.mattworzala.debug;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mattworzala.debug.shape.Shape;
import net.kyori.adventure.audience.Audience;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A message to send the client to show debug objects.
 *
 * @param ops The operations to perform.
 */
public record DebugMessage(
        List<Operation> ops
) {

    private static final Logger log = Logger.getLogger(DebugMessage.class.getName());

    /**
     * @return A new {@link DebugMessage.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Sends this DebugMessage to an audience.
     */
    public void sendTo(Audience audience, JavaPlugin plugin) {
        if (audience instanceof Player player) {
            byte[] payloadData = getPayloadData();
            player.sendPluginMessage(plugin, "debug:shapes", payloadData);
        } else {
            log.warning("Audience is not a player.");
        }
    }

    /**
     * Generates the payload data to be sent in the plugin message.
     */
    private byte[] getPayloadData() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeInt(ops.size());
        for (Operation op : ops) {
            op.write(out);
        }

        return out.toByteArray();
    }

    public static class Builder {

        private final List<Operation> ops = new ArrayList<>();

        /**
         * Sets a shape with the specified namespace ID.
         *
         * @param namespaceId The namespace ID for this shape. If reused, the previous shape will be replaced.
         * @param shape       The shape to associate with the namespace ID.
         * @return The builder.
         */
        public Builder set(String namespaceId, Shape shape) {
            return set(NamespacedKey.fromString(namespaceId), shape);
        }

        /**
         * Sets a shape with the specified namespace ID.
         *
         * @param id    The namespace ID for this shape. If reused, the previous shape will be replaced.
         * @param shape The shape to associate with the namespace ID.
         * @return The builder.
         */
        public Builder set(NamespacedKey id, Shape shape) {
            ops.add(new Operation.Set(id, shape));
            return this;
        }

        /**
         * Removes a shape with a specified namespace ID.
         *
         * @param namespaceId The namespace ID to remove.
         * @return The builder.
         */
        public Builder remove(String namespaceId) {
            return remove(NamespacedKey.fromString(namespaceId));
        }

        /**
         * Removes a shape with a specified namespace ID.
         *
         * @param id The namespace ID to remove.
         * @return The builder.
         */
        public Builder remove(NamespacedKey id) {
            ops.add(new Operation.Remove(id));
            return this;
        }

        public Builder clear(String namespace) {
            ops.add(new Operation.ClearNS(namespace));
            return this;
        }

        public Builder clear() {
            ops.add(new Operation.Clear());
            return this;
        }

        /**
         * @return Constructs a new {@link DebugMessage} with the provided builder parameters.
         */
        public DebugMessage build() {
            return new DebugMessage(ops);
        }

    }

}
