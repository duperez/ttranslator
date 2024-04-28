package net.duperez.ttranslator.client.gui.baseScreens;

import net.duperez.ttranslator.client.services.ClientSideClientModService;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public abstract class BaseTTranslatorPaginatedScreen extends BaseTTranslatorScreen {

    public static final int BUTTON_WIDTH = 100;
    public static final int HALF_BUTTON_WIDTH = BUTTON_WIDTH / 2;

    public static final int MAX_ITEMS_PER_PAGE = 4; //TODO make it user adjustable


    public int currentPage = 1;
    public int totalPages = 1;

    @Override
    public void init() {
        super.init();
        totalPages = getTotalPages();
    }

    public static int getTotalPages() {
        return (int) Math.ceil((double) ClientSideClientModService.getInstance().getClientEntity().getLanguages().size() / MAX_ITEMS_PER_PAGE);
    }


    protected BaseTTranslatorPaginatedScreen(Component pTitle) {
        super(pTitle);
    }

    public void addDefaultPaginationController() {
        addController();
    }

    private void addController() {

        int buttonWidth = 20;
        int width = (this.width / 2) - (buttonWidth / 2); // Centraliza os botões na tela
        int height = this.height - 20;

        Button backButton = new Button((width - buttonWidth) - buttonWidth, height, buttonWidth, buttonWidth, new TextComponent("<"), (Button button) -> {
            if (currentPage > 1) {
                currentPage--;
                renderMenu();
            }
        });
        this.addRenderableWidget(backButton);

        Button currentPageButton = new Button(width, height, buttonWidth, buttonWidth, new TextComponent(currentPage + "/" + totalPages), (Button button) -> {
        });
        this.addRenderableWidget(currentPageButton);

        Button forwardButton = new Button((width + buttonWidth) + buttonWidth, height, buttonWidth, buttonWidth, new TextComponent(">"), (Button button) -> {
            if (totalPages > currentPage) {
                currentPage++;
                renderMenu();
            }
        });
        this.addRenderableWidget(forwardButton);
    }


    protected abstract void renderMenu();
}
