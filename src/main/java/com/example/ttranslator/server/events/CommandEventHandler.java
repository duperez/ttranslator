package com.example.ttranslator.server.events;

import com.example.ttranslator.Ttranslator;
import com.example.ttranslator.common.Network.NetworkHandler;
import com.example.ttranslator.server.objects.language.Language;
import com.example.ttranslator.server.objects.language.LanguageManager;
import com.example.ttranslator.server.objects.player.PlayerInfo;
import com.example.ttranslator.server.objects.player.PlayerOnMemoryManager;
import com.example.ttranslator.server.objects.language.TranslatedMessage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.example.ttranslator.server.service.TranslationService.*;

@Mod.EventBusSubscriber(modid = Ttranslator.MODID, value = Dist.DEDICATED_SERVER)
public class CommandEventHandler {
    static LanguageManager languageManager = LanguageManager.getInstance();

    private static String checkSignAndGetText(ServerPlayer player) {
        HitResult hitResult = player.pick(5.0D, 0.0F, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = player.level().getBlockState(blockPos);
            Block block = blockState.getBlock();

            if (block instanceof SignBlock) {
                BlockEntity blockEntity = player.level().getBlockEntity(blockPos);
                if (blockEntity instanceof SignBlockEntity) {
                    SignBlockEntity signEntity = (SignBlockEntity) blockEntity;
                    Component[] messages = signEntity.getText(true).getMessages(true);

                    StringBuilder signStringBuilder = new StringBuilder();
                    Arrays.stream(messages).forEach(s -> {
                        if(!s.getString().isEmpty() && !s.getString().isBlank()) {
                            signStringBuilder.append(s.getString()).append(" ");
                        }
                    });
                    return signStringBuilder.toString();
                }
            }
        } else {
            player.sendSystemMessage(Component.literal("You are not looking at a sign!"));
        }
        return null;
    }

