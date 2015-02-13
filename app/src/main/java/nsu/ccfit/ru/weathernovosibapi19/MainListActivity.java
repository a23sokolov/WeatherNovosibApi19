package nsu.ccfit.ru.weathernovosibapi19;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainListActivity extends Activity {

   // private ProgressBar pbConnect;
    private Button btnTryToConnect;
    private NewTask mt;
    ListView lvSimple;
    SimpleAdapter simpleAdapter;
    private final String ATTRIBUTE_DATE = "date";
    private final String ATTRIBUTE_DAY_TIME = "day_time";
    private final String ATTRIBUTE_MASSIVE_OF_DATA = "data";
    String[] from = { ATTRIBUTE_DATE, ATTRIBUTE_DAY_TIME,
            ATTRIBUTE_MASSIVE_OF_DATA };
    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.tvDate, R.id.tvTimeDay, R.id.tvDataLook };
    private static ArrayList<Map<String, String>> data = null;
    private static ArrayList<Map<String,String>> bigData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        lvSimple = (ListView) findViewById(R.id.lvList);
        data = new ArrayList<Map<String, String>>();
        bigData = new ArrayList<Map<String, String>>();
        simpleAdapter = new SimpleAdapter(this,data,R.layout.item,from,to);
        mt = (NewTask)getLastNonConfigurationInstance();
        Log.d("Chech NewTask"," cheking");
        if (mt == null) {
            mt = new NewTask(data,bigData,simpleAdapter);
            Log.d("Chech NewTask is null ","yes");
        }
        mt.link(this);
        lvSimple.addFooterView(getLayoutInflater().inflate(R.layout.button_footer,null));
        lvSimple.setAdapter(simpleAdapter);
        lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(),ItemActivity.class);
                try {
                    Map<String,String> map = bigData.get(position);
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue());
                    }
                    startActivity(intent);
                }catch (IndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
            }
        });
        registerForContextMenu(lvSimple);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    public void onClickButton(View v)
    {
        //mt = new NewTask(data,bigData,simpleAdapter);
        if(mt.getStatus()==AsyncTask.Status.FINISHED)
        {
            MainListActivity activity = (MainListActivity)mt.getActivity();
            mt = new NewTask(data,bigData,simpleAdapter);
            mt.link(activity);
        }
        mt.execute();
        View view = findViewById(R.id.tvDataNotDownload);
        if(view !=null)
            ((LinearLayout)view.getParent()).removeView(view);

    }
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    public static void setBigData(ArrayList<Map<String, String>> bigData1)
    {
        bigData.clear();
        bigData.addAll(bigData1);
    }
    @Override
    public NewTask onRetainNonConfigurationInstance() {
        // удаляем из MyTask ссылку на старое MainActivity
        mt.unLink();
        return mt;
    }
    static class NewTask extends AsyncTask<Void, Void, Void> {
        SimpleAdapter simpleAdapter;
        ArrayList<Map<String, String>> data;
        ArrayList<Map<String, String>> bigData;
        static private final String ATTRIBUTE_DATE = "date";
        private final String ATTRIBUTE_MASSIVE_OF_DATA = "data";
        private final String ATTRIBUTE_MASSIVE_OF_DATA_BIG ="data";
        private final String ATTRIBUTE_PRESSURE = "pressure";
        private final String ATTRIBUTE_WIND = "wind";
        private final String ATTRIBUTE_RELVET ="relvet";
        private final String ATTRIBUTE_HEAT = "heat";
        String url = "http://informer.gismeteo.ru/xml/29634_1.xml";
        private final String ATTRIBUTE_DAY_TIME = "day_time";


        MainListActivity mainListActivity;
        private final String TEMPERATURE = "TEMPERATURE";
        private final String PRESSURE = "PRESSURE";
        private final String WIND = "WIND";
        private final String PHENOMENA = "PHENOMENA";
        private final String FORECAST = "FORECAST";
        private final String RELVET = "RELWET";
        private final String HEAT = "HEAT";
        private final String TAG_tod = "tod";
        private final String TAG_cloudiness = "cloudiness";
        private final String TAG_precipitation = "precipitation";
        private final String TAG_FORECAST_day = "day";
        private final String TAG_FORECAST_month = "month";
        private final String TAG_FORECAST_year = "year";
        private final String TAG_max = "max";
        private final String TAG_min = "min";
        private final String TAG_WIND_direction = "direction";

        private String[] timeDayString = {"ночь","утро", "день","вечер"};
        private String[] cloudiness = {"ясно","малооблачно", "облачно","пасмурно"};
        private String[]  precipitation = {"","","","","дождь","ливень","снег","снег","гроза","нет данных","без осадков"};
        private String[] windDirection = {"северный", "северо-восточный","восточный","юго-восточный","южный","юго-западный","западный","северо-западный"};


        public NewTask(ArrayList<Map<String, String>> datas,ArrayList<Map<String, String>> bigDatas,SimpleAdapter simpleAdapter)
        {
            this.simpleAdapter = simpleAdapter;
            if(!datas.isEmpty())
                datas.clear();
            this.data=datas;
            if(!bigDatas.isEmpty())
                bigDatas.clear();
            this.bigData = bigDatas;
        }

        public void link(MainListActivity act) {
            mainListActivity = act;
        }
        public Activity getActivity()
        {
            return mainListActivity;
        }
        // обнуляем ссылку
        public void unLink() {
            mainListActivity = null;
        }
        @Override
        protected Void doInBackground(Void... params)
        {
            GetXmlFromUrl getXmlFromUrl = new GetXmlFromUrl(url);
            Document doc = getXmlFromUrl.getXMLSign();
            NodeList nListForecast = doc.getElementsByTagName(FORECAST);
            NodeList nListTemperature = doc.getElementsByTagName(TEMPERATURE);
            NodeList nListPhenomena = doc.getElementsByTagName(PHENOMENA);
            NodeList nListWind = doc.getElementsByTagName(WIND);
            NodeList nListPressure = doc.getElementsByTagName(PRESSURE);
            NodeList nListRelvet = doc.getElementsByTagName(RELVET);
            NodeList nListHeat = doc.getElementsByTagName(HEAT);
            bigData = new ArrayList<Map<String, String>>(nListForecast.getLength());
            Map<String, String> m;
            Map<String, String> bigM;

            for (int temp = 0; temp < nListForecast.getLength(); temp++) {

                Node nNodeFORECAST =  nListForecast.item(temp);
                Node nNodeTEMPERATURE = nListTemperature.item(temp);
                Node nNodePHENOMENA = nListPhenomena.item(temp);
                Node nNodeWind = nListWind.item(temp);
                Node nNodePressure = nListPressure.item(temp);
                Node nNodeRelvet = nListRelvet.item(temp);
                Node nNodeHeat = nListHeat.item(temp);

                if (nNodeFORECAST.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElementFORECAST = (Element) nNodeFORECAST;
                    Element eElementTEMPERATURE = (Element) nNodeTEMPERATURE;
                    Element eElementPHENOMENA = (Element) nNodePHENOMENA;
                    Element eElementWind = (Element) nNodeWind;
                    Element eElementPressure = (Element) nNodePressure;
                    Element eElementRelvet = (Element) nNodeRelvet;
                    Element eElementHeat = (Element) nNodeHeat;
                    m = new HashMap<String, String>();
                    bigM = new HashMap<String, String>();

                    m.put(ATTRIBUTE_DATE,eElementFORECAST.getAttribute(TAG_FORECAST_day)+"."
                            +eElementFORECAST.getAttribute(TAG_FORECAST_month)+"."
                            +eElementFORECAST.getAttribute(TAG_FORECAST_year));
                    m.put(ATTRIBUTE_DAY_TIME, timeDayString[Integer.parseInt(eElementFORECAST.getAttribute(TAG_tod))]);
                    bigM.putAll(m);
                    m.put(ATTRIBUTE_MASSIVE_OF_DATA, eElementTEMPERATURE.getAttribute(TAG_min) + ","
                            + cloudiness[Integer.parseInt(eElementPHENOMENA.getAttribute(TAG_cloudiness))] + ","
                            + precipitation[Integer.parseInt(eElementPHENOMENA.getAttribute(TAG_precipitation))]);
                    data.add(m);
                    bigM.put(ATTRIBUTE_MASSIVE_OF_DATA_BIG,eElementTEMPERATURE.getAttribute(TAG_min)+".."+
                            eElementTEMPERATURE.getAttribute(TAG_max)+","
                            +cloudiness[Integer.parseInt(eElementPHENOMENA.getAttribute(TAG_cloudiness))]+","
                            +precipitation[Integer.parseInt(eElementPHENOMENA.getAttribute(TAG_precipitation))]);
                    bigM.put(ATTRIBUTE_PRESSURE,"Давление: "+eElementPressure.getAttribute(TAG_max)+"мм ртст");
                    bigM.put(ATTRIBUTE_WIND,"Ветер: "+windDirection[Integer.parseInt(eElementWind.getAttribute(TAG_WIND_direction))]+", "
                            +eElementWind.getAttribute(TAG_min)+"-"+eElementWind.getAttribute(TAG_max));
                    bigM.put(ATTRIBUTE_RELVET,"Относительная влажность: "+eElementRelvet.getAttribute(TAG_max));
                    bigM.put(ATTRIBUTE_HEAT,"Комфортная температура: "+eElementHeat.getAttribute(TAG_min)+".."+eElementHeat.getAttribute(TAG_max));
                    bigData.add(bigM);
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MainListActivity.setBigData(bigData);
            simpleAdapter.notifyDataSetChanged();
        }
    }
}
