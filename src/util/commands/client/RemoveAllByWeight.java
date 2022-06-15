package util.commands.client;

import server.DatabaseConnector;
import server.DatagramServer;
import server.list.PersonList;
import util.commands.Command;
import util.person.Person;
import util.sendingUtils.Response;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveAllByWeight extends Command {


    public Response execute(List<Object> params){
        DatabaseConnector db = new DatabaseConnector();
        List<Integer> forRemoving;
        String message;
        PersonList personList = (PersonList) params.get(0);
        String login = (String) params.get(1);
        int arg = (int) params.get(2);
        forRemoving = personList.getList().stream().filter(person -> person.getWeight() == arg).map(Person::getId).collect(Collectors.toList());
        if (forRemoving.isEmpty()){
            return new Response("Success", "Elements weren't removed(no suitable elements)");
        }
        if (db.remove(forRemoving, login)) {
            db.collect(personList);
            DatagramServer.logger.info("Elements were successfully removed");
            message = "Elements were removed";

        } else {
            DatagramServer.logger.info("No Elements were removed");
            message = "No elements were removed";
        }
        DatagramServer.logger.info("Command RemoveAllByWeight completed");
        return new Response("Success",message);
    }
}
