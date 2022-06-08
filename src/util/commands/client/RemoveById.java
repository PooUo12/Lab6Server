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

public class RemoveById extends Command {

    public Response execute(List<Object> params) {
        DatabaseConnector db = new DatabaseConnector();
        String login = (String) params.get(1);
        String title = "Success";
        String message;
        List<Integer> list = new ArrayList<>();
        PersonList personList = (PersonList) params.get(0);
        int arg = (int) params.get(2);
        list.add(arg);
        boolean flag = db.remove(list, login);
        if (flag) {
            message = "Element was successfully removed";
        } else {
            message = "Element with this id can't be removed";
        }
        personList.removePerson(arg, login);

        DatagramServer.logger.info("Command RemoveById completed");
        return new Response(title, message);

    }
}
