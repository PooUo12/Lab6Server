package util.commands;

import server.DatagramServer;
import util.sendingUtils.Response;

import java.util.List;

public class Connect extends Command{
    public Response execute(List<Object> params){
        DatagramServer.logger.info("Connection with client set");
        return new Response("Success", "Connection set");
    }
}
