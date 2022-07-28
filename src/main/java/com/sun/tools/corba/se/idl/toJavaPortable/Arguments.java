/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Arguments;
/*     */ import com.sun.tools.corba.se.idl.InvalidArgument;
/*     */ import java.io.File;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Arguments
/*     */   extends Arguments
/*     */ {
/*     */   protected void parseOtherArgs(String[] paramArrayOfString, Properties paramProperties) throws InvalidArgument {
/*  74 */     String str1 = null;
/*  75 */     String str2 = null;
/*     */ 
/*     */     
/*  78 */     this.packages.put("CORBA", "org.omg");
/*  79 */     packageFromProps(paramProperties);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  86 */       Vector vector = new Vector();
/*     */ 
/*     */       
/*  89 */       for (int i = 0; i < paramArrayOfString.length; i++) {
/*     */         
/*  91 */         String str = paramArrayOfString[i].toLowerCase();
/*     */         
/*  93 */         if (str.charAt(0) != '-' && str.charAt(0) != '/')
/*  94 */           throw new InvalidArgument(paramArrayOfString[i]); 
/*  95 */         if (str.charAt(0) == '-') {
/*  96 */           str = str.substring(1);
/*     */         }
/*     */ 
/*     */         
/* 100 */         if (str.startsWith("f")) {
/*     */ 
/*     */           
/* 103 */           if (str.equals("f")) {
/* 104 */             str = 'f' + paramArrayOfString[++i].toLowerCase();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 110 */           if (str.equals("fclient")) {
/*     */             
/* 112 */             this.emit = (this.emit == 2 || this.emit == 3) ? 3 : 1;
/*     */           }
/* 114 */           else if (str.equals("fserver")) {
/*     */             
/* 116 */             this.emit = (this.emit == 1 || this.emit == 3) ? 3 : 2;
/* 117 */             this.TIEServer = false;
/*     */           }
/* 119 */           else if (str.equals("fall")) {
/*     */             
/* 121 */             this.emit = 3;
/* 122 */             this.TIEServer = false;
/*     */ 
/*     */           
/*     */           }
/* 126 */           else if (str.equals("fservertie")) {
/*     */             
/* 128 */             this.emit = (this.emit == 1 || this.emit == 3) ? 3 : 2;
/* 129 */             this.TIEServer = true;
/*     */           }
/* 131 */           else if (str.equals("falltie")) {
/*     */             
/* 133 */             this.emit = 3;
/* 134 */             this.TIEServer = true;
/*     */           } else {
/*     */             
/* 137 */             i = collectUnknownArg(paramArrayOfString, i, vector);
/*     */           } 
/* 139 */         } else if (str.equals("pkgtranslate")) {
/*     */           
/* 141 */           if (i + 2 >= paramArrayOfString.length) {
/* 142 */             throw new InvalidArgument(paramArrayOfString[i]);
/*     */           }
/* 144 */           String str3 = paramArrayOfString[++i];
/* 145 */           String str4 = paramArrayOfString[++i];
/* 146 */           checkPackageNameValid(str3);
/* 147 */           checkPackageNameValid(str4);
/* 148 */           if (str3.equals("org") || str3.startsWith("org.omg"))
/* 149 */             throw new InvalidArgument(paramArrayOfString[i]); 
/* 150 */           str3 = str3.replace('.', '/');
/* 151 */           str4 = str4.replace('.', '/');
/* 152 */           this.packageTranslation.put(str3, str4);
/*     */         
/*     */         }
/* 155 */         else if (str.equals("pkgprefix")) {
/*     */           
/* 157 */           if (i + 2 >= paramArrayOfString.length) {
/* 158 */             throw new InvalidArgument(paramArrayOfString[i]);
/*     */           }
/* 160 */           String str3 = paramArrayOfString[++i];
/* 161 */           String str4 = paramArrayOfString[++i];
/* 162 */           checkPackageNameValid(str3);
/* 163 */           checkPackageNameValid(str4);
/* 164 */           this.packages.put(str3, str4);
/*     */         
/*     */         }
/* 167 */         else if (str.equals("td")) {
/*     */           
/* 169 */           if (i + 1 >= paramArrayOfString.length)
/* 170 */             throw new InvalidArgument(paramArrayOfString[i]); 
/* 171 */           String str3 = paramArrayOfString[++i];
/* 172 */           if (str3.charAt(0) == '-') {
/* 173 */             throw new InvalidArgument(paramArrayOfString[i - 1]);
/*     */           }
/*     */           
/* 176 */           this.targetDir = str3.replace('/', File.separatorChar);
/* 177 */           if (this.targetDir.charAt(this.targetDir.length() - 1) != File.separatorChar) {
/* 178 */             this.targetDir += File.separatorChar;
/*     */           
/*     */           }
/*     */         }
/* 182 */         else if (str.equals("sep")) {
/*     */           
/* 184 */           if (i + 1 >= paramArrayOfString.length)
/* 185 */             throw new InvalidArgument(paramArrayOfString[i]); 
/* 186 */           this.separator = paramArrayOfString[++i];
/*     */         
/*     */         }
/* 189 */         else if (str.equals("oldimplbase")) {
/* 190 */           this.POAServer = false;
/*     */         }
/* 192 */         else if (str.equals("skeletonname")) {
/* 193 */           if (i + 1 >= paramArrayOfString.length)
/* 194 */             throw new InvalidArgument(paramArrayOfString[i]); 
/* 195 */           str1 = paramArrayOfString[++i];
/*     */         }
/* 197 */         else if (str.equals("tiename")) {
/* 198 */           if (i + 1 >= paramArrayOfString.length)
/* 199 */             throw new InvalidArgument(paramArrayOfString[i]); 
/* 200 */           str2 = paramArrayOfString[++i];
/*     */         }
/* 202 */         else if (str.equals("localoptimization")) {
/* 203 */           this.LocalOptimization = true;
/*     */         } else {
/* 205 */           i = collectUnknownArg(paramArrayOfString, i, vector);
/*     */         } 
/*     */       } 
/*     */       
/* 209 */       if (vector.size() > 0) {
/*     */         
/* 211 */         String[] arrayOfString = new String[vector.size()];
/* 212 */         vector.copyInto((Object[])arrayOfString);
/*     */         
/* 214 */         super.parseOtherArgs(arrayOfString, paramProperties);
/*     */       } 
/*     */       
/* 217 */       setDefaultEmitter();
/* 218 */       setNameModifiers(str1, str2);
/*     */     }
/* 220 */     catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 225 */       throw new InvalidArgument(paramArrayOfString[paramArrayOfString.length - 1]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int collectUnknownArg(String[] paramArrayOfString, int paramInt, Vector<String> paramVector) {
/* 234 */     paramVector.addElement(paramArrayOfString[paramInt]);
/* 235 */     paramInt++;
/* 236 */     while (paramInt < paramArrayOfString.length && paramArrayOfString[paramInt].charAt(0) != '-' && paramArrayOfString[paramInt].charAt(0) != '/')
/* 237 */       paramVector.addElement(paramArrayOfString[paramInt++]); 
/* 238 */     return --paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void packageFromProps(Properties paramProperties) throws InvalidArgument {
/* 247 */     Enumeration<?> enumeration = paramProperties.propertyNames();
/* 248 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 250 */       String str = (String)enumeration.nextElement();
/* 251 */       if (str.startsWith("PkgPrefix.")) {
/*     */         
/* 253 */         String str1 = str.substring(10);
/* 254 */         String str2 = paramProperties.getProperty(str);
/* 255 */         checkPackageNameValid(str2);
/* 256 */         checkPackageNameValid(str1);
/* 257 */         this.packages.put(str1, str2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setDefaultEmitter() {
/* 267 */     if (this.emit == 0) this.emit = 1;
/*     */   
/*     */   }
/*     */   
/*     */   protected void setNameModifiers(String paramString1, String paramString2) {
/* 272 */     if (this.emit > 1) {
/*     */       String str1, str2;
/*     */ 
/*     */       
/* 276 */       if (paramString1 != null) {
/* 277 */         str2 = paramString1;
/* 278 */       } else if (this.POAServer) {
/* 279 */         str2 = "%POA";
/*     */       } else {
/* 281 */         str2 = "_%ImplBase";
/*     */       } 
/* 283 */       if (paramString2 != null) {
/* 284 */         str1 = paramString2;
/* 285 */       } else if (this.POAServer) {
/* 286 */         str1 = "%POATie";
/*     */       } else {
/* 288 */         str1 = "%_Tie";
/*     */       } 
/* 290 */       this.skeletonNameModifier = new NameModifierImpl(str2);
/* 291 */       this.tieNameModifier = new NameModifierImpl(str1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkPackageNameValid(String paramString) throws InvalidArgument {
/* 300 */     if (paramString.charAt(0) == '.')
/* 301 */       throw new InvalidArgument(paramString); 
/* 302 */     for (byte b = 0; b < paramString.length(); b++) {
/* 303 */       if (paramString.charAt(b) == '.') {
/*     */         
/* 305 */         if (b == paramString.length() - 1 || !Character.isJavaIdentifierStart(paramString.charAt(++b))) {
/* 306 */           throw new InvalidArgument(paramString);
/*     */         }
/* 308 */       } else if (!Character.isJavaIdentifierPart(paramString.charAt(b))) {
/* 309 */         throw new InvalidArgument(paramString);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 317 */   public Hashtable packages = new Hashtable<>();
/*     */   
/* 319 */   public String separator = null;
/*     */   
/*     */   public static final int None = 0;
/*     */   
/*     */   public static final int Client = 1;
/*     */   public static final int Server = 2;
/*     */   public static final int All = 3;
/* 326 */   public int emit = 0;
/*     */   
/*     */   public boolean TIEServer = false;
/*     */   
/*     */   public boolean POAServer = true;
/*     */   
/*     */   public boolean LocalOptimization = false;
/* 333 */   public NameModifier skeletonNameModifier = null;
/* 334 */   public NameModifier tieNameModifier = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 339 */   public Hashtable packageTranslation = new Hashtable<>();
/*     */   
/* 341 */   public String targetDir = "";
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\Arguments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */