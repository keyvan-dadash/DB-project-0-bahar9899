package Exceptions.ArtistExceptions;

public class DuplicateAtristException extends Exception{

    private String message;

    public DuplicateAtristException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
