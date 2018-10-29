package mary.travelcards;


import java.util.List;

public class FlightsWrapper extends Wrapper {

    private List<Flight> flights;


    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
