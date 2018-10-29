package mary.travelcards;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static mary.travelcards.MainActivity.ALL;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    public static final String VARIANTS_KEY = "variants";

    private ArrayList<Card> cards;
    private Context context;

    public CardsAdapter(ArrayList<Card> cards, Context context) {
        this.cards = cards;
        this.context = context;
    }

    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_card, parent, false);
        return new CardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardsAdapter.ViewHolder holder, int position) {
        Card card = cards.get(position);
        HashMap<String, Integer> companiesWithPrices = card.getFlights();

        //Если нет фильтра по авиакомпании
        if (card.getCompanyNameFilter().equals(ALL)) {
            HashMap.Entry<String, Integer> minEntry = null;
            for (HashMap.Entry<String, Integer> entry : companiesWithPrices.entrySet()) {
                //находим строчку с минимальной ценой, прибавить отель и залить в карточку
                if ((minEntry == null) || (entry.getValue() < minEntry.getValue())) {
                    minEntry = entry;
                }
            }
            int minPrice = minEntry.getValue();
            holder.totalPrice.setText(String.valueOf(minPrice + card.getHotelPrice()) + "р");
            holder.companyWithPrice.setText("Летим с " + minEntry.getKey());
        } else {
            //Если есть фильтр по авиакомпании
            for (HashMap.Entry<String, Integer> entry : companiesWithPrices.entrySet()) {
                //цена перелета выбранной авиакомпании + цена отеля
                if (entry.getKey().equals(card.getCompanyNameFilter())) {
                    holder.totalPrice.setText(String.valueOf(entry.getValue() + card.getHotelPrice()) + "р");
                    holder.companyWithPrice.setText("Летим с " + entry.getKey());
                }
            }
        }
        holder.name.setText(card.getHotelName());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView totalPrice;
        private TextView companyWithPrice;

        private ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.hotelName);
            totalPrice = view.findViewById(R.id.totalPrice);
            companyWithPrice = view.findViewById(R.id.companyPrice);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Card cardClicked = cards.get(getAdapterPosition());
            final String[] variantsStrings = TravelDataManager.getManager().createFlightVariantsStrings(cardClicked);
            FlightsDialog flightsDialog = new FlightsDialog();
            Bundle bundle = new Bundle();
            bundle.putStringArray(VARIANTS_KEY, variantsStrings);
            flightsDialog.setArguments(bundle);
            if (context instanceof MainActivity) {
                flightsDialog.show(((MainActivity) context).getFragmentManager(), "");
            }
        }
    }
}