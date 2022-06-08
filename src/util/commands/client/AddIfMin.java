package util.commands.client;

import server.DatabaseConnector;
import server.DatagramServer;
import server.list.PersonList;
import util.commands.Command;
import util.person.Person;
import util.sendingUtils.Response;

import java.util.List;

public class AddIfMin extends Command {

    public Response execute(List<Object> params){
        DatabaseConnector db = new DatabaseConnector();
        String title = "";
        String message;
        PersonList personList = (PersonList) params.get(0);
        String login = (String) params.get(1);
        Person person = (Person) params.get(2);
        person.setUser(login);
        try {
            if (person.compareTo(personList.getList().peekFirst()) < 0) {
                int id = db.generateId();
                person.setId(id + 1);
                if (db.add(person)){
                personList.getList().add(person);
                title = "Success";
                message = "Person added";
                } else{
                    title = "Error";
                    message = "Element was the biggest but it can't be added to DataBase";
                }
            } else{
                title = "Error";
                message = "Person wasn't added";
            }
            DatagramServer.logger.info("Command AddIfMin completed");
        }catch (NullPointerException e){
            System.out.println("Can't compare because server.list is empty");
            title = "Error";
            message = "List is empty";
            DatagramServer.logger.info("Command AddIfMin couldn't be  completed");
        }
    return new Response(title, message);
    }
}
