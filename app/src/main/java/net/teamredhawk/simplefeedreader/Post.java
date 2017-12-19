package net.teamredhawk.simplefeedreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luffynando on 18/12/2017.
 */


public class Post {
    private String titulo= "Titulo";
    private ArrayList<String> categorias= new ArrayList<>();
    //private String fecha= "Fecha";
    //private String thumbnail= "Imagenurl";
    //private String Autor= "Nombredeautor";7
    private String Content= "Contenido html";

    public static List<Post> Posts= new ArrayList<>();

    public Post(String titulo, ArrayList<String> categorias, String Content){
        this.titulo = titulo;
        this.categorias = categorias;
        this.Content= Content;
    }

    public String getTitulo(){
        return titulo;
    }

    public String getContent(){
        return Content;
    }

    public ArrayList<String> getCategorias(){
        return categorias;
    }
}
