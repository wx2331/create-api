package com.sun.jarsigner;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import jdk.Exported;

@Exported
public abstract class ContentSigner {
  public abstract byte[] generateSignedData(ContentSignerParameters paramContentSignerParameters, boolean paramBoolean1, boolean paramBoolean2) throws NoSuchAlgorithmException, CertificateException, IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jarsigner\ContentSigner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */