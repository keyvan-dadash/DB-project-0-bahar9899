package Models;


import Exceptions.ArtistExceptions.AgeException;
import Exceptions.ArtistExceptions.ArtistIDException;
import Exceptions.ArtistExceptions.ArtistNameException;

import java.util.List;

public class Artist {
    private static long Globalindex = 1;
    private long index;
    private int artistID;
    private int age;
    private String artistName;
    private List<String> artistFilms;


    public Artist(int artistID, int age, String artistName, List<String> artistFilms) throws Exception{
        this.index = Globalindex;
        this.setArtistID(artistID);
        this.setAge(age);
        this.setArtistName(artistName);
        this.setArtistFilms(artistFilms);
        Globalindex++;
    }

    public int getArtistID() {
        return artistID;
    }

    public void setArtistID(int artistID) throws ArtistIDException {
        if(artistID < 1000 || artistID > 9999){
            throw new ArtistIDException("ArtistID must be 4 number");
        }
        this.artistID = artistID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws AgeException {
        if(age < 1 || age > 999){
            throw new AgeException("Age must be 3 number");
        }
        this.age = age;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) throws ArtistNameException {
        if (artistName.length() > 100){
            throw new ArtistNameException("length of ArtistName cant be above 100 char");
        }
        this.artistName = artistName;
    }

    public List<String> getArtistFilms() {
        return artistFilms;
    }

    public void setArtistFilms(List<String> artistFilms) {
        this.artistFilms = artistFilms;
    }

    public long getIndex() {
        return index;
    }
}
