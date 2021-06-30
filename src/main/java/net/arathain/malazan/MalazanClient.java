package net.arathain.malazan;

import net.arathain.malazan.common.render.FlareRenderer;
import net.arathain.malazan.common.render.PortalRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.Random;

public class MalazanClient implements ClientModInitializer {

        public static KeyBinding spellbind1;
        public static KeyBinding spellbind2;


        @Override
        public void onInitializeClient() {
                this.initKeybinds();
                EntityRendererRegistry.INSTANCE.register(Malazan.FLARE,
                        FlareRenderer::new);
                EntityRendererRegistry.INSTANCE.register(Malazan.PORTAL,
                        PortalRenderer::new);
            ClientSidePacketRegistry.INSTANCE.register(Malazan.TELAS_PARTICLE_ID,
                    (packetContext, attachedData) -> packetContext.getTaskQueue().execute(() -> {
                        // For now we will use the player's position since we don't know (yet) how to use the BlockPos
                        // in the onBlockRemoved callback. This is explained in "passing information to packets".
                        Vec3d pos = packetContext.getPlayer().getPos();
                        MinecraftClient.getInstance().particleManager.addParticle(
                                ParticleTypes.FLAME, pos.getX() + packetContext.getPlayer().getRandom().nextGaussian() / 10, pos.getY() + packetContext.getPlayer().getRandom().nextGaussian() / 10, pos.getZ() + packetContext.getPlayer().getRandom().nextGaussian() / 10,
                                0.0D, 0.1D, 0.0D
                        );
                    }));
        }

        private void initKeybinds() {
                // The translation key of the keybinding's name
                // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                // The keycode of the key
                spellbind1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key.malazan.spell1", // The translation key of the keybinding's name
                        InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                        GLFW.GLFW_KEY_Z, // The keycode of the key
                        "category.malazan.spells"
                ));
                // The translation key of the keybinding's name
                // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                // The keycode of the key
                // The translation key of the keybinding's category.
                spellbind2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key.malazan.spell2", // The translation key of the keybinding's name
                        InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                        GLFW.GLFW_KEY_X, // The keycode of the key
                        "category.malazan.spells" // The translation key of the keybinding's category.
                ));
        }
}
