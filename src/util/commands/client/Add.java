package util.commands.client;

import server.DatabaseConnector;
import server.DatagramServer;
import server.list.PersonList;
import util.commands.Command;
import util.person.Person;
import util.sendingUtils.Response;

import java.util.List;

public class Add extends Command {

    public Response execute(List<Object> params){
        Response response;
        DatabaseConnector db = new DatabaseConnector();
        PersonList personList = (PersonList) params.get(0);
        String login = (String) params.get(1);
        Person person = (Person) params.get(2);
        person.setUser(login);
        if (db.add(person)){
            db.collect(personList);
            DatagramServer.logger.info("New element added to personList");
            response = new Response("Success", "Person added");
        } else {
            DatagramServer.logger.info("PersonList wasn't updated");
            response = new Response("Error", "Person can't be added to DataBase");
        }
        DatagramServer.logger.info("Command Add completed");
        return response;
    }

}
