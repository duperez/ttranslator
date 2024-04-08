package net.duperez.ttranslator.network;

import net.duperez.ttranslator.messages.LanguagePacket;
import net.duperez.ttranslator.messages.TranslationKeyPackage;
import net.duperez.ttranslator.messages.MessagePackage;
import net.duperez.ttranslator.messages.UserPackage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel CHANNEL;

    public static void setupNetwork() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("ttranslator", "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        registerPackets();
    }

    public static void registerPackets() {
        int packetId = 0;
        // ExamplePacket é a sua classe de pacote
        // Supõe-se que ExamplePacket implemente a interface correta e tenha um construtor adequado
        CHANNEL.registerMessage(packetId++,
                LanguagePacket.class,
                LanguagePacket::encode,  // Método para codificar o pacote para dados de rede
                LanguagePacket::decode,  // Método para decodificar os dados de rede de volta para um pacote
                LanguagePacket::handle); // Método chamado para processar o pacote no lado que o recebe

        CHANNEL.registerMessage(packetId++,
                MessagePackage.class,
                MessagePackage::encode,  // Método para codificar o pacote para dados de rede
                MessagePackage::decode,  // Método para decodificar os dados de rede de volta para um pacote
                MessagePackage::handle); // Método chamado para processar o pacote no lado que o recebe

        CHANNEL.registerMessage(packetId++,
                UserPackage.class,
                UserPackage::encode,  // Método para codificar o pacote para dados de rede
                UserPackage::decode,  // Método para decodificar os dados de rede de volta para um pacote
                UserPackage::handle); // Método chamado para processar o pacote no lado que o recebe

        CHANNEL.registerMessage(packetId++,
                TranslationKeyPackage.class,
                TranslationKeyPackage::encode,  // Método para codificar o pacote para dados de rede
                TranslationKeyPackage::decode,  // Método para decodificar os dados de rede de volta para um pacote
                TranslationKeyPackage::handle); // Método chamado para processar o pacote no lado que o recebe
    }
}