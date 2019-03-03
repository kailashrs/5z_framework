package com.android.internal.telephony.uicc.euicc.apdu;

public abstract interface RequestProvider
{
  public abstract void buildRequest(byte[] paramArrayOfByte, RequestBuilder paramRequestBuilder)
    throws Throwable;
}
