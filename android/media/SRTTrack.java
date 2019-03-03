package android.media;

import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

class SRTTrack
  extends WebVttTrack
{
  private static final int KEY_LOCAL_SETTING = 102;
  private static final int KEY_START_TIME = 7;
  private static final int KEY_STRUCT_TEXT = 16;
  private static final int MEDIA_TIMED_TEXT = 99;
  private static final String TAG = "SRTTrack";
  private final Handler mEventHandler;
  
  SRTTrack(WebVttRenderingWidget paramWebVttRenderingWidget, MediaFormat paramMediaFormat)
  {
    super(paramWebVttRenderingWidget, paramMediaFormat);
    mEventHandler = null;
  }
  
  SRTTrack(Handler paramHandler, MediaFormat paramMediaFormat)
  {
    super(null, paramMediaFormat);
    mEventHandler = paramHandler;
  }
  
  private static long parseMs(String paramString)
  {
    return Long.parseLong(paramString.split(":")[0].trim()) * 60L * 60L * 1000L + 60L * Long.parseLong(paramString.split(":")[1].trim()) * 1000L + 1000L * Long.parseLong(paramString.split(":")[2].split(",")[0].trim()) + Long.parseLong(paramString.split(":")[2].split(",")[1].trim());
  }
  
  protected void onData(SubtitleData paramSubtitleData)
  {
    try
    {
      TextTrackCue localTextTrackCue = new android/media/TextTrackCue;
      localTextTrackCue.<init>();
      mStartTimeMs = (paramSubtitleData.getStartTimeUs() / 1000L);
      mEndTimeMs = ((paramSubtitleData.getStartTimeUs() + paramSubtitleData.getDurationUs()) / 1000L);
      String str = new java/lang/String;
      str.<init>(paramSubtitleData.getData(), "UTF-8");
      String[] arrayOfString = str.split("\\r?\\n");
      mLines = new TextTrackCueSpan[arrayOfString.length][];
      int i = arrayOfString.length;
      int j = 0;
      int k = 0;
      while (k < i)
      {
        str = arrayOfString[k];
        paramSubtitleData = new android/media/TextTrackCueSpan;
        paramSubtitleData.<init>(str, -1L);
        mLines[j] = { paramSubtitleData };
        k++;
        j++;
      }
      addCue(localTextTrackCue);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      paramSubtitleData = new StringBuilder();
      paramSubtitleData.append("subtitle data is not UTF-8 encoded: ");
      paramSubtitleData.append(localUnsupportedEncodingException);
      Log.w("SRTTrack", paramSubtitleData.toString());
    }
  }
  
  public void onData(byte[] paramArrayOfByte, boolean paramBoolean, long paramLong)
  {
    Object localObject2;
    try
    {
      Object localObject1 = new java/io/InputStreamReader;
      localObject2 = new java/io/ByteArrayInputStream;
      try
      {
        ((ByteArrayInputStream)localObject2).<init>(paramArrayOfByte);
        ((InputStreamReader)localObject1).<init>((InputStream)localObject2, "UTF-8");
        localObject2 = new java/io/BufferedReader;
        ((BufferedReader)localObject2).<init>((Reader)localObject1);
        for (;;)
        {
          if (((BufferedReader)localObject2).readLine() != null)
          {
            paramArrayOfByte = ((BufferedReader)localObject2).readLine();
            if (paramArrayOfByte != null)
            {
              localObject1 = new android/media/TextTrackCue;
              ((TextTrackCue)localObject1).<init>();
              paramArrayOfByte = paramArrayOfByte.split("-->");
              mStartTimeMs = parseMs(paramArrayOfByte[0]);
              mEndTimeMs = parseMs(paramArrayOfByte[1]);
              paramArrayOfByte = new java/util/ArrayList;
              paramArrayOfByte.<init>();
              for (;;)
              {
                localObject3 = ((BufferedReader)localObject2).readLine();
                if ((localObject3 == null) || (((String)localObject3).trim().equals(""))) {
                  break;
                }
                paramArrayOfByte.add(localObject3);
              }
              int i = 0;
              mLines = new TextTrackCueSpan[paramArrayOfByte.size()][];
              mStrings = ((String[])paramArrayOfByte.toArray(new String[0]));
              Object localObject3 = paramArrayOfByte.iterator();
              while (((Iterator)localObject3).hasNext())
              {
                String str = (String)((Iterator)localObject3).next();
                TextTrackCueSpan localTextTrackCueSpan = new android/media/TextTrackCueSpan;
                localTextTrackCueSpan.<init>(str, -1L);
                mStrings[i] = str;
                mLines[i] = { localTextTrackCueSpan };
                i++;
              }
              try
              {
                addCue((SubtitleTrack.Cue)localObject1);
              }
              catch (IOException paramArrayOfByte) {}catch (UnsupportedEncodingException paramArrayOfByte) {}
            }
          }
        }
      }
      catch (IOException paramArrayOfByte) {}catch (UnsupportedEncodingException paramArrayOfByte) {}
      localObject2 = new StringBuilder();
    }
    catch (IOException paramArrayOfByte)
    {
      Log.e("SRTTrack", paramArrayOfByte.getMessage(), paramArrayOfByte);
    }
    catch (UnsupportedEncodingException paramArrayOfByte) {}
    ((StringBuilder)localObject2).append("subtitle data is not UTF-8 encoded: ");
    ((StringBuilder)localObject2).append(paramArrayOfByte);
    Log.w("SRTTrack", ((StringBuilder)localObject2).toString());
  }
  
  public void updateView(Vector<SubtitleTrack.Cue> paramVector)
  {
    if (getRenderingWidget() != null)
    {
      super.updateView(paramVector);
      return;
    }
    if (mEventHandler == null) {
      return;
    }
    Iterator localIterator = paramVector.iterator();
    while (localIterator.hasNext())
    {
      Object localObject1 = (SubtitleTrack.Cue)localIterator.next();
      Object localObject2 = (TextTrackCue)localObject1;
      Object localObject3 = Parcel.obtain();
      ((Parcel)localObject3).writeInt(102);
      ((Parcel)localObject3).writeInt(7);
      ((Parcel)localObject3).writeInt((int)mStartTimeMs);
      ((Parcel)localObject3).writeInt(16);
      localObject1 = new StringBuilder();
      localObject2 = mStrings;
      int i = localObject2.length;
      for (int j = 0; j < i; j++)
      {
        ((StringBuilder)localObject1).append(localObject2[j]);
        ((StringBuilder)localObject1).append('\n');
      }
      localObject1 = ((StringBuilder)localObject1).toString().getBytes();
      ((Parcel)localObject3).writeInt(localObject1.length);
      ((Parcel)localObject3).writeByteArray((byte[])localObject1);
      localObject3 = mEventHandler.obtainMessage(99, 0, 0, localObject3);
      mEventHandler.sendMessage((Message)localObject3);
    }
    paramVector.clear();
  }
}
