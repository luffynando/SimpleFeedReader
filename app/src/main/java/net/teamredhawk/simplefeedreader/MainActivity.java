package net.teamredhawk.simplefeedreader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements AdaptadorPosts.OnItemClickListener {
    private XmlPullParser xmlreader = Xml.newPullParser();
    private RecyclerView reciclador;
    private LinearLayoutManager linearManager;
    private AdaptadorPosts adaptador;

    private final static String URL = "http://lanixilliums520.blogspot.mx/feeds/posts/default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usarToolbar();

        reciclador = (RecyclerView) findViewById(R.id.reciclador);
        reciclador.setHasFixedSize(true);
        linearManager = new LinearLayoutManager(this);
        reciclador.setLayoutManager(linearManager);

        adaptador = new AdaptadorPosts();
        adaptador.setHasStableIds(true);
        adaptador.setOnItemClickListener(this);

        reciclador.setAdapter(adaptador);

        new TareaDescargaXml().execute(URL);

    }

    private void usarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdaptadorPosts.ViewHolder item, int position) {
        //Nada aqui deberias crear el nuevo intento para crear la nueva ventana con el contenido
    }

    private class TareaDescargaXml extends AsyncTask<String, Void, List<Post>> {

        @Override
        protected List<Post> doInBackground(String... urls) {
            try {
                return parsearXmlDeUrl(urls[0]);
            } catch (IOException e) {
                return null; // null si hay error de red
            } catch (XmlPullParserException e) {
                return null; // null si hay error de parsing XML
            }
        }

        @Override
        protected void onPostExecute(List<Post> result) {
            // Actualizar contenido del proveedor de datos
            Post.Posts = result;
            // Actualizar la vista del adaptador
            adaptador.notifyDataSetChanged();
        }
    }

    private List<Post> parsearXmlDeUrl(String urlString)
            throws XmlPullParserException, IOException {
        InputStream stream = null;
        ParseXml parserXml = new ParseXml();
        List<Post> entries = null;

        try {
            stream = descargarContenido(urlString);
            entries = parserXml.parsear(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return entries;
    }

    private InputStream descargarContenido(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Iniciar la petición
        conn.connect();
        return conn.getInputStream();
    }
}
