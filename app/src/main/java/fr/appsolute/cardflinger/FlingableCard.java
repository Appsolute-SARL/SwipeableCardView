package fr.appsolute.cardflinger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

/**
 * Created by Benjamin on 3/24/2015.
 *
 * Custom CardView that implements onTouchListener to move cards.
 */
public class FlingableCard extends CardView implements View.OnTouchListener {

    private TextView textContent;

    private long velocitySumX;
    private long velocitySumY;
    private long velocityMeasures;

    private float originalX;
    private float originalY;
    private float cardVelocityX;
    private float cardVelocityY;
    private VelocityTracker velocityTracker;
    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private OvershootInterpolator overshootInterpolator = new OvershootInterpolator(0.9f);
    private CardCallbacks onCardEventListener;

    public interface CardCallbacks{
        void cardDismissed(FlingableCard dismissedCard);
    }

    public FlingableCard(Context context) {
        super(context);
        setOnTouchListener(this);

    }

    public FlingableCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.FlingableCard,
                0,0);

        try{
            setText(a.getString(R.styleable.FlingableCard_text));
            textContent.setPadding(50, 50, 50, 50);
        } finally{
            a.recycle();
            addView(textContent);
        }
        setOnTouchListener(this);
    }

    public FlingableCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    public FlingableCard(Context context,FlingableCard card){
        super(context);
        setText(card.getText());
        textContent.setPadding(50, 50, 50, 50);
        onCardEventListener = card.getOnCardEventListener();
        addView(textContent);
        setOnTouchListener(this);
    }

    public FlingableCard(Context context,String text, CardCallbacks callbacks){
        super(context);
        setText(text);
        textContent.setPadding(50, 50, 50, 50);
        onCardEventListener = callbacks;
        addView(textContent);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                originalX = event.getX();
                originalY = event.getY();
                velocityTracker = VelocityTracker.obtain();
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - originalX;
                float dy = event.getY() - originalY;
                setTranslationX(getTranslationX() + dx);
                setTranslationY(getTranslationY() + dy);

                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);

                velocitySumX += velocityTracker.getXVelocity();
                velocitySumY += velocityTracker.getYVelocity();
                velocityMeasures +=1;
                break;

            case MotionEvent.ACTION_UP:
                if((velocitySumX/velocityMeasures) > 200 && (velocitySumY/velocityMeasures) < 0){
                    Log.d("Mean velocities","Mean X velocity : "+(velocitySumX/velocityMeasures)+" Mean Y velocity : "+(velocitySumY/velocityMeasures));
                    animate().setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime))
                            .setInterpolator(decelerateInterpolator)
                            .xBy(velocitySumX / velocityMeasures)
                            .yBy(velocitySumY / velocityMeasures)
                            .alphaBy(.75f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (onCardEventListener != null)
                                        onCardEventListener.cardDismissed(FlingableCard.this);
                                    setAlpha(1);
                                    setX(0);
                                    setY(0);
                                }
                            });
                    velocitySumX = 0;
                    velocitySumY = 0;
                    velocityMeasures = 0;
                }else{
                    animate().x(0)
                            .y(0)
                            .setListener(null)
                            .setInterpolator(overshootInterpolator)
                            .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));

                    velocitySumX = 0;
                    velocitySumY = 0;
                    velocityMeasures = 0;
                }
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Log.d("Card dimensions", "" + getMeasuredHeight() + " : " + getMeasuredWidth());
    }

    public void setText(String text){
        if(textContent != null)
            textContent.setText(text);
        else{
            textContent = new TextView(getContext());
            textContent.setText(text);
        }
    }

    public String getText(){
        return textContent.getText().toString();
    }

    public TextView getTextContent() {
        return textContent;
    }

    public void setOnCardEventListener(CardCallbacks callbacks){
        this.onCardEventListener = callbacks;
    }

    public CardCallbacks getOnCardEventListener() {
        return onCardEventListener;
    }

    @Override
    public String toString() {
        return getText()+" Elevation : "+getCardElevation();
    }
}