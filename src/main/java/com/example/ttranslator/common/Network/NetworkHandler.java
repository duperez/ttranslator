package com.example.ttranslator.common.Network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayInputStream;
import java.util.function.Supplier;

import static com.example.ttranslator.Ttranslator.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("yourmodid", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, ClientAudioPacket.class, ClientAudioPacket::encode, ClientAudioPacket::decode, ClientAudioPacket::handle);
    }

    public static void sendToClient(Object message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static class ClientAudioPacket {
        private byte[] audioBytes;

        public ClientAudioPacket() {}

        public ClientAudioPacket(byte[] audioBytes) {
            this.audioBytes = audioBytes;
        }

        public static void encode(ClientAudioPacket packet, FriendlyByteBuf buffer) {
            buffer.writeByteArray(packet.audioBytes);
        }

        public static ClientAudioPacket decode(FriendlyByteBuf buffer) {
            return new ClientAudioPacket(buffer.readByteArray());
        }

        public static void handle(ClientAudioPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                new Thread(() -> playSound(packet.audioBytes)).start();
            });
            context.setPacketHandled(true);
        }

        private static void playSound(byte[] audioBytes) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bais);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();

                // Esperar o áudio terminar de tocar
                while (!clip.isRunning()) {
                    Thread.sleep(10);
                }
                while (clip.isRunning()) {
                    Thread.sleep(10);
                }

                clip.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}