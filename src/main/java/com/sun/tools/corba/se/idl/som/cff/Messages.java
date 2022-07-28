/*     */ package com.sun.tools.corba.se.idl.som.cff;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Messages
/*     */ {
/*     */   private static final String LTB = "%B";
/*     */   private static final char NL = '\n';
/*  74 */   private static final String lineSeparator = System.getProperty("line.separator");
/*     */   
/*  76 */   private static final Properties m = new Properties();
/*     */ 
/*     */   
/*     */   private static boolean loadNeeded = true;
/*     */ 
/*     */   
/*     */   private static final synchronized void loadDefaultProperties() {
/*  83 */     if (!loadNeeded)
/*     */       return; 
/*     */     try {
/*  86 */       m.load(FileLocator.locateLocaleSpecificFileInClassPath("com/sun/tools/corba/se/idl/som/cff/cff.properties"));
/*     */     }
/*  88 */     catch (IOException iOException) {}
/*  89 */     fixMessages(m);
/*  90 */     loadNeeded = false;
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
/*     */   private static final void fixMessages(Properties paramProperties) {
/* 114 */     Enumeration<String> enumeration1 = paramProperties.keys();
/* 115 */     Enumeration<String> enumeration2 = paramProperties.elements();
/* 116 */     while (enumeration1.hasMoreElements()) {
/* 117 */       String str1 = enumeration1.nextElement();
/* 118 */       String str2 = enumeration2.nextElement();
/* 119 */       int i = str2.indexOf("%B");
/* 120 */       boolean bool = false;
/* 121 */       while (i != -1) {
/* 122 */         if (i == 0) {
/* 123 */           str2 = " " + str2.substring(2);
/*     */         } else {
/* 125 */           str2 = str2.substring(0, i) + " " + str2.substring(i + 2);
/* 126 */         }  bool = true;
/* 127 */         i = str2.indexOf("%B");
/*     */       } 
/* 129 */       int j = lineSeparator.length() - 1;
/* 130 */       for (i = 0; i < str2.length(); i++) {
/* 131 */         if (str2.charAt(i) == '\n') {
/*     */           
/* 133 */           str2 = str2.substring(0, i) + lineSeparator + str2.substring(i + 1);
/* 134 */           i += j;
/* 135 */           bool = true;
/*     */         } 
/*     */       } 
/* 138 */       if (bool) {
/* 139 */         paramProperties.put(str1, str2);
/*     */       }
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
/*     */   public static final synchronized void msgLoad(String paramString) throws IOException {
/* 152 */     m.load(FileLocator.locateLocaleSpecificFileInClassPath(paramString));
/*     */     
/* 154 */     fixMessages(m);
/* 155 */     loadNeeded = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String msg(String paramString) {
/* 166 */     if (loadNeeded)
/* 167 */       loadDefaultProperties(); 
/* 168 */     return m.getProperty(paramString, paramString);
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
/*     */   public static final String msg(String paramString1, String paramString2) {
/* 186 */     if (loadNeeded)
/* 187 */       loadDefaultProperties(); 
/* 188 */     String str = m.getProperty(paramString1, paramString1);
/* 189 */     int i = str.indexOf("%1");
/* 190 */     if (i >= 0) {
/* 191 */       String str1 = "";
/* 192 */       if (i + 2 < str.length())
/* 193 */         str1 = str.substring(i + 2); 
/* 194 */       return str.substring(0, i) + paramString2 + str1;
/*     */     } 
/* 196 */     str = str + " " + paramString2;
/* 197 */     return str;
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
/*     */   public static final String msg(String paramString, int paramInt) {
/* 214 */     return msg(paramString, String.valueOf(paramInt));
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
/*     */   public static final String msg(String paramString1, String paramString2, String paramString3) {
/* 231 */     if (loadNeeded)
/* 232 */       loadDefaultProperties(); 
/* 233 */     String str1 = m.getProperty(paramString1, paramString1);
/* 234 */     String str2 = "";
/* 235 */     int i = str1.indexOf("%1");
/* 236 */     if (i >= 0) {
/* 237 */       if (i + 2 < str1.length())
/* 238 */         str2 = str1.substring(i + 2); 
/* 239 */       str1 = str1.substring(0, i) + paramString2 + str2;
/*     */     } else {
/* 241 */       str1 = str1 + " " + paramString2;
/* 242 */     }  i = str1.indexOf("%2");
/* 243 */     if (i >= 0) {
/* 244 */       str2 = "";
/* 245 */       if (i + 2 < str1.length())
/* 246 */         str2 = str1.substring(i + 2); 
/* 247 */       str1 = str1.substring(0, i) + paramString3 + str2;
/*     */     } else {
/* 249 */       str1 = str1 + " " + paramString3;
/* 250 */     }  return str1;
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
/*     */   public static final String msg(String paramString1, int paramInt, String paramString2) {
/* 267 */     return msg(paramString1, String.valueOf(paramInt), paramString2);
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
/*     */   public static final String msg(String paramString1, String paramString2, int paramInt) {
/* 284 */     return msg(paramString1, paramString2, String.valueOf(paramInt));
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
/*     */   public static final String msg(String paramString, int paramInt1, int paramInt2) {
/* 301 */     return msg(paramString, String.valueOf(paramInt1), String.valueOf(paramInt2));
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
/*     */   public static final String msg(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 320 */     if (loadNeeded)
/* 321 */       loadDefaultProperties(); 
/* 322 */     String str = m.getProperty(paramString1, paramString1);
/* 323 */     str = substituteString(str, 1, paramString2);
/* 324 */     str = substituteString(str, 2, paramString3);
/* 325 */     str = substituteString(str, 3, paramString4);
/*     */     
/* 327 */     return str;
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
/*     */   private static String substituteString(String paramString1, int paramInt, String paramString2) {
/* 340 */     String str1 = paramString1;
/* 341 */     String str2 = "%" + paramInt;
/* 342 */     int i = str2.length();
/* 343 */     int j = str1.indexOf(str2);
/* 344 */     String str3 = "";
/* 345 */     if (j >= 0) {
/* 346 */       if (j + i < str1.length())
/* 347 */         str3 = str1.substring(j + i); 
/* 348 */       str1 = str1.substring(0, j) + paramString2 + str3;
/*     */     } else {
/* 350 */       str1 = str1 + " " + paramString2;
/*     */     } 
/* 352 */     return str1;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\som\cff\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */