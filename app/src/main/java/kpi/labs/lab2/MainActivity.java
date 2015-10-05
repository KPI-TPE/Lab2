package kpi.labs.lab2;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity {


    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.res);
        final Core core = new Core(-30, -35, 0, 10, 8);
        core.calculation();
        adapter.strings = core.generateTable();
        final GridView gridView = (GridView) findViewById(R.id.mas);
        adapter = new Adapter(getApplicationContext(), android.R.layout.simple_list_item_1);
        gridView.setAdapter(adapter);
        textView.setText(core.getText());
        textView.setTextSize(12);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Core core = new Core(-30, -35, 0, 10, 8);
                core.calculation();
                adapter.strings = core.generateTable();
                adapter = new Adapter(getApplicationContext(), android.R.layout.simple_list_item_1);
                gridView.setAdapter(adapter);
                textView.setText(core.getText());
                textView.setTextSize(12);
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
