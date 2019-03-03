package com.android.internal.midi;

public final class MidiConstants
{
  public static final int[] CHANNEL_BYTE_LENGTHS = { 3, 3, 3, 3, 2, 2, 3 };
  public static final byte STATUS_ACTIVE_SENSING = -2;
  public static final byte STATUS_CHANNEL_MASK = 15;
  public static final byte STATUS_CHANNEL_PRESSURE = -48;
  public static final byte STATUS_COMMAND_MASK = -16;
  public static final byte STATUS_CONTINUE = -5;
  public static final byte STATUS_CONTROL_CHANGE = -80;
  public static final byte STATUS_END_SYSEX = -9;
  public static final byte STATUS_MIDI_TIME_CODE = -15;
  public static final byte STATUS_NOTE_OFF = -128;
  public static final byte STATUS_NOTE_ON = -112;
  public static final byte STATUS_PITCH_BEND = -32;
  public static final byte STATUS_POLYPHONIC_AFTERTOUCH = -96;
  public static final byte STATUS_PROGRAM_CHANGE = -64;
  public static final byte STATUS_RESET = -1;
  public static final byte STATUS_SONG_POSITION = -14;
  public static final byte STATUS_SONG_SELECT = -13;
  public static final byte STATUS_START = -6;
  public static final byte STATUS_STOP = -4;
  public static final byte STATUS_SYSTEM_EXCLUSIVE = -16;
  public static final byte STATUS_TIMING_CLOCK = -8;
  public static final byte STATUS_TUNE_REQUEST = -10;
  public static final int[] SYSTEM_BYTE_LENGTHS = { 1, 2, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
  
  public MidiConstants() {}
  
  public static boolean allowRunningStatus(byte paramByte)
  {
    boolean bool;
    if ((paramByte >= Byte.MIN_VALUE) && (paramByte < -16)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean cancelsRunningStatus(byte paramByte)
  {
    boolean bool;
    if ((paramByte >= -16) && (paramByte <= -9)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static int getBytesPerMessage(byte paramByte)
  {
    paramByte &= 0xFF;
    if (paramByte >= 240) {
      return SYSTEM_BYTE_LENGTHS[(paramByte & 0xF)];
    }
    if (paramByte >= 128) {
      return CHANNEL_BYTE_LENGTHS[((paramByte >> 4) - 8)];
    }
    return 0;
  }
  
  public static boolean isAllActiveSensing(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    boolean bool = false;
    int i = 0;
    int j = 0;
    while (j < paramInt2)
    {
      int k = i;
      if (paramArrayOfByte[(paramInt1 + j)] != -2) {
        k = i + 1;
      }
      j++;
      i = k;
    }
    if (i == 0) {
      bool = true;
    }
    return bool;
  }
}
