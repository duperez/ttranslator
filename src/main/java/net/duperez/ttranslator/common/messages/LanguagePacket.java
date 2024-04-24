package net.duperez.ttranslator.common.messages;

import com.google.gson.Gson;
import net.duperez.ttranslator.client.services.ClientSideClientLanguageService;
import net.duperez.ttranslator.client.services.ClientSideClientModService;
import net.duperez.ttranslator.common.messages.handlers.client.ClientLanguagePackageHandler;
import net.duperez.ttranslator.common.messages.handlers.server.ServerLanguagePackageHandler;
import net.duperez.ttranslator.common.network.ModNetworking;
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
            if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                ClientLanguagePackageHandler.playerProcessMessage(message.message);
                UserPackage userPackage = new UserPackage(new Gson().toJson(ClientSideClientLanguageService.getInstance().getClientEntity()));
                ModNetworking.CHANNEL.sendToServer(userPackage);
            } else {
                ServerLanguagePackageHandler.serverProcessMessage(message.message);
                //LanguagePacket packet = new LanguagePacket(new Gson().toJson(ClientSideClientModService.getInstance().getClientEntity().getLanguages()));
                //ModNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), new MessagePackage(new Gson().toJson(packet)));
            }

        });
        context.setPacketHandled(true);
    }
}