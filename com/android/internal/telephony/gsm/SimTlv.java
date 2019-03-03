package com.android.internal.telephony.gsm;

public class SimTlv
{
  int mCurDataLength;
  int mCurDataOffset;
  int mCurOffset;
  boolean mHasValidTlvObject;
  byte[] mRecord;
  int mTlvLength;
  int mTlvOffset;
  
  public SimTlv(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    mRecord = paramArrayOfByte;
    mTlvOffset = paramInt1;
    mTlvLength = paramInt2;
    mCurOffset = paramInt1;
    mHasValidTlvObject = parseCurrentTlvObject();
  }
  
  private boolean parseCurrentTlvObject()
  {
    try
    {
      if ((mRecord[mCurOffset] != 0) && ((mRecord[mCurOffset] & 0xFF) != 255))
      {
        if ((mRecord[(mCurOffset + 1)] & 0xFF) < 128)
        {
          mCurDataLength = (mRecord[(mCurOffset + 1)] & 0xFF);
          mCurDataOffset = (mCurOffset + 2);
        }
        else
        {
          if ((mRecord[(mCurOffset + 1)] & 0xFF) != 129) {
            break label162;
          }
          mCurDataLength = (mRecord[(mCurOffset + 2)] & 0xFF);
          mCurDataOffset = (mCurOffset + 3);
        }
        return mCurDataLength + mCurDataOffset <= mTlvOffset + mTlvLength;
        label162:
        return false;
      }
      return false;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
    return false;
  }
  
  public byte[] getData()
  {
    if (!mHasValidTlvObject) {
      return null;
    }
    byte[] arrayOfByte = new byte[mCurDataLength];
    System.arraycopy(mRecord, mCurDataOffset, arrayOfByte, 0, mCurDataLength);
    return arrayOfByte;
  }
  
  public int getTag()
  {
    if (!mHasValidTlvObject) {
      return 0;
    }
    return mRecord[mCurOffset] & 0xFF;
  }
  
  public boolean isValidObject()
  {
    return mHasValidTlvObject;
  }
  
  public boolean nextObject()
  {
    if (!mHasValidTlvObject) {
      return false;
    }
    mCurOffset = (mCurDataOffset + mCurDataLength);
    mHasValidTlvObject = parseCurrentTlvObject();
    return mHasValidTlvObject;
  }
}
