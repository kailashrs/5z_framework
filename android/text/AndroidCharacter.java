package android.text;

@Deprecated
public class AndroidCharacter
{
  public static final int EAST_ASIAN_WIDTH_AMBIGUOUS = 1;
  public static final int EAST_ASIAN_WIDTH_FULL_WIDTH = 3;
  public static final int EAST_ASIAN_WIDTH_HALF_WIDTH = 2;
  public static final int EAST_ASIAN_WIDTH_NARROW = 4;
  public static final int EAST_ASIAN_WIDTH_NEUTRAL = 0;
  public static final int EAST_ASIAN_WIDTH_WIDE = 5;
  
  public AndroidCharacter() {}
  
  public static native void getDirectionalities(char[] paramArrayOfChar, byte[] paramArrayOfByte, int paramInt);
  
  public static native int getEastAsianWidth(char paramChar);
  
  public static native void getEastAsianWidths(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte);
  
  public static native char getMirror(char paramChar);
  
  public static native boolean mirror(char[] paramArrayOfChar, int paramInt1, int paramInt2);
}
