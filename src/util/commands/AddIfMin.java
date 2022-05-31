package util.commands;

import server.DatagramServer;
import server.list.PersonList;
import util.person.Person;
import util.sendingUtils.Response;

import java.util.List;

public class AddIfMin extends Command{

    public Response execute(List<Object> params){
        String title = "";
        String message;
        PersonList personList = (PersonList) params.get(0);
        Person person = (Person) params.get(1);
        try {
            if (person.compareTo(personList.getList().peekFirst()) > 0) {
                personList.getList().add(person);
                title = "Success";
                message = "Person added";
            } else{
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
