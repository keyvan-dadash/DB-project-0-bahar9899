package Exceptions.FilmExceptions;

public class DuplicateFilmException extends Exception{

    private String message;

    public DuplicateFilmException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}