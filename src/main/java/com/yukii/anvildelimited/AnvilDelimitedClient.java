package com.yukii.anvildelimited;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class AnvilDelimitedClient implements ClientModInitializer {
    public static int gameRuleValue;

    @Environment(EnvType.CLIENT)
    private void registerGlobalValueReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(AnvilDelimited.ID, (client, handler, buf, responseSender) -> {
            int i = buf.readInt();
            gameRuleValue = i < 2 || i > Short.MAX_VALUE ? 40 : i;
        });
    }

    @Override
    public void onInitializeClient() {
        gameRuleValue = 40;
        registerGlobalValueReceiver();
    }
}
