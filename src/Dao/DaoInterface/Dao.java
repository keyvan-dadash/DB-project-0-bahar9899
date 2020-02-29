package Dao.DaoInterface;


import java.util.List;

public interface Dao<T> {

    void save(T t);
    void deleteItem(int ID);
    void update(T t);
    List<T> findByName(String name);
    T findByID(String name);
}
