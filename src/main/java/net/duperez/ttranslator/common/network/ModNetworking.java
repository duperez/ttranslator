package net.duperez.ttranslator.common.network;

import net.duperez.ttranslator.common.messages.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    private static int packetId = 0;
    public static SimpleChannel CHANNEL;

    public static void setupNetwork() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("ttranslator", "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );
        registerAllPackets();
    }

    private static <T> void registerPacket(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                           Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> consumer) {
        CHANNEL.registerMessage(packetId++, type, encoder, decoder, consumer);
    }

    public static void registerAllPackets() {
        registerPacket(LanguagePacket.class,
                LanguagePacket::encode,
                LanguagePacket::decode,
                LanguagePacket::handle);

        registerPacket(MessagePackage.class,
                MessagePackage::encode,
                MessagePackage::decode,
                MessagePackage::handle);

        registerPacket(UserPackage.class,
                UserPackage::encode,
                UserPackage::decode,
                UserPackage::handle);

        registerPacket(TranslationKeyPackage.class,
                TranslationKeyPackage::encode,
                TranslationKeyPackage::decode,
                TranslationKeyPackage::handle);

        registerPacket(OpPackage.class,
                OpPackage::encode,
                OpPackage::decode,
                OpPackage::handle);
    }
}