/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.util.BlockIterator
 */
package me.realized.duels.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public final class BlockUtil {
    private BlockUtil() {
    }

    public static <T extends BlockState> T getTargetBlock(Player player, Class<T> type, int range) {
        BlockIterator iterator = new BlockIterator((LivingEntity)player, range);
        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (!type.isInstance(block.getState())) continue;
            return (T)((BlockState)type.cast(block.getState()));
        }
        return null;
    }

    public static boolean near(Player player, Block block, int hDiff, int vDiff) {
        int pX = player.getLocation().getBlockX();
        int pY = player.getLocation().getBlockY();
        int pZ = player.getLocation().getBlockZ();
        int bX = block.getLocation().getBlockX();
        int bY = block.getLocation().getBlockY();
        int bZ = block.getLocation().getBlockZ();
        return Math.abs(pX - bX) <= hDiff && Math.abs(pY - bY) <= vDiff && Math.abs(pZ - bZ) <= hDiff;
    }
}

