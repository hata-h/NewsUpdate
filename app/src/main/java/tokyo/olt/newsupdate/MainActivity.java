package tokyo.olt.newsupdate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private static String TAG="XXX";
    private ArrayList<BeanURL> urls;
    public final Handler handler=new Handler();
    private String rss=null;
    private String pubDate=null;
    private String name=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.listview1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(urls.size()<=position){
                    Snackbar.make(view, "NotFound", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                BeanURL b=urls.get(position);
                Snackbar.make(view, b.url, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("URL", b.url);
                startActivity(intent);
            }
        });
        rss = getString(R.string.url_0);
        TextView tv=(TextView)findViewById(R.id.textView1);
        tv.setText(getString(R.string.action_0));
        updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String url=null;
        name=null;
        //noinspection SimplifiableIfStatement
        switch(id ) {
            case R.id.action_0:
                url = getString(R.string.url_0);
                name = getString(R.string.action_0);
                break;
            case R.id.action_1:
                url = getString(R.string.url_1);
                name=getString(R.string.action_1);
                break;
            case R.id.action_2:
                url = getString(R.string.url_2);
                name=getString(R.string.action_2);
                break;
            case R.id.action_3:
                url = getString(R.string.url_3);
                name=getString(R.string.action_3);
                break;
            case R.id.action_4:
                url = getString(R.string.url_4);
                name=getString(R.string.action_4);
                break;
            case R.id.action_5:
                url = getString(R.string.url_5);
                name=getString(R.string.action_5);
                break;
            case R.id.action_6:
                url = getString(R.string.url_6);
                name=getString(R.string.action_6);
                break;
            case R.id.action_7:
                url = getString(R.string.url_7);
                name=getString(R.string.action_7);
                break;
            case R.id.action_8:
                url = getString(R.string.url_8);
                name=getString(R.string.action_8);
                break;
            case R.id.action_9:
                url = getString(R.string.url_9);
                name=getString(R.string.action_9);
                break;
            case R.id.action_10:
                url = getString(R.string.url_10);
                name=getString(R.string.action_10);
                break;
            case R.id.action_11:
                url = getString(R.string.url_11);
                name=getString(R.string.action_11);
                break;
            case R.id.action_12:
                url=getString(R.string.url_12);
                name=getString(R.string.action_12);
                break;
            default:
                url = null;
        }
        if(url!=null){
            rss=url;
            ((TextView)findViewById(R.id.textView1)).setText(name);
            updateList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(rss);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    parseXML(con.getInputStream());
                    handler.post(
                            new Runnable() {
                                public void run() {
                                    exeDisp();
                                }
                            }
                    );
                } catch (Exception ex) {
                    Log.d(TAG, ex.getMessage());
                }
            }
        }).start();
    }
    private void parseXML(InputStream inputStream)
    {
        int itemCount=0;
        int item=0;
        int title=0;
        int link=0;
        int pubd=0;
        int li_count=0;
        pubDate=null;
        BeanURL b=null;
        String name;
        urls = new ArrayList<>();

        XmlPullParser xmlPullParser = Xml.newPullParser();
        try {
            xmlPullParser.setInput( inputStream,"UTF-8");
        } catch (XmlPullParserException e) {
            Log.d(TAG, "XmlPullParserException Error: "+e.getMessage());
        }

        try {
            int eventType;
            eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d(TAG, "Start document");
                } else if(eventType == XmlPullParser.END_DOCUMENT) {
                    Log.d(TAG, "End document");
                } else if(eventType == XmlPullParser.START_TAG) {
                    name = xmlPullParser.getName();
                    if (name == null) {
                        continue;
                    }
                    if (name.compareTo("item") == 0) {
                        if (itemCount++ > 10) {
                            return;
                        }
                        item = 1;
                        b = new BeanURL();
                    } else if (name.compareTo("li") == 0) {
                        String mtype = xmlPullParser.getAttributeValue(null, "class");
                        if (mtype != null && mtype.compareTo("block") == 0) {
                            if (b != null) {
                                urls.add(b);
                            }
                            item = 1;
                            b = new BeanURL();
                            li_count = 1;
                        } else {
                            li_count++;
                        }
                        if (mtype != null && mtype.compareTo("btnMovie") == 0) {
                            link = 1;
                        }
                    }else if(name.compareTo("figure") == 0){
                        link=2;
                    }else if(link==2 && name.compareTo("span") == 0){
                        String mtype = xmlPullParser.getAttributeValue(null, "class");
                        if (mtype != null && mtype.compareTo("date") == 0) {
                            title=1;
                            link=3;
                        }
                    }else if(item==1 && name.compareTo("title")==0){
                        title=1;
                    }else if(item==1 && name.compareTo("link")==0 && b.linktype==0) {
                        link = 1;
                    }else if(item==1 &&name.compareTo("enclosure")==0){
                        String mtype=xmlPullParser.getAttributeValue(null, "type");
                        if(mtype.endsWith("mp3")
                                ||mtype.endsWith("mp4")
                                ||mtype.endsWith("mpeg")) {
                            b.url = xmlPullParser.getAttributeValue(null, "url");
                            b.linktype = 1;
                        }
                    }else if(pubDate==null && name.compareTo("pubDate")==0) {
                        pubd = 1;
                    } else if(item==1 && name.compareTo("header")==0) {
                        title=1;
                    }else if(link==1 && name.compareTo("a")==0){
                        String mtype=xmlPullParser.getAttributeValue(null, "href");
                        b.url = mtype;
                        b.linktype = 1;
                        link=0;
                    }else if(link==2 && name.compareTo("a")==0){
                        String uname=xmlPullParser.getAttributeValue(null, "href");
                        if(uname!=null && b==null) {
                            b = new BeanURL();
                            b.url = uname;
                            b.linktype = 1;
                            link = 2;
                        }
                    }
                    Log.d(TAG, "Start tag "+name);
                } else if(eventType == XmlPullParser.END_TAG) {
                    name = xmlPullParser.getName();
                    if(item!=0 && name.compareTo("item")==0){
                        item=0;
                        urls.add(b);
                        b=null;
                    }else if(link==3 && name.compareTo("span")==0) {
                        if(b!=null) urls.add(b);
                        b=null;
                        link = 0;
                    }else if(name.compareTo("link")==0) {
                        link = 0;
                    }else if(name.compareTo("figure")==0) {
                        link = 2;
                    }else if(name.compareTo("title")==0) {
                        title = 0;
                    }else if(name.compareTo("pubDate")==0) {
                        pubd = 0;
                    }else if(name.compareTo("li")==0) {
                        li_count--;
                        if (li_count == 0) {
                            if (item != 0 && name.compareTo("item") == 0) {
                                item = 0;
                                urls.add(b);
                                b = null;
                            }
                        }
                    }
                    Log.d(TAG, "End tag "+name);
                } else if(eventType == XmlPullParser.TEXT) {
                    name = xmlPullParser.getText();
                    if(name!=null) {
                        if (pubDate == null && pubd == 1) {
                            pubDate = name;
                        }
                        if (link == 1) {
                            b.url = name;
                        }
                        if (title == 1) {
                            if(!name.startsWith("\n") && !name.equals("\0")) {
                                b.title = name;
                                title = 0;
                            }
                        }
                        Log.d(TAG, "Text " + name);
                    }
                }
                try{
                    eventType = xmlPullParser.next();
                }catch(Exception e){
                    Log.d(TAG, "XML Error0 "+e.getMessage());
                }
            }
            if(b!=null){
                urls.add(b);
            }
        } catch (Exception e) {
            Log.d(TAG, "XML Error "+e.getMessage());
        }
    }
    private void exeDisp()
    {
        //check URL
        int idx=rss.indexOf("/",9);
        int idx2=rss.lastIndexOf("/");
        String base=rss.substring(0, idx);
        String base2=rss.substring(0,idx2);
        for(BeanURL b:urls){
            if(b.url.startsWith("/")){
                b.url=base+b.url;
            }else if(!b.url.startsWith("http:") && !b.url.startsWith("https:")){
                b.url=base2+b.url;
            }
        }
        AdapterListURL adapter = new AdapterListURL(this, 0, urls);
        lv.setAdapter(adapter);
        TextView tv=(TextView)findViewById(R.id.textView1);
        if(pubDate!=null)tv.setText(name+" "+pubDate);
    }
}
