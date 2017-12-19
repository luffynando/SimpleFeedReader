package net.teamredhawk.simplefeedreader;

/**
 * Created by luffynando on 18/12/2017.
 */
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ParseXml {
    private static final String nullstring = null;
    private static final String Etiqueta_feed= "feed";
    private static final String Etiqueta_number_posts= "opensearch:totalresults";
    private static final String Etiqueta_content= "content";
    private static final String Etiqueta_entry= "entry";
    private static final String Etiqueta_title= "title";
    private static final String Etiqueta_categoria= "category";
    //private static final String Etiqueta_thumb="media:thumbnail";

    //private static final String Atributo_url= "url";
    private static final String Atributo_term= "term";


    public List<Post> parsear(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(in, null);
            parser.nextTag();
            return leerfeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Post> leerfeed(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<Post> listaPosts = new ArrayList<Post>();

        parser.require(XmlPullParser.START_TAG, nullstring, Etiqueta_feed);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nombreEtiqueta = parser.getName();
            // Buscar etiqueta <entry>
            if (nombreEtiqueta.equals(Etiqueta_entry)) {
                listaPosts.add(leerPost(parser));
            } else {
                saltarEtiqueta(parser);
            }
        }
        return listaPosts;
    }

    private Post leerPost(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, nullstring, Etiqueta_entry);
        String titulo= null;
        String content= null;
        ArrayList<String> categorias=new ArrayList<>();
        //String url_imagen = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.d("Etiqueta", name);
            switch (name) {
                case Etiqueta_title:
                    titulo = leerTitulo(parser);
                    break;
                case Etiqueta_categoria:
                    categorias.add(leerCategoria(parser));
                    break;
                case Etiqueta_content:
                    content= leerContent(parser);
                    break;
                default:
                    saltarEtiqueta(parser);
                    break;
            }
        }
        return new Post(titulo,categorias,content);
    }

    private String leerTitulo(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, nullstring, Etiqueta_title);
        String titulo = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, nullstring, Etiqueta_title);
        return titulo;
    }

    private String leerCategoria(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, nullstring, Etiqueta_categoria);
        String categoria= parser.getAttributeValue(null, Atributo_term);
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, nullstring, Etiqueta_categoria);
        return categoria;
    }

    private String leerContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        String content = "";
        parser.require(XmlPullParser.START_TAG, nullstring,Etiqueta_content);
        content= obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, nullstring, Etiqueta_content);
        return content;
    }

    private String obtenerTexto(XmlPullParser parser) throws IOException, XmlPullParserException {
        String resultado = "";
        if (parser.next() == XmlPullParser.TEXT) {
            resultado = parser.getText();
            parser.nextTag();
        }
        return resultado;
    }

    private void saltarEtiqueta(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
