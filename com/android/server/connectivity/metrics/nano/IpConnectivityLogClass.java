package com.android.server.connectivity.metrics.nano;

import com.android.framework.protobuf.nano.CodedInputByteBufferNano;
import com.android.framework.protobuf.nano.CodedOutputByteBufferNano;
import com.android.framework.protobuf.nano.InternalNano;
import com.android.framework.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.framework.protobuf.nano.MessageNano;
import com.android.framework.protobuf.nano.WireFormatNano;
import java.io.IOException;

public abstract interface IpConnectivityLogClass
{
  public static final int BLUETOOTH = 1;
  public static final int CELLULAR = 2;
  public static final int ETHERNET = 3;
  public static final int LOWPAN = 9;
  public static final int MULTIPLE = 6;
  public static final int NONE = 5;
  public static final int UNKNOWN = 0;
  public static final int WIFI = 4;
  public static final int WIFI_NAN = 8;
  public static final int WIFI_P2P = 7;
  
  public static final class ApfProgramEvent
    extends MessageNano
  {
    private static volatile ApfProgramEvent[] _emptyArray;
    public int currentRas;
    public boolean dropMulticast;
    public long effectiveLifetime;
    public int filteredRas;
    public boolean hasIpv4Addr;
    public long lifetime;
    public int programLength;
    
    public ApfProgramEvent()
    {
      clear();
    }
    
    public static ApfProgramEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new ApfProgramEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static ApfProgramEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new ApfProgramEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static ApfProgramEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (ApfProgramEvent)MessageNano.mergeFrom(new ApfProgramEvent(), paramArrayOfByte);
    }
    
