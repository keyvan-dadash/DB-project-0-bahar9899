package Exceptions.FilmExceptions;

public class ProductionYearException extends Exception{

    private String message;
    public ProductionYearException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Exception From ProductionYear : " + message;
    }
}
