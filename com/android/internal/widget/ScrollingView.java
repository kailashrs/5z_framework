package com.android.internal.widget;

public abstract interface ScrollingView
{
  public abstract int computeHorizontalScrollExtent();
  
  public abstract int computeHorizontalScrollOffset();
  
  public abstract int computeHorizontalScrollRange();
  
  public abstract int computeVerticalScrollExtent();
  
  public abstract int computeVerticalScrollOffset();
  
  public abstract int computeVerticalScrollRange();
}
