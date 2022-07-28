/*     */ package sun.security.tools.jarsigner;
/*     */ 
/*     */ import com.sun.jarsigner.ContentSignerParameters;
/*     */ import java.net.URI;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.zip.ZipFile;
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
/*     */ class JarSignerParameters
/*     */   implements ContentSignerParameters
/*     */ {
/*     */   private String[] args;
/*     */   private URI tsa;
/*     */   private X509Certificate tsaCertificate;
/*     */   private byte[] signature;
/*     */   private String signatureAlgorithm;
/*     */   private X509Certificate[] signerCertificateChain;
/*     */   private byte[] content;
/*     */   private ZipFile source;
/*     */   private String tSAPolicyID;
/*     */   private String tSADigestAlg;
/*     */   
/*     */   JarSignerParameters(String[] paramArrayOfString, URI paramURI, X509Certificate paramX509Certificate, String paramString1, String paramString2, byte[] paramArrayOfbyte1, String paramString3, X509Certificate[] paramArrayOfX509Certificate, byte[] paramArrayOfbyte2, ZipFile paramZipFile) {
/*  57 */     if (paramArrayOfbyte1 == null || paramString3 == null || paramArrayOfX509Certificate == null || paramString2 == null)
/*     */     {
/*  59 */       throw new NullPointerException();
/*     */     }
/*  61 */     this.args = paramArrayOfString;
/*  62 */     this.tsa = paramURI;
/*  63 */     this.tsaCertificate = paramX509Certificate;
/*  64 */     this.tSAPolicyID = paramString1;
/*  65 */     this.tSADigestAlg = paramString2;
/*  66 */     this.signature = paramArrayOfbyte1;
/*  67 */     this.signatureAlgorithm = paramString3;
/*  68 */     this.signerCertificateChain = paramArrayOfX509Certificate;
/*  69 */     this.content = paramArrayOfbyte2;
/*  70 */     this.source = paramZipFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getCommandLine() {
/*  79 */     return this.args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getTimestampingAuthority() {
/*  88 */     return this.tsa;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X509Certificate getTimestampingAuthorityCertificate() {
/*  97 */     return this.tsaCertificate;
/*     */   }
/*     */   
/*     */   public String getTSAPolicyID() {
/* 101 */     return this.tSAPolicyID;
/*     */   }
/*     */   
/*     */   public String getTSADigestAlg() {
/* 105 */     return this.tSADigestAlg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getSignature() {
/* 114 */     return this.signature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignatureAlgorithm() {
/* 123 */     return this.signatureAlgorithm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X509Certificate[] getSignerCertificateChain() {
/* 132 */     return this.signerCertificateChain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getContent() {
/* 141 */     return this.content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipFile getSource() {
/* 150 */     return this.source;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\security\tools\jarsigner\JarSignerParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */