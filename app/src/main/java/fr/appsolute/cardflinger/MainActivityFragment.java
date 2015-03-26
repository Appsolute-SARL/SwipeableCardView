package fr.appsolute.cardflinger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    private final static int VISIBLE_CARD_NUMBER = 3;

    FrameLayout layout;
    List<FlingableCard> cards = new ArrayList<>();
    List<FlingableCard> subsetOfVisibleCards = new ArrayList<>(5);

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        layout = (FrameLayout) rootView.findViewById(R.id.container);
        Button addCard = (Button) rootView.findViewById(R.id.add_card);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(20,20,20,20);

        for(int i = 0; i < 50; i++){
            FlingableCard card = new FlingableCard(getActivity(),"Text "+i,this);
            card.setLayoutParams(params);
            cards.add(card);
        }

        for(int i =0; i < VISIBLE_CARD_NUMBER; i++){
            subsetOfVisibleCards.add(cards.get(i));
        }

        for(int i = 0; i < subsetOfVisibleCards.size(); i++){
            layout.addView(cards.get(i), layout.getChildCount());
            subsetOfVisibleCards.get(i).setCardElevation((subsetOfVisibleCards.size() - i)*3);
            subsetOfVisibleCards.get(i).setRotation(-5 + new Random().nextInt(10));
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
        layout.removeView(dismissedCard);
        permuteCards(dismissedCard);
    }

    /**
     * Method that applies a circular permutation ([0,1,...,n] = [1,2,...,n,0]) to the card collection
     * and add the next card in the collection at the end of the {@link} #subsetOfVisibleCards
     *
     * @param dismissedCard : the card that has been dismissed from the stack.
     */
    private void permuteCards(@Nullable FlingableCard dismissedCard){
        if(dismissedCard != null){

            FlingableCard nextCard = cards.get(cards.indexOf(dismissedCard) + VISIBLE_CARD_NUMBER); //Get the next card in the overall collection
            subsetOfVisibleCards.remove(dismissedCard);
            subsetOfVisibleCards.add(subsetOfVisibleCards.size(), nextCard);

            cards.remove(dismissedCard);
            cards.add(dismissedCard); //Add the dismissed card to the end of the overall collection
            nextCard.setRotation(-5 + new Random().nextInt(10)); //Apply random rotation between +5� and +5�
            layout.addView(nextCard); //Display the card


            for(int i = 0; i < subsetOfVisibleCards.size(); i++){
                subsetOfVisibleCards.get(i).setCardElevation((subsetOfVisibleCards.size() - i) * 3); //set the elevation of the card in the subset to the right value again
            }
        }

    }
}