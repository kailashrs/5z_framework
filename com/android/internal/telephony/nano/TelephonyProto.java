package com.android.internal.telephony.nano;

import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import com.android.internal.telephony.protobuf.nano.WireFormatNano;
import java.io.IOException;

public abstract interface TelephonyProto
{
  public static final class ImsCapabilities
    extends ExtendableMessageNano<ImsCapabilities>
  {
    private static volatile ImsCapabilities[] _emptyArray;
    public boolean utOverLte;
    public boolean utOverWifi;
    public boolean videoOverLte;
    public boolean videoOverWifi;
    public boolean voiceOverLte;
    public boolean voiceOverWifi;
    
    public ImsCapabilities()
    {
      clear();
    }
    
    public static ImsCapabilities[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new ImsCapabilities[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static ImsCapabilities parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new ImsCapabilities().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static ImsCapabilities parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (ImsCapabilities)MessageNano.mergeFrom(new ImsCapabilities(), paramArrayOfByte);
    }
    
    public ImsCapabilities clear()
    {
      voiceOverLte = false;
      voiceOverWifi = false;
      videoOverLte = false;
      videoOverWifi = false;
      utOverLte = false;
      utOverWifi = false;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (voiceOverLte) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(1, voiceOverLte);
      }
      i = j;
      if (voiceOverWifi) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(2, voiceOverWifi);
      }
      j = i;
      if (videoOverLte) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(3, videoOverLte);
      }
      i = j;
      if (videoOverWifi) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(4, videoOverWifi);
      }
      j = i;
      if (utOverLte) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(5, utOverLte);
      }
      i = j;
      if (utOverWifi) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(6, utOverWifi);
      }
      return i;
    }
    
    public ImsCapabilities mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
          if (i != 16)
          {
            if (i != 24)
            {
              if (i != 32)
              {
                if (i != 40)
                {
                  if (i != 48)
                  {
                    if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                      return this;
                    }
                  }
                  else {
                    utOverWifi = paramCodedInputByteBufferNano.readBool();
                  }
                }
                else {
                  utOverLte = paramCodedInputByteBufferNano.readBool();
                }
              }
              else {
                videoOverWifi = paramCodedInputByteBufferNano.readBool();
              }
            }
            else {
              videoOverLte = paramCodedInputByteBufferNano.readBool();
            }
          }
          else {
            voiceOverWifi = paramCodedInputByteBufferNano.readBool();
          }
        }
        else {
          voiceOverLte = paramCodedInputByteBufferNano.readBool();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (voiceOverLte) {
        paramCodedOutputByteBufferNano.writeBool(1, voiceOverLte);
      }
      if (voiceOverWifi) {
        paramCodedOutputByteBufferNano.writeBool(2, voiceOverWifi);
      }
      if (videoOverLte) {
        paramCodedOutputByteBufferNano.writeBool(3, videoOverLte);
      }
      if (videoOverWifi) {
        paramCodedOutputByteBufferNano.writeBool(4, videoOverWifi);
      }
      if (utOverLte) {
        paramCodedOutputByteBufferNano.writeBool(5, utOverLte);
      }
      if (utOverWifi) {
        paramCodedOutputByteBufferNano.writeBool(6, utOverWifi);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class ImsConnectionState
    extends ExtendableMessageNano<ImsConnectionState>
  {
    private static volatile ImsConnectionState[] _emptyArray;
    public TelephonyProto.ImsReasonInfo reasonInfo;
    public int state;
    
    public ImsConnectionState()
    {
      clear();
    }
    
    public static ImsConnectionState[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new ImsConnectionState[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static ImsConnectionState parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new ImsConnectionState().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static ImsConnectionState parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (ImsConnectionState)MessageNano.mergeFrom(new ImsConnectionState(), paramArrayOfByte);
    }
    
    public ImsConnectionState clear()
    {
      state = 0;
      reasonInfo = null;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (state != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, state);
      }
      i = j;
      if (reasonInfo != null) {
        i = j + CodedOutputByteBufferNano.computeMessageSize(2, reasonInfo);
      }
      return i;
    }
    
    public ImsConnectionState mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
              return this;
            }
          }
          else
          {
            if (reasonInfo == null) {
              reasonInfo = new TelephonyProto.ImsReasonInfo();
            }
            paramCodedInputByteBufferNano.readMessage(reasonInfo);
          }
        }
        else
        {
          int j = paramCodedInputByteBufferNano.getPosition();
          int k = paramCodedInputByteBufferNano.readInt32();
          switch (k)
          {
          default: 
            paramCodedInputByteBufferNano.rewindToPosition(j);
            storeUnknownField(paramCodedInputByteBufferNano, i);
            break;
          case 0: 
          case 1: 
          case 2: 
          case 3: 
          case 4: 
          case 5: 
            state = k;
          }
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (state != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, state);
      }
      if (reasonInfo != null) {
        paramCodedOutputByteBufferNano.writeMessage(2, reasonInfo);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static abstract interface State
    {
      public static final int CONNECTED = 1;
      public static final int DISCONNECTED = 3;
      public static final int PROGRESSING = 2;
      public static final int RESUMED = 4;
      public static final int STATE_UNKNOWN = 0;
      public static final int SUSPENDED = 5;
    }
  }
  
  public static final class ImsReasonInfo
    extends ExtendableMessageNano<ImsReasonInfo>
  {
    private static volatile ImsReasonInfo[] _emptyArray;
    public int extraCode;
    public String extraMessage;
    public int reasonCode;
    
    public ImsReasonInfo()
    {
      clear();
    }
    
    public static ImsReasonInfo[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new ImsReasonInfo[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static ImsReasonInfo parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new ImsReasonInfo().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static ImsReasonInfo parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (ImsReasonInfo)MessageNano.mergeFrom(new ImsReasonInfo(), paramArrayOfByte);
    }
    
    public ImsReasonInfo clear()
    {
      reasonCode = 0;
      extraCode = 0;
      extraMessage = "";
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (reasonCode != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, reasonCode);
      }
      i = j;
      if (extraCode != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, extraCode);
      }
      j = i;
      if (!extraMessage.equals("")) {
        j = i + CodedOutputByteBufferNano.computeStringSize(3, extraMessage);
      }
      return j;
    }
    
    public ImsReasonInfo mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
          if (i != 16)
          {
            if (i != 26)
            {
              if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else {
              extraMessage = paramCodedInputByteBufferNano.readString();
            }
          }
          else {
            extraCode = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          reasonCode = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (reasonCode != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, reasonCode);
      }
      if (extraCode != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, extraCode);
      }
      if (!extraMessage.equals("")) {
        paramCodedOutputByteBufferNano.writeString(3, extraMessage);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class ModemPowerStats
    extends ExtendableMessageNano<ModemPowerStats>
  {
    private static volatile ModemPowerStats[] _emptyArray;
    public long cellularKernelActiveTimeMs;
    public double energyConsumedMah;
    public long idleTimeMs;
    public long loggingDurationMs;
    public long numPacketsTx;
    public long rxTimeMs;
    public long sleepTimeMs;
    public long timeInVeryPoorRxSignalLevelMs;
    public long[] txTimeMs;
    
    public ModemPowerStats()
    {
      clear();
    }
    
    public static ModemPowerStats[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new ModemPowerStats[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static ModemPowerStats parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new ModemPowerStats().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static ModemPowerStats parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (ModemPowerStats)MessageNano.mergeFrom(new ModemPowerStats(), paramArrayOfByte);
    }
    
    public ModemPowerStats clear()
    {
      loggingDurationMs = 0L;
      energyConsumedMah = 0.0D;
      numPacketsTx = 0L;
      cellularKernelActiveTimeMs = 0L;
      timeInVeryPoorRxSignalLevelMs = 0L;
      sleepTimeMs = 0L;
      idleTimeMs = 0L;
      rxTimeMs = 0L;
      txTimeMs = WireFormatNano.EMPTY_LONG_ARRAY;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (loggingDurationMs != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(1, loggingDurationMs);
      }
      i = j;
      if (Double.doubleToLongBits(energyConsumedMah) != Double.doubleToLongBits(0.0D)) {
        i = j + CodedOutputByteBufferNano.computeDoubleSize(2, energyConsumedMah);
      }
      j = i;
      if (numPacketsTx != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(3, numPacketsTx);
      }
      int k = j;
      if (cellularKernelActiveTimeMs != 0L) {
        k = j + CodedOutputByteBufferNano.computeInt64Size(4, cellularKernelActiveTimeMs);
      }
      i = k;
      if (timeInVeryPoorRxSignalLevelMs != 0L) {
        i = k + CodedOutputByteBufferNano.computeInt64Size(5, timeInVeryPoorRxSignalLevelMs);
      }
      j = i;
      if (sleepTimeMs != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(6, sleepTimeMs);
      }
      i = j;
      if (idleTimeMs != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(7, idleTimeMs);
      }
      j = i;
      if (rxTimeMs != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(8, rxTimeMs);
      }
      i = j;
      if (txTimeMs != null)
      {
        i = j;
        if (txTimeMs.length > 0)
        {
          k = 0;
          for (i = 0; i < txTimeMs.length; i++)
          {
            long l = txTimeMs[i];
            k += CodedOutputByteBufferNano.computeInt64SizeNoTag(l);
          }
          i = j + k + 1 * txTimeMs.length;
        }
      }
      return i;
    }
    
    public ModemPowerStats mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        int k;
        long[] arrayOfLong;
        switch (i)
        {
        default: 
          if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 74: 
          int j = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
          k = 0;
          i = paramCodedInputByteBufferNano.getPosition();
          while (paramCodedInputByteBufferNano.getBytesUntilLimit() > 0)
          {
            paramCodedInputByteBufferNano.readInt64();
            k++;
          }
          paramCodedInputByteBufferNano.rewindToPosition(i);
          if (txTimeMs == null) {
            i = 0;
          } else {
            i = txTimeMs.length;
          }
          arrayOfLong = new long[i + k];
          k = i;
          if (i != 0) {
            System.arraycopy(txTimeMs, 0, arrayOfLong, 0, i);
          }
          for (k = i; k < arrayOfLong.length; k++) {
            arrayOfLong[k] = paramCodedInputByteBufferNano.readInt64();
          }
          txTimeMs = arrayOfLong;
          paramCodedInputByteBufferNano.popLimit(j);
          break;
        case 72: 
          k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 72);
          if (txTimeMs == null) {
            i = 0;
          } else {
            i = txTimeMs.length;
          }
          arrayOfLong = new long[i + k];
          k = i;
          if (i != 0) {
            System.arraycopy(txTimeMs, 0, arrayOfLong, 0, i);
          }
          for (k = i; k < arrayOfLong.length - 1; k++)
          {
            arrayOfLong[k] = paramCodedInputByteBufferNano.readInt64();
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfLong[k] = paramCodedInputByteBufferNano.readInt64();
          txTimeMs = arrayOfLong;
          break;
        case 64: 
          rxTimeMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 56: 
          idleTimeMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 48: 
          sleepTimeMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 40: 
          timeInVeryPoorRxSignalLevelMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 32: 
          cellularKernelActiveTimeMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 24: 
          numPacketsTx = paramCodedInputByteBufferNano.readInt64();
          break;
        case 17: 
          energyConsumedMah = paramCodedInputByteBufferNano.readDouble();
          break;
        case 8: 
          loggingDurationMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (loggingDurationMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(1, loggingDurationMs);
      }
      if (Double.doubleToLongBits(energyConsumedMah) != Double.doubleToLongBits(0.0D)) {
        paramCodedOutputByteBufferNano.writeDouble(2, energyConsumedMah);
      }
      if (numPacketsTx != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(3, numPacketsTx);
      }
      if (cellularKernelActiveTimeMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(4, cellularKernelActiveTimeMs);
      }
      if (timeInVeryPoorRxSignalLevelMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(5, timeInVeryPoorRxSignalLevelMs);
      }
      if (sleepTimeMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(6, sleepTimeMs);
      }
      if (idleTimeMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(7, idleTimeMs);
      }
      if (rxTimeMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(8, rxTimeMs);
      }
      if ((txTimeMs != null) && (txTimeMs.length > 0)) {
        for (int i = 0; i < txTimeMs.length; i++) {
          paramCodedOutputByteBufferNano.writeInt64(9, txTimeMs[i]);
        }
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static abstract interface PdpType
  {
    public static final int PDP_TYPE_IP = 1;
    public static final int PDP_TYPE_IPV4V6 = 3;
    public static final int PDP_TYPE_IPV6 = 2;
    public static final int PDP_TYPE_PPP = 4;
    public static final int PDP_UNKNOWN = 0;
  }
  
  public static abstract interface RadioAccessTechnology
  {
    public static final int RAT_1XRTT = 6;
    public static final int RAT_EDGE = 2;
    public static final int RAT_EHRPD = 13;
    public static final int RAT_EVDO_0 = 7;
    public static final int RAT_EVDO_A = 8;
    public static final int RAT_EVDO_B = 12;
    public static final int RAT_GPRS = 1;
    public static final int RAT_GSM = 16;
    public static final int RAT_HSDPA = 9;
    public static final int RAT_HSPA = 11;
    public static final int RAT_HSPAP = 15;
    public static final int RAT_HSUPA = 10;
    public static final int RAT_IS95A = 4;
    public static final int RAT_IS95B = 5;
    public static final int RAT_IWLAN = 18;
    public static final int RAT_LTE = 14;
    public static final int RAT_LTE_CA = 19;
    public static final int RAT_TD_SCDMA = 17;
    public static final int RAT_UMTS = 3;
    public static final int RAT_UNKNOWN = 0;
    public static final int UNKNOWN = -1;
  }
  
  public static final class RilDataCall
    extends ExtendableMessageNano<RilDataCall>
  {
    private static volatile RilDataCall[] _emptyArray;
    public int cid;
    public String iframe;
    public int type;
    
    public RilDataCall()
    {
      clear();
    }
    
    public static RilDataCall[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new RilDataCall[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static RilDataCall parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new RilDataCall().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static RilDataCall parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (RilDataCall)MessageNano.mergeFrom(new RilDataCall(), paramArrayOfByte);
    }
    
    public RilDataCall clear()
    {
      cid = 0;
      type = 0;
      iframe = "";
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (cid != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, cid);
      }
      i = j;
      if (type != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, type);
      }
      j = i;
      if (!iframe.equals("")) {
        j = i + CodedOutputByteBufferNano.computeStringSize(3, iframe);
      }
      return j;
    }
    
    public RilDataCall mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
          if (i != 16)
          {
            if (i != 26)
            {
              if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else {
              iframe = paramCodedInputByteBufferNano.readString();
            }
          }
          else
          {
            int j = paramCodedInputByteBufferNano.getPosition();
            int k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(j);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
              type = k;
              break;
            }
          }
        }
        else {
          cid = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (cid != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, cid);
      }
      if (type != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, type);
      }
      if (!iframe.equals("")) {
        paramCodedOutputByteBufferNano.writeString(3, iframe);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static abstract interface RilErrno
  {
    public static final int RIL_E_ABORTED = 66;
    public static final int RIL_E_CANCELLED = 8;
    public static final int RIL_E_DEVICE_IN_USE = 65;
    public static final int RIL_E_DIAL_MODIFIED_TO_DIAL = 21;
    public static final int RIL_E_DIAL_MODIFIED_TO_SS = 20;
    public static final int RIL_E_DIAL_MODIFIED_TO_USSD = 19;
    public static final int RIL_E_EMPTY_RECORD = 56;
    public static final int RIL_E_ENCODING_ERR = 58;
    public static final int RIL_E_FDN_CHECK_FAILURE = 15;
    public static final int RIL_E_GENERIC_FAILURE = 3;
    public static final int RIL_E_ILLEGAL_SIM_OR_ME = 16;
    public static final int RIL_E_INTERNAL_ERR = 39;
    public static final int RIL_E_INVALID_ARGUMENTS = 45;
    public static final int RIL_E_INVALID_CALL_ID = 48;
    public static final int RIL_E_INVALID_MODEM_STATE = 47;
    public static final int RIL_E_INVALID_RESPONSE = 67;
    public static final int RIL_E_INVALID_SIM_STATE = 46;
    public static final int RIL_E_INVALID_SMSC_ADDRESS = 59;
    public static final int RIL_E_INVALID_SMS_FORMAT = 57;
    public static final int RIL_E_INVALID_STATE = 42;
    public static final int RIL_E_LCE_NOT_SUPPORTED = 36;
    public static final int RIL_E_LCE_NOT_SUPPORTED_NEW = 37;
    public static final int RIL_E_MISSING_RESOURCE = 17;
    public static final int RIL_E_MODEM_ERR = 41;
    public static final int RIL_E_MODE_NOT_SUPPORTED = 14;
    public static final int RIL_E_NETWORK_ERR = 50;
    public static final int RIL_E_NETWORK_NOT_READY = 61;
    public static final int RIL_E_NETWORK_REJECT = 54;
    public static final int RIL_E_NOT_PROVISIONED = 62;
    public static final int RIL_E_NO_MEMORY = 38;
    public static final int RIL_E_NO_NETWORK_FOUND = 64;
    public static final int RIL_E_NO_RESOURCES = 43;
    public static final int RIL_E_NO_SMS_TO_ACK = 49;
    public static final int RIL_E_NO_SUBSCRIPTION = 63;
    public static final int RIL_E_NO_SUCH_ELEMENT = 18;
    public static final int RIL_E_NO_SUCH_ENTRY = 60;
    public static final int RIL_E_OPERATION_NOT_ALLOWED = 55;
    public static final int RIL_E_OP_NOT_ALLOWED_BEFORE_REG_TO_NW = 10;
    public static final int RIL_E_OP_NOT_ALLOWED_DURING_VOICE_CALL = 9;
    public static final int RIL_E_PASSWORD_INCORRECT = 4;
    public static final int RIL_E_RADIO_NOT_AVAILABLE = 2;
    public static final int RIL_E_REQUEST_NOT_SUPPORTED = 7;
    public static final int RIL_E_REQUEST_RATE_LIMITED = 51;
    public static final int RIL_E_SIM_ABSENT = 12;
    public static final int RIL_E_SIM_BUSY = 52;
    public static final int RIL_E_SIM_ERR = 44;
    public static final int RIL_E_SIM_FULL = 53;
    public static final int RIL_E_SIM_PIN2 = 5;
    public static final int RIL_E_SIM_PUK2 = 6;
    public static final int RIL_E_SMS_SEND_FAIL_RETRY = 11;
    public static final int RIL_E_SS_MODIFIED_TO_DIAL = 25;
    public static final int RIL_E_SS_MODIFIED_TO_SS = 28;
    public static final int RIL_E_SS_MODIFIED_TO_USSD = 26;
    public static final int RIL_E_SUBSCRIPTION_NOT_AVAILABLE = 13;
    public static final int RIL_E_SUBSCRIPTION_NOT_SUPPORTED = 27;
    public static final int RIL_E_SUCCESS = 1;
    public static final int RIL_E_SYSTEM_ERR = 40;
    public static final int RIL_E_UNKNOWN = 0;
    public static final int RIL_E_USSD_MODIFIED_TO_DIAL = 22;
    public static final int RIL_E_USSD_MODIFIED_TO_SS = 23;
    public static final int RIL_E_USSD_MODIFIED_TO_USSD = 24;
  }
  
  public static final class SmsSession
    extends ExtendableMessageNano<SmsSession>
  {
    private static volatile SmsSession[] _emptyArray;
    public Event[] events;
    public boolean eventsDropped;
    public int phoneId;
    public int startTimeMinutes;
    
    public SmsSession()
    {
      clear();
    }
    
    public static SmsSession[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new SmsSession[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static SmsSession parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new SmsSession().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static SmsSession parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (SmsSession)MessageNano.mergeFrom(new SmsSession(), paramArrayOfByte);
    }
    
    public SmsSession clear()
    {
      startTimeMinutes = 0;
      phoneId = 0;
      events = Event.emptyArray();
      eventsDropped = false;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (startTimeMinutes != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, startTimeMinutes);
      }
      i = j;
      if (phoneId != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, phoneId);
      }
      j = i;
      if (events != null)
      {
        j = i;
        if (events.length > 0)
        {
          int k = 0;
          for (;;)
          {
            j = i;
            if (k >= events.length) {
              break;
            }
            Event localEvent = events[k];
            j = i;
            if (localEvent != null) {
              j = i + CodedOutputByteBufferNano.computeMessageSize(3, localEvent);
            }
            k++;
            i = j;
          }
        }
      }
      i = j;
      if (eventsDropped) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(4, eventsDropped);
      }
      return i;
    }
    
    public SmsSession mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
          if (i != 16)
          {
            if (i != 26)
            {
              if (i != 32)
              {
                if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else {
                eventsDropped = paramCodedInputByteBufferNano.readBool();
              }
            }
            else
            {
              int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 26);
              if (events == null) {
                i = 0;
              } else {
                i = events.length;
              }
              Event[] arrayOfEvent = new Event[i + j];
              j = i;
              if (i != 0) {
                System.arraycopy(events, 0, arrayOfEvent, 0, i);
              }
              for (j = i; j < arrayOfEvent.length - 1; j++)
              {
                arrayOfEvent[j] = new Event();
                paramCodedInputByteBufferNano.readMessage(arrayOfEvent[j]);
                paramCodedInputByteBufferNano.readTag();
              }
              arrayOfEvent[j] = new Event();
              paramCodedInputByteBufferNano.readMessage(arrayOfEvent[j]);
              events = arrayOfEvent;
            }
          }
          else {
            phoneId = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          startTimeMinutes = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (startTimeMinutes != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, startTimeMinutes);
      }
      if (phoneId != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, phoneId);
      }
      if ((events != null) && (events.length > 0)) {
        for (int i = 0; i < events.length; i++)
        {
          Event localEvent = events[i];
          if (localEvent != null) {
            paramCodedOutputByteBufferNano.writeMessage(3, localEvent);
          }
        }
      }
      if (eventsDropped) {
        paramCodedOutputByteBufferNano.writeBool(4, eventsDropped);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static final class Event
      extends ExtendableMessageNano<Event>
    {
      private static volatile Event[] _emptyArray;
      public CBMessage cellBroadcastMessage;
      public TelephonyProto.RilDataCall[] dataCalls;
      public int delay;
      public int error;
      public int errorCode;
      public int format;
      public TelephonyProto.ImsCapabilities imsCapabilities;
      public TelephonyProto.ImsConnectionState imsConnectionState;
      public int rilRequestId;
      public TelephonyProto.TelephonyServiceState serviceState;
      public TelephonyProto.TelephonySettings settings;
      public int tech;
      public int type;
      
      public Event()
      {
        clear();
      }
      
      public static Event[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new Event[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static Event parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new Event().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static Event parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (Event)MessageNano.mergeFrom(new Event(), paramArrayOfByte);
      }
      
      public Event clear()
      {
        type = 0;
        delay = 0;
        settings = null;
        serviceState = null;
        imsConnectionState = null;
        imsCapabilities = null;
        dataCalls = TelephonyProto.RilDataCall.emptyArray();
        format = 0;
        tech = 0;
        errorCode = 0;
        error = 0;
        rilRequestId = 0;
        cellBroadcastMessage = null;
        unknownFieldData = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (type != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, type);
        }
        i = j;
        if (delay != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, delay);
        }
        j = i;
        if (settings != null) {
          j = i + CodedOutputByteBufferNano.computeMessageSize(3, settings);
        }
        int k = j;
        if (serviceState != null) {
          k = j + CodedOutputByteBufferNano.computeMessageSize(4, serviceState);
        }
        i = k;
        if (imsConnectionState != null) {
          i = k + CodedOutputByteBufferNano.computeMessageSize(5, imsConnectionState);
        }
        j = i;
        if (imsCapabilities != null) {
          j = i + CodedOutputByteBufferNano.computeMessageSize(6, imsCapabilities);
        }
        i = j;
        if (dataCalls != null)
        {
          i = j;
          if (dataCalls.length > 0)
          {
            k = 0;
            for (;;)
            {
              i = j;
              if (k >= dataCalls.length) {
                break;
              }
              TelephonyProto.RilDataCall localRilDataCall = dataCalls[k];
              i = j;
              if (localRilDataCall != null) {
                i = j + CodedOutputByteBufferNano.computeMessageSize(7, localRilDataCall);
              }
              k++;
              j = i;
            }
          }
        }
        j = i;
        if (format != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(8, format);
        }
        i = j;
        if (tech != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(9, tech);
        }
        j = i;
        if (errorCode != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(10, errorCode);
        }
        i = j;
        if (error != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(11, error);
        }
        j = i;
        if (rilRequestId != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(12, rilRequestId);
        }
        i = j;
        if (cellBroadcastMessage != null) {
          i = j + CodedOutputByteBufferNano.computeMessageSize(13, cellBroadcastMessage);
        }
        return i;
      }
      
      public Event mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        for (;;)
        {
          int i = paramCodedInputByteBufferNano.readTag();
          int j;
          int k;
          switch (i)
          {
          default: 
            if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
              return this;
            }
            break;
          case 106: 
            if (cellBroadcastMessage == null) {
              cellBroadcastMessage = new CBMessage();
            }
            paramCodedInputByteBufferNano.readMessage(cellBroadcastMessage);
            break;
          case 96: 
            rilRequestId = paramCodedInputByteBufferNano.readInt32();
            break;
          case 88: 
            j = paramCodedInputByteBufferNano.getPosition();
            k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              switch (k)
              {
              default: 
                paramCodedInputByteBufferNano.rewindToPosition(j);
                storeUnknownField(paramCodedInputByteBufferNano, i);
              }
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: 
            case 26: 
            case 27: 
            case 28: 
              error = k;
            }
            break;
          case 80: 
            errorCode = paramCodedInputByteBufferNano.readInt32();
            break;
          case 72: 
            k = paramCodedInputByteBufferNano.getPosition();
            j = paramCodedInputByteBufferNano.readInt32();
            switch (j)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(k);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
              tech = j;
            }
            break;
          case 64: 
            k = paramCodedInputByteBufferNano.getPosition();
            j = paramCodedInputByteBufferNano.readInt32();
            switch (j)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(k);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
              format = j;
            }
            break;
          case 58: 
            k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 58);
            if (dataCalls == null) {
              i = 0;
            } else {
              i = dataCalls.length;
            }
            TelephonyProto.RilDataCall[] arrayOfRilDataCall = new TelephonyProto.RilDataCall[i + k];
            k = i;
            if (i != 0) {
              System.arraycopy(dataCalls, 0, arrayOfRilDataCall, 0, i);
            }
            for (k = i; k < arrayOfRilDataCall.length - 1; k++)
            {
              arrayOfRilDataCall[k] = new TelephonyProto.RilDataCall();
              paramCodedInputByteBufferNano.readMessage(arrayOfRilDataCall[k]);
              paramCodedInputByteBufferNano.readTag();
            }
            arrayOfRilDataCall[k] = new TelephonyProto.RilDataCall();
            paramCodedInputByteBufferNano.readMessage(arrayOfRilDataCall[k]);
            dataCalls = arrayOfRilDataCall;
            break;
          case 50: 
            if (imsCapabilities == null) {
              imsCapabilities = new TelephonyProto.ImsCapabilities();
            }
            paramCodedInputByteBufferNano.readMessage(imsCapabilities);
            break;
          case 42: 
            if (imsConnectionState == null) {
              imsConnectionState = new TelephonyProto.ImsConnectionState();
            }
            paramCodedInputByteBufferNano.readMessage(imsConnectionState);
            break;
          case 34: 
            if (serviceState == null) {
              serviceState = new TelephonyProto.TelephonyServiceState();
            }
            paramCodedInputByteBufferNano.readMessage(serviceState);
            break;
          case 26: 
            if (settings == null) {
              settings = new TelephonyProto.TelephonySettings();
            }
            paramCodedInputByteBufferNano.readMessage(settings);
            break;
          case 16: 
            k = paramCodedInputByteBufferNano.getPosition();
            j = paramCodedInputByteBufferNano.readInt32();
            switch (j)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(k);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
              delay = j;
            }
            break;
          case 8: 
            j = paramCodedInputByteBufferNano.getPosition();
            k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(j);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
              type = k;
            }
            break;
          case 0: 
            return this;
          }
        }
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (type != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, type);
        }
        if (delay != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, delay);
        }
        if (settings != null) {
          paramCodedOutputByteBufferNano.writeMessage(3, settings);
        }
        if (serviceState != null) {
          paramCodedOutputByteBufferNano.writeMessage(4, serviceState);
        }
        if (imsConnectionState != null) {
          paramCodedOutputByteBufferNano.writeMessage(5, imsConnectionState);
        }
        if (imsCapabilities != null) {
          paramCodedOutputByteBufferNano.writeMessage(6, imsCapabilities);
        }
        if ((dataCalls != null) && (dataCalls.length > 0)) {
          for (int i = 0; i < dataCalls.length; i++)
          {
            TelephonyProto.RilDataCall localRilDataCall = dataCalls[i];
            if (localRilDataCall != null) {
              paramCodedOutputByteBufferNano.writeMessage(7, localRilDataCall);
            }
          }
        }
        if (format != 0) {
          paramCodedOutputByteBufferNano.writeInt32(8, format);
        }
        if (tech != 0) {
          paramCodedOutputByteBufferNano.writeInt32(9, tech);
        }
        if (errorCode != 0) {
          paramCodedOutputByteBufferNano.writeInt32(10, errorCode);
        }
        if (error != 0) {
          paramCodedOutputByteBufferNano.writeInt32(11, error);
        }
        if (rilRequestId != 0) {
          paramCodedOutputByteBufferNano.writeInt32(12, rilRequestId);
        }
        if (cellBroadcastMessage != null) {
          paramCodedOutputByteBufferNano.writeMessage(13, cellBroadcastMessage);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
      
      public static final class CBMessage
        extends ExtendableMessageNano<CBMessage>
      {
        private static volatile CBMessage[] _emptyArray;
        public int msgFormat;
        public int msgPriority;
        public int msgType;
        public int serviceCategory;
        
        public CBMessage()
        {
          clear();
        }
        
        public static CBMessage[] emptyArray()
        {
          if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK)
            {
              if (_emptyArray == null) {
                _emptyArray = new CBMessage[0];
              }
            }
          }
          return _emptyArray;
        }
        
        public static CBMessage parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
          throws IOException
        {
          return new CBMessage().mergeFrom(paramCodedInputByteBufferNano);
        }
        
        public static CBMessage parseFrom(byte[] paramArrayOfByte)
          throws InvalidProtocolBufferNanoException
        {
          return (CBMessage)MessageNano.mergeFrom(new CBMessage(), paramArrayOfByte);
        }
        
        public CBMessage clear()
        {
          msgFormat = 0;
          msgPriority = 0;
          msgType = 0;
          serviceCategory = 0;
          unknownFieldData = null;
          cachedSize = -1;
          return this;
        }
        
        protected int computeSerializedSize()
        {
          int i = super.computeSerializedSize();
          int j = i;
          if (msgFormat != 0) {
            j = i + CodedOutputByteBufferNano.computeInt32Size(1, msgFormat);
          }
          i = j;
          if (msgPriority != 0) {
            i = j + CodedOutputByteBufferNano.computeInt32Size(2, msgPriority);
          }
          j = i;
          if (msgType != 0) {
            j = i + CodedOutputByteBufferNano.computeInt32Size(3, msgType);
          }
          i = j;
          if (serviceCategory != 0) {
            i = j + CodedOutputByteBufferNano.computeInt32Size(4, serviceCategory);
          }
          return i;
        }
        
        public CBMessage mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
          throws IOException
        {
          for (;;)
          {
            int i = paramCodedInputByteBufferNano.readTag();
            if (i == 0) {
              break;
            }
            int j;
            int k;
            if (i != 8)
            {
              if (i != 16)
              {
                if (i != 24)
                {
                  if (i != 32)
                  {
                    if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                      return this;
                    }
                  }
                  else {
                    serviceCategory = paramCodedInputByteBufferNano.readInt32();
                  }
                }
                else
                {
                  j = paramCodedInputByteBufferNano.getPosition();
                  k = paramCodedInputByteBufferNano.readInt32();
                  switch (k)
                  {
                  default: 
                    paramCodedInputByteBufferNano.rewindToPosition(j);
                    storeUnknownField(paramCodedInputByteBufferNano, i);
                    break;
                  case 0: 
                  case 1: 
                  case 2: 
                  case 3: 
                    msgType = k;
                    break;
                  }
                }
              }
              else
              {
                j = paramCodedInputByteBufferNano.getPosition();
                k = paramCodedInputByteBufferNano.readInt32();
                switch (k)
                {
                default: 
                  paramCodedInputByteBufferNano.rewindToPosition(j);
                  storeUnknownField(paramCodedInputByteBufferNano, i);
                  break;
                case 0: 
                case 1: 
                case 2: 
                case 3: 
                case 4: 
                  msgPriority = k;
                  break;
                }
              }
            }
            else
            {
              k = paramCodedInputByteBufferNano.getPosition();
              j = paramCodedInputByteBufferNano.readInt32();
              switch (j)
              {
              default: 
                paramCodedInputByteBufferNano.rewindToPosition(k);
                storeUnknownField(paramCodedInputByteBufferNano, i);
                break;
              case 0: 
              case 1: 
              case 2: 
                msgFormat = j;
              }
            }
          }
          return this;
        }
        
        public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
          throws IOException
        {
          if (msgFormat != 0) {
            paramCodedOutputByteBufferNano.writeInt32(1, msgFormat);
          }
          if (msgPriority != 0) {
            paramCodedOutputByteBufferNano.writeInt32(2, msgPriority);
          }
          if (msgType != 0) {
            paramCodedOutputByteBufferNano.writeInt32(3, msgType);
          }
          if (serviceCategory != 0) {
            paramCodedOutputByteBufferNano.writeInt32(4, serviceCategory);
          }
          super.writeTo(paramCodedOutputByteBufferNano);
        }
      }
      
      public static abstract interface CBMessageType
      {
        public static final int CMAS = 2;
        public static final int ETWS = 1;
        public static final int OTHER = 3;
        public static final int TYPE_UNKNOWN = 0;
      }
      
      public static abstract interface CBPriority
      {
        public static final int EMERGENCY = 4;
        public static final int INTERACTIVE = 2;
        public static final int NORMAL = 1;
        public static final int PRIORITY_UNKNOWN = 0;
        public static final int URGENT = 3;
      }
      
      public static abstract interface Format
      {
        public static final int SMS_FORMAT_3GPP = 1;
        public static final int SMS_FORMAT_3GPP2 = 2;
        public static final int SMS_FORMAT_UNKNOWN = 0;
      }
      
      public static abstract interface Tech
      {
        public static final int SMS_CDMA = 2;
        public static final int SMS_GSM = 1;
        public static final int SMS_IMS = 3;
        public static final int SMS_UNKNOWN = 0;
      }
      
      public static abstract interface Type
      {
        public static final int CB_SMS_RECEIVED = 9;
        public static final int DATA_CALL_LIST_CHANGED = 5;
        public static final int EVENT_UNKNOWN = 0;
        public static final int IMS_CAPABILITIES_CHANGED = 4;
        public static final int IMS_CONNECTION_STATE_CHANGED = 3;
        public static final int RIL_SERVICE_STATE_CHANGED = 2;
        public static final int SETTINGS_CHANGED = 1;
        public static final int SMS_RECEIVED = 8;
        public static final int SMS_SEND = 6;
        public static final int SMS_SEND_RESULT = 7;
      }
    }
  }
  
  public static final class TelephonyCallSession
    extends ExtendableMessageNano<TelephonyCallSession>
  {
    private static volatile TelephonyCallSession[] _emptyArray;
    public Event[] events;
    public boolean eventsDropped;
    public int phoneId;
    public int startTimeMinutes;
    
    public TelephonyCallSession()
    {
      clear();
    }
    
    public static TelephonyCallSession[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new TelephonyCallSession[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static TelephonyCallSession parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new TelephonyCallSession().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static TelephonyCallSession parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (TelephonyCallSession)MessageNano.mergeFrom(new TelephonyCallSession(), paramArrayOfByte);
    }
    
    public TelephonyCallSession clear()
    {
      startTimeMinutes = 0;
      phoneId = 0;
      events = Event.emptyArray();
      eventsDropped = false;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (startTimeMinutes != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, startTimeMinutes);
      }
      i = j;
      if (phoneId != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, phoneId);
      }
      j = i;
      if (events != null)
      {
        j = i;
        if (events.length > 0)
        {
          int k = 0;
          for (;;)
          {
            j = i;
            if (k >= events.length) {
              break;
            }
            Event localEvent = events[k];
            j = i;
            if (localEvent != null) {
              j = i + CodedOutputByteBufferNano.computeMessageSize(3, localEvent);
            }
            k++;
            i = j;
          }
        }
      }
      i = j;
      if (eventsDropped) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(4, eventsDropped);
      }
      return i;
    }
    
    public TelephonyCallSession mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
          if (i != 16)
          {
            if (i != 26)
            {
              if (i != 32)
              {
                if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else {
                eventsDropped = paramCodedInputByteBufferNano.readBool();
              }
            }
            else
            {
              int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 26);
              if (events == null) {
                i = 0;
              } else {
                i = events.length;
              }
              Event[] arrayOfEvent = new Event[i + j];
              j = i;
              if (i != 0) {
                System.arraycopy(events, 0, arrayOfEvent, 0, i);
              }
              for (j = i; j < arrayOfEvent.length - 1; j++)
              {
                arrayOfEvent[j] = new Event();
                paramCodedInputByteBufferNano.readMessage(arrayOfEvent[j]);
                paramCodedInputByteBufferNano.readTag();
              }
              arrayOfEvent[j] = new Event();
              paramCodedInputByteBufferNano.readMessage(arrayOfEvent[j]);
              events = arrayOfEvent;
            }
          }
          else {
            phoneId = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          startTimeMinutes = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (startTimeMinutes != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, startTimeMinutes);
      }
      if (phoneId != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, phoneId);
      }
      if ((events != null) && (events.length > 0)) {
        for (int i = 0; i < events.length; i++)
        {
          Event localEvent = events[i];
          if (localEvent != null) {
            paramCodedOutputByteBufferNano.writeMessage(3, localEvent);
          }
        }
      }
      if (eventsDropped) {
        paramCodedOutputByteBufferNano.writeBool(4, eventsDropped);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static final class Event
      extends ExtendableMessageNano<Event>
    {
      private static volatile Event[] _emptyArray;
      public int callIndex;
      public int callState;
      public RilCall[] calls;
      public TelephonyProto.RilDataCall[] dataCalls;
      public int delay;
      public int error;
      public TelephonyProto.ImsCapabilities imsCapabilities;
      public int imsCommand;
      public TelephonyProto.ImsConnectionState imsConnectionState;
      public int mergedCallIndex;
      public long nitzTimestampMillis;
      public int phoneState;
      public TelephonyProto.ImsReasonInfo reasonInfo;
      public int rilRequest;
      public int rilRequestId;
      public TelephonyProto.TelephonyServiceState serviceState;
      public TelephonyProto.TelephonySettings settings;
      public int srcAccessTech;
      public int srvccState;
      public int targetAccessTech;
      public int type;
      
      public Event()
      {
        clear();
      }
      
      public static Event[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new Event[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static Event parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new Event().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static Event parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (Event)MessageNano.mergeFrom(new Event(), paramArrayOfByte);
      }
      
      public Event clear()
      {
        type = 0;
        delay = 0;
        settings = null;
        serviceState = null;
        imsConnectionState = null;
        imsCapabilities = null;
        dataCalls = TelephonyProto.RilDataCall.emptyArray();
        phoneState = 0;
        callState = 0;
        callIndex = 0;
        mergedCallIndex = 0;
        calls = RilCall.emptyArray();
        error = 0;
        rilRequest = 0;
        rilRequestId = 0;
        srvccState = 0;
        imsCommand = 0;
        reasonInfo = null;
        srcAccessTech = -1;
        targetAccessTech = -1;
        nitzTimestampMillis = 0L;
        unknownFieldData = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (type != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, type);
        }
        i = j;
        if (delay != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, delay);
        }
        int k = i;
        if (settings != null) {
          k = i + CodedOutputByteBufferNano.computeMessageSize(3, settings);
        }
        j = k;
        if (serviceState != null) {
          j = k + CodedOutputByteBufferNano.computeMessageSize(4, serviceState);
        }
        i = j;
        if (imsConnectionState != null) {
          i = j + CodedOutputByteBufferNano.computeMessageSize(5, imsConnectionState);
        }
        j = i;
        if (imsCapabilities != null) {
          j = i + CodedOutputByteBufferNano.computeMessageSize(6, imsCapabilities);
        }
        Object localObject = dataCalls;
        int m = 0;
        i = j;
        if (localObject != null)
        {
          i = j;
          if (dataCalls.length > 0)
          {
            i = 0;
            while (i < dataCalls.length)
            {
              localObject = dataCalls[i];
              k = j;
              if (localObject != null) {
                k = j + CodedOutputByteBufferNano.computeMessageSize(7, (MessageNano)localObject);
              }
              i++;
              j = k;
            }
            i = j;
          }
        }
        j = i;
        if (phoneState != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(8, phoneState);
        }
        i = j;
        if (callState != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(9, callState);
        }
        k = i;
        if (callIndex != 0) {
          k = i + CodedOutputByteBufferNano.computeInt32Size(10, callIndex);
        }
        j = k;
        if (mergedCallIndex != 0) {
          j = k + CodedOutputByteBufferNano.computeInt32Size(11, mergedCallIndex);
        }
        i = j;
        if (calls != null)
        {
          i = j;
          if (calls.length > 0)
          {
            k = m;
            for (;;)
            {
              i = j;
              if (k >= calls.length) {
                break;
              }
              localObject = calls[k];
              i = j;
              if (localObject != null) {
                i = j + CodedOutputByteBufferNano.computeMessageSize(12, (MessageNano)localObject);
              }
              k++;
              j = i;
            }
          }
        }
        j = i;
        if (error != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(13, error);
        }
        i = j;
        if (rilRequest != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(14, rilRequest);
        }
        j = i;
        if (rilRequestId != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(15, rilRequestId);
        }
        i = j;
        if (srvccState != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(16, srvccState);
        }
        j = i;
        if (imsCommand != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(17, imsCommand);
        }
        i = j;
        if (reasonInfo != null) {
          i = j + CodedOutputByteBufferNano.computeMessageSize(18, reasonInfo);
        }
        j = i;
        if (srcAccessTech != -1) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(19, srcAccessTech);
        }
        i = j;
        if (targetAccessTech != -1) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(20, targetAccessTech);
        }
        j = i;
        if (nitzTimestampMillis != 0L) {
          j = i + CodedOutputByteBufferNano.computeInt64Size(21, nitzTimestampMillis);
        }
        return j;
      }
      
      public Event mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        for (;;)
        {
          int i = paramCodedInputByteBufferNano.readTag();
          int j;
          int k;
          Object localObject;
          switch (i)
          {
          default: 
            if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
              return this;
            }
            break;
          case 168: 
            nitzTimestampMillis = paramCodedInputByteBufferNano.readInt64();
            break;
          case 160: 
            j = paramCodedInputByteBufferNano.getPosition();
            k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(j);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case -1: 
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
              targetAccessTech = k;
            }
            break;
          case 152: 
            k = paramCodedInputByteBufferNano.getPosition();
            j = paramCodedInputByteBufferNano.readInt32();
            switch (j)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(k);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case -1: 
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
              srcAccessTech = j;
            }
            break;
          case 146: 
            if (reasonInfo == null) {
              reasonInfo = new TelephonyProto.ImsReasonInfo();
            }
            paramCodedInputByteBufferNano.readMessage(reasonInfo);
            break;
          case 136: 
            j = paramCodedInputByteBufferNano.getPosition();
            k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(j);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
              imsCommand = k;
            }
            break;
          case 128: 
            k = paramCodedInputByteBufferNano.getPosition();
            j = paramCodedInputByteBufferNano.readInt32();
            switch (j)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(k);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
              srvccState = j;
            }
            break;
          case 120: 
            rilRequestId = paramCodedInputByteBufferNano.readInt32();
            break;
          case 112: 
            j = paramCodedInputByteBufferNano.getPosition();
            k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(j);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
              rilRequest = k;
            }
            break;
          case 104: 
            j = paramCodedInputByteBufferNano.getPosition();
            k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              switch (k)
              {
              default: 
                paramCodedInputByteBufferNano.rewindToPosition(j);
                storeUnknownField(paramCodedInputByteBufferNano, i);
              }
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: 
            case 26: 
            case 27: 
            case 28: 
              error = k;
            }
            break;
          case 98: 
            j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 98);
            if (calls == null) {
              i = 0;
            } else {
              i = calls.length;
            }
            localObject = new RilCall[i + j];
            j = i;
            if (i != 0) {
              System.arraycopy(calls, 0, localObject, 0, i);
            }
            for (j = i; j < localObject.length - 1; j++)
            {
              localObject[j] = new RilCall();
              paramCodedInputByteBufferNano.readMessage(localObject[j]);
              paramCodedInputByteBufferNano.readTag();
            }
            localObject[j] = new RilCall();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            calls = ((RilCall[])localObject);
            break;
          case 88: 
            mergedCallIndex = paramCodedInputByteBufferNano.readInt32();
            break;
          case 80: 
            callIndex = paramCodedInputByteBufferNano.readInt32();
            break;
          case 72: 
            j = paramCodedInputByteBufferNano.getPosition();
            k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(j);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
              callState = k;
            }
            break;
          case 64: 
            k = paramCodedInputByteBufferNano.getPosition();
            j = paramCodedInputByteBufferNano.readInt32();
            switch (j)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(k);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
              phoneState = j;
            }
            break;
          case 58: 
            j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 58);
            if (dataCalls == null) {
              i = 0;
            } else {
              i = dataCalls.length;
            }
            localObject = new TelephonyProto.RilDataCall[i + j];
            j = i;
            if (i != 0) {
              System.arraycopy(dataCalls, 0, localObject, 0, i);
            }
            for (j = i; j < localObject.length - 1; j++)
            {
              localObject[j] = new TelephonyProto.RilDataCall();
              paramCodedInputByteBufferNano.readMessage(localObject[j]);
              paramCodedInputByteBufferNano.readTag();
            }
            localObject[j] = new TelephonyProto.RilDataCall();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            dataCalls = ((TelephonyProto.RilDataCall[])localObject);
            break;
          case 50: 
            if (imsCapabilities == null) {
              imsCapabilities = new TelephonyProto.ImsCapabilities();
            }
            paramCodedInputByteBufferNano.readMessage(imsCapabilities);
            break;
          case 42: 
            if (imsConnectionState == null) {
              imsConnectionState = new TelephonyProto.ImsConnectionState();
            }
            paramCodedInputByteBufferNano.readMessage(imsConnectionState);
            break;
          case 34: 
            if (serviceState == null) {
              serviceState = new TelephonyProto.TelephonyServiceState();
            }
            paramCodedInputByteBufferNano.readMessage(serviceState);
            break;
          case 26: 
            if (settings == null) {
              settings = new TelephonyProto.TelephonySettings();
            }
            paramCodedInputByteBufferNano.readMessage(settings);
            break;
          case 16: 
            j = paramCodedInputByteBufferNano.getPosition();
            k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(j);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
              delay = k;
            }
            break;
          case 8: 
            j = paramCodedInputByteBufferNano.getPosition();
            k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(j);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 21: 
              type = k;
            }
            break;
          case 0: 
            return this;
          }
        }
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (type != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, type);
        }
        if (delay != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, delay);
        }
        if (settings != null) {
          paramCodedOutputByteBufferNano.writeMessage(3, settings);
        }
        if (serviceState != null) {
          paramCodedOutputByteBufferNano.writeMessage(4, serviceState);
        }
        if (imsConnectionState != null) {
          paramCodedOutputByteBufferNano.writeMessage(5, imsConnectionState);
        }
        if (imsCapabilities != null) {
          paramCodedOutputByteBufferNano.writeMessage(6, imsCapabilities);
        }
        Object localObject = dataCalls;
        int i = 0;
        int j;
        if ((localObject != null) && (dataCalls.length > 0)) {
          for (j = 0; j < dataCalls.length; j++)
          {
            localObject = dataCalls[j];
            if (localObject != null) {
              paramCodedOutputByteBufferNano.writeMessage(7, (MessageNano)localObject);
            }
          }
        }
        if (phoneState != 0) {
          paramCodedOutputByteBufferNano.writeInt32(8, phoneState);
        }
        if (callState != 0) {
          paramCodedOutputByteBufferNano.writeInt32(9, callState);
        }
        if (callIndex != 0) {
          paramCodedOutputByteBufferNano.writeInt32(10, callIndex);
        }
        if (mergedCallIndex != 0) {
          paramCodedOutputByteBufferNano.writeInt32(11, mergedCallIndex);
        }
        if ((calls != null) && (calls.length > 0)) {
          for (j = i; j < calls.length; j++)
          {
            localObject = calls[j];
            if (localObject != null) {
              paramCodedOutputByteBufferNano.writeMessage(12, (MessageNano)localObject);
            }
          }
        }
        if (error != 0) {
          paramCodedOutputByteBufferNano.writeInt32(13, error);
        }
        if (rilRequest != 0) {
          paramCodedOutputByteBufferNano.writeInt32(14, rilRequest);
        }
        if (rilRequestId != 0) {
          paramCodedOutputByteBufferNano.writeInt32(15, rilRequestId);
        }
        if (srvccState != 0) {
          paramCodedOutputByteBufferNano.writeInt32(16, srvccState);
        }
        if (imsCommand != 0) {
          paramCodedOutputByteBufferNano.writeInt32(17, imsCommand);
        }
        if (reasonInfo != null) {
          paramCodedOutputByteBufferNano.writeMessage(18, reasonInfo);
        }
        if (srcAccessTech != -1) {
          paramCodedOutputByteBufferNano.writeInt32(19, srcAccessTech);
        }
        if (targetAccessTech != -1) {
          paramCodedOutputByteBufferNano.writeInt32(20, targetAccessTech);
        }
        if (nitzTimestampMillis != 0L) {
          paramCodedOutputByteBufferNano.writeInt64(21, nitzTimestampMillis);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
      
      public static abstract interface CallState
      {
        public static final int CALL_ACTIVE = 2;
        public static final int CALL_ALERTING = 5;
        public static final int CALL_DIALING = 4;
        public static final int CALL_DISCONNECTED = 8;
        public static final int CALL_DISCONNECTING = 9;
        public static final int CALL_HOLDING = 3;
        public static final int CALL_IDLE = 1;
        public static final int CALL_INCOMING = 6;
        public static final int CALL_UNKNOWN = 0;
        public static final int CALL_WAITING = 7;
      }
      
      public static abstract interface ImsCommand
      {
        public static final int IMS_CMD_ACCEPT = 2;
        public static final int IMS_CMD_CONFERENCE_EXTEND = 9;
        public static final int IMS_CMD_HOLD = 5;
        public static final int IMS_CMD_INVITE_PARTICIPANT = 10;
        public static final int IMS_CMD_MERGE = 7;
        public static final int IMS_CMD_REJECT = 3;
        public static final int IMS_CMD_REMOVE_PARTICIPANT = 11;
        public static final int IMS_CMD_RESUME = 6;
        public static final int IMS_CMD_START = 1;
        public static final int IMS_CMD_TERMINATE = 4;
        public static final int IMS_CMD_UNKNOWN = 0;
        public static final int IMS_CMD_UPDATE = 8;
      }
      
      public static abstract interface PhoneState
      {
        public static final int STATE_IDLE = 1;
        public static final int STATE_OFFHOOK = 3;
        public static final int STATE_RINGING = 2;
        public static final int STATE_UNKNOWN = 0;
      }
      
      public static final class RilCall
        extends ExtendableMessageNano<RilCall>
      {
        private static volatile RilCall[] _emptyArray;
        public int callEndReason;
        public int index;
        public boolean isMultiparty;
        public int state;
        public int type;
        
        public RilCall()
        {
          clear();
        }
        
        public static RilCall[] emptyArray()
        {
          if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK)
            {
              if (_emptyArray == null) {
                _emptyArray = new RilCall[0];
              }
            }
          }
          return _emptyArray;
        }
        
        public static RilCall parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
          throws IOException
        {
          return new RilCall().mergeFrom(paramCodedInputByteBufferNano);
        }
        
        public static RilCall parseFrom(byte[] paramArrayOfByte)
          throws InvalidProtocolBufferNanoException
        {
          return (RilCall)MessageNano.mergeFrom(new RilCall(), paramArrayOfByte);
        }
        
        public RilCall clear()
        {
          index = 0;
          state = 0;
          type = 0;
          callEndReason = 0;
          isMultiparty = false;
          unknownFieldData = null;
          cachedSize = -1;
          return this;
        }
        
        protected int computeSerializedSize()
        {
          int i = super.computeSerializedSize();
          int j = i;
          if (index != 0) {
            j = i + CodedOutputByteBufferNano.computeInt32Size(1, index);
          }
          i = j;
          if (state != 0) {
            i = j + CodedOutputByteBufferNano.computeInt32Size(2, state);
          }
          j = i;
          if (type != 0) {
            j = i + CodedOutputByteBufferNano.computeInt32Size(3, type);
          }
          i = j;
          if (callEndReason != 0) {
            i = j + CodedOutputByteBufferNano.computeInt32Size(4, callEndReason);
          }
          j = i;
          if (isMultiparty) {
            j = i + CodedOutputByteBufferNano.computeBoolSize(5, isMultiparty);
          }
          return j;
        }
        
        public RilCall mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              int j;
              int k;
              if (i != 16)
              {
                if (i != 24)
                {
                  if (i != 32)
                  {
                    if (i != 40)
                    {
                      if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                        return this;
                      }
                    }
                    else {
                      isMultiparty = paramCodedInputByteBufferNano.readBool();
                    }
                  }
                  else {
                    callEndReason = paramCodedInputByteBufferNano.readInt32();
                  }
                }
                else
                {
                  j = paramCodedInputByteBufferNano.getPosition();
                  k = paramCodedInputByteBufferNano.readInt32();
                  switch (k)
                  {
                  default: 
                    paramCodedInputByteBufferNano.rewindToPosition(j);
                    storeUnknownField(paramCodedInputByteBufferNano, i);
                    break;
                  case 0: 
                  case 1: 
                  case 2: 
                    type = k;
                    break;
                  }
                }
              }
              else
              {
                j = paramCodedInputByteBufferNano.getPosition();
                k = paramCodedInputByteBufferNano.readInt32();
                switch (k)
                {
                default: 
                  paramCodedInputByteBufferNano.rewindToPosition(j);
                  storeUnknownField(paramCodedInputByteBufferNano, i);
                  break;
                case 0: 
                case 1: 
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                case 6: 
                case 7: 
                case 8: 
                case 9: 
                  state = k;
                  break;
                }
              }
            }
            else
            {
              index = paramCodedInputByteBufferNano.readInt32();
            }
          }
          return this;
        }
        
        public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
          throws IOException
        {
          if (index != 0) {
            paramCodedOutputByteBufferNano.writeInt32(1, index);
          }
          if (state != 0) {
            paramCodedOutputByteBufferNano.writeInt32(2, state);
          }
          if (type != 0) {
            paramCodedOutputByteBufferNano.writeInt32(3, type);
          }
          if (callEndReason != 0) {
            paramCodedOutputByteBufferNano.writeInt32(4, callEndReason);
          }
          if (isMultiparty) {
            paramCodedOutputByteBufferNano.writeBool(5, isMultiparty);
          }
          super.writeTo(paramCodedOutputByteBufferNano);
        }
        
        public static abstract interface Type
        {
          public static final int MO = 1;
          public static final int MT = 2;
          public static final int UNKNOWN = 0;
        }
      }
      
      public static abstract interface RilRequest
      {
        public static final int RIL_REQUEST_ANSWER = 2;
        public static final int RIL_REQUEST_CDMA_FLASH = 6;
        public static final int RIL_REQUEST_CONFERENCE = 7;
        public static final int RIL_REQUEST_DIAL = 1;
        public static final int RIL_REQUEST_HANGUP = 3;
        public static final int RIL_REQUEST_SET_CALL_WAITING = 4;
        public static final int RIL_REQUEST_SWITCH_HOLDING_AND_ACTIVE = 5;
        public static final int RIL_REQUEST_UNKNOWN = 0;
      }
      
      public static abstract interface RilSrvccState
      {
        public static final int HANDOVER_CANCELED = 4;
        public static final int HANDOVER_COMPLETED = 2;
        public static final int HANDOVER_FAILED = 3;
        public static final int HANDOVER_STARTED = 1;
        public static final int HANDOVER_UNKNOWN = 0;
      }
      
      public static abstract interface Type
      {
        public static final int DATA_CALL_LIST_CHANGED = 5;
        public static final int EVENT_UNKNOWN = 0;
        public static final int IMS_CALL_HANDOVER = 18;
        public static final int IMS_CALL_HANDOVER_FAILED = 19;
        public static final int IMS_CALL_RECEIVE = 15;
        public static final int IMS_CALL_STATE_CHANGED = 16;
        public static final int IMS_CALL_TERMINATED = 17;
        public static final int IMS_CAPABILITIES_CHANGED = 4;
        public static final int IMS_COMMAND = 11;
        public static final int IMS_COMMAND_COMPLETE = 14;
        public static final int IMS_COMMAND_FAILED = 13;
        public static final int IMS_COMMAND_RECEIVED = 12;
        public static final int IMS_CONNECTION_STATE_CHANGED = 3;
        public static final int NITZ_TIME = 21;
        public static final int PHONE_STATE_CHANGED = 20;
        public static final int RIL_CALL_LIST_CHANGED = 10;
        public static final int RIL_CALL_RING = 8;
        public static final int RIL_CALL_SRVCC = 9;
        public static final int RIL_REQUEST = 6;
        public static final int RIL_RESPONSE = 7;
        public static final int RIL_SERVICE_STATE_CHANGED = 2;
        public static final int SETTINGS_CHANGED = 1;
      }
    }
  }
  
  public static final class TelephonyEvent
    extends ExtendableMessageNano<TelephonyEvent>
  {
    private static volatile TelephonyEvent[] _emptyArray;
    public CarrierIdMatching carrierIdMatching;
    public CarrierKeyChange carrierKeyChange;
    public TelephonyProto.RilDataCall[] dataCalls;
    public int dataStallAction;
    public RilDeactivateDataCall deactivateDataCall;
    public int error;
    public TelephonyProto.ImsCapabilities imsCapabilities;
    public TelephonyProto.ImsConnectionState imsConnectionState;
    public ModemRestart modemRestart;
    public long nitzTimestampMillis;
    public int phoneId;
    public TelephonyProto.TelephonyServiceState serviceState;
    public TelephonyProto.TelephonySettings settings;
    public RilSetupDataCall setupDataCall;
    public RilSetupDataCallResponse setupDataCallResponse;
    public long timestampMillis;
    public int type;
    
    public TelephonyEvent()
    {
      clear();
    }
    
    public static TelephonyEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new TelephonyEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static TelephonyEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new TelephonyEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static TelephonyEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (TelephonyEvent)MessageNano.mergeFrom(new TelephonyEvent(), paramArrayOfByte);
    }
    
    public TelephonyEvent clear()
    {
      timestampMillis = 0L;
      phoneId = 0;
      type = 0;
      settings = null;
      serviceState = null;
      imsConnectionState = null;
      imsCapabilities = null;
      dataCalls = TelephonyProto.RilDataCall.emptyArray();
      error = 0;
      setupDataCall = null;
      setupDataCallResponse = null;
      deactivateDataCall = null;
      dataStallAction = 0;
      modemRestart = null;
      nitzTimestampMillis = 0L;
      carrierIdMatching = null;
      carrierKeyChange = null;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (timestampMillis != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(1, timestampMillis);
      }
      int k = j;
      if (phoneId != 0) {
        k = j + CodedOutputByteBufferNano.computeInt32Size(2, phoneId);
      }
      i = k;
      if (type != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(3, type);
      }
      j = i;
      if (settings != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(4, settings);
      }
      k = j;
      if (serviceState != null) {
        k = j + CodedOutputByteBufferNano.computeMessageSize(5, serviceState);
      }
      i = k;
      if (imsConnectionState != null) {
        i = k + CodedOutputByteBufferNano.computeMessageSize(6, imsConnectionState);
      }
      j = i;
      if (imsCapabilities != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(7, imsCapabilities);
      }
      i = j;
      if (dataCalls != null)
      {
        i = j;
        if (dataCalls.length > 0)
        {
          k = 0;
          for (;;)
          {
            i = j;
            if (k >= dataCalls.length) {
              break;
            }
            TelephonyProto.RilDataCall localRilDataCall = dataCalls[k];
            i = j;
            if (localRilDataCall != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(8, localRilDataCall);
            }
            k++;
            j = i;
          }
        }
      }
      k = i;
      if (error != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(9, error);
      }
      j = k;
      if (setupDataCall != null) {
        j = k + CodedOutputByteBufferNano.computeMessageSize(10, setupDataCall);
      }
      k = j;
      if (setupDataCallResponse != null) {
        k = j + CodedOutputByteBufferNano.computeMessageSize(11, setupDataCallResponse);
      }
      i = k;
      if (deactivateDataCall != null) {
        i = k + CodedOutputByteBufferNano.computeMessageSize(12, deactivateDataCall);
      }
      j = i;
      if (dataStallAction != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(13, dataStallAction);
      }
      k = j;
      if (modemRestart != null) {
        k = j + CodedOutputByteBufferNano.computeMessageSize(14, modemRestart);
      }
      i = k;
      if (nitzTimestampMillis != 0L) {
        i = k + CodedOutputByteBufferNano.computeInt64Size(15, nitzTimestampMillis);
      }
      j = i;
      if (carrierIdMatching != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(16, carrierIdMatching);
      }
      i = j;
      if (carrierKeyChange != null) {
        i = j + CodedOutputByteBufferNano.computeMessageSize(17, carrierKeyChange);
      }
      return i;
    }
    
    public TelephonyEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        int j;
        int k;
        switch (i)
        {
        default: 
          if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 138: 
          if (carrierKeyChange == null) {
            carrierKeyChange = new CarrierKeyChange();
          }
          paramCodedInputByteBufferNano.readMessage(carrierKeyChange);
          break;
        case 130: 
          if (carrierIdMatching == null) {
            carrierIdMatching = new CarrierIdMatching();
          }
          paramCodedInputByteBufferNano.readMessage(carrierIdMatching);
          break;
        case 120: 
          nitzTimestampMillis = paramCodedInputByteBufferNano.readInt64();
          break;
        case 114: 
          if (modemRestart == null) {
            modemRestart = new ModemRestart();
          }
          paramCodedInputByteBufferNano.readMessage(modemRestart);
          break;
        case 104: 
          dataStallAction = paramCodedInputByteBufferNano.readInt32();
          break;
        case 98: 
          if (deactivateDataCall == null) {
            deactivateDataCall = new RilDeactivateDataCall();
          }
          paramCodedInputByteBufferNano.readMessage(deactivateDataCall);
          break;
        case 90: 
          if (setupDataCallResponse == null) {
            setupDataCallResponse = new RilSetupDataCallResponse();
          }
          paramCodedInputByteBufferNano.readMessage(setupDataCallResponse);
          break;
        case 82: 
          if (setupDataCall == null) {
            setupDataCall = new RilSetupDataCall();
          }
          paramCodedInputByteBufferNano.readMessage(setupDataCall);
          break;
        case 72: 
          j = paramCodedInputByteBufferNano.getPosition();
          k = paramCodedInputByteBufferNano.readInt32();
          switch (k)
          {
          default: 
            switch (k)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(j);
              storeUnknownField(paramCodedInputByteBufferNano, i);
            }
            break;
          case 0: 
          case 1: 
          case 2: 
          case 3: 
          case 4: 
          case 5: 
          case 6: 
          case 7: 
          case 8: 
          case 9: 
          case 10: 
          case 11: 
          case 12: 
          case 13: 
          case 14: 
          case 15: 
          case 16: 
          case 17: 
          case 18: 
          case 19: 
          case 20: 
          case 21: 
          case 22: 
          case 23: 
          case 24: 
          case 25: 
          case 26: 
          case 27: 
          case 28: 
            error = k;
          }
          break;
        case 66: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 66);
          if (dataCalls == null) {
            i = 0;
          } else {
            i = dataCalls.length;
          }
          TelephonyProto.RilDataCall[] arrayOfRilDataCall = new TelephonyProto.RilDataCall[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(dataCalls, 0, arrayOfRilDataCall, 0, i);
          }
          for (j = i; j < arrayOfRilDataCall.length - 1; j++)
          {
            arrayOfRilDataCall[j] = new TelephonyProto.RilDataCall();
            paramCodedInputByteBufferNano.readMessage(arrayOfRilDataCall[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfRilDataCall[j] = new TelephonyProto.RilDataCall();
          paramCodedInputByteBufferNano.readMessage(arrayOfRilDataCall[j]);
          dataCalls = arrayOfRilDataCall;
          break;
        case 58: 
          if (imsCapabilities == null) {
            imsCapabilities = new TelephonyProto.ImsCapabilities();
          }
          paramCodedInputByteBufferNano.readMessage(imsCapabilities);
          break;
        case 50: 
          if (imsConnectionState == null) {
            imsConnectionState = new TelephonyProto.ImsConnectionState();
          }
          paramCodedInputByteBufferNano.readMessage(imsConnectionState);
          break;
        case 42: 
          if (serviceState == null) {
            serviceState = new TelephonyProto.TelephonyServiceState();
          }
          paramCodedInputByteBufferNano.readMessage(serviceState);
          break;
        case 34: 
          if (settings == null) {
            settings = new TelephonyProto.TelephonySettings();
          }
          paramCodedInputByteBufferNano.readMessage(settings);
          break;
        case 24: 
          k = paramCodedInputByteBufferNano.getPosition();
          j = paramCodedInputByteBufferNano.readInt32();
          switch (j)
          {
          default: 
            paramCodedInputByteBufferNano.rewindToPosition(k);
            storeUnknownField(paramCodedInputByteBufferNano, i);
            break;
          case 0: 
          case 1: 
          case 2: 
          case 3: 
          case 4: 
          case 5: 
          case 6: 
          case 7: 
          case 8: 
          case 9: 
          case 10: 
          case 11: 
          case 12: 
          case 13: 
          case 14: 
            type = j;
          }
          break;
        case 16: 
          phoneId = paramCodedInputByteBufferNano.readInt32();
          break;
        case 8: 
          timestampMillis = paramCodedInputByteBufferNano.readInt64();
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (timestampMillis != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(1, timestampMillis);
      }
      if (phoneId != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, phoneId);
      }
      if (type != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, type);
      }
      if (settings != null) {
        paramCodedOutputByteBufferNano.writeMessage(4, settings);
      }
      if (serviceState != null) {
        paramCodedOutputByteBufferNano.writeMessage(5, serviceState);
      }
      if (imsConnectionState != null) {
        paramCodedOutputByteBufferNano.writeMessage(6, imsConnectionState);
      }
      if (imsCapabilities != null) {
        paramCodedOutputByteBufferNano.writeMessage(7, imsCapabilities);
      }
      if ((dataCalls != null) && (dataCalls.length > 0)) {
        for (int i = 0; i < dataCalls.length; i++)
        {
          TelephonyProto.RilDataCall localRilDataCall = dataCalls[i];
          if (localRilDataCall != null) {
            paramCodedOutputByteBufferNano.writeMessage(8, localRilDataCall);
          }
        }
      }
      if (error != 0) {
        paramCodedOutputByteBufferNano.writeInt32(9, error);
      }
      if (setupDataCall != null) {
        paramCodedOutputByteBufferNano.writeMessage(10, setupDataCall);
      }
      if (setupDataCallResponse != null) {
        paramCodedOutputByteBufferNano.writeMessage(11, setupDataCallResponse);
      }
      if (deactivateDataCall != null) {
        paramCodedOutputByteBufferNano.writeMessage(12, deactivateDataCall);
      }
      if (dataStallAction != 0) {
        paramCodedOutputByteBufferNano.writeInt32(13, dataStallAction);
      }
      if (modemRestart != null) {
        paramCodedOutputByteBufferNano.writeMessage(14, modemRestart);
      }
      if (nitzTimestampMillis != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(15, nitzTimestampMillis);
      }
      if (carrierIdMatching != null) {
        paramCodedOutputByteBufferNano.writeMessage(16, carrierIdMatching);
      }
      if (carrierKeyChange != null) {
        paramCodedOutputByteBufferNano.writeMessage(17, carrierKeyChange);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static final class CarrierIdMatching
      extends ExtendableMessageNano<CarrierIdMatching>
    {
      private static volatile CarrierIdMatching[] _emptyArray;
      public int cidTableVersion;
      public TelephonyProto.TelephonyEvent.CarrierIdMatchingResult result;
      
      public CarrierIdMatching()
      {
        clear();
      }
      
      public static CarrierIdMatching[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new CarrierIdMatching[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static CarrierIdMatching parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new CarrierIdMatching().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static CarrierIdMatching parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (CarrierIdMatching)MessageNano.mergeFrom(new CarrierIdMatching(), paramArrayOfByte);
      }
      
      public CarrierIdMatching clear()
      {
        cidTableVersion = 0;
        result = null;
        unknownFieldData = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (cidTableVersion != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, cidTableVersion);
        }
        i = j;
        if (result != null) {
          i = j + CodedOutputByteBufferNano.computeMessageSize(2, result);
        }
        return i;
      }
      
      public CarrierIdMatching mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else
            {
              if (result == null) {
                result = new TelephonyProto.TelephonyEvent.CarrierIdMatchingResult();
              }
              paramCodedInputByteBufferNano.readMessage(result);
            }
          }
          else {
            cidTableVersion = paramCodedInputByteBufferNano.readInt32();
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (cidTableVersion != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, cidTableVersion);
        }
        if (result != null) {
          paramCodedOutputByteBufferNano.writeMessage(2, result);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
    
    public static final class CarrierIdMatchingResult
      extends ExtendableMessageNano<CarrierIdMatchingResult>
    {
      private static volatile CarrierIdMatchingResult[] _emptyArray;
      public int carrierId;
      public String gid1;
      public String mccmnc;
      
      public CarrierIdMatchingResult()
      {
        clear();
      }
      
      public static CarrierIdMatchingResult[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new CarrierIdMatchingResult[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static CarrierIdMatchingResult parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new CarrierIdMatchingResult().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static CarrierIdMatchingResult parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (CarrierIdMatchingResult)MessageNano.mergeFrom(new CarrierIdMatchingResult(), paramArrayOfByte);
      }
      
      public CarrierIdMatchingResult clear()
      {
        carrierId = 0;
        gid1 = "";
        mccmnc = "";
        unknownFieldData = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (carrierId != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, carrierId);
        }
        i = j;
        if (!gid1.equals("")) {
          i = j + CodedOutputByteBufferNano.computeStringSize(2, gid1);
        }
        j = i;
        if (!mccmnc.equals("")) {
          j = i + CodedOutputByteBufferNano.computeStringSize(3, mccmnc);
        }
        return j;
      }
      
      public CarrierIdMatchingResult mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              else {
                mccmnc = paramCodedInputByteBufferNano.readString();
              }
            }
            else {
              gid1 = paramCodedInputByteBufferNano.readString();
            }
          }
          else {
            carrierId = paramCodedInputByteBufferNano.readInt32();
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (carrierId != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, carrierId);
        }
        if (!gid1.equals("")) {
          paramCodedOutputByteBufferNano.writeString(2, gid1);
        }
        if (!mccmnc.equals("")) {
          paramCodedOutputByteBufferNano.writeString(3, mccmnc);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
    
    public static final class CarrierKeyChange
      extends ExtendableMessageNano<CarrierKeyChange>
    {
      private static volatile CarrierKeyChange[] _emptyArray;
      public boolean isDownloadSuccessful;
      public int keyType;
      
      public CarrierKeyChange()
      {
        clear();
      }
      
      public static CarrierKeyChange[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new CarrierKeyChange[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static CarrierKeyChange parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new CarrierKeyChange().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static CarrierKeyChange parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (CarrierKeyChange)MessageNano.mergeFrom(new CarrierKeyChange(), paramArrayOfByte);
      }
      
      public CarrierKeyChange clear()
      {
        keyType = 0;
        isDownloadSuccessful = false;
        unknownFieldData = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (keyType != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, keyType);
        }
        i = j;
        if (isDownloadSuccessful) {
          i = j + CodedOutputByteBufferNano.computeBoolSize(2, isDownloadSuccessful);
        }
        return i;
      }
      
      public CarrierKeyChange mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 16)
            {
              if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else {
              isDownloadSuccessful = paramCodedInputByteBufferNano.readBool();
            }
          }
          else
          {
            int j = paramCodedInputByteBufferNano.getPosition();
            int k = paramCodedInputByteBufferNano.readInt32();
            switch (k)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(j);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case 0: 
            case 1: 
            case 2: 
              keyType = k;
            }
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (keyType != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, keyType);
        }
        if (isDownloadSuccessful) {
          paramCodedOutputByteBufferNano.writeBool(2, isDownloadSuccessful);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
      
      public static abstract interface KeyType
      {
        public static final int EPDG = 2;
        public static final int UNKNOWN = 0;
        public static final int WLAN = 1;
      }
    }
    
    public static final class ModemRestart
      extends ExtendableMessageNano<ModemRestart>
    {
      private static volatile ModemRestart[] _emptyArray;
      public String basebandVersion;
      public String reason;
      
      public ModemRestart()
      {
        clear();
      }
      
      public static ModemRestart[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new ModemRestart[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static ModemRestart parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new ModemRestart().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static ModemRestart parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (ModemRestart)MessageNano.mergeFrom(new ModemRestart(), paramArrayOfByte);
      }
      
      public ModemRestart clear()
      {
        basebandVersion = "";
        reason = "";
        unknownFieldData = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (!basebandVersion.equals("")) {
          j = i + CodedOutputByteBufferNano.computeStringSize(1, basebandVersion);
        }
        i = j;
        if (!reason.equals("")) {
          i = j + CodedOutputByteBufferNano.computeStringSize(2, reason);
        }
        return i;
      }
      
      public ModemRestart mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 18)
            {
              if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else {
              reason = paramCodedInputByteBufferNano.readString();
            }
          }
          else {
            basebandVersion = paramCodedInputByteBufferNano.readString();
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (!basebandVersion.equals("")) {
          paramCodedOutputByteBufferNano.writeString(1, basebandVersion);
        }
        if (!reason.equals("")) {
          paramCodedOutputByteBufferNano.writeString(2, reason);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
    
    public static final class RilDeactivateDataCall
      extends ExtendableMessageNano<RilDeactivateDataCall>
    {
      private static volatile RilDeactivateDataCall[] _emptyArray;
      public int cid;
      public int reason;
      
      public RilDeactivateDataCall()
      {
        clear();
      }
      
      public static RilDeactivateDataCall[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new RilDeactivateDataCall[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static RilDeactivateDataCall parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new RilDeactivateDataCall().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static RilDeactivateDataCall parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (RilDeactivateDataCall)MessageNano.mergeFrom(new RilDeactivateDataCall(), paramArrayOfByte);
      }
      
      public RilDeactivateDataCall clear()
      {
        cid = 0;
        reason = 0;
        unknownFieldData = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (cid != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, cid);
        }
        i = j;
        if (reason != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, reason);
        }
        return i;
      }
      
      public RilDeactivateDataCall mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 16)
            {
              if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else
            {
              int j = paramCodedInputByteBufferNano.getPosition();
              int k = paramCodedInputByteBufferNano.readInt32();
              switch (k)
              {
              default: 
                paramCodedInputByteBufferNano.rewindToPosition(j);
                storeUnknownField(paramCodedInputByteBufferNano, i);
                break;
              case 0: 
              case 1: 
              case 2: 
              case 3: 
              case 4: 
                reason = k;
                break;
              }
            }
          }
          else {
            cid = paramCodedInputByteBufferNano.readInt32();
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (cid != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, cid);
        }
        if (reason != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, reason);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
      
      public static abstract interface DeactivateReason
      {
        public static final int DEACTIVATE_REASON_HANDOVER = 4;
        public static final int DEACTIVATE_REASON_NONE = 1;
        public static final int DEACTIVATE_REASON_PDP_RESET = 3;
        public static final int DEACTIVATE_REASON_RADIO_OFF = 2;
        public static final int DEACTIVATE_REASON_UNKNOWN = 0;
      }
    }
    
    public static final class RilSetupDataCall
      extends ExtendableMessageNano<RilSetupDataCall>
    {
      private static volatile RilSetupDataCall[] _emptyArray;
      public String apn;
      public int dataProfile;
      public int rat;
      public int type;
      
      public RilSetupDataCall()
      {
        clear();
      }
      
      public static RilSetupDataCall[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new RilSetupDataCall[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static RilSetupDataCall parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new RilSetupDataCall().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static RilSetupDataCall parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (RilSetupDataCall)MessageNano.mergeFrom(new RilSetupDataCall(), paramArrayOfByte);
      }
      
      public RilSetupDataCall clear()
      {
        rat = -1;
        dataProfile = 0;
        apn = "";
        type = 0;
        unknownFieldData = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (rat != -1) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, rat);
        }
        i = j;
        if (dataProfile != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, dataProfile);
        }
        j = i;
        if (!apn.equals("")) {
          j = i + CodedOutputByteBufferNano.computeStringSize(3, apn);
        }
        i = j;
        if (type != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(4, type);
        }
        return i;
      }
      
      public RilSetupDataCall mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        for (;;)
        {
          int i = paramCodedInputByteBufferNano.readTag();
          if (i == 0) {
            break;
          }
          int j;
          int k;
          if (i != 8)
          {
            if (i != 16)
            {
              if (i != 26)
              {
                if (i != 32)
                {
                  if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                    return this;
                  }
                }
                else
                {
                  j = paramCodedInputByteBufferNano.getPosition();
                  k = paramCodedInputByteBufferNano.readInt32();
                  switch (k)
                  {
                  default: 
                    paramCodedInputByteBufferNano.rewindToPosition(j);
                    storeUnknownField(paramCodedInputByteBufferNano, i);
                    break;
                  case 0: 
                  case 1: 
                  case 2: 
                  case 3: 
                  case 4: 
                    type = k;
                    break;
                  }
                }
              }
              else {
                apn = paramCodedInputByteBufferNano.readString();
              }
            }
            else
            {
              k = paramCodedInputByteBufferNano.getPosition();
              j = paramCodedInputByteBufferNano.readInt32();
              switch (j)
              {
              default: 
                paramCodedInputByteBufferNano.rewindToPosition(k);
                storeUnknownField(paramCodedInputByteBufferNano, i);
                break;
              case 0: 
              case 1: 
              case 2: 
              case 3: 
              case 4: 
              case 5: 
              case 6: 
              case 7: 
                dataProfile = j;
                break;
              }
            }
          }
          else
          {
            k = paramCodedInputByteBufferNano.getPosition();
            j = paramCodedInputByteBufferNano.readInt32();
            switch (j)
            {
            default: 
              paramCodedInputByteBufferNano.rewindToPosition(k);
              storeUnknownField(paramCodedInputByteBufferNano, i);
              break;
            case -1: 
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
              rat = j;
            }
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (rat != -1) {
          paramCodedOutputByteBufferNano.writeInt32(1, rat);
        }
        if (dataProfile != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, dataProfile);
        }
        if (!apn.equals("")) {
          paramCodedOutputByteBufferNano.writeString(3, apn);
        }
        if (type != 0) {
          paramCodedOutputByteBufferNano.writeInt32(4, type);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
      
      public static abstract interface RilDataProfile
      {
        public static final int RIL_DATA_PROFILE_CBS = 5;
        public static final int RIL_DATA_PROFILE_DEFAULT = 1;
        public static final int RIL_DATA_PROFILE_FOTA = 4;
        public static final int RIL_DATA_PROFILE_IMS = 3;
        public static final int RIL_DATA_PROFILE_INVALID = 7;
        public static final int RIL_DATA_PROFILE_OEM_BASE = 6;
        public static final int RIL_DATA_PROFILE_TETHERED = 2;
        public static final int RIL_DATA_UNKNOWN = 0;
      }
    }
    
    public static final class RilSetupDataCallResponse
      extends ExtendableMessageNano<RilSetupDataCallResponse>
    {
      private static volatile RilSetupDataCallResponse[] _emptyArray;
      public TelephonyProto.RilDataCall call;
      public int status;
      public int suggestedRetryTimeMillis;
      
      public RilSetupDataCallResponse()
      {
        clear();
      }
      
      public static RilSetupDataCallResponse[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new RilSetupDataCallResponse[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static RilSetupDataCallResponse parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new RilSetupDataCallResponse().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static RilSetupDataCallResponse parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (RilSetupDataCallResponse)MessageNano.mergeFrom(new RilSetupDataCallResponse(), paramArrayOfByte);
      }
      
      public RilSetupDataCallResponse clear()
      {
        status = 0;
        suggestedRetryTimeMillis = 0;
        call = null;
        unknownFieldData = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (status != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, status);
        }
        i = j;
        if (suggestedRetryTimeMillis != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, suggestedRetryTimeMillis);
        }
        j = i;
        if (call != null) {
          j = i + CodedOutputByteBufferNano.computeMessageSize(3, call);
        }
        return j;
      }
      
      public RilSetupDataCallResponse mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 16)
            {
              if (i != 26)
              {
                if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else
              {
                if (call == null) {
                  call = new TelephonyProto.RilDataCall();
                }
                paramCodedInputByteBufferNano.readMessage(call);
              }
            }
            else {
              suggestedRetryTimeMillis = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else
          {
            int j = paramCodedInputByteBufferNano.getPosition();
            int k = paramCodedInputByteBufferNano.readInt32();
            if ((k != 8) && (k != 14) && (k != 81) && (k != 65535)) {}
            switch (k)
            {
            default: 
              switch (k)
              {
              default: 
                switch (k)
                {
                default: 
                  switch (k)
                  {
                  default: 
                    switch (k)
                    {
                    default: 
                      switch (k)
                      {
                      default: 
                        paramCodedInputByteBufferNano.rewindToPosition(j);
                        storeUnknownField(paramCodedInputByteBufferNano, i);
                      }
                      break;
                    }
                    break;
                  }
                  break;
                }
                break;
              }
              break;
            case -6: 
            case -5: 
            case -4: 
            case -3: 
            case -2: 
            case -1: 
            case 0: 
            case 1: 
              status = k;
            }
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (status != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, status);
        }
        if (suggestedRetryTimeMillis != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, suggestedRetryTimeMillis);
        }
        if (call != null) {
          paramCodedOutputByteBufferNano.writeMessage(3, call);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
      
      public static abstract interface RilDataCallFailCause
      {
        public static final int PDP_FAIL_ACTIVATION_REJECT_GGSN = 30;
        public static final int PDP_FAIL_ACTIVATION_REJECT_UNSPECIFIED = 31;
        public static final int PDP_FAIL_APN_TYPE_CONFLICT = 112;
        public static final int PDP_FAIL_AUTH_FAILURE_ON_EMERGENCY_CALL = 122;
        public static final int PDP_FAIL_COMPANION_IFACE_IN_USE = 118;
        public static final int PDP_FAIL_CONDITIONAL_IE_ERROR = 100;
        public static final int PDP_FAIL_DATA_REGISTRATION_FAIL = -2;
        public static final int PDP_FAIL_EMERGENCY_IFACE_ONLY = 116;
        public static final int PDP_FAIL_EMM_ACCESS_BARRED = 115;
        public static final int PDP_FAIL_EMM_ACCESS_BARRED_INFINITE_RETRY = 121;
        public static final int PDP_FAIL_ERROR_UNSPECIFIED = 65535;
        public static final int PDP_FAIL_ESM_INFO_NOT_RECEIVED = 53;
        public static final int PDP_FAIL_FEATURE_NOT_SUPP = 40;
        public static final int PDP_FAIL_FILTER_SEMANTIC_ERROR = 44;
        public static final int PDP_FAIL_FILTER_SYTAX_ERROR = 45;
        public static final int PDP_FAIL_IFACE_AND_POL_FAMILY_MISMATCH = 120;
        public static final int PDP_FAIL_IFACE_MISMATCH = 117;
        public static final int PDP_FAIL_INSUFFICIENT_RESOURCES = 26;
        public static final int PDP_FAIL_INTERNAL_CALL_PREEMPT_BY_HIGH_PRIO_APN = 114;
        public static final int PDP_FAIL_INVALID_MANDATORY_INFO = 96;
        public static final int PDP_FAIL_INVALID_PCSCF_ADDR = 113;
        public static final int PDP_FAIL_INVALID_TRANSACTION_ID = 81;
        public static final int PDP_FAIL_IP_ADDRESS_MISMATCH = 119;
        public static final int PDP_FAIL_LLC_SNDCP = 25;
        public static final int PDP_FAIL_MAX_ACTIVE_PDP_CONTEXT_REACHED = 65;
        public static final int PDP_FAIL_MESSAGE_INCORRECT_SEMANTIC = 95;
        public static final int PDP_FAIL_MESSAGE_TYPE_UNSUPPORTED = 97;
        public static final int PDP_FAIL_MISSING_UKNOWN_APN = 27;
        public static final int PDP_FAIL_MSG_AND_PROTOCOL_STATE_UNCOMPATIBLE = 101;
        public static final int PDP_FAIL_MSG_TYPE_NONCOMPATIBLE_STATE = 98;
        public static final int PDP_FAIL_MULTI_CONN_TO_SAME_PDN_NOT_ALLOWED = 55;
        public static final int PDP_FAIL_NAS_SIGNALLING = 14;
        public static final int PDP_FAIL_NETWORK_FAILURE = 38;
        public static final int PDP_FAIL_NONE = 1;
        public static final int PDP_FAIL_NSAPI_IN_USE = 35;
        public static final int PDP_FAIL_ONLY_IPV4_ALLOWED = 50;
        public static final int PDP_FAIL_ONLY_IPV6_ALLOWED = 51;
        public static final int PDP_FAIL_ONLY_SINGLE_BEARER_ALLOWED = 52;
        public static final int PDP_FAIL_OPERATOR_BARRED = 8;
        public static final int PDP_FAIL_PDN_CONN_DOES_NOT_EXIST = 54;
        public static final int PDP_FAIL_PDP_WITHOUT_ACTIVE_TFT = 46;
        public static final int PDP_FAIL_PREF_RADIO_TECH_CHANGED = -4;
        public static final int PDP_FAIL_PROTOCOL_ERRORS = 111;
        public static final int PDP_FAIL_QOS_NOT_ACCEPTED = 37;
        public static final int PDP_FAIL_RADIO_POWER_OFF = -5;
        public static final int PDP_FAIL_REGULAR_DEACTIVATION = 36;
        public static final int PDP_FAIL_SERVICE_OPTION_NOT_SUBSCRIBED = 33;
        public static final int PDP_FAIL_SERVICE_OPTION_NOT_SUPPORTED = 32;
        public static final int PDP_FAIL_SERVICE_OPTION_OUT_OF_ORDER = 34;
        public static final int PDP_FAIL_SIGNAL_LOST = -3;
        public static final int PDP_FAIL_TETHERED_CALL_ACTIVE = -6;
        public static final int PDP_FAIL_TFT_SEMANTIC_ERROR = 41;
        public static final int PDP_FAIL_TFT_SYTAX_ERROR = 42;
        public static final int PDP_FAIL_UMTS_REACTIVATION_REQ = 39;
        public static final int PDP_FAIL_UNKNOWN = 0;
        public static final int PDP_FAIL_UNKNOWN_INFO_ELEMENT = 99;
        public static final int PDP_FAIL_UNKNOWN_PDP_ADDRESS_TYPE = 28;
        public static final int PDP_FAIL_UNKNOWN_PDP_CONTEXT = 43;
        public static final int PDP_FAIL_UNSUPPORTED_APN_IN_CURRENT_PLMN = 66;
        public static final int PDP_FAIL_USER_AUTHENTICATION = 29;
        public static final int PDP_FAIL_VOICE_REGISTRATION_FAIL = -1;
      }
    }
    
    public static abstract interface Type
    {
      public static final int CARRIER_ID_MATCHING = 13;
      public static final int CARRIER_KEY_CHANGED = 14;
      public static final int DATA_CALL_DEACTIVATE = 8;
      public static final int DATA_CALL_DEACTIVATE_RESPONSE = 9;
      public static final int DATA_CALL_LIST_CHANGED = 7;
      public static final int DATA_CALL_SETUP = 5;
      public static final int DATA_CALL_SETUP_RESPONSE = 6;
      public static final int DATA_STALL_ACTION = 10;
      public static final int IMS_CAPABILITIES_CHANGED = 4;
      public static final int IMS_CONNECTION_STATE_CHANGED = 3;
      public static final int MODEM_RESTART = 11;
      public static final int NITZ_TIME = 12;
      public static final int RIL_SERVICE_STATE_CHANGED = 2;
      public static final int SETTINGS_CHANGED = 1;
      public static final int UNKNOWN = 0;
    }
  }
  
  public static final class TelephonyHistogram
    extends ExtendableMessageNano<TelephonyHistogram>
  {
    private static volatile TelephonyHistogram[] _emptyArray;
    public int avgTimeMillis;
    public int bucketCount;
    public int[] bucketCounters;
    public int[] bucketEndPoints;
    public int category;
    public int count;
    public int id;
    public int maxTimeMillis;
    public int minTimeMillis;
    
    public TelephonyHistogram()
    {
      clear();
    }
    
    public static TelephonyHistogram[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new TelephonyHistogram[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static TelephonyHistogram parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new TelephonyHistogram().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static TelephonyHistogram parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (TelephonyHistogram)MessageNano.mergeFrom(new TelephonyHistogram(), paramArrayOfByte);
    }
    
    public TelephonyHistogram clear()
    {
      category = 0;
      id = 0;
      minTimeMillis = 0;
      maxTimeMillis = 0;
      avgTimeMillis = 0;
      count = 0;
      bucketCount = 0;
      bucketEndPoints = WireFormatNano.EMPTY_INT_ARRAY;
      bucketCounters = WireFormatNano.EMPTY_INT_ARRAY;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (category != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, category);
      }
      i = j;
      if (id != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, id);
      }
      j = i;
      if (minTimeMillis != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, minTimeMillis);
      }
      i = j;
      if (maxTimeMillis != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, maxTimeMillis);
      }
      j = i;
      if (avgTimeMillis != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(5, avgTimeMillis);
      }
      i = j;
      if (count != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(6, count);
      }
      j = i;
      if (bucketCount != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(7, bucketCount);
      }
      int[] arrayOfInt = bucketEndPoints;
      int k = 0;
      i = j;
      int m;
      if (arrayOfInt != null)
      {
        i = j;
        if (bucketEndPoints.length > 0)
        {
          m = 0;
          for (i = 0; i < bucketEndPoints.length; i++)
          {
            int n = bucketEndPoints[i];
            m += CodedOutputByteBufferNano.computeInt32SizeNoTag(n);
          }
          i = j + m + bucketEndPoints.length * 1;
        }
      }
      j = i;
      if (bucketCounters != null)
      {
        j = i;
        if (bucketCounters.length > 0)
        {
          m = 0;
          for (j = k; j < bucketCounters.length; j++)
          {
            k = bucketCounters[j];
            m += CodedOutputByteBufferNano.computeInt32SizeNoTag(k);
          }
          j = i + m + 1 * bucketCounters.length;
        }
      }
      return j;
    }
    
    public TelephonyHistogram mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        int j;
        int k;
        int[] arrayOfInt;
        switch (i)
        {
        default: 
          if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 74: 
          j = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
          k = 0;
          i = paramCodedInputByteBufferNano.getPosition();
          while (paramCodedInputByteBufferNano.getBytesUntilLimit() > 0)
          {
            paramCodedInputByteBufferNano.readInt32();
            k++;
          }
          paramCodedInputByteBufferNano.rewindToPosition(i);
          if (bucketCounters == null) {
            i = 0;
          } else {
            i = bucketCounters.length;
          }
          arrayOfInt = new int[i + k];
          k = i;
          if (i != 0) {
            System.arraycopy(bucketCounters, 0, arrayOfInt, 0, i);
          }
          for (k = i; k < arrayOfInt.length; k++) {
            arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
          }
          bucketCounters = arrayOfInt;
          paramCodedInputByteBufferNano.popLimit(j);
          break;
        case 72: 
          k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 72);
          if (bucketCounters == null) {
            i = 0;
          } else {
            i = bucketCounters.length;
          }
          arrayOfInt = new int[i + k];
          k = i;
          if (i != 0) {
            System.arraycopy(bucketCounters, 0, arrayOfInt, 0, i);
          }
          for (k = i; k < arrayOfInt.length - 1; k++)
          {
            arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
          bucketCounters = arrayOfInt;
          break;
        case 66: 
          j = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
          k = 0;
          i = paramCodedInputByteBufferNano.getPosition();
          while (paramCodedInputByteBufferNano.getBytesUntilLimit() > 0)
          {
            paramCodedInputByteBufferNano.readInt32();
            k++;
          }
          paramCodedInputByteBufferNano.rewindToPosition(i);
          if (bucketEndPoints == null) {
            i = 0;
          } else {
            i = bucketEndPoints.length;
          }
          arrayOfInt = new int[i + k];
          k = i;
          if (i != 0) {
            System.arraycopy(bucketEndPoints, 0, arrayOfInt, 0, i);
          }
          for (k = i; k < arrayOfInt.length; k++) {
            arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
          }
          bucketEndPoints = arrayOfInt;
          paramCodedInputByteBufferNano.popLimit(j);
          break;
        case 64: 
          k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 64);
          if (bucketEndPoints == null) {
            i = 0;
          } else {
            i = bucketEndPoints.length;
          }
          arrayOfInt = new int[i + k];
          k = i;
          if (i != 0) {
            System.arraycopy(bucketEndPoints, 0, arrayOfInt, 0, i);
          }
          for (k = i; k < arrayOfInt.length - 1; k++)
          {
            arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
          bucketEndPoints = arrayOfInt;
          break;
        case 56: 
          bucketCount = paramCodedInputByteBufferNano.readInt32();
          break;
        case 48: 
          count = paramCodedInputByteBufferNano.readInt32();
          break;
        case 40: 
          avgTimeMillis = paramCodedInputByteBufferNano.readInt32();
          break;
        case 32: 
          maxTimeMillis = paramCodedInputByteBufferNano.readInt32();
          break;
        case 24: 
          minTimeMillis = paramCodedInputByteBufferNano.readInt32();
          break;
        case 16: 
          id = paramCodedInputByteBufferNano.readInt32();
          break;
        case 8: 
          category = paramCodedInputByteBufferNano.readInt32();
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (category != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, category);
      }
      if (id != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, id);
      }
      if (minTimeMillis != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, minTimeMillis);
      }
      if (maxTimeMillis != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, maxTimeMillis);
      }
      if (avgTimeMillis != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, avgTimeMillis);
      }
      if (count != 0) {
        paramCodedOutputByteBufferNano.writeInt32(6, count);
      }
      if (bucketCount != 0) {
        paramCodedOutputByteBufferNano.writeInt32(7, bucketCount);
      }
      int[] arrayOfInt = bucketEndPoints;
      int i = 0;
      int j;
      if ((arrayOfInt != null) && (bucketEndPoints.length > 0)) {
        for (j = 0; j < bucketEndPoints.length; j++) {
          paramCodedOutputByteBufferNano.writeInt32(8, bucketEndPoints[j]);
        }
      }
      if ((bucketCounters != null) && (bucketCounters.length > 0)) {
        for (j = i; j < bucketCounters.length; j++) {
          paramCodedOutputByteBufferNano.writeInt32(9, bucketCounters[j]);
        }
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class TelephonyLog
    extends ExtendableMessageNano<TelephonyLog>
  {
    private static volatile TelephonyLog[] _emptyArray;
    public TelephonyProto.TelephonyCallSession[] callSessions;
    public TelephonyProto.Time endTime;
    public TelephonyProto.TelephonyEvent[] events;
    public boolean eventsDropped;
    public TelephonyProto.TelephonyHistogram[] histograms;
    public TelephonyProto.ModemPowerStats modemPowerStats;
    public TelephonyProto.SmsSession[] smsSessions;
    public TelephonyProto.Time startTime;
    
    public TelephonyLog()
    {
      clear();
    }
    
    public static TelephonyLog[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new TelephonyLog[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static TelephonyLog parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new TelephonyLog().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static TelephonyLog parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (TelephonyLog)MessageNano.mergeFrom(new TelephonyLog(), paramArrayOfByte);
    }
    
    public TelephonyLog clear()
    {
      events = TelephonyProto.TelephonyEvent.emptyArray();
      callSessions = TelephonyProto.TelephonyCallSession.emptyArray();
      smsSessions = TelephonyProto.SmsSession.emptyArray();
      histograms = TelephonyProto.TelephonyHistogram.emptyArray();
      eventsDropped = false;
      startTime = null;
      endTime = null;
      modemPowerStats = null;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      Object localObject = events;
      int j = 0;
      int k = i;
      if (localObject != null)
      {
        k = i;
        if (events.length > 0)
        {
          k = i;
          m = 0;
          while (m < events.length)
          {
            localObject = events[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(1, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      i = k;
      if (callSessions != null)
      {
        i = k;
        if (callSessions.length > 0)
        {
          i = 0;
          while (i < callSessions.length)
          {
            localObject = callSessions[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(2, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      k = i;
      if (smsSessions != null)
      {
        k = i;
        if (smsSessions.length > 0)
        {
          k = i;
          m = 0;
          while (m < smsSessions.length)
          {
            localObject = smsSessions[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(3, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      i = k;
      if (histograms != null)
      {
        i = k;
        if (histograms.length > 0)
        {
          m = j;
          for (;;)
          {
            i = k;
            if (m >= histograms.length) {
              break;
            }
            localObject = histograms[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(4, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      int m = i;
      if (eventsDropped) {
        m = i + CodedOutputByteBufferNano.computeBoolSize(5, eventsDropped);
      }
      k = m;
      if (startTime != null) {
        k = m + CodedOutputByteBufferNano.computeMessageSize(6, startTime);
      }
      i = k;
      if (endTime != null) {
        i = k + CodedOutputByteBufferNano.computeMessageSize(7, endTime);
      }
      k = i;
      if (modemPowerStats != null) {
        k = i + CodedOutputByteBufferNano.computeMessageSize(8, modemPowerStats);
      }
      return k;
    }
    
    public TelephonyLog mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        if (i == 0) {
          break;
        }
        int j;
        Object localObject;
        if (i != 10)
        {
          if (i != 18)
          {
            if (i != 26)
            {
              if (i != 34)
              {
                if (i != 40)
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
                        if (modemPowerStats == null) {
                          modemPowerStats = new TelephonyProto.ModemPowerStats();
                        }
                        paramCodedInputByteBufferNano.readMessage(modemPowerStats);
                      }
                    }
                    else
                    {
                      if (endTime == null) {
                        endTime = new TelephonyProto.Time();
                      }
                      paramCodedInputByteBufferNano.readMessage(endTime);
                    }
                  }
                  else
                  {
                    if (startTime == null) {
                      startTime = new TelephonyProto.Time();
                    }
                    paramCodedInputByteBufferNano.readMessage(startTime);
                  }
                }
                else {
                  eventsDropped = paramCodedInputByteBufferNano.readBool();
                }
              }
              else
              {
                j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 34);
                if (histograms == null) {
                  i = 0;
                } else {
                  i = histograms.length;
                }
                localObject = new TelephonyProto.TelephonyHistogram[i + j];
                j = i;
                if (i != 0) {
                  System.arraycopy(histograms, 0, localObject, 0, i);
                }
                for (j = i; j < localObject.length - 1; j++)
                {
                  localObject[j] = new TelephonyProto.TelephonyHistogram();
                  paramCodedInputByteBufferNano.readMessage(localObject[j]);
                  paramCodedInputByteBufferNano.readTag();
                }
                localObject[j] = new TelephonyProto.TelephonyHistogram();
                paramCodedInputByteBufferNano.readMessage(localObject[j]);
                histograms = ((TelephonyProto.TelephonyHistogram[])localObject);
              }
            }
            else
            {
              j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 26);
              if (smsSessions == null) {
                i = 0;
              } else {
                i = smsSessions.length;
              }
              localObject = new TelephonyProto.SmsSession[i + j];
              j = i;
              if (i != 0) {
                System.arraycopy(smsSessions, 0, localObject, 0, i);
              }
              for (j = i; j < localObject.length - 1; j++)
              {
                localObject[j] = new TelephonyProto.SmsSession();
                paramCodedInputByteBufferNano.readMessage(localObject[j]);
                paramCodedInputByteBufferNano.readTag();
              }
              localObject[j] = new TelephonyProto.SmsSession();
              paramCodedInputByteBufferNano.readMessage(localObject[j]);
              smsSessions = ((TelephonyProto.SmsSession[])localObject);
            }
          }
          else
          {
            j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 18);
            if (callSessions == null) {
              i = 0;
            } else {
              i = callSessions.length;
            }
            localObject = new TelephonyProto.TelephonyCallSession[i + j];
            j = i;
            if (i != 0) {
              System.arraycopy(callSessions, 0, localObject, 0, i);
            }
            for (j = i; j < localObject.length - 1; j++)
            {
              localObject[j] = new TelephonyProto.TelephonyCallSession();
              paramCodedInputByteBufferNano.readMessage(localObject[j]);
              paramCodedInputByteBufferNano.readTag();
            }
            localObject[j] = new TelephonyProto.TelephonyCallSession();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            callSessions = ((TelephonyProto.TelephonyCallSession[])localObject);
          }
        }
        else
        {
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 10);
          if (events == null) {
            i = 0;
          } else {
            i = events.length;
          }
          localObject = new TelephonyProto.TelephonyEvent[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(events, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new TelephonyProto.TelephonyEvent();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new TelephonyProto.TelephonyEvent();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          events = ((TelephonyProto.TelephonyEvent[])localObject);
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      Object localObject = events;
      int i = 0;
      int j;
      if ((localObject != null) && (events.length > 0)) {
        for (j = 0; j < events.length; j++)
        {
          localObject = events[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(1, (MessageNano)localObject);
          }
        }
      }
      if ((callSessions != null) && (callSessions.length > 0)) {
        for (j = 0; j < callSessions.length; j++)
        {
          localObject = callSessions[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(2, (MessageNano)localObject);
          }
        }
      }
      if ((smsSessions != null) && (smsSessions.length > 0)) {
        for (j = 0; j < smsSessions.length; j++)
        {
          localObject = smsSessions[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(3, (MessageNano)localObject);
          }
        }
      }
      if ((histograms != null) && (histograms.length > 0)) {
        for (j = i; j < histograms.length; j++)
        {
          localObject = histograms[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(4, (MessageNano)localObject);
          }
        }
      }
      if (eventsDropped) {
        paramCodedOutputByteBufferNano.writeBool(5, eventsDropped);
      }
      if (startTime != null) {
        paramCodedOutputByteBufferNano.writeMessage(6, startTime);
      }
      if (endTime != null) {
        paramCodedOutputByteBufferNano.writeMessage(7, endTime);
      }
      if (modemPowerStats != null) {
        paramCodedOutputByteBufferNano.writeMessage(8, modemPowerStats);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class TelephonyServiceState
    extends ExtendableMessageNano<TelephonyServiceState>
  {
    private static volatile TelephonyServiceState[] _emptyArray;
    public TelephonyOperator dataOperator;
    public int dataRat;
    public int dataRoamingType;
    public TelephonyOperator voiceOperator;
    public int voiceRat;
    public int voiceRoamingType;
    
    public TelephonyServiceState()
    {
      clear();
    }
    
    public static TelephonyServiceState[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new TelephonyServiceState[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static TelephonyServiceState parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new TelephonyServiceState().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static TelephonyServiceState parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (TelephonyServiceState)MessageNano.mergeFrom(new TelephonyServiceState(), paramArrayOfByte);
    }
    
    public TelephonyServiceState clear()
    {
      voiceOperator = null;
      dataOperator = null;
      voiceRoamingType = -1;
      dataRoamingType = -1;
      voiceRat = -1;
      dataRat = -1;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (voiceOperator != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(1, voiceOperator);
      }
      i = j;
      if (dataOperator != null) {
        i = j + CodedOutputByteBufferNano.computeMessageSize(2, dataOperator);
      }
      j = i;
      if (voiceRoamingType != -1) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, voiceRoamingType);
      }
      i = j;
      if (dataRoamingType != -1) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, dataRoamingType);
      }
      j = i;
      if (voiceRat != -1) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(5, voiceRat);
      }
      i = j;
      if (dataRat != -1) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(6, dataRat);
      }
      return i;
    }
    
    public TelephonyServiceState mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
          if (i != 18)
          {
            int j;
            int k;
            if (i != 24)
            {
              if (i != 32)
              {
                if (i != 40)
                {
                  if (i != 48)
                  {
                    if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                      return this;
                    }
                  }
                  else
                  {
                    j = paramCodedInputByteBufferNano.getPosition();
                    k = paramCodedInputByteBufferNano.readInt32();
                    switch (k)
                    {
                    default: 
                      paramCodedInputByteBufferNano.rewindToPosition(j);
                      storeUnknownField(paramCodedInputByteBufferNano, i);
                      break;
                    case -1: 
                    case 0: 
                    case 1: 
                    case 2: 
                    case 3: 
                    case 4: 
                    case 5: 
                    case 6: 
                    case 7: 
                    case 8: 
                    case 9: 
                    case 10: 
                    case 11: 
                    case 12: 
                    case 13: 
                    case 14: 
                    case 15: 
                    case 16: 
                    case 17: 
                    case 18: 
                    case 19: 
                      dataRat = k;
                      break;
                    }
                  }
                }
                else
                {
                  k = paramCodedInputByteBufferNano.getPosition();
                  j = paramCodedInputByteBufferNano.readInt32();
                  switch (j)
                  {
                  default: 
                    paramCodedInputByteBufferNano.rewindToPosition(k);
                    storeUnknownField(paramCodedInputByteBufferNano, i);
                    break;
                  case -1: 
                  case 0: 
                  case 1: 
                  case 2: 
                  case 3: 
                  case 4: 
                  case 5: 
                  case 6: 
                  case 7: 
                  case 8: 
                  case 9: 
                  case 10: 
                  case 11: 
                  case 12: 
                  case 13: 
                  case 14: 
                  case 15: 
                  case 16: 
                  case 17: 
                  case 18: 
                  case 19: 
                    voiceRat = j;
                    break;
                  }
                }
              }
              else
              {
                j = paramCodedInputByteBufferNano.getPosition();
                k = paramCodedInputByteBufferNano.readInt32();
                switch (k)
                {
                default: 
                  paramCodedInputByteBufferNano.rewindToPosition(j);
                  storeUnknownField(paramCodedInputByteBufferNano, i);
                  break;
                case -1: 
                case 0: 
                case 1: 
                case 2: 
                case 3: 
                  dataRoamingType = k;
                  break;
                }
              }
            }
            else
            {
              k = paramCodedInputByteBufferNano.getPosition();
              j = paramCodedInputByteBufferNano.readInt32();
              switch (j)
              {
              default: 
                paramCodedInputByteBufferNano.rewindToPosition(k);
                storeUnknownField(paramCodedInputByteBufferNano, i);
                break;
              case -1: 
              case 0: 
              case 1: 
              case 2: 
              case 3: 
                voiceRoamingType = j;
                break;
              }
            }
          }
          else
          {
            if (dataOperator == null) {
              dataOperator = new TelephonyOperator();
            }
            paramCodedInputByteBufferNano.readMessage(dataOperator);
          }
        }
        else
        {
          if (voiceOperator == null) {
            voiceOperator = new TelephonyOperator();
          }
          paramCodedInputByteBufferNano.readMessage(voiceOperator);
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (voiceOperator != null) {
        paramCodedOutputByteBufferNano.writeMessage(1, voiceOperator);
      }
      if (dataOperator != null) {
        paramCodedOutputByteBufferNano.writeMessage(2, dataOperator);
      }
      if (voiceRoamingType != -1) {
        paramCodedOutputByteBufferNano.writeInt32(3, voiceRoamingType);
      }
      if (dataRoamingType != -1) {
        paramCodedOutputByteBufferNano.writeInt32(4, dataRoamingType);
      }
      if (voiceRat != -1) {
        paramCodedOutputByteBufferNano.writeInt32(5, voiceRat);
      }
      if (dataRat != -1) {
        paramCodedOutputByteBufferNano.writeInt32(6, dataRat);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static abstract interface RoamingType
    {
      public static final int ROAMING_TYPE_DOMESTIC = 2;
      public static final int ROAMING_TYPE_INTERNATIONAL = 3;
      public static final int ROAMING_TYPE_NOT_ROAMING = 0;
      public static final int ROAMING_TYPE_UNKNOWN = 1;
      public static final int UNKNOWN = -1;
    }
    
    public static final class TelephonyOperator
      extends ExtendableMessageNano<TelephonyOperator>
    {
      private static volatile TelephonyOperator[] _emptyArray;
      public String alphaLong;
      public String alphaShort;
      public String numeric;
      
      public TelephonyOperator()
      {
        clear();
      }
      
      public static TelephonyOperator[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new TelephonyOperator[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static TelephonyOperator parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new TelephonyOperator().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static TelephonyOperator parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (TelephonyOperator)MessageNano.mergeFrom(new TelephonyOperator(), paramArrayOfByte);
      }
      
      public TelephonyOperator clear()
      {
        alphaLong = "";
        alphaShort = "";
        numeric = "";
        unknownFieldData = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (!alphaLong.equals("")) {
          j = i + CodedOutputByteBufferNano.computeStringSize(1, alphaLong);
        }
        i = j;
        if (!alphaShort.equals("")) {
          i = j + CodedOutputByteBufferNano.computeStringSize(2, alphaShort);
        }
        j = i;
        if (!numeric.equals("")) {
          j = i + CodedOutputByteBufferNano.computeStringSize(3, numeric);
        }
        return j;
      }
      
      public TelephonyOperator mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 18)
            {
              if (i != 26)
              {
                if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else {
                numeric = paramCodedInputByteBufferNano.readString();
              }
            }
            else {
              alphaShort = paramCodedInputByteBufferNano.readString();
            }
          }
          else {
            alphaLong = paramCodedInputByteBufferNano.readString();
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (!alphaLong.equals("")) {
          paramCodedOutputByteBufferNano.writeString(1, alphaLong);
        }
        if (!alphaShort.equals("")) {
          paramCodedOutputByteBufferNano.writeString(2, alphaShort);
        }
        if (!numeric.equals("")) {
          paramCodedOutputByteBufferNano.writeString(3, numeric);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
  }
  
  public static final class TelephonySettings
    extends ExtendableMessageNano<TelephonySettings>
  {
    private static volatile TelephonySettings[] _emptyArray;
    public boolean isAirplaneMode;
    public boolean isCellularDataEnabled;
    public boolean isDataRoamingEnabled;
    public boolean isEnhanced4GLteModeEnabled;
    public boolean isVtOverLteEnabled;
    public boolean isVtOverWifiEnabled;
    public boolean isWifiCallingEnabled;
    public boolean isWifiEnabled;
    public int preferredNetworkMode;
    public int wifiCallingMode;
    
    public TelephonySettings()
    {
      clear();
    }
    
    public static TelephonySettings[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new TelephonySettings[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static TelephonySettings parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new TelephonySettings().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static TelephonySettings parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (TelephonySettings)MessageNano.mergeFrom(new TelephonySettings(), paramArrayOfByte);
    }
    
    public TelephonySettings clear()
    {
      isAirplaneMode = false;
      isCellularDataEnabled = false;
      isDataRoamingEnabled = false;
      preferredNetworkMode = 0;
      isEnhanced4GLteModeEnabled = false;
      isWifiEnabled = false;
      isWifiCallingEnabled = false;
      wifiCallingMode = 0;
      isVtOverLteEnabled = false;
      isVtOverWifiEnabled = false;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (isAirplaneMode) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(1, isAirplaneMode);
      }
      i = j;
      if (isCellularDataEnabled) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(2, isCellularDataEnabled);
      }
      j = i;
      if (isDataRoamingEnabled) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(3, isDataRoamingEnabled);
      }
      i = j;
      if (preferredNetworkMode != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, preferredNetworkMode);
      }
      j = i;
      if (isEnhanced4GLteModeEnabled) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(5, isEnhanced4GLteModeEnabled);
      }
      i = j;
      if (isWifiEnabled) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(6, isWifiEnabled);
      }
      j = i;
      if (isWifiCallingEnabled) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(7, isWifiCallingEnabled);
      }
      i = j;
      if (wifiCallingMode != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(8, wifiCallingMode);
      }
      j = i;
      if (isVtOverLteEnabled) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(9, isVtOverLteEnabled);
      }
      i = j;
      if (isVtOverWifiEnabled) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(10, isVtOverWifiEnabled);
      }
      return i;
    }
    
    public TelephonySettings mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        int j;
        int k;
        switch (i)
        {
        default: 
          if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 80: 
          isVtOverWifiEnabled = paramCodedInputByteBufferNano.readBool();
          break;
        case 72: 
          isVtOverLteEnabled = paramCodedInputByteBufferNano.readBool();
          break;
        case 64: 
          j = paramCodedInputByteBufferNano.getPosition();
          k = paramCodedInputByteBufferNano.readInt32();
          switch (k)
          {
          default: 
            paramCodedInputByteBufferNano.rewindToPosition(j);
            storeUnknownField(paramCodedInputByteBufferNano, i);
            break;
          case 0: 
          case 1: 
          case 2: 
          case 3: 
            wifiCallingMode = k;
          }
          break;
        case 56: 
          isWifiCallingEnabled = paramCodedInputByteBufferNano.readBool();
          break;
        case 48: 
          isWifiEnabled = paramCodedInputByteBufferNano.readBool();
          break;
        case 40: 
          isEnhanced4GLteModeEnabled = paramCodedInputByteBufferNano.readBool();
          break;
        case 32: 
          k = paramCodedInputByteBufferNano.getPosition();
          j = paramCodedInputByteBufferNano.readInt32();
          switch (j)
          {
          default: 
            paramCodedInputByteBufferNano.rewindToPosition(k);
            storeUnknownField(paramCodedInputByteBufferNano, i);
            break;
          case 0: 
          case 1: 
          case 2: 
          case 3: 
          case 4: 
          case 5: 
          case 6: 
          case 7: 
          case 8: 
          case 9: 
          case 10: 
          case 11: 
          case 12: 
          case 13: 
          case 14: 
          case 15: 
          case 16: 
          case 17: 
          case 18: 
          case 19: 
          case 20: 
          case 21: 
          case 22: 
          case 23: 
            preferredNetworkMode = j;
          }
          break;
        case 24: 
          isDataRoamingEnabled = paramCodedInputByteBufferNano.readBool();
          break;
        case 16: 
          isCellularDataEnabled = paramCodedInputByteBufferNano.readBool();
          break;
        case 8: 
          isAirplaneMode = paramCodedInputByteBufferNano.readBool();
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (isAirplaneMode) {
        paramCodedOutputByteBufferNano.writeBool(1, isAirplaneMode);
      }
      if (isCellularDataEnabled) {
        paramCodedOutputByteBufferNano.writeBool(2, isCellularDataEnabled);
      }
      if (isDataRoamingEnabled) {
        paramCodedOutputByteBufferNano.writeBool(3, isDataRoamingEnabled);
      }
      if (preferredNetworkMode != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, preferredNetworkMode);
      }
      if (isEnhanced4GLteModeEnabled) {
        paramCodedOutputByteBufferNano.writeBool(5, isEnhanced4GLteModeEnabled);
      }
      if (isWifiEnabled) {
        paramCodedOutputByteBufferNano.writeBool(6, isWifiEnabled);
      }
      if (isWifiCallingEnabled) {
        paramCodedOutputByteBufferNano.writeBool(7, isWifiCallingEnabled);
      }
      if (wifiCallingMode != 0) {
        paramCodedOutputByteBufferNano.writeInt32(8, wifiCallingMode);
      }
      if (isVtOverLteEnabled) {
        paramCodedOutputByteBufferNano.writeBool(9, isVtOverLteEnabled);
      }
      if (isVtOverWifiEnabled) {
        paramCodedOutputByteBufferNano.writeBool(10, isVtOverWifiEnabled);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static abstract interface RilNetworkMode
    {
      public static final int NETWORK_MODE_CDMA = 5;
      public static final int NETWORK_MODE_CDMA_NO_EVDO = 6;
      public static final int NETWORK_MODE_EVDO_NO_CDMA = 7;
      public static final int NETWORK_MODE_GLOBAL = 8;
      public static final int NETWORK_MODE_GSM_ONLY = 2;
      public static final int NETWORK_MODE_GSM_UMTS = 4;
      public static final int NETWORK_MODE_LTE_CDMA_EVDO = 9;
      public static final int NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA = 11;
      public static final int NETWORK_MODE_LTE_GSM_WCDMA = 10;
      public static final int NETWORK_MODE_LTE_ONLY = 12;
      public static final int NETWORK_MODE_LTE_TDSCDMA = 16;
      public static final int NETWORK_MODE_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA = 23;
      public static final int NETWORK_MODE_LTE_TDSCDMA_GSM = 18;
      public static final int NETWORK_MODE_LTE_TDSCDMA_GSM_WCDMA = 21;
      public static final int NETWORK_MODE_LTE_TDSCDMA_WCDMA = 20;
      public static final int NETWORK_MODE_LTE_WCDMA = 13;
      public static final int NETWORK_MODE_TDSCDMA_CDMA_EVDO_GSM_WCDMA = 22;
      public static final int NETWORK_MODE_TDSCDMA_GSM = 17;
      public static final int NETWORK_MODE_TDSCDMA_GSM_WCDMA = 19;
      public static final int NETWORK_MODE_TDSCDMA_ONLY = 14;
      public static final int NETWORK_MODE_TDSCDMA_WCDMA = 15;
      public static final int NETWORK_MODE_UNKNOWN = 0;
      public static final int NETWORK_MODE_WCDMA_ONLY = 3;
      public static final int NETWORK_MODE_WCDMA_PREF = 1;
    }
    
    public static abstract interface WiFiCallingMode
    {
      public static final int WFC_MODE_CELLULAR_PREFERRED = 2;
      public static final int WFC_MODE_UNKNOWN = 0;
      public static final int WFC_MODE_WIFI_ONLY = 1;
      public static final int WFC_MODE_WIFI_PREFERRED = 3;
    }
  }
  
  public static final class Time
    extends ExtendableMessageNano<Time>
  {
    private static volatile Time[] _emptyArray;
    public long elapsedTimestampMillis;
    public long systemTimestampMillis;
    
    public Time()
    {
      clear();
    }
    
    public static Time[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new Time[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static Time parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new Time().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static Time parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (Time)MessageNano.mergeFrom(new Time(), paramArrayOfByte);
    }
    
    public Time clear()
    {
      systemTimestampMillis = 0L;
      elapsedTimestampMillis = 0L;
      unknownFieldData = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (systemTimestampMillis != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(1, systemTimestampMillis);
      }
      i = j;
      if (elapsedTimestampMillis != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(2, elapsedTimestampMillis);
      }
      return i;
    }
    
    public Time mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
          if (i != 16)
          {
            if (!storeUnknownField(paramCodedInputByteBufferNano, i)) {
              return this;
            }
          }
          else {
            elapsedTimestampMillis = paramCodedInputByteBufferNano.readInt64();
          }
        }
        else {
          systemTimestampMillis = paramCodedInputByteBufferNano.readInt64();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (systemTimestampMillis != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(1, systemTimestampMillis);
      }
      if (elapsedTimestampMillis != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(2, elapsedTimestampMillis);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static abstract interface TimeInterval
  {
    public static final int TI_100_MILLIS = 4;
    public static final int TI_10_MILLIS = 1;
    public static final int TI_10_MINUTES = 14;
    public static final int TI_10_SEC = 10;
    public static final int TI_1_HOUR = 16;
    public static final int TI_1_MINUTE = 12;
    public static final int TI_1_SEC = 7;
    public static final int TI_200_MILLIS = 5;
    public static final int TI_20_MILLIS = 2;
    public static final int TI_2_HOURS = 17;
    public static final int TI_2_SEC = 8;
    public static final int TI_30_MINUTES = 15;
    public static final int TI_30_SEC = 11;
    public static final int TI_3_MINUTES = 13;
    public static final int TI_4_HOURS = 18;
    public static final int TI_500_MILLIS = 6;
    public static final int TI_50_MILLIS = 3;
    public static final int TI_5_SEC = 9;
    public static final int TI_MANY_HOURS = 19;
    public static final int TI_UNKNOWN = 0;
  }
}
