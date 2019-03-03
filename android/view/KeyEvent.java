package android.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseIntArray;

public class KeyEvent
  extends InputEvent
  implements Parcelable
{
  public static final int ACTION_DOWN = 0;
  public static final int ACTION_MULTIPLE = 2;
  public static final int ACTION_UP = 1;
  private static final int ASUS_KEYCODE_OFFSET = 800;
  public static final Parcelable.Creator<KeyEvent> CREATOR = new Parcelable.Creator()
  {
    public KeyEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return KeyEvent.createFromParcelBody(paramAnonymousParcel);
    }
    
    public KeyEvent[] newArray(int paramAnonymousInt)
    {
      return new KeyEvent[paramAnonymousInt];
    }
  };
  static final boolean DEBUG = false;
  public static final int FLAG_CANCELED = 32;
  public static final int FLAG_CANCELED_LONG_PRESS = 256;
  public static final int FLAG_EDITOR_ACTION = 16;
  public static final int FLAG_FALLBACK = 1024;
  public static final int FLAG_FROM_SYSTEM = 8;
  public static final int FLAG_KEEP_TOUCH_MODE = 4;
  public static final int FLAG_LONG_PRESS = 128;
  public static final int FLAG_PREDISPATCH = 536870912;
  public static final int FLAG_SOFT_KEYBOARD = 2;
  public static final int FLAG_START_TRACKING = 1073741824;
  public static final int FLAG_TAINTED = Integer.MIN_VALUE;
  public static final int FLAG_TRACKING = 512;
  public static final int FLAG_VIRTUAL_HARD_KEY = 64;
  @Deprecated
  public static final int FLAG_WOKE_HERE = 1;
  public static final int KEYCODE_0 = 7;
  public static final int KEYCODE_1 = 8;
  public static final int KEYCODE_11 = 227;
  public static final int KEYCODE_12 = 228;
  public static final int KEYCODE_2 = 9;
  public static final int KEYCODE_3 = 10;
  public static final int KEYCODE_3D_MODE = 206;
  public static final int KEYCODE_4 = 11;
  public static final int KEYCODE_5 = 12;
  public static final int KEYCODE_6 = 13;
  public static final int KEYCODE_7 = 14;
  public static final int KEYCODE_8 = 15;
  public static final int KEYCODE_9 = 16;
  public static final int KEYCODE_A = 29;
  public static final int KEYCODE_ALL_APPS = 284;
  public static final int KEYCODE_ALT_LEFT = 57;
  public static final int KEYCODE_ALT_RIGHT = 58;
  public static final int KEYCODE_APOSTROPHE = 75;
  public static final int KEYCODE_APP_SWITCH = 187;
  public static final int KEYCODE_ASSIST = 219;
  public static final int KEYCODE_ASUS_KEY_LOCK = 841;
  public static final int KEYCODE_ASUS_KEY_UNLOCK = 842;
  public static final int KEYCODE_AT = 77;
  public static final int KEYCODE_AVR_INPUT = 182;
  public static final int KEYCODE_AVR_POWER = 181;
  public static final int KEYCODE_B = 30;
  public static final int KEYCODE_BACK = 4;
  public static final int KEYCODE_BACKSLASH = 73;
  public static final int KEYCODE_BOOKMARK = 174;
  public static final int KEYCODE_BREAK = 121;
  public static final int KEYCODE_BRIGHTNESS_DOWN = 220;
  public static final int KEYCODE_BRIGHTNESS_UP = 221;
  public static final int KEYCODE_BUTTON_1 = 188;
  public static final int KEYCODE_BUTTON_10 = 197;
  public static final int KEYCODE_BUTTON_11 = 198;
  public static final int KEYCODE_BUTTON_12 = 199;
  public static final int KEYCODE_BUTTON_13 = 200;
  public static final int KEYCODE_BUTTON_14 = 201;
  public static final int KEYCODE_BUTTON_15 = 202;
  public static final int KEYCODE_BUTTON_16 = 203;
  public static final int KEYCODE_BUTTON_2 = 189;
  public static final int KEYCODE_BUTTON_3 = 190;
  public static final int KEYCODE_BUTTON_4 = 191;
  public static final int KEYCODE_BUTTON_5 = 192;
  public static final int KEYCODE_BUTTON_6 = 193;
  public static final int KEYCODE_BUTTON_7 = 194;
  public static final int KEYCODE_BUTTON_8 = 195;
  public static final int KEYCODE_BUTTON_9 = 196;
  public static final int KEYCODE_BUTTON_A = 96;
  public static final int KEYCODE_BUTTON_B = 97;
  public static final int KEYCODE_BUTTON_C = 98;
  public static final int KEYCODE_BUTTON_L1 = 102;
  public static final int KEYCODE_BUTTON_L2 = 104;
  public static final int KEYCODE_BUTTON_MODE = 110;
  public static final int KEYCODE_BUTTON_R1 = 103;
  public static final int KEYCODE_BUTTON_R2 = 105;
  public static final int KEYCODE_BUTTON_SELECT = 109;
  public static final int KEYCODE_BUTTON_START = 108;
  public static final int KEYCODE_BUTTON_THUMBL = 106;
  public static final int KEYCODE_BUTTON_THUMBR = 107;
  public static final int KEYCODE_BUTTON_X = 99;
  public static final int KEYCODE_BUTTON_Y = 100;
  public static final int KEYCODE_BUTTON_Z = 101;
  public static final int KEYCODE_C = 31;
  public static final int KEYCODE_CALCULATOR = 210;
  public static final int KEYCODE_CALENDAR = 208;
  public static final int KEYCODE_CALL = 5;
  public static final int KEYCODE_CAMERA = 27;
  public static final int KEYCODE_CAMERA_RECORD = 799;
  public static final int KEYCODE_CAPS_LOCK = 115;
  public static final int KEYCODE_CAPTIONS = 175;
  public static final int KEYCODE_CHANNEL_DOWN = 167;
  public static final int KEYCODE_CHANNEL_UP = 166;
  public static final int KEYCODE_CLEAR = 28;
  public static final int KEYCODE_COMMA = 55;
  public static final int KEYCODE_CONTACTS = 207;
  public static final int KEYCODE_COPY = 278;
  public static final int KEYCODE_CTRL_LEFT = 113;
  public static final int KEYCODE_CTRL_RIGHT = 114;
  public static final int KEYCODE_CUT = 277;
  public static final int KEYCODE_D = 32;
  public static final int KEYCODE_DEL = 67;
  public static final int KEYCODE_DPAD_CENTER = 23;
  public static final int KEYCODE_DPAD_DOWN = 20;
  public static final int KEYCODE_DPAD_DOWN_LEFT = 269;
  public static final int KEYCODE_DPAD_DOWN_RIGHT = 271;
  public static final int KEYCODE_DPAD_LEFT = 21;
  public static final int KEYCODE_DPAD_RIGHT = 22;
  public static final int KEYCODE_DPAD_UP = 19;
  public static final int KEYCODE_DPAD_UP_LEFT = 268;
  public static final int KEYCODE_DPAD_UP_RIGHT = 270;
  public static final int KEYCODE_DVR = 173;
  public static final int KEYCODE_E = 33;
  public static final int KEYCODE_EISU = 212;
  public static final int KEYCODE_ENDCALL = 6;
  public static final int KEYCODE_ENTER = 66;
  public static final int KEYCODE_ENVELOPE = 65;
  public static final int KEYCODE_EQUALS = 70;
  public static final int KEYCODE_ESCAPE = 111;
  public static final int KEYCODE_EXPLORER = 64;
  public static final int KEYCODE_F = 34;
  public static final int KEYCODE_F1 = 131;
  public static final int KEYCODE_F10 = 140;
  public static final int KEYCODE_F11 = 141;
  public static final int KEYCODE_F12 = 142;
  public static final int KEYCODE_F2 = 132;
  public static final int KEYCODE_F3 = 133;
  public static final int KEYCODE_F4 = 134;
  public static final int KEYCODE_F5 = 135;
  public static final int KEYCODE_F6 = 136;
  public static final int KEYCODE_F7 = 137;
  public static final int KEYCODE_F8 = 138;
  public static final int KEYCODE_F9 = 139;
  public static final int KEYCODE_FINGERPRINT_DTAP = 832;
  public static final int KEYCODE_FINGERPRINT_EARLYUPDONE = 835;
  public static final int KEYCODE_FINGERPRINT_EARLYUPFAIL = 836;
  public static final int KEYCODE_FINGERPRINT_EARLYUPRESET = 838;
  public static final int KEYCODE_FINGERPRINT_EARLYWAKEUP = 834;
  public static final int KEYCODE_FINGERPRINT_LONGPRESS = 833;
  public static final int KEYCODE_FINGERPRINT_POWERKEY = 837;
  public static final int KEYCODE_FINGERPRINT_SWIPE_DOWN = 828;
  public static final int KEYCODE_FINGERPRINT_SWIPE_LEFT = 829;
  public static final int KEYCODE_FINGERPRINT_SWIPE_RIGHT = 830;
  public static final int KEYCODE_FINGERPRINT_SWIPE_UP = 827;
  public static final int KEYCODE_FINGERPRINT_TAP = 831;
  public static final int KEYCODE_FOCUS = 80;
  public static final int KEYCODE_FORWARD = 125;
  public static final int KEYCODE_FORWARD_DEL = 112;
  public static final int KEYCODE_FUNCTION = 119;
  public static final int KEYCODE_G = 35;
  public static final int KEYCODE_GAMEGENIE_LOCK = 839;
  public static final int KEYCODE_GAMEGENIE_UNLOCK = 840;
  public static final int KEYCODE_GESTURE_C = 821;
  public static final int KEYCODE_GESTURE_DOUBLE_CLICK = 820;
  public static final int KEYCODE_GESTURE_E = 822;
  public static final int KEYCODE_GESTURE_S = 823;
  public static final int KEYCODE_GESTURE_V = 824;
  public static final int KEYCODE_GESTURE_W = 825;
  public static final int KEYCODE_GESTURE_WAKE = 819;
  public static final int KEYCODE_GESTURE_Z = 826;
  public static final int KEYCODE_GRAVE = 68;
  public static final int KEYCODE_GUIDE = 172;
  public static final int KEYCODE_H = 36;
  public static final int KEYCODE_HEADSETHOOK = 79;
  public static final int KEYCODE_HELP = 259;
  public static final int KEYCODE_HENKAN = 214;
  public static final int KEYCODE_HOME = 3;
  public static final int KEYCODE_I = 37;
  public static final int KEYCODE_INFO = 165;
  public static final int KEYCODE_INSERT = 124;
  public static final int KEYCODE_J = 38;
  public static final int KEYCODE_K = 39;
  public static final int KEYCODE_KANA = 218;
  public static final int KEYCODE_KATAKANA_HIRAGANA = 215;
  public static final int KEYCODE_L = 40;
  public static final int KEYCODE_LANGUAGE_SWITCH = 204;
  public static final int KEYCODE_LAST_CHANNEL = 229;
  public static final int KEYCODE_LEFT_BRACKET = 71;
  public static final int KEYCODE_M = 41;
  public static final int KEYCODE_MANNER_MODE = 205;
  public static final int KEYCODE_MEDIA_AUDIO_TRACK = 222;
  public static final int KEYCODE_MEDIA_CLOSE = 128;
  public static final int KEYCODE_MEDIA_EJECT = 129;
  public static final int KEYCODE_MEDIA_FAST_FORWARD = 90;
  public static final int KEYCODE_MEDIA_NEXT = 87;
  public static final int KEYCODE_MEDIA_PAUSE = 127;
  public static final int KEYCODE_MEDIA_PLAY = 126;
  public static final int KEYCODE_MEDIA_PLAY_PAUSE = 85;
  public static final int KEYCODE_MEDIA_PREVIOUS = 88;
  public static final int KEYCODE_MEDIA_RECORD = 130;
  public static final int KEYCODE_MEDIA_REWIND = 89;
  public static final int KEYCODE_MEDIA_SKIP_BACKWARD = 273;
  public static final int KEYCODE_MEDIA_SKIP_FORWARD = 272;
  public static final int KEYCODE_MEDIA_STEP_BACKWARD = 275;
  public static final int KEYCODE_MEDIA_STEP_FORWARD = 274;
  public static final int KEYCODE_MEDIA_STOP = 86;
  public static final int KEYCODE_MEDIA_TOP_MENU = 226;
  public static final int KEYCODE_MENU = 82;
  public static final int KEYCODE_META_LEFT = 117;
  public static final int KEYCODE_META_RIGHT = 118;
  public static final int KEYCODE_MINUS = 69;
  public static final int KEYCODE_MOVE_END = 123;
  public static final int KEYCODE_MOVE_HOME = 122;
  public static final int KEYCODE_MUHENKAN = 213;
  public static final int KEYCODE_MUSIC = 209;
  public static final int KEYCODE_MUTE = 91;
  public static final int KEYCODE_N = 42;
  public static final int KEYCODE_NAVIGATE_IN = 262;
  public static final int KEYCODE_NAVIGATE_NEXT = 261;
  public static final int KEYCODE_NAVIGATE_OUT = 263;
  public static final int KEYCODE_NAVIGATE_PREVIOUS = 260;
  public static final int KEYCODE_NOTIFICATION = 83;
  public static final int KEYCODE_NUM = 78;
  public static final int KEYCODE_NUMPAD_0 = 144;
  public static final int KEYCODE_NUMPAD_1 = 145;
  public static final int KEYCODE_NUMPAD_2 = 146;
  public static final int KEYCODE_NUMPAD_3 = 147;
  public static final int KEYCODE_NUMPAD_4 = 148;
  public static final int KEYCODE_NUMPAD_5 = 149;
  public static final int KEYCODE_NUMPAD_6 = 150;
  public static final int KEYCODE_NUMPAD_7 = 151;
  public static final int KEYCODE_NUMPAD_8 = 152;
  public static final int KEYCODE_NUMPAD_9 = 153;
  public static final int KEYCODE_NUMPAD_ADD = 157;
  public static final int KEYCODE_NUMPAD_COMMA = 159;
  public static final int KEYCODE_NUMPAD_DIVIDE = 154;
  public static final int KEYCODE_NUMPAD_DOT = 158;
  public static final int KEYCODE_NUMPAD_ENTER = 160;
  public static final int KEYCODE_NUMPAD_EQUALS = 161;
  public static final int KEYCODE_NUMPAD_LEFT_PAREN = 162;
  public static final int KEYCODE_NUMPAD_MULTIPLY = 155;
  public static final int KEYCODE_NUMPAD_RIGHT_PAREN = 163;
  public static final int KEYCODE_NUMPAD_SUBTRACT = 156;
  public static final int KEYCODE_NUM_LOCK = 143;
  public static final int KEYCODE_O = 43;
  public static final int KEYCODE_P = 44;
  public static final int KEYCODE_PAGE_DOWN = 93;
  public static final int KEYCODE_PAGE_UP = 92;
  public static final int KEYCODE_PAIRING = 225;
  public static final int KEYCODE_PASTE = 279;
  public static final int KEYCODE_PERIOD = 56;
  public static final int KEYCODE_PICTSYMBOLS = 94;
  public static final int KEYCODE_PLUS = 81;
  public static final int KEYCODE_POUND = 18;
  public static final int KEYCODE_POWER = 26;
  public static final int KEYCODE_PROG_BLUE = 186;
  public static final int KEYCODE_PROG_GREEN = 184;
  public static final int KEYCODE_PROG_RED = 183;
  public static final int KEYCODE_PROG_YELLOW = 185;
  public static final int KEYCODE_Q = 45;
  public static final int KEYCODE_R = 46;
  public static final int KEYCODE_REFRESH = 285;
  public static final int KEYCODE_RIGHT_BRACKET = 72;
  public static final int KEYCODE_RO = 217;
  public static final int KEYCODE_S = 47;
  public static final int KEYCODE_SCROLL_LOCK = 116;
  public static final int KEYCODE_SEARCH = 84;
  public static final int KEYCODE_SEMICOLON = 74;
  public static final int KEYCODE_SETTINGS = 176;
  public static final int KEYCODE_SHIFT_LEFT = 59;
  public static final int KEYCODE_SHIFT_RIGHT = 60;
  public static final int KEYCODE_SLASH = 76;
  public static final int KEYCODE_SLEEP = 223;
  public static final int KEYCODE_SOFT_LEFT = 1;
  public static final int KEYCODE_SOFT_RIGHT = 2;
  public static final int KEYCODE_SOFT_SLEEP = 276;
  public static final int KEYCODE_SPACE = 62;
  public static final int KEYCODE_STAR = 17;
  public static final int KEYCODE_STB_INPUT = 180;
  public static final int KEYCODE_STB_POWER = 179;
  public static final int KEYCODE_STEM_1 = 265;
  public static final int KEYCODE_STEM_2 = 266;
  public static final int KEYCODE_STEM_3 = 267;
  public static final int KEYCODE_STEM_PRIMARY = 264;
  public static final int KEYCODE_SWITCH_CHARSET = 95;
  public static final int KEYCODE_SYM = 63;
  public static final int KEYCODE_SYSRQ = 120;
  public static final int KEYCODE_SYSTEM_NAVIGATION_DOWN = 281;
  public static final int KEYCODE_SYSTEM_NAVIGATION_LEFT = 282;
  public static final int KEYCODE_SYSTEM_NAVIGATION_RIGHT = 283;
  public static final int KEYCODE_SYSTEM_NAVIGATION_UP = 280;
  public static final int KEYCODE_T = 48;
  public static final int KEYCODE_TAB = 61;
  public static final int KEYCODE_TV = 170;
  public static final int KEYCODE_TV_ANTENNA_CABLE = 242;
  public static final int KEYCODE_TV_AUDIO_DESCRIPTION = 252;
  public static final int KEYCODE_TV_AUDIO_DESCRIPTION_MIX_DOWN = 254;
  public static final int KEYCODE_TV_AUDIO_DESCRIPTION_MIX_UP = 253;
  public static final int KEYCODE_TV_CONTENTS_MENU = 256;
  public static final int KEYCODE_TV_DATA_SERVICE = 230;
  public static final int KEYCODE_TV_INPUT = 178;
  public static final int KEYCODE_TV_INPUT_COMPONENT_1 = 249;
  public static final int KEYCODE_TV_INPUT_COMPONENT_2 = 250;
  public static final int KEYCODE_TV_INPUT_COMPOSITE_1 = 247;
  public static final int KEYCODE_TV_INPUT_COMPOSITE_2 = 248;
  public static final int KEYCODE_TV_INPUT_HDMI_1 = 243;
  public static final int KEYCODE_TV_INPUT_HDMI_2 = 244;
  public static final int KEYCODE_TV_INPUT_HDMI_3 = 245;
  public static final int KEYCODE_TV_INPUT_HDMI_4 = 246;
  public static final int KEYCODE_TV_INPUT_VGA_1 = 251;
  public static final int KEYCODE_TV_MEDIA_CONTEXT_MENU = 257;
  public static final int KEYCODE_TV_NETWORK = 241;
  public static final int KEYCODE_TV_NUMBER_ENTRY = 234;
  public static final int KEYCODE_TV_POWER = 177;
  public static final int KEYCODE_TV_RADIO_SERVICE = 232;
  public static final int KEYCODE_TV_SATELLITE = 237;
  public static final int KEYCODE_TV_SATELLITE_BS = 238;
  public static final int KEYCODE_TV_SATELLITE_CS = 239;
  public static final int KEYCODE_TV_SATELLITE_SERVICE = 240;
  public static final int KEYCODE_TV_TELETEXT = 233;
  public static final int KEYCODE_TV_TERRESTRIAL_ANALOG = 235;
  public static final int KEYCODE_TV_TERRESTRIAL_DIGITAL = 236;
  public static final int KEYCODE_TV_TIMER_PROGRAMMING = 258;
  public static final int KEYCODE_TV_ZOOM_MODE = 255;
  public static final int KEYCODE_U = 49;
  public static final int KEYCODE_UNKNOWN = 0;
  public static final int KEYCODE_V = 50;
  public static final int KEYCODE_VOICE_ASSIST = 231;
  public static final int KEYCODE_VOLUME_DOWN = 25;
  public static final int KEYCODE_VOLUME_MUTE = 164;
  public static final int KEYCODE_VOLUME_UP = 24;
  public static final int KEYCODE_W = 51;
  public static final int KEYCODE_WAKEUP = 224;
  public static final int KEYCODE_WINDOW = 171;
  public static final int KEYCODE_X = 52;
  public static final int KEYCODE_Y = 53;
  public static final int KEYCODE_YEN = 216;
  public static final int KEYCODE_Z = 54;
  public static final int KEYCODE_ZENKAKU_HANKAKU = 211;
  public static final int KEYCODE_ZOOM_IN = 168;
  public static final int KEYCODE_ZOOM_OUT = 169;
  private static final String LABEL_PREFIX = "KEYCODE_";
  private static final int LAST_KEYCODE = 842;
  @Deprecated
  public static final int MAX_KEYCODE = 84;
  private static final int MAX_RECYCLED = 10;
  private static final int META_ALL_MASK = 7827711;
  public static final int META_ALT_LEFT_ON = 16;
  public static final int META_ALT_LOCKED = 512;
  public static final int META_ALT_MASK = 50;
  public static final int META_ALT_ON = 2;
  public static final int META_ALT_RIGHT_ON = 32;
  public static final int META_CAPS_LOCK_ON = 1048576;
  public static final int META_CAP_LOCKED = 256;
  public static final int META_CTRL_LEFT_ON = 8192;
  public static final int META_CTRL_MASK = 28672;
  public static final int META_CTRL_ON = 4096;
  public static final int META_CTRL_RIGHT_ON = 16384;
  public static final int META_FUNCTION_ON = 8;
  private static final int META_INVALID_MODIFIER_MASK = 7343872;
  private static final int META_LOCK_MASK = 7340032;
  public static final int META_META_LEFT_ON = 131072;
  public static final int META_META_MASK = 458752;
  public static final int META_META_ON = 65536;
  public static final int META_META_RIGHT_ON = 262144;
  private static final int META_MODIFIER_MASK = 487679;
  public static final int META_NUM_LOCK_ON = 2097152;
  public static final int META_SCROLL_LOCK_ON = 4194304;
  public static final int META_SELECTING = 2048;
  public static final int META_SHIFT_LEFT_ON = 64;
  public static final int META_SHIFT_MASK = 193;
  public static final int META_SHIFT_ON = 1;
  public static final int META_SHIFT_RIGHT_ON = 128;
  private static final String[] META_SYMBOLIC_NAMES = { "META_SHIFT_ON", "META_ALT_ON", "META_SYM_ON", "META_FUNCTION_ON", "META_ALT_LEFT_ON", "META_ALT_RIGHT_ON", "META_SHIFT_LEFT_ON", "META_SHIFT_RIGHT_ON", "META_CAP_LOCKED", "META_ALT_LOCKED", "META_SYM_LOCKED", "0x00000800", "META_CTRL_ON", "META_CTRL_LEFT_ON", "META_CTRL_RIGHT_ON", "0x00008000", "META_META_ON", "META_META_LEFT_ON", "META_META_RIGHT_ON", "0x00080000", "META_CAPS_LOCK_ON", "META_NUM_LOCK_ON", "META_SCROLL_LOCK_ON", "0x00800000", "0x01000000", "0x02000000", "0x04000000", "0x08000000", "0x10000000", "0x20000000", "0x40000000", "0x80000000" };
  public static final int META_SYM_LOCKED = 1024;
  public static final int META_SYM_ON = 4;
  private static final int META_SYNTHETIC_MASK = 3840;
  static final String TAG = "KeyEvent";
  private static final Object gRecyclerLock = new Object();
  private static KeyEvent gRecyclerTop;
  private static int gRecyclerUsed;
  private int mAction;
  private String mCharacters;
  private int mDeviceId;
  private long mDownTime;
  private long mEventTime;
  private int mFlags;
  private int mKeyCode;
  private int mMetaState;
  private KeyEvent mNext;
  private int mRepeatCount;
  private int mScanCode;
  private int mSource;
  
  private KeyEvent() {}
  
  public KeyEvent(int paramInt1, int paramInt2)
  {
    mAction = paramInt1;
    mKeyCode = paramInt2;
    mRepeatCount = 0;
    mDeviceId = -1;
  }
  
  public KeyEvent(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3)
  {
    mDownTime = paramLong1;
    mEventTime = paramLong2;
    mAction = paramInt1;
    mKeyCode = paramInt2;
    mRepeatCount = paramInt3;
    mDeviceId = -1;
  }
  
  public KeyEvent(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mDownTime = paramLong1;
    mEventTime = paramLong2;
    mAction = paramInt1;
    mKeyCode = paramInt2;
    mRepeatCount = paramInt3;
    mMetaState = paramInt4;
    mDeviceId = -1;
  }
  
  public KeyEvent(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    mDownTime = paramLong1;
    mEventTime = paramLong2;
    mAction = paramInt1;
    mKeyCode = paramInt2;
    mRepeatCount = paramInt3;
    mMetaState = paramInt4;
    mDeviceId = paramInt5;
    mScanCode = paramInt6;
  }
  
  public KeyEvent(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    mDownTime = paramLong1;
    mEventTime = paramLong2;
    mAction = paramInt1;
    mKeyCode = paramInt2;
    mRepeatCount = paramInt3;
    mMetaState = paramInt4;
    mDeviceId = paramInt5;
    mScanCode = paramInt6;
    mFlags = paramInt7;
  }
  
  public KeyEvent(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    mDownTime = paramLong1;
    mEventTime = paramLong2;
    mAction = paramInt1;
    mKeyCode = paramInt2;
    mRepeatCount = paramInt3;
    mMetaState = paramInt4;
    mDeviceId = paramInt5;
    mScanCode = paramInt6;
    mFlags = paramInt7;
    mSource = paramInt8;
  }
  
  public KeyEvent(long paramLong, String paramString, int paramInt1, int paramInt2)
  {
    mDownTime = paramLong;
    mEventTime = paramLong;
    mCharacters = paramString;
    mAction = 2;
    mKeyCode = 0;
    mRepeatCount = 0;
    mDeviceId = paramInt1;
    mFlags = paramInt2;
    mSource = 257;
  }
  
  private KeyEvent(Parcel paramParcel)
  {
    mDeviceId = paramParcel.readInt();
    mSource = paramParcel.readInt();
    mAction = paramParcel.readInt();
    mKeyCode = paramParcel.readInt();
    mRepeatCount = paramParcel.readInt();
    mMetaState = paramParcel.readInt();
    mScanCode = paramParcel.readInt();
    mFlags = paramParcel.readInt();
    mDownTime = paramParcel.readLong();
    mEventTime = paramParcel.readLong();
  }
  
  public KeyEvent(KeyEvent paramKeyEvent)
  {
    mDownTime = mDownTime;
    mEventTime = mEventTime;
    mAction = mAction;
    mKeyCode = mKeyCode;
    mRepeatCount = mRepeatCount;
    mMetaState = mMetaState;
    mDeviceId = mDeviceId;
    mSource = mSource;
    mScanCode = mScanCode;
    mFlags = mFlags;
    mCharacters = mCharacters;
  }
  
  private KeyEvent(KeyEvent paramKeyEvent, int paramInt)
  {
    mDownTime = mDownTime;
    mEventTime = mEventTime;
    mAction = paramInt;
    mKeyCode = mKeyCode;
    mRepeatCount = mRepeatCount;
    mMetaState = mMetaState;
    mDeviceId = mDeviceId;
    mSource = mSource;
    mScanCode = mScanCode;
    mFlags = mFlags;
  }
  
  @Deprecated
  public KeyEvent(KeyEvent paramKeyEvent, long paramLong, int paramInt)
  {
    mDownTime = mDownTime;
    mEventTime = paramLong;
    mAction = mAction;
    mKeyCode = mKeyCode;
    mRepeatCount = paramInt;
    mMetaState = mMetaState;
    mDeviceId = mDeviceId;
    mSource = mSource;
    mScanCode = mScanCode;
    mFlags = mFlags;
    mCharacters = mCharacters;
  }
  
  public static String actionToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 2: 
      return "ACTION_MULTIPLE";
    case 1: 
      return "ACTION_UP";
    }
    return "ACTION_DOWN";
  }
  
  public static KeyEvent changeAction(KeyEvent paramKeyEvent, int paramInt)
  {
    return new KeyEvent(paramKeyEvent, paramInt);
  }
  
  public static KeyEvent changeFlags(KeyEvent paramKeyEvent, int paramInt)
  {
    paramKeyEvent = new KeyEvent(paramKeyEvent);
    mFlags = paramInt;
    return paramKeyEvent;
  }
  
  public static KeyEvent changeTimeRepeat(KeyEvent paramKeyEvent, long paramLong, int paramInt)
  {
    return new KeyEvent(paramKeyEvent, paramLong, paramInt);
  }
  
  public static KeyEvent changeTimeRepeat(KeyEvent paramKeyEvent, long paramLong, int paramInt1, int paramInt2)
  {
    paramKeyEvent = new KeyEvent(paramKeyEvent);
    mEventTime = paramLong;
    mRepeatCount = paramInt1;
    mFlags = paramInt2;
    return paramKeyEvent;
  }
  
  public static KeyEvent createFromParcelBody(Parcel paramParcel)
  {
    return new KeyEvent(paramParcel);
  }
  
  public static int getDeadChar(int paramInt1, int paramInt2)
  {
    return KeyCharacterMap.getDeadChar(paramInt1, paramInt2);
  }
  
  public static int getMaxKeyCode()
  {
    return 842;
  }
  
  public static int getModifierMetaStateMask()
  {
    return 487679;
  }
  
  public static final boolean isAltKey(int paramInt)
  {
    boolean bool;
    if ((paramInt != 57) && (paramInt != 58)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static final boolean isConfirmKey(int paramInt)
  {
    return (paramInt == 23) || (paramInt == 62) || (paramInt == 66) || (paramInt == 160);
  }
  
  public static final boolean isGamepadButton(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        return false;
      }
      break;
    }
    return true;
  }
  
  public static final boolean isMediaKey(int paramInt)
  {
    if ((paramInt != 79) && (paramInt != 130)) {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          return false;
        }
        break;
      }
    }
    return true;
  }
  
  public static final boolean isMetaKey(int paramInt)
  {
    boolean bool;
    if ((paramInt != 117) && (paramInt != 118)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isModifierKey(int paramInt)
  {
    if ((paramInt != 63) && (paramInt != 78)) {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            return false;
          }
          break;
        }
        break;
      }
    }
    return true;
  }
  
  public static final boolean isSystemKey(int paramInt)
  {
    if ((paramInt != 82) && (paramInt != 130) && (paramInt != 164)) {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            switch (paramInt)
            {
            default: 
              switch (paramInt)
              {
              default: 
                switch (paramInt)
                {
                default: 
                  switch (paramInt)
                  {
                  default: 
                    return false;
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
        }
        break;
      }
    }
    return true;
  }
  
  public static final boolean isWakeKey(int paramInt)
  {
    if (paramInt != 82) {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          return false;
        }
        break;
      }
    }
    return true;
  }
  
  public static int keyCodeFromString(String paramString)
  {
    String str = paramString;
    int i;
    if (paramString.startsWith("KEYCODE_"))
    {
      str = paramString.substring("KEYCODE_".length());
      i = nativeKeyCodeFromString(str);
      if (i > 0) {
        return i;
      }
    }
    try
    {
      i = Integer.parseInt(str, 10);
      return i;
    }
    catch (NumberFormatException paramString) {}
    return 0;
  }
  
  public static String keyCodeToString(int paramInt)
  {
    String str = nativeKeyCodeToString(paramInt);
    if (str != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("KEYCODE_");
      localStringBuilder.append(str);
      str = localStringBuilder.toString();
    }
    else
    {
      str = Integer.toString(paramInt);
    }
    return str;
  }
  
  private static int metaStateFilterDirectionalModifiers(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = 0;
    int j;
    if ((paramInt2 & paramInt3) != 0) {
      j = 1;
    } else {
      j = 0;
    }
    int k = paramInt4 | paramInt5;
    if ((paramInt2 & k) != 0) {
      i = 1;
    }
    if (j != 0)
    {
      if (i == 0) {
        return k & paramInt1;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("modifiers must not contain ");
      localStringBuilder.append(metaStateToString(paramInt3));
      localStringBuilder.append(" combined with ");
      localStringBuilder.append(metaStateToString(paramInt4));
      localStringBuilder.append(" or ");
      localStringBuilder.append(metaStateToString(paramInt5));
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    if (i != 0) {
      return paramInt3 & paramInt1;
    }
    return paramInt1;
  }
  
  public static boolean metaStateHasModifiers(int paramInt1, int paramInt2)
  {
    if ((0x700F00 & paramInt2) == 0)
    {
      paramInt1 = normalizeMetaState(paramInt1);
      boolean bool = true;
      if (metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(paramInt1 & 0x770FF, paramInt2, 1, 64, 128), paramInt2, 2, 16, 32), paramInt2, 4096, 8192, 16384), paramInt2, 65536, 131072, 262144) != paramInt2) {
        bool = false;
      }
      return bool;
    }
    throw new IllegalArgumentException("modifiers must not contain META_CAPS_LOCK_ON, META_NUM_LOCK_ON, META_SCROLL_LOCK_ON, META_CAP_LOCKED, META_ALT_LOCKED, META_SYM_LOCKED, or META_SELECTING");
  }
  
  public static boolean metaStateHasNoModifiers(int paramInt)
  {
    boolean bool;
    if ((normalizeMetaState(paramInt) & 0x770FF) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static String metaStateToString(int paramInt)
  {
    if (paramInt == 0) {
      return "0";
    }
    Object localObject1 = null;
    int i = paramInt;
    paramInt = 0;
    while (i != 0)
    {
      int j;
      if ((i & 0x1) != 0) {
        j = 1;
      } else {
        j = 0;
      }
      i >>>= 1;
      Object localObject2 = localObject1;
      if (j != 0)
      {
        localObject2 = META_SYMBOLIC_NAMES[paramInt];
        if (localObject1 == null)
        {
          if (i == 0) {
            return localObject2;
          }
          localObject2 = new StringBuilder((String)localObject2);
        }
        else
        {
          localObject1.append('|');
          localObject1.append((String)localObject2);
          localObject2 = localObject1;
        }
      }
      paramInt++;
      localObject1 = localObject2;
    }
    return localObject1.toString();
  }
  
  private static native int nativeKeyCodeFromString(String paramString);
  
  private static native String nativeKeyCodeToString(int paramInt);
  
  public static int normalizeMetaState(int paramInt)
  {
    int i = paramInt;
    if ((paramInt & 0xC0) != 0) {
      i = paramInt | 0x1;
    }
    int j = i;
    if ((i & 0x30) != 0) {
      j = i | 0x2;
    }
    paramInt = j;
    if ((j & 0x6000) != 0) {
      paramInt = j | 0x1000;
    }
    i = paramInt;
    if ((0x60000 & paramInt) != 0) {
      i = paramInt | 0x10000;
    }
    paramInt = i;
    if ((i & 0x100) != 0) {
      paramInt = i | 0x100000;
    }
    i = paramInt;
    if ((paramInt & 0x200) != 0) {
      i = paramInt | 0x2;
    }
    paramInt = i;
    if ((i & 0x400) != 0) {
      paramInt = i | 0x4;
    }
    return 0x7770FF & paramInt;
  }
  
  private static KeyEvent obtain()
  {
    synchronized (gRecyclerLock)
    {
      KeyEvent localKeyEvent = gRecyclerTop;
      if (localKeyEvent == null)
      {
        localKeyEvent = new android/view/KeyEvent;
        localKeyEvent.<init>();
        return localKeyEvent;
      }
      gRecyclerTop = mNext;
      gRecyclerUsed -= 1;
      mNext = null;
      localKeyEvent.prepareForReuse();
      return localKeyEvent;
    }
  }
  
  public static KeyEvent obtain(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, String paramString)
  {
    KeyEvent localKeyEvent = obtain();
    mDownTime = paramLong1;
    mEventTime = paramLong2;
    mAction = paramInt1;
    mKeyCode = paramInt2;
    mRepeatCount = paramInt3;
    mMetaState = paramInt4;
    mDeviceId = paramInt5;
    mScanCode = paramInt6;
    mFlags = paramInt7;
    mSource = paramInt8;
    mCharacters = paramString;
    return localKeyEvent;
  }
  
  public static KeyEvent obtain(KeyEvent paramKeyEvent)
  {
    KeyEvent localKeyEvent = obtain();
    mDownTime = mDownTime;
    mEventTime = mEventTime;
    mAction = mAction;
    mKeyCode = mKeyCode;
    mRepeatCount = mRepeatCount;
    mMetaState = mMetaState;
    mDeviceId = mDeviceId;
    mScanCode = mScanCode;
    mFlags = mFlags;
    mSource = mSource;
    mCharacters = mCharacters;
    return localKeyEvent;
  }
  
  public final void cancel()
  {
    mFlags |= 0x20;
  }
  
  public KeyEvent copy()
  {
    return obtain(this);
  }
  
  @Deprecated
  public final boolean dispatch(Callback paramCallback)
  {
    return dispatch(paramCallback, null, null);
  }
  
  public final boolean dispatch(Callback paramCallback, DispatcherState paramDispatcherState, Object paramObject)
  {
    switch (mAction)
    {
    default: 
      return false;
    case 2: 
      int i = mRepeatCount;
      int j = mKeyCode;
      if (paramCallback.onKeyMultiple(j, i, this)) {
        return true;
      }
      if (j != 0)
      {
        mAction = 0;
        mRepeatCount = 0;
        bool1 = paramCallback.onKeyDown(j, this);
        if (bool1)
        {
          mAction = 1;
          paramCallback.onKeyUp(j, this);
        }
        mAction = 2;
        mRepeatCount = i;
        return bool1;
      }
      return false;
    case 1: 
      if (paramDispatcherState != null) {
        paramDispatcherState.handleUpEvent(this);
      }
      return paramCallback.onKeyUp(mKeyCode, this);
    }
    mFlags &= 0xBFFFFFFF;
    boolean bool2 = paramCallback.onKeyDown(mKeyCode, this);
    boolean bool1 = bool2;
    if (paramDispatcherState != null) {
      if ((bool2) && (mRepeatCount == 0) && ((mFlags & 0x40000000) != 0))
      {
        paramDispatcherState.startTracking(this, paramObject);
        bool1 = bool2;
      }
      else
      {
        bool1 = bool2;
        if (isLongPress())
        {
          bool1 = bool2;
          if (paramDispatcherState.isTracking(this))
          {
            bool1 = bool2;
            try
            {
              if (paramCallback.onKeyLongPress(mKeyCode, this))
              {
                paramDispatcherState.performedLongPress(this);
                bool1 = true;
              }
            }
            catch (AbstractMethodError paramCallback)
            {
              bool1 = bool2;
            }
          }
        }
      }
    }
    return bool1;
  }
  
  public final int getAction()
  {
    return mAction;
  }
  
  public final String getCharacters()
  {
    return mCharacters;
  }
  
  public final int getDeviceId()
  {
    return mDeviceId;
  }
  
  public char getDisplayLabel()
  {
    return getKeyCharacterMap().getDisplayLabel(mKeyCode);
  }
  
  public final long getDownTime()
  {
    return mDownTime;
  }
  
  public final long getEventTime()
  {
    return mEventTime;
  }
  
  public final long getEventTimeNano()
  {
    return mEventTime * 1000000L;
  }
  
  public final int getFlags()
  {
    return mFlags;
  }
  
  public final KeyCharacterMap getKeyCharacterMap()
  {
    return KeyCharacterMap.load(mDeviceId);
  }
  
  public final int getKeyCode()
  {
    return mKeyCode;
  }
  
  @Deprecated
  public boolean getKeyData(KeyCharacterMap.KeyData paramKeyData)
  {
    return getKeyCharacterMap().getKeyData(mKeyCode, paramKeyData);
  }
  
  @Deprecated
  public final int getKeyboardDevice()
  {
    return mDeviceId;
  }
  
  public char getMatch(char[] paramArrayOfChar)
  {
    return getMatch(paramArrayOfChar, 0);
  }
  
  public char getMatch(char[] paramArrayOfChar, int paramInt)
  {
    return getKeyCharacterMap().getMatch(mKeyCode, paramArrayOfChar, paramInt);
  }
  
  public final int getMetaState()
  {
    return mMetaState;
  }
  
  public final int getModifiers()
  {
    return normalizeMetaState(mMetaState) & 0x770FF;
  }
  
  public char getNumber()
  {
    return getKeyCharacterMap().getNumber(mKeyCode);
  }
  
  public final int getRepeatCount()
  {
    return mRepeatCount;
  }
  
  public final int getScanCode()
  {
    return mScanCode;
  }
  
  public final int getSource()
  {
    return mSource;
  }
  
  public int getUnicodeChar()
  {
    return getUnicodeChar(mMetaState);
  }
  
  public int getUnicodeChar(int paramInt)
  {
    return getKeyCharacterMap().get(mKeyCode, paramInt);
  }
  
  public final boolean hasModifiers(int paramInt)
  {
    return metaStateHasModifiers(mMetaState, paramInt);
  }
  
  public final boolean hasNoModifiers()
  {
    return metaStateHasNoModifiers(mMetaState);
  }
  
  public final boolean isAltPressed()
  {
    boolean bool;
    if ((mMetaState & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isCanceled()
  {
    boolean bool;
    if ((mFlags & 0x20) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isCapsLockOn()
  {
    boolean bool;
    if ((mMetaState & 0x100000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isCtrlPressed()
  {
    boolean bool;
    if ((mMetaState & 0x1000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public final boolean isDown()
  {
    boolean bool;
    if (mAction == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isFunctionPressed()
  {
    boolean bool;
    if ((mMetaState & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isLongPress()
  {
    boolean bool;
    if ((mFlags & 0x80) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isMetaPressed()
  {
    boolean bool;
    if ((mMetaState & 0x10000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isNumLockOn()
  {
    boolean bool;
    if ((mMetaState & 0x200000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPrintingKey()
  {
    return getKeyCharacterMap().isPrintingKey(mKeyCode);
  }
  
  public final boolean isScrollLockOn()
  {
    boolean bool;
    if ((mMetaState & 0x400000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isShiftPressed()
  {
    int i = mMetaState;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isSymPressed()
  {
    boolean bool;
    if ((mMetaState & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isSystem()
  {
    return isSystemKey(mKeyCode);
  }
  
  public final boolean isTainted()
  {
    boolean bool;
    if ((mFlags & 0x80000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isTracking()
  {
    boolean bool;
    if ((mFlags & 0x200) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isWakeKey()
  {
    return isWakeKey(mKeyCode);
  }
  
  public final void recycle()
  {
    super.recycle();
    mCharacters = null;
    synchronized (gRecyclerLock)
    {
      if (gRecyclerUsed < 10)
      {
        gRecyclerUsed += 1;
        mNext = gRecyclerTop;
        gRecyclerTop = this;
      }
      return;
    }
  }
  
  public final void recycleIfNeededAfterDispatch() {}
  
  public final void setSource(int paramInt)
  {
    mSource = paramInt;
  }
  
  public final void setTainted(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = mFlags | 0x80000000;
    } else {
      i = mFlags & 0x7FFFFFFF;
    }
    mFlags = i;
  }
  
  public final void startTracking()
  {
    mFlags |= 0x40000000;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("KeyEvent { action=");
    localStringBuilder.append(actionToString(mAction));
    localStringBuilder.append(", keyCode=");
    localStringBuilder.append(keyCodeToString(mKeyCode));
    localStringBuilder.append(", scanCode=");
    localStringBuilder.append(mScanCode);
    if (mCharacters != null)
    {
      localStringBuilder.append(", characters=\"");
      localStringBuilder.append(mCharacters);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(", metaState=");
    localStringBuilder.append(metaStateToString(mMetaState));
    localStringBuilder.append(", flags=0x");
    localStringBuilder.append(Integer.toHexString(mFlags));
    localStringBuilder.append(", repeatCount=");
    localStringBuilder.append(mRepeatCount);
    localStringBuilder.append(", eventTime=");
    localStringBuilder.append(mEventTime);
    localStringBuilder.append(", downTime=");
    localStringBuilder.append(mDownTime);
    localStringBuilder.append(", deviceId=");
    localStringBuilder.append(mDeviceId);
    localStringBuilder.append(", source=0x");
    localStringBuilder.append(Integer.toHexString(mSource));
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(2);
    paramParcel.writeInt(mDeviceId);
    paramParcel.writeInt(mSource);
    paramParcel.writeInt(mAction);
    paramParcel.writeInt(mKeyCode);
    paramParcel.writeInt(mRepeatCount);
    paramParcel.writeInt(mMetaState);
    paramParcel.writeInt(mScanCode);
    paramParcel.writeInt(mFlags);
    paramParcel.writeLong(mDownTime);
    paramParcel.writeLong(mEventTime);
  }
  
  public static abstract interface Callback
  {
    public abstract boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent);
    
    public abstract boolean onKeyLongPress(int paramInt, KeyEvent paramKeyEvent);
    
    public abstract boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent);
    
    public abstract boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent);
  }
  
  public static class DispatcherState
  {
    SparseIntArray mActiveLongPresses = new SparseIntArray();
    int mDownKeyCode;
    Object mDownTarget;
    
    public DispatcherState() {}
    
    public void handleUpEvent(KeyEvent paramKeyEvent)
    {
      int i = paramKeyEvent.getKeyCode();
      int j = mActiveLongPresses.indexOfKey(i);
      if (j >= 0)
      {
        KeyEvent.access$076(paramKeyEvent, 288);
        mActiveLongPresses.removeAt(j);
      }
      if (mDownKeyCode == i)
      {
        KeyEvent.access$076(paramKeyEvent, 512);
        mDownKeyCode = 0;
        mDownTarget = null;
      }
    }
    
    public boolean isTracking(KeyEvent paramKeyEvent)
    {
      boolean bool;
      if (mDownKeyCode == paramKeyEvent.getKeyCode()) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void performedLongPress(KeyEvent paramKeyEvent)
    {
      mActiveLongPresses.put(paramKeyEvent.getKeyCode(), 1);
    }
    
    public void reset()
    {
      mDownKeyCode = 0;
      mDownTarget = null;
      mActiveLongPresses.clear();
    }
    
    public void reset(Object paramObject)
    {
      if (mDownTarget == paramObject)
      {
        mDownKeyCode = 0;
        mDownTarget = null;
      }
    }
    
    public void startTracking(KeyEvent paramKeyEvent, Object paramObject)
    {
      if (paramKeyEvent.getAction() == 0)
      {
        mDownKeyCode = paramKeyEvent.getKeyCode();
        mDownTarget = paramObject;
        return;
      }
      throw new IllegalArgumentException("Can only start tracking on a down event");
    }
  }
}
