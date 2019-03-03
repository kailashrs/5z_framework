package android.media;

import java.util.ArrayList;
import java.util.List;

public class DecoderCapabilities
{
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  private DecoderCapabilities() {}
  
  public static List<AudioDecoder> getAudioDecoders()
  {
    ArrayList localArrayList = new ArrayList();
    int i = native_get_num_audio_decoders();
    for (int j = 0; j < i; j++) {
      localArrayList.add(AudioDecoder.values()[native_get_audio_decoder_type(j)]);
    }
    return localArrayList;
  }
  
  public static List<VideoDecoder> getVideoDecoders()
  {
    ArrayList localArrayList = new ArrayList();
    int i = native_get_num_video_decoders();
    for (int j = 0; j < i; j++) {
      localArrayList.add(VideoDecoder.values()[native_get_video_decoder_type(j)]);
    }
    return localArrayList;
  }
  
  private static final native int native_get_audio_decoder_type(int paramInt);
  
  private static final native int native_get_num_audio_decoders();
  
  private static final native int native_get_num_video_decoders();
  
  private static final native int native_get_video_decoder_type(int paramInt);
  
  private static final native void native_init();
  
  public static enum AudioDecoder
  {
    AUDIO_DECODER_WMA;
    
    private AudioDecoder() {}
  }
  
  public static enum VideoDecoder
  {
    VIDEO_DECODER_WMV;
    
    private VideoDecoder() {}
  }
}
