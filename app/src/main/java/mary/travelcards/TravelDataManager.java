package mary.travelcards;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static mary.travelcards.MainActivity.ALL;
import static mary.travelcards.MainActivity.COMPANY;
import static mary.travelcards.MainActivity.FLIGHT;
import static mary.travelcards.MainActivity.HOTEL;

public class TravelDataManager {

    private static final String HOTELS_URL = "https://5bcf90bce7268800136fb9f2.mockapi.io/hotels";
    private static final String FLIGHTS_URL = "https://5bcf90bce7268800136fb9f2.mockapi.io/flights";
    private static final String COMPANIES_URL = "https://5bcf90bce7268800136fb9f2.mockapi.io/companies";

    private static TravelDataManager manager;
    private HashMap<String, Wrapper> travelData;

    private HotelsWrapper hotelsWrapper;
    private FlightsWrapper flightsWrapper;
    private CompaniesWrapper companiesWrapper;

    private List<Hotel> hotels;
    private List<Flight> flights;
    private List<Company> companies;

    private TravelDataManager(Context context) {
        travelData = new HashMap<>();
    }

    public static void initManager(Context context) {
        if (manager == null) {
            manager = new TravelDataManager(context);
        }
    }

    public static TravelDataManager getManager() {
        return manager;
    }

    public HashMap<String, Wrapper> getTravelDataFromServer() {
        String[] URLStrings = {HOTELS_URL, FLIGHTS_URL, COMPANIES_URL};
        try {
            for (String currentURLString : URLStrings) {
                URL url = new URL(currentURLString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                Wrapper wrapper;
                switch (currentURLString) {
                    case HOTELS_URL:
                        wrapper = convertJsonToHotelsWrapperObject(bufferedReader);
                        travelData.put(HOTEL, wrapper);
                        break;
                    case FLIGHTS_URL:
                        wrapper = convertJsonToFlightsWrapperObject(bufferedReader);
                        travelData.put(FLIGHT, wrapper);
                        break;
                    case COMPANIES_URL:
                        wrapper = convertJsonToCompaniesWrapperObject(bufferedReader);
                        travelData.put(COMPANY, wrapper);
                        break;
                }
            }
            updateLists(travelData);
            return travelData;

        } catch (Exception e) {
            Log.e("TravelDataManager", e.getMessage());
        }
        return null;
    }

    private HotelsWrapper convertJsonToHotelsWrapperObject(BufferedReader bufferedReader) {
        final Gson gson = new Gson();
        hotelsWrapper = gson.fromJson(bufferedReader, HotelsWrapper.class);
        return hotelsWrapper;
    }

    private FlightsWrapper convertJsonToFlightsWrapperObject(BufferedReader bufferedReader) {
        final Gson gson = new Gson();
        flightsWrapper = gson.fromJson(bufferedReader, FlightsWrapper.class);
        return flightsWrapper;
    }

    private CompaniesWrapper convertJsonToCompaniesWrapperObject(BufferedReader bufferedReader) {
        final Gson gson = new Gson();
        companiesWrapper = gson.fromJson(bufferedReader, CompaniesWrapper.class);
        return companiesWrapper;
    }

    private void updateLists(HashMap<String, Wrapper> travelDataFromServer) {
        this.hotelsWrapper = (HotelsWrapper) travelDataFromServer.get(HOTEL);
        this.flightsWrapper = (FlightsWrapper) travelDataFromServer.get(FLIGHT);
        this.companiesWrapper = (CompaniesWrapper) travelDataFromServer.get(COMPANY);
        this.hotels = hotelsWrapper.getHotels();
        this.flights = flightsWrapper.getFlights();
        this.companies = companiesWrapper.getCompanies();
    }

    private String getCompanyNameForFlight(Flight flight) {
        String companyName = "";
        String companyId = flight.getCompanyId();
        for (int k = 0; k < companies.size(); k++) {
            if (companies.get(k).getId().equals(companyId)) {
                companyName = companies.get(k).getName();
            }
        }
        return companyName;
    }

    private Flight getFlightForFlightId(String flightId) {
        Flight flightForFlightId = new Flight();
        for (Flight fl : flights) {
            if (fl.getId().equals(flightId)) {
                flightForFlightId = fl;
                break;
            }
        }
        return flightForFlightId;
    }

    public String[] createFlightVariantsStrings(Card cardClicked) {
        HashMap<String, Integer> flights = cardClicked.getFlights();
        ArrayList<String> variantsStrings = new ArrayList<>();
        for (HashMap.Entry<String, Integer> entry : flights.entrySet()) {
            int totalPrice = entry.getValue() + cardClicked.getHotelPrice();
            variantsStrings.add(String.valueOf(totalPrice) + "р - " + entry.getKey());
        }
        return variantsStrings.toArray(new String[variantsStrings.size()]);
    }

    public String[] getCompaniesNamesArray() {
        String[] companiesNames = new String[companies.size() + 1];
        companiesNames[0] = ALL;
        for (int i = 0; i < companies.size(); i++){
            companiesNames[i + 1] = companies.get(i).getName();
        }
        return companiesNames;
    }

    public ArrayList<Card> createCardsArrayListForRecyclerView(String companyNameFilter) {
        ArrayList<Card> cards = new ArrayList<>();

        //для каждого отеля
        for (int i = 0; i < hotels.size(); i++) {
            Hotel currentHotel = hotels.get(i);
            String[] flightIdsForHotel = currentHotel.getFlights();
            HashMap<String, Integer> flightsHashMap = new HashMap<>();
            Boolean isMatchCompanyNameFilter = false;

            //для каждого flightId конкретного отеля
            for (int k = 0; k < flightIdsForHotel.length; k++) {
                Flight currentFlight = getFlightForFlightId(flightIdsForHotel[k]);
                String currentCompanyName = getCompanyNameForFlight(currentFlight);
                flightsHashMap.put(getCompanyNameForFlight(currentFlight), currentFlight.getPrice());
                if (currentCompanyName.equals(companyNameFilter)) {
                    isMatchCompanyNameFilter = true;
                }
            }
            //применяем фильтр
            if (companyNameFilter.equals(ALL) || isMatchCompanyNameFilter) {
                //заполняем карточку
                Card card = new Card();
                card.setHotelName(currentHotel.getName());
                card.setHotelPrice(currentHotel.getPrice());
                card.setFlights(flightsHashMap);
                card.setCompanyNameFilter(companyNameFilter);
                cards.add(card);
            }
        }
        return cards;
    }

}
