package server.list;

import util.person.Person;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static server.Constants.FIRST_LINE;

public class ListSaver {

    private final List<Person> personList;

    public ListSaver(List<Person> personList){
        this.personList = personList;
    }

    public void save() {
        FileWriter fw;
        try {
            fw = new FileWriter(System.getenv("file"));
            fw.write(FIRST_LINE+ "\n");
            for(Person s: personList){
                fw.write(s.toString()+ "\n");
            }
            fw.close();

        } catch (FileNotFoundException e ){
            System.out.println("Permission denied || File doesn't exist");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
