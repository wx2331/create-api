/*     */ package sun.tools.jmap;
/*     */ 
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import sun.tools.attach.HotSpotVirtualMachine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JMap
/*     */ {
/*  47 */   private static String HISTO_OPTION = "-histo";
/*  48 */   private static String LIVE_HISTO_OPTION = "-histo:live";
/*  49 */   private static String DUMP_OPTION_PREFIX = "-dump:";
/*     */ 
/*     */   
/*  52 */   private static String SA_TOOL_OPTIONS = "-heap|-heap:format=b|-clstats|-finalizerinfo";
/*     */ 
/*     */ 
/*     */   
/*  56 */   private static String FORCE_SA_OPTION = "-F";
/*     */ 
/*     */   
/*  59 */   private static String DEFAULT_OPTION = "-pmap"; private static final String LIVE_OBJECTS_OPTION = "-live";
/*     */   
/*     */   public static void main(String[] paramArrayOfString) throws Exception {
/*  62 */     if (paramArrayOfString.length == 0) {
/*  63 */       usage(1);
/*     */     }
/*     */ 
/*     */     
/*  67 */     boolean bool = false;
/*     */ 
/*     */     
/*  70 */     String str = null;
/*     */ 
/*     */ 
/*     */     
/*  74 */     byte b = 0;
/*  75 */     while (b < paramArrayOfString.length) {
/*  76 */       String str1 = paramArrayOfString[b];
/*  77 */       if (!str1.startsWith("-")) {
/*     */         break;
/*     */       }
/*  80 */       if (str1.equals("-help") || str1.equals("-h")) {
/*  81 */         usage(0);
/*  82 */       } else if (str1.equals(FORCE_SA_OPTION)) {
/*  83 */         bool = true;
/*     */       } else {
/*  85 */         if (str != null) {
/*  86 */           usage(1);
/*     */         }
/*  88 */         str = str1;
/*     */       } 
/*  90 */       b++;
/*     */     } 
/*     */ 
/*     */     
/*  94 */     if (str == null) {
/*  95 */       str = DEFAULT_OPTION;
/*     */     }
/*  97 */     if (str.matches(SA_TOOL_OPTIONS)) {
/*  98 */       bool = true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     int i = paramArrayOfString.length - b;
/* 105 */     if (i == 0 || i > 2) {
/* 106 */       usage(1);
/*     */     }
/*     */     
/* 109 */     if (b == 0 || i != 1) {
/* 110 */       bool = true;
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 115 */     else if (!paramArrayOfString[b].matches("[0-9]+")) {
/* 116 */       bool = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (bool) {
/*     */       
/* 126 */       String[] arrayOfString = new String[i];
/* 127 */       for (byte b1 = b; b1 < paramArrayOfString.length; b1++) {
/* 128 */         arrayOfString[b1 - b] = paramArrayOfString[b1];
/*     */       }
/* 130 */       runTool(str, arrayOfString);
/*     */     } else {
/*     */       
/* 133 */       String str1 = paramArrayOfString[1];
/*     */ 
/*     */ 
/*     */       
/* 137 */       if (str.equals(HISTO_OPTION)) {
/* 138 */         histo(str1, false);
/* 139 */       } else if (str.equals(LIVE_HISTO_OPTION)) {
/* 140 */         histo(str1, true);
/* 141 */       } else if (str.startsWith(DUMP_OPTION_PREFIX)) {
/* 142 */         dump(str1, str);
/*     */       } else {
/* 144 */         usage(1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static final String ALL_OBJECTS_OPTION = "-all";
/*     */   
/*     */   private static void runTool(String paramString, String[] paramArrayOfString) throws Exception {
/* 151 */     String[][] arrayOfString = { { "-pmap", "sun.jvm.hotspot.tools.PMap" }, { "-heap", "sun.jvm.hotspot.tools.HeapSummary" }, { "-heap:format=b", "sun.jvm.hotspot.tools.HeapDumper" }, { "-histo", "sun.jvm.hotspot.tools.ObjectHistogram" }, { "-clstats", "sun.jvm.hotspot.tools.ClassLoaderStats" }, { "-finalizerinfo", "sun.jvm.hotspot.tools.FinalizerInfo" } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     String str = null;
/*     */ 
/*     */     
/* 163 */     if (paramString.startsWith(DUMP_OPTION_PREFIX)) {
/*     */       
/* 165 */       String str1 = parseDumpOptions(paramString);
/* 166 */       if (str1 == null) {
/* 167 */         usage(1);
/*     */       }
/*     */ 
/*     */       
/* 171 */       str = "sun.jvm.hotspot.tools.HeapDumper";
/*     */ 
/*     */       
/* 174 */       paramArrayOfString = prepend(str1, paramArrayOfString);
/* 175 */       paramArrayOfString = prepend("-f", paramArrayOfString);
/*     */     } else {
/* 177 */       byte b = 0;
/* 178 */       while (b < arrayOfString.length) {
/* 179 */         if (paramString.equals(arrayOfString[b][0])) {
/* 180 */           str = arrayOfString[b][1];
/*     */           break;
/*     */         } 
/* 183 */         b++;
/*     */       } 
/*     */     } 
/* 186 */     if (str == null) {
/* 187 */       usage(1);
/*     */     }
/*     */ 
/*     */     
/* 191 */     Class<?> clazz = loadClass(str);
/* 192 */     if (clazz == null) {
/* 193 */       usage(1);
/*     */     }
/*     */ 
/*     */     
/* 197 */     Class[] arrayOfClass = { String[].class };
/* 198 */     Method method = clazz.getDeclaredMethod("main", arrayOfClass);
/*     */     
/* 200 */     Object[] arrayOfObject = { paramArrayOfString };
/* 201 */     method.invoke(null, arrayOfObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> loadClass(String paramString) {
/*     */     try {
/* 213 */       return Class.forName(paramString, true, 
/* 214 */           ClassLoader.getSystemClassLoader());
/* 215 */     } catch (Exception exception) {
/* 216 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void histo(String paramString, boolean paramBoolean) throws IOException {
/* 222 */     VirtualMachine virtualMachine = attach(paramString);
/*     */     
/* 224 */     InputStream inputStream = ((HotSpotVirtualMachine)virtualMachine).heapHisto(new Object[] { paramBoolean ? "-live" : "-all" });
/* 225 */     drain(virtualMachine, inputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dump(String paramString1, String paramString2) throws IOException {
/* 230 */     String str = parseDumpOptions(paramString2);
/* 231 */     if (str == null) {
/* 232 */       usage(1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 239 */     str = (new File(str)).getCanonicalPath();
/*     */ 
/*     */     
/* 242 */     boolean bool = isDumpLiveObjects(paramString2);
/*     */     
/* 244 */     VirtualMachine virtualMachine = attach(paramString1);
/* 245 */     System.out.println("Dumping heap to " + str + " ...");
/*     */     
/* 247 */     InputStream inputStream = ((HotSpotVirtualMachine)virtualMachine).dumpHeap(new Object[] { str, bool ? "-live" : "-all" });
/*     */     
/* 249 */     drain(virtualMachine, inputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String parseDumpOptions(String paramString) {
/* 256 */     assert paramString.startsWith(DUMP_OPTION_PREFIX);
/*     */     
/* 258 */     String str = null;
/*     */ 
/*     */     
/* 261 */     String[] arrayOfString = paramString.substring(DUMP_OPTION_PREFIX.length()).split(",");
/*     */     
/* 263 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 264 */       String str1 = arrayOfString[b];
/*     */       
/* 266 */       if (!str1.equals("format=b"))
/*     */       {
/* 268 */         if (!str1.equals("live"))
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 273 */           if (str1.startsWith("file=")) {
/* 274 */             str = str1.substring(5);
/* 275 */             if (str.length() == 0) {
/* 276 */               return null;
/*     */             }
/*     */           } else {
/* 279 */             return null;
/*     */           }  } 
/*     */       }
/*     */     } 
/* 283 */     return str;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDumpLiveObjects(String paramString) {
/* 288 */     String[] arrayOfString = paramString.substring(DUMP_OPTION_PREFIX.length()).split(",");
/* 289 */     for (String str : arrayOfString) {
/* 290 */       if (str.equals("live")) {
/* 291 */         return true;
/*     */       }
/*     */     } 
/* 294 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static VirtualMachine attach(String paramString) {
/*     */     try {
/* 300 */       return VirtualMachine.attach(paramString);
/* 301 */     } catch (Exception exception) {
/* 302 */       String str = exception.getMessage();
/* 303 */       if (str != null) {
/* 304 */         System.err.println(paramString + ": " + str);
/*     */       } else {
/* 306 */         exception.printStackTrace();
/*     */       } 
/* 308 */       if (exception instanceof com.sun.tools.attach.AttachNotSupportedException && haveSA()) {
/* 309 */         System.err.println("The -F option can be used when the target process is not responding");
/*     */       }
/*     */       
/* 312 */       System.exit(1);
/* 313 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void drain(VirtualMachine paramVirtualMachine, InputStream paramInputStream) throws IOException {
/* 320 */     byte[] arrayOfByte = new byte[256];
/*     */     
/*     */     while (true) {
/* 323 */       int i = paramInputStream.read(arrayOfByte);
/* 324 */       if (i > 0) {
/* 325 */         String str = new String(arrayOfByte, 0, i, "UTF-8");
/* 326 */         System.out.print(str);
/*     */       } 
/* 328 */       if (i <= 0) {
/* 329 */         paramInputStream.close();
/* 330 */         paramVirtualMachine.detach();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   } private static String[] prepend(String paramString, String[] paramArrayOfString) {
/* 335 */     String[] arrayOfString = new String[paramArrayOfString.length + 1];
/* 336 */     arrayOfString[0] = paramString;
/* 337 */     System.arraycopy(paramArrayOfString, 0, arrayOfString, 1, paramArrayOfString.length);
/* 338 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean haveSA() {
/* 343 */     Class<?> clazz = loadClass("sun.jvm.hotspot.tools.HeapSummary");
/* 344 */     return (clazz != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void usage(int paramInt) {
/* 349 */     System.err.println("Usage:");
/* 350 */     if (haveSA()) {
/* 351 */       System.err.println("    jmap [option] <pid>");
/* 352 */       System.err.println("        (to connect to running process)");
/* 353 */       System.err.println("    jmap [option] <executable <core>");
/* 354 */       System.err.println("        (to connect to a core file)");
/* 355 */       System.err.println("    jmap [option] [server_id@]<remote server IP or hostname>");
/* 356 */       System.err.println("        (to connect to remote debug server)");
/* 357 */       System.err.println("");
/* 358 */       System.err.println("where <option> is one of:");
/* 359 */       System.err.println("    <none>               to print same info as Solaris pmap");
/* 360 */       System.err.println("    -heap                to print java heap summary");
/* 361 */       System.err.println("    -histo[:live]        to print histogram of java object heap; if the \"live\"");
/* 362 */       System.err.println("                         suboption is specified, only count live objects");
/* 363 */       System.err.println("    -clstats             to print class loader statistics");
/* 364 */       System.err.println("    -finalizerinfo       to print information on objects awaiting finalization");
/* 365 */       System.err.println("    -dump:<dump-options> to dump java heap in hprof binary format");
/* 366 */       System.err.println("                         dump-options:");
/* 367 */       System.err.println("                           live         dump only live objects; if not specified,");
/* 368 */       System.err.println("                                        all objects in the heap are dumped.");
/* 369 */       System.err.println("                           format=b     binary format");
/* 370 */       System.err.println("                           file=<file>  dump heap to <file>");
/* 371 */       System.err.println("                         Example: jmap -dump:live,format=b,file=heap.bin <pid>");
/* 372 */       System.err.println("    -F                   force. Use with -dump:<dump-options> <pid> or -histo");
/* 373 */       System.err.println("                         to force a heap dump or histogram when <pid> does not");
/* 374 */       System.err.println("                         respond. The \"live\" suboption is not supported");
/* 375 */       System.err.println("                         in this mode.");
/* 376 */       System.err.println("    -h | -help           to print this help message");
/* 377 */       System.err.println("    -J<flag>             to pass <flag> directly to the runtime system");
/*     */     } else {
/* 379 */       System.err.println("    jmap -histo <pid>");
/* 380 */       System.err.println("      (to connect to running process and print histogram of java object heap");
/* 381 */       System.err.println("    jmap -dump:<dump-options> <pid>");
/* 382 */       System.err.println("      (to connect to running process and dump java heap)");
/* 383 */       System.err.println("");
/* 384 */       System.err.println("    dump-options:");
/* 385 */       System.err.println("      format=b     binary default");
/* 386 */       System.err.println("      file=<file>  dump heap to <file>");
/* 387 */       System.err.println("");
/* 388 */       System.err.println("    Example:       jmap -dump:format=b,file=heap.bin <pid>");
/*     */     } 
/*     */     
/* 391 */     System.exit(paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jmap\JMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */