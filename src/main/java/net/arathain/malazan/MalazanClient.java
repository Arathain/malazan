package net.arathain.malazan;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MalazanClient implements ClientModInitializer {

        public KeyBinding spellbind1;
        public KeyBinding spellbind2;

        @Override
        public void onInitializeClient() {
                this.initKeybinds();
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