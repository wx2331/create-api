/*     */ package sun.rmi.rmic;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import sun.rmi.rmic.newrmic.Main;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassFile;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.ClassPath;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.javac.SourceClass;
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
/*     */   implements Constants
/*     */ {
/*     */   String sourcePathArg;
/*     */   String sysClassPathArg;
/*     */   String extDirsArg;
/*     */   String classPathString;
/*     */   File destDir;
/*     */   int flags;
/*     */   long tm;
/*     */   Vector<String> classes;
/*     */   boolean nowrite;
/*     */   boolean nocompile;
/*     */   boolean keepGenerated;
/*     */   boolean status;
/*     */   String[] generatorArgs;
/*     */   Vector<Generator> generators;
/*  83 */   Class<? extends BatchEnvironment> environmentClass = BatchEnvironment.class;
/*     */ 
/*     */ 
/*     */   
/*     */   boolean iiopGeneration = false;
/*     */ 
/*     */ 
/*     */   
/*     */   String program;
/*     */ 
/*     */ 
/*     */   
/*     */   OutputStream out;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Main(OutputStream paramOutputStream, String paramString) {
/* 101 */     this.out = paramOutputStream;
/* 102 */     this.program = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void output(String paramString) {
/* 109 */     PrintStream printStream = (this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true);
/*     */ 
/*     */     
/* 112 */     printStream.println(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String paramString) {
/* 120 */     output(getText(paramString));
/*     */   }
/*     */   
/*     */   public void error(String paramString1, String paramString2) {
/* 124 */     output(getText(paramString1, paramString2));
/*     */   }
/*     */   
/*     */   public void error(String paramString1, String paramString2, String paramString3) {
/* 128 */     output(getText(paramString1, paramString2, paramString3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void usage() {
/* 135 */     error("rmic.usage", this.program);
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
/*     */   public synchronized boolean compile(String[] paramArrayOfString) {
/* 149 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 150 */       if (paramArrayOfString[b].equals("-Xnew")) {
/* 151 */         return (new Main(this.out, this.program))
/* 152 */           .compile(paramArrayOfString);
/*     */       }
/*     */     } 
/*     */     
/* 156 */     if (!parseArgs(paramArrayOfString)) {
/* 157 */       return false;
/*     */     }
/*     */     
/* 160 */     if (this.classes.size() == 0) {
/* 161 */       usage();
/* 162 */       return false;
/*     */     } 
/*     */     
/* 165 */     if ((this.flags & 0x4) != 0) {
/* 166 */       for (Generator generator : this.generators) {
/* 167 */         if (generator instanceof RMIGenerator) {
/* 168 */           output(getText("rmic.jrmp.stubs.deprecated", this.program));
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 174 */     return doCompile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDestinationDir() {
/* 181 */     return this.destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean parseArgs(String[] paramArrayOfString) {
/* 188 */     this.sourcePathArg = null;
/* 189 */     this.sysClassPathArg = null;
/* 190 */     this.extDirsArg = null;
/*     */     
/* 192 */     this.classPathString = null;
/* 193 */     this.destDir = null;
/* 194 */     this.flags = 4;
/* 195 */     this.tm = System.currentTimeMillis();
/* 196 */     this.classes = new Vector<>();
/* 197 */     this.nowrite = false;
/* 198 */     this.nocompile = false;
/* 199 */     this.keepGenerated = false;
/* 200 */     this.generatorArgs = getArray("generator.args", true);
/* 201 */     if (this.generatorArgs == null) {
/* 202 */       return false;
/*     */     }
/* 204 */     this.generators = new Vector<>();
/*     */ 
/*     */     
/*     */     try {
/* 208 */       paramArrayOfString = CommandLine.parse(paramArrayOfString);
/* 209 */     } catch (FileNotFoundException fileNotFoundException) {
/* 210 */       error("rmic.cant.read", fileNotFoundException.getMessage());
/* 211 */       return false;
/* 212 */     } catch (IOException iOException) {
/* 213 */       iOException.printStackTrace((this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true));
/*     */ 
/*     */       
/* 216 */       return false;
/*     */     } 
/*     */     
/*     */     byte b;
/* 220 */     for (b = 0; b < paramArrayOfString.length; b++) {
/* 221 */       if (paramArrayOfString[b] != null) {
/* 222 */         if (paramArrayOfString[b].equals("-g"))
/* 223 */         { this.flags &= 0xFFFFBFFF;
/* 224 */           this.flags |= 0x3000;
/* 225 */           paramArrayOfString[b] = null; }
/* 226 */         else if (paramArrayOfString[b].equals("-O"))
/* 227 */         { this.flags &= 0xFFFFEFFF;
/* 228 */           this.flags &= 0xFFFFDFFF;
/* 229 */           this.flags |= 0x4020;
/* 230 */           paramArrayOfString[b] = null; }
/* 231 */         else if (paramArrayOfString[b].equals("-nowarn"))
/* 232 */         { this.flags &= 0xFFFFFFFB;
/* 233 */           paramArrayOfString[b] = null; }
/* 234 */         else if (paramArrayOfString[b].equals("-debug"))
/* 235 */         { this.flags |= 0x2;
/* 236 */           paramArrayOfString[b] = null; }
/* 237 */         else if (paramArrayOfString[b].equals("-depend"))
/* 238 */         { this.flags |= 0x20;
/* 239 */           paramArrayOfString[b] = null; }
/* 240 */         else if (paramArrayOfString[b].equals("-verbose"))
/* 241 */         { this.flags |= 0x1;
/* 242 */           paramArrayOfString[b] = null; }
/* 243 */         else if (paramArrayOfString[b].equals("-nowrite"))
/* 244 */         { this.nowrite = true;
/* 245 */           paramArrayOfString[b] = null; }
/* 246 */         else if (paramArrayOfString[b].equals("-Xnocompile"))
/* 247 */         { this.nocompile = true;
/* 248 */           this.keepGenerated = true;
/* 249 */           paramArrayOfString[b] = null; }
/* 250 */         else if (paramArrayOfString[b].equals("-keep") || paramArrayOfString[b]
/* 251 */           .equals("-keepgenerated"))
/* 252 */         { this.keepGenerated = true;
/* 253 */           paramArrayOfString[b] = null; }
/* 254 */         else { if (paramArrayOfString[b].equals("-show")) {
/* 255 */             error("rmic.option.unsupported", "-show");
/* 256 */             usage();
/* 257 */             return false;
/* 258 */           }  if (paramArrayOfString[b].equals("-classpath")) {
/* 259 */             if (b + 1 < paramArrayOfString.length) {
/* 260 */               if (this.classPathString != null) {
/* 261 */                 error("rmic.option.already.seen", "-classpath");
/* 262 */                 usage();
/* 263 */                 return false;
/*     */               } 
/* 265 */               paramArrayOfString[b] = null;
/* 266 */               this.classPathString = paramArrayOfString[++b];
/* 267 */               paramArrayOfString[b] = null;
/*     */             } else {
/* 269 */               error("rmic.option.requires.argument", "-classpath");
/* 270 */               usage();
/* 271 */               return false;
/*     */             } 
/* 273 */           } else if (paramArrayOfString[b].equals("-sourcepath")) {
/* 274 */             if (b + 1 < paramArrayOfString.length) {
/* 275 */               if (this.sourcePathArg != null) {
/* 276 */                 error("rmic.option.already.seen", "-sourcepath");
/* 277 */                 usage();
/* 278 */                 return false;
/*     */               } 
/* 280 */               paramArrayOfString[b] = null;
/* 281 */               this.sourcePathArg = paramArrayOfString[++b];
/* 282 */               paramArrayOfString[b] = null;
/*     */             } else {
/* 284 */               error("rmic.option.requires.argument", "-sourcepath");
/* 285 */               usage();
/* 286 */               return false;
/*     */             } 
/* 288 */           } else if (paramArrayOfString[b].equals("-bootclasspath")) {
/* 289 */             if (b + 1 < paramArrayOfString.length) {
/* 290 */               if (this.sysClassPathArg != null) {
/* 291 */                 error("rmic.option.already.seen", "-bootclasspath");
/* 292 */                 usage();
/* 293 */                 return false;
/*     */               } 
/* 295 */               paramArrayOfString[b] = null;
/* 296 */               this.sysClassPathArg = paramArrayOfString[++b];
/* 297 */               paramArrayOfString[b] = null;
/*     */             } else {
/* 299 */               error("rmic.option.requires.argument", "-bootclasspath");
/* 300 */               usage();
/* 301 */               return false;
/*     */             } 
/* 303 */           } else if (paramArrayOfString[b].equals("-extdirs")) {
/* 304 */             if (b + 1 < paramArrayOfString.length) {
/* 305 */               if (this.extDirsArg != null) {
/* 306 */                 error("rmic.option.already.seen", "-extdirs");
/* 307 */                 usage();
/* 308 */                 return false;
/*     */               } 
/* 310 */               paramArrayOfString[b] = null;
/* 311 */               this.extDirsArg = paramArrayOfString[++b];
/* 312 */               paramArrayOfString[b] = null;
/*     */             } else {
/* 314 */               error("rmic.option.requires.argument", "-extdirs");
/* 315 */               usage();
/* 316 */               return false;
/*     */             } 
/* 318 */           } else if (paramArrayOfString[b].equals("-d")) {
/* 319 */             if (b + 1 < paramArrayOfString.length) {
/* 320 */               if (this.destDir != null) {
/* 321 */                 error("rmic.option.already.seen", "-d");
/* 322 */                 usage();
/* 323 */                 return false;
/*     */               } 
/* 325 */               paramArrayOfString[b] = null;
/* 326 */               this.destDir = new File(paramArrayOfString[++b]);
/* 327 */               paramArrayOfString[b] = null;
/* 328 */               if (!this.destDir.exists()) {
/* 329 */                 error("rmic.no.such.directory", this.destDir.getPath());
/* 330 */                 usage();
/* 331 */                 return false;
/*     */               } 
/*     */             } else {
/* 334 */               error("rmic.option.requires.argument", "-d");
/* 335 */               usage();
/* 336 */               return false;
/*     */             }
/*     */           
/* 339 */           } else if (!checkGeneratorArg(paramArrayOfString, b)) {
/* 340 */             usage();
/* 341 */             return false;
/*     */           }  }
/*     */       
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 351 */     for (b = 0; b < paramArrayOfString.length; b++) {
/* 352 */       if (paramArrayOfString[b] != null) {
/* 353 */         if (paramArrayOfString[b].startsWith("-")) {
/* 354 */           error("rmic.no.such.option", paramArrayOfString[b]);
/* 355 */           usage();
/* 356 */           return false;
/*     */         } 
/* 358 */         this.classes.addElement(paramArrayOfString[b]);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 366 */     if (this.generators.size() == 0) {
/* 367 */       addGenerator("default");
/*     */     }
/*     */     
/* 370 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkGeneratorArg(String[] paramArrayOfString, int paramInt) {
/* 379 */     boolean bool = true;
/* 380 */     if (paramArrayOfString[paramInt].startsWith("-")) {
/* 381 */       String str = paramArrayOfString[paramInt].substring(1).toLowerCase();
/* 382 */       for (byte b = 0; b < this.generatorArgs.length; b++) {
/* 383 */         if (str.equalsIgnoreCase(this.generatorArgs[b])) {
/*     */           
/* 385 */           Generator generator = addGenerator(str);
/* 386 */           if (generator == null) {
/* 387 */             return false;
/*     */           }
/* 389 */           bool = generator.parseArgs(paramArrayOfString, this);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 394 */     return bool;
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
/*     */   protected Generator addGenerator(String paramString) {
/*     */     Generator generator;
/* 407 */     String str1 = getString("generator.class." + paramString);
/* 408 */     if (str1 == null) {
/* 409 */       error("rmic.missing.property", paramString);
/* 410 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/* 414 */       generator = (Generator)Class.forName(str1).newInstance();
/* 415 */     } catch (Exception exception) {
/* 416 */       error("rmic.cannot.instantiate", str1);
/* 417 */       return null;
/*     */     } 
/*     */     
/* 420 */     this.generators.addElement(generator);
/*     */ 
/*     */ 
/*     */     
/* 424 */     Class<BatchEnvironment> clazz = BatchEnvironment.class;
/* 425 */     String str2 = getString("generator.env." + paramString);
/* 426 */     if (str2 != null) {
/*     */       try {
/* 428 */         clazz = (Class)Class.forName(str2);
/*     */ 
/*     */ 
/*     */         
/* 432 */         if (this.environmentClass.isAssignableFrom(clazz))
/*     */         {
/*     */ 
/*     */           
/* 436 */           this.environmentClass = clazz.asSubclass(BatchEnvironment.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 443 */         else if (!clazz.isAssignableFrom(this.environmentClass))
/*     */         {
/*     */ 
/*     */           
/* 447 */           error("rmic.cannot.use.both", this.environmentClass.getName(), clazz.getName());
/* 448 */           return null;
/*     */         }
/*     */       
/* 451 */       } catch (ClassNotFoundException classNotFoundException) {
/* 452 */         error("rmic.class.not.found", str2);
/* 453 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 460 */     if (paramString.equals("iiop")) {
/* 461 */       this.iiopGeneration = true;
/*     */     }
/* 463 */     return generator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getArray(String paramString, boolean paramBoolean) {
/* 474 */     String[] arrayOfString = null;
/* 475 */     String str = getString(paramString);
/* 476 */     if (str == null) {
/* 477 */       if (paramBoolean) {
/* 478 */         error("rmic.resource.not.found", paramString);
/* 479 */         return null;
/*     */       } 
/* 481 */       return new String[0];
/*     */     } 
/*     */ 
/*     */     
/* 485 */     StringTokenizer stringTokenizer = new StringTokenizer(str, ", \t\n\r", false);
/* 486 */     int i = stringTokenizer.countTokens();
/* 487 */     arrayOfString = new String[i];
/* 488 */     for (byte b = 0; b < i; b++) {
/* 489 */       arrayOfString[b] = stringTokenizer.nextToken();
/*     */     }
/*     */     
/* 492 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BatchEnvironment getEnv() {
/* 501 */     ClassPath classPath = BatchEnvironment.createClassPath(this.classPathString, this.sysClassPathArg, this.extDirsArg);
/*     */ 
/*     */     
/* 504 */     BatchEnvironment batchEnvironment = null;
/*     */     try {
/* 506 */       Class[] arrayOfClass = { OutputStream.class, ClassPath.class, Main.class };
/* 507 */       Object[] arrayOfObject = { this.out, classPath, this };
/*     */       
/* 509 */       Constructor<? extends BatchEnvironment> constructor = this.environmentClass.getConstructor(arrayOfClass);
/* 510 */       batchEnvironment = constructor.newInstance(arrayOfObject);
/* 511 */       batchEnvironment.reset();
/*     */     }
/* 513 */     catch (Exception exception) {
/* 514 */       error("rmic.cannot.instantiate", this.environmentClass.getName());
/*     */     } 
/* 516 */     return batchEnvironment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doCompile() {
/* 525 */     BatchEnvironment batchEnvironment = getEnv();
/* 526 */     batchEnvironment.flags |= this.flags;
/*     */ 
/*     */ 
/*     */     
/* 530 */     batchEnvironment.majorVersion = 45;
/* 531 */     batchEnvironment.minorVersion = 3;
/*     */ 
/*     */ 
/*     */     
/* 535 */     String str1 = getText("rmic.no.memory");
/* 536 */     String str2 = getText("rmic.stack.overflow");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 542 */       for (int i = this.classes.size() - 1; i >= 0; i--) {
/*     */         
/* 544 */         Identifier identifier = Identifier.lookup(this.classes.elementAt(i));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 556 */         identifier = batchEnvironment.resolvePackageQualifiedName(identifier);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 566 */         identifier = Names.mangleClass(identifier);
/*     */         
/* 568 */         ClassDeclaration classDeclaration = batchEnvironment.getClassDeclaration(identifier);
/*     */         try {
/* 570 */           ClassDefinition classDefinition = classDeclaration.getClassDefinition((Environment)batchEnvironment);
/* 571 */           for (byte b = 0; b < this.generators.size(); b++) {
/* 572 */             Generator generator = this.generators.elementAt(b);
/* 573 */             generator.generate(batchEnvironment, classDefinition, this.destDir);
/*     */           } 
/* 575 */         } catch (ClassNotFound classNotFound) {
/* 576 */           batchEnvironment.error(0L, "rmic.class.not.found", identifier);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 582 */       if (!this.nocompile) {
/* 583 */         compileAllClasses(batchEnvironment);
/*     */       }
/* 585 */     } catch (OutOfMemoryError outOfMemoryError) {
/*     */ 
/*     */       
/* 588 */       batchEnvironment.output(str1);
/* 589 */       return false;
/* 590 */     } catch (StackOverflowError stackOverflowError) {
/* 591 */       batchEnvironment.output(str2);
/* 592 */       return false;
/* 593 */     } catch (Error error) {
/*     */ 
/*     */ 
/*     */       
/* 597 */       if (batchEnvironment.nerrors == 0 || batchEnvironment.dump()) {
/* 598 */         batchEnvironment.error(0L, "fatal.error");
/* 599 */         error.printStackTrace((this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true));
/*     */       }
/*     */     
/*     */     }
/* 603 */     catch (Exception exception) {
/* 604 */       if (batchEnvironment.nerrors == 0 || batchEnvironment.dump()) {
/* 605 */         batchEnvironment.error(0L, "fatal.exception");
/* 606 */         exception.printStackTrace((this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 612 */     batchEnvironment.flushErrors();
/*     */     
/* 614 */     boolean bool = true;
/* 615 */     if (batchEnvironment.nerrors > 0) {
/* 616 */       String str = "";
/* 617 */       if (batchEnvironment.nerrors > 1) {
/* 618 */         str = getText("rmic.errors", batchEnvironment.nerrors);
/*     */       } else {
/* 620 */         str = getText("rmic.1error");
/*     */       } 
/* 622 */       if (batchEnvironment.nwarnings > 0) {
/* 623 */         if (batchEnvironment.nwarnings > 1) {
/* 624 */           str = str + ", " + getText("rmic.warnings", batchEnvironment.nwarnings);
/*     */         } else {
/* 626 */           str = str + ", " + getText("rmic.1warning");
/*     */         } 
/*     */       }
/* 629 */       output(str);
/* 630 */       bool = false;
/*     */     }
/* 632 */     else if (batchEnvironment.nwarnings > 0) {
/* 633 */       if (batchEnvironment.nwarnings > 1) {
/* 634 */         output(getText("rmic.warnings", batchEnvironment.nwarnings));
/*     */       } else {
/* 636 */         output(getText("rmic.1warning"));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 642 */     if (!this.keepGenerated) {
/* 643 */       batchEnvironment.deleteGeneratedFiles();
/*     */     }
/*     */ 
/*     */     
/* 647 */     if (batchEnvironment.verbose()) {
/* 648 */       this.tm = System.currentTimeMillis() - this.tm;
/* 649 */       output(getText("rmic.done_in", Long.toString(this.tm)));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 658 */     batchEnvironment.shutdown();
/*     */     
/* 660 */     this.sourcePathArg = null;
/* 661 */     this.sysClassPathArg = null;
/* 662 */     this.extDirsArg = null;
/* 663 */     this.classPathString = null;
/* 664 */     this.destDir = null;
/* 665 */     this.classes = null;
/* 666 */     this.generatorArgs = null;
/* 667 */     this.generators = null;
/* 668 */     this.environmentClass = null;
/* 669 */     this.program = null;
/* 670 */     this.out = null;
/*     */     
/* 672 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void compileAllClasses(BatchEnvironment paramBatchEnvironment) throws ClassNotFound, IOException, InterruptedException {
/*     */     boolean bool;
/* 682 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);
/*     */ 
/*     */     
/*     */     do {
/* 686 */       bool = true;
/* 687 */       for (Enumeration<ClassDeclaration> enumeration = paramBatchEnvironment.getClasses(); enumeration.hasMoreElements(); ) {
/* 688 */         ClassDeclaration classDeclaration = enumeration.nextElement();
/* 689 */         bool = compileClass(classDeclaration, byteArrayOutputStream, paramBatchEnvironment);
/*     */       } 
/* 691 */     } while (!bool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean compileClass(ClassDeclaration paramClassDeclaration, ByteArrayOutputStream paramByteArrayOutputStream, BatchEnvironment paramBatchEnvironment) throws ClassNotFound, IOException, InterruptedException {
/*     */     SourceClass sourceClass;
/*     */     String str1, str2;
/*     */     File file;
/* 705 */     boolean bool = true;
/* 706 */     paramBatchEnvironment.flushErrors();
/*     */ 
/*     */     
/* 709 */     switch (paramClassDeclaration.getStatus()) {
/*     */       
/*     */       case 0:
/* 712 */         if (!paramBatchEnvironment.dependencies()) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/* 720 */         bool = false;
/* 721 */         paramBatchEnvironment.loadDefinition(paramClassDeclaration);
/* 722 */         if (paramClassDeclaration.getStatus() != 4) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 4:
/* 730 */         if (paramClassDeclaration.getClassDefinition().isInsideLocal()) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 739 */         if (this.nocompile) {
/* 740 */           throw new IOException("Compilation required, but -Xnocompile option in effect");
/*     */         }
/*     */         
/* 743 */         bool = false;
/*     */         
/* 745 */         sourceClass = (SourceClass)paramClassDeclaration.getClassDefinition((Environment)paramBatchEnvironment);
/* 746 */         sourceClass.check((Environment)paramBatchEnvironment);
/* 747 */         paramClassDeclaration.setDefinition((ClassDefinition)sourceClass, 5);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 5:
/* 753 */         sourceClass = (SourceClass)paramClassDeclaration.getClassDefinition((Environment)paramBatchEnvironment);
/*     */         
/* 755 */         if (sourceClass.getError()) {
/* 756 */           paramClassDeclaration.setDefinition((ClassDefinition)sourceClass, 6);
/*     */           break;
/*     */         } 
/* 759 */         bool = false;
/* 760 */         paramByteArrayOutputStream.reset();
/* 761 */         sourceClass.compile(paramByteArrayOutputStream);
/* 762 */         paramClassDeclaration.setDefinition((ClassDefinition)sourceClass, 6);
/* 763 */         sourceClass.cleanup((Environment)paramBatchEnvironment);
/*     */         
/* 765 */         if (sourceClass.getError() || this.nowrite) {
/*     */           break;
/*     */         }
/*     */         
/* 769 */         str1 = paramClassDeclaration.getName().getQualifier().toString().replace('.', File.separatorChar);
/* 770 */         str2 = paramClassDeclaration.getName().getFlatName().toString().replace('.', '$') + ".class";
/*     */ 
/*     */         
/* 773 */         if (this.destDir != null) {
/* 774 */           if (str1.length() > 0) {
/* 775 */             file = new File(this.destDir, str1);
/* 776 */             if (!file.exists()) {
/* 777 */               file.mkdirs();
/*     */             }
/* 779 */             file = new File(file, str2);
/*     */           } else {
/* 781 */             file = new File(this.destDir, str2);
/*     */           } 
/*     */         } else {
/* 784 */           ClassFile classFile = (ClassFile)sourceClass.getSource();
/* 785 */           if (classFile.isZipped()) {
/* 786 */             paramBatchEnvironment.error(0L, "cant.write", classFile.getPath());
/*     */             break;
/*     */           } 
/* 789 */           file = new File(classFile.getPath());
/* 790 */           file = new File(file.getParent(), str2);
/*     */         } 
/*     */ 
/*     */         
/*     */         try {
/* 795 */           FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());
/* 796 */           paramByteArrayOutputStream.writeTo(fileOutputStream);
/* 797 */           fileOutputStream.close();
/* 798 */           if (paramBatchEnvironment.verbose()) {
/* 799 */             output(getText("rmic.wrote", file.getPath()));
/*     */           }
/* 801 */         } catch (IOException iOException) {
/* 802 */           paramBatchEnvironment.error(0L, "cant.write", file.getPath());
/*     */         } 
/*     */         break;
/*     */     } 
/* 806 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/* 813 */     Main main = new Main(System.out, "rmic");
/* 814 */     System.exit(main.compile(paramArrayOfString) ? 0 : 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(String paramString) {
/* 822 */     if (!resourcesInitialized) {
/* 823 */       initResources();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 829 */     if (resourcesExt != null) {
/*     */       try {
/* 831 */         return resourcesExt.getString(paramString);
/* 832 */       } catch (MissingResourceException missingResourceException) {}
/*     */     }
/*     */     
/*     */     try {
/* 836 */       return resources.getString(paramString);
/* 837 */     } catch (MissingResourceException missingResourceException) {
/*     */       
/* 839 */       return null;
/*     */     } 
/*     */   }
/*     */   private static boolean resourcesInitialized = false;
/*     */   private static ResourceBundle resources;
/* 844 */   private static ResourceBundle resourcesExt = null;
/*     */ 
/*     */   
/*     */   private static void initResources() {
/*     */     try {
/* 849 */       resources = ResourceBundle.getBundle("sun.rmi.rmic.resources.rmic");
/* 850 */       resourcesInitialized = true;
/*     */       
/*     */       try {
/* 853 */         resourcesExt = ResourceBundle.getBundle("sun.rmi.rmic.resources.rmicext");
/* 854 */       } catch (MissingResourceException missingResourceException) {}
/* 855 */     } catch (MissingResourceException missingResourceException) {
/* 856 */       throw new Error("fatal: missing resource bundle: " + missingResourceException
/* 857 */           .getClassName());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getText(String paramString) {
/* 862 */     String str = getString(paramString);
/* 863 */     if (str == null) {
/* 864 */       str = "no text found: \"" + paramString + "\"";
/*     */     }
/* 866 */     return str;
/*     */   }
/*     */   
/*     */   public static String getText(String paramString, int paramInt) {
/* 870 */     return getText(paramString, Integer.toString(paramInt), null, null);
/*     */   }
/*     */   
/*     */   public static String getText(String paramString1, String paramString2) {
/* 874 */     return getText(paramString1, paramString2, null, null);
/*     */   }
/*     */   
/*     */   public static String getText(String paramString1, String paramString2, String paramString3) {
/* 878 */     return getText(paramString1, paramString2, paramString3, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getText(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 884 */     String str = getString(paramString1);
/* 885 */     if (str == null) {
/* 886 */       str = "no text found: key = \"" + paramString1 + "\", arguments = \"{0}\", \"{1}\", \"{2}\"";
/*     */     }
/*     */ 
/*     */     
/* 890 */     String[] arrayOfString = new String[3];
/* 891 */     arrayOfString[0] = (paramString2 != null) ? paramString2 : "null";
/* 892 */     arrayOfString[1] = (paramString3 != null) ? paramString3 : "null";
/* 893 */     arrayOfString[2] = (paramString4 != null) ? paramString4 : "null";
/*     */     
/* 895 */     return MessageFormat.format(str, (Object[])arrayOfString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */