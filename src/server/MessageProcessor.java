package server;

import server.list.PersonList;
import util.commands.Command;
import util.sendingUtils.Request;
import util.sendingUtils.Response;
import util.sendingUtils.Serializer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class MessageProcessor {
    List<Object> args = null;
    PersonList personList;

    public MessageProcessor(PersonList personList){
        this.personList = personList;
    }

    public void receiveMessage(DatagramChannel channel) throws IOException {
        SocketAddress remoteAdd = null;
        ByteBuffer buffer = ByteBuffer.allocate(Constants.size);
        while (remoteAdd == null){
            try {
                remoteAdd = channel.receive(buffer);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Response response = extractMessage(buffer);
        ByteBuffer answer = ByteBuffer.wrap(Serializer.serializer(response).toByteArray());
        sendMessage(answer, remoteAdd, channel);

    }

    private Response extractMessage(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        Request request = (Request) Serializer.deserializer(buffer.array());
        Command command = request.getCommand();
        args = request.getArgs();
        if (args == null){
            args = new ArrayList<>();
            args.add(personList);
        } else {
            args.add(0, personList);
        }
        DatagramServer.logger.info("Client sent: " + request.getCommand().toString());
        Response response;
        response = command.execute(args);
        return response;
    }

    private void sendMessage(ByteBuffer buffer, SocketAddress serverAddress, DatagramChannel server) {
        try {
            DatagramServer.logger.info("Sending response...");
            Instant deadline = Instant.now().plusSeconds(1);
            while (Instant.now().isBefore(deadline)) {
                buffer.rewind();
                int numSent = server.send(buffer, serverAddress);
                if (numSent > 0) break;
                Thread.sleep(100);
            }
            DatagramServer.logger.info("Response was successfully sent");
        } catch (ClosedChannelException e){
            DatagramServer.logger.info("Unable to send packet because channel is closed");
        } catch (SecurityException e) {
            DatagramServer.logger.info("Security manager doesn't allow to send packet");
        }
        catch (IOException e) {
            DatagramServer.logger.info("Error sending packet to client");
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
