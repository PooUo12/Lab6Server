package util.commands;

import server.DatagramServer;
import server.list.PersonList;
import util.sendingUtils.Response;

import java.util.List;


public class Clear extends Command {

    public Response execute(List<Object> params){
        PersonList personList = (PersonList) params.get(0);
        personList.listClean();
        DatagramServer.logger.info("Command Clear completed");
        return new Response("Success","List was cleaned");

    }
}
