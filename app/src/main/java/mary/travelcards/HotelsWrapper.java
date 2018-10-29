package mary.travelcards;


import java.util.List;

public class HotelsWrapper extends Wrapper {

    private List<Hotel> hotels;


    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }
}
