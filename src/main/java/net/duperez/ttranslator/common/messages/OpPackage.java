package net.duperez.ttranslator.common.messages;

import net.duperez.ttranslator.client.entities.ClientSideClientConfigs;
import net.duperez.ttranslator.client.entities.ClientTranslationUiConfigEntity;
import net.duperez.ttranslator.client.services.ClientSideClientLanguageService;
import net.duperez.ttranslator.client.services.ClientSideUiConfigsService;
import net.duperez.ttranslator.common.messages.handlers.client.ClientMessagePackageHandler;
import net.duperez.ttranslator.common.messages.handlers.server.ServerMessagePackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class OpPackage {

    public boolean getMessage() {
        return isOp;
    }

    private final boolean isOp;


    public OpPackage(boolean isOp) {
        this.isOp = isOp;
    }

    public static void encode(OpPackage packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.isOp);
    }

    public static OpPackage decode(FriendlyByteBuf buffer) {
        return new OpPackage(buffer.readBoolean());
    }


    public static void handle(final OpPackage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        System.out.println("received boolean: ");
        System.out.println(message.isOp);
        context.enqueueWork(() -> {
            ClientSideClientConfigs clientConfig = ClientSideClientLanguageService.getInstance().getClientEntity();
            clientConfig.setOp(message.isOp);

        });
        context.setPacketHandled(true);
    }


}
