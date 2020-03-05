package Validator;



public class ArtistValidator {

    public static boolean validAddCommand(String command){
        if (!command.split(" ", 2)[1].split(",")[0].split(":")[0].equals("ArtistID")){
            return false;
        }
        if (!command.split(" ", 2)[1].split(",")[1].split(":")[0].equals("ArtistName")){
            return false;
        }
        if (!command.split(" ", 2)[1].split(",")[2].split(":")[0].equals("Age")){
            return false;
        }
        if (!command.split(" ", 2)[1].split(",")[3].split(":")[0].equals("ArtistFilms")){
            return false;
        }
        return true;
    }

    public static boolean validUpdateCommand(String command){
        String[] temp = command.split(" Set ");
        if (temp.length !=3){
            return false;
        }
        if (!temp[2].split(" ")[0].equals("ArtistID")){
            return false;
        }
        if(temp[temp.length - 1].split(" ")[0].equals("ArtistID")){
            return true;
        }
        if(temp[temp.length - 1].split(" ")[0].equals("ArtistName")){
            return true;
        }
        if(temp[temp.length - 1].split(" ")[0].equals("Age")){
            return true;
        }
        if(temp[temp.length - 1].split(" ")[0].equals("ArtistFilm")) {
            return true;
        }
        return false;
    }
}
