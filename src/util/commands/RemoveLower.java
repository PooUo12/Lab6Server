package util.commands;

import server.DatagramServer;
import server.list.PersonList;
import util.person.Person;
import util.sendingUtils.Response;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveLower extends Command {

    public Response execute(List<Object> params){
        List<Person> forRemoving;
        String title = "Success";
        String message = "";

        PersonList personList = (PersonList) params.get(0);
        Person person = (Person) params.get(1);

        forRemoving = personList.getList().stream().
                filter(s -> s.compareTo(person) < 0).
                collect(Collectors.toList());
        if (forRemoving.isEmpty()){
            message = "No elements were deleted( no suitable elements)";
        }
        for (Person s: forRemoving) {
            personList.removePerson(s);
            message = forRemoving.size() + " elements were removed";
        }
        DatagramServer.logger.info("Command RemoveLower completed");
        return new Response(title,message);
    }
}
