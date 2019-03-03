package android.text;

import android.graphics.BaseCanvas;
import android.graphics.Paint;

public abstract interface GraphicsOperations
  extends CharSequence
{
  public abstract void drawText(BaseCanvas paramBaseCanvas, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, Paint paramPaint);
  
  public abstract void drawTextRun(BaseCanvas paramBaseCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, boolean paramBoolean, Paint paramPaint);
  
  public abstract float getTextRunAdvances(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, float[] paramArrayOfFloat, int paramInt5, Paint paramPaint);
  
  public abstract int getTextRunCursor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Paint paramPaint);
  
  public abstract int getTextWidths(int paramInt1, int paramInt2, float[] paramArrayOfFloat, Paint paramPaint);
  
  public abstract float measureText(int paramInt1, int paramInt2, Paint paramPaint);
}
