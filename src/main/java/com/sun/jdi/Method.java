/*     */ package com.sun.jdi;
/*     */ 
/*     */ import java.util.List;
/*     */ import jdk.Exported;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Exported
/*     */ public interface Method
/*     */   extends TypeComponent, Locatable, Comparable<Method>
/*     */ {
/*     */   String returnTypeName();
/*     */   
/*     */   Type returnType() throws ClassNotLoadedException;
/*     */   
/*     */   List<String> argumentTypeNames();
/*     */   
/*     */   List<Type> argumentTypes() throws ClassNotLoadedException;
/*     */   
/*     */   boolean isAbstract();
/*     */   
/*     */   default boolean isDefault() {
/* 149 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   boolean isSynchronized();
/*     */   
/*     */   boolean isNative();
/*     */   
/*     */   boolean isVarArgs();
/*     */   
/*     */   boolean isBridge();
/*     */   
/*     */   boolean isConstructor();
/*     */   
/*     */   boolean isStaticInitializer();
/*     */   
/*     */   boolean isObsolete();
/*     */   
/*     */   List<Location> allLineLocations() throws AbsentInformationException;
/*     */   
/*     */   List<Location> allLineLocations(String paramString1, String paramString2) throws AbsentInformationException;
/*     */   
/*     */   List<Location> locationsOfLine(int paramInt) throws AbsentInformationException;
/*     */   
/*     */   List<Location> locationsOfLine(String paramString1, String paramString2, int paramInt) throws AbsentInformationException;
/*     */   
/*     */   Location locationOfCodeIndex(long paramLong);
/*     */   
/*     */   List<LocalVariable> variables() throws AbsentInformationException;
/*     */   
/*     */   List<LocalVariable> variablesByName(String paramString) throws AbsentInformationException;
/*     */   
/*     */   List<LocalVariable> arguments() throws AbsentInformationException;
/*     */   
/*     */   byte[] bytecodes();
/*     */   
/*     */   Location location();
/*     */   
/*     */   boolean equals(Object paramObject);
/*     */   
/*     */   int hashCode();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\Method.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */