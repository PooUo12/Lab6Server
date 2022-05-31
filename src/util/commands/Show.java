package util.commands;

import server.DatagramServer;
import server.list.PersonList;
import util.sendingUtils.Response;

import java.util.List;
import java.util.stream.Collectors;

public class Show extends Command {

    public Response execute(List<Object> params){
        PersonList personList = (PersonList) params.get(0);
        if (personList.getList().isEmpty()){
            DatagramServer.logger.info("Command Show couldn't be completed");
            return new Response("Fail", "List has no elements");

        }
        String output = personList.
                getList().stream().
                map(person -> person.toString()).collect(Collectors.joining("\n"));
        DatagramServer.logger.info("Command Show completed");
        return new Response("Success", output);
    }
}
