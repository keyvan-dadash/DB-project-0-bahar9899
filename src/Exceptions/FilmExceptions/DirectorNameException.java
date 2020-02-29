package Exceptions.FilmExceptions;

public class DirectorNameException extends Exception{

    private String message;
    public DirectorNameException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Exception From DirectorName : " + message;
    }
}
