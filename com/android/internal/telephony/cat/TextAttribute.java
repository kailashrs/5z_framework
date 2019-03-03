package com.android.internal.telephony.cat;

public class TextAttribute
{
  public TextAlignment align;
  public boolean bold;
  public TextColor color;
  public boolean italic;
  public int length;
  public FontSize size;
  public int start;
  public boolean strikeThrough;
  public boolean underlined;
  
  public TextAttribute(int paramInt1, int paramInt2, TextAlignment paramTextAlignment, FontSize paramFontSize, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, TextColor paramTextColor)
  {
    start = paramInt1;
    length = paramInt2;
    align = paramTextAlignment;
    size = paramFontSize;
    bold = paramBoolean1;
    italic = paramBoolean2;
    underlined = paramBoolean3;
    strikeThrough = paramBoolean4;
    color = paramTextColor;
  }
}
