/*     */ package com.sun.tools.doclets.internal.toolkit;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.BuilderFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletManager;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassDocCatalog;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Extern;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Group;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MetaKeywords;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.sym.Profiles;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.tools.JavaFileManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Configuration
/*     */ {
/*     */   protected BuilderFactory builderFactory;
/*     */   public TagletManager tagletManager;
/*     */   public String builderXMLPath;
/*     */   private static final String DEFAULT_BUILDER_XML = "resources/doclet.xml";
/*     */   
/*     */   public static class Fault
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     Fault(String param1String) {
/*  66 */       super(param1String);
/*     */     }
/*     */     
/*     */     Fault(String param1String, Exception param1Exception) {
/*  70 */       super(param1String, param1Exception);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public String tagletpath = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean serialwarn = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int sourcetab;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String tabSpaces;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean linksource = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nosince = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean copydocfilesubdirs = false;
/*     */ 
/*     */ 
/*     */   
/* 131 */   public String charset = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keywords = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public final MetaKeywords metakeywords = new MetaKeywords(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<String> excludedDocFileDirs;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<String> excludedQualifiers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RootDoc root;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public String destDirName = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public String docFileDestDirName = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public String docencoding = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nocomment = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   public String encoding = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean showauthor = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean javafx = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean showversion = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 214 */   public String sourcepath = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 219 */   public String profilespath = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean showProfiles = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nodeprecated = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDocCatalog classDocCatalog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   public MessageRetriever message = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean notimestamp = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public final Group group = new Group(this);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   public final Extern extern = new Extern(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Profiles profiles;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, PackageDoc[]> profilePackages;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageDoc[] packages;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getDocletSpecificBuildDate();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setSpecificDocletOptions(String[][] paramArrayOfString) throws Fault;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract MessageRetriever getDocletSpecificMsg();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration() {
/* 307 */     this.message = new MessageRetriever(this, "com.sun.tools.doclets.internal.toolkit.resources.doclets");
/*     */ 
/*     */     
/* 310 */     this.excludedDocFileDirs = new HashSet<>();
/* 311 */     this.excludedQualifiers = new HashSet<>();
/* 312 */     setTabWidth(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BuilderFactory getBuilderFactory() {
/* 321 */     if (this.builderFactory == null) {
/* 322 */       this.builderFactory = new BuilderFactory(this);
/*     */     }
/* 324 */     return this.builderFactory;
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
/*     */   public int optionLength(String paramString) {
/* 341 */     paramString = StringUtils.toLowerCase(paramString);
/* 342 */     if (paramString.equals("-author") || paramString
/* 343 */       .equals("-docfilessubdirs") || paramString
/* 344 */       .equals("-javafx") || paramString
/* 345 */       .equals("-keywords") || paramString
/* 346 */       .equals("-linksource") || paramString
/* 347 */       .equals("-nocomment") || paramString
/* 348 */       .equals("-nodeprecated") || paramString
/* 349 */       .equals("-nosince") || paramString
/* 350 */       .equals("-notimestamp") || paramString
/* 351 */       .equals("-quiet") || paramString
/* 352 */       .equals("-xnodate") || paramString
/* 353 */       .equals("-version"))
/* 354 */       return 1; 
/* 355 */     if (paramString.equals("-d") || paramString
/* 356 */       .equals("-docencoding") || paramString
/* 357 */       .equals("-encoding") || paramString
/* 358 */       .equals("-excludedocfilessubdir") || paramString
/* 359 */       .equals("-link") || paramString
/* 360 */       .equals("-sourcetab") || paramString
/* 361 */       .equals("-noqualifier") || paramString
/* 362 */       .equals("-output") || paramString
/* 363 */       .equals("-sourcepath") || paramString
/* 364 */       .equals("-tag") || paramString
/* 365 */       .equals("-taglet") || paramString
/* 366 */       .equals("-tagletpath") || paramString
/* 367 */       .equals("-xprofilespath"))
/* 368 */       return 2; 
/* 369 */     if (paramString.equals("-group") || paramString
/* 370 */       .equals("-linkoffline")) {
/* 371 */       return 3;
/*     */     }
/* 373 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean validOptions(String[][] paramArrayOfString, DocErrorReporter paramDocErrorReporter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initProfiles() throws IOException {
/* 387 */     if (this.profilespath.isEmpty()) {
/*     */       return;
/*     */     }
/* 390 */     this.profiles = Profiles.read(new File(this.profilespath));
/*     */ 
/*     */ 
/*     */     
/* 394 */     EnumMap<Profile, Object> enumMap = new EnumMap<>(Profile.class);
/*     */     
/* 396 */     for (Profile profile : Profile.values()) {
/* 397 */       enumMap.put(profile, new ArrayList());
/*     */     }
/* 399 */     for (PackageDoc packageDoc : this.packages) {
/* 400 */       if (!this.nodeprecated || !Util.isDeprecated((Doc)packageDoc)) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 405 */         int i = this.profiles.getProfile(packageDoc.name().replace(".", "/") + "/*");
/* 406 */         Profile profile = Profile.lookup(i);
/* 407 */         if (profile != null) {
/* 408 */           List<PackageDoc> list1 = (List)enumMap.get(profile);
/* 409 */           list1.add(packageDoc);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 414 */     this.profilePackages = (Map)new HashMap<>();
/* 415 */     List<?> list = Collections.emptyList();
/*     */     
/* 417 */     for (Map.Entry<Profile, Object> entry : enumMap.entrySet()) {
/* 418 */       Profile profile = (Profile)entry.getKey();
/* 419 */       List<?> list1 = (List)entry.getValue();
/* 420 */       list1.addAll(list);
/* 421 */       Collections.sort(list1);
/* 422 */       int i = list1.size();
/*     */ 
/*     */       
/* 425 */       if (i > 0)
/* 426 */         this.profilePackages.put(profile.name, list1.toArray(new PackageDoc[list1.size()])); 
/* 427 */       list = list1;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 432 */     this.showProfiles = !list.isEmpty();
/*     */   }
/*     */   
/*     */   private void initPackageArray() {
/* 436 */     HashSet<PackageDoc> hashSet = new HashSet(Arrays.asList((Object[])this.root.specifiedPackages()));
/* 437 */     ClassDoc[] arrayOfClassDoc = this.root.specifiedClasses();
/* 438 */     for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 439 */       hashSet.add(arrayOfClassDoc[b].containingPackage());
/*     */     }
/* 441 */     ArrayList<PackageDoc> arrayList = new ArrayList<>(hashSet);
/* 442 */     Collections.sort(arrayList);
/* 443 */     this.packages = arrayList.<PackageDoc>toArray(new PackageDoc[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptions(String[][] paramArrayOfString) throws Fault {
/* 452 */     LinkedHashSet<String[]> linkedHashSet = new LinkedHashSet();
/*     */     
/*     */     byte b;
/*     */     
/* 456 */     for (b = 0; b < paramArrayOfString.length; b++) {
/* 457 */       String[] arrayOfString = paramArrayOfString[b];
/* 458 */       String str = StringUtils.toLowerCase(arrayOfString[0]);
/* 459 */       if (str.equals("-d")) {
/* 460 */         this.destDirName = addTrailingFileSep(arrayOfString[1]);
/* 461 */         this.docFileDestDirName = this.destDirName;
/* 462 */         ensureOutputDirExists();
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 467 */     for (b = 0; b < paramArrayOfString.length; b++) {
/* 468 */       String[] arrayOfString = paramArrayOfString[b];
/* 469 */       String str = StringUtils.toLowerCase(arrayOfString[0]);
/* 470 */       if (str.equals("-docfilessubdirs")) {
/* 471 */         this.copydocfilesubdirs = true;
/* 472 */       } else if (str.equals("-docencoding")) {
/* 473 */         this.docencoding = arrayOfString[1];
/* 474 */       } else if (str.equals("-encoding")) {
/* 475 */         this.encoding = arrayOfString[1];
/* 476 */       } else if (str.equals("-author")) {
/* 477 */         this.showauthor = true;
/* 478 */       } else if (str.equals("-javafx")) {
/* 479 */         this.javafx = true;
/* 480 */       } else if (str.equals("-nosince")) {
/* 481 */         this.nosince = true;
/* 482 */       } else if (str.equals("-version")) {
/* 483 */         this.showversion = true;
/* 484 */       } else if (str.equals("-nodeprecated")) {
/* 485 */         this.nodeprecated = true;
/* 486 */       } else if (str.equals("-sourcepath")) {
/* 487 */         this.sourcepath = arrayOfString[1];
/* 488 */       } else if ((str.equals("-classpath") || str.equals("-cp")) && this.sourcepath
/* 489 */         .length() == 0) {
/* 490 */         this.sourcepath = arrayOfString[1];
/* 491 */       } else if (str.equals("-excludedocfilessubdir")) {
/* 492 */         addToSet(this.excludedDocFileDirs, arrayOfString[1]);
/* 493 */       } else if (str.equals("-noqualifier")) {
/* 494 */         addToSet(this.excludedQualifiers, arrayOfString[1]);
/* 495 */       } else if (str.equals("-linksource")) {
/* 496 */         this.linksource = true;
/* 497 */       } else if (str.equals("-sourcetab")) {
/* 498 */         this.linksource = true;
/*     */         try {
/* 500 */           setTabWidth(Integer.parseInt(arrayOfString[1]));
/* 501 */         } catch (NumberFormatException numberFormatException) {
/*     */ 
/*     */           
/* 504 */           this.sourcetab = -1;
/*     */         } 
/* 506 */         if (this.sourcetab <= 0) {
/* 507 */           this.message.warning("doclet.sourcetab_warning", new Object[0]);
/* 508 */           setTabWidth(8);
/*     */         } 
/* 510 */       } else if (str.equals("-notimestamp")) {
/* 511 */         this.notimestamp = true;
/* 512 */       } else if (str.equals("-nocomment")) {
/* 513 */         this.nocomment = true;
/* 514 */       } else if (str.equals("-tag") || str.equals("-taglet")) {
/* 515 */         linkedHashSet.add(arrayOfString);
/* 516 */       } else if (str.equals("-tagletpath")) {
/* 517 */         this.tagletpath = arrayOfString[1];
/* 518 */       } else if (str.equals("-xprofilespath")) {
/* 519 */         this.profilespath = arrayOfString[1];
/* 520 */       } else if (str.equals("-keywords")) {
/* 521 */         this.keywords = true;
/* 522 */       } else if (str.equals("-serialwarn")) {
/* 523 */         this.serialwarn = true;
/* 524 */       } else if (str.equals("-group")) {
/* 525 */         this.group.checkPackageGroups(arrayOfString[1], arrayOfString[2]);
/* 526 */       } else if (str.equals("-link")) {
/* 527 */         String str1 = arrayOfString[1];
/* 528 */         this.extern.link(str1, str1, (DocErrorReporter)this.root, false);
/* 529 */       } else if (str.equals("-linkoffline")) {
/* 530 */         String str1 = arrayOfString[1];
/* 531 */         String str2 = arrayOfString[2];
/* 532 */         this.extern.link(str1, str2, (DocErrorReporter)this.root, true);
/*     */       } 
/*     */     } 
/* 535 */     if (this.sourcepath.length() == 0) {
/* 536 */       this
/* 537 */         .sourcepath = (System.getProperty("env.class.path") == null) ? "" : System.getProperty("env.class.path");
/*     */     }
/* 539 */     if (this.docencoding == null) {
/* 540 */       this.docencoding = this.encoding;
/*     */     }
/*     */     
/* 543 */     this.classDocCatalog = new ClassDocCatalog(this.root.specifiedClasses(), this);
/* 544 */     initTagletManager((Set<String[]>)linkedHashSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptions() throws Fault {
/* 553 */     initPackageArray();
/* 554 */     setOptions(this.root.options());
/*     */     try {
/* 556 */       initProfiles();
/* 557 */     } catch (Exception exception) {
/* 558 */       throw new DocletAbortException(exception);
/*     */     } 
/* 560 */     setSpecificDocletOptions(this.root.options());
/*     */   }
/*     */   
/*     */   private void ensureOutputDirExists() throws Fault {
/* 564 */     DocFile docFile = DocFile.createFileForDirectory(this, this.destDirName);
/* 565 */     if (!docFile.exists())
/*     */     
/* 567 */     { this.root.printNotice(getText("doclet.dest_dir_create", this.destDirName));
/* 568 */       docFile.mkdirs(); }
/* 569 */     else { if (!docFile.isDirectory())
/* 570 */         throw new Fault(getText("doclet.destination_directory_not_directory_0", docFile
/*     */               
/* 572 */               .getPath())); 
/* 573 */       if (!docFile.canWrite()) {
/* 574 */         throw new Fault(getText("doclet.destination_directory_not_writable_0", docFile
/*     */               
/* 576 */               .getPath()));
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initTagletManager(Set<String[]> paramSet) {
/* 588 */     this.tagletManager = (this.tagletManager == null) ? new TagletManager(this.nosince, this.showversion, this.showauthor, this.javafx, this.message) : this.tagletManager;
/*     */ 
/*     */ 
/*     */     
/* 592 */     for (String[] arrayOfString1 : paramSet) {
/*     */       
/* 594 */       if (arrayOfString1[0].equals("-taglet")) {
/* 595 */         this.tagletManager.addCustomTag(arrayOfString1[1], getFileManager(), this.tagletpath);
/*     */         continue;
/*     */       } 
/* 598 */       String[] arrayOfString2 = tokenize(arrayOfString1[1], ':', 3);
/*     */       
/* 600 */       if (arrayOfString2.length == 1) {
/* 601 */         String str = arrayOfString1[1];
/* 602 */         if (this.tagletManager.isKnownCustomTag(str)) {
/*     */           
/* 604 */           this.tagletManager.addNewSimpleCustomTag(str, null, "");
/*     */           continue;
/*     */         } 
/* 607 */         StringBuilder stringBuilder = new StringBuilder(str + ":");
/* 608 */         stringBuilder.setCharAt(0, Character.toUpperCase(str.charAt(0)));
/* 609 */         this.tagletManager.addNewSimpleCustomTag(str, stringBuilder.toString(), "a"); continue;
/*     */       } 
/* 611 */       if (arrayOfString2.length == 2) {
/*     */         
/* 613 */         this.tagletManager.addNewSimpleCustomTag(arrayOfString2[0], arrayOfString2[1], ""); continue;
/* 614 */       }  if (arrayOfString2.length >= 3) {
/* 615 */         this.tagletManager.addNewSimpleCustomTag(arrayOfString2[0], arrayOfString2[2], arrayOfString2[1]); continue;
/*     */       } 
/* 617 */       this.message.error("doclet.Error_invalid_custom_tag_argument", new Object[] { arrayOfString1[1] });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] tokenize(String paramString, char paramChar, int paramInt) {
/* 636 */     ArrayList<String> arrayList = new ArrayList();
/* 637 */     StringBuilder stringBuilder = new StringBuilder();
/* 638 */     boolean bool = false; int i;
/* 639 */     for (i = 0; i < paramString.length(); i += Character.charCount(i)) {
/* 640 */       int j = paramString.codePointAt(i);
/* 641 */       if (bool) {
/*     */         
/* 643 */         stringBuilder.appendCodePoint(j);
/* 644 */         bool = false;
/* 645 */       } else if (j == paramChar && arrayList.size() < paramInt - 1) {
/*     */         
/* 647 */         arrayList.add(stringBuilder.toString());
/* 648 */         stringBuilder = new StringBuilder();
/* 649 */       } else if (j == 92) {
/*     */         
/* 651 */         bool = true;
/*     */       } else {
/*     */         
/* 654 */         stringBuilder.appendCodePoint(j);
/*     */       } 
/*     */     } 
/* 657 */     if (stringBuilder.length() > 0) {
/* 658 */       arrayList.add(stringBuilder.toString());
/*     */     }
/* 660 */     return arrayList.<String>toArray(new String[0]);
/*     */   }
/*     */   
/*     */   private void addToSet(Set<String> paramSet, String paramString) {
/* 664 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, ":");
/*     */     
/* 666 */     while (stringTokenizer.hasMoreTokens()) {
/* 667 */       String str = stringTokenizer.nextToken();
/* 668 */       paramSet.add(str);
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
/*     */   
/*     */   public static String addTrailingFileSep(String paramString) {
/* 681 */     String str1 = System.getProperty("file.separator");
/* 682 */     String str2 = str1 + str1;
/*     */     int i;
/* 684 */     while ((i = paramString.indexOf(str2, 1)) >= 0)
/*     */     {
/* 686 */       paramString = paramString.substring(0, i) + paramString.substring(i + str1.length());
/*     */     }
/* 688 */     if (!paramString.endsWith(str1))
/* 689 */       paramString = paramString + str1; 
/* 690 */     return paramString;
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
/*     */   public boolean generalValidOptions(String[][] paramArrayOfString, DocErrorReporter paramDocErrorReporter) {
/* 708 */     boolean bool = false;
/* 709 */     String str = "";
/* 710 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 711 */       String[] arrayOfString = paramArrayOfString[b];
/* 712 */       String str1 = StringUtils.toLowerCase(arrayOfString[0]);
/* 713 */       if (str1.equals("-docencoding")) {
/* 714 */         bool = true;
/* 715 */         if (!checkOutputFileEncoding(arrayOfString[1], paramDocErrorReporter)) {
/* 716 */           return false;
/*     */         }
/* 718 */       } else if (str1.equals("-encoding")) {
/* 719 */         str = arrayOfString[1];
/*     */       } 
/*     */     } 
/* 722 */     if (!bool && str.length() > 0 && 
/* 723 */       !checkOutputFileEncoding(str, paramDocErrorReporter)) {
/* 724 */       return false;
/*     */     }
/*     */     
/* 727 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldDocumentProfile(String paramString) {
/* 738 */     return this.profilePackages.containsKey(paramString);
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
/*     */   private boolean checkOutputFileEncoding(String paramString, DocErrorReporter paramDocErrorReporter) {
/* 750 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 751 */     OutputStreamWriter outputStreamWriter = null;
/*     */     try {
/* 753 */       outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, paramString);
/* 754 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/* 755 */       paramDocErrorReporter.printError(getText("doclet.Encoding_not_supported", paramString));
/*     */       
/* 757 */       return false;
/*     */     } finally {
/*     */       try {
/* 760 */         if (outputStreamWriter != null) {
/* 761 */           outputStreamWriter.close();
/*     */         }
/* 763 */       } catch (IOException iOException) {}
/*     */     } 
/*     */     
/* 766 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExcludeDocFileDir(String paramString) {
/* 775 */     if (this.excludedDocFileDirs.contains(paramString)) {
/* 776 */       return true;
/*     */     }
/* 778 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExcludeQualifier(String paramString) {
/* 787 */     if (this.excludedQualifiers.contains("all") || this.excludedQualifiers
/* 788 */       .contains(paramString) || this.excludedQualifiers
/* 789 */       .contains(paramString + ".*")) {
/* 790 */       return true;
/*     */     }
/* 792 */     int i = -1;
/* 793 */     while ((i = paramString.indexOf(".", i + 1)) != -1) {
/* 794 */       if (this.excludedQualifiers.contains(paramString.substring(0, i + 1) + "*")) {
/* 795 */         return true;
/*     */       }
/*     */     } 
/* 798 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName(ClassDoc paramClassDoc) {
/* 808 */     PackageDoc packageDoc = paramClassDoc.containingPackage();
/* 809 */     if (packageDoc != null && shouldExcludeQualifier(paramClassDoc.containingPackage().name())) {
/* 810 */       return paramClassDoc.name();
/*     */     }
/* 812 */     return paramClassDoc.qualifiedName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText(String paramString) {
/*     */     try {
/* 819 */       return getDocletSpecificMsg().getText(paramString, new Object[0]);
/* 820 */     } catch (Exception exception) {
/*     */       
/* 822 */       return this.message.getText(paramString, new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText(String paramString1, String paramString2) {
/*     */     try {
/* 829 */       return getDocletSpecificMsg().getText(paramString1, new Object[] { paramString2 });
/* 830 */     } catch (Exception exception) {
/*     */       
/* 832 */       return this.message.getText(paramString1, new Object[] { paramString2 });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText(String paramString1, String paramString2, String paramString3) {
/*     */     try {
/* 839 */       return getDocletSpecificMsg().getText(paramString1, new Object[] { paramString2, paramString3 });
/* 840 */     } catch (Exception exception) {
/*     */       
/* 842 */       return this.message.getText(paramString1, new Object[] { paramString2, paramString3 });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText(String paramString1, String paramString2, String paramString3, String paramString4) {
/*     */     try {
/* 849 */       return getDocletSpecificMsg().getText(paramString1, new Object[] { paramString2, paramString3, paramString4 });
/* 850 */     } catch (Exception exception) {
/*     */       
/* 852 */       return this.message.getText(paramString1, new Object[] { paramString2, paramString3, paramString4 });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Content newContent();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getResource(String paramString) {
/* 865 */     Content content = newContent();
/* 866 */     content.addContent(getText(paramString));
/* 867 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getResource(String paramString, Object paramObject) {
/* 878 */     return getResource(paramString, paramObject, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getResource(String paramString, Object paramObject1, Object paramObject2) {
/* 889 */     return getResource(paramString, paramObject1, paramObject2, null);
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
/*     */   public Content getResource(String paramString, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 901 */     Content content = newContent();
/* 902 */     Pattern pattern = Pattern.compile("\\{([012])\\}");
/* 903 */     String str = getText(paramString);
/* 904 */     Matcher matcher = pattern.matcher(str);
/* 905 */     int i = 0;
/* 906 */     while (matcher.find(i)) {
/* 907 */       content.addContent(str.substring(i, matcher.start()));
/*     */       
/* 909 */       Object object = null;
/* 910 */       switch (matcher.group(1).charAt(0)) { case '0':
/* 911 */           object = paramObject1; break;
/* 912 */         case '1': object = paramObject2; break;
/* 913 */         case '2': object = paramObject3;
/*     */           break; }
/*     */       
/* 916 */       if (object == null) {
/* 917 */         content.addContent("{" + matcher.group(1) + "}");
/* 918 */       } else if (object instanceof String) {
/* 919 */         content.addContent((String)object);
/* 920 */       } else if (object instanceof Content) {
/* 921 */         content.addContent((Content)object);
/*     */       } 
/*     */       
/* 924 */       i = matcher.end();
/*     */     } 
/*     */     
/* 927 */     content.addContent(str.substring(i));
/* 928 */     return content;
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
/*     */   public boolean isGeneratedDoc(ClassDoc paramClassDoc) {
/* 941 */     if (!this.nodeprecated) {
/* 942 */       return true;
/*     */     }
/* 944 */     return (!Util.isDeprecated((Doc)paramClassDoc) && !Util.isDeprecated((Doc)paramClassDoc.containingPackage()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract WriterFactory getWriterFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getBuilderXML() throws IOException {
/* 960 */     return (this.builderXMLPath == null) ? Configuration.class
/* 961 */       .getResourceAsStream("resources/doclet.xml") : 
/* 962 */       DocFile.createFileForInput(this, this.builderXMLPath).openInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Locale getLocale();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JavaFileManager getFileManager();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Comparator<ProgramElementDoc> getMemberComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setTabWidth(int paramInt) {
/* 984 */     this.sourcetab = paramInt;
/* 985 */     this.tabSpaces = String.format("%" + paramInt + "s", new Object[] { "" });
/*     */   }
/*     */   
/*     */   public abstract boolean showMessage(SourcePosition paramSourcePosition, String paramString);
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\Configuration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */