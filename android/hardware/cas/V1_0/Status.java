package android.hardware.cas.V1_0;

import java.util.ArrayList;

public final class Status
{
  public static final int BAD_VALUE = 6;
  public static final int ERROR_CAS_CANNOT_HANDLE = 4;
  public static final int ERROR_CAS_DECRYPT = 13;
  public static final int ERROR_CAS_DECRYPT_UNIT_NOT_INITIALIZED = 12;
  public static final int ERROR_CAS_DEVICE_REVOKED = 11;
  public static final int ERROR_CAS_INSUFFICIENT_OUTPUT_PROTECTION = 9;
  public static final int ERROR_CAS_INVALID_STATE = 5;
  public static final int ERROR_CAS_LICENSE_EXPIRED = 2;
  public static final int ERROR_CAS_NOT_PROVISIONED = 7;
  public static final int ERROR_CAS_NO_LICENSE = 1;
  public static final int ERROR_CAS_RESOURCE_BUSY = 8;
  public static final int ERROR_CAS_SESSION_NOT_OPENED = 3;
  public static final int ERROR_CAS_TAMPER_DETECTED = 10;
  public static final int ERROR_CAS_UNKNOWN = 14;
  public static final int OK = 0;
  
  public Status() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("OK");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ERROR_CAS_NO_LICENSE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("ERROR_CAS_LICENSE_EXPIRED");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("ERROR_CAS_SESSION_NOT_OPENED");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("ERROR_CAS_CANNOT_HANDLE");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("ERROR_CAS_INVALID_STATE");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("BAD_VALUE");
      j = i | 0x6;
    }
    i = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("ERROR_CAS_NOT_PROVISIONED");
      i = j | 0x7;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("ERROR_CAS_RESOURCE_BUSY");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("ERROR_CAS_INSUFFICIENT_OUTPUT_PROTECTION");
      i = j | 0x9;
    }
    j = i;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("ERROR_CAS_TAMPER_DETECTED");
      j = i | 0xA;
    }
    i = j;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("ERROR_CAS_DEVICE_REVOKED");
      i = j | 0xB;
    }
    j = i;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("ERROR_CAS_DECRYPT_UNIT_NOT_INITIALIZED");
      j = i | 0xC;
    }
    i = j;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("ERROR_CAS_DECRYPT");
      i = j | 0xD;
    }
    j = i;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("ERROR_CAS_UNKNOWN");
      j = i | 0xE;
    }
    if (paramInt != j)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(j & paramInt));
      localArrayList.add(localStringBuilder.toString());
    }
    return String.join(" | ", localArrayList);
  }
  
  public static final String toString(int paramInt)
  {
    if (paramInt == 0) {
      return "OK";
    }
    if (paramInt == 1) {
      return "ERROR_CAS_NO_LICENSE";
    }
    if (paramInt == 2) {
      return "ERROR_CAS_LICENSE_EXPIRED";
    }
    if (paramInt == 3) {
      return "ERROR_CAS_SESSION_NOT_OPENED";
    }
    if (paramInt == 4) {
      return "ERROR_CAS_CANNOT_HANDLE";
    }
    if (paramInt == 5) {
      return "ERROR_CAS_INVALID_STATE";
    }
    if (paramInt == 6) {
      return "BAD_VALUE";
    }
    if (paramInt == 7) {
      return "ERROR_CAS_NOT_PROVISIONED";
    }
    if (paramInt == 8) {
      return "ERROR_CAS_RESOURCE_BUSY";
    }
    if (paramInt == 9) {
      return "ERROR_CAS_INSUFFICIENT_OUTPUT_PROTECTION";
    }
    if (paramInt == 10) {
      return "ERROR_CAS_TAMPER_DETECTED";
    }
    if (paramInt == 11) {
      return "ERROR_CAS_DEVICE_REVOKED";
    }
    if (paramInt == 12) {
      return "ERROR_CAS_DECRYPT_UNIT_NOT_INITIALIZED";
    }
    if (paramInt == 13) {
      return "ERROR_CAS_DECRYPT";
    }
    if (paramInt == 14) {
      return "ERROR_CAS_UNKNOWN";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
