package net.duperez.ttranslator.messages;

import com.google.gson.Gson;
import net.duperez.ttranslator.messages.handlers.client.ClientLanguagePackageHandler;
import net.duperez.ttranslator.messages.handlers.server.ServerLanguagePackageHandler;
import net.duperez.ttranslator.network.ModNetworking;
import net.duperez.ttranslator.services.client.ClientUserService;
import net.duperez.ttranslator.services.common.ConfigService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;


public class LanguagePacket {
    private final String message;

    public LanguagePacket(String message) {
        this.message = message;
    }

    public static void encode(LanguagePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.message);
    }

    public static LanguagePacket decode(FriendlyByteBuf buffer) {
        return new LanguagePacket(buffer.readUtf(32767));
    }

    public static void handle(final LanguagePacket message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // Verifica se estamos no lado do cliente antes de tentar acessar classes do cliente.
            if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                ClientLanguagePackageHandler.playerProcessMessage(message.message);
                UserPackage userPackage = new UserPackage(new Gson().toJson(ClientUserService.getInstance().getUser()));
                ModNetworking.CHANNEL.sendToServer(userPackage);
            } else {
                ServerLanguagePackageHandler.playerProcessMessage(message.message);
                LanguagePacket packet = new LanguagePacket(new Gson().toJson(ConfigService.getInstance().getConfigs().getLanguages()));
                ModNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), new MessagePackage(new Gson().toJson(packet)));
            }

        });
        context.setPacketHandled(true);
    }
}