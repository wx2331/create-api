/*     */ package sun.security.tools.jarsigner;
/*     */ 
/*     */ import com.sun.jarsigner.ContentSigner;
/*     */ import com.sun.jarsigner.ContentSignerParameters;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import sun.security.pkcs.PKCS7;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AccessDescription;
/*     */ import sun.security.x509.GeneralName;
/*     */ import sun.security.x509.URIName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TimestampedSigner
/*     */   extends ContentSigner
/*     */ {
/*     */   private static final String SUBJECT_INFO_ACCESS_OID = "1.3.6.1.5.5.7.1.11";
/*     */   private static final ObjectIdentifier AD_TIMESTAMPING_Id;
/*     */   
/*     */   static {
/*  62 */     ObjectIdentifier objectIdentifier = null;
/*     */     try {
/*  64 */       objectIdentifier = new ObjectIdentifier("1.3.6.1.5.5.7.48.3");
/*  65 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/*  68 */     AD_TIMESTAMPING_Id = objectIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generateSignedData(ContentSignerParameters paramContentSignerParameters, boolean paramBoolean1, boolean paramBoolean2) throws NoSuchAlgorithmException, CertificateException, IOException {
/* 106 */     if (paramContentSignerParameters == null) {
/* 107 */       throw new NullPointerException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     String str1 = paramContentSignerParameters.getSignatureAlgorithm();
/*     */     
/* 116 */     X509Certificate[] arrayOfX509Certificate = paramContentSignerParameters.getSignerCertificateChain();
/* 117 */     byte[] arrayOfByte1 = paramContentSignerParameters.getSignature();
/*     */ 
/*     */     
/* 120 */     byte[] arrayOfByte2 = (paramBoolean1 == true) ? null : paramContentSignerParameters.getContent();
/*     */     
/* 122 */     URI uRI = null;
/* 123 */     if (paramBoolean2) {
/* 124 */       uRI = paramContentSignerParameters.getTimestampingAuthority();
/* 125 */       if (uRI == null) {
/*     */         
/* 127 */         uRI = getTimestampingURI(paramContentSignerParameters
/* 128 */             .getTimestampingAuthorityCertificate());
/* 129 */         if (uRI == null) {
/* 130 */           throw new CertificateException("Subject Information Access extension not found");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     String str2 = "SHA-256";
/* 136 */     if (paramContentSignerParameters instanceof JarSignerParameters) {
/* 137 */       str2 = ((JarSignerParameters)paramContentSignerParameters).getTSADigestAlg();
/*     */     }
/* 139 */     return PKCS7.generateSignedData(arrayOfByte1, arrayOfX509Certificate, arrayOfByte2, paramContentSignerParameters
/* 140 */         .getSignatureAlgorithm(), uRI, paramContentSignerParameters
/* 141 */         .getTSAPolicyID(), str2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI getTimestampingURI(X509Certificate paramX509Certificate) {
/* 157 */     if (paramX509Certificate == null) {
/* 158 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 163 */       byte[] arrayOfByte = paramX509Certificate.getExtensionValue("1.3.6.1.5.5.7.1.11");
/* 164 */       if (arrayOfByte == null) {
/* 165 */         return null;
/*     */       }
/* 167 */       DerInputStream derInputStream = new DerInputStream(arrayOfByte);
/* 168 */       derInputStream = new DerInputStream(derInputStream.getOctetString());
/* 169 */       DerValue[] arrayOfDerValue = derInputStream.getSequence(5);
/*     */ 
/*     */ 
/*     */       
/* 173 */       for (byte b = 0; b < arrayOfDerValue.length; b++) {
/* 174 */         AccessDescription accessDescription = new AccessDescription(arrayOfDerValue[b]);
/* 175 */         if (accessDescription.getAccessMethod()
/* 176 */           .equals(AD_TIMESTAMPING_Id)) {
/* 177 */           GeneralName generalName = accessDescription.getAccessLocation();
/* 178 */           if (generalName.getType() == 6) {
/* 179 */             URIName uRIName = (URIName)generalName.getName();
/* 180 */             if (uRIName.getScheme().equalsIgnoreCase("http") || uRIName
/* 181 */               .getScheme().equalsIgnoreCase("https")) {
/* 182 */               return uRIName.getURI();
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 187 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 190 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\security\tools\jarsigner\TimestampedSigner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */