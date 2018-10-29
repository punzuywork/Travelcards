package mary.travelcards;


public class Hotel {

    private String id;
    private String[] flights;
    private String name;
    private int price;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getFlights() {
        return flights;
    }

    public void setFlights(String[] flights) {
        this.flights = flights;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
