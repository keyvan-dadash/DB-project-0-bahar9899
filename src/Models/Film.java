package Models;

import Exceptions.FilmExceptions.FilmIDException;
import Exceptions.FilmExceptions.FilmNameException;
import Exceptions.FilmExceptions.GenreException;
import Exceptions.FilmExceptions.ProductionYearException;

public class Film {
    private static long Globalindex = 1;
    private long index;
    private int filmID;
    private String filmName;
    private String directorName;
    private int productionYear;
    private String genre;

    public Film(int filmID, String filmName, String directorName, int productionYear, String genre) throws Exception {
        this.index = this.Globalindex;
        this.setFilmID(filmID);
        this.setFilmName(filmName);
        this.setDirectorName(directorName);
        this.setProductionYear(productionYear);
        this.setGenre(genre);
        Globalindex++;
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) throws FilmIDException {
        if(filmID < 1000 || filmID > 9999){
            throw new FilmIDException("FilmID must be 4 number");
        }
        this.filmID = filmID;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) throws FilmNameException {
        if (filmName.length() > 100){
            throw new FilmNameException("length of FilmName cant be above 100 char");
        }
        this.filmName = filmName;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) throws FilmNameException {
        if (directorName.length() > 100){
            throw new FilmNameException("length of DirectorName cant be above 100 char");
        }
        this.directorName = directorName;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) throws ProductionYearException {
        if(productionYear < 1000 || productionYear > 9999){
            throw new ProductionYearException("ProductionYear must be 4 number");
        }
        this.productionYear = productionYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) throws GenreException {
        if(genre.length() > 20){
            throw new GenreException("Genre must be 20 char");
        }
        this.genre = genre;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
