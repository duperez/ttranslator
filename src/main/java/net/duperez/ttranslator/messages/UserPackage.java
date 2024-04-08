package net.duperez.ttranslator.messages;

import net.duperez.ttranslator.messages.handlers.server.ServerClientPackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UserPackage {


    public String getMessage() {
        return message;
    }

    private final String message;


    public UserPackage(String message) {
        this.message = message;
    }

    public static void encode(UserPackage packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.message);
    }

    public static UserPackage decode(FriendlyByteBuf buffer) {
        return new UserPackage(buffer.readUtf(32767));
    }


    public static void handle(final UserPackage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // Verifica se estamos no lado do cliente antes de tentar acessar classes do cliente.
            if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                ServerClientPackageHandler.serverProcessMessage(context.getSender(), message.message);
            }
        });
        context.setPacketHandled(true);
    }


}
