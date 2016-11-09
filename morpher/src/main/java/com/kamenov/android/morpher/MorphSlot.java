package com.kamenov.android.morpher;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Borislav on 24.5.2016 г..
 */
public class MorphSlot extends ImageView {

    private Context mContext;
    private int mDigit;

    private float mStrokeWidth;
    private int mDuration;

    public MorphSlot(Context context, float strokeWidth, int textColor, int duration) {
        this(context, null, strokeWidth, textColor, duration);
    }

    public MorphSlot(Context context, AttributeSet attrs, float strokeWidth, int textColor, int duration) {
        this(context, attrs, 0, strokeWidth, textColor, duration);
    }

    public MorphSlot(Context context, AttributeSet attrs, int defStyleAttr, float strokeWidth, int textColor, int duration) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        setAdjustViewBounds(true);
        setImageResource(R.drawable.zero_to_four_animated_vector);
        setStrokeWidth(strokeWidth);
        setTextColor(textColor);
        setDuration(duration);
    }

    // Public methods
    public void setDigit(int digit, boolean disableAnimation) {

        if (mDigit != digit) {
            Drawable drawable = ContextCompat.getDrawable(mContext, getDrawableId(digit));

            resolveDuration(drawable, disableAnimation ? 0 : mDuration);
            resolveStrokeWidth(drawable, mStrokeWidth);
            setImageDrawable(drawable);

            if (getDrawable() instanceof Animatable) {
                ((Animatable) getDrawable()).start();
            }
            mDigit = digit;
        }
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        resolveStrokeWidth(getDrawable(), strokeWidth);
    }

    public void setDuration(int duration) {
        mDuration = duration;
        resolveDuration(getDrawable(), duration);
    }

    public void setTextColor(int textColor) {
        setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
    }

    // Private methods
    private void resolveDuration(Drawable drawable, int duration) {

        if (duration == Morpher.DEFAULT_DURATION) {
            // Desired duration is the same as default one, so do not change it.
            return;
        }

        try {
            Field animatedVectorStateField = drawable.getClass().getDeclaredField("mAnimatedVectorState");
            animatedVectorStateField.setAccessible(true);
            Object animatedVectorDrawableStateObj = animatedVectorStateField.get(drawable);

            Field animatorsField = animatedVectorDrawableStateObj.getClass().getDeclaredField("mAnimators");
            animatorsField.setAccessible(true);
            ArrayList<AnimatorSet> mAnimatorsObj = (ArrayList<AnimatorSet>) animatorsField.get(animatedVectorDrawableStateObj);

            if (mAnimatorsObj != null && mAnimatorsObj.size() > 0) {
                AnimatorSet mAnimatorSet = mAnimatorsObj.get(0);

                Field mDurationFirstField = mAnimatorSet.getClass().getDeclaredField("mDuration");
                mDurationFirstField.setAccessible(true);
                mDurationFirstField.set(mAnimatorSet, duration);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void resolveStrokeWidth(Drawable drawable, float strokeWidth) {

        if (strokeWidth == Morpher.DEFAULT_STROKE_WIDTH) {
            // Desired stroke width is the same as default one, so do not change it.
            return;
        }

        try {
            Field animatedVectorStateField = drawable.getClass().getDeclaredField("mAnimatedVectorState");
            animatedVectorStateField.setAccessible(true);
            Object animatedVectorDrawableStateObj = animatedVectorStateField.get(drawable);

            Field mVectorDrawableField = animatedVectorDrawableStateObj.getClass().getDeclaredField("mVectorDrawable");
            mVectorDrawableField.setAccessible(true);
            VectorDrawable vectorDrawableObj = (VectorDrawable) mVectorDrawableField.get(animatedVectorDrawableStateObj);

            Field mVectorDrawableStateField = vectorDrawableObj.getClass().getDeclaredField("mVectorState");
            mVectorDrawableStateField.setAccessible(true);
            Object mVectorStateObj = mVectorDrawableStateField.get(vectorDrawableObj);

            Field mVPathRendererField = mVectorStateObj.getClass().getDeclaredField("mVPathRenderer");
            mVPathRendererField.setAccessible(true);
            Object mVPathRendererObj = mVPathRendererField.get(mVectorStateObj);

            Field mRootGroupField = mVPathRendererObj.getClass().getDeclaredField("mRootGroup");
            mRootGroupField.setAccessible(true);
            Object mVGroup = mRootGroupField.get(mVPathRendererObj);

            Field mChildrenField = mVGroup.getClass().getDeclaredField("mChildren");
            mChildrenField.setAccessible(true);
            ArrayList<Object> mChildrenObj = (ArrayList<Object>) mChildrenField.get(mVGroup);

            if (mChildrenObj != null && mChildrenObj.size() > 0) {
                Object mVFullPath = mChildrenObj.get(0);
                Method method = mVFullPath.getClass().getDeclaredMethod("setStrokeWidth", Float.TYPE);
                method.setAccessible(true);
                method.invoke(mVFullPath, strokeWidth);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private int getDrawableId(int goToDigit) {
        String resourceName = String.format("%s_to_%s_animated_vector", getDigitName(mDigit), getDigitName(goToDigit));
        return mContext.getResources().getIdentifier(resourceName, "drawable", mContext.getPackageName());
    }

    private String getDigitName(int digit) {
        switch (digit) {
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
            default:
                return "zero";
        }
    }

    static class Builder {

        private Context context;

        private float strokeWidth;
        private int textColor;
        private int duration;

        Builder(Context context) {
            this.context = context;
        }

        Builder strokeWidth(float strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        MorphSlot build() {
            return new MorphSlot(context, strokeWidth, textColor, duration);
        }
    }
}
