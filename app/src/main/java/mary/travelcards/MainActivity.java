package mary.travelcards;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final String HOTEL = "hotel";
    public static final String FLIGHT = "flight";
    public static final String COMPANY = "company";

    public static final String ALL = "Все";

    private TravelDataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TravelDataManager.initManager(this);
        manager = TravelDataManager.getManager();
        new GetTravelDataAsync().execute(this);
    }


    private class GetTravelDataAsync extends AsyncTask<Context, Void, HashMap<String, Wrapper>> {

        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, Wrapper> doInBackground(Context... params) {
            context = params[0];
            return manager.getTravelDataFromServer();
        }

        @Override
        protected void onPostExecute(HashMap<String, Wrapper> result) {
            super.onPostExecute(result);
            setDataToRecyclerView(ALL);
            setDataToCompaniesSpinner();
        }
    }

    public void setDataToRecyclerView(String companyNameFilter) {
        RecyclerView recyclerView = findViewById(R.id.hotelsRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        CardsAdapter cardsAdapter = new CardsAdapter(manager.createCardsArrayListForRecyclerView(companyNameFilter), this);
        recyclerView.setAdapter(cardsAdapter);
    }

    public void setDataToCompaniesSpinner() {
        final String[] companiesNames = manager.getCompaniesNamesArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, companiesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                setDataToRecyclerView(companiesNames[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


}