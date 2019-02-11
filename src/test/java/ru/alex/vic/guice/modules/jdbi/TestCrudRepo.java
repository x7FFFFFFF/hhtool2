package ru.alex.vic.guice.modules.jdbi;

import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@Repository
public interface TestCrudRepo<I,T> {

    @SqlQuery("SELECT * FROM <T>")
    List<T> getAll();


}
