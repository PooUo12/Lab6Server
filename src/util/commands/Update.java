package util.commands;

import server.DatagramServer;
import server.list.PersonList;
import util.exceptions.InvalidCommandException;
import util.person.Person;
import util.sendingUtils.Response;

import java.util.List;
import java.util.stream.Collectors;

public class Update extends Command {

    public Response execute(List<Object> params){
        String title;
        String message;
        List<Person> forRemoving;
        PersonList personList = (PersonList) params.get(0);
        Person person = (Person) params.get(1);
        int arg = (int) params.get(2);
        forRemoving = personList.getList().stream().
                filter(s -> s.getId() == arg).
                collect(Collectors.toList());
        try {
            if (!forRemoving.isEmpty()) {
                for (Person s : forRemoving) {
                    personList.removePerson(s);
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
