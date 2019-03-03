package android.media;

import android.content.res.AssetManager.AssetInputStream;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.Pair;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import libcore.io.IoUtils;
import libcore.io.Streams;

public class ExifInterface
{
  private static final Charset ASCII;
  private static final int[] BITS_PER_SAMPLE_GREYSCALE_1;
  private static final int[] BITS_PER_SAMPLE_GREYSCALE_2;
  private static final int[] BITS_PER_SAMPLE_RGB;
  private static final short BYTE_ALIGN_II = 18761;
  private static final short BYTE_ALIGN_MM = 19789;
  private static final int DATA_DEFLATE_ZIP = 8;
  private static final int DATA_HUFFMAN_COMPRESSED = 2;
  private static final int DATA_JPEG = 6;
  private static final int DATA_JPEG_COMPRESSED = 7;
  private static final int DATA_LOSSY_JPEG = 34892;
  private static final int DATA_PACK_BITS_COMPRESSED = 32773;
  private static final int DATA_UNCOMPRESSED = 1;
  private static final boolean DEBUG = false;
  private static final byte[] EXIF_ASCII_PREFIX;
  private static final ExifTag[] EXIF_POINTER_TAGS;
  private static final ExifTag[][] EXIF_TAGS;
  private static final byte[] HEIF_BRAND_HEIC;
  private static final byte[] HEIF_BRAND_MIF1;
  private static final byte[] HEIF_TYPE_FTYP;
  private static final byte[] IDENTIFIER_EXIF_APP1;
  private static final ExifTag[] IFD_EXIF_TAGS;
  private static final int IFD_FORMAT_BYTE = 1;
  private static final int[] IFD_FORMAT_BYTES_PER_FORMAT;
  private static final int IFD_FORMAT_DOUBLE = 12;
  private static final int IFD_FORMAT_IFD = 13;
  private static final String[] IFD_FORMAT_NAMES;
  private static final int IFD_FORMAT_SBYTE = 6;
  private static final int IFD_FORMAT_SINGLE = 11;
  private static final int IFD_FORMAT_SLONG = 9;
  private static final int IFD_FORMAT_SRATIONAL = 10;
  private static final int IFD_FORMAT_SSHORT = 8;
  private static final int IFD_FORMAT_STRING = 2;
  private static final int IFD_FORMAT_ULONG = 4;
  private static final int IFD_FORMAT_UNDEFINED = 7;
  private static final int IFD_FORMAT_URATIONAL = 5;
  private static final int IFD_FORMAT_USHORT = 3;
  private static final ExifTag[] IFD_GPS_TAGS;
  private static final ExifTag[] IFD_INTEROPERABILITY_TAGS;
  private static final int IFD_OFFSET = 8;
  private static final ExifTag[] IFD_THUMBNAIL_TAGS;
  private static final ExifTag[] IFD_TIFF_TAGS;
  private static final int IFD_TYPE_EXIF = 1;
  private static final int IFD_TYPE_GPS = 2;
  private static final int IFD_TYPE_INTEROPERABILITY = 3;
  private static final int IFD_TYPE_ORF_CAMERA_SETTINGS = 7;
  private static final int IFD_TYPE_ORF_IMAGE_PROCESSING = 8;
  private static final int IFD_TYPE_ORF_MAKER_NOTE = 6;
  private static final int IFD_TYPE_PEF = 9;
  private static final int IFD_TYPE_PREVIEW = 5;
  private static final int IFD_TYPE_PRIMARY = 0;
  private static final int IFD_TYPE_THUMBNAIL = 4;
  private static final int IMAGE_TYPE_ARW = 1;
  private static final int IMAGE_TYPE_CR2 = 2;
  private static final int IMAGE_TYPE_DNG = 3;
  private static final int IMAGE_TYPE_HEIF = 12;
  private static final int IMAGE_TYPE_JPEG = 4;
  private static final int IMAGE_TYPE_NEF = 5;
  private static final int IMAGE_TYPE_NRW = 6;
  private static final int IMAGE_TYPE_ORF = 7;
  private static final int IMAGE_TYPE_PEF = 8;
  private static final int IMAGE_TYPE_RAF = 9;
  private static final int IMAGE_TYPE_RW2 = 10;
  private static final int IMAGE_TYPE_SRW = 11;
  private static final int IMAGE_TYPE_UNKNOWN = 0;
  private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG;
  private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG;
  private static final byte[] JPEG_SIGNATURE = { -1, -40, -1 };
  private static final byte MARKER = -1;
  private static final byte MARKER_APP1 = -31;
  private static final byte MARKER_COM = -2;
  private static final byte MARKER_EOI = -39;
  private static final byte MARKER_SOF0 = -64;
  private static final byte MARKER_SOF1 = -63;
  private static final byte MARKER_SOF10 = -54;
  private static final byte MARKER_SOF11 = -53;
  private static final byte MARKER_SOF13 = -51;
  private static final byte MARKER_SOF14 = -50;
  private static final byte MARKER_SOF15 = -49;
  private static final byte MARKER_SOF2 = -62;
  private static final byte MARKER_SOF3 = -61;
  private static final byte MARKER_SOF5 = -59;
  private static final byte MARKER_SOF6 = -58;
  private static final byte MARKER_SOF7 = -57;
  private static final byte MARKER_SOF9 = -55;
  private static final byte MARKER_SOI = -40;
  private static final byte MARKER_SOS = -38;
  private static final int MAX_THUMBNAIL_SIZE = 512;
  private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS;
  private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS;
  private static final byte[] ORF_MAKER_NOTE_HEADER_1;
  private static final int ORF_MAKER_NOTE_HEADER_1_SIZE = 8;
  private static final byte[] ORF_MAKER_NOTE_HEADER_2;
  private static final int ORF_MAKER_NOTE_HEADER_2_SIZE = 12;
  private static final ExifTag[] ORF_MAKER_NOTE_TAGS;
  private static final short ORF_SIGNATURE_1 = 20306;
  private static final short ORF_SIGNATURE_2 = 21330;
  public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
  public static final int ORIENTATION_FLIP_VERTICAL = 4;
  public static final int ORIENTATION_NORMAL = 1;
  public static final int ORIENTATION_ROTATE_180 = 3;
  public static final int ORIENTATION_ROTATE_270 = 8;
  public static final int ORIENTATION_ROTATE_90 = 6;
  public static final int ORIENTATION_TRANSPOSE = 5;
  public static final int ORIENTATION_TRANSVERSE = 7;
  public static final int ORIENTATION_UNDEFINED = 0;
  private static final int ORIGINAL_RESOLUTION_IMAGE = 0;
  private static final int PEF_MAKER_NOTE_SKIP_SIZE = 6;
  private static final String PEF_SIGNATURE = "PENTAX";
  private static final ExifTag[] PEF_TAGS;
  private static final int PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO = 1;
  private static final int PHOTOMETRIC_INTERPRETATION_RGB = 2;
  private static final int PHOTOMETRIC_INTERPRETATION_WHITE_IS_ZERO = 0;
  private static final int PHOTOMETRIC_INTERPRETATION_YCBCR = 6;
  private static final int RAF_INFO_SIZE = 160;
  private static final int RAF_JPEG_LENGTH_VALUE_SIZE = 4;
  private static final int RAF_OFFSET_TO_JPEG_IMAGE_OFFSET = 84;
  private static final String RAF_SIGNATURE = "FUJIFILMCCD-RAW";
  private static final int REDUCED_RESOLUTION_IMAGE = 1;
  private static final short RW2_SIGNATURE = 85;
  private static final int SIGNATURE_CHECK_SIZE = 5000;
  private static final byte START_CODE = 42;
  private static final String TAG = "ExifInterface";
  @Deprecated
  public static final String TAG_APERTURE = "FNumber";
  public static final String TAG_APERTURE_VALUE = "ApertureValue";
  public static final String TAG_ARTIST = "Artist";
  public static final String TAG_BITS_PER_SAMPLE = "BitsPerSample";
  public static final String TAG_BRIGHTNESS_VALUE = "BrightnessValue";
  public static final String TAG_CFA_PATTERN = "CFAPattern";
  public static final String TAG_COLOR_SPACE = "ColorSpace";
  public static final String TAG_COMPONENTS_CONFIGURATION = "ComponentsConfiguration";
  public static final String TAG_COMPRESSED_BITS_PER_PIXEL = "CompressedBitsPerPixel";
  public static final String TAG_COMPRESSION = "Compression";
  public static final String TAG_CONTRAST = "Contrast";
  public static final String TAG_COPYRIGHT = "Copyright";
  public static final String TAG_CUSTOM_RENDERED = "CustomRendered";
  public static final String TAG_DATETIME = "DateTime";
  public static final String TAG_DATETIME_DIGITIZED = "DateTimeDigitized";
  public static final String TAG_DATETIME_ORIGINAL = "DateTimeOriginal";
  public static final String TAG_DEFAULT_CROP_SIZE = "DefaultCropSize";
  public static final String TAG_DEVICE_SETTING_DESCRIPTION = "DeviceSettingDescription";
  public static final String TAG_DIGITAL_ZOOM_RATIO = "DigitalZoomRatio";
  public static final String TAG_DNG_VERSION = "DNGVersion";
  private static final String TAG_EXIF_IFD_POINTER = "ExifIFDPointer";
  public static final String TAG_EXIF_VERSION = "ExifVersion";
  public static final String TAG_EXPOSURE_BIAS_VALUE = "ExposureBiasValue";
  public static final String TAG_EXPOSURE_INDEX = "ExposureIndex";
  public static final String TAG_EXPOSURE_MODE = "ExposureMode";
  public static final String TAG_EXPOSURE_PROGRAM = "ExposureProgram";
  public static final String TAG_EXPOSURE_TIME = "ExposureTime";
  public static final String TAG_FILE_SOURCE = "FileSource";
  public static final String TAG_FLASH = "Flash";
  public static final String TAG_FLASHPIX_VERSION = "FlashpixVersion";
  public static final String TAG_FLASH_ENERGY = "FlashEnergy";
  public static final String TAG_FOCAL_LENGTH = "FocalLength";
  public static final String TAG_FOCAL_LENGTH_IN_35MM_FILM = "FocalLengthIn35mmFilm";
  public static final String TAG_FOCAL_PLANE_RESOLUTION_UNIT = "FocalPlaneResolutionUnit";
  public static final String TAG_FOCAL_PLANE_X_RESOLUTION = "FocalPlaneXResolution";
  public static final String TAG_FOCAL_PLANE_Y_RESOLUTION = "FocalPlaneYResolution";
  public static final String TAG_F_NUMBER = "FNumber";
  public static final String TAG_GAIN_CONTROL = "GainControl";
  public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
  public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
  public static final String TAG_GPS_AREA_INFORMATION = "GPSAreaInformation";
  public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
  public static final String TAG_GPS_DEST_BEARING = "GPSDestBearing";
  public static final String TAG_GPS_DEST_BEARING_REF = "GPSDestBearingRef";
  public static final String TAG_GPS_DEST_DISTANCE = "GPSDestDistance";
  public static final String TAG_GPS_DEST_DISTANCE_REF = "GPSDestDistanceRef";
  public static final String TAG_GPS_DEST_LATITUDE = "GPSDestLatitude";
  public static final String TAG_GPS_DEST_LATITUDE_REF = "GPSDestLatitudeRef";
  public static final String TAG_GPS_DEST_LONGITUDE = "GPSDestLongitude";
  public static final String TAG_GPS_DEST_LONGITUDE_REF = "GPSDestLongitudeRef";
  public static final String TAG_GPS_DIFFERENTIAL = "GPSDifferential";
  public static final String TAG_GPS_DOP = "GPSDOP";
  public static final String TAG_GPS_IMG_DIRECTION = "GPSImgDirection";
  public static final String TAG_GPS_IMG_DIRECTION_REF = "GPSImgDirectionRef";
  private static final String TAG_GPS_INFO_IFD_POINTER = "GPSInfoIFDPointer";
  public static final String TAG_GPS_LATITUDE = "GPSLatitude";
  public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
  public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
  public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
  public static final String TAG_GPS_MAP_DATUM = "GPSMapDatum";
  public static final String TAG_GPS_MEASURE_MODE = "GPSMeasureMode";
  public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
  public static final String TAG_GPS_SATELLITES = "GPSSatellites";
  public static final String TAG_GPS_SPEED = "GPSSpeed";
  public static final String TAG_GPS_SPEED_REF = "GPSSpeedRef";
  public static final String TAG_GPS_STATUS = "GPSStatus";
  public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
  public static final String TAG_GPS_TRACK = "GPSTrack";
  public static final String TAG_GPS_TRACK_REF = "GPSTrackRef";
  public static final String TAG_GPS_VERSION_ID = "GPSVersionID";
  private static final String TAG_HAS_THUMBNAIL = "HasThumbnail";
  public static final String TAG_IMAGE_DESCRIPTION = "ImageDescription";
  public static final String TAG_IMAGE_LENGTH = "ImageLength";
  public static final String TAG_IMAGE_UNIQUE_ID = "ImageUniqueID";
  public static final String TAG_IMAGE_WIDTH = "ImageWidth";
  private static final String TAG_INTEROPERABILITY_IFD_POINTER = "InteroperabilityIFDPointer";
  public static final String TAG_INTEROPERABILITY_INDEX = "InteroperabilityIndex";
  @Deprecated
  public static final String TAG_ISO = "ISOSpeedRatings";
  public static final String TAG_ISO_SPEED_RATINGS = "ISOSpeedRatings";
  public static final String TAG_JPEG_INTERCHANGE_FORMAT = "JPEGInterchangeFormat";
  public static final String TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = "JPEGInterchangeFormatLength";
  public static final String TAG_LIGHT_SOURCE = "LightSource";
  public static final String TAG_MAKE = "Make";
  public static final String TAG_MAKER_NOTE = "MakerNote";
  public static final String TAG_MAX_APERTURE_VALUE = "MaxApertureValue";
  public static final String TAG_METERING_MODE = "MeteringMode";
  public static final String TAG_MODEL = "Model";
  public static final String TAG_NEW_SUBFILE_TYPE = "NewSubfileType";
  public static final String TAG_OECF = "OECF";
  public static final String TAG_ORF_ASPECT_FRAME = "AspectFrame";
  private static final String TAG_ORF_CAMERA_SETTINGS_IFD_POINTER = "CameraSettingsIFDPointer";
  private static final String TAG_ORF_IMAGE_PROCESSING_IFD_POINTER = "ImageProcessingIFDPointer";
  public static final String TAG_ORF_PREVIEW_IMAGE_LENGTH = "PreviewImageLength";
  public static final String TAG_ORF_PREVIEW_IMAGE_START = "PreviewImageStart";
  public static final String TAG_ORF_THUMBNAIL_IMAGE = "ThumbnailImage";
  public static final String TAG_ORIENTATION = "Orientation";
  public static final String TAG_PHOTOMETRIC_INTERPRETATION = "PhotometricInterpretation";
  public static final String TAG_PIXEL_X_DIMENSION = "PixelXDimension";
  public static final String TAG_PIXEL_Y_DIMENSION = "PixelYDimension";
  public static final String TAG_PLANAR_CONFIGURATION = "PlanarConfiguration";
  public static final String TAG_PRIMARY_CHROMATICITIES = "PrimaryChromaticities";
  private static final ExifTag TAG_RAF_IMAGE_SIZE;
  public static final String TAG_REFERENCE_BLACK_WHITE = "ReferenceBlackWhite";
  public static final String TAG_RELATED_SOUND_FILE = "RelatedSoundFile";
  public static final String TAG_RESOLUTION_UNIT = "ResolutionUnit";
  public static final String TAG_ROWS_PER_STRIP = "RowsPerStrip";
  public static final String TAG_RW2_ISO = "ISO";
  public static final String TAG_RW2_JPG_FROM_RAW = "JpgFromRaw";
  public static final String TAG_RW2_SENSOR_BOTTOM_BORDER = "SensorBottomBorder";
  public static final String TAG_RW2_SENSOR_LEFT_BORDER = "SensorLeftBorder";
  public static final String TAG_RW2_SENSOR_RIGHT_BORDER = "SensorRightBorder";
  public static final String TAG_RW2_SENSOR_TOP_BORDER = "SensorTopBorder";
  public static final String TAG_SAMPLES_PER_PIXEL = "SamplesPerPixel";
  public static final String TAG_SATURATION = "Saturation";
  public static final String TAG_SCENE_CAPTURE_TYPE = "SceneCaptureType";
  public static final String TAG_SCENE_TYPE = "SceneType";
  public static final String TAG_SENSING_METHOD = "SensingMethod";
  public static final String TAG_SHARPNESS = "Sharpness";
  public static final String TAG_SHUTTER_SPEED_VALUE = "ShutterSpeedValue";
  public static final String TAG_SOFTWARE = "Software";
  public static final String TAG_SPATIAL_FREQUENCY_RESPONSE = "SpatialFrequencyResponse";
  public static final String TAG_SPECTRAL_SENSITIVITY = "SpectralSensitivity";
  public static final String TAG_STRIP_BYTE_COUNTS = "StripByteCounts";
  public static final String TAG_STRIP_OFFSETS = "StripOffsets";
  public static final String TAG_SUBFILE_TYPE = "SubfileType";
  public static final String TAG_SUBJECT_AREA = "SubjectArea";
  public static final String TAG_SUBJECT_DISTANCE = "SubjectDistance";
  public static final String TAG_SUBJECT_DISTANCE_RANGE = "SubjectDistanceRange";
  public static final String TAG_SUBJECT_LOCATION = "SubjectLocation";
  public static final String TAG_SUBSEC_TIME = "SubSecTime";
  public static final String TAG_SUBSEC_TIME_DIG = "SubSecTimeDigitized";
  public static final String TAG_SUBSEC_TIME_DIGITIZED = "SubSecTimeDigitized";
  public static final String TAG_SUBSEC_TIME_ORIG = "SubSecTimeOriginal";
  public static final String TAG_SUBSEC_TIME_ORIGINAL = "SubSecTimeOriginal";
  private static final String TAG_SUB_IFD_POINTER = "SubIFDPointer";
  private static final String TAG_THUMBNAIL_DATA = "ThumbnailData";
  public static final String TAG_THUMBNAIL_IMAGE_LENGTH = "ThumbnailImageLength";
  public static final String TAG_THUMBNAIL_IMAGE_WIDTH = "ThumbnailImageWidth";
  private static final String TAG_THUMBNAIL_LENGTH = "ThumbnailLength";
  private static final String TAG_THUMBNAIL_OFFSET = "ThumbnailOffset";
  public static final String TAG_TRANSFER_FUNCTION = "TransferFunction";
  public static final String TAG_USER_COMMENT = "UserComment";
  public static final String TAG_WHITE_BALANCE = "WhiteBalance";
  public static final String TAG_WHITE_POINT = "WhitePoint";
  public static final String TAG_X_RESOLUTION = "XResolution";
  public static final String TAG_Y_CB_CR_COEFFICIENTS = "YCbCrCoefficients";
  public static final String TAG_Y_CB_CR_POSITIONING = "YCbCrPositioning";
  public static final String TAG_Y_CB_CR_SUB_SAMPLING = "YCbCrSubSampling";
  public static final String TAG_Y_RESOLUTION = "YResolution";
  public static final int WHITEBALANCE_AUTO = 0;
  public static final int WHITEBALANCE_MANUAL = 1;
  private static final HashMap<Integer, Integer> sExifPointerTagMap;
  private static final HashMap[] sExifTagMapsForReading;
  private static final HashMap[] sExifTagMapsForWriting;
  private static SimpleDateFormat sFormatter;
  private static final Pattern sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
  private static final Pattern sNonZeroTimePattern;
  private static final HashSet<String> sTagSetForCompatibility;
  private final AssetManager.AssetInputStream mAssetInputStream;
  private final HashMap[] mAttributes = new HashMap[EXIF_TAGS.length];
  private Set<Integer> mAttributesOffsets = new HashSet(EXIF_TAGS.length);
  private ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
  private int mExifOffset;
  private final String mFilename;
  private boolean mHasThumbnail;
  private final boolean mIsInputStream;
  private boolean mIsSupportedFile;
  private int mMimeType;
  private int mOrfMakerNoteOffset;
  private int mOrfThumbnailLength;
  private int mOrfThumbnailOffset;
  private int mRw2JpgFromRawOffset;
  private final FileDescriptor mSeekableFileDescriptor;
  private byte[] mThumbnailBytes;
  private int mThumbnailCompression;
  private int mThumbnailLength;
  private int mThumbnailOffset;
  
