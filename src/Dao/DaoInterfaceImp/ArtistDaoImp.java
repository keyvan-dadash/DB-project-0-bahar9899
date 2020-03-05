package Dao.DaoInterfaceImp;

import Dao.DaoInterface.Dao;
import Exceptions.ArtistExceptions.DuplicateAtristException;
import IOHelper.ReadLineHelper;
import Models.Artist;
import Models.Film;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArtistDaoImp implements Dao<Artist> {

    private String path;
    private long lines = 1;
    private String pathForArtistID;
    private String pathForArtistName;
    private String pathForArtistAndFilm;
    private HashMap<Integer , Integer> indexByArtistID = new HashMap<>();
    private HashMap<String , String> indexByArtistName = new HashMap<>();
    private FilmDaoImp filmDaoImp;

    //fuck check this dublicate name
    public ArtistDaoImp(String path, String pathForArtistID, String pathForArtistName, FilmDaoImp filmDaoImp, String pathForArtistAndFilm) {
        this.path = path;
        this.filmDaoImp = filmDaoImp;
        this.pathForArtistID = pathForArtistID;
        this.pathForArtistName = pathForArtistName;
        this.pathForArtistAndFilm = pathForArtistAndFilm;
        BufferedReader bufferedReaderID = null;
        BufferedReader bufferedReaderName = null;
        obtainIndex();
    }

    private void obtainIndex(){
        indexByArtistName.clear();
        indexByArtistID.clear();
        String line;
        BufferedReader bufferedReaderID = null;
        BufferedReader bufferedReaderName = null;
        try{
            bufferedReaderID = new BufferedReader(new FileReader(new File(pathForArtistID)));
            bufferedReaderName = new BufferedReader(new FileReader(new File(pathForArtistName)));
            while ((line = bufferedReaderID.readLine()) != null) {
                indexByArtistID.put(Integer.parseInt(line.split("->")[0]), Integer.parseInt(line.split("->")[1]));
            }

            while ((line = bufferedReaderName.readLine()) != null) {
                indexByArtistName.put(line.split("->")[0], line.split("->")[1]);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            try{
                if (bufferedReaderID != null){
                    bufferedReaderID.close();
                }
                if (bufferedReaderName != null){
                    bufferedReaderName.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void updateIndex(){
        indexByArtistName.clear();
        indexByArtistID.clear();
        String line;
        int locallines = 1;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriterID = null;
        BufferedWriter bufferedWriterName = null;
        try{
            File file = new File(pathForArtistName);
            File file1 = new File(pathForArtistID);
            if(!file.delete()) throw new Exception("File " + pathForArtistName + " Can not be Delete");
            if(!file1.delete()) throw new Exception("File " + pathForArtistID + " Can not be Delete");
            bufferedReader = new BufferedReader(new FileReader(new File(path)));
            bufferedWriterID = new BufferedWriter(new FileWriter(new File(pathForArtistID)));
            bufferedWriterName = new BufferedWriter(new FileWriter(new File(pathForArtistName)));
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriterID.write(line.split("/")[0].split("-")[1] + "->" + String.valueOf(locallines));
                bufferedWriterID.newLine();
                indexByArtistID.put(Integer.valueOf(line.split("/")[0].split("-")[1]), locallines);
                bufferedWriterName.write(line.split("/")[1] + "->" + String.valueOf(locallines));
                bufferedWriterName.newLine();
                if (indexByArtistName.containsKey(line.split("/")[1])){
                    String liness = indexByArtistName.get(line.split("/")[1]);
                    liness += ";" + String.valueOf(locallines);
                    indexByArtistName.replace(line.split("/")[1], liness);
                    locallines++;
                    continue;
                }
                indexByArtistName.put(line.split("/")[1], String.valueOf(locallines));
                locallines++;
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            System.err.println("From ArtistDao(UpdateIndexMethod): " + ex.getMessage());
        }finally {
            try{
                if (bufferedReader != null){
                    bufferedReader.close();
                }
                if (bufferedWriterName != null){
                    bufferedWriterName.close();
                }
                if (bufferedWriterID != null){
                    bufferedWriterID.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void insertArtistFilms(Artist artist){
        File file;
        BufferedWriter bufferedWriter = null;
        try{
            file = new File(pathForArtistAndFilm);
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            //Film film;
            for (String str:
                 artist.getArtistFilms()) {
                for (Film film:
                     filmDaoImp.findByName(str)) {
                    bufferedWriter.write(String.valueOf(film.getFilmID()) + "/" + String.valueOf(artist.getArtistID()));
                    bufferedWriter.newLine();
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            if(bufferedWriter!=null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteArtistFilms(int ID){
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        boolean flag = true;
        try {
            String line;
            File file = new File(pathForArtistAndFilm);
            File tempfile = new File("tempfordeleteArtistFilms.txt");
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedWriter = new BufferedWriter(new FileWriter(tempfile));
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.split("/")[1].equals(String.valueOf(ID))){
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }
            bufferedReader.close();
            bufferedWriter.close();
            if(!file.delete()) throw new Exception("Deleting File " + pathForArtistAndFilm + " Operation unsuccessfully");
            if(!tempfile.renameTo(new File(pathForArtistAndFilm))) throw new Exception("Renaming File Temp TO " + pathForArtistAndFilm + " Operation Unsuccessfully");
            flag = false;
        }catch (Exception ex){
            System.err.println("From ArtistDao(DeleteArtistFilmsMethod): " + ex.getMessage());
        }finally {
            try {
                if(bufferedReader != null && flag){
                    bufferedReader.close();
                }
                if(bufferedWriter != null && flag){
                    bufferedWriter.close();
                }
                if (bufferedReader != null && bufferedWriter != null && !flag){
                    updateIndex();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void updateArtistFilms(Artist artist, int ID){
        String line;
        boolean flag = true;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            File file = new File(pathForArtistAndFilm);
            File tempfile = new File("tempforupdateartistfilms.txt");
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedWriter = new BufferedWriter(new FileWriter(tempfile));
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.split("/")[1].equals(String.valueOf(ID))){
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }else{
                    bufferedWriter.write(line.split("/")[0] + "/" + artist.getArtistID());
                    bufferedWriter.newLine();
                }
            }
            bufferedReader.close();
            bufferedWriter.close();
            if(!file.delete()) throw new Exception("Deleting File " + pathForArtistAndFilm + " Operation unsuccessfully");
            if(!tempfile.renameTo(new File(pathForArtistAndFilm))) throw new Exception("Renaming File Temp TO " + pathForArtistAndFilm + " Operation Unsuccessfully");
            flag = false;
        }catch (Exception ex){
            System.err.println("From ArtistDao(UpdateArtistFilmsMethod): " + ex.getMessage());
        }finally {
            try {
                if(bufferedReader != null && flag){
                    bufferedReader.close();
                }
                if(bufferedWriter != null && flag){
                    bufferedWriter.close();
                }
                if (bufferedReader != null && bufferedWriter != null && !flag){
                    updateIndex();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private Artist stringToArtist(String str) throws Exception {
        String[] array = str.split("/");
        int index = Integer.valueOf(array[0].split("-")[0]);
        int artistID = Integer.valueOf(array[0].split("-")[1]);
        String artistName = array[1];
        int age = Integer.valueOf(array[2]);
        List<String> films = new ArrayList<>();
        if (array.length != 3){
            for (String string:
                    array[3].split(",")) {
                films.add(string);
            }
        }
        Artist temp = new Artist(artistID, age, artistName, films);
        temp.setIndex(index);
        return temp;
    }

    //fuck again check dublicate name
    @Override
    public void save(Artist artist) {
        File file;
        BufferedWriter bufferedWriter = null;
        try {
            if (findByID(String.valueOf(artist.getArtistID())) != null){
                throw new DuplicateAtristException("This Artist Already Exist");
            }
            for (String films:
                 artist.getArtistFilms()) {
                if (filmDaoImp.findByName(films).size() == 0){
                    System.err.println(films + " Does Not Exist");
                    return;
                }
            }
            file = new File(path);
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            String line = new String();
            line += String.valueOf(artist.getIndex()) + '-';
            line += String.valueOf(artist.getArtistID()) + '/';
            line += artist.getArtistName() + '/';
            line += String.valueOf(artist.getAge()) + '/';
            for (String str :
                    artist.getArtistFilms()) {
                line += str + ',';
            }
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            indexByArtistID.put(artist.getArtistID(), (int) lines);
            if (indexByArtistName.containsKey(artist.getArtistName())){
                String liness = indexByArtistName.get(artist.getArtistName());
                liness += ";" + String.valueOf(lines);
                indexByArtistName.replace(line.split("/")[1], liness);
            }else{
                indexByArtistName.put(artist.getArtistName(), String.valueOf(lines));
            }
            lines++;
            insertArtistFilms(artist);
        }catch (IOException e) {
            e.printStackTrace();
        }catch (DuplicateAtristException ax) {
            System.err.println("From ArtistDaoImp(SaveMethod): " + ax);
        } finally {
            if(bufferedWriter != null){
                try {
                    bufferedWriter.close();
                    updateIndex();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void deleteItem(int ID) {
        String line;
        String name = null;
        boolean flag = true;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            if (findByID(String.valueOf(ID)) == null)
                return;
            File file = new File(path);
            File tempfile = new File("tempfordeleteartist.txt");
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedWriter = new BufferedWriter(new FileWriter(tempfile));
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.split("/")[0].split("-")[1].equals(String.valueOf(ID))){
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }else{
                    name = line.split("/")[1];
                }
            }
            bufferedReader.close();
            bufferedWriter.close();
            if(!file.delete()) throw new Exception("Deleting File " + path + " Operation unsuccessfully");
            if(!tempfile.renameTo(new File(path))) throw new Exception("Renaming File Temp TO " + path + " Operation Unsuccessfully");
            flag = false;
            updateIndex();
            lines--;
            deleteArtistFilms(ID);
        }catch (Exception ex){
            System.err.println("From ArtistDao(DeleteItemMethod): " + ex.getMessage());
        }finally {
            try{
                if(bufferedReader!=null && flag){
                    bufferedReader.close();
                }
                if(bufferedWriter!=null && flag){
                    bufferedWriter.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Artist artist, int prevID) {
        if (findByID(String.valueOf(artist.getArtistID())) != null)
            System.err.println("From ArtistDao(UpdateMethod): This Artist Already Exist");
        if(findByID(String.valueOf(prevID)) == null){
            System.err.println("This Artist Does Not Exist");
        }
        String newArtist = new String();
        newArtist += String.valueOf(artist.getIndex()) + '-';
        newArtist += String.valueOf(artist.getArtistID()) + '/';
        newArtist += artist.getArtistName() + '/';
        newArtist += String.valueOf(artist.getAge()) + '/';
        for (String str :
                artist.getArtistFilms()) {
            newArtist += str + ',';
        }
        String line;
        boolean flag = true;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            File file = new File(path);
            File tempfile = new File("tempforupdateartist.txt");
            bufferedReader = new BufferedReader(new FileReader(file));
            bufferedWriter = new BufferedWriter(new FileWriter(tempfile));
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.split("/")[0].split("-")[1].equals(String.valueOf(prevID))){
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }else{
                    bufferedWriter.write(newArtist);
                    bufferedWriter.newLine();
                }
            }
            bufferedReader.close();
            bufferedWriter.close();
            if(!file.delete()) throw new Exception("Deleting File " + path + " Operation unsuccessfully");
            if(!tempfile.renameTo(new File(path))) throw new Exception("Renaming File Temp TO " + path + " Operation Unsuccessfully");
            flag = false;
            updateArtistFilms(artist, prevID);
        }catch (Exception ex){
            System.err.println("From ArtistDao(UpdateMethod): " + ex.getMessage());
        }finally {
            try {
                if(bufferedReader != null && flag){
                    bufferedReader.close();
                }
                if(bufferedWriter != null && flag){
                    bufferedWriter.close();
                }
                if (bufferedReader != null && bufferedWriter != null && !flag){
                    updateIndex();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<Artist> findByName(String name) {
        List<Artist> artists = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            if (indexByArtistName.get(name) != null){
                String str = indexByArtistName.get(name);
                String[] lines = str.split(";");
                String artist = null;
                for (String line:
                     lines) {
                    artist = ReadLineHelper.readLine(Long.valueOf(line), path);
                    artists.add(stringToArtist(artist));
                }
            }
            if (artists.size() == 0){
                String line;
                File file = new File(path);
                bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    if(line.split("/")[1].contains(name)){
                        artists.add(stringToArtist(line));
                    }
                }
            }
        }catch (Exception ex){
            System.err.println("From ArtistDao(FindByNameMethod): " + ex.getMessage());
        }finally {
            try{
                if (bufferedReader != null){
                    bufferedReader.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return artists;
    }

    @Override
    public Artist findByID(String name) {
        String line;
        Artist artists = null;
        BufferedReader bufferedReader = null;
        try {
            File file = new File(path);
            if (indexByArtistID.get(Integer.valueOf(name)) != null)
                artists = stringToArtist(ReadLineHelper.readLine(indexByArtistID.get(Integer.valueOf(name)), path));
            if (artists == null){
                bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    if(line.split("/")[0].split("-")[1].equals(name)){
                        artists = stringToArtist(line);
                        break;
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            System.err.println("From ArtistDao(FindByIDMethod): " + ex.getMessage());
        }finally {
            try {
                if(bufferedReader != null){
                    bufferedReader.close();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return artists;
    }
}
