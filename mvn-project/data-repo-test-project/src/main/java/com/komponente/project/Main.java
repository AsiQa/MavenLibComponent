package com.komponente.project;


import com.fasterxml.jackson.databind.JsonNode;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.events.EntityReference;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main {

    static File chosenFile;
    static String Directory;
    static List<Entity> outputList;
    static String extension;

    public static void main(String[] args) throws IOException {

//        DataRepository dataRepository = new DataRepositoryJson();

        DataRepository dataRepository = new DataRepositoryYaml();

        extension = dataRepository.extension();

//        dataRepository.save("yamlYest", new Entity("4", "Student", new HashMap<String,String>() {
//            { put("ime", "Msad");
//            put("prezime", "Misdc");
//            }}, new HashMap<>()));


        System.out.println("-------------------------------");


        //--------------     GUI     --------------//


        JFrame frame = new JFrame("App Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(530, 500);

        //-------------------------------------------------

        JMenuBar mb = new JMenuBar();


        JTextArea area = new JTextArea(20,45);
        JScrollPane scrollPane = new JScrollPane(area);
        area.setEditable(false);


        JMenu m1 = new JMenu("File");
        mb.add(m1);

        JMenuItem m11 = new JMenuItem("Open");

        m11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open folder");


                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "JSON and YAML files", "json", "yaml", "txt");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(chooser.getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    chosenFile = chooser.getSelectedFile();
                    Directory = chosenFile.getParent();

                    System.out.println("You chose to open this file: " + chosenFile.getName());
                    System.out.println("Selected file: " + chosenFile.getAbsolutePath());
                    System.out.println("Selected dir: " + Directory);

                    String collection = chosenFile + "";
                    collection = collection.replace("\\", "/");

                    area.setText("");
                    List<Entity> list = dataRepository.findAll(collection, Entity.class);
                    for(int i = 0; i<list.size();i++){
                        area.append(list.get(i).toString() + "\n");
                    }
                    System.out.println("list All");
                }

            }
        });


        JMenuItem m22 = new JMenuItem("Create new");

        m22.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Random random = new Random();
                int num = random.nextInt(1000);

                String desktopPath = System.getProperty("user.home");
                String updatedPath = desktopPath.replace("\\", "/");
                updatedPath = updatedPath + "\\Desktop\\fileNew"+num+".txt";

                try {
//
                    File newFile = new File(updatedPath);

                    if (newFile.createNewFile()) {
                        System.out.println("File created: " + newFile.getName());
                    } else {
                        System.out.println("File already exists.");
                    }
                } catch (IOException eo) {
                    System.out.println("An error occurred.");
                    eo.printStackTrace();
                }
            }
        });

        m1.add(m11);
        m1.add(m22);

        //--------------------------------------------------

        JPanel panel = new JPanel();
//        JLabel label1 = new JLabel("File name:");
//        JTextField tf1 = new JTextField(10);

        JLabel label2 = new JLabel("ID:");
        JTextField tfId = new JTextField(17);

        JLabel label3 = new JLabel("Naziv:");
        JTextField tfNaziv = new JTextField(17);

        JLabel label4 = new JLabel("Simple Props:");
        JTextField tfSimple = new JTextField(17);
        JTextField tfSimple2 = new JTextField(17);

        JLabel label5 = new JLabel("Entity Props:");
        JTextField tfEntity = new JTextField(17);
        JTextField tfEntity2 = new JTextField(17);




        panel.add(scrollPane);

//        panel.add(label1);
//        panel.add(tf1);

        panel.add(label2);
        panel.add(tfId);

        panel.add(label3);
        panel.add(tfNaziv);

        panel.add(label4);
        panel.add(tfSimple);
        panel.add(tfSimple2);

        panel.add(label5);
        panel.add(tfEntity);
        panel.add(tfEntity2);

        //----------------------------------------------------

        JPanel panel2 = new JPanel();

        JButton save = new JButton("Save");
        JButton delete = new JButton("Delete");
        JButton search = new JButton("Search");
        JButton sortId = new JButton("Sort ID");
        JButton sortName = new JButton("Sort Name");
        JButton listAll = new JButton("List All");

        panel2.add(save);
        panel2.add(delete);
        panel2.add(search);
        panel2.add(sortId);
        panel2.add(sortName);
        panel2.add(listAll);

        //-----------------------------------------------------

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String collection = chosenFile + "";
                collection = collection.replace("\\", "/");

                dataRepository.save(collection, new Entity(tfId.getText(), tfNaziv.getText(), new HashMap<String, String>() {{
                    put(tfSimple.getText(), tfSimple2.getText());
                    }}, new HashMap<>()), tfId.getText());
                System.out.println("save");


            }
        });

        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String collection = chosenFile + "";
                collection = collection.replace("\\", "/");

                if (!(tfId.getText().isEmpty())) {
                    String id = tfId.getText();
                    try {
                        double d = Double.parseDouble(id);
                        dataRepository.deleteById(collection, id, Entity.class);
                        System.out.println("delete by ID");
                    } catch (NumberFormatException nfe) {
                        System.out.println("Not a number");
                    }
                } else if (!(tfNaziv.getText().isEmpty())){
                    String name = tfNaziv.getText();
                    dataRepository.deleteByName(collection, name, Entity.class);
                    System.out.println("delete by Name");
                } else{
                    System.out.println("Fields are empty");
                }
            }

        });

        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String collection = chosenFile + "";
                collection = collection.replace("\\", "/");

                if (!(tfId.getText().isEmpty())) {
                    String id = tfId.getText();
                    try {
                        double d = Double.parseDouble(id);
                        area.setText("");
                        area.append(dataRepository.findById(collection, id, Entity.class).toString() + "\n");
                        System.out.println("search by ID");
                    } catch (NumberFormatException nfe) {
                        System.out.println("Not a number");
                    }
                } else if (!(tfNaziv.getText().isEmpty())){
                    String name = tfNaziv.getText();

                    area.setText("");

                    List<Entity> list = dataRepository.findByName(collection, name, Entity.class);
                    for(int i = 0; i<list.size();i++){
                        area.append(list.get(i).toString() + "\n");
                    }
                    System.out.println("list All");
                    System.out.println("search by Name");
                } else{
                    System.out.println("Fields are empty");
                }

            }
        });

        sortId.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){

                String collection = chosenFile + "";
                collection = collection.replace("\\", "/");

                if(e.getClickCount()==2){

                    area.setText("");

//                    List<Entity> list2 = dataRepository.sortByIdCurr(outputList, "desc");
                    List<Object> list = dataRepository.sortById(collection, "desc");


                    for(int i = 0; i<list.size();i++){
                        area.append(list.get(i).toString() + "\n");
                    }

                    System.out.println("Double Click Sort");

                }else if(e.getClickCount()==1){
                    area.setText("");

//                    List<Entity> list = dataRepository.sortByIdCurr(outputList, "asc");
                    List<Object> list = dataRepository.sortById(collection, "asc");

                    for(int i = 0; i<list.size();i++){
                        area.append(list.get(i).toString() + "\n");
                    }

                    System.out.println("Single Click Sort");
                }
            }
        });

        sortName.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){

                String collection = chosenFile + "";
                collection = collection.replace("\\", "/");

                if(e.getClickCount()==2){

                    area.setText("");

//                    List<Object> list = dataRepository.sortByNameCurr(outputList, "desc");
                    List<Object> list = dataRepository.sortByName(collection, "desc");

                    for(int i = 0; i<list.size();i++){
                        area.append(list.get(i).toString() + "\n");
                    }

                    System.out.println("Double Click Sort");
                }else if(e.getClickCount()==1){

                    area.setText("");

//                    List<Object> list = dataRepository.sortByNameCurr(outputList, "asc");
                    List<Object> list = dataRepository.sortByName(collection, "asc");

                    for(int i = 0; i<list.size();i++){
                        area.append(list.get(i).toString() + "\n");
                    }
                    System.out.println("Single Click Sort");
                }
            }
        });

        listAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String collection = chosenFile + "";
                collection = collection.replace("\\", "/");

                area.setText("");

                List<Entity> list = dataRepository.findAll(collection, Entity.class);
                outputList = list;

                for(int i = 0; i<list.size();i++){
                    area.append(list.get(i).toString() + "\n");
                }
                System.out.println("list All");
            }
        });

        //-----------------------------------------------------

        frame.getContentPane().add(BorderLayout.SOUTH, panel2);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
//        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
