package dev.wj.client.gui;

import dev.wj.client.module.Category;
import dev.wj.client.module.Module;
import dev.wj.client.module.ModuleManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class WJClickGui extends Screen {
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 310;
    private static final int SIDEBAR_WIDTH = 126;
    private static final int PURPLE = 0xFF9B6CFF;
    private static final int PANEL = 0xF20D0D12;
    private static final int PANEL_LIGHT = 0xFF17171F;
    private static final int TEXT = 0xFFF2F0F7;
    private static final int MUTED = 0xFF85818F;
    private Category selectedCategory = Category.MOVEMENT;

    public WJClickGui() {
        super(Component.literal("WJ Client"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        int panelWidth = Math.min(PANEL_WIDTH, width - 24);
        int panelHeight = Math.min(PANEL_HEIGHT, height - 24);
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;
        int sidebar = Math.min(SIDEBAR_WIDTH, panelWidth / 3);

        graphics.fill(0, 0, width, height, 0x99000000);
        graphics.fill(left, top, left + panelWidth, top + panelHeight, PANEL);
        graphics.outline(left, top, panelWidth, panelHeight, 0xFF34313D);
        graphics.fill(left, top, left + 3, top + panelHeight, PURPLE);

        graphics.text(font, "WJ", left + 20, top + 17, TEXT);
        graphics.text(font, "CLIENT  /  26.2", left + 54, top + 18, MUTED);
        graphics.fill(left + 14, top + 39, left + panelWidth - 14, top + 40, 0xFF302D38);

        graphics.fill(left + 1, top + 41, left + sidebar, top + panelHeight - 1, 0xB80A0A0F);
        graphics.text(font, "CATEGORIES", left + 15, top + 55, MUTED);

        Category[] categories = Category.values();
        int categoryY = top + 78;
        for (Category category : categories) {
            boolean selected = category == selectedCategory;
            int rowTop = categoryY;
            if (selected) {
                graphics.fill(left + 10, rowTop - 5, left + sidebar - 10, rowTop + 18, 0xFF292333);
                graphics.fill(left + 10, rowTop - 5, left + 12, rowTop + 18, PURPLE);
            }
            graphics.text(font, label(category), left + 20, rowTop + 2, selected ? TEXT : MUTED);
            categoryY += 31;
        }

        int contentLeft = left + sidebar + 20;
        graphics.text(font, label(selectedCategory), contentLeft, top + 57, TEXT);
        List<Module> modules = modulesFor(selectedCategory);
        graphics.text(font, modules.size() + " MODULES", contentLeft, top + 73, MUTED);

        int cardY = top + 96;
        int cardWidth = panelWidth - sidebar - 40;
        for (Module module : modules) {
            int cardBottom = cardY + 34;
            boolean hovered = mouseX >= contentLeft && mouseX <= contentLeft + cardWidth
                    && mouseY >= cardY && mouseY <= cardBottom;
            graphics.fill(contentLeft, cardY, contentLeft + cardWidth, cardBottom,
                    hovered ? 0xFF24212D : PANEL_LIGHT);
            graphics.outline(contentLeft, cardY, cardWidth, 34,
                    module.isEnabled() ? 0xFF7450A9 : 0xFF302D38);
            graphics.text(font, module.getName(), contentLeft + 12, cardY + 7, TEXT);
            graphics.text(font, module.isEnabled() ? "ON" : "OFF",
                    contentLeft + cardWidth - 38, cardY + 7,
                    module.isEnabled() ? PURPLE : MUTED);
            cardY += 42;
        }

        graphics.text(font, "RIGHT SHIFT  •  CLOSE", left + 18,
                top + panelHeight - 20, MUTED);
        graphics.text(font, "WJ", left + panelWidth - 34,
                top + panelHeight - 20, PURPLE);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() != 0) {
            return super.mouseClicked(event, doubleClick);
        }

        int panelWidth = Math.min(PANEL_WIDTH, width - 24);
        int panelHeight = Math.min(PANEL_HEIGHT, height - 24);
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;
        int sidebar = Math.min(SIDEBAR_WIDTH, panelWidth / 3);
        double mouseX = event.x();
        double mouseY = event.y();

        int categoryY = top + 73;
        for (Category category : Category.values()) {
            if (mouseX >= left + 10 && mouseX <= left + sidebar - 10
                    && mouseY >= categoryY && mouseY <= categoryY + 24) {
                selectedCategory = category;
                return true;
            }
            categoryY += 31;
        }

        int contentLeft = left + sidebar + 20;
        int cardWidth = panelWidth - sidebar - 40;
        int cardY = top + 96;
        for (Module module : modulesFor(selectedCategory)) {
            if (mouseX >= contentLeft && mouseX <= contentLeft + cardWidth
                    && mouseY >= cardY && mouseY <= cardY + 34) {
                module.toggle();
                return true;
            }
            cardY += 42;
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static List<Module> modulesFor(Category category) {
        List<Module> result = new ArrayList<>();
        for (Module module : ModuleManager.getModules()) {
            if (module.getCategory() == category) {
                result.add(module);
            }
        }
        return result;
    }

    private static String label(Category category) {
        return switch (category) {
            case COMBAT -> "Combat";
            case MOVEMENT -> "Movement";
            case RENDER -> "Render";
            case PLAYER -> "Player";
            case MISC -> "Misc";
        };
    }
}
