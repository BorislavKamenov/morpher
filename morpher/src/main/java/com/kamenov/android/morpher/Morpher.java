package com.kamenov.android.morpher;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Morpher extends LinearLayout {

    static final int DEFAULT_ZEROES = 1;
    static final int DEFAULT_DURATION = 350;
    static final int DEFAULT_COLOR = Color.BLACK;
    static final int DEFAULT_STROKE_WIDTH = 20;

    private Context mContext;

    private int mZeroFiller;
    private String mNumber = "0";
    private List<MorphSlot> mDigitsSlots;

    private int mDuration;
    private float mStrokeWidth;
    private int mTextColor;

    public Morpher(Context context) {
        this(context, null);
    }

    public Morpher(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Morpher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        mContext = context;

        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.Morpher, 0, 0);
        try {
            mZeroFiller = ta.getInteger(R.styleable.Morpher_zeroFiller, DEFAULT_ZEROES);
            mDuration = ta.getInteger(R.styleable.Morpher_duration, DEFAULT_DURATION);
            mTextColor = ta.getColor(R.styleable.Morpher_textColor, DEFAULT_COLOR);
            mStrokeWidth = ta.getFloat(R.styleable.Morpher_strokeWidth, DEFAULT_STROKE_WIDTH);
        } finally {
            ta.recycle();
        }

        setupParams();
        buildLayout();
    }

    // Public methods
    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        invalidateSlots();
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidateSlots();
    }

    public void setDuration(int duration) {
        mDuration = duration;
        invalidateSlots();
    }

    public void setZeroFiller(int zeroFiller) {
        this.mZeroFiller = zeroFiller;
        String txtNumber = resolveZeroFiller(Integer.valueOf(mNumber));
        resolveMorphSlots(txtNumber);
        mNumber = txtNumber;
    }

    public void setNumber(int number) {
        setNumber(number, false);
    }

    public void setNumber(int number, boolean disableAnimation) {
        String txtNumber = resolveZeroFiller(number);

        resolveMorphSlots(txtNumber);
        for (int i = txtNumber.length() - 1; i >= 0; i--) {
            mDigitsSlots.get(i).setDigit(Integer.parseInt(String.valueOf(txtNumber.charAt(i))), disableAnimation);
        }
        mNumber = txtNumber;
    }

    public int getNumber() {
        return Integer.valueOf(mNumber);
    }

    // Private methods
    private void setupParams() {
        setOrientation(HORIZONTAL);
        mDigitsSlots = new ArrayList<>();
    }

    private void buildLayout() {

        mNumber = resolveZeroFiller(Integer.valueOf(mNumber));

        for (int i = 0; i < mNumber.length(); i++) {
            MorphSlot morphSlot = new MorphSlot.Builder(mContext).strokeWidth(mStrokeWidth).textColor(mTextColor).duration(mDuration).build();
            morphSlot.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mDigitsSlots.add(morphSlot);
            addView(morphSlot);
        }
    }

    private void invalidateSlots() {
        for (MorphSlot morphSlot : mDigitsSlots) {
            morphSlot.setStrokeWidth(mStrokeWidth);
            morphSlot.setTextColor(mTextColor);
            morphSlot.setDuration(mDuration);
        }
    }

    private String resolveZeroFiller(int number) {
        if (mZeroFiller == 0) {
            return String.valueOf(number);
        } else {
            String formatter = "%0" + mZeroFiller + "d";
            return String.format(formatter, number);
        }
    }

    private void resolveMorphSlots(String txtNumber) {
        int deltaDigits = txtNumber.length() - mNumber.length();

        if (deltaDigits > 0) {
            // Add slots
            for (int i = 0; i < deltaDigits; i++) {
                MorphSlot morphSlot = new MorphSlot.Builder(mContext).strokeWidth(mStrokeWidth).textColor(mTextColor).duration(mDuration).build();
                morphSlot.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (mDigitsSlots.size() == 0) {
                    mDigitsSlots.add(morphSlot);
                } else {
                    mDigitsSlots.add(i, morphSlot);
                }
                addView(morphSlot, i);
            }
        } else if (deltaDigits < 0) {
            // Remove slots
            for (int i = 0; i < Math.abs(deltaDigits); i++) {
                mDigitsSlots.remove(0);
                removeViewAt(0);
            }
        }
    }
}
