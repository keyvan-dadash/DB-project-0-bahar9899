package Dao.DaoInterfaceImp;

import Dao.DaoInterface.Dao;
import Models.Artist;
import Models.Film;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDaoImp implements Dao<Film> {

    private String path;

    public FilmDaoImp(String path) {
        this.path = path;
    }


    private Film stringToFilm(String str) throws Exception {
        String[] array = str.split("/");
        int filmID = Integer.valueOf(array[0].split("-")[1]);
        String filmName = array[1];
        String director = array[2];
        int year = Integer.valueOf(array[3]);
        String genre = array[4];
        return new Film(filmID, filmName, director, year, genre);
    }

    @Override
    public void save(Film film) {
        try {
            File file = new File(path);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            String line = new String();
            line += film.getIndex() + '-';
            line += film.getFilmID() + '/';
            line += film.getDirectorName() + '/';
            line += film.getProductionYear() + '/';
            line += film.getGenre();
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteItem(int ID) {
        String line;
        try {
            File file = new File(path);
            File tempfile = new File("tempppp.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempfile));
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.contains(String.valueOf(ID))){
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }
            if(!file.delete()) throw new Exception("Deleting File " + path + " Operation unsuccessfully");
            if(!tempfile.renameTo(new File(path))) throw new Exception("Renaming File Temp TO " + path + " Operation Unsuccessfully");
        }catch (Exception ex){
            System.err.println("From FilmDao(DeleteItemMethod): " + ex.getMessage());
        }
    }

    @Override
    public void update(Film film) {
        String newFilm = new String();
        newFilm += film.getIndex() + '-';
        newFilm += film.getFilmID() + '/';
        newFilm += film.getDirectorName() + '/';
        newFilm += film.getProductionYear() + '/';
        newFilm += film.getGenre();
        String line;
        try {
            File file = new File(path);
            File tempfile = new File("temppp.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempfile));
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.contains(String.valueOf(film.getFilmID()))){
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }else{
                    bufferedWriter.write(newFilm);
                    bufferedWriter.newLine();
                }
            }
            if(!file.delete()) throw new Exception("Deleting File " + path + " Operation unsuccessfully");
            if(!tempfile.renameTo(new File(path))) throw new Exception("Renaming File Temp TO " + path + " Operation Unsuccessfully");
        }catch (Exception ex){
            System.err.println("From FilmDao(UpdateMethod): " + ex.getMessage());
        }
    }

    @Override
    public List<Film> findByName(String name) {
        String line;
        List<Film> films = new ArrayList<>();
        try {
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split("/")[1].equals(name)){
                    films.add(stringToFilm(line));
                }
            }
        }catch (Exception ex){
            System.err.println("From FilmDao(FindByNameMethod): " + ex.getMessage());
        }
        return films;
    }

    @Override
    public Film findByID(String name) {
        String line;
        Film film = null;
        try {
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split("/")[1].equals(name)){
                    film = stringToFilm(line);
                    break;
                }
            }
        }catch (Exception ex){
            System.err.println("From ArtistDao(FindByIDMethod): " + ex.getMessage());
        }
        return film;
    }
}
