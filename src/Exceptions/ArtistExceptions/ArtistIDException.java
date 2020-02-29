package Exceptions.ArtistExceptions;

public class ArtistIDException extends Exception{

    private String message;

    public ArtistIDException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Exception form ArtistID : " + message;
    }
}
