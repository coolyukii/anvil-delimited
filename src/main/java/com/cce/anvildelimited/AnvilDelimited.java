package com.cce.anvildelimited;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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
    public static int gameRuleValue;
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

    @Override
    public void onInitialize() {
        gameRuleValue = 40;
        registerServerPlayerListener();
        registerGlobalValueReceiver();
    }

    /**
     * Registers a global receiver. For syncing the client and server sides together.
     */
    private void registerGlobalValueReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(AnvilDelimited.ID, (client, handler, buf, responseSender) -> {
            int i = buf.readInt();
            gameRuleValue = i < 2 || i > Short.MAX_VALUE ? 40 : i;
        });
    }

    /**
     * Registers a listener. The code executes everytime a player joins a world or a server and sends the player the gamerule's value integer as a packet.
     */

    private void registerServerPlayerListener() {
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