    public ApfProgramEvent clear()
    {
      lifetime = 0L;
      effectiveLifetime = 0L;
      filteredRas = 0;
      currentRas = 0;
      programLength = 0;
      dropMulticast = false;
      hasIpv4Addr = false;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (lifetime != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(1, lifetime);
      }
      i = j;
      if (filteredRas != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, filteredRas);
      }
      j = i;
      if (currentRas != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, currentRas);
      }
      i = j;
      if (programLength != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, programLength);
      }
      j = i;
      if (dropMulticast) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(5, dropMulticast);
      }
      i = j;
      if (hasIpv4Addr) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(6, hasIpv4Addr);
      }
      j = i;
      if (effectiveLifetime != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(7, effectiveLifetime);
      }
      return j;
    }
    
    public ApfProgramEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                    if (i != 56)
                    {
                      if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                        return this;
                      }
                    }
                    else {
                      effectiveLifetime = paramCodedInputByteBufferNano.readInt64();
                    }
                  }
                  else {
                    hasIpv4Addr = paramCodedInputByteBufferNano.readBool();
                  }
                }
                else {
                  dropMulticast = paramCodedInputByteBufferNano.readBool();
                }
              }
              else {
                programLength = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              currentRas = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            filteredRas = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          lifetime = paramCodedInputByteBufferNano.readInt64();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (lifetime != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(1, lifetime);
      }
      if (filteredRas != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, filteredRas);
      }
      if (currentRas != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, currentRas);
      }
      if (programLength != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, programLength);
      }
      if (dropMulticast) {
        paramCodedOutputByteBufferNano.writeBool(5, dropMulticast);
      }
      if (hasIpv4Addr) {
        paramCodedOutputByteBufferNano.writeBool(6, hasIpv4Addr);
      }
      if (effectiveLifetime != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(7, effectiveLifetime);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class ApfStatistics
    extends MessageNano
  {
    private static volatile ApfStatistics[] _emptyArray;
    public int droppedRas;
    public long durationMs;
    public IpConnectivityLogClass.Pair[] hardwareCounters;
    public int matchingRas;
    public int maxProgramSize;
    public int parseErrors;
    public int programUpdates;
    public int programUpdatesAll;
    public int programUpdatesAllowingMulticast;
    public int receivedRas;
    public int totalPacketDropped;
    public int totalPacketProcessed;
    public int zeroLifetimeRas;
    
    public ApfStatistics()
    {
      clear();
    }
    
    public static ApfStatistics[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new ApfStatistics[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static ApfStatistics parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new ApfStatistics().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static ApfStatistics parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (ApfStatistics)MessageNano.mergeFrom(new ApfStatistics(), paramArrayOfByte);
    }
    
    public ApfStatistics clear()
    {
      durationMs = 0L;
      receivedRas = 0;
      matchingRas = 0;
      droppedRas = 0;
      zeroLifetimeRas = 0;
      parseErrors = 0;
      programUpdates = 0;
      maxProgramSize = 0;
      programUpdatesAll = 0;
      programUpdatesAllowingMulticast = 0;
      totalPacketProcessed = 0;
      totalPacketDropped = 0;
      hardwareCounters = IpConnectivityLogClass.Pair.emptyArray();
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (durationMs != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(1, durationMs);
      }
      i = j;
      if (receivedRas != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, receivedRas);
      }
      j = i;
      if (matchingRas != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, matchingRas);
      }
      i = j;
      if (droppedRas != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(5, droppedRas);
      }
      j = i;
      if (zeroLifetimeRas != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(6, zeroLifetimeRas);
      }
      i = j;
      if (parseErrors != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(7, parseErrors);
      }
      j = i;
      if (programUpdates != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(8, programUpdates);
      }
      i = j;
      if (maxProgramSize != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(9, maxProgramSize);
      }
      j = i;
      if (programUpdatesAll != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(10, programUpdatesAll);
      }
      int k = j;
      if (programUpdatesAllowingMulticast != 0) {
        k = j + CodedOutputByteBufferNano.computeInt32Size(11, programUpdatesAllowingMulticast);
      }
      i = k;
      if (totalPacketProcessed != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(12, totalPacketProcessed);
      }
      j = i;
      if (totalPacketDropped != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(13, totalPacketDropped);
      }
      k = j;
      if (hardwareCounters != null)
      {
        k = j;
        if (hardwareCounters.length > 0)
        {
          i = 0;
          for (;;)
          {
            k = j;
            if (i >= hardwareCounters.length) {
              break;
            }
            IpConnectivityLogClass.Pair localPair = hardwareCounters[i];
            k = j;
            if (localPair != null) {
              k = j + CodedOutputByteBufferNano.computeMessageSize(14, localPair);
            }
            i++;
            j = k;
          }
        }
      }
      return k;
    }
    
    public ApfStatistics mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default: 
          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 114: 
          int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 114);
          if (hardwareCounters == null) {
            i = 0;
          } else {
            i = hardwareCounters.length;
          }
          IpConnectivityLogClass.Pair[] arrayOfPair = new IpConnectivityLogClass.Pair[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(hardwareCounters, 0, arrayOfPair, 0, i);
          }
          for (j = i; j < arrayOfPair.length - 1; j++)
          {
            arrayOfPair[j] = new IpConnectivityLogClass.Pair();
            paramCodedInputByteBufferNano.readMessage(arrayOfPair[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfPair[j] = new IpConnectivityLogClass.Pair();
          paramCodedInputByteBufferNano.readMessage(arrayOfPair[j]);
          hardwareCounters = arrayOfPair;
          break;
        case 104: 
          totalPacketDropped = paramCodedInputByteBufferNano.readInt32();
          break;
        case 96: 
          totalPacketProcessed = paramCodedInputByteBufferNano.readInt32();
          break;
        case 88: 
          programUpdatesAllowingMulticast = paramCodedInputByteBufferNano.readInt32();
          break;
        case 80: 
          programUpdatesAll = paramCodedInputByteBufferNano.readInt32();
          break;
        case 72: 
          maxProgramSize = paramCodedInputByteBufferNano.readInt32();
          break;
        case 64: 
          programUpdates = paramCodedInputByteBufferNano.readInt32();
          break;
        case 56: 
          parseErrors = paramCodedInputByteBufferNano.readInt32();
          break;
        case 48: 
          zeroLifetimeRas = paramCodedInputByteBufferNano.readInt32();
          break;
        case 40: 
          droppedRas = paramCodedInputByteBufferNano.readInt32();
          break;
        case 24: 
          matchingRas = paramCodedInputByteBufferNano.readInt32();
          break;
        case 16: 
          receivedRas = paramCodedInputByteBufferNano.readInt32();
          break;
        case 8: 
          durationMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (durationMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(1, durationMs);
      }
      if (receivedRas != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, receivedRas);
      }
      if (matchingRas != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, matchingRas);
      }
      if (droppedRas != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, droppedRas);
      }
      if (zeroLifetimeRas != 0) {
        paramCodedOutputByteBufferNano.writeInt32(6, zeroLifetimeRas);
      }
      if (parseErrors != 0) {
        paramCodedOutputByteBufferNano.writeInt32(7, parseErrors);
      }
      if (programUpdates != 0) {
        paramCodedOutputByteBufferNano.writeInt32(8, programUpdates);
      }
      if (maxProgramSize != 0) {
        paramCodedOutputByteBufferNano.writeInt32(9, maxProgramSize);
      }
      if (programUpdatesAll != 0) {
        paramCodedOutputByteBufferNano.writeInt32(10, programUpdatesAll);
      }
      if (programUpdatesAllowingMulticast != 0) {
        paramCodedOutputByteBufferNano.writeInt32(11, programUpdatesAllowingMulticast);
      }
      if (totalPacketProcessed != 0) {
        paramCodedOutputByteBufferNano.writeInt32(12, totalPacketProcessed);
      }
      if (totalPacketDropped != 0) {
        paramCodedOutputByteBufferNano.writeInt32(13, totalPacketDropped);
      }
      if ((hardwareCounters != null) && (hardwareCounters.length > 0)) {
        for (int i = 0; i < hardwareCounters.length; i++)
        {
          IpConnectivityLogClass.Pair localPair = hardwareCounters[i];
          if (localPair != null) {
            paramCodedOutputByteBufferNano.writeMessage(14, localPair);
          }
        }
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class ConnectStatistics
    extends MessageNano
  {
    private static volatile ConnectStatistics[] _emptyArray;
    public int connectBlockingCount;
    public int connectCount;
    public IpConnectivityLogClass.Pair[] errnosCounters;
    public int ipv6AddrCount;
    public int[] latenciesMs;
    public int[] nonBlockingLatenciesMs;
    
    public ConnectStatistics()
    {
      clear();
    }
    
    public static ConnectStatistics[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new ConnectStatistics[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static ConnectStatistics parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new ConnectStatistics().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static ConnectStatistics parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (ConnectStatistics)MessageNano.mergeFrom(new ConnectStatistics(), paramArrayOfByte);
    }
    
    public ConnectStatistics clear()
    {
      connectCount = 0;
      connectBlockingCount = 0;
      ipv6AddrCount = 0;
      latenciesMs = WireFormatNano.EMPTY_INT_ARRAY;
      nonBlockingLatenciesMs = WireFormatNano.EMPTY_INT_ARRAY;
      errnosCounters = IpConnectivityLogClass.Pair.emptyArray();
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (connectCount != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, connectCount);
      }
      i = j;
      if (ipv6AddrCount != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, ipv6AddrCount);
      }
      Object localObject = latenciesMs;
      int k = 0;
      j = i;
      int m;
      if (localObject != null)
      {
        j = i;
        if (latenciesMs.length > 0)
        {
          j = 0;
          for (m = 0; m < latenciesMs.length; m++)
          {
            int n = latenciesMs[m];
            j += CodedOutputByteBufferNano.computeInt32SizeNoTag(n);
          }
          j = i + j + latenciesMs.length * 1;
        }
      }
      i = j;
      if (errnosCounters != null)
      {
        i = j;
        if (errnosCounters.length > 0)
        {
          i = 0;
          while (i < errnosCounters.length)
          {
            localObject = errnosCounters[i];
            m = j;
            if (localObject != null) {
              m = j + CodedOutputByteBufferNano.computeMessageSize(4, (MessageNano)localObject);
            }
            i++;
            j = m;
          }
          i = j;
        }
      }
      j = i;
      if (connectBlockingCount != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(5, connectBlockingCount);
      }
      i = j;
      if (nonBlockingLatenciesMs != null)
      {
        i = j;
        if (nonBlockingLatenciesMs.length > 0)
        {
          m = 0;
          for (i = k; i < nonBlockingLatenciesMs.length; i++)
          {
            k = nonBlockingLatenciesMs[i];
            m += CodedOutputByteBufferNano.computeInt32SizeNoTag(k);
          }
          i = j + m + 1 * nonBlockingLatenciesMs.length;
        }
      }
      return i;
    }
    
    public ConnectStatistics mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            int k;
            Object localObject;
            if (i != 24)
            {
              int j;
              if (i != 26)
              {
                if (i != 34)
                {
                  if (i != 40)
                  {
                    if (i != 48)
                    {
                      if (i != 50)
                      {
                        if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                          return this;
                        }
                      }
                      else
                      {
                        j = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
                        k = 0;
                        i = paramCodedInputByteBufferNano.getPosition();
                        while (paramCodedInputByteBufferNano.getBytesUntilLimit() > 0)
                        {
                          paramCodedInputByteBufferNano.readInt32();
                          k++;
                        }
                        paramCodedInputByteBufferNano.rewindToPosition(i);
                        if (nonBlockingLatenciesMs == null) {
                          i = 0;
                        } else {
                          i = nonBlockingLatenciesMs.length;
                        }
                        localObject = new int[i + k];
                        k = i;
                        if (i != 0) {
                          System.arraycopy(nonBlockingLatenciesMs, 0, localObject, 0, i);
                        }
                        for (k = i; k < localObject.length; k++) {
                          localObject[k] = paramCodedInputByteBufferNano.readInt32();
                        }
                        nonBlockingLatenciesMs = ((int[])localObject);
                        paramCodedInputByteBufferNano.popLimit(j);
                      }
                    }
                    else
                    {
                      k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 48);
                      if (nonBlockingLatenciesMs == null) {
                        i = 0;
                      } else {
                        i = nonBlockingLatenciesMs.length;
                      }
                      localObject = new int[i + k];
                      k = i;
                      if (i != 0) {
                        System.arraycopy(nonBlockingLatenciesMs, 0, localObject, 0, i);
                      }
                      for (k = i; k < localObject.length - 1; k++)
                      {
                        localObject[k] = paramCodedInputByteBufferNano.readInt32();
                        paramCodedInputByteBufferNano.readTag();
                      }
                      localObject[k] = paramCodedInputByteBufferNano.readInt32();
                      nonBlockingLatenciesMs = ((int[])localObject);
                    }
                  }
                  else {
                    connectBlockingCount = paramCodedInputByteBufferNano.readInt32();
                  }
                }
                else
                {
                  k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 34);
                  if (errnosCounters == null) {
                    i = 0;
                  } else {
                    i = errnosCounters.length;
                  }
                  localObject = new IpConnectivityLogClass.Pair[i + k];
                  k = i;
                  if (i != 0) {
                    System.arraycopy(errnosCounters, 0, localObject, 0, i);
                  }
                  for (k = i; k < localObject.length - 1; k++)
                  {
                    localObject[k] = new IpConnectivityLogClass.Pair();
                    paramCodedInputByteBufferNano.readMessage(localObject[k]);
                    paramCodedInputByteBufferNano.readTag();
                  }
                  localObject[k] = new IpConnectivityLogClass.Pair();
                  paramCodedInputByteBufferNano.readMessage(localObject[k]);
                  errnosCounters = ((IpConnectivityLogClass.Pair[])localObject);
                }
              }
              else
              {
                j = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
                k = 0;
                i = paramCodedInputByteBufferNano.getPosition();
                while (paramCodedInputByteBufferNano.getBytesUntilLimit() > 0)
                {
                  paramCodedInputByteBufferNano.readInt32();
                  k++;
                }
                paramCodedInputByteBufferNano.rewindToPosition(i);
                if (latenciesMs == null) {
                  i = 0;
                } else {
                  i = latenciesMs.length;
                }
                localObject = new int[i + k];
                k = i;
                if (i != 0) {
                  System.arraycopy(latenciesMs, 0, localObject, 0, i);
                }
                for (k = i; k < localObject.length; k++) {
                  localObject[k] = paramCodedInputByteBufferNano.readInt32();
                }
                latenciesMs = ((int[])localObject);
                paramCodedInputByteBufferNano.popLimit(j);
              }
            }
            else
            {
              k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 24);
              if (latenciesMs == null) {
                i = 0;
              } else {
                i = latenciesMs.length;
              }
              localObject = new int[i + k];
              k = i;
              if (i != 0) {
                System.arraycopy(latenciesMs, 0, localObject, 0, i);
              }
              for (k = i; k < localObject.length - 1; k++)
              {
                localObject[k] = paramCodedInputByteBufferNano.readInt32();
                paramCodedInputByteBufferNano.readTag();
              }
              localObject[k] = paramCodedInputByteBufferNano.readInt32();
              latenciesMs = ((int[])localObject);
            }
          }
          else
          {
            ipv6AddrCount = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          connectCount = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (connectCount != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, connectCount);
      }
      if (ipv6AddrCount != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, ipv6AddrCount);
      }
      Object localObject = latenciesMs;
      int i = 0;
      int j;
      if ((localObject != null) && (latenciesMs.length > 0)) {
        for (j = 0; j < latenciesMs.length; j++) {
          paramCodedOutputByteBufferNano.writeInt32(3, latenciesMs[j]);
        }
      }
      if ((errnosCounters != null) && (errnosCounters.length > 0)) {
        for (j = 0; j < errnosCounters.length; j++)
        {
          localObject = errnosCounters[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(4, (MessageNano)localObject);
          }
        }
      }
      if (connectBlockingCount != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, connectBlockingCount);
      }
      if ((nonBlockingLatenciesMs != null) && (nonBlockingLatenciesMs.length > 0)) {
        for (j = i; j < nonBlockingLatenciesMs.length; j++) {
          paramCodedOutputByteBufferNano.writeInt32(6, nonBlockingLatenciesMs[j]);
        }
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class DHCPEvent
    extends MessageNano
  {
    public static final int ERROR_CODE_FIELD_NUMBER = 3;
    public static final int STATE_TRANSITION_FIELD_NUMBER = 2;
    private static volatile DHCPEvent[] _emptyArray;
    public int durationMs;
    public String ifName;
    private int valueCase_ = 0;
    private Object value_;
    
    public DHCPEvent()
    {
      clear();
    }
    
    public static DHCPEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new DHCPEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static DHCPEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new DHCPEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static DHCPEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (DHCPEvent)MessageNano.mergeFrom(new DHCPEvent(), paramArrayOfByte);
    }
    
    public DHCPEvent clear()
    {
      ifName = "";
      durationMs = 0;
      clearValue();
      cachedSize = -1;
      return this;
    }
    
    public DHCPEvent clearValue()
    {
      valueCase_ = 0;
      value_ = null;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (!ifName.equals("")) {
        j = i + CodedOutputByteBufferNano.computeStringSize(1, ifName);
      }
      i = j;
      if (valueCase_ == 2) {
        i = j + CodedOutputByteBufferNano.computeStringSize(2, (String)value_);
      }
      j = i;
      if (valueCase_ == 3) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, ((Integer)value_).intValue());
      }
      i = j;
      if (durationMs != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, durationMs);
      }
      return i;
    }
    
    public int getErrorCode()
    {
      if (valueCase_ == 3) {
        return ((Integer)value_).intValue();
      }
      return 0;
    }
    
    public String getStateTransition()
    {
      if (valueCase_ == 2) {
        return (String)value_;
      }
      return "";
    }
    
    public int getValueCase()
    {
      return valueCase_;
    }
    
    public boolean hasErrorCode()
    {
      boolean bool;
      if (valueCase_ == 3) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasStateTransition()
    {
      boolean bool;
      if (valueCase_ == 2) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public DHCPEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 24)
            {
              if (i != 32)
              {
                if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else {
                durationMs = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else
            {
              value_ = Integer.valueOf(paramCodedInputByteBufferNano.readInt32());
              valueCase_ = 3;
            }
          }
          else
          {
            value_ = paramCodedInputByteBufferNano.readString();
            valueCase_ = 2;
          }
        }
        else {
          ifName = paramCodedInputByteBufferNano.readString();
        }
      }
      return this;
    }
    
    public DHCPEvent setErrorCode(int paramInt)
    {
      valueCase_ = 3;
      value_ = Integer.valueOf(paramInt);
      return this;
    }
    
    public DHCPEvent setStateTransition(String paramString)
    {
      valueCase_ = 2;
      value_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (!ifName.equals("")) {
        paramCodedOutputByteBufferNano.writeString(1, ifName);
      }
      if (valueCase_ == 2) {
        paramCodedOutputByteBufferNano.writeString(2, (String)value_);
      }
      if (valueCase_ == 3) {
        paramCodedOutputByteBufferNano.writeInt32(3, ((Integer)value_).intValue());
      }
      if (durationMs != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, durationMs);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class DNSLatencies
    extends MessageNano
  {
    private static volatile DNSLatencies[] _emptyArray;
    public int aCount;
    public int aaaaCount;
    public int[] latenciesMs;
    public int queryCount;
    public int returnCode;
    public int type;
    
    public DNSLatencies()
    {
      clear();
    }
    
    public static DNSLatencies[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new DNSLatencies[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static DNSLatencies parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new DNSLatencies().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static DNSLatencies parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (DNSLatencies)MessageNano.mergeFrom(new DNSLatencies(), paramArrayOfByte);
    }
    
    public DNSLatencies clear()
    {
      type = 0;
      returnCode = 0;
      queryCount = 0;
      aCount = 0;
      aaaaCount = 0;
      latenciesMs = WireFormatNano.EMPTY_INT_ARRAY;
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
      if (returnCode != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, returnCode);
      }
      j = i;
      if (queryCount != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, queryCount);
      }
      i = j;
      if (aCount != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, aCount);
      }
      j = i;
      if (aaaaCount != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(5, aaaaCount);
      }
      i = j;
      if (latenciesMs != null)
      {
        i = j;
        if (latenciesMs.length > 0)
        {
          int k = 0;
          for (i = 0; i < latenciesMs.length; i++)
          {
            int m = latenciesMs[i];
            k += CodedOutputByteBufferNano.computeInt32SizeNoTag(m);
          }
          i = j + k + 1 * latenciesMs.length;
        }
      }
      return i;
    }
    
    public DNSLatencies mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                  int k;
                  int[] arrayOfInt;
                  if (i != 48)
                  {
                    if (i != 50)
                    {
                      if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                        return this;
                      }
                    }
                    else
                    {
                      int j = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
                      k = 0;
                      i = paramCodedInputByteBufferNano.getPosition();
                      while (paramCodedInputByteBufferNano.getBytesUntilLimit() > 0)
                      {
                        paramCodedInputByteBufferNano.readInt32();
                        k++;
                      }
                      paramCodedInputByteBufferNano.rewindToPosition(i);
                      if (latenciesMs == null) {
                        i = 0;
                      } else {
                        i = latenciesMs.length;
                      }
                      arrayOfInt = new int[i + k];
                      k = i;
                      if (i != 0) {
                        System.arraycopy(latenciesMs, 0, arrayOfInt, 0, i);
                      }
                      for (k = i; k < arrayOfInt.length; k++) {
                        arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
                      }
                      latenciesMs = arrayOfInt;
                      paramCodedInputByteBufferNano.popLimit(j);
                    }
                  }
                  else
                  {
                    k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 48);
                    if (latenciesMs == null) {
                      i = 0;
                    } else {
                      i = latenciesMs.length;
                    }
                    arrayOfInt = new int[i + k];
                    k = i;
                    if (i != 0) {
                      System.arraycopy(latenciesMs, 0, arrayOfInt, 0, i);
                    }
                    for (k = i; k < arrayOfInt.length - 1; k++)
                    {
                      arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
                      paramCodedInputByteBufferNano.readTag();
                    }
                    arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
                    latenciesMs = arrayOfInt;
                  }
                }
                else
                {
                  aaaaCount = paramCodedInputByteBufferNano.readInt32();
                }
              }
              else {
                aCount = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              queryCount = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            returnCode = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          type = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (type != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, type);
      }
      if (returnCode != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, returnCode);
      }
      if (queryCount != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, queryCount);
      }
      if (aCount != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, aCount);
      }
      if (aaaaCount != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, aaaaCount);
      }
      if ((latenciesMs != null) && (latenciesMs.length > 0)) {
        for (int i = 0; i < latenciesMs.length; i++) {
          paramCodedOutputByteBufferNano.writeInt32(6, latenciesMs[i]);
        }
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class DNSLookupBatch
    extends MessageNano
  {
    private static volatile DNSLookupBatch[] _emptyArray;
    public int[] eventTypes;
    public long getaddrinfoErrorCount;
    public IpConnectivityLogClass.Pair[] getaddrinfoErrors;
    public long getaddrinfoQueryCount;
    public long gethostbynameErrorCount;
    public IpConnectivityLogClass.Pair[] gethostbynameErrors;
    public long gethostbynameQueryCount;
    public int[] latenciesMs;
    public IpConnectivityLogClass.NetworkId networkId;
    public int[] returnCodes;
    
    public DNSLookupBatch()
    {
      clear();
    }
    
    public static DNSLookupBatch[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new DNSLookupBatch[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static DNSLookupBatch parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new DNSLookupBatch().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static DNSLookupBatch parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (DNSLookupBatch)MessageNano.mergeFrom(new DNSLookupBatch(), paramArrayOfByte);
    }
    
    public DNSLookupBatch clear()
    {
      latenciesMs = WireFormatNano.EMPTY_INT_ARRAY;
      getaddrinfoQueryCount = 0L;
      gethostbynameQueryCount = 0L;
      getaddrinfoErrorCount = 0L;
      gethostbynameErrorCount = 0L;
      getaddrinfoErrors = IpConnectivityLogClass.Pair.emptyArray();
      gethostbynameErrors = IpConnectivityLogClass.Pair.emptyArray();
      networkId = null;
      eventTypes = WireFormatNano.EMPTY_INT_ARRAY;
      returnCodes = WireFormatNano.EMPTY_INT_ARRAY;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (networkId != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(1, networkId);
      }
      Object localObject = eventTypes;
      int k = 0;
      i = j;
      int n;
      if (localObject != null)
      {
        i = j;
        if (eventTypes.length > 0)
        {
          i = 0;
          for (m = 0; m < eventTypes.length; m++)
          {
            n = eventTypes[m];
            i += CodedOutputByteBufferNano.computeInt32SizeNoTag(n);
          }
          i = j + i + eventTypes.length * 1;
        }
      }
      j = i;
      if (returnCodes != null)
      {
        j = i;
        if (returnCodes.length > 0)
        {
          m = 0;
          for (j = 0; j < returnCodes.length; j++)
          {
            n = returnCodes[j];
            m += CodedOutputByteBufferNano.computeInt32SizeNoTag(n);
          }
          j = i + m + returnCodes.length * 1;
        }
      }
      i = j;
      if (latenciesMs != null)
      {
        i = j;
        if (latenciesMs.length > 0)
        {
          m = 0;
          for (i = 0; i < latenciesMs.length; i++)
          {
            n = latenciesMs[i];
            m += CodedOutputByteBufferNano.computeInt32SizeNoTag(n);
          }
          i = j + m + 1 * latenciesMs.length;
        }
      }
      j = i;
      if (getaddrinfoQueryCount != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(5, getaddrinfoQueryCount);
      }
      i = j;
      if (gethostbynameQueryCount != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(6, gethostbynameQueryCount);
      }
      int m = i;
      if (getaddrinfoErrorCount != 0L) {
        m = i + CodedOutputByteBufferNano.computeInt64Size(7, getaddrinfoErrorCount);
      }
      j = m;
      if (gethostbynameErrorCount != 0L) {
        j = m + CodedOutputByteBufferNano.computeInt64Size(8, gethostbynameErrorCount);
      }
      i = j;
      if (getaddrinfoErrors != null)
      {
        i = j;
        if (getaddrinfoErrors.length > 0)
        {
          i = j;
          j = 0;
          while (j < getaddrinfoErrors.length)
          {
            localObject = getaddrinfoErrors[j];
            m = i;
            if (localObject != null) {
              m = i + CodedOutputByteBufferNano.computeMessageSize(9, (MessageNano)localObject);
            }
            j++;
            i = m;
          }
        }
      }
      m = i;
      if (gethostbynameErrors != null)
      {
        m = i;
        if (gethostbynameErrors.length > 0)
        {
          j = k;
          for (;;)
          {
            m = i;
            if (j >= gethostbynameErrors.length) {
              break;
            }
            localObject = gethostbynameErrors[j];
            m = i;
            if (localObject != null) {
              m = i + CodedOutputByteBufferNano.computeMessageSize(10, (MessageNano)localObject);
            }
            j++;
            i = m;
          }
        }
      }
      return m;
    }
    
    public DNSLookupBatch mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        int j;
        Object localObject;
        int k;
        switch (i)
        {
        default: 
          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 82: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 82);
          if (gethostbynameErrors == null) {
            i = 0;
          } else {
            i = gethostbynameErrors.length;
          }
          localObject = new IpConnectivityLogClass.Pair[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(gethostbynameErrors, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new IpConnectivityLogClass.Pair();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new IpConnectivityLogClass.Pair();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          gethostbynameErrors = ((IpConnectivityLogClass.Pair[])localObject);
          break;
        case 74: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 74);
          if (getaddrinfoErrors == null) {
            i = 0;
          } else {
            i = getaddrinfoErrors.length;
          }
          localObject = new IpConnectivityLogClass.Pair[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(getaddrinfoErrors, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new IpConnectivityLogClass.Pair();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new IpConnectivityLogClass.Pair();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          getaddrinfoErrors = ((IpConnectivityLogClass.Pair[])localObject);
          break;
        case 64: 
          gethostbynameErrorCount = paramCodedInputByteBufferNano.readInt64();
          break;
        case 56: 
          getaddrinfoErrorCount = paramCodedInputByteBufferNano.readInt64();
          break;
        case 48: 
          gethostbynameQueryCount = paramCodedInputByteBufferNano.readInt64();
          break;
        case 40: 
          getaddrinfoQueryCount = paramCodedInputByteBufferNano.readInt64();
          break;
        case 34: 
          k = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
          j = 0;
          i = paramCodedInputByteBufferNano.getPosition();
          while (paramCodedInputByteBufferNano.getBytesUntilLimit() > 0)
          {
            paramCodedInputByteBufferNano.readInt32();
            j++;
          }
          paramCodedInputByteBufferNano.rewindToPosition(i);
          if (latenciesMs == null) {
            i = 0;
          } else {
            i = latenciesMs.length;
          }
          localObject = new int[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(latenciesMs, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length; j++) {
            localObject[j] = paramCodedInputByteBufferNano.readInt32();
          }
          latenciesMs = ((int[])localObject);
          paramCodedInputByteBufferNano.popLimit(k);
          break;
        case 32: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 32);
          if (latenciesMs == null) {
            i = 0;
          } else {
            i = latenciesMs.length;
          }
          localObject = new int[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(latenciesMs, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = paramCodedInputByteBufferNano.readInt32();
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = paramCodedInputByteBufferNano.readInt32();
          latenciesMs = ((int[])localObject);
          break;
        case 26: 
          k = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
          j = 0;
          i = paramCodedInputByteBufferNano.getPosition();
          while (paramCodedInputByteBufferNano.getBytesUntilLimit() > 0)
          {
            paramCodedInputByteBufferNano.readInt32();
            j++;
          }
          paramCodedInputByteBufferNano.rewindToPosition(i);
          if (returnCodes == null) {
            i = 0;
          } else {
            i = returnCodes.length;
          }
          localObject = new int[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(returnCodes, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length; j++) {
            localObject[j] = paramCodedInputByteBufferNano.readInt32();
          }
          returnCodes = ((int[])localObject);
          paramCodedInputByteBufferNano.popLimit(k);
          break;
        case 24: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 24);
          if (returnCodes == null) {
            i = 0;
          } else {
            i = returnCodes.length;
          }
          localObject = new int[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(returnCodes, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = paramCodedInputByteBufferNano.readInt32();
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = paramCodedInputByteBufferNano.readInt32();
          returnCodes = ((int[])localObject);
          break;
        case 18: 
          k = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
          j = 0;
          i = paramCodedInputByteBufferNano.getPosition();
          while (paramCodedInputByteBufferNano.getBytesUntilLimit() > 0)
          {
            paramCodedInputByteBufferNano.readInt32();
            j++;
          }
          paramCodedInputByteBufferNano.rewindToPosition(i);
          if (eventTypes == null) {
            i = 0;
          } else {
            i = eventTypes.length;
          }
          localObject = new int[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(eventTypes, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length; j++) {
            localObject[j] = paramCodedInputByteBufferNano.readInt32();
          }
          eventTypes = ((int[])localObject);
          paramCodedInputByteBufferNano.popLimit(k);
          break;
        case 16: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 16);
          if (eventTypes == null) {
            i = 0;
          } else {
            i = eventTypes.length;
          }
          localObject = new int[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(eventTypes, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = paramCodedInputByteBufferNano.readInt32();
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = paramCodedInputByteBufferNano.readInt32();
          eventTypes = ((int[])localObject);
          break;
        case 10: 
          if (networkId == null) {
            networkId = new IpConnectivityLogClass.NetworkId();
          }
          paramCodedInputByteBufferNano.readMessage(networkId);
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (networkId != null) {
        paramCodedOutputByteBufferNano.writeMessage(1, networkId);
      }
      Object localObject = eventTypes;
      int i = 0;
      int j;
      if ((localObject != null) && (eventTypes.length > 0)) {
        for (j = 0; j < eventTypes.length; j++) {
          paramCodedOutputByteBufferNano.writeInt32(2, eventTypes[j]);
        }
      }
      if ((returnCodes != null) && (returnCodes.length > 0)) {
        for (j = 0; j < returnCodes.length; j++) {
          paramCodedOutputByteBufferNano.writeInt32(3, returnCodes[j]);
        }
      }
      if ((latenciesMs != null) && (latenciesMs.length > 0)) {
        for (j = 0; j < latenciesMs.length; j++) {
          paramCodedOutputByteBufferNano.writeInt32(4, latenciesMs[j]);
        }
      }
      if (getaddrinfoQueryCount != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(5, getaddrinfoQueryCount);
      }
      if (gethostbynameQueryCount != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(6, gethostbynameQueryCount);
      }
      if (getaddrinfoErrorCount != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(7, getaddrinfoErrorCount);
      }
      if (gethostbynameErrorCount != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(8, gethostbynameErrorCount);
      }
      if ((getaddrinfoErrors != null) && (getaddrinfoErrors.length > 0)) {
        for (j = 0; j < getaddrinfoErrors.length; j++)
        {
          localObject = getaddrinfoErrors[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(9, (MessageNano)localObject);
          }
        }
      }
      if ((gethostbynameErrors != null) && (gethostbynameErrors.length > 0)) {
        for (j = i; j < gethostbynameErrors.length; j++)
        {
          localObject = gethostbynameErrors[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(10, (MessageNano)localObject);
          }
        }
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class DefaultNetworkEvent
    extends MessageNano
  {
    public static final int DISCONNECT = 3;
    public static final int DUAL = 3;
    public static final int INVALIDATION = 2;
    public static final int IPV4 = 1;
    public static final int IPV6 = 2;
    public static final int NONE = 0;
    public static final int OUTSCORED = 1;
    public static final int UNKNOWN = 0;
    private static volatile DefaultNetworkEvent[] _emptyArray;
    public long defaultNetworkDurationMs;
    public long finalScore;
    public long initialScore;
    public int ipSupport;
    public IpConnectivityLogClass.NetworkId networkId;
    public long noDefaultNetworkDurationMs;
    public int previousDefaultNetworkLinkLayer;
    public IpConnectivityLogClass.NetworkId previousNetworkId;
    public int previousNetworkIpSupport;
    public int[] transportTypes;
    public long validationDurationMs;
    
    public DefaultNetworkEvent()
    {
      clear();
    }
    
    public static DefaultNetworkEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new DefaultNetworkEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static DefaultNetworkEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new DefaultNetworkEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static DefaultNetworkEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (DefaultNetworkEvent)MessageNano.mergeFrom(new DefaultNetworkEvent(), paramArrayOfByte);
    }
    
    public DefaultNetworkEvent clear()
    {
      defaultNetworkDurationMs = 0L;
      validationDurationMs = 0L;
      initialScore = 0L;
      finalScore = 0L;
      ipSupport = 0;
      previousDefaultNetworkLinkLayer = 0;
      networkId = null;
      previousNetworkId = null;
      previousNetworkIpSupport = 0;
      transportTypes = WireFormatNano.EMPTY_INT_ARRAY;
      noDefaultNetworkDurationMs = 0L;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (networkId != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(1, networkId);
      }
      i = j;
      if (previousNetworkId != null) {
        i = j + CodedOutputByteBufferNano.computeMessageSize(2, previousNetworkId);
      }
      j = i;
      if (previousNetworkIpSupport != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, previousNetworkIpSupport);
      }
      i = j;
      if (transportTypes != null)
      {
        i = j;
        if (transportTypes.length > 0)
        {
          k = 0;
          for (i = 0; i < transportTypes.length; i++)
          {
            int m = transportTypes[i];
            k += CodedOutputByteBufferNano.computeInt32SizeNoTag(m);
          }
          i = j + k + 1 * transportTypes.length;
        }
      }
      j = i;
      if (defaultNetworkDurationMs != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(5, defaultNetworkDurationMs);
      }
      i = j;
      if (noDefaultNetworkDurationMs != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(6, noDefaultNetworkDurationMs);
      }
      j = i;
      if (initialScore != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(7, initialScore);
      }
      int k = j;
      if (finalScore != 0L) {
        k = j + CodedOutputByteBufferNano.computeInt64Size(8, finalScore);
      }
      i = k;
      if (ipSupport != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(9, ipSupport);
      }
      j = i;
      if (previousDefaultNetworkLinkLayer != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(10, previousDefaultNetworkLinkLayer);
      }
      i = j;
      if (validationDurationMs != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(11, validationDurationMs);
      }
      return i;
    }
    
    public DefaultNetworkEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        int k;
        int[] arrayOfInt;
        switch (i)
        {
        default: 
          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 88: 
          validationDurationMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 80: 
          i = paramCodedInputByteBufferNano.readInt32();
          switch (i)
          {
          default: 
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
            previousDefaultNetworkLinkLayer = i;
          }
          break;
        case 72: 
          i = paramCodedInputByteBufferNano.readInt32();
          switch (i)
          {
          default: 
            break;
          case 0: 
          case 1: 
          case 2: 
          case 3: 
            ipSupport = i;
          }
          break;
        case 64: 
          finalScore = paramCodedInputByteBufferNano.readInt64();
          break;
        case 56: 
          initialScore = paramCodedInputByteBufferNano.readInt64();
          break;
        case 48: 
          noDefaultNetworkDurationMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 40: 
          defaultNetworkDurationMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 34: 
          int j = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
          k = 0;
          i = paramCodedInputByteBufferNano.getPosition();
          while (paramCodedInputByteBufferNano.getBytesUntilLimit() > 0)
          {
            paramCodedInputByteBufferNano.readInt32();
            k++;
          }
          paramCodedInputByteBufferNano.rewindToPosition(i);
          if (transportTypes == null) {
            i = 0;
          } else {
            i = transportTypes.length;
          }
          arrayOfInt = new int[i + k];
          k = i;
          if (i != 0) {
            System.arraycopy(transportTypes, 0, arrayOfInt, 0, i);
          }
          for (k = i; k < arrayOfInt.length; k++) {
            arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
          }
          transportTypes = arrayOfInt;
          paramCodedInputByteBufferNano.popLimit(j);
          break;
        case 32: 
          k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 32);
          if (transportTypes == null) {
            i = 0;
          } else {
            i = transportTypes.length;
          }
          arrayOfInt = new int[i + k];
          k = i;
          if (i != 0) {
            System.arraycopy(transportTypes, 0, arrayOfInt, 0, i);
          }
          for (k = i; k < arrayOfInt.length - 1; k++)
          {
            arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfInt[k] = paramCodedInputByteBufferNano.readInt32();
          transportTypes = arrayOfInt;
          break;
        case 24: 
          i = paramCodedInputByteBufferNano.readInt32();
          switch (i)
          {
          default: 
            break;
          case 0: 
          case 1: 
          case 2: 
          case 3: 
            previousNetworkIpSupport = i;
          }
          break;
        case 18: 
          if (previousNetworkId == null) {
            previousNetworkId = new IpConnectivityLogClass.NetworkId();
          }
          paramCodedInputByteBufferNano.readMessage(previousNetworkId);
          break;
        case 10: 
          if (networkId == null) {
            networkId = new IpConnectivityLogClass.NetworkId();
          }
          paramCodedInputByteBufferNano.readMessage(networkId);
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (networkId != null) {
        paramCodedOutputByteBufferNano.writeMessage(1, networkId);
      }
      if (previousNetworkId != null) {
        paramCodedOutputByteBufferNano.writeMessage(2, previousNetworkId);
      }
      if (previousNetworkIpSupport != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, previousNetworkIpSupport);
      }
      if ((transportTypes != null) && (transportTypes.length > 0)) {
        for (int i = 0; i < transportTypes.length; i++) {
          paramCodedOutputByteBufferNano.writeInt32(4, transportTypes[i]);
        }
      }
      if (defaultNetworkDurationMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(5, defaultNetworkDurationMs);
      }
      if (noDefaultNetworkDurationMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(6, noDefaultNetworkDurationMs);
      }
      if (initialScore != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(7, initialScore);
      }
      if (finalScore != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(8, finalScore);
      }
      if (ipSupport != 0) {
        paramCodedOutputByteBufferNano.writeInt32(9, ipSupport);
      }
      if (previousDefaultNetworkLinkLayer != 0) {
        paramCodedOutputByteBufferNano.writeInt32(10, previousDefaultNetworkLinkLayer);
      }
      if (validationDurationMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(11, validationDurationMs);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class IpConnectivityEvent
    extends MessageNano
  {
    public static final int APF_PROGRAM_EVENT_FIELD_NUMBER = 9;
    public static final int APF_STATISTICS_FIELD_NUMBER = 10;
    public static final int CONNECT_STATISTICS_FIELD_NUMBER = 14;
    public static final int DEFAULT_NETWORK_EVENT_FIELD_NUMBER = 2;
    public static final int DHCP_EVENT_FIELD_NUMBER = 6;
    public static final int DNS_LATENCIES_FIELD_NUMBER = 13;
    public static final int DNS_LOOKUP_BATCH_FIELD_NUMBER = 5;
    public static final int IP_PROVISIONING_EVENT_FIELD_NUMBER = 7;
    public static final int IP_REACHABILITY_EVENT_FIELD_NUMBER = 3;
    public static final int NETWORK_EVENT_FIELD_NUMBER = 4;
    public static final int NETWORK_STATS_FIELD_NUMBER = 19;
    public static final int RA_EVENT_FIELD_NUMBER = 11;
    public static final int VALIDATION_PROBE_EVENT_FIELD_NUMBER = 8;
    public static final int WAKEUP_STATS_FIELD_NUMBER = 20;
    private static volatile IpConnectivityEvent[] _emptyArray;
    private int eventCase_ = 0;
    private Object event_;
    public String ifName;
    public int linkLayer;
    public int networkId;
    public long timeMs;
    public long transports;
    
    public IpConnectivityEvent()
    {
      clear();
    }
    
    public static IpConnectivityEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new IpConnectivityEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static IpConnectivityEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new IpConnectivityEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static IpConnectivityEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (IpConnectivityEvent)MessageNano.mergeFrom(new IpConnectivityEvent(), paramArrayOfByte);
    }
    
    public IpConnectivityEvent clear()
    {
      timeMs = 0L;
      linkLayer = 0;
      networkId = 0;
      ifName = "";
      transports = 0L;
      clearEvent();
      cachedSize = -1;
      return this;
    }
    
    public IpConnectivityEvent clearEvent()
    {
      eventCase_ = 0;
      event_ = null;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (timeMs != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(1, timeMs);
      }
      i = j;
      if (eventCase_ == 2) {
        i = j + CodedOutputByteBufferNano.computeMessageSize(2, (MessageNano)event_);
      }
      int k = i;
      if (eventCase_ == 3) {
        k = i + CodedOutputByteBufferNano.computeMessageSize(3, (MessageNano)event_);
      }
      j = k;
      if (eventCase_ == 4) {
        j = k + CodedOutputByteBufferNano.computeMessageSize(4, (MessageNano)event_);
      }
      i = j;
      if (eventCase_ == 5) {
        i = j + CodedOutputByteBufferNano.computeMessageSize(5, (MessageNano)event_);
      }
      j = i;
      if (eventCase_ == 6) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(6, (MessageNano)event_);
      }
      k = j;
      if (eventCase_ == 7) {
        k = j + CodedOutputByteBufferNano.computeMessageSize(7, (MessageNano)event_);
      }
      i = k;
      if (eventCase_ == 8) {
        i = k + CodedOutputByteBufferNano.computeMessageSize(8, (MessageNano)event_);
      }
      k = i;
      if (eventCase_ == 9) {
        k = i + CodedOutputByteBufferNano.computeMessageSize(9, (MessageNano)event_);
      }
      j = k;
      if (eventCase_ == 10) {
        j = k + CodedOutputByteBufferNano.computeMessageSize(10, (MessageNano)event_);
      }
      i = j;
      if (eventCase_ == 11) {
        i = j + CodedOutputByteBufferNano.computeMessageSize(11, (MessageNano)event_);
      }
      j = i;
      if (eventCase_ == 13) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(13, (MessageNano)event_);
      }
      i = j;
      if (eventCase_ == 14) {
        i = j + CodedOutputByteBufferNano.computeMessageSize(14, (MessageNano)event_);
      }
      j = i;
      if (linkLayer != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(15, linkLayer);
      }
      k = j;
      if (networkId != 0) {
        k = j + CodedOutputByteBufferNano.computeInt32Size(16, networkId);
      }
      i = k;
      if (!ifName.equals("")) {
        i = k + CodedOutputByteBufferNano.computeStringSize(17, ifName);
      }
      j = i;
      if (transports != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(18, transports);
      }
      i = j;
      if (eventCase_ == 19) {
        i = j + CodedOutputByteBufferNano.computeMessageSize(19, (MessageNano)event_);
      }
      j = i;
      if (eventCase_ == 20) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(20, (MessageNano)event_);
      }
      return j;
    }
    
    public IpConnectivityLogClass.ApfProgramEvent getApfProgramEvent()
    {
      if (eventCase_ == 9) {
        return (IpConnectivityLogClass.ApfProgramEvent)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.ApfStatistics getApfStatistics()
    {
      if (eventCase_ == 10) {
        return (IpConnectivityLogClass.ApfStatistics)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.ConnectStatistics getConnectStatistics()
    {
      if (eventCase_ == 14) {
        return (IpConnectivityLogClass.ConnectStatistics)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.DefaultNetworkEvent getDefaultNetworkEvent()
    {
      if (eventCase_ == 2) {
        return (IpConnectivityLogClass.DefaultNetworkEvent)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.DHCPEvent getDhcpEvent()
    {
      if (eventCase_ == 6) {
        return (IpConnectivityLogClass.DHCPEvent)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.DNSLatencies getDnsLatencies()
    {
      if (eventCase_ == 13) {
        return (IpConnectivityLogClass.DNSLatencies)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.DNSLookupBatch getDnsLookupBatch()
    {
      if (eventCase_ == 5) {
        return (IpConnectivityLogClass.DNSLookupBatch)event_;
      }
      return null;
    }
    
    public int getEventCase()
    {
      return eventCase_;
    }
    
    public IpConnectivityLogClass.IpProvisioningEvent getIpProvisioningEvent()
    {
      if (eventCase_ == 7) {
        return (IpConnectivityLogClass.IpProvisioningEvent)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.IpReachabilityEvent getIpReachabilityEvent()
    {
      if (eventCase_ == 3) {
        return (IpConnectivityLogClass.IpReachabilityEvent)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.NetworkEvent getNetworkEvent()
    {
      if (eventCase_ == 4) {
        return (IpConnectivityLogClass.NetworkEvent)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.NetworkStats getNetworkStats()
    {
      if (eventCase_ == 19) {
        return (IpConnectivityLogClass.NetworkStats)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.RaEvent getRaEvent()
    {
      if (eventCase_ == 11) {
        return (IpConnectivityLogClass.RaEvent)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.ValidationProbeEvent getValidationProbeEvent()
    {
      if (eventCase_ == 8) {
        return (IpConnectivityLogClass.ValidationProbeEvent)event_;
      }
      return null;
    }
    
    public IpConnectivityLogClass.WakeupStats getWakeupStats()
    {
      if (eventCase_ == 20) {
        return (IpConnectivityLogClass.WakeupStats)event_;
      }
      return null;
    }
    
    public boolean hasApfProgramEvent()
    {
      boolean bool;
      if (eventCase_ == 9) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasApfStatistics()
    {
      boolean bool;
      if (eventCase_ == 10) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasConnectStatistics()
    {
      boolean bool;
      if (eventCase_ == 14) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasDefaultNetworkEvent()
    {
      boolean bool;
      if (eventCase_ == 2) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasDhcpEvent()
    {
      boolean bool;
      if (eventCase_ == 6) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasDnsLatencies()
    {
      boolean bool;
      if (eventCase_ == 13) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasDnsLookupBatch()
    {
      boolean bool;
      if (eventCase_ == 5) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasIpProvisioningEvent()
    {
      boolean bool;
      if (eventCase_ == 7) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasIpReachabilityEvent()
    {
      boolean bool;
      if (eventCase_ == 3) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasNetworkEvent()
    {
      boolean bool;
      if (eventCase_ == 4) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasNetworkStats()
    {
      boolean bool;
      if (eventCase_ == 19) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasRaEvent()
    {
      boolean bool;
      if (eventCase_ == 11) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasValidationProbeEvent()
    {
      boolean bool;
      if (eventCase_ == 8) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasWakeupStats()
    {
      boolean bool;
      if (eventCase_ == 20) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public IpConnectivityEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default: 
          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 162: 
          if (eventCase_ != 20) {
            event_ = new IpConnectivityLogClass.WakeupStats();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 20;
          break;
        case 154: 
          if (eventCase_ != 19) {
            event_ = new IpConnectivityLogClass.NetworkStats();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 19;
          break;
        case 144: 
          transports = paramCodedInputByteBufferNano.readInt64();
          break;
        case 138: 
          ifName = paramCodedInputByteBufferNano.readString();
          break;
        case 128: 
          networkId = paramCodedInputByteBufferNano.readInt32();
          break;
        case 120: 
          i = paramCodedInputByteBufferNano.readInt32();
          switch (i)
          {
          default: 
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
            linkLayer = i;
          }
          break;
        case 114: 
          if (eventCase_ != 14) {
            event_ = new IpConnectivityLogClass.ConnectStatistics();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 14;
          break;
        case 106: 
          if (eventCase_ != 13) {
            event_ = new IpConnectivityLogClass.DNSLatencies();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 13;
          break;
        case 90: 
          if (eventCase_ != 11) {
            event_ = new IpConnectivityLogClass.RaEvent();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 11;
          break;
        case 82: 
          if (eventCase_ != 10) {
            event_ = new IpConnectivityLogClass.ApfStatistics();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 10;
          break;
        case 74: 
          if (eventCase_ != 9) {
            event_ = new IpConnectivityLogClass.ApfProgramEvent();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 9;
          break;
        case 66: 
          if (eventCase_ != 8) {
            event_ = new IpConnectivityLogClass.ValidationProbeEvent();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 8;
          break;
        case 58: 
          if (eventCase_ != 7) {
            event_ = new IpConnectivityLogClass.IpProvisioningEvent();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 7;
          break;
        case 50: 
          if (eventCase_ != 6) {
            event_ = new IpConnectivityLogClass.DHCPEvent();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 6;
          break;
        case 42: 
          if (eventCase_ != 5) {
            event_ = new IpConnectivityLogClass.DNSLookupBatch();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 5;
          break;
        case 34: 
          if (eventCase_ != 4) {
            event_ = new IpConnectivityLogClass.NetworkEvent();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 4;
          break;
        case 26: 
          if (eventCase_ != 3) {
            event_ = new IpConnectivityLogClass.IpReachabilityEvent();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 3;
          break;
        case 18: 
          if (eventCase_ != 2) {
            event_ = new IpConnectivityLogClass.DefaultNetworkEvent();
          }
          paramCodedInputByteBufferNano.readMessage((MessageNano)event_);
          eventCase_ = 2;
          break;
        case 8: 
          timeMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public IpConnectivityEvent setApfProgramEvent(IpConnectivityLogClass.ApfProgramEvent paramApfProgramEvent)
    {
      if (paramApfProgramEvent != null)
      {
        eventCase_ = 9;
        event_ = paramApfProgramEvent;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setApfStatistics(IpConnectivityLogClass.ApfStatistics paramApfStatistics)
    {
      if (paramApfStatistics != null)
      {
        eventCase_ = 10;
        event_ = paramApfStatistics;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setConnectStatistics(IpConnectivityLogClass.ConnectStatistics paramConnectStatistics)
    {
      if (paramConnectStatistics != null)
      {
        eventCase_ = 14;
        event_ = paramConnectStatistics;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setDefaultNetworkEvent(IpConnectivityLogClass.DefaultNetworkEvent paramDefaultNetworkEvent)
    {
      if (paramDefaultNetworkEvent != null)
      {
        eventCase_ = 2;
        event_ = paramDefaultNetworkEvent;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setDhcpEvent(IpConnectivityLogClass.DHCPEvent paramDHCPEvent)
    {
      if (paramDHCPEvent != null)
      {
        eventCase_ = 6;
        event_ = paramDHCPEvent;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setDnsLatencies(IpConnectivityLogClass.DNSLatencies paramDNSLatencies)
    {
      if (paramDNSLatencies != null)
      {
        eventCase_ = 13;
        event_ = paramDNSLatencies;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setDnsLookupBatch(IpConnectivityLogClass.DNSLookupBatch paramDNSLookupBatch)
    {
      if (paramDNSLookupBatch != null)
      {
        eventCase_ = 5;
        event_ = paramDNSLookupBatch;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setIpProvisioningEvent(IpConnectivityLogClass.IpProvisioningEvent paramIpProvisioningEvent)
    {
      if (paramIpProvisioningEvent != null)
      {
        eventCase_ = 7;
        event_ = paramIpProvisioningEvent;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setIpReachabilityEvent(IpConnectivityLogClass.IpReachabilityEvent paramIpReachabilityEvent)
    {
      if (paramIpReachabilityEvent != null)
      {
        eventCase_ = 3;
        event_ = paramIpReachabilityEvent;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setNetworkEvent(IpConnectivityLogClass.NetworkEvent paramNetworkEvent)
    {
      if (paramNetworkEvent != null)
      {
        eventCase_ = 4;
        event_ = paramNetworkEvent;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setNetworkStats(IpConnectivityLogClass.NetworkStats paramNetworkStats)
    {
      if (paramNetworkStats != null)
      {
        eventCase_ = 19;
        event_ = paramNetworkStats;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setRaEvent(IpConnectivityLogClass.RaEvent paramRaEvent)
    {
      if (paramRaEvent != null)
      {
        eventCase_ = 11;
        event_ = paramRaEvent;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setValidationProbeEvent(IpConnectivityLogClass.ValidationProbeEvent paramValidationProbeEvent)
    {
      if (paramValidationProbeEvent != null)
      {
        eventCase_ = 8;
        event_ = paramValidationProbeEvent;
        return this;
      }
      throw new NullPointerException();
    }
    
    public IpConnectivityEvent setWakeupStats(IpConnectivityLogClass.WakeupStats paramWakeupStats)
    {
      if (paramWakeupStats != null)
      {
        eventCase_ = 20;
        event_ = paramWakeupStats;
        return this;
      }
      throw new NullPointerException();
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (timeMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(1, timeMs);
      }
      if (eventCase_ == 2) {
        paramCodedOutputByteBufferNano.writeMessage(2, (MessageNano)event_);
      }
      if (eventCase_ == 3) {
        paramCodedOutputByteBufferNano.writeMessage(3, (MessageNano)event_);
      }
      if (eventCase_ == 4) {
        paramCodedOutputByteBufferNano.writeMessage(4, (MessageNano)event_);
      }
      if (eventCase_ == 5) {
        paramCodedOutputByteBufferNano.writeMessage(5, (MessageNano)event_);
      }
      if (eventCase_ == 6) {
        paramCodedOutputByteBufferNano.writeMessage(6, (MessageNano)event_);
      }
      if (eventCase_ == 7) {
        paramCodedOutputByteBufferNano.writeMessage(7, (MessageNano)event_);
      }
      if (eventCase_ == 8) {
        paramCodedOutputByteBufferNano.writeMessage(8, (MessageNano)event_);
      }
      if (eventCase_ == 9) {
        paramCodedOutputByteBufferNano.writeMessage(9, (MessageNano)event_);
      }
      if (eventCase_ == 10) {
        paramCodedOutputByteBufferNano.writeMessage(10, (MessageNano)event_);
      }
      if (eventCase_ == 11) {
        paramCodedOutputByteBufferNano.writeMessage(11, (MessageNano)event_);
      }
      if (eventCase_ == 13) {
        paramCodedOutputByteBufferNano.writeMessage(13, (MessageNano)event_);
      }
      if (eventCase_ == 14) {
        paramCodedOutputByteBufferNano.writeMessage(14, (MessageNano)event_);
      }
      if (linkLayer != 0) {
        paramCodedOutputByteBufferNano.writeInt32(15, linkLayer);
      }
      if (networkId != 0) {
        paramCodedOutputByteBufferNano.writeInt32(16, networkId);
      }
      if (!ifName.equals("")) {
        paramCodedOutputByteBufferNano.writeString(17, ifName);
      }
      if (transports != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(18, transports);
      }
      if (eventCase_ == 19) {
        paramCodedOutputByteBufferNano.writeMessage(19, (MessageNano)event_);
      }
      if (eventCase_ == 20) {
        paramCodedOutputByteBufferNano.writeMessage(20, (MessageNano)event_);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class IpConnectivityLog
    extends MessageNano
  {
    private static volatile IpConnectivityLog[] _emptyArray;
    public int droppedEvents;
    public IpConnectivityLogClass.IpConnectivityEvent[] events;
    public int version;
    
    public IpConnectivityLog()
    {
      clear();
    }
    
    public static IpConnectivityLog[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new IpConnectivityLog[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static IpConnectivityLog parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new IpConnectivityLog().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static IpConnectivityLog parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (IpConnectivityLog)MessageNano.mergeFrom(new IpConnectivityLog(), paramArrayOfByte);
    }
    
    public IpConnectivityLog clear()
    {
      events = IpConnectivityLogClass.IpConnectivityEvent.emptyArray();
      droppedEvents = 0;
      version = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
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
            IpConnectivityLogClass.IpConnectivityEvent localIpConnectivityEvent = events[k];
            j = i;
            if (localIpConnectivityEvent != null) {
              j = i + CodedOutputByteBufferNano.computeMessageSize(1, localIpConnectivityEvent);
            }
            k++;
            i = j;
          }
        }
      }
      i = j;
      if (droppedEvents != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, droppedEvents);
      }
      j = i;
      if (version != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, version);
      }
      return j;
    }
    
    public IpConnectivityLog mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 24)
            {
              if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else {
              version = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            droppedEvents = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else
        {
          int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 10);
          if (events == null) {
            i = 0;
          } else {
            i = events.length;
          }
          IpConnectivityLogClass.IpConnectivityEvent[] arrayOfIpConnectivityEvent = new IpConnectivityLogClass.IpConnectivityEvent[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(events, 0, arrayOfIpConnectivityEvent, 0, i);
          }
          for (j = i; j < arrayOfIpConnectivityEvent.length - 1; j++)
          {
            arrayOfIpConnectivityEvent[j] = new IpConnectivityLogClass.IpConnectivityEvent();
            paramCodedInputByteBufferNano.readMessage(arrayOfIpConnectivityEvent[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfIpConnectivityEvent[j] = new IpConnectivityLogClass.IpConnectivityEvent();
          paramCodedInputByteBufferNano.readMessage(arrayOfIpConnectivityEvent[j]);
          events = arrayOfIpConnectivityEvent;
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if ((events != null) && (events.length > 0)) {
        for (int i = 0; i < events.length; i++)
        {
          IpConnectivityLogClass.IpConnectivityEvent localIpConnectivityEvent = events[i];
          if (localIpConnectivityEvent != null) {
            paramCodedOutputByteBufferNano.writeMessage(1, localIpConnectivityEvent);
          }
        }
      }
      if (droppedEvents != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, droppedEvents);
      }
      if (version != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, version);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class IpProvisioningEvent
    extends MessageNano
  {
    private static volatile IpProvisioningEvent[] _emptyArray;
    public int eventType;
    public String ifName;
    public int latencyMs;
    
    public IpProvisioningEvent()
    {
      clear();
    }
    
    public static IpProvisioningEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new IpProvisioningEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static IpProvisioningEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new IpProvisioningEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static IpProvisioningEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (IpProvisioningEvent)MessageNano.mergeFrom(new IpProvisioningEvent(), paramArrayOfByte);
    }
    
    public IpProvisioningEvent clear()
    {
      ifName = "";
      eventType = 0;
      latencyMs = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (!ifName.equals("")) {
        j = i + CodedOutputByteBufferNano.computeStringSize(1, ifName);
      }
      i = j;
      if (eventType != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, eventType);
      }
      j = i;
      if (latencyMs != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, latencyMs);
      }
      return j;
    }
    
    public IpProvisioningEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 24)
            {
              if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else {
              latencyMs = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            eventType = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          ifName = paramCodedInputByteBufferNano.readString();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (!ifName.equals("")) {
        paramCodedOutputByteBufferNano.writeString(1, ifName);
      }
      if (eventType != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, eventType);
      }
      if (latencyMs != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, latencyMs);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class IpReachabilityEvent
    extends MessageNano
  {
    private static volatile IpReachabilityEvent[] _emptyArray;
    public int eventType;
    public String ifName;
    
    public IpReachabilityEvent()
    {
      clear();
    }
    
    public static IpReachabilityEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new IpReachabilityEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static IpReachabilityEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new IpReachabilityEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static IpReachabilityEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (IpReachabilityEvent)MessageNano.mergeFrom(new IpReachabilityEvent(), paramArrayOfByte);
    }
    
    public IpReachabilityEvent clear()
    {
      ifName = "";
      eventType = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (!ifName.equals("")) {
        j = i + CodedOutputByteBufferNano.computeStringSize(1, ifName);
      }
      i = j;
      if (eventType != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, eventType);
      }
      return i;
    }
    
    public IpReachabilityEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
              return this;
            }
          }
          else {
            eventType = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          ifName = paramCodedInputByteBufferNano.readString();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (!ifName.equals("")) {
        paramCodedOutputByteBufferNano.writeString(1, ifName);
      }
      if (eventType != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, eventType);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class NetworkEvent
    extends MessageNano
  {
    private static volatile NetworkEvent[] _emptyArray;
    public int eventType;
    public int latencyMs;
    public IpConnectivityLogClass.NetworkId networkId;
    
    public NetworkEvent()
    {
      clear();
    }
    
    public static NetworkEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new NetworkEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static NetworkEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new NetworkEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static NetworkEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (NetworkEvent)MessageNano.mergeFrom(new NetworkEvent(), paramArrayOfByte);
    }
    
    public NetworkEvent clear()
    {
      networkId = null;
      eventType = 0;
      latencyMs = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (networkId != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(1, networkId);
      }
      i = j;
      if (eventType != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, eventType);
      }
      j = i;
      if (latencyMs != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, latencyMs);
      }
      return j;
    }
    
    public NetworkEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 24)
            {
              if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else {
              latencyMs = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            eventType = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else
        {
          if (networkId == null) {
            networkId = new IpConnectivityLogClass.NetworkId();
          }
          paramCodedInputByteBufferNano.readMessage(networkId);
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (networkId != null) {
        paramCodedOutputByteBufferNano.writeMessage(1, networkId);
      }
      if (eventType != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, eventType);
      }
      if (latencyMs != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, latencyMs);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class NetworkId
    extends MessageNano
  {
    private static volatile NetworkId[] _emptyArray;
    public int networkId;
    
    public NetworkId()
    {
      clear();
    }
    
    public static NetworkId[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new NetworkId[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static NetworkId parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new NetworkId().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static NetworkId parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (NetworkId)MessageNano.mergeFrom(new NetworkId(), paramArrayOfByte);
    }
    
    public NetworkId clear()
    {
      networkId = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (networkId != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, networkId);
      }
      return j;
    }
    
    public NetworkId mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
        }
        else {
          networkId = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (networkId != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, networkId);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class NetworkStats
    extends MessageNano
  {
    private static volatile NetworkStats[] _emptyArray;
    public long durationMs;
    public boolean everValidated;
    public int ipSupport;
    public int noConnectivityReports;
    public boolean portalFound;
    public int validationAttempts;
    public IpConnectivityLogClass.Pair[] validationEvents;
    public IpConnectivityLogClass.Pair[] validationStates;
    
    public NetworkStats()
    {
      clear();
    }
    
    public static NetworkStats[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new NetworkStats[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static NetworkStats parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new NetworkStats().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static NetworkStats parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (NetworkStats)MessageNano.mergeFrom(new NetworkStats(), paramArrayOfByte);
    }
    
    public NetworkStats clear()
    {
      durationMs = 0L;
      ipSupport = 0;
      everValidated = false;
      portalFound = false;
      noConnectivityReports = 0;
      validationAttempts = 0;
      validationEvents = IpConnectivityLogClass.Pair.emptyArray();
      validationStates = IpConnectivityLogClass.Pair.emptyArray();
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (durationMs != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(1, durationMs);
      }
      int k = j;
      if (ipSupport != 0) {
        k = j + CodedOutputByteBufferNano.computeInt32Size(2, ipSupport);
      }
      i = k;
      if (everValidated) {
        i = k + CodedOutputByteBufferNano.computeBoolSize(3, everValidated);
      }
      j = i;
      if (portalFound) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(4, portalFound);
      }
      k = j;
      if (noConnectivityReports != 0) {
        k = j + CodedOutputByteBufferNano.computeInt32Size(5, noConnectivityReports);
      }
      i = k;
      if (validationAttempts != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(6, validationAttempts);
      }
      Object localObject = validationEvents;
      int m = 0;
      j = i;
      if (localObject != null)
      {
        j = i;
        if (validationEvents.length > 0)
        {
          j = i;
          k = 0;
          while (k < validationEvents.length)
          {
            localObject = validationEvents[k];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(7, (MessageNano)localObject);
            }
            k++;
            j = i;
          }
        }
      }
      k = j;
      if (validationStates != null)
      {
        k = j;
        if (validationStates.length > 0)
        {
          i = m;
          for (;;)
          {
            k = j;
            if (i >= validationStates.length) {
              break;
            }
            localObject = validationStates[i];
            k = j;
            if (localObject != null) {
              k = j + CodedOutputByteBufferNano.computeMessageSize(8, (MessageNano)localObject);
            }
            i++;
            j = k;
          }
        }
      }
      return k;
    }
    
    public NetworkStats mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                    int j;
                    IpConnectivityLogClass.Pair[] arrayOfPair;
                    if (i != 58)
                    {
                      if (i != 66)
                      {
                        if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                          return this;
                        }
                      }
                      else
                      {
                        j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 66);
                        if (validationStates == null) {
                          i = 0;
                        } else {
                          i = validationStates.length;
                        }
                        arrayOfPair = new IpConnectivityLogClass.Pair[i + j];
                        j = i;
                        if (i != 0) {
                          System.arraycopy(validationStates, 0, arrayOfPair, 0, i);
                        }
                        for (j = i; j < arrayOfPair.length - 1; j++)
                        {
                          arrayOfPair[j] = new IpConnectivityLogClass.Pair();
                          paramCodedInputByteBufferNano.readMessage(arrayOfPair[j]);
                          paramCodedInputByteBufferNano.readTag();
                        }
                        arrayOfPair[j] = new IpConnectivityLogClass.Pair();
                        paramCodedInputByteBufferNano.readMessage(arrayOfPair[j]);
                        validationStates = arrayOfPair;
                      }
                    }
                    else
                    {
                      j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 58);
                      if (validationEvents == null) {
                        i = 0;
                      } else {
                        i = validationEvents.length;
                      }
                      arrayOfPair = new IpConnectivityLogClass.Pair[i + j];
                      j = i;
                      if (i != 0) {
                        System.arraycopy(validationEvents, 0, arrayOfPair, 0, i);
                      }
                      for (j = i; j < arrayOfPair.length - 1; j++)
                      {
                        arrayOfPair[j] = new IpConnectivityLogClass.Pair();
                        paramCodedInputByteBufferNano.readMessage(arrayOfPair[j]);
                        paramCodedInputByteBufferNano.readTag();
                      }
                      arrayOfPair[j] = new IpConnectivityLogClass.Pair();
                      paramCodedInputByteBufferNano.readMessage(arrayOfPair[j]);
                      validationEvents = arrayOfPair;
                    }
                  }
                  else
                  {
                    validationAttempts = paramCodedInputByteBufferNano.readInt32();
                  }
                }
                else {
                  noConnectivityReports = paramCodedInputByteBufferNano.readInt32();
                }
              }
              else {
                portalFound = paramCodedInputByteBufferNano.readBool();
              }
            }
            else {
              everValidated = paramCodedInputByteBufferNano.readBool();
            }
          }
          else
          {
            i = paramCodedInputByteBufferNano.readInt32();
            switch (i)
            {
            default: 
              break;
            case 0: 
            case 1: 
            case 2: 
            case 3: 
              ipSupport = i;
            }
          }
        }
        else {
          durationMs = paramCodedInputByteBufferNano.readInt64();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (durationMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(1, durationMs);
      }
      if (ipSupport != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, ipSupport);
      }
      if (everValidated) {
        paramCodedOutputByteBufferNano.writeBool(3, everValidated);
      }
      if (portalFound) {
        paramCodedOutputByteBufferNano.writeBool(4, portalFound);
      }
      if (noConnectivityReports != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, noConnectivityReports);
      }
      if (validationAttempts != 0) {
        paramCodedOutputByteBufferNano.writeInt32(6, validationAttempts);
      }
      Object localObject = validationEvents;
      int i = 0;
      int j;
      if ((localObject != null) && (validationEvents.length > 0)) {
        for (j = 0; j < validationEvents.length; j++)
        {
          localObject = validationEvents[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(7, (MessageNano)localObject);
          }
        }
      }
      if ((validationStates != null) && (validationStates.length > 0)) {
        for (j = i; j < validationStates.length; j++)
        {
          localObject = validationStates[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(8, (MessageNano)localObject);
          }
        }
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class Pair
    extends MessageNano
  {
    private static volatile Pair[] _emptyArray;
    public int key;
    public int value;
    
    public Pair()
    {
      clear();
    }
    
    public static Pair[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new Pair[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static Pair parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new Pair().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static Pair parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (Pair)MessageNano.mergeFrom(new Pair(), paramArrayOfByte);
    }
    
    public Pair clear()
    {
      key = 0;
      value = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (key != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, key);
      }
      i = j;
      if (value != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, value);
      }
      return i;
    }
    
    public Pair mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
              return this;
            }
          }
          else {
            value = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          key = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (key != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, key);
      }
      if (value != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, value);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class RaEvent
    extends MessageNano
  {
    private static volatile RaEvent[] _emptyArray;
    public long dnsslLifetime;
    public long prefixPreferredLifetime;
    public long prefixValidLifetime;
    public long rdnssLifetime;
    public long routeInfoLifetime;
    public long routerLifetime;
    
    public RaEvent()
    {
      clear();
    }
    
    public static RaEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new RaEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static RaEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new RaEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static RaEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (RaEvent)MessageNano.mergeFrom(new RaEvent(), paramArrayOfByte);
    }
    
    public RaEvent clear()
    {
      routerLifetime = 0L;
      prefixValidLifetime = 0L;
      prefixPreferredLifetime = 0L;
      routeInfoLifetime = 0L;
      rdnssLifetime = 0L;
      dnsslLifetime = 0L;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (routerLifetime != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(1, routerLifetime);
      }
      i = j;
      if (prefixValidLifetime != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(2, prefixValidLifetime);
      }
      j = i;
      if (prefixPreferredLifetime != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(3, prefixPreferredLifetime);
      }
      i = j;
      if (routeInfoLifetime != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(4, routeInfoLifetime);
      }
      j = i;
      if (rdnssLifetime != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(5, rdnssLifetime);
      }
      i = j;
      if (dnsslLifetime != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(6, dnsslLifetime);
      }
      return i;
    }
    
    public RaEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                    if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                      return this;
                    }
                  }
                  else {
                    dnsslLifetime = paramCodedInputByteBufferNano.readInt64();
                  }
                }
                else {
                  rdnssLifetime = paramCodedInputByteBufferNano.readInt64();
                }
              }
              else {
                routeInfoLifetime = paramCodedInputByteBufferNano.readInt64();
              }
            }
            else {
              prefixPreferredLifetime = paramCodedInputByteBufferNano.readInt64();
            }
          }
          else {
            prefixValidLifetime = paramCodedInputByteBufferNano.readInt64();
          }
        }
        else {
          routerLifetime = paramCodedInputByteBufferNano.readInt64();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (routerLifetime != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(1, routerLifetime);
      }
      if (prefixValidLifetime != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(2, prefixValidLifetime);
      }
      if (prefixPreferredLifetime != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(3, prefixPreferredLifetime);
      }
      if (routeInfoLifetime != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(4, routeInfoLifetime);
      }
      if (rdnssLifetime != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(5, rdnssLifetime);
      }
      if (dnsslLifetime != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(6, dnsslLifetime);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class ValidationProbeEvent
    extends MessageNano
  {
    private static volatile ValidationProbeEvent[] _emptyArray;
    public int latencyMs;
    public IpConnectivityLogClass.NetworkId networkId;
    public int probeResult;
    public int probeType;
    
    public ValidationProbeEvent()
    {
      clear();
    }
    
    public static ValidationProbeEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new ValidationProbeEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static ValidationProbeEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new ValidationProbeEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static ValidationProbeEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (ValidationProbeEvent)MessageNano.mergeFrom(new ValidationProbeEvent(), paramArrayOfByte);
    }
    
    public ValidationProbeEvent clear()
    {
      networkId = null;
      latencyMs = 0;
      probeType = 0;
      probeResult = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (networkId != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(1, networkId);
      }
      i = j;
      if (latencyMs != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, latencyMs);
      }
      j = i;
      if (probeType != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, probeType);
      }
      i = j;
      if (probeResult != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, probeResult);
      }
      return i;
    }
    
    public ValidationProbeEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 24)
            {
              if (i != 32)
              {
                if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else {
                probeResult = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              probeType = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            latencyMs = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else
        {
          if (networkId == null) {
            networkId = new IpConnectivityLogClass.NetworkId();
          }
          paramCodedInputByteBufferNano.readMessage(networkId);
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (networkId != null) {
        paramCodedOutputByteBufferNano.writeMessage(1, networkId);
      }
      if (latencyMs != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, latencyMs);
      }
      if (probeType != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, probeType);
      }
      if (probeResult != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, probeResult);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class WakeupStats
    extends MessageNano
  {
    private static volatile WakeupStats[] _emptyArray;
    public long applicationWakeups;
    public long durationSec;
    public IpConnectivityLogClass.Pair[] ethertypeCounts;
    public IpConnectivityLogClass.Pair[] ipNextHeaderCounts;
    public long l2BroadcastCount;
    public long l2MulticastCount;
    public long l2UnicastCount;
    public long noUidWakeups;
    public long nonApplicationWakeups;
    public long rootWakeups;
    public long systemWakeups;
    public long totalWakeups;
    
    public WakeupStats()
    {
      clear();
    }
    
    public static WakeupStats[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new WakeupStats[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static WakeupStats parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new WakeupStats().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static WakeupStats parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (WakeupStats)MessageNano.mergeFrom(new WakeupStats(), paramArrayOfByte);
    }
    
    public WakeupStats clear()
    {
      durationSec = 0L;
      totalWakeups = 0L;
      rootWakeups = 0L;
      systemWakeups = 0L;
      applicationWakeups = 0L;
      nonApplicationWakeups = 0L;
      noUidWakeups = 0L;
      ethertypeCounts = IpConnectivityLogClass.Pair.emptyArray();
      ipNextHeaderCounts = IpConnectivityLogClass.Pair.emptyArray();
      l2UnicastCount = 0L;
      l2MulticastCount = 0L;
      l2BroadcastCount = 0L;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (durationSec != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(1, durationSec);
      }
      int k = j;
      if (totalWakeups != 0L) {
        k = j + CodedOutputByteBufferNano.computeInt64Size(2, totalWakeups);
      }
      i = k;
      if (rootWakeups != 0L) {
        i = k + CodedOutputByteBufferNano.computeInt64Size(3, rootWakeups);
      }
      j = i;
      if (systemWakeups != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(4, systemWakeups);
      }
      i = j;
      if (applicationWakeups != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(5, applicationWakeups);
      }
      j = i;
      if (nonApplicationWakeups != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(6, nonApplicationWakeups);
      }
      i = j;
      if (noUidWakeups != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(7, noUidWakeups);
      }
      Object localObject = ethertypeCounts;
      int m = 0;
      j = i;
      if (localObject != null)
      {
        j = i;
        if (ethertypeCounts.length > 0)
        {
          j = i;
          k = 0;
          while (k < ethertypeCounts.length)
          {
            localObject = ethertypeCounts[k];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(8, (MessageNano)localObject);
            }
            k++;
            j = i;
          }
        }
      }
      i = j;
      if (ipNextHeaderCounts != null)
      {
        i = j;
        if (ipNextHeaderCounts.length > 0)
        {
          k = m;
          for (;;)
          {
            i = j;
            if (k >= ipNextHeaderCounts.length) {
              break;
            }
            localObject = ipNextHeaderCounts[k];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(9, (MessageNano)localObject);
            }
            k++;
            j = i;
          }
        }
      }
      j = i;
      if (l2UnicastCount != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(10, l2UnicastCount);
      }
      i = j;
      if (l2MulticastCount != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(11, l2MulticastCount);
      }
      j = i;
      if (l2BroadcastCount != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(12, l2BroadcastCount);
      }
      return j;
    }
    
    public WakeupStats mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        int j;
        IpConnectivityLogClass.Pair[] arrayOfPair;
        switch (i)
        {
        default: 
          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 96: 
          l2BroadcastCount = paramCodedInputByteBufferNano.readInt64();
          break;
        case 88: 
          l2MulticastCount = paramCodedInputByteBufferNano.readInt64();
          break;
        case 80: 
          l2UnicastCount = paramCodedInputByteBufferNano.readInt64();
          break;
        case 74: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 74);
          if (ipNextHeaderCounts == null) {
            i = 0;
          } else {
            i = ipNextHeaderCounts.length;
          }
          arrayOfPair = new IpConnectivityLogClass.Pair[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(ipNextHeaderCounts, 0, arrayOfPair, 0, i);
          }
          for (j = i; j < arrayOfPair.length - 1; j++)
          {
            arrayOfPair[j] = new IpConnectivityLogClass.Pair();
            paramCodedInputByteBufferNano.readMessage(arrayOfPair[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfPair[j] = new IpConnectivityLogClass.Pair();
          paramCodedInputByteBufferNano.readMessage(arrayOfPair[j]);
          ipNextHeaderCounts = arrayOfPair;
          break;
        case 66: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 66);
          if (ethertypeCounts == null) {
            i = 0;
          } else {
            i = ethertypeCounts.length;
          }
          arrayOfPair = new IpConnectivityLogClass.Pair[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(ethertypeCounts, 0, arrayOfPair, 0, i);
          }
          for (j = i; j < arrayOfPair.length - 1; j++)
          {
            arrayOfPair[j] = new IpConnectivityLogClass.Pair();
            paramCodedInputByteBufferNano.readMessage(arrayOfPair[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          arrayOfPair[j] = new IpConnectivityLogClass.Pair();
          paramCodedInputByteBufferNano.readMessage(arrayOfPair[j]);
          ethertypeCounts = arrayOfPair;
          break;
        case 56: 
          noUidWakeups = paramCodedInputByteBufferNano.readInt64();
          break;
        case 48: 
          nonApplicationWakeups = paramCodedInputByteBufferNano.readInt64();
          break;
        case 40: 
          applicationWakeups = paramCodedInputByteBufferNano.readInt64();
          break;
        case 32: 
          systemWakeups = paramCodedInputByteBufferNano.readInt64();
          break;
        case 24: 
          rootWakeups = paramCodedInputByteBufferNano.readInt64();
          break;
        case 16: 
          totalWakeups = paramCodedInputByteBufferNano.readInt64();
          break;
        case 8: 
          durationSec = paramCodedInputByteBufferNano.readInt64();
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (durationSec != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(1, durationSec);
      }
      if (totalWakeups != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(2, totalWakeups);
      }
      if (rootWakeups != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(3, rootWakeups);
      }
      if (systemWakeups != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(4, systemWakeups);
      }
      if (applicationWakeups != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(5, applicationWakeups);
      }
      if (nonApplicationWakeups != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(6, nonApplicationWakeups);
      }
      if (noUidWakeups != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(7, noUidWakeups);
      }
      Object localObject = ethertypeCounts;
      int i = 0;
      int j;
      if ((localObject != null) && (ethertypeCounts.length > 0)) {
        for (j = 0; j < ethertypeCounts.length; j++)
        {
          localObject = ethertypeCounts[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(8, (MessageNano)localObject);
          }
        }
      }
      if ((ipNextHeaderCounts != null) && (ipNextHeaderCounts.length > 0)) {
        for (j = i; j < ipNextHeaderCounts.length; j++)
        {
          localObject = ipNextHeaderCounts[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(9, (MessageNano)localObject);
          }
        }
      }
      if (l2UnicastCount != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(10, l2UnicastCount);
      }
      if (l2MulticastCount != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(11, l2MulticastCount);
      }
      if (l2BroadcastCount != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(12, l2BroadcastCount);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
}
