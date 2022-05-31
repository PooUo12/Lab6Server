package server.listfiller;

import server.list.PersonList;
import util.exceptions.IllegalFieldsException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static server.Constants.FIRST_LINE;


public class Parser {

    private static final String FILEPATH = System.getenv("file");
    public void parser(PersonCreator personCreator, PersonList personList) throws IOException {
        try {
            String firstLine;
            BufferedInputStream buff = new BufferedInputStream(new FileInputStream(FILEPATH));
            Scanner s = new Scanner(buff);
            try {
                StringBuilder output = new StringBuilder();
                firstLine = s.nextLine();
                for (String str : FIRST_LINE.split(",")) {
                    if (!firstLine.contains(str)) {
                        output.append(str).append(",");
                    }
                }
                if (output.length() != 0) {
                    throw new IllegalFieldsException("Following args ain't right:" + output.deleteCharAt(output.length() - 1));
                }
            } catch (NoSuchElementException e) {
                System.out.println("Csv-file is empty, that's why can't use it");
            }
            while (s.hasNext()) {
                try {
                    personList.addPerson(personCreator.createPerson(s.nextLine()));
                } catch (IllegalFieldsException | NullPointerException e) {
                    System.out.println(e.getMessage());
                }
            }


            buff.close();
        } catch (FileNotFoundException e){
            System.out.println("Permission denied");
        } catch (NullPointerException e){
            System.out.println("Environmental variable doesn't exist");
        }
        }

    }

