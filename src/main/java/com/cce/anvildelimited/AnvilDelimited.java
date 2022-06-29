package com.cce.anvildelimited;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

public class AnvilDelimited implements ModInitializer {
    public static final Identifier ID = new Identifier("anvil-delimited", "send-gamerule-value");

    /**
     * Creates the actual gamerule. The lambda will run every time the gamerule value updates,
     * syncing the server and client sides.
     */
    public static final GameRules.Key<GameRules.IntRule> ANVIL_XP_LIMIT = GameRuleRegistry
            .register("anvilXPLimit", GameRules.Category.MISC, GameRuleFactory
                    .createIntRule(40, 2, Short.MAX_VALUE,
                            ((minecraftServer, intRule) -> {
                                PacketByteBuf sBuf = PacketByteBufs.create();
                                sBuf.writeInt(intRule.get());
                                for (ServerPlayerEntity player : minecraftServer.getPlayerManager().getPlayerList()) {
                                    ServerPlayNetworking.send(player, ID, sBuf);
                                }
                            })));

    /**
     * Registers a listener upon mod initialization. The code runs every time a player joins a world for the purposes
     * of syncing the server and client sides.
     */
    @Override
    public void onInitialize() {
        ServerEntityEvents.ENTITY_LOAD.register(
                ((entity, world) -> {
                    if (entity instanceof ServerPlayerEntity) {
                        ServerPlayerEntity player = (ServerPlayerEntity) entity;
                        PacketByteBuf sBuf = PacketByteBufs.create();
                        sBuf.writeInt(world.getGameRules().getInt(AnvilDelimited.ANVIL_XP_LIMIT));
                        if (ServerPlayNetworking.canSend(player, ID)) ServerPlayNetworking.send(player, ID, sBuf);
                    }
                })
        );
    }
}
