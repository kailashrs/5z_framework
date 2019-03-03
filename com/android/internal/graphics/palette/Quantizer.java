package com.android.internal.graphics.palette;

import java.util.List;

public abstract interface Quantizer
{
  public abstract List<Palette.Swatch> getQuantizedColors();
  
  public abstract void quantize(int[] paramArrayOfInt, int paramInt, Palette.Filter[] paramArrayOfFilter);
}
