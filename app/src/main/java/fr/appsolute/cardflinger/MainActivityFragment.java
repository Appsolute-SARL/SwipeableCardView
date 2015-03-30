package fr.appsolute.cardflinger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment{

    CardContainer cardContainer;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        cardContainer = (CardContainer) rootView.findViewById(R.id.container);

        List<FlingableCard> cards = new ArrayList<>();
        for(int i = 0; i < 25; i++){
            FlingableCard card;
            switch(i%8){
                case 0:
                    card = new FlingableCard(getActivity(),"Text "+i,ResourcesCompat.getDrawable(getResources(),R.drawable.picture_8,null) ,cardContainer);
                    cards.add(card);
                    break;
                case 1:
                    card = new FlingableCard(getActivity(),"Text "+i,ResourcesCompat.getDrawable(getResources(),R.drawable.picture_1,null) ,cardContainer);
                    cards.add(card);
                    break;
                case 2:
                    card = new FlingableCard(getActivity(),"Text "+i,ResourcesCompat.getDrawable(getResources(),R.drawable.picture_2,null) ,cardContainer);
                    cards.add(card);
                    break;
                case 3:
                    card = new FlingableCard(getActivity(),"Text "+i,ResourcesCompat.getDrawable(getResources(),R.drawable.picture_3,null) ,cardContainer);
                    cards.add(card);
                    break;
                case 4:
                    card = new FlingableCard(getActivity(),"Text "+i,ResourcesCompat.getDrawable(getResources(),R.drawable.picture_4,null) ,cardContainer);
                    cards.add(card);
                    break;
                case 5:
                    card = new FlingableCard(getActivity(),"Text "+i,ResourcesCompat.getDrawable(getResources(),R.drawable.picture_5,null) ,cardContainer);
                    cards.add(card);
                    break;
                case 6:
                    card = new FlingableCard(getActivity(),"Text "+i,ResourcesCompat.getDrawable(getResources(),R.drawable.picture_6,null) ,cardContainer);
                    cards.add(card);
                    break;
                case 7:
                    card = new FlingableCard(getActivity(),"Text "+i,ResourcesCompat.getDrawable(getResources(),R.drawable.picture_7,null) ,cardContainer);
                    cards.add(card);
                    break;
                default:
                    card = new FlingableCard(getActivity(),"Text "+i,ResourcesCompat.getDrawable(getResources(),R.drawable.picture_8,null) ,cardContainer);
                    cards.add(card);
                    break;
            }
        }

        try {
            cardContainer.populateFullCardsList(cards);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cardContainer.layOutViews();

        return rootView;
    }


}
