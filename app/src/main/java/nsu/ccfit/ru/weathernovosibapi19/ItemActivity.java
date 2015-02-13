package nsu.ccfit.ru.weathernovosibapi19;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Android on 22.11.2014.
 */
public class ItemActivity extends Activity {
    private final String ATTRIBUTE_DATE = "date";
    private final String ATTRIBUTE_DAY_TIME = "day_time";
    private final String ATTRIBUTE_MASSIVE_OF_DATA_BIG ="data";
    private final String ATTRIBUTE_PRESSURE = "pressure";
    private final String ATTRIBUTE_WIND = "wind";
    private final String ATTRIBUTE_RELVET ="relvet";
    private final String ATTRIBUTE_HEAT = "heat";
    String[] from = { ATTRIBUTE_DATE, ATTRIBUTE_DAY_TIME,
            ATTRIBUTE_MASSIVE_OF_DATA_BIG,ATTRIBUTE_PRESSURE, ATTRIBUTE_WIND,
            ATTRIBUTE_RELVET, ATTRIBUTE_HEAT};
    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.tvDate, R.id.tvTimeDay, R.id.tvDataLook };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intent = getIntent();
        try{
            TextView textView = (TextView)findViewById(R.id.tvDateA);
            textView.setText(intent.getStringExtra(ATTRIBUTE_DATE));
            textView = (TextView)findViewById(R.id.tvTImeDayA);
            textView.setText(intent.getStringExtra(ATTRIBUTE_DAY_TIME));
            textView = (TextView) findViewById(R.id.tvDataA);
            textView.setText(intent.getStringExtra(ATTRIBUTE_MASSIVE_OF_DATA_BIG));
            textView = (TextView) findViewById(R.id.tvPressureA);
            textView.setText(intent.getStringExtra(ATTRIBUTE_PRESSURE));
            textView = (TextView) findViewById(R.id.tvWindA);
            textView.setText(intent.getStringExtra(ATTRIBUTE_WIND));
            textView = (TextView) findViewById(R.id.tvRelvetA);
            textView.setText(intent.getStringExtra(ATTRIBUTE_RELVET));
            textView = (TextView) findViewById(R.id.tvHeatA);
            textView.setText(intent.getStringExtra(ATTRIBUTE_HEAT));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
