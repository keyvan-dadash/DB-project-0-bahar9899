import Dao.DaoInterface.Dao;
import Dao.DaoInterfaceImp.ArtistDaoImp;
import Dao.DaoInterfaceImp.FilmDaoImp;
import Models.Artist;
import Models.Film;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class main {

    public static void main(String[] args) throws Exception {
        String path = "./Artist.txt";
        String pathid = "./ArtistIDIndex.txt";
        String pathname = "./ArtistNameIndex.txt";
        Dao<Artist> artistdao = new ArtistDaoImp(path, pathid, pathname);
        Artist astt = artistdao.findByID("5463");
        System.out.println(astt.getIndex());
        System.out.println(astt.getArtistID());
        System.out.println(astt.getAge());
        System.out.println(astt.getArtistName());
        for (String str:
                astt.getArtistFilms()) {
            System.out.println(str);
        }
        List<Artist> art = artistdao.findByName("hal");
        for (Artist artist4:
                art) {
            System.out.println(artist4.getIndex());
            System.out.println(artist4.getArtistID());
            System.out.println(artist4.getAge());
            System.out.println(artist4.getArtistName());
            for (String str:
                    artist4.getArtistFilms()) {
                System.out.println(str);
            }
            System.out.println("-----------------------------------------------------");
        }
        /*String path = "./Artist.txt";
        String pathid = "./ArtistIDIndex.txt";
        String pathname = "./ArtistNameIndex.txt";
        String path1 = "./Film.txt";
        File file = new File(path);
        file.createNewFile();
        File file1 = new File(path1);
        file1.createNewFile();
        File file2 = new File(pathid);
        file2.createNewFile();
        File file3 = new File(pathname);
        file3.createNewFile();
        List<String> films = new ArrayList<>();
        Dao<Artist> artistdao = new ArtistDaoImp(path, pathid, pathname);
        Dao<Film> filmdao = new FilmDaoImp(path1);
        films.add("dff");
        films.add("ddww");
        films.add("dwrwf");
        Artist artist = new Artist(3333,55,"hal", films);
        Artist artist1 = new Artist(3553,52,"hal", films);
        Artist artist2 = new Artist(5463,12,"sdfsf", films);
        Film film = new Film(4444, "dfjkd", "sdfkjj", 1397, "sdf");
        Film film1 = new Film(3312, "dddkk", "zczcz", 1398, "bnbv");
        Film film2 = new Film(1111, "oiliol", "qweq", 1399, "uyi");
        artistdao.save(artist);
        artistdao.save(artist1);
        artistdao.save(artist2);
        artistdao.deleteItem(3333);
        Artist artist5 = new Artist(5455, 55,"s34wefffdsff", films);
        artistdao.save(artist5);
        filmdao.save(film);
        filmdao.save(film1);
        filmdao.save(film2);
        Artist artist3 = new Artist(5463,15,"sdfsfffffffff", films);
        Film film3 = new Film(1111, "dlj", "qweq", 1379, "uyi");
        artistdao.update(artist3);
        filmdao.update(film3);

        artistdao.save(artist3);
        artistdao.save(artist3);
        artistdao.save(artist3);
        artistdao.save(artist3);
        Film film5 = new Film(1111, "bbwee", "3ok3", 1366, "uyi");
        Film film6 = new Film(1112, "bbwee", "3ok3", 1366, "uyi");
        filmdao.save(film5);
        filmdao.save(film6);
        Film film7 = new Film(1113, "bbwee", "3ok3", 1366, "uyi");
        filmdao.deleteItem(1113);
        filmdao.update(film7);
        List<Artist> art = artistdao.findByName("hal");
        for (Artist artist4:
             art) {
            System.out.println(artist4.getIndex());
            System.out.println(artist4.getArtistID());
            System.out.println(artist4.getAge());
            System.out.println(artist4.getArtistName());
            for (String str:
                    artist4.getArtistFilms()) {
                System.out.println(str);
            }
            System.out.println("-----------------------------------------------------");
        }*/



    }
}
