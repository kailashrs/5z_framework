package android.media;

import com.android.internal.util.Preconditions;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MediaFile
{
  public static final int FILE_TYPE_3GPA = 211;
  public static final int FILE_TYPE_3GPP = 23;
  public static final int FILE_TYPE_3GPP2 = 24;
  public static final int FILE_TYPE_AAC = 8;
  public static final int FILE_TYPE_ABU = 607;
  public static final int FILE_TYPE_AC3 = 212;
  public static final int FILE_TYPE_ACSM = 601;
  public static final int FILE_TYPE_AIFF = 216;
  public static final int FILE_TYPE_AMR = 4;
  public static final int FILE_TYPE_APE = 217;
  public static final int FILE_TYPE_ARW = 304;
  public static final int FILE_TYPE_ASF = 26;
  public static final int FILE_TYPE_AVI = 29;
  public static final int FILE_TYPE_AWB = 5;
  public static final int FILE_TYPE_BMP = 34;
  public static final int FILE_TYPE_CALLLOG_BACKUP = 109;
  public static final int FILE_TYPE_CEPUB = 602;
  public static final int FILE_TYPE_CR2 = 301;
  public static final int FILE_TYPE_DIVX = 202;
  public static final int FILE_TYPE_DNG = 300;
  public static final int FILE_TYPE_DSD = 218;
  public static final int FILE_TYPE_DTS = 210;
  public static final int FILE_TYPE_EC3 = 215;
  public static final int FILE_TYPE_EML = 606;
  public static final int FILE_TYPE_EPUB = 600;
  public static final int FILE_TYPE_FL = 51;
  public static final int FILE_TYPE_FLAC = 10;
  public static final int FILE_TYPE_FLV = 203;
  public static final int FILE_TYPE_GIF = 32;
  public static final int FILE_TYPE_HEIF = 37;
  public static final int FILE_TYPE_HTML = 101;
  public static final int FILE_TYPE_HTTPLIVE = 44;
  public static final int FILE_TYPE_IMY = 13;
  public static final int FILE_TYPE_JPEG = 31;
  public static final int FILE_TYPE_M3U = 41;
  public static final int FILE_TYPE_M4A = 2;
  public static final int FILE_TYPE_M4V = 22;
  public static final int FILE_TYPE_MHAS = 219;
  public static final int FILE_TYPE_MID = 11;
  public static final int FILE_TYPE_MKA = 9;
  public static final int FILE_TYPE_MKV = 27;
  public static final int FILE_TYPE_MP2PS = 200;
  public static final int FILE_TYPE_MP2TS = 28;
  public static final int FILE_TYPE_MP3 = 1;
  public static final int FILE_TYPE_MP4 = 21;
  public static final int FILE_TYPE_MS_EXCEL = 105;
  public static final int FILE_TYPE_MS_POWERPOINT = 106;
  public static final int FILE_TYPE_MS_WORD = 104;
  public static final int FILE_TYPE_NEF = 302;
  public static final int FILE_TYPE_NRW = 303;
  public static final int FILE_TYPE_OGG = 7;
  public static final int FILE_TYPE_ORF = 306;
  public static final int FILE_TYPE_PCM = 214;
  public static final int FILE_TYPE_PDB = 603;
  public static final int FILE_TYPE_PDF = 102;
  public static final int FILE_TYPE_PEF = 308;
  public static final int FILE_TYPE_PLS = 42;
  public static final int FILE_TYPE_PNG = 33;
  public static final int FILE_TYPE_PUB = 605;
  public static final int FILE_TYPE_QCP = 213;
  public static final int FILE_TYPE_QT = 201;
  public static final int FILE_TYPE_RAF = 307;
  public static final int FILE_TYPE_RW2 = 305;
  public static final int FILE_TYPE_SMF = 12;
  public static final int FILE_TYPE_SRW = 309;
  public static final int FILE_TYPE_TEXT = 100;
  public static final int FILE_TYPE_UPDB = 604;
  public static final int FILE_TYPE_WAV = 3;
  public static final int FILE_TYPE_WBMP = 35;
  public static final int FILE_TYPE_WEBM = 30;
  public static final int FILE_TYPE_WEBP = 36;
  public static final int FILE_TYPE_WMA = 6;
  public static final int FILE_TYPE_WMV = 25;
  public static final int FILE_TYPE_WPL = 43;
  public static final int FILE_TYPE_XML = 103;
  public static final int FILE_TYPE_ZIP = 107;
  private static final int FIRST_AUDIO_FILE_TYPE = 1;
  private static final int FIRST_AUDIO_FILE_TYPE_EXT = 210;
  private static final int FIRST_DRM_FILE_TYPE = 51;
  private static final int FIRST_IMAGE_FILE_TYPE = 31;
  private static final int FIRST_MIDI_FILE_TYPE = 11;
  private static final int FIRST_PLAYLIST_FILE_TYPE = 41;
  private static final int FIRST_RAW_IMAGE_FILE_TYPE = 300;
  private static final int FIRST_VIDEO_FILE_TYPE = 21;
  private static final int FIRST_VIDEO_FILE_TYPE2 = 200;
  private static final int LAST_AUDIO_FILE_TYPE = 10;
  private static final int LAST_AUDIO_FILE_TYPE_EXT = 219;
  private static final int LAST_DRM_FILE_TYPE = 51;
  private static final int LAST_IMAGE_FILE_TYPE = 37;
  private static final int LAST_MIDI_FILE_TYPE = 13;
  private static final int LAST_PLAYLIST_FILE_TYPE = 44;
  private static final int LAST_RAW_IMAGE_FILE_TYPE = 309;
  private static final int LAST_VIDEO_FILE_TYPE = 30;
  private static final int LAST_VIDEO_FILE_TYPE2 = 203;
  private static final HashMap<String, MediaFileType> sFileTypeMap = new HashMap();
  private static final HashMap<String, Integer> sFileTypeToFormatMap;
  private static final HashMap<Integer, String> sFormatToMimeTypeMap;
  private static final HashMap<String, Integer> sMimeTypeMap = new HashMap();
  private static final HashMap<String, Integer> sMimeTypeToFormatMap;
  
  static
  {
    sFileTypeToFormatMap = new HashMap();
    sMimeTypeToFormatMap = new HashMap();
    sFormatToMimeTypeMap = new HashMap();
    addFileType("MP3", 1, "audio/mpeg", 12297, true);
    addFileType("MPGA", 1, "audio/mpeg", 12297, false);
    addFileType("M4A", 2, "audio/mp4", 12299, false);
    addFileType("WAV", 3, "audio/x-wav", 12296, true);
    addFileType("AMR", 4, "audio/amr");
    addFileType("AWB", 5, "audio/amr-wb");
    addFileType("OGG", 7, "audio/ogg", 47362, false);
    addFileType("OGG", 7, "application/ogg", 47362, true);
    addFileType("OGA", 7, "application/ogg", 47362, false);
    addFileType("AAC", 8, "audio/aac", 47363, true);
    addFileType("AAC", 8, "audio/aac-adts", 47363, false);
    addFileType("MKA", 9, "audio/x-matroska");
    addFileType("MID", 11, "audio/midi");
    addFileType("MIDI", 11, "audio/midi");
    addFileType("XMF", 11, "audio/midi");
    addFileType("RTTTL", 11, "audio/midi");
    addFileType("SMF", 12, "audio/sp-midi");
    addFileType("IMY", 13, "audio/imelody");
    addFileType("RTX", 11, "audio/midi");
    addFileType("OTA", 11, "audio/midi");
    addFileType("MP4", 219, "audio/mhas", 12299, false);
    addFileType("MPEG", 21, "video/mpeg", 12299, true);
    addFileType("MPG", 21, "video/mpeg", 12299, false);
    addFileType("MP4", 21, "video/mp4", 12299, false);
    addFileType("M4V", 22, "video/mp4", 12299, false);
    addFileType("MOV", 201, "video/quicktime", 12299, false);
    addFileType("3GP", 23, "video/3gpp", 47492, true);
    addFileType("3GPP", 23, "video/3gpp", 47492, false);
    addFileType("3G2", 24, "video/3gpp2", 47492, false);
    addFileType("3GPP2", 24, "video/3gpp2", 47492, false);
    addFileType("MKV", 27, "video/x-matroska");
    addFileType("WEBM", 30, "video/webm");
    addFileType("TS", 28, "video/mp2ts");
    addFileType("AVI", 29, "video/avi");
    addFileType("JPG", 31, "image/jpeg", 14337, true);
    addFileType("JPEG", 31, "image/jpeg", 14337, false);
    addFileType("GIF", 32, "image/gif", 14343, true);
    addFileType("PNG", 33, "image/png", 14347, true);
    addFileType("BMP", 34, "image/x-ms-bmp", 14340, true);
    addFileType("WBMP", 35, "image/vnd.wap.wbmp", 14336, false);
    addFileType("WEBP", 36, "image/webp", 14336, false);
    addFileType("HEIC", 37, "image/heif", 14354, true);
    addFileType("HEIF", 37, "image/heif", 14354, false);
    addFileType("DNG", 300, "image/x-adobe-dng", 14353, true);
    addFileType("CR2", 301, "image/x-canon-cr2", 14349, false);
    addFileType("NEF", 302, "image/x-nikon-nef", 14338, false);
    addFileType("NRW", 303, "image/x-nikon-nrw", 14349, false);
    addFileType("ARW", 304, "image/x-sony-arw", 14349, false);
    addFileType("RW2", 305, "image/x-panasonic-rw2", 14349, false);
    addFileType("ORF", 306, "image/x-olympus-orf", 14349, false);
    addFileType("RAF", 307, "image/x-fuji-raf", 14336, false);
    addFileType("PEF", 308, "image/x-pentax-pef", 14349, false);
    addFileType("SRW", 309, "image/x-samsung-srw", 14349, false);
    addFileType("M3U", 41, "audio/x-mpegurl", 47633, true);
    addFileType("M3U", 41, "application/x-mpegurl", 47633, false);
    addFileType("PLS", 42, "audio/x-scpls", 47636, true);
    addFileType("WPL", 43, "application/vnd.ms-wpl", 47632, true);
    addFileType("M3U8", 44, "application/vnd.apple.mpegurl");
    addFileType("M3U8", 44, "audio/mpegurl");
    addFileType("M3U8", 44, "audio/x-mpegurl");
    addFileType("FL", 51, "application/x-android-drm-fl");
    addFileType("TXT", 100, "text/plain", 12292, true);
    addFileType("HTM", 101, "text/html", 12293, true);
    addFileType("HTML", 101, "text/html", 12293, false);
    addFileType("PDF", 102, "application/pdf");
    addFileType("DOC", 104, "application/msword", 47747, true);
    addFileType("XLS", 105, "application/vnd.ms-excel", 47749, true);
    addFileType("PPT", 106, "application/vnd.ms-powerpoint", 47750, true);
    addFileType("FLAC", 10, "audio/flac", 47366, true);
    addFileType("ZIP", 107, "application/zip");
    addFileType("MPG", 200, "video/mp2p");
    addFileType("MPEG", 200, "video/mp2p");
    addFileType("DIVX", 202, "video/divx");
    addFileType("FLV", 203, "video/flv");
    addFileType("QCP", 213, "audio/qcelp");
    addFileType("AC3", 212, "audio/ac3");
    addFileType("EC3", 215, "audio/eac3");
    addFileType("AIF", 216, "audio/x-aiff");
    addFileType("AIFF", 216, "audio/x-aiff");
    addFileType("APE", 217, "audio/x-ape");
    addFileType("DSF", 218, "audio/x-dsf");
    addFileType("DFF", 218, "audio/x-dff");
    addFileType("DSD", 218, "audio/dsd");
    addFileType("CLBU", 109, "application/calllog-backup");
    addFileType("MHAS", 219, "audio/mhas");
    addFileType("DOCX", 104, "application/msword");
    addFileType("XLT", 105, "application/vnd.ms-excel");
    addFileType("XLSX", 105, "application/vnd.ms-excel");
    addFileType("POT", 106, "application/vnd.ms-powerpoint");
    addFileType("PPS", 106, "application/vnd.ms-powerpoint");
    addFileType("PPTX", 106, "application/vnd.ms-powerpoint");
    addFileType("EPUB", 600, "application/epub+zip");
    addFileType("ACSM", 601, "application/vnd.adobe.adept+xml");
    addFileType("CEPUB", 602, "application/octet-stream");
    addFileType("PDB", 603, "application/vnd.palm");
    addFileType("UPDB", 604, "application/vnd.palm");
    addFileType("PUB", 605, "application/x-mspublisher");
    addFileType("EML", 606, "message/rfc822");
    addFileType("ABU", 607, "application/vnd.asus-appbackup");
  }
  
  public MediaFile() {}
  
  static void addFileType(String paramString1, int paramInt, String paramString2)
  {
    sFileTypeMap.put(paramString1, new MediaFileType(paramInt, paramString2));
    sMimeTypeMap.put(paramString2, Integer.valueOf(paramInt));
  }
  
  private static void addFileType(String paramString1, int paramInt1, String paramString2, int paramInt2, boolean paramBoolean)
  {
    addFileType(paramString1, paramInt1, paramString2);
    sFileTypeToFormatMap.put(paramString1, Integer.valueOf(paramInt2));
    sMimeTypeToFormatMap.put(paramString2, Integer.valueOf(paramInt2));
    if (paramBoolean)
    {
      Preconditions.checkArgument(sFormatToMimeTypeMap.containsKey(Integer.valueOf(paramInt2)) ^ true);
      sFormatToMimeTypeMap.put(Integer.valueOf(paramInt2), paramString2);
    }
  }
  
  public static String getFileTitle(String paramString)
  {
    int i = paramString.lastIndexOf('/');
    String str = paramString;
    if (i >= 0)
    {
      i++;
      str = paramString;
      if (i < paramString.length()) {
        str = paramString.substring(i);
      }
    }
    i = str.lastIndexOf('.');
    paramString = str;
    if (i > 0) {
      paramString = str.substring(0, i);
    }
    return paramString;
  }
  
  public static MediaFileType getFileType(String paramString)
  {
    int i = paramString.lastIndexOf('.');
    if (i < 0) {
      return null;
    }
    return (MediaFileType)sFileTypeMap.get(paramString.substring(i + 1).toUpperCase(Locale.ROOT));
  }
  
  public static int getFileTypeForMimeType(String paramString)
  {
    paramString = (Integer)sMimeTypeMap.get(paramString);
    int i;
    if (paramString == null) {
      i = 0;
    } else {
      i = paramString.intValue();
    }
    return i;
  }
  
  public static int getFormatCode(String paramString1, String paramString2)
  {
    if (paramString2 != null)
    {
      paramString2 = (Integer)sMimeTypeToFormatMap.get(paramString2);
      if (paramString2 != null) {
        return paramString2.intValue();
      }
    }
    int i = paramString1.lastIndexOf('.');
    if (i > 0)
    {
      paramString1 = paramString1.substring(i + 1).toUpperCase(Locale.ROOT);
      paramString1 = (Integer)sFileTypeToFormatMap.get(paramString1);
      if (paramString1 != null) {
        return paramString1.intValue();
      }
    }
    return 12288;
  }
  
  public static String getMimeTypeForFile(String paramString)
  {
    paramString = getFileType(paramString);
    if (paramString == null) {
      paramString = null;
    } else {
      paramString = mimeType;
    }
    return paramString;
  }
  
  public static String getMimeTypeForFormatCode(int paramInt)
  {
    return (String)sFormatToMimeTypeMap.get(Integer.valueOf(paramInt));
  }
  
  public static boolean isAudioFileType(int paramInt)
  {
    boolean bool = true;
    if (((paramInt >= 1) && (paramInt <= 10)) || ((paramInt >= 11) && (paramInt <= 13)) || ((paramInt < 210) || (paramInt > 219))) {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isDrmFileType(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 51) && (paramInt <= 51)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isImageFileType(int paramInt)
  {
    boolean bool;
    if (((paramInt >= 31) && (paramInt <= 37)) || ((paramInt >= 300) && (paramInt <= 309))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isMimeTypeMedia(String paramString)
  {
    int i = getFileTypeForMimeType(paramString);
    boolean bool;
    if ((!isAudioFileType(i)) && (!isVideoFileType(i)) && (!isImageFileType(i)) && (!isPlayListFileType(i))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isPlayListFileType(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 41) && (paramInt <= 44)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isRawImageFileType(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 300) && (paramInt <= 309)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isVideoFileType(int paramInt)
  {
    boolean bool;
    if (((paramInt >= 21) && (paramInt <= 30)) || ((paramInt >= 200) && (paramInt <= 203))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isWMAEnabled()
  {
    List localList = DecoderCapabilities.getAudioDecoders();
    int i = localList.size();
    for (int j = 0; j < i; j++) {
      if ((DecoderCapabilities.AudioDecoder)localList.get(j) == DecoderCapabilities.AudioDecoder.AUDIO_DECODER_WMA) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean isWMVEnabled()
  {
    List localList = DecoderCapabilities.getVideoDecoders();
    int i = localList.size();
    for (int j = 0; j < i; j++) {
      if ((DecoderCapabilities.VideoDecoder)localList.get(j) == DecoderCapabilities.VideoDecoder.VIDEO_DECODER_WMV) {
        return true;
      }
    }
    return false;
  }
  
  public static class MediaFileType
  {
    public final int fileType;
    public final String mimeType;
    
    MediaFileType(int paramInt, String paramString)
    {
      fileType = paramInt;
      mimeType = paramString;
    }
  }
}
