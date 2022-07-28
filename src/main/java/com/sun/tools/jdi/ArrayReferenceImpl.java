/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ArrayReference;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayReferenceImpl
/*     */   extends ObjectReferenceImpl
/*     */   implements ArrayReference
/*     */ {
/*  38 */   int length = -1;
/*     */   
/*     */   ArrayReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  41 */     super(paramVirtualMachine, paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassTypeImpl invokableReferenceType(Method paramMethod) {
/*  49 */     return (ClassTypeImpl)paramMethod.declaringType();
/*     */   }
/*     */   
/*     */   ArrayTypeImpl arrayType() {
/*  53 */     return (ArrayTypeImpl)type();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  61 */     if (this.length == -1) {
/*     */       try {
/*  63 */         this
/*  64 */           .length = (JDWP.ArrayReference.Length.process(this.vm, this)).arrayLength;
/*  65 */       } catch (JDWPException jDWPException) {
/*  66 */         throw jDWPException.toJDIException();
/*     */       } 
/*     */     }
/*  69 */     return this.length;
/*     */   }
/*     */   
/*     */   public Value getValue(int paramInt) {
/*  73 */     List<Value> list = getValues(paramInt, 1);
/*  74 */     return list.get(0);
/*     */   }
/*     */   
/*     */   public List<Value> getValues() {
/*  78 */     return getValues(0, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateArrayAccess(int paramInt1, int paramInt2) {
/*  89 */     if (paramInt1 < 0 || paramInt1 > length()) {
/*  90 */       throw new IndexOutOfBoundsException("Invalid array index: " + paramInt1);
/*     */     }
/*     */     
/*  93 */     if (paramInt2 < 0) {
/*  94 */       throw new IndexOutOfBoundsException("Invalid array range length: " + paramInt2);
/*     */     }
/*     */     
/*  97 */     if (paramInt1 + paramInt2 > length()) {
/*  98 */       throw new IndexOutOfBoundsException("Invalid array range: " + paramInt1 + " to " + (paramInt1 + paramInt2 - 1));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T cast(Object paramObject) {
/* 106 */     return (T)paramObject;
/*     */   }
/*     */   public List<Value> getValues(int paramInt1, int paramInt2) {
/*     */     List<Value> list;
/* 110 */     if (paramInt2 == -1) {
/* 111 */       paramInt2 = length() - paramInt1;
/*     */     }
/* 113 */     validateArrayAccess(paramInt1, paramInt2);
/* 114 */     if (paramInt2 == 0) {
/* 115 */       return new ArrayList<>();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 120 */       list = cast((JDWP.ArrayReference.GetValues.process(this.vm, this, paramInt1, paramInt2)).values);
/* 121 */     } catch (JDWPException jDWPException) {
/* 122 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */     
/* 125 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(int paramInt, Value paramValue) throws InvalidTypeException, ClassNotLoadedException {
/* 131 */     ArrayList<Value> arrayList = new ArrayList(1);
/* 132 */     arrayList.add(paramValue);
/* 133 */     setValues(paramInt, arrayList, 0, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValues(List<? extends Value> paramList) throws InvalidTypeException, ClassNotLoadedException {
/* 139 */     setValues(0, paramList, 0, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValues(int paramInt1, List<? extends Value> paramList, int paramInt2, int paramInt3) throws InvalidTypeException, ClassNotLoadedException {
/* 147 */     if (paramInt3 == -1)
/*     */     {
/*     */       
/* 150 */       paramInt3 = Math.min(length() - paramInt1, paramList
/* 151 */           .size() - paramInt2);
/*     */     }
/* 153 */     validateMirrorsOrNulls((Collection)paramList);
/* 154 */     validateArrayAccess(paramInt1, paramInt3);
/*     */     
/* 156 */     if (paramInt2 < 0 || paramInt2 > paramList.size()) {
/* 157 */       throw new IndexOutOfBoundsException("Invalid source index: " + paramInt2);
/*     */     }
/*     */     
/* 160 */     if (paramInt2 + paramInt3 > paramList.size()) {
/* 161 */       throw new IndexOutOfBoundsException("Invalid source range: " + paramInt2 + " to " + (paramInt2 + paramInt3 - 1));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     boolean bool = false;
/* 168 */     ValueImpl[] arrayOfValueImpl = new ValueImpl[paramInt3];
/*     */     
/* 170 */     for (byte b = 0; b < paramInt3; b++) {
/* 171 */       ValueImpl valueImpl = (ValueImpl)paramList.get(paramInt2 + b);
/*     */ 
/*     */       
/*     */       try {
/* 175 */         arrayOfValueImpl[b] = 
/* 176 */           ValueImpl.prepareForAssignment(valueImpl, new Component());
/*     */         
/* 178 */         bool = true;
/* 179 */       } catch (ClassNotLoadedException classNotLoadedException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 190 */         if (valueImpl != null) {
/* 191 */           throw classNotLoadedException;
/*     */         }
/*     */       } 
/*     */     } 
/* 195 */     if (bool) {
/*     */       
/*     */       try {
/* 198 */         JDWP.ArrayReference.SetValues.process(this.vm, this, paramInt1, arrayOfValueImpl);
/* 199 */       } catch (JDWPException jDWPException) {
/* 200 */         throw jDWPException.toJDIException();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 206 */     return "instance of " + arrayType().componentTypeName() + "[" + 
/* 207 */       length() + "] (id=" + uniqueID() + ")";
/*     */   }
/*     */   
/*     */   byte typeValueKey() {
/* 211 */     return 91;
/*     */   }
/*     */ 
/*     */   
/*     */   void validateAssignment(ValueContainer paramValueContainer) throws InvalidTypeException, ClassNotLoadedException {
/*     */     try {
/* 217 */       super.validateAssignment(paramValueContainer);
/* 218 */     } catch (ClassNotLoadedException classNotLoadedException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 228 */       boolean bool = false;
/*     */       
/* 230 */       JNITypeParser jNITypeParser1 = new JNITypeParser(paramValueContainer.signature());
/*     */       
/* 232 */       JNITypeParser jNITypeParser2 = new JNITypeParser(arrayType().signature());
/* 233 */       int i = jNITypeParser1.dimensionCount();
/* 234 */       if (i <= jNITypeParser2.dimensionCount()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 242 */         String str1 = jNITypeParser1.componentSignature(i);
/*     */         
/* 244 */         Type type1 = paramValueContainer.findType(str1);
/*     */         
/* 246 */         String str2 = jNITypeParser2.componentSignature(i);
/*     */         
/* 248 */         Type type2 = arrayType().findComponentType(str2);
/* 249 */         bool = ArrayTypeImpl.isComponentAssignable(type1, type2);
/*     */       } 
/*     */ 
/*     */       
/* 253 */       if (!bool) {
/* 254 */         throw new InvalidTypeException("Cannot assign " + 
/* 255 */             arrayType().name() + " to " + paramValueContainer
/*     */             
/* 257 */             .typeName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class Component
/*     */     implements ValueContainer
/*     */   {
/*     */     public Type type() throws ClassNotLoadedException {
/* 271 */       return ArrayReferenceImpl.this.arrayType().componentType();
/*     */     }
/*     */     public String typeName() {
/* 274 */       return ArrayReferenceImpl.this.arrayType().componentTypeName();
/*     */     }
/*     */     public String signature() {
/* 277 */       return ArrayReferenceImpl.this.arrayType().componentSignature();
/*     */     }
/*     */     public Type findType(String param1String) throws ClassNotLoadedException {
/* 280 */       return ArrayReferenceImpl.this.arrayType().findComponentType(param1String);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ArrayReferenceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */