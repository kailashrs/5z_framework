package android.hardware.usb.gadget.V1_0;

import java.util.ArrayList;

public final class GadgetFunction
{
  public static final long ACCESSORY = 2L;
  public static final long ADB = 1L;
  public static final long AUDIO_SOURCE = 64L;
  public static final long MIDI = 8L;
  public static final long MTP = 4L;
  public static final long NONE = 0L;
  public static final long PTP = 16L;
  public static final long RNDIS = 32L;
  
  public GadgetFunction() {}
  
  public static final String dumpBitfield(long paramLong)
  {
    ArrayList localArrayList = new ArrayList();
    long l1 = 0L;
    localArrayList.add("NONE");
    if ((paramLong & 1L) == 1L)
    {
      localArrayList.add("ADB");
      l1 = 0L | 1L;
    }
    long l2 = l1;
    if ((paramLong & 0x2) == 2L)
    {
      localArrayList.add("ACCESSORY");
      l2 = l1 | 0x2;
    }
    l1 = l2;
    if ((paramLong & 0x4) == 4L)
    {
      localArrayList.add("MTP");
      l1 = l2 | 0x4;
    }
    long l3 = l1;
    if ((paramLong & 0x8) == 8L)
    {
      localArrayList.add("MIDI");
      l3 = l1 | 0x8;
    }
    l2 = l3;
    if ((paramLong & 0x10) == 16L)
    {
      localArrayList.add("PTP");
      l2 = l3 | 0x10;
    }
    l1 = l2;
    if ((paramLong & 0x20) == 32L)
    {
      localArrayList.add("RNDIS");
      l1 = l2 | 0x20;
    }
    l2 = l1;
    if ((paramLong & 0x40) == 64L)
    {
      localArrayList.add("AUDIO_SOURCE");
      l2 = l1 | 0x40;
    }
    if (paramLong != l2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Long.toHexString(l2 & paramLong));
      localArrayList.add(localStringBuilder.toString());
    }
    return String.join(" | ", localArrayList);
  }
  
  public static final String toString(long paramLong)
  {
    if (paramLong == 0L) {
      return "NONE";
    }
    if (paramLong == 1L) {
      return "ADB";
    }
    if (paramLong == 2L) {
      return "ACCESSORY";
    }
    if (paramLong == 4L) {
      return "MTP";
    }
    if (paramLong == 8L) {
      return "MIDI";
    }
    if (paramLong == 16L) {
      return "PTP";
    }
    if (paramLong == 32L) {
      return "RNDIS";
    }
    if (paramLong == 64L) {
      return "AUDIO_SOURCE";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Long.toHexString(paramLong));
    return localStringBuilder.toString();
  }
}
