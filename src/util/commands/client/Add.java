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
        int id = db.generateId();
        person.setId(id + 1);
        if (db.add(person)){
            personList.addPerson(person);
            response = new Response("Success", "Person added");
        } else {
            response = new Response("Error", "Person can't be added to DataBase");
        }
        DatagramServer.logger.info("Command Add completed");
        return response;
    }

}
