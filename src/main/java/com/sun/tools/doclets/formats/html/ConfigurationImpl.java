/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.WriterFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.FatalError;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclint.DocLint;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import com.sun.tools.javadoc.JavaScriptScanner;
/*     */ import com.sun.tools.javadoc.RootDocImpl;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import javax.tools.JavaFileManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigurationImpl
/*     */   extends Configuration
/*     */ {
/*  73 */   public static final String BUILD_DATE = System.getProperty("java.version");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public String header = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public String packagesheader = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public String footer = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public String doctitle = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public String windowtitle = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public String top = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public String bottom = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public String helpfile = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public String stylesheetfile = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public String docrootparent = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nohelp = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean splitindex = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean createindex = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean classuse = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean createtree = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nodeprecatedlist = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nonavbar = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean nooverview = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean overview = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean createoverview = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public Set<String> doclintOpts = new LinkedHashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean allowScriptInComments;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MessageRetriever standardmessage;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 198 */   public DocPath topFile = DocPath.empty;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public ClassDoc currentcd = null;
/*     */ 
/*     */   
/*     */   private final String versionRBName = "com.sun.tools.javadoc.resources.version";
/*     */   
/*     */   private ResourceBundle versionRB;
/*     */   
/*     */   private JavaFileManager fileManager;
/*     */ 
/*     */   
/*     */   public ConfigurationImpl() {
/* 214 */     this.versionRBName = "com.sun.tools.javadoc.resources.version";
/*     */     this.standardmessage = new MessageRetriever(this, "com.sun.tools.doclets.formats.html.resources.standard");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDocletSpecificBuildDate() {
/* 222 */     if (this.versionRB == null) {
/*     */       try {
/* 224 */         this.versionRB = ResourceBundle.getBundle("com.sun.tools.javadoc.resources.version");
/* 225 */       } catch (MissingResourceException missingResourceException) {
/* 226 */         return BUILD_DATE;
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/* 231 */       return this.versionRB.getString("release");
/* 232 */     } catch (MissingResourceException missingResourceException) {
/* 233 */       return BUILD_DATE;
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
/*     */   public void setSpecificDocletOptions(String[][] paramArrayOfString) {
/* 245 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 246 */       String[] arrayOfString = paramArrayOfString[b];
/* 247 */       String str = StringUtils.toLowerCase(arrayOfString[0]);
/* 248 */       if (str.equals("-footer")) {
/* 249 */         this.footer = arrayOfString[1];
/* 250 */       } else if (str.equals("-header")) {
/* 251 */         this.header = arrayOfString[1];
/* 252 */       } else if (str.equals("-packagesheader")) {
/* 253 */         this.packagesheader = arrayOfString[1];
/* 254 */       } else if (str.equals("-doctitle")) {
/* 255 */         this.doctitle = arrayOfString[1];
/* 256 */       } else if (str.equals("-windowtitle")) {
/* 257 */         this.windowtitle = arrayOfString[1].replaceAll("\\<.*?>", "");
/* 258 */       } else if (str.equals("-top")) {
/* 259 */         this.top = arrayOfString[1];
/* 260 */       } else if (str.equals("-bottom")) {
/* 261 */         this.bottom = arrayOfString[1];
/* 262 */       } else if (str.equals("-helpfile")) {
/* 263 */         this.helpfile = arrayOfString[1];
/* 264 */       } else if (str.equals("-stylesheetfile")) {
/* 265 */         this.stylesheetfile = arrayOfString[1];
/* 266 */       } else if (str.equals("-charset")) {
/* 267 */         this.charset = arrayOfString[1];
/* 268 */       } else if (str.equals("-xdocrootparent")) {
/* 269 */         this.docrootparent = arrayOfString[1];
/* 270 */       } else if (str.equals("-nohelp")) {
/* 271 */         this.nohelp = true;
/* 272 */       } else if (str.equals("-splitindex")) {
/* 273 */         this.splitindex = true;
/* 274 */       } else if (str.equals("-noindex")) {
/* 275 */         this.createindex = false;
/* 276 */       } else if (str.equals("-use")) {
/* 277 */         this.classuse = true;
/* 278 */       } else if (str.equals("-notree")) {
/* 279 */         this.createtree = false;
/* 280 */       } else if (str.equals("-nodeprecatedlist")) {
/* 281 */         this.nodeprecatedlist = true;
/* 282 */       } else if (str.equals("-nonavbar")) {
/* 283 */         this.nonavbar = true;
/* 284 */       } else if (str.equals("-nooverview")) {
/* 285 */         this.nooverview = true;
/* 286 */       } else if (str.equals("-overview")) {
/* 287 */         this.overview = true;
/* 288 */       } else if (str.equals("-xdoclint")) {
/* 289 */         this.doclintOpts.add(null);
/* 290 */       } else if (str.startsWith("-xdoclint:")) {
/* 291 */         this.doclintOpts.add(str.substring(str.indexOf(":") + 1));
/* 292 */       } else if (str.equals("--allow-script-in-comments")) {
/* 293 */         this.allowScriptInComments = true;
/*     */       } 
/*     */     } 
/*     */     
/* 297 */     if ((this.root.specifiedClasses()).length > 0) {
/* 298 */       HashMap<Object, Object> hashMap = new HashMap<>();
/*     */       
/* 300 */       ClassDoc[] arrayOfClassDoc = this.root.classes();
/* 301 */       for (byte b1 = 0; b1 < arrayOfClassDoc.length; b1++) {
/* 302 */         PackageDoc packageDoc = arrayOfClassDoc[b1].containingPackage();
/* 303 */         if (!hashMap.containsKey(packageDoc.name())) {
/* 304 */           hashMap.put(packageDoc.name(), packageDoc);
/*     */         }
/*     */       } 
/*     */     } 
/* 308 */     setCreateOverview();
/* 309 */     setTopFile(this.root);
/*     */     
/* 311 */     if (this.root instanceof RootDocImpl) {
/* 312 */       ((RootDocImpl)this.root).initDocLint(this.doclintOpts, this.tagletManager.getCustomTagNames());
/* 313 */       JavaScriptScanner javaScriptScanner = ((RootDocImpl)this.root).initJavaScriptScanner(isAllowScriptInComments());
/* 314 */       if (javaScriptScanner != null) {
/*     */ 
/*     */ 
/*     */         
/* 318 */         checkJavaScript(javaScriptScanner, "-header", this.header);
/* 319 */         checkJavaScript(javaScriptScanner, "-footer", this.footer);
/* 320 */         checkJavaScript(javaScriptScanner, "-top", this.top);
/* 321 */         checkJavaScript(javaScriptScanner, "-bottom", this.bottom);
/* 322 */         checkJavaScript(javaScriptScanner, "-doctitle", this.doctitle);
/* 323 */         checkJavaScript(javaScriptScanner, "-packagesheader", this.packagesheader);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkJavaScript(JavaScriptScanner paramJavaScriptScanner, final String opt, String paramString2) {
/* 329 */     paramJavaScriptScanner.parse(paramString2, new JavaScriptScanner.Reporter() {
/*     */           public void report() {
/* 331 */             ConfigurationImpl.this.root.printError(ConfigurationImpl.this.getText("doclet.JavaScript_in_option", opt));
/* 332 */             throw new FatalError();
/*     */           }
/*     */         });
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
/*     */   public int optionLength(String paramString) {
/* 354 */     int i = -1;
/* 355 */     if ((i = super.optionLength(paramString)) > 0) {
/* 356 */       return i;
/*     */     }
/*     */     
/* 359 */     paramString = StringUtils.toLowerCase(paramString);
/* 360 */     if (paramString.equals("-nodeprecatedlist") || paramString
/* 361 */       .equals("-noindex") || paramString
/* 362 */       .equals("-notree") || paramString
/* 363 */       .equals("-nohelp") || paramString
/* 364 */       .equals("-splitindex") || paramString
/* 365 */       .equals("-serialwarn") || paramString
/* 366 */       .equals("-use") || paramString
/* 367 */       .equals("-nonavbar") || paramString
/* 368 */       .equals("-nooverview") || paramString
/* 369 */       .equals("-xdoclint") || paramString
/* 370 */       .startsWith("-xdoclint:") || paramString
/* 371 */       .equals("--allow-script-in-comments"))
/* 372 */       return 1; 
/* 373 */     if (paramString.equals("-help")) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 378 */       System.out.println(getText("doclet.usage"));
/* 379 */       return 1;
/* 380 */     }  if (paramString.equals("-x")) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 385 */       System.out.println(getText("doclet.X.usage"));
/* 386 */       return 1;
/* 387 */     }  if (paramString.equals("-footer") || paramString
/* 388 */       .equals("-header") || paramString
/* 389 */       .equals("-packagesheader") || paramString
/* 390 */       .equals("-doctitle") || paramString
/* 391 */       .equals("-windowtitle") || paramString
/* 392 */       .equals("-top") || paramString
/* 393 */       .equals("-bottom") || paramString
/* 394 */       .equals("-helpfile") || paramString
/* 395 */       .equals("-stylesheetfile") || paramString
/* 396 */       .equals("-charset") || paramString
/* 397 */       .equals("-overview") || paramString
/* 398 */       .equals("-xdocrootparent")) {
/* 399 */       return 2;
/*     */     }
/* 401 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validOptions(String[][] paramArrayOfString, DocErrorReporter paramDocErrorReporter) {
/* 411 */     boolean bool1 = false;
/* 412 */     boolean bool2 = false;
/* 413 */     boolean bool3 = false;
/* 414 */     boolean bool4 = false;
/* 415 */     boolean bool5 = false;
/* 416 */     boolean bool6 = false;
/*     */     
/* 418 */     if (!generalValidOptions(paramArrayOfString, paramDocErrorReporter)) {
/* 419 */       return false;
/*     */     }
/*     */     
/* 422 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 423 */       String[] arrayOfString = paramArrayOfString[b];
/* 424 */       String str = StringUtils.toLowerCase(arrayOfString[0]);
/* 425 */       if (str.equals("-helpfile")) {
/* 426 */         if (bool2 == true) {
/* 427 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-helpfile", "-nohelp"));
/*     */           
/* 429 */           return false;
/*     */         } 
/* 431 */         if (bool1 == true) {
/* 432 */           paramDocErrorReporter.printError(getText("doclet.Option_reuse", "-helpfile"));
/*     */           
/* 434 */           return false;
/*     */         } 
/* 436 */         DocFile docFile = DocFile.createFileForInput(this, arrayOfString[1]);
/* 437 */         if (!docFile.exists()) {
/* 438 */           paramDocErrorReporter.printError(getText("doclet.File_not_found", arrayOfString[1]));
/* 439 */           return false;
/*     */         } 
/* 441 */         bool1 = true;
/* 442 */       } else if (str.equals("-nohelp")) {
/* 443 */         if (bool1 == true) {
/* 444 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-nohelp", "-helpfile"));
/*     */           
/* 446 */           return false;
/*     */         } 
/* 448 */         bool2 = true;
/* 449 */       } else if (str.equals("-xdocrootparent")) {
/*     */         try {
/* 451 */           new URL(arrayOfString[1]);
/* 452 */         } catch (MalformedURLException malformedURLException) {
/* 453 */           paramDocErrorReporter.printError(getText("doclet.MalformedURL", arrayOfString[1]));
/* 454 */           return false;
/*     */         } 
/* 456 */       } else if (str.equals("-overview")) {
/* 457 */         if (bool4 == true) {
/* 458 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-overview", "-nooverview"));
/*     */           
/* 460 */           return false;
/*     */         } 
/* 462 */         if (bool3 == true) {
/* 463 */           paramDocErrorReporter.printError(getText("doclet.Option_reuse", "-overview"));
/*     */           
/* 465 */           return false;
/*     */         } 
/* 467 */         bool3 = true;
/* 468 */       } else if (str.equals("-nooverview")) {
/* 469 */         if (bool3 == true) {
/* 470 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-nooverview", "-overview"));
/*     */           
/* 472 */           return false;
/*     */         } 
/* 474 */         bool4 = true;
/* 475 */       } else if (str.equals("-splitindex")) {
/* 476 */         if (bool6 == true) {
/* 477 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-splitindex", "-noindex"));
/*     */           
/* 479 */           return false;
/*     */         } 
/* 481 */         bool5 = true;
/* 482 */       } else if (str.equals("-noindex")) {
/* 483 */         if (bool5 == true) {
/* 484 */           paramDocErrorReporter.printError(getText("doclet.Option_conflict", "-noindex", "-splitindex"));
/*     */           
/* 486 */           return false;
/*     */         } 
/* 488 */         bool6 = true;
/* 489 */       } else if (str.startsWith("-xdoclint:")) {
/* 490 */         if (str.contains("/")) {
/* 491 */           paramDocErrorReporter.printError(getText("doclet.Option_doclint_no_qualifiers"));
/* 492 */           return false;
/*     */         } 
/* 494 */         if (!DocLint.isValidOption(str
/* 495 */             .replace("-xdoclint:", "-Xmsgs:"))) {
/* 496 */           paramDocErrorReporter.printError(getText("doclet.Option_doclint_invalid_arg"));
/* 497 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/* 501 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageRetriever getDocletSpecificMsg() {
/* 509 */     return this.standardmessage;
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
/*     */   protected void setTopFile(RootDoc paramRootDoc) {
/* 523 */     if (!checkForDeprecation(paramRootDoc)) {
/*     */       return;
/*     */     }
/* 526 */     if (this.createoverview) {
/* 527 */       this.topFile = DocPaths.OVERVIEW_SUMMARY;
/*     */     }
/* 529 */     else if (this.packages.length == 1 && this.packages[0].name().equals("")) {
/* 530 */       if ((paramRootDoc.classes()).length > 0) {
/* 531 */         ClassDoc[] arrayOfClassDoc = paramRootDoc.classes();
/* 532 */         Arrays.sort((Object[])arrayOfClassDoc);
/* 533 */         ClassDoc classDoc = getValidClass(arrayOfClassDoc);
/* 534 */         this.topFile = DocPath.forClass(classDoc);
/*     */       } 
/*     */     } else {
/* 537 */       this.topFile = DocPath.forPackage(this.packages[0]).resolve(DocPaths.PACKAGE_SUMMARY);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassDoc getValidClass(ClassDoc[] paramArrayOfClassDoc) {
/* 543 */     if (!this.nodeprecated) {
/* 544 */       return paramArrayOfClassDoc[0];
/*     */     }
/* 546 */     for (byte b = 0; b < paramArrayOfClassDoc.length; b++) {
/* 547 */       if ((paramArrayOfClassDoc[b].tags("deprecated")).length == 0) {
/* 548 */         return paramArrayOfClassDoc[b];
/*     */       }
/*     */     } 
/* 551 */     return null;
/*     */   }
/*     */   
/*     */   protected boolean checkForDeprecation(RootDoc paramRootDoc) {
/* 555 */     ClassDoc[] arrayOfClassDoc = paramRootDoc.classes();
/* 556 */     for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 557 */       if (isGeneratedDoc(arrayOfClassDoc[b])) {
/* 558 */         return true;
/*     */       }
/*     */     } 
/* 561 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setCreateOverview() {
/* 569 */     if ((this.overview || this.packages.length > 1) && !this.nooverview) {
/* 570 */       this.createoverview = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriterFactory getWriterFactory() {
/* 579 */     return new WriterFactoryImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<ProgramElementDoc> getMemberComparator() {
/* 587 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 595 */     if (this.root instanceof RootDocImpl) {
/* 596 */       return ((RootDocImpl)this.root).getLocale();
/*     */     }
/* 598 */     return Locale.getDefault();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaFileManager getFileManager() {
/* 606 */     if (this.fileManager == null)
/* 607 */       if (this.root instanceof RootDocImpl) {
/* 608 */         this.fileManager = ((RootDocImpl)this.root).getFileManager();
/*     */       } else {
/* 610 */         this.fileManager = (JavaFileManager)new JavacFileManager(new Context(), false, null);
/*     */       }  
/* 612 */     return this.fileManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean showMessage(SourcePosition paramSourcePosition, String paramString) {
/* 619 */     if (this.root instanceof RootDocImpl) {
/* 620 */       return (paramSourcePosition == null || ((RootDocImpl)this.root).showTagMessages());
/*     */     }
/* 622 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Content newContent() {
/* 627 */     return (Content)new ContentBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAllowScriptInComments() {
/* 636 */     return this.allowScriptInComments;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\ConfigurationImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */