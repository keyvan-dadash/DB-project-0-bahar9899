package Exceptions.ArtistExceptions;

public class ArtistNameException extends Exception{

    private String message;

    public ArtistNameException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Exception form ArtistID : " + message;
    }
}
