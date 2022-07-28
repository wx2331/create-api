/*      */ package com.sun.tools.jdi;
/*      */ 
/*      */ import com.sun.jdi.AbsentInformationException;
/*      */ import com.sun.jdi.ClassLoaderReference;
/*      */ import com.sun.jdi.ClassNotLoadedException;
/*      */ import com.sun.jdi.ClassObjectReference;
/*      */ import com.sun.jdi.Field;
/*      */ import com.sun.jdi.InterfaceType;
/*      */ import com.sun.jdi.InternalException;
/*      */ import com.sun.jdi.Location;
/*      */ import com.sun.jdi.Method;
/*      */ import com.sun.jdi.ObjectReference;
/*      */ import com.sun.jdi.PrimitiveType;
/*      */ import com.sun.jdi.ReferenceType;
/*      */ import com.sun.jdi.Type;
/*      */ import com.sun.jdi.Value;
/*      */ import com.sun.jdi.VirtualMachine;
/*      */ import com.sun.jdi.VoidType;
/*      */ import java.io.File;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ public abstract class ReferenceTypeImpl
/*      */   extends TypeImpl
/*      */   implements ReferenceType {
/*      */   protected long ref;
/*   36 */   private String signature = null;
/*   37 */   private String genericSignature = null;
/*      */   private boolean genericSignatureGotten = false;
/*   39 */   private String baseSourceName = null;
/*   40 */   private String baseSourceDir = null;
/*   41 */   private String baseSourcePath = null;
/*   42 */   protected int modifiers = -1;
/*   43 */   private SoftReference<List<Field>> fieldsRef = null;
/*   44 */   private SoftReference<List<Method>> methodsRef = null;
/*   45 */   private SoftReference<SDE> sdeRef = null;
/*      */   
/*      */   private boolean isClassLoaderCached = false;
/*   48 */   private ClassLoaderReference classLoader = null;
/*   49 */   private ClassObjectReference classObject = null;
/*      */   
/*   51 */   private int status = 0;
/*      */   
/*      */   private boolean isPrepared = false;
/*      */   
/*      */   private boolean versionNumberGotten = false;
/*      */   
/*      */   private int majorVersion;
/*      */   private int minorVersion;
/*      */   private boolean constantPoolInfoGotten = false;
/*      */   private int constanPoolCount;
/*      */   private byte[] constantPoolBytes;
/*   62 */   private SoftReference<byte[]> constantPoolBytesRef = null;
/*      */ 
/*      */   
/*      */   private static final String ABSENT_BASE_SOURCE_NAME = "**ABSENT_BASE_SOURCE_NAME**";
/*      */ 
/*      */   
/*   68 */   static final SDE NO_SDE_INFO_MARK = new SDE();
/*      */ 
/*      */   
/*      */   private static final int INITIALIZED_OR_FAILED = 12;
/*      */ 
/*      */ 
/*      */   
/*      */   protected ReferenceTypeImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*   76 */     super(paramVirtualMachine);
/*   77 */     this.ref = paramLong;
/*   78 */     this.genericSignatureGotten = false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void noticeRedefineClass() {
/*   84 */     this.baseSourceName = null;
/*   85 */     this.baseSourcePath = null;
/*   86 */     this.modifiers = -1;
/*   87 */     this.fieldsRef = null;
/*   88 */     this.methodsRef = null;
/*   89 */     this.sdeRef = null;
/*   90 */     this.versionNumberGotten = false;
/*   91 */     this.constantPoolInfoGotten = false;
/*      */   }
/*      */   
/*      */   Method getMethodMirror(long paramLong) {
/*   95 */     if (paramLong == 0L)
/*      */     {
/*   97 */       return new ObsoleteMethodImpl((VirtualMachine)this.vm, this);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  102 */     Iterator<Method> iterator = methods().iterator();
/*  103 */     while (iterator.hasNext()) {
/*  104 */       MethodImpl methodImpl = (MethodImpl)iterator.next();
/*  105 */       if (methodImpl.ref() == paramLong) {
/*  106 */         return methodImpl;
/*      */       }
/*      */     } 
/*  109 */     throw new IllegalArgumentException("Invalid method id: " + paramLong);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Field getFieldMirror(long paramLong) {
/*  116 */     Iterator<Field> iterator = fields().iterator();
/*  117 */     while (iterator.hasNext()) {
/*  118 */       FieldImpl fieldImpl = (FieldImpl)iterator.next();
/*  119 */       if (fieldImpl.ref() == paramLong) {
/*  120 */         return fieldImpl;
/*      */       }
/*      */     } 
/*  123 */     throw new IllegalArgumentException("Invalid field id: " + paramLong);
/*      */   }
/*      */   
/*      */   public boolean equals(Object paramObject) {
/*  127 */     if (paramObject != null && paramObject instanceof ReferenceTypeImpl) {
/*  128 */       ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)paramObject;
/*  129 */       return (ref() == referenceTypeImpl.ref() && this.vm
/*  130 */         .equals(referenceTypeImpl.virtualMachine()));
/*      */     } 
/*  132 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  137 */     return (int)ref();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int compareTo(ReferenceType paramReferenceType) {
/*  149 */     ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)paramReferenceType;
/*  150 */     int i = name().compareTo(referenceTypeImpl.name());
/*  151 */     if (i == 0) {
/*  152 */       long l1 = ref();
/*  153 */       long l2 = referenceTypeImpl.ref();
/*      */       
/*  155 */       if (l1 == l2) {
/*      */ 
/*      */         
/*  158 */         i = this.vm.sequenceNumber - ((VirtualMachineImpl)referenceTypeImpl.virtualMachine()).sequenceNumber;
/*      */       } else {
/*  160 */         i = (l1 < l2) ? -1 : 1;
/*      */       } 
/*      */     } 
/*  163 */     return i;
/*      */   }
/*      */   
/*      */   public String signature() {
/*  167 */     if (this.signature == null)
/*      */     {
/*      */       
/*  170 */       if (this.vm.canGet1_5LanguageFeatures()) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  175 */         genericSignature();
/*      */       } else {
/*      */         try {
/*  178 */           this
/*  179 */             .signature = (JDWP.ReferenceType.Signature.process(this.vm, this)).signature;
/*  180 */         } catch (JDWPException jDWPException) {
/*  181 */           throw jDWPException.toJDIException();
/*      */         } 
/*      */       } 
/*      */     }
/*  185 */     return this.signature;
/*      */   }
/*      */ 
/*      */   
/*      */   public String genericSignature() {
/*  190 */     if (this.vm.canGet1_5LanguageFeatures() && !this.genericSignatureGotten) {
/*      */       JDWP.ReferenceType.SignatureWithGeneric signatureWithGeneric;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  196 */         signatureWithGeneric = JDWP.ReferenceType.SignatureWithGeneric.process(this.vm, this);
/*  197 */       } catch (JDWPException jDWPException) {
/*  198 */         throw jDWPException.toJDIException();
/*      */       } 
/*  200 */       this.signature = signatureWithGeneric.signature;
/*  201 */       setGenericSignature(signatureWithGeneric.genericSignature);
/*      */     } 
/*  203 */     return this.genericSignature;
/*      */   }
/*      */   
/*      */   public ClassLoaderReference classLoader() {
/*  207 */     if (!this.isClassLoaderCached) {
/*      */       
/*      */       try {
/*      */         
/*  211 */         this
/*      */           
/*  213 */           .classLoader = (JDWP.ReferenceType.ClassLoader.process(this.vm, this)).classLoader;
/*  214 */         this.isClassLoaderCached = true;
/*  215 */       } catch (JDWPException jDWPException) {
/*  216 */         throw jDWPException.toJDIException();
/*      */       } 
/*      */     }
/*  219 */     return this.classLoader;
/*      */   }
/*      */   
/*      */   public boolean isPublic() {
/*  223 */     if (this.modifiers == -1) {
/*  224 */       getModifiers();
/*      */     }
/*  226 */     return ((this.modifiers & 0x1) > 0);
/*      */   }
/*      */   
/*      */   public boolean isProtected() {
/*  230 */     if (this.modifiers == -1) {
/*  231 */       getModifiers();
/*      */     }
/*  233 */     return ((this.modifiers & 0x4) > 0);
/*      */   }
/*      */   
/*      */   public boolean isPrivate() {
/*  237 */     if (this.modifiers == -1) {
/*  238 */       getModifiers();
/*      */     }
/*  240 */     return ((this.modifiers & 0x2) > 0);
/*      */   }
/*      */   
/*      */   public boolean isPackagePrivate() {
/*  244 */     return (!isPublic() && !isPrivate() && !isProtected());
/*      */   }
/*      */   
/*      */   public boolean isAbstract() {
/*  248 */     if (this.modifiers == -1) {
/*  249 */       getModifiers();
/*      */     }
/*  251 */     return ((this.modifiers & 0x400) > 0);
/*      */   }
/*      */   
/*      */   public boolean isFinal() {
/*  255 */     if (this.modifiers == -1) {
/*  256 */       getModifiers();
/*      */     }
/*  258 */     return ((this.modifiers & 0x10) > 0);
/*      */   }
/*      */   
/*      */   public boolean isStatic() {
/*  262 */     if (this.modifiers == -1) {
/*  263 */       getModifiers();
/*      */     }
/*  265 */     return ((this.modifiers & 0x8) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrepared() {
/*  273 */     if (this.status == 0) {
/*  274 */       updateStatus();
/*      */     }
/*  276 */     return this.isPrepared;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isVerified() {
/*  281 */     if ((this.status & 0x1) == 0) {
/*  282 */       updateStatus();
/*      */     }
/*  284 */     return ((this.status & 0x1) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInitialized() {
/*  290 */     if ((this.status & 0xC) == 0) {
/*  291 */       updateStatus();
/*      */     }
/*  293 */     return ((this.status & 0x4) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean failedToInitialize() {
/*  299 */     if ((this.status & 0xC) == 0) {
/*  300 */       updateStatus();
/*      */     }
/*  302 */     return ((this.status & 0x8) != 0);
/*      */   }
/*      */   
/*      */   public List<Field> fields() {
/*  306 */     List<FieldImpl> list = (this.fieldsRef == null) ? null : this.fieldsRef.get();
/*  307 */     if (list == null) {
/*  308 */       if (this.vm.canGet1_5LanguageFeatures()) {
/*      */         JDWP.ReferenceType.FieldsWithGeneric.FieldInfo[] arrayOfFieldInfo;
/*      */         try {
/*  311 */           arrayOfFieldInfo = (JDWP.ReferenceType.FieldsWithGeneric.process(this.vm, this)).declared;
/*  312 */         } catch (JDWPException jDWPException) {
/*  313 */           throw jDWPException.toJDIException();
/*      */         } 
/*  315 */         list = new ArrayList(arrayOfFieldInfo.length);
/*  316 */         for (byte b = 0; b < arrayOfFieldInfo.length; b++) {
/*  317 */           JDWP.ReferenceType.FieldsWithGeneric.FieldInfo fieldInfo = arrayOfFieldInfo[b];
/*      */ 
/*      */           
/*  320 */           FieldImpl fieldImpl = new FieldImpl((VirtualMachine)this.vm, this, fieldInfo.fieldID, fieldInfo.name, fieldInfo.signature, fieldInfo.genericSignature, fieldInfo.modBits);
/*      */ 
/*      */ 
/*      */           
/*  324 */           list.add(fieldImpl);
/*      */         } 
/*      */       } else {
/*      */         JDWP.ReferenceType.Fields.FieldInfo[] arrayOfFieldInfo;
/*      */         
/*      */         try {
/*  330 */           arrayOfFieldInfo = (JDWP.ReferenceType.Fields.process(this.vm, this)).declared;
/*  331 */         } catch (JDWPException jDWPException) {
/*  332 */           throw jDWPException.toJDIException();
/*      */         } 
/*  334 */         list = new ArrayList<>(arrayOfFieldInfo.length);
/*  335 */         for (byte b = 0; b < arrayOfFieldInfo.length; b++) {
/*  336 */           JDWP.ReferenceType.Fields.FieldInfo fieldInfo = arrayOfFieldInfo[b];
/*      */           
/*  338 */           FieldImpl fieldImpl = new FieldImpl((VirtualMachine)this.vm, this, fieldInfo.fieldID, fieldInfo.name, fieldInfo.signature, null, fieldInfo.modBits);
/*      */ 
/*      */ 
/*      */           
/*  342 */           list.add(fieldImpl);
/*      */         } 
/*      */       } 
/*      */       
/*  346 */       list = Collections.unmodifiableList(list);
/*  347 */       this.fieldsRef = new SoftReference(list);
/*      */     } 
/*  349 */     return (List)list;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void addVisibleFields(List<Field> paramList, Map<String, Field> paramMap, List<String> paramList1) {
/*  355 */     for (Field field : visibleFields()) {
/*  356 */       String str = field.name();
/*  357 */       if (!paramList1.contains(str)) {
/*  358 */         Field field1 = paramMap.get(str);
/*  359 */         if (field1 == null) {
/*  360 */           paramList.add(field);
/*  361 */           paramMap.put(str, field); continue;
/*  362 */         }  if (!field.equals(field1)) {
/*  363 */           paramList1.add(str);
/*  364 */           paramMap.remove(str);
/*  365 */           paramList.remove(field1);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Field> visibleFields() {
/*  380 */     ArrayList<Field> arrayList1 = new ArrayList();
/*  381 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*      */ 
/*      */     
/*  384 */     ArrayList<String> arrayList = new ArrayList();
/*      */ 
/*      */     
/*  387 */     List<? extends ReferenceType> list = inheritedTypes();
/*  388 */     Iterator<? extends ReferenceType> iterator = list.iterator();
/*  389 */     while (iterator.hasNext()) {
/*      */ 
/*      */ 
/*      */       
/*  393 */       ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)iterator.next();
/*  394 */       referenceTypeImpl.addVisibleFields(arrayList1, (Map)hashMap, arrayList);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  401 */     ArrayList<Field> arrayList2 = new ArrayList<>(fields());
/*  402 */     for (Field field1 : arrayList2) {
/*  403 */       Field field2 = (Field)hashMap.get(field1.name());
/*  404 */       if (field2 != null) {
/*  405 */         arrayList1.remove(field2);
/*      */       }
/*      */     } 
/*  408 */     arrayList2.addAll(arrayList1);
/*  409 */     return arrayList2;
/*      */   }
/*      */ 
/*      */   
/*      */   void addAllFields(List<Field> paramList, Set<ReferenceType> paramSet) {
/*  414 */     if (!paramSet.contains(this)) {
/*  415 */       paramSet.add(this);
/*      */ 
/*      */       
/*  418 */       paramList.addAll(fields());
/*      */ 
/*      */       
/*  421 */       List<? extends ReferenceType> list = inheritedTypes();
/*  422 */       Iterator<? extends ReferenceType> iterator = list.iterator();
/*  423 */       while (iterator.hasNext()) {
/*  424 */         ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)iterator.next();
/*  425 */         referenceTypeImpl.addAllFields(paramList, paramSet);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   public List<Field> allFields() {
/*  430 */     ArrayList<Field> arrayList = new ArrayList();
/*  431 */     HashSet<ReferenceType> hashSet = new HashSet();
/*  432 */     addAllFields(arrayList, hashSet);
/*  433 */     return arrayList;
/*      */   }
/*      */   
/*      */   public Field fieldByName(String paramString) {
/*  437 */     List<Field> list = visibleFields();
/*      */     
/*  439 */     for (byte b = 0; b < list.size(); b++) {
/*  440 */       Field field = list.get(b);
/*      */       
/*  442 */       if (field.name().equals(paramString)) {
/*  443 */         return field;
/*      */       }
/*      */     } 
/*      */     
/*  447 */     return null;
/*      */   }
/*      */   
/*      */   public List<Method> methods() {
/*  451 */     List<Method> list = (this.methodsRef == null) ? null : this.methodsRef.get();
/*  452 */     if (list == null) {
/*  453 */       if (!this.vm.canGet1_5LanguageFeatures()) {
/*  454 */         list = methods1_4();
/*      */       } else {
/*      */         JDWP.ReferenceType.MethodsWithGeneric.MethodInfo[] arrayOfMethodInfo;
/*      */         
/*      */         try {
/*  459 */           arrayOfMethodInfo = (JDWP.ReferenceType.MethodsWithGeneric.process(this.vm, this)).declared;
/*  460 */         } catch (JDWPException jDWPException) {
/*  461 */           throw jDWPException.toJDIException();
/*      */         } 
/*  463 */         list = new ArrayList<>(arrayOfMethodInfo.length);
/*  464 */         for (byte b = 0; b < arrayOfMethodInfo.length; b++) {
/*      */           
/*  466 */           JDWP.ReferenceType.MethodsWithGeneric.MethodInfo methodInfo = arrayOfMethodInfo[b];
/*      */           
/*  468 */           MethodImpl methodImpl = MethodImpl.createMethodImpl((VirtualMachine)this.vm, this, methodInfo.methodID, methodInfo.name, methodInfo.signature, methodInfo.genericSignature, methodInfo.modBits);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  473 */           list.add(methodImpl);
/*      */         } 
/*      */       } 
/*  476 */       list = Collections.unmodifiableList(list);
/*  477 */       this.methodsRef = new SoftReference<>(list);
/*      */     } 
/*  479 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Method> methods1_4() {
/*      */     JDWP.ReferenceType.Methods.MethodInfo[] arrayOfMethodInfo;
/*      */     try {
/*  487 */       arrayOfMethodInfo = (JDWP.ReferenceType.Methods.process(this.vm, this)).declared;
/*  488 */     } catch (JDWPException jDWPException) {
/*  489 */       throw jDWPException.toJDIException();
/*      */     } 
/*  491 */     ArrayList<MethodImpl> arrayList = new ArrayList(arrayOfMethodInfo.length);
/*  492 */     for (byte b = 0; b < arrayOfMethodInfo.length; b++) {
/*  493 */       JDWP.ReferenceType.Methods.MethodInfo methodInfo = arrayOfMethodInfo[b];
/*      */       
/*  495 */       MethodImpl methodImpl = MethodImpl.createMethodImpl((VirtualMachine)this.vm, this, methodInfo.methodID, methodInfo.name, methodInfo.signature, null, methodInfo.modBits);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  500 */       arrayList.add(methodImpl);
/*      */     } 
/*  502 */     return (List)arrayList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void addToMethodMap(Map<String, Method> paramMap, List<Method> paramList) {
/*  510 */     for (Method method : paramList) {
/*  511 */       paramMap.put(method.name().concat(method.signature()), method);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Method> visibleMethods() {
/*  522 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*  523 */     addVisibleMethods((Map)hashMap, new HashSet<>());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  531 */     List<Method> list = allMethods();
/*  532 */     list.retainAll(hashMap.values());
/*  533 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Method> methodsByName(String paramString) {
/*  539 */     List<Method> list = visibleMethods();
/*  540 */     ArrayList<Method> arrayList = new ArrayList(list.size());
/*  541 */     for (Method method : list) {
/*  542 */       if (method.name().equals(paramString)) {
/*  543 */         arrayList.add(method);
/*      */       }
/*      */     } 
/*  546 */     arrayList.trimToSize();
/*  547 */     return arrayList;
/*      */   }
/*      */   
/*      */   public List<Method> methodsByName(String paramString1, String paramString2) {
/*  551 */     List<Method> list = visibleMethods();
/*  552 */     ArrayList<Method> arrayList = new ArrayList(list.size());
/*  553 */     for (Method method : list) {
/*  554 */       if (method.name().equals(paramString1) && method
/*  555 */         .signature().equals(paramString2)) {
/*  556 */         arrayList.add(method);
/*      */       }
/*      */     } 
/*  559 */     arrayList.trimToSize();
/*  560 */     return arrayList;
/*      */   }
/*      */ 
/*      */   
/*      */   List<InterfaceType> getInterfaces() {
/*      */     InterfaceTypeImpl[] arrayOfInterfaceTypeImpl;
/*      */     try {
/*  567 */       arrayOfInterfaceTypeImpl = (JDWP.ReferenceType.Interfaces.process(this.vm, this)).interfaces;
/*  568 */     } catch (JDWPException jDWPException) {
/*  569 */       throw jDWPException.toJDIException();
/*      */     } 
/*  571 */     return Arrays.asList((InterfaceType[])arrayOfInterfaceTypeImpl);
/*      */   }
/*      */   
/*      */   public List<ReferenceType> nestedTypes() {
/*  575 */     List<ReferenceType> list = this.vm.allClasses();
/*  576 */     ArrayList<ReferenceType> arrayList = new ArrayList();
/*  577 */     String str = name();
/*  578 */     int i = str.length();
/*  579 */     Iterator<ReferenceType> iterator = list.iterator();
/*  580 */     while (iterator.hasNext()) {
/*  581 */       ReferenceType referenceType = iterator.next();
/*  582 */       String str1 = referenceType.name();
/*  583 */       int j = str1.length();
/*      */       
/*  585 */       if (j > i && str1.startsWith(str)) {
/*  586 */         char c = str1.charAt(i);
/*  587 */         if (c == '$' || c == '#') {
/*  588 */           arrayList.add(referenceType);
/*      */         }
/*      */       } 
/*      */     } 
/*  592 */     return arrayList;
/*      */   }
/*      */   
/*      */   public Value getValue(Field paramField) {
/*  596 */     ArrayList<Field> arrayList = new ArrayList(1);
/*  597 */     arrayList.add(paramField);
/*  598 */     Map<Field, Value> map = getValues(arrayList);
/*  599 */     return map.get(paramField);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void validateFieldAccess(Field paramField) {
/*  608 */     ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)paramField.declaringType();
/*  609 */     if (!referenceTypeImpl.isAssignableFrom(this)) {
/*  610 */       throw new IllegalArgumentException("Invalid field");
/*      */     }
/*      */   }
/*      */   
/*      */   void validateFieldSet(Field paramField) {
/*  615 */     validateFieldAccess(paramField);
/*  616 */     if (paramField.isFinal()) {
/*  617 */       throw new IllegalArgumentException("Cannot set value of final field");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Field, Value> getValues(List<? extends Field> paramList) {
/*      */     ValueImpl[] arrayOfValueImpl;
/*  625 */     validateMirrors((Collection)paramList);
/*      */     
/*  627 */     int i = paramList.size();
/*  628 */     JDWP.ReferenceType.GetValues.Field[] arrayOfField = new JDWP.ReferenceType.GetValues.Field[i];
/*      */ 
/*      */     
/*  631 */     for (byte b1 = 0; b1 < i; b1++) {
/*  632 */       FieldImpl fieldImpl = (FieldImpl)paramList.get(b1);
/*      */       
/*  634 */       validateFieldAccess(fieldImpl);
/*      */ 
/*      */       
/*  637 */       if (!fieldImpl.isStatic()) {
/*  638 */         throw new IllegalArgumentException("Attempt to use non-static field with ReferenceType");
/*      */       }
/*      */       
/*  641 */       arrayOfField[b1] = new JDWP.ReferenceType.GetValues.Field(fieldImpl
/*  642 */           .ref());
/*      */     } 
/*      */     
/*  645 */     HashMap<Object, Object> hashMap = new HashMap<>(i);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  650 */       arrayOfValueImpl = (JDWP.ReferenceType.GetValues.process(this.vm, this, arrayOfField)).values;
/*  651 */     } catch (JDWPException jDWPException) {
/*  652 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */     
/*  655 */     if (i != arrayOfValueImpl.length) {
/*  656 */       throw new InternalException("Wrong number of values returned from target VM");
/*      */     }
/*      */     
/*  659 */     for (byte b2 = 0; b2 < i; b2++) {
/*  660 */       FieldImpl fieldImpl = (FieldImpl)paramList.get(b2);
/*  661 */       hashMap.put(fieldImpl, arrayOfValueImpl[b2]);
/*      */     } 
/*      */     
/*  664 */     return (Map)hashMap;
/*      */   }
/*      */   
/*      */   public ClassObjectReference classObject() {
/*  668 */     if (this.classObject == null)
/*      */     {
/*      */       
/*  671 */       synchronized (this) {
/*  672 */         if (this.classObject == null) {
/*      */           try {
/*  674 */             this
/*  675 */               .classObject = (JDWP.ReferenceType.ClassObject.process(this.vm, this)).classObject;
/*  676 */           } catch (JDWPException jDWPException) {
/*  677 */             throw jDWPException.toJDIException();
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*  682 */     return this.classObject;
/*      */   }
/*      */   
/*      */   SDE.Stratum stratum(String paramString) {
/*  686 */     SDE sDE = sourceDebugExtensionInfo();
/*  687 */     if (!sDE.isValid()) {
/*  688 */       sDE = NO_SDE_INFO_MARK;
/*      */     }
/*  690 */     return sDE.stratum(paramString);
/*      */   }
/*      */   
/*      */   public String sourceName() throws AbsentInformationException {
/*  694 */     return sourceNames(this.vm.getDefaultStratum()).get(0);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> sourceNames(String paramString) throws AbsentInformationException {
/*  699 */     SDE.Stratum stratum = stratum(paramString);
/*  700 */     if (stratum.isJava()) {
/*  701 */       ArrayList<String> arrayList = new ArrayList(1);
/*  702 */       arrayList.add(baseSourceName());
/*  703 */       return arrayList;
/*      */     } 
/*  705 */     return stratum.sourceNames(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> sourcePaths(String paramString) throws AbsentInformationException {
/*  710 */     SDE.Stratum stratum = stratum(paramString);
/*  711 */     if (stratum.isJava()) {
/*  712 */       ArrayList<String> arrayList = new ArrayList(1);
/*  713 */       arrayList.add(baseSourceDir() + baseSourceName());
/*  714 */       return arrayList;
/*      */     } 
/*  716 */     return stratum.sourcePaths(this);
/*      */   }
/*      */   
/*      */   String baseSourceName() throws AbsentInformationException {
/*  720 */     String str = this.baseSourceName;
/*  721 */     if (str == null) {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/*  726 */         str = (JDWP.ReferenceType.SourceFile.process(this.vm, this)).sourceFile;
/*  727 */       } catch (JDWPException jDWPException) {
/*  728 */         if (jDWPException.errorCode() == 101) {
/*  729 */           str = "**ABSENT_BASE_SOURCE_NAME**";
/*      */         } else {
/*  731 */           throw jDWPException.toJDIException();
/*      */         } 
/*      */       } 
/*  734 */       this.baseSourceName = str;
/*      */     } 
/*  736 */     if (str == "**ABSENT_BASE_SOURCE_NAME**") {
/*  737 */       throw new AbsentInformationException();
/*      */     }
/*  739 */     return str;
/*      */   }
/*      */   
/*      */   String baseSourcePath() throws AbsentInformationException {
/*  743 */     String str = this.baseSourcePath;
/*  744 */     if (str == null) {
/*  745 */       str = baseSourceDir() + baseSourceName();
/*  746 */       this.baseSourcePath = str;
/*      */     } 
/*  748 */     return str;
/*      */   }
/*      */   
/*      */   String baseSourceDir() {
/*  752 */     if (this.baseSourceDir == null) {
/*  753 */       String str = name();
/*  754 */       StringBuffer stringBuffer = new StringBuffer(str.length() + 10);
/*  755 */       int i = 0;
/*      */       
/*      */       int j;
/*  758 */       while ((j = str.indexOf('.', i)) > 0) {
/*  759 */         stringBuffer.append(str.substring(i, j));
/*  760 */         stringBuffer.append(File.separatorChar);
/*  761 */         i = j + 1;
/*      */       } 
/*  763 */       this.baseSourceDir = stringBuffer.toString();
/*      */     } 
/*  765 */     return this.baseSourceDir;
/*      */   }
/*      */ 
/*      */   
/*      */   public String sourceDebugExtension() throws AbsentInformationException {
/*  770 */     if (!this.vm.canGetSourceDebugExtension()) {
/*  771 */       throw new UnsupportedOperationException();
/*      */     }
/*  773 */     SDE sDE = sourceDebugExtensionInfo();
/*  774 */     if (sDE == NO_SDE_INFO_MARK) {
/*  775 */       throw new AbsentInformationException();
/*      */     }
/*  777 */     return sDE.sourceDebugExtension;
/*      */   }
/*      */   
/*      */   private SDE sourceDebugExtensionInfo() {
/*  781 */     if (!this.vm.canGetSourceDebugExtension()) {
/*  782 */       return NO_SDE_INFO_MARK;
/*      */     }
/*  784 */     SDE sDE = (this.sdeRef == null) ? null : this.sdeRef.get();
/*  785 */     if (sDE == null) {
/*  786 */       String str = null;
/*      */       
/*      */       try {
/*  789 */         str = (JDWP.ReferenceType.SourceDebugExtension.process(this.vm, this)).extension;
/*  790 */       } catch (JDWPException jDWPException) {
/*  791 */         if (jDWPException.errorCode() != 101) {
/*  792 */           this.sdeRef = new SoftReference<>(NO_SDE_INFO_MARK);
/*  793 */           throw jDWPException.toJDIException();
/*      */         } 
/*      */       } 
/*  796 */       if (str == null) {
/*  797 */         sDE = NO_SDE_INFO_MARK;
/*      */       } else {
/*  799 */         sDE = new SDE(str);
/*      */       } 
/*  801 */       this.sdeRef = new SoftReference<>(sDE);
/*      */     } 
/*  803 */     return sDE;
/*      */   }
/*      */   
/*      */   public List<String> availableStrata() {
/*  807 */     SDE sDE = sourceDebugExtensionInfo();
/*  808 */     if (sDE.isValid()) {
/*  809 */       return sDE.availableStrata();
/*      */     }
/*  811 */     ArrayList<String> arrayList = new ArrayList();
/*  812 */     arrayList.add("Java");
/*  813 */     return arrayList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String defaultStratum() {
/*  821 */     SDE sDE = sourceDebugExtensionInfo();
/*  822 */     if (sDE.isValid()) {
/*  823 */       return sDE.defaultStratumId;
/*      */     }
/*  825 */     return "Java";
/*      */   }
/*      */ 
/*      */   
/*      */   public int modifiers() {
/*  830 */     if (this.modifiers == -1) {
/*  831 */       getModifiers();
/*      */     }
/*  833 */     return this.modifiers;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Location> allLineLocations() throws AbsentInformationException {
/*  838 */     return allLineLocations(this.vm.getDefaultStratum(), (String)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Location> allLineLocations(String paramString1, String paramString2) throws AbsentInformationException {
/*  843 */     boolean bool = false;
/*  844 */     SDE.Stratum stratum = stratum(paramString1);
/*  845 */     ArrayList<Location> arrayList = new ArrayList();
/*      */     
/*  847 */     for (MethodImpl methodImpl : methods()) {
/*      */       
/*      */       try {
/*  850 */         arrayList.addAll(methodImpl
/*  851 */             .allLineLocations(stratum, paramString2));
/*  852 */       } catch (AbsentInformationException absentInformationException) {
/*  853 */         bool = true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  861 */     if (bool && arrayList.size() == 0) {
/*  862 */       throw new AbsentInformationException();
/*      */     }
/*  864 */     return arrayList;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Location> locationsOfLine(int paramInt) throws AbsentInformationException {
/*  869 */     return locationsOfLine(this.vm.getDefaultStratum(), (String)null, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Location> locationsOfLine(String paramString1, String paramString2, int paramInt) throws AbsentInformationException {
/*  879 */     boolean bool1 = false;
/*      */     
/*  881 */     boolean bool2 = false;
/*  882 */     List<Method> list = methods();
/*  883 */     SDE.Stratum stratum = stratum(paramString1);
/*      */     
/*  885 */     ArrayList<Location> arrayList = new ArrayList();
/*      */     
/*  887 */     Iterator<Method> iterator = list.iterator();
/*  888 */     while (iterator.hasNext()) {
/*  889 */       MethodImpl methodImpl = (MethodImpl)iterator.next();
/*      */ 
/*      */       
/*  892 */       if (!methodImpl.isAbstract() && 
/*  893 */         !methodImpl.isNative()) {
/*      */         try {
/*  895 */           arrayList.addAll(methodImpl
/*  896 */               .locationsOfLine(stratum, paramString2, paramInt));
/*      */ 
/*      */           
/*  899 */           bool2 = true;
/*  900 */         } catch (AbsentInformationException absentInformationException) {
/*  901 */           bool1 = true;
/*      */         } 
/*      */       }
/*      */     } 
/*  905 */     if (bool1 && !bool2) {
/*  906 */       throw new AbsentInformationException();
/*      */     }
/*  908 */     return arrayList;
/*      */   }
/*      */   
/*      */   public List<ObjectReference> instances(long paramLong) {
/*  912 */     if (!this.vm.canGetInstanceInfo()) {
/*  913 */       throw new UnsupportedOperationException("target does not support getting instances");
/*      */     }
/*      */ 
/*      */     
/*  917 */     if (paramLong < 0L) {
/*  918 */       throw new IllegalArgumentException("maxInstances is less than zero: " + paramLong);
/*      */     }
/*      */     
/*  921 */     int i = (paramLong > 2147483647L) ? Integer.MAX_VALUE : (int)paramLong;
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  926 */       return Arrays.asList(
/*      */           
/*  928 */           (ObjectReference[])(JDWP.ReferenceType.Instances.process(this.vm, this, i)).instances);
/*  929 */     } catch (JDWPException jDWPException) {
/*  930 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */   }
/*      */   private void getClassFileVersion() {
/*      */     JDWP.ReferenceType.ClassFileVersion classFileVersion;
/*  935 */     if (!this.vm.canGetClassFileVersion()) {
/*  936 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*  939 */     if (this.versionNumberGotten) {
/*      */       return;
/*      */     }
/*      */     try {
/*  943 */       classFileVersion = JDWP.ReferenceType.ClassFileVersion.process(this.vm, this);
/*  944 */     } catch (JDWPException jDWPException) {
/*  945 */       if (jDWPException.errorCode() == 101) {
/*  946 */         this.majorVersion = 0;
/*  947 */         this.minorVersion = 0;
/*  948 */         this.versionNumberGotten = true;
/*      */         return;
/*      */       } 
/*  951 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */     
/*  954 */     this.majorVersion = classFileVersion.majorVersion;
/*  955 */     this.minorVersion = classFileVersion.minorVersion;
/*  956 */     this.versionNumberGotten = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int majorVersion() {
/*      */     try {
/*  962 */       getClassFileVersion();
/*  963 */     } catch (RuntimeException runtimeException) {
/*  964 */       throw runtimeException;
/*      */     } 
/*  966 */     return this.majorVersion;
/*      */   }
/*      */   
/*      */   public int minorVersion() {
/*      */     try {
/*  971 */       getClassFileVersion();
/*  972 */     } catch (RuntimeException runtimeException) {
/*  973 */       throw runtimeException;
/*      */     } 
/*  975 */     return this.minorVersion;
/*      */   }
/*      */   
/*      */   private void getConstantPoolInfo() {
/*      */     JDWP.ReferenceType.ConstantPool constantPool;
/*  980 */     if (!this.vm.canGetConstantPool()) {
/*  981 */       throw new UnsupportedOperationException();
/*      */     }
/*  983 */     if (this.constantPoolInfoGotten) {
/*      */       return;
/*      */     }
/*      */     try {
/*  987 */       constantPool = JDWP.ReferenceType.ConstantPool.process(this.vm, this);
/*  988 */     } catch (JDWPException jDWPException) {
/*  989 */       if (jDWPException.errorCode() == 101) {
/*  990 */         this.constanPoolCount = 0;
/*  991 */         this.constantPoolBytesRef = null;
/*  992 */         this.constantPoolInfoGotten = true;
/*      */         return;
/*      */       } 
/*  995 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */ 
/*      */     
/*  999 */     this.constanPoolCount = constantPool.count;
/* 1000 */     byte[] arrayOfByte = constantPool.bytes;
/* 1001 */     this.constantPoolBytesRef = (SoftReference)new SoftReference<>(arrayOfByte);
/* 1002 */     this.constantPoolInfoGotten = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int constantPoolCount() {
/*      */     try {
/* 1008 */       getConstantPoolInfo();
/* 1009 */     } catch (RuntimeException runtimeException) {
/* 1010 */       throw runtimeException;
/*      */     } 
/* 1012 */     return this.constanPoolCount;
/*      */   }
/*      */   
/*      */   public byte[] constantPool() {
/*      */     try {
/* 1017 */       getConstantPoolInfo();
/* 1018 */     } catch (RuntimeException runtimeException) {
/* 1019 */       throw runtimeException;
/*      */     } 
/* 1021 */     if (this.constantPoolBytesRef != null) {
/* 1022 */       byte[] arrayOfByte = this.constantPoolBytesRef.get();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1028 */       return (byte[])arrayOfByte.clone();
/*      */     } 
/* 1030 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void getModifiers() {
/* 1037 */     if (this.modifiers != -1) {
/*      */       return;
/*      */     }
/*      */     try {
/* 1041 */       this
/* 1042 */         .modifiers = (JDWP.ReferenceType.Modifiers.process(this.vm, this)).modBits;
/* 1043 */     } catch (JDWPException jDWPException) {
/* 1044 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */   }
/*      */   
/*      */   void decodeStatus(int paramInt) {
/* 1049 */     this.status = paramInt;
/* 1050 */     if ((paramInt & 0x2) != 0) {
/* 1051 */       this.isPrepared = true;
/*      */     }
/*      */   }
/*      */   
/*      */   void updateStatus() {
/*      */     try {
/* 1057 */       decodeStatus((JDWP.ReferenceType.Status.process(this.vm, this)).status);
/* 1058 */     } catch (JDWPException jDWPException) {
/* 1059 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */   }
/*      */   
/*      */   void markPrepared() {
/* 1064 */     this.isPrepared = true;
/*      */   }
/*      */   
/*      */   long ref() {
/* 1068 */     return this.ref;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   int indexOf(Method paramMethod) {
/* 1074 */     return methods().indexOf(paramMethod);
/*      */   }
/*      */ 
/*      */   
/*      */   int indexOf(Field paramField) {
/* 1079 */     return fields().indexOf(paramField);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isAssignableFrom(ReferenceType paramReferenceType) {
/* 1089 */     return ((ReferenceTypeImpl)paramReferenceType).isAssignableTo(this);
/*      */   }
/*      */   
/*      */   boolean isAssignableFrom(ObjectReference paramObjectReference) {
/* 1093 */     return (paramObjectReference == null || 
/* 1094 */       isAssignableFrom(paramObjectReference.referenceType()));
/*      */   }
/*      */   
/*      */   void setStatus(int paramInt) {
/* 1098 */     decodeStatus(paramInt);
/*      */   }
/*      */   
/*      */   void setSignature(String paramString) {
/* 1102 */     this.signature = paramString;
/*      */   }
/*      */   
/*      */   void setGenericSignature(String paramString) {
/* 1106 */     if (paramString != null && paramString.length() == 0) {
/* 1107 */       this.genericSignature = null;
/*      */     } else {
/* 1109 */       this.genericSignature = paramString;
/*      */     } 
/* 1111 */     this.genericSignatureGotten = true;
/*      */   }
/*      */   private static boolean isOneDimensionalPrimitiveArray(String paramString) {
/*      */     boolean bool;
/* 1115 */     int i = paramString.lastIndexOf('[');
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1123 */     if (i < 0 || paramString.startsWith("[[")) {
/* 1124 */       bool = false;
/*      */     } else {
/* 1126 */       char c = paramString.charAt(i + 1);
/* 1127 */       bool = (c != 'L') ? true : false;
/*      */     } 
/* 1129 */     return bool;
/*      */   }
/*      */   
/*      */   Type findType(String paramString) throws ClassNotLoadedException {
/*      */     Type type;
/* 1134 */     if (paramString.length() == 1) {
/*      */       
/* 1136 */       char c = paramString.charAt(0);
/* 1137 */       if (c == 'V') {
/* 1138 */         VoidType voidType = this.vm.theVoidType();
/*      */       } else {
/* 1140 */         PrimitiveType primitiveType = this.vm.primitiveTypeMirror((byte)c);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1145 */       ClassLoaderReferenceImpl classLoaderReferenceImpl = (ClassLoaderReferenceImpl)classLoader();
/* 1146 */       if (classLoaderReferenceImpl == null || 
/* 1147 */         isOneDimensionalPrimitiveArray(paramString)) {
/*      */ 
/*      */         
/* 1150 */         type = this.vm.findBootType(paramString);
/*      */       } else {
/*      */         
/* 1153 */         type = classLoaderReferenceImpl.findType(paramString);
/*      */       } 
/*      */     } 
/* 1156 */     return type;
/*      */   }
/*      */   
/*      */   String loaderString() {
/* 1160 */     if (classLoader() != null) {
/* 1161 */       return "loaded by " + classLoader().toString();
/*      */     }
/* 1163 */     return "no class loader";
/*      */   }
/*      */   
/*      */   abstract List<? extends ReferenceType> inheritedTypes();
/*      */   
/*      */   abstract void addVisibleMethods(Map<String, Method> paramMap, Set<InterfaceType> paramSet);
/*      */   
/*      */   public abstract List<Method> allMethods();
/*      */   
/*      */   abstract boolean isAssignableTo(ReferenceType paramReferenceType);
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ReferenceTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */