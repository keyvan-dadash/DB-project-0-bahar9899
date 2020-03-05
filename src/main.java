import Dao.DaoInterfaceImp.ArtistDaoImp;
import Dao.DaoInterfaceImp.FilmDaoImp;
import Models.Artist;
import Models.Film;
import Validator.ArtistValidator;
import Validator.FilmValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws Exception {
        Scanner get = new Scanner(System.in);

        //example
        /*Add FilmID:1222,FilmName:haha,DirectorName:kdfj,ProductionYear:1398,Genre:drama
        Add FilmID:1482,FilmName:haha1,DirectorName:sf,ProductionYear:1399,Genre:sdfs
        Add FilmID:1242,FilmName:haha2,DirectorName:il,ProductionYear:1397,Genre:hf
        Add FilmID:1992,FilmName:haha3,DirectorName:ytjt,ProductionYear:1392,Genre:fghf
        Add FilmID:1522,FilmName:haha4,DirectorName:nfngff,ProductionYear:1393,Genre:ghfg

        Add ArtistID:1333,ArtistName:kdfj,Age:44,ArtistFilms:haha
        Add ArtistID:1334,ArtistName:sdfdsf,Age:45,ArtistFilms:haha1,haha2
        Add ArtistID:1335,ArtistName:sidfj,Age:98,ArtistFilms:haha1,haha3
        Add ArtistID:1336,ArtistName:ssjlk,Age:23,ArtistFilms:haha3*/
        String command;
        int filmID;
        String filmname;
        String directorname;
        int productionYear;
        String genre;

        int artistID;
        int age;
        String artistname;
        List<String> artistfilms = new ArrayList<>();

        String films;

        String artistPath = "./Artist.txt";
        String artistIDIndexPath = "./ArtistID.txt";
        String artistNameIndexPAth = "./ArtistName.txt";
        String artistFilmsPath = "./ArtistFilms.txt";
        String filmsPath = "./films.txt";
        File file = new File(artistPath);
        file.createNewFile();
        File file1 = new File(artistIDIndexPath);
        file1.createNewFile();
        File file2 = new File(artistNameIndexPAth);
        file2.createNewFile();
        File file3 = new File(artistFilmsPath);
        file3.createNewFile();
        File file4 = new File(filmsPath);
        file4.createNewFile();
        FilmDaoImp filmDaoImp = new FilmDaoImp(filmsPath, artistFilmsPath, artistPath);
        ArtistDaoImp artistDaoImp = new ArtistDaoImp(artistPath, artistIDIndexPath, artistNameIndexPAth, filmDaoImp, artistFilmsPath);
        filmDaoImp.setArtistDaoImp(artistDaoImp);
        while (true){
            command = get.nextLine();

            switch (command.split(" ", 2)[0]){
                case "Add"://check is okey
                    if (command.split(" ", 2)[1].contains("FilmID")){
                        if (FilmValidator.validAddCommand(command)){
                            filmID = Integer.valueOf(command.split(" ", 2)[1].split(",")[0].split(":")[1]);
                            filmname = command.split(" ", 2)[1].split(",")[1].split(":")[1];
                            directorname = command.split(" ", 2)[1].split(",")[2].split(":")[1];
                            productionYear = Integer.valueOf(command.split(" ", 2)[1].split(",")[3].split(":")[1]);
                            genre = command.split(" ", 2)[1].split(",")[4].split(":")[1];
                            try {
                                Film film = new Film(filmID, filmname, directorname, productionYear, genre);
                                filmDaoImp.save(film);
                            }catch (Exception ex){
                                System.err.println(ex.toString());
                            }
                        }else{
                            System.err.println("Your Command is Wrong");
                        }
                    }
                    else if (command.split(" ", 2)[1].contains("ArtistID")){
                        if (ArtistValidator.validAddCommand(command)){

                            artistfilms.clear();
                            artistID = Integer.valueOf(command.split(" ", 2)[1].split(",")[0].split(":")[1]);
                            artistname = command.split(" ", 2)[1].split(",")[1].split(":")[1];
                            age = Integer.valueOf(command.split(" ", 2)[1].split(",")[2].split(":")[1]);
                            films = command.split("ArtistFilms:")[1];
                            for (String film:
                                 films.split(",")) {
                                artistfilms.add(film);
                            }
                            try {
                                Artist artist = new Artist(artistID, age, artistname, artistfilms);
                                artistDaoImp.save(artist);
                            }catch (Exception ex){
                                System.err.println(ex.toString());
                            }
                        }else{
                            System.err.println("Your Command is Wrong");
                        }
                    }
                    else{
                        System.err.println("Your Command is Wrong");
                    }
                    break;
                case "Update": //check okey
                    if (command.split(" ")[1].equals("Film")){
                        if (FilmValidator.validUpdateCommand(command)){
                            filmname = command.split(" ", 3)[2].split(" Set ")[0];
                            List<Film> filmslist = filmDaoImp.findByName(filmname);
                            try{
                                for (Film film:
                                        filmslist) {
                                    filmID = film.getFilmID();
                                    film.setFilmID(Integer.valueOf(command.split(" Set ")[2].split(" ")[2]));
                                    if (command.split(" Set ")[1].split(" ")[0].equals("Genre")){
                                        film.setGenre(command.split(" Set ")[1].split(" ")[2]);
                                    }
                                    else if (command.split(" Set ")[1].split(" ")[0].equals("DirectorName")){
                                        film.setDirectorName(command.split(" Set ")[1].split(" ")[2]);
                                    }
                                    else if (command.split(" Set ")[1].split(" ")[0].equals("ProductionYear")){
                                        film.setProductionYear(Integer.valueOf(command.split(" Set ")[1].split(" ")[2]));
                                    }
                                    filmDaoImp.update(film, filmID);
                                    break;
                                }
                            }catch (Exception ex){
                                System.err.println(ex.toString());
                            }
                        }else{
                            System.err.println("Your Command is Wrong");
                        }
                    }
                    else if (command.split(" ")[1].equals("Artist")){
                        if (ArtistValidator.validUpdateCommand(command)){
                            artistname = command.split(" ", 3)[2].split(" Set ")[0];
                            List<Artist> artistList = artistDaoImp.findByName(artistname);
                            try{
                                for (Artist artist:
                                        artistList) {
                                    artistID = artist.getArtistID();
                                    artist.setArtistID(Integer.valueOf(command.split(" Set ")[2].split(" ")[2]));
                                    if (command.split(" Set ")[1].split(" ")[0].equals("ArtistName")){
                                        artist.setArtistName(command.split(" Set ")[1].split(" ")[2]);
                                    }
                                    else if (command.split(" Set ")[1].split(" ")[0].equals("Age")){
                                        artist.setAge(Integer.valueOf(command.split(" Set ")[1].split(" ")[2]));
                                    }
                                    artistDaoImp.update(artist, artistID);
                                    break;
                                }
                            }catch (Exception ex){
                                System.err.println(ex.toString());
                            }
                        }else{
                            System.err.println("Your Command is Wrong");
                        }
                    }
                    else{
                        System.err.println("Your Command is Wrong");
                    }
                    break;
                case "Remove"://check okey
                    if (command.split(" ")[1].contains("FilmID")){
                        try{
                            filmID = Integer.valueOf(command.split(" ")[2]);
                            filmDaoImp.deleteItem(filmID);
                        }catch (Exception ex){
                            System.err.println(ex.toString());
                        }
                    }
                    else if (command.split(" ")[1].contains("ArtistID")){
                        try{
                            artistID = Integer.valueOf(command.split(" ")[2]);
                            artistDaoImp.deleteItem(artistID);
                        }catch (Exception ex){
                            System.err.println(ex.toString());
                        }
                    }
                    else{
                        System.err.println("Your Command is Wrong");
                    }
                    break;
                case "Find"://check okey
                    if (command.split(" ")[1].equals("Film")){
                        if (FilmValidator.validFindCommand(command)){
                            if (command.split(" By ")[1].equals("FilmName")){
                                try{
                                    List<Film> filmss = filmDaoImp.findByName(command.split(" ", 3)[2].split(" By ")[0]);
                                    for (Film film:
                                         filmss) {
                                        System.out.println(film.getFilmName());
                                    }
                                }catch (Exception ex){
                                    System.err.println(ex.toString());
                                }
                            }else if (command.split(" By ")[1].equals("FilmID")){
                                try {
                                    Film film = filmDaoImp.findByID(command.split(" ", 3)[2].split(" By ")[0]);
                                    System.out.println(film.getFilmName());
                                }catch (Exception ex){
                                    System.err.println(ex.toString());
                                }
                            }
                        }else{
                            System.err.println("Your Command is Wrong");
                        }
                    }
                    else{
                        System.err.println("Your Command is Wrong");
                    }
                    break;
                case "Info"://check okey
                    for (String str:
                         filmDaoImp.getAllinfo(command.split(" ", 2)[1])) {
                        System.out.println(str);
                    }
                    break;
            }
        }
    }
}
