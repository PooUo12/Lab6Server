package server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

public class DatagramChannelBuilder {
    public static DatagramChannel bindChannel(SocketAddress socket) throws IOException {
        return openChannel().bind(socket);
    }

    private static DatagramChannel openChannel() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        return datagramChannel;
    }
}


