package Dao.DaoInterfaceImp;

import Dao.DaoInterface.Dao;
import Exceptions.ArtistExceptions.DuplicateAtristException;
import IOHelper.ReadLineHelper;
import Models.Artist;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArtistDaoImp implements Dao<Artist> {

    private String path;
    private long lines = 1;
    private String pathForArtistID;
    private String pathForArtistName;
    private HashMap<Integer , Integer> indexByArtistID = new HashMap<>();
    private HashMap<String , Integer> indexByArtistName = new HashMap<>();

    //fuck check this dublicate name
    public ArtistDaoImp(String path, String pathForArtistID, String pathForArtistName) {
        this.path = path;
        this.pathForArtistID = pathForArtistID;
        this.pathForArtistName = pathForArtistName;
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
                indexByArtistName.put(line.split("->")[0], Integer.parseInt(line.split("->")[1]));
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
                indexByArtistName.put(line.split("/")[1], locallines);
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

    private Artist stringToArtist(String str) throws Exception {
        String[] array = str.split("/");
        int index = Integer.valueOf(array[0].split("-")[0]);
        int artistID = Integer.valueOf(array[0].split("-")[1]);
        String artistName = array[1];
        int age = Integer.valueOf(array[2]);
        List<String> films = new ArrayList<>();
        for (String string:
             array[3].split(",")) {
            films.add(string);
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
            indexByArtistName.put(artist.getArtistName(), (int) lines);
            lines++;
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
        if (findByID(String.valueOf(artist.getArtistID())) == null)
            System.err.println("This Artist does not Exist");
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
            /*bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if(line.split("/")[0].split("-")[1].equals(name)){
                    artists = stringToArtist(line);
                    break;
                }
            }*/
            if (indexByArtistID.get(Integer.valueOf(name)) != null)
                artists = stringToArtist(ReadLineHelper.readLine(indexByArtistID.get(Integer.valueOf(name)), path));
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
