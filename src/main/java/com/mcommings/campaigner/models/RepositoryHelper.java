package com.mcommings.campaigner.models;

import org.springframework.data.repository.CrudRepository;

public class RepositoryHelper {

    public static <T> boolean cannotFindId(CrudRepository<T, Integer> repository, int id) {
        return !repository.existsById(id);
    }

    public static <T> T getById(CrudRepository<T, Integer> repository, int id) {
        return repository.findById(id).get();
    }

}
