package com.cce.anvildelimited.mixin;


import com.cce.anvildelimited.AnvilDelimited;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = AnvilScreenHandler.class, priority = 997)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 40))
    private int modifyVanillaConstant(int i) {
        return AnvilDelimited.gameRuleValue;
    }

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 39))
    private int modifyVanillaRenameConstant(int i) {
        return AnvilDelimited.gameRuleValue - 1;
    }
}
