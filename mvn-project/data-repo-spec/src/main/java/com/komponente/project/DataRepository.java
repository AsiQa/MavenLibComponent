package com.komponente.project;

import java.io.IOException;
import java.util.List;

public interface DataRepository {

    /**
     * Save object in the specified collection of the storage.
     *
     * @param collection name of the collection
     * @param object     data
     */
    void save(String collection, Object object, String id);


    /**
     * Get the object with the specified id.
     *
     * @param collection name of the collection
     * @param id         id of the object we want to get
     * @param type       type of the object
     * @return object with specified id
     */
    <T> T findById(String collection, String id, Class<T> type);


    /**
     * Get the object with the specified name.
     *
     * @param collection name of the collection
     * @param name       name of the object we want to get
     * @param type       type of the object
     * @return object with specified id
     */
    <T> List<T> findByName(String collection, String name, Class<T> type);


    /**
     * Get all objects from specified collection
     *
     * @param collection name of the collection
     * @param type       of objects
     * @return list off objects from specified collection
     */
    <T> List<T> findAll(String collection, Class<T> type);


    /**
     * Delete the object with the specified id.
     *
     * @param collection name of the collection
     * @param id         id of the object we want to get
     * @param type       type of the object
     */
    <T> void deleteById(String collection, String id, Class<T> type);


    /**
     * Delete objects with the specified name.
     *
     * @param collection name of the collection
     * @param name       id of the object we want to get
     * @param type       type of the object
     */
    <T> void deleteByName(String collection, String name, Class <T> type);


    /**
     * Sort objects by id.
     *
     * @param collection name of the collection.
     * @param how        ascending/descending
     * @return sorted list by id.
     */
    <T> List<T> sortById(String collection, String how);


    /**
     * Sort objects by name.
     *
     * @param collection name of the collection
     * @param how        ascending/descending
     * @return sorted list by name.
     */
    <T> List<T> sortByName(String collection, String how);


    /**
     * Sort objects by name.
     *
     * @param list       currently displayed list
     * @param how        ascending/descending
     * @return sorted list by id.
     */
    <T> List<T> sortByIdCurr(List<T> list, String how);


    /**
     * Sort objects by name.
     *
     * @param list       currently displayed list
     * @param how        ascending/descending
     * @return sorted list by name.
     */
    <T> List<T> sortByNameCurr(List<T> list, String how);


    /**
     * Get the extension based on file type
     * @return extension string
     */
    String extension();
}
