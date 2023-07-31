package com.mcommings.campaigner.models;

import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static java.util.Objects.isNull;

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
            String name = getNameValueAsString(object);
            Optional<T> existingRecord = (Optional<T>) findByNameMethod.invoke(repository, name);
            return existingRecord.isPresent();
        }
        catch (Exception e) {
            return false;
        }
    }

    public static <T> boolean nameIsNullOrEmpty(T object) {
        try {
            return isNull(getNameValueAsObject(object)) || getNameValueAsString(object).isEmpty();
        }
        catch (Exception e) {
            return false;
        }
    }

    private static <T> Method getRepoMethod(CrudRepository<T, Integer> repository, String methodName) throws NoSuchMethodException {
        return repository.getClass().getMethod(methodName, String.class);
    }

    private static <T> String getNameValueAsString(T object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return String.valueOf(object.getClass().getMethod("getName").invoke(object));
    }

    private static <T> Object getNameValueAsObject(T object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return object.getClass().getMethod("getName").invoke(object);
    }

}
