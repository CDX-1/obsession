package world.smallwoken.obsession.client;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class ObsessionClient implements ClientModInitializer {

    private static boolean isLeaveButton(Text message) {
        if (!(message.getContent() instanceof TranslatableTextContent translatable)) {
            return false;
        }

        String key = translatable.getKey();
        return key.equals("menu.returnToMenu") || key.equals("menu.disconnect");
    }

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (!(screen instanceof GameMenuScreen)) {
                return;
            }

            List<ClickableWidget> buttons = Screens.getButtons(screen);
            List<ButtonWidget> leaveButtons = new ArrayList<>();

            for (ClickableWidget widget : buttons) {
                if (widget instanceof ButtonWidget button && isLeaveButton(button.getMessage())) {
                    leaveButtons.add(button);
                }
            }

            for (ButtonWidget button : leaveButtons) {
                buttons.remove(button);

                ButtonWidget replacement = ButtonWidget.builder(button.getMessage(), btn -> client.setScreen(new DontLeaveMeScreen()))
                        .dimensions(button.getX(), button.getY(), button.getWidth(), button.getHeight())
                        .build();

                buttons.add(replacement);
            }
        });
    }
}
