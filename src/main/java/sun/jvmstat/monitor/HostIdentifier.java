/*     */ package sun.jvmstat.monitor;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
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
/*     */ public class HostIdentifier
/*     */ {
/*     */   private URI uri;
/*     */   
/*     */   private URI canonicalize(String paramString) throws URISyntaxException {
/* 110 */     if (paramString == null || paramString.compareTo("localhost") == 0) {
/* 111 */       paramString = "//localhost";
/* 112 */       return new URI(paramString);
/*     */     } 
/*     */     
/* 115 */     URI uRI = new URI(paramString);
/*     */     
/* 117 */     if (uRI.isAbsolute()) {
/* 118 */       if (uRI.isOpaque()) {
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
/* 136 */         String str1 = uRI.getScheme();
/* 137 */         String str2 = uRI.getSchemeSpecificPart();
/* 138 */         String str3 = uRI.getFragment();
/* 139 */         URI uRI1 = null;
/*     */         
/* 141 */         int i = paramString.indexOf(":");
/* 142 */         int j = paramString.lastIndexOf(":");
/* 143 */         if (j != i) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 151 */           if (str3 == null) {
/* 152 */             uRI1 = new URI(str1 + "://" + str2);
/*     */           } else {
/* 154 */             uRI1 = new URI(str1 + "://" + str2 + "#" + str3);
/*     */           } 
/* 156 */           return uRI1;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 164 */         uRI1 = new URI("//" + paramString);
/* 165 */         return uRI1;
/*     */       } 
/* 167 */       return uRI;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     String str = uRI.getSchemeSpecificPart();
/* 179 */     if (str.startsWith("//")) {
/* 180 */       return uRI;
/*     */     }
/* 182 */     return new URI("//" + paramString);
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
/*     */   public HostIdentifier(String paramString) throws URISyntaxException {
/* 201 */     this.uri = canonicalize(paramString);
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
/*     */   public HostIdentifier(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) throws URISyntaxException {
/* 222 */     this.uri = new URI(paramString1, paramString2, paramString3, paramString4, paramString5);
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
/*     */   public HostIdentifier(VmIdentifier paramVmIdentifier) {
/* 240 */     StringBuilder stringBuilder = new StringBuilder();
/* 241 */     String str1 = paramVmIdentifier.getScheme();
/* 242 */     String str2 = paramVmIdentifier.getHost();
/* 243 */     String str3 = paramVmIdentifier.getAuthority();
/*     */ 
/*     */     
/* 246 */     if (str1 != null && str1.compareTo("file") == 0) {
/*     */       try {
/* 248 */         this.uri = new URI("file://localhost");
/* 249 */       } catch (URISyntaxException uRISyntaxException) {}
/*     */       
/*     */       return;
/*     */     } 
/* 253 */     if (str2 != null && str2.compareTo(str3) == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 258 */       str2 = null;
/*     */     }
/*     */     
/* 261 */     if (str1 == null) {
/* 262 */       if (str2 == null) {
/* 263 */         str1 = "local";
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 269 */         str1 = "rmi";
/*     */       } 
/*     */     }
/*     */     
/* 273 */     stringBuilder.append(str1).append("://");
/*     */     
/* 275 */     if (str2 == null) {
/* 276 */       stringBuilder.append("localhost");
/*     */     } else {
/* 278 */       stringBuilder.append(str2);
/*     */     } 
/*     */     
/* 281 */     int i = paramVmIdentifier.getPort();
/* 282 */     if (i != -1) {
/* 283 */       stringBuilder.append(":").append(i);
/*     */     }
/*     */     
/* 286 */     String str4 = paramVmIdentifier.getPath();
/* 287 */     if (str4 != null && str4.length() != 0) {
/* 288 */       stringBuilder.append(str4);
/*     */     }
/*     */     
/* 291 */     String str5 = paramVmIdentifier.getQuery();
/* 292 */     if (str5 != null) {
/* 293 */       stringBuilder.append("?").append(str5);
/*     */     }
/*     */     
/* 296 */     String str6 = paramVmIdentifier.getFragment();
/* 297 */     if (str6 != null) {
/* 298 */       stringBuilder.append("#").append(str6);
/*     */     }
/*     */     
/*     */     try {
/* 302 */       this.uri = new URI(stringBuilder.toString());
/* 303 */     } catch (URISyntaxException uRISyntaxException) {
/*     */       
/* 305 */       throw new RuntimeException("Internal Error", uRISyntaxException);
/*     */     } 
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
/*     */   public VmIdentifier resolve(VmIdentifier paramVmIdentifier) throws URISyntaxException, MonitorException {
/* 338 */     String str1 = paramVmIdentifier.getScheme();
/* 339 */     String str2 = paramVmIdentifier.getHost();
/* 340 */     String str3 = paramVmIdentifier.getAuthority();
/*     */     
/* 342 */     if (str1 != null && str1.compareTo("file") == 0)
/*     */     {
/* 344 */       return paramVmIdentifier;
/*     */     }
/*     */     
/* 347 */     if (str2 != null && str2.compareTo(str3) == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 352 */       str2 = null;
/*     */     }
/*     */     
/* 355 */     if (str1 == null) {
/* 356 */       str1 = getScheme();
/*     */     }
/*     */     
/* 359 */     Object object = null;
/*     */     
/* 361 */     StringBuffer stringBuffer = new StringBuffer();
/*     */     
/* 363 */     stringBuffer.append(str1).append("://");
/*     */     
/* 365 */     String str4 = paramVmIdentifier.getUserInfo();
/* 366 */     if (str4 != null) {
/* 367 */       stringBuffer.append(str4);
/*     */     } else {
/* 369 */       stringBuffer.append(paramVmIdentifier.getAuthority());
/*     */     } 
/*     */     
/* 372 */     if (str2 == null) {
/* 373 */       str2 = getHost();
/*     */     }
/* 375 */     stringBuffer.append("@").append(str2);
/*     */     
/* 377 */     int i = paramVmIdentifier.getPort();
/* 378 */     if (i == -1) {
/* 379 */       i = getPort();
/*     */     }
/*     */     
/* 382 */     if (i != -1) {
/* 383 */       stringBuffer.append(":").append(i);
/*     */     }
/*     */     
/* 386 */     String str5 = paramVmIdentifier.getPath();
/* 387 */     if (str5 == null || str5.length() == 0) {
/* 388 */       str5 = getPath();
/*     */     }
/*     */     
/* 391 */     if (str5 != null && str5.length() > 0) {
/* 392 */       stringBuffer.append(str5);
/*     */     }
/*     */     
/* 395 */     String str6 = paramVmIdentifier.getQuery();
/* 396 */     if (str6 == null) {
/* 397 */       str6 = getQuery();
/*     */     }
/* 399 */     if (str6 != null) {
/* 400 */       stringBuffer.append("?").append(str6);
/*     */     }
/*     */     
/* 403 */     String str7 = paramVmIdentifier.getFragment();
/* 404 */     if (str7 == null) {
/* 405 */       str7 = getFragment();
/*     */     }
/* 407 */     if (str7 != null) {
/* 408 */       stringBuffer.append("#").append(str7);
/*     */     }
/*     */     
/* 411 */     String str8 = stringBuffer.toString();
/* 412 */     return new VmIdentifier(str8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScheme() {
/* 422 */     return this.uri.isAbsolute() ? this.uri.getScheme() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeSpecificPart() {
/* 432 */     return this.uri.getSchemeSpecificPart();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserInfo() {
/* 442 */     return this.uri.getUserInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHost() {
/* 453 */     return (this.uri.getHost() == null) ? "localhost" : this.uri.getHost();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 463 */     return this.uri.getPort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 473 */     return this.uri.getPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getQuery() {
/* 483 */     return this.uri.getQuery();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFragment() {
/* 493 */     return this.uri.getFragment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMode() {
/* 503 */     String str = getQuery();
/* 504 */     if (str != null) {
/* 505 */       String[] arrayOfString = str.split("\\+");
/* 506 */       for (byte b = 0; b < arrayOfString.length; b++) {
/* 507 */         if (arrayOfString[b].startsWith("mode=")) {
/* 508 */           int i = arrayOfString[b].indexOf('=');
/* 509 */           return arrayOfString[b].substring(i + 1);
/*     */         } 
/*     */       } 
/*     */     } 
/* 513 */     return "r";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() {
/* 523 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 534 */     return this.uri.hashCode();
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
/*     */   public boolean equals(Object paramObject) {
/* 548 */     if (paramObject == this) {
/* 549 */       return true;
/*     */     }
/* 551 */     if (!(paramObject instanceof HostIdentifier)) {
/* 552 */       return false;
/*     */     }
/* 554 */     return this.uri.equals(((HostIdentifier)paramObject).uri);
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
/*     */   public String toString() {
/* 567 */     return this.uri.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\HostIdentifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */