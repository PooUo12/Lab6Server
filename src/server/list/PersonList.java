package server.list;

import util.person.Person;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonList {
    private final LinkedList<Person> list;
    private final ZonedDateTime initializationDate;

    public PersonList(){
        this.list = new LinkedList<>();
        this.initializationDate = ZonedDateTime.now();
    }

    public LinkedList<Person> getList(){
        return this.list;
    }

    public String getInfo(){
         return "Type: LinkedList \n" + "Initialization date: " +initializationDate + "\n" + "Amount of elements: "+ list.size();
    }

    public void addPerson(Person person){
        if (person != null) {
            if (person.getId() == -1){
                person.setId(this.maxId() + 1);
                person.setCreationDate();
            }
            list.add(person);
            Collections.sort(list);
        }
    }

    public void removePerson(int id, String user){

        List<Person> forRemoving = this.list.stream().filter(s -> s.getId() == id).collect(Collectors.toList());

        for (Person person : forRemoving) {
            if (person.getUser().equals(user)) {
                this.list.remove(person);
            }
        }
    }

    public void listClean(String login){
        List<Person> forRemoving = this.list.stream().filter(person -> person.getUser().equals(login)).collect(Collectors.toList());
        forRemoving.forEach(this.list::remove);
    }

    public boolean compareIds(int id){
        for (Person p: list){
            if (id == p.getId()){
                return true;
            }
        }
        return false;
    }

    public int maxId(){
       int ret = 0;
       try {
           for (Person p : list) {
               if (ret < p.getId()) {
                   ret = p.getId();
               }
           }
       }catch(NullPointerException e){
           return 0;
       }
        return ret;
    }
}
