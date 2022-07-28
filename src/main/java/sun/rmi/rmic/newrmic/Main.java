/*     */ package sun.rmi.rmic.newrmic;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.rmi.rmic.newrmic.jrmp.JrmpGenerator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 101 */   private static final Object batchCountLock = new Object();
/*     */ 
/*     */   
/* 104 */   private static long batchCount = 0L;
/*     */ 
/*     */ 
/*     */   
/* 108 */   private static final Map<Long, Batch> batchTable = Collections.synchronizedMap(new HashMap<>());
/*     */ 
/*     */ 
/*     */   
/*     */   private final PrintStream out;
/*     */ 
/*     */   
/*     */   private final String program;
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/* 120 */     Main main = new Main(System.err, "rmic");
/* 121 */     System.exit(main.compile(paramArrayOfString) ? 0 : 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Main(OutputStream paramOutputStream, String paramString) {
/* 129 */     this.out = (paramOutputStream instanceof PrintStream) ? (PrintStream)paramOutputStream : new PrintStream(paramOutputStream);
/*     */     
/* 131 */     this.program = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean compile(String[] paramArrayOfString) {
/*     */     long l2;
/*     */     boolean bool;
/* 144 */     long l1 = System.currentTimeMillis();
/*     */ 
/*     */     
/* 147 */     synchronized (batchCountLock) {
/* 148 */       l2 = batchCount++;
/*     */     } 
/*     */ 
/*     */     
/* 152 */     Batch batch = parseArgs(paramArrayOfString);
/* 153 */     if (batch == null) {
/* 154 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 164 */       batchTable.put(Long.valueOf(l2), batch);
/* 165 */       bool = invokeJavadoc(batch, l2);
/*     */     } finally {
/* 167 */       batchTable.remove(Long.valueOf(l2));
/*     */     } 
/*     */     
/* 170 */     if (batch.verbose) {
/* 171 */       long l = System.currentTimeMillis() - l1;
/* 172 */       output(Resources.getText("rmic.done_in", new String[] {
/* 173 */               Long.toString(l)
/*     */             }));
/*     */     } 
/* 176 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void output(String paramString) {
/* 184 */     this.out.println(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String paramString, String... paramVarArgs) {
/* 194 */     output(Resources.getText(paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void usage() {
/* 205 */     error("rmic.usage", new String[] { this.program });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Batch parseArgs(String[] paramArrayOfString) {
/* 215 */     Batch batch = new Batch();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 221 */       paramArrayOfString = CommandLine.parse(paramArrayOfString);
/* 222 */     } catch (FileNotFoundException fileNotFoundException) {
/* 223 */       error("rmic.cant.read", new String[] { fileNotFoundException.getMessage() });
/* 224 */       return null;
/* 225 */     } catch (IOException iOException) {
/* 226 */       iOException.printStackTrace(this.out);
/* 227 */       return null;
/*     */     } 
/*     */     byte b;
/* 230 */     for (b = 0; b < paramArrayOfString.length; b++) {
/*     */       
/* 232 */       if (paramArrayOfString[b] != null)
/*     */       {
/*     */ 
/*     */         
/* 236 */         if (paramArrayOfString[b].equals("-Xnew")) {
/*     */           
/* 238 */           paramArrayOfString[b] = null;
/*     */         } else {
/* 240 */           if (paramArrayOfString[b].equals("-show")) {
/*     */             
/* 242 */             error("rmic.option.unsupported", new String[] { paramArrayOfString[b] });
/* 243 */             usage();
/* 244 */             return null;
/*     */           } 
/* 246 */           if (paramArrayOfString[b].equals("-O")) {
/*     */             
/* 248 */             error("rmic.option.unsupported", new String[] { paramArrayOfString[b] });
/* 249 */             paramArrayOfString[b] = null;
/*     */           }
/* 251 */           else if (paramArrayOfString[b].equals("-debug")) {
/*     */             
/* 253 */             error("rmic.option.unsupported", new String[] { paramArrayOfString[b] });
/* 254 */             paramArrayOfString[b] = null;
/*     */           }
/* 256 */           else if (paramArrayOfString[b].equals("-depend")) {
/*     */ 
/*     */             
/* 259 */             error("rmic.option.unsupported", new String[] { paramArrayOfString[b] });
/* 260 */             paramArrayOfString[b] = null;
/*     */           }
/* 262 */           else if (paramArrayOfString[b].equals("-keep") || paramArrayOfString[b]
/* 263 */             .equals("-keepgenerated")) {
/*     */             
/* 265 */             batch.keepGenerated = true;
/* 266 */             paramArrayOfString[b] = null;
/*     */           }
/* 268 */           else if (paramArrayOfString[b].equals("-g")) {
/* 269 */             batch.debug = true;
/* 270 */             paramArrayOfString[b] = null;
/*     */           }
/* 272 */           else if (paramArrayOfString[b].equals("-nowarn")) {
/* 273 */             batch.noWarn = true;
/* 274 */             paramArrayOfString[b] = null;
/*     */           }
/* 276 */           else if (paramArrayOfString[b].equals("-nowrite")) {
/* 277 */             batch.noWrite = true;
/* 278 */             paramArrayOfString[b] = null;
/*     */           }
/* 280 */           else if (paramArrayOfString[b].equals("-verbose")) {
/* 281 */             batch.verbose = true;
/* 282 */             paramArrayOfString[b] = null;
/*     */           }
/* 284 */           else if (paramArrayOfString[b].equals("-Xnocompile")) {
/* 285 */             batch.noCompile = true;
/* 286 */             batch.keepGenerated = true;
/* 287 */             paramArrayOfString[b] = null;
/*     */           }
/* 289 */           else if (paramArrayOfString[b].equals("-bootclasspath")) {
/* 290 */             if (b + 1 >= paramArrayOfString.length) {
/* 291 */               error("rmic.option.requires.argument", new String[] { paramArrayOfString[b] });
/* 292 */               usage();
/* 293 */               return null;
/*     */             } 
/* 295 */             if (batch.bootClassPath != null) {
/* 296 */               error("rmic.option.already.seen", new String[] { paramArrayOfString[b] });
/* 297 */               usage();
/* 298 */               return null;
/*     */             } 
/* 300 */             paramArrayOfString[b] = null;
/* 301 */             batch.bootClassPath = paramArrayOfString[++b];
/* 302 */             assert batch.bootClassPath != null;
/* 303 */             paramArrayOfString[b] = null;
/*     */           }
/* 305 */           else if (paramArrayOfString[b].equals("-extdirs")) {
/* 306 */             if (b + 1 >= paramArrayOfString.length) {
/* 307 */               error("rmic.option.requires.argument", new String[] { paramArrayOfString[b] });
/* 308 */               usage();
/* 309 */               return null;
/*     */             } 
/* 311 */             if (batch.extDirs != null) {
/* 312 */               error("rmic.option.already.seen", new String[] { paramArrayOfString[b] });
/* 313 */               usage();
/* 314 */               return null;
/*     */             } 
/* 316 */             paramArrayOfString[b] = null;
/* 317 */             batch.extDirs = paramArrayOfString[++b];
/* 318 */             assert batch.extDirs != null;
/* 319 */             paramArrayOfString[b] = null;
/*     */           }
/* 321 */           else if (paramArrayOfString[b].equals("-classpath")) {
/* 322 */             if (b + 1 >= paramArrayOfString.length) {
/* 323 */               error("rmic.option.requires.argument", new String[] { paramArrayOfString[b] });
/* 324 */               usage();
/* 325 */               return null;
/*     */             } 
/* 327 */             if (batch.classPath != null) {
/* 328 */               error("rmic.option.already.seen", new String[] { paramArrayOfString[b] });
/* 329 */               usage();
/* 330 */               return null;
/*     */             } 
/* 332 */             paramArrayOfString[b] = null;
/* 333 */             batch.classPath = paramArrayOfString[++b];
/* 334 */             assert batch.classPath != null;
/* 335 */             paramArrayOfString[b] = null;
/*     */           }
/* 337 */           else if (paramArrayOfString[b].equals("-d")) {
/* 338 */             if (b + 1 >= paramArrayOfString.length) {
/* 339 */               error("rmic.option.requires.argument", new String[] { paramArrayOfString[b] });
/* 340 */               usage();
/* 341 */               return null;
/*     */             } 
/* 343 */             if (batch.destDir != null) {
/* 344 */               error("rmic.option.already.seen", new String[] { paramArrayOfString[b] });
/* 345 */               usage();
/* 346 */               return null;
/*     */             } 
/* 348 */             paramArrayOfString[b] = null;
/* 349 */             batch.destDir = new File(paramArrayOfString[++b]);
/* 350 */             assert batch.destDir != null;
/* 351 */             paramArrayOfString[b] = null;
/* 352 */             if (!batch.destDir.exists()) {
/* 353 */               error("rmic.no.such.directory", new String[] { batch.destDir.getPath() });
/* 354 */               usage();
/* 355 */               return null;
/*     */             }
/*     */           
/* 358 */           } else if (paramArrayOfString[b].equals("-v1.1") || paramArrayOfString[b]
/* 359 */             .equals("-vcompat") || paramArrayOfString[b]
/* 360 */             .equals("-v1.2")) {
/*     */             
/* 362 */             JrmpGenerator jrmpGenerator = new JrmpGenerator();
/* 363 */             batch.generators.add(jrmpGenerator);
/*     */             
/* 365 */             if (!jrmpGenerator.parseArgs(paramArrayOfString, this)) {
/* 366 */               return null;
/*     */             }
/*     */           } else {
/* 369 */             if (paramArrayOfString[b].equalsIgnoreCase("-iiop")) {
/* 370 */               error("rmic.option.unimplemented", new String[] { paramArrayOfString[b] });
/* 371 */               return null;
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 385 */             if (paramArrayOfString[b].equalsIgnoreCase("-idl")) {
/* 386 */               error("rmic.option.unimplemented", new String[] { paramArrayOfString[b] });
/* 387 */               return null;
/*     */             } 
/*     */ 
/*     */             
/* 391 */             if (paramArrayOfString[b].equalsIgnoreCase("-xprint")) {
/* 392 */               error("rmic.option.unimplemented", new String[] { paramArrayOfString[b] });
/* 393 */               return null;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 403 */     for (b = 0; b < paramArrayOfString.length; b++) {
/* 404 */       if (paramArrayOfString[b] != null) {
/* 405 */         if (paramArrayOfString[b].startsWith("-")) {
/* 406 */           error("rmic.no.such.option", new String[] { paramArrayOfString[b] });
/* 407 */           usage();
/* 408 */           return null;
/*     */         } 
/* 410 */         batch.classes.add(paramArrayOfString[b]);
/*     */       } 
/*     */     } 
/*     */     
/* 414 */     if (batch.classes.isEmpty()) {
/* 415 */       usage();
/* 416 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 423 */     if (batch.generators.isEmpty()) {
/* 424 */       batch.generators.add(new JrmpGenerator());
/*     */     }
/* 426 */     return batch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean start(RootDoc paramRootDoc) {
/*     */     BatchEnvironment batchEnvironment;
/* 438 */     long l = -1L;
/* 439 */     for (String[] arrayOfString : paramRootDoc.options()) {
/* 440 */       if (arrayOfString[0].equals("-batchID")) {
/*     */         try {
/* 442 */           l = Long.parseLong(arrayOfString[1]);
/* 443 */         } catch (NumberFormatException numberFormatException) {
/* 444 */           throw new AssertionError(numberFormatException);
/*     */         } 
/*     */       }
/*     */     } 
/* 448 */     Batch batch = batchTable.get(Long.valueOf(l));
/* 449 */     assert batch != null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 458 */       Constructor<? extends BatchEnvironment> constructor = batch.envClass.getConstructor(new Class[] { RootDoc.class });
/* 459 */       batchEnvironment = constructor.newInstance(new Object[] { paramRootDoc });
/* 460 */     } catch (NoSuchMethodException noSuchMethodException) {
/* 461 */       throw new AssertionError(noSuchMethodException);
/* 462 */     } catch (IllegalAccessException illegalAccessException) {
/* 463 */       throw new AssertionError(illegalAccessException);
/* 464 */     } catch (InstantiationException instantiationException) {
/* 465 */       throw new AssertionError(instantiationException);
/* 466 */     } catch (InvocationTargetException invocationTargetException) {
/* 467 */       throw new AssertionError(invocationTargetException);
/*     */     } 
/*     */     
/* 470 */     batchEnvironment.setVerbose(batch.verbose);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 478 */     File file = batch.destDir;
/* 479 */     if (file == null) {
/* 480 */       file = new File(System.getProperty("user.dir"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 486 */     for (String str : batch.classes) {
/* 487 */       ClassDoc classDoc = paramRootDoc.classNamed(str);
/*     */       try {
/* 489 */         for (Generator generator : batch.generators) {
/* 490 */           generator.generate(batchEnvironment, classDoc, file);
/*     */         }
/* 492 */       } catch (NullPointerException nullPointerException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 509 */     boolean bool = true;
/* 510 */     List<File> list = batchEnvironment.generatedFiles();
/* 511 */     if (!batch.noCompile && !batch.noWrite && !list.isEmpty()) {
/* 512 */       bool = batch.enclosingMain().invokeJavac(batch, list);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 518 */     if (!batch.keepGenerated) {
/* 519 */       for (File file1 : list) {
/* 520 */         file1.delete();
/*     */       }
/*     */     }
/*     */     
/* 524 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int optionLength(String paramString) {
/* 534 */     if (paramString.equals("-batchID")) {
/* 535 */       return 2;
/*     */     }
/* 537 */     return 0;
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
/*     */   private boolean invokeJavadoc(Batch paramBatch, long paramLong) {
/* 551 */     ArrayList<String> arrayList = new ArrayList();
/*     */ 
/*     */     
/* 554 */     arrayList.add("-private");
/*     */ 
/*     */     
/* 557 */     arrayList.add("-Xclasses");
/*     */ 
/*     */     
/* 560 */     if (paramBatch.verbose) {
/* 561 */       arrayList.add("-verbose");
/*     */     }
/* 563 */     if (paramBatch.bootClassPath != null) {
/* 564 */       arrayList.add("-bootclasspath");
/* 565 */       arrayList.add(paramBatch.bootClassPath);
/*     */     } 
/* 567 */     if (paramBatch.extDirs != null) {
/* 568 */       arrayList.add("-extdirs");
/* 569 */       arrayList.add(paramBatch.extDirs);
/*     */     } 
/* 571 */     if (paramBatch.classPath != null) {
/* 572 */       arrayList.add("-classpath");
/* 573 */       arrayList.add(paramBatch.classPath);
/*     */     } 
/*     */ 
/*     */     
/* 577 */     arrayList.add("-batchID");
/* 578 */     arrayList.add(Long.toString(paramLong));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 585 */     HashSet<String> hashSet = new HashSet();
/* 586 */     for (Generator generator : paramBatch.generators) {
/* 587 */       hashSet.addAll(generator.bootstrapClassNames());
/*     */     }
/* 589 */     hashSet.addAll(paramBatch.classes);
/* 590 */     for (String str : hashSet) {
/* 591 */       arrayList.add(str);
/*     */     }
/*     */ 
/*     */     
/* 595 */     int i = com.sun.tools.javadoc.Main.execute(this.program, new PrintWriter(this.out, true), new PrintWriter(this.out, true), new PrintWriter(this.out, true), 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 600 */         getClass().getName(), arrayList
/* 601 */         .<String>toArray(new String[arrayList.size()]));
/* 602 */     return (i == 0);
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
/*     */   private boolean invokeJavac(Batch paramBatch, List<File> paramList) {
/* 615 */     ArrayList<String> arrayList = new ArrayList();
/*     */ 
/*     */     
/* 618 */     arrayList.add("-nowarn");
/*     */ 
/*     */     
/* 621 */     if (paramBatch.debug) {
/* 622 */       arrayList.add("-g");
/*     */     }
/* 624 */     if (paramBatch.verbose) {
/* 625 */       arrayList.add("-verbose");
/*     */     }
/* 627 */     if (paramBatch.bootClassPath != null) {
/* 628 */       arrayList.add("-bootclasspath");
/* 629 */       arrayList.add(paramBatch.bootClassPath);
/*     */     } 
/* 631 */     if (paramBatch.extDirs != null) {
/* 632 */       arrayList.add("-extdirs");
/* 633 */       arrayList.add(paramBatch.extDirs);
/*     */     } 
/* 635 */     if (paramBatch.classPath != null) {
/* 636 */       arrayList.add("-classpath");
/* 637 */       arrayList.add(paramBatch.classPath);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 644 */     arrayList.add("-source");
/* 645 */     arrayList.add("1.3");
/* 646 */     arrayList.add("-target");
/* 647 */     arrayList.add("1.1");
/*     */ 
/*     */     
/* 650 */     for (File file : paramList) {
/* 651 */       arrayList.add(file.getPath());
/*     */     }
/*     */ 
/*     */     
/* 655 */     int i = com.sun.tools.javac.Main.compile(arrayList
/* 656 */         .<String>toArray(new String[arrayList.size()]), new PrintWriter(this.out, true));
/*     */     
/* 658 */     return (i == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private class Batch
/*     */   {
/*     */     boolean keepGenerated = false;
/*     */     
/*     */     boolean debug = false;
/*     */     
/*     */     boolean noWarn = false;
/*     */     boolean noWrite = false;
/*     */     boolean verbose = false;
/*     */     boolean noCompile = false;
/* 672 */     String bootClassPath = null;
/* 673 */     String extDirs = null;
/* 674 */     String classPath = null;
/* 675 */     File destDir = null;
/* 676 */     List<Generator> generators = new ArrayList<>();
/* 677 */     Class<? extends BatchEnvironment> envClass = BatchEnvironment.class;
/* 678 */     List<String> classes = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Main enclosingMain() {
/* 686 */       return Main.this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\newrmic\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */