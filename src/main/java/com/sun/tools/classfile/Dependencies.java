/*     */ package com.sun.tools.classfile;
/*     */
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class Dependencies
/*     */ {
/*     */   private Dependency.Filter filter;
/*     */   private Dependency.Finder finder;
/*     */
/*     */   public static class ClassFileNotFoundException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 3632265927794475048L;
/*     */     public final String className;
/*     */
/*     */     public ClassFileNotFoundException(String param1String) {
/*  70 */       super(param1String);
/*  71 */       this.className = param1String;
/*     */     }
/*     */
/*     */     public ClassFileNotFoundException(String param1String, Throwable param1Throwable) {
/*  75 */       this(param1String);
/*  76 */       initCause(param1Throwable);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   public static class ClassFileError
/*     */     extends Error
/*     */   {
/*     */     private static final long serialVersionUID = 4111110813961313203L;
/*     */
/*     */
/*     */     public ClassFileError(Throwable param1Throwable) {
/*  89 */       initCause(param1Throwable);
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static Dependency.Finder getDefaultFinder() {
/* 124 */     return new APIDependencyFinder(2);
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
/*     */   public static Dependency.Finder getAPIFinder(int paramInt) {
/* 141 */     return new APIDependencyFinder(paramInt);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static Dependency.Finder getClassDependencyFinder() {
/* 150 */     return new ClassDependencyFinder();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Dependency.Finder getFinder() {
/* 158 */     if (this.finder == null)
/* 159 */       this.finder = getDefaultFinder();
/* 160 */     return this.finder;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void setFinder(Dependency.Finder paramFinder) {
/* 168 */     paramFinder.getClass();
/* 169 */     this.finder = paramFinder;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static Dependency.Filter getDefaultFilter() {
/* 179 */     return DefaultFilter.instance();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static Dependency.Filter getRegexFilter(Pattern paramPattern) {
/* 189 */     return new TargetRegexFilter(paramPattern);
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
/*     */   public static Dependency.Filter getPackageFilter(Set<String> paramSet, boolean paramBoolean) {
/* 202 */     return new TargetPackageFilter(paramSet, paramBoolean);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Dependency.Filter getFilter() {
/* 212 */     if (this.filter == null)
/* 213 */       this.filter = getDefaultFilter();
/* 214 */     return this.filter;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void setFilter(Dependency.Filter paramFilter) {
/* 223 */     paramFilter.getClass();
/* 224 */     this.filter = paramFilter;
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
/*     */   public Set<Dependency> findAllDependencies(ClassFileReader paramClassFileReader, Set<String> paramSet, boolean paramBoolean) throws ClassFileNotFoundException {
/* 248 */     final HashSet<Dependency> results = new HashSet();
/* 249 */     Recorder recorder = new Recorder() {
/*     */         public void addDependency(Dependency param1Dependency) {
/* 251 */           results.add(param1Dependency);
/*     */         }
/*     */       };
/* 254 */     findAllDependencies(paramClassFileReader, paramSet, paramBoolean, recorder);
/* 255 */     return hashSet;
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
/*     */   public void findAllDependencies(ClassFileReader paramClassFileReader, Set<String> paramSet, boolean paramBoolean, Recorder paramRecorder) throws ClassFileNotFoundException {
/* 279 */     HashSet<String> hashSet = new HashSet();
/*     */
/* 281 */     getFinder();
/* 282 */     getFilter();
/*     */
/*     */
/*     */
/*     */
/* 287 */     LinkedList<String> linkedList = new LinkedList<>(paramSet);
/*     */
/*     */     String str;
/* 290 */     while ((str = linkedList.poll()) != null) {
/* 291 */       assert !hashSet.contains(str);
/* 292 */       hashSet.add(str);
/*     */
/* 294 */       ClassFile classFile = paramClassFileReader.getClassFile(str);
/*     */
/*     */
/*     */
/* 298 */       for (Dependency dependency : this.finder.findDependencies(classFile)) {
/* 299 */         paramRecorder.addDependency(dependency);
/* 300 */         if (paramBoolean && this.filter.accepts(dependency)) {
/* 301 */           String str1 = dependency.getTarget().getClassName();
/* 302 */           if (!hashSet.contains(str1)) {
/* 303 */             linkedList.add(str1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   static class SimpleLocation
/*     */     implements Dependency.Location
/*     */   {
/*     */     private String name;
/*     */     private String className;
/*     */
/*     */     public SimpleLocation(String param1String) {
/* 317 */       this.name = param1String;
/* 318 */       this.className = param1String.replace('/', '.');
/*     */     }
/*     */
/*     */     public String getName() {
/* 322 */       return this.name;
/*     */     }
/*     */
/*     */     public String getClassName() {
/* 326 */       return this.className;
/*     */     }
/*     */
/*     */     public String getPackageName() {
/* 330 */       int i = this.name.lastIndexOf('/');
/* 331 */       return (i > 0) ? this.name.substring(0, i).replace('/', '.') : "";
/*     */     }
/*     */
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 336 */       if (this == param1Object)
/* 337 */         return true;
/* 338 */       if (!(param1Object instanceof SimpleLocation))
/* 339 */         return false;
/* 340 */       return this.name.equals(((SimpleLocation)param1Object).name);
/*     */     }
/*     */
/*     */
/*     */     public int hashCode() {
/* 345 */       return this.name.hashCode();
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 350 */       return this.name;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   static class SimpleDependency
/*     */     implements Dependency
/*     */   {
/*     */     private Location origin;
/*     */     private Location target;
/*     */
/*     */     public SimpleDependency(Location param1Location1, Location param1Location2) {
/* 362 */       this.origin = param1Location1;
/* 363 */       this.target = param1Location2;
/*     */     }
/*     */
/*     */     public Location getOrigin() {
/* 367 */       return this.origin;
/*     */     }
/*     */
/*     */     public Location getTarget() {
/* 371 */       return this.target;
/*     */     }
/*     */
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 376 */       if (this == param1Object)
/* 377 */         return true;
/* 378 */       if (!(param1Object instanceof SimpleDependency))
/* 379 */         return false;
/* 380 */       SimpleDependency simpleDependency = (SimpleDependency)param1Object;
/* 381 */       return (this.origin.equals(simpleDependency.origin) && this.target.equals(simpleDependency.target));
/*     */     }
/*     */
/*     */
/*     */     public int hashCode() {
/* 386 */       return this.origin.hashCode() * 31 + this.target.hashCode();
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 391 */       return this.origin + ":" + this.target;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   static class DefaultFilter
/*     */     implements Dependency.Filter
/*     */   {
/*     */     private static DefaultFilter instance;
/*     */
/*     */
/*     */
/*     */     static DefaultFilter instance() {
/* 406 */       if (instance == null)
/* 407 */         instance = new DefaultFilter();
/* 408 */       return instance;
/*     */     }
/*     */
/*     */     public boolean accepts(Dependency param1Dependency) {
/* 412 */       return true;
/*     */     }
/*     */   }
/*     */
/*     */   static class TargetRegexFilter
/*     */     implements Dependency.Filter
/*     */   {
/*     */     private final Pattern pattern;
/*     */
/*     */     TargetRegexFilter(Pattern param1Pattern) {
/* 422 */       this.pattern = param1Pattern;
/*     */     }
/*     */
/*     */     public boolean accepts(Dependency param1Dependency) {
/* 426 */       return this.pattern.matcher(param1Dependency.getTarget().getClassName()).matches();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   static class TargetPackageFilter
/*     */     implements Dependency.Filter
/*     */   {
/*     */     private final Set<String> packageNames;
/*     */     private final boolean matchSubpackages;
/*     */
/*     */     TargetPackageFilter(Set<String> param1Set, boolean param1Boolean) {
/* 438 */       for (String str : param1Set) {
/* 439 */         if (str.length() == 0)
/* 440 */           throw new IllegalArgumentException();
/*     */       }
/* 442 */       this.packageNames = param1Set;
/* 443 */       this.matchSubpackages = param1Boolean;
/*     */     }
/*     */
/*     */     public boolean accepts(Dependency param1Dependency) {
/* 447 */       String str = param1Dependency.getTarget().getPackageName();
/* 448 */       if (this.packageNames.contains(str)) {
/* 449 */         return true;
/*     */       }
/* 451 */       if (this.matchSubpackages) {
/* 452 */         for (String str1 : this.packageNames) {
/* 453 */           if (str.startsWith(str1 + ".")) {
/* 454 */             return true;
/*     */           }
/*     */         }
/*     */       }
/* 458 */       return false;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static class ClassDependencyFinder
/*     */     extends BasicDependencyFinder
/*     */   {
/*     */     public Iterable<? extends Dependency> findDependencies(ClassFile param1ClassFile) {
/* 470 */       Visitor visitor = new Visitor(this, param1ClassFile);
/* 471 */       for (ConstantPool.CPInfo cPInfo : param1ClassFile.constant_pool.entries()) {
/* 472 */         visitor.scan(cPInfo);
/*     */       }
/*     */       try {
/* 475 */         visitor.addClass(param1ClassFile.super_class);
/* 476 */         visitor.addClasses(param1ClassFile.interfaces);
/* 477 */         visitor.scan(param1ClassFile.attributes);
/*     */
/* 479 */         for (Field field : param1ClassFile.fields) {
/* 480 */           visitor.scan(field.descriptor, field.attributes);
/*     */         }
/* 482 */         for (Method method : param1ClassFile.methods) {
/* 483 */           visitor.scan(method.descriptor, method.attributes);
/*     */
/* 485 */           Exceptions_attribute exceptions_attribute = (Exceptions_attribute)method.attributes.get("Exceptions");
/* 486 */           if (exceptions_attribute != null) {
/* 487 */             visitor.addClasses(exceptions_attribute.exception_index_table);
/*     */           }
/*     */         }
/* 490 */       } catch (ConstantPoolException constantPoolException) {
/* 491 */         throw new ClassFileError(constantPoolException);
/*     */       }
/*     */
/* 494 */       return visitor.deps;
/*     */     }
/*     */   }
/*     */
/*     */   static class APIDependencyFinder
/*     */     extends BasicDependencyFinder
/*     */   {
/*     */     private int showAccess;
/*     */
/*     */     APIDependencyFinder(int param1Int) {
/* 504 */       switch (param1Int) {
/*     */         case 0:
/*     */         case 1:
/*     */         case 2:
/*     */         case 4:
/* 509 */           this.showAccess = param1Int;
/*     */           return;
/*     */       }
/* 512 */       throw new IllegalArgumentException("invalid access 0x" +
/* 513 */           Integer.toHexString(param1Int));
/*     */     }
/*     */
/*     */
/*     */     public Iterable<? extends Dependency> findDependencies(ClassFile param1ClassFile) {
/*     */       try {
/* 519 */         Visitor visitor = new Visitor(this, param1ClassFile);
/* 520 */         visitor.addClass(param1ClassFile.super_class);
/* 521 */         visitor.addClasses(param1ClassFile.interfaces);
/*     */
/* 523 */         for (Field field : param1ClassFile.fields) {
/* 524 */           if (checkAccess(field.access_flags))
/* 525 */             visitor.scan(field.descriptor, field.attributes);
/*     */         }
/* 527 */         for (Method method : param1ClassFile.methods) {
/* 528 */           if (checkAccess(method.access_flags)) {
/* 529 */             visitor.scan(method.descriptor, method.attributes);
/*     */
/* 531 */             Exceptions_attribute exceptions_attribute = (Exceptions_attribute)method.attributes.get("Exceptions");
/* 532 */             if (exceptions_attribute != null)
/* 533 */               visitor.addClasses(exceptions_attribute.exception_index_table);
/*     */           }
/*     */         }
/* 536 */         return visitor.deps;
/* 537 */       } catch (ConstantPoolException constantPoolException) {
/* 538 */         throw new ClassFileError(constantPoolException);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     boolean checkAccess(AccessFlags param1AccessFlags) {
/* 544 */       boolean bool1 = param1AccessFlags.is(1);
/* 545 */       boolean bool2 = param1AccessFlags.is(4);
/* 546 */       boolean bool3 = param1AccessFlags.is(2);
/* 547 */       boolean bool = (!bool1 && !bool2 && !bool3) ? true : false;
/*     */
/* 549 */       if (this.showAccess == 1 && (bool2 || bool3 || bool))
/* 550 */         return false;
/* 551 */       if (this.showAccess == 4 && (bool3 || bool))
/* 552 */         return false;
/* 553 */       if (this.showAccess == 0 && bool3) {
/* 554 */         return false;
/*     */       }
/* 556 */       return true;
/*     */     }
/*     */   }
/*     */
/*     */   static abstract class BasicDependencyFinder
/*     */     implements Dependency.Finder
/*     */   {
/* 563 */     private Map<String, Dependency.Location> locations = new HashMap<>();
/*     */
/*     */     Dependency.Location getLocation(String param1String) {
/* 566 */       Dependency.Location location = this.locations.get(param1String);
/* 567 */       if (location == null)
/* 568 */         this.locations.put(param1String, location = new SimpleLocation(param1String));
/* 569 */       return location;
/*     */     }
/*     */
/*     */     class Visitor implements ConstantPool.Visitor<Void, Void>, Type.Visitor<Void, Void> {
/*     */       private ConstantPool constant_pool;
/*     */       private Dependency.Location origin;
/*     */       Set<Dependency> deps;
/*     */
/*     */       Visitor(ClassFile param2ClassFile) {
/*     */         try {
/* 579 */           this.constant_pool = param2ClassFile.constant_pool;
/* 580 */           this.origin = BasicDependencyFinder.this.getLocation(param2ClassFile.getName());
/* 581 */           this.deps = new HashSet<>();
/* 582 */         } catch (ConstantPoolException constantPoolException) {
/* 583 */           throw new ClassFileError(constantPoolException);
/*     */         }
/*     */       }
/*     */
/*     */       void scan(Descriptor param2Descriptor, Attributes param2Attributes) {
/*     */         try {
/* 589 */           scan((new Signature(param2Descriptor.index)).getType(this.constant_pool));
/* 590 */           scan(param2Attributes);
/* 591 */         } catch (ConstantPoolException constantPoolException) {
/* 592 */           throw new ClassFileError(constantPoolException);
/*     */         }
/*     */       }
/*     */
/*     */       void scan(ConstantPool.CPInfo param2CPInfo) {
/* 597 */         param2CPInfo.accept(this, null);
/*     */       }
/*     */
/*     */       void scan(Type param2Type) {
/* 601 */         param2Type.accept(this, null);
/*     */       }
/*     */
/*     */       void scan(Attributes param2Attributes) {
/*     */         try {
/* 606 */           Signature_attribute signature_attribute = (Signature_attribute)param2Attributes.get("Signature");
/* 607 */           if (signature_attribute != null) {
/* 608 */             scan(signature_attribute.getParsedSignature().getType(this.constant_pool));
/*     */           }
/* 610 */           scan((RuntimeVisibleAnnotations_attribute)param2Attributes
/* 611 */               .get("RuntimeVisibleAnnotations"));
/* 612 */           scan((RuntimeVisibleParameterAnnotations_attribute)param2Attributes
/* 613 */               .get("RuntimeVisibleParameterAnnotations"));
/* 614 */         } catch (ConstantPoolException constantPoolException) {
/* 615 */           throw new ClassFileError(constantPoolException);
/*     */         }
/*     */       }
/*     */
/*     */       private void scan(RuntimeAnnotations_attribute param2RuntimeAnnotations_attribute) throws ConstantPoolException {
/* 620 */         if (param2RuntimeAnnotations_attribute == null) {
/*     */           return;
/*     */         }
/* 623 */         for (byte b = 0; b < param2RuntimeAnnotations_attribute.annotations.length; b++) {
/* 624 */           int i = (param2RuntimeAnnotations_attribute.annotations[b]).type_index;
/* 625 */           scan((new Signature(i)).getType(this.constant_pool));
/*     */         }
/*     */       }
/*     */
/*     */       private void scan(RuntimeParameterAnnotations_attribute param2RuntimeParameterAnnotations_attribute) throws ConstantPoolException {
/* 630 */         if (param2RuntimeParameterAnnotations_attribute == null) {
/*     */           return;
/*     */         }
/* 633 */         for (byte b = 0; b < param2RuntimeParameterAnnotations_attribute.parameter_annotations.length; b++) {
/* 634 */           for (byte b1 = 0; b1 < (param2RuntimeParameterAnnotations_attribute.parameter_annotations[b]).length; b1++) {
/* 635 */             int i = (param2RuntimeParameterAnnotations_attribute.parameter_annotations[b][b1]).type_index;
/* 636 */             scan((new Signature(i)).getType(this.constant_pool));
/*     */           }
/*     */         }
/*     */       }
/*     */
/*     */       void addClass(int param2Int) throws ConstantPoolException {
/* 642 */         if (param2Int != 0) {
/* 643 */           String str = this.constant_pool.getClassInfo(param2Int).getBaseName();
/* 644 */           if (str != null)
/* 645 */             addDependency(str);
/*     */         }
/*     */       }
/*     */
/*     */       void addClasses(int[] param2ArrayOfint) throws ConstantPoolException {
/* 650 */         for (int i : param2ArrayOfint)
/* 651 */           addClass(i);
/*     */       }
/*     */
/*     */       private void addDependency(String param2String) {
/* 655 */         this.deps.add(new SimpleDependency(this.origin, BasicDependencyFinder.this.getLocation(param2String)));
/*     */       }
/*     */
/*     */
/*     */
/*     */       public Void visitClass(ConstantPool.CONSTANT_Class_info param2CONSTANT_Class_info, Void param2Void) {
/*     */         try {
/* 662 */           if (param2CONSTANT_Class_info.getName().startsWith("[")) {
/* 663 */             (new Signature(param2CONSTANT_Class_info.name_index)).getType(this.constant_pool).accept(this, null);
/*     */           } else {
/* 665 */             addDependency(param2CONSTANT_Class_info.getBaseName());
/* 666 */           }  return null;
/* 667 */         } catch (ConstantPoolException constantPoolException) {
/* 668 */           throw new ClassFileError(constantPoolException);
/*     */         }
/*     */       }
/*     */
/*     */       public Void visitDouble(ConstantPool.CONSTANT_Double_info param2CONSTANT_Double_info, Void param2Void) {
/* 673 */         return null;
/*     */       }
/*     */
/*     */       public Void visitFieldref(ConstantPool.CONSTANT_Fieldref_info param2CONSTANT_Fieldref_info, Void param2Void) {
/* 677 */         return visitRef(param2CONSTANT_Fieldref_info, param2Void);
/*     */       }
/*     */
/*     */       public Void visitFloat(ConstantPool.CONSTANT_Float_info param2CONSTANT_Float_info, Void param2Void) {
/* 681 */         return null;
/*     */       }
/*     */
/*     */       public Void visitInteger(ConstantPool.CONSTANT_Integer_info param2CONSTANT_Integer_info, Void param2Void) {
/* 685 */         return null;
/*     */       }
/*     */
/*     */       public Void visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info param2CONSTANT_InterfaceMethodref_info, Void param2Void) {
/* 689 */         return visitRef(param2CONSTANT_InterfaceMethodref_info, param2Void);
/*     */       }
/*     */
/*     */       public Void visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info param2CONSTANT_InvokeDynamic_info, Void param2Void) {
/* 693 */         return null;
/*     */       }
/*     */
/*     */       public Void visitLong(ConstantPool.CONSTANT_Long_info param2CONSTANT_Long_info, Void param2Void) {
/* 697 */         return null;
/*     */       }
/*     */
/*     */       public Void visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info param2CONSTANT_MethodHandle_info, Void param2Void) {
/* 701 */         return null;
/*     */       }
/*     */
/*     */       public Void visitMethodType(ConstantPool.CONSTANT_MethodType_info param2CONSTANT_MethodType_info, Void param2Void) {
/* 705 */         return null;
/*     */       }
/*     */
/*     */       public Void visitMethodref(ConstantPool.CONSTANT_Methodref_info param2CONSTANT_Methodref_info, Void param2Void) {
/* 709 */         return visitRef(param2CONSTANT_Methodref_info, param2Void);
/*     */       }
/*     */
/*     */       public Void visitNameAndType(ConstantPool.CONSTANT_NameAndType_info param2CONSTANT_NameAndType_info, Void param2Void) {
/*     */         try {
/* 714 */           (new Signature(param2CONSTANT_NameAndType_info.type_index)).getType(this.constant_pool).accept(this, null);
/* 715 */           return null;
/* 716 */         } catch (ConstantPoolException constantPoolException) {
/* 717 */           throw new ClassFileError(constantPoolException);
/*     */         }
/*     */       }
/*     */
/*     */       public Void visitString(ConstantPool.CONSTANT_String_info param2CONSTANT_String_info, Void param2Void) {
/* 722 */         return null;
/*     */       }
/*     */
/*     */       public Void visitUtf8(ConstantPool.CONSTANT_Utf8_info param2CONSTANT_Utf8_info, Void param2Void) {
/* 726 */         return null;
/*     */       }
/*     */
/*     */       private Void visitRef(ConstantPool.CPRefInfo param2CPRefInfo, Void param2Void) {
/*     */         try {
/* 731 */           visitClass(param2CPRefInfo.getClassInfo(), param2Void);
/* 732 */           return null;
/* 733 */         } catch (ConstantPoolException constantPoolException) {
/* 734 */           throw new ClassFileError(constantPoolException);
/*     */         }
/*     */       }
/*     */
/*     */
/*     */
/*     */       private void findDependencies(Type param2Type) {
/* 741 */         if (param2Type != null)
/* 742 */           param2Type.accept(this, null);
/*     */       }
/*     */
/*     */       private void findDependencies(List<? extends Type> param2List) {
/* 746 */         if (param2List != null)
/* 747 */           for (Type type : param2List) {
/* 748 */             type.accept(this, null);
/*     */           }
/*     */       }
/*     */
/*     */       public Void visitSimpleType(Type.SimpleType param2SimpleType, Void param2Void) {
/* 753 */         return null;
/*     */       }
/*     */
/*     */       public Void visitArrayType(Type.ArrayType param2ArrayType, Void param2Void) {
/* 757 */         findDependencies(param2ArrayType.elemType);
/* 758 */         return null;
/*     */       }
/*     */
/*     */       public Void visitMethodType(Type.MethodType param2MethodType, Void param2Void) {
/* 762 */         findDependencies(param2MethodType.paramTypes);
/* 763 */         findDependencies(param2MethodType.returnType);
/* 764 */         findDependencies(param2MethodType.throwsTypes);
/* 765 */         findDependencies((List)param2MethodType.typeParamTypes);
/* 766 */         return null;
/*     */       }
/*     */
/*     */       public Void visitClassSigType(Type.ClassSigType param2ClassSigType, Void param2Void) {
/* 770 */         findDependencies(param2ClassSigType.superclassType);
/* 771 */         findDependencies(param2ClassSigType.superinterfaceTypes);
/* 772 */         return null;
/*     */       }
/*     */
/*     */       public Void visitClassType(Type.ClassType param2ClassType, Void param2Void) {
/* 776 */         findDependencies(param2ClassType.outerType);
/* 777 */         addDependency(param2ClassType.getBinaryName());
/* 778 */         findDependencies(param2ClassType.typeArgs);
/* 779 */         return null;
/*     */       }
/*     */
/*     */       public Void visitTypeParamType(Type.TypeParamType param2TypeParamType, Void param2Void) {
/* 783 */         findDependencies(param2TypeParamType.classBound);
/* 784 */         findDependencies(param2TypeParamType.interfaceBounds);
/* 785 */         return null;
/*     */       }
/*     */
/*     */       public Void visitWildcardType(Type.WildcardType param2WildcardType, Void param2Void) {
/* 789 */         findDependencies(param2WildcardType.boundType);
/* 790 */         return null;
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   public static interface ClassFileReader {
/*     */     ClassFile getClassFile(String param1String) throws ClassFileNotFoundException;
/*     */   }
/*     */
/*     */   public static interface Recorder {
/*     */     void addDependency(Dependency param1Dependency);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Dependencies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
