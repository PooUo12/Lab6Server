package server;

import server.list.PersonList;
import util.commands.Command;
import util.sendingUtils.Marks;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class MessageProcessor {
    private final PersonList personList;
    private final ExecutorService fixedPool = Executors.newFixedThreadPool(10);
    private final ForkJoinPool forkPool = new ForkJoinPool(10);
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock wLock = rwLock.writeLock();
    private final Lock rLock = rwLock.readLock();

    public MessageProcessor(PersonList personList) {
        this.personList = personList;
    }

    public void receiveMessage(DatagramChannel channel) throws IOException {
        SocketAddress remoteAdd = null;
        ByteBuffer buffer = ByteBuffer.allocate(Constants.size);
        while (remoteAdd == null) {
            try {
                remoteAdd = channel.receive(buffer);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        final SocketAddress socketAddress = remoteAdd;
        Response response = forkPool.invoke(new RecursiveTask<Response>() {
            @Override
            protected Response compute() {
                return extractMessage(buffer);
            }
        });
        ByteBuffer answer = ByteBuffer.wrap(Serializer.serializer(response).toByteArray());
        fixedPool.execute(() -> {
            sendMessage(answer, socketAddress, channel);

        });
    }

    private Response extractMessage(ByteBuffer buffer) {
        DatabaseConnector db = new DatabaseConnector();
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        Request request = (Request) Serializer.deserializer(buffer.array());
        if (request.getLogin() != null && request.getPassword() != null) {
            String login = request.getLogin();
            String pass = request.getPassword();
            Response response = db.login(login, pass);
            if (response.getTitle().equals("Error")) {
                return new Response("Error", "Your login and password are invalid");
            }
        }
        Marks mark = request.getMark();

        Command command = request.getCommand();
        List<Object> args = request.getArgs();
        if (args == null) {
            args = new ArrayList<>();
            args.add(personList);
            args.add(request.getLogin());
        } else {
            args.add(0, personList);
            args.add(1, request.getLogin());
        }
        DatagramServer.logger.info("Client sent: " + request.getCommand().toString());
        Response response;
        if (mark.equals(Marks.READ)){
            wLock.lock();
        } else if (mark.equals(Marks.WRITE)){
            rLock.lock();
        }
        response = command.execute(args);
        if (mark.equals(Marks.READ)){
            wLock.unlock();
        } else if (mark.equals(Marks.WRITE)){
            rLock.unlock();
        }
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
        } catch (ClosedChannelException e) {
            DatagramServer.logger.info("Unable to send packet because channel is closed");
        } catch (SecurityException e) {
            DatagramServer.logger.info("Security manager doesn't allow to send packet");
        } catch (IOException e) {
            DatagramServer.logger.info("Error sending packet to client");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
