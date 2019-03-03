package com.android.server.wifi.nano;

import com.android.framework.protobuf.nano.CodedInputByteBufferNano;
import com.android.framework.protobuf.nano.CodedOutputByteBufferNano;
import com.android.framework.protobuf.nano.InternalNano;
import com.android.framework.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.framework.protobuf.nano.MessageNano;
import com.android.framework.protobuf.nano.WireFormatNano;
import java.io.IOException;

public abstract interface WifiMetricsProto
{
  public static final class AlertReasonCount
    extends MessageNano
  {
    private static volatile AlertReasonCount[] _emptyArray;
    public int count;
    public int reason;
    
    public AlertReasonCount()
    {
      clear();
    }
    
    public static AlertReasonCount[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new AlertReasonCount[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static AlertReasonCount parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new AlertReasonCount().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static AlertReasonCount parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (AlertReasonCount)MessageNano.mergeFrom(new AlertReasonCount(), paramArrayOfByte);
    }
    
    public AlertReasonCount clear()
    {
      reason = 0;
      count = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (reason != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, reason);
      }
      i = j;
      if (count != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, count);
      }
      return i;
    }
    
    public AlertReasonCount mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            count = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          reason = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (reason != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, reason);
      }
      if (count != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, count);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class ConnectToNetworkNotificationAndActionCount
    extends MessageNano
  {
    public static final int ACTION_CONNECT_TO_NETWORK = 2;
    public static final int ACTION_PICK_WIFI_NETWORK = 3;
    public static final int ACTION_PICK_WIFI_NETWORK_AFTER_CONNECT_FAILURE = 4;
    public static final int ACTION_UNKNOWN = 0;
    public static final int ACTION_USER_DISMISSED_NOTIFICATION = 1;
    public static final int NOTIFICATION_CONNECTED_TO_NETWORK = 3;
    public static final int NOTIFICATION_CONNECTING_TO_NETWORK = 2;
    public static final int NOTIFICATION_FAILED_TO_CONNECT = 4;
    public static final int NOTIFICATION_RECOMMEND_NETWORK = 1;
    public static final int NOTIFICATION_UNKNOWN = 0;
    public static final int RECOMMENDER_OPEN = 1;
    public static final int RECOMMENDER_UNKNOWN = 0;
    private static volatile ConnectToNetworkNotificationAndActionCount[] _emptyArray;
    public int action;
    public int count;
    public int notification;
    public int recommender;
    
    public ConnectToNetworkNotificationAndActionCount()
    {
      clear();
    }
    
    public static ConnectToNetworkNotificationAndActionCount[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new ConnectToNetworkNotificationAndActionCount[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static ConnectToNetworkNotificationAndActionCount parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new ConnectToNetworkNotificationAndActionCount().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static ConnectToNetworkNotificationAndActionCount parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (ConnectToNetworkNotificationAndActionCount)MessageNano.mergeFrom(new ConnectToNetworkNotificationAndActionCount(), paramArrayOfByte);
    }
    
    public ConnectToNetworkNotificationAndActionCount clear()
    {
      notification = 0;
      action = 0;
      recommender = 0;
      count = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (notification != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, notification);
      }
      i = j;
      if (action != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, action);
      }
      j = i;
      if (recommender != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, recommender);
      }
      i = j;
      if (count != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, count);
      }
      return i;
    }
    
    public ConnectToNetworkNotificationAndActionCount mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else {
                count = paramCodedInputByteBufferNano.readInt32();
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
                recommender = i;
              }
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
            case 4: 
              action = i;
            }
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
          case 4: 
            notification = i;
          }
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (notification != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, notification);
      }
      if (action != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, action);
      }
      if (recommender != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, recommender);
      }
      if (count != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, count);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class ConnectionEvent
    extends MessageNano
  {
    public static final int HLF_DHCP = 2;
    public static final int HLF_NONE = 1;
    public static final int HLF_NO_INTERNET = 3;
    public static final int HLF_UNKNOWN = 0;
    public static final int HLF_UNWANTED = 4;
    public static final int ROAM_DBDC = 2;
    public static final int ROAM_ENTERPRISE = 3;
    public static final int ROAM_NONE = 1;
    public static final int ROAM_UNKNOWN = 0;
    public static final int ROAM_UNRELATED = 5;
    public static final int ROAM_USER_SELECTED = 4;
    private static volatile ConnectionEvent[] _emptyArray;
    public boolean automaticBugReportTaken;
    public int connectionResult;
    public int connectivityLevelFailureCode;
    public int durationTakenToConnectMillis;
    public int level2FailureCode;
    public int roamType;
    public WifiMetricsProto.RouterFingerPrint routerFingerprint;
    public int signalStrength;
    public long startTimeMillis;
    
    public ConnectionEvent()
    {
      clear();
    }
    
    public static ConnectionEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new ConnectionEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static ConnectionEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new ConnectionEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static ConnectionEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (ConnectionEvent)MessageNano.mergeFrom(new ConnectionEvent(), paramArrayOfByte);
    }
    
    public ConnectionEvent clear()
    {
      startTimeMillis = 0L;
      durationTakenToConnectMillis = 0;
      routerFingerprint = null;
      signalStrength = 0;
      roamType = 0;
      connectionResult = 0;
      level2FailureCode = 0;
      connectivityLevelFailureCode = 0;
      automaticBugReportTaken = false;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (startTimeMillis != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(1, startTimeMillis);
      }
      i = j;
      if (durationTakenToConnectMillis != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, durationTakenToConnectMillis);
      }
      j = i;
      if (routerFingerprint != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(3, routerFingerprint);
      }
      i = j;
      if (signalStrength != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, signalStrength);
      }
      j = i;
      if (roamType != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(5, roamType);
      }
      i = j;
      if (connectionResult != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(6, connectionResult);
      }
      j = i;
      if (level2FailureCode != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(7, level2FailureCode);
      }
      i = j;
      if (connectivityLevelFailureCode != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(8, connectivityLevelFailureCode);
      }
      j = i;
      if (automaticBugReportTaken) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(9, automaticBugReportTaken);
      }
      return j;
    }
    
    public ConnectionEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                if (i != 40)
                {
                  if (i != 48)
                  {
                    if (i != 56)
                    {
                      if (i != 64)
                      {
                        if (i != 72)
                        {
                          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                            return this;
                          }
                        }
                        else {
                          automaticBugReportTaken = paramCodedInputByteBufferNano.readBool();
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
                        case 4: 
                          connectivityLevelFailureCode = i;
                        }
                      }
                    }
                    else {
                      level2FailureCode = paramCodedInputByteBufferNano.readInt32();
                    }
                  }
                  else {
                    connectionResult = paramCodedInputByteBufferNano.readInt32();
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
                  case 4: 
                  case 5: 
                    roamType = i;
                  }
                }
              }
              else {
                signalStrength = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else
            {
              if (routerFingerprint == null) {
                routerFingerprint = new WifiMetricsProto.RouterFingerPrint();
              }
              paramCodedInputByteBufferNano.readMessage(routerFingerprint);
            }
          }
          else {
            durationTakenToConnectMillis = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          startTimeMillis = paramCodedInputByteBufferNano.readInt64();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (startTimeMillis != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(1, startTimeMillis);
      }
      if (durationTakenToConnectMillis != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, durationTakenToConnectMillis);
      }
      if (routerFingerprint != null) {
        paramCodedOutputByteBufferNano.writeMessage(3, routerFingerprint);
      }
      if (signalStrength != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, signalStrength);
      }
      if (roamType != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, roamType);
      }
      if (connectionResult != 0) {
        paramCodedOutputByteBufferNano.writeInt32(6, connectionResult);
      }
      if (level2FailureCode != 0) {
        paramCodedOutputByteBufferNano.writeInt32(7, level2FailureCode);
      }
      if (connectivityLevelFailureCode != 0) {
        paramCodedOutputByteBufferNano.writeInt32(8, connectivityLevelFailureCode);
      }
      if (automaticBugReportTaken) {
        paramCodedOutputByteBufferNano.writeBool(9, automaticBugReportTaken);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class NumConnectableNetworksBucket
    extends MessageNano
  {
    private static volatile NumConnectableNetworksBucket[] _emptyArray;
    public int count;
    public int numConnectableNetworks;
    
    public NumConnectableNetworksBucket()
    {
      clear();
    }
    
    public static NumConnectableNetworksBucket[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new NumConnectableNetworksBucket[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static NumConnectableNetworksBucket parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new NumConnectableNetworksBucket().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static NumConnectableNetworksBucket parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (NumConnectableNetworksBucket)MessageNano.mergeFrom(new NumConnectableNetworksBucket(), paramArrayOfByte);
    }
    
    public NumConnectableNetworksBucket clear()
    {
      numConnectableNetworks = 0;
      count = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (numConnectableNetworks != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, numConnectableNetworks);
      }
      i = j;
      if (count != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, count);
      }
      return i;
    }
    
    public NumConnectableNetworksBucket mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            count = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          numConnectableNetworks = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (numConnectableNetworks != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, numConnectableNetworks);
      }
      if (count != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, count);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class PnoScanMetrics
    extends MessageNano
  {
    private static volatile PnoScanMetrics[] _emptyArray;
    public int numPnoFoundNetworkEvents;
    public int numPnoScanAttempts;
    public int numPnoScanFailed;
    public int numPnoScanFailedOverOffload;
    public int numPnoScanStartedOverOffload;
    
    public PnoScanMetrics()
    {
      clear();
    }
    
    public static PnoScanMetrics[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new PnoScanMetrics[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static PnoScanMetrics parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new PnoScanMetrics().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static PnoScanMetrics parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (PnoScanMetrics)MessageNano.mergeFrom(new PnoScanMetrics(), paramArrayOfByte);
    }
    
    public PnoScanMetrics clear()
    {
      numPnoScanAttempts = 0;
      numPnoScanFailed = 0;
      numPnoScanStartedOverOffload = 0;
      numPnoScanFailedOverOffload = 0;
      numPnoFoundNetworkEvents = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (numPnoScanAttempts != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, numPnoScanAttempts);
      }
      i = j;
      if (numPnoScanFailed != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, numPnoScanFailed);
      }
      j = i;
      if (numPnoScanStartedOverOffload != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, numPnoScanStartedOverOffload);
      }
      i = j;
      if (numPnoScanFailedOverOffload != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, numPnoScanFailedOverOffload);
      }
      j = i;
      if (numPnoFoundNetworkEvents != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(5, numPnoFoundNetworkEvents);
      }
      return j;
    }
    
    public PnoScanMetrics mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                  if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                    return this;
                  }
                }
                else {
                  numPnoFoundNetworkEvents = paramCodedInputByteBufferNano.readInt32();
                }
              }
              else {
                numPnoScanFailedOverOffload = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              numPnoScanStartedOverOffload = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            numPnoScanFailed = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          numPnoScanAttempts = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (numPnoScanAttempts != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, numPnoScanAttempts);
      }
      if (numPnoScanFailed != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, numPnoScanFailed);
      }
      if (numPnoScanStartedOverOffload != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, numPnoScanStartedOverOffload);
      }
      if (numPnoScanFailedOverOffload != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, numPnoScanFailedOverOffload);
      }
      if (numPnoFoundNetworkEvents != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, numPnoFoundNetworkEvents);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class RouterFingerPrint
    extends MessageNano
  {
    public static final int AUTH_ENTERPRISE = 3;
    public static final int AUTH_OPEN = 1;
    public static final int AUTH_PERSONAL = 2;
    public static final int AUTH_UNKNOWN = 0;
    public static final int ROAM_TYPE_DBDC = 3;
    public static final int ROAM_TYPE_ENTERPRISE = 2;
    public static final int ROAM_TYPE_NONE = 1;
    public static final int ROAM_TYPE_UNKNOWN = 0;
    public static final int ROUTER_TECH_A = 1;
    public static final int ROUTER_TECH_AC = 5;
    public static final int ROUTER_TECH_B = 2;
    public static final int ROUTER_TECH_G = 3;
    public static final int ROUTER_TECH_N = 4;
    public static final int ROUTER_TECH_OTHER = 6;
    public static final int ROUTER_TECH_UNKNOWN = 0;
    private static volatile RouterFingerPrint[] _emptyArray;
    public int authentication;
    public int channelInfo;
    public int dtim;
    public boolean hidden;
    public boolean passpoint;
    public int roamType;
    public int routerTechnology;
    public boolean supportsIpv6;
    
    public RouterFingerPrint()
    {
      clear();
    }
    
    public static RouterFingerPrint[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new RouterFingerPrint[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static RouterFingerPrint parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new RouterFingerPrint().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static RouterFingerPrint parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (RouterFingerPrint)MessageNano.mergeFrom(new RouterFingerPrint(), paramArrayOfByte);
    }
    
    public RouterFingerPrint clear()
    {
      roamType = 0;
      channelInfo = 0;
      dtim = 0;
      authentication = 0;
      hidden = false;
      routerTechnology = 0;
      supportsIpv6 = false;
      passpoint = false;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (roamType != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, roamType);
      }
      i = j;
      if (channelInfo != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, channelInfo);
      }
      j = i;
      if (dtim != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, dtim);
      }
      i = j;
      if (authentication != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, authentication);
      }
      j = i;
      if (hidden) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(5, hidden);
      }
      i = j;
      if (routerTechnology != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(6, routerTechnology);
      }
      j = i;
      if (supportsIpv6) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(7, supportsIpv6);
      }
      i = j;
      if (passpoint) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(8, passpoint);
      }
      return i;
    }
    
    public RouterFingerPrint mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                      if (i != 64)
                      {
                        if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                          return this;
                        }
                      }
                      else {
                        passpoint = paramCodedInputByteBufferNano.readBool();
                      }
                    }
                    else {
                      supportsIpv6 = paramCodedInputByteBufferNano.readBool();
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
                    case 4: 
                    case 5: 
                    case 6: 
                      routerTechnology = i;
                    }
                  }
                }
                else {
                  hidden = paramCodedInputByteBufferNano.readBool();
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
                  authentication = i;
                }
              }
            }
            else {
              dtim = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            channelInfo = paramCodedInputByteBufferNano.readInt32();
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
            roamType = i;
          }
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (roamType != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, roamType);
      }
      if (channelInfo != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, channelInfo);
      }
      if (dtim != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, dtim);
      }
      if (authentication != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, authentication);
      }
      if (hidden) {
        paramCodedOutputByteBufferNano.writeBool(5, hidden);
      }
      if (routerTechnology != 0) {
        paramCodedOutputByteBufferNano.writeInt32(6, routerTechnology);
      }
      if (supportsIpv6) {
        paramCodedOutputByteBufferNano.writeBool(7, supportsIpv6);
      }
      if (passpoint) {
        paramCodedOutputByteBufferNano.writeBool(8, passpoint);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class RssiPollCount
    extends MessageNano
  {
    private static volatile RssiPollCount[] _emptyArray;
    public int count;
    public int frequency;
    public int rssi;
    
    public RssiPollCount()
    {
      clear();
    }
    
    public static RssiPollCount[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new RssiPollCount[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static RssiPollCount parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new RssiPollCount().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static RssiPollCount parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (RssiPollCount)MessageNano.mergeFrom(new RssiPollCount(), paramArrayOfByte);
    }
    
    public RssiPollCount clear()
    {
      rssi = 0;
      count = 0;
      frequency = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (rssi != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, rssi);
      }
      i = j;
      if (count != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, count);
      }
      j = i;
      if (frequency != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, frequency);
      }
      return j;
    }
    
    public RssiPollCount mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else {
              frequency = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            count = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          rssi = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (rssi != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, rssi);
      }
      if (count != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, count);
      }
      if (frequency != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, frequency);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class SoftApConnectedClientsEvent
    extends MessageNano
  {
    public static final int BANDWIDTH_160 = 6;
    public static final int BANDWIDTH_20 = 2;
    public static final int BANDWIDTH_20_NOHT = 1;
    public static final int BANDWIDTH_40 = 3;
    public static final int BANDWIDTH_80 = 4;
    public static final int BANDWIDTH_80P80 = 5;
    public static final int BANDWIDTH_INVALID = 0;
    public static final int NUM_CLIENTS_CHANGED = 2;
    public static final int SOFT_AP_DOWN = 1;
    public static final int SOFT_AP_UP = 0;
    private static volatile SoftApConnectedClientsEvent[] _emptyArray;
    public int channelBandwidth;
    public int channelFrequency;
    public int eventType;
    public int numConnectedClients;
    public long timeStampMillis;
    
    public SoftApConnectedClientsEvent()
    {
      clear();
    }
    
    public static SoftApConnectedClientsEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new SoftApConnectedClientsEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static SoftApConnectedClientsEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new SoftApConnectedClientsEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static SoftApConnectedClientsEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (SoftApConnectedClientsEvent)MessageNano.mergeFrom(new SoftApConnectedClientsEvent(), paramArrayOfByte);
    }
    
    public SoftApConnectedClientsEvent clear()
    {
      eventType = 0;
      timeStampMillis = 0L;
      numConnectedClients = 0;
      channelFrequency = 0;
      channelBandwidth = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (eventType != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, eventType);
      }
      i = j;
      if (timeStampMillis != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(2, timeStampMillis);
      }
      int k = i;
      if (numConnectedClients != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(3, numConnectedClients);
      }
      j = k;
      if (channelFrequency != 0) {
        j = k + CodedOutputByteBufferNano.computeInt32Size(4, channelFrequency);
      }
      i = j;
      if (channelBandwidth != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(5, channelBandwidth);
      }
      return i;
    }
    
    public SoftApConnectedClientsEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                  if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                    return this;
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
                  case 4: 
                  case 5: 
                  case 6: 
                    channelBandwidth = i;
                  }
                }
              }
              else {
                channelFrequency = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              numConnectedClients = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            timeStampMillis = paramCodedInputByteBufferNano.readInt64();
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
            eventType = i;
          }
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (eventType != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, eventType);
      }
      if (timeStampMillis != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(2, timeStampMillis);
      }
      if (numConnectedClients != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, numConnectedClients);
      }
      if (channelFrequency != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, channelFrequency);
      }
      if (channelBandwidth != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, channelBandwidth);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class SoftApDurationBucket
    extends MessageNano
  {
    private static volatile SoftApDurationBucket[] _emptyArray;
    public int bucketSizeSec;
    public int count;
    public int durationSec;
    
    public SoftApDurationBucket()
    {
      clear();
    }
    
    public static SoftApDurationBucket[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new SoftApDurationBucket[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static SoftApDurationBucket parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new SoftApDurationBucket().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static SoftApDurationBucket parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (SoftApDurationBucket)MessageNano.mergeFrom(new SoftApDurationBucket(), paramArrayOfByte);
    }
    
    public SoftApDurationBucket clear()
    {
      durationSec = 0;
      bucketSizeSec = 0;
      count = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (durationSec != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, durationSec);
      }
      i = j;
      if (bucketSizeSec != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, bucketSizeSec);
      }
      j = i;
      if (count != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, count);
      }
      return j;
    }
    
    public SoftApDurationBucket mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
              }
            }
            else {
              count = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            bucketSizeSec = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          durationSec = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (durationSec != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, durationSec);
      }
      if (bucketSizeSec != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, bucketSizeSec);
      }
      if (count != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, count);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class SoftApReturnCodeCount
    extends MessageNano
  {
    public static final int SOFT_AP_FAILED_GENERAL_ERROR = 2;
    public static final int SOFT_AP_FAILED_NO_CHANNEL = 3;
    public static final int SOFT_AP_RETURN_CODE_UNKNOWN = 0;
    public static final int SOFT_AP_STARTED_SUCCESSFULLY = 1;
    private static volatile SoftApReturnCodeCount[] _emptyArray;
    public int count;
    public int returnCode;
    public int startResult;
    
    public SoftApReturnCodeCount()
    {
      clear();
    }
    
    public static SoftApReturnCodeCount[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new SoftApReturnCodeCount[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static SoftApReturnCodeCount parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new SoftApReturnCodeCount().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static SoftApReturnCodeCount parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (SoftApReturnCodeCount)MessageNano.mergeFrom(new SoftApReturnCodeCount(), paramArrayOfByte);
    }
    
    public SoftApReturnCodeCount clear()
    {
      returnCode = 0;
      count = 0;
      startResult = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (returnCode != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, returnCode);
      }
      i = j;
      if (count != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, count);
      }
      j = i;
      if (startResult != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, startResult);
      }
      return j;
    }
    
    public SoftApReturnCodeCount mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                return this;
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
                startResult = i;
              }
            }
          }
          else {
            count = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          returnCode = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (returnCode != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, returnCode);
      }
      if (count != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, count);
      }
      if (startResult != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, startResult);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class StaEvent
    extends MessageNano
  {
    public static final int AUTH_FAILURE_EAP_FAILURE = 4;
    public static final int AUTH_FAILURE_NONE = 1;
    public static final int AUTH_FAILURE_TIMEOUT = 2;
    public static final int AUTH_FAILURE_UNKNOWN = 0;
    public static final int AUTH_FAILURE_WRONG_PSWD = 3;
    public static final int DISCONNECT_API = 1;
    public static final int DISCONNECT_GENERIC = 2;
    public static final int DISCONNECT_P2P_DISCONNECT_WIFI_REQUEST = 5;
    public static final int DISCONNECT_RESET_SIM_NETWORKS = 6;
    public static final int DISCONNECT_ROAM_WATCHDOG_TIMER = 4;
    public static final int DISCONNECT_UNKNOWN = 0;
    public static final int DISCONNECT_UNWANTED = 3;
    public static final int STATE_ASSOCIATED = 6;
    public static final int STATE_ASSOCIATING = 5;
    public static final int STATE_AUTHENTICATING = 4;
    public static final int STATE_COMPLETED = 9;
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_DORMANT = 10;
    public static final int STATE_FOUR_WAY_HANDSHAKE = 7;
    public static final int STATE_GROUP_HANDSHAKE = 8;
    public static final int STATE_INACTIVE = 2;
    public static final int STATE_INTERFACE_DISABLED = 1;
    public static final int STATE_INVALID = 12;
    public static final int STATE_SCANNING = 3;
    public static final int STATE_UNINITIALIZED = 11;
    public static final int TYPE_ASSOCIATION_REJECTION_EVENT = 1;
    public static final int TYPE_AUTHENTICATION_FAILURE_EVENT = 2;
    public static final int TYPE_CMD_ASSOCIATED_BSSID = 6;
    public static final int TYPE_CMD_IP_CONFIGURATION_LOST = 8;
    public static final int TYPE_CMD_IP_CONFIGURATION_SUCCESSFUL = 7;
    public static final int TYPE_CMD_IP_REACHABILITY_LOST = 9;
    public static final int TYPE_CMD_START_CONNECT = 11;
    public static final int TYPE_CMD_START_ROAM = 12;
    public static final int TYPE_CMD_TARGET_BSSID = 10;
    public static final int TYPE_CONNECT_NETWORK = 13;
    public static final int TYPE_FRAMEWORK_DISCONNECT = 15;
    public static final int TYPE_MAC_CHANGE = 17;
    public static final int TYPE_NETWORK_AGENT_VALID_NETWORK = 14;
    public static final int TYPE_NETWORK_CONNECTION_EVENT = 3;
    public static final int TYPE_NETWORK_DISCONNECTION_EVENT = 4;
    public static final int TYPE_SCORE_BREACH = 16;
    public static final int TYPE_SUPPLICANT_STATE_CHANGE_EVENT = 5;
    public static final int TYPE_UNKNOWN = 0;
    private static volatile StaEvent[] _emptyArray;
    public boolean associationTimedOut;
    public int authFailureReason;
    public ConfigInfo configInfo;
    public int frameworkDisconnectReason;
    public int lastFreq;
    public int lastLinkSpeed;
    public int lastRssi;
    public int lastScore;
    public boolean localGen;
    public int reason;
    public long startTimeMillis;
    public int status;
    public int supplicantStateChangesBitmask;
    public int type;
    
    public StaEvent()
    {
      clear();
    }
    
    public static StaEvent[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new StaEvent[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static StaEvent parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new StaEvent().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static StaEvent parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (StaEvent)MessageNano.mergeFrom(new StaEvent(), paramArrayOfByte);
    }
    
    public StaEvent clear()
    {
      type = 0;
      reason = -1;
      status = -1;
      localGen = false;
      configInfo = null;
      lastRssi = -127;
      lastLinkSpeed = -1;
      lastFreq = -1;
      supplicantStateChangesBitmask = 0;
      startTimeMillis = 0L;
      frameworkDisconnectReason = 0;
      associationTimedOut = false;
      authFailureReason = 0;
      lastScore = -1;
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
      if (reason != -1) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, reason);
      }
      j = i;
      if (status != -1) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, status);
      }
      int k = j;
      if (localGen) {
        k = j + CodedOutputByteBufferNano.computeBoolSize(4, localGen);
      }
      i = k;
      if (configInfo != null) {
        i = k + CodedOutputByteBufferNano.computeMessageSize(5, configInfo);
      }
      j = i;
      if (lastRssi != -127) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(6, lastRssi);
      }
      k = j;
      if (lastLinkSpeed != -1) {
        k = j + CodedOutputByteBufferNano.computeInt32Size(7, lastLinkSpeed);
      }
      i = k;
      if (lastFreq != -1) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(8, lastFreq);
      }
      k = i;
      if (supplicantStateChangesBitmask != 0) {
        k = i + CodedOutputByteBufferNano.computeUInt32Size(9, supplicantStateChangesBitmask);
      }
      j = k;
      if (startTimeMillis != 0L) {
        j = k + CodedOutputByteBufferNano.computeInt64Size(10, startTimeMillis);
      }
      i = j;
      if (frameworkDisconnectReason != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(11, frameworkDisconnectReason);
      }
      j = i;
      if (associationTimedOut) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(12, associationTimedOut);
      }
      i = j;
      if (authFailureReason != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(13, authFailureReason);
      }
      j = i;
      if (lastScore != -1) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(14, lastScore);
      }
      return j;
    }
    
    public StaEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
        case 112: 
          lastScore = paramCodedInputByteBufferNano.readInt32();
          break;
        case 104: 
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
            authFailureReason = i;
          }
          break;
        case 96: 
          associationTimedOut = paramCodedInputByteBufferNano.readBool();
          break;
        case 88: 
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
            frameworkDisconnectReason = i;
          }
          break;
        case 80: 
          startTimeMillis = paramCodedInputByteBufferNano.readInt64();
          break;
        case 72: 
          supplicantStateChangesBitmask = paramCodedInputByteBufferNano.readUInt32();
          break;
        case 64: 
          lastFreq = paramCodedInputByteBufferNano.readInt32();
          break;
        case 56: 
          lastLinkSpeed = paramCodedInputByteBufferNano.readInt32();
          break;
        case 48: 
          lastRssi = paramCodedInputByteBufferNano.readInt32();
          break;
        case 42: 
          if (configInfo == null) {
            configInfo = new ConfigInfo();
          }
          paramCodedInputByteBufferNano.readMessage(configInfo);
          break;
        case 32: 
          localGen = paramCodedInputByteBufferNano.readBool();
          break;
        case 24: 
          status = paramCodedInputByteBufferNano.readInt32();
          break;
        case 16: 
          reason = paramCodedInputByteBufferNano.readInt32();
          break;
        case 8: 
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
          case 10: 
          case 11: 
          case 12: 
          case 13: 
          case 14: 
          case 15: 
          case 16: 
          case 17: 
            type = i;
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
      if (reason != -1) {
        paramCodedOutputByteBufferNano.writeInt32(2, reason);
      }
      if (status != -1) {
        paramCodedOutputByteBufferNano.writeInt32(3, status);
      }
      if (localGen) {
        paramCodedOutputByteBufferNano.writeBool(4, localGen);
      }
      if (configInfo != null) {
        paramCodedOutputByteBufferNano.writeMessage(5, configInfo);
      }
      if (lastRssi != -127) {
        paramCodedOutputByteBufferNano.writeInt32(6, lastRssi);
      }
      if (lastLinkSpeed != -1) {
        paramCodedOutputByteBufferNano.writeInt32(7, lastLinkSpeed);
      }
      if (lastFreq != -1) {
        paramCodedOutputByteBufferNano.writeInt32(8, lastFreq);
      }
      if (supplicantStateChangesBitmask != 0) {
        paramCodedOutputByteBufferNano.writeUInt32(9, supplicantStateChangesBitmask);
      }
      if (startTimeMillis != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(10, startTimeMillis);
      }
      if (frameworkDisconnectReason != 0) {
        paramCodedOutputByteBufferNano.writeInt32(11, frameworkDisconnectReason);
      }
      if (associationTimedOut) {
        paramCodedOutputByteBufferNano.writeBool(12, associationTimedOut);
      }
      if (authFailureReason != 0) {
        paramCodedOutputByteBufferNano.writeInt32(13, authFailureReason);
      }
      if (lastScore != -1) {
        paramCodedOutputByteBufferNano.writeInt32(14, lastScore);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static final class ConfigInfo
      extends MessageNano
    {
      private static volatile ConfigInfo[] _emptyArray;
      public int allowedAuthAlgorithms;
      public int allowedGroupCiphers;
      public int allowedKeyManagement;
      public int allowedPairwiseCiphers;
      public int allowedProtocols;
      public boolean hasEverConnected;
      public boolean hiddenSsid;
      public boolean isEphemeral;
      public boolean isPasspoint;
      public int scanFreq;
      public int scanRssi;
      
      public ConfigInfo()
      {
        clear();
      }
      
      public static ConfigInfo[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new ConfigInfo[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static ConfigInfo parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new ConfigInfo().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static ConfigInfo parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (ConfigInfo)MessageNano.mergeFrom(new ConfigInfo(), paramArrayOfByte);
      }
      
      public ConfigInfo clear()
      {
        allowedKeyManagement = 0;
        allowedProtocols = 0;
        allowedAuthAlgorithms = 0;
        allowedPairwiseCiphers = 0;
        allowedGroupCiphers = 0;
        hiddenSsid = false;
        isPasspoint = false;
        isEphemeral = false;
        hasEverConnected = false;
        scanRssi = -127;
        scanFreq = -1;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (allowedKeyManagement != 0) {
          j = i + CodedOutputByteBufferNano.computeUInt32Size(1, allowedKeyManagement);
        }
        i = j;
        if (allowedProtocols != 0) {
          i = j + CodedOutputByteBufferNano.computeUInt32Size(2, allowedProtocols);
        }
        j = i;
        if (allowedAuthAlgorithms != 0) {
          j = i + CodedOutputByteBufferNano.computeUInt32Size(3, allowedAuthAlgorithms);
        }
        i = j;
        if (allowedPairwiseCiphers != 0) {
          i = j + CodedOutputByteBufferNano.computeUInt32Size(4, allowedPairwiseCiphers);
        }
        j = i;
        if (allowedGroupCiphers != 0) {
          j = i + CodedOutputByteBufferNano.computeUInt32Size(5, allowedGroupCiphers);
        }
        i = j;
        if (hiddenSsid) {
          i = j + CodedOutputByteBufferNano.computeBoolSize(6, hiddenSsid);
        }
        j = i;
        if (isPasspoint) {
          j = i + CodedOutputByteBufferNano.computeBoolSize(7, isPasspoint);
        }
        i = j;
        if (isEphemeral) {
          i = j + CodedOutputByteBufferNano.computeBoolSize(8, isEphemeral);
        }
        int k = i;
        if (hasEverConnected) {
          k = i + CodedOutputByteBufferNano.computeBoolSize(9, hasEverConnected);
        }
        j = k;
        if (scanRssi != -127) {
          j = k + CodedOutputByteBufferNano.computeInt32Size(10, scanRssi);
        }
        i = j;
        if (scanFreq != -1) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(11, scanFreq);
        }
        return i;
      }
      
      public ConfigInfo mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
          case 88: 
            scanFreq = paramCodedInputByteBufferNano.readInt32();
            break;
          case 80: 
            scanRssi = paramCodedInputByteBufferNano.readInt32();
            break;
          case 72: 
            hasEverConnected = paramCodedInputByteBufferNano.readBool();
            break;
          case 64: 
            isEphemeral = paramCodedInputByteBufferNano.readBool();
            break;
          case 56: 
            isPasspoint = paramCodedInputByteBufferNano.readBool();
            break;
          case 48: 
            hiddenSsid = paramCodedInputByteBufferNano.readBool();
            break;
          case 40: 
            allowedGroupCiphers = paramCodedInputByteBufferNano.readUInt32();
            break;
          case 32: 
            allowedPairwiseCiphers = paramCodedInputByteBufferNano.readUInt32();
            break;
          case 24: 
            allowedAuthAlgorithms = paramCodedInputByteBufferNano.readUInt32();
            break;
          case 16: 
            allowedProtocols = paramCodedInputByteBufferNano.readUInt32();
            break;
          case 8: 
            allowedKeyManagement = paramCodedInputByteBufferNano.readUInt32();
            break;
          case 0: 
            return this;
          }
        }
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (allowedKeyManagement != 0) {
          paramCodedOutputByteBufferNano.writeUInt32(1, allowedKeyManagement);
        }
        if (allowedProtocols != 0) {
          paramCodedOutputByteBufferNano.writeUInt32(2, allowedProtocols);
        }
        if (allowedAuthAlgorithms != 0) {
          paramCodedOutputByteBufferNano.writeUInt32(3, allowedAuthAlgorithms);
        }
        if (allowedPairwiseCiphers != 0) {
          paramCodedOutputByteBufferNano.writeUInt32(4, allowedPairwiseCiphers);
        }
        if (allowedGroupCiphers != 0) {
          paramCodedOutputByteBufferNano.writeUInt32(5, allowedGroupCiphers);
        }
        if (hiddenSsid) {
          paramCodedOutputByteBufferNano.writeBool(6, hiddenSsid);
        }
        if (isPasspoint) {
          paramCodedOutputByteBufferNano.writeBool(7, isPasspoint);
        }
        if (isEphemeral) {
          paramCodedOutputByteBufferNano.writeBool(8, isEphemeral);
        }
        if (hasEverConnected) {
          paramCodedOutputByteBufferNano.writeBool(9, hasEverConnected);
        }
        if (scanRssi != -127) {
          paramCodedOutputByteBufferNano.writeInt32(10, scanRssi);
        }
        if (scanFreq != -1) {
          paramCodedOutputByteBufferNano.writeInt32(11, scanFreq);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
  }
  
  public static final class WifiAwareLog
    extends MessageNano
  {
    public static final int ALREADY_ENABLED = 11;
    public static final int FOLLOWUP_TX_QUEUE_FULL = 12;
    public static final int INTERNAL_FAILURE = 2;
    public static final int INVALID_ARGS = 6;
    public static final int INVALID_NDP_ID = 8;
    public static final int INVALID_PEER_ID = 7;
    public static final int INVALID_SESSION_ID = 4;
    public static final int NAN_NOT_ALLOWED = 9;
    public static final int NO_OTA_ACK = 10;
    public static final int NO_RESOURCES_AVAILABLE = 5;
    public static final int PROTOCOL_FAILURE = 3;
    public static final int SUCCESS = 1;
    public static final int UNKNOWN = 0;
    public static final int UNKNOWN_HAL_STATUS = 14;
    public static final int UNSUPPORTED_CONCURRENCY_NAN_DISABLED = 13;
    private static volatile WifiAwareLog[] _emptyArray;
    public long availableTimeMs;
    public long enabledTimeMs;
    public HistogramBucket[] histogramAttachDurationMs;
    public NanStatusHistogramBucket[] histogramAttachSessionStatus;
    public HistogramBucket[] histogramAwareAvailableDurationMs;
    public HistogramBucket[] histogramAwareEnabledDurationMs;
    public HistogramBucket[] histogramNdpCreationTimeMs;
    public HistogramBucket[] histogramNdpSessionDataUsageMb;
    public HistogramBucket[] histogramNdpSessionDurationMs;
    public HistogramBucket[] histogramPublishSessionDurationMs;
    public NanStatusHistogramBucket[] histogramPublishStatus;
    public NanStatusHistogramBucket[] histogramRequestNdpOobStatus;
    public NanStatusHistogramBucket[] histogramRequestNdpStatus;
    public HistogramBucket[] histogramSubscribeGeofenceMax;
    public HistogramBucket[] histogramSubscribeGeofenceMin;
    public HistogramBucket[] histogramSubscribeSessionDurationMs;
    public NanStatusHistogramBucket[] histogramSubscribeStatus;
    public int maxConcurrentAttachSessionsInApp;
    public int maxConcurrentDiscoverySessionsInApp;
    public int maxConcurrentDiscoverySessionsInSystem;
    public int maxConcurrentNdiInApp;
    public int maxConcurrentNdiInSystem;
    public int maxConcurrentNdpInApp;
    public int maxConcurrentNdpInSystem;
    public int maxConcurrentNdpPerNdi;
    public int maxConcurrentPublishInApp;
    public int maxConcurrentPublishInSystem;
    public int maxConcurrentPublishWithRangingInApp;
    public int maxConcurrentPublishWithRangingInSystem;
    public int maxConcurrentSecureNdpInApp;
    public int maxConcurrentSecureNdpInSystem;
    public int maxConcurrentSubscribeInApp;
    public int maxConcurrentSubscribeInSystem;
    public int maxConcurrentSubscribeWithRangingInApp;
    public int maxConcurrentSubscribeWithRangingInSystem;
    public long ndpCreationTimeMsMax;
    public long ndpCreationTimeMsMin;
    public long ndpCreationTimeMsNumSamples;
    public long ndpCreationTimeMsSum;
    public long ndpCreationTimeMsSumOfSq;
    public int numApps;
    public int numAppsUsingIdentityCallback;
    public int numAppsWithDiscoverySessionFailureOutOfResources;
    public int numMatchesWithRanging;
    public int numMatchesWithoutRangingForRangingEnabledSubscribes;
    public int numSubscribesWithRanging;
    
    public WifiAwareLog()
    {
      clear();
    }
    
    public static WifiAwareLog[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new WifiAwareLog[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static WifiAwareLog parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new WifiAwareLog().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static WifiAwareLog parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (WifiAwareLog)MessageNano.mergeFrom(new WifiAwareLog(), paramArrayOfByte);
    }
    
    public WifiAwareLog clear()
    {
      numApps = 0;
      numAppsUsingIdentityCallback = 0;
      maxConcurrentAttachSessionsInApp = 0;
      histogramAttachSessionStatus = NanStatusHistogramBucket.emptyArray();
      maxConcurrentPublishInApp = 0;
      maxConcurrentSubscribeInApp = 0;
      maxConcurrentDiscoverySessionsInApp = 0;
      maxConcurrentPublishInSystem = 0;
      maxConcurrentSubscribeInSystem = 0;
      maxConcurrentDiscoverySessionsInSystem = 0;
      histogramPublishStatus = NanStatusHistogramBucket.emptyArray();
      histogramSubscribeStatus = NanStatusHistogramBucket.emptyArray();
      numAppsWithDiscoverySessionFailureOutOfResources = 0;
      histogramRequestNdpStatus = NanStatusHistogramBucket.emptyArray();
      histogramRequestNdpOobStatus = NanStatusHistogramBucket.emptyArray();
      maxConcurrentNdiInApp = 0;
      maxConcurrentNdiInSystem = 0;
      maxConcurrentNdpInApp = 0;
      maxConcurrentNdpInSystem = 0;
      maxConcurrentSecureNdpInApp = 0;
      maxConcurrentSecureNdpInSystem = 0;
      maxConcurrentNdpPerNdi = 0;
      histogramAwareAvailableDurationMs = HistogramBucket.emptyArray();
      histogramAwareEnabledDurationMs = HistogramBucket.emptyArray();
      histogramAttachDurationMs = HistogramBucket.emptyArray();
      histogramPublishSessionDurationMs = HistogramBucket.emptyArray();
      histogramSubscribeSessionDurationMs = HistogramBucket.emptyArray();
      histogramNdpSessionDurationMs = HistogramBucket.emptyArray();
      histogramNdpSessionDataUsageMb = HistogramBucket.emptyArray();
      histogramNdpCreationTimeMs = HistogramBucket.emptyArray();
      ndpCreationTimeMsMin = 0L;
      ndpCreationTimeMsMax = 0L;
      ndpCreationTimeMsSum = 0L;
      ndpCreationTimeMsSumOfSq = 0L;
      ndpCreationTimeMsNumSamples = 0L;
      availableTimeMs = 0L;
      enabledTimeMs = 0L;
      maxConcurrentPublishWithRangingInApp = 0;
      maxConcurrentSubscribeWithRangingInApp = 0;
      maxConcurrentPublishWithRangingInSystem = 0;
      maxConcurrentSubscribeWithRangingInSystem = 0;
      histogramSubscribeGeofenceMin = HistogramBucket.emptyArray();
      histogramSubscribeGeofenceMax = HistogramBucket.emptyArray();
      numSubscribesWithRanging = 0;
      numMatchesWithRanging = 0;
      numMatchesWithoutRangingForRangingEnabledSubscribes = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (numApps != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, numApps);
      }
      i = j;
      if (numAppsUsingIdentityCallback != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, numAppsUsingIdentityCallback);
      }
      j = i;
      if (maxConcurrentAttachSessionsInApp != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, maxConcurrentAttachSessionsInApp);
      }
      Object localObject = histogramAttachSessionStatus;
      int k = 0;
      i = j;
      if (localObject != null)
      {
        i = j;
        if (histogramAttachSessionStatus.length > 0)
        {
          m = 0;
          while (m < histogramAttachSessionStatus.length)
          {
            localObject = histogramAttachSessionStatus[m];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(4, (MessageNano)localObject);
            }
            m++;
            j = i;
          }
          i = j;
        }
      }
      j = i;
      if (maxConcurrentPublishInApp != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(5, maxConcurrentPublishInApp);
      }
      i = j;
      if (maxConcurrentSubscribeInApp != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(6, maxConcurrentSubscribeInApp);
      }
      int m = i;
      if (maxConcurrentDiscoverySessionsInApp != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(7, maxConcurrentDiscoverySessionsInApp);
      }
      j = m;
      if (maxConcurrentPublishInSystem != 0) {
        j = m + CodedOutputByteBufferNano.computeInt32Size(8, maxConcurrentPublishInSystem);
      }
      m = j;
      if (maxConcurrentSubscribeInSystem != 0) {
        m = j + CodedOutputByteBufferNano.computeInt32Size(9, maxConcurrentSubscribeInSystem);
      }
      i = m;
      if (maxConcurrentDiscoverySessionsInSystem != 0) {
        i = m + CodedOutputByteBufferNano.computeInt32Size(10, maxConcurrentDiscoverySessionsInSystem);
      }
      j = i;
      if (histogramPublishStatus != null)
      {
        j = i;
        if (histogramPublishStatus.length > 0)
        {
          j = i;
          m = 0;
          while (m < histogramPublishStatus.length)
          {
            localObject = histogramPublishStatus[m];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(11, (MessageNano)localObject);
            }
            m++;
            j = i;
          }
        }
      }
      m = j;
      if (histogramSubscribeStatus != null)
      {
        m = j;
        if (histogramSubscribeStatus.length > 0)
        {
          i = 0;
          while (i < histogramSubscribeStatus.length)
          {
            localObject = histogramSubscribeStatus[i];
            m = j;
            if (localObject != null) {
              m = j + CodedOutputByteBufferNano.computeMessageSize(12, (MessageNano)localObject);
            }
            i++;
            j = m;
          }
          m = j;
        }
      }
      i = m;
      if (numAppsWithDiscoverySessionFailureOutOfResources != 0) {
        i = m + CodedOutputByteBufferNano.computeInt32Size(13, numAppsWithDiscoverySessionFailureOutOfResources);
      }
      j = i;
      if (histogramRequestNdpStatus != null)
      {
        j = i;
        if (histogramRequestNdpStatus.length > 0)
        {
          j = i;
          m = 0;
          while (m < histogramRequestNdpStatus.length)
          {
            localObject = histogramRequestNdpStatus[m];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(14, (MessageNano)localObject);
            }
            m++;
            j = i;
          }
        }
      }
      i = j;
      if (histogramRequestNdpOobStatus != null)
      {
        i = j;
        if (histogramRequestNdpOobStatus.length > 0)
        {
          i = 0;
          while (i < histogramRequestNdpOobStatus.length)
          {
            localObject = histogramRequestNdpOobStatus[i];
            m = j;
            if (localObject != null) {
              m = j + CodedOutputByteBufferNano.computeMessageSize(15, (MessageNano)localObject);
            }
            i++;
            j = m;
          }
          i = j;
        }
      }
      j = i;
      if (maxConcurrentNdiInApp != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(19, maxConcurrentNdiInApp);
      }
      i = j;
      if (maxConcurrentNdiInSystem != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(20, maxConcurrentNdiInSystem);
      }
      j = i;
      if (maxConcurrentNdpInApp != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(21, maxConcurrentNdpInApp);
      }
      i = j;
      if (maxConcurrentNdpInSystem != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(22, maxConcurrentNdpInSystem);
      }
      j = i;
      if (maxConcurrentSecureNdpInApp != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(23, maxConcurrentSecureNdpInApp);
      }
      i = j;
      if (maxConcurrentSecureNdpInSystem != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(24, maxConcurrentSecureNdpInSystem);
      }
      j = i;
      if (maxConcurrentNdpPerNdi != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(25, maxConcurrentNdpPerNdi);
      }
      i = j;
      if (histogramAwareAvailableDurationMs != null)
      {
        i = j;
        if (histogramAwareAvailableDurationMs.length > 0)
        {
          m = 0;
          while (m < histogramAwareAvailableDurationMs.length)
          {
            localObject = histogramAwareAvailableDurationMs[m];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(26, (MessageNano)localObject);
            }
            m++;
            j = i;
          }
          i = j;
        }
      }
      j = i;
      if (histogramAwareEnabledDurationMs != null)
      {
        j = i;
        if (histogramAwareEnabledDurationMs.length > 0)
        {
          j = i;
          i = 0;
          while (i < histogramAwareEnabledDurationMs.length)
          {
            localObject = histogramAwareEnabledDurationMs[i];
            m = j;
            if (localObject != null) {
              m = j + CodedOutputByteBufferNano.computeMessageSize(27, (MessageNano)localObject);
            }
            i++;
            j = m;
          }
        }
      }
      i = j;
      if (histogramAttachDurationMs != null)
      {
        i = j;
        if (histogramAttachDurationMs.length > 0)
        {
          m = 0;
          while (m < histogramAttachDurationMs.length)
          {
            localObject = histogramAttachDurationMs[m];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(28, (MessageNano)localObject);
            }
            m++;
            j = i;
          }
          i = j;
        }
      }
      j = i;
      if (histogramPublishSessionDurationMs != null)
      {
        j = i;
        if (histogramPublishSessionDurationMs.length > 0)
        {
          j = i;
          m = 0;
          while (m < histogramPublishSessionDurationMs.length)
          {
            localObject = histogramPublishSessionDurationMs[m];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(29, (MessageNano)localObject);
            }
            m++;
            j = i;
          }
        }
      }
      i = j;
      if (histogramSubscribeSessionDurationMs != null)
      {
        i = j;
        if (histogramSubscribeSessionDurationMs.length > 0)
        {
          i = 0;
          while (i < histogramSubscribeSessionDurationMs.length)
          {
            localObject = histogramSubscribeSessionDurationMs[i];
            m = j;
            if (localObject != null) {
              m = j + CodedOutputByteBufferNano.computeMessageSize(30, (MessageNano)localObject);
            }
            i++;
            j = m;
          }
          i = j;
        }
      }
      j = i;
      if (histogramNdpSessionDurationMs != null)
      {
        j = i;
        if (histogramNdpSessionDurationMs.length > 0)
        {
          j = i;
          m = 0;
          while (m < histogramNdpSessionDurationMs.length)
          {
            localObject = histogramNdpSessionDurationMs[m];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(31, (MessageNano)localObject);
            }
            m++;
            j = i;
          }
        }
      }
      i = j;
      if (histogramNdpSessionDataUsageMb != null)
      {
        i = j;
        if (histogramNdpSessionDataUsageMb.length > 0)
        {
          i = 0;
          while (i < histogramNdpSessionDataUsageMb.length)
          {
            localObject = histogramNdpSessionDataUsageMb[i];
            m = j;
            if (localObject != null) {
              m = j + CodedOutputByteBufferNano.computeMessageSize(32, (MessageNano)localObject);
            }
            i++;
            j = m;
          }
          i = j;
        }
      }
      j = i;
      if (histogramNdpCreationTimeMs != null)
      {
        j = i;
        if (histogramNdpCreationTimeMs.length > 0)
        {
          j = i;
          i = 0;
          while (i < histogramNdpCreationTimeMs.length)
          {
            localObject = histogramNdpCreationTimeMs[i];
            m = j;
            if (localObject != null) {
              m = j + CodedOutputByteBufferNano.computeMessageSize(33, (MessageNano)localObject);
            }
            i++;
            j = m;
          }
        }
      }
      m = j;
      if (ndpCreationTimeMsMin != 0L) {
        m = j + CodedOutputByteBufferNano.computeInt64Size(34, ndpCreationTimeMsMin);
      }
      i = m;
      if (ndpCreationTimeMsMax != 0L) {
        i = m + CodedOutputByteBufferNano.computeInt64Size(35, ndpCreationTimeMsMax);
      }
      j = i;
      if (ndpCreationTimeMsSum != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(36, ndpCreationTimeMsSum);
      }
      m = j;
      if (ndpCreationTimeMsSumOfSq != 0L) {
        m = j + CodedOutputByteBufferNano.computeInt64Size(37, ndpCreationTimeMsSumOfSq);
      }
      i = m;
      if (ndpCreationTimeMsNumSamples != 0L) {
        i = m + CodedOutputByteBufferNano.computeInt64Size(38, ndpCreationTimeMsNumSamples);
      }
      j = i;
      if (availableTimeMs != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(39, availableTimeMs);
      }
      i = j;
      if (enabledTimeMs != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(40, enabledTimeMs);
      }
      j = i;
      if (maxConcurrentPublishWithRangingInApp != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(41, maxConcurrentPublishWithRangingInApp);
      }
      i = j;
      if (maxConcurrentSubscribeWithRangingInApp != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(42, maxConcurrentSubscribeWithRangingInApp);
      }
      j = i;
      if (maxConcurrentPublishWithRangingInSystem != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(43, maxConcurrentPublishWithRangingInSystem);
      }
      i = j;
      if (maxConcurrentSubscribeWithRangingInSystem != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(44, maxConcurrentSubscribeWithRangingInSystem);
      }
      j = i;
      if (histogramSubscribeGeofenceMin != null)
      {
        j = i;
        if (histogramSubscribeGeofenceMin.length > 0)
        {
          j = i;
          m = 0;
          while (m < histogramSubscribeGeofenceMin.length)
          {
            localObject = histogramSubscribeGeofenceMin[m];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(45, (MessageNano)localObject);
            }
            m++;
            j = i;
          }
        }
      }
      i = j;
      if (histogramSubscribeGeofenceMax != null)
      {
        i = j;
        if (histogramSubscribeGeofenceMax.length > 0)
        {
          m = k;
          for (;;)
          {
            i = j;
            if (m >= histogramSubscribeGeofenceMax.length) {
              break;
            }
            localObject = histogramSubscribeGeofenceMax[m];
            i = j;
            if (localObject != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(46, (MessageNano)localObject);
            }
            m++;
            j = i;
          }
        }
      }
      m = i;
      if (numSubscribesWithRanging != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(47, numSubscribesWithRanging);
      }
      j = m;
      if (numMatchesWithRanging != 0) {
        j = m + CodedOutputByteBufferNano.computeInt32Size(48, numMatchesWithRanging);
      }
      i = j;
      if (numMatchesWithoutRangingForRangingEnabledSubscribes != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(49, numMatchesWithoutRangingForRangingEnabledSubscribes);
      }
      return i;
    }
    
    public WifiAwareLog mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        int j;
        Object localObject;
        switch (i)
        {
        default: 
          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 392: 
          numMatchesWithoutRangingForRangingEnabledSubscribes = paramCodedInputByteBufferNano.readInt32();
          break;
        case 384: 
          numMatchesWithRanging = paramCodedInputByteBufferNano.readInt32();
          break;
        case 376: 
          numSubscribesWithRanging = paramCodedInputByteBufferNano.readInt32();
          break;
        case 370: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 370);
          if (histogramSubscribeGeofenceMax == null) {
            i = 0;
          } else {
            i = histogramSubscribeGeofenceMax.length;
          }
          localObject = new HistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramSubscribeGeofenceMax, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new HistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new HistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramSubscribeGeofenceMax = ((HistogramBucket[])localObject);
          break;
        case 362: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 362);
          if (histogramSubscribeGeofenceMin == null) {
            i = 0;
          } else {
            i = histogramSubscribeGeofenceMin.length;
          }
          localObject = new HistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramSubscribeGeofenceMin, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new HistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new HistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramSubscribeGeofenceMin = ((HistogramBucket[])localObject);
          break;
        case 352: 
          maxConcurrentSubscribeWithRangingInSystem = paramCodedInputByteBufferNano.readInt32();
          break;
        case 344: 
          maxConcurrentPublishWithRangingInSystem = paramCodedInputByteBufferNano.readInt32();
          break;
        case 336: 
          maxConcurrentSubscribeWithRangingInApp = paramCodedInputByteBufferNano.readInt32();
          break;
        case 328: 
          maxConcurrentPublishWithRangingInApp = paramCodedInputByteBufferNano.readInt32();
          break;
        case 320: 
          enabledTimeMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 312: 
          availableTimeMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 304: 
          ndpCreationTimeMsNumSamples = paramCodedInputByteBufferNano.readInt64();
          break;
        case 296: 
          ndpCreationTimeMsSumOfSq = paramCodedInputByteBufferNano.readInt64();
          break;
        case 288: 
          ndpCreationTimeMsSum = paramCodedInputByteBufferNano.readInt64();
          break;
        case 280: 
          ndpCreationTimeMsMax = paramCodedInputByteBufferNano.readInt64();
          break;
        case 272: 
          ndpCreationTimeMsMin = paramCodedInputByteBufferNano.readInt64();
          break;
        case 266: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 266);
          if (histogramNdpCreationTimeMs == null) {
            i = 0;
          } else {
            i = histogramNdpCreationTimeMs.length;
          }
          localObject = new HistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramNdpCreationTimeMs, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new HistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new HistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramNdpCreationTimeMs = ((HistogramBucket[])localObject);
          break;
        case 258: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 258);
          if (histogramNdpSessionDataUsageMb == null) {
            i = 0;
          } else {
            i = histogramNdpSessionDataUsageMb.length;
          }
          localObject = new HistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramNdpSessionDataUsageMb, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new HistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new HistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramNdpSessionDataUsageMb = ((HistogramBucket[])localObject);
          break;
        case 250: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 250);
          if (histogramNdpSessionDurationMs == null) {
            i = 0;
          } else {
            i = histogramNdpSessionDurationMs.length;
          }
          localObject = new HistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramNdpSessionDurationMs, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new HistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new HistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramNdpSessionDurationMs = ((HistogramBucket[])localObject);
          break;
        case 242: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 242);
          if (histogramSubscribeSessionDurationMs == null) {
            i = 0;
          } else {
            i = histogramSubscribeSessionDurationMs.length;
          }
          localObject = new HistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramSubscribeSessionDurationMs, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new HistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new HistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramSubscribeSessionDurationMs = ((HistogramBucket[])localObject);
          break;
        case 234: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 234);
          if (histogramPublishSessionDurationMs == null) {
            i = 0;
          } else {
            i = histogramPublishSessionDurationMs.length;
          }
          localObject = new HistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramPublishSessionDurationMs, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new HistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new HistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramPublishSessionDurationMs = ((HistogramBucket[])localObject);
          break;
        case 226: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 226);
          if (histogramAttachDurationMs == null) {
            i = 0;
          } else {
            i = histogramAttachDurationMs.length;
          }
          localObject = new HistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramAttachDurationMs, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new HistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new HistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramAttachDurationMs = ((HistogramBucket[])localObject);
          break;
        case 218: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 218);
          if (histogramAwareEnabledDurationMs == null) {
            i = 0;
          } else {
            i = histogramAwareEnabledDurationMs.length;
          }
          localObject = new HistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramAwareEnabledDurationMs, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new HistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new HistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramAwareEnabledDurationMs = ((HistogramBucket[])localObject);
          break;
        case 210: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 210);
          if (histogramAwareAvailableDurationMs == null) {
            i = 0;
          } else {
            i = histogramAwareAvailableDurationMs.length;
          }
          localObject = new HistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramAwareAvailableDurationMs, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new HistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new HistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramAwareAvailableDurationMs = ((HistogramBucket[])localObject);
          break;
        case 200: 
          maxConcurrentNdpPerNdi = paramCodedInputByteBufferNano.readInt32();
          break;
        case 192: 
          maxConcurrentSecureNdpInSystem = paramCodedInputByteBufferNano.readInt32();
          break;
        case 184: 
          maxConcurrentSecureNdpInApp = paramCodedInputByteBufferNano.readInt32();
          break;
        case 176: 
          maxConcurrentNdpInSystem = paramCodedInputByteBufferNano.readInt32();
          break;
        case 168: 
          maxConcurrentNdpInApp = paramCodedInputByteBufferNano.readInt32();
          break;
        case 160: 
          maxConcurrentNdiInSystem = paramCodedInputByteBufferNano.readInt32();
          break;
        case 152: 
          maxConcurrentNdiInApp = paramCodedInputByteBufferNano.readInt32();
          break;
        case 122: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 122);
          if (histogramRequestNdpOobStatus == null) {
            i = 0;
          } else {
            i = histogramRequestNdpOobStatus.length;
          }
          localObject = new NanStatusHistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramRequestNdpOobStatus, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new NanStatusHistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new NanStatusHistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramRequestNdpOobStatus = ((NanStatusHistogramBucket[])localObject);
          break;
        case 114: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 114);
          if (histogramRequestNdpStatus == null) {
            i = 0;
          } else {
            i = histogramRequestNdpStatus.length;
          }
          localObject = new NanStatusHistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramRequestNdpStatus, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new NanStatusHistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new NanStatusHistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramRequestNdpStatus = ((NanStatusHistogramBucket[])localObject);
          break;
        case 104: 
          numAppsWithDiscoverySessionFailureOutOfResources = paramCodedInputByteBufferNano.readInt32();
          break;
        case 98: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 98);
          if (histogramSubscribeStatus == null) {
            i = 0;
          } else {
            i = histogramSubscribeStatus.length;
          }
          localObject = new NanStatusHistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramSubscribeStatus, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new NanStatusHistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new NanStatusHistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramSubscribeStatus = ((NanStatusHistogramBucket[])localObject);
          break;
        case 90: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 90);
          if (histogramPublishStatus == null) {
            i = 0;
          } else {
            i = histogramPublishStatus.length;
          }
          localObject = new NanStatusHistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramPublishStatus, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new NanStatusHistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new NanStatusHistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramPublishStatus = ((NanStatusHistogramBucket[])localObject);
          break;
        case 80: 
          maxConcurrentDiscoverySessionsInSystem = paramCodedInputByteBufferNano.readInt32();
          break;
        case 72: 
          maxConcurrentSubscribeInSystem = paramCodedInputByteBufferNano.readInt32();
          break;
        case 64: 
          maxConcurrentPublishInSystem = paramCodedInputByteBufferNano.readInt32();
          break;
        case 56: 
          maxConcurrentDiscoverySessionsInApp = paramCodedInputByteBufferNano.readInt32();
          break;
        case 48: 
          maxConcurrentSubscribeInApp = paramCodedInputByteBufferNano.readInt32();
          break;
        case 40: 
          maxConcurrentPublishInApp = paramCodedInputByteBufferNano.readInt32();
          break;
        case 34: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 34);
          if (histogramAttachSessionStatus == null) {
            i = 0;
          } else {
            i = histogramAttachSessionStatus.length;
          }
          localObject = new NanStatusHistogramBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(histogramAttachSessionStatus, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new NanStatusHistogramBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new NanStatusHistogramBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          histogramAttachSessionStatus = ((NanStatusHistogramBucket[])localObject);
          break;
        case 24: 
          maxConcurrentAttachSessionsInApp = paramCodedInputByteBufferNano.readInt32();
          break;
        case 16: 
          numAppsUsingIdentityCallback = paramCodedInputByteBufferNano.readInt32();
          break;
        case 8: 
          numApps = paramCodedInputByteBufferNano.readInt32();
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (numApps != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, numApps);
      }
      if (numAppsUsingIdentityCallback != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, numAppsUsingIdentityCallback);
      }
      if (maxConcurrentAttachSessionsInApp != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, maxConcurrentAttachSessionsInApp);
      }
      Object localObject = histogramAttachSessionStatus;
      int i = 0;
      int j;
      if ((localObject != null) && (histogramAttachSessionStatus.length > 0)) {
        for (j = 0; j < histogramAttachSessionStatus.length; j++)
        {
          localObject = histogramAttachSessionStatus[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(4, (MessageNano)localObject);
          }
        }
      }
      if (maxConcurrentPublishInApp != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, maxConcurrentPublishInApp);
      }
      if (maxConcurrentSubscribeInApp != 0) {
        paramCodedOutputByteBufferNano.writeInt32(6, maxConcurrentSubscribeInApp);
      }
      if (maxConcurrentDiscoverySessionsInApp != 0) {
        paramCodedOutputByteBufferNano.writeInt32(7, maxConcurrentDiscoverySessionsInApp);
      }
      if (maxConcurrentPublishInSystem != 0) {
        paramCodedOutputByteBufferNano.writeInt32(8, maxConcurrentPublishInSystem);
      }
      if (maxConcurrentSubscribeInSystem != 0) {
        paramCodedOutputByteBufferNano.writeInt32(9, maxConcurrentSubscribeInSystem);
      }
      if (maxConcurrentDiscoverySessionsInSystem != 0) {
        paramCodedOutputByteBufferNano.writeInt32(10, maxConcurrentDiscoverySessionsInSystem);
      }
      if ((histogramPublishStatus != null) && (histogramPublishStatus.length > 0)) {
        for (j = 0; j < histogramPublishStatus.length; j++)
        {
          localObject = histogramPublishStatus[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(11, (MessageNano)localObject);
          }
        }
      }
      if ((histogramSubscribeStatus != null) && (histogramSubscribeStatus.length > 0)) {
        for (j = 0; j < histogramSubscribeStatus.length; j++)
        {
          localObject = histogramSubscribeStatus[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(12, (MessageNano)localObject);
          }
        }
      }
      if (numAppsWithDiscoverySessionFailureOutOfResources != 0) {
        paramCodedOutputByteBufferNano.writeInt32(13, numAppsWithDiscoverySessionFailureOutOfResources);
      }
      if ((histogramRequestNdpStatus != null) && (histogramRequestNdpStatus.length > 0)) {
        for (j = 0; j < histogramRequestNdpStatus.length; j++)
        {
          localObject = histogramRequestNdpStatus[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(14, (MessageNano)localObject);
          }
        }
      }
      if ((histogramRequestNdpOobStatus != null) && (histogramRequestNdpOobStatus.length > 0)) {
        for (j = 0; j < histogramRequestNdpOobStatus.length; j++)
        {
          localObject = histogramRequestNdpOobStatus[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(15, (MessageNano)localObject);
          }
        }
      }
      if (maxConcurrentNdiInApp != 0) {
        paramCodedOutputByteBufferNano.writeInt32(19, maxConcurrentNdiInApp);
      }
      if (maxConcurrentNdiInSystem != 0) {
        paramCodedOutputByteBufferNano.writeInt32(20, maxConcurrentNdiInSystem);
      }
      if (maxConcurrentNdpInApp != 0) {
        paramCodedOutputByteBufferNano.writeInt32(21, maxConcurrentNdpInApp);
      }
      if (maxConcurrentNdpInSystem != 0) {
        paramCodedOutputByteBufferNano.writeInt32(22, maxConcurrentNdpInSystem);
      }
      if (maxConcurrentSecureNdpInApp != 0) {
        paramCodedOutputByteBufferNano.writeInt32(23, maxConcurrentSecureNdpInApp);
      }
      if (maxConcurrentSecureNdpInSystem != 0) {
        paramCodedOutputByteBufferNano.writeInt32(24, maxConcurrentSecureNdpInSystem);
      }
      if (maxConcurrentNdpPerNdi != 0) {
        paramCodedOutputByteBufferNano.writeInt32(25, maxConcurrentNdpPerNdi);
      }
      if ((histogramAwareAvailableDurationMs != null) && (histogramAwareAvailableDurationMs.length > 0)) {
        for (j = 0; j < histogramAwareAvailableDurationMs.length; j++)
        {
          localObject = histogramAwareAvailableDurationMs[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(26, (MessageNano)localObject);
          }
        }
      }
      if ((histogramAwareEnabledDurationMs != null) && (histogramAwareEnabledDurationMs.length > 0)) {
        for (j = 0; j < histogramAwareEnabledDurationMs.length; j++)
        {
          localObject = histogramAwareEnabledDurationMs[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(27, (MessageNano)localObject);
          }
        }
      }
      if ((histogramAttachDurationMs != null) && (histogramAttachDurationMs.length > 0)) {
        for (j = 0; j < histogramAttachDurationMs.length; j++)
        {
          localObject = histogramAttachDurationMs[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(28, (MessageNano)localObject);
          }
        }
      }
      if ((histogramPublishSessionDurationMs != null) && (histogramPublishSessionDurationMs.length > 0)) {
        for (j = 0; j < histogramPublishSessionDurationMs.length; j++)
        {
          localObject = histogramPublishSessionDurationMs[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(29, (MessageNano)localObject);
          }
        }
      }
      if ((histogramSubscribeSessionDurationMs != null) && (histogramSubscribeSessionDurationMs.length > 0)) {
        for (j = 0; j < histogramSubscribeSessionDurationMs.length; j++)
        {
          localObject = histogramSubscribeSessionDurationMs[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(30, (MessageNano)localObject);
          }
        }
      }
      if ((histogramNdpSessionDurationMs != null) && (histogramNdpSessionDurationMs.length > 0)) {
        for (j = 0; j < histogramNdpSessionDurationMs.length; j++)
        {
          localObject = histogramNdpSessionDurationMs[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(31, (MessageNano)localObject);
          }
        }
      }
      if ((histogramNdpSessionDataUsageMb != null) && (histogramNdpSessionDataUsageMb.length > 0)) {
        for (j = 0; j < histogramNdpSessionDataUsageMb.length; j++)
        {
          localObject = histogramNdpSessionDataUsageMb[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(32, (MessageNano)localObject);
          }
        }
      }
      if ((histogramNdpCreationTimeMs != null) && (histogramNdpCreationTimeMs.length > 0)) {
        for (j = 0; j < histogramNdpCreationTimeMs.length; j++)
        {
          localObject = histogramNdpCreationTimeMs[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(33, (MessageNano)localObject);
          }
        }
      }
      if (ndpCreationTimeMsMin != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(34, ndpCreationTimeMsMin);
      }
      if (ndpCreationTimeMsMax != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(35, ndpCreationTimeMsMax);
      }
      if (ndpCreationTimeMsSum != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(36, ndpCreationTimeMsSum);
      }
      if (ndpCreationTimeMsSumOfSq != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(37, ndpCreationTimeMsSumOfSq);
      }
      if (ndpCreationTimeMsNumSamples != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(38, ndpCreationTimeMsNumSamples);
      }
      if (availableTimeMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(39, availableTimeMs);
      }
      if (enabledTimeMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(40, enabledTimeMs);
      }
      if (maxConcurrentPublishWithRangingInApp != 0) {
        paramCodedOutputByteBufferNano.writeInt32(41, maxConcurrentPublishWithRangingInApp);
      }
      if (maxConcurrentSubscribeWithRangingInApp != 0) {
        paramCodedOutputByteBufferNano.writeInt32(42, maxConcurrentSubscribeWithRangingInApp);
      }
      if (maxConcurrentPublishWithRangingInSystem != 0) {
        paramCodedOutputByteBufferNano.writeInt32(43, maxConcurrentPublishWithRangingInSystem);
      }
      if (maxConcurrentSubscribeWithRangingInSystem != 0) {
        paramCodedOutputByteBufferNano.writeInt32(44, maxConcurrentSubscribeWithRangingInSystem);
      }
      if ((histogramSubscribeGeofenceMin != null) && (histogramSubscribeGeofenceMin.length > 0)) {
        for (j = 0; j < histogramSubscribeGeofenceMin.length; j++)
        {
          localObject = histogramSubscribeGeofenceMin[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(45, (MessageNano)localObject);
          }
        }
      }
      if ((histogramSubscribeGeofenceMax != null) && (histogramSubscribeGeofenceMax.length > 0)) {
        for (j = i; j < histogramSubscribeGeofenceMax.length; j++)
        {
          localObject = histogramSubscribeGeofenceMax[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(46, (MessageNano)localObject);
          }
        }
      }
      if (numSubscribesWithRanging != 0) {
        paramCodedOutputByteBufferNano.writeInt32(47, numSubscribesWithRanging);
      }
      if (numMatchesWithRanging != 0) {
        paramCodedOutputByteBufferNano.writeInt32(48, numMatchesWithRanging);
      }
      if (numMatchesWithoutRangingForRangingEnabledSubscribes != 0) {
        paramCodedOutputByteBufferNano.writeInt32(49, numMatchesWithoutRangingForRangingEnabledSubscribes);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static final class HistogramBucket
      extends MessageNano
    {
      private static volatile HistogramBucket[] _emptyArray;
      public int count;
      public long end;
      public long start;
      
      public HistogramBucket()
      {
        clear();
      }
      
      public static HistogramBucket[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new HistogramBucket[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static HistogramBucket parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new HistogramBucket().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static HistogramBucket parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (HistogramBucket)MessageNano.mergeFrom(new HistogramBucket(), paramArrayOfByte);
      }
      
      public HistogramBucket clear()
      {
        start = 0L;
        end = 0L;
        count = 0;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (start != 0L) {
          j = i + CodedOutputByteBufferNano.computeInt64Size(1, start);
        }
        i = j;
        if (end != 0L) {
          i = j + CodedOutputByteBufferNano.computeInt64Size(2, end);
        }
        j = i;
        if (count != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(3, count);
        }
        return j;
      }
      
      public HistogramBucket mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else {
                count = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              end = paramCodedInputByteBufferNano.readInt64();
            }
          }
          else {
            start = paramCodedInputByteBufferNano.readInt64();
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (start != 0L) {
          paramCodedOutputByteBufferNano.writeInt64(1, start);
        }
        if (end != 0L) {
          paramCodedOutputByteBufferNano.writeInt64(2, end);
        }
        if (count != 0) {
          paramCodedOutputByteBufferNano.writeInt32(3, count);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
    
    public static final class NanStatusHistogramBucket
      extends MessageNano
    {
      private static volatile NanStatusHistogramBucket[] _emptyArray;
      public int count;
      public int nanStatusType;
      
      public NanStatusHistogramBucket()
      {
        clear();
      }
      
      public static NanStatusHistogramBucket[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new NanStatusHistogramBucket[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static NanStatusHistogramBucket parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new NanStatusHistogramBucket().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static NanStatusHistogramBucket parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (NanStatusHistogramBucket)MessageNano.mergeFrom(new NanStatusHistogramBucket(), paramArrayOfByte);
      }
      
      public NanStatusHistogramBucket clear()
      {
        nanStatusType = 0;
        count = 0;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (nanStatusType != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, nanStatusType);
        }
        i = j;
        if (count != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, count);
        }
        return i;
      }
      
      public NanStatusHistogramBucket mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              count = paramCodedInputByteBufferNano.readInt32();
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
              nanStatusType = i;
            }
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (nanStatusType != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, nanStatusType);
        }
        if (count != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, count);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
  }
  
  public static final class WifiLog
    extends MessageNano
  {
    public static final int FAILURE_WIFI_DISABLED = 4;
    public static final int SCAN_FAILURE_INTERRUPTED = 2;
    public static final int SCAN_FAILURE_INVALID_CONFIGURATION = 3;
    public static final int SCAN_SUCCESS = 1;
    public static final int SCAN_UNKNOWN = 0;
    public static final int WIFI_ASSOCIATED = 3;
    public static final int WIFI_DISABLED = 1;
    public static final int WIFI_DISCONNECTED = 2;
    public static final int WIFI_UNKNOWN = 0;
    private static volatile WifiLog[] _emptyArray;
    public WifiMetricsProto.AlertReasonCount[] alertReasonCount;
    public WifiMetricsProto.NumConnectableNetworksBucket[] availableOpenBssidsInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] availableOpenOrSavedBssidsInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] availableOpenOrSavedSsidsInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] availableOpenSsidsInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] availableSavedBssidsInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] availableSavedPasspointProviderBssidsInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] availableSavedPasspointProviderProfilesInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] availableSavedSsidsInScanHistogram;
    public WifiSystemStateEntry[] backgroundScanRequestState;
    public ScanReturnEntry[] backgroundScanReturnEntries;
    public WifiMetricsProto.ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationActionCount;
    public WifiMetricsProto.ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationCount;
    public WifiMetricsProto.ConnectionEvent[] connectionEvent;
    public int fullBandAllSingleScanListenerResults;
    public boolean isLocationEnabled;
    public boolean isMacRandomizationOn;
    public boolean isScanningAlwaysEnabled;
    public boolean isWifiNetworksAvailableNotificationOn;
    public int numBackgroundScans;
    public int numClientInterfaceDown;
    public int numConnectivityOneshotScans;
    public int numConnectivityWatchdogBackgroundBad;
    public int numConnectivityWatchdogBackgroundGood;
    public int numConnectivityWatchdogPnoBad;
    public int numConnectivityWatchdogPnoGood;
    public int numEmptyScanResults;
    public int numEnterpriseNetworkScanResults;
    public int numEnterpriseNetworks;
    public int numExternalAppOneshotScanRequests;
    public int numExternalBackgroundAppOneshotScanRequestsThrottled;
    public int numExternalForegroundAppOneshotScanRequestsThrottled;
    public int numHalCrashes;
    public int numHiddenNetworkScanResults;
    public int numHiddenNetworks;
    public int numHostapdCrashes;
    public int numHotspot2R1NetworkScanResults;
    public int numHotspot2R2NetworkScanResults;
    public int numLastResortWatchdogAvailableNetworksTotal;
    public int numLastResortWatchdogBadAssociationNetworksTotal;
    public int numLastResortWatchdogBadAuthenticationNetworksTotal;
    public int numLastResortWatchdogBadDhcpNetworksTotal;
    public int numLastResortWatchdogBadOtherNetworksTotal;
    public int numLastResortWatchdogSuccesses;
    public int numLastResortWatchdogTriggers;
    public int numLastResortWatchdogTriggersWithBadAssociation;
    public int numLastResortWatchdogTriggersWithBadAuthentication;
    public int numLastResortWatchdogTriggersWithBadDhcp;
    public int numLastResortWatchdogTriggersWithBadOther;
    public int numNetworksAddedByApps;
    public int numNetworksAddedByUser;
    public int numNonEmptyScanResults;
    public int numOneshotHasDfsChannelScans;
    public int numOneshotScans;
    public int numOpenNetworkConnectMessageFailedToSend;
    public int numOpenNetworkRecommendationUpdates;
    public int numOpenNetworkScanResults;
    public int numOpenNetworks;
    public int numPasspointNetworks;
    public int numPasspointProviderInstallSuccess;
    public int numPasspointProviderInstallation;
    public int numPasspointProviderUninstallSuccess;
    public int numPasspointProviderUninstallation;
    public int numPasspointProviders;
    public int numPasspointProvidersSuccessfullyConnected;
    public int numPersonalNetworkScanResults;
    public int numPersonalNetworks;
    public int numRadioModeChangeToDbs;
    public int numRadioModeChangeToMcc;
    public int numRadioModeChangeToSbs;
    public int numRadioModeChangeToScc;
    public int numSavedNetworks;
    public int numScans;
    public int numSetupClientInterfaceFailureDueToHal;
    public int numSetupClientInterfaceFailureDueToSupplicant;
    public int numSetupClientInterfaceFailureDueToWificond;
    public int numSetupSoftApInterfaceFailureDueToHal;
    public int numSetupSoftApInterfaceFailureDueToHostapd;
    public int numSetupSoftApInterfaceFailureDueToWificond;
    public int numSoftApInterfaceDown;
    public int numSoftApUserBandPreferenceUnsatisfied;
    public int numSupplicantCrashes;
    public int numTotalScanResults;
    public int numWifiToggledViaAirplane;
    public int numWifiToggledViaSettings;
    public int numWificondCrashes;
    public WifiMetricsProto.NumConnectableNetworksBucket[] observed80211McSupportingApsInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] observedHotspotR1ApsInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] observedHotspotR1ApsPerEssInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] observedHotspotR1EssInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] observedHotspotR2ApsInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] observedHotspotR2ApsPerEssInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] observedHotspotR2EssInScanHistogram;
    public int openNetworkRecommenderBlacklistSize;
    public int partialAllSingleScanListenerResults;
    public WifiMetricsProto.PnoScanMetrics pnoScanMetrics;
    public int recordDurationSec;
    public WifiMetricsProto.RssiPollCount[] rssiPollDeltaCount;
    public WifiMetricsProto.RssiPollCount[] rssiPollRssiCount;
    public ScanReturnEntry[] scanReturnEntries;
    public String scoreExperimentId;
    public WifiMetricsProto.SoftApConnectedClientsEvent[] softApConnectedClientsEventsLocalOnly;
    public WifiMetricsProto.SoftApConnectedClientsEvent[] softApConnectedClientsEventsTethered;
    public WifiMetricsProto.SoftApDurationBucket[] softApDuration;
    public WifiMetricsProto.SoftApReturnCodeCount[] softApReturnCode;
    public WifiMetricsProto.StaEvent[] staEventList;
    public WifiMetricsProto.NumConnectableNetworksBucket[] totalBssidsInScanHistogram;
    public WifiMetricsProto.NumConnectableNetworksBucket[] totalSsidsInScanHistogram;
    public long watchdogTotalConnectionFailureCountAfterTrigger;
    public long watchdogTriggerToConnectionSuccessDurationMs;
    public WifiMetricsProto.WifiAwareLog wifiAwareLog;
    public WifiMetricsProto.WifiPowerStats wifiPowerStats;
    public WifiMetricsProto.WifiRttLog wifiRttLog;
    public WifiMetricsProto.WifiScoreCount[] wifiScoreCount;
    public WifiSystemStateEntry[] wifiSystemStateEntries;
    public WifiMetricsProto.WifiWakeStats wifiWakeStats;
    public WifiMetricsProto.WpsMetrics wpsMetrics;
    
    public WifiLog()
    {
      clear();
    }
    
    public static WifiLog[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new WifiLog[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static WifiLog parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new WifiLog().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static WifiLog parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (WifiLog)MessageNano.mergeFrom(new WifiLog(), paramArrayOfByte);
    }
    
    public WifiLog clear()
    {
      connectionEvent = WifiMetricsProto.ConnectionEvent.emptyArray();
      numSavedNetworks = 0;
      numOpenNetworks = 0;
      numPersonalNetworks = 0;
      numEnterpriseNetworks = 0;
      isLocationEnabled = false;
      isScanningAlwaysEnabled = false;
      numWifiToggledViaSettings = 0;
      numWifiToggledViaAirplane = 0;
      numNetworksAddedByUser = 0;
      numNetworksAddedByApps = 0;
      numEmptyScanResults = 0;
      numNonEmptyScanResults = 0;
      numOneshotScans = 0;
      numBackgroundScans = 0;
      scanReturnEntries = ScanReturnEntry.emptyArray();
      wifiSystemStateEntries = WifiSystemStateEntry.emptyArray();
      backgroundScanReturnEntries = ScanReturnEntry.emptyArray();
      backgroundScanRequestState = WifiSystemStateEntry.emptyArray();
      numLastResortWatchdogTriggers = 0;
      numLastResortWatchdogBadAssociationNetworksTotal = 0;
      numLastResortWatchdogBadAuthenticationNetworksTotal = 0;
      numLastResortWatchdogBadDhcpNetworksTotal = 0;
      numLastResortWatchdogBadOtherNetworksTotal = 0;
      numLastResortWatchdogAvailableNetworksTotal = 0;
      numLastResortWatchdogTriggersWithBadAssociation = 0;
      numLastResortWatchdogTriggersWithBadAuthentication = 0;
      numLastResortWatchdogTriggersWithBadDhcp = 0;
      numLastResortWatchdogTriggersWithBadOther = 0;
      numConnectivityWatchdogPnoGood = 0;
      numConnectivityWatchdogPnoBad = 0;
      numConnectivityWatchdogBackgroundGood = 0;
      numConnectivityWatchdogBackgroundBad = 0;
      recordDurationSec = 0;
      rssiPollRssiCount = WifiMetricsProto.RssiPollCount.emptyArray();
      numLastResortWatchdogSuccesses = 0;
      numHiddenNetworks = 0;
      numPasspointNetworks = 0;
      numTotalScanResults = 0;
      numOpenNetworkScanResults = 0;
      numPersonalNetworkScanResults = 0;
      numEnterpriseNetworkScanResults = 0;
      numHiddenNetworkScanResults = 0;
      numHotspot2R1NetworkScanResults = 0;
      numHotspot2R2NetworkScanResults = 0;
      numScans = 0;
      alertReasonCount = WifiMetricsProto.AlertReasonCount.emptyArray();
      wifiScoreCount = WifiMetricsProto.WifiScoreCount.emptyArray();
      softApDuration = WifiMetricsProto.SoftApDurationBucket.emptyArray();
      softApReturnCode = WifiMetricsProto.SoftApReturnCodeCount.emptyArray();
      rssiPollDeltaCount = WifiMetricsProto.RssiPollCount.emptyArray();
      staEventList = WifiMetricsProto.StaEvent.emptyArray();
      numHalCrashes = 0;
      numWificondCrashes = 0;
      numSetupClientInterfaceFailureDueToHal = 0;
      numSetupClientInterfaceFailureDueToWificond = 0;
      wifiAwareLog = null;
      numPasspointProviders = 0;
      numPasspointProviderInstallation = 0;
      numPasspointProviderInstallSuccess = 0;
      numPasspointProviderUninstallation = 0;
      numPasspointProviderUninstallSuccess = 0;
      numPasspointProvidersSuccessfullyConnected = 0;
      totalSsidsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      totalBssidsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      availableOpenSsidsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      availableOpenBssidsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      availableSavedSsidsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      availableSavedBssidsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      availableOpenOrSavedSsidsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      availableOpenOrSavedBssidsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      availableSavedPasspointProviderProfilesInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      availableSavedPasspointProviderBssidsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      fullBandAllSingleScanListenerResults = 0;
      partialAllSingleScanListenerResults = 0;
      pnoScanMetrics = null;
      connectToNetworkNotificationCount = WifiMetricsProto.ConnectToNetworkNotificationAndActionCount.emptyArray();
      connectToNetworkNotificationActionCount = WifiMetricsProto.ConnectToNetworkNotificationAndActionCount.emptyArray();
      openNetworkRecommenderBlacklistSize = 0;
      isWifiNetworksAvailableNotificationOn = false;
      numOpenNetworkRecommendationUpdates = 0;
      numOpenNetworkConnectMessageFailedToSend = 0;
      observedHotspotR1ApsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      observedHotspotR2ApsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      observedHotspotR1EssInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      observedHotspotR2EssInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      observedHotspotR1ApsPerEssInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      observedHotspotR2ApsPerEssInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      softApConnectedClientsEventsTethered = WifiMetricsProto.SoftApConnectedClientsEvent.emptyArray();
      softApConnectedClientsEventsLocalOnly = WifiMetricsProto.SoftApConnectedClientsEvent.emptyArray();
      wpsMetrics = null;
      wifiPowerStats = null;
      numConnectivityOneshotScans = 0;
      wifiWakeStats = null;
      observed80211McSupportingApsInScanHistogram = WifiMetricsProto.NumConnectableNetworksBucket.emptyArray();
      numSupplicantCrashes = 0;
      numHostapdCrashes = 0;
      numSetupClientInterfaceFailureDueToSupplicant = 0;
      numSetupSoftApInterfaceFailureDueToHal = 0;
      numSetupSoftApInterfaceFailureDueToWificond = 0;
      numSetupSoftApInterfaceFailureDueToHostapd = 0;
      numClientInterfaceDown = 0;
      numSoftApInterfaceDown = 0;
      numExternalAppOneshotScanRequests = 0;
      numExternalForegroundAppOneshotScanRequestsThrottled = 0;
      numExternalBackgroundAppOneshotScanRequestsThrottled = 0;
      watchdogTriggerToConnectionSuccessDurationMs = -1L;
      watchdogTotalConnectionFailureCountAfterTrigger = 0L;
      numOneshotHasDfsChannelScans = 0;
      wifiRttLog = null;
      isMacRandomizationOn = false;
      numRadioModeChangeToMcc = 0;
      numRadioModeChangeToScc = 0;
      numRadioModeChangeToSbs = 0;
      numRadioModeChangeToDbs = 0;
      numSoftApUserBandPreferenceUnsatisfied = 0;
      scoreExperimentId = "";
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      Object localObject = connectionEvent;
      int j = 0;
      int k = i;
      if (localObject != null)
      {
        k = i;
        if (connectionEvent.length > 0)
        {
          k = i;
          i = 0;
          while (i < connectionEvent.length)
          {
            localObject = connectionEvent[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(1, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
        }
      }
      i = k;
      if (numSavedNetworks != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(2, numSavedNetworks);
      }
      k = i;
      if (numOpenNetworks != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(3, numOpenNetworks);
      }
      i = k;
      if (numPersonalNetworks != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(4, numPersonalNetworks);
      }
      k = i;
      if (numEnterpriseNetworks != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(5, numEnterpriseNetworks);
      }
      i = k;
      if (isLocationEnabled) {
        i = k + CodedOutputByteBufferNano.computeBoolSize(6, isLocationEnabled);
      }
      k = i;
      if (isScanningAlwaysEnabled) {
        k = i + CodedOutputByteBufferNano.computeBoolSize(7, isScanningAlwaysEnabled);
      }
      i = k;
      if (numWifiToggledViaSettings != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(8, numWifiToggledViaSettings);
      }
      k = i;
      if (numWifiToggledViaAirplane != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(9, numWifiToggledViaAirplane);
      }
      int m = k;
      if (numNetworksAddedByUser != 0) {
        m = k + CodedOutputByteBufferNano.computeInt32Size(10, numNetworksAddedByUser);
      }
      i = m;
      if (numNetworksAddedByApps != 0) {
        i = m + CodedOutputByteBufferNano.computeInt32Size(11, numNetworksAddedByApps);
      }
      k = i;
      if (numEmptyScanResults != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(12, numEmptyScanResults);
      }
      i = k;
      if (numNonEmptyScanResults != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(13, numNonEmptyScanResults);
      }
      k = i;
      if (numOneshotScans != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(14, numOneshotScans);
      }
      i = k;
      if (numBackgroundScans != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(15, numBackgroundScans);
      }
      k = i;
      if (scanReturnEntries != null)
      {
        k = i;
        if (scanReturnEntries.length > 0)
        {
          k = i;
          m = 0;
          while (m < scanReturnEntries.length)
          {
            localObject = scanReturnEntries[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(16, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      i = k;
      if (wifiSystemStateEntries != null)
      {
        i = k;
        if (wifiSystemStateEntries.length > 0)
        {
          m = 0;
          while (m < wifiSystemStateEntries.length)
          {
            localObject = wifiSystemStateEntries[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(17, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
          i = k;
        }
      }
      k = i;
      if (backgroundScanReturnEntries != null)
      {
        k = i;
        if (backgroundScanReturnEntries.length > 0)
        {
          k = i;
          m = 0;
          while (m < backgroundScanReturnEntries.length)
          {
            localObject = backgroundScanReturnEntries[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(18, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      m = k;
      if (backgroundScanRequestState != null)
      {
        m = k;
        if (backgroundScanRequestState.length > 0)
        {
          i = 0;
          while (i < backgroundScanRequestState.length)
          {
            localObject = backgroundScanRequestState[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(19, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          m = k;
        }
      }
      i = m;
      if (numLastResortWatchdogTriggers != 0) {
        i = m + CodedOutputByteBufferNano.computeInt32Size(20, numLastResortWatchdogTriggers);
      }
      k = i;
      if (numLastResortWatchdogBadAssociationNetworksTotal != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(21, numLastResortWatchdogBadAssociationNetworksTotal);
      }
      m = k;
      if (numLastResortWatchdogBadAuthenticationNetworksTotal != 0) {
        m = k + CodedOutputByteBufferNano.computeInt32Size(22, numLastResortWatchdogBadAuthenticationNetworksTotal);
      }
      i = m;
      if (numLastResortWatchdogBadDhcpNetworksTotal != 0) {
        i = m + CodedOutputByteBufferNano.computeInt32Size(23, numLastResortWatchdogBadDhcpNetworksTotal);
      }
      k = i;
      if (numLastResortWatchdogBadOtherNetworksTotal != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(24, numLastResortWatchdogBadOtherNetworksTotal);
      }
      i = k;
      if (numLastResortWatchdogAvailableNetworksTotal != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(25, numLastResortWatchdogAvailableNetworksTotal);
      }
      k = i;
      if (numLastResortWatchdogTriggersWithBadAssociation != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(26, numLastResortWatchdogTriggersWithBadAssociation);
      }
      i = k;
      if (numLastResortWatchdogTriggersWithBadAuthentication != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(27, numLastResortWatchdogTriggersWithBadAuthentication);
      }
      k = i;
      if (numLastResortWatchdogTriggersWithBadDhcp != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(28, numLastResortWatchdogTriggersWithBadDhcp);
      }
      i = k;
      if (numLastResortWatchdogTriggersWithBadOther != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(29, numLastResortWatchdogTriggersWithBadOther);
      }
      m = i;
      if (numConnectivityWatchdogPnoGood != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(30, numConnectivityWatchdogPnoGood);
      }
      k = m;
      if (numConnectivityWatchdogPnoBad != 0) {
        k = m + CodedOutputByteBufferNano.computeInt32Size(31, numConnectivityWatchdogPnoBad);
      }
      m = k;
      if (numConnectivityWatchdogBackgroundGood != 0) {
        m = k + CodedOutputByteBufferNano.computeInt32Size(32, numConnectivityWatchdogBackgroundGood);
      }
      i = m;
      if (numConnectivityWatchdogBackgroundBad != 0) {
        i = m + CodedOutputByteBufferNano.computeInt32Size(33, numConnectivityWatchdogBackgroundBad);
      }
      k = i;
      if (recordDurationSec != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(34, recordDurationSec);
      }
      i = k;
      if (rssiPollRssiCount != null)
      {
        i = k;
        if (rssiPollRssiCount.length > 0)
        {
          i = 0;
          while (i < rssiPollRssiCount.length)
          {
            localObject = rssiPollRssiCount[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(35, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      k = i;
      if (numLastResortWatchdogSuccesses != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(36, numLastResortWatchdogSuccesses);
      }
      i = k;
      if (numHiddenNetworks != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(37, numHiddenNetworks);
      }
      m = i;
      if (numPasspointNetworks != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(38, numPasspointNetworks);
      }
      k = m;
      if (numTotalScanResults != 0) {
        k = m + CodedOutputByteBufferNano.computeInt32Size(39, numTotalScanResults);
      }
      i = k;
      if (numOpenNetworkScanResults != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(40, numOpenNetworkScanResults);
      }
      k = i;
      if (numPersonalNetworkScanResults != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(41, numPersonalNetworkScanResults);
      }
      i = k;
      if (numEnterpriseNetworkScanResults != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(42, numEnterpriseNetworkScanResults);
      }
      k = i;
      if (numHiddenNetworkScanResults != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(43, numHiddenNetworkScanResults);
      }
      m = k;
      if (numHotspot2R1NetworkScanResults != 0) {
        m = k + CodedOutputByteBufferNano.computeInt32Size(44, numHotspot2R1NetworkScanResults);
      }
      i = m;
      if (numHotspot2R2NetworkScanResults != 0) {
        i = m + CodedOutputByteBufferNano.computeInt32Size(45, numHotspot2R2NetworkScanResults);
      }
      k = i;
      if (numScans != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(46, numScans);
      }
      i = k;
      if (alertReasonCount != null)
      {
        i = k;
        if (alertReasonCount.length > 0)
        {
          i = 0;
          while (i < alertReasonCount.length)
          {
            localObject = alertReasonCount[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(47, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      m = i;
      if (wifiScoreCount != null)
      {
        m = i;
        if (wifiScoreCount.length > 0)
        {
          k = i;
          m = 0;
          while (m < wifiScoreCount.length)
          {
            localObject = wifiScoreCount[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(48, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
          m = k;
        }
      }
      k = m;
      if (softApDuration != null)
      {
        k = m;
        if (softApDuration.length > 0)
        {
          k = m;
          m = 0;
          while (m < softApDuration.length)
          {
            localObject = softApDuration[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(49, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      i = k;
      if (softApReturnCode != null)
      {
        i = k;
        if (softApReturnCode.length > 0)
        {
          i = 0;
          while (i < softApReturnCode.length)
          {
            localObject = softApReturnCode[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(50, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      k = i;
      if (rssiPollDeltaCount != null)
      {
        k = i;
        if (rssiPollDeltaCount.length > 0)
        {
          k = i;
          i = 0;
          while (i < rssiPollDeltaCount.length)
          {
            localObject = rssiPollDeltaCount[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(51, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
        }
      }
      i = k;
      if (staEventList != null)
      {
        i = k;
        if (staEventList.length > 0)
        {
          i = 0;
          while (i < staEventList.length)
          {
            localObject = staEventList[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(52, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      m = i;
      if (numHalCrashes != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(53, numHalCrashes);
      }
      k = m;
      if (numWificondCrashes != 0) {
        k = m + CodedOutputByteBufferNano.computeInt32Size(54, numWificondCrashes);
      }
      i = k;
      if (numSetupClientInterfaceFailureDueToHal != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(55, numSetupClientInterfaceFailureDueToHal);
      }
      m = i;
      if (numSetupClientInterfaceFailureDueToWificond != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(56, numSetupClientInterfaceFailureDueToWificond);
      }
      k = m;
      if (wifiAwareLog != null) {
        k = m + CodedOutputByteBufferNano.computeMessageSize(57, wifiAwareLog);
      }
      i = k;
      if (numPasspointProviders != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(58, numPasspointProviders);
      }
      k = i;
      if (numPasspointProviderInstallation != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(59, numPasspointProviderInstallation);
      }
      i = k;
      if (numPasspointProviderInstallSuccess != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(60, numPasspointProviderInstallSuccess);
      }
      k = i;
      if (numPasspointProviderUninstallation != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(61, numPasspointProviderUninstallation);
      }
      i = k;
      if (numPasspointProviderUninstallSuccess != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(62, numPasspointProviderUninstallSuccess);
      }
      k = i;
      if (numPasspointProvidersSuccessfullyConnected != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(63, numPasspointProvidersSuccessfullyConnected);
      }
      i = k;
      if (totalSsidsInScanHistogram != null)
      {
        i = k;
        if (totalSsidsInScanHistogram.length > 0)
        {
          i = 0;
          while (i < totalSsidsInScanHistogram.length)
          {
            localObject = totalSsidsInScanHistogram[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(64, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      k = i;
      if (totalBssidsInScanHistogram != null)
      {
        k = i;
        if (totalBssidsInScanHistogram.length > 0)
        {
          k = i;
          i = 0;
          while (i < totalBssidsInScanHistogram.length)
          {
            localObject = totalBssidsInScanHistogram[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(65, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
        }
      }
      i = k;
      if (availableOpenSsidsInScanHistogram != null)
      {
        i = k;
        if (availableOpenSsidsInScanHistogram.length > 0)
        {
          i = 0;
          while (i < availableOpenSsidsInScanHistogram.length)
          {
            localObject = availableOpenSsidsInScanHistogram[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(66, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      k = i;
      if (availableOpenBssidsInScanHistogram != null)
      {
        k = i;
        if (availableOpenBssidsInScanHistogram.length > 0)
        {
          k = i;
          m = 0;
          while (m < availableOpenBssidsInScanHistogram.length)
          {
            localObject = availableOpenBssidsInScanHistogram[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(67, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      i = k;
      if (availableSavedSsidsInScanHistogram != null)
      {
        i = k;
        if (availableSavedSsidsInScanHistogram.length > 0)
        {
          i = 0;
          while (i < availableSavedSsidsInScanHistogram.length)
          {
            localObject = availableSavedSsidsInScanHistogram[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(68, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      k = i;
      if (availableSavedBssidsInScanHistogram != null)
      {
        k = i;
        if (availableSavedBssidsInScanHistogram.length > 0)
        {
          k = i;
          m = 0;
          while (m < availableSavedBssidsInScanHistogram.length)
          {
            localObject = availableSavedBssidsInScanHistogram[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(69, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      i = k;
      if (availableOpenOrSavedSsidsInScanHistogram != null)
      {
        i = k;
        if (availableOpenOrSavedSsidsInScanHistogram.length > 0)
        {
          m = 0;
          while (m < availableOpenOrSavedSsidsInScanHistogram.length)
          {
            localObject = availableOpenOrSavedSsidsInScanHistogram[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(70, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
          i = k;
        }
      }
      k = i;
      if (availableOpenOrSavedBssidsInScanHistogram != null)
      {
        k = i;
        if (availableOpenOrSavedBssidsInScanHistogram.length > 0)
        {
          k = i;
          m = 0;
          while (m < availableOpenOrSavedBssidsInScanHistogram.length)
          {
            localObject = availableOpenOrSavedBssidsInScanHistogram[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(71, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      i = k;
      if (availableSavedPasspointProviderProfilesInScanHistogram != null)
      {
        i = k;
        if (availableSavedPasspointProviderProfilesInScanHistogram.length > 0)
        {
          m = 0;
          while (m < availableSavedPasspointProviderProfilesInScanHistogram.length)
          {
            localObject = availableSavedPasspointProviderProfilesInScanHistogram[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(72, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
          i = k;
        }
      }
      k = i;
      if (availableSavedPasspointProviderBssidsInScanHistogram != null)
      {
        k = i;
        if (availableSavedPasspointProviderBssidsInScanHistogram.length > 0)
        {
          k = i;
          i = 0;
          while (i < availableSavedPasspointProviderBssidsInScanHistogram.length)
          {
            localObject = availableSavedPasspointProviderBssidsInScanHistogram[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(73, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
        }
      }
      m = k;
      if (fullBandAllSingleScanListenerResults != 0) {
        m = k + CodedOutputByteBufferNano.computeInt32Size(74, fullBandAllSingleScanListenerResults);
      }
      i = m;
      if (partialAllSingleScanListenerResults != 0) {
        i = m + CodedOutputByteBufferNano.computeInt32Size(75, partialAllSingleScanListenerResults);
      }
      k = i;
      if (pnoScanMetrics != null) {
        k = i + CodedOutputByteBufferNano.computeMessageSize(76, pnoScanMetrics);
      }
      i = k;
      if (connectToNetworkNotificationCount != null)
      {
        i = k;
        if (connectToNetworkNotificationCount.length > 0)
        {
          i = 0;
          while (i < connectToNetworkNotificationCount.length)
          {
            localObject = connectToNetworkNotificationCount[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(77, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      k = i;
      if (connectToNetworkNotificationActionCount != null)
      {
        k = i;
        if (connectToNetworkNotificationActionCount.length > 0)
        {
          k = i;
          m = 0;
          while (m < connectToNetworkNotificationActionCount.length)
          {
            localObject = connectToNetworkNotificationActionCount[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(78, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      i = k;
      if (openNetworkRecommenderBlacklistSize != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(79, openNetworkRecommenderBlacklistSize);
      }
      k = i;
      if (isWifiNetworksAvailableNotificationOn) {
        k = i + CodedOutputByteBufferNano.computeBoolSize(80, isWifiNetworksAvailableNotificationOn);
      }
      m = k;
      if (numOpenNetworkRecommendationUpdates != 0) {
        m = k + CodedOutputByteBufferNano.computeInt32Size(81, numOpenNetworkRecommendationUpdates);
      }
      i = m;
      if (numOpenNetworkConnectMessageFailedToSend != 0) {
        i = m + CodedOutputByteBufferNano.computeInt32Size(82, numOpenNetworkConnectMessageFailedToSend);
      }
      k = i;
      if (observedHotspotR1ApsInScanHistogram != null)
      {
        k = i;
        if (observedHotspotR1ApsInScanHistogram.length > 0)
        {
          k = i;
          i = 0;
          while (i < observedHotspotR1ApsInScanHistogram.length)
          {
            localObject = observedHotspotR1ApsInScanHistogram[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(83, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
        }
      }
      m = k;
      if (observedHotspotR2ApsInScanHistogram != null)
      {
        m = k;
        if (observedHotspotR2ApsInScanHistogram.length > 0)
        {
          i = 0;
          while (i < observedHotspotR2ApsInScanHistogram.length)
          {
            localObject = observedHotspotR2ApsInScanHistogram[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(84, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          m = k;
        }
      }
      i = m;
      if (observedHotspotR1EssInScanHistogram != null)
      {
        i = m;
        if (observedHotspotR1EssInScanHistogram.length > 0)
        {
          k = m;
          i = 0;
          while (i < observedHotspotR1EssInScanHistogram.length)
          {
            localObject = observedHotspotR1EssInScanHistogram[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(85, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      k = i;
      if (observedHotspotR2EssInScanHistogram != null)
      {
        k = i;
        if (observedHotspotR2EssInScanHistogram.length > 0)
        {
          k = i;
          m = 0;
          while (m < observedHotspotR2EssInScanHistogram.length)
          {
            localObject = observedHotspotR2EssInScanHistogram[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(86, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      i = k;
      if (observedHotspotR1ApsPerEssInScanHistogram != null)
      {
        i = k;
        if (observedHotspotR1ApsPerEssInScanHistogram.length > 0)
        {
          i = 0;
          while (i < observedHotspotR1ApsPerEssInScanHistogram.length)
          {
            localObject = observedHotspotR1ApsPerEssInScanHistogram[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(87, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      m = i;
      if (observedHotspotR2ApsPerEssInScanHistogram != null)
      {
        m = i;
        if (observedHotspotR2ApsPerEssInScanHistogram.length > 0)
        {
          k = i;
          m = 0;
          while (m < observedHotspotR2ApsPerEssInScanHistogram.length)
          {
            localObject = observedHotspotR2ApsPerEssInScanHistogram[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(88, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
          m = k;
        }
      }
      k = m;
      if (softApConnectedClientsEventsTethered != null)
      {
        k = m;
        if (softApConnectedClientsEventsTethered.length > 0)
        {
          k = m;
          i = 0;
          while (i < softApConnectedClientsEventsTethered.length)
          {
            localObject = softApConnectedClientsEventsTethered[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(89, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
        }
      }
      i = k;
      if (softApConnectedClientsEventsLocalOnly != null)
      {
        i = k;
        if (softApConnectedClientsEventsLocalOnly.length > 0)
        {
          i = 0;
          while (i < softApConnectedClientsEventsLocalOnly.length)
          {
            localObject = softApConnectedClientsEventsLocalOnly[i];
            m = k;
            if (localObject != null) {
              m = k + CodedOutputByteBufferNano.computeMessageSize(90, (MessageNano)localObject);
            }
            i++;
            k = m;
          }
          i = k;
        }
      }
      k = i;
      if (wpsMetrics != null) {
        k = i + CodedOutputByteBufferNano.computeMessageSize(91, wpsMetrics);
      }
      i = k;
      if (wifiPowerStats != null) {
        i = k + CodedOutputByteBufferNano.computeMessageSize(92, wifiPowerStats);
      }
      m = i;
      if (numConnectivityOneshotScans != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(93, numConnectivityOneshotScans);
      }
      k = m;
      if (wifiWakeStats != null) {
        k = m + CodedOutputByteBufferNano.computeMessageSize(94, wifiWakeStats);
      }
      i = k;
      if (observed80211McSupportingApsInScanHistogram != null)
      {
        i = k;
        if (observed80211McSupportingApsInScanHistogram.length > 0)
        {
          m = j;
          for (;;)
          {
            i = k;
            if (m >= observed80211McSupportingApsInScanHistogram.length) {
              break;
            }
            localObject = observed80211McSupportingApsInScanHistogram[m];
            i = k;
            if (localObject != null) {
              i = k + CodedOutputByteBufferNano.computeMessageSize(95, (MessageNano)localObject);
            }
            m++;
            k = i;
          }
        }
      }
      m = i;
      if (numSupplicantCrashes != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(96, numSupplicantCrashes);
      }
      k = m;
      if (numHostapdCrashes != 0) {
        k = m + CodedOutputByteBufferNano.computeInt32Size(97, numHostapdCrashes);
      }
      i = k;
      if (numSetupClientInterfaceFailureDueToSupplicant != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(98, numSetupClientInterfaceFailureDueToSupplicant);
      }
      m = i;
      if (numSetupSoftApInterfaceFailureDueToHal != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(99, numSetupSoftApInterfaceFailureDueToHal);
      }
      k = m;
      if (numSetupSoftApInterfaceFailureDueToWificond != 0) {
        k = m + CodedOutputByteBufferNano.computeInt32Size(100, numSetupSoftApInterfaceFailureDueToWificond);
      }
      i = k;
      if (numSetupSoftApInterfaceFailureDueToHostapd != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(101, numSetupSoftApInterfaceFailureDueToHostapd);
      }
      m = i;
      if (numClientInterfaceDown != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(102, numClientInterfaceDown);
      }
      k = m;
      if (numSoftApInterfaceDown != 0) {
        k = m + CodedOutputByteBufferNano.computeInt32Size(103, numSoftApInterfaceDown);
      }
      i = k;
      if (numExternalAppOneshotScanRequests != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(104, numExternalAppOneshotScanRequests);
      }
      k = i;
      if (numExternalForegroundAppOneshotScanRequestsThrottled != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(105, numExternalForegroundAppOneshotScanRequestsThrottled);
      }
      i = k;
      if (numExternalBackgroundAppOneshotScanRequestsThrottled != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(106, numExternalBackgroundAppOneshotScanRequestsThrottled);
      }
      k = i;
      if (watchdogTriggerToConnectionSuccessDurationMs != -1L) {
        k = i + CodedOutputByteBufferNano.computeInt64Size(107, watchdogTriggerToConnectionSuccessDurationMs);
      }
      i = k;
      if (watchdogTotalConnectionFailureCountAfterTrigger != 0L) {
        i = k + CodedOutputByteBufferNano.computeInt64Size(108, watchdogTotalConnectionFailureCountAfterTrigger);
      }
      k = i;
      if (numOneshotHasDfsChannelScans != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(109, numOneshotHasDfsChannelScans);
      }
      i = k;
      if (wifiRttLog != null) {
        i = k + CodedOutputByteBufferNano.computeMessageSize(110, wifiRttLog);
      }
      k = i;
      if (isMacRandomizationOn) {
        k = i + CodedOutputByteBufferNano.computeBoolSize(111, isMacRandomizationOn);
      }
      i = k;
      if (numRadioModeChangeToMcc != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(112, numRadioModeChangeToMcc);
      }
      k = i;
      if (numRadioModeChangeToScc != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(113, numRadioModeChangeToScc);
      }
      i = k;
      if (numRadioModeChangeToSbs != 0) {
        i = k + CodedOutputByteBufferNano.computeInt32Size(114, numRadioModeChangeToSbs);
      }
      m = i;
      if (numRadioModeChangeToDbs != 0) {
        m = i + CodedOutputByteBufferNano.computeInt32Size(115, numRadioModeChangeToDbs);
      }
      k = m;
      if (numSoftApUserBandPreferenceUnsatisfied != 0) {
        k = m + CodedOutputByteBufferNano.computeInt32Size(116, numSoftApUserBandPreferenceUnsatisfied);
      }
      i = k;
      if (!scoreExperimentId.equals("")) {
        i = k + CodedOutputByteBufferNano.computeStringSize(117, scoreExperimentId);
      }
      return i;
    }
    
    public WifiLog mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        int j;
        Object localObject;
        switch (i)
        {
        default: 
          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            return this;
          }
          break;
        case 938: 
          scoreExperimentId = paramCodedInputByteBufferNano.readString();
          break;
        case 928: 
          numSoftApUserBandPreferenceUnsatisfied = paramCodedInputByteBufferNano.readInt32();
          break;
        case 920: 
          numRadioModeChangeToDbs = paramCodedInputByteBufferNano.readInt32();
          break;
        case 912: 
          numRadioModeChangeToSbs = paramCodedInputByteBufferNano.readInt32();
          break;
        case 904: 
          numRadioModeChangeToScc = paramCodedInputByteBufferNano.readInt32();
          break;
        case 896: 
          numRadioModeChangeToMcc = paramCodedInputByteBufferNano.readInt32();
          break;
        case 888: 
          isMacRandomizationOn = paramCodedInputByteBufferNano.readBool();
          break;
        case 882: 
          if (wifiRttLog == null) {
            wifiRttLog = new WifiMetricsProto.WifiRttLog();
          }
          paramCodedInputByteBufferNano.readMessage(wifiRttLog);
          break;
        case 872: 
          numOneshotHasDfsChannelScans = paramCodedInputByteBufferNano.readInt32();
          break;
        case 864: 
          watchdogTotalConnectionFailureCountAfterTrigger = paramCodedInputByteBufferNano.readInt64();
          break;
        case 856: 
          watchdogTriggerToConnectionSuccessDurationMs = paramCodedInputByteBufferNano.readInt64();
          break;
        case 848: 
          numExternalBackgroundAppOneshotScanRequestsThrottled = paramCodedInputByteBufferNano.readInt32();
          break;
        case 840: 
          numExternalForegroundAppOneshotScanRequestsThrottled = paramCodedInputByteBufferNano.readInt32();
          break;
        case 832: 
          numExternalAppOneshotScanRequests = paramCodedInputByteBufferNano.readInt32();
          break;
        case 824: 
          numSoftApInterfaceDown = paramCodedInputByteBufferNano.readInt32();
          break;
        case 816: 
          numClientInterfaceDown = paramCodedInputByteBufferNano.readInt32();
          break;
        case 808: 
          numSetupSoftApInterfaceFailureDueToHostapd = paramCodedInputByteBufferNano.readInt32();
          break;
        case 800: 
          numSetupSoftApInterfaceFailureDueToWificond = paramCodedInputByteBufferNano.readInt32();
          break;
        case 792: 
          numSetupSoftApInterfaceFailureDueToHal = paramCodedInputByteBufferNano.readInt32();
          break;
        case 784: 
          numSetupClientInterfaceFailureDueToSupplicant = paramCodedInputByteBufferNano.readInt32();
          break;
        case 776: 
          numHostapdCrashes = paramCodedInputByteBufferNano.readInt32();
          break;
        case 768: 
          numSupplicantCrashes = paramCodedInputByteBufferNano.readInt32();
          break;
        case 762: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 762);
          if (observed80211McSupportingApsInScanHistogram == null) {
            i = 0;
          } else {
            i = observed80211McSupportingApsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(observed80211McSupportingApsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          observed80211McSupportingApsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 754: 
          if (wifiWakeStats == null) {
            wifiWakeStats = new WifiMetricsProto.WifiWakeStats();
          }
          paramCodedInputByteBufferNano.readMessage(wifiWakeStats);
          break;
        case 744: 
          numConnectivityOneshotScans = paramCodedInputByteBufferNano.readInt32();
          break;
        case 738: 
          if (wifiPowerStats == null) {
            wifiPowerStats = new WifiMetricsProto.WifiPowerStats();
          }
          paramCodedInputByteBufferNano.readMessage(wifiPowerStats);
          break;
        case 730: 
          if (wpsMetrics == null) {
            wpsMetrics = new WifiMetricsProto.WpsMetrics();
          }
          paramCodedInputByteBufferNano.readMessage(wpsMetrics);
          break;
        case 722: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 722);
          if (softApConnectedClientsEventsLocalOnly == null) {
            i = 0;
          } else {
            i = softApConnectedClientsEventsLocalOnly.length;
          }
          localObject = new WifiMetricsProto.SoftApConnectedClientsEvent[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(softApConnectedClientsEventsLocalOnly, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.SoftApConnectedClientsEvent();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.SoftApConnectedClientsEvent();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          softApConnectedClientsEventsLocalOnly = ((WifiMetricsProto.SoftApConnectedClientsEvent[])localObject);
          break;
        case 714: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 714);
          if (softApConnectedClientsEventsTethered == null) {
            i = 0;
          } else {
            i = softApConnectedClientsEventsTethered.length;
          }
          localObject = new WifiMetricsProto.SoftApConnectedClientsEvent[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(softApConnectedClientsEventsTethered, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.SoftApConnectedClientsEvent();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.SoftApConnectedClientsEvent();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          softApConnectedClientsEventsTethered = ((WifiMetricsProto.SoftApConnectedClientsEvent[])localObject);
          break;
        case 706: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 706);
          if (observedHotspotR2ApsPerEssInScanHistogram == null) {
            i = 0;
          } else {
            i = observedHotspotR2ApsPerEssInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(observedHotspotR2ApsPerEssInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          observedHotspotR2ApsPerEssInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 698: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 698);
          if (observedHotspotR1ApsPerEssInScanHistogram == null) {
            i = 0;
          } else {
            i = observedHotspotR1ApsPerEssInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(observedHotspotR1ApsPerEssInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          observedHotspotR1ApsPerEssInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 690: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 690);
          if (observedHotspotR2EssInScanHistogram == null) {
            i = 0;
          } else {
            i = observedHotspotR2EssInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(observedHotspotR2EssInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          observedHotspotR2EssInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 682: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 682);
          if (observedHotspotR1EssInScanHistogram == null) {
            i = 0;
          } else {
            i = observedHotspotR1EssInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(observedHotspotR1EssInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          observedHotspotR1EssInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 674: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 674);
          if (observedHotspotR2ApsInScanHistogram == null) {
            i = 0;
          } else {
            i = observedHotspotR2ApsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(observedHotspotR2ApsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          observedHotspotR2ApsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 666: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 666);
          if (observedHotspotR1ApsInScanHistogram == null) {
            i = 0;
          } else {
            i = observedHotspotR1ApsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(observedHotspotR1ApsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          observedHotspotR1ApsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 656: 
          numOpenNetworkConnectMessageFailedToSend = paramCodedInputByteBufferNano.readInt32();
          break;
        case 648: 
          numOpenNetworkRecommendationUpdates = paramCodedInputByteBufferNano.readInt32();
          break;
        case 640: 
          isWifiNetworksAvailableNotificationOn = paramCodedInputByteBufferNano.readBool();
          break;
        case 632: 
          openNetworkRecommenderBlacklistSize = paramCodedInputByteBufferNano.readInt32();
          break;
        case 626: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 626);
          if (connectToNetworkNotificationActionCount == null) {
            i = 0;
          } else {
            i = connectToNetworkNotificationActionCount.length;
          }
          localObject = new WifiMetricsProto.ConnectToNetworkNotificationAndActionCount[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(connectToNetworkNotificationActionCount, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.ConnectToNetworkNotificationAndActionCount();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.ConnectToNetworkNotificationAndActionCount();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          connectToNetworkNotificationActionCount = ((WifiMetricsProto.ConnectToNetworkNotificationAndActionCount[])localObject);
          break;
        case 618: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 618);
          if (connectToNetworkNotificationCount == null) {
            i = 0;
          } else {
            i = connectToNetworkNotificationCount.length;
          }
          localObject = new WifiMetricsProto.ConnectToNetworkNotificationAndActionCount[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(connectToNetworkNotificationCount, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.ConnectToNetworkNotificationAndActionCount();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.ConnectToNetworkNotificationAndActionCount();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          connectToNetworkNotificationCount = ((WifiMetricsProto.ConnectToNetworkNotificationAndActionCount[])localObject);
          break;
        case 610: 
          if (pnoScanMetrics == null) {
            pnoScanMetrics = new WifiMetricsProto.PnoScanMetrics();
          }
          paramCodedInputByteBufferNano.readMessage(pnoScanMetrics);
          break;
        case 600: 
          partialAllSingleScanListenerResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 592: 
          fullBandAllSingleScanListenerResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 586: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 586);
          if (availableSavedPasspointProviderBssidsInScanHistogram == null) {
            i = 0;
          } else {
            i = availableSavedPasspointProviderBssidsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(availableSavedPasspointProviderBssidsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          availableSavedPasspointProviderBssidsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 578: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 578);
          if (availableSavedPasspointProviderProfilesInScanHistogram == null) {
            i = 0;
          } else {
            i = availableSavedPasspointProviderProfilesInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(availableSavedPasspointProviderProfilesInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          availableSavedPasspointProviderProfilesInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 570: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 570);
          if (availableOpenOrSavedBssidsInScanHistogram == null) {
            i = 0;
          } else {
            i = availableOpenOrSavedBssidsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(availableOpenOrSavedBssidsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          availableOpenOrSavedBssidsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 562: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 562);
          if (availableOpenOrSavedSsidsInScanHistogram == null) {
            i = 0;
          } else {
            i = availableOpenOrSavedSsidsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(availableOpenOrSavedSsidsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          availableOpenOrSavedSsidsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 554: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 554);
          if (availableSavedBssidsInScanHistogram == null) {
            i = 0;
          } else {
            i = availableSavedBssidsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(availableSavedBssidsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          availableSavedBssidsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 546: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 546);
          if (availableSavedSsidsInScanHistogram == null) {
            i = 0;
          } else {
            i = availableSavedSsidsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(availableSavedSsidsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          availableSavedSsidsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 538: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 538);
          if (availableOpenBssidsInScanHistogram == null) {
            i = 0;
          } else {
            i = availableOpenBssidsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(availableOpenBssidsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          availableOpenBssidsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 530: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 530);
          if (availableOpenSsidsInScanHistogram == null) {
            i = 0;
          } else {
            i = availableOpenSsidsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(availableOpenSsidsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          availableOpenSsidsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 522: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 522);
          if (totalBssidsInScanHistogram == null) {
            i = 0;
          } else {
            i = totalBssidsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(totalBssidsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          totalBssidsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 514: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 514);
          if (totalSsidsInScanHistogram == null) {
            i = 0;
          } else {
            i = totalSsidsInScanHistogram.length;
          }
          localObject = new WifiMetricsProto.NumConnectableNetworksBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(totalSsidsInScanHistogram, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.NumConnectableNetworksBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          totalSsidsInScanHistogram = ((WifiMetricsProto.NumConnectableNetworksBucket[])localObject);
          break;
        case 504: 
          numPasspointProvidersSuccessfullyConnected = paramCodedInputByteBufferNano.readInt32();
          break;
        case 496: 
          numPasspointProviderUninstallSuccess = paramCodedInputByteBufferNano.readInt32();
          break;
        case 488: 
          numPasspointProviderUninstallation = paramCodedInputByteBufferNano.readInt32();
          break;
        case 480: 
          numPasspointProviderInstallSuccess = paramCodedInputByteBufferNano.readInt32();
          break;
        case 472: 
          numPasspointProviderInstallation = paramCodedInputByteBufferNano.readInt32();
          break;
        case 464: 
          numPasspointProviders = paramCodedInputByteBufferNano.readInt32();
          break;
        case 458: 
          if (wifiAwareLog == null) {
            wifiAwareLog = new WifiMetricsProto.WifiAwareLog();
          }
          paramCodedInputByteBufferNano.readMessage(wifiAwareLog);
          break;
        case 448: 
          numSetupClientInterfaceFailureDueToWificond = paramCodedInputByteBufferNano.readInt32();
          break;
        case 440: 
          numSetupClientInterfaceFailureDueToHal = paramCodedInputByteBufferNano.readInt32();
          break;
        case 432: 
          numWificondCrashes = paramCodedInputByteBufferNano.readInt32();
          break;
        case 424: 
          numHalCrashes = paramCodedInputByteBufferNano.readInt32();
          break;
        case 418: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 418);
          if (staEventList == null) {
            i = 0;
          } else {
            i = staEventList.length;
          }
          localObject = new WifiMetricsProto.StaEvent[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(staEventList, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.StaEvent();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.StaEvent();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          staEventList = ((WifiMetricsProto.StaEvent[])localObject);
          break;
        case 410: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 410);
          if (rssiPollDeltaCount == null) {
            i = 0;
          } else {
            i = rssiPollDeltaCount.length;
          }
          localObject = new WifiMetricsProto.RssiPollCount[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(rssiPollDeltaCount, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.RssiPollCount();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.RssiPollCount();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          rssiPollDeltaCount = ((WifiMetricsProto.RssiPollCount[])localObject);
          break;
        case 402: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 402);
          if (softApReturnCode == null) {
            i = 0;
          } else {
            i = softApReturnCode.length;
          }
          localObject = new WifiMetricsProto.SoftApReturnCodeCount[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(softApReturnCode, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.SoftApReturnCodeCount();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.SoftApReturnCodeCount();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          softApReturnCode = ((WifiMetricsProto.SoftApReturnCodeCount[])localObject);
          break;
        case 394: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 394);
          if (softApDuration == null) {
            i = 0;
          } else {
            i = softApDuration.length;
          }
          localObject = new WifiMetricsProto.SoftApDurationBucket[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(softApDuration, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.SoftApDurationBucket();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.SoftApDurationBucket();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          softApDuration = ((WifiMetricsProto.SoftApDurationBucket[])localObject);
          break;
        case 386: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 386);
          if (wifiScoreCount == null) {
            i = 0;
          } else {
            i = wifiScoreCount.length;
          }
          localObject = new WifiMetricsProto.WifiScoreCount[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(wifiScoreCount, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.WifiScoreCount();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.WifiScoreCount();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          wifiScoreCount = ((WifiMetricsProto.WifiScoreCount[])localObject);
          break;
        case 378: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 378);
          if (alertReasonCount == null) {
            i = 0;
          } else {
            i = alertReasonCount.length;
          }
          localObject = new WifiMetricsProto.AlertReasonCount[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(alertReasonCount, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.AlertReasonCount();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.AlertReasonCount();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          alertReasonCount = ((WifiMetricsProto.AlertReasonCount[])localObject);
          break;
        case 368: 
          numScans = paramCodedInputByteBufferNano.readInt32();
          break;
        case 360: 
          numHotspot2R2NetworkScanResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 352: 
          numHotspot2R1NetworkScanResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 344: 
          numHiddenNetworkScanResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 336: 
          numEnterpriseNetworkScanResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 328: 
          numPersonalNetworkScanResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 320: 
          numOpenNetworkScanResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 312: 
          numTotalScanResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 304: 
          numPasspointNetworks = paramCodedInputByteBufferNano.readInt32();
          break;
        case 296: 
          numHiddenNetworks = paramCodedInputByteBufferNano.readInt32();
          break;
        case 288: 
          numLastResortWatchdogSuccesses = paramCodedInputByteBufferNano.readInt32();
          break;
        case 282: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 282);
          if (rssiPollRssiCount == null) {
            i = 0;
          } else {
            i = rssiPollRssiCount.length;
          }
          localObject = new WifiMetricsProto.RssiPollCount[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(rssiPollRssiCount, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.RssiPollCount();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.RssiPollCount();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          rssiPollRssiCount = ((WifiMetricsProto.RssiPollCount[])localObject);
          break;
        case 272: 
          recordDurationSec = paramCodedInputByteBufferNano.readInt32();
          break;
        case 264: 
          numConnectivityWatchdogBackgroundBad = paramCodedInputByteBufferNano.readInt32();
          break;
        case 256: 
          numConnectivityWatchdogBackgroundGood = paramCodedInputByteBufferNano.readInt32();
          break;
        case 248: 
          numConnectivityWatchdogPnoBad = paramCodedInputByteBufferNano.readInt32();
          break;
        case 240: 
          numConnectivityWatchdogPnoGood = paramCodedInputByteBufferNano.readInt32();
          break;
        case 232: 
          numLastResortWatchdogTriggersWithBadOther = paramCodedInputByteBufferNano.readInt32();
          break;
        case 224: 
          numLastResortWatchdogTriggersWithBadDhcp = paramCodedInputByteBufferNano.readInt32();
          break;
        case 216: 
          numLastResortWatchdogTriggersWithBadAuthentication = paramCodedInputByteBufferNano.readInt32();
          break;
        case 208: 
          numLastResortWatchdogTriggersWithBadAssociation = paramCodedInputByteBufferNano.readInt32();
          break;
        case 200: 
          numLastResortWatchdogAvailableNetworksTotal = paramCodedInputByteBufferNano.readInt32();
          break;
        case 192: 
          numLastResortWatchdogBadOtherNetworksTotal = paramCodedInputByteBufferNano.readInt32();
          break;
        case 184: 
          numLastResortWatchdogBadDhcpNetworksTotal = paramCodedInputByteBufferNano.readInt32();
          break;
        case 176: 
          numLastResortWatchdogBadAuthenticationNetworksTotal = paramCodedInputByteBufferNano.readInt32();
          break;
        case 168: 
          numLastResortWatchdogBadAssociationNetworksTotal = paramCodedInputByteBufferNano.readInt32();
          break;
        case 160: 
          numLastResortWatchdogTriggers = paramCodedInputByteBufferNano.readInt32();
          break;
        case 154: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 154);
          if (backgroundScanRequestState == null) {
            i = 0;
          } else {
            i = backgroundScanRequestState.length;
          }
          localObject = new WifiSystemStateEntry[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(backgroundScanRequestState, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiSystemStateEntry();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiSystemStateEntry();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          backgroundScanRequestState = ((WifiSystemStateEntry[])localObject);
          break;
        case 146: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 146);
          if (backgroundScanReturnEntries == null) {
            i = 0;
          } else {
            i = backgroundScanReturnEntries.length;
          }
          localObject = new ScanReturnEntry[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(backgroundScanReturnEntries, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new ScanReturnEntry();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new ScanReturnEntry();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          backgroundScanReturnEntries = ((ScanReturnEntry[])localObject);
          break;
        case 138: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 138);
          if (wifiSystemStateEntries == null) {
            i = 0;
          } else {
            i = wifiSystemStateEntries.length;
          }
          localObject = new WifiSystemStateEntry[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(wifiSystemStateEntries, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiSystemStateEntry();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiSystemStateEntry();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          wifiSystemStateEntries = ((WifiSystemStateEntry[])localObject);
          break;
        case 130: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 130);
          if (scanReturnEntries == null) {
            i = 0;
          } else {
            i = scanReturnEntries.length;
          }
          localObject = new ScanReturnEntry[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(scanReturnEntries, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new ScanReturnEntry();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new ScanReturnEntry();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          scanReturnEntries = ((ScanReturnEntry[])localObject);
          break;
        case 120: 
          numBackgroundScans = paramCodedInputByteBufferNano.readInt32();
          break;
        case 112: 
          numOneshotScans = paramCodedInputByteBufferNano.readInt32();
          break;
        case 104: 
          numNonEmptyScanResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 96: 
          numEmptyScanResults = paramCodedInputByteBufferNano.readInt32();
          break;
        case 88: 
          numNetworksAddedByApps = paramCodedInputByteBufferNano.readInt32();
          break;
        case 80: 
          numNetworksAddedByUser = paramCodedInputByteBufferNano.readInt32();
          break;
        case 72: 
          numWifiToggledViaAirplane = paramCodedInputByteBufferNano.readInt32();
          break;
        case 64: 
          numWifiToggledViaSettings = paramCodedInputByteBufferNano.readInt32();
          break;
        case 56: 
          isScanningAlwaysEnabled = paramCodedInputByteBufferNano.readBool();
          break;
        case 48: 
          isLocationEnabled = paramCodedInputByteBufferNano.readBool();
          break;
        case 40: 
          numEnterpriseNetworks = paramCodedInputByteBufferNano.readInt32();
          break;
        case 32: 
          numPersonalNetworks = paramCodedInputByteBufferNano.readInt32();
          break;
        case 24: 
          numOpenNetworks = paramCodedInputByteBufferNano.readInt32();
          break;
        case 16: 
          numSavedNetworks = paramCodedInputByteBufferNano.readInt32();
          break;
        case 10: 
          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 10);
          if (connectionEvent == null) {
            i = 0;
          } else {
            i = connectionEvent.length;
          }
          localObject = new WifiMetricsProto.ConnectionEvent[i + j];
          j = i;
          if (i != 0) {
            System.arraycopy(connectionEvent, 0, localObject, 0, i);
          }
          for (j = i; j < localObject.length - 1; j++)
          {
            localObject[j] = new WifiMetricsProto.ConnectionEvent();
            paramCodedInputByteBufferNano.readMessage(localObject[j]);
            paramCodedInputByteBufferNano.readTag();
          }
          localObject[j] = new WifiMetricsProto.ConnectionEvent();
          paramCodedInputByteBufferNano.readMessage(localObject[j]);
          connectionEvent = ((WifiMetricsProto.ConnectionEvent[])localObject);
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      Object localObject = connectionEvent;
      int i = 0;
      int j;
      if ((localObject != null) && (connectionEvent.length > 0)) {
        for (j = 0; j < connectionEvent.length; j++)
        {
          localObject = connectionEvent[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(1, (MessageNano)localObject);
          }
        }
      }
      if (numSavedNetworks != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, numSavedNetworks);
      }
      if (numOpenNetworks != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, numOpenNetworks);
      }
      if (numPersonalNetworks != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, numPersonalNetworks);
      }
      if (numEnterpriseNetworks != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, numEnterpriseNetworks);
      }
      if (isLocationEnabled) {
        paramCodedOutputByteBufferNano.writeBool(6, isLocationEnabled);
      }
      if (isScanningAlwaysEnabled) {
        paramCodedOutputByteBufferNano.writeBool(7, isScanningAlwaysEnabled);
      }
      if (numWifiToggledViaSettings != 0) {
        paramCodedOutputByteBufferNano.writeInt32(8, numWifiToggledViaSettings);
      }
      if (numWifiToggledViaAirplane != 0) {
        paramCodedOutputByteBufferNano.writeInt32(9, numWifiToggledViaAirplane);
      }
      if (numNetworksAddedByUser != 0) {
        paramCodedOutputByteBufferNano.writeInt32(10, numNetworksAddedByUser);
      }
      if (numNetworksAddedByApps != 0) {
        paramCodedOutputByteBufferNano.writeInt32(11, numNetworksAddedByApps);
      }
      if (numEmptyScanResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(12, numEmptyScanResults);
      }
      if (numNonEmptyScanResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(13, numNonEmptyScanResults);
      }
      if (numOneshotScans != 0) {
        paramCodedOutputByteBufferNano.writeInt32(14, numOneshotScans);
      }
      if (numBackgroundScans != 0) {
        paramCodedOutputByteBufferNano.writeInt32(15, numBackgroundScans);
      }
      if ((scanReturnEntries != null) && (scanReturnEntries.length > 0)) {
        for (j = 0; j < scanReturnEntries.length; j++)
        {
          localObject = scanReturnEntries[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(16, (MessageNano)localObject);
          }
        }
      }
      if ((wifiSystemStateEntries != null) && (wifiSystemStateEntries.length > 0)) {
        for (j = 0; j < wifiSystemStateEntries.length; j++)
        {
          localObject = wifiSystemStateEntries[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(17, (MessageNano)localObject);
          }
        }
      }
      if ((backgroundScanReturnEntries != null) && (backgroundScanReturnEntries.length > 0)) {
        for (j = 0; j < backgroundScanReturnEntries.length; j++)
        {
          localObject = backgroundScanReturnEntries[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(18, (MessageNano)localObject);
          }
        }
      }
      if ((backgroundScanRequestState != null) && (backgroundScanRequestState.length > 0)) {
        for (j = 0; j < backgroundScanRequestState.length; j++)
        {
          localObject = backgroundScanRequestState[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(19, (MessageNano)localObject);
          }
        }
      }
      if (numLastResortWatchdogTriggers != 0) {
        paramCodedOutputByteBufferNano.writeInt32(20, numLastResortWatchdogTriggers);
      }
      if (numLastResortWatchdogBadAssociationNetworksTotal != 0) {
        paramCodedOutputByteBufferNano.writeInt32(21, numLastResortWatchdogBadAssociationNetworksTotal);
      }
      if (numLastResortWatchdogBadAuthenticationNetworksTotal != 0) {
        paramCodedOutputByteBufferNano.writeInt32(22, numLastResortWatchdogBadAuthenticationNetworksTotal);
      }
      if (numLastResortWatchdogBadDhcpNetworksTotal != 0) {
        paramCodedOutputByteBufferNano.writeInt32(23, numLastResortWatchdogBadDhcpNetworksTotal);
      }
      if (numLastResortWatchdogBadOtherNetworksTotal != 0) {
        paramCodedOutputByteBufferNano.writeInt32(24, numLastResortWatchdogBadOtherNetworksTotal);
      }
      if (numLastResortWatchdogAvailableNetworksTotal != 0) {
        paramCodedOutputByteBufferNano.writeInt32(25, numLastResortWatchdogAvailableNetworksTotal);
      }
      if (numLastResortWatchdogTriggersWithBadAssociation != 0) {
        paramCodedOutputByteBufferNano.writeInt32(26, numLastResortWatchdogTriggersWithBadAssociation);
      }
      if (numLastResortWatchdogTriggersWithBadAuthentication != 0) {
        paramCodedOutputByteBufferNano.writeInt32(27, numLastResortWatchdogTriggersWithBadAuthentication);
      }
      if (numLastResortWatchdogTriggersWithBadDhcp != 0) {
        paramCodedOutputByteBufferNano.writeInt32(28, numLastResortWatchdogTriggersWithBadDhcp);
      }
      if (numLastResortWatchdogTriggersWithBadOther != 0) {
        paramCodedOutputByteBufferNano.writeInt32(29, numLastResortWatchdogTriggersWithBadOther);
      }
      if (numConnectivityWatchdogPnoGood != 0) {
        paramCodedOutputByteBufferNano.writeInt32(30, numConnectivityWatchdogPnoGood);
      }
      if (numConnectivityWatchdogPnoBad != 0) {
        paramCodedOutputByteBufferNano.writeInt32(31, numConnectivityWatchdogPnoBad);
      }
      if (numConnectivityWatchdogBackgroundGood != 0) {
        paramCodedOutputByteBufferNano.writeInt32(32, numConnectivityWatchdogBackgroundGood);
      }
      if (numConnectivityWatchdogBackgroundBad != 0) {
        paramCodedOutputByteBufferNano.writeInt32(33, numConnectivityWatchdogBackgroundBad);
      }
      if (recordDurationSec != 0) {
        paramCodedOutputByteBufferNano.writeInt32(34, recordDurationSec);
      }
      if ((rssiPollRssiCount != null) && (rssiPollRssiCount.length > 0)) {
        for (j = 0; j < rssiPollRssiCount.length; j++)
        {
          localObject = rssiPollRssiCount[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(35, (MessageNano)localObject);
          }
        }
      }
      if (numLastResortWatchdogSuccesses != 0) {
        paramCodedOutputByteBufferNano.writeInt32(36, numLastResortWatchdogSuccesses);
      }
      if (numHiddenNetworks != 0) {
        paramCodedOutputByteBufferNano.writeInt32(37, numHiddenNetworks);
      }
      if (numPasspointNetworks != 0) {
        paramCodedOutputByteBufferNano.writeInt32(38, numPasspointNetworks);
      }
      if (numTotalScanResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(39, numTotalScanResults);
      }
      if (numOpenNetworkScanResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(40, numOpenNetworkScanResults);
      }
      if (numPersonalNetworkScanResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(41, numPersonalNetworkScanResults);
      }
      if (numEnterpriseNetworkScanResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(42, numEnterpriseNetworkScanResults);
      }
      if (numHiddenNetworkScanResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(43, numHiddenNetworkScanResults);
      }
      if (numHotspot2R1NetworkScanResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(44, numHotspot2R1NetworkScanResults);
      }
      if (numHotspot2R2NetworkScanResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(45, numHotspot2R2NetworkScanResults);
      }
      if (numScans != 0) {
        paramCodedOutputByteBufferNano.writeInt32(46, numScans);
      }
      if ((alertReasonCount != null) && (alertReasonCount.length > 0)) {
        for (j = 0; j < alertReasonCount.length; j++)
        {
          localObject = alertReasonCount[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(47, (MessageNano)localObject);
          }
        }
      }
      if ((wifiScoreCount != null) && (wifiScoreCount.length > 0)) {
        for (j = 0; j < wifiScoreCount.length; j++)
        {
          localObject = wifiScoreCount[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(48, (MessageNano)localObject);
          }
        }
      }
      if ((softApDuration != null) && (softApDuration.length > 0)) {
        for (j = 0; j < softApDuration.length; j++)
        {
          localObject = softApDuration[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(49, (MessageNano)localObject);
          }
        }
      }
      if ((softApReturnCode != null) && (softApReturnCode.length > 0)) {
        for (j = 0; j < softApReturnCode.length; j++)
        {
          localObject = softApReturnCode[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(50, (MessageNano)localObject);
          }
        }
      }
      if ((rssiPollDeltaCount != null) && (rssiPollDeltaCount.length > 0)) {
        for (j = 0; j < rssiPollDeltaCount.length; j++)
        {
          localObject = rssiPollDeltaCount[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(51, (MessageNano)localObject);
          }
        }
      }
      if ((staEventList != null) && (staEventList.length > 0)) {
        for (j = 0; j < staEventList.length; j++)
        {
          localObject = staEventList[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(52, (MessageNano)localObject);
          }
        }
      }
      if (numHalCrashes != 0) {
        paramCodedOutputByteBufferNano.writeInt32(53, numHalCrashes);
      }
      if (numWificondCrashes != 0) {
        paramCodedOutputByteBufferNano.writeInt32(54, numWificondCrashes);
      }
      if (numSetupClientInterfaceFailureDueToHal != 0) {
        paramCodedOutputByteBufferNano.writeInt32(55, numSetupClientInterfaceFailureDueToHal);
      }
      if (numSetupClientInterfaceFailureDueToWificond != 0) {
        paramCodedOutputByteBufferNano.writeInt32(56, numSetupClientInterfaceFailureDueToWificond);
      }
      if (wifiAwareLog != null) {
        paramCodedOutputByteBufferNano.writeMessage(57, wifiAwareLog);
      }
      if (numPasspointProviders != 0) {
        paramCodedOutputByteBufferNano.writeInt32(58, numPasspointProviders);
      }
      if (numPasspointProviderInstallation != 0) {
        paramCodedOutputByteBufferNano.writeInt32(59, numPasspointProviderInstallation);
      }
      if (numPasspointProviderInstallSuccess != 0) {
        paramCodedOutputByteBufferNano.writeInt32(60, numPasspointProviderInstallSuccess);
      }
      if (numPasspointProviderUninstallation != 0) {
        paramCodedOutputByteBufferNano.writeInt32(61, numPasspointProviderUninstallation);
      }
      if (numPasspointProviderUninstallSuccess != 0) {
        paramCodedOutputByteBufferNano.writeInt32(62, numPasspointProviderUninstallSuccess);
      }
      if (numPasspointProvidersSuccessfullyConnected != 0) {
        paramCodedOutputByteBufferNano.writeInt32(63, numPasspointProvidersSuccessfullyConnected);
      }
      if ((totalSsidsInScanHistogram != null) && (totalSsidsInScanHistogram.length > 0)) {
        for (j = 0; j < totalSsidsInScanHistogram.length; j++)
        {
          localObject = totalSsidsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(64, (MessageNano)localObject);
          }
        }
      }
      if ((totalBssidsInScanHistogram != null) && (totalBssidsInScanHistogram.length > 0)) {
        for (j = 0; j < totalBssidsInScanHistogram.length; j++)
        {
          localObject = totalBssidsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(65, (MessageNano)localObject);
          }
        }
      }
      if ((availableOpenSsidsInScanHistogram != null) && (availableOpenSsidsInScanHistogram.length > 0)) {
        for (j = 0; j < availableOpenSsidsInScanHistogram.length; j++)
        {
          localObject = availableOpenSsidsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(66, (MessageNano)localObject);
          }
        }
      }
      if ((availableOpenBssidsInScanHistogram != null) && (availableOpenBssidsInScanHistogram.length > 0)) {
        for (j = 0; j < availableOpenBssidsInScanHistogram.length; j++)
        {
          localObject = availableOpenBssidsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(67, (MessageNano)localObject);
          }
        }
      }
      if ((availableSavedSsidsInScanHistogram != null) && (availableSavedSsidsInScanHistogram.length > 0)) {
        for (j = 0; j < availableSavedSsidsInScanHistogram.length; j++)
        {
          localObject = availableSavedSsidsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(68, (MessageNano)localObject);
          }
        }
      }
      if ((availableSavedBssidsInScanHistogram != null) && (availableSavedBssidsInScanHistogram.length > 0)) {
        for (j = 0; j < availableSavedBssidsInScanHistogram.length; j++)
        {
          localObject = availableSavedBssidsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(69, (MessageNano)localObject);
          }
        }
      }
      if ((availableOpenOrSavedSsidsInScanHistogram != null) && (availableOpenOrSavedSsidsInScanHistogram.length > 0)) {
        for (j = 0; j < availableOpenOrSavedSsidsInScanHistogram.length; j++)
        {
          localObject = availableOpenOrSavedSsidsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(70, (MessageNano)localObject);
          }
        }
      }
      if ((availableOpenOrSavedBssidsInScanHistogram != null) && (availableOpenOrSavedBssidsInScanHistogram.length > 0)) {
        for (j = 0; j < availableOpenOrSavedBssidsInScanHistogram.length; j++)
        {
          localObject = availableOpenOrSavedBssidsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(71, (MessageNano)localObject);
          }
        }
      }
      if ((availableSavedPasspointProviderProfilesInScanHistogram != null) && (availableSavedPasspointProviderProfilesInScanHistogram.length > 0)) {
        for (j = 0; j < availableSavedPasspointProviderProfilesInScanHistogram.length; j++)
        {
          localObject = availableSavedPasspointProviderProfilesInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(72, (MessageNano)localObject);
          }
        }
      }
      if ((availableSavedPasspointProviderBssidsInScanHistogram != null) && (availableSavedPasspointProviderBssidsInScanHistogram.length > 0)) {
        for (j = 0; j < availableSavedPasspointProviderBssidsInScanHistogram.length; j++)
        {
          localObject = availableSavedPasspointProviderBssidsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(73, (MessageNano)localObject);
          }
        }
      }
      if (fullBandAllSingleScanListenerResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(74, fullBandAllSingleScanListenerResults);
      }
      if (partialAllSingleScanListenerResults != 0) {
        paramCodedOutputByteBufferNano.writeInt32(75, partialAllSingleScanListenerResults);
      }
      if (pnoScanMetrics != null) {
        paramCodedOutputByteBufferNano.writeMessage(76, pnoScanMetrics);
      }
      if ((connectToNetworkNotificationCount != null) && (connectToNetworkNotificationCount.length > 0)) {
        for (j = 0; j < connectToNetworkNotificationCount.length; j++)
        {
          localObject = connectToNetworkNotificationCount[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(77, (MessageNano)localObject);
          }
        }
      }
      if ((connectToNetworkNotificationActionCount != null) && (connectToNetworkNotificationActionCount.length > 0)) {
        for (j = 0; j < connectToNetworkNotificationActionCount.length; j++)
        {
          localObject = connectToNetworkNotificationActionCount[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(78, (MessageNano)localObject);
          }
        }
      }
      if (openNetworkRecommenderBlacklistSize != 0) {
        paramCodedOutputByteBufferNano.writeInt32(79, openNetworkRecommenderBlacklistSize);
      }
      if (isWifiNetworksAvailableNotificationOn) {
        paramCodedOutputByteBufferNano.writeBool(80, isWifiNetworksAvailableNotificationOn);
      }
      if (numOpenNetworkRecommendationUpdates != 0) {
        paramCodedOutputByteBufferNano.writeInt32(81, numOpenNetworkRecommendationUpdates);
      }
      if (numOpenNetworkConnectMessageFailedToSend != 0) {
        paramCodedOutputByteBufferNano.writeInt32(82, numOpenNetworkConnectMessageFailedToSend);
      }
      if ((observedHotspotR1ApsInScanHistogram != null) && (observedHotspotR1ApsInScanHistogram.length > 0)) {
        for (j = 0; j < observedHotspotR1ApsInScanHistogram.length; j++)
        {
          localObject = observedHotspotR1ApsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(83, (MessageNano)localObject);
          }
        }
      }
      if ((observedHotspotR2ApsInScanHistogram != null) && (observedHotspotR2ApsInScanHistogram.length > 0)) {
        for (j = 0; j < observedHotspotR2ApsInScanHistogram.length; j++)
        {
          localObject = observedHotspotR2ApsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(84, (MessageNano)localObject);
          }
        }
      }
      if ((observedHotspotR1EssInScanHistogram != null) && (observedHotspotR1EssInScanHistogram.length > 0)) {
        for (j = 0; j < observedHotspotR1EssInScanHistogram.length; j++)
        {
          localObject = observedHotspotR1EssInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(85, (MessageNano)localObject);
          }
        }
      }
      if ((observedHotspotR2EssInScanHistogram != null) && (observedHotspotR2EssInScanHistogram.length > 0)) {
        for (j = 0; j < observedHotspotR2EssInScanHistogram.length; j++)
        {
          localObject = observedHotspotR2EssInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(86, (MessageNano)localObject);
          }
        }
      }
      if ((observedHotspotR1ApsPerEssInScanHistogram != null) && (observedHotspotR1ApsPerEssInScanHistogram.length > 0)) {
        for (j = 0; j < observedHotspotR1ApsPerEssInScanHistogram.length; j++)
        {
          localObject = observedHotspotR1ApsPerEssInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(87, (MessageNano)localObject);
          }
        }
      }
      if ((observedHotspotR2ApsPerEssInScanHistogram != null) && (observedHotspotR2ApsPerEssInScanHistogram.length > 0)) {
        for (j = 0; j < observedHotspotR2ApsPerEssInScanHistogram.length; j++)
        {
          localObject = observedHotspotR2ApsPerEssInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(88, (MessageNano)localObject);
          }
        }
      }
      if ((softApConnectedClientsEventsTethered != null) && (softApConnectedClientsEventsTethered.length > 0)) {
        for (j = 0; j < softApConnectedClientsEventsTethered.length; j++)
        {
          localObject = softApConnectedClientsEventsTethered[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(89, (MessageNano)localObject);
          }
        }
      }
      if ((softApConnectedClientsEventsLocalOnly != null) && (softApConnectedClientsEventsLocalOnly.length > 0)) {
        for (j = 0; j < softApConnectedClientsEventsLocalOnly.length; j++)
        {
          localObject = softApConnectedClientsEventsLocalOnly[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(90, (MessageNano)localObject);
          }
        }
      }
      if (wpsMetrics != null) {
        paramCodedOutputByteBufferNano.writeMessage(91, wpsMetrics);
      }
      if (wifiPowerStats != null) {
        paramCodedOutputByteBufferNano.writeMessage(92, wifiPowerStats);
      }
      if (numConnectivityOneshotScans != 0) {
        paramCodedOutputByteBufferNano.writeInt32(93, numConnectivityOneshotScans);
      }
      if (wifiWakeStats != null) {
        paramCodedOutputByteBufferNano.writeMessage(94, wifiWakeStats);
      }
      if ((observed80211McSupportingApsInScanHistogram != null) && (observed80211McSupportingApsInScanHistogram.length > 0)) {
        for (j = i; j < observed80211McSupportingApsInScanHistogram.length; j++)
        {
          localObject = observed80211McSupportingApsInScanHistogram[j];
          if (localObject != null) {
            paramCodedOutputByteBufferNano.writeMessage(95, (MessageNano)localObject);
          }
        }
      }
      if (numSupplicantCrashes != 0) {
        paramCodedOutputByteBufferNano.writeInt32(96, numSupplicantCrashes);
      }
      if (numHostapdCrashes != 0) {
        paramCodedOutputByteBufferNano.writeInt32(97, numHostapdCrashes);
      }
      if (numSetupClientInterfaceFailureDueToSupplicant != 0) {
        paramCodedOutputByteBufferNano.writeInt32(98, numSetupClientInterfaceFailureDueToSupplicant);
      }
      if (numSetupSoftApInterfaceFailureDueToHal != 0) {
        paramCodedOutputByteBufferNano.writeInt32(99, numSetupSoftApInterfaceFailureDueToHal);
      }
      if (numSetupSoftApInterfaceFailureDueToWificond != 0) {
        paramCodedOutputByteBufferNano.writeInt32(100, numSetupSoftApInterfaceFailureDueToWificond);
      }
      if (numSetupSoftApInterfaceFailureDueToHostapd != 0) {
        paramCodedOutputByteBufferNano.writeInt32(101, numSetupSoftApInterfaceFailureDueToHostapd);
      }
      if (numClientInterfaceDown != 0) {
        paramCodedOutputByteBufferNano.writeInt32(102, numClientInterfaceDown);
      }
      if (numSoftApInterfaceDown != 0) {
        paramCodedOutputByteBufferNano.writeInt32(103, numSoftApInterfaceDown);
      }
      if (numExternalAppOneshotScanRequests != 0) {
        paramCodedOutputByteBufferNano.writeInt32(104, numExternalAppOneshotScanRequests);
      }
      if (numExternalForegroundAppOneshotScanRequestsThrottled != 0) {
        paramCodedOutputByteBufferNano.writeInt32(105, numExternalForegroundAppOneshotScanRequestsThrottled);
      }
      if (numExternalBackgroundAppOneshotScanRequestsThrottled != 0) {
        paramCodedOutputByteBufferNano.writeInt32(106, numExternalBackgroundAppOneshotScanRequestsThrottled);
      }
      if (watchdogTriggerToConnectionSuccessDurationMs != -1L) {
        paramCodedOutputByteBufferNano.writeInt64(107, watchdogTriggerToConnectionSuccessDurationMs);
      }
      if (watchdogTotalConnectionFailureCountAfterTrigger != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(108, watchdogTotalConnectionFailureCountAfterTrigger);
      }
      if (numOneshotHasDfsChannelScans != 0) {
        paramCodedOutputByteBufferNano.writeInt32(109, numOneshotHasDfsChannelScans);
      }
      if (wifiRttLog != null) {
        paramCodedOutputByteBufferNano.writeMessage(110, wifiRttLog);
      }
      if (isMacRandomizationOn) {
        paramCodedOutputByteBufferNano.writeBool(111, isMacRandomizationOn);
      }
      if (numRadioModeChangeToMcc != 0) {
        paramCodedOutputByteBufferNano.writeInt32(112, numRadioModeChangeToMcc);
      }
      if (numRadioModeChangeToScc != 0) {
        paramCodedOutputByteBufferNano.writeInt32(113, numRadioModeChangeToScc);
      }
      if (numRadioModeChangeToSbs != 0) {
        paramCodedOutputByteBufferNano.writeInt32(114, numRadioModeChangeToSbs);
      }
      if (numRadioModeChangeToDbs != 0) {
        paramCodedOutputByteBufferNano.writeInt32(115, numRadioModeChangeToDbs);
      }
      if (numSoftApUserBandPreferenceUnsatisfied != 0) {
        paramCodedOutputByteBufferNano.writeInt32(116, numSoftApUserBandPreferenceUnsatisfied);
      }
      if (!scoreExperimentId.equals("")) {
        paramCodedOutputByteBufferNano.writeString(117, scoreExperimentId);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static final class ScanReturnEntry
      extends MessageNano
    {
      private static volatile ScanReturnEntry[] _emptyArray;
      public int scanResultsCount;
      public int scanReturnCode;
      
      public ScanReturnEntry()
      {
        clear();
      }
      
      public static ScanReturnEntry[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new ScanReturnEntry[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static ScanReturnEntry parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new ScanReturnEntry().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static ScanReturnEntry parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (ScanReturnEntry)MessageNano.mergeFrom(new ScanReturnEntry(), paramArrayOfByte);
      }
      
      public ScanReturnEntry clear()
      {
        scanReturnCode = 0;
        scanResultsCount = 0;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (scanReturnCode != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, scanReturnCode);
        }
        i = j;
        if (scanResultsCount != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, scanResultsCount);
        }
        return i;
      }
      
      public ScanReturnEntry mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              scanResultsCount = paramCodedInputByteBufferNano.readInt32();
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
            case 4: 
              scanReturnCode = i;
            }
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (scanReturnCode != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, scanReturnCode);
        }
        if (scanResultsCount != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, scanResultsCount);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
    
    public static final class WifiSystemStateEntry
      extends MessageNano
    {
      private static volatile WifiSystemStateEntry[] _emptyArray;
      public boolean isScreenOn;
      public int wifiState;
      public int wifiStateCount;
      
      public WifiSystemStateEntry()
      {
        clear();
      }
      
      public static WifiSystemStateEntry[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new WifiSystemStateEntry[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static WifiSystemStateEntry parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new WifiSystemStateEntry().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static WifiSystemStateEntry parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (WifiSystemStateEntry)MessageNano.mergeFrom(new WifiSystemStateEntry(), paramArrayOfByte);
      }
      
      public WifiSystemStateEntry clear()
      {
        wifiState = 0;
        wifiStateCount = 0;
        isScreenOn = false;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (wifiState != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, wifiState);
        }
        i = j;
        if (wifiStateCount != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, wifiStateCount);
        }
        j = i;
        if (isScreenOn) {
          j = i + CodedOutputByteBufferNano.computeBoolSize(3, isScreenOn);
        }
        return j;
      }
      
      public WifiSystemStateEntry mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else {
                isScreenOn = paramCodedInputByteBufferNano.readBool();
              }
            }
            else {
              wifiStateCount = paramCodedInputByteBufferNano.readInt32();
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
              wifiState = i;
            }
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (wifiState != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, wifiState);
        }
        if (wifiStateCount != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, wifiStateCount);
        }
        if (isScreenOn) {
          paramCodedOutputByteBufferNano.writeBool(3, isScreenOn);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
  }
  
  public static final class WifiPowerStats
    extends MessageNano
  {
    private static volatile WifiPowerStats[] _emptyArray;
    public double energyConsumedMah;
    public long idleTimeMs;
    public long loggingDurationMs;
    public long rxTimeMs;
    public long txTimeMs;
    
    public WifiPowerStats()
    {
      clear();
    }
    
    public static WifiPowerStats[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new WifiPowerStats[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static WifiPowerStats parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new WifiPowerStats().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static WifiPowerStats parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (WifiPowerStats)MessageNano.mergeFrom(new WifiPowerStats(), paramArrayOfByte);
    }
    
    public WifiPowerStats clear()
    {
      loggingDurationMs = 0L;
      energyConsumedMah = 0.0D;
      idleTimeMs = 0L;
      rxTimeMs = 0L;
      txTimeMs = 0L;
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
      if (idleTimeMs != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(3, idleTimeMs);
      }
      i = j;
      if (rxTimeMs != 0L) {
        i = j + CodedOutputByteBufferNano.computeInt64Size(4, rxTimeMs);
      }
      j = i;
      if (txTimeMs != 0L) {
        j = i + CodedOutputByteBufferNano.computeInt64Size(5, txTimeMs);
      }
      return j;
    }
    
    public WifiPowerStats mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
          if (i != 17)
          {
            if (i != 24)
            {
              if (i != 32)
              {
                if (i != 40)
                {
                  if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                    return this;
                  }
                }
                else {
                  txTimeMs = paramCodedInputByteBufferNano.readInt64();
                }
              }
              else {
                rxTimeMs = paramCodedInputByteBufferNano.readInt64();
              }
            }
            else {
              idleTimeMs = paramCodedInputByteBufferNano.readInt64();
            }
          }
          else {
            energyConsumedMah = paramCodedInputByteBufferNano.readDouble();
          }
        }
        else {
          loggingDurationMs = paramCodedInputByteBufferNano.readInt64();
        }
      }
      return this;
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
      if (idleTimeMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(3, idleTimeMs);
      }
      if (rxTimeMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(4, rxTimeMs);
      }
      if (txTimeMs != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(5, txTimeMs);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class WifiRttLog
    extends MessageNano
  {
    public static final int ABORTED = 9;
    public static final int FAILURE = 2;
    public static final int FAIL_AP_ON_DIFF_CHANNEL = 7;
    public static final int FAIL_BUSY_TRY_LATER = 13;
    public static final int FAIL_FTM_PARAM_OVERRIDE = 16;
    public static final int FAIL_INVALID_TS = 10;
    public static final int FAIL_NOT_SCHEDULED_YET = 5;
    public static final int FAIL_NO_CAPABILITY = 8;
    public static final int FAIL_NO_RSP = 3;
    public static final int FAIL_PROTOCOL = 11;
    public static final int FAIL_REJECTED = 4;
    public static final int FAIL_SCHEDULE = 12;
    public static final int FAIL_TM_TIMEOUT = 6;
    public static final int INVALID_REQ = 14;
    public static final int MISSING_RESULT = 17;
    public static final int NO_WIFI = 15;
    public static final int OVERALL_AWARE_TRANSLATION_FAILURE = 7;
    public static final int OVERALL_FAIL = 2;
    public static final int OVERALL_HAL_FAILURE = 6;
    public static final int OVERALL_LOCATION_PERMISSION_MISSING = 8;
    public static final int OVERALL_RTT_NOT_AVAILABLE = 3;
    public static final int OVERALL_SUCCESS = 1;
    public static final int OVERALL_THROTTLE = 5;
    public static final int OVERALL_TIMEOUT = 4;
    public static final int OVERALL_UNKNOWN = 0;
    public static final int SUCCESS = 1;
    public static final int UNKNOWN = 0;
    private static volatile WifiRttLog[] _emptyArray;
    public RttOverallStatusHistogramBucket[] histogramOverallStatus;
    public int numRequests;
    public RttToPeerLog rttToAp;
    public RttToPeerLog rttToAware;
    
    public WifiRttLog()
    {
      clear();
    }
    
    public static WifiRttLog[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new WifiRttLog[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static WifiRttLog parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new WifiRttLog().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static WifiRttLog parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (WifiRttLog)MessageNano.mergeFrom(new WifiRttLog(), paramArrayOfByte);
    }
    
    public WifiRttLog clear()
    {
      numRequests = 0;
      histogramOverallStatus = RttOverallStatusHistogramBucket.emptyArray();
      rttToAp = null;
      rttToAware = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (numRequests != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, numRequests);
      }
      i = j;
      if (histogramOverallStatus != null)
      {
        i = j;
        if (histogramOverallStatus.length > 0)
        {
          int k = 0;
          for (;;)
          {
            i = j;
            if (k >= histogramOverallStatus.length) {
              break;
            }
            RttOverallStatusHistogramBucket localRttOverallStatusHistogramBucket = histogramOverallStatus[k];
            i = j;
            if (localRttOverallStatusHistogramBucket != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(2, localRttOverallStatusHistogramBucket);
            }
            k++;
            j = i;
          }
        }
      }
      j = i;
      if (rttToAp != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(3, rttToAp);
      }
      i = j;
      if (rttToAware != null) {
        i = j + CodedOutputByteBufferNano.computeMessageSize(4, rttToAware);
      }
      return i;
    }
    
    public WifiRttLog mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              if (i != 34)
              {
                if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else
              {
                if (rttToAware == null) {
                  rttToAware = new RttToPeerLog();
                }
                paramCodedInputByteBufferNano.readMessage(rttToAware);
              }
            }
            else
            {
              if (rttToAp == null) {
                rttToAp = new RttToPeerLog();
              }
              paramCodedInputByteBufferNano.readMessage(rttToAp);
            }
          }
          else
          {
            int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 18);
            if (histogramOverallStatus == null) {
              i = 0;
            } else {
              i = histogramOverallStatus.length;
            }
            RttOverallStatusHistogramBucket[] arrayOfRttOverallStatusHistogramBucket = new RttOverallStatusHistogramBucket[i + j];
            j = i;
            if (i != 0) {
              System.arraycopy(histogramOverallStatus, 0, arrayOfRttOverallStatusHistogramBucket, 0, i);
            }
            for (j = i; j < arrayOfRttOverallStatusHistogramBucket.length - 1; j++)
            {
              arrayOfRttOverallStatusHistogramBucket[j] = new RttOverallStatusHistogramBucket();
              paramCodedInputByteBufferNano.readMessage(arrayOfRttOverallStatusHistogramBucket[j]);
              paramCodedInputByteBufferNano.readTag();
            }
            arrayOfRttOverallStatusHistogramBucket[j] = new RttOverallStatusHistogramBucket();
            paramCodedInputByteBufferNano.readMessage(arrayOfRttOverallStatusHistogramBucket[j]);
            histogramOverallStatus = arrayOfRttOverallStatusHistogramBucket;
          }
        }
        else {
          numRequests = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (numRequests != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, numRequests);
      }
      if ((histogramOverallStatus != null) && (histogramOverallStatus.length > 0)) {
        for (int i = 0; i < histogramOverallStatus.length; i++)
        {
          RttOverallStatusHistogramBucket localRttOverallStatusHistogramBucket = histogramOverallStatus[i];
          if (localRttOverallStatusHistogramBucket != null) {
            paramCodedOutputByteBufferNano.writeMessage(2, localRttOverallStatusHistogramBucket);
          }
        }
      }
      if (rttToAp != null) {
        paramCodedOutputByteBufferNano.writeMessage(3, rttToAp);
      }
      if (rttToAware != null) {
        paramCodedOutputByteBufferNano.writeMessage(4, rttToAware);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static final class HistogramBucket
      extends MessageNano
    {
      private static volatile HistogramBucket[] _emptyArray;
      public int count;
      public long end;
      public long start;
      
      public HistogramBucket()
      {
        clear();
      }
      
      public static HistogramBucket[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new HistogramBucket[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static HistogramBucket parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new HistogramBucket().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static HistogramBucket parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (HistogramBucket)MessageNano.mergeFrom(new HistogramBucket(), paramArrayOfByte);
      }
      
      public HistogramBucket clear()
      {
        start = 0L;
        end = 0L;
        count = 0;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (start != 0L) {
          j = i + CodedOutputByteBufferNano.computeInt64Size(1, start);
        }
        i = j;
        if (end != 0L) {
          i = j + CodedOutputByteBufferNano.computeInt64Size(2, end);
        }
        j = i;
        if (count != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(3, count);
        }
        return j;
      }
      
      public HistogramBucket mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else {
                count = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              end = paramCodedInputByteBufferNano.readInt64();
            }
          }
          else {
            start = paramCodedInputByteBufferNano.readInt64();
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (start != 0L) {
          paramCodedOutputByteBufferNano.writeInt64(1, start);
        }
        if (end != 0L) {
          paramCodedOutputByteBufferNano.writeInt64(2, end);
        }
        if (count != 0) {
          paramCodedOutputByteBufferNano.writeInt32(3, count);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
    
    public static final class RttIndividualStatusHistogramBucket
      extends MessageNano
    {
      private static volatile RttIndividualStatusHistogramBucket[] _emptyArray;
      public int count;
      public int statusType;
      
      public RttIndividualStatusHistogramBucket()
      {
        clear();
      }
      
      public static RttIndividualStatusHistogramBucket[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new RttIndividualStatusHistogramBucket[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static RttIndividualStatusHistogramBucket parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new RttIndividualStatusHistogramBucket().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static RttIndividualStatusHistogramBucket parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (RttIndividualStatusHistogramBucket)MessageNano.mergeFrom(new RttIndividualStatusHistogramBucket(), paramArrayOfByte);
      }
      
      public RttIndividualStatusHistogramBucket clear()
      {
        statusType = 0;
        count = 0;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (statusType != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, statusType);
        }
        i = j;
        if (count != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, count);
        }
        return i;
      }
      
      public RttIndividualStatusHistogramBucket mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              count = paramCodedInputByteBufferNano.readInt32();
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
              statusType = i;
            }
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (statusType != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, statusType);
        }
        if (count != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, count);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
    
    public static final class RttOverallStatusHistogramBucket
      extends MessageNano
    {
      private static volatile RttOverallStatusHistogramBucket[] _emptyArray;
      public int count;
      public int statusType;
      
      public RttOverallStatusHistogramBucket()
      {
        clear();
      }
      
      public static RttOverallStatusHistogramBucket[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new RttOverallStatusHistogramBucket[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static RttOverallStatusHistogramBucket parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new RttOverallStatusHistogramBucket().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static RttOverallStatusHistogramBucket parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (RttOverallStatusHistogramBucket)MessageNano.mergeFrom(new RttOverallStatusHistogramBucket(), paramArrayOfByte);
      }
      
      public RttOverallStatusHistogramBucket clear()
      {
        statusType = 0;
        count = 0;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (statusType != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, statusType);
        }
        i = j;
        if (count != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, count);
        }
        return i;
      }
      
      public RttOverallStatusHistogramBucket mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
              count = paramCodedInputByteBufferNano.readInt32();
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
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
              statusType = i;
            }
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (statusType != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, statusType);
        }
        if (count != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, count);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
    
    public static final class RttToPeerLog
      extends MessageNano
    {
      private static volatile RttToPeerLog[] _emptyArray;
      public WifiMetricsProto.WifiRttLog.HistogramBucket[] histogramDistance;
      public WifiMetricsProto.WifiRttLog.RttIndividualStatusHistogramBucket[] histogramIndividualStatus;
      public WifiMetricsProto.WifiRttLog.HistogramBucket[] histogramNumPeersPerRequest;
      public WifiMetricsProto.WifiRttLog.HistogramBucket[] histogramNumRequestsPerApp;
      public WifiMetricsProto.WifiRttLog.HistogramBucket[] histogramRequestIntervalMs;
      public int numApps;
      public int numIndividualRequests;
      public int numRequests;
      
      public RttToPeerLog()
      {
        clear();
      }
      
      public static RttToPeerLog[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new RttToPeerLog[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static RttToPeerLog parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new RttToPeerLog().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static RttToPeerLog parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (RttToPeerLog)MessageNano.mergeFrom(new RttToPeerLog(), paramArrayOfByte);
      }
      
      public RttToPeerLog clear()
      {
        numRequests = 0;
        numIndividualRequests = 0;
        numApps = 0;
        histogramNumRequestsPerApp = WifiMetricsProto.WifiRttLog.HistogramBucket.emptyArray();
        histogramNumPeersPerRequest = WifiMetricsProto.WifiRttLog.HistogramBucket.emptyArray();
        histogramIndividualStatus = WifiMetricsProto.WifiRttLog.RttIndividualStatusHistogramBucket.emptyArray();
        histogramDistance = WifiMetricsProto.WifiRttLog.HistogramBucket.emptyArray();
        histogramRequestIntervalMs = WifiMetricsProto.WifiRttLog.HistogramBucket.emptyArray();
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (numRequests != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(1, numRequests);
        }
        i = j;
        if (numIndividualRequests != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, numIndividualRequests);
        }
        j = i;
        if (numApps != 0) {
          j = i + CodedOutputByteBufferNano.computeInt32Size(3, numApps);
        }
        Object localObject = histogramNumRequestsPerApp;
        int k = 0;
        i = j;
        if (localObject != null)
        {
          i = j;
          if (histogramNumRequestsPerApp.length > 0)
          {
            i = j;
            m = 0;
            while (m < histogramNumRequestsPerApp.length)
            {
              localObject = histogramNumRequestsPerApp[m];
              j = i;
              if (localObject != null) {
                j = i + CodedOutputByteBufferNano.computeMessageSize(4, (MessageNano)localObject);
              }
              m++;
              i = j;
            }
          }
        }
        j = i;
        if (histogramNumPeersPerRequest != null)
        {
          j = i;
          if (histogramNumPeersPerRequest.length > 0)
          {
            j = 0;
            while (j < histogramNumPeersPerRequest.length)
            {
              localObject = histogramNumPeersPerRequest[j];
              m = i;
              if (localObject != null) {
                m = i + CodedOutputByteBufferNano.computeMessageSize(5, (MessageNano)localObject);
              }
              j++;
              i = m;
            }
            j = i;
          }
        }
        int m = j;
        if (histogramIndividualStatus != null)
        {
          m = j;
          if (histogramIndividualStatus.length > 0)
          {
            i = j;
            j = 0;
            while (j < histogramIndividualStatus.length)
            {
              localObject = histogramIndividualStatus[j];
              m = i;
              if (localObject != null) {
                m = i + CodedOutputByteBufferNano.computeMessageSize(6, (MessageNano)localObject);
              }
              j++;
              i = m;
            }
            m = i;
          }
        }
        i = m;
        if (histogramDistance != null)
        {
          i = m;
          if (histogramDistance.length > 0)
          {
            i = m;
            j = 0;
            while (j < histogramDistance.length)
            {
              localObject = histogramDistance[j];
              m = i;
              if (localObject != null) {
                m = i + CodedOutputByteBufferNano.computeMessageSize(7, (MessageNano)localObject);
              }
              j++;
              i = m;
            }
          }
        }
        m = i;
        if (histogramRequestIntervalMs != null)
        {
          m = i;
          if (histogramRequestIntervalMs.length > 0)
          {
            j = k;
            for (;;)
            {
              m = i;
              if (j >= histogramRequestIntervalMs.length) {
                break;
              }
              localObject = histogramRequestIntervalMs[j];
              m = i;
              if (localObject != null) {
                m = i + CodedOutputByteBufferNano.computeMessageSize(8, (MessageNano)localObject);
              }
              j++;
              i = m;
            }
          }
        }
        return m;
      }
      
      public RttToPeerLog mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                int j;
                Object localObject;
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
                          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                            return this;
                          }
                        }
                        else
                        {
                          j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 66);
                          if (histogramRequestIntervalMs == null) {
                            i = 0;
                          } else {
                            i = histogramRequestIntervalMs.length;
                          }
                          localObject = new WifiMetricsProto.WifiRttLog.HistogramBucket[i + j];
                          j = i;
                          if (i != 0) {
                            System.arraycopy(histogramRequestIntervalMs, 0, localObject, 0, i);
                          }
                          for (j = i; j < localObject.length - 1; j++)
                          {
                            localObject[j] = new WifiMetricsProto.WifiRttLog.HistogramBucket();
                            paramCodedInputByteBufferNano.readMessage(localObject[j]);
                            paramCodedInputByteBufferNano.readTag();
                          }
                          localObject[j] = new WifiMetricsProto.WifiRttLog.HistogramBucket();
                          paramCodedInputByteBufferNano.readMessage(localObject[j]);
                          histogramRequestIntervalMs = ((WifiMetricsProto.WifiRttLog.HistogramBucket[])localObject);
                        }
                      }
                      else
                      {
                        j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 58);
                        if (histogramDistance == null) {
                          i = 0;
                        } else {
                          i = histogramDistance.length;
                        }
                        localObject = new WifiMetricsProto.WifiRttLog.HistogramBucket[i + j];
                        j = i;
                        if (i != 0) {
                          System.arraycopy(histogramDistance, 0, localObject, 0, i);
                        }
                        for (j = i; j < localObject.length - 1; j++)
                        {
                          localObject[j] = new WifiMetricsProto.WifiRttLog.HistogramBucket();
                          paramCodedInputByteBufferNano.readMessage(localObject[j]);
                          paramCodedInputByteBufferNano.readTag();
                        }
                        localObject[j] = new WifiMetricsProto.WifiRttLog.HistogramBucket();
                        paramCodedInputByteBufferNano.readMessage(localObject[j]);
                        histogramDistance = ((WifiMetricsProto.WifiRttLog.HistogramBucket[])localObject);
                      }
                    }
                    else
                    {
                      j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 50);
                      if (histogramIndividualStatus == null) {
                        i = 0;
                      } else {
                        i = histogramIndividualStatus.length;
                      }
                      localObject = new WifiMetricsProto.WifiRttLog.RttIndividualStatusHistogramBucket[i + j];
                      j = i;
                      if (i != 0) {
                        System.arraycopy(histogramIndividualStatus, 0, localObject, 0, i);
                      }
                      for (j = i; j < localObject.length - 1; j++)
                      {
                        localObject[j] = new WifiMetricsProto.WifiRttLog.RttIndividualStatusHistogramBucket();
                        paramCodedInputByteBufferNano.readMessage(localObject[j]);
                        paramCodedInputByteBufferNano.readTag();
                      }
                      localObject[j] = new WifiMetricsProto.WifiRttLog.RttIndividualStatusHistogramBucket();
                      paramCodedInputByteBufferNano.readMessage(localObject[j]);
                      histogramIndividualStatus = ((WifiMetricsProto.WifiRttLog.RttIndividualStatusHistogramBucket[])localObject);
                    }
                  }
                  else
                  {
                    j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 42);
                    if (histogramNumPeersPerRequest == null) {
                      i = 0;
                    } else {
                      i = histogramNumPeersPerRequest.length;
                    }
                    localObject = new WifiMetricsProto.WifiRttLog.HistogramBucket[i + j];
                    j = i;
                    if (i != 0) {
                      System.arraycopy(histogramNumPeersPerRequest, 0, localObject, 0, i);
                    }
                    for (j = i; j < localObject.length - 1; j++)
                    {
                      localObject[j] = new WifiMetricsProto.WifiRttLog.HistogramBucket();
                      paramCodedInputByteBufferNano.readMessage(localObject[j]);
                      paramCodedInputByteBufferNano.readTag();
                    }
                    localObject[j] = new WifiMetricsProto.WifiRttLog.HistogramBucket();
                    paramCodedInputByteBufferNano.readMessage(localObject[j]);
                    histogramNumPeersPerRequest = ((WifiMetricsProto.WifiRttLog.HistogramBucket[])localObject);
                  }
                }
                else
                {
                  j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 34);
                  if (histogramNumRequestsPerApp == null) {
                    i = 0;
                  } else {
                    i = histogramNumRequestsPerApp.length;
                  }
                  localObject = new WifiMetricsProto.WifiRttLog.HistogramBucket[i + j];
                  j = i;
                  if (i != 0) {
                    System.arraycopy(histogramNumRequestsPerApp, 0, localObject, 0, i);
                  }
                  for (j = i; j < localObject.length - 1; j++)
                  {
                    localObject[j] = new WifiMetricsProto.WifiRttLog.HistogramBucket();
                    paramCodedInputByteBufferNano.readMessage(localObject[j]);
                    paramCodedInputByteBufferNano.readTag();
                  }
                  localObject[j] = new WifiMetricsProto.WifiRttLog.HistogramBucket();
                  paramCodedInputByteBufferNano.readMessage(localObject[j]);
                  histogramNumRequestsPerApp = ((WifiMetricsProto.WifiRttLog.HistogramBucket[])localObject);
                }
              }
              else
              {
                numApps = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              numIndividualRequests = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            numRequests = paramCodedInputByteBufferNano.readInt32();
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (numRequests != 0) {
          paramCodedOutputByteBufferNano.writeInt32(1, numRequests);
        }
        if (numIndividualRequests != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, numIndividualRequests);
        }
        if (numApps != 0) {
          paramCodedOutputByteBufferNano.writeInt32(3, numApps);
        }
        Object localObject = histogramNumRequestsPerApp;
        int i = 0;
        int j;
        if ((localObject != null) && (histogramNumRequestsPerApp.length > 0)) {
          for (j = 0; j < histogramNumRequestsPerApp.length; j++)
          {
            localObject = histogramNumRequestsPerApp[j];
            if (localObject != null) {
              paramCodedOutputByteBufferNano.writeMessage(4, (MessageNano)localObject);
            }
          }
        }
        if ((histogramNumPeersPerRequest != null) && (histogramNumPeersPerRequest.length > 0)) {
          for (j = 0; j < histogramNumPeersPerRequest.length; j++)
          {
            localObject = histogramNumPeersPerRequest[j];
            if (localObject != null) {
              paramCodedOutputByteBufferNano.writeMessage(5, (MessageNano)localObject);
            }
          }
        }
        if ((histogramIndividualStatus != null) && (histogramIndividualStatus.length > 0)) {
          for (j = 0; j < histogramIndividualStatus.length; j++)
          {
            localObject = histogramIndividualStatus[j];
            if (localObject != null) {
              paramCodedOutputByteBufferNano.writeMessage(6, (MessageNano)localObject);
            }
          }
        }
        if ((histogramDistance != null) && (histogramDistance.length > 0)) {
          for (j = 0; j < histogramDistance.length; j++)
          {
            localObject = histogramDistance[j];
            if (localObject != null) {
              paramCodedOutputByteBufferNano.writeMessage(7, (MessageNano)localObject);
            }
          }
        }
        if ((histogramRequestIntervalMs != null) && (histogramRequestIntervalMs.length > 0)) {
          for (j = i; j < histogramRequestIntervalMs.length; j++)
          {
            localObject = histogramRequestIntervalMs[j];
            if (localObject != null) {
              paramCodedOutputByteBufferNano.writeMessage(8, (MessageNano)localObject);
            }
          }
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
    }
  }
  
  public static final class WifiScoreCount
    extends MessageNano
  {
    private static volatile WifiScoreCount[] _emptyArray;
    public int count;
    public int score;
    
    public WifiScoreCount()
    {
      clear();
    }
    
    public static WifiScoreCount[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new WifiScoreCount[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static WifiScoreCount parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new WifiScoreCount().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static WifiScoreCount parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (WifiScoreCount)MessageNano.mergeFrom(new WifiScoreCount(), paramArrayOfByte);
    }
    
    public WifiScoreCount clear()
    {
      score = 0;
      count = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (score != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, score);
      }
      i = j;
      if (count != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, count);
      }
      return i;
    }
    
    public WifiScoreCount mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            count = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          score = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (score != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, score);
      }
      if (count != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, count);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class WifiWakeStats
    extends MessageNano
  {
    private static volatile WifiWakeStats[] _emptyArray;
    public int numIgnoredStarts;
    public int numSessions;
    public int numWakeups;
    public Session[] sessions;
    
    public WifiWakeStats()
    {
      clear();
    }
    
    public static WifiWakeStats[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new WifiWakeStats[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static WifiWakeStats parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new WifiWakeStats().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static WifiWakeStats parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (WifiWakeStats)MessageNano.mergeFrom(new WifiWakeStats(), paramArrayOfByte);
    }
    
    public WifiWakeStats clear()
    {
      numSessions = 0;
      sessions = Session.emptyArray();
      numIgnoredStarts = 0;
      numWakeups = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (numSessions != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, numSessions);
      }
      i = j;
      if (sessions != null)
      {
        i = j;
        if (sessions.length > 0)
        {
          int k = 0;
          for (;;)
          {
            i = j;
            if (k >= sessions.length) {
              break;
            }
            Session localSession = sessions[k];
            i = j;
            if (localSession != null) {
              i = j + CodedOutputByteBufferNano.computeMessageSize(2, localSession);
            }
            k++;
            j = i;
          }
        }
      }
      j = i;
      if (numIgnoredStarts != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, numIgnoredStarts);
      }
      i = j;
      if (numWakeups != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, numWakeups);
      }
      return i;
    }
    
    public WifiWakeStats mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            if (i != 24)
            {
              if (i != 32)
              {
                if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                  return this;
                }
              }
              else {
                numWakeups = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              numIgnoredStarts = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else
          {
            int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 18);
            if (sessions == null) {
              i = 0;
            } else {
              i = sessions.length;
            }
            Session[] arrayOfSession = new Session[i + j];
            j = i;
            if (i != 0) {
              System.arraycopy(sessions, 0, arrayOfSession, 0, i);
            }
            for (j = i; j < arrayOfSession.length - 1; j++)
            {
              arrayOfSession[j] = new Session();
              paramCodedInputByteBufferNano.readMessage(arrayOfSession[j]);
              paramCodedInputByteBufferNano.readTag();
            }
            arrayOfSession[j] = new Session();
            paramCodedInputByteBufferNano.readMessage(arrayOfSession[j]);
            sessions = arrayOfSession;
          }
        }
        else {
          numSessions = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (numSessions != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, numSessions);
      }
      if ((sessions != null) && (sessions.length > 0)) {
        for (int i = 0; i < sessions.length; i++)
        {
          Session localSession = sessions[i];
          if (localSession != null) {
            paramCodedOutputByteBufferNano.writeMessage(2, localSession);
          }
        }
      }
      if (numIgnoredStarts != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, numIgnoredStarts);
      }
      if (numWakeups != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, numWakeups);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
    
    public static final class Session
      extends MessageNano
    {
      private static volatile Session[] _emptyArray;
      public Event initializeEvent;
      public int lockedNetworksAtInitialize;
      public int lockedNetworksAtStart;
      public Event resetEvent;
      public long startTimeMillis;
      public Event unlockEvent;
      public Event wakeupEvent;
      
      public Session()
      {
        clear();
      }
      
      public static Session[] emptyArray()
      {
        if (_emptyArray == null) {
          synchronized (InternalNano.LAZY_INIT_LOCK)
          {
            if (_emptyArray == null) {
              _emptyArray = new Session[0];
            }
          }
        }
        return _emptyArray;
      }
      
      public static Session parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
        throws IOException
      {
        return new Session().mergeFrom(paramCodedInputByteBufferNano);
      }
      
      public static Session parseFrom(byte[] paramArrayOfByte)
        throws InvalidProtocolBufferNanoException
      {
        return (Session)MessageNano.mergeFrom(new Session(), paramArrayOfByte);
      }
      
      public Session clear()
      {
        startTimeMillis = 0L;
        lockedNetworksAtStart = 0;
        lockedNetworksAtInitialize = 0;
        initializeEvent = null;
        unlockEvent = null;
        wakeupEvent = null;
        resetEvent = null;
        cachedSize = -1;
        return this;
      }
      
      protected int computeSerializedSize()
      {
        int i = super.computeSerializedSize();
        int j = i;
        if (startTimeMillis != 0L) {
          j = i + CodedOutputByteBufferNano.computeInt64Size(1, startTimeMillis);
        }
        i = j;
        if (lockedNetworksAtStart != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(2, lockedNetworksAtStart);
        }
        j = i;
        if (unlockEvent != null) {
          j = i + CodedOutputByteBufferNano.computeMessageSize(3, unlockEvent);
        }
        i = j;
        if (wakeupEvent != null) {
          i = j + CodedOutputByteBufferNano.computeMessageSize(4, wakeupEvent);
        }
        j = i;
        if (resetEvent != null) {
          j = i + CodedOutputByteBufferNano.computeMessageSize(5, resetEvent);
        }
        i = j;
        if (lockedNetworksAtInitialize != 0) {
          i = j + CodedOutputByteBufferNano.computeInt32Size(6, lockedNetworksAtInitialize);
        }
        j = i;
        if (initializeEvent != null) {
          j = i + CodedOutputByteBufferNano.computeMessageSize(7, initializeEvent);
        }
        return j;
      }
      
      public Session mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                if (i != 34)
                {
                  if (i != 42)
                  {
                    if (i != 48)
                    {
                      if (i != 58)
                      {
                        if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                          return this;
                        }
                      }
                      else
                      {
                        if (initializeEvent == null) {
                          initializeEvent = new Event();
                        }
                        paramCodedInputByteBufferNano.readMessage(initializeEvent);
                      }
                    }
                    else {
                      lockedNetworksAtInitialize = paramCodedInputByteBufferNano.readInt32();
                    }
                  }
                  else
                  {
                    if (resetEvent == null) {
                      resetEvent = new Event();
                    }
                    paramCodedInputByteBufferNano.readMessage(resetEvent);
                  }
                }
                else
                {
                  if (wakeupEvent == null) {
                    wakeupEvent = new Event();
                  }
                  paramCodedInputByteBufferNano.readMessage(wakeupEvent);
                }
              }
              else
              {
                if (unlockEvent == null) {
                  unlockEvent = new Event();
                }
                paramCodedInputByteBufferNano.readMessage(unlockEvent);
              }
            }
            else {
              lockedNetworksAtStart = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            startTimeMillis = paramCodedInputByteBufferNano.readInt64();
          }
        }
        return this;
      }
      
      public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
        throws IOException
      {
        if (startTimeMillis != 0L) {
          paramCodedOutputByteBufferNano.writeInt64(1, startTimeMillis);
        }
        if (lockedNetworksAtStart != 0) {
          paramCodedOutputByteBufferNano.writeInt32(2, lockedNetworksAtStart);
        }
        if (unlockEvent != null) {
          paramCodedOutputByteBufferNano.writeMessage(3, unlockEvent);
        }
        if (wakeupEvent != null) {
          paramCodedOutputByteBufferNano.writeMessage(4, wakeupEvent);
        }
        if (resetEvent != null) {
          paramCodedOutputByteBufferNano.writeMessage(5, resetEvent);
        }
        if (lockedNetworksAtInitialize != 0) {
          paramCodedOutputByteBufferNano.writeInt32(6, lockedNetworksAtInitialize);
        }
        if (initializeEvent != null) {
          paramCodedOutputByteBufferNano.writeMessage(7, initializeEvent);
        }
        super.writeTo(paramCodedOutputByteBufferNano);
      }
      
      public static final class Event
        extends MessageNano
      {
        private static volatile Event[] _emptyArray;
        public int elapsedScans;
        public long elapsedTimeMillis;
        
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
          elapsedTimeMillis = 0L;
          elapsedScans = 0;
          cachedSize = -1;
          return this;
        }
        
        protected int computeSerializedSize()
        {
          int i = super.computeSerializedSize();
          int j = i;
          if (elapsedTimeMillis != 0L) {
            j = i + CodedOutputByteBufferNano.computeInt64Size(1, elapsedTimeMillis);
          }
          i = j;
          if (elapsedScans != 0) {
            i = j + CodedOutputByteBufferNano.computeInt32Size(2, elapsedScans);
          }
          return i;
        }
        
        public Event mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                elapsedScans = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              elapsedTimeMillis = paramCodedInputByteBufferNano.readInt64();
            }
          }
          return this;
        }
        
        public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
          throws IOException
        {
          if (elapsedTimeMillis != 0L) {
            paramCodedOutputByteBufferNano.writeInt64(1, elapsedTimeMillis);
          }
          if (elapsedScans != 0) {
            paramCodedOutputByteBufferNano.writeInt32(2, elapsedScans);
          }
          super.writeTo(paramCodedOutputByteBufferNano);
        }
      }
    }
  }
  
  public static final class WpsMetrics
    extends MessageNano
  {
    private static volatile WpsMetrics[] _emptyArray;
    public int numWpsAttempts;
    public int numWpsCancellation;
    public int numWpsOtherConnectionFailure;
    public int numWpsOverlapFailure;
    public int numWpsStartFailure;
    public int numWpsSuccess;
    public int numWpsSupplicantFailure;
    public int numWpsTimeoutFailure;
    
    public WpsMetrics()
    {
      clear();
    }
    
    public static WpsMetrics[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new WpsMetrics[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static WpsMetrics parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new WpsMetrics().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static WpsMetrics parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (WpsMetrics)MessageNano.mergeFrom(new WpsMetrics(), paramArrayOfByte);
    }
    
    public WpsMetrics clear()
    {
      numWpsAttempts = 0;
      numWpsSuccess = 0;
      numWpsStartFailure = 0;
      numWpsOverlapFailure = 0;
      numWpsTimeoutFailure = 0;
      numWpsOtherConnectionFailure = 0;
      numWpsSupplicantFailure = 0;
      numWpsCancellation = 0;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (numWpsAttempts != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, numWpsAttempts);
      }
      i = j;
      if (numWpsSuccess != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, numWpsSuccess);
      }
      int k = i;
      if (numWpsStartFailure != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(3, numWpsStartFailure);
      }
      j = k;
      if (numWpsOverlapFailure != 0) {
        j = k + CodedOutputByteBufferNano.computeInt32Size(4, numWpsOverlapFailure);
      }
      i = j;
      if (numWpsTimeoutFailure != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(5, numWpsTimeoutFailure);
      }
      j = i;
      if (numWpsOtherConnectionFailure != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(6, numWpsOtherConnectionFailure);
      }
      i = j;
      if (numWpsSupplicantFailure != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(7, numWpsSupplicantFailure);
      }
      j = i;
      if (numWpsCancellation != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(8, numWpsCancellation);
      }
      return j;
    }
    
    public WpsMetrics mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
                      if (i != 64)
                      {
                        if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                          return this;
                        }
                      }
                      else {
                        numWpsCancellation = paramCodedInputByteBufferNano.readInt32();
                      }
                    }
                    else {
                      numWpsSupplicantFailure = paramCodedInputByteBufferNano.readInt32();
                    }
                  }
                  else {
                    numWpsOtherConnectionFailure = paramCodedInputByteBufferNano.readInt32();
                  }
                }
                else {
                  numWpsTimeoutFailure = paramCodedInputByteBufferNano.readInt32();
                }
              }
              else {
                numWpsOverlapFailure = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              numWpsStartFailure = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            numWpsSuccess = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          numWpsAttempts = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (numWpsAttempts != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, numWpsAttempts);
      }
      if (numWpsSuccess != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, numWpsSuccess);
      }
      if (numWpsStartFailure != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, numWpsStartFailure);
      }
      if (numWpsOverlapFailure != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, numWpsOverlapFailure);
      }
      if (numWpsTimeoutFailure != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, numWpsTimeoutFailure);
      }
      if (numWpsOtherConnectionFailure != 0) {
        paramCodedOutputByteBufferNano.writeInt32(6, numWpsOtherConnectionFailure);
      }
      if (numWpsSupplicantFailure != 0) {
        paramCodedOutputByteBufferNano.writeInt32(7, numWpsSupplicantFailure);
      }
      if (numWpsCancellation != 0) {
        paramCodedOutputByteBufferNano.writeInt32(8, numWpsCancellation);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
}
