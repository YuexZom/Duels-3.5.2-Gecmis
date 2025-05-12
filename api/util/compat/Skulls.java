/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.jetbrains.annotations.NotNull
 */
package me.realized.duels.util.compat;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import me.realized.duels.util.reflect.ReflectionUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public final class Skulls {
    private static final Method GET_PROFILE;
    private static final Field PROFILE;
    private static final Method SET_PROFILE;
    private static final LoadingCache<Player, GameProfile> cache;

    private static GameProfile getProfile(Player player) throws InvocationTargetException, IllegalAccessException {
        return (GameProfile)GET_PROFILE.invoke(player, new Object[0]);
    }

    public static void setProfile(SkullMeta meta, Player player) {
        if (SET_PROFILE != null) {
            meta.setOwningPlayer((OfflinePlayer)player);
            return;
        }
        try {
            GameProfile cached = cache.get(player);
            PROFILE.set(meta, cached);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Skulls() {
    }

    static {
        cache = CacheBuilder.newBuilder().maximumSize(1000L).weakKeys().expireAfterAccess(1L, TimeUnit.HOURS).build(new CacheLoader<Player, GameProfile>(){

            @Override
            public GameProfile load(@NotNull Player player) throws InvocationTargetException, IllegalAccessException {
                return Skulls.getProfile(player);
            }
        });
        Class<?> CB_PLAYER = ReflectionUtil.getCBClass("entity.CraftPlayer");
        GET_PROFILE = ReflectionUtil.getMethod(CB_PLAYER, "getProfile", new Class[0]);
        Class<?> CB_SKULL_META = ReflectionUtil.getCBClass("inventory.CraftMetaSkull");
        PROFILE = ReflectionUtil.getDeclaredField(CB_SKULL_META, "profile");
        SET_PROFILE = ReflectionUtil.getDeclaredMethodUnsafe(CB_SKULL_META, "setProfile", GameProfile.class);
    }
}

