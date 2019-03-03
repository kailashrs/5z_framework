package android.security.keystore;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Date;
import javax.security.auth.x500.X500Principal;

public final class ParcelableKeyGenParameterSpec
  implements Parcelable
{
  private static final int ALGORITHM_PARAMETER_SPEC_EC = 3;
  private static final int ALGORITHM_PARAMETER_SPEC_NONE = 1;
  private static final int ALGORITHM_PARAMETER_SPEC_RSA = 2;
  public static final Parcelable.Creator<ParcelableKeyGenParameterSpec> CREATOR = new Parcelable.Creator()
  {
    public ParcelableKeyGenParameterSpec createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ParcelableKeyGenParameterSpec(paramAnonymousParcel, null);
    }
    
    public ParcelableKeyGenParameterSpec[] newArray(int paramAnonymousInt)
    {
      return new ParcelableKeyGenParameterSpec[paramAnonymousInt];
    }
  };
  private final KeyGenParameterSpec mSpec;
  
  private ParcelableKeyGenParameterSpec(Parcel paramParcel)
  {
    KeyGenParameterSpec.Builder localBuilder = new KeyGenParameterSpec.Builder(paramParcel.readString(), paramParcel.readInt());
    localBuilder.setUid(paramParcel.readInt());
    int i = paramParcel.readInt();
    if (i >= 0) {
      localBuilder.setKeySize(i);
    }
    i = paramParcel.readInt();
    if (i == 1)
    {
      localObject = null;
    }
    else if (i == 2)
    {
      localObject = new RSAKeyGenParameterSpec(paramParcel.readInt(), new BigInteger(paramParcel.createByteArray()));
    }
    else
    {
      if (i != 3) {
        break label339;
      }
      localObject = new ECGenParameterSpec(paramParcel.readString());
    }
    if (localObject != null) {
      localBuilder.setAlgorithmParameterSpec((AlgorithmParameterSpec)localObject);
    }
    localBuilder.setCertificateSubject(new X500Principal(paramParcel.createByteArray()));
    localBuilder.setCertificateSerialNumber(new BigInteger(paramParcel.createByteArray()));
    localBuilder.setCertificateNotBefore(new Date(paramParcel.readLong()));
    localBuilder.setCertificateNotAfter(new Date(paramParcel.readLong()));
    localBuilder.setKeyValidityStart(readDateOrNull(paramParcel));
    localBuilder.setKeyValidityForOriginationEnd(readDateOrNull(paramParcel));
    localBuilder.setKeyValidityForConsumptionEnd(readDateOrNull(paramParcel));
    Object localObject = paramParcel.createStringArray();
    if (localObject != null) {
      localBuilder.setDigests((String[])localObject);
    }
    localBuilder.setEncryptionPaddings(paramParcel.createStringArray());
    localBuilder.setSignaturePaddings(paramParcel.createStringArray());
    localBuilder.setBlockModes(paramParcel.createStringArray());
    localBuilder.setRandomizedEncryptionRequired(paramParcel.readBoolean());
    localBuilder.setUserAuthenticationRequired(paramParcel.readBoolean());
    localBuilder.setUserAuthenticationValidityDurationSeconds(paramParcel.readInt());
    localBuilder.setAttestationChallenge(paramParcel.createByteArray());
    localBuilder.setUniqueIdIncluded(paramParcel.readBoolean());
    localBuilder.setUserAuthenticationValidWhileOnBody(paramParcel.readBoolean());
    localBuilder.setInvalidatedByBiometricEnrollment(paramParcel.readBoolean());
    localBuilder.setUserPresenceRequired(paramParcel.readBoolean());
    mSpec = localBuilder.build();
    return;
    label339:
    throw new IllegalArgumentException(String.format("Unknown algorithm parameter spec: %d", new Object[] { Integer.valueOf(i) }));
  }
  
  public ParcelableKeyGenParameterSpec(KeyGenParameterSpec paramKeyGenParameterSpec)
  {
    mSpec = paramKeyGenParameterSpec;
  }
  
  private static Date readDateOrNull(Parcel paramParcel)
  {
    if (paramParcel.readBoolean()) {
      return new Date(paramParcel.readLong());
    }
    return null;
  }
  
  private static void writeOptionalDate(Parcel paramParcel, Date paramDate)
  {
    if (paramDate != null)
    {
      paramParcel.writeBoolean(true);
      paramParcel.writeLong(paramDate.getTime());
    }
    else
    {
      paramParcel.writeBoolean(false);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public KeyGenParameterSpec getSpec()
  {
    return mSpec;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mSpec.getKeystoreAlias());
    paramParcel.writeInt(mSpec.getPurposes());
    paramParcel.writeInt(mSpec.getUid());
    paramParcel.writeInt(mSpec.getKeySize());
    Object localObject = mSpec.getAlgorithmParameterSpec();
    if (localObject == null)
    {
      paramParcel.writeInt(1);
    }
    else if ((localObject instanceof RSAKeyGenParameterSpec))
    {
      localObject = (RSAKeyGenParameterSpec)localObject;
      paramParcel.writeInt(2);
      paramParcel.writeInt(((RSAKeyGenParameterSpec)localObject).getKeysize());
      paramParcel.writeByteArray(((RSAKeyGenParameterSpec)localObject).getPublicExponent().toByteArray());
    }
    else
    {
      if (!(localObject instanceof ECGenParameterSpec)) {
        break label368;
      }
      localObject = (ECGenParameterSpec)localObject;
      paramParcel.writeInt(3);
      paramParcel.writeString(((ECGenParameterSpec)localObject).getName());
    }
    paramParcel.writeByteArray(mSpec.getCertificateSubject().getEncoded());
    paramParcel.writeByteArray(mSpec.getCertificateSerialNumber().toByteArray());
    paramParcel.writeLong(mSpec.getCertificateNotBefore().getTime());
    paramParcel.writeLong(mSpec.getCertificateNotAfter().getTime());
    writeOptionalDate(paramParcel, mSpec.getKeyValidityStart());
    writeOptionalDate(paramParcel, mSpec.getKeyValidityForOriginationEnd());
    writeOptionalDate(paramParcel, mSpec.getKeyValidityForConsumptionEnd());
    if (mSpec.isDigestsSpecified()) {
      paramParcel.writeStringArray(mSpec.getDigests());
    } else {
      paramParcel.writeStringArray(null);
    }
    paramParcel.writeStringArray(mSpec.getEncryptionPaddings());
    paramParcel.writeStringArray(mSpec.getSignaturePaddings());
    paramParcel.writeStringArray(mSpec.getBlockModes());
    paramParcel.writeBoolean(mSpec.isRandomizedEncryptionRequired());
    paramParcel.writeBoolean(mSpec.isUserAuthenticationRequired());
    paramParcel.writeInt(mSpec.getUserAuthenticationValidityDurationSeconds());
    paramParcel.writeByteArray(mSpec.getAttestationChallenge());
    paramParcel.writeBoolean(mSpec.isUniqueIdIncluded());
    paramParcel.writeBoolean(mSpec.isUserAuthenticationValidWhileOnBody());
    paramParcel.writeBoolean(mSpec.isInvalidatedByBiometricEnrollment());
    paramParcel.writeBoolean(mSpec.isUserPresenceRequired());
    return;
    label368:
    throw new IllegalArgumentException(String.format("Unknown algorithm parameter spec: %s", new Object[] { localObject.getClass() }));
  }
}
