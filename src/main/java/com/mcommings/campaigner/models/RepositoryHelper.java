package com.mcommings.campaigner.models;

import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class RepositoryHelper {

    public static <T> boolean cannotFindId(CrudRepository<T, Integer> repository, int id) {
        return !repository.existsById(id);
    }

    public static <T> T getById(CrudRepository<T, Integer> repository, int id) {
        return repository.findById(id).get();
    }

    public static <T> boolean nameAlreadyExists(CrudRepository<T, Integer> repository, T object) {
        try {
            Method findByNameMethod = getRepoMethod(repository, "findByName");
            String name = getNameValue(object);
            Optional<T> existingRecord = (Optional<T>) findByNameMethod.invoke(repository, name);
            return existingRecord.isPresent();
        }
        catch (Exception e) {
            return false;
        }
    }

    private static <T> Method getRepoMethod(CrudRepository<T, Integer> repository, String methodName) throws NoSuchMethodException {
        return repository.getClass().getMethod(methodName, String.class);
    }

    private static<T> String getNameValue(T object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return String.valueOf(object.getClass().getMethod("getName").invoke(object));
    }

}
