package Validator;




public class FilmValidator {

    public static boolean validAddCommand(String commad){
        if (!commad.split(" ", 2)[1].split(",")[0].split(":")[0].equals("FilmID")){
            return false;
        }
        if (!commad.split(" ", 2)[1].split(",")[1].split(":")[0].equals("FilmName")){
            return false;
        }
        if (!commad.split(" ", 2)[1].split(",")[2].split(":")[0].equals("DirectorName")){
            return false;
        }
        if (!commad.split(" ", 2)[1].split(",")[3].split(":")[0].equals("ProductionYear")){
            return false;
        }
        if (!commad.split(" ", 2)[1].split(",")[4].split(":")[0].equals("Genre")){
            return false;
        }
        return true;
    }

    public static boolean validFindCommand(String command){
        String[] temp = command.split(" By ");
        if (!temp[temp.length - 1].equals("FilmName") && !temp[temp.length - 1].equals("FilmID")){
            return false;
        }
        return true;
    }

    public static boolean validUpdateCommand(String command){
        String[] temp = command.split(" Set ");
        if (temp.length !=3){
            return false;
        }
        if (!temp[2].split(" ")[0].equals("FilmID")){
            return false;
        }
        if(temp[temp.length - 1].split(" ")[0].equals("FilmID")){
            return true;
        }
        if(temp[temp.length - 1].split(" ")[0].equals("FilmName")){
            return true;
        }
        if(temp[temp.length - 1].split(" ")[0].equals("DirectorName")){
            return true;
        }
        if(temp[temp.length - 1].split(" ")[0].equals("ProductionYear")) {
            return true;
        }
        if(temp[temp.length - 1].split(" ")[0].equals("Genre")){
            return true;
        }
        return false;
    }

}
