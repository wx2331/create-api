/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.Taglet;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.tools.DocumentationTool;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TagletManager
/*     */ {
/*     */   public static final char SIMPLE_TAGLET_OPT_SEPARATOR = ':';
/*     */   public static final String ALT_SIMPLE_TAGLET_OPT_SEPARATOR = "-";
/*     */   private LinkedHashMap<String, Taglet> customTags;
/*     */   private Taglet[] packageTags;
/*     */   private Taglet[] typeTags;
/*     */   private Taglet[] fieldTags;
/*     */   private Taglet[] constructorTags;
/*     */   private Taglet[] methodTags;
/*     */   private Taglet[] overviewTags;
/*     */   private Taglet[] inlineTags;
/*     */   private Taglet[] serializedFormTags;
/*     */   private MessageRetriever message;
/*     */   private Set<String> standardTags;
/*     */   private Set<String> standardTagsLowercase;
/*     */   private Set<String> overridenStandardTags;
/*     */   private Set<String> potentiallyConflictingTags;
/*     */   private Set<String> unseenCustomTags;
/*     */   private boolean nosince;
/*     */   private boolean showversion;
/*     */   private boolean showauthor;
/*     */   private boolean javafx;
/*     */   
/*     */   public TagletManager(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, MessageRetriever paramMessageRetriever) {
/* 176 */     this.overridenStandardTags = new HashSet<>();
/* 177 */     this.potentiallyConflictingTags = new HashSet<>();
/* 178 */     this.standardTags = new HashSet<>();
/* 179 */     this.standardTagsLowercase = new HashSet<>();
/* 180 */     this.unseenCustomTags = new HashSet<>();
/* 181 */     this.customTags = new LinkedHashMap<>();
/* 182 */     this.nosince = paramBoolean1;
/* 183 */     this.showversion = paramBoolean2;
/* 184 */     this.showauthor = paramBoolean3;
/* 185 */     this.javafx = paramBoolean4;
/* 186 */     this.message = paramMessageRetriever;
/* 187 */     initStandardTaglets();
/* 188 */     initStandardTagsLowercase();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCustomTag(Taglet paramTaglet) {
/* 199 */     if (paramTaglet != null) {
/* 200 */       String str = paramTaglet.getName();
/* 201 */       if (this.customTags.containsKey(str)) {
/* 202 */         this.customTags.remove(str);
/*     */       }
/* 204 */       this.customTags.put(str, paramTaglet);
/* 205 */       checkTagName(str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<String> getCustomTagNames() {
/* 210 */     return this.customTags.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCustomTag(String paramString1, JavaFileManager paramJavaFileManager, String paramString2) {
/*     */     try {
/*     */       ClassLoader classLoader;
/* 221 */       Class<?> clazz = null;
/*     */       
/* 223 */       String str = null;
/*     */ 
/*     */       
/* 226 */       if (paramJavaFileManager != null && paramJavaFileManager.hasLocation(DocumentationTool.Location.TAGLET_PATH)) {
/* 227 */         classLoader = paramJavaFileManager.getClassLoader(DocumentationTool.Location.TAGLET_PATH);
/*     */       } else {
/*     */         
/* 230 */         str = appendPath(System.getProperty("env.class.path"), str);
/* 231 */         str = appendPath(System.getProperty("java.class.path"), str);
/* 232 */         str = appendPath(paramString2, str);
/* 233 */         classLoader = new URLClassLoader(pathToURLs(str));
/*     */       } 
/*     */       
/* 236 */       clazz = classLoader.loadClass(paramString1);
/* 237 */       Method method = clazz.getMethod("register", new Class[] { Map.class });
/*     */       
/* 239 */       Object[] arrayOfObject = this.customTags.values().toArray();
/* 240 */       Taglet taglet = (arrayOfObject != null && arrayOfObject.length > 0) ? (Taglet)arrayOfObject[arrayOfObject.length - 1] : null;
/*     */       
/* 242 */       method.invoke(null, new Object[] { this.customTags });
/* 243 */       arrayOfObject = this.customTags.values().toArray();
/* 244 */       Object object = (arrayOfObject != null && arrayOfObject.length > 0) ? arrayOfObject[arrayOfObject.length - 1] : null;
/*     */       
/* 246 */       if (taglet != object) {
/*     */ 
/*     */ 
/*     */         
/* 250 */         this.message.notice("doclet.Notice_taglet_registered", new Object[] { paramString1 });
/* 251 */         if (object != null) {
/* 252 */           checkTaglet(object);
/*     */         }
/*     */       } 
/* 255 */     } catch (Exception exception) {
/* 256 */       this.message.error("doclet.Error_taglet_not_registered", new Object[] { exception.getClass().getName(), paramString1 });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String appendPath(String paramString1, String paramString2) {
/* 262 */     if (paramString1 == null || paramString1.length() == 0)
/* 263 */       return (paramString2 == null) ? "." : paramString2; 
/* 264 */     if (paramString2 == null || paramString2.length() == 0) {
/* 265 */       return paramString1;
/*     */     }
/* 267 */     return paramString1 + File.pathSeparator + paramString2;
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
/*     */   private URL[] pathToURLs(String paramString) {
/* 279 */     LinkedHashSet<URL> linkedHashSet = new LinkedHashSet();
/* 280 */     for (String str : paramString.split(File.pathSeparator)) {
/* 281 */       if (!str.isEmpty())
/*     */         try {
/* 283 */           linkedHashSet.add((new File(str)).getAbsoluteFile().toURI().toURL());
/* 284 */         } catch (MalformedURLException malformedURLException) {
/* 285 */           this.message.error("doclet.MalformedURL", new Object[] { str });
/*     */         }  
/*     */     } 
/* 288 */     return linkedHashSet.<URL>toArray(new URL[linkedHashSet.size()]);
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
/*     */   public void addNewSimpleCustomTag(String paramString1, String paramString2, String paramString3) {
/* 304 */     if (paramString1 == null || paramString3 == null) {
/*     */       return;
/*     */     }
/* 307 */     Taglet taglet = this.customTags.get(paramString1);
/* 308 */     paramString3 = StringUtils.toLowerCase(paramString3);
/* 309 */     if (taglet == null || paramString2 != null) {
/* 310 */       this.customTags.remove(paramString1);
/* 311 */       this.customTags.put(paramString1, new SimpleTaglet(paramString1, paramString2, paramString3));
/* 312 */       if (paramString3 != null && paramString3.indexOf('x') == -1) {
/* 313 */         checkTagName(paramString1);
/*     */       }
/*     */     } else {
/*     */       
/* 317 */       this.customTags.remove(paramString1);
/* 318 */       this.customTags.put(paramString1, taglet);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkTagName(String paramString) {
/* 326 */     if (this.standardTags.contains(paramString)) {
/* 327 */       this.overridenStandardTags.add(paramString);
/*     */     } else {
/* 329 */       if (paramString.indexOf('.') == -1) {
/* 330 */         this.potentiallyConflictingTags.add(paramString);
/*     */       }
/* 332 */       this.unseenCustomTags.add(paramString);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkTaglet(Object paramObject) {
/* 341 */     if (paramObject instanceof Taglet) {
/* 342 */       checkTagName(((Taglet)paramObject).getName());
/* 343 */     } else if (paramObject instanceof Taglet) {
/* 344 */       Taglet taglet = (Taglet)paramObject;
/* 345 */       this.customTags.remove(taglet.getName());
/* 346 */       this.customTags.put(taglet.getName(), new LegacyTaglet(taglet));
/* 347 */       checkTagName(taglet.getName());
/*     */     } else {
/* 349 */       throw new IllegalArgumentException("Given object is not a taglet.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void seenCustomTag(String paramString) {
/* 359 */     this.unseenCustomTags.remove(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkTags(Doc paramDoc, Tag[] paramArrayOfTag, boolean paramBoolean) {
/* 369 */     if (paramArrayOfTag == null) {
/*     */       return;
/*     */     }
/*     */     
/* 373 */     for (byte b = 0; b < paramArrayOfTag.length; b++) {
/* 374 */       String str = paramArrayOfTag[b].name();
/* 375 */       if (str.length() > 0 && str.charAt(0) == '@') {
/* 376 */         str = str.substring(1, str.length());
/*     */       }
/* 378 */       if (!this.standardTags.contains(str) && !this.customTags.containsKey(str)) {
/* 379 */         if (this.standardTagsLowercase.contains(StringUtils.toLowerCase(str))) {
/* 380 */           this.message.warning(paramArrayOfTag[b].position(), "doclet.UnknownTagLowercase", new Object[] { paramArrayOfTag[b].name() });
/*     */         } else {
/*     */           
/* 383 */           this.message.warning(paramArrayOfTag[b].position(), "doclet.UnknownTag", new Object[] { paramArrayOfTag[b].name() });
/*     */         } 
/*     */       } else {
/*     */         Taglet taglet;
/*     */         
/* 388 */         if ((taglet = this.customTags.get(str)) != null) {
/* 389 */           if (paramBoolean && !taglet.isInlineTag()) {
/* 390 */             printTagMisuseWarn(taglet, paramArrayOfTag[b], "inline");
/*     */           }
/* 392 */           if (paramDoc instanceof com.sun.javadoc.RootDoc && !taglet.inOverview()) {
/* 393 */             printTagMisuseWarn(taglet, paramArrayOfTag[b], "overview");
/* 394 */           } else if (paramDoc instanceof com.sun.javadoc.PackageDoc && !taglet.inPackage()) {
/* 395 */             printTagMisuseWarn(taglet, paramArrayOfTag[b], "package");
/* 396 */           } else if (paramDoc instanceof com.sun.javadoc.ClassDoc && !taglet.inType()) {
/* 397 */             printTagMisuseWarn(taglet, paramArrayOfTag[b], "class");
/* 398 */           } else if (paramDoc instanceof com.sun.javadoc.ConstructorDoc && !taglet.inConstructor()) {
/* 399 */             printTagMisuseWarn(taglet, paramArrayOfTag[b], "constructor");
/* 400 */           } else if (paramDoc instanceof com.sun.javadoc.FieldDoc && !taglet.inField()) {
/* 401 */             printTagMisuseWarn(taglet, paramArrayOfTag[b], "field");
/* 402 */           } else if (paramDoc instanceof com.sun.javadoc.MethodDoc && !taglet.inMethod()) {
/* 403 */             printTagMisuseWarn(taglet, paramArrayOfTag[b], "method");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printTagMisuseWarn(Taglet paramTaglet, Tag paramTag, String paramString) {
/* 417 */     LinkedHashSet<String> linkedHashSet = new LinkedHashSet();
/* 418 */     if (paramTaglet.inOverview()) {
/* 419 */       linkedHashSet.add("overview");
/*     */     }
/* 421 */     if (paramTaglet.inPackage()) {
/* 422 */       linkedHashSet.add("package");
/*     */     }
/* 424 */     if (paramTaglet.inType()) {
/* 425 */       linkedHashSet.add("class/interface");
/*     */     }
/* 427 */     if (paramTaglet.inConstructor()) {
/* 428 */       linkedHashSet.add("constructor");
/*     */     }
/* 430 */     if (paramTaglet.inField()) {
/* 431 */       linkedHashSet.add("field");
/*     */     }
/* 433 */     if (paramTaglet.inMethod()) {
/* 434 */       linkedHashSet.add("method");
/*     */     }
/* 436 */     if (paramTaglet.isInlineTag()) {
/* 437 */       linkedHashSet.add("inline text");
/*     */     }
/* 439 */     String[] arrayOfString = linkedHashSet.<String>toArray(new String[0]);
/* 440 */     if (arrayOfString == null || arrayOfString.length == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 444 */     StringBuilder stringBuilder = new StringBuilder();
/* 445 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 446 */       if (b > 0) {
/* 447 */         stringBuilder.append(", ");
/*     */       }
/* 449 */       stringBuilder.append(arrayOfString[b]);
/*     */     } 
/* 451 */     this.message.warning(paramTag.position(), "doclet.tag_misuse", new Object[] { "@" + paramTaglet
/* 452 */           .getName(), paramString, stringBuilder.toString() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Taglet[] getPackageCustomTaglets() {
/* 462 */     if (this.packageTags == null) {
/* 463 */       initCustomTagletArrays();
/*     */     }
/* 465 */     return this.packageTags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Taglet[] getTypeCustomTaglets() {
/* 475 */     if (this.typeTags == null) {
/* 476 */       initCustomTagletArrays();
/*     */     }
/* 478 */     return this.typeTags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Taglet[] getInlineCustomTaglets() {
/* 488 */     if (this.inlineTags == null) {
/* 489 */       initCustomTagletArrays();
/*     */     }
/* 491 */     return this.inlineTags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Taglet[] getFieldCustomTaglets() {
/* 501 */     if (this.fieldTags == null) {
/* 502 */       initCustomTagletArrays();
/*     */     }
/* 504 */     return this.fieldTags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Taglet[] getSerializedFormTaglets() {
/* 514 */     if (this.serializedFormTags == null) {
/* 515 */       initCustomTagletArrays();
/*     */     }
/* 517 */     return this.serializedFormTags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Taglet[] getCustomTaglets(Doc paramDoc) {
/* 525 */     if (paramDoc instanceof com.sun.javadoc.ConstructorDoc)
/* 526 */       return getConstructorCustomTaglets(); 
/* 527 */     if (paramDoc instanceof com.sun.javadoc.MethodDoc)
/* 528 */       return getMethodCustomTaglets(); 
/* 529 */     if (paramDoc instanceof com.sun.javadoc.FieldDoc)
/* 530 */       return getFieldCustomTaglets(); 
/* 531 */     if (paramDoc instanceof com.sun.javadoc.ClassDoc)
/* 532 */       return getTypeCustomTaglets(); 
/* 533 */     if (paramDoc instanceof com.sun.javadoc.PackageDoc)
/* 534 */       return getPackageCustomTaglets(); 
/* 535 */     if (paramDoc instanceof com.sun.javadoc.RootDoc) {
/* 536 */       return getOverviewCustomTaglets();
/*     */     }
/* 538 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Taglet[] getConstructorCustomTaglets() {
/* 548 */     if (this.constructorTags == null) {
/* 549 */       initCustomTagletArrays();
/*     */     }
/* 551 */     return this.constructorTags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Taglet[] getMethodCustomTaglets() {
/* 561 */     if (this.methodTags == null) {
/* 562 */       initCustomTagletArrays();
/*     */     }
/* 564 */     return this.methodTags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Taglet[] getOverviewCustomTaglets() {
/* 574 */     if (this.overviewTags == null) {
/* 575 */       initCustomTagletArrays();
/*     */     }
/* 577 */     return this.overviewTags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initCustomTagletArrays() {
/* 584 */     Iterator<Taglet> iterator = this.customTags.values().iterator();
/* 585 */     ArrayList<Taglet> arrayList1 = new ArrayList(this.customTags.size());
/* 586 */     ArrayList<Taglet> arrayList2 = new ArrayList(this.customTags.size());
/* 587 */     ArrayList<Taglet> arrayList3 = new ArrayList(this.customTags.size());
/* 588 */     ArrayList<Taglet> arrayList4 = new ArrayList(this.customTags.size());
/* 589 */     ArrayList<Taglet> arrayList5 = new ArrayList(this.customTags.size());
/* 590 */     ArrayList<Taglet> arrayList6 = new ArrayList(this.customTags.size());
/* 591 */     ArrayList<Taglet> arrayList7 = new ArrayList(this.customTags.size());
/* 592 */     ArrayList arrayList = new ArrayList();
/*     */     
/* 594 */     while (iterator.hasNext()) {
/* 595 */       Taglet taglet = iterator.next();
/* 596 */       if (taglet.inPackage() && !taglet.isInlineTag()) {
/* 597 */         arrayList1.add(taglet);
/*     */       }
/* 599 */       if (taglet.inType() && !taglet.isInlineTag()) {
/* 600 */         arrayList2.add(taglet);
/*     */       }
/* 602 */       if (taglet.inField() && !taglet.isInlineTag()) {
/* 603 */         arrayList3.add(taglet);
/*     */       }
/* 605 */       if (taglet.inConstructor() && !taglet.isInlineTag()) {
/* 606 */         arrayList4.add(taglet);
/*     */       }
/* 608 */       if (taglet.inMethod() && !taglet.isInlineTag()) {
/* 609 */         arrayList5.add(taglet);
/*     */       }
/* 611 */       if (taglet.isInlineTag()) {
/* 612 */         arrayList6.add(taglet);
/*     */       }
/* 614 */       if (taglet.inOverview() && !taglet.isInlineTag()) {
/* 615 */         arrayList7.add(taglet);
/*     */       }
/*     */     } 
/* 618 */     this.packageTags = arrayList1.<Taglet>toArray(new Taglet[0]);
/* 619 */     this.typeTags = arrayList2.<Taglet>toArray(new Taglet[0]);
/* 620 */     this.fieldTags = arrayList3.<Taglet>toArray(new Taglet[0]);
/* 621 */     this.constructorTags = arrayList4.<Taglet>toArray(new Taglet[0]);
/* 622 */     this.methodTags = arrayList5.<Taglet>toArray(new Taglet[0]);
/* 623 */     this.overviewTags = arrayList7.<Taglet>toArray(new Taglet[0]);
/* 624 */     this.inlineTags = arrayList6.<Taglet>toArray(new Taglet[0]);
/*     */ 
/*     */     
/* 627 */     arrayList.add(this.customTags.get("serialData"));
/* 628 */     arrayList.add(this.customTags.get("throws"));
/* 629 */     if (!this.nosince)
/* 630 */       arrayList.add(this.customTags.get("since")); 
/* 631 */     arrayList.add(this.customTags.get("see"));
/* 632 */     this.serializedFormTags = (Taglet[])arrayList.toArray((Object[])new Taglet[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initStandardTaglets() {
/* 639 */     if (this.javafx) {
/* 640 */       initJavaFXTaglets();
/*     */     }
/*     */ 
/*     */     
/* 644 */     addStandardTaglet(new ParamTaglet());
/* 645 */     addStandardTaglet(new ReturnTaglet());
/* 646 */     addStandardTaglet(new ThrowsTaglet());
/* 647 */     addStandardTaglet(new SimpleTaglet("exception", null, "mc"));
/*     */     
/* 649 */     addStandardTaglet(!this.nosince, new SimpleTaglet("since", this.message.getText("doclet.Since", new Object[0]), "a"));
/*     */     
/* 651 */     addStandardTaglet(this.showversion, new SimpleTaglet("version", this.message.getText("doclet.Version", new Object[0]), "pto"));
/*     */     
/* 653 */     addStandardTaglet(this.showauthor, new SimpleTaglet("author", this.message.getText("doclet.Author", new Object[0]), "pto"));
/*     */     
/* 655 */     addStandardTaglet(new SimpleTaglet("serialData", this.message.getText("doclet.SerialData", new Object[0]), "x"));
/*     */     SimpleTaglet simpleTaglet;
/* 657 */     this.customTags.put((simpleTaglet = new SimpleTaglet("factory", this.message.getText("doclet.Factory", new Object[0]), "m"))
/* 658 */         .getName(), simpleTaglet);
/* 659 */     addStandardTaglet(new SeeTaglet());
/*     */     
/* 661 */     addStandardTaglet(new DocRootTaglet());
/* 662 */     addStandardTaglet(new InheritDocTaglet());
/* 663 */     addStandardTaglet(new ValueTaglet());
/* 664 */     addStandardTaglet(new LiteralTaglet());
/* 665 */     addStandardTaglet(new CodeTaglet());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 670 */     this.standardTags.add("deprecated");
/* 671 */     this.standardTags.add("link");
/* 672 */     this.standardTags.add("linkplain");
/* 673 */     this.standardTags.add("serial");
/* 674 */     this.standardTags.add("serialField");
/* 675 */     this.standardTags.add("Text");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initJavaFXTaglets() {
/* 682 */     addStandardTaglet(new PropertyGetterTaglet());
/* 683 */     addStandardTaglet(new PropertySetterTaglet());
/* 684 */     addStandardTaglet(new SimpleTaglet("propertyDescription", this.message
/* 685 */           .getText("doclet.PropertyDescription", new Object[0]), "fm"));
/*     */     
/* 687 */     addStandardTaglet(new SimpleTaglet("defaultValue", this.message.getText("doclet.DefaultValue", new Object[0]), "fm"));
/*     */     
/* 689 */     addStandardTaglet(new SimpleTaglet("treatAsPrivate", null, "fmt"));
/*     */   }
/*     */ 
/*     */   
/*     */   void addStandardTaglet(Taglet paramTaglet) {
/* 694 */     String str = paramTaglet.getName();
/* 695 */     this.customTags.put(str, paramTaglet);
/* 696 */     this.standardTags.add(str);
/*     */   }
/*     */   
/*     */   void addStandardTaglet(boolean paramBoolean, Taglet paramTaglet) {
/* 700 */     String str = paramTaglet.getName();
/* 701 */     if (paramBoolean)
/* 702 */       this.customTags.put(str, paramTaglet); 
/* 703 */     this.standardTags.add(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initStandardTagsLowercase() {
/* 710 */     Iterator<String> iterator = this.standardTags.iterator();
/* 711 */     while (iterator.hasNext()) {
/* 712 */       this.standardTagsLowercase.add(StringUtils.toLowerCase(iterator.next()));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isKnownCustomTag(String paramString) {
/* 717 */     return this.customTags.containsKey(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printReport() {
/* 726 */     printReportHelper("doclet.Notice_taglet_conflict_warn", this.potentiallyConflictingTags);
/* 727 */     printReportHelper("doclet.Notice_taglet_overriden", this.overridenStandardTags);
/* 728 */     printReportHelper("doclet.Notice_taglet_unseen", this.unseenCustomTags);
/*     */   }
/*     */   
/*     */   private void printReportHelper(String paramString, Set<String> paramSet) {
/* 732 */     if (paramSet.size() > 0) {
/* 733 */       String[] arrayOfString = paramSet.<String>toArray(new String[0]);
/* 734 */       String str = " ";
/* 735 */       for (byte b = 0; b < arrayOfString.length; b++) {
/* 736 */         str = str + "@" + arrayOfString[b];
/* 737 */         if (b + 1 < arrayOfString.length) {
/* 738 */           str = str + ", ";
/*     */         }
/*     */       } 
/* 741 */       this.message.notice(paramString, new Object[] { str });
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
/*     */   public Taglet getTaglet(String paramString) {
/* 754 */     if (paramString.indexOf("@") == 0) {
/* 755 */       return this.customTags.get(paramString.substring(1));
/*     */     }
/* 757 */     return this.customTags.get(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\TagletManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */