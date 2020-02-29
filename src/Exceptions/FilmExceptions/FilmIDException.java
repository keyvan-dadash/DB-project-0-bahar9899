package Exceptions.FilmExceptions;

public class FilmIDException extends Exception{

    private String message;
    public FilmIDException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Exception From FilmID : " + message;
    }
}
