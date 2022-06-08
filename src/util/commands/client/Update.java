package util.commands.client;

import server.DatabaseConnector;
import server.DatagramServer;
import server.list.PersonList;
import util.commands.Command;
import util.exceptions.InvalidCommandException;
import util.person.Person;
import util.sendingUtils.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Update extends Command {

    public Response execute(List<Object> params){
        DatabaseConnector db = new DatabaseConnector();
        String title;
        String message;
        List<Integer> forRemoving;
        PersonList personList = (PersonList) params.get(0);
        String login = (String) params.get(1);
        Person person = (Person) params.get(2);
        int arg = (int) params.get(3);

        forRemoving = personList.getList().stream().map(Person::getId).filter(id -> id == arg).collect(Collectors.toList());
        try {
            if (!forRemoving.isEmpty()) {
                if (!db.update(person, arg, login)){
                    return new Response("Error", "This Person can't be edited");
                }
                for (int s : forRemoving) {
                    personList.removePerson(s, login);
                }
                personList.addPerson(person);
                title = "Success";
                message = "Element was replaced";
                DatagramServer.logger.info("Command Update completed");
            } else {
                throw new InvalidCommandException("Element with this id doesn't exists");
            }
        } catch (InvalidCommandException e){
            System.out.println(e.getMessage());
            title = "Fail";
            message = "Element with this id doesn't exists";
            DatagramServer.logger.info("Command Update couldn't be completed");
        }
        return new Response(title,message);
    }
}
