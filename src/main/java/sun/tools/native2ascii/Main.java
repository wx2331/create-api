/*     */ package sun.tools.native2ascii;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */ {
/*  83 */   String inputFileName = null;
/*  84 */   String outputFileName = null;
/*  85 */   File tempFile = null;
/*     */   boolean reverse = false;
/*  87 */   static String encodingString = null;
/*  88 */   static String defaultEncoding = null;
/*  89 */   static CharsetEncoder encoder = null;
/*     */   
/*     */   private static ResourceBundle rsrc;
/*     */ 
/*     */   
/*     */   public synchronized boolean convert(String[] paramArrayOfString) {
/*  95 */     ArrayList<String> arrayList = new ArrayList(2);
/*  96 */     File file = null;
/*  97 */     boolean bool = false;
/*     */ 
/*     */     
/* 100 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 101 */       if (paramArrayOfString[b].equals("-encoding")) {
/* 102 */         if (b + 1 < paramArrayOfString.length) {
/* 103 */           encodingString = paramArrayOfString[++b];
/*     */         } else {
/* 105 */           error(getMsg("err.bad.arg"));
/* 106 */           usage();
/* 107 */           return false;
/*     */         } 
/* 109 */       } else if (paramArrayOfString[b].equals("-reverse")) {
/* 110 */         this.reverse = true;
/*     */       } else {
/* 112 */         if (arrayList.size() > 1) {
/* 113 */           usage();
/* 114 */           return false;
/*     */         } 
/* 116 */         arrayList.add(paramArrayOfString[b]);
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     if (encodingString == null) {
/* 121 */       defaultEncoding = Charset.defaultCharset().name();
/*     */     }
/* 123 */     char[] arrayOfChar = System.getProperty("line.separator").toCharArray();
/*     */     
/*     */     try {
/* 126 */       initializeConverter();
/*     */       
/* 128 */       if (arrayList.size() == 1) {
/* 129 */         this.inputFileName = arrayList.get(0);
/*     */       }
/*     */       
/* 132 */       if (arrayList.size() == 2) {
/* 133 */         this.inputFileName = arrayList.get(0);
/* 134 */         this.outputFileName = arrayList.get(1);
/* 135 */         bool = true;
/*     */       } 
/*     */       
/* 138 */       if (bool) {
/* 139 */         file = new File(this.outputFileName);
/* 140 */         if (file.exists() && !file.canWrite()) {
/* 141 */           throw new Exception(formatMsg("err.cannot.write", this.outputFileName));
/*     */         }
/*     */       } 
/*     */       
/* 145 */       if (this.reverse) {
/* 146 */         try(BufferedReader null = getA2NInput(this.inputFileName); 
/* 147 */             Writer null = getA2NOutput(this.outputFileName)) {
/*     */           String str;
/* 149 */           while ((str = bufferedReader.readLine()) != null) {
/* 150 */             writer.write(str.toCharArray());
/* 151 */             writer.write(arrayOfChar);
/* 152 */             if (this.outputFileName == null) {
/* 153 */               writer.flush();
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 159 */         try(BufferedReader null = getN2AInput(this.inputFileName); 
/* 160 */             BufferedWriter null = getN2AOutput(this.outputFileName)) {
/*     */           String str;
/* 162 */           while ((str = bufferedReader.readLine()) != null) {
/* 163 */             bufferedWriter.write(str.toCharArray());
/* 164 */             bufferedWriter.write(arrayOfChar);
/* 165 */             if (this.outputFileName == null) {
/* 166 */               bufferedWriter.flush();
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 173 */       if (bool) {
/* 174 */         if (file.exists())
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 179 */           file.delete();
/*     */         }
/* 181 */         this.tempFile.renameTo(file);
/*     */       } 
/* 183 */     } catch (Exception exception) {
/* 184 */       error(exception.toString());
/* 185 */       return false;
/*     */     } 
/*     */     
/* 188 */     return true;
/*     */   }
/*     */   
/*     */   private void error(String paramString) {
/* 192 */     System.out.println(paramString);
/*     */   }
/*     */   
/*     */   private void usage() {
/* 196 */     System.out.println(getMsg("usage"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BufferedReader getN2AInput(String paramString) throws Exception {
/*     */     InputStream inputStream;
/* 203 */     if (paramString == null) {
/* 204 */       inputStream = System.in;
/*     */     } else {
/* 206 */       File file = new File(paramString);
/* 207 */       if (!file.canRead()) {
/* 208 */         throw new Exception(formatMsg("err.cannot.read", file.getName()));
/*     */       }
/*     */       
/*     */       try {
/* 212 */         inputStream = new FileInputStream(paramString);
/* 213 */       } catch (IOException iOException) {
/* 214 */         throw new Exception(formatMsg("err.cannot.read", file.getName()));
/*     */       } 
/*     */     } 
/*     */     
/* 218 */     return (encodingString != null) ? new BufferedReader(new InputStreamReader(inputStream, encodingString)) : new BufferedReader(new InputStreamReader(inputStream));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BufferedWriter getN2AOutput(String paramString) throws Exception {
/*     */     OutputStreamWriter outputStreamWriter;
/* 230 */     if (paramString == null) {
/* 231 */       outputStreamWriter = new OutputStreamWriter(System.out, "US-ASCII");
/*     */     } else {
/*     */       
/* 234 */       File file1 = new File(paramString);
/*     */       
/* 236 */       File file2 = file1.getParentFile();
/*     */       
/* 238 */       if (file2 == null) {
/* 239 */         file2 = new File(System.getProperty("user.dir"));
/*     */       }
/* 241 */       this.tempFile = File.createTempFile("_N2A", ".TMP", file2);
/*     */ 
/*     */       
/* 244 */       this.tempFile.deleteOnExit();
/*     */       
/*     */       try {
/* 247 */         outputStreamWriter = new FileWriter(this.tempFile);
/* 248 */       } catch (IOException iOException) {
/* 249 */         throw new Exception(formatMsg("err.cannot.write", this.tempFile.getName()));
/*     */       } 
/*     */     } 
/*     */     
/* 253 */     return new BufferedWriter(new N2AFilter(outputStreamWriter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BufferedReader getA2NInput(String paramString) throws Exception {
/*     */     InputStreamReader inputStreamReader;
/* 261 */     if (paramString == null) {
/* 262 */       inputStreamReader = new InputStreamReader(System.in, "US-ASCII");
/*     */     } else {
/* 264 */       File file = new File(paramString);
/* 265 */       if (!file.canRead()) {
/* 266 */         throw new Exception(formatMsg("err.cannot.read", file.getName()));
/*     */       }
/*     */       
/*     */       try {
/* 270 */         inputStreamReader = new FileReader(paramString);
/* 271 */       } catch (Exception exception) {
/* 272 */         throw new Exception(formatMsg("err.cannot.read", file.getName()));
/*     */       } 
/*     */     } 
/*     */     
/* 276 */     return new BufferedReader(new A2NFilter(inputStreamReader));
/*     */   }
/*     */ 
/*     */   
/*     */   private Writer getA2NOutput(String paramString) throws Exception {
/*     */     FileOutputStream fileOutputStream;
/* 282 */     OutputStreamWriter outputStreamWriter = null;
/* 283 */     PrintStream printStream = null;
/*     */     
/* 285 */     if (paramString == null) {
/* 286 */       printStream = System.out;
/*     */     } else {
/* 288 */       File file1 = new File(paramString);
/*     */       
/* 290 */       File file2 = file1.getParentFile();
/* 291 */       if (file2 == null)
/* 292 */         file2 = new File(System.getProperty("user.dir")); 
/* 293 */       this.tempFile = File.createTempFile("_N2A", ".TMP", file2);
/*     */ 
/*     */       
/* 296 */       this.tempFile.deleteOnExit();
/*     */       
/*     */       try {
/* 299 */         fileOutputStream = new FileOutputStream(this.tempFile);
/* 300 */       } catch (IOException iOException) {
/* 301 */         throw new Exception(formatMsg("err.cannot.write", this.tempFile.getName()));
/*     */       } 
/*     */     } 
/*     */     
/* 305 */     outputStreamWriter = (encodingString != null) ? new OutputStreamWriter(fileOutputStream, encodingString) : new OutputStreamWriter(fileOutputStream);
/*     */ 
/*     */ 
/*     */     
/* 309 */     return outputStreamWriter;
/*     */   }
/*     */   
/*     */   private static Charset lookupCharset(String paramString) {
/* 313 */     if (Charset.isSupported(paramString)) {
/*     */       try {
/* 315 */         return Charset.forName(paramString);
/* 316 */       } catch (UnsupportedCharsetException unsupportedCharsetException) {
/* 317 */         throw new Error(unsupportedCharsetException);
/*     */       } 
/*     */     }
/* 320 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean canConvert(char paramChar) {
/* 324 */     return (encoder != null && encoder.canEncode(paramChar));
/*     */   }
/*     */   
/*     */   private static void initializeConverter() throws UnsupportedEncodingException {
/* 328 */     Charset charset = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 333 */       charset = (encodingString == null) ? lookupCharset(defaultEncoding) : lookupCharset(encodingString);
/*     */ 
/*     */       
/* 336 */       encoder = (charset != null) ? charset.newEncoder() : null;
/*     */     }
/* 338 */     catch (IllegalCharsetNameException illegalCharsetNameException) {
/* 339 */       throw new Error(illegalCharsetNameException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 347 */       rsrc = ResourceBundle.getBundle("sun.tools.native2ascii.resources.MsgNative2ascii");
/*     */     }
/* 349 */     catch (MissingResourceException missingResourceException) {
/* 350 */       throw new Error("Missing message file.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getMsg(String paramString) {
/*     */     try {
/* 356 */       return rsrc.getString(paramString);
/* 357 */     } catch (MissingResourceException missingResourceException) {
/* 358 */       throw new Error("Error in  message file format.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private String formatMsg(String paramString1, String paramString2) {
/* 363 */     String str = getMsg(paramString1);
/* 364 */     return MessageFormat.format(str, new Object[] { paramString2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/* 372 */     Main main = new Main();
/* 373 */     System.exit(main.convert(paramArrayOfString) ? 0 : 1);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\native2ascii\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */