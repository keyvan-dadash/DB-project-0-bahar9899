package Exceptions.FilmExceptions;

public class FilmNameException extends Exception{
    private String message;
    public FilmNameException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Exception From FilmName : " + message;
    }
}
