/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.request.MethodEntryRequest;
/*     */ import com.sun.jdi.request.MethodExitRequest;
/*     */ import com.sun.jdi.request.StepRequest;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Env
/*     */ {
/*  47 */   static EventRequestSpecList specList = new EventRequestSpecList();
/*     */   
/*     */   private static VMConnection connection;
/*     */   
/*  51 */   private static SourceMapper sourceMapper = new SourceMapper("");
/*     */   
/*     */   private static List<String> excludes;
/*     */   private static final int SOURCE_CACHE_SIZE = 5;
/*  55 */   private static List<SourceCode> sourceCache = new LinkedList<>();
/*     */   
/*  57 */   private static HashMap<String, Value> savedValues = new HashMap<>();
/*     */   private static Method atExitMethod;
/*     */   
/*     */   static void init(String paramString, boolean paramBoolean, int paramInt) {
/*  61 */     connection = new VMConnection(paramString, paramInt);
/*  62 */     if (!connection.isLaunch() || paramBoolean) {
/*  63 */       connection.open();
/*     */     }
/*     */   }
/*     */   
/*     */   static VMConnection connection() {
/*  68 */     return connection;
/*     */   }
/*     */   
/*     */   static VirtualMachine vm() {
/*  72 */     return connection.vm();
/*     */   }
/*     */   
/*     */   static void shutdown() {
/*  76 */     shutdown(null);
/*     */   }
/*     */   
/*     */   static void shutdown(String paramString) {
/*  80 */     if (connection != null) {
/*     */       try {
/*  82 */         connection.disposeVM();
/*  83 */       } catch (VMDisconnectedException vMDisconnectedException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  88 */     if (paramString != null) {
/*  89 */       MessageOutput.lnprint(paramString);
/*  90 */       MessageOutput.println();
/*     */     } 
/*  92 */     System.exit(0);
/*     */   }
/*     */   
/*     */   static void setSourcePath(String paramString) {
/*  96 */     sourceMapper = new SourceMapper(paramString);
/*  97 */     sourceCache.clear();
/*     */   }
/*     */   
/*     */   static void setSourcePath(List<String> paramList) {
/* 101 */     sourceMapper = new SourceMapper(paramList);
/* 102 */     sourceCache.clear();
/*     */   }
/*     */   
/*     */   static String getSourcePath() {
/* 106 */     return sourceMapper.getSourcePath();
/*     */   }
/*     */   
/*     */   private static List<String> excludes() {
/* 110 */     if (excludes == null) {
/* 111 */       setExcludes("java.*, javax.*, sun.*, com.sun.*");
/*     */     }
/* 113 */     return excludes;
/*     */   }
/*     */   
/*     */   static String excludesString() {
/* 117 */     StringBuffer stringBuffer = new StringBuffer();
/* 118 */     for (String str : excludes()) {
/* 119 */       stringBuffer.append(str);
/* 120 */       stringBuffer.append(",");
/*     */     } 
/* 122 */     return stringBuffer.toString();
/*     */   }
/*     */   
/*     */   static void addExcludes(StepRequest paramStepRequest) {
/* 126 */     for (String str : excludes()) {
/* 127 */       paramStepRequest.addClassExclusionFilter(str);
/*     */     }
/*     */   }
/*     */   
/*     */   static void addExcludes(MethodEntryRequest paramMethodEntryRequest) {
/* 132 */     for (String str : excludes()) {
/* 133 */       paramMethodEntryRequest.addClassExclusionFilter(str);
/*     */     }
/*     */   }
/*     */   
/*     */   static void addExcludes(MethodExitRequest paramMethodExitRequest) {
/* 138 */     for (String str : excludes()) {
/* 139 */       paramMethodExitRequest.addClassExclusionFilter(str);
/*     */     }
/*     */   }
/*     */   
/*     */   static void setExcludes(String paramString) {
/* 144 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, " ,;");
/* 145 */     ArrayList<String> arrayList = new ArrayList();
/* 146 */     while (stringTokenizer.hasMoreTokens()) {
/* 147 */       arrayList.add(stringTokenizer.nextToken());
/*     */     }
/* 149 */     excludes = arrayList;
/*     */   }
/*     */   
/*     */   static Method atExitMethod() {
/* 153 */     return atExitMethod;
/*     */   }
/*     */   
/*     */   static void setAtExitMethod(Method paramMethod) {
/* 157 */     atExitMethod = paramMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BufferedReader sourceReader(Location paramLocation) {
/* 166 */     return sourceMapper.sourceReader(paramLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   static synchronized String sourceLine(Location paramLocation, int paramInt) throws IOException {
/* 171 */     if (paramInt == -1) {
/* 172 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*     */     try {
/* 176 */       String str = paramLocation.sourceName();
/*     */       
/* 178 */       Iterator<SourceCode> iterator = sourceCache.iterator();
/* 179 */       SourceCode sourceCode = null;
/* 180 */       while (iterator.hasNext()) {
/* 181 */         SourceCode sourceCode1 = iterator.next();
/* 182 */         if (sourceCode1.fileName().equals(str)) {
/* 183 */           sourceCode = sourceCode1;
/* 184 */           iterator.remove();
/*     */           break;
/*     */         } 
/*     */       } 
/* 188 */       if (sourceCode == null) {
/* 189 */         BufferedReader bufferedReader = sourceReader(paramLocation);
/* 190 */         if (bufferedReader == null) {
/* 191 */           throw new FileNotFoundException(str);
/*     */         }
/* 193 */         sourceCode = new SourceCode(str, bufferedReader);
/* 194 */         if (sourceCache.size() == 5) {
/* 195 */           sourceCache.remove(sourceCache.size() - 1);
/*     */         }
/*     */       } 
/* 198 */       sourceCache.add(0, sourceCode);
/* 199 */       return sourceCode.sourceLine(paramInt);
/* 200 */     } catch (AbsentInformationException absentInformationException) {
/* 201 */       throw new IllegalArgumentException();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static String description(ObjectReference paramObjectReference) {
/* 207 */     ReferenceType referenceType = paramObjectReference.referenceType();
/* 208 */     long l = paramObjectReference.uniqueID();
/* 209 */     if (referenceType == null) {
/* 210 */       return toHex(l);
/*     */     }
/* 212 */     return MessageOutput.format("object description and hex id", new Object[] { referenceType
/* 213 */           .name(), 
/* 214 */           toHex(l) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static String toHex(long paramLong) {
/* 220 */     char[] arrayOfChar1 = new char[16];
/* 221 */     char[] arrayOfChar2 = new char[18];
/*     */ 
/*     */     
/* 224 */     byte b1 = 0;
/*     */     do {
/* 226 */       long l = paramLong & 0xFL;
/* 227 */       arrayOfChar1[b1++] = (char)(int)((l < 10L) ? (48L + l) : (97L + l - 10L));
/* 228 */     } while ((paramLong >>>= 4L) > 0L);
/*     */ 
/*     */     
/* 231 */     arrayOfChar2[0] = '0';
/* 232 */     arrayOfChar2[1] = 'x';
/* 233 */     byte b2 = 2;
/* 234 */     while (--b1 >= 0) {
/* 235 */       arrayOfChar2[b2++] = arrayOfChar1[b1];
/*     */     }
/* 237 */     return new String(arrayOfChar2, 0, b2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static long fromHex(String paramString) {
/* 243 */     String str = paramString.startsWith("0x") ? paramString.substring(2).toLowerCase() : paramString.toLowerCase();
/* 244 */     if (paramString.length() == 0) {
/* 245 */       throw new NumberFormatException();
/*     */     }
/*     */     
/* 248 */     long l = 0L;
/* 249 */     for (byte b = 0; b < str.length(); b++) {
/* 250 */       char c = str.charAt(b);
/* 251 */       if (c >= '0' && c <= '9') {
/* 252 */         l = l * 16L + (c - 48);
/* 253 */       } else if (c >= 'a' && c <= 'f') {
/* 254 */         l = l * 16L + (c - 97 + 10);
/*     */       } else {
/* 256 */         throw new NumberFormatException();
/*     */       } 
/*     */     } 
/* 259 */     return l;
/*     */   }
/*     */   
/*     */   static ReferenceType getReferenceTypeFromToken(String paramString) {
/* 263 */     ReferenceType referenceType = null;
/* 264 */     if (Character.isDigit(paramString.charAt(0))) {
/* 265 */       referenceType = null;
/* 266 */     } else if (paramString.startsWith("*.")) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 271 */       paramString = paramString.substring(1);
/* 272 */       for (ReferenceType referenceType1 : vm().allClasses()) {
/* 273 */         if (referenceType1.name().endsWith(paramString)) {
/* 274 */           referenceType = referenceType1;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 280 */       List<ReferenceType> list = vm().classesByName(paramString);
/* 281 */       if (list.size() > 0)
/*     */       {
/* 283 */         referenceType = list.get(0);
/*     */       }
/*     */     } 
/* 286 */     return referenceType;
/*     */   }
/*     */   
/*     */   static Set<String> getSaveKeys() {
/* 290 */     return savedValues.keySet();
/*     */   }
/*     */   
/*     */   static Value getSavedValue(String paramString) {
/* 294 */     return savedValues.get(paramString);
/*     */   }
/*     */   
/*     */   static void setSavedValue(String paramString, Value paramValue) {
/* 298 */     savedValues.put(paramString, paramValue);
/*     */   }
/*     */   
/*     */   static class SourceCode {
/*     */     private String fileName;
/* 303 */     private List<String> sourceLines = new ArrayList<>();
/*     */     
/*     */     SourceCode(String param1String, BufferedReader param1BufferedReader) throws IOException {
/* 306 */       this.fileName = param1String;
/*     */       try {
/* 308 */         String str = param1BufferedReader.readLine();
/* 309 */         while (str != null) {
/* 310 */           this.sourceLines.add(str);
/* 311 */           str = param1BufferedReader.readLine();
/*     */         } 
/*     */       } finally {
/* 314 */         param1BufferedReader.close();
/*     */       } 
/*     */     }
/*     */     
/*     */     String fileName() {
/* 319 */       return this.fileName;
/*     */     }
/*     */     
/*     */     String sourceLine(int param1Int) {
/* 323 */       int i = param1Int - 1;
/* 324 */       if (i >= this.sourceLines.size()) {
/* 325 */         return null;
/*     */       }
/* 327 */       return this.sourceLines.get(i);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\Env.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */