package com.yukii.anvildelimited.mixin;

import com.yukii.anvildelimited.AnvilDelimited;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = AnvilScreen.class, priority = 997)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

    public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @ModifyConstant(method = "drawForeground", constant = @Constant(intValue = 40))
    private int modifyTooExpensiveValue(int i) {
        return AnvilDelimited.gameRuleValue;
    }
}
