package fr.epita.launcher;


import fr.epita.datamodel.Person;
import fr.epita.services.Configuration;

import java.io.*;

import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;

public class Launcher {

    private static final String DATA_FILE = Configuration.getValueFromKey("data.file");
    private static final String SORTED_FILE = Configuration.getValueFromKey("sorted.file");
    private static final String COLUMN_DELIMITER = "@@@";
    private static final String NAME_DELIMITER = " ";

    protected static final List<Person> list = new ArrayList<>();

    public static void main(String[] args){
        readFile();
        writeFile();
    }


    public static void readFile(){

        List<String> lines;

        try {

            lines = Files.readAllLines(new File(DATA_FILE).toPath());
            for (String line : lines) {
                Person newPerson = new Person();
                String[] parts = line.split(COLUMN_DELIMITER);

                System.out.println(Arrays.asList(parts));

                String[] name = parts[0].split(NAME_DELIMITER);

                newPerson.setFirstName(name[0]);
                newPerson.setLastName(name[1]);
                newPerson.setPhoneNumber(parts[1]);
                newPerson.setAddress(parts[2]);

                list.add(newPerson);

            }

            sortPlayers(list);

            for (Person p: list){
                System.out.println(p.getFirstName()+NAME_DELIMITER+p.getLastName());
            }

        } catch (IOException e) {
            // TODO consider making a custom exception
            Logger.getLogger(e.getMessage());
        }
    }
    public static void sortPlayers(List<Person> persons) {
        Collections.sort(persons, (p1, p2) -> {
             int res =  p1.getLastName().compareToIgnoreCase(p2.getLastName());
             if (res != 0)
                 return res;
             return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
         });
    }

    public static void writeFile(){
       if(!new File(SORTED_FILE).exists()){

            try (FileOutputStream output_file = new FileOutputStream(SORTED_FILE, true)) {
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
       }

       for(Person p : list) {

            String formatted = p.getFirstName() + NAME_DELIMITER + p.getLastName();
            formatted += COLUMN_DELIMITER + p.getPhoneNumber();
            formatted += COLUMN_DELIMITER + p.getAddress();
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(SORTED_FILE,true))) {
                writer.println(formatted);
            } catch (FileNotFoundException e) {
                // TODO custom exception to be written
                Logger.getLogger(e.getMessage());
            }

        }
    }
}

