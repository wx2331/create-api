/*     */ package com.sun.xml.internal.dtdparser;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Hashtable;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class Resolver
/*     */   implements EntityResolver
/*     */ {
/*     */   private boolean ignoringMIME;
/*     */   private Hashtable id2uri;
/*     */   private Hashtable id2resource;
/*     */   private Hashtable id2loader;
/* 113 */   private static final String[] types = new String[] { "application/xml", "text/xml", "text/plain", "text/html", "application/x-netcdf", "content/unknown" };
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
/*     */   public static InputSource createInputSource(String contentType, InputStream stream, boolean checkType, String scheme) throws IOException {
/* 164 */     String charset = null;
/*     */     
/* 166 */     if (contentType != null) {
/*     */ 
/*     */       
/* 169 */       contentType = contentType.toLowerCase();
/* 170 */       int index = contentType.indexOf(';');
/* 171 */       if (index != -1) {
/*     */ 
/*     */         
/* 174 */         String attributes = contentType.substring(index + 1);
/* 175 */         contentType = contentType.substring(0, index);
/*     */ 
/*     */         
/* 178 */         index = attributes.indexOf("charset");
/* 179 */         if (index != -1) {
/* 180 */           attributes = attributes.substring(index + 7);
/*     */           
/* 182 */           if ((index = attributes.indexOf(';')) != -1) {
/* 183 */             attributes = attributes.substring(0, index);
/*     */           }
/* 185 */           if ((index = attributes.indexOf('=')) != -1) {
/* 186 */             attributes = attributes.substring(index + 1);
/*     */             
/* 188 */             if ((index = attributes.indexOf('(')) != -1) {
/* 189 */               attributes = attributes.substring(0, index);
/*     */             }
/* 191 */             if ((index = attributes.indexOf('"')) != -1) {
/* 192 */               attributes = attributes.substring(index + 1);
/* 193 */               attributes = attributes.substring(0, attributes
/* 194 */                   .indexOf('"'));
/*     */             } 
/* 196 */             charset = attributes.trim();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 205 */       if (checkType) {
/* 206 */         boolean isOK = false;
/* 207 */         for (int i = 0; i < types.length; i++) {
/* 208 */           if (types[i].equals(contentType)) {
/* 209 */             isOK = true; break;
/*     */           } 
/*     */         } 
/* 212 */         if (!isOK) {
/* 213 */           throw new IOException("Not XML: " + contentType);
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 222 */       if (charset == null) {
/* 223 */         contentType = contentType.trim();
/* 224 */         if (contentType.startsWith("text/") && 
/* 225 */           !"file".equalsIgnoreCase(scheme)) {
/* 226 */           charset = "US-ASCII";
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 232 */     InputSource retval = new InputSource(XmlReader.createReader(stream, charset));
/* 233 */     retval.setByteStream(stream);
/* 234 */     retval.setEncoding(charset);
/* 235 */     return retval;
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
/*     */   public static InputSource createInputSource(URL uri, boolean checkType) throws IOException {
/*     */     InputSource retval;
/* 249 */     URLConnection conn = uri.openConnection();
/*     */ 
/*     */     
/* 252 */     if (checkType) {
/* 253 */       String contentType = conn.getContentType();
/* 254 */       retval = createInputSource(contentType, conn.getInputStream(), false, uri
/* 255 */           .getProtocol());
/*     */     } else {
/* 257 */       retval = new InputSource(XmlReader.createReader(conn.getInputStream()));
/*     */     } 
/* 259 */     retval.setSystemId(conn.getURL().toString());
/* 260 */     return retval;
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
/*     */   public static InputSource createInputSource(File file) throws IOException {
/* 273 */     InputSource retval = new InputSource(XmlReader.createReader(new FileInputStream(file)));
/*     */ 
/*     */ 
/*     */     
/* 277 */     String path = file.getAbsolutePath();
/* 278 */     if (File.separatorChar != '/')
/* 279 */       path = path.replace(File.separatorChar, '/'); 
/* 280 */     if (!path.startsWith("/"))
/* 281 */       path = "/" + path; 
/* 282 */     if (!path.endsWith("/") && file.isDirectory()) {
/* 283 */       path = path + "/";
/*     */     }
/* 285 */     retval.setSystemId("file:" + path);
/* 286 */     return retval;
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
/*     */   public InputSource resolveEntity(String name, String uri) throws IOException {
/*     */     InputSource retval;
/* 311 */     String mappedURI = name2uri(name);
/*     */     
/*     */     InputStream stream;
/*     */     
/* 315 */     if (mappedURI == null && (stream = mapResource(name)) != null) {
/* 316 */       uri = "java:resource:" + (String)this.id2resource.get(name);
/* 317 */       retval = new InputSource(XmlReader.createReader(stream));
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 324 */       if (mappedURI != null) {
/* 325 */         uri = mappedURI;
/* 326 */       } else if (uri == null) {
/* 327 */         return null;
/*     */       } 
/* 329 */       URL url = new URL(uri);
/* 330 */       URLConnection conn = url.openConnection();
/* 331 */       uri = conn.getURL().toString();
/*     */       
/* 333 */       if (this.ignoringMIME) {
/* 334 */         retval = new InputSource(XmlReader.createReader(conn.getInputStream()));
/*     */       } else {
/* 336 */         String contentType = conn.getContentType();
/* 337 */         retval = createInputSource(contentType, conn
/* 338 */             .getInputStream(), false, url
/* 339 */             .getProtocol());
/*     */       } 
/*     */     } 
/* 342 */     retval.setSystemId(uri);
/* 343 */     retval.setPublicId(name);
/* 344 */     return retval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoringMIME() {
/* 354 */     return this.ignoringMIME;
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
/*     */   public void setIgnoringMIME(boolean value) {
/* 368 */     this.ignoringMIME = value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String name2uri(String publicId) {
/* 374 */     if (publicId == null || this.id2uri == null)
/* 375 */       return null; 
/* 376 */     return (String)this.id2uri.get(publicId);
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
/*     */   public void registerCatalogEntry(String publicId, String uri) {
/* 392 */     if (this.id2uri == null)
/* 393 */       this.id2uri = new Hashtable<>(17); 
/* 394 */     this.id2uri.put(publicId, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputStream mapResource(String publicId) {
/* 401 */     if (publicId == null || this.id2resource == null) {
/* 402 */       return null;
/*     */     }
/* 404 */     String resourceName = (String)this.id2resource.get(publicId);
/* 405 */     ClassLoader loader = null;
/*     */     
/* 407 */     if (resourceName == null) {
/* 408 */       return null;
/*     */     }
/*     */     
/* 411 */     if (this.id2loader != null) {
/* 412 */       loader = (ClassLoader)this.id2loader.get(publicId);
/*     */     }
/* 414 */     if (loader == null)
/* 415 */       return ClassLoader.getSystemResourceAsStream(resourceName); 
/* 416 */     return loader.getResourceAsStream(resourceName);
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
/*     */   public void registerCatalogEntry(String publicId, String resourceName, ClassLoader loader) {
/* 438 */     if (this.id2resource == null)
/* 439 */       this.id2resource = new Hashtable<>(17); 
/* 440 */     this.id2resource.put(publicId, resourceName);
/*     */     
/* 442 */     if (loader != null) {
/* 443 */       if (this.id2loader == null)
/* 444 */         this.id2loader = new Hashtable<>(17); 
/* 445 */       this.id2loader.put(publicId, loader);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\dtdparser\Resolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */