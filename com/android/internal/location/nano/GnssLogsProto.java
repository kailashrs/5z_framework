package com.android.internal.location.nano;

import com.android.framework.protobuf.nano.CodedInputByteBufferNano;
import com.android.framework.protobuf.nano.CodedOutputByteBufferNano;
import com.android.framework.protobuf.nano.InternalNano;
import com.android.framework.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.framework.protobuf.nano.MessageNano;
import com.android.framework.protobuf.nano.WireFormatNano;
import java.io.IOException;

public abstract interface GnssLogsProto
{
  public static final class GnssLog
    extends MessageNano
  {
    private static volatile GnssLog[] _emptyArray;
    public int meanPositionAccuracyMeters;
    public int meanTimeToFirstFixSecs;
    public double meanTopFourAverageCn0DbHz;
    public int numLocationReportProcessed;
    public int numPositionAccuracyProcessed;
    public int numTimeToFirstFixProcessed;
    public int numTopFourAverageCn0Processed;
    public int percentageLocationFailure;
    public GnssLogsProto.PowerMetrics powerMetrics;
    public int standardDeviationPositionAccuracyMeters;
    public int standardDeviationTimeToFirstFixSecs;
    public double standardDeviationTopFourAverageCn0DbHz;
    
    public GnssLog()
    {
      clear();
    }
    
    public static GnssLog[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new GnssLog[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static GnssLog parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new GnssLog().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static GnssLog parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (GnssLog)MessageNano.mergeFrom(new GnssLog(), paramArrayOfByte);
    }
    
    public GnssLog clear()
    {
      numLocationReportProcessed = 0;
      percentageLocationFailure = 0;
      numTimeToFirstFixProcessed = 0;
      meanTimeToFirstFixSecs = 0;
      standardDeviationTimeToFirstFixSecs = 0;
      numPositionAccuracyProcessed = 0;
      meanPositionAccuracyMeters = 0;
      standardDeviationPositionAccuracyMeters = 0;
      numTopFourAverageCn0Processed = 0;
      meanTopFourAverageCn0DbHz = 0.0D;
      standardDeviationTopFourAverageCn0DbHz = 0.0D;
      powerMetrics = null;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (numLocationReportProcessed != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, numLocationReportProcessed);
      }
      i = j;
      if (percentageLocationFailure != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, percentageLocationFailure);
      }
      int k = i;
      if (numTimeToFirstFixProcessed != 0) {
        k = i + CodedOutputByteBufferNano.computeInt32Size(3, numTimeToFirstFixProcessed);
      }
      j = k;
      if (meanTimeToFirstFixSecs != 0) {
        j = k + CodedOutputByteBufferNano.computeInt32Size(4, meanTimeToFirstFixSecs);
      }
      i = j;
      if (standardDeviationTimeToFirstFixSecs != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(5, standardDeviationTimeToFirstFixSecs);
      }
      j = i;
      if (numPositionAccuracyProcessed != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(6, numPositionAccuracyProcessed);
      }
      i = j;
      if (meanPositionAccuracyMeters != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(7, meanPositionAccuracyMeters);
      }
      j = i;
      if (standardDeviationPositionAccuracyMeters != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(8, standardDeviationPositionAccuracyMeters);
      }
      i = j;
      if (numTopFourAverageCn0Processed != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(9, numTopFourAverageCn0Processed);
      }
      j = i;
      if (Double.doubleToLongBits(meanTopFourAverageCn0DbHz) != Double.doubleToLongBits(0.0D)) {
        j = i + CodedOutputByteBufferNano.computeDoubleSize(10, meanTopFourAverageCn0DbHz);
      }
      i = j;
      if (Double.doubleToLongBits(standardDeviationTopFourAverageCn0DbHz) != Double.doubleToLongBits(0.0D)) {
        i = j + CodedOutputByteBufferNano.computeDoubleSize(11, standardDeviationTopFourAverageCn0DbHz);
      }
      j = i;
      if (powerMetrics != null) {
        j = i + CodedOutputByteBufferNano.computeMessageSize(12, powerMetrics);
      }
      return j;
    }
    
    public GnssLog mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
        case 98: 
          if (powerMetrics == null) {
            powerMetrics = new GnssLogsProto.PowerMetrics();
          }
          paramCodedInputByteBufferNano.readMessage(powerMetrics);
          break;
        case 89: 
          standardDeviationTopFourAverageCn0DbHz = paramCodedInputByteBufferNano.readDouble();
          break;
        case 81: 
          meanTopFourAverageCn0DbHz = paramCodedInputByteBufferNano.readDouble();
          break;
        case 72: 
          numTopFourAverageCn0Processed = paramCodedInputByteBufferNano.readInt32();
          break;
        case 64: 
          standardDeviationPositionAccuracyMeters = paramCodedInputByteBufferNano.readInt32();
          break;
        case 56: 
          meanPositionAccuracyMeters = paramCodedInputByteBufferNano.readInt32();
          break;
        case 48: 
          numPositionAccuracyProcessed = paramCodedInputByteBufferNano.readInt32();
          break;
        case 40: 
          standardDeviationTimeToFirstFixSecs = paramCodedInputByteBufferNano.readInt32();
          break;
        case 32: 
          meanTimeToFirstFixSecs = paramCodedInputByteBufferNano.readInt32();
          break;
        case 24: 
          numTimeToFirstFixProcessed = paramCodedInputByteBufferNano.readInt32();
          break;
        case 16: 
          percentageLocationFailure = paramCodedInputByteBufferNano.readInt32();
          break;
        case 8: 
          numLocationReportProcessed = paramCodedInputByteBufferNano.readInt32();
          break;
        case 0: 
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (numLocationReportProcessed != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, numLocationReportProcessed);
      }
      if (percentageLocationFailure != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, percentageLocationFailure);
      }
      if (numTimeToFirstFixProcessed != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, numTimeToFirstFixProcessed);
      }
      if (meanTimeToFirstFixSecs != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, meanTimeToFirstFixSecs);
      }
      if (standardDeviationTimeToFirstFixSecs != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, standardDeviationTimeToFirstFixSecs);
      }
      if (numPositionAccuracyProcessed != 0) {
        paramCodedOutputByteBufferNano.writeInt32(6, numPositionAccuracyProcessed);
      }
      if (meanPositionAccuracyMeters != 0) {
        paramCodedOutputByteBufferNano.writeInt32(7, meanPositionAccuracyMeters);
      }
      if (standardDeviationPositionAccuracyMeters != 0) {
        paramCodedOutputByteBufferNano.writeInt32(8, standardDeviationPositionAccuracyMeters);
      }
      if (numTopFourAverageCn0Processed != 0) {
        paramCodedOutputByteBufferNano.writeInt32(9, numTopFourAverageCn0Processed);
      }
      if (Double.doubleToLongBits(meanTopFourAverageCn0DbHz) != Double.doubleToLongBits(0.0D)) {
        paramCodedOutputByteBufferNano.writeDouble(10, meanTopFourAverageCn0DbHz);
      }
      if (Double.doubleToLongBits(standardDeviationTopFourAverageCn0DbHz) != Double.doubleToLongBits(0.0D)) {
        paramCodedOutputByteBufferNano.writeDouble(11, standardDeviationTopFourAverageCn0DbHz);
      }
      if (powerMetrics != null) {
        paramCodedOutputByteBufferNano.writeMessage(12, powerMetrics);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
  
  public static final class PowerMetrics
    extends MessageNano
  {
    private static volatile PowerMetrics[] _emptyArray;
    public double energyConsumedMah;
    public long loggingDurationMs;
    public long[] timeInSignalQualityLevelMs;
    
    public PowerMetrics()
    {
      clear();
    }
    
    public static PowerMetrics[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new PowerMetrics[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static PowerMetrics parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new PowerMetrics().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static PowerMetrics parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (PowerMetrics)MessageNano.mergeFrom(new PowerMetrics(), paramArrayOfByte);
    }
    
    public PowerMetrics clear()
    {
      loggingDurationMs = 0L;
      energyConsumedMah = 0.0D;
      timeInSignalQualityLevelMs = WireFormatNano.EMPTY_LONG_ARRAY;
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
      if (timeInSignalQualityLevelMs != null)
      {
        j = i;
        if (timeInSignalQualityLevelMs.length > 0)
        {
          int k = 0;
          for (j = 0; j < timeInSignalQualityLevelMs.length; j++)
          {
            long l = timeInSignalQualityLevelMs[j];
            k += CodedOutputByteBufferNano.computeInt64SizeNoTag(l);
          }
          j = i + k + 1 * timeInSignalQualityLevelMs.length;
        }
      }
      return j;
    }
    
    public PowerMetrics mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
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
            int k;
            long[] arrayOfLong;
            if (i != 24)
            {
              if (i != 26)
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
                  paramCodedInputByteBufferNano.readInt64();
                  k++;
                }
                paramCodedInputByteBufferNano.rewindToPosition(i);
                if (timeInSignalQualityLevelMs == null) {
                  i = 0;
                } else {
                  i = timeInSignalQualityLevelMs.length;
                }
                arrayOfLong = new long[i + k];
                k = i;
                if (i != 0) {
                  System.arraycopy(timeInSignalQualityLevelMs, 0, arrayOfLong, 0, i);
                }
                for (k = i; k < arrayOfLong.length; k++) {
                  arrayOfLong[k] = paramCodedInputByteBufferNano.readInt64();
                }
                timeInSignalQualityLevelMs = arrayOfLong;
                paramCodedInputByteBufferNano.popLimit(j);
              }
            }
            else
            {
              k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 24);
              if (timeInSignalQualityLevelMs == null) {
                i = 0;
              } else {
                i = timeInSignalQualityLevelMs.length;
              }
              arrayOfLong = new long[i + k];
              k = i;
              if (i != 0) {
                System.arraycopy(timeInSignalQualityLevelMs, 0, arrayOfLong, 0, i);
              }
              for (k = i; k < arrayOfLong.length - 1; k++)
              {
                arrayOfLong[k] = paramCodedInputByteBufferNano.readInt64();
                paramCodedInputByteBufferNano.readTag();
              }
              arrayOfLong[k] = paramCodedInputByteBufferNano.readInt64();
              timeInSignalQualityLevelMs = arrayOfLong;
            }
          }
          else
          {
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
      if ((timeInSignalQualityLevelMs != null) && (timeInSignalQualityLevelMs.length > 0)) {
        for (int i = 0; i < timeInSignalQualityLevelMs.length; i++) {
          paramCodedOutputByteBufferNano.writeInt64(3, timeInSignalQualityLevelMs[i]);
        }
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
}
