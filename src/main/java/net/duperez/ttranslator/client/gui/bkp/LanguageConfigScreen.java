package net.duperez.ttranslator.client.gui.bkp;

import com.mojang.blaze3d.vertex.PoseStack;
import net.duperez.ttranslator.objects.common.Language;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.List;

public class LanguageConfigScreen extends Screen {
    private List<Language> languages; //= ConfigService.getInstance().getConfigs().getLanguages(); // Sua lista de línguas
    private EditBox languageNameInput;
    private EditBox languageISOInput;
    private int currentPage = 0;
    private final int languagesPerPage = 5;
    private int totalPages = 0;

    public LanguageConfigScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        // Aqui, carregue as línguas da sua configuração ou fonte de dados
        //languages = ConfigService.getInstance().getConfigs().getLanguages();
        totalPages = (int) Math.ceil((double) languages.size() / languagesPerPage);

        int startY = 20;
        int startX = this.width / 4;
        int languageIndexStart = currentPage * languagesPerPage;

        for (int i = 0; i < languagesPerPage && (languageIndexStart + i) < languages.size(); i++) {
            Language language = languages.get(languageIndexStart + i);
            this.addRenderableWidget(new Button(startX - 20, startY + (i * 24), 20, 20, new TextComponent("X"), button -> {
                // Lógica para deletar a língua
                languages.remove(language);
                // Provavelmente será necessário salvar essa alteração na sua fonte de dados/configuração
                this.init(); // Re-inicializar para atualizar a lista
            }));
            this.addRenderableWidget(new Button(startX, startY + (i * 24), 150, 20, new TextComponent(language.getName()), null));
        }

        // Campos de entrada para adicionar nova língua
        languageNameInput = new EditBox(this.font, startX, this.height - 60, 100, 20, new TextComponent("Language Name"));
        languageISOInput = new EditBox(this.font, startX + 110, this.height - 60, 50, 20, new TextComponent("ISO"));
        this.addRenderableWidget(languageNameInput);
        this.addRenderableWidget(languageISOInput);

        // Botão para adicionar língua
        this.addRenderableWidget(new Button(this.width / 4, this.height - 30, 160, 20, new TextComponent("Add Language"), button -> {
            // Lógica para adicionar a nova língua com base nos inputs
            languages.add(new Language(languageNameInput.getValue(), languageISOInput.getValue()));
            // Salvar essa nova língua na configuração ou fonte de dados
            this.init(); // Re-inicializar para atualizar a lista
        }));

        // Botões de navegação
        addPageNavigationButtons();
    }

    private void addPageNavigationButtons() {
        // Lógica para adicionar botões de navegação de página ([back] [number] [forward])
        // Similar ao exemplo anterior, apenas ajuste as posições e ações
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        languageNameInput.render(poseStack, mouseX, mouseY, partialTicks);
        languageISOInput.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}