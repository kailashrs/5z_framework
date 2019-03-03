package android.text;

import android.icu.lang.UCharacter;

public class Emoji
{
  public static int CANCEL_TAG = 917631;
  public static int COMBINING_ENCLOSING_KEYCAP = 8419;
  public static int VARIATION_SELECTOR_16;
  public static int ZERO_WIDTH_JOINER = 8205;
  
  static
  {
    VARIATION_SELECTOR_16 = 65039;
  }
  
  public Emoji() {}
  
  public static boolean isEmoji(int paramInt)
  {
    boolean bool;
    if ((!isNewEmoji(paramInt)) && (!UCharacter.hasBinaryProperty(paramInt, 57))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isEmojiModifier(int paramInt)
  {
    return UCharacter.hasBinaryProperty(paramInt, 59);
  }
  
  public static boolean isEmojiModifierBase(int paramInt)
  {
    if ((paramInt != 129309) && (paramInt != 129340))
    {
      if (((129461 <= paramInt) && (paramInt <= 129462)) || ((129464 <= paramInt) && (paramInt <= 129465))) {
        return true;
      }
      return UCharacter.hasBinaryProperty(paramInt, 60);
    }
    return true;
  }
  
  public static boolean isKeycapBase(int paramInt)
  {
    boolean bool;
    if (((48 > paramInt) || (paramInt > 57)) && (paramInt != 35) && (paramInt != 42)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isNewEmoji(int paramInt)
  {
    boolean bool = false;
    if ((paramInt >= 128761) && (paramInt <= 129535))
    {
      if ((paramInt != 9823) && (paramInt != 9854) && (paramInt != 128761) && (paramInt != 129402) && ((129357 > paramInt) || (paramInt > 129359)) && ((129388 > paramInt) || (paramInt > 129392)) && ((129395 > paramInt) || (paramInt > 129398)) && ((129404 > paramInt) || (paramInt > 129407)) && ((129432 > paramInt) || (paramInt > 129442)) && ((129456 > paramInt) || (paramInt > 129465)) && ((129473 > paramInt) || (paramInt > 129474)) && ((129511 > paramInt) || (paramInt > 129535))) {
        break label147;
      }
      bool = true;
      label147:
      return bool;
    }
    return false;
  }
  
  public static boolean isRegionalIndicatorSymbol(int paramInt)
  {
    boolean bool;
    if ((127462 <= paramInt) && (paramInt <= 127487)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isTagSpecChar(int paramInt)
  {
    boolean bool;
    if ((917536 <= paramInt) && (paramInt <= 917630)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