  static
  {
    HEIF_TYPE_FTYP = new byte[] { 102, 116, 121, 112 };
    HEIF_BRAND_MIF1 = new byte[] { 109, 105, 102, 49 };
    HEIF_BRAND_HEIC = new byte[] { 104, 101, 105, 99 };
    ORF_MAKER_NOTE_HEADER_1 = new byte[] { 79, 76, 89, 77, 80, 0 };
    ORF_MAKER_NOTE_HEADER_2 = new byte[] { 79, 76, 89, 77, 80, 85, 83, 0, 73, 73 };
    IFD_FORMAT_NAMES = new String[] { "", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE" };
    IFD_FORMAT_BYTES_PER_FORMAT = new int[] { 0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1 };
    EXIF_ASCII_PREFIX = new byte[] { 65, 83, 67, 73, 73, 0, 0, 0 };
    BITS_PER_SAMPLE_RGB = new int[] { 8, 8, 8 };
    BITS_PER_SAMPLE_GREYSCALE_1 = new int[] { 4 };
    BITS_PER_SAMPLE_GREYSCALE_2 = new int[] { 8 };
    IFD_TIFF_TAGS = new ExifTag[] { new ExifTag("NewSubfileType", 254, 4, null), new ExifTag("SubfileType", 255, 4, null), new ExifTag("ImageWidth", 256, 3, 4, null), new ExifTag("ImageLength", 257, 3, 4, null), new ExifTag("BitsPerSample", 258, 3, null), new ExifTag("Compression", 259, 3, null), new ExifTag("PhotometricInterpretation", 262, 3, null), new ExifTag("ImageDescription", 270, 2, null), new ExifTag("Make", 271, 2, null), new ExifTag("Model", 272, 2, null), new ExifTag("StripOffsets", 273, 3, 4, null), new ExifTag("Orientation", 274, 3, null), new ExifTag("SamplesPerPixel", 277, 3, null), new ExifTag("RowsPerStrip", 278, 3, 4, null), new ExifTag("StripByteCounts", 279, 3, 4, null), new ExifTag("XResolution", 282, 5, null), new ExifTag("YResolution", 283, 5, null), new ExifTag("PlanarConfiguration", 284, 3, null), new ExifTag("ResolutionUnit", 296, 3, null), new ExifTag("TransferFunction", 301, 3, null), new ExifTag("Software", 305, 2, null), new ExifTag("DateTime", 306, 2, null), new ExifTag("Artist", 315, 2, null), new ExifTag("WhitePoint", 318, 5, null), new ExifTag("PrimaryChromaticities", 319, 5, null), new ExifTag("SubIFDPointer", 330, 4, null), new ExifTag("JPEGInterchangeFormat", 513, 4, null), new ExifTag("JPEGInterchangeFormatLength", 514, 4, null), new ExifTag("YCbCrCoefficients", 529, 5, null), new ExifTag("YCbCrSubSampling", 530, 3, null), new ExifTag("YCbCrPositioning", 531, 3, null), new ExifTag("ReferenceBlackWhite", 532, 5, null), new ExifTag("Copyright", 33432, 2, null), new ExifTag("ExifIFDPointer", 34665, 4, null), new ExifTag("GPSInfoIFDPointer", 34853, 4, null), new ExifTag("SensorTopBorder", 4, 4, null), new ExifTag("SensorLeftBorder", 5, 4, null), new ExifTag("SensorBottomBorder", 6, 4, null), new ExifTag("SensorRightBorder", 7, 4, null), new ExifTag("ISO", 23, 3, null), new ExifTag("JpgFromRaw", 46, 7, null) };
    IFD_EXIF_TAGS = new ExifTag[] { new ExifTag("ExposureTime", 33434, 5, null), new ExifTag("FNumber", 33437, 5, null), new ExifTag("ExposureProgram", 34850, 3, null), new ExifTag("SpectralSensitivity", 34852, 2, null), new ExifTag("ISOSpeedRatings", 34855, 3, null), new ExifTag("OECF", 34856, 7, null), new ExifTag("ExifVersion", 36864, 2, null), new ExifTag("DateTimeOriginal", 36867, 2, null), new ExifTag("DateTimeDigitized", 36868, 2, null), new ExifTag("ComponentsConfiguration", 37121, 7, null), new ExifTag("CompressedBitsPerPixel", 37122, 5, null), new ExifTag("ShutterSpeedValue", 37377, 10, null), new ExifTag("ApertureValue", 37378, 5, null), new ExifTag("BrightnessValue", 37379, 10, null), new ExifTag("ExposureBiasValue", 37380, 10, null), new ExifTag("MaxApertureValue", 37381, 5, null), new ExifTag("SubjectDistance", 37382, 5, null), new ExifTag("MeteringMode", 37383, 3, null), new ExifTag("LightSource", 37384, 3, null), new ExifTag("Flash", 37385, 3, null), new ExifTag("FocalLength", 37386, 5, null), new ExifTag("SubjectArea", 37396, 3, null), new ExifTag("MakerNote", 37500, 7, null), new ExifTag("UserComment", 37510, 7, null), new ExifTag("SubSecTime", 37520, 2, null), new ExifTag("SubSecTimeOriginal", 37521, 2, null), new ExifTag("SubSecTimeDigitized", 37522, 2, null), new ExifTag("FlashpixVersion", 40960, 7, null), new ExifTag("ColorSpace", 40961, 3, null), new ExifTag("PixelXDimension", 40962, 3, 4, null), new ExifTag("PixelYDimension", 40963, 3, 4, null), new ExifTag("RelatedSoundFile", 40964, 2, null), new ExifTag("InteroperabilityIFDPointer", 40965, 4, null), new ExifTag("FlashEnergy", 41483, 5, null), new ExifTag("SpatialFrequencyResponse", 41484, 7, null), new ExifTag("FocalPlaneXResolution", 41486, 5, null), new ExifTag("FocalPlaneYResolution", 41487, 5, null), new ExifTag("FocalPlaneResolutionUnit", 41488, 3, null), new ExifTag("SubjectLocation", 41492, 3, null), new ExifTag("ExposureIndex", 41493, 5, null), new ExifTag("SensingMethod", 41495, 3, null), new ExifTag("FileSource", 41728, 7, null), new ExifTag("SceneType", 41729, 7, null), new ExifTag("CFAPattern", 41730, 7, null), new ExifTag("CustomRendered", 41985, 3, null), new ExifTag("ExposureMode", 41986, 3, null), new ExifTag("WhiteBalance", 41987, 3, null), new ExifTag("DigitalZoomRatio", 41988, 5, null), new ExifTag("FocalLengthIn35mmFilm", 41989, 3, null), new ExifTag("SceneCaptureType", 41990, 3, null), new ExifTag("GainControl", 41991, 3, null), new ExifTag("Contrast", 41992, 3, null), new ExifTag("Saturation", 41993, 3, null), new ExifTag("Sharpness", 41994, 3, null), new ExifTag("DeviceSettingDescription", 41995, 7, null), new ExifTag("SubjectDistanceRange", 41996, 3, null), new ExifTag("ImageUniqueID", 42016, 2, null), new ExifTag("DNGVersion", 50706, 1, null), new ExifTag("DefaultCropSize", 50720, 3, 4, null) };
    IFD_GPS_TAGS = new ExifTag[] { new ExifTag("GPSVersionID", 0, 1, null), new ExifTag("GPSLatitudeRef", 1, 2, null), new ExifTag("GPSLatitude", 2, 5, null), new ExifTag("GPSLongitudeRef", 3, 2, null), new ExifTag("GPSLongitude", 4, 5, null), new ExifTag("GPSAltitudeRef", 5, 1, null), new ExifTag("GPSAltitude", 6, 5, null), new ExifTag("GPSTimeStamp", 7, 5, null), new ExifTag("GPSSatellites", 8, 2, null), new ExifTag("GPSStatus", 9, 2, null), new ExifTag("GPSMeasureMode", 10, 2, null), new ExifTag("GPSDOP", 11, 5, null), new ExifTag("GPSSpeedRef", 12, 2, null), new ExifTag("GPSSpeed", 13, 5, null), new ExifTag("GPSTrackRef", 14, 2, null), new ExifTag("GPSTrack", 15, 5, null), new ExifTag("GPSImgDirectionRef", 16, 2, null), new ExifTag("GPSImgDirection", 17, 5, null), new ExifTag("GPSMapDatum", 18, 2, null), new ExifTag("GPSDestLatitudeRef", 19, 2, null), new ExifTag("GPSDestLatitude", 20, 5, null), new ExifTag("GPSDestLongitudeRef", 21, 2, null), new ExifTag("GPSDestLongitude", 22, 5, null), new ExifTag("GPSDestBearingRef", 23, 2, null), new ExifTag("GPSDestBearing", 24, 5, null), new ExifTag("GPSDestDistanceRef", 25, 2, null), new ExifTag("GPSDestDistance", 26, 5, null), new ExifTag("GPSProcessingMethod", 27, 7, null), new ExifTag("GPSAreaInformation", 28, 7, null), new ExifTag("GPSDateStamp", 29, 2, null), new ExifTag("GPSDifferential", 30, 3, null) };
    IFD_INTEROPERABILITY_TAGS = new ExifTag[] { new ExifTag("InteroperabilityIndex", 1, 2, null) };
    IFD_THUMBNAIL_TAGS = new ExifTag[] { new ExifTag("NewSubfileType", 254, 4, null), new ExifTag("SubfileType", 255, 4, null), new ExifTag("ThumbnailImageWidth", 256, 3, 4, null), new ExifTag("ThumbnailImageLength", 257, 3, 4, null), new ExifTag("BitsPerSample", 258, 3, null), new ExifTag("Compression", 259, 3, null), new ExifTag("PhotometricInterpretation", 262, 3, null), new ExifTag("ImageDescription", 270, 2, null), new ExifTag("Make", 271, 2, null), new ExifTag("Model", 272, 2, null), new ExifTag("StripOffsets", 273, 3, 4, null), new ExifTag("Orientation", 274, 3, null), new ExifTag("SamplesPerPixel", 277, 3, null), new ExifTag("RowsPerStrip", 278, 3, 4, null), new ExifTag("StripByteCounts", 279, 3, 4, null), new ExifTag("XResolution", 282, 5, null), new ExifTag("YResolution", 283, 5, null), new ExifTag("PlanarConfiguration", 284, 3, null), new ExifTag("ResolutionUnit", 296, 3, null), new ExifTag("TransferFunction", 301, 3, null), new ExifTag("Software", 305, 2, null), new ExifTag("DateTime", 306, 2, null), new ExifTag("Artist", 315, 2, null), new ExifTag("WhitePoint", 318, 5, null), new ExifTag("PrimaryChromaticities", 319, 5, null), new ExifTag("SubIFDPointer", 330, 4, null), new ExifTag("JPEGInterchangeFormat", 513, 4, null), new ExifTag("JPEGInterchangeFormatLength", 514, 4, null), new ExifTag("YCbCrCoefficients", 529, 5, null), new ExifTag("YCbCrSubSampling", 530, 3, null), new ExifTag("YCbCrPositioning", 531, 3, null), new ExifTag("ReferenceBlackWhite", 532, 5, null), new ExifTag("Copyright", 33432, 2, null), new ExifTag("ExifIFDPointer", 34665, 4, null), new ExifTag("GPSInfoIFDPointer", 34853, 4, null), new ExifTag("DNGVersion", 50706, 1, null), new ExifTag("DefaultCropSize", 50720, 3, 4, null) };
    TAG_RAF_IMAGE_SIZE = new ExifTag("StripOffsets", 273, 3, null);
    ORF_MAKER_NOTE_TAGS = new ExifTag[] { new ExifTag("ThumbnailImage", 256, 7, null), new ExifTag("CameraSettingsIFDPointer", 8224, 4, null), new ExifTag("ImageProcessingIFDPointer", 8256, 4, null) };
    ORF_CAMERA_SETTINGS_TAGS = new ExifTag[] { new ExifTag("PreviewImageStart", 257, 4, null), new ExifTag("PreviewImageLength", 258, 4, null) };
    ORF_IMAGE_PROCESSING_TAGS = new ExifTag[] { new ExifTag("AspectFrame", 4371, 3, null) };
    PEF_TAGS = new ExifTag[] { new ExifTag("ColorSpace", 55, 3, null) };
    EXIF_TAGS = new ExifTag[][] { IFD_TIFF_TAGS, IFD_EXIF_TAGS, IFD_GPS_TAGS, IFD_INTEROPERABILITY_TAGS, IFD_THUMBNAIL_TAGS, IFD_TIFF_TAGS, ORF_MAKER_NOTE_TAGS, ORF_CAMERA_SETTINGS_TAGS, ORF_IMAGE_PROCESSING_TAGS, PEF_TAGS };
    EXIF_POINTER_TAGS = new ExifTag[] { new ExifTag("SubIFDPointer", 330, 4, null), new ExifTag("ExifIFDPointer", 34665, 4, null), new ExifTag("GPSInfoIFDPointer", 34853, 4, null), new ExifTag("InteroperabilityIFDPointer", 40965, 4, null), new ExifTag("CameraSettingsIFDPointer", 8224, 1, null), new ExifTag("ImageProcessingIFDPointer", 8256, 1, null) };
    JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag("JPEGInterchangeFormat", 513, 4, null);
    JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag("JPEGInterchangeFormatLength", 514, 4, null);
    sExifTagMapsForReading = new HashMap[EXIF_TAGS.length];
    sExifTagMapsForWriting = new HashMap[EXIF_TAGS.length];
    sTagSetForCompatibility = new HashSet(Arrays.asList(new String[] { "FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp" }));
    sExifPointerTagMap = new HashMap();
    ASCII = Charset.forName("US-ASCII");
    IDENTIFIER_EXIF_APP1 = "Exif\000\000".getBytes(ASCII);
    sFormatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    sFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    for (int i = 0; i < EXIF_TAGS.length; i++)
    {
      sExifTagMapsForReading[i] = new HashMap();
      sExifTagMapsForWriting[i] = new HashMap();
      for (ExifTag localExifTag : EXIF_TAGS[i])
      {
        sExifTagMapsForReading[i].put(Integer.valueOf(number), localExifTag);
        sExifTagMapsForWriting[i].put(name, localExifTag);
      }
    }
    sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS0number), Integer.valueOf(5));
    sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS1number), Integer.valueOf(1));
    sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS2number), Integer.valueOf(2));
    sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS3number), Integer.valueOf(3));
    sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS4number), Integer.valueOf(7));
    sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS5number), Integer.valueOf(8));
    sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
  }
  
  public ExifInterface(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    if (paramFileDescriptor != null)
    {
      Object localObject1 = null;
      mAssetInputStream = null;
      mFilename = null;
      Object localObject2;
      if (isSeekableFD(paramFileDescriptor))
      {
        mSeekableFileDescriptor = paramFileDescriptor;
        try
        {
          localObject2 = Os.dup(paramFileDescriptor);
        }
        catch (ErrnoException paramFileDescriptor)
        {
          throw paramFileDescriptor.rethrowAsIOException();
        }
      }
      else
      {
        mSeekableFileDescriptor = null;
        localObject2 = paramFileDescriptor;
      }
      mIsInputStream = false;
      paramFileDescriptor = localObject1;
      try
      {
        FileInputStream localFileInputStream = new java/io/FileInputStream;
        paramFileDescriptor = localObject1;
        localFileInputStream.<init>((FileDescriptor)localObject2);
        localObject2 = localFileInputStream;
        paramFileDescriptor = (FileDescriptor)localObject2;
        loadAttributes((InputStream)localObject2);
        IoUtils.closeQuietly((AutoCloseable)localObject2);
        return;
      }
      finally
      {
        IoUtils.closeQuietly(paramFileDescriptor);
      }
    }
    throw new IllegalArgumentException("fileDescriptor cannot be null");
  }
  
  public ExifInterface(InputStream paramInputStream)
    throws IOException
  {
    if (paramInputStream != null)
    {
      mFilename = null;
      if ((paramInputStream instanceof AssetManager.AssetInputStream))
      {
        mAssetInputStream = ((AssetManager.AssetInputStream)paramInputStream);
        mSeekableFileDescriptor = null;
      }
      else if (((paramInputStream instanceof FileInputStream)) && (isSeekableFD(((FileInputStream)paramInputStream).getFD())))
      {
        mAssetInputStream = null;
        mSeekableFileDescriptor = ((FileInputStream)paramInputStream).getFD();
      }
      else
      {
        mAssetInputStream = null;
        mSeekableFileDescriptor = null;
      }
      mIsInputStream = true;
      loadAttributes(paramInputStream);
      return;
    }
    throw new IllegalArgumentException("inputStream cannot be null");
  }
  
  public ExifInterface(String paramString)
    throws IOException
  {
    if (paramString != null)
    {
      Object localObject1 = null;
      mAssetInputStream = null;
      mFilename = paramString;
      mIsInputStream = false;
      Object localObject2 = localObject1;
      try
      {
        FileInputStream localFileInputStream = new java/io/FileInputStream;
        localObject2 = localObject1;
        localFileInputStream.<init>(paramString);
        paramString = localFileInputStream;
        localObject2 = paramString;
        if (isSeekableFD(paramString.getFD()))
        {
          localObject2 = paramString;
          mSeekableFileDescriptor = paramString.getFD();
        }
        else
        {
          localObject2 = paramString;
          mSeekableFileDescriptor = null;
        }
        localObject2 = paramString;
        loadAttributes(paramString);
        IoUtils.closeQuietly(paramString);
        return;
      }
      finally
      {
        IoUtils.closeQuietly((AutoCloseable)localObject2);
      }
    }
    throw new IllegalArgumentException("filename cannot be null");
  }
  
  private void addDefaultValuesForCompatibility()
  {
    String str = getAttribute("DateTimeOriginal");
    if ((str != null) && (getAttribute("DateTime") == null)) {
      mAttributes[0].put("DateTime", ExifAttribute.createString(str));
    }
    if (getAttribute("ImageWidth") == null) {
      mAttributes[0].put("ImageWidth", ExifAttribute.createULong(0L, mExifByteOrder));
    }
    if (getAttribute("ImageLength") == null) {
      mAttributes[0].put("ImageLength", ExifAttribute.createULong(0L, mExifByteOrder));
    }
    if (getAttribute("Orientation") == null) {
      mAttributes[0].put("Orientation", ExifAttribute.createUShort(0, mExifByteOrder));
    }
    if (getAttribute("LightSource") == null) {
      mAttributes[1].put("LightSource", ExifAttribute.createULong(0L, mExifByteOrder));
    }
  }
  
  private boolean containsMatch(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    for (int i = 0; i < paramArrayOfByte1.length - paramArrayOfByte2.length; i++) {
      for (int j = 0; (j < paramArrayOfByte2.length) && (paramArrayOfByte1[(i + j)] == paramArrayOfByte2[j]); j++) {
        if (j == paramArrayOfByte2.length - 1) {
          return true;
        }
      }
    }
    return false;
  }
  
  public static float convertRationalLatLonToFloat(String paramString1, String paramString2)
  {
    try
    {
      paramString1 = paramString1.split(",");
      String[] arrayOfString = paramString1[0].split("/");
      double d1 = Double.parseDouble(arrayOfString[0].trim()) / Double.parseDouble(arrayOfString[1].trim());
      arrayOfString = paramString1[1].split("/");
      double d2 = Double.parseDouble(arrayOfString[0].trim()) / Double.parseDouble(arrayOfString[1].trim());
      paramString1 = paramString1[2].split("/");
      double d3 = Double.parseDouble(paramString1[0].trim()) / Double.parseDouble(paramString1[1].trim());
      d3 = d2 / 60.0D + d1 + d3 / 3600.0D;
      if (!paramString2.equals("S"))
      {
        boolean bool = paramString2.equals("W");
        if (!bool) {
          return (float)d3;
        }
      }
      return (float)-d3;
    }
    catch (NumberFormatException|ArrayIndexOutOfBoundsException paramString1)
    {
      throw new IllegalArgumentException();
    }
  }
  
  private static long[] convertToLongArray(Object paramObject)
  {
    if ((paramObject instanceof int[]))
    {
      paramObject = (int[])paramObject;
      long[] arrayOfLong = new long[paramObject.length];
      for (int i = 0; i < paramObject.length; i++) {
        arrayOfLong[i] = paramObject[i];
      }
      return arrayOfLong;
    }
    if ((paramObject instanceof long[])) {
      return (long[])paramObject;
    }
    return null;
  }
  
  private ExifAttribute getExifAttribute(String paramString)
  {
    for (int i = 0; i < EXIF_TAGS.length; i++)
    {
      Object localObject = mAttributes[i].get(paramString);
      if (localObject != null) {
        return (ExifAttribute)localObject;
      }
    }
    return null;
  }
  
  private void getHeifAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream)
    throws IOException
  {
    MediaMetadataRetriever localMediaMetadataRetriever = new MediaMetadataRetriever();
    try
    {
      Object localObject = new android/media/ExifInterface$1;
      ((1)localObject).<init>(this, paramByteOrderedDataInputStream);
      localMediaMetadataRetriever.setDataSource((MediaDataSource)localObject);
      String str1 = localMediaMetadataRetriever.extractMetadata(33);
      String str2 = localMediaMetadataRetriever.extractMetadata(34);
      String str3 = localMediaMetadataRetriever.extractMetadata(26);
      String str4 = localMediaMetadataRetriever.extractMetadata(17);
      localObject = null;
      String str5 = null;
      String str6 = null;
      if ("yes".equals(str3))
      {
        localObject = localMediaMetadataRetriever.extractMetadata(29);
        str5 = localMediaMetadataRetriever.extractMetadata(30);
        str6 = localMediaMetadataRetriever.extractMetadata(31);
      }
      else if ("yes".equals(str4))
      {
        localObject = localMediaMetadataRetriever.extractMetadata(18);
        str5 = localMediaMetadataRetriever.extractMetadata(19);
        str6 = localMediaMetadataRetriever.extractMetadata(24);
      }
      if (localObject != null) {
        mAttributes[0].put("ImageWidth", ExifAttribute.createUShort(Integer.parseInt((String)localObject), mExifByteOrder));
      }
      if (str5 != null) {
        mAttributes[0].put("ImageLength", ExifAttribute.createUShort(Integer.parseInt(str5), mExifByteOrder));
      }
      int i;
      int j;
      if (str6 != null)
      {
        i = 1;
        j = Integer.parseInt(str6);
        if (j != 90)
        {
          if (j != 180)
          {
            if (j == 270) {
              i = 8;
            }
          }
          else {
            i = 3;
          }
        }
        else {
          i = 6;
        }
        mAttributes[0].put("Orientation", ExifAttribute.createUShort(i, mExifByteOrder));
      }
      if ((str1 != null) && (str2 != null))
      {
        i = Integer.parseInt(str1);
        j = Integer.parseInt(str2);
        if (j > 6)
        {
          paramByteOrderedDataInputStream.seek(i);
          localObject = new byte[6];
          if (paramByteOrderedDataInputStream.read((byte[])localObject) == 6)
          {
            i = j - 6;
            if (Arrays.equals((byte[])localObject, IDENTIFIER_EXIF_APP1))
            {
              localObject = new byte[i];
              if (paramByteOrderedDataInputStream.read((byte[])localObject) == i)
              {
                readExifSegment((byte[])localObject, 0);
              }
              else
              {
                paramByteOrderedDataInputStream = new java/io/IOException;
                paramByteOrderedDataInputStream.<init>("Can't read exif");
                throw paramByteOrderedDataInputStream;
              }
            }
            else
            {
              paramByteOrderedDataInputStream = new java/io/IOException;
              paramByteOrderedDataInputStream.<init>("Invalid identifier");
              throw paramByteOrderedDataInputStream;
            }
          }
          else
          {
            paramByteOrderedDataInputStream = new java/io/IOException;
            paramByteOrderedDataInputStream.<init>("Can't read identifier");
            throw paramByteOrderedDataInputStream;
          }
        }
        else
        {
          paramByteOrderedDataInputStream = new java/io/IOException;
          paramByteOrderedDataInputStream.<init>("Invalid exif length");
          throw paramByteOrderedDataInputStream;
        }
      }
      return;
    }
    finally
    {
      localMediaMetadataRetriever.release();
    }
  }
  
  private void getJpegAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream, int paramInt1, int paramInt2)
    throws IOException
  {
    paramByteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
    paramByteOrderedDataInputStream.seek(paramInt1);
    int i = paramByteOrderedDataInputStream.readByte();
    if (i == -1)
    {
      if (paramByteOrderedDataInputStream.readByte() == -40)
      {
        for (paramInt1 = paramInt1 + 1 + 1;; paramInt1 = i + paramInt1)
        {
          i = paramByteOrderedDataInputStream.readByte();
          if (i != -1) {
            break label603;
          }
          i = paramByteOrderedDataInputStream.readByte();
          if ((i == -39) || (i == -38)) {
            break label594;
          }
          int j = paramByteOrderedDataInputStream.readUnsignedShort() - 2;
          int k = paramInt1 + 1 + 1 + 2;
          if (j < 0) {
            break label583;
          }
          byte[] arrayOfByte;
          if (i != -31)
          {
            if (i != -2)
            {
              switch (i)
              {
              default: 
                switch (i)
                {
                default: 
                  switch (i)
                  {
                  default: 
                    switch (i)
                    {
                    default: 
                      i = k;
                      paramInt1 = j;
                    }
                    break;
                  }
                  break;
                }
                break;
              case -64: 
              case -63: 
              case -62: 
              case -61: 
                if (paramByteOrderedDataInputStream.skipBytes(1) == 1)
                {
                  mAttributes[paramInt2].put("ImageLength", ExifAttribute.createULong(paramByteOrderedDataInputStream.readUnsignedShort(), mExifByteOrder));
                  mAttributes[paramInt2].put("ImageWidth", ExifAttribute.createULong(paramByteOrderedDataInputStream.readUnsignedShort(), mExifByteOrder));
                  paramInt1 = j - 5;
                  i = k;
                  break;
                }
                throw new IOException("Invalid SOFx");
              }
            }
            else
            {
              arrayOfByte = new byte[j];
              if (paramByteOrderedDataInputStream.read(arrayOfByte) == j)
              {
                j = 0;
                i = k;
                paramInt1 = j;
                if (getAttribute("UserComment") == null)
                {
                  mAttributes[1].put("UserComment", ExifAttribute.createString(new String(arrayOfByte, ASCII)));
                  i = k;
                  paramInt1 = j;
                }
              }
              else
              {
                throw new IOException("Invalid exif");
              }
            }
          }
          else if (j < 6)
          {
            i = k;
            paramInt1 = j;
          }
          else
          {
            arrayOfByte = new byte[6];
            if (paramByteOrderedDataInputStream.read(arrayOfByte) != 6) {
              break label572;
            }
            i = k + 6;
            paramInt1 = j - 6;
            if (Arrays.equals(arrayOfByte, IDENTIFIER_EXIF_APP1))
            {
              if (paramInt1 <= 0) {
                break label561;
              }
              mExifOffset = i;
              arrayOfByte = new byte[paramInt1];
              if (paramByteOrderedDataInputStream.read(arrayOfByte) != paramInt1) {
                break label550;
              }
              i += paramInt1;
              paramInt1 = 0;
              readExifSegment(arrayOfByte, paramInt2);
            }
          }
          if (paramInt1 < 0) {
            break label539;
          }
          if (paramByteOrderedDataInputStream.skipBytes(paramInt1) != paramInt1) {
            break;
          }
        }
        throw new IOException("Invalid JPEG segment");
        label539:
        throw new IOException("Invalid length");
        label550:
        throw new IOException("Invalid exif");
        label561:
        throw new IOException("Invalid exif");
        label572:
        throw new IOException("Invalid exif");
        label583:
        throw new IOException("Invalid length");
        label594:
        paramByteOrderedDataInputStream.setByteOrder(mExifByteOrder);
        return;
        label603:
        paramByteOrderedDataInputStream = new StringBuilder();
        paramByteOrderedDataInputStream.append("Invalid marker:");
        paramByteOrderedDataInputStream.append(Integer.toHexString(i & 0xFF));
        throw new IOException(paramByteOrderedDataInputStream.toString());
      }
      paramByteOrderedDataInputStream = new StringBuilder();
      paramByteOrderedDataInputStream.append("Invalid marker: ");
      paramByteOrderedDataInputStream.append(Integer.toHexString(i & 0xFF));
      throw new IOException(paramByteOrderedDataInputStream.toString());
    }
    paramByteOrderedDataInputStream = new StringBuilder();
    paramByteOrderedDataInputStream.append("Invalid marker: ");
    paramByteOrderedDataInputStream.append(Integer.toHexString(i & 0xFF));
    throw new IOException(paramByteOrderedDataInputStream.toString());
  }
  
  private int getMimeType(BufferedInputStream paramBufferedInputStream)
    throws IOException
  {
    paramBufferedInputStream.mark(5000);
    byte[] arrayOfByte = new byte['ᎈ'];
    paramBufferedInputStream.read(arrayOfByte);
    paramBufferedInputStream.reset();
    if (isJpegFormat(arrayOfByte)) {
      return 4;
    }
    if (isRafFormat(arrayOfByte)) {
      return 9;
    }
    if (isHeifFormat(arrayOfByte)) {
      return 12;
    }
    if (isOrfFormat(arrayOfByte)) {
      return 7;
    }
    if (isRw2Format(arrayOfByte)) {
      return 10;
    }
    return 0;
  }
  
  private void getOrfAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream)
    throws IOException
  {
    getRawAttributes(paramByteOrderedDataInputStream);
    paramByteOrderedDataInputStream = (ExifAttribute)mAttributes[1].get("MakerNote");
    if (paramByteOrderedDataInputStream != null)
    {
      Object localObject = new ByteOrderedDataInputStream(bytes);
      ((ByteOrderedDataInputStream)localObject).setByteOrder(mExifByteOrder);
      byte[] arrayOfByte = new byte[ORF_MAKER_NOTE_HEADER_1.length];
      ((ByteOrderedDataInputStream)localObject).readFully(arrayOfByte);
      ((ByteOrderedDataInputStream)localObject).seek(0L);
      paramByteOrderedDataInputStream = new byte[ORF_MAKER_NOTE_HEADER_2.length];
      ((ByteOrderedDataInputStream)localObject).readFully(paramByteOrderedDataInputStream);
      if (Arrays.equals(arrayOfByte, ORF_MAKER_NOTE_HEADER_1)) {
        ((ByteOrderedDataInputStream)localObject).seek(8L);
      } else if (Arrays.equals(paramByteOrderedDataInputStream, ORF_MAKER_NOTE_HEADER_2)) {
        ((ByteOrderedDataInputStream)localObject).seek(12L);
      }
      readImageFileDirectory((ByteOrderedDataInputStream)localObject, 6);
      paramByteOrderedDataInputStream = (ExifAttribute)mAttributes[7].get("PreviewImageStart");
      localObject = (ExifAttribute)mAttributes[7].get("PreviewImageLength");
      if ((paramByteOrderedDataInputStream != null) && (localObject != null))
      {
        mAttributes[5].put("JPEGInterchangeFormat", paramByteOrderedDataInputStream);
        mAttributes[5].put("JPEGInterchangeFormatLength", localObject);
      }
      paramByteOrderedDataInputStream = (ExifAttribute)mAttributes[8].get("AspectFrame");
      if (paramByteOrderedDataInputStream != null)
      {
        localObject = new int[4];
        paramByteOrderedDataInputStream = (int[])paramByteOrderedDataInputStream.getValue(mExifByteOrder);
        if ((paramByteOrderedDataInputStream[2] > paramByteOrderedDataInputStream[0]) && (paramByteOrderedDataInputStream[3] > paramByteOrderedDataInputStream[1]))
        {
          int i = paramByteOrderedDataInputStream[2] - paramByteOrderedDataInputStream[0] + 1;
          int j = paramByteOrderedDataInputStream[3] - paramByteOrderedDataInputStream[1] + 1;
          int k = i;
          int m = j;
          if (i < j)
          {
            k = i + j;
            m = k - j;
            k -= m;
          }
          paramByteOrderedDataInputStream = ExifAttribute.createUShort(k, mExifByteOrder);
          localObject = ExifAttribute.createUShort(m, mExifByteOrder);
          mAttributes[0].put("ImageWidth", paramByteOrderedDataInputStream);
          mAttributes[0].put("ImageLength", localObject);
        }
      }
    }
  }
  
  private void getRafAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream)
    throws IOException
  {
    paramByteOrderedDataInputStream.skipBytes(84);
    byte[] arrayOfByte = new byte[4];
    Object localObject = new byte[4];
    paramByteOrderedDataInputStream.read(arrayOfByte);
    paramByteOrderedDataInputStream.skipBytes(4);
    paramByteOrderedDataInputStream.read((byte[])localObject);
    int i = ByteBuffer.wrap(arrayOfByte).getInt();
    int j = ByteBuffer.wrap((byte[])localObject).getInt();
    getJpegAttributes(paramByteOrderedDataInputStream, i, 5);
    paramByteOrderedDataInputStream.seek(j);
    paramByteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
    i = paramByteOrderedDataInputStream.readInt();
    for (j = 0; j < i; j++)
    {
      int k = paramByteOrderedDataInputStream.readUnsignedShort();
      int m = paramByteOrderedDataInputStream.readUnsignedShort();
      if (k == TAG_RAF_IMAGE_SIZEnumber)
      {
        j = paramByteOrderedDataInputStream.readShort();
        i = paramByteOrderedDataInputStream.readShort();
        paramByteOrderedDataInputStream = ExifAttribute.createUShort(j, mExifByteOrder);
        localObject = ExifAttribute.createUShort(i, mExifByteOrder);
        mAttributes[0].put("ImageLength", paramByteOrderedDataInputStream);
        mAttributes[0].put("ImageWidth", localObject);
        return;
      }
      paramByteOrderedDataInputStream.skipBytes(m);
    }
  }
  
  private void getRawAttributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream)
    throws IOException
  {
    parseTiffHeaders(paramByteOrderedDataInputStream, paramByteOrderedDataInputStream.available());
    readImageFileDirectory(paramByteOrderedDataInputStream, 0);
    updateImageSizeValues(paramByteOrderedDataInputStream, 0);
    updateImageSizeValues(paramByteOrderedDataInputStream, 5);
    updateImageSizeValues(paramByteOrderedDataInputStream, 4);
    validateImages(paramByteOrderedDataInputStream);
    if (mMimeType == 8)
    {
      paramByteOrderedDataInputStream = (ExifAttribute)mAttributes[1].get("MakerNote");
      if (paramByteOrderedDataInputStream != null)
      {
        paramByteOrderedDataInputStream = new ByteOrderedDataInputStream(bytes);
        paramByteOrderedDataInputStream.setByteOrder(mExifByteOrder);
        paramByteOrderedDataInputStream.seek(6L);
        readImageFileDirectory(paramByteOrderedDataInputStream, 9);
        paramByteOrderedDataInputStream = (ExifAttribute)mAttributes[9].get("ColorSpace");
        if (paramByteOrderedDataInputStream != null) {
          mAttributes[1].put("ColorSpace", paramByteOrderedDataInputStream);
        }
      }
    }
  }
  
  private void getRw2Attributes(ByteOrderedDataInputStream paramByteOrderedDataInputStream)
    throws IOException
  {
    getRawAttributes(paramByteOrderedDataInputStream);
    if ((ExifAttribute)mAttributes[0].get("JpgFromRaw") != null) {
      getJpegAttributes(paramByteOrderedDataInputStream, mRw2JpgFromRawOffset, 5);
    }
    paramByteOrderedDataInputStream = (ExifAttribute)mAttributes[0].get("ISO");
    ExifAttribute localExifAttribute = (ExifAttribute)mAttributes[1].get("ISOSpeedRatings");
    if ((paramByteOrderedDataInputStream != null) && (localExifAttribute == null)) {
      mAttributes[1].put("ISOSpeedRatings", paramByteOrderedDataInputStream);
    }
  }
  
  private static Pair<Integer, Integer> guessDataFormat(String paramString)
  {
    boolean bool = paramString.contains(",");
    int i = 1;
    Object localObject;
    if (bool)
    {
      localObject = paramString.split(",");
      paramString = guessDataFormat(localObject[0]);
      if (((Integer)first).intValue() == 2) {
        return paramString;
      }
      while (i < localObject.length)
      {
        Pair localPair = guessDataFormat(localObject[i]);
        int j = -1;
        int k = -1;
        if ((first == first) || (second == first)) {
          j = ((Integer)first).intValue();
        }
        int m = k;
        if (((Integer)second).intValue() != -1) {
          if (first != second)
          {
            m = k;
            if (second != second) {}
          }
          else
          {
            m = ((Integer)second).intValue();
          }
        }
        if ((j == -1) && (m == -1)) {
          return new Pair(Integer.valueOf(2), Integer.valueOf(-1));
        }
        if (j == -1) {
          paramString = new Pair(Integer.valueOf(m), Integer.valueOf(-1));
        } else if (m == -1) {
          paramString = new Pair(Integer.valueOf(j), Integer.valueOf(-1));
        }
        i++;
      }
      return paramString;
    }
    if (paramString.contains("/"))
    {
      paramString = paramString.split("/");
      if (paramString.length == 2) {
        try
        {
          long l1 = Double.parseDouble(paramString[0]);
          long l2 = Double.parseDouble(paramString[1]);
          if ((l1 >= 0L) && (l2 >= 0L))
          {
            if ((l1 <= 2147483647L) && (l2 <= 2147483647L)) {
              return new Pair(Integer.valueOf(10), Integer.valueOf(5));
            }
            return new Pair(Integer.valueOf(5), Integer.valueOf(-1));
          }
          paramString = new Pair(Integer.valueOf(10), Integer.valueOf(-1));
          return paramString;
        }
        catch (NumberFormatException paramString) {}
      }
      return new Pair(Integer.valueOf(2), Integer.valueOf(-1));
    }
    try
    {
      localObject = Long.valueOf(Long.parseLong(paramString));
      if ((((Long)localObject).longValue() >= 0L) && (((Long)localObject).longValue() <= 65535L)) {
        return new Pair(Integer.valueOf(3), Integer.valueOf(4));
      }
      if (((Long)localObject).longValue() < 0L) {
        return new Pair(Integer.valueOf(9), Integer.valueOf(-1));
      }
      localObject = new Pair(Integer.valueOf(4), Integer.valueOf(-1));
      return localObject;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      try
      {
        Double.parseDouble(paramString);
        paramString = new Pair(Integer.valueOf(12), Integer.valueOf(-1));
        return paramString;
      }
      catch (NumberFormatException paramString) {}
    }
    return new Pair(Integer.valueOf(2), Integer.valueOf(-1));
  }
  
  private void handleThumbnailFromJfif(ByteOrderedDataInputStream paramByteOrderedDataInputStream, HashMap paramHashMap)
    throws IOException
  {
    ExifAttribute localExifAttribute = (ExifAttribute)paramHashMap.get("JPEGInterchangeFormat");
    paramHashMap = (ExifAttribute)paramHashMap.get("JPEGInterchangeFormatLength");
    if ((localExifAttribute != null) && (paramHashMap != null))
    {
      int i = localExifAttribute.getIntValue(mExifByteOrder);
      int j = Math.min(paramHashMap.getIntValue(mExifByteOrder), paramByteOrderedDataInputStream.available() - i);
      int k;
      if ((mMimeType != 4) && (mMimeType != 9) && (mMimeType != 10))
      {
        k = i;
        if (mMimeType == 7) {
          k = i + mOrfMakerNoteOffset;
        }
      }
      else
      {
        k = i + mExifOffset;
      }
      if ((k > 0) && (j > 0))
      {
        mHasThumbnail = true;
        mThumbnailOffset = k;
        mThumbnailLength = j;
        mThumbnailCompression = 6;
        if ((mFilename == null) && (mAssetInputStream == null) && (mSeekableFileDescriptor == null))
        {
          paramHashMap = new byte[j];
          paramByteOrderedDataInputStream.seek(k);
          paramByteOrderedDataInputStream.readFully(paramHashMap);
          mThumbnailBytes = paramHashMap;
        }
      }
    }
  }
  
  private void handleThumbnailFromStrips(ByteOrderedDataInputStream paramByteOrderedDataInputStream, HashMap paramHashMap)
    throws IOException
  {
    Object localObject = (ExifAttribute)paramHashMap.get("StripOffsets");
    paramHashMap = (ExifAttribute)paramHashMap.get("StripByteCounts");
    if ((localObject != null) && (paramHashMap != null))
    {
      long[] arrayOfLong1 = convertToLongArray(((ExifAttribute)localObject).getValue(mExifByteOrder));
      long[] arrayOfLong2 = convertToLongArray(paramHashMap.getValue(mExifByteOrder));
      if (arrayOfLong1 == null)
      {
        Log.w("ExifInterface", "stripOffsets should not be null.");
        return;
      }
      if (arrayOfLong2 == null)
      {
        Log.w("ExifInterface", "stripByteCounts should not be null.");
        return;
      }
      byte[] arrayOfByte = new byte[(int)Arrays.stream(arrayOfLong2).sum()];
      int i = 0;
      int j = 0;
      int k = 0;
      paramHashMap = (HashMap)localObject;
      while (k < arrayOfLong1.length)
      {
        int m = (int)arrayOfLong1[k];
        int n = (int)arrayOfLong2[k];
        m -= j;
        if (m < 0) {
          Log.d("ExifInterface", "Invalid strip offset value");
        }
        paramByteOrderedDataInputStream.seek(m);
        localObject = new byte[n];
        paramByteOrderedDataInputStream.read((byte[])localObject);
        j = j + m + n;
        System.arraycopy((byte[])localObject, 0, arrayOfByte, i, localObject.length);
        i += localObject.length;
        k++;
      }
      mHasThumbnail = true;
      mThumbnailBytes = arrayOfByte;
      mThumbnailLength = arrayOfByte.length;
    }
  }
  
  /* Error */
  private boolean isHeifFormat(byte[] paramArrayOfByte)
    throws IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aload_3
    //   5: astore 4
    //   7: aload_2
    //   8: astore 5
    //   10: new 8	android/media/ExifInterface$ByteOrderedDataInputStream
    //   13: astore 6
    //   15: aload_3
    //   16: astore 4
    //   18: aload_2
    //   19: astore 5
    //   21: aload 6
    //   23: aload_1
    //   24: invokespecial 1231	android/media/ExifInterface$ByteOrderedDataInputStream:<init>	([B)V
    //   27: aload 6
    //   29: astore_3
    //   30: aload_3
    //   31: astore 4
    //   33: aload_3
    //   34: astore 5
    //   36: aload_3
    //   37: getstatic 970	java/nio/ByteOrder:BIG_ENDIAN	Ljava/nio/ByteOrder;
    //   40: invokevirtual 1152	android/media/ExifInterface$ByteOrderedDataInputStream:setByteOrder	(Ljava/nio/ByteOrder;)V
    //   43: aload_3
    //   44: astore 4
    //   46: aload_3
    //   47: astore 5
    //   49: aload_3
    //   50: invokevirtual 1261	android/media/ExifInterface$ByteOrderedDataInputStream:readInt	()I
    //   53: i2l
    //   54: lstore 7
    //   56: aload_3
    //   57: astore 4
    //   59: aload_3
    //   60: astore 5
    //   62: iconst_4
    //   63: newarray byte
    //   65: astore 6
    //   67: aload_3
    //   68: astore 4
    //   70: aload_3
    //   71: astore 5
    //   73: aload_3
    //   74: aload 6
    //   76: invokevirtual 1128	android/media/ExifInterface$ByteOrderedDataInputStream:read	([B)I
    //   79: pop
    //   80: aload_3
    //   81: astore 4
    //   83: aload_3
    //   84: astore 5
    //   86: aload 6
    //   88: getstatic 698	android/media/ExifInterface:HEIF_TYPE_FTYP	[B
    //   91: invokestatic 1130	java/util/Arrays:equals	([B[B)Z
    //   94: istore 9
    //   96: iload 9
    //   98: ifne +9 -> 107
    //   101: aload_3
    //   102: invokevirtual 1388	android/media/ExifInterface$ByteOrderedDataInputStream:close	()V
    //   105: iconst_0
    //   106: ireturn
    //   107: ldc2_w 1235
    //   110: lstore 10
    //   112: lload 7
    //   114: lstore 12
    //   116: lload 7
    //   118: lconst_1
    //   119: lcmp
    //   120: ifne +39 -> 159
    //   123: aload_3
    //   124: astore 4
    //   126: aload_3
    //   127: astore 5
    //   129: aload_3
    //   130: invokevirtual 1391	android/media/ExifInterface$ByteOrderedDataInputStream:readLong	()J
    //   133: lstore 12
    //   135: lload 12
    //   137: ldc2_w 1392
    //   140: lcmp
    //   141: ifge +9 -> 150
    //   144: aload_3
    //   145: invokevirtual 1388	android/media/ExifInterface$ByteOrderedDataInputStream:close	()V
    //   148: iconst_0
    //   149: ireturn
    //   150: ldc2_w 1235
    //   153: ldc2_w 1235
    //   156: ladd
    //   157: lstore 10
    //   159: lload 12
    //   161: lstore 7
    //   163: aload_3
    //   164: astore 4
    //   166: aload_3
    //   167: astore 5
    //   169: lload 12
    //   171: aload_1
    //   172: arraylength
    //   173: i2l
    //   174: lcmp
    //   175: ifle +18 -> 193
    //   178: aload_3
    //   179: astore 4
    //   181: aload_3
    //   182: astore 5
    //   184: aload_1
    //   185: arraylength
    //   186: istore 14
    //   188: iload 14
    //   190: i2l
    //   191: lstore 7
    //   193: lload 7
    //   195: lload 10
    //   197: lsub
    //   198: lstore 10
    //   200: lload 10
    //   202: ldc2_w 1235
    //   205: lcmp
    //   206: ifge +9 -> 215
    //   209: aload_3
    //   210: invokevirtual 1388	android/media/ExifInterface$ByteOrderedDataInputStream:close	()V
    //   213: iconst_0
    //   214: ireturn
    //   215: aload_3
    //   216: astore 4
    //   218: aload_3
    //   219: astore 5
    //   221: iconst_4
    //   222: newarray byte
    //   224: astore_1
    //   225: iconst_0
    //   226: istore 15
    //   228: iconst_0
    //   229: istore 14
    //   231: lconst_0
    //   232: lstore 12
    //   234: aload_3
    //   235: astore 4
    //   237: aload_3
    //   238: astore 5
    //   240: lload 12
    //   242: lload 10
    //   244: ldc2_w 1394
    //   247: ldiv
    //   248: lcmp
    //   249: ifge +151 -> 400
    //   252: aload_3
    //   253: astore 4
    //   255: aload_3
    //   256: astore 5
    //   258: aload_3
    //   259: aload_1
    //   260: invokevirtual 1128	android/media/ExifInterface$ByteOrderedDataInputStream:read	([B)I
    //   263: istore 16
    //   265: aload_3
    //   266: astore 4
    //   268: aload_3
    //   269: astore 5
    //   271: aload_1
    //   272: arraylength
    //   273: istore 17
    //   275: iload 16
    //   277: iload 17
    //   279: if_icmpeq +9 -> 288
    //   282: aload_3
    //   283: invokevirtual 1388	android/media/ExifInterface$ByteOrderedDataInputStream:close	()V
    //   286: iconst_0
    //   287: ireturn
    //   288: lload 12
    //   290: lconst_1
    //   291: lcmp
    //   292: ifne +10 -> 302
    //   295: iload 14
    //   297: istore 17
    //   299: goto +88 -> 387
    //   302: aload_3
    //   303: astore 4
    //   305: aload_3
    //   306: astore 5
    //   308: aload_1
    //   309: getstatic 703	android/media/ExifInterface:HEIF_BRAND_MIF1	[B
    //   312: invokestatic 1130	java/util/Arrays:equals	([B[B)Z
    //   315: ifeq +9 -> 324
    //   318: iconst_1
    //   319: istore 16
    //   321: goto +34 -> 355
    //   324: aload_3
    //   325: astore 4
    //   327: aload_3
    //   328: astore 5
    //   330: aload_1
    //   331: getstatic 708	android/media/ExifInterface:HEIF_BRAND_HEIC	[B
    //   334: invokestatic 1130	java/util/Arrays:equals	([B[B)Z
    //   337: istore 9
    //   339: iload 15
    //   341: istore 16
    //   343: iload 9
    //   345: ifeq +10 -> 355
    //   348: iconst_1
    //   349: istore 14
    //   351: iload 15
    //   353: istore 16
    //   355: iload 16
    //   357: istore 15
    //   359: iload 14
    //   361: istore 17
    //   363: iload 16
    //   365: ifeq +22 -> 387
    //   368: iload 16
    //   370: istore 15
    //   372: iload 14
    //   374: istore 17
    //   376: iload 14
    //   378: ifeq +9 -> 387
    //   381: aload_3
    //   382: invokevirtual 1388	android/media/ExifInterface$ByteOrderedDataInputStream:close	()V
    //   385: iconst_1
    //   386: ireturn
    //   387: lload 12
    //   389: lconst_1
    //   390: ladd
    //   391: lstore 12
    //   393: iload 17
    //   395: istore 14
    //   397: goto -163 -> 234
    //   400: aload_3
    //   401: invokevirtual 1388	android/media/ExifInterface$ByteOrderedDataInputStream:close	()V
    //   404: goto +27 -> 431
    //   407: astore_1
    //   408: aload 4
    //   410: ifnull +8 -> 418
    //   413: aload 4
    //   415: invokevirtual 1388	android/media/ExifInterface$ByteOrderedDataInputStream:close	()V
    //   418: aload_1
    //   419: athrow
    //   420: astore_1
    //   421: aload 5
    //   423: ifnull +8 -> 431
    //   426: aload 5
    //   428: invokevirtual 1388	android/media/ExifInterface$ByteOrderedDataInputStream:close	()V
    //   431: iconst_0
    //   432: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	433	0	this	ExifInterface
    //   0	433	1	paramArrayOfByte	byte[]
    //   1	18	2	localObject1	Object
    //   3	398	3	localObject2	Object
    //   5	409	4	localObject3	Object
    //   8	419	5	localObject4	Object
    //   13	74	6	localObject5	Object
    //   54	140	7	l1	long
    //   94	250	9	bool	boolean
    //   110	133	10	l2	long
    //   114	278	12	l3	long
    //   186	210	14	i	int
    //   226	145	15	j	int
    //   263	106	16	k	int
    //   273	121	17	m	int
    // Exception table:
    //   from	to	target	type
    //   10	15	407	finally
    //   21	27	407	finally
    //   36	43	407	finally
    //   49	56	407	finally
    //   62	67	407	finally
    //   73	80	407	finally
    //   86	96	407	finally
    //   129	135	407	finally
    //   169	178	407	finally
    //   184	188	407	finally
    //   221	225	407	finally
    //   240	252	407	finally
    //   258	265	407	finally
    //   271	275	407	finally
    //   308	318	407	finally
    //   330	339	407	finally
    //   10	15	420	java/lang/Exception
    //   21	27	420	java/lang/Exception
    //   36	43	420	java/lang/Exception
    //   49	56	420	java/lang/Exception
    //   62	67	420	java/lang/Exception
    //   73	80	420	java/lang/Exception
    //   86	96	420	java/lang/Exception
    //   129	135	420	java/lang/Exception
    //   169	178	420	java/lang/Exception
    //   184	188	420	java/lang/Exception
    //   221	225	420	java/lang/Exception
    //   240	252	420	java/lang/Exception
    //   258	265	420	java/lang/Exception
    //   271	275	420	java/lang/Exception
    //   308	318	420	java/lang/Exception
    //   330	339	420	java/lang/Exception
  }
  
  private static boolean isJpegFormat(byte[] paramArrayOfByte)
    throws IOException
  {
    for (int i = 0; i < JPEG_SIGNATURE.length; i++) {
      if (paramArrayOfByte[i] != JPEG_SIGNATURE[i]) {
        return false;
      }
    }
    return true;
  }
  
  private boolean isOrfFormat(byte[] paramArrayOfByte)
    throws IOException
  {
    paramArrayOfByte = new ByteOrderedDataInputStream(paramArrayOfByte);
    mExifByteOrder = readByteOrder(paramArrayOfByte);
    paramArrayOfByte.setByteOrder(mExifByteOrder);
    int i = paramArrayOfByte.readShort();
    return (i == 20306) || (i == 21330);
  }
  
  private boolean isRafFormat(byte[] paramArrayOfByte)
    throws IOException
  {
    byte[] arrayOfByte = "FUJIFILMCCD-RAW".getBytes();
    for (int i = 0; i < arrayOfByte.length; i++) {
      if (paramArrayOfByte[i] != arrayOfByte[i]) {
        return false;
      }
    }
    return true;
  }
  
  private boolean isRw2Format(byte[] paramArrayOfByte)
    throws IOException
  {
    paramArrayOfByte = new ByteOrderedDataInputStream(paramArrayOfByte);
    mExifByteOrder = readByteOrder(paramArrayOfByte);
    paramArrayOfByte.setByteOrder(mExifByteOrder);
    return paramArrayOfByte.readShort() == 85;
  }
  
  private static boolean isSeekableFD(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    try
    {
      Os.lseek(paramFileDescriptor, 0L, OsConstants.SEEK_CUR);
      return true;
    }
    catch (ErrnoException paramFileDescriptor) {}
    return false;
  }
  
  private boolean isSupportedDataType(HashMap paramHashMap)
    throws IOException
  {
    Object localObject = (ExifAttribute)paramHashMap.get("BitsPerSample");
    if (localObject != null)
    {
      localObject = (int[])((ExifAttribute)localObject).getValue(mExifByteOrder);
      if (Arrays.equals(BITS_PER_SAMPLE_RGB, (int[])localObject)) {
        return true;
      }
      if (mMimeType == 3)
      {
        paramHashMap = (ExifAttribute)paramHashMap.get("PhotometricInterpretation");
        if (paramHashMap != null)
        {
          int i = paramHashMap.getIntValue(mExifByteOrder);
          if (((i == 1) && (Arrays.equals((int[])localObject, BITS_PER_SAMPLE_GREYSCALE_2))) || ((i == 6) && (Arrays.equals((int[])localObject, BITS_PER_SAMPLE_RGB)))) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  private boolean isThumbnail(HashMap paramHashMap)
    throws IOException
  {
    ExifAttribute localExifAttribute = (ExifAttribute)paramHashMap.get("ImageLength");
    paramHashMap = (ExifAttribute)paramHashMap.get("ImageWidth");
    if ((localExifAttribute != null) && (paramHashMap != null))
    {
      int i = localExifAttribute.getIntValue(mExifByteOrder);
      int j = paramHashMap.getIntValue(mExifByteOrder);
      if ((i <= 512) && (j <= 512)) {
        return true;
      }
    }
    return false;
  }
  
  /* Error */
  private void loadAttributes(InputStream paramInputStream)
    throws IOException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iload_2
    //   3: getstatic 853	android/media/ExifInterface:EXIF_TAGS	[[Landroid/media/ExifInterface$ExifTag;
    //   6: arraylength
    //   7: if_icmpge +22 -> 29
    //   10: aload_0
    //   11: getfield 960	android/media/ExifInterface:mAttributes	[Ljava/util/HashMap;
    //   14: iload_2
    //   15: new 861	java/util/HashMap
    //   18: dup
    //   19: invokespecial 880	java/util/HashMap:<init>	()V
    //   22: aastore
    //   23: iinc 2 1
    //   26: goto -24 -> 2
    //   29: new 1198	java/io/BufferedInputStream
    //   32: astore_3
    //   33: aload_3
    //   34: aload_1
    //   35: sipush 5000
    //   38: invokespecial 1419	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   41: aload_0
    //   42: aload_0
    //   43: aload_3
    //   44: checkcast 1198	java/io/BufferedInputStream
    //   47: invokespecial 1421	android/media/ExifInterface:getMimeType	(Ljava/io/BufferedInputStream;)I
    //   50: putfield 1279	android/media/ExifInterface:mMimeType	I
    //   53: new 8	android/media/ExifInterface$ByteOrderedDataInputStream
    //   56: astore_1
    //   57: aload_1
    //   58: aload_3
    //   59: invokespecial 1423	android/media/ExifInterface$ByteOrderedDataInputStream:<init>	(Ljava/io/InputStream;)V
    //   62: aload_0
    //   63: getfield 1279	android/media/ExifInterface:mMimeType	I
    //   66: tableswitch	default:+66->132, 0:+111->177, 1:+111->177, 2:+111->177, 3:+111->177, 4:+101->167, 5:+111->177, 6:+111->177, 7:+93->159, 8:+111->177, 9:+85->151, 10:+77->143, 11:+111->177, 12:+69->135
    //   132: goto +50 -> 182
    //   135: aload_0
    //   136: aload_1
    //   137: invokespecial 1425	android/media/ExifInterface:getHeifAttributes	(Landroid/media/ExifInterface$ByteOrderedDataInputStream;)V
    //   140: goto +42 -> 182
    //   143: aload_0
    //   144: aload_1
    //   145: invokespecial 1427	android/media/ExifInterface:getRw2Attributes	(Landroid/media/ExifInterface$ByteOrderedDataInputStream;)V
    //   148: goto +34 -> 182
    //   151: aload_0
    //   152: aload_1
    //   153: invokespecial 1429	android/media/ExifInterface:getRafAttributes	(Landroid/media/ExifInterface$ByteOrderedDataInputStream;)V
    //   156: goto +26 -> 182
    //   159: aload_0
    //   160: aload_1
    //   161: invokespecial 1431	android/media/ExifInterface:getOrfAttributes	(Landroid/media/ExifInterface$ByteOrderedDataInputStream;)V
    //   164: goto +18 -> 182
    //   167: aload_0
    //   168: aload_1
    //   169: iconst_0
    //   170: iconst_0
    //   171: invokespecial 1258	android/media/ExifInterface:getJpegAttributes	(Landroid/media/ExifInterface$ByteOrderedDataInputStream;II)V
    //   174: goto +8 -> 182
    //   177: aload_0
    //   178: aload_1
    //   179: invokespecial 1225	android/media/ExifInterface:getRawAttributes	(Landroid/media/ExifInterface$ByteOrderedDataInputStream;)V
    //   182: aload_0
    //   183: aload_1
    //   184: invokespecial 1434	android/media/ExifInterface:setThumbnailData	(Landroid/media/ExifInterface$ByteOrderedDataInputStream;)V
    //   187: aload_0
    //   188: iconst_1
    //   189: putfield 1436	android/media/ExifInterface:mIsSupportedFile	Z
    //   192: goto +13 -> 205
    //   195: astore_1
    //   196: goto +14 -> 210
    //   199: astore_1
    //   200: aload_0
    //   201: iconst_0
    //   202: putfield 1436	android/media/ExifInterface:mIsSupportedFile	Z
    //   205: aload_0
    //   206: invokespecial 1438	android/media/ExifInterface:addDefaultValuesForCompatibility	()V
    //   209: return
    //   210: aload_0
    //   211: invokespecial 1438	android/media/ExifInterface:addDefaultValuesForCompatibility	()V
    //   214: aload_1
    //   215: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	216	0	this	ExifInterface
    //   0	216	1	paramInputStream	InputStream
    //   1	23	2	i	int
    //   32	27	3	localBufferedInputStream	BufferedInputStream
    // Exception table:
    //   from	to	target	type
    //   2	23	195	finally
    //   29	132	195	finally
    //   135	140	195	finally
    //   143	148	195	finally
    //   151	156	195	finally
    //   159	164	195	finally
    //   167	174	195	finally
    //   177	182	195	finally
    //   182	192	195	finally
    //   200	205	195	finally
    //   2	23	199	java/io/IOException
    //   29	132	199	java/io/IOException
    //   135	140	199	java/io/IOException
    //   143	148	199	java/io/IOException
    //   151	156	199	java/io/IOException
    //   159	164	199	java/io/IOException
    //   167	174	199	java/io/IOException
    //   177	182	199	java/io/IOException
    //   182	192	199	java/io/IOException
  }
  
  private void parseTiffHeaders(ByteOrderedDataInputStream paramByteOrderedDataInputStream, int paramInt)
    throws IOException
  {
    mExifByteOrder = readByteOrder(paramByteOrderedDataInputStream);
    paramByteOrderedDataInputStream.setByteOrder(mExifByteOrder);
    int i = paramByteOrderedDataInputStream.readUnsignedShort();
    if ((mMimeType != 7) && (mMimeType != 10) && (i != 42))
    {
      paramByteOrderedDataInputStream = new StringBuilder();
      paramByteOrderedDataInputStream.append("Invalid start code: ");
      paramByteOrderedDataInputStream.append(Integer.toHexString(i));
      throw new IOException(paramByteOrderedDataInputStream.toString());
    }
    i = paramByteOrderedDataInputStream.readInt();
    if ((i >= 8) && (i < paramInt))
    {
      paramInt = i - 8;
      if ((paramInt > 0) && (paramByteOrderedDataInputStream.skipBytes(paramInt) != paramInt))
      {
        paramByteOrderedDataInputStream = new StringBuilder();
        paramByteOrderedDataInputStream.append("Couldn't jump to first Ifd: ");
        paramByteOrderedDataInputStream.append(paramInt);
        throw new IOException(paramByteOrderedDataInputStream.toString());
      }
      return;
    }
    paramByteOrderedDataInputStream = new StringBuilder();
    paramByteOrderedDataInputStream.append("Invalid first Ifd offset: ");
    paramByteOrderedDataInputStream.append(i);
    throw new IOException(paramByteOrderedDataInputStream.toString());
  }
  
  private void printAttributes()
  {
    for (int i = 0; i < mAttributes.length; i++)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("The size of tag group[");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append("]: ");
      ((StringBuilder)localObject).append(mAttributes[i].size());
      Log.d("ExifInterface", ((StringBuilder)localObject).toString());
      Iterator localIterator = mAttributes[i].entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        localObject = (ExifAttribute)localEntry.getValue();
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("tagName: ");
        localStringBuilder.append(localEntry.getKey());
        localStringBuilder.append(", tagType: ");
        localStringBuilder.append(((ExifAttribute)localObject).toString());
        localStringBuilder.append(", tagValue: '");
        localStringBuilder.append(((ExifAttribute)localObject).getStringValue(mExifByteOrder));
        localStringBuilder.append("'");
        Log.d("ExifInterface", localStringBuilder.toString());
      }
    }
  }
  
  private ByteOrder readByteOrder(ByteOrderedDataInputStream paramByteOrderedDataInputStream)
    throws IOException
  {
    int i = paramByteOrderedDataInputStream.readShort();
    if (i != 18761)
    {
      if (i == 19789) {
        return ByteOrder.BIG_ENDIAN;
      }
      paramByteOrderedDataInputStream = new StringBuilder();
      paramByteOrderedDataInputStream.append("Invalid byte order: ");
      paramByteOrderedDataInputStream.append(Integer.toHexString(i));
      throw new IOException(paramByteOrderedDataInputStream.toString());
    }
    return ByteOrder.LITTLE_ENDIAN;
  }
  
  private void readExifSegment(byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    ByteOrderedDataInputStream localByteOrderedDataInputStream = new ByteOrderedDataInputStream(paramArrayOfByte);
    parseTiffHeaders(localByteOrderedDataInputStream, paramArrayOfByte.length);
    readImageFileDirectory(localByteOrderedDataInputStream, paramInt);
  }
  
  private void readImageFileDirectory(ByteOrderedDataInputStream paramByteOrderedDataInputStream, int paramInt)
    throws IOException
  {
    if (mPosition + 2 > mLength) {
      return;
    }
    int i = paramByteOrderedDataInputStream.readShort();
    if ((mPosition + 12 * i <= mLength) && (i > 0))
    {
      for (int j = 0;; j = (short)(j + 1))
      {
        int k = paramInt;
        if (j >= i) {
          break;
        }
        int m = paramByteOrderedDataInputStream.readUnsignedShort();
        int n = paramByteOrderedDataInputStream.readUnsignedShort();
        int i1 = paramByteOrderedDataInputStream.readInt();
        long l1 = paramByteOrderedDataInputStream.peek() + 4;
        Object localObject1 = (ExifTag)sExifTagMapsForReading[k].get(Integer.valueOf(m));
        int i2 = 0;
        Object localObject2;
        if (localObject1 == null)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Skip the tag entry since tag number is not defined: ");
          ((StringBuilder)localObject2).append(m);
          Log.w("ExifInterface", ((StringBuilder)localObject2).toString());
        }
        else
        {
          if ((n > 0) && (n < IFD_FORMAT_BYTES_PER_FORMAT.length))
          {
            l2 = i1;
            l2 = IFD_FORMAT_BYTES_PER_FORMAT[n] * l2;
            if ((l2 >= 0L) && (l2 <= 2147483647L))
            {
              i2 = 1;
              break label287;
            }
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Skip the tag entry since the number of components is invalid: ");
            ((StringBuilder)localObject2).append(i1);
            Log.w("ExifInterface", ((StringBuilder)localObject2).toString());
            break label287;
          }
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Skip the tag entry since data format is invalid: ");
          ((StringBuilder)localObject2).append(n);
          Log.w("ExifInterface", ((StringBuilder)localObject2).toString());
        }
        long l2 = 0L;
        label287:
        if (i2 == 0)
        {
          paramByteOrderedDataInputStream.seek(l1);
        }
        else
        {
          if (l2 > 4L)
          {
            i2 = paramByteOrderedDataInputStream.readInt();
            if (mMimeType == 7)
            {
              if (name == "MakerNote")
              {
                mOrfMakerNoteOffset = i2;
              }
              else if ((k == 6) && (name == "ThumbnailImage"))
              {
                mOrfThumbnailOffset = i2;
                mOrfThumbnailLength = i1;
                localObject2 = ExifAttribute.createUShort(6, mExifByteOrder);
                ExifAttribute localExifAttribute1 = ExifAttribute.createULong(mOrfThumbnailOffset, mExifByteOrder);
                ExifAttribute localExifAttribute2 = ExifAttribute.createULong(mOrfThumbnailLength, mExifByteOrder);
                mAttributes[4].put("Compression", localObject2);
                mAttributes[4].put("JPEGInterchangeFormat", localExifAttribute1);
                mAttributes[4].put("JPEGInterchangeFormatLength", localExifAttribute2);
              }
            }
            else if ((mMimeType == 10) && (name == "JpgFromRaw")) {
              mRw2JpgFromRawOffset = i2;
            }
            if (i2 + l2 <= mLength)
            {
              paramByteOrderedDataInputStream.seek(i2);
            }
            else
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Skip the tag entry since data offset is invalid: ");
              ((StringBuilder)localObject1).append(i2);
              Log.w("ExifInterface", ((StringBuilder)localObject1).toString());
              paramByteOrderedDataInputStream.seek(l1);
              continue;
            }
          }
          localObject2 = (Integer)sExifPointerTagMap.get(Integer.valueOf(m));
          if (localObject2 != null)
          {
            l2 = -1L;
            switch (n)
            {
            default: 
              break;
            case 9: 
            case 13: 
              l2 = paramByteOrderedDataInputStream.readInt();
              break;
            case 8: 
              l2 = paramByteOrderedDataInputStream.readShort();
              break;
            case 4: 
              l2 = paramByteOrderedDataInputStream.readUnsignedInt();
              break;
            case 3: 
              l2 = paramByteOrderedDataInputStream.readUnsignedShort();
            }
            if ((l2 > 0L) && (l2 < mLength))
            {
              if (!mAttributesOffsets.contains(Integer.valueOf((int)l2)))
              {
                mAttributesOffsets.add(Integer.valueOf(mPosition));
                paramByteOrderedDataInputStream.seek(l2);
                readImageFileDirectory(paramByteOrderedDataInputStream, ((Integer)localObject2).intValue());
              }
              else
              {
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("Skip jump into the IFD since it has already been read: IfdType ");
                ((StringBuilder)localObject1).append(localObject2);
                ((StringBuilder)localObject1).append(" (at ");
                ((StringBuilder)localObject1).append(l2);
                ((StringBuilder)localObject1).append(")");
                Log.w("ExifInterface", ((StringBuilder)localObject1).toString());
              }
            }
            else
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Skip jump into the IFD since its offset is invalid: ");
              ((StringBuilder)localObject1).append(l2);
              Log.w("ExifInterface", ((StringBuilder)localObject1).toString());
            }
            paramByteOrderedDataInputStream.seek(l1);
          }
          else
          {
            localObject2 = new byte[(int)l2];
            paramByteOrderedDataInputStream.readFully((byte[])localObject2);
            localObject2 = new ExifAttribute(n, i1, (byte[])localObject2, null);
            mAttributes[k].put(name, localObject2);
            if (name == "DNGVersion") {
              mMimeType = 3;
            }
            if (((name != "Make") && (name != "Model")) || ((((ExifAttribute)localObject2).getStringValue(mExifByteOrder).contains("PENTAX")) || ((name == "Compression") && (((ExifAttribute)localObject2).getIntValue(mExifByteOrder) == 65535)))) {
              mMimeType = 8;
            }
            if (paramByteOrderedDataInputStream.peek() != l1) {
              paramByteOrderedDataInputStream.seek(l1);
            }
          }
        }
      }
      if (paramByteOrderedDataInputStream.peek() + 4 <= mLength)
      {
        paramInt = paramByteOrderedDataInputStream.readInt();
        if ((paramInt > 0L) && (paramInt < mLength))
        {
          if (!mAttributesOffsets.contains(Integer.valueOf(paramInt)))
          {
            mAttributesOffsets.add(Integer.valueOf(mPosition));
            paramByteOrderedDataInputStream.seek(paramInt);
            if (mAttributes[4].isEmpty()) {
              readImageFileDirectory(paramByteOrderedDataInputStream, 4);
            } else if (mAttributes[5].isEmpty()) {
              readImageFileDirectory(paramByteOrderedDataInputStream, 5);
            }
          }
          else
          {
            paramByteOrderedDataInputStream = new StringBuilder();
            paramByteOrderedDataInputStream.append("Stop reading file since re-reading an IFD may cause an infinite loop: ");
            paramByteOrderedDataInputStream.append(paramInt);
            Log.w("ExifInterface", paramByteOrderedDataInputStream.toString());
          }
        }
        else
        {
          paramByteOrderedDataInputStream = new StringBuilder();
          paramByteOrderedDataInputStream.append("Stop reading file since a wrong offset may cause an infinite loop: ");
          paramByteOrderedDataInputStream.append(paramInt);
          Log.w("ExifInterface", paramByteOrderedDataInputStream.toString());
        }
      }
      return;
    }
  }
  
  private void removeAttribute(String paramString)
  {
    for (int i = 0; i < EXIF_TAGS.length; i++) {
      mAttributes[i].remove(paramString);
    }
  }
  
  private void retrieveJpegImageSize(ByteOrderedDataInputStream paramByteOrderedDataInputStream, int paramInt)
    throws IOException
  {
    ExifAttribute localExifAttribute1 = (ExifAttribute)mAttributes[paramInt].get("ImageLength");
    ExifAttribute localExifAttribute2 = (ExifAttribute)mAttributes[paramInt].get("ImageWidth");
    if ((localExifAttribute1 == null) || (localExifAttribute2 == null))
    {
      localExifAttribute2 = (ExifAttribute)mAttributes[paramInt].get("JPEGInterchangeFormat");
      if (localExifAttribute2 != null) {
        getJpegAttributes(paramByteOrderedDataInputStream, localExifAttribute2.getIntValue(mExifByteOrder), paramInt);
      }
    }
  }
  
  private void saveJpegAttributes(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    paramInputStream = new DataInputStream(paramInputStream);
    paramOutputStream = new ByteOrderedDataOutputStream(paramOutputStream, ByteOrder.BIG_ENDIAN);
    if (paramInputStream.readByte() == -1)
    {
      paramOutputStream.writeByte(-1);
      if (paramInputStream.readByte() == -40)
      {
        paramOutputStream.writeByte(-40);
        paramOutputStream.writeByte(-1);
        paramOutputStream.writeByte(-31);
        writeExifSegment(paramOutputStream, 6);
        byte[] arrayOfByte1 = new byte['က'];
        while (paramInputStream.readByte() == -1)
        {
          int i = paramInputStream.readByte();
          if (i != -31)
          {
            switch (i)
            {
            default: 
              paramOutputStream.writeByte(-1);
              paramOutputStream.writeByte(i);
              i = paramInputStream.readUnsignedShort();
              paramOutputStream.writeUnsignedShort(i);
              i -= 2;
              if (i >= 0) {
                while (i > 0)
                {
                  j = paramInputStream.read(arrayOfByte1, 0, Math.min(i, arrayOfByte1.length));
                  if (j < 0) {
                    break;
                  }
                  paramOutputStream.write(arrayOfByte1, 0, j);
                  i -= j;
                }
              }
              throw new IOException("Invalid length");
            }
            paramOutputStream.writeByte(-1);
            paramOutputStream.writeByte(i);
            Streams.copy(paramInputStream, paramOutputStream);
            return;
          }
          int j = paramInputStream.readUnsignedShort() - 2;
          if (j >= 0)
          {
            byte[] arrayOfByte2 = new byte[6];
            if (j >= 6) {
              if (paramInputStream.read(arrayOfByte2) == 6)
              {
                if (Arrays.equals(arrayOfByte2, IDENTIFIER_EXIF_APP1))
                {
                  if (paramInputStream.skipBytes(j - 6) == j - 6) {
                    break label399;
                  }
                  throw new IOException("Invalid length");
                }
              }
              else {
                throw new IOException("Invalid exif");
              }
            }
            paramOutputStream.writeByte(-1);
            paramOutputStream.writeByte(i);
            paramOutputStream.writeUnsignedShort(j + 2);
            i = j;
            if (j >= 6)
            {
              i = j - 6;
              paramOutputStream.write(arrayOfByte2);
            }
            while (i > 0)
            {
              j = paramInputStream.read(arrayOfByte1, 0, Math.min(i, arrayOfByte1.length));
              if (j < 0) {
                break;
              }
              paramOutputStream.write(arrayOfByte1, 0, j);
              i -= j;
            }
          }
          else
          {
            label399:
            throw new IOException("Invalid length");
          }
        }
        throw new IOException("Invalid marker");
      }
      throw new IOException("Invalid marker");
    }
    throw new IOException("Invalid marker");
  }
  
  private void setThumbnailData(ByteOrderedDataInputStream paramByteOrderedDataInputStream)
    throws IOException
  {
    HashMap localHashMap = mAttributes[4];
    ExifAttribute localExifAttribute = (ExifAttribute)localHashMap.get("Compression");
    if (localExifAttribute != null)
    {
      mThumbnailCompression = localExifAttribute.getIntValue(mExifByteOrder);
      int i = mThumbnailCompression;
      if (i != 1) {
        switch (i)
        {
        default: 
          break;
        case 6: 
          handleThumbnailFromJfif(paramByteOrderedDataInputStream, localHashMap);
          break;
        }
      } else if (isSupportedDataType(localHashMap)) {
        handleThumbnailFromStrips(paramByteOrderedDataInputStream, localHashMap);
      }
    }
    else
    {
      handleThumbnailFromJfif(paramByteOrderedDataInputStream, localHashMap);
    }
  }
  
  private void swapBasedOnImageSize(int paramInt1, int paramInt2)
    throws IOException
  {
    if ((!mAttributes[paramInt1].isEmpty()) && (!mAttributes[paramInt2].isEmpty()))
    {
      Object localObject = (ExifAttribute)mAttributes[paramInt1].get("ImageLength");
      ExifAttribute localExifAttribute1 = (ExifAttribute)mAttributes[paramInt1].get("ImageWidth");
      ExifAttribute localExifAttribute2 = (ExifAttribute)mAttributes[paramInt2].get("ImageLength");
      ExifAttribute localExifAttribute3 = (ExifAttribute)mAttributes[paramInt2].get("ImageWidth");
      if ((localObject != null) && (localExifAttribute1 != null) && (localExifAttribute2 != null) && (localExifAttribute3 != null))
      {
        int i = ((ExifAttribute)localObject).getIntValue(mExifByteOrder);
        int j = localExifAttribute1.getIntValue(mExifByteOrder);
        int k = localExifAttribute2.getIntValue(mExifByteOrder);
        int m = localExifAttribute3.getIntValue(mExifByteOrder);
        if ((i < k) && (j < m))
        {
          localObject = mAttributes[paramInt1];
          mAttributes[paramInt1] = mAttributes[paramInt2];
          mAttributes[paramInt2] = localObject;
        }
      }
      return;
    }
  }
  
  private boolean updateAttribute(String paramString, ExifAttribute paramExifAttribute)
  {
    boolean bool = false;
    for (int i = 0; i < EXIF_TAGS.length; i++) {
      if (mAttributes[i].containsKey(paramString))
      {
        mAttributes[i].put(paramString, paramExifAttribute);
        bool = true;
      }
    }
    return bool;
  }
  
  private void updateImageSizeValues(ByteOrderedDataInputStream paramByteOrderedDataInputStream, int paramInt)
    throws IOException
  {
    ExifAttribute localExifAttribute1 = (ExifAttribute)mAttributes[paramInt].get("DefaultCropSize");
    ExifAttribute localExifAttribute2 = (ExifAttribute)mAttributes[paramInt].get("SensorTopBorder");
    ExifAttribute localExifAttribute3 = (ExifAttribute)mAttributes[paramInt].get("SensorLeftBorder");
    ExifAttribute localExifAttribute4 = (ExifAttribute)mAttributes[paramInt].get("SensorBottomBorder");
    Object localObject = (ExifAttribute)mAttributes[paramInt].get("SensorRightBorder");
    if (localExifAttribute1 != null)
    {
      if (format == 5)
      {
        localObject = (Rational[])localExifAttribute1.getValue(mExifByteOrder);
        paramByteOrderedDataInputStream = ExifAttribute.createURational(localObject[0], mExifByteOrder);
        localObject = ExifAttribute.createURational(localObject[1], mExifByteOrder);
      }
      else
      {
        localObject = (int[])localExifAttribute1.getValue(mExifByteOrder);
        paramByteOrderedDataInputStream = ExifAttribute.createUShort(localObject[0], mExifByteOrder);
        localObject = ExifAttribute.createUShort(localObject[1], mExifByteOrder);
      }
      mAttributes[paramInt].put("ImageWidth", paramByteOrderedDataInputStream);
      mAttributes[paramInt].put("ImageLength", localObject);
    }
    else if ((localExifAttribute2 != null) && (localExifAttribute3 != null) && (localExifAttribute4 != null) && (localObject != null))
    {
      int i = localExifAttribute2.getIntValue(mExifByteOrder);
      int j = localExifAttribute4.getIntValue(mExifByteOrder);
      int k = ((ExifAttribute)localObject).getIntValue(mExifByteOrder);
      int m = localExifAttribute3.getIntValue(mExifByteOrder);
      if ((j > i) && (k > m))
      {
        localObject = ExifAttribute.createUShort(j - i, mExifByteOrder);
        paramByteOrderedDataInputStream = ExifAttribute.createUShort(k - m, mExifByteOrder);
        mAttributes[paramInt].put("ImageLength", localObject);
        mAttributes[paramInt].put("ImageWidth", paramByteOrderedDataInputStream);
      }
    }
    else
    {
      retrieveJpegImageSize(paramByteOrderedDataInputStream, paramInt);
    }
  }
  
  private void validateImages(InputStream paramInputStream)
    throws IOException
  {
    swapBasedOnImageSize(0, 5);
    swapBasedOnImageSize(0, 4);
    swapBasedOnImageSize(5, 4);
    paramInputStream = (ExifAttribute)mAttributes[1].get("PixelXDimension");
    ExifAttribute localExifAttribute = (ExifAttribute)mAttributes[1].get("PixelYDimension");
    if ((paramInputStream != null) && (localExifAttribute != null))
    {
      mAttributes[0].put("ImageWidth", paramInputStream);
      mAttributes[0].put("ImageLength", localExifAttribute);
    }
    if ((mAttributes[4].isEmpty()) && (isThumbnail(mAttributes[5])))
    {
      mAttributes[4] = mAttributes[5];
      mAttributes[5] = new HashMap();
    }
    if (!isThumbnail(mAttributes[4])) {
      Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image.");
    }
  }
  
  private int writeExifSegment(ByteOrderedDataOutputStream paramByteOrderedDataOutputStream, int paramInt)
    throws IOException
  {
    int[] arrayOfInt = new int[EXIF_TAGS.length];
    Object localObject1 = new int[EXIF_TAGS.length];
    Object localObject2 = EXIF_POINTER_TAGS;
    int i = localObject2.length;
    for (int j = 0; j < i; j++) {
      removeAttribute(name);
    }
    removeAttribute(JPEG_INTERCHANGE_FORMAT_TAGname);
    removeAttribute(JPEG_INTERCHANGE_FORMAT_LENGTH_TAGname);
    for (j = 0; j < EXIF_TAGS.length; j++)
    {
      Object[] arrayOfObject = mAttributes[j].entrySet().toArray();
      k = arrayOfObject.length;
      for (i = 0; i < k; i++)
      {
        localObject2 = (Map.Entry)arrayOfObject[i];
        if (((Map.Entry)localObject2).getValue() == null) {
          mAttributes[j].remove(((Map.Entry)localObject2).getKey());
        }
      }
    }
    if (!mAttributes[1].isEmpty()) {
      mAttributes[0].put(EXIF_POINTER_TAGS1name, ExifAttribute.createULong(0L, mExifByteOrder));
    }
    if (!mAttributes[2].isEmpty()) {
      mAttributes[0].put(EXIF_POINTER_TAGS2name, ExifAttribute.createULong(0L, mExifByteOrder));
    }
    if (!mAttributes[3].isEmpty()) {
      mAttributes[1].put(EXIF_POINTER_TAGS3name, ExifAttribute.createULong(0L, mExifByteOrder));
    }
    if (mHasThumbnail)
    {
      mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAGname, ExifAttribute.createULong(0L, mExifByteOrder));
      mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_LENGTH_TAGname, ExifAttribute.createULong(mThumbnailLength, mExifByteOrder));
    }
    int m;
    for (j = 0; j < EXIF_TAGS.length; j++)
    {
      k = 0;
      localObject2 = mAttributes[j].entrySet().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        m = ((ExifAttribute)((Map.Entry)((Iterator)localObject2).next()).getValue()).size();
        i = k;
        if (m > 4) {
          i = k + m;
        }
        k = i;
      }
      localObject1[j] += k;
    }
    i = 8;
    int k = 0;
    while (k < EXIF_TAGS.length)
    {
      j = i;
      if (!mAttributes[k].isEmpty())
      {
        arrayOfInt[k] = i;
        j = i + (mAttributes[k].size() * 12 + 2 + 4 + localObject1[k]);
      }
      k++;
      i = j;
    }
    j = i;
    if (mHasThumbnail)
    {
      mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAGname, ExifAttribute.createULong(i, mExifByteOrder));
      mThumbnailOffset = (paramInt + i);
      j = i + mThumbnailLength;
    }
    int n = j + 8;
    if (!mAttributes[1].isEmpty()) {
      mAttributes[0].put(EXIF_POINTER_TAGS1name, ExifAttribute.createULong(arrayOfInt[1], mExifByteOrder));
    }
    if (!mAttributes[2].isEmpty()) {
      mAttributes[0].put(EXIF_POINTER_TAGS2name, ExifAttribute.createULong(arrayOfInt[2], mExifByteOrder));
    }
    if (!mAttributes[3].isEmpty()) {
      mAttributes[1].put(EXIF_POINTER_TAGS3name, ExifAttribute.createULong(arrayOfInt[3], mExifByteOrder));
    }
    paramByteOrderedDataOutputStream.writeUnsignedShort(n);
    paramByteOrderedDataOutputStream.write(IDENTIFIER_EXIF_APP1);
    int i1;
    if (mExifByteOrder == ByteOrder.BIG_ENDIAN)
    {
      paramInt = 19789;
      i1 = paramInt;
    }
    else
    {
      paramInt = 18761;
      i1 = paramInt;
    }
    paramByteOrderedDataOutputStream.writeShort(i1);
    paramByteOrderedDataOutputStream.setByteOrder(mExifByteOrder);
    paramByteOrderedDataOutputStream.writeUnsignedShort(42);
    paramByteOrderedDataOutputStream.writeUnsignedInt(8L);
    i = 0;
    paramInt = j;
    for (j = i; j < EXIF_TAGS.length; j++) {
      if (!mAttributes[j].isEmpty())
      {
        paramByteOrderedDataOutputStream.writeUnsignedShort(mAttributes[j].size());
        i = arrayOfInt[j] + 2 + mAttributes[j].size() * 12 + 4;
        localObject1 = mAttributes[j].entrySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject2 = (Map.Entry)((Iterator)localObject1).next();
          k = sExifTagMapsForWritinggetgetKeynumber;
          localObject2 = (ExifAttribute)((Map.Entry)localObject2).getValue();
          m = ((ExifAttribute)localObject2).size();
          paramByteOrderedDataOutputStream.writeUnsignedShort(k);
          paramByteOrderedDataOutputStream.writeUnsignedShort(format);
          paramByteOrderedDataOutputStream.writeInt(numberOfComponents);
          if (m > 4)
          {
            paramByteOrderedDataOutputStream.writeUnsignedInt(i);
            k = i + m;
          }
          else
          {
            paramByteOrderedDataOutputStream.write(bytes);
            k = i;
            if (m < 4) {
              for (;;)
              {
                k = i;
                if (m >= 4) {
                  break;
                }
                paramByteOrderedDataOutputStream.writeByte(0);
                m++;
              }
            }
          }
          i = k;
        }
        if ((j == 0) && (!mAttributes[4].isEmpty())) {
          paramByteOrderedDataOutputStream.writeUnsignedInt(arrayOfInt[4]);
        } else {
          paramByteOrderedDataOutputStream.writeUnsignedInt(0L);
        }
        localObject1 = mAttributes[j].entrySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject2 = (ExifAttribute)((Map.Entry)((Iterator)localObject1).next()).getValue();
          if (bytes.length > 4) {
            paramByteOrderedDataOutputStream.write(bytes, 0, bytes.length);
          }
        }
      }
    }
    if (mHasThumbnail) {
      paramByteOrderedDataOutputStream.write(getThumbnailBytes());
    }
    paramByteOrderedDataOutputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
    return n;
  }
  
  public double getAltitude(double paramDouble)
  {
    double d = getAttributeDouble("GPSAltitude", -1.0D);
    int i = -1;
    int j = getAttributeInt("GPSAltitudeRef", -1);
    if ((d >= 0.0D) && (j >= 0))
    {
      if (j != 1) {
        i = 1;
      }
      return i * d;
    }
    return paramDouble;
  }
  
  public String getAttribute(String paramString)
  {
    ExifAttribute localExifAttribute = getExifAttribute(paramString);
    if (localExifAttribute != null)
    {
      if (!sTagSetForCompatibility.contains(paramString)) {
        return localExifAttribute.getStringValue(mExifByteOrder);
      }
      if (paramString.equals("GPSTimeStamp"))
      {
        if ((format != 5) && (format != 10)) {
          return null;
        }
        paramString = (Rational[])localExifAttribute.getValue(mExifByteOrder);
        if (paramString.length != 3) {
          return null;
        }
        return String.format("%02d:%02d:%02d", new Object[] { Integer.valueOf((int)((float)0numerator / (float)0denominator)), Integer.valueOf((int)((float)1numerator / (float)1denominator)), Integer.valueOf((int)((float)2numerator / (float)2denominator)) });
      }
      try
      {
        paramString = Double.toString(localExifAttribute.getDoubleValue(mExifByteOrder));
        return paramString;
      }
      catch (NumberFormatException paramString)
      {
        return null;
      }
    }
    return null;
  }
  
  public double getAttributeDouble(String paramString, double paramDouble)
  {
    paramString = getExifAttribute(paramString);
    if (paramString == null) {
      return paramDouble;
    }
    try
    {
      double d = paramString.getDoubleValue(mExifByteOrder);
      return d;
    }
    catch (NumberFormatException paramString) {}
    return paramDouble;
  }
  
  public int getAttributeInt(String paramString, int paramInt)
  {
    paramString = getExifAttribute(paramString);
    if (paramString == null) {
      return paramInt;
    }
    try
    {
      int i = paramString.getIntValue(mExifByteOrder);
      return i;
    }
    catch (NumberFormatException paramString) {}
    return paramInt;
  }
  
  public long getDateTime()
  {
    Object localObject = getAttribute("DateTime");
    if ((localObject != null) && (sNonZeroTimePattern.matcher((CharSequence)localObject).matches()))
    {
      ParsePosition localParsePosition = new ParsePosition(0);
      try
      {
        localObject = sFormatter.parse((String)localObject, localParsePosition);
        if (localObject == null) {
          return -1L;
        }
        long l1 = ((Date)localObject).getTime();
        localObject = getAttribute("SubSecTime");
        long l2 = l1;
        if (localObject != null) {
          try
          {
            for (l2 = Long.parseLong((String)localObject); l2 > 1000L; l2 /= 10L) {}
            l2 = l1 + l2;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            l2 = l1;
          }
        }
        return l2;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        return -1L;
      }
    }
    return -1L;
  }
  
  public long getGpsDateTime()
  {
    String str = getAttribute("GPSDateStamp");
    Object localObject1 = getAttribute("GPSTimeStamp");
    if ((str != null) && (localObject1 != null) && ((sNonZeroTimePattern.matcher(str).matches()) || (sNonZeroTimePattern.matcher((CharSequence)localObject1).matches())))
    {
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(str);
      ((StringBuilder)localObject2).append(' ');
      ((StringBuilder)localObject2).append((String)localObject1);
      localObject2 = ((StringBuilder)localObject2).toString();
      localObject1 = new ParsePosition(0);
      try
      {
        localObject1 = sFormatter.parse((String)localObject2, (ParsePosition)localObject1);
        if (localObject1 == null) {
          return -1L;
        }
        long l = ((Date)localObject1).getTime();
        return l;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        return -1L;
      }
    }
    return -1L;
  }
  
  public boolean getLatLong(float[] paramArrayOfFloat)
  {
    String str1 = getAttribute("GPSLatitude");
    String str2 = getAttribute("GPSLatitudeRef");
    String str3 = getAttribute("GPSLongitude");
    String str4 = getAttribute("GPSLongitudeRef");
    if ((str1 != null) && (str2 != null) && (str3 != null) && (str4 != null)) {
      try
      {
        paramArrayOfFloat[0] = convertRationalLatLonToFloat(str1, str2);
        paramArrayOfFloat[1] = convertRationalLatLonToFloat(str3, str4);
        return true;
      }
      catch (IllegalArgumentException paramArrayOfFloat) {}
    }
    return false;
  }
  
  public byte[] getThumbnail()
  {
    if ((mThumbnailCompression != 6) && (mThumbnailCompression != 7)) {
      return null;
    }
    return getThumbnailBytes();
  }
  
  public Bitmap getThumbnailBitmap()
  {
    if (!mHasThumbnail) {
      return null;
    }
    if (mThumbnailBytes == null) {
      mThumbnailBytes = getThumbnailBytes();
    }
    int i = mThumbnailCompression;
    int j = 0;
    if ((i != 6) && (mThumbnailCompression != 7))
    {
      if (mThumbnailCompression == 1)
      {
        int[] arrayOfInt = new int[mThumbnailBytes.length / 3];
        while (j < arrayOfInt.length)
        {
          arrayOfInt[j] = ((mThumbnailBytes[(3 * j)] << 16) + 0 + (mThumbnailBytes[(3 * j + 1)] << 8) + mThumbnailBytes[(3 * j + 2)]);
          j++;
        }
        ExifAttribute localExifAttribute1 = (ExifAttribute)mAttributes[4].get("ImageLength");
        ExifAttribute localExifAttribute2 = (ExifAttribute)mAttributes[4].get("ImageWidth");
        if ((localExifAttribute1 != null) && (localExifAttribute2 != null))
        {
          j = localExifAttribute1.getIntValue(mExifByteOrder);
          return Bitmap.createBitmap(arrayOfInt, localExifAttribute2.getIntValue(mExifByteOrder), j, Bitmap.Config.ARGB_8888);
        }
      }
      return null;
    }
    return BitmapFactory.decodeByteArray(mThumbnailBytes, 0, mThumbnailLength);
  }
  
  /* Error */
  public byte[] getThumbnailBytes()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 1342	android/media/ExifInterface:mHasThumbnail	Z
    //   4: ifne +5 -> 9
    //   7: aconst_null
    //   8: areturn
    //   9: aload_0
    //   10: getfield 1350	android/media/ExifInterface:mThumbnailBytes	[B
    //   13: ifnull +8 -> 21
    //   16: aload_0
    //   17: getfield 1350	android/media/ExifInterface:mThumbnailBytes	[B
    //   20: areturn
    //   21: aconst_null
    //   22: astore_1
    //   23: aconst_null
    //   24: astore_2
    //   25: aconst_null
    //   26: astore_3
    //   27: aload_1
    //   28: astore 4
    //   30: aload_2
    //   31: astore 5
    //   33: aload_0
    //   34: getfield 974	android/media/ExifInterface:mAssetInputStream	Landroid/content/res/AssetManager$AssetInputStream;
    //   37: ifnull +61 -> 98
    //   40: aload_1
    //   41: astore 4
    //   43: aload_2
    //   44: astore 5
    //   46: aload_0
    //   47: getfield 974	android/media/ExifInterface:mAssetInputStream	Landroid/content/res/AssetManager$AssetInputStream;
    //   50: astore_3
    //   51: aload_3
    //   52: astore 4
    //   54: aload_3
    //   55: astore 5
    //   57: aload_3
    //   58: invokevirtual 1750	java/io/InputStream:markSupported	()Z
    //   61: ifeq +16 -> 77
    //   64: aload_3
    //   65: astore 4
    //   67: aload_3
    //   68: astore 5
    //   70: aload_3
    //   71: invokevirtual 1751	java/io/InputStream:reset	()V
    //   74: goto +115 -> 189
    //   77: aload_3
    //   78: astore 4
    //   80: aload_3
    //   81: astore 5
    //   83: ldc -46
    //   85: ldc_w 1753
    //   88: invokestatic 1377	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   91: pop
    //   92: aload_3
    //   93: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   96: aconst_null
    //   97: areturn
    //   98: aload_1
    //   99: astore 4
    //   101: aload_2
    //   102: astore 5
    //   104: aload_0
    //   105: getfield 976	android/media/ExifInterface:mFilename	Ljava/lang/String;
    //   108: ifnull +24 -> 132
    //   111: aload_1
    //   112: astore 4
    //   114: aload_2
    //   115: astore 5
    //   117: new 996	java/io/FileInputStream
    //   120: dup
    //   121: aload_0
    //   122: getfield 976	android/media/ExifInterface:mFilename	Ljava/lang/String;
    //   125: invokespecial 1023	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   128: astore_3
    //   129: goto +60 -> 189
    //   132: aload_1
    //   133: astore 4
    //   135: aload_2
    //   136: astore 5
    //   138: aload_0
    //   139: getfield 982	android/media/ExifInterface:mSeekableFileDescriptor	Ljava/io/FileDescriptor;
    //   142: ifnull +47 -> 189
    //   145: aload_1
    //   146: astore 4
    //   148: aload_2
    //   149: astore 5
    //   151: aload_0
    //   152: getfield 982	android/media/ExifInterface:mSeekableFileDescriptor	Ljava/io/FileDescriptor;
    //   155: invokestatic 988	android/system/Os:dup	(Ljava/io/FileDescriptor;)Ljava/io/FileDescriptor;
    //   158: astore_3
    //   159: aload_1
    //   160: astore 4
    //   162: aload_2
    //   163: astore 5
    //   165: aload_3
    //   166: lconst_0
    //   167: getstatic 1756	android/system/OsConstants:SEEK_SET	I
    //   170: invokestatic 1410	android/system/Os:lseek	(Ljava/io/FileDescriptor;JI)J
    //   173: pop2
    //   174: aload_1
    //   175: astore 4
    //   177: aload_2
    //   178: astore 5
    //   180: new 996	java/io/FileInputStream
    //   183: dup
    //   184: aload_3
    //   185: invokespecial 998	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   188: astore_3
    //   189: aload_3
    //   190: ifnull +137 -> 327
    //   193: aload_3
    //   194: astore 4
    //   196: aload_3
    //   197: astore 5
    //   199: aload_3
    //   200: aload_0
    //   201: getfield 1344	android/media/ExifInterface:mThumbnailOffset	I
    //   204: i2l
    //   205: invokevirtual 1760	java/io/InputStream:skip	(J)J
    //   208: aload_0
    //   209: getfield 1344	android/media/ExifInterface:mThumbnailOffset	I
    //   212: i2l
    //   213: lcmp
    //   214: ifne +82 -> 296
    //   217: aload_3
    //   218: astore 4
    //   220: aload_3
    //   221: astore 5
    //   223: aload_0
    //   224: getfield 1346	android/media/ExifInterface:mThumbnailLength	I
    //   227: newarray byte
    //   229: astore_1
    //   230: aload_3
    //   231: astore 4
    //   233: aload_3
    //   234: astore 5
    //   236: aload_3
    //   237: aload_1
    //   238: invokevirtual 1761	java/io/InputStream:read	([B)I
    //   241: aload_0
    //   242: getfield 1346	android/media/ExifInterface:mThumbnailLength	I
    //   245: if_icmpne +20 -> 265
    //   248: aload_3
    //   249: astore 4
    //   251: aload_3
    //   252: astore 5
    //   254: aload_0
    //   255: aload_1
    //   256: putfield 1350	android/media/ExifInterface:mThumbnailBytes	[B
    //   259: aload_3
    //   260: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   263: aload_1
    //   264: areturn
    //   265: aload_3
    //   266: astore 4
    //   268: aload_3
    //   269: astore 5
    //   271: new 955	java/io/IOException
    //   274: astore_1
    //   275: aload_3
    //   276: astore 4
    //   278: aload_3
    //   279: astore 5
    //   281: aload_1
    //   282: ldc_w 1763
    //   285: invokespecial 1137	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   288: aload_3
    //   289: astore 4
    //   291: aload_3
    //   292: astore 5
    //   294: aload_1
    //   295: athrow
    //   296: aload_3
    //   297: astore 4
    //   299: aload_3
    //   300: astore 5
    //   302: new 955	java/io/IOException
    //   305: astore_1
    //   306: aload_3
    //   307: astore 4
    //   309: aload_3
    //   310: astore 5
    //   312: aload_1
    //   313: ldc_w 1763
    //   316: invokespecial 1137	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   319: aload_3
    //   320: astore 4
    //   322: aload_3
    //   323: astore 5
    //   325: aload_1
    //   326: athrow
    //   327: aload_3
    //   328: astore 4
    //   330: aload_3
    //   331: astore 5
    //   333: new 1765	java/io/FileNotFoundException
    //   336: astore_1
    //   337: aload_3
    //   338: astore 4
    //   340: aload_3
    //   341: astore 5
    //   343: aload_1
    //   344: invokespecial 1766	java/io/FileNotFoundException:<init>	()V
    //   347: aload_3
    //   348: astore 4
    //   350: aload_3
    //   351: astore 5
    //   353: aload_1
    //   354: athrow
    //   355: astore_3
    //   356: goto +25 -> 381
    //   359: astore_3
    //   360: aload 5
    //   362: astore 4
    //   364: ldc -46
    //   366: ldc_w 1768
    //   369: aload_3
    //   370: invokestatic 1771	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   373: pop
    //   374: aload 5
    //   376: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   379: aconst_null
    //   380: areturn
    //   381: aload 4
    //   383: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   386: aload_3
    //   387: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	388	0	this	ExifInterface
    //   22	332	1	localObject1	Object
    //   24	154	2	localObject2	Object
    //   26	325	3	localObject3	Object
    //   355	1	3	localObject4	Object
    //   359	28	3	localIOException	IOException
    //   28	354	4	localObject5	Object
    //   31	344	5	localObject6	Object
    // Exception table:
    //   from	to	target	type
    //   33	40	355	finally
    //   46	51	355	finally
    //   57	64	355	finally
    //   70	74	355	finally
    //   83	92	355	finally
    //   104	111	355	finally
    //   117	129	355	finally
    //   138	145	355	finally
    //   151	159	355	finally
    //   165	174	355	finally
    //   180	189	355	finally
    //   199	217	355	finally
    //   223	230	355	finally
    //   236	248	355	finally
    //   254	259	355	finally
    //   271	275	355	finally
    //   281	288	355	finally
    //   294	296	355	finally
    //   302	306	355	finally
    //   312	319	355	finally
    //   325	327	355	finally
    //   333	337	355	finally
    //   343	347	355	finally
    //   353	355	355	finally
    //   364	374	355	finally
    //   33	40	359	java/io/IOException
    //   33	40	359	android/system/ErrnoException
    //   46	51	359	java/io/IOException
    //   46	51	359	android/system/ErrnoException
    //   57	64	359	java/io/IOException
    //   57	64	359	android/system/ErrnoException
    //   70	74	359	java/io/IOException
    //   70	74	359	android/system/ErrnoException
    //   83	92	359	java/io/IOException
    //   83	92	359	android/system/ErrnoException
    //   104	111	359	java/io/IOException
    //   104	111	359	android/system/ErrnoException
    //   117	129	359	java/io/IOException
    //   117	129	359	android/system/ErrnoException
    //   138	145	359	java/io/IOException
    //   138	145	359	android/system/ErrnoException
    //   151	159	359	java/io/IOException
    //   151	159	359	android/system/ErrnoException
    //   165	174	359	java/io/IOException
    //   165	174	359	android/system/ErrnoException
    //   180	189	359	java/io/IOException
    //   180	189	359	android/system/ErrnoException
    //   199	217	359	java/io/IOException
    //   199	217	359	android/system/ErrnoException
    //   223	230	359	java/io/IOException
    //   223	230	359	android/system/ErrnoException
    //   236	248	359	java/io/IOException
    //   236	248	359	android/system/ErrnoException
    //   254	259	359	java/io/IOException
    //   254	259	359	android/system/ErrnoException
    //   271	275	359	java/io/IOException
    //   271	275	359	android/system/ErrnoException
    //   281	288	359	java/io/IOException
    //   281	288	359	android/system/ErrnoException
    //   294	296	359	java/io/IOException
    //   294	296	359	android/system/ErrnoException
    //   302	306	359	java/io/IOException
    //   302	306	359	android/system/ErrnoException
    //   312	319	359	java/io/IOException
    //   312	319	359	android/system/ErrnoException
    //   325	327	359	java/io/IOException
    //   325	327	359	android/system/ErrnoException
    //   333	337	359	java/io/IOException
    //   333	337	359	android/system/ErrnoException
    //   343	347	359	java/io/IOException
    //   343	347	359	android/system/ErrnoException
    //   353	355	359	java/io/IOException
    //   353	355	359	android/system/ErrnoException
  }
  
  public long[] getThumbnailRange()
  {
    if (!mHasThumbnail) {
      return null;
    }
    return new long[] { mThumbnailOffset, mThumbnailLength };
  }
  
  public boolean hasThumbnail()
  {
    return mHasThumbnail;
  }
  
  public boolean isThumbnailCompressed()
  {
    if (!mHasThumbnail) {
      return false;
    }
    return (mThumbnailCompression == 6) || (mThumbnailCompression == 7);
  }
  
  /* Error */
  public void saveAttributes()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 1436	android/media/ExifInterface:mIsSupportedFile	Z
    //   4: ifeq +921 -> 925
    //   7: aload_0
    //   8: getfield 1279	android/media/ExifInterface:mMimeType	I
    //   11: iconst_4
    //   12: if_icmpne +913 -> 925
    //   15: aload_0
    //   16: getfield 994	android/media/ExifInterface:mIsInputStream	Z
    //   19: ifne +895 -> 914
    //   22: aload_0
    //   23: getfield 982	android/media/ExifInterface:mSeekableFileDescriptor	Ljava/io/FileDescriptor;
    //   26: ifnonnull +10 -> 36
    //   29: aload_0
    //   30: getfield 976	android/media/ExifInterface:mFilename	Ljava/lang/String;
    //   33: ifnull +881 -> 914
    //   36: aload_0
    //   37: aload_0
    //   38: invokevirtual 1778	android/media/ExifInterface:getThumbnail	()[B
    //   41: putfield 1350	android/media/ExifInterface:mThumbnailBytes	[B
    //   44: aconst_null
    //   45: astore_1
    //   46: aconst_null
    //   47: astore_2
    //   48: aconst_null
    //   49: astore_3
    //   50: aconst_null
    //   51: astore 4
    //   53: aconst_null
    //   54: astore 5
    //   56: aconst_null
    //   57: astore 6
    //   59: aconst_null
    //   60: astore 7
    //   62: aload_1
    //   63: astore 8
    //   65: aload 4
    //   67: astore 9
    //   69: aload_2
    //   70: astore 10
    //   72: aload 5
    //   74: astore 11
    //   76: aload_0
    //   77: getfield 976	android/media/ExifInterface:mFilename	Ljava/lang/String;
    //   80: ifnull +343 -> 423
    //   83: aload_1
    //   84: astore 8
    //   86: aload 4
    //   88: astore 9
    //   90: aload_2
    //   91: astore 10
    //   93: aload 5
    //   95: astore 11
    //   97: new 1780	java/io/File
    //   100: astore 7
    //   102: aload_1
    //   103: astore 8
    //   105: aload 4
    //   107: astore 9
    //   109: aload_2
    //   110: astore 10
    //   112: aload 5
    //   114: astore 11
    //   116: new 1179	java/lang/StringBuilder
    //   119: astore 12
    //   121: aload_1
    //   122: astore 8
    //   124: aload 4
    //   126: astore 9
    //   128: aload_2
    //   129: astore 10
    //   131: aload 5
    //   133: astore 11
    //   135: aload 12
    //   137: invokespecial 1180	java/lang/StringBuilder:<init>	()V
    //   140: aload_1
    //   141: astore 8
    //   143: aload 4
    //   145: astore 9
    //   147: aload_2
    //   148: astore 10
    //   150: aload 5
    //   152: astore 11
    //   154: aload 12
    //   156: aload_0
    //   157: getfield 976	android/media/ExifInterface:mFilename	Ljava/lang/String;
    //   160: invokevirtual 1186	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: pop
    //   164: aload_1
    //   165: astore 8
    //   167: aload 4
    //   169: astore 9
    //   171: aload_2
    //   172: astore 10
    //   174: aload 5
    //   176: astore 11
    //   178: aload 12
    //   180: ldc_w 1782
    //   183: invokevirtual 1186	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload_1
    //   188: astore 8
    //   190: aload 4
    //   192: astore 9
    //   194: aload_2
    //   195: astore 10
    //   197: aload 5
    //   199: astore 11
    //   201: aload 7
    //   203: aload 12
    //   205: invokevirtual 1192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   208: invokespecial 1783	java/io/File:<init>	(Ljava/lang/String;)V
    //   211: aload_1
    //   212: astore 8
    //   214: aload 4
    //   216: astore 9
    //   218: aload_2
    //   219: astore 10
    //   221: aload 5
    //   223: astore 11
    //   225: new 1780	java/io/File
    //   228: astore 12
    //   230: aload_1
    //   231: astore 8
    //   233: aload 4
    //   235: astore 9
    //   237: aload_2
    //   238: astore 10
    //   240: aload 5
    //   242: astore 11
    //   244: aload 12
    //   246: aload_0
    //   247: getfield 976	android/media/ExifInterface:mFilename	Ljava/lang/String;
    //   250: invokespecial 1783	java/io/File:<init>	(Ljava/lang/String;)V
    //   253: aload_1
    //   254: astore 8
    //   256: aload 4
    //   258: astore 9
    //   260: aload_2
    //   261: astore 10
    //   263: aload 5
    //   265: astore 11
    //   267: aload 12
    //   269: aload 7
    //   271: invokevirtual 1787	java/io/File:renameTo	(Ljava/io/File;)Z
    //   274: ifeq +6 -> 280
    //   277: goto +319 -> 596
    //   280: aload_1
    //   281: astore 8
    //   283: aload 4
    //   285: astore 9
    //   287: aload_2
    //   288: astore 10
    //   290: aload 5
    //   292: astore 11
    //   294: new 955	java/io/IOException
    //   297: astore_3
    //   298: aload_1
    //   299: astore 8
    //   301: aload 4
    //   303: astore 9
    //   305: aload_2
    //   306: astore 10
    //   308: aload 5
    //   310: astore 11
    //   312: new 1179	java/lang/StringBuilder
    //   315: astore 6
    //   317: aload_1
    //   318: astore 8
    //   320: aload 4
    //   322: astore 9
    //   324: aload_2
    //   325: astore 10
    //   327: aload 5
    //   329: astore 11
    //   331: aload 6
    //   333: invokespecial 1180	java/lang/StringBuilder:<init>	()V
    //   336: aload_1
    //   337: astore 8
    //   339: aload 4
    //   341: astore 9
    //   343: aload_2
    //   344: astore 10
    //   346: aload 5
    //   348: astore 11
    //   350: aload 6
    //   352: ldc_w 1789
    //   355: invokevirtual 1186	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   358: pop
    //   359: aload_1
    //   360: astore 8
    //   362: aload 4
    //   364: astore 9
    //   366: aload_2
    //   367: astore 10
    //   369: aload 5
    //   371: astore 11
    //   373: aload 6
    //   375: aload 7
    //   377: invokevirtual 1792	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   380: invokevirtual 1186	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   383: pop
    //   384: aload_1
    //   385: astore 8
    //   387: aload 4
    //   389: astore 9
    //   391: aload_2
    //   392: astore 10
    //   394: aload 5
    //   396: astore 11
    //   398: aload_3
    //   399: aload 6
    //   401: invokevirtual 1192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   404: invokespecial 1137	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   407: aload_1
    //   408: astore 8
    //   410: aload 4
    //   412: astore 9
    //   414: aload_2
    //   415: astore 10
    //   417: aload 5
    //   419: astore 11
    //   421: aload_3
    //   422: athrow
    //   423: aload_1
    //   424: astore 8
    //   426: aload 4
    //   428: astore 9
    //   430: aload_2
    //   431: astore 10
    //   433: aload 5
    //   435: astore 11
    //   437: aload_0
    //   438: getfield 982	android/media/ExifInterface:mSeekableFileDescriptor	Ljava/io/FileDescriptor;
    //   441: ifnull +155 -> 596
    //   444: aload_1
    //   445: astore 8
    //   447: aload 4
    //   449: astore 9
    //   451: aload_2
    //   452: astore 10
    //   454: aload 5
    //   456: astore 11
    //   458: ldc_w 1794
    //   461: ldc_w 1796
    //   464: invokestatic 1800	java/io/File:createTempFile	(Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;
    //   467: astore 7
    //   469: aload_1
    //   470: astore 8
    //   472: aload 4
    //   474: astore 9
    //   476: aload_2
    //   477: astore 10
    //   479: aload 5
    //   481: astore 11
    //   483: aload_0
    //   484: getfield 982	android/media/ExifInterface:mSeekableFileDescriptor	Ljava/io/FileDescriptor;
    //   487: lconst_0
    //   488: getstatic 1756	android/system/OsConstants:SEEK_SET	I
    //   491: invokestatic 1410	android/system/Os:lseek	(Ljava/io/FileDescriptor;JI)J
    //   494: pop2
    //   495: aload_1
    //   496: astore 8
    //   498: aload 4
    //   500: astore 9
    //   502: aload_2
    //   503: astore 10
    //   505: aload 5
    //   507: astore 11
    //   509: new 996	java/io/FileInputStream
    //   512: astore_3
    //   513: aload_1
    //   514: astore 8
    //   516: aload 4
    //   518: astore 9
    //   520: aload_2
    //   521: astore 10
    //   523: aload 5
    //   525: astore 11
    //   527: aload_3
    //   528: aload_0
    //   529: getfield 982	android/media/ExifInterface:mSeekableFileDescriptor	Ljava/io/FileDescriptor;
    //   532: invokespecial 998	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   535: aload_3
    //   536: astore 8
    //   538: aload 4
    //   540: astore 9
    //   542: aload_3
    //   543: astore 10
    //   545: aload 5
    //   547: astore 11
    //   549: new 1802	java/io/FileOutputStream
    //   552: astore 6
    //   554: aload_3
    //   555: astore 8
    //   557: aload 4
    //   559: astore 9
    //   561: aload_3
    //   562: astore 10
    //   564: aload 5
    //   566: astore 11
    //   568: aload 6
    //   570: aload 7
    //   572: invokespecial 1805	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   575: aload_3
    //   576: astore 8
    //   578: aload 6
    //   580: astore 9
    //   582: aload_3
    //   583: astore 10
    //   585: aload 6
    //   587: astore 11
    //   589: aload_3
    //   590: aload 6
    //   592: invokestatic 1596	libcore/io/Streams:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;)I
    //   595: pop
    //   596: aload_3
    //   597: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   600: aload 6
    //   602: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   605: aconst_null
    //   606: astore_2
    //   607: aconst_null
    //   608: astore_1
    //   609: aconst_null
    //   610: astore 4
    //   612: aconst_null
    //   613: astore 5
    //   615: aconst_null
    //   616: astore_3
    //   617: aload_1
    //   618: astore 6
    //   620: aload 4
    //   622: astore 8
    //   624: aload_2
    //   625: astore 9
    //   627: aload 5
    //   629: astore 10
    //   631: new 996	java/io/FileInputStream
    //   634: astore 11
    //   636: aload_1
    //   637: astore 6
    //   639: aload 4
    //   641: astore 8
    //   643: aload_2
    //   644: astore 9
    //   646: aload 5
    //   648: astore 10
    //   650: aload 11
    //   652: aload 7
    //   654: invokespecial 1806	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   657: aload 11
    //   659: astore 6
    //   661: aload 4
    //   663: astore 8
    //   665: aload 11
    //   667: astore 9
    //   669: aload 5
    //   671: astore 10
    //   673: aload_0
    //   674: getfield 976	android/media/ExifInterface:mFilename	Ljava/lang/String;
    //   677: ifnull +50 -> 727
    //   680: aload 11
    //   682: astore 6
    //   684: aload 4
    //   686: astore 8
    //   688: aload 11
    //   690: astore 9
    //   692: aload 5
    //   694: astore 10
    //   696: new 1802	java/io/FileOutputStream
    //   699: astore_3
    //   700: aload 11
    //   702: astore 6
    //   704: aload 4
    //   706: astore 8
    //   708: aload 11
    //   710: astore 9
    //   712: aload 5
    //   714: astore 10
    //   716: aload_3
    //   717: aload_0
    //   718: getfield 976	android/media/ExifInterface:mFilename	Ljava/lang/String;
    //   721: invokespecial 1807	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   724: goto +82 -> 806
    //   727: aload 11
    //   729: astore 6
    //   731: aload 4
    //   733: astore 8
    //   735: aload 11
    //   737: astore 9
    //   739: aload 5
    //   741: astore 10
    //   743: aload_0
    //   744: getfield 982	android/media/ExifInterface:mSeekableFileDescriptor	Ljava/io/FileDescriptor;
    //   747: ifnull +59 -> 806
    //   750: aload 11
    //   752: astore 6
    //   754: aload 4
    //   756: astore 8
    //   758: aload 11
    //   760: astore 9
    //   762: aload 5
    //   764: astore 10
    //   766: aload_0
    //   767: getfield 982	android/media/ExifInterface:mSeekableFileDescriptor	Ljava/io/FileDescriptor;
    //   770: lconst_0
    //   771: getstatic 1756	android/system/OsConstants:SEEK_SET	I
    //   774: invokestatic 1410	android/system/Os:lseek	(Ljava/io/FileDescriptor;JI)J
    //   777: pop2
    //   778: aload 11
    //   780: astore 6
    //   782: aload 4
    //   784: astore 8
    //   786: aload 11
    //   788: astore 9
    //   790: aload 5
    //   792: astore 10
    //   794: new 1802	java/io/FileOutputStream
    //   797: dup
    //   798: aload_0
    //   799: getfield 982	android/media/ExifInterface:mSeekableFileDescriptor	Ljava/io/FileDescriptor;
    //   802: invokespecial 1808	java/io/FileOutputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   805: astore_3
    //   806: aload 11
    //   808: astore 6
    //   810: aload_3
    //   811: astore 8
    //   813: aload 11
    //   815: astore 9
    //   817: aload_3
    //   818: astore 10
    //   820: aload_0
    //   821: aload 11
    //   823: aload_3
    //   824: invokespecial 1810	android/media/ExifInterface:saveJpegAttributes	(Ljava/io/InputStream;Ljava/io/OutputStream;)V
    //   827: aload 11
    //   829: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   832: aload_3
    //   833: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   836: aload 7
    //   838: invokevirtual 1813	java/io/File:delete	()Z
    //   841: pop
    //   842: aload_0
    //   843: aconst_null
    //   844: putfield 1350	android/media/ExifInterface:mThumbnailBytes	[B
    //   847: return
    //   848: astore_3
    //   849: goto +17 -> 866
    //   852: astore_3
    //   853: aload 9
    //   855: astore 6
    //   857: aload 10
    //   859: astore 8
    //   861: aload_3
    //   862: invokevirtual 992	android/system/ErrnoException:rethrowAsIOException	()Ljava/io/IOException;
    //   865: athrow
    //   866: aload 6
    //   868: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   871: aload 8
    //   873: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   876: aload 7
    //   878: invokevirtual 1813	java/io/File:delete	()Z
    //   881: pop
    //   882: aload_3
    //   883: athrow
    //   884: astore_3
    //   885: goto +17 -> 902
    //   888: astore_3
    //   889: aload 10
    //   891: astore 8
    //   893: aload 11
    //   895: astore 9
    //   897: aload_3
    //   898: invokevirtual 992	android/system/ErrnoException:rethrowAsIOException	()Ljava/io/IOException;
    //   901: athrow
    //   902: aload 8
    //   904: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   907: aload 9
    //   909: invokestatic 1008	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   912: aload_3
    //   913: athrow
    //   914: new 955	java/io/IOException
    //   917: dup
    //   918: ldc_w 1815
    //   921: invokespecial 1137	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   924: athrow
    //   925: new 955	java/io/IOException
    //   928: dup
    //   929: ldc_w 1817
    //   932: invokespecial 1137	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   935: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	936	0	this	ExifInterface
    //   45	592	1	localObject1	Object
    //   47	597	2	localObject2	Object
    //   49	784	3	localObject3	Object
    //   848	1	3	localObject4	Object
    //   852	31	3	localErrnoException1	ErrnoException
    //   884	1	3	localObject5	Object
    //   888	25	3	localErrnoException2	ErrnoException
    //   51	732	4	localObject6	Object
    //   54	737	5	localObject7	Object
    //   57	810	6	localObject8	Object
    //   60	817	7	localFile	java.io.File
    //   63	840	8	localObject9	Object
    //   67	841	9	localObject10	Object
    //   70	820	10	localObject11	Object
    //   74	820	11	localObject12	Object
    //   119	149	12	localObject13	Object
    // Exception table:
    //   from	to	target	type
    //   631	636	848	finally
    //   650	657	848	finally
    //   673	680	848	finally
    //   696	700	848	finally
    //   716	724	848	finally
    //   743	750	848	finally
    //   766	778	848	finally
    //   794	806	848	finally
    //   820	827	848	finally
    //   861	866	848	finally
    //   631	636	852	android/system/ErrnoException
    //   650	657	852	android/system/ErrnoException
    //   673	680	852	android/system/ErrnoException
    //   696	700	852	android/system/ErrnoException
    //   716	724	852	android/system/ErrnoException
    //   743	750	852	android/system/ErrnoException
    //   766	778	852	android/system/ErrnoException
    //   794	806	852	android/system/ErrnoException
    //   820	827	852	android/system/ErrnoException
    //   76	83	884	finally
    //   97	102	884	finally
    //   116	121	884	finally
    //   135	140	884	finally
    //   154	164	884	finally
    //   178	187	884	finally
    //   201	211	884	finally
    //   225	230	884	finally
    //   244	253	884	finally
    //   267	277	884	finally
    //   294	298	884	finally
    //   312	317	884	finally
    //   331	336	884	finally
    //   350	359	884	finally
    //   373	384	884	finally
    //   398	407	884	finally
    //   421	423	884	finally
    //   437	444	884	finally
    //   458	469	884	finally
    //   483	495	884	finally
    //   509	513	884	finally
    //   527	535	884	finally
    //   549	554	884	finally
    //   568	575	884	finally
    //   589	596	884	finally
    //   897	902	884	finally
    //   76	83	888	android/system/ErrnoException
    //   97	102	888	android/system/ErrnoException
    //   116	121	888	android/system/ErrnoException
    //   135	140	888	android/system/ErrnoException
    //   154	164	888	android/system/ErrnoException
    //   178	187	888	android/system/ErrnoException
    //   201	211	888	android/system/ErrnoException
    //   225	230	888	android/system/ErrnoException
    //   244	253	888	android/system/ErrnoException
    //   267	277	888	android/system/ErrnoException
    //   294	298	888	android/system/ErrnoException
    //   312	317	888	android/system/ErrnoException
    //   331	336	888	android/system/ErrnoException
    //   350	359	888	android/system/ErrnoException
    //   373	384	888	android/system/ErrnoException
    //   398	407	888	android/system/ErrnoException
    //   421	423	888	android/system/ErrnoException
    //   437	444	888	android/system/ErrnoException
    //   458	469	888	android/system/ErrnoException
    //   483	495	888	android/system/ErrnoException
    //   509	513	888	android/system/ErrnoException
    //   527	535	888	android/system/ErrnoException
    //   549	554	888	android/system/ErrnoException
    //   568	575	888	android/system/ErrnoException
    //   589	596	888	android/system/ErrnoException
  }
  
  public void setAttribute(String paramString1, String paramString2)
  {
    Object localObject1 = this;
    Object localObject2 = paramString2;
    Object localObject3 = localObject2;
    if (localObject2 != null)
    {
      localObject3 = localObject2;
      if (sTagSetForCompatibility.contains(paramString1)) {
        if (paramString1.equals("GPSTimeStamp"))
        {
          paramString2 = sGpsTimestampPattern.matcher((CharSequence)localObject2);
          if (!paramString2.find())
          {
            paramString2 = new StringBuilder();
            paramString2.append("Invalid value for ");
            paramString2.append(paramString1);
            paramString2.append(" : ");
            paramString2.append((String)localObject2);
            Log.w("ExifInterface", paramString2.toString());
            return;
          }
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append(Integer.parseInt(paramString2.group(1)));
          ((StringBuilder)localObject3).append("/1,");
          ((StringBuilder)localObject3).append(Integer.parseInt(paramString2.group(2)));
          ((StringBuilder)localObject3).append("/1,");
          ((StringBuilder)localObject3).append(Integer.parseInt(paramString2.group(3)));
          ((StringBuilder)localObject3).append("/1");
          localObject3 = ((StringBuilder)localObject3).toString();
        }
        else
        {
          try
          {
            double d = Double.parseDouble(paramString2);
            paramString2 = new java/lang/StringBuilder;
            paramString2.<init>();
            paramString2.append((10000.0D * d));
            paramString2.append("/10000");
            localObject3 = paramString2.toString();
          }
          catch (NumberFormatException paramString2)
          {
            paramString2 = new StringBuilder();
            paramString2.append("Invalid value for ");
            paramString2.append(paramString1);
            paramString2.append(" : ");
            paramString2.append((String)localObject2);
            Log.w("ExifInterface", paramString2.toString());
            return;
          }
        }
      }
    }
    int i = 0;
    int j = 0;
    paramString2 = (String)localObject1;
    while (j < EXIF_TAGS.length)
    {
      if ((j != 4) || (mHasThumbnail))
      {
        Object localObject4;
        for (;;)
        {
          localObject1 = sExifTagMapsForWriting[j].get(paramString1);
          if (localObject1 == null) {
            break;
          }
          if (localObject3 == null)
          {
            mAttributes[j].remove(paramString1);
          }
          else
          {
            ExifTag localExifTag = (ExifTag)localObject1;
            localObject4 = guessDataFormat((String)localObject3);
            if ((primaryFormat != ((Integer)first).intValue()) && (primaryFormat != ((Integer)second).intValue()))
            {
              if ((secondaryFormat != -1) && ((secondaryFormat == ((Integer)first).intValue()) || (secondaryFormat == ((Integer)second).intValue())))
              {
                k = secondaryFormat;
              }
              else
              {
                if ((primaryFormat != 1) && (primaryFormat != 7) && (primaryFormat != 2))
                {
                  localObject2 = new StringBuilder();
                  ((StringBuilder)localObject2).append("Given tag (");
                  ((StringBuilder)localObject2).append(paramString1);
                  ((StringBuilder)localObject2).append(") value didn't match with one of expected formats: ");
                  ((StringBuilder)localObject2).append(IFD_FORMAT_NAMES[primaryFormat]);
                  if (secondaryFormat == -1)
                  {
                    localObject1 = "";
                  }
                  else
                  {
                    localObject1 = new StringBuilder();
                    ((StringBuilder)localObject1).append(", ");
                    ((StringBuilder)localObject1).append(IFD_FORMAT_NAMES[secondaryFormat]);
                    localObject1 = ((StringBuilder)localObject1).toString();
                  }
                  ((StringBuilder)localObject2).append((String)localObject1);
                  ((StringBuilder)localObject2).append(" (guess: ");
                  ((StringBuilder)localObject2).append(IFD_FORMAT_NAMES[((Integer)first).intValue()]);
                  if (((Integer)second).intValue() == -1)
                  {
                    localObject1 = "";
                  }
                  else
                  {
                    localObject1 = new StringBuilder();
                    ((StringBuilder)localObject1).append(", ");
                    ((StringBuilder)localObject1).append(IFD_FORMAT_NAMES[((Integer)second).intValue()]);
                    localObject1 = ((StringBuilder)localObject1).toString();
                  }
                  ((StringBuilder)localObject2).append((String)localObject1);
                  ((StringBuilder)localObject2).append(")");
                  Log.w("ExifInterface", ((StringBuilder)localObject2).toString());
                  continue;
                }
                k = primaryFormat;
              }
            }
            else {
              k = primaryFormat;
            }
            switch (k)
            {
            case 6: 
            case 8: 
            case 11: 
            default: 
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Data format isn't one of expected formats: ");
              ((StringBuilder)localObject1).append(k);
              Log.w("ExifInterface", ((StringBuilder)localObject1).toString());
              break;
            case 12: 
              localObject2 = ((String)localObject3).split(",");
              localObject1 = new double[localObject2.length];
              for (k = i; k < localObject2.length; k++) {
                localObject1[k] = Double.parseDouble(localObject2[k]);
              }
              mAttributes[j].put(paramString1, ExifAttribute.createDouble((double[])localObject1, mExifByteOrder));
            }
          }
        }
        paramString2 = ((String)localObject3).split(",");
        localObject1 = new Rational[paramString2.length];
        int k = i;
        while (k < paramString2.length)
        {
          localObject2 = paramString2[k].split("/");
          localObject1[k] = new Rational(Double.parseDouble(localObject2[i]), Double.parseDouble(localObject2[1]), null);
          k++;
          i = 0;
        }
        paramString2 = this;
        mAttributes[j].put(paramString1, ExifAttribute.createSRational((Rational[])localObject1, mExifByteOrder));
        break label1074;
        localObject1 = ((String)localObject3).split(",");
        localObject2 = new int[localObject1.length];
        for (i = 0; i < localObject1.length; i++) {
          localObject2[i] = Integer.parseInt(localObject1[i]);
        }
        mAttributes[j].put(paramString1, ExifAttribute.createSLong((int[])localObject2, mExifByteOrder));
        label1074:
        i = 0;
        break label1366;
        localObject2 = ((String)localObject3).split(",");
        localObject1 = new Rational[localObject2.length];
        for (i = 0; i < localObject2.length; i++)
        {
          localObject4 = localObject2[i].split("/");
          localObject1[i] = new Rational(Double.parseDouble(localObject4[0]), Double.parseDouble(localObject4[1]), null);
        }
        i = 0;
        mAttributes[j].put(paramString1, ExifAttribute.createURational((Rational[])localObject1, mExifByteOrder));
        break label1366;
        k = i;
        localObject1 = ((String)localObject3).split(",");
        localObject2 = new long[localObject1.length];
        while (i < localObject1.length)
        {
          localObject2[i] = Long.parseLong(localObject1[i]);
          i++;
        }
        mAttributes[j].put(paramString1, ExifAttribute.createULong((long[])localObject2, mExifByteOrder));
        i = k;
        break label1366;
        k = i;
        localObject2 = ((String)localObject3).split(",");
        localObject1 = new int[localObject2.length];
        while (i < localObject2.length)
        {
          localObject1[i] = Integer.parseInt(localObject2[i]);
          i++;
        }
        mAttributes[j].put(paramString1, ExifAttribute.createUShort((int[])localObject1, mExifByteOrder));
        i = k;
        break label1366;
        mAttributes[j].put(paramString1, ExifAttribute.createString((String)localObject3));
        break label1366;
        mAttributes[j].put(paramString1, ExifAttribute.createByte((String)localObject3));
      }
      label1366:
      j++;
    }
  }
  
  private static class ByteOrderedDataInputStream
    extends InputStream
    implements DataInput
  {
    private static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
    private static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
    private ByteOrder mByteOrder = ByteOrder.BIG_ENDIAN;
    private DataInputStream mDataInputStream;
    private InputStream mInputStream;
    private final int mLength;
    private int mPosition;
    
    public ByteOrderedDataInputStream(InputStream paramInputStream)
      throws IOException
    {
      mInputStream = paramInputStream;
      mDataInputStream = new DataInputStream(paramInputStream);
      mLength = mDataInputStream.available();
      mPosition = 0;
      mDataInputStream.mark(mLength);
    }
    
    public ByteOrderedDataInputStream(byte[] paramArrayOfByte)
      throws IOException
    {
      this(new ByteArrayInputStream(paramArrayOfByte));
    }
    
    public int available()
      throws IOException
    {
      return mDataInputStream.available();
    }
    
    public int peek()
    {
      return mPosition;
    }
    
    public int read()
      throws IOException
    {
      mPosition += 1;
      return mDataInputStream.read();
    }
    
    public boolean readBoolean()
      throws IOException
    {
      mPosition += 1;
      return mDataInputStream.readBoolean();
    }
    
    public byte readByte()
      throws IOException
    {
      mPosition += 1;
      if (mPosition <= mLength)
      {
        int i = mDataInputStream.read();
        if (i >= 0) {
          return (byte)i;
        }
        throw new EOFException();
      }
      throw new EOFException();
    }
    
    public char readChar()
      throws IOException
    {
      mPosition += 2;
      return mDataInputStream.readChar();
    }
    
    public double readDouble()
      throws IOException
    {
      return Double.longBitsToDouble(readLong());
    }
    
    public float readFloat()
      throws IOException
    {
      return Float.intBitsToFloat(readInt());
    }
    
    public void readFully(byte[] paramArrayOfByte)
      throws IOException
    {
      mPosition += paramArrayOfByte.length;
      if (mPosition <= mLength)
      {
        if (mDataInputStream.read(paramArrayOfByte, 0, paramArrayOfByte.length) == paramArrayOfByte.length) {
          return;
        }
        throw new IOException("Couldn't read up to the length of buffer");
      }
      throw new EOFException();
    }
    
    public void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      mPosition += paramInt2;
      if (mPosition <= mLength)
      {
        if (mDataInputStream.read(paramArrayOfByte, paramInt1, paramInt2) == paramInt2) {
          return;
        }
        throw new IOException("Couldn't read up to the length of buffer");
      }
      throw new EOFException();
    }
    
    public int readInt()
      throws IOException
    {
      mPosition += 4;
      if (mPosition <= mLength)
      {
        int i = mDataInputStream.read();
        int j = mDataInputStream.read();
        int k = mDataInputStream.read();
        int m = mDataInputStream.read();
        if ((i | j | k | m) >= 0)
        {
          if (mByteOrder == LITTLE_ENDIAN) {
            return (m << 24) + (k << 16) + (j << 8) + i;
          }
          if (mByteOrder == BIG_ENDIAN) {
            return (i << 24) + (j << 16) + (k << 8) + m;
          }
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Invalid byte order: ");
          localStringBuilder.append(mByteOrder);
          throw new IOException(localStringBuilder.toString());
        }
        throw new EOFException();
      }
      throw new EOFException();
    }
    
    public String readLine()
      throws IOException
    {
      Log.d("ExifInterface", "Currently unsupported");
      return null;
    }
    
    public long readLong()
      throws IOException
    {
      mPosition += 8;
      if (mPosition <= mLength)
      {
        int i = mDataInputStream.read();
        int j = mDataInputStream.read();
        int k = mDataInputStream.read();
        int m = mDataInputStream.read();
        int n = mDataInputStream.read();
        int i1 = mDataInputStream.read();
        int i2 = mDataInputStream.read();
        int i3 = mDataInputStream.read();
        if ((i | j | k | m | n | i1 | i2 | i3) >= 0)
        {
          if (mByteOrder == LITTLE_ENDIAN) {
            return (i3 << 56) + (i2 << 48) + (i1 << 40) + (n << 32) + (m << 24) + (k << 16) + (j << 8) + i;
          }
          if (mByteOrder == BIG_ENDIAN) {
            return (i << 56) + (j << 48) + (k << 40) + (m << 32) + (n << 24) + (i1 << 16) + (i2 << 8) + i3;
          }
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Invalid byte order: ");
          localStringBuilder.append(mByteOrder);
          throw new IOException(localStringBuilder.toString());
        }
        throw new EOFException();
      }
      throw new EOFException();
    }
    
    public short readShort()
      throws IOException
    {
      mPosition += 2;
      if (mPosition <= mLength)
      {
        int i = mDataInputStream.read();
        int j = mDataInputStream.read();
        if ((i | j) >= 0)
        {
          if (mByteOrder == LITTLE_ENDIAN) {
            return (short)((j << 8) + i);
          }
          if (mByteOrder == BIG_ENDIAN) {
            return (short)((i << 8) + j);
          }
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Invalid byte order: ");
          localStringBuilder.append(mByteOrder);
          throw new IOException(localStringBuilder.toString());
        }
        throw new EOFException();
      }
      throw new EOFException();
    }
    
    public String readUTF()
      throws IOException
    {
      mPosition += 2;
      return mDataInputStream.readUTF();
    }
    
    public int readUnsignedByte()
      throws IOException
    {
      mPosition += 1;
      return mDataInputStream.readUnsignedByte();
    }
    
    public long readUnsignedInt()
      throws IOException
    {
      return readInt() & 0xFFFFFFFF;
    }
    
    public int readUnsignedShort()
      throws IOException
    {
      mPosition += 2;
      if (mPosition <= mLength)
      {
        int i = mDataInputStream.read();
        int j = mDataInputStream.read();
        if ((i | j) >= 0)
        {
          if (mByteOrder == LITTLE_ENDIAN) {
            return (j << 8) + i;
          }
          if (mByteOrder == BIG_ENDIAN) {
            return (i << 8) + j;
          }
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Invalid byte order: ");
          localStringBuilder.append(mByteOrder);
          throw new IOException(localStringBuilder.toString());
        }
        throw new EOFException();
      }
      throw new EOFException();
    }
    
    public void seek(long paramLong)
      throws IOException
    {
      if (mPosition > paramLong)
      {
        mPosition = 0;
        mDataInputStream.reset();
        mDataInputStream.mark(mLength);
      }
      else
      {
        paramLong -= mPosition;
      }
      if (skipBytes((int)paramLong) == (int)paramLong) {
        return;
      }
      throw new IOException("Couldn't seek up to the byteCount");
    }
    
    public void setByteOrder(ByteOrder paramByteOrder)
    {
      mByteOrder = paramByteOrder;
    }
    
    public int skipBytes(int paramInt)
      throws IOException
    {
      int i = Math.min(paramInt, mLength - mPosition);
      paramInt = 0;
      while (paramInt < i) {
        paramInt += mDataInputStream.skipBytes(i - paramInt);
      }
      mPosition += paramInt;
      return paramInt;
    }
  }
  
  private static class ByteOrderedDataOutputStream
    extends FilterOutputStream
  {
    private ByteOrder mByteOrder;
    private final OutputStream mOutputStream;
    
    public ByteOrderedDataOutputStream(OutputStream paramOutputStream, ByteOrder paramByteOrder)
    {
      super();
      mOutputStream = paramOutputStream;
      mByteOrder = paramByteOrder;
    }
    
    public void setByteOrder(ByteOrder paramByteOrder)
    {
      mByteOrder = paramByteOrder;
    }
    
    public void write(byte[] paramArrayOfByte)
      throws IOException
    {
      mOutputStream.write(paramArrayOfByte);
    }
    
    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      mOutputStream.write(paramArrayOfByte, paramInt1, paramInt2);
    }
    
    public void writeByte(int paramInt)
      throws IOException
    {
      mOutputStream.write(paramInt);
    }
    
    public void writeInt(int paramInt)
      throws IOException
    {
      if (mByteOrder == ByteOrder.LITTLE_ENDIAN)
      {
        mOutputStream.write(paramInt >>> 0 & 0xFF);
        mOutputStream.write(paramInt >>> 8 & 0xFF);
        mOutputStream.write(paramInt >>> 16 & 0xFF);
        mOutputStream.write(paramInt >>> 24 & 0xFF);
      }
      else if (mByteOrder == ByteOrder.BIG_ENDIAN)
      {
        mOutputStream.write(paramInt >>> 24 & 0xFF);
        mOutputStream.write(paramInt >>> 16 & 0xFF);
        mOutputStream.write(paramInt >>> 8 & 0xFF);
        mOutputStream.write(paramInt >>> 0 & 0xFF);
      }
    }
    
    public void writeShort(short paramShort)
      throws IOException
    {
      if (mByteOrder == ByteOrder.LITTLE_ENDIAN)
      {
        mOutputStream.write(paramShort >>> 0 & 0xFF);
        mOutputStream.write(paramShort >>> 8 & 0xFF);
      }
      else if (mByteOrder == ByteOrder.BIG_ENDIAN)
      {
        mOutputStream.write(paramShort >>> 8 & 0xFF);
        mOutputStream.write(paramShort >>> 0 & 0xFF);
      }
    }
    
    public void writeUnsignedInt(long paramLong)
      throws IOException
    {
      writeInt((int)paramLong);
    }
    
    public void writeUnsignedShort(int paramInt)
      throws IOException
    {
      writeShort((short)paramInt);
    }
  }
  
  private static class ExifAttribute
  {
    public final byte[] bytes;
    public final int format;
    public final int numberOfComponents;
    
    private ExifAttribute(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    {
      format = paramInt1;
      numberOfComponents = paramInt2;
      bytes = paramArrayOfByte;
    }
    
    public static ExifAttribute createByte(String paramString)
    {
      if ((paramString.length() == 1) && (paramString.charAt(0) >= '0') && (paramString.charAt(0) <= '1'))
      {
        byte[] arrayOfByte = new byte[1];
        arrayOfByte[0] = ((byte)(byte)(paramString.charAt(0) - '0'));
        return new ExifAttribute(1, arrayOfByte.length, arrayOfByte);
      }
      paramString = paramString.getBytes(ExifInterface.ASCII);
      return new ExifAttribute(1, paramString.length, paramString);
    }
    
    public static ExifAttribute createDouble(double paramDouble, ByteOrder paramByteOrder)
    {
      return createDouble(new double[] { paramDouble }, paramByteOrder);
    }
    
    public static ExifAttribute createDouble(double[] paramArrayOfDouble, ByteOrder paramByteOrder)
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[12] * paramArrayOfDouble.length]);
      localByteBuffer.order(paramByteOrder);
      int i = paramArrayOfDouble.length;
      for (int j = 0; j < i; j++) {
        localByteBuffer.putDouble(paramArrayOfDouble[j]);
      }
      return new ExifAttribute(12, paramArrayOfDouble.length, localByteBuffer.array());
    }
    
    public static ExifAttribute createSLong(int paramInt, ByteOrder paramByteOrder)
    {
      return createSLong(new int[] { paramInt }, paramByteOrder);
    }
    
    public static ExifAttribute createSLong(int[] paramArrayOfInt, ByteOrder paramByteOrder)
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[9] * paramArrayOfInt.length]);
      localByteBuffer.order(paramByteOrder);
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++) {
        localByteBuffer.putInt(paramArrayOfInt[j]);
      }
      return new ExifAttribute(9, paramArrayOfInt.length, localByteBuffer.array());
    }
    
    public static ExifAttribute createSRational(ExifInterface.Rational paramRational, ByteOrder paramByteOrder)
    {
      return createSRational(new ExifInterface.Rational[] { paramRational }, paramByteOrder);
    }
    
    public static ExifAttribute createSRational(ExifInterface.Rational[] paramArrayOfRational, ByteOrder paramByteOrder)
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[10] * paramArrayOfRational.length]);
      localByteBuffer.order(paramByteOrder);
      int i = paramArrayOfRational.length;
      for (int j = 0; j < i; j++)
      {
        paramByteOrder = paramArrayOfRational[j];
        localByteBuffer.putInt((int)numerator);
        localByteBuffer.putInt((int)denominator);
      }
      return new ExifAttribute(10, paramArrayOfRational.length, localByteBuffer.array());
    }
    
    public static ExifAttribute createString(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append('\000');
      paramString = localStringBuilder.toString().getBytes(ExifInterface.ASCII);
      return new ExifAttribute(2, paramString.length, paramString);
    }
    
    public static ExifAttribute createULong(long paramLong, ByteOrder paramByteOrder)
    {
      return createULong(new long[] { paramLong }, paramByteOrder);
    }
    
    public static ExifAttribute createULong(long[] paramArrayOfLong, ByteOrder paramByteOrder)
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * paramArrayOfLong.length]);
      localByteBuffer.order(paramByteOrder);
      int i = paramArrayOfLong.length;
      for (int j = 0; j < i; j++) {
        localByteBuffer.putInt((int)paramArrayOfLong[j]);
      }
      return new ExifAttribute(4, paramArrayOfLong.length, localByteBuffer.array());
    }
    
    public static ExifAttribute createURational(ExifInterface.Rational paramRational, ByteOrder paramByteOrder)
    {
      return createURational(new ExifInterface.Rational[] { paramRational }, paramByteOrder);
    }
    
    public static ExifAttribute createURational(ExifInterface.Rational[] paramArrayOfRational, ByteOrder paramByteOrder)
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * paramArrayOfRational.length]);
      localByteBuffer.order(paramByteOrder);
      int i = paramArrayOfRational.length;
      for (int j = 0; j < i; j++)
      {
        paramByteOrder = paramArrayOfRational[j];
        localByteBuffer.putInt((int)numerator);
        localByteBuffer.putInt((int)denominator);
      }
      return new ExifAttribute(5, paramArrayOfRational.length, localByteBuffer.array());
    }
    
    public static ExifAttribute createUShort(int paramInt, ByteOrder paramByteOrder)
    {
      return createUShort(new int[] { paramInt }, paramByteOrder);
    }
    
    public static ExifAttribute createUShort(int[] paramArrayOfInt, ByteOrder paramByteOrder)
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * paramArrayOfInt.length]);
      localByteBuffer.order(paramByteOrder);
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++) {
        localByteBuffer.putShort((short)paramArrayOfInt[j]);
      }
      return new ExifAttribute(3, paramArrayOfInt.length, localByteBuffer.array());
    }
    
    private Object getValue(ByteOrder paramByteOrder)
    {
      try
      {
        ExifInterface.ByteOrderedDataInputStream localByteOrderedDataInputStream = new android/media/ExifInterface$ByteOrderedDataInputStream;
        localByteOrderedDataInputStream.<init>(bytes);
        try
        {
          localByteOrderedDataInputStream.setByteOrder(paramByteOrder);
          int i = format;
          int j = 0;
          int k = 0;
          int m = 0;
          int n = 0;
          int i1 = 0;
          int i2 = 0;
          int i3 = 0;
          int i4 = 0;
          int i5 = 0;
          switch (i)
          {
          default: 
            return null;
          case 12: 
            paramByteOrder = new double[numberOfComponents];
            while (i5 < numberOfComponents)
            {
              paramByteOrder[i5] = localByteOrderedDataInputStream.readDouble();
              i5++;
            }
            return paramByteOrder;
          case 11: 
            paramByteOrder = new double[numberOfComponents];
            for (i5 = j; i5 < numberOfComponents; i5++) {
              paramByteOrder[i5] = localByteOrderedDataInputStream.readFloat();
            }
            return paramByteOrder;
          case 10: 
            paramByteOrder = new ExifInterface.Rational[numberOfComponents];
            for (i5 = k; i5 < numberOfComponents; i5++) {
              paramByteOrder[i5] = new ExifInterface.Rational(localByteOrderedDataInputStream.readInt(), localByteOrderedDataInputStream.readInt(), null);
            }
            return paramByteOrder;
          case 9: 
            paramByteOrder = new int[numberOfComponents];
            for (i5 = m; i5 < numberOfComponents; i5++) {
              paramByteOrder[i5] = localByteOrderedDataInputStream.readInt();
            }
            return paramByteOrder;
          case 8: 
            paramByteOrder = new int[numberOfComponents];
            for (i5 = n; i5 < numberOfComponents; i5++) {
              paramByteOrder[i5] = localByteOrderedDataInputStream.readShort();
            }
            return paramByteOrder;
          case 5: 
            paramByteOrder = new ExifInterface.Rational[numberOfComponents];
            for (i5 = i1; i5 < numberOfComponents; i5++) {
              paramByteOrder[i5] = new ExifInterface.Rational(localByteOrderedDataInputStream.readUnsignedInt(), localByteOrderedDataInputStream.readUnsignedInt(), null);
            }
            return paramByteOrder;
          case 4: 
            paramByteOrder = new long[numberOfComponents];
            for (i5 = i2; i5 < numberOfComponents; i5++) {
              paramByteOrder[i5] = localByteOrderedDataInputStream.readUnsignedInt();
            }
            return paramByteOrder;
          case 3: 
            paramByteOrder = new int[numberOfComponents];
            for (i5 = i3; i5 < numberOfComponents; i5++) {
              paramByteOrder[i5] = localByteOrderedDataInputStream.readUnsignedShort();
            }
            return paramByteOrder;
          case 2: 
          case 7: 
            j = 0;
            i5 = j;
            if (numberOfComponents >= ExifInterface.EXIF_ASCII_PREFIX.length)
            {
              k = 1;
              for (i5 = i4;; i5++)
              {
                i4 = k;
                if (i5 >= ExifInterface.EXIF_ASCII_PREFIX.length) {
                  break;
                }
                if (bytes[i5] != ExifInterface.EXIF_ASCII_PREFIX[i5])
                {
                  i4 = 0;
                  break;
                }
              }
              i5 = j;
              if (i4 != 0) {
                i5 = ExifInterface.EXIF_ASCII_PREFIX.length;
              }
            }
            paramByteOrder = new java/lang/StringBuilder;
            paramByteOrder.<init>();
            while (i5 < numberOfComponents)
            {
              i4 = bytes[i5];
              if (i4 == 0) {
                break;
              }
              if (i4 >= 32) {
                paramByteOrder.append((char)i4);
              } else {
                paramByteOrder.append('?');
              }
              i5++;
            }
            return paramByteOrder.toString();
          }
          if ((bytes.length == 1) && (bytes[0] >= 0) && (bytes[0] <= 1)) {
            return new String(new char[] { (char)(bytes[0] + 48) });
          }
          paramByteOrder = new String(bytes, ExifInterface.ASCII);
          return paramByteOrder;
        }
        catch (IOException paramByteOrder) {}
        Log.w("ExifInterface", "IOException occurred during reading a value", paramByteOrder);
      }
      catch (IOException paramByteOrder) {}
      return null;
    }
    
    public double getDoubleValue(ByteOrder paramByteOrder)
    {
      paramByteOrder = getValue(paramByteOrder);
      if (paramByteOrder != null)
      {
        if ((paramByteOrder instanceof String)) {
          return Double.parseDouble((String)paramByteOrder);
        }
        if ((paramByteOrder instanceof long[]))
        {
          paramByteOrder = (long[])paramByteOrder;
          if (paramByteOrder.length == 1) {
            return paramByteOrder[0];
          }
          throw new NumberFormatException("There are more than one component");
        }
        if ((paramByteOrder instanceof int[]))
        {
          paramByteOrder = (int[])paramByteOrder;
          if (paramByteOrder.length == 1) {
            return paramByteOrder[0];
          }
          throw new NumberFormatException("There are more than one component");
        }
        if ((paramByteOrder instanceof double[]))
        {
          paramByteOrder = (double[])paramByteOrder;
          if (paramByteOrder.length == 1) {
            return paramByteOrder[0];
          }
          throw new NumberFormatException("There are more than one component");
        }
        if ((paramByteOrder instanceof ExifInterface.Rational[]))
        {
          paramByteOrder = (ExifInterface.Rational[])paramByteOrder;
          if (paramByteOrder.length == 1) {
            return paramByteOrder[0].calculate();
          }
          throw new NumberFormatException("There are more than one component");
        }
        throw new NumberFormatException("Couldn't find a double value");
      }
      throw new NumberFormatException("NULL can't be converted to a double value");
    }
    
    public int getIntValue(ByteOrder paramByteOrder)
    {
      paramByteOrder = getValue(paramByteOrder);
      if (paramByteOrder != null)
      {
        if ((paramByteOrder instanceof String)) {
          return Integer.parseInt((String)paramByteOrder);
        }
        if ((paramByteOrder instanceof long[]))
        {
          paramByteOrder = (long[])paramByteOrder;
          if (paramByteOrder.length == 1) {
            return (int)paramByteOrder[0];
          }
          throw new NumberFormatException("There are more than one component");
        }
        if ((paramByteOrder instanceof int[]))
        {
          paramByteOrder = (int[])paramByteOrder;
          if (paramByteOrder.length == 1) {
            return paramByteOrder[0];
          }
          throw new NumberFormatException("There are more than one component");
        }
        throw new NumberFormatException("Couldn't find a integer value");
      }
      throw new NumberFormatException("NULL can't be converted to a integer value");
    }
    
    public String getStringValue(ByteOrder paramByteOrder)
    {
      Object localObject = getValue(paramByteOrder);
      if (localObject == null) {
        return null;
      }
      if ((localObject instanceof String)) {
        return (String)localObject;
      }
      paramByteOrder = new StringBuilder();
      boolean bool = localObject instanceof long[];
      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      if (bool)
      {
        localObject = (long[])localObject;
        while (m < localObject.length)
        {
          paramByteOrder.append(localObject[m]);
          if (m + 1 != localObject.length) {
            paramByteOrder.append(",");
          }
          m++;
        }
        return paramByteOrder.toString();
      }
      if ((localObject instanceof int[]))
      {
        localObject = (int[])localObject;
        for (m = i; m < localObject.length; m++)
        {
          paramByteOrder.append(localObject[m]);
          if (m + 1 != localObject.length) {
            paramByteOrder.append(",");
          }
        }
        return paramByteOrder.toString();
      }
      if ((localObject instanceof double[]))
      {
        localObject = (double[])localObject;
        for (m = j; m < localObject.length; m++)
        {
          paramByteOrder.append(localObject[m]);
          if (m + 1 != localObject.length) {
            paramByteOrder.append(",");
          }
        }
        return paramByteOrder.toString();
      }
      if ((localObject instanceof ExifInterface.Rational[]))
      {
        localObject = (ExifInterface.Rational[])localObject;
        for (m = k; m < localObject.length; m++)
        {
          paramByteOrder.append(numerator);
          paramByteOrder.append('/');
          paramByteOrder.append(denominator);
          if (m + 1 != localObject.length) {
            paramByteOrder.append(",");
          }
        }
        return paramByteOrder.toString();
      }
      return null;
    }
    
    public int size()
    {
      return ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[format] * numberOfComponents;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("(");
      localStringBuilder.append(ExifInterface.IFD_FORMAT_NAMES[format]);
      localStringBuilder.append(", data length:");
      localStringBuilder.append(bytes.length);
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    }
  }
  
  private static class ExifTag
  {
    public final String name;
    public final int number;
    public final int primaryFormat;
    public final int secondaryFormat;
    
    private ExifTag(String paramString, int paramInt1, int paramInt2)
    {
      name = paramString;
      number = paramInt1;
      primaryFormat = paramInt2;
      secondaryFormat = -1;
    }
    
    private ExifTag(String paramString, int paramInt1, int paramInt2, int paramInt3)
    {
      name = paramString;
      number = paramInt1;
      primaryFormat = paramInt2;
      secondaryFormat = paramInt3;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface IfdType {}
  
  private static class Rational
  {
    public final long denominator;
    public final long numerator;
    
    private Rational(long paramLong1, long paramLong2)
    {
      if (paramLong2 == 0L)
      {
        numerator = 0L;
        denominator = 1L;
        return;
      }
      numerator = paramLong1;
      denominator = paramLong2;
    }
    
    public double calculate()
    {
      return numerator / denominator;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(numerator);
      localStringBuilder.append("/");
      localStringBuilder.append(denominator);
      return localStringBuilder.toString();
    }
  }
}
