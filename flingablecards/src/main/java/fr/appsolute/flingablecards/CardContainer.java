package fr.appsolute.flingablecards;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Benjamin on 3/26/2015.
 * Container for the FlingableCards. This is created in an attempt to make the flingable cards
 * independent from the application cardContainer.
 */
public class CardContainer extends FrameLayout implements FlingableCard.CardCallbacks {

    public byte visibleCardNumber = 5;
    public List<FlingableCard> subsetOfVisibleCards = new ArrayList<>(visibleCardNumber);
    public List<FlingableCard> fullCardList = new ArrayList<>();

    private boolean messyStack = false;
    private int tiltAngle = 5;
    private int cornerRadius = 5;

    private boolean infiniteStack = true;

    public CardContainer(Context context) {
        super(context);
    }

    public CardContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CardContainer,0,0);
        try{
            messyStack = a.getBoolean(R.styleable.CardContainer_messy,false);
            cornerRadius = a.getInt(R.styleable.CardContainer_corner_radius,5);
            tiltAngle = a.getInt(R.styleable.CardContainer_angular_amplitude,5);
            visibleCardNumber = (byte)a.getInt(R.styleable.CardContainer_displayed_cards,5);
            infiniteStack = a.getBoolean(R.styleable.CardContainer_inifinite,true);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            a.recycle();
        }
    }

    public CardContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void populateFullCardsList(List<FlingableCard> cards) throws Exception {
        if(cards.size() < visibleCardNumber)
            throw new Exception("Cards list size : "+cards.size()+" is less than the visible cards number : "+visibleCardNumber);
        for(int i = 0; i < cards.size(); i++){
            fullCardList.add(cards.get(i));
        }
        setCornerRadius(cornerRadius);
        populateSubset();
    }

    public List<FlingableCard> populateSubset(){
        for(int i = 0; i < visibleCardNumber; i++)
            subsetOfVisibleCards.add(fullCardList.get(i));
        return subsetOfVisibleCards;
    }

    /**
     * Call this method last as it add the cards to the card container. Should you fail to call this
     * after setting the parameters of your cards like tilt value, your cards won't take them into
     * account.
     */
    public void layOutViews(){
        for (int i = 0; i < subsetOfVisibleCards.size(); i++){
            addView(subsetOfVisibleCards.get(i), 0);
        }
        if(messyStack)
            applyMess(tiltAngle);
    }

    @Override
    public void addView(@NonNull View child, int index) {
        super.addView(child, index);
        for(int i = 0; i < visibleCardNumber; i++){
            subsetOfVisibleCards.get(i).setCardElevation((visibleCardNumber - i) * 3);
        }

    }

    public void addView(@NonNull View child, int index, int rotation){
        addView(child, index);
        for(int i = 0; i < visibleCardNumber; i++){
            subsetOfVisibleCards.get(i).setRotation(rotation);
        }
    }

    /**
     * Apply original tilting to the cards and be done with it.
     * @param angularAmplitude : the amplitude of the angle the cards will be rotated. The amplitude
     *                         actually is 2 times this value since it can go from -angularAmplitude
     *                         to +angularAmplitude.
     */
    public void applyMess(int angularAmplitude){
        for (FlingableCard card : fullCardList){
            card.setRotation(-angularAmplitude + new Random().nextInt(2*angularAmplitude));
        }
    }

    /**
     * Set the corner radius for all cards in the list
     * @param cornerRadiusPx : the corner radius to apply in pixels.
     */
    public void setCornerRadius(int cornerRadiusPx){
        cornerRadius = cornerRadiusPx;
        for(FlingableCard card : fullCardList)
            card.setRadius(cornerRadius);
    }

    /**
     * Method that applies a circular permutation ([0,1,...,n] = [1,2,...,n,0]) to the card collection
     * and add the next card in the collection at the end of the {@link} #subsetOfVisibleCards
     *
     * @param dismissedCard : the card that has been dismissed from the stack.
     */
    public void permuteCards(FlingableCard dismissedCard){
        if(fullCardList.indexOf(dismissedCard) + visibleCardNumber < fullCardList.size()){
            FlingableCard nextCard = fullCardList.get(fullCardList.indexOf(dismissedCard) + visibleCardNumber); //Get the next card in the overall collection
            subsetOfVisibleCards.remove(dismissedCard);
            subsetOfVisibleCards.add(subsetOfVisibleCards.size(), nextCard);

            fullCardList.remove(dismissedCard);
            if(infiniteStack)
                fullCardList.add(dismissedCard); //Add the dismissed card to the end of the overall collection

            addView(nextCard, 0); //Display the card at the end of the stack
        }
    }


    /***************************Interface Implementation***************************/
    @Override
    public void cardDismissedRight(FlingableCard dismissedCard) {
        removeView(dismissedCard);
        permuteCards(dismissedCard);
        Log.d("Card container", "Dismissed Right");
    }

    @Override
    public void cardDismissedLeft(FlingableCard dismissedCard) {
        removeView(dismissedCard);
        permuteCards(dismissedCard);
        Log.d("Card container", "Dismissed Left");
    }

    /***************************Accessors***************************/
    public boolean isMessyStack() {
        return messyStack;
    }

    /**
     * Setting this value to true will make the cards tilt between the default values
     * -5 degrees and +5 degrees for the stack to appear messy. Setting this to no will tidy up the cards.
     * @param messyStack : determine if the cards are messy or not.
     */
    public void setMessyStack(boolean messyStack) {
        this.messyStack = messyStack;
        if(!messyStack)
            tiltAngle = 5; //reinitialize the tilt angle to its default value.
    }

    /**
     * Convenience method to directly set the messiness to true and give the tilt angle that will be
     * applied to the cards. To set the messiness to false use the previous method.
     * Careful ! : Values above 45 degrees will see weird behavior happen.
     * @param angularAmplitude : the angle with which the cards will be tilted. The resulting rotation
     *                         angle of the cards is between -angularAmplitude and +angularAmplitude.
     */
    public void setMessyStack(int angularAmplitude) {
        this.messyStack = true;
        tiltAngle = angularAmplitude;
    }


}
