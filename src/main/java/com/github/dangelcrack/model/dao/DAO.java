package com.github.dangelcrack.model.dao;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

/**
 * Generic Data Access Object (DAO) interface that defines the basic operations
 * for interacting with a database. It extends Closeable to allow for resource cleanup.
 *
 * @param <T> The type of the entity.
 * @param <K> The type of the key used to find entities.
 */
public interface DAO<T, K> extends Closeable {

    /**
     * Saves the given entity to the database.
     *
     * @param entity The entity to save.
     * @return The saved entity.
     */
    T save(T entity);

    /**
     * Deletes the given entity from the database.
     *
     * @param entity The entity to delete.
     * @return The deleted entity.
     * @throws SQLException If a database access error occurs.
     */
    T delete(T entity) throws SQLException;

    /**
     * Finds an entity by its key.
     *
     * @param key The key to search for.
     * @return The found entity, or null if not found.
     */
    T findByName(K key);

    /**
     * Finds all entities in the database.
     *
     * @return A list of all entities.
     */
    List<T> findAll();
}
