package fr.appsolute.cardflinger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements FlingableCard.CardCallbacks{

    FrameLayout layout;
    List<FlingableCard> cards = new ArrayList<>();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        layout = (FrameLayout) rootView.findViewById(R.id.container);
        Button addCard = (Button) rootView.findViewById(R.id.add_card);

        for(int i = 0; i < 5; i++){
            FlingableCard card = new FlingableCard(getActivity(),"Text "+i,this);
            cards.add(card);
        }

        for (FlingableCard card : cards){
            card.setCardElevation(cards.size() - cards.indexOf(card));
            card.setRotation(-5 + new Random().nextInt(10));
            layout.addView(card, 0);
            Log.d("Card added", "Elevation : "+card.getCardElevation()+" Position : "+cards.indexOf(card)+" Card Text : "+card.toString());
        }

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cards.add(0,new FlingableCard(getActivity(),"Text 0",MainActivityFragment.this));

            }
        });


        return rootView;
    }

    @Override
    public void cardDismissed(FlingableCard dismissedCard) {
        permuteCards(dismissedCard);
        for (FlingableCard card : cards){
            Log.d("Card elevation/position", "Elevation : "+card.getCardElevation()+" Position : "+cards.indexOf(card)+" Card Text : "+card.toString());
        }
        dismissedCard.setX(0);
        dismissedCard.setY(0);
        Log.d("Layout count", ""+layout.getChildCount());
    }

    private void permuteCards(@Nullable FlingableCard dismissedCard){
        if(dismissedCard != null){
            dismissedCard.setCardElevation(1);
            cards.remove(dismissedCard);
            for(int i = 0; i < cards.size(); i++){
                cards.get(i).setCardElevation(cards.size() - i);
            }
            cards.add(dismissedCard);
        }else{
            //cards.add(0)
        }

    }
}
