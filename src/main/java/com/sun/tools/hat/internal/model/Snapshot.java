/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.parser.ReadBuffer;
/*     */ import com.sun.tools.hat.internal.util.Misc;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
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
/*     */ public class Snapshot
/*     */ {
/*  53 */   public static long SMALL_ID_MASK = 4294967295L;
/*  54 */   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */   
/*  56 */   private static final JavaField[] EMPTY_FIELD_ARRAY = new JavaField[0];
/*  57 */   private static final JavaStatic[] EMPTY_STATIC_ARRAY = new JavaStatic[0];
/*     */ 
/*     */   
/*  60 */   private Hashtable<Number, JavaHeapObject> heapObjects = new Hashtable<>();
/*     */ 
/*     */   
/*  63 */   private Hashtable<Number, JavaClass> fakeClasses = new Hashtable<>();
/*     */ 
/*     */ 
/*     */   
/*  67 */   private Vector<Root> roots = new Vector<>();
/*     */ 
/*     */   
/*  70 */   private Map<String, JavaClass> classes = new TreeMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile Map<JavaHeapObject, Boolean> newObjects;
/*     */ 
/*     */   
/*     */   private volatile Map<JavaHeapObject, StackTrace> siteTraces;
/*     */ 
/*     */   
/*  80 */   private Map<JavaHeapObject, Root> rootsMap = new HashMap<>();
/*     */ 
/*     */   
/*     */   private SoftReference<Vector> finalizablesCache;
/*     */ 
/*     */   
/*     */   private JavaThing nullThing;
/*     */ 
/*     */   
/*     */   private JavaClass weakReferenceClass;
/*     */ 
/*     */   
/*     */   private int referentFieldIndex;
/*     */ 
/*     */   
/*     */   private JavaClass javaLangClass;
/*     */ 
/*     */   
/*     */   private JavaClass javaLangString;
/*     */ 
/*     */   
/*     */   private JavaClass javaLangClassLoader;
/*     */ 
/*     */   
/*     */   private volatile JavaClass otherArrayType;
/*     */ 
/*     */   
/*     */   private ReachableExcludes reachableExcludes;
/*     */   
/*     */   private ReadBuffer readBuf;
/*     */   
/*     */   private boolean hasNewSet;
/*     */   
/*     */   private boolean unresolvedObjectsOK;
/*     */   
/*     */   private boolean newStyleArrayClass;
/*     */   
/* 117 */   private int identifierSize = 4;
/*     */   
/*     */   private int minimumObjectSize;
/*     */   
/*     */   private static final int DOT_LIMIT = 5000;
/*     */ 
/*     */   
/*     */   public Snapshot(ReadBuffer paramReadBuffer) {
/* 125 */     this.nullThing = new HackJavaValue("<null>", 0);
/* 126 */     this.readBuf = paramReadBuffer;
/*     */   }
/*     */   
/*     */   public void setSiteTrace(JavaHeapObject paramJavaHeapObject, StackTrace paramStackTrace) {
/* 130 */     if (paramStackTrace != null && (paramStackTrace.getFrames()).length != 0) {
/* 131 */       initSiteTraces();
/* 132 */       this.siteTraces.put(paramJavaHeapObject, paramStackTrace);
/*     */     } 
/*     */   }
/*     */   
/*     */   public StackTrace getSiteTrace(JavaHeapObject paramJavaHeapObject) {
/* 137 */     if (this.siteTraces != null) {
/* 138 */       return this.siteTraces.get(paramJavaHeapObject);
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNewStyleArrayClass(boolean paramBoolean) {
/* 145 */     this.newStyleArrayClass = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean isNewStyleArrayClass() {
/* 149 */     return this.newStyleArrayClass;
/*     */   }
/*     */   
/*     */   public void setIdentifierSize(int paramInt) {
/* 153 */     this.identifierSize = paramInt;
/* 154 */     this.minimumObjectSize = 2 * paramInt;
/*     */   }
/*     */   
/*     */   public int getIdentifierSize() {
/* 158 */     return this.identifierSize;
/*     */   }
/*     */   
/*     */   public int getMinimumObjectSize() {
/* 162 */     return this.minimumObjectSize;
/*     */   }
/*     */   
/*     */   public void addHeapObject(long paramLong, JavaHeapObject paramJavaHeapObject) {
/* 166 */     this.heapObjects.put(makeId(paramLong), paramJavaHeapObject);
/*     */   }
/*     */   
/*     */   public void addRoot(Root paramRoot) {
/* 170 */     paramRoot.setIndex(this.roots.size());
/* 171 */     this.roots.addElement(paramRoot);
/*     */   }
/*     */   
/*     */   public void addClass(long paramLong, JavaClass paramJavaClass) {
/* 175 */     addHeapObject(paramLong, paramJavaClass);
/* 176 */     putInClassesMap(paramJavaClass);
/*     */   }
/*     */ 
/*     */   
/*     */   JavaClass addFakeInstanceClass(long paramLong, int paramInt) {
/* 181 */     String str = "unknown-class<@" + Misc.toHex(paramLong) + ">";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     int i = paramInt / 4;
/* 187 */     int j = paramInt % 4;
/* 188 */     JavaField[] arrayOfJavaField = new JavaField[i + j];
/*     */     byte b;
/* 190 */     for (b = 0; b < i; b++) {
/* 191 */       arrayOfJavaField[b] = new JavaField("unknown-field-" + b, "I");
/*     */     }
/* 193 */     for (b = 0; b < j; b++) {
/* 194 */       arrayOfJavaField[b + i] = new JavaField("unknown-field-" + b + i, "B");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 199 */     JavaClass javaClass = new JavaClass(str, 0L, 0L, 0L, 0L, arrayOfJavaField, EMPTY_STATIC_ARRAY, paramInt);
/*     */ 
/*     */     
/* 202 */     addFakeClass(makeId(paramLong), javaClass);
/* 203 */     return javaClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasNewSet() {
/* 214 */     return this.hasNewSet;
/*     */   }
/*     */   
/*     */   private static class MyVisitor extends AbstractJavaHeapObjectVisitor {
/*     */     JavaHeapObject t;
/*     */     
/*     */     private MyVisitor() {}
/*     */     
/*     */     public void visit(JavaHeapObject param1JavaHeapObject) {
/* 223 */       param1JavaHeapObject.addReferenceFrom(this.t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resolve(boolean paramBoolean) {
/* 234 */     System.out.println("Resolving " + this.heapObjects.size() + " objects...");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 239 */     this.javaLangClass = findClass("java.lang.Class");
/* 240 */     if (this.javaLangClass == null) {
/* 241 */       System.out.println("WARNING:  hprof file does not include java.lang.Class!");
/* 242 */       this.javaLangClass = new JavaClass("java.lang.Class", 0L, 0L, 0L, 0L, EMPTY_FIELD_ARRAY, EMPTY_STATIC_ARRAY, 0);
/*     */       
/* 244 */       addFakeClass(this.javaLangClass);
/*     */     } 
/* 246 */     this.javaLangString = findClass("java.lang.String");
/* 247 */     if (this.javaLangString == null) {
/* 248 */       System.out.println("WARNING:  hprof file does not include java.lang.String!");
/* 249 */       this.javaLangString = new JavaClass("java.lang.String", 0L, 0L, 0L, 0L, EMPTY_FIELD_ARRAY, EMPTY_STATIC_ARRAY, 0);
/*     */       
/* 251 */       addFakeClass(this.javaLangString);
/*     */     } 
/* 253 */     this.javaLangClassLoader = findClass("java.lang.ClassLoader");
/* 254 */     if (this.javaLangClassLoader == null) {
/* 255 */       System.out.println("WARNING:  hprof file does not include java.lang.ClassLoader!");
/* 256 */       this.javaLangClassLoader = new JavaClass("java.lang.ClassLoader", 0L, 0L, 0L, 0L, EMPTY_FIELD_ARRAY, EMPTY_STATIC_ARRAY, 0);
/*     */       
/* 258 */       addFakeClass(this.javaLangClassLoader);
/*     */     } 
/*     */     
/* 261 */     for (JavaHeapObject javaHeapObject : this.heapObjects.values()) {
/* 262 */       if (javaHeapObject instanceof JavaClass) {
/* 263 */         javaHeapObject.resolve(this);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 268 */     for (JavaHeapObject javaHeapObject : this.heapObjects.values()) {
/* 269 */       if (!(javaHeapObject instanceof JavaClass)) {
/* 270 */         javaHeapObject.resolve(this);
/*     */       }
/*     */     } 
/*     */     
/* 274 */     this.heapObjects.putAll((Map)this.fakeClasses);
/* 275 */     this.fakeClasses.clear();
/*     */     
/* 277 */     this.weakReferenceClass = findClass("java.lang.ref.Reference");
/* 278 */     if (this.weakReferenceClass == null) {
/* 279 */       this.weakReferenceClass = findClass("sun.misc.Ref");
/* 280 */       this.referentFieldIndex = 0;
/*     */     } else {
/* 282 */       JavaField[] arrayOfJavaField = this.weakReferenceClass.getFieldsForInstance();
/* 283 */       for (byte b1 = 0; b1 < arrayOfJavaField.length; b1++) {
/* 284 */         if ("referent".equals(arrayOfJavaField[b1].getName())) {
/* 285 */           this.referentFieldIndex = b1;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 291 */     if (paramBoolean) {
/* 292 */       calculateReferencesToObjects();
/* 293 */       System.out.print("Eliminating duplicate references");
/* 294 */       System.out.flush();
/*     */     } 
/*     */     
/* 297 */     byte b = 0;
/* 298 */     for (JavaHeapObject javaHeapObject : this.heapObjects.values()) {
/* 299 */       javaHeapObject.setupReferers();
/* 300 */       b++;
/* 301 */       if (paramBoolean && b % 5000 == 0) {
/* 302 */         System.out.print(".");
/* 303 */         System.out.flush();
/*     */       } 
/*     */     } 
/* 306 */     if (paramBoolean) {
/* 307 */       System.out.println("");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 312 */     this.classes = Collections.unmodifiableMap(this.classes);
/*     */   }
/*     */   
/*     */   private void calculateReferencesToObjects() {
/* 316 */     System.out.print("Chasing references, expect " + (this.heapObjects
/* 317 */         .size() / 5000) + " dots");
/* 318 */     System.out.flush();
/* 319 */     byte b = 0;
/* 320 */     MyVisitor myVisitor = new MyVisitor();
/* 321 */     for (JavaHeapObject javaHeapObject : this.heapObjects.values()) {
/* 322 */       myVisitor.t = javaHeapObject;
/*     */       
/* 324 */       javaHeapObject.visitReferencedObjects(myVisitor);
/* 325 */       b++;
/* 326 */       if (b % 5000 == 0) {
/* 327 */         System.out.print(".");
/* 328 */         System.out.flush();
/*     */       } 
/*     */     } 
/* 331 */     System.out.println();
/* 332 */     for (Root root : this.roots) {
/* 333 */       root.resolve(this);
/* 334 */       JavaHeapObject javaHeapObject = findThing(root.getId());
/* 335 */       if (javaHeapObject != null) {
/* 336 */         javaHeapObject.addReferenceFromRoot(root);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void markNewRelativeTo(Snapshot paramSnapshot) {
/* 342 */     this.hasNewSet = true;
/* 343 */     for (JavaHeapObject javaHeapObject : this.heapObjects.values()) {
/*     */       boolean bool;
/* 345 */       long l = javaHeapObject.getId();
/* 346 */       if (l == 0L || l == -1L) {
/* 347 */         bool = false;
/*     */       } else {
/* 349 */         JavaHeapObject javaHeapObject1 = paramSnapshot.findThing(javaHeapObject.getId());
/* 350 */         if (javaHeapObject1 == null) {
/* 351 */           bool = true;
/*     */         } else {
/* 353 */           bool = !javaHeapObject.isSameTypeAs(javaHeapObject1) ? true : false;
/*     */         } 
/*     */       } 
/* 356 */       javaHeapObject.setNew(bool);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Enumeration<JavaHeapObject> getThings() {
/* 361 */     return this.heapObjects.elements();
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaHeapObject findThing(long paramLong) {
/* 366 */     Number number = makeId(paramLong);
/* 367 */     JavaHeapObject javaHeapObject = this.heapObjects.get(number);
/* 368 */     return (javaHeapObject != null) ? javaHeapObject : this.fakeClasses.get(number);
/*     */   }
/*     */   
/*     */   public JavaHeapObject findThing(String paramString) {
/* 372 */     return findThing(Misc.parseHex(paramString));
/*     */   }
/*     */   
/*     */   public JavaClass findClass(String paramString) {
/* 376 */     if (paramString.startsWith("0x")) {
/* 377 */       return (JavaClass)findThing(paramString);
/*     */     }
/* 379 */     return this.classes.get(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator getClasses() {
/* 389 */     return this.classes.values().iterator();
/*     */   }
/*     */   
/*     */   public JavaClass[] getClassesArray() {
/* 393 */     JavaClass[] arrayOfJavaClass = new JavaClass[this.classes.size()];
/* 394 */     this.classes.values().toArray((Object[])arrayOfJavaClass);
/* 395 */     return arrayOfJavaClass;
/*     */   }
/*     */   
/*     */   public synchronized Enumeration getFinalizerObjects() {
/*     */     Vector vector;
/* 400 */     if (this.finalizablesCache != null && (
/* 401 */       vector = this.finalizablesCache.get()) != null) {
/* 402 */       return vector.elements();
/*     */     }
/*     */     
/* 405 */     JavaClass javaClass = findClass("java.lang.ref.Finalizer");
/* 406 */     JavaObject javaObject = (JavaObject)javaClass.getStaticField("queue");
/* 407 */     JavaThing javaThing = javaObject.getField("head");
/* 408 */     Vector<JavaHeapObject> vector1 = new Vector();
/* 409 */     if (javaThing != getNullThing()) {
/* 410 */       JavaObject javaObject1 = (JavaObject)javaThing;
/*     */       while (true) {
/* 412 */         JavaHeapObject javaHeapObject = (JavaHeapObject)javaObject1.getField("referent");
/* 413 */         JavaThing javaThing1 = javaObject1.getField("next");
/* 414 */         if (javaThing1 == getNullThing() || javaThing1.equals(javaObject1)) {
/*     */           break;
/*     */         }
/* 417 */         javaObject1 = (JavaObject)javaThing1;
/* 418 */         vector1.add(javaHeapObject);
/*     */       } 
/*     */     } 
/* 421 */     this.finalizablesCache = new SoftReference<>(vector1);
/* 422 */     return vector1.elements();
/*     */   }
/*     */   
/*     */   public Enumeration<Root> getRoots() {
/* 426 */     return this.roots.elements();
/*     */   }
/*     */   
/*     */   public Root[] getRootsArray() {
/* 430 */     Root[] arrayOfRoot = new Root[this.roots.size()];
/* 431 */     this.roots.toArray(arrayOfRoot);
/* 432 */     return arrayOfRoot;
/*     */   }
/*     */   
/*     */   public Root getRootAt(int paramInt) {
/* 436 */     return this.roots.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ReferenceChain[] rootsetReferencesTo(JavaHeapObject paramJavaHeapObject, boolean paramBoolean) {
/* 441 */     Vector<ReferenceChain> vector1 = new Vector();
/*     */     
/* 443 */     Hashtable<Object, Object> hashtable = new Hashtable<>();
/*     */     
/* 445 */     Vector<ReferenceChain> vector2 = new Vector();
/* 446 */     hashtable.put(paramJavaHeapObject, paramJavaHeapObject);
/* 447 */     vector1.addElement(new ReferenceChain(paramJavaHeapObject, null));
/*     */     
/* 449 */     while (vector1.size() > 0) {
/* 450 */       ReferenceChain referenceChain = vector1.elementAt(0);
/* 451 */       vector1.removeElementAt(0);
/* 452 */       JavaHeapObject javaHeapObject = referenceChain.getObj();
/* 453 */       if (javaHeapObject.getRoot() != null) {
/* 454 */         vector2.addElement(referenceChain);
/*     */       }
/*     */ 
/*     */       
/* 458 */       Enumeration<JavaHeapObject> enumeration = javaHeapObject.getReferers();
/* 459 */       while (enumeration.hasMoreElements()) {
/* 460 */         JavaHeapObject javaHeapObject1 = enumeration.nextElement();
/* 461 */         if (javaHeapObject1 != null && !hashtable.containsKey(javaHeapObject1) && (
/* 462 */           paramBoolean || !javaHeapObject1.refersOnlyWeaklyTo(this, javaHeapObject))) {
/* 463 */           hashtable.put(javaHeapObject1, javaHeapObject1);
/* 464 */           vector1.addElement(new ReferenceChain(javaHeapObject1, referenceChain));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 470 */     ReferenceChain[] arrayOfReferenceChain = new ReferenceChain[vector2.size()];
/* 471 */     for (byte b = 0; b < vector2.size(); b++) {
/* 472 */       arrayOfReferenceChain[b] = vector2.elementAt(b);
/*     */     }
/* 474 */     return arrayOfReferenceChain;
/*     */   }
/*     */   
/*     */   public boolean getUnresolvedObjectsOK() {
/* 478 */     return this.unresolvedObjectsOK;
/*     */   }
/*     */   
/*     */   public void setUnresolvedObjectsOK(boolean paramBoolean) {
/* 482 */     this.unresolvedObjectsOK = paramBoolean;
/*     */   }
/*     */   
/*     */   public JavaClass getWeakReferenceClass() {
/* 486 */     return this.weakReferenceClass;
/*     */   }
/*     */   
/*     */   public int getReferentFieldIndex() {
/* 490 */     return this.referentFieldIndex;
/*     */   }
/*     */   
/*     */   public JavaThing getNullThing() {
/* 494 */     return this.nullThing;
/*     */   }
/*     */   
/*     */   public void setReachableExcludes(ReachableExcludes paramReachableExcludes) {
/* 498 */     this.reachableExcludes = paramReachableExcludes;
/*     */   }
/*     */   
/*     */   public ReachableExcludes getReachableExcludes() {
/* 502 */     return this.reachableExcludes;
/*     */   }
/*     */ 
/*     */   
/*     */   void addReferenceFromRoot(Root paramRoot, JavaHeapObject paramJavaHeapObject) {
/* 507 */     Root root = this.rootsMap.get(paramJavaHeapObject);
/* 508 */     if (root == null) {
/* 509 */       this.rootsMap.put(paramJavaHeapObject, paramRoot);
/*     */     } else {
/* 511 */       this.rootsMap.put(paramJavaHeapObject, root.mostInteresting(paramRoot));
/*     */     } 
/*     */   }
/*     */   
/*     */   Root getRoot(JavaHeapObject paramJavaHeapObject) {
/* 516 */     return this.rootsMap.get(paramJavaHeapObject);
/*     */   }
/*     */   
/*     */   JavaClass getJavaLangClass() {
/* 520 */     return this.javaLangClass;
/*     */   }
/*     */   
/*     */   JavaClass getJavaLangString() {
/* 524 */     return this.javaLangString;
/*     */   }
/*     */   
/*     */   JavaClass getJavaLangClassLoader() {
/* 528 */     return this.javaLangClassLoader;
/*     */   }
/*     */   
/*     */   JavaClass getOtherArrayType() {
/* 532 */     if (this.otherArrayType == null) {
/* 533 */       synchronized (this) {
/* 534 */         if (this.otherArrayType == null) {
/* 535 */           addFakeClass(new JavaClass("[<other>", 0L, 0L, 0L, 0L, EMPTY_FIELD_ARRAY, EMPTY_STATIC_ARRAY, 0));
/*     */ 
/*     */           
/* 538 */           this.otherArrayType = findClass("[<other>");
/*     */         } 
/*     */       } 
/*     */     }
/* 542 */     return this.otherArrayType;
/*     */   }
/*     */   
/*     */   JavaClass getArrayClass(String paramString) {
/*     */     JavaClass javaClass;
/* 547 */     synchronized (this.classes) {
/* 548 */       javaClass = findClass("[" + paramString);
/* 549 */       if (javaClass == null) {
/* 550 */         javaClass = new JavaClass("[" + paramString, 0L, 0L, 0L, 0L, EMPTY_FIELD_ARRAY, EMPTY_STATIC_ARRAY, 0);
/*     */         
/* 552 */         addFakeClass(javaClass);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 559 */     return javaClass;
/*     */   }
/*     */   
/*     */   ReadBuffer getReadBuffer() {
/* 563 */     return this.readBuf;
/*     */   }
/*     */   
/*     */   void setNew(JavaHeapObject paramJavaHeapObject, boolean paramBoolean) {
/* 567 */     initNewObjects();
/* 568 */     if (paramBoolean) {
/* 569 */       this.newObjects.put(paramJavaHeapObject, Boolean.TRUE);
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isNew(JavaHeapObject paramJavaHeapObject) {
/* 574 */     if (this.newObjects != null) {
/* 575 */       return (this.newObjects.get(paramJavaHeapObject) != null);
/*     */     }
/* 577 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Number makeId(long paramLong) {
/* 583 */     if (this.identifierSize == 4) {
/* 584 */       return new Integer((int)paramLong);
/*     */     }
/* 586 */     return new Long(paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   private void putInClassesMap(JavaClass paramJavaClass) {
/* 591 */     String str = paramJavaClass.getName();
/* 592 */     if (this.classes.containsKey(str))
/*     */     {
/*     */ 
/*     */       
/* 596 */       str = str + "-" + paramJavaClass.getIdString();
/*     */     }
/* 598 */     this.classes.put(paramJavaClass.getName(), paramJavaClass);
/*     */   }
/*     */   
/*     */   private void addFakeClass(JavaClass paramJavaClass) {
/* 602 */     putInClassesMap(paramJavaClass);
/* 603 */     paramJavaClass.resolve(this);
/*     */   }
/*     */   
/*     */   private void addFakeClass(Number paramNumber, JavaClass paramJavaClass) {
/* 607 */     this.fakeClasses.put(paramNumber, paramJavaClass);
/* 608 */     addFakeClass(paramJavaClass);
/*     */   }
/*     */   
/*     */   private synchronized void initNewObjects() {
/* 612 */     if (this.newObjects == null) {
/* 613 */       synchronized (this) {
/* 614 */         if (this.newObjects == null) {
/* 615 */           this.newObjects = new HashMap<>();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void initSiteTraces() {
/* 622 */     if (this.siteTraces == null)
/* 623 */       synchronized (this) {
/* 624 */         if (this.siteTraces == null)
/* 625 */           this.siteTraces = new HashMap<>(); 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\Snapshot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */