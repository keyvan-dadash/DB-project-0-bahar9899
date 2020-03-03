package Dao.DaoInterfaceImp;

import Dao.DaoInterface.Dao;
import Exceptions.FilmExceptions.DuplicateFilmException;
import Models.Artist;
import Models.Film;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDaoImp implements Dao<Film> {

    private String path;
    private String pathArtistFilms;

    public FilmDaoImp(String path, String pathArtistFilms) {
        this.pathArtistFilms = pathArtistFilms;
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
        BufferedWriter bufferedWriter = null;
        try {
            if(findByID(String.valueOf(film.getFilmID())) != null){
                throw new DuplicateFilmException("This Film Already Exist");
            }
            File file = new File(path);
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            String line = new String();
            line += String.valueOf(film.getIndex()) + '-';
            line += String.valueOf(film.getFilmID()) + '/';
            line += film.getFilmName() + '/';
            line += film.getDirectorName() + '/';
            line += String.valueOf(film.getProductionYear()) + '/';
            line += film.getGenre();
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (DuplicateFilmException fx) {
            System.err.println("From FilmDao(Save Method): " + fx);
        }finally {
            if (bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void deleteItem(int ID) {
        String line;
        boolean flag = true;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            if (findByID(String.valueOf(ID)) == null)
                return;
            File file = new File(path);
            File tempfile = new File("tempfordeletefilm.txt");
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedWriter = new BufferedWriter(new FileWriter(tempfile));
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.split("/")[0].split("-")[1].equals(String.valueOf(ID))){
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }
            bufferedReader.close();
            bufferedWriter.close();
            if(!file.delete()) throw new Exception("Deleting File " + path + " Operation unsuccessfully");
            if(!tempfile.renameTo(new File(path))) throw new Exception("Renaming File Temp TO " + path + " Operation Unsuccessfully");
            flag = false;
        }catch (Exception ex){
            System.err.println("From FilmDao(DeleteItemMethod): " + ex.getMessage());
        }finally {
            try {
                if (bufferedReader != null && flag){
                    bufferedReader.close();
                }
                if (bufferedWriter != null && flag){
                    bufferedWriter.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void update(Film film, int prevID) {
        if (findByID(String.valueOf(film.getFilmID())) != null)
            System.err.println("From FilmDao(UpdateMethod): This Film Already Exist");
        if (findByID(String.valueOf(prevID)) == null){
            System.err.println("This Film Does Not Exist");
        }
        String newFilm = new String();
        newFilm += String.valueOf(film.getIndex()) + '-';
        newFilm += String.valueOf(film.getFilmID()) + '/';
        newFilm += film.getFilmName() + '/';
        newFilm += film.getDirectorName() + '/';
        newFilm += String.valueOf(film.getProductionYear()) + '/';
        newFilm += film.getGenre();
        String line;
        boolean flag = true;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            File file = new File(path);
            File tempfile = new File("tempforupdatefilm.txt");
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedWriter = new BufferedWriter(new FileWriter(tempfile));
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.split("/")[0].split("-")[1].equals(String.valueOf(prevID))){
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }else{
                    bufferedWriter.write(newFilm);
                    bufferedWriter.newLine();
                }
            }
            bufferedReader.close();
            bufferedWriter.close();
            if(!file.delete()) throw new Exception("Deleting File " + path + " Operation unsuccessfully");
            if(!tempfile.renameTo(new File(path))) throw new Exception("Renaming File Temp TO " + path + " Operation Unsuccessfully");
            flag = false;
        }catch (Exception ex){
            System.err.println("From FilmDao(UpdateMethod): " + ex.getMessage());
        }finally {
            try {
                if (bufferedReader != null && flag){
                    bufferedReader.close();
                }
                if (bufferedWriter != null && flag){
                    bufferedWriter.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<Film> findByName(String name) {
        String line;
        List<Film> films = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            File file = new File(path);
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split("/")[1].contains(name)){
                    films.add(stringToFilm(line));
                }
            }
        }catch (Exception ex){
            System.err.println("From FilmDao(FindByNameMethod): " + ex.getMessage());
        }finally {
            try {
                if (bufferedReader != null){
                    bufferedReader.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return films;
    }

    @Override
    public Film findByID(String name) {
        String line;
        Film film = null;
        BufferedReader bufferedReader = null;
        try {
            File file = new File(path);
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split("/")[0].split("-")[1].equals(name)){
                    film = stringToFilm(line);
                    break;
                }
            }
        }catch (Exception ex){
            System.err.println("From ArtistDao(FindByIDMethod): " + ex.getMessage());
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return film;
    }
}
