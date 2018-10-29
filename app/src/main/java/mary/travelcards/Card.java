package mary.travelcards;


import java.util.HashMap;

public class Card {

    private String id;
    private String hotelName;
    private int hotelPrice;
    private HashMap<String, Integer> flights;
    private String companyNameFilter;

    public String getCompanyNameFilter() {
        return companyNameFilter;
    }

    public void setCompanyNameFilter(String companyNameFilter) {
        this.companyNameFilter = companyNameFilter;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(int hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public HashMap<String, Integer> getFlights() {
        return flights;
    }

    public void setFlights(HashMap<String, Integer> flights) {
        this.flights = flights;
    }
}
