/*    */ package com.sun.jarsigner;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.zip.ZipFile;
/*    */ import jdk.Exported;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Exported
/*    */ public interface ContentSignerParameters
/*    */ {
/*    */   String[] getCommandLine();
/*    */   
/*    */   URI getTimestampingAuthority();
/*    */   
/*    */   X509Certificate getTimestampingAuthorityCertificate();
/*    */   
/*    */   default String getTSAPolicyID() {
/* 68 */     return null;
/*    */   }
/*    */   
/*    */   byte[] getSignature();
/*    */   
/*    */   String getSignatureAlgorithm();
/*    */   
/*    */   X509Certificate[] getSignerCertificateChain();
/*    */   
/*    */   byte[] getContent();
/*    */   
/*    */   ZipFile getSource();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jarsigner\ContentSignerParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */