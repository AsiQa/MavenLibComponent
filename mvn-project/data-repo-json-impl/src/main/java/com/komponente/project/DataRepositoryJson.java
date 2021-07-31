package com.komponente.project;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class DataRepositoryJson implements DataRepository {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void save(String collection, Object entity, String id) {

        File file = new File(collection);
        boolean exists = false;

        try {

            List<Object> objects = objectMapper.readValue(file, new TypeReference<List<Object>>() {
            });

            for (int i = 0; i < objects.size(); i++) {
                JsonNode josnNode = objectMapper.valueToTree(objects.get(i));
                if (josnNode.get("id").asText().equals(id)) {
                    System.out.println("ID already exists");
                    exists = true;
                }
            }

            if (!exists) {
                if (objects.size() + 1 > 10) {
                    Random random = new Random();
                    int num = random.nextInt(1000);

                    String desktopPath = System.getProperty("user.home");
                    String updatedPath = desktopPath.replace("\\", "/");
                    updatedPath = updatedPath + "\\Desktop\\fileNew" + num + ".txt";

                    try {
                        File newFile = new File(updatedPath);
                        objectMapper.writeValue(new File(updatedPath), entity);

                        if (newFile.createNewFile()) {
                            System.out.println("File created: " + newFile.getName());
                        } else {
                            System.out.println("File already exists.");
                        }
                    } catch (IOException eo) {
                        System.out.println("An error occurred.");
                        eo.printStackTrace();
                    }

                } else {
                    objects.add(entity);
                    objectMapper.writeValue(file, objects);
                    System.out.println("Object added");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Storage error");
        }
    }

    @Override
    public <T> T findById(String collection, String id, Class<T> type) {
        try {
            File file = new File(collection);
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
            List<T> objects = objectMapper.readValue(file, javaType);
            return objects.stream().filter(object -> {
                JsonNode josnNode = objectMapper.valueToTree(object);
                return josnNode.get("id").asText().equals(id);
            }).findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Storage error.");
        }
    }

    @Override
    public <T> List<T> findByName(String collection, String name, Class<T> type) {
        File file = new File(collection);
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
        List<T> list = new ArrayList<>();

        try {
            List<T> objects = objectMapper.readValue(file, javaType);
            for(int i = 0; i<objects.size();i++){
                JsonNode josnNode = objectMapper.valueToTree(objects.get(i));
                if(josnNode.get("naziv").asText().equals(name)) {
                    list.add(objects.get(i));
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Storage error.");
        }
    }

    @Override
    public <T> List<T> findAll(String collection, Class<T> type) {
        try {
            File file = new File(collection);
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
            return objectMapper.readValue(file, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Storage error.");
        }
    }

    @Override
    public <T> void deleteById(String collection, String id, Class<T> type) {
        File file = new File(collection);

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
        try {
            List<T> objects = objectMapper.readValue(file, javaType);

            for(int i = 0; i<objects.size();i++){
                JsonNode josnNode = objectMapper.valueToTree(objects.get(i));
                if(josnNode.get("id").asText().equals(id)) {
                    objects.remove(objects.get(i));
                }
            }

            objectMapper.writeValue(new File(collection), objects);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Storage error.");
        }
    }

    @Override
    public <T> void deleteByName(String collection, String name, Class<T> type) {
        File file = new File(collection);

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
        try {
            List<T> objects = objectMapper.readValue(file, javaType);

            for(int i = 0; i<objects.size();i++){
                JsonNode josnNode = objectMapper.valueToTree(objects.get(i));
                if(josnNode.get("naziv").asText().equals(name)) {
                    objects.remove(objects.get(i));
                }
            }

            objectMapper.writeValue(new File(collection), objects);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Storage error.");
        }

    }

    @Override
    public <T> List<T> sortById(String collection, String how) {

        File file = new File(collection);
        List<T> output = new ArrayList<>();

        try {
            JsonNode node = objectMapper.readTree(file);
            ArrayNode array = (ArrayNode) node;
            Iterator<JsonNode> i =array.elements();
            List<JsonNode> list = new ArrayList<>();
            while(i.hasNext()){
                list.add(i.next());
            }

            list.sort(Comparator.comparing(o -> o.get("id").asText()));

            if(how.equals("desc")){
                Collections.reverse(list);
            }

            output = (List<T>) list;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    @Override
    public <T> List<T> sortByName(String collection, String how) {

        File file = new File(collection);
        List<T> output = new ArrayList<>();

        try {
            JsonNode node = objectMapper.readTree(file);
            ArrayNode array = (ArrayNode) node;
            Iterator<JsonNode> i =array.elements();
            List<JsonNode> list = new ArrayList<>();
            while(i.hasNext()){
                list.add(i.next());
            }

            list.sort(Comparator.comparing(o -> o.get("naziv").asText()));

            if(how.equals("desc")){
                Collections.reverse(list);
            }

            output = (List<T>) list;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    @Override
    public <T> List<T> sortByIdCurr (List<T> list, String how) {

        List<T> output = new ArrayList<>();

        List<JsonNode> list2 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            JsonNode josnNode = objectMapper.valueToTree(list.get(i));
            list2.add(josnNode);
        }

        list2.sort(Comparator.comparing(o -> o.get("id").asText()));

        if(how.equals("desc")){
            Collections.reverse(list2);
        }

        output = (List<T>) list2;

        return output;
    }

    @Override
    public <T> List<T> sortByNameCurr(List<T> list, String how) {
        List<T> output = new ArrayList<>();

        List<JsonNode> list2 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            JsonNode josnNode = objectMapper.valueToTree(list.get(i));
            list2.add(josnNode);
        }

        list2.sort(Comparator.comparing(o -> o.get("naziv").asText()));

        if(how.equals("desc")){
            Collections.reverse(list2);
        }

        output = (List<T>) list2;

        return output;
    }

    @Override
    public String extension() {
        return (".json");
    }
}
