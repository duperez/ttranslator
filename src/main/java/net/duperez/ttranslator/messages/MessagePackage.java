package net.duperez.ttranslator.messages;

import net.duperez.ttranslator.messages.handlers.client.ClientMessagePackageHandler;
import net.duperez.ttranslator.messages.handlers.server.ServerMessagePackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class MessagePackage {

    public String getMessage() {
        return message;
    }

    private final String message;


    public MessagePackage(String message) {
        this.message = message;
    }

    public static void encode(MessagePackage packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.message);
    }

    public static MessagePackage decode(FriendlyByteBuf buffer) {
        return new MessagePackage(buffer.readUtf(32767));
    }


    public static void handle(final MessagePackage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                ClientMessagePackageHandler.playerProcessMessage(message.message);
            } else {
                try {
                    ServerMessagePackageHandler.serverProcessMessage(context.getSender(), message.message);
                } catch (IOException | ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        context.setPacketHandled(true);
    }


}
