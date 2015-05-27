package com.svo.library.widget.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class RotateImageView extends ImageView
{
  private RotateAnimation rotateAnimation;

  public RotateImageView(Context paramContext)
  {
    super(paramContext);
  }

  public RotateImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    rotate();
  }

  public void rotate()
  {
    this.rotateAnimation = new RotateAnimation(0.0F, 360000.0F, 1, 0.5F, 1, 0.5F);
    this.rotateAnimation.setDuration(1000000L);
    this.rotateAnimation.setInterpolator(new LinearInterpolator());
    this.rotateAnimation.setFillAfter(true);
    startAnimation(this.rotateAnimation);
  }

  public void stopRotate()
  {
    clearAnimation();
  }
}