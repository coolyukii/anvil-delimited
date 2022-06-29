package com.cce.anvildelimited.client;

import com.cce.anvildelimited.AnvilDelimited;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class AnvilDelimitedClient implements ClientModInitializer {
    public static int gameRuleValue;

    /**
     *  Establishes a global receiver to retrieve the gamerule's value from the server.
     *  This is literally only needed for {@link com.cce.anvildelimited.mixin.AnvilScreenMixin},
     *  since it's not possible to retrieve the server world and its gamerules through the class due to it being client-sided.
     */
    @Override
    public void onInitializeClient() {
        gameRuleValue = 40;
        ClientPlayNetworking.registerGlobalReceiver(AnvilDelimited.ID, (client, handler, buf, responseSender) -> {
            int integer = buf.readInt();
            if (!(integer < 2 || integer > Short.MAX_VALUE))
                gameRuleValue = integer;
            else gameRuleValue = 40;
        });
    }
}
