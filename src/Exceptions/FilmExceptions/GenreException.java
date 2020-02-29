package Exceptions.FilmExceptions;

public class GenreException extends Exception{

    private String message;
    public GenreException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Exception From Genre : " + message;
    }
}
