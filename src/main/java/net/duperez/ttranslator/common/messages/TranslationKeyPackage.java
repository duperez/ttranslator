package net.duperez.ttranslator.common.messages;

import net.duperez.ttranslator.common.messages.handlers.server.ServerTranslationKeyPackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TranslationKeyPackage {


    public String getMessage() {
        return message;
    }

    private final String message;


    public TranslationKeyPackage(String message) {
        this.message = message;
    }

    public static void encode(TranslationKeyPackage packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.message);
    }

    public static TranslationKeyPackage decode(FriendlyByteBuf buffer) {
        return new TranslationKeyPackage(buffer.readUtf(32767));
    }


    public static void handle(final TranslationKeyPackage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // Verifica se estamos no lado do cliente antes de tentar acessar classes do cliente.
            if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                ServerTranslationKeyPackageHandler.serverProcessMessage(message.message);
            }
        });
        context.setPacketHandled(true);
    }


}
