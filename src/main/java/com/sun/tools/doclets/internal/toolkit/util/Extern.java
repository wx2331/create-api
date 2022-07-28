/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.tools.DocumentationTool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Extern
/*     */ {
/*     */   private Map<String, Item> packageToItemMap;
/*     */   private final Configuration configuration;
/*     */   private boolean linkoffline = false;
/*     */   
/*     */   private class Item
/*     */   {
/*     */     final String packageName;
/*     */     final String path;
/*     */     final boolean relative;
/*     */     
/*     */     Item(String param1String1, String param1String2, boolean param1Boolean) {
/* 103 */       this.packageName = param1String1;
/* 104 */       this.path = param1String2;
/* 105 */       this.relative = param1Boolean;
/* 106 */       if (Extern.this.packageToItemMap == null) {
/* 107 */         Extern.this.packageToItemMap = (Map)new HashMap<>();
/*     */       }
/* 109 */       if (!Extern.this.packageToItemMap.containsKey(param1String1)) {
/* 110 */         Extern.this.packageToItemMap.put(param1String1, this);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 118 */       return this.packageName + (this.relative ? " -> " : " => ") + this.path;
/*     */     }
/*     */   }
/*     */   
/*     */   public Extern(Configuration paramConfiguration) {
/* 123 */     this.configuration = paramConfiguration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExternal(ProgramElementDoc paramProgramElementDoc) {
/* 132 */     if (this.packageToItemMap == null) {
/* 133 */       return false;
/*     */     }
/* 135 */     return (this.packageToItemMap.get(paramProgramElementDoc.containingPackage().name()) != null);
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
/*     */   public DocLink getExternalLink(String paramString1, DocPath paramDocPath, String paramString2) {
/* 148 */     return getExternalLink(paramString1, paramDocPath, paramString2, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DocLink getExternalLink(String paramString1, DocPath paramDocPath, String paramString2, String paramString3) {
/* 153 */     Item item = findPackageItem(paramString1);
/* 154 */     if (item == null) {
/* 155 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 159 */     DocPath docPath = item.relative ? paramDocPath.resolve(item.path).resolve(paramString2) : DocPath.create(item.path).resolve(paramString2);
/*     */     
/* 161 */     return new DocLink(docPath, "is-external=true", paramString3);
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
/*     */   public boolean link(String paramString1, String paramString2, DocErrorReporter paramDocErrorReporter, boolean paramBoolean) {
/* 176 */     this.linkoffline = paramBoolean;
/*     */     try {
/* 178 */       paramString1 = adjustEndFileSeparator(paramString1);
/* 179 */       if (isUrl(paramString2)) {
/* 180 */         readPackageListFromURL(paramString1, toURL(adjustEndFileSeparator(paramString2)));
/*     */       } else {
/* 182 */         readPackageListFromFile(paramString1, DocFile.createFileForInput(this.configuration, paramString2));
/*     */       } 
/* 184 */       return true;
/* 185 */     } catch (Fault fault) {
/* 186 */       paramDocErrorReporter.printWarning(fault.getMessage());
/* 187 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private URL toURL(String paramString) throws Fault {
/*     */     try {
/* 193 */       return new URL(paramString);
/* 194 */     } catch (MalformedURLException malformedURLException) {
/* 195 */       throw new Fault(this.configuration.getText("doclet.MalformedURL", paramString), malformedURLException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private class Fault extends Exception {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     Fault(String param1String, Exception param1Exception) {
/* 203 */       super(param1String, param1Exception);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Item findPackageItem(String paramString) {
/* 213 */     if (this.packageToItemMap == null) {
/* 214 */       return null;
/*     */     }
/* 216 */     return this.packageToItemMap.get(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String adjustEndFileSeparator(String paramString) {
/* 223 */     return paramString.endsWith("/") ? paramString : (paramString + '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readPackageListFromURL(String paramString, URL paramURL) throws Fault {
/*     */     try {
/* 235 */       URL uRL = paramURL.toURI().resolve(DocPaths.PACKAGE_LIST.getPath()).toURL();
/* 236 */       readPackageList(uRL.openStream(), paramString, false);
/* 237 */     } catch (URISyntaxException uRISyntaxException) {
/* 238 */       throw new Fault(this.configuration.getText("doclet.MalformedURL", paramURL.toString()), uRISyntaxException);
/* 239 */     } catch (MalformedURLException malformedURLException) {
/* 240 */       throw new Fault(this.configuration.getText("doclet.MalformedURL", paramURL.toString()), malformedURLException);
/* 241 */     } catch (IOException iOException) {
/* 242 */       throw new Fault(this.configuration.getText("doclet.URL_error", paramURL.toString()), iOException);
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
/*     */   private void readPackageListFromFile(String paramString, DocFile paramDocFile) throws Fault {
/* 254 */     DocFile docFile = paramDocFile.resolve(DocPaths.PACKAGE_LIST);
/* 255 */     if (!docFile.isAbsolute() && !this.linkoffline) {
/* 256 */       docFile = docFile.resolveAgainst(DocumentationTool.Location.DOCUMENTATION_OUTPUT);
/*     */     }
/*     */     try {
/* 259 */       if (docFile.exists() && docFile.canRead()) {
/*     */ 
/*     */         
/* 262 */         boolean bool = (!DocFile.createFileForInput(this.configuration, paramString).isAbsolute() && !isUrl(paramString)) ? true : false;
/* 263 */         readPackageList(docFile.openInputStream(), paramString, bool);
/*     */       } else {
/* 265 */         throw new Fault(this.configuration.getText("doclet.File_error", docFile.getPath()), null);
/*     */       } 
/* 267 */     } catch (IOException iOException) {
/* 268 */       throw new Fault(this.configuration.getText("doclet.File_error", docFile.getPath()), iOException);
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
/*     */   private void readPackageList(InputStream paramInputStream, String paramString, boolean paramBoolean) throws IOException {
/* 283 */     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
/* 284 */     StringBuilder stringBuilder = new StringBuilder();
/*     */     try {
/*     */       int i;
/* 287 */       while ((i = bufferedReader.read()) >= 0) {
/* 288 */         char c = (char)i;
/* 289 */         if (c == '\n' || c == '\r') {
/* 290 */           if (stringBuilder.length() > 0) {
/* 291 */             String str1 = stringBuilder.toString();
/*     */             
/* 293 */             String str2 = paramString + str1.replace('.', '/') + '/';
/* 294 */             new Item(str1, str2, paramBoolean);
/* 295 */             stringBuilder.setLength(0);
/*     */           }  continue;
/*     */         } 
/* 298 */         stringBuilder.append(c);
/*     */       } 
/*     */     } finally {
/*     */       
/* 302 */       paramInputStream.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isUrl(String paramString) {
/*     */     try {
/* 308 */       new URL(paramString);
/*     */       
/* 310 */       return true;
/* 311 */     } catch (MalformedURLException malformedURLException) {
/*     */       
/* 313 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\Extern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */