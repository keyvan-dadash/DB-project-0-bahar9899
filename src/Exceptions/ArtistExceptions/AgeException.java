package Exceptions.ArtistExceptions;

public class AgeException extends Exception{


    private String message;

    public AgeException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Exception form ArtistID : " + message;
    }

}
