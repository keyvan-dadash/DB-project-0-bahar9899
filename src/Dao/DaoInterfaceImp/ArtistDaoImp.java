package Dao.DaoInterfaceImp;

import Dao.DaoInterface.Dao;
import Exceptions.ArtistExceptions.DuplicateAtristException;
import Models.Artist;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

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
        File file;
        BufferedWriter bufferedWriter = null;
        try {
            if (findByID(String.valueOf(artist.getArtistID())) != null){
                throw new DuplicateAtristException("This Artist Already Exist");
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
        }catch (IOException e) {
            e.printStackTrace();
        }catch (DuplicateAtristException ax) {
            System.err.println(ax);
        } finally {
            if(bufferedWriter != null){
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
            File tempfile = new File("tempfordeleteartist.txt");
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
    public void update(Artist artist) {
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
                if(!line.split("/")[0].split("-")[1].equals(String.valueOf(artist.getArtistID()))){
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
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<Artist> findByName(String name) {
        String line;
        List<Artist> artists = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            File file = new File(path);
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split("/")[1].contains(name)){
                    artists.add(stringToArtist(line));
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
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split("/")[0].split("-")[1].equals(name)){
                    artists = stringToArtist(line);
                    break;
                }
            }
        }catch (Exception ex){
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
