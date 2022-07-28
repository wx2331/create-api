/*     */ package sun.tools.javac;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Vector;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassFile;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.util.CommandLine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class Main
/*     */   implements Constants
/*     */ {
/*     */   String program;
/*     */   OutputStream out;
/*     */   public static final int EXIT_OK = 0;
/*     */   public static final int EXIT_ERROR = 1;
/*     */   public static final int EXIT_CMDERR = 2;
/*     */   public static final int EXIT_SYSERR = 3;
/*     */   public static final int EXIT_ABNORMAL = 4;
/*     */   private int exitStatus;
/*     */   private static ResourceBundle messageRB;
/*     */   
/*     */   public Main(OutputStream paramOutputStream, String paramString) {
/*  66 */     this.out = paramOutputStream;
/*  67 */     this.program = paramString;
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
/*     */   public int getExitStatus() {
/*  86 */     return this.exitStatus;
/*     */   }
/*     */   
/*     */   public boolean compilationPerformedSuccessfully() {
/*  90 */     return (this.exitStatus == 0 || this.exitStatus == 1);
/*     */   }
/*     */   
/*     */   public boolean compilationReportedErrors() {
/*  94 */     return (this.exitStatus != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void output(String paramString) {
/* 101 */     PrintStream printStream = (this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true);
/*     */ 
/*     */     
/* 104 */     printStream.println(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void error(String paramString) {
/* 112 */     this.exitStatus = 2;
/* 113 */     output(getText(paramString));
/*     */   }
/*     */   
/*     */   private void error(String paramString1, String paramString2) {
/* 117 */     this.exitStatus = 2;
/* 118 */     output(getText(paramString1, paramString2));
/*     */   }
/*     */   
/*     */   private void error(String paramString1, String paramString2, String paramString3) {
/* 122 */     this.exitStatus = 2;
/* 123 */     output(getText(paramString1, paramString2, paramString3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void usage_error() {
/* 132 */     error("main.usage", this.program);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void initResource() {
/*     */     try {
/* 143 */       messageRB = ResourceBundle.getBundle("sun.tools.javac.resources.javac");
/* 144 */     } catch (MissingResourceException missingResourceException) {
/* 145 */       throw new Error("Fatal: Resource for javac is missing");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getText(String paramString) {
/* 153 */     return getText(paramString, (String)null);
/*     */   }
/*     */   
/*     */   public static String getText(String paramString, int paramInt) {
/* 157 */     return getText(paramString, Integer.toString(paramInt));
/*     */   }
/*     */   
/*     */   public static String getText(String paramString1, String paramString2) {
/* 161 */     return getText(paramString1, paramString2, null);
/*     */   }
/*     */   
/*     */   public static String getText(String paramString1, String paramString2, String paramString3) {
/* 165 */     return getText(paramString1, paramString2, paramString3, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getText(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 170 */     if (messageRB == null) {
/* 171 */       initResource();
/*     */     }
/*     */     try {
/* 174 */       String str = messageRB.getString(paramString1);
/* 175 */       return MessageFormat.format(str, new Object[] { paramString2, paramString3, paramString4 });
/* 176 */     } catch (MissingResourceException missingResourceException) {
/* 177 */       if (paramString2 == null) paramString2 = "null"; 
/* 178 */       if (paramString3 == null) paramString3 = "null"; 
/* 179 */       if (paramString4 == null) paramString4 = "null"; 
/* 180 */       String str = "JAVAC MESSAGE FILE IS BROKEN: key={0}, arguments={1}, {2}, {3}";
/* 181 */       return MessageFormat.format(str, new Object[] { paramString1, paramString2, paramString3, paramString4 });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   private static final String[] releases = new String[] { "1.1", "1.2", "1.3", "1.4" };
/* 189 */   private static final short[] majorVersions = new short[] { 45, 46, 47, 48 };
/* 190 */   private static final short[] minorVersions = new short[] { 3, 0, 0, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean compile(String[] paramArrayOfString) {
/* 196 */     String str1 = null;
/* 197 */     String str2 = null;
/* 198 */     String str3 = null;
/* 199 */     String str4 = null;
/* 200 */     boolean bool1 = false;
/*     */     
/* 202 */     String str5 = null;
/* 203 */     short s1 = 45;
/* 204 */     short s2 = 3;
/*     */     
/* 206 */     File file1 = null;
/*     */     
/* 208 */     File file2 = null;
/* 209 */     String str6 = "-Xjcov";
/* 210 */     String str7 = "-Xjcov:file=";
/*     */     
/* 212 */     int i = 266244;
/* 213 */     long l = System.currentTimeMillis();
/* 214 */     Vector<String> vector = new Vector();
/* 215 */     boolean bool2 = false;
/* 216 */     Object object = null;
/* 217 */     String str8 = null;
/*     */ 
/*     */ 
/*     */     
/* 221 */     String str9 = null;
/* 222 */     String str10 = null;
/*     */     
/* 224 */     this.exitStatus = 0;
/*     */ 
/*     */     
/*     */     try {
/* 228 */       paramArrayOfString = CommandLine.parse(paramArrayOfString);
/* 229 */     } catch (FileNotFoundException fileNotFoundException) {
/* 230 */       error("javac.err.cant.read", fileNotFoundException.getMessage());
/* 231 */       System.exit(1);
/* 232 */     } catch (IOException iOException) {
/* 233 */       iOException.printStackTrace();
/* 234 */       System.exit(1);
/*     */     } 
/*     */ 
/*     */     
/* 238 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 239 */       if (paramArrayOfString[b].equals("-g"))
/* 240 */       { if (str9 != null && !str9.equals("-g"))
/* 241 */           error("main.conflicting.options", str9, "-g"); 
/* 242 */         str9 = "-g";
/* 243 */         i |= 0x1000;
/* 244 */         i |= 0x2000;
/* 245 */         i |= 0x40000; }
/* 246 */       else if (paramArrayOfString[b].equals("-g:none"))
/* 247 */       { if (str9 != null && !str9.equals("-g:none"))
/* 248 */           error("main.conflicting.options", str9, "-g:none"); 
/* 249 */         str9 = "-g:none";
/* 250 */         i &= 0xFFFFEFFF;
/* 251 */         i &= 0xFFFFDFFF;
/* 252 */         i &= 0xFFFBFFFF; }
/* 253 */       else if (paramArrayOfString[b].startsWith("-g:"))
/*     */       
/*     */       { 
/*     */ 
/*     */ 
/*     */         
/* 259 */         if (str9 != null && !str9.equals(paramArrayOfString[b]))
/* 260 */           error("main.conflicting.options", str9, paramArrayOfString[b]); 
/* 261 */         str9 = paramArrayOfString[b];
/* 262 */         String str = paramArrayOfString[b].substring("-g:".length());
/* 263 */         i &= 0xFFFFEFFF;
/* 264 */         i &= 0xFFFFDFFF;
/* 265 */         i &= 0xFFFBFFFF;
/*     */         while (true) {
/* 267 */           if (str.startsWith("lines")) {
/* 268 */             i |= 0x1000;
/* 269 */             str = str.substring("lines".length());
/* 270 */           } else if (str.startsWith("vars")) {
/* 271 */             i |= 0x2000;
/* 272 */             str = str.substring("vars".length());
/* 273 */           } else if (str.startsWith("source")) {
/* 274 */             i |= 0x40000;
/* 275 */             str = str.substring("source".length());
/*     */           } else {
/* 277 */             error("main.bad.debug.option", paramArrayOfString[b]);
/* 278 */             usage_error();
/* 279 */             return false;
/*     */           } 
/* 281 */           if (str.length() == 0)
/* 282 */             break;  if (str.startsWith(","))
/* 283 */             str = str.substring(",".length()); 
/*     */         }  }
/* 285 */       else if (paramArrayOfString[b].equals("-O"))
/*     */       
/*     */       { 
/*     */         
/* 289 */         if (str10 != null && !str10.equals("-O"))
/* 290 */           error("main.conflicting.options", str10, "-O"); 
/* 291 */         str10 = "-O"; }
/* 292 */       else if (paramArrayOfString[b].equals("-nowarn"))
/* 293 */       { i &= 0xFFFFFFFB; }
/* 294 */       else if (paramArrayOfString[b].equals("-deprecation"))
/* 295 */       { i |= 0x200; }
/* 296 */       else if (paramArrayOfString[b].equals("-verbose"))
/* 297 */       { i |= 0x1; }
/* 298 */       else if (paramArrayOfString[b].equals("-nowrite"))
/* 299 */       { bool2 = true; }
/* 300 */       else if (paramArrayOfString[b].equals("-classpath"))
/* 301 */       { if (b + 1 < paramArrayOfString.length) {
/* 302 */           if (str2 != null) {
/* 303 */             error("main.option.already.seen", "-classpath");
/*     */           }
/* 305 */           str2 = paramArrayOfString[++b];
/*     */         } else {
/* 307 */           error("main.option.requires.argument", "-classpath");
/* 308 */           usage_error();
/* 309 */           return false;
/*     */         }  }
/* 311 */       else if (paramArrayOfString[b].equals("-sourcepath"))
/* 312 */       { if (b + 1 < paramArrayOfString.length) {
/* 313 */           if (str1 != null) {
/* 314 */             error("main.option.already.seen", "-sourcepath");
/*     */           }
/* 316 */           str1 = paramArrayOfString[++b];
/*     */         } else {
/* 318 */           error("main.option.requires.argument", "-sourcepath");
/* 319 */           usage_error();
/* 320 */           return false;
/*     */         }  }
/* 322 */       else if (paramArrayOfString[b].equals("-sysclasspath"))
/* 323 */       { if (b + 1 < paramArrayOfString.length) {
/* 324 */           if (str3 != null) {
/* 325 */             error("main.option.already.seen", "-sysclasspath");
/*     */           }
/* 327 */           str3 = paramArrayOfString[++b];
/*     */         } else {
/* 329 */           error("main.option.requires.argument", "-sysclasspath");
/* 330 */           usage_error();
/* 331 */           return false;
/*     */         }  }
/* 333 */       else if (paramArrayOfString[b].equals("-bootclasspath"))
/* 334 */       { if (b + 1 < paramArrayOfString.length) {
/* 335 */           if (str3 != null) {
/* 336 */             error("main.option.already.seen", "-bootclasspath");
/*     */           }
/* 338 */           str3 = paramArrayOfString[++b];
/*     */         } else {
/* 340 */           error("main.option.requires.argument", "-bootclasspath");
/* 341 */           usage_error();
/* 342 */           return false;
/*     */         }  }
/* 344 */       else if (paramArrayOfString[b].equals("-extdirs"))
/* 345 */       { if (b + 1 < paramArrayOfString.length) {
/* 346 */           if (str4 != null) {
/* 347 */             error("main.option.already.seen", "-extdirs");
/*     */           }
/* 349 */           str4 = paramArrayOfString[++b];
/*     */         } else {
/* 351 */           error("main.option.requires.argument", "-extdirs");
/* 352 */           usage_error();
/* 353 */           return false;
/*     */         }  }
/* 355 */       else if (paramArrayOfString[b].equals("-encoding"))
/* 356 */       { if (b + 1 < paramArrayOfString.length) {
/* 357 */           if (str8 != null)
/* 358 */             error("main.option.already.seen", "-encoding"); 
/* 359 */           str8 = paramArrayOfString[++b];
/*     */         } else {
/* 361 */           error("main.option.requires.argument", "-encoding");
/* 362 */           usage_error();
/* 363 */           return false;
/*     */         }  }
/* 365 */       else if (paramArrayOfString[b].equals("-target"))
/* 366 */       { if (b + 1 < paramArrayOfString.length) {
/* 367 */           if (str5 != null)
/* 368 */             error("main.option.already.seen", "-target"); 
/* 369 */           str5 = paramArrayOfString[++b];
/*     */           byte b1;
/* 371 */           for (b1 = 0; b1 < releases.length; b1++) {
/* 372 */             if (releases[b1].equals(str5)) {
/* 373 */               s1 = majorVersions[b1];
/* 374 */               s2 = minorVersions[b1];
/*     */               break;
/*     */             } 
/*     */           } 
/* 378 */           if (b1 == releases.length) {
/* 379 */             error("main.unknown.release", str5);
/* 380 */             usage_error();
/* 381 */             return false;
/*     */           } 
/*     */         } else {
/* 384 */           error("main.option.requires.argument", "-target");
/* 385 */           usage_error();
/* 386 */           return false;
/*     */         }  }
/* 388 */       else if (paramArrayOfString[b].equals("-d"))
/* 389 */       { if (b + 1 < paramArrayOfString.length) {
/* 390 */           if (file1 != null)
/* 391 */             error("main.option.already.seen", "-d"); 
/* 392 */           file1 = new File(paramArrayOfString[++b]);
/* 393 */           if (!file1.exists()) {
/* 394 */             error("main.no.such.directory", file1.getPath());
/* 395 */             usage_error();
/* 396 */             return false;
/*     */           } 
/*     */         } else {
/* 399 */           error("main.option.requires.argument", "-d");
/* 400 */           usage_error();
/* 401 */           return false;
/*     */         }
/*     */          }
/* 404 */       else if (paramArrayOfString[b].equals(str6))
/* 405 */       { i |= 0x40;
/* 406 */         i &= 0xFFFFBFFF;
/* 407 */         i &= 0xFFFF7FFF; }
/* 408 */       else if (paramArrayOfString[b].startsWith(str7) && paramArrayOfString[b]
/* 409 */         .length() > str7.length())
/* 410 */       { file2 = new File(paramArrayOfString[b].substring(str7.length()));
/* 411 */         i &= 0xFFFFBFFF;
/* 412 */         i &= 0xFFFF7FFF;
/* 413 */         i |= 0x40;
/* 414 */         i |= 0x80; }
/*     */       
/* 416 */       else if (paramArrayOfString[b].equals("-XO"))
/*     */       
/* 418 */       { if (str10 != null && !str10.equals("-XO"))
/* 419 */           error("main.conflicting.options", str10, "-XO"); 
/* 420 */         str10 = "-XO";
/* 421 */         i |= 0x4000; }
/* 422 */       else if (paramArrayOfString[b].equals("-Xinterclass"))
/* 423 */       { if (str10 != null && !str10.equals("-Xinterclass"))
/* 424 */           error("main.conflicting.options", str10, "-Xinterclass"); 
/* 425 */         str10 = "-Xinterclass";
/* 426 */         i |= 0x4000;
/* 427 */         i |= 0x8000;
/* 428 */         i |= 0x20; }
/* 429 */       else if (paramArrayOfString[b].equals("-Xdepend"))
/* 430 */       { i |= 0x20; }
/* 431 */       else if (paramArrayOfString[b].equals("-Xdebug"))
/* 432 */       { i |= 0x2;
/*     */         
/*     */          }
/*     */       
/* 436 */       else if (paramArrayOfString[b].equals("-xdepend") || paramArrayOfString[b].equals("-Xjws"))
/* 437 */       { i |= 0x400;
/*     */         
/* 439 */         if (this.out == System.err) {
/* 440 */           this.out = System.out;
/*     */         } }
/* 442 */       else if (paramArrayOfString[b].equals("-Xstrictdefault"))
/*     */       
/* 444 */       { i |= 0x20000; }
/* 445 */       else if (paramArrayOfString[b].equals("-Xverbosepath"))
/* 446 */       { bool1 = true; }
/* 447 */       else if (paramArrayOfString[b].equals("-Xstdout"))
/* 448 */       { this.out = System.out; }
/* 449 */       else { if (paramArrayOfString[b].equals("-X")) {
/* 450 */           error("main.unsupported.usage");
/* 451 */           return false;
/* 452 */         }  if (paramArrayOfString[b].equals("-Xversion1.2")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 459 */           i |= 0x800;
/* 460 */         } else if (paramArrayOfString[b].endsWith(".java")) {
/* 461 */           vector.addElement(paramArrayOfString[b]);
/*     */         } else {
/* 463 */           error("main.no.such.option", paramArrayOfString[b]);
/* 464 */           usage_error();
/* 465 */           return false;
/*     */         }  }
/*     */     
/* 468 */     }  if (vector.size() == 0 || this.exitStatus == 2) {
/* 469 */       usage_error();
/* 470 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 474 */     BatchEnvironment batchEnvironment = BatchEnvironment.create(this.out, str1, str2, str3, str4);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 479 */     if (bool1) {
/* 480 */       output(getText("main.path.msg", batchEnvironment.sourcePath
/* 481 */             .toString(), batchEnvironment.binaryPath
/* 482 */             .toString()));
/*     */     }
/*     */     
/* 485 */     batchEnvironment.flags |= i;
/* 486 */     batchEnvironment.majorVersion = s1;
/* 487 */     batchEnvironment.minorVersion = s2;
/*     */     
/* 489 */     batchEnvironment.covFile = file2;
/*     */     
/* 491 */     batchEnvironment.setCharacterEncoding(str8);
/*     */ 
/*     */ 
/*     */     
/* 495 */     String str11 = getText("main.no.memory");
/* 496 */     String str12 = getText("main.stack.overflow");
/*     */     
/* 498 */     batchEnvironment.error(0L, "warn.class.is.deprecated", "sun.tools.javac.Main");
/*     */     try {
/*     */       boolean bool;
/*     */       Enumeration<String> enumeration;
/* 502 */       for (enumeration = vector.elements(); enumeration.hasMoreElements(); ) {
/* 503 */         File file = new File(enumeration.nextElement());
/*     */         try {
/* 505 */           batchEnvironment.parseFile(new ClassFile(file));
/* 506 */         } catch (FileNotFoundException fileNotFoundException) {
/* 507 */           batchEnvironment.error(0L, "cant.read", file.getPath());
/* 508 */           this.exitStatus = 2;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 514 */       for (enumeration = batchEnvironment.getClasses(); enumeration.hasMoreElements(); ) {
/* 515 */         ClassDeclaration classDeclaration = (ClassDeclaration)enumeration.nextElement();
/* 516 */         if (classDeclaration.getStatus() != 4 || 
/* 517 */           classDeclaration.getClassDefinition().isLocal())
/*     */           continue; 
/*     */         try {
/* 520 */           classDeclaration.getClassDefinition(batchEnvironment);
/* 521 */         } catch (ClassNotFound classNotFound) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 527 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);
/*     */ 
/*     */       
/*     */       do {
/* 531 */         bool = true;
/* 532 */         batchEnvironment.flushErrors();
/* 533 */         for (Enumeration<ClassDeclaration> enumeration1 = batchEnvironment.getClasses(); enumeration1.hasMoreElements(); ) {
/* 534 */           SourceClass sourceClass; String str13, str14; File file; ClassDeclaration classDeclaration = enumeration1.nextElement();
/*     */ 
/*     */           
/* 537 */           switch (classDeclaration.getStatus()) {
/*     */             case 0:
/* 539 */               if (!batchEnvironment.dependencies());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 3:
/* 546 */               batchEnvironment.dtEvent("Main.compile (SOURCE): loading, " + classDeclaration);
/* 547 */               bool = false;
/* 548 */               batchEnvironment.loadDefinition(classDeclaration);
/* 549 */               if (classDeclaration.getStatus() != 4)
/*     */               {
/* 551 */                 batchEnvironment.dtEvent("Main.compile (SOURCE): not parsed, " + classDeclaration);
/*     */               }
/*     */ 
/*     */ 
/*     */             
/*     */             case 4:
/* 557 */               if (classDeclaration.getClassDefinition().isInsideLocal()) {
/*     */ 
/*     */                 
/* 560 */                 batchEnvironment.dtEvent("Main.compile (PARSED): skipping local class, " + classDeclaration);
/*     */                 continue;
/*     */               } 
/* 563 */               bool = false;
/* 564 */               batchEnvironment.dtEvent("Main.compile (PARSED): checking, " + classDeclaration);
/* 565 */               sourceClass = (SourceClass)classDeclaration.getClassDefinition(batchEnvironment);
/* 566 */               sourceClass.check(batchEnvironment);
/* 567 */               classDeclaration.setDefinition(sourceClass, 5);
/*     */ 
/*     */             
/*     */             case 5:
/* 571 */               sourceClass = (SourceClass)classDeclaration.getClassDefinition(batchEnvironment);
/*     */               
/* 573 */               if (sourceClass.getError()) {
/*     */                 
/* 575 */                 batchEnvironment.dtEvent("Main.compile (CHECKED): bailing out on error, " + classDeclaration);
/* 576 */                 classDeclaration.setDefinition(sourceClass, 6);
/*     */                 continue;
/*     */               } 
/* 579 */               bool = false;
/* 580 */               byteArrayOutputStream.reset();
/*     */               
/* 582 */               batchEnvironment.dtEvent("Main.compile (CHECKED): compiling, " + classDeclaration);
/* 583 */               sourceClass.compile(byteArrayOutputStream);
/* 584 */               classDeclaration.setDefinition(sourceClass, 6);
/* 585 */               sourceClass.cleanup(batchEnvironment);
/*     */               
/* 587 */               if (sourceClass.getNestError() || bool2) {
/*     */                 continue;
/*     */               }
/*     */               
/* 591 */               str13 = classDeclaration.getName().getQualifier().toString().replace('.', File.separatorChar);
/* 592 */               str14 = classDeclaration.getName().getFlatName().toString().replace('.', '$') + ".class";
/*     */ 
/*     */               
/* 595 */               if (file1 != null) {
/* 596 */                 if (str13.length() > 0) {
/* 597 */                   file = new File(file1, str13);
/* 598 */                   if (!file.exists()) {
/* 599 */                     file.mkdirs();
/*     */                   }
/* 601 */                   file = new File(file, str14);
/*     */                 } else {
/* 603 */                   file = new File(file1, str14);
/*     */                 } 
/*     */               } else {
/* 606 */                 ClassFile classFile = (ClassFile)sourceClass.getSource();
/* 607 */                 if (classFile.isZipped()) {
/* 608 */                   batchEnvironment.error(0L, "cant.write", classFile.getPath());
/* 609 */                   this.exitStatus = 2;
/*     */                   continue;
/*     */                 } 
/* 612 */                 file = new File(classFile.getPath());
/* 613 */                 file = new File(file.getParent(), str14);
/*     */               } 
/*     */ 
/*     */               
/*     */               try {
/* 618 */                 FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());
/* 619 */                 byteArrayOutputStream.writeTo(fileOutputStream);
/* 620 */                 fileOutputStream.close();
/*     */                 
/* 622 */                 if (batchEnvironment.verbose()) {
/* 623 */                   output(getText("main.wrote", file.getPath()));
/*     */                 }
/* 625 */               } catch (IOException iOException) {
/* 626 */                 batchEnvironment.error(0L, "cant.write", file.getPath());
/* 627 */                 this.exitStatus = 2;
/*     */               } 
/*     */ 
/*     */               
/* 631 */               if (batchEnvironment.print_dependencies()) {
/* 632 */                 sourceClass.printClassDependencies(batchEnvironment);
/*     */               }
/*     */           } 
/*     */         } 
/* 636 */       } while (!bool);
/* 637 */     } catch (OutOfMemoryError outOfMemoryError) {
/*     */ 
/*     */       
/* 640 */       batchEnvironment.output(str11);
/* 641 */       this.exitStatus = 3;
/* 642 */       return false;
/* 643 */     } catch (StackOverflowError stackOverflowError) {
/* 644 */       batchEnvironment.output(str12);
/* 645 */       this.exitStatus = 3;
/* 646 */       return false;
/* 647 */     } catch (Error error) {
/*     */ 
/*     */ 
/*     */       
/* 651 */       if (batchEnvironment.nerrors == 0 || batchEnvironment.dump()) {
/* 652 */         error.printStackTrace();
/* 653 */         batchEnvironment.error(0L, "fatal.error");
/* 654 */         this.exitStatus = 4;
/*     */       } 
/* 656 */     } catch (Exception exception) {
/* 657 */       if (batchEnvironment.nerrors == 0 || batchEnvironment.dump()) {
/* 658 */         exception.printStackTrace();
/* 659 */         batchEnvironment.error(0L, "fatal.exception");
/* 660 */         this.exitStatus = 4;
/*     */       } 
/*     */     } 
/*     */     
/* 664 */     int j = batchEnvironment.deprecationFiles.size();
/* 665 */     if (j > 0 && batchEnvironment.warnings()) {
/* 666 */       int k = batchEnvironment.ndeprecations;
/* 667 */       Object object1 = batchEnvironment.deprecationFiles.elementAt(0);
/* 668 */       if (batchEnvironment.deprecation()) {
/* 669 */         if (j > 1) {
/* 670 */           batchEnvironment.error(0L, "warn.note.deprecations", new Integer(j), new Integer(k));
/*     */         } else {
/*     */           
/* 673 */           batchEnvironment.error(0L, "warn.note.1deprecation", object1, new Integer(k));
/*     */         }
/*     */       
/*     */       }
/* 677 */       else if (j > 1) {
/* 678 */         batchEnvironment.error(0L, "warn.note.deprecations.silent", new Integer(j), new Integer(k));
/*     */       } else {
/*     */         
/* 681 */         batchEnvironment.error(0L, "warn.note.1deprecation.silent", object1, new Integer(k));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 687 */     batchEnvironment.flushErrors();
/* 688 */     batchEnvironment.shutdown();
/*     */     
/* 690 */     boolean bool3 = true;
/* 691 */     if (batchEnvironment.nerrors > 0) {
/* 692 */       String str = "";
/* 693 */       if (batchEnvironment.nerrors > 1) {
/* 694 */         str = getText("main.errors", batchEnvironment.nerrors);
/*     */       } else {
/* 696 */         str = getText("main.1error");
/*     */       } 
/* 698 */       if (batchEnvironment.nwarnings > 0) {
/* 699 */         if (batchEnvironment.nwarnings > 1) {
/* 700 */           str = str + ", " + getText("main.warnings", batchEnvironment.nwarnings);
/*     */         } else {
/* 702 */           str = str + ", " + getText("main.1warning");
/*     */         } 
/*     */       }
/* 705 */       output(str);
/* 706 */       if (this.exitStatus == 0)
/*     */       {
/* 708 */         this.exitStatus = 1;
/*     */       }
/* 710 */       bool3 = false;
/*     */     }
/* 712 */     else if (batchEnvironment.nwarnings > 0) {
/* 713 */       if (batchEnvironment.nwarnings > 1) {
/* 714 */         output(getText("main.warnings", batchEnvironment.nwarnings));
/*     */       } else {
/* 716 */         output(getText("main.1warning"));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 721 */     if (batchEnvironment.covdata()) {
/* 722 */       Assembler assembler = new Assembler();
/* 723 */       assembler.GenJCov(batchEnvironment);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 728 */     if (batchEnvironment.verbose()) {
/* 729 */       l = System.currentTimeMillis() - l;
/* 730 */       output(getText("main.done_in", Long.toString(l)));
/*     */     } 
/*     */     
/* 733 */     return bool3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/* 740 */     PrintStream printStream = System.err;
/*     */ 
/*     */ 
/*     */     
/* 744 */     if (Boolean.getBoolean("javac.pipe.output")) {
/* 745 */       printStream = System.out;
/*     */     }
/*     */     
/* 748 */     Main main = new Main(printStream, "javac");
/* 749 */     System.exit(main.compile(paramArrayOfString) ? 0 : main.exitStatus);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\javac\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */