package server;

import server.list.ListSaver;
import server.list.PersonList;
import sun.misc.Signal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class DatagramServer {
    public static final Logger logger = Logger.getLogger(DatagramServer.class.getSimpleName());
    public static InetSocketAddress address;


    PersonList personList;

    public DatagramChannel startServer() {
        DatagramChannel channel;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Write your host, example: " + InetAddress.getLocalHost().toString());
                InetAddress host = InetAddress.getByName(scanner.nextLine());
                System.out.println("Write your port");
                int port = Integer.parseInt(scanner.nextLine());
                address = new InetSocketAddress(host, port);
                channel = DatagramChannelBuilder.bindChannel(address);
                channel.configureBlocking(false);
                System.out.println("Bound successfully");
                break;
            } catch (UnknownHostException e) {
                System.out.println("Illegal host");
            } catch (NumberFormatException e) {
                System.out.println("Illegal port");
            } catch (IllegalArgumentException e) {
                System.out.println("Port out of legal range");
            } catch (IOException e) {
                System.out.println("Binding error");
            }
        }
        personList = new PersonList();
        DatabaseConnector db = new DatabaseConnector();
        db.collect(personList);
        logger.info("Collection from file saved");

        setupShutDownWork(personList, channel);
        setupSignalHandler(personList, channel);

        logger.info("Server started at #" + address);
        return channel;
    }


    private void setupSignalHandler(PersonList personList, DatagramChannel channel) {
        Signal.handle(new Signal("TSTP"), signal -> {
            ListSaver listSaver = new ListSaver(personList.getList());
            listSaver.save();
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setupShutDownWork(PersonList personList, DatagramChannel channel) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ListSaver listSaver = new ListSaver(personList.getList());
            listSaver.save();
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    public static void main(String[] args) {
        ExecutorService fixedPool = Executors.newFixedThreadPool(10);
        DatagramServer server = new DatagramServer();
        DatagramChannel channel = server.startServer();
        MessageProcessor messageProcessor = new MessageProcessor(server.personList);
        fixedPool.execute(() -> {
            while (true) {
                try {
                    messageProcessor.receiveMessage(channel);
                } catch (IOException e) {
                    try {
                        channel.close();
                        break;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        });
    }
}
