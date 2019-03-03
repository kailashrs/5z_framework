package com.android.internal.telephony.uicc.euicc.apdu;

import java.util.ArrayList;
import java.util.List;

public class RequestBuilder
{
  private static final int CLA_STORE_DATA = 128;
  private static final int INS_STORE_DATA = 226;
  private static final int MAX_APDU_DATA_LEN = 255;
  private static final int MAX_EXT_APDU_DATA_LEN = 65535;
  private static final int P1_STORE_DATA_END = 145;
  private static final int P1_STORE_DATA_INTERM = 17;
  private final int mChannel;
  private final List<ApduCommand> mCommands = new ArrayList();
  private final int mMaxApduDataLen;
  
  RequestBuilder(int paramInt, boolean paramBoolean)
  {
    mChannel = paramInt;
    if (paramBoolean) {
      paramInt = 65535;
    } else {
      paramInt = 255;
    }
    mMaxApduDataLen = paramInt;
  }
  
  public void addApdu(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mCommands.add(new ApduCommand(mChannel, paramInt1, paramInt2, paramInt3, paramInt4, 0, ""));
  }
  
  public void addApdu(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString)
  {
    mCommands.add(new ApduCommand(mChannel, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramString));
  }
  
  public void addApdu(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString)
  {
    mCommands.add(new ApduCommand(mChannel, paramInt1, paramInt2, paramInt3, paramInt4, paramString.length() / 2, paramString));
  }
  
  public void addStoreData(String paramString)
  {
    int i = mMaxApduDataLen * 2;
    int j = 0;
    int k = paramString.length() / 2;
    int m = 1;
    if (k == 0) {
      k = 1;
    } else {
      k = (mMaxApduDataLen + k - 1) / mMaxApduDataLen;
    }
    while (m < k)
    {
      addApdu(128, 226, 17, m - 1, paramString.substring(j, j + i));
      j += i;
      m++;
    }
    addApdu(128, 226, 145, k - 1, paramString.substring(j));
  }
  
  List<ApduCommand> getCommands()
  {
    return mCommands;
  }
}
