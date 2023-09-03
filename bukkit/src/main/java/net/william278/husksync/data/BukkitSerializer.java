package net.william278.husksync.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import net.william278.husksync.HuskSync;
import net.william278.husksync.adapter.Adaptable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class BukkitSerializer {

    protected final HuskSync plugin;

    public BukkitSerializer(@NotNull HuskSync plugin) {
        this.plugin = plugin;
    }

    public static class Inventory extends BukkitSerializer implements Serializer<BukkitDataContainer.Items.Inventory> {
        private static final String ITEMS_TAG = "items";
        private static final String HELD_ITEM_SLOT_TAG = "held_item_slot";

        public Inventory(@NotNull HuskSync plugin) {
            super(plugin);
        }

        @Override
        public BukkitDataContainer.Items.Inventory deserialize(byte[] serialized) throws DeserializationException {
            final ReadWriteNBT root = NBT.parseNBT(new String(serialized, StandardCharsets.UTF_8));
            final ItemStack[] items = root.getItemStackArray(ITEMS_TAG);
            final int selectedHotbarSlot = root.getInteger(HELD_ITEM_SLOT_TAG);
            return BukkitDataContainer.Items.Inventory.from(items, selectedHotbarSlot);
        }

        @NotNull
        @Override
        public byte[] serialize(@NotNull BukkitDataContainer.Items.Inventory data) throws SerializationException {
            final ReadWriteNBT root = NBT.createNBTObject();
            root.setItemStackArray(ITEMS_TAG, data.getContents());
            root.setInteger(HELD_ITEM_SLOT_TAG, data.getHeldItemSlot());
            return root.toString().getBytes(StandardCharsets.UTF_8);
        }
    }

    public static class EnderChest extends BukkitSerializer implements Serializer<BukkitDataContainer.Items.EnderChest> {

        public EnderChest(@NotNull HuskSync plugin) {
            super(plugin);
        }

        @Override
        public BukkitDataContainer.Items.EnderChest deserialize(byte[] serialized) throws DeserializationException {
            return BukkitDataContainer.Items.EnderChest.adapt(
                    NBT.itemStackArrayFromNBT(NBT.parseNBT(new String(serialized, StandardCharsets.UTF_8)))
            );
        }

        @NotNull
        @Override
        public byte[] serialize(@NotNull BukkitDataContainer.Items.EnderChest data) throws SerializationException {
            return NBT.itemStackArrayToNBT(data.getContents()).toString().getBytes(StandardCharsets.UTF_8);
        }
    }

    public static class PotionEffects extends BukkitSerializer implements Serializer<BukkitDataContainer.PotionEffects> {

        private static final TypeToken<List<DataContainer.PotionEffects.Effect>> TYPE = new TypeToken<>() {
        };

        public PotionEffects(@NotNull HuskSync plugin) {
            super(plugin);
        }

        @Override
        public BukkitDataContainer.PotionEffects deserialize(byte[] serialized) throws DeserializationException {
            return BukkitDataContainer.PotionEffects.adapt(
                    new Gson().fromJson(new String(serialized, StandardCharsets.UTF_8), TYPE.getType())
            );
        }

        @NotNull
        @Override
        public byte[] serialize(@NotNull BukkitDataContainer.PotionEffects element) throws SerializationException {
            return new Gson().toJson(element.getActiveEffects()).getBytes(StandardCharsets.UTF_8);
        }

    }

    public static class Advancements extends BukkitSerializer implements Serializer<BukkitDataContainer.Advancements> {

        private static final TypeToken<List<DataContainer.Advancements.Advancement>> TYPE = new TypeToken<>() {
        };

        public Advancements(@NotNull HuskSync plugin) {
            super(plugin);
        }

        @Override
        public BukkitDataContainer.Advancements deserialize(byte[] serialized) throws DeserializationException {
            return BukkitDataContainer.Advancements.from(
                    new Gson().fromJson(new String(serialized, StandardCharsets.UTF_8), TYPE.getType()), plugin
            );
        }

        @NotNull
        @Override
        public byte[] serialize(@NotNull BukkitDataContainer.Advancements element) throws SerializationException {
            return new Gson().toJson(element.getCompleted()).getBytes(StandardCharsets.UTF_8);
        }
    }

    public static class Location extends BukkitSerializer implements Serializer<BukkitDataContainer.Location> {

        public Location(@NotNull HuskSync plugin) {
            super(plugin);
        }

        @Override
        public BukkitDataContainer.Location deserialize(byte[] serialized) throws DeserializationException {
            return plugin.getDataAdapter().fromBytes(serialized, BukkitDataContainer.Location.class);
        }

        @NotNull
        @Override
        public byte[] serialize(@NotNull BukkitDataContainer.Location element) throws SerializationException {
            return plugin.getDataAdapter().toBytes(element);
        }
    }

    public static class Statistics extends BukkitSerializer implements Serializer<BukkitDataContainer.Statistics> {

        public Statistics(@NotNull HuskSync plugin) {
            super(plugin);
        }

        @Override
        public BukkitDataContainer.Statistics deserialize(byte[] serialized) throws DeserializationException {
            return plugin.getDataAdapter().fromBytes(serialized, BukkitDataContainer.Statistics.class);
        }

        @NotNull
        @Override
        public byte[] serialize(@NotNull BukkitDataContainer.Statistics element) throws SerializationException {
            return plugin.getDataAdapter().toBytes(element);
        }

    }

    public static class PersistentData extends BukkitSerializer implements Serializer<BukkitDataContainer.PersistentData> {

        public PersistentData(@NotNull HuskSync plugin) {
            super(plugin);
        }

        @Override
        public BukkitDataContainer.PersistentData deserialize(byte[] serialized) throws DeserializationException {
            return BukkitDataContainer.PersistentData.from(new NBTContainer(
                    new String(serialized, StandardCharsets.UTF_8)
            ));
        }

        @NotNull
        @Override
        public byte[] serialize(@NotNull BukkitDataContainer.PersistentData element) throws SerializationException {
            return element.getPersistentData().toString().getBytes(StandardCharsets.UTF_8);
        }

    }

    public static class Health extends Json<BukkitDataContainer.Health> implements Serializer<BukkitDataContainer.Health> {

        public Health(@NotNull HuskSync plugin) {
            super(plugin, BukkitDataContainer.Health.class);
        }

    }

    public static class Food extends Json<BukkitDataContainer.Food> implements Serializer<BukkitDataContainer.Food> {

        public Food(@NotNull HuskSync plugin) {
            super(plugin, BukkitDataContainer.Food.class);
        }

    }

    public static class Experience extends Json<BukkitDataContainer.Experience> implements Serializer<BukkitDataContainer.Experience> {

        public Experience(@NotNull HuskSync plugin) {
            super(plugin, BukkitDataContainer.Experience.class);
        }

    }

    public static class GameMode extends Json<BukkitDataContainer.GameMode> implements Serializer<BukkitDataContainer.GameMode> {

        public GameMode(@NotNull HuskSync plugin) {
            super(plugin, BukkitDataContainer.GameMode.class);
        }

    }

    public static abstract class Json<T extends DataContainer & Adaptable> extends BukkitSerializer implements Serializer<T> {

        private final Class<T> type;

        protected Json(@NotNull HuskSync plugin, Class<T> type) {
            super(plugin);
            this.type = type;
        }

        @Override
        public T deserialize(byte[] serialized) throws DeserializationException {
            return plugin.getDataAdapter().fromBytes(serialized, type);
        }

        @NotNull
        @Override
        public byte[] serialize(@NotNull T element) throws SerializationException {
            return plugin.getDataAdapter().toBytes(element);
        }

    }

}
