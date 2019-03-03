package com.android.internal.telephony.uicc.asn1;

import com.android.internal.telephony.uicc.IccUtils;

public final class Asn1Decoder
{
  private final int mEnd;
  private int mPosition;
  private final byte[] mSrc;
  
  public Asn1Decoder(String paramString)
  {
    this(IccUtils.hexStringToBytes(paramString));
  }
  
  public Asn1Decoder(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public Asn1Decoder(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
    {
      mSrc = paramArrayOfByte;
      mPosition = paramInt1;
      mEnd = (paramInt1 + paramInt2);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Out of the bounds: bytes=[");
    localStringBuilder.append(paramArrayOfByte.length);
    localStringBuilder.append("], offset=");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(", length=");
    localStringBuilder.append(paramInt2);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public int getPosition()
  {
    return mPosition;
  }
  
  public boolean hasNextNode()
  {
    boolean bool;
    if (mPosition < mEnd) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Asn1Node nextNode()
    throws InvalidAsn1DataException
  {
    if (mPosition < mEnd)
    {
      int i = mPosition;
      Object localObject = mSrc;
      int j = i + 1;
      int k = j;
      if ((localObject[i] & 0x1F) == 31) {
        for (;;)
        {
          k = j;
          if (j >= mEnd) {
            break;
          }
          localObject = mSrc;
          k = j + 1;
          if ((localObject[j] & 0x80) == 0) {
            break;
          }
          j = k;
        }
      }
      if (k < mEnd) {
        try
        {
          int m = IccUtils.bytesToInt(mSrc, i, k - i);
          localObject = mSrc;
          j = k + 1;
          k = localObject[k];
          if ((k & 0x80) != 0)
          {
            i = k & 0x7F;
            if (j + i > mEnd) {}
          }
          else
          {
            try
            {
              k = IccUtils.bytesToInt(mSrc, j, i);
              j += i;
              if (j + k <= mEnd)
              {
                localObject = new Asn1Node(m, mSrc, j, k);
                mPosition = (j + k);
                return localObject;
              }
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("Incomplete data at position: ");
              ((StringBuilder)localObject).append(j);
              ((StringBuilder)localObject).append(", expected bytes: ");
              ((StringBuilder)localObject).append(k);
              ((StringBuilder)localObject).append(", actual bytes: ");
              ((StringBuilder)localObject).append(mEnd - j);
              throw new InvalidAsn1DataException(m, ((StringBuilder)localObject).toString());
            }
            catch (IllegalArgumentException localIllegalArgumentException1)
            {
              StringBuilder localStringBuilder2 = new StringBuilder();
              localStringBuilder2.append("Cannot parse length at position: ");
              localStringBuilder2.append(j);
              throw new InvalidAsn1DataException(m, localStringBuilder2.toString(), localIllegalArgumentException1);
            }
          }
          localStringBuilder1 = new StringBuilder();
          localStringBuilder1.append("Cannot parse length at position: ");
          localStringBuilder1.append(j);
          throw new InvalidAsn1DataException(m, localStringBuilder1.toString());
        }
        catch (IllegalArgumentException localIllegalArgumentException2)
        {
          localStringBuilder1 = new StringBuilder();
          localStringBuilder1.append("Cannot parse tag at position: ");
          localStringBuilder1.append(i);
          throw new InvalidAsn1DataException(0, localStringBuilder1.toString(), localIllegalArgumentException2);
        }
      }
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Invalid length at position: ");
      localStringBuilder1.append(k);
      throw new InvalidAsn1DataException(0, localStringBuilder1.toString());
    }
    throw new IllegalStateException("No bytes to parse.");
  }
}
