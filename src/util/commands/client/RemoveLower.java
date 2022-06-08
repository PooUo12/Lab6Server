package util.commands.client;

import server.DatabaseConnector;
import server.DatagramServer;
import server.list.PersonList;
import util.commands.Command;
import util.person.Person;
import util.sendingUtils.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveLower extends Command {

    public Response execute(List<Object> params){
        String login = (String) params.get(1);
        DatabaseConnector db = new DatabaseConnector();
        List<Integer> forRemoving;
        String title = "Success";
        String message = "";

        PersonList personList = (PersonList) params.get(0);
        Person person = (Person) params.get(2);

        forRemoving = personList.getList().stream().filter(s1 -> s1.compareTo(person) < 0).map(Person::getId).collect(Collectors.toList());
        if (forRemoving.isEmpty()){
            return new Response("Success","No elements were deleted( no suitable elements)");
        }
        if (!db.remove(forRemoving, login)){
            message = "You can't access these elements";
        }
        for (int s: forRemoving) {
            personList.removePerson(s, login);
            message = forRemoving.size() + " elements were removed";
        }
        DatagramServer.logger.info("Command RemoveLower completed");
        return new Response(title,message);
    }
}
