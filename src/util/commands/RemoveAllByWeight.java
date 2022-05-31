package util.commands;

import server.DatagramServer;
import server.list.PersonList;
import util.person.Person;
import util.sendingUtils.Response;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveAllByWeight extends Command {


    public Response execute(List<Object> params){
        List<Person> forRemoving;
        String message = "";
        PersonList personList = (PersonList) params.get(0);
        int arg = (int) params.get(1);
        forRemoving = personList.getList().stream().
                filter(s -> s.getWeight() == arg).
                collect(Collectors.toList());
        if (forRemoving.isEmpty()){
            message = "Elements weren't removed(no suitable elements)";
        }
        for (Person s: forRemoving) {
            personList.removePerson(s);
            message = forRemoving.size() + " elements were removed";
        }
        DatagramServer.logger.info("Command RemoveAllByWeight completed");
        return new Response("Success",message);
    }
}
