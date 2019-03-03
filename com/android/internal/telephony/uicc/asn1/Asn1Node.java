package com.android.internal.telephony.uicc.asn1;

import com.android.internal.telephony.uicc.IccUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Asn1Node
{
  private static final List<Asn1Node> EMPTY_NODE_LIST = ;
  private static final byte[] FALSE_BYTES = { 0 };
  private static final int INT_BYTES = 4;
  private static final byte[] TRUE_BYTES = { -1 };
  private final List<Asn1Node> mChildren;
  private final boolean mConstructed;
  private byte[] mDataBytes;
  private int mDataLength;
  private int mDataOffset;
  private int mEncodedLength;
  private final int mTag;
  
  private Asn1Node(int paramInt, List<Asn1Node> paramList)
  {
    mTag = paramInt;
    mConstructed = true;
    mChildren = paramList;
    paramInt = 0;
    mDataLength = 0;
    int i = paramList.size();
    while (paramInt < i)
    {
      mDataLength += getmEncodedLength;
      paramInt++;
    }
    mEncodedLength = (IccUtils.byteNumForUnsignedInt(mTag) + calculateEncodedBytesNumForLength(mDataLength) + mDataLength);
  }
  
  Asn1Node(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    mTag = paramInt1;
    mConstructed = isConstructedTag(paramInt1);
    mDataBytes = paramArrayOfByte;
    mDataOffset = paramInt2;
    mDataLength = paramInt3;
    if (mConstructed) {
      paramArrayOfByte = new ArrayList();
    } else {
      paramArrayOfByte = EMPTY_NODE_LIST;
    }
    mChildren = paramArrayOfByte;
    mEncodedLength = (IccUtils.byteNumForUnsignedInt(mTag) + calculateEncodedBytesNumForLength(mDataLength) + mDataLength);
  }
  
  private static int calculateEncodedBytesNumForLength(int paramInt)
  {
    int i = 1;
    if (paramInt > 127) {
      i = 1 + IccUtils.byteNumForUnsignedInt(paramInt);
    }
    return i;
  }
  
  private static boolean isConstructedTag(int paramInt)
  {
    byte[] arrayOfByte = IccUtils.unsignedIntToBytes(paramInt);
    boolean bool = false;
    if ((arrayOfByte[0] & 0x20) != 0) {
      bool = true;
    }
    return bool;
  }
  
  public static Builder newBuilder(int paramInt)
  {
    return new Builder(paramInt, null);
  }
  
  private int write(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + IccUtils.unsignedIntToBytes(mTag, paramArrayOfByte, paramInt);
    if (mDataLength <= 127)
    {
      paramInt = i + 1;
      paramArrayOfByte[i] = ((byte)(byte)mDataLength);
    }
    else
    {
      paramInt = mDataLength;
      i++;
      paramInt = IccUtils.unsignedIntToBytes(paramInt, paramArrayOfByte, i);
      paramArrayOfByte[(i - 1)] = ((byte)(byte)(paramInt | 0x80));
      paramInt += i;
    }
    if ((mConstructed) && (mDataBytes == null))
    {
      int j = mChildren.size();
      for (i = 0; i < j; i++) {
        paramInt = ((Asn1Node)mChildren.get(i)).write(paramArrayOfByte, paramInt);
      }
      i = paramInt;
    }
    else
    {
      i = paramInt;
      if (mDataBytes != null)
      {
        System.arraycopy(mDataBytes, mDataOffset, paramArrayOfByte, paramInt, mDataLength);
        i = paramInt + mDataLength;
      }
    }
    return i;
  }
  
  public int asBits()
    throws InvalidAsn1DataException
  {
    if (!mConstructed)
    {
      if (mDataBytes != null) {
        try
        {
          int i = IccUtils.bytesToInt(mDataBytes, mDataOffset + 1, mDataLength - 1);
          for (int j = mDataLength - 1; j < 4; j++) {
            i <<= 8;
          }
          return Integer.reverse(i);
        }
        catch (IllegalArgumentException|IndexOutOfBoundsException localIllegalArgumentException)
        {
          throw new InvalidAsn1DataException(mTag, "Cannot parse data bytes.", localIllegalArgumentException);
        }
      }
      throw new InvalidAsn1DataException(mTag, "Data bytes cannot be null.");
    }
    throw new IllegalStateException("Cannot get value of a constructed node.");
  }
  
  public boolean asBoolean()
    throws InvalidAsn1DataException
  {
    if (!mConstructed)
    {
      if (mDataBytes != null)
      {
        if (mDataLength == 1)
        {
          if ((mDataOffset >= 0) && (mDataOffset < mDataBytes.length))
          {
            if (mDataBytes[mDataOffset] == -1) {
              return Boolean.TRUE.booleanValue();
            }
            if (mDataBytes[mDataOffset] == 0) {
              return Boolean.FALSE.booleanValue();
            }
            i = mTag;
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Cannot parse data bytes as boolean: ");
            localStringBuilder.append(mDataBytes[mDataOffset]);
            throw new InvalidAsn1DataException(i, localStringBuilder.toString());
          }
          throw new InvalidAsn1DataException(mTag, "Cannot parse data bytes.", new ArrayIndexOutOfBoundsException(mDataOffset));
        }
        int i = mTag;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Cannot parse data bytes as boolean: length=");
        localStringBuilder.append(mDataLength);
        throw new InvalidAsn1DataException(i, localStringBuilder.toString());
      }
      throw new InvalidAsn1DataException(mTag, "Data bytes cannot be null.");
    }
    throw new IllegalStateException("Cannot get value of a constructed node.");
  }
  
  public byte[] asBytes()
    throws InvalidAsn1DataException
  {
    if (!mConstructed)
    {
      if (mDataBytes != null)
      {
        byte[] arrayOfByte = new byte[mDataLength];
        try
        {
          System.arraycopy(mDataBytes, mDataOffset, arrayOfByte, 0, mDataLength);
          return arrayOfByte;
        }
        catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
        {
          throw new InvalidAsn1DataException(mTag, "Cannot parse data bytes.", localIndexOutOfBoundsException);
        }
      }
      throw new InvalidAsn1DataException(mTag, "Data bytes cannot be null.");
    }
    throw new IllegalStateException("Cannot get value of a constructed node.");
  }
  
  public int asInteger()
    throws InvalidAsn1DataException
  {
    if (!mConstructed)
    {
      if (mDataBytes != null) {
        try
        {
          int i = IccUtils.bytesToInt(mDataBytes, mDataOffset, mDataLength);
          return i;
        }
        catch (IllegalArgumentException|IndexOutOfBoundsException localIllegalArgumentException)
        {
          throw new InvalidAsn1DataException(mTag, "Cannot parse data bytes.", localIllegalArgumentException);
        }
      }
      throw new InvalidAsn1DataException(mTag, "Data bytes cannot be null.");
    }
    throw new IllegalStateException("Cannot get value of a constructed node.");
  }
  
  public long asRawLong()
    throws InvalidAsn1DataException
  {
    if (!mConstructed)
    {
      if (mDataBytes != null) {
        try
        {
          long l = IccUtils.bytesToRawLong(mDataBytes, mDataOffset, mDataLength);
          return l;
        }
        catch (IllegalArgumentException|IndexOutOfBoundsException localIllegalArgumentException)
        {
          throw new InvalidAsn1DataException(mTag, "Cannot parse data bytes.", localIllegalArgumentException);
        }
      }
      throw new InvalidAsn1DataException(mTag, "Data bytes cannot be null.");
    }
    throw new IllegalStateException("Cannot get value of a constructed node.");
  }
  
  public String asString()
    throws InvalidAsn1DataException
  {
    if (!mConstructed)
    {
      if (mDataBytes != null) {
        try
        {
          String str = new String(mDataBytes, mDataOffset, mDataLength, StandardCharsets.UTF_8);
          return str;
        }
        catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
        {
          throw new InvalidAsn1DataException(mTag, "Cannot parse data bytes.", localIndexOutOfBoundsException);
        }
      }
      throw new InvalidAsn1DataException(mTag, "Data bytes cannot be null.");
    }
    throw new IllegalStateException("Cannot get value of a constructed node.");
  }
  
  public Asn1Node getChild(int paramInt, int... paramVarArgs)
    throws TagNotFoundException, InvalidAsn1DataException
  {
    if (mConstructed)
    {
      int i = 0;
      int j = paramInt;
      Object localObject1 = this;
      Object localObject2;
      for (paramInt = i;; paramInt++)
      {
        localObject2 = localObject1;
        if (localObject1 == null) {
          break;
        }
        List localList = ((Asn1Node)localObject1).getChildren();
        int k = localList.size();
        localObject2 = null;
        for (i = 0;; i++)
        {
          localObject1 = localObject2;
          if (i >= k) {
            break;
          }
          localObject1 = (Asn1Node)localList.get(i);
          if (((Asn1Node)localObject1).getTag() == j) {
            break;
          }
        }
        if (paramInt >= paramVarArgs.length)
        {
          localObject2 = localObject1;
          break;
        }
        j = paramVarArgs[paramInt];
      }
      if (localObject2 != null) {
        return localObject2;
      }
      throw new TagNotFoundException(j);
    }
    throw new TagNotFoundException(paramInt);
  }
  
  public List<Asn1Node> getChildren()
    throws InvalidAsn1DataException
  {
    if (!mConstructed) {
      return EMPTY_NODE_LIST;
    }
    if (mDataBytes != null)
    {
      Asn1Decoder localAsn1Decoder = new Asn1Decoder(mDataBytes, mDataOffset, mDataLength);
      while (localAsn1Decoder.hasNextNode()) {
        mChildren.add(localAsn1Decoder.nextNode());
      }
      mDataBytes = null;
      mDataOffset = 0;
    }
    return mChildren;
  }
  
  public List<Asn1Node> getChildren(int paramInt)
    throws TagNotFoundException, InvalidAsn1DataException
  {
    if (!mConstructed) {
      return EMPTY_NODE_LIST;
    }
    List localList = getChildren();
    if (localList.isEmpty()) {
      return EMPTY_NODE_LIST;
    }
    Object localObject = new ArrayList();
    int i = localList.size();
    for (int j = 0; j < i; j++)
    {
      Asn1Node localAsn1Node = (Asn1Node)localList.get(j);
      if (localAsn1Node.getTag() == paramInt) {
        ((List)localObject).add(localAsn1Node);
      }
    }
    if (((List)localObject).isEmpty()) {
      localObject = EMPTY_NODE_LIST;
    }
    return localObject;
  }
  
  public int getDataLength()
  {
    return mDataLength;
  }
  
  public int getEncodedLength()
  {
    return mEncodedLength;
  }
  
  public String getHeadAsHex()
  {
    Object localObject1 = IccUtils.bytesToHexString(IccUtils.unsignedIntToBytes(mTag));
    Object localObject2;
    if (mDataLength <= 127)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(IccUtils.byteToHex((byte)mDataLength));
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    else
    {
      localObject2 = IccUtils.unsignedIntToBytes(mDataLength);
      Object localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append((String)localObject1);
      ((StringBuilder)localObject3).append(IccUtils.byteToHex((byte)(localObject2.length | 0x80)));
      localObject3 = ((StringBuilder)localObject3).toString();
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject3);
      ((StringBuilder)localObject1).append(IccUtils.bytesToHexString((byte[])localObject2));
      localObject2 = ((StringBuilder)localObject1).toString();
    }
    return localObject2;
  }
  
  public int getTag()
  {
    return mTag;
  }
  
  public boolean hasChild(int paramInt, int... paramVarArgs)
    throws InvalidAsn1DataException
  {
    try
    {
      getChild(paramInt, paramVarArgs);
      return true;
    }
    catch (TagNotFoundException paramVarArgs) {}
    return false;
  }
  
  public boolean hasValue()
  {
    boolean bool;
    if ((!mConstructed) && (mDataBytes != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isConstructed()
  {
    return mConstructed;
  }
  
  public byte[] toBytes()
  {
    byte[] arrayOfByte = new byte[mEncodedLength];
    write(arrayOfByte, 0);
    return arrayOfByte;
  }
  
  public String toHex()
  {
    return IccUtils.bytesToHexString(toBytes());
  }
  
  public void writeToBytes(byte[] paramArrayOfByte, int paramInt)
  {
    if ((paramInt >= 0) && (mEncodedLength + paramInt <= paramArrayOfByte.length))
    {
      write(paramArrayOfByte, paramInt);
      return;
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("Not enough space to write. Required bytes: ");
    paramArrayOfByte.append(mEncodedLength);
    throw new IndexOutOfBoundsException(paramArrayOfByte.toString());
  }
  
  public static final class Builder
  {
    private final List<Asn1Node> mChildren;
    private final int mTag;
    
    private Builder(int paramInt)
    {
      if (Asn1Node.isConstructedTag(paramInt))
      {
        mTag = paramInt;
        mChildren = new ArrayList();
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Builder should be created for a constructed tag: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder addChild(Builder paramBuilder)
    {
      mChildren.add(paramBuilder.build());
      return this;
    }
    
    public Builder addChild(Asn1Node paramAsn1Node)
    {
      mChildren.add(paramAsn1Node);
      return this;
    }
    
    public Builder addChildAsBits(int paramInt1, int paramInt2)
    {
      if (!Asn1Node.isConstructedTag(paramInt1))
      {
        localObject = new byte[5];
        int i = Integer.reverse(paramInt2);
        int j = 0;
        for (paramInt2 = 1; paramInt2 < localObject.length; paramInt2++)
        {
          localObject[paramInt2] = ((byte)(byte)(i >> (4 - paramInt2) * 8));
          if (localObject[paramInt2] != 0) {
            j = paramInt2;
          }
        }
        paramInt2 = j + 1;
        localObject[0] = IccUtils.countTrailingZeros(localObject[(paramInt2 - 1)]);
        addChild(new Asn1Node(paramInt1, (byte[])localObject, 0, paramInt2));
        return this;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Cannot set value of a constructed tag: ");
      ((StringBuilder)localObject).append(paramInt1);
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    }
    
    public Builder addChildAsBoolean(int paramInt, boolean paramBoolean)
    {
      if (!Asn1Node.isConstructedTag(paramInt))
      {
        if (paramBoolean) {
          localObject = Asn1Node.TRUE_BYTES;
        } else {
          localObject = Asn1Node.FALSE_BYTES;
        }
        addChild(new Asn1Node(paramInt, (byte[])localObject, 0, 1));
        return this;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Cannot set value of a constructed tag: ");
      ((StringBuilder)localObject).append(paramInt);
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    }
    
    public Builder addChildAsBytes(int paramInt, byte[] paramArrayOfByte)
    {
      if (!Asn1Node.isConstructedTag(paramInt))
      {
        addChild(new Asn1Node(paramInt, paramArrayOfByte, 0, paramArrayOfByte.length));
        return this;
      }
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Cannot set value of a constructed tag: ");
      paramArrayOfByte.append(paramInt);
      throw new IllegalStateException(paramArrayOfByte.toString());
    }
    
    public Builder addChildAsBytesFromHex(int paramInt, String paramString)
    {
      return addChildAsBytes(paramInt, IccUtils.hexStringToBytes(paramString));
    }
    
    public Builder addChildAsInteger(int paramInt1, int paramInt2)
    {
      if (!Asn1Node.isConstructedTag(paramInt1))
      {
        localObject = IccUtils.signedIntToBytes(paramInt2);
        addChild(new Asn1Node(paramInt1, (byte[])localObject, 0, localObject.length));
        return this;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Cannot set value of a constructed tag: ");
      ((StringBuilder)localObject).append(paramInt1);
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    }
    
    public Builder addChildAsString(int paramInt, String paramString)
    {
      if (!Asn1Node.isConstructedTag(paramInt))
      {
        paramString = paramString.getBytes(StandardCharsets.UTF_8);
        addChild(new Asn1Node(paramInt, paramString, 0, paramString.length));
        return this;
      }
      paramString = new StringBuilder();
      paramString.append("Cannot set value of a constructed tag: ");
      paramString.append(paramInt);
      throw new IllegalStateException(paramString.toString());
    }
    
    public Builder addChildren(byte[] paramArrayOfByte)
      throws InvalidAsn1DataException
    {
      paramArrayOfByte = new Asn1Decoder(paramArrayOfByte, 0, paramArrayOfByte.length);
      while (paramArrayOfByte.hasNextNode()) {
        mChildren.add(paramArrayOfByte.nextNode());
      }
      return this;
    }
    
    public Asn1Node build()
    {
      return new Asn1Node(mTag, mChildren, null);
    }
  }
}
