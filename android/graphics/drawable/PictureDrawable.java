package android.graphics.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Picture;
import android.graphics.Rect;

public class PictureDrawable
  extends Drawable
{
  private Picture mPicture;
  
  public PictureDrawable(Picture paramPicture)
  {
    mPicture = paramPicture;
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (mPicture != null)
    {
      Rect localRect = getBounds();
      paramCanvas.save();
      paramCanvas.clipRect(localRect);
      paramCanvas.translate(left, top);
      paramCanvas.drawPicture(mPicture);
      paramCanvas.restore();
    }
  }
  
  public int getIntrinsicHeight()
  {
    int i;
    if (mPicture != null) {
      i = mPicture.getHeight();
    } else {
      i = -1;
    }
    return i;
  }
  
  public int getIntrinsicWidth()
  {
    int i;
    if (mPicture != null) {
      i = mPicture.getWidth();
    } else {
      i = -1;
    }
    return i;
  }
  
  public int getOpacity()
  {
    return -3;
  }
  
  public Picture getPicture()
  {
    return mPicture;
  }
  
  public void setAlpha(int paramInt) {}
  
  public void setColorFilter(ColorFilter paramColorFilter) {}
  
  public void setPicture(Picture paramPicture)
  {
    mPicture = paramPicture;
  }
}
