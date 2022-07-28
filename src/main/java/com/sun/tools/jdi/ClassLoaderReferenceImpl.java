/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassLoaderReference;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassLoaderReferenceImpl
/*     */   extends ObjectReferenceImpl
/*     */   implements ClassLoaderReference, VMListener
/*     */ {
/*     */   private static class Cache
/*     */     extends ObjectReferenceImpl.Cache
/*     */   {
/*     */     private Cache() {}
/*     */     
/*  36 */     List<ReferenceType> visibleClasses = null;
/*     */   }
/*     */   
/*     */   protected ObjectReferenceImpl.Cache newCache() {
/*  40 */     return new Cache();
/*     */   }
/*     */   
/*     */   ClassLoaderReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  44 */     super(paramVirtualMachine, paramLong);
/*  45 */     this.vm.state().addListener(this);
/*     */   }
/*     */   
/*     */   protected String description() {
/*  49 */     return "ClassLoaderReference " + uniqueID();
/*     */   }
/*     */   
/*     */   public List<ReferenceType> definedClasses() {
/*  53 */     ArrayList<ReferenceType> arrayList = new ArrayList();
/*  54 */     for (ReferenceType referenceType : this.vm.allClasses()) {
/*  55 */       if (referenceType.isPrepared() && 
/*  56 */         equals(referenceType.classLoader())) {
/*  57 */         arrayList.add(referenceType);
/*     */       }
/*     */     } 
/*  60 */     return arrayList;
/*     */   }
/*     */   
/*     */   public List<ReferenceType> visibleClasses() {
/*  64 */     List<ReferenceType> list = null;
/*     */     try {
/*  66 */       Cache cache = (Cache)getCache();
/*     */       
/*  68 */       if (cache != null) {
/*  69 */         list = cache.visibleClasses;
/*     */       }
/*  71 */       if (list == null) {
/*     */ 
/*     */         
/*  74 */         JDWP.ClassLoaderReference.VisibleClasses.ClassInfo[] arrayOfClassInfo = (JDWP.ClassLoaderReference.VisibleClasses.process(this.vm, this)).classes;
/*  75 */         list = new ArrayList<>(arrayOfClassInfo.length);
/*  76 */         for (byte b = 0; b < arrayOfClassInfo.length; b++) {
/*  77 */           list.add(this.vm.referenceType((arrayOfClassInfo[b]).typeID, (arrayOfClassInfo[b]).refTypeTag));
/*     */         }
/*     */         
/*  80 */         list = Collections.unmodifiableList(list);
/*  81 */         if (cache != null) {
/*  82 */           cache.visibleClasses = list;
/*  83 */           if ((this.vm.traceFlags & 0x10) != 0) {
/*  84 */             this.vm.printTrace(description() + " temporarily caching visible classes (count = " + list
/*     */                 
/*  86 */                 .size() + ")");
/*     */           }
/*     */         } 
/*     */       } 
/*  90 */     } catch (JDWPException jDWPException) {
/*  91 */       throw jDWPException.toJDIException();
/*     */     } 
/*  93 */     return list;
/*     */   }
/*     */   
/*     */   Type findType(String paramString) throws ClassNotLoadedException {
/*  97 */     List<ReferenceType> list = visibleClasses();
/*  98 */     Iterator<ReferenceType> iterator = list.iterator();
/*  99 */     while (iterator.hasNext()) {
/* 100 */       ReferenceType referenceType = iterator.next();
/* 101 */       if (referenceType.signature().equals(paramString)) {
/* 102 */         return (Type)referenceType;
/*     */       }
/*     */     } 
/* 105 */     JNITypeParser jNITypeParser = new JNITypeParser(paramString);
/* 106 */     throw new ClassNotLoadedException(jNITypeParser.typeName(), "Class " + jNITypeParser
/* 107 */         .typeName() + " not loaded");
/*     */   }
/*     */   
/*     */   byte typeValueKey() {
/* 111 */     return 108;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ClassLoaderReferenceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */