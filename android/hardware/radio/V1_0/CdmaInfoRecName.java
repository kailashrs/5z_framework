package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaInfoRecName
{
  public static final int CALLED_PARTY_NUMBER = 1;
  public static final int CALLING_PARTY_NUMBER = 2;
  public static final int CONNECTED_NUMBER = 3;
  public static final int DISPLAY = 0;
  public static final int EXTENDED_DISPLAY = 7;
  public static final int LINE_CONTROL = 6;
  public static final int REDIRECTING_NUMBER = 5;
  public static final int SIGNAL = 4;
  public static final int T53_AUDIO_CONTROL = 10;
  public static final int T53_CLIR = 8;
  public static final int T53_RELEASE = 9;
  
  public CdmaInfoRecName() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("DISPLAY");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("CALLED_PARTY_NUMBER");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("CALLING_PARTY_NUMBER");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("CONNECTED_NUMBER");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("SIGNAL");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("REDIRECTING_NUMBER");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("LINE_CONTROL");
      j = i | 0x6;
    }
    int k = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("EXTENDED_DISPLAY");
      k = j | 0x7;
    }
    i = k;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("T53_CLIR");
      i = k | 0x8;
    }
    j = i;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("T53_RELEASE");
      j = i | 0x9;
    }
    i = j;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("T53_AUDIO_CONTROL");
      i = j | 0xA;
    }
    if (paramInt != i)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(i & paramInt));
      localArrayList.add(localStringBuilder.toString());
    }
    return String.join(" | ", localArrayList);
  }
  
  public static final String toString(int paramInt)
  {
    if (paramInt == 0) {
      return "DISPLAY";
    }
    if (paramInt == 1) {
      return "CALLED_PARTY_NUMBER";
    }
    if (paramInt == 2) {
      return "CALLING_PARTY_NUMBER";
    }
    if (paramInt == 3) {
      return "CONNECTED_NUMBER";
    }
    if (paramInt == 4) {
      return "SIGNAL";
    }
    if (paramInt == 5) {
      return "REDIRECTING_NUMBER";
    }
    if (paramInt == 6) {
      return "LINE_CONTROL";
    }
    if (paramInt == 7) {
      return "EXTENDED_DISPLAY";
    }
    if (paramInt == 8) {
      return "T53_CLIR";
    }
    if (paramInt == 9) {
      return "T53_RELEASE";
    }
    if (paramInt == 10) {
      return "T53_AUDIO_CONTROL";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
