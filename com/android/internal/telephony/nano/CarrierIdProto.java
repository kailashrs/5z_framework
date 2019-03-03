package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import com.android.internal.telephony.protobuf.nano.WireFormatNano;
import java.io.IOException;

public abstract interface CarrierIdProto
{
  public static final class CarrierAttribute
    extends ExtendableMessageNano<CarrierAttribute>
  {
    private static volatile CarrierAttribute[] _emptyArray;
    public String[] gid1;
    public String[] gid2;
    public String[] iccidPrefix;
    public String[] imsiPrefixXpattern;
    public String[] mccmncTuple;
    public String[] plmn;
    public String[] preferredApn;
    public String[] spn;
    
    public CarrierAttribute()
    {
      clear();
    }
    
    public static CarrierAttribute[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new CarrierAttribute[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static CarrierAttribute parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new CarrierAttribute().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static CarrierAttribute parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (CarrierAttribute)MessageNano.mergeFrom(new CarrierAttribute(), paramArrayOfByte);
    }
    
    public CarrierAttribute clear()
    {
      mccmncTuple = WireFormatNano.EMPTY_STRING_ARRAY;
      imsiPrefixXpattern = WireFormatNano.EMPTY_STRING_ARRAY;
      spn = WireFormatNano.EMPTY_STRING_ARRAY;
      plmn = WireFormatNano.EMPTY_STRING_ARRAY;
      gid1 = WireFormatNano.EMPTY_STRING_ARRAY;
      gid2 = WireFormatNano.EMPTY_STRING_ARRAY;
      preferredApn = WireFormatNano.EMPTY_STRING_ARRAY;
      iccidPrefix = WireFormatNano.EMPTY_STRING_ARRAY;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      Object localObject = mccmncTuple;
      int j = 0;
      int k = i;
      int m;
      int n;
      int i1;
      if (localObject != null)
      {
        k = i;
        if (mccmncTuple.length > 0)
        {
          m = 0;
          n = 0;
          k = 0;
          while (k < mccmncTuple.length)
          {
            localObject = mccmncTuple[k];
            i1 = m;
            i2 = n;
            if (localObject != null)
            {
              i2 = n + 1;
              i1 = m + CodedOutputByteBufferNano.computeStringSizeNoTag((String)localObject);
            }
            k++;
            m = i1;
            n = i2;
          }
          k = i + m + 1 * n;
        }
      }
      int i2 = k;
      if (imsiPrefixXpattern != null)
      {
        i2 = k;
        if (imsiPrefixXpattern.length > 0)
        {
          i1 = 0;
          i2 = 0;
          n = 0;
          while (n < imsiPrefixXpattern.length)
          {
            localObject = imsiPrefixXpattern[n];
            i = i1;
            m = i2;
            if (localObject != null)
            {
              m = i2 + 1;
              i = i1 + CodedOutputByteBufferNano.computeStringSizeNoTag((String)localObject);
            }
            n++;
            i1 = i;
            i2 = m;
          }
          i2 = k + i1 + 1 * i2;
        }
      }
      k = i2;
      if (spn != null)
      {
        k = i2;
        if (spn.length > 0)
        {
          i1 = 0;
          n = 0;
          m = 0;
          while (m < spn.length)
          {
            localObject = spn[m];
            i = i1;
            k = n;
            if (localObject != null)
            {
              k = n + 1;
              i = i1 + CodedOutputByteBufferNano.computeStringSizeNoTag((String)localObject);
            }
            m++;
            i1 = i;
            n = k;
          }
          k = i2 + i1 + 1 * n;
        }
      }
      i2 = k;
      if (plmn != null)
      {
        i2 = k;
        if (plmn.length > 0)
        {
          i1 = 0;
          n = 0;
          m = 0;
          while (m < plmn.length)
          {
            localObject = plmn[m];
            i = i1;
            i2 = n;
            if (localObject != null)
            {
              i2 = n + 1;
              i = i1 + CodedOutputByteBufferNano.computeStringSizeNoTag((String)localObject);
            }
            m++;
            i1 = i;
            n = i2;
          }
          i2 = k + i1 + 1 * n;
        }
      }
      k = i2;
      if (gid1 != null)
      {
        k = i2;
        if (gid1.length > 0)
        {
          i1 = 0;
          n = 0;
          k = 0;
          while (k < gid1.length)
          {
            localObject = gid1[k];
            i = i1;
            m = n;
            if (localObject != null)
            {
              m = n + 1;
              i = i1 + CodedOutputByteBufferNano.computeStringSizeNoTag((String)localObject);
            }
            k++;
            i1 = i;
            n = m;
          }
          k = i2 + i1 + 1 * n;
        }
      }
      i2 = k;
      if (gid2 != null)
      {
        i2 = k;
        if (gid2.length > 0)
        {
          n = 0;
          m = 0;
          i2 = 0;
          while (i2 < gid2.length)
          {
            localObject = gid2[i2];
            i = n;
            i1 = m;
            if (localObject != null)
            {
              i1 = m + 1;
              i = n + CodedOutputByteBufferNano.computeStringSizeNoTag((String)localObject);
            }
            i2++;
            n = i;
            m = i1;
          }
          i2 = k + n + 1 * m;
        }
      }
      k = i2;
      if (preferredApn != null)
      {
        k = i2;
        if (preferredApn.length > 0)
        {
          m = 0;
          k = 0;
          n = 0;
          while (n < preferredApn.length)
          {
            localObject = preferredApn[n];
            i = m;
            i1 = k;
            if (localObject != null)
            {
              i1 = k + 1;
              i = m + CodedOutputByteBufferNano.computeStringSizeNoTag((String)localObject);
            }
            n++;
            m = i;
            k = i1;
          }
          k = i2 + m + 1 * k;
        }
      }
      i2 = k;
      if (iccidPrefix != null)
      {
        i2 = k;
        if (iccidPrefix.length > 0)
        {
          i1 = 0;
          i2 = 0;
          m = j;
          while (m < iccidPrefix.length)
          {
            localObject = iccidPrefix[m];
            i = i1;
            n = i2;
            if (localObject != null)
            {
              i = i1 + 1;
              n = i2 + CodedOutputByteBufferNano.computeStringSizeNoTag((String)localObject);
            }
            m++;
            i1 = i;
            i2 = n;
          }
          i2 = k + i2 + 1 * i1;
        }
      }
      return i2;
    }
    
    public CarrierAttribute mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        if (i == 0) {
          break;
        }
        int j;
        String[] arrayOfString;
        if (i != 10)
        {
          if (i != 18)
          {
            if (i != 26)
            {
              if (i != 34)
              {
                if (i != 42)
                {
                  if (i != 50)
                  {
                    if (i != 58)
                    {
                      if (i != 66)
                      {
                        if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                          return this;
                        }
                      }
                      else
                      {
                        j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 66);
                        if (iccidPrefix == null) {
                          i = 0;
                        } else {
                          i = iccidPrefix.length;
                        }
                        arrayOfString = new String[i + j];
                        j = i;
                        if (i != 0) {
                          System.arraycopy(iccidPrefix, 0, arrayOfString, 0, i);
                        }
                        for (j = i; j < arrayOfString.length - 1; j++)
                        {
                          arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                          paramCodedInputByteBufferNano.readTag();
                        }
                        arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                        iccidPrefix = arrayOfString;
                      }
                    }
                    else
                    {
                      j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 58);
                      if (preferredApn == null) {
                        i = 0;
                      } else {
                        i = preferredApn.length;
                      }
                      arrayOfString = new String[i + j];
                      j = i;
                      if (i != 0) {
                        System.arraycopy(preferredApn, 0, arrayOfString, 0, i);
                      }
                      for (j = i; j < arrayOfString.length - 1; j++)
                      {
                        arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                        paramCodedInputByteBufferNano.readTag();
                      }
                      arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                      preferredApn = arrayOfString;
                    }
                  }
                  else
                  {
                    j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 50);
                    if (gid2 == null) {
                      i = 0;
                    } else {
                      i = gid2.length;
                    }
                    arrayOfString = new String[i + j];
                    j = i;
                    if (i != 0) {
                      System.arraycopy(gid2, 0, arrayOfString, 0, i);
                    }
                    for (j = i; j < arrayOfString.length - 1; j++)
                    {
                      arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                      paramCodedInputByteBufferNano.readTag();
                    }
                    arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                    gid2 = arrayOfString;
                  }
                }
                else
                {
                  j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 42);
                  if (gid1 == null) {
                    i = 0;
                  } else {
                    i = gid1.length;
                  }
                  arrayOfString = new String[i + j];
                  j = i;
                  if (i != 0) {
                    System.arraycopy(gid1, 0, arrayOfString, 0, i);
                  }
                  for (j = i; j < arrayOfString.length - 1; j++)
                  {
                    arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                    paramCodedInputByteBufferNano.readTag();
                  }
                  arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                  gid1 = arrayOfString;
                }
              }
              else
              {
                j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 34);
                if (plmn == null) {
                  i = 0;
                } else {
                  i = plmn.length;
                }
                arrayOfString = new String[i + j];
                j = i;
                if (i != 0) {
                  System.arraycopy(plmn, 0, arrayOfString, 0, i);
                }
                for (j = i; j < arrayOfString.length - 1; j++)
                {
                  arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                  paramCodedInputByteBufferNano.readTag();
                }
                arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                plmn = arrayOfString;
              }
            }
            else
            {
              j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 26);
              if (spn == null) {
                i = 0;
              } else {
                i = spn.length;
              }
              arrayOfString = new String[i + j];
              j = i;
              if (i != 0) {
                System.arraycopy(spn, 0, arrayOfString, 0, i);
              }
              for (j = i; j < arrayOfString.length - 1; j++)
              {
                arrayOfString[j] = paramCodedInputByteBufferNano.readString();
                paramCodedInputByteBufferNano.readTag();
              }
              arrayOfString[j] = paramCodedInputByteBufferNano.readString();
              spn = arrayOfString;
            }
          }
          else
          {
            j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 18);
            if (imsiPrefixXpattern == null) {
              i = 0;
            } else {
              i = imsiPrefixXpattern.length;
            }
            arrayOfString = new String[i + j];
            j = i;
            if (i != 0) {
              System.arraycopy(imsiPrefixXpattern, 0, arrayOfString, 0, i);
            }
            for (j = i; j < arrayOfString.length - 1; j++)
            {
              arrayOfString[j] = paramCodedInputByteBufferNano.readString();
              paramCodedInputByteBufferNano.readTag();
            }
            arrayOfString[j] = paramCodedInputByteBufferNano.readString();
            imsiPrefixXpattern = arrayOfString;
          }
        }
        else
        {
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 10);
          if (mccmncTuple == null) {
            i = 0;
          } else {
            i = mccmncTuple.length;
          }
          arrayOfString = new String[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(mccmncTuple, 0, arrayOfString, 0, i);
          }
          for (j = i; j < arrayOfString.length - 1; j++)
          {
            arrayOfString[j] = paramCodedInputByteBufferNano.readString();
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfString[j] = paramCodedInputByteBufferNano.readString();
          mccmncTuple = arrayOfString;
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      Object localObject = mccmncTuple;
      int i = 0;
      int j;
      if ((localObject != null) && (mccmncTuple.length > 0)) {
        for (j = 0; j < mccmncTuple.length; j++)
        {
          localObject = mccmncTuple[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeString(1, (String)localObject);
          }
        }
      }
      if ((imsiPrefixXpattern != null) && (imsiPrefixXpattern.length > 0)) {
        for (j = 0; j < imsiPrefixXpattern.length; j++)
        {
          localObject = imsiPrefixXpattern[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeString(2, (String)localObject);
          }
        }
      }
      if ((spn != null) && (spn.length > 0)) {
        for (j = 0; j < spn.length; j++)
        {
          localObject = spn[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeString(3, (String)localObject);
          }
        }
      }
      if ((plmn != null) && (plmn.length > 0)) {
        for (j = 0; j < plmn.length; j++)
        {
          localObject = plmn[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeString(4, (String)localObject);
          }
        }
      }
      if ((gid1 != null) && (gid1.length > 0)) {
        for (j = 0; j < gid1.length; j++)
        {
          localObject = gid1[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeString(5, (String)localObject);
          }
        }
      }
      if ((gid2 != null) && (gid2.length > 0)) {
        for (j = 0; j < gid2.length; j++)
        {
          localObject = gid2[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeString(6, (String)localObject);
          }
        }
      }
      if ((preferredApn != null) && (preferredApn.length > 0)) {
        for (j = 0; j < preferredApn.length; j++)
        {
          localObject = preferredApn[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeString(7, (String)localObject);
          }
        }
      }
      if ((iccidPrefix != null) && (iccidPrefix.length > 0)) {
        for (j = i; j < iccidPrefix.length; j++)
        {
          localObject = iccidPrefix[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeString(8, (String)localObject);
          }
        }
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class CarrierId
    extends ExtendableMessageNano<CarrierId>
  {
    private static volatile CarrierId[] _emptyArray;
    public int canonicalId;
    public CarrierIdProto.CarrierAttribute[] carrierAttribute;
    public String carrierName;
    
    public CarrierId()
    {
      clear();
    }
    
    public static CarrierId[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new CarrierId[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static CarrierId parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new CarrierId().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static CarrierId parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (CarrierId)MessageNano.mergeFrom(new CarrierId(), paramArrayOfByte);
    }
    
    public CarrierId clear()
    {
      canonicalId = 0;
      carrierName = "";
      carrierAttribute = CarrierIdProto.CarrierAttribute.emptyArray();
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (canonicalId != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, canonicalId);
      }
      i = j;
      if (!carrierName.equals("")) {
        i = j + CodedOutputByteBufferNano.computeStringSize(2, carrierName);
      }
      int k = i;
      if (carrierAttribute != null)
      {
        k = i;
        if (carrierAttribute.length > 0)
        {
          j = 0;
          for (;;)
          {
            k = i;
            if (j >= carrierAttribute.length) {
              break;
            }
            CarrierIdProto.CarrierAttribute localCarrierAttribute = carrierAttribute[j];
            k = i;
            if (localCarrierAttribute != null) {
              k = i + CodedOutputByteBufferNano.computeMessageSize(3, localCarrierAttribute);
            }
            j++;
            i = k;
          }
        }
      }
      return k;
    }
    
    public CarrierId mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        if (i == 0) {
          break;
        }
        if (i != 8)
        {
          if (i != 18)
          {
            if (i != 26)
            {
              if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else
            {
              int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 26);
              if (carrierAttribute == null) {
                i = 0;
              } else {
                i = carrierAttribute.length;
              }
              CarrierIdProto.CarrierAttribute[] arrayOfCarrierAttribute = new CarrierIdProto.CarrierAttribute[i + j];
              j = i;
              if (i != 0) {
                System.arraycopy(carrierAttribute, 0, arrayOfCarrierAttribute, 0, i);
              }
              for (j = i; j < arrayOfCarrierAttribute.length - 1; j++)
              {
                arrayOfCarrierAttribute[j] = new CarrierIdProto.CarrierAttribute();
                paramCodedInputByteBufferNano.readMessage(arrayOfCarrierAttribute[j]);
                paramCodedInputByteBufferNano.readTag();
              }
              arrayOfCarrierAttribute[j] = new CarrierIdProto.CarrierAttribute();
              paramCodedInputByteBufferNano.readMessage(arrayOfCarrierAttribute[j]);
              carrierAttribute = arrayOfCarrierAttribute;
            }
          }
          else {
            carrierName = paramCodedInputByteBufferNano.readString();
          }
        }
        else {
          canonicalId = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (canonicalId != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, canonicalId);
      }
      if (!carrierName.equals("")) {
        paramCodedOutputByteBufferNano.writeString(2, carrierName);
      }
      if ((carrierAttribute != null) && (carrierAttribute.length > 0)) {
        for (int i = 0; i < carrierAttribute.length; i++)
        {
          CarrierIdProto.CarrierAttribute localCarrierAttribute = carrierAttribute[i];
          if (localCarrierAttribute != null) {
            paramCodedOutputByteBufferNano.writeMessage(3, localCarrierAttribute);
          }
        }
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class CarrierList
    extends ExtendableMessageNano<CarrierList>
  {
    private static volatile CarrierList[] _emptyArray;
    public CarrierIdProto.CarrierId[] carrierId;
    public int version;
    
    public CarrierList()
    {
      clear();
    }
    
    public static CarrierList[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new CarrierList[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static CarrierList parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new CarrierList().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static CarrierList parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (CarrierList)MessageNano.mergeFrom(new CarrierList(), paramArrayOfByte);
    }
    
    public CarrierList clear()
    {
      carrierId = CarrierIdProto.CarrierId.emptyArray();
      version = 0;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (carrierId != null)
      {
        j = i;
        if (carrierId.length > 0)
        {
          int k = 0;
          for (;;)
          {
            j = i;
            if (k >= carrierId.length) {
              break;
            }
            CarrierIdProto.CarrierId localCarrierId = carrierId[k];
            j = i;
            if (localCarrierId != null) {
              j = i + CodedOutputByteBufferNano.computeMessageSize(1, localCarrierId);
            }
            k++;
            i = j;
          }
        }
      }
      i = j;
      if (version != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, version);
      }
      return i;
    }
    
    public CarrierList mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        if (i == 0) {
          break;
        }
        if (i != 10)
        {
          if (i != 16)
          {
            if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
              return this;
            }
          }
          else {
            version = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else
        {
          int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 10);
          if (carrierId == null) {
            i = 0;
          } else {
            i = carrierId.length;
          }
          CarrierIdProto.CarrierId[] arrayOfCarrierId = new CarrierIdProto.CarrierId[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(carrierId, 0, arrayOfCarrierId, 0, i);
          }
          for (j = i; j < arrayOfCarrierId.length - 1; j++)
          {
            arrayOfCarrierId[j] = new CarrierIdProto.CarrierId();
            paramCodedInputByteBufferNano.readMessage(arrayOfCarrierId[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfCarrierId[j] = new CarrierIdProto.CarrierId();
          paramCodedInputByteBufferNano.readMessage(arrayOfCarrierId[j]);
          carrierId = arrayOfCarrierId;
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if ((carrierId != null) && (carrierId.length > 0)) {
        for (int i = 0; i < carrierId.length; i++)
        {
          CarrierIdProto.CarrierId localCarrierId = carrierId[i];
          if (localCarrierId != null) {
            paramCodedOutputByteBufferNano.writeMessage(1, localCarrierId);
          }
        }
      }
      if (version != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, version);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
}
