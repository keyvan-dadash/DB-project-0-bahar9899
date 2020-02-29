package Dao.DaoInterface;


import java.util.List;

public interface Dao<T> {

    void save(T t);
    void deleteFilm(T t);
    void update(T t);
    List<T> findByName(String name);
    List<T> findByID(String name);
}