    private static void checkSignAndSendText(ServerPlayer player) throws IOException, ExecutionException, InterruptedException {
        String text = checkSignAndGetText(player);
        if(text != null) {
            String originalLanguage = detectLanguage(text);
            PlayerInfo playerInfo = PlayerOnMemoryManager.getInstance().getPlayerByUUID(player.getStringUUID()).orElse(null);
            List<TranslatedMessage> translatedMessages = translateMessageMultiTread(text, originalLanguage, playerInfo.getReadLanguage().stream().filter(s -> !s.equals(originalLanguage)).toList());
            translatedMessages.forEach(s -> player.sendSystemMessage(Component.literal("[SIGN - " + s.getTranslatedLanguage() + "] " + s.getMessage())));
        } else {
            player.sendSystemMessage(Component.literal("You are not looking at a sign!"));
        }
    }

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("translation_players_info")
                        .executes(commandContext -> {
                            StringBuilder playersInfo = new StringBuilder("UUID | NAME | SPOKEN LANGUAGE | READ LANGUAGE\n");

                            for (PlayerInfo playerInfo : PlayerOnMemoryManager.getInstance().getPlayers()) {
                                playersInfo.append(playerInfo.getUUID())
                                        .append(" | ").append(playerInfo.getName())
                                        .append(" | ").append(playerInfo.getSpokenLanguage())
                                        .append(" | ").append(Arrays.toString(playerInfo.getReadLanguage().toArray()))
                                        .append("\n");
                            }
                            playersInfo.append("-------------------");
                            return 1;
                        })
        );

        dispatcher.register(
                Commands.literal("translate")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    chatTranslationMenu(player);
                                    return 1;
                                })
                        .then(Commands.literal("chat")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    chatTranslationMenu(player);
                                    return 1;
                                }))
                        .then(Commands.literal("sign")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    try {
                                        checkSignAndSendText(player);
                                    } catch (IOException | ExecutionException | InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return 1;
                                }))
                        .then(Commands.literal("setSpokenLanguage")
                                .then(Commands.argument("language", StringArgumentType.string())
                                        .executes(context -> {
                                            String language = StringArgumentType.getString(context, "language");
                                            PlayerOnMemoryManager.getInstance().getPlayerByUUID(Objects.requireNonNull(context.getSource().getPlayer()).getStringUUID()).ifPresent(s -> {
                                                s.setSpokenLanguage(language);
                                                PlayerOnMemoryManager.getInstance().updatePlayer(s);
                                            });
                                            chatTranslationMenu(context.getSource().getPlayer());
                                            return 1;
                                        })))
                        .then(Commands.literal("showTranslation")
                                .then(Commands.argument("isShowTranslation", StringArgumentType.string())
                                        .executes(context -> {
                                            String isShowTranslation = StringArgumentType.getString(context, "isShowTranslation");
                                            PlayerOnMemoryManager.getInstance().getPlayerByUUID(Objects.requireNonNull(context.getSource().getPlayer()).getStringUUID()).ifPresent(s -> {
                                                s.setTranslating(isShowTranslation.equals("true"));
                                            });
                                            chatTranslationMenu(context.getSource().getPlayer());
                                            return 1;
                                        })))
                        .then(Commands.literal("showOriginalMessages")
                                .then(Commands.argument("isShowOriginalMessages", StringArgumentType.string())
                                        .executes(context -> {
                                            String isShowOriginalMessages = StringArgumentType.getString(context, "isShowOriginalMessages");
                                            PlayerOnMemoryManager.getInstance().getPlayerByUUID(Objects.requireNonNull(context.getSource().getPlayer()).getStringUUID()).ifPresent(s -> {
                                                s.setShowOriginalMessages(isShowOriginalMessages.equals("true"));
                                            });
                                            chatTranslationMenu(context.getSource().getPlayer());
                                            return 1;
                                        })))
                        .then(Commands.literal("addReadLanguage")
                                .then(Commands.argument("language", StringArgumentType.string())
                                        .executes(context -> {
                                            String language = StringArgumentType.getString(context, "language");
                                            PlayerOnMemoryManager.getInstance().getPlayerByUUID(Objects.requireNonNull(context.getSource().getPlayer()).getStringUUID()).ifPresent(s -> {
                                                if(!s.getReadLanguage().contains(language)) {
                                                    s.getReadLanguage().add(language);
                                                } else {
                                                    s.getReadLanguage().remove(language);
                                                }

                                                PlayerOnMemoryManager.getInstance().updatePlayer(s);
                                            });
                                            chatTranslationMenu(context.getSource().getPlayer());
                                            return 1;
                                        })))
                        .then(Commands.literal("removeReadLanguage")
                                .then(Commands.argument("language", StringArgumentType.string())
                                        .executes(context -> {
                                            String language = StringArgumentType.getString(context, "language");
                                            PlayerOnMemoryManager.getInstance().getPlayerByUUID(Objects.requireNonNull(context.getSource().getPlayer()).getStringUUID()).ifPresent(s -> {
                                                s.getReadLanguage().remove(language);
                                                PlayerOnMemoryManager.getInstance().updatePlayer(s);
                                            });
                                            //sendTranslateMenu(context.getSource().getPlayer());
                                            return 1;
                                        })))
                        .then(Commands.literal("status")
                                .executes(context -> {
                                    PlayerInfo playerInfo = PlayerOnMemoryManager.getInstance().getPlayerByUUID(context.getSource().getPlayer().getStringUUID()).orElse(null);
                                    if (playerInfo != null) {
                                        StringBuilder statusInfo = new StringBuilder("UUID | NAME | SPOKEN LANGUAGE\n");
                                        statusInfo.append(playerInfo.getUUID())
                                                .append(" | ").append(playerInfo.getName())
                                                .append(" | ").append(playerInfo.getSpokenLanguage())
                                                .append("\n-------------------");
                                    } else {
                                        context.getSource().sendFailure(Component.literal("Player not found"));
                                    }
                                    return 1;
                                }))
        );

        dispatcher.register(
                Commands.literal("TTS")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            String text = checkSignAndGetText(player);
                            if(text != null) {
                                NetworkHandler.sendToClient(new NetworkHandler.ClientAudioPacket(generateTTS(text)), player);
                            }
                            return 1;
                        })
        );

    }

    private static void mainTranslationMenu(ServerPlayer player) {
        Component menu = Component.literal("§7============================\n")
                .append(Component.literal("§6TTRANSLATOR MENU\n").setStyle(Style.EMPTY.withBold(true)))
                .append(Component.literal("§7============================\n"))
                .append(createClickableOption("§e- CHAT TRANSLATION MENU", "Open chat translation", "/translate chat"))
                .append(createClickableOption("§e- SIGN TRANSLATION MENU", "Open sign translation", "/translate sign"))
                //.append(createClickableOption("§e- BOOK TRANSLATION MENU", "Open book translation", "/translate book"))
                //.append(createClickableOption("§e- SERVER CONFIGURATION MENU", "Open server configuration menu", "/translate server"))
                .append(Component.literal("§7============================\n"));

        player.sendSystemMessage(menu);
    }

    private static void chatTranslationMenu(ServerPlayer player) {
        PlayerInfo playerInfo = PlayerOnMemoryManager.getInstance().getPlayerByUUID(player.getStringUUID()).orElse(null);

        String isTranslatingColor = playerInfo.isTranslating() ? "§e" : "§7";
        String isNotTranslatingColor = !playerInfo.isTranslating() ? "§e" : "§7";
        String isShowingOriginalMessageColor = playerInfo.isShowOriginalMessages() ? "§e" : "§7";
        String isNotShowingOriginalMessageColor = !playerInfo.isShowOriginalMessages() ? "§e" : "§7";


        MutableComponent menu = Component.literal("§7============================\n")
                .append(Component.literal("§6TTRANSLATOR MENU\n").setStyle(Style.EMPTY.withBold(true)))
                .append(Component.literal("§7============================\n"))
                .append(Component.literal("§7SHOW TRANSLATION\n"))
                .append(createClickableOption(isTranslatingColor + "- ON", "Turn translations on", "/translate showTranslation true"))
                .append(createClickableOption(isNotTranslatingColor + "- OFF", "Turn translations off", "/translate showTranslation false"))
                .append(Component.literal("§7============================\n"))
                .append(Component.literal("§7SHOW ORIGINAL MESSAGES\n"))
                .append(createClickableOption(isShowingOriginalMessageColor + "- ON", "show original messages", "/translate showOriginalMessages true"))
                .append(createClickableOption(isNotShowingOriginalMessageColor + "- OFF", "dont show original messages", "/translate showOriginalMessages false"))
                .append(Component.literal("§7============================\n"))
                .append(Component.literal("§6SPOKEN LANGUAGE\n").setStyle(Style.EMPTY.withBold(true)));

        assert playerInfo != null;
        createLanguagesOptions(playerInfo.getSpokenLanguage(), "Set spoken language to", "/translate setSpokenLanguage")
                .forEach(menu::append);

        menu.append(Component.literal("§7============================\n"));

        assert playerInfo != null;
        createLanguagesOptions(playerInfo.getReadLanguage(), "Set read language to", "/translate addReadLanguage")
                .forEach(menu::append);

        menu.append(Component.literal("§7============================\n"));

        player.sendSystemMessage(menu);
    }



    private static List<MutableComponent> createLanguagesOptions(String language, String hover, String command) {
        List<MutableComponent> options = new ArrayList<>();
        languageManager.getLanguages().forEach(s -> {
            String color = s.getAcronym().toLowerCase().equals(language) ? "§a" : "§7";
            String optionString = color + "- " + s.getName() + " (" + s.getAcronym().toUpperCase() + ")";
            options.add(createClickableOption(optionString, hover + " " + s.getName(), command + " " + s.getAcronym()));
        });
        return options;
    }

    private static List<MutableComponent> createLanguagesOptions(List userLanguages, String hover, String command) {
        List<MutableComponent> options = new ArrayList<>();
        languageManager.getLanguages().forEach(s -> {
            String color = userLanguages.contains(s.getAcronym().toLowerCase()) ? "§a" : "§7";
            String optionString = color + "- " + s.getName() + " (" + s.getAcronym().toUpperCase() + ")";
            options.add(createClickableOption(optionString, hover + " " + s.getName(), command + " " + s.getAcronym()));
        });
        return options;
    }

    private static MutableComponent createClickableOption(String option, String hoverText, String command) {
        return Component.literal(option + "\n")
                .setStyle(Style.EMPTY
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(hoverText)))
                        .withColor(net.minecraft.network.chat.TextColor.fromRgb(0x00FF00)) // Optional: Set text color
                );
    }
}
