/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.som.cff.FileLocator;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Arguments
/*     */ {
/*     */   protected void parseOtherArgs(String[] paramArrayOfString, Properties paramProperties) throws InvalidArgument {
/*  81 */     if (paramArrayOfString.length > 0) {
/*  82 */       throw new InvalidArgument(paramArrayOfString[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setDebugFlags(String paramString) {
/*  88 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, ",");
/*  89 */     while (stringTokenizer.hasMoreTokens()) {
/*  90 */       String str = stringTokenizer.nextToken();
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  95 */         Field field = getClass().getField(str + "DebugFlag");
/*  96 */         int i = field.getModifiers();
/*  97 */         if (Modifier.isPublic(i) && !Modifier.isStatic(i) && 
/*  98 */           field.getType() == boolean.class)
/*  99 */           field.setBoolean(this, true); 
/* 100 */       } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void parseArgs(String[] paramArrayOfString) throws InvalidArgument {
/*     */     String[] arrayOfString;
/* 110 */     Vector<String> vector = new Vector();
/* 111 */     byte b = 0;
/*     */ 
/*     */     
/*     */     try {
/* 115 */       for (b = 0; b < paramArrayOfString.length - 1; b++) {
/* 116 */         String str = paramArrayOfString[b].toLowerCase();
/* 117 */         if (str.charAt(0) != '-' && str.charAt(0) != '/')
/* 118 */           throw new InvalidArgument(paramArrayOfString[b]); 
/* 119 */         if (str.charAt(0) == '-') {
/* 120 */           str = str.substring(1);
/*     */         }
/*     */ 
/*     */         
/* 124 */         if (str.equals("i")) {
/* 125 */           this.includePaths.addElement(paramArrayOfString[++b]);
/* 126 */         } else if (str.startsWith("i")) {
/* 127 */           this.includePaths.addElement(paramArrayOfString[b].substring(2));
/* 128 */         } else if (str.equals("v") || str.equals("verbose")) {
/*     */           
/* 130 */           this.verbose = true;
/* 131 */         } else if (str.equals("d")) {
/*     */           
/* 133 */           this.definedSymbols.put(paramArrayOfString[++b], "");
/* 134 */         } else if (str.equals("debug")) {
/*     */           
/* 136 */           setDebugFlags(paramArrayOfString[++b]);
/* 137 */         } else if (str.startsWith("d")) {
/* 138 */           this.definedSymbols.put(paramArrayOfString[b].substring(2), "");
/* 139 */         } else if (str.equals("emitall")) {
/*     */           
/* 141 */           this.emitAll = true;
/* 142 */         } else if (str.equals("keep")) {
/*     */           
/* 144 */           this.keepOldFiles = true;
/* 145 */         } else if (str.equals("nowarn")) {
/*     */           
/* 147 */           this.noWarn = true;
/* 148 */         } else if (str.equals("trace")) {
/*     */           
/* 150 */           Runtime.getRuntime().traceMethodCalls(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 159 */         else if (str.equals("cppmodule")) {
/* 160 */           this.cppModule = true;
/* 161 */         } else if (str.equals("version")) {
/*     */           
/* 163 */           this.versionRequest = true;
/* 164 */         } else if (str.equals("corba")) {
/*     */           
/* 166 */           if (b + 1 >= paramArrayOfString.length)
/* 167 */             throw new InvalidArgument(paramArrayOfString[b]); 
/* 168 */           String str1 = paramArrayOfString[++b];
/* 169 */           if (str1.charAt(0) == '-')
/* 170 */             throw new InvalidArgument(paramArrayOfString[b - 1]); 
/*     */           try {
/* 172 */             this.corbaLevel = (new Float(str1)).floatValue();
/* 173 */           } catch (NumberFormatException numberFormatException) {
/* 174 */             throw new InvalidArgument(paramArrayOfString[b]);
/*     */           } 
/*     */         } else {
/* 177 */           vector.addElement(paramArrayOfString[b]);
/* 178 */           b++;
/* 179 */           while (b < paramArrayOfString.length - 1 && paramArrayOfString[b]
/* 180 */             .charAt(0) != '-' && paramArrayOfString[b]
/* 181 */             .charAt(0) != '/') {
/* 182 */             vector.addElement(paramArrayOfString[b++]);
/*     */           }
/* 184 */           b--;
/*     */         } 
/*     */       } 
/* 187 */     } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
/*     */ 
/*     */ 
/*     */       
/* 191 */       throw new InvalidArgument(paramArrayOfString[paramArrayOfString.length - 1]);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     if (b == paramArrayOfString.length - 1)
/* 198 */     { if (paramArrayOfString[b].toLowerCase().equals("-version")) {
/* 199 */         this.versionRequest = true;
/*     */       } else {
/* 201 */         this.file = paramArrayOfString[b];
/*     */       }  }
/* 203 */     else { throw new InvalidArgument(); }
/*     */ 
/*     */     
/* 206 */     Properties properties = new Properties();
/*     */     try {
/* 208 */       DataInputStream dataInputStream = FileLocator.locateFileInClassPath("idl.config");
/* 209 */       properties.load(dataInputStream);
/* 210 */       addIncludePaths(properties);
/* 211 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     if (vector.size() > 0) {
/* 219 */       arrayOfString = new String[vector.size()];
/* 220 */       vector.copyInto((Object[])arrayOfString);
/*     */     } else {
/* 222 */       arrayOfString = new String[0];
/*     */     } 
/* 224 */     parseOtherArgs(arrayOfString, properties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addIncludePaths(Properties paramProperties) {
/* 232 */     String str = paramProperties.getProperty("includes");
/* 233 */     if (str != null) {
/*     */       
/* 235 */       String str1 = System.getProperty("path.separator");
/* 236 */       int i = -str1.length();
/*     */       
/*     */       do {
/* 239 */         str = str.substring(i + str1.length());
/* 240 */         i = str.indexOf(str1);
/* 241 */         if (i < 0)
/* 242 */           i = str.length(); 
/* 243 */         this.includePaths.addElement(str.substring(0, i));
/*     */       }
/* 245 */       while (i != str.length());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 252 */   public String file = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean verbose = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keepOldFiles = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean emitAll = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 283 */   public Vector includePaths = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   public Hashtable definedSymbols = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cppModule = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean versionRequest = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 311 */   public float corbaLevel = 2.2F;
/*     */   public boolean noWarn = false;
/*     */   public boolean scannerDebugFlag = false;
/*     */   public boolean tokenDebugFlag = false;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\Arguments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */