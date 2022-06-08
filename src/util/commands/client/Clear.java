package util.commands.client;

import server.DatabaseConnector;
import server.DatagramServer;
import server.list.PersonList;
import util.commands.Command;
import util.sendingUtils.Response;

import java.util.List;


public class Clear extends Command {

    public Response execute(List<Object> params){
        DatabaseConnector db = new DatabaseConnector();
        PersonList personList = (PersonList) params.get(0);
        String login = (String) params.get(1);
        if (db.clear(login)) {
            personList.listClean(login);
        } else {

        }
        DatagramServer.logger.info("Command Clear completed");
        return new Response("Success","List was cleaned");
    }
}
