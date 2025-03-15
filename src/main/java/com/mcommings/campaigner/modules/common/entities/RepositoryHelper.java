package com.mcommings.campaigner.modules.common.entities;

import lombok.SneakyThrows;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;
import static com.mcommings.campaigner.enums.ErrorMessage.RH_UNABLE_TO_PROCESS_FOREIGN_KEY_LOOKUP;
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
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> boolean nameAlreadyExists(CrudRepository<T, Integer> repository, String name) {
        try {
            Method findByNameMethod = getRepoMethod(repository, "findByName");
            Optional<T> existingRecord = (Optional<T>) findByNameMethod.invoke(repository, name);
            return existingRecord.isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> boolean nameIsNullOrEmpty(T object) {
        try {
            return isNull(getNameValueAsObject(object)) || getNameValueAsString(object).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> boolean isForeignKey(List<CrudRepository> repositories, String columnName, int id) {

        return repositories.stream().anyMatch(r -> {
            String fkFind = getForeignKeyColumnMethodName(r, columnName);
            return containsForeignKeyValues(r, fkFind, id);
        });
    }

    public static <T> boolean foreignKeyIsNotValid(CrudRepository<T, Integer> repository,
                                                   List<CrudRepository> repositories,
                                                   Object requestItem) {
        List<String> names = getListOfForeignKeyColumnNames(requestItem);
        HashMap<CrudRepository, String> reposAndColumns = createReposAndColumnsHashMap(repositories, names);

        return !reposAndColumns.entrySet().stream().allMatch(entry -> {
            String getterName = retrieveNameOfForeignKeyGetterMethod(requestItem, entry);
            return isTheRecordAForeignKeyForThisRepo(entry, getterName, requestItem);
        });
    }

    public static <T> boolean foreignKeyIsNotValid(HashMap<CrudRepository, String> reposAndColumns, Object requestItem) {
        return !reposAndColumns.entrySet().stream().allMatch(entry -> {
            String getterName = retrieveNameOfForeignKeyGetterMethod(requestItem, entry);
            return isTheRecordAForeignKeyForThisRepo(entry, getterName, requestItem);
        });
    }

    private static String getForeignKeyColumnMethodName(CrudRepository r, String columnName) {
        return Arrays.stream(r.getClass().getDeclaredMethods()).filter(m ->
                doesTheMethodContainColumnName(m, columnName)
        ).findFirst().get().getName();
    }

    private static boolean doesTheMethodContainColumnName(Method m, String columnName) {
        return m.getName().toUpperCase().contains(columnName.toUpperCase());
    }

    @SneakyThrows
    private static boolean containsForeignKeyValues(CrudRepository r, String fkFind, int id) {
        List<Object> value = (List<Object>) r.getClass().getMethod(fkFind, Integer.class).invoke(r, id);
        return value != null && value.size() > 0;
    }

    private static <T> List<String> getListOfForeignKeyColumnNames(T record) {
        Field[] fields = record.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames.stream().filter(n -> n.contains("fk_") && !n.equals("fk_campaign_uuid")).collect(Collectors.toList());
    }

    private static HashMap<CrudRepository, String> createReposAndColumnsHashMap(List<CrudRepository> repositories,
                                                                                List<String> names) {
        if (repositories.size() != names.size()) {
            throw new IllegalArgumentException(RH_UNABLE_TO_PROCESS_FOREIGN_KEY_LOOKUP.message);
        }

        HashMap<CrudRepository, String> reposAndColumns = new HashMap<>();
        for (int i = 0; i < repositories.size(); i++) {
            reposAndColumns.put(repositories.get(i), names.get(i));
        }
        return reposAndColumns;
    }

    private static <T> String retrieveNameOfForeignKeyGetterMethod(T record, Map.Entry<CrudRepository, String> entry) {
        return Arrays.stream(record.getClass().getDeclaredMethods()).filter(m ->
                methodHasAGetterThatMatchesForeignKeyColumnName(m, entry)
        ).findFirst().get().getName();
    }

    private static boolean methodHasAGetterThatMatchesForeignKeyColumnName(Method m, Map.Entry<CrudRepository, String> entry) {
        return m.getName().contains("get") &&
                m.getName().toUpperCase().contains(String.valueOf(entry.getValue()).toUpperCase());
    }

    private static <T> boolean isTheRecordAForeignKeyForThisRepo(Map.Entry<CrudRepository, String> entry,
                                                                 String getterName,
                                                                 T record) {
        Object value = getForeignKeyIdValueFromMethod(entry, getterName, record);
        if (isNull(value)) {
            throw new IllegalArgumentException(ID_NOT_FOUND.message);
        }

        return doesForeignKeyExist(entry, value);
    }

    @SneakyThrows
    private static <T> Object getForeignKeyIdValueFromMethod(Map.Entry<CrudRepository, String> entry,
                                                         String getterName,
                                                         T record) {
        return record.getClass().getMethod(getterName).invoke(record);
    }

    private static boolean foreignKeyIsNotFound(Map.Entry<CrudRepository, String> entry, Object value) {
        return !entry.getKey().findById(value).isPresent();
    }

    private static boolean doesForeignKeyExist(Map.Entry<CrudRepository, String> entry, Object value) {
        return entry.getKey().existsById(value);
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
