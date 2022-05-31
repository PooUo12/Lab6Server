package util.commands;

import server.DatagramServer;
import server.list.PersonList;
import util.sendingUtils.Response;

import java.util.List;

public class Info extends Command {

    public Response execute(List<Object> params){
        PersonList personList = (PersonList) params.get(0);
        DatagramServer.logger.info("Command Info completed");
        return new Response("Success", personList.getInfo());
    }
}
