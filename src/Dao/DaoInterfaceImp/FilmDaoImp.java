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
    private String pathArtistFile;
    private ArtistDaoImp artistDaoImp;

    public FilmDaoImp(String path, String pathArtistFilms, String pathArtisFile) {
        this.pathArtistFilms = pathArtistFilms;
        this.path = path;
        this.pathArtistFile = pathArtisFile;
    }

    private void updateAndDeleteArtistFilms(Film film, int prevID, String prevName, boolean delete){
        BufferedReader bufferedReaderArtistFile = null;
        BufferedWriter bufferedWriterArtistFile = null;
        BufferedReader bufferedReaderArtistFilmFile = null;
        BufferedWriter bufferedWriterArtistFilmFile = null;
        String line;
        boolean flag = true;
        try{
            File fileartistfile = new File(pathArtistFile);
            File tempartistfile = new File("./tempforartistfileupdatefilms.txt");
            bufferedReaderArtistFile = new BufferedReader(new FileReader(fileartistfile));
            bufferedWriterArtistFile = new BufferedWriter(new FileWriter(tempartistfile, true));
            while ((line = bufferedReaderArtistFile.readLine()) != null) {
                bufferedWriterArtistFile.write(line.substring(0, line.lastIndexOf("/") + 1));
                for (String strfilm:
                        line.split("/")[3].split(",")) {
                    if(!strfilm.equals(prevName)){
                        bufferedWriterArtistFile.write(strfilm + ",");
                        continue;
                    }
                    else if (!delete){
                        bufferedWriterArtistFile.write(film.getFilmName() + ',');
                    }
                }
                bufferedWriterArtistFile.newLine();
            }
            bufferedReaderArtistFile.close();
            bufferedWriterArtistFile.close();
            if (!fileartistfile.delete()) System.err.println("Operation Deleting " + pathArtistFile + " unsuccessfully");
            if (!tempartistfile.renameTo(new File(pathArtistFile))) System.err.println("Operation Renaming " + pathArtistFile + " unsuccessfully");
            //delete and rename file
            File fileartistfilmfile = new File(pathArtistFilms);
            File filetempartistfilmfile = new File("./tempforartistfilmfileupdatefilms.txt");
            bufferedReaderArtistFilmFile = new BufferedReader(new FileReader(fileartistfilmfile));
            bufferedWriterArtistFilmFile = new BufferedWriter(new FileWriter(filetempartistfilmfile));
            while ((line = bufferedReaderArtistFilmFile.readLine()) != null) {
                if(!line.split("/")[0].equals(String.valueOf(prevID))){
                    bufferedWriterArtistFilmFile.write(line);
                    bufferedWriterArtistFilmFile.newLine();
                    continue;
                }
                else if (!delete){
                    bufferedWriterArtistFilmFile.write(String.valueOf(film.getFilmID()) + "/" + line.split("/")[1]);
                    bufferedWriterArtistFilmFile.newLine();
                }
            }
            //delete and rename file
            bufferedReaderArtistFilmFile.close();
            bufferedWriterArtistFilmFile.close();
            if (!fileartistfilmfile.delete()) System.err.println("Operation Deleting " + pathArtistFilms + " unsuccessfully");
            if (!filetempartistfilmfile.renameTo(new File(pathArtistFilms))) System.err.println("Operation Renaming " + pathArtistFilms + " unsuccessfully");
            flag = false;
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            try {
                if (bufferedReaderArtistFile != null && flag)
                    bufferedReaderArtistFile.close();
                if (bufferedWriterArtistFile != null && flag)
                    bufferedWriterArtistFile.close();
                if (bufferedReaderArtistFilmFile != null && flag)
                    bufferedReaderArtistFilmFile.close();
                if (bufferedWriterArtistFilmFile != null && flag)
                    bufferedWriterArtistFilmFile.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private Film stringToFilm(String str) throws Exception {
        String[] array = str.split("/");
        int filmID = Integer.valueOf(array[0].split("-")[1]);
        String filmName = array[1];
        String director = array[2];
        int year = Integer.valueOf(array[3]);
        String genre = array[4];
        Film film = new Film(filmID, filmName, director, year, genre);
        film.setIndex(Long.valueOf(array[0].split("-")[0]));
        return film;
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
        Film film = null;
        try {
            if (findByID(String.valueOf(ID)) == null)
                return;
            film = findByID(String.valueOf(ID));
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
                if (film != null){
                    updateAndDeleteArtistFilms(film, ID, film.getFilmName(), true);
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void update(Film film, int prevID) {
        if (findByID(String.valueOf(film.getFilmID())) != null) {
            System.err.println("From FilmDao(UpdateMethod): This Film Already Exist");
            return;
        }
        if (findByID(String.valueOf(prevID)) == null){
            System.err.println("This Film Does Not Exist");
            return;
        }
        Film prevfilm = findByID(String.valueOf(prevID));
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
                if (prevfilm != null){
                    updateAndDeleteArtistFilms(film, prevID, prevfilm.getFilmName(), false);
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

    private List<Artist> getArtisOfFilms(Film film){
        BufferedReader bufferedReader = null;
        List<Artist> allartist = new ArrayList<>();
        String line;
        Artist temp = null;
        try {
            File file = new File(pathArtistFilms);
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split("/")[0].equals(String.valueOf(film.getFilmID()))){
                    temp =artistDaoImp.findByID(line.split("/")[1]);
                    allartist.add(temp);
                }
            }
        }catch (Exception ex){
            System.err.println("From FilmDao(UpdateMethod): " + ex.getMessage());
        }finally {
            try {
                if (bufferedReader != null){
                    bufferedReader.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return allartist;
    }

    public List<String> getAllinfo(String name){
        List<String> allinfo = new ArrayList<>();
        String query;
        for (Film film:
             findByName(name)) {
            query = film.getFilmName() + "/";
            for (Artist artist:
                 getArtisOfFilms(film)) {
                query += artist.getArtistName() + ",";
            }
            query += "/" + film.getDirectorName() + "/";
            query += String.valueOf(film.getProductionYear()) + "/";
            query += film.getGenre();
            allinfo.add(query);
            query = "";
        }
        return allinfo;
    }

    public void setArtistDaoImp(ArtistDaoImp artistDaoImp) {
        this.artistDaoImp = artistDaoImp;
    }
}
