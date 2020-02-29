package Dao.DaoInterfaceImp;

import Dao.DaoInterface.Dao;
import Models.Artist;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistDaoImp implements Dao<Artist> {

    private String path;

    public ArtistDaoImp(String path) {
        this.path = path;
    }


    private Artist stringToArtist(String str) throws Exception {
        String[] array = str.split("/");
        int artistID = Integer.valueOf(array[0].split("-")[1]);
        String artistName = array[1];
        int age = Integer.valueOf(array[2]);
        List<String> films = new ArrayList<>();
        for (String string:
             array[3].split(",")) {
            films.add(string);
        }
        return new Artist(artistID, age, artistName, films);
    }

    @Override
    public void save(Artist artist) {
        try {
            File file = new File(path);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            String line = new String();
            line += artist.getIndex() + '-';
            line += artist.getArtistID() + '/';
            line += artist.getArtistName() + '/';
            for (String str :
                    artist.getArtistFilms()) {
                line += str + ',';
            }
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
            File tempfile = new File("temppp.txt");
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
            System.err.println("From ArtistDao(DeleteItemMethod): " + ex.getMessage());
        }
    }

    @Override
    public void update(Artist artist) {
        String newArtist = new String();
        newArtist += artist.getIndex() + '-';
        newArtist += artist.getArtistID() + '/';
        newArtist += artist.getArtistName() + '/';
        for (String str :
                artist.getArtistFilms()) {
            newArtist += str + ',';
        }
        String line;
        try {
            File file = new File(path);
            File tempfile = new File("temppp.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempfile));
            while ((line = bufferedReader.readLine()) != null) {
                if(!line.contains(String.valueOf(artist.getArtistID()))){
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }else{
                    bufferedWriter.write(newArtist);
                    bufferedWriter.newLine();
                }
            }
            if(!file.delete()) throw new Exception("Deleting File " + path + " Operation unsuccessfully");
            if(!tempfile.renameTo(new File(path))) throw new Exception("Renaming File Temp TO " + path + " Operation Unsuccessfully");
        }catch (Exception ex){
            System.err.println("From ArtistDao(UpdateMethod): " + ex.getMessage());
        }
    }

    @Override
    public List<Artist> findByName(String name) {
        String line;
        List<Artist> artists = new ArrayList<>();
        try {
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split("/")[1].equals(name)){
                    artists.add(stringToArtist(line));
                }
            }
        }catch (Exception ex){
            System.err.println("From ArtistDao(FindByNameMethod): " + ex.getMessage());
        }
        return artists;
    }

    @Override
    public Artist findByID(String name) {
        String line;
        Artist artists = null;
        try {
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split("/")[1].equals(name)){
                    artists = stringToArtist(line);
                    break;
                }
            }
        }catch (Exception ex){
            System.err.println("From ArtistDao(FindByIDMethod): " + ex.getMessage());
        }
        return artists;
    }
}
