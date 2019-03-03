package android.media;

import java.util.ArrayList;
import java.util.List;

public class EncoderCapabilities
{
  private static final String TAG = "EncoderCapabilities";
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  private EncoderCapabilities() {}
  
  public static List<AudioEncoderCap> getAudioEncoders()
  {
    int i = native_get_num_audio_encoders();
    if (i == 0) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    for (int j = 0; j < i; j++) {
      localArrayList.add(native_get_audio_encoder_cap(j));
    }
    return localArrayList;
  }
  
  public static int[] getOutputFileFormats()
  {
    int i = native_get_num_file_formats();
    if (i == 0) {
      return null;
    }
    int[] arrayOfInt = new int[i];
    for (int j = 0; j < i; j++) {
      arrayOfInt[j] = native_get_file_format(j);
    }
    return arrayOfInt;
  }
  
  public static List<VideoEncoderCap> getVideoEncoders()
  {
    int i = native_get_num_video_encoders();
    if (i == 0) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    for (int j = 0; j < i; j++) {
      localArrayList.add(native_get_video_encoder_cap(j));
    }
    return localArrayList;
  }
  
  private static final native AudioEncoderCap native_get_audio_encoder_cap(int paramInt);
  
  private static final native int native_get_file_format(int paramInt);
  
  private static final native int native_get_num_audio_encoders();
  
  private static final native int native_get_num_file_formats();
  
  private static final native int native_get_num_video_encoders();
  
  private static final native VideoEncoderCap native_get_video_encoder_cap(int paramInt);
  
  private static final native void native_init();
  
  public static class AudioEncoderCap
  {
    public final int mCodec;
    public final int mMaxBitRate;
    public final int mMaxChannels;
    public final int mMaxSampleRate;
    public final int mMinBitRate;
    public final int mMinChannels;
    public final int mMinSampleRate;
    
    private AudioEncoderCap(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
    {
      mCodec = paramInt1;
      mMinBitRate = paramInt2;
      mMaxBitRate = paramInt3;
      mMinSampleRate = paramInt4;
      mMaxSampleRate = paramInt5;
      mMinChannels = paramInt6;
      mMaxChannels = paramInt7;
    }
  }
  
  public static class VideoEncoderCap
  {
    public final int mCodec;
    public final int mMaxBitRate;
    public final int mMaxFrameHeight;
    public final int mMaxFrameRate;
    public final int mMaxFrameWidth;
    public final int mMinBitRate;
    public final int mMinFrameHeight;
    public final int mMinFrameRate;
    public final int mMinFrameWidth;
    
    private VideoEncoderCap(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
    {
      mCodec = paramInt1;
      mMinBitRate = paramInt2;
      mMaxBitRate = paramInt3;
      mMinFrameRate = paramInt4;
      mMaxFrameRate = paramInt5;
      mMinFrameWidth = paramInt6;
      mMaxFrameWidth = paramInt7;
      mMinFrameHeight = paramInt8;
      mMaxFrameHeight = paramInt9;
    }
  }
}
