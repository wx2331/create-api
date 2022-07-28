/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.SerialFieldTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SerializedForm
/*     */ {
/*  71 */   ListBuffer<MethodDoc> methods = new ListBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private final ListBuffer<FieldDocImpl> fields = new ListBuffer();
/*     */ 
/*     */   
/*     */   private boolean definesSerializableFields = false;
/*     */ 
/*     */   
/*     */   private static final String SERIALIZABLE_FIELDS = "serialPersistentFields";
/*     */ 
/*     */   
/*     */   private static final String READOBJECT = "readObject";
/*     */ 
/*     */   
/*     */   private static final String WRITEOBJECT = "writeObject";
/*     */ 
/*     */   
/*     */   private static final String READRESOLVE = "readResolve";
/*     */   
/*     */   private static final String WRITEREPLACE = "writeReplace";
/*     */   
/*     */   private static final String READOBJECTNODATA = "readObjectNoData";
/*     */ 
/*     */   
/*     */   SerializedForm(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, ClassDocImpl paramClassDocImpl) {
/* 101 */     if (paramClassDocImpl.isExternalizable()) {
/*     */ 
/*     */ 
/*     */       
/* 105 */       String[] arrayOfString1 = { "java.io.ObjectInput" };
/* 106 */       String[] arrayOfString2 = { "java.io.ObjectOutput" };
/* 107 */       MethodDocImpl methodDocImpl = paramClassDocImpl.findMethod("readExternal", arrayOfString1);
/* 108 */       if (methodDocImpl != null) {
/* 109 */         this.methods.append(methodDocImpl);
/*     */       }
/* 111 */       methodDocImpl = paramClassDocImpl.findMethod("writeExternal", arrayOfString2);
/* 112 */       if (methodDocImpl != null) {
/* 113 */         this.methods.append(methodDocImpl);
/* 114 */         Tag[] arrayOfTag = methodDocImpl.tags("serialData");
/*     */       }
/*     */     
/* 117 */     } else if (paramClassDocImpl.isSerializable()) {
/*     */       
/* 119 */       Symbol.VarSymbol varSymbol = getDefinedSerializableFields(paramClassSymbol);
/* 120 */       if (varSymbol != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 126 */         this.definesSerializableFields = true;
/*     */         
/* 128 */         FieldDocImpl fieldDocImpl = paramDocEnv.getFieldDoc(varSymbol);
/* 129 */         this.fields.append(fieldDocImpl);
/* 130 */         mapSerialFieldTagImplsToFieldDocImpls(fieldDocImpl, paramDocEnv, paramClassSymbol);
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 137 */         computeDefaultSerializableFields(paramDocEnv, paramClassSymbol, paramClassDocImpl);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 143 */       addMethodIfExist(paramDocEnv, paramClassSymbol, "readObject");
/* 144 */       addMethodIfExist(paramDocEnv, paramClassSymbol, "writeObject");
/* 145 */       addMethodIfExist(paramDocEnv, paramClassSymbol, "readResolve");
/* 146 */       addMethodIfExist(paramDocEnv, paramClassSymbol, "writeReplace");
/* 147 */       addMethodIfExist(paramDocEnv, paramClassSymbol, "readObjectNoData");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Symbol.VarSymbol getDefinedSerializableFields(Symbol.ClassSymbol paramClassSymbol) {
/* 157 */     Names names = paramClassSymbol.name.table.names;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     for (Scope.Entry entry = paramClassSymbol.members().lookup(names.fromString("serialPersistentFields")); entry.scope != null; entry = entry.next()) {
/* 163 */       if (entry.sym.kind == 4) {
/* 164 */         Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)entry.sym;
/* 165 */         if ((varSymbol.flags() & 0x8L) != 0L && (varSymbol
/* 166 */           .flags() & 0x2L) != 0L) {
/* 167 */           return varSymbol;
/*     */         }
/*     */       } 
/*     */     } 
/* 171 */     return null;
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
/*     */   private void computeDefaultSerializableFields(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, ClassDocImpl paramClassDocImpl) {
/* 183 */     for (Scope.Entry entry = (paramClassSymbol.members()).elems; entry != null; entry = entry.sibling) {
/* 184 */       if (entry.sym != null && entry.sym.kind == 4) {
/* 185 */         Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)entry.sym;
/* 186 */         if ((varSymbol.flags() & 0x8L) == 0L && (varSymbol
/* 187 */           .flags() & 0x80L) == 0L) {
/*     */           
/* 189 */           FieldDocImpl fieldDocImpl = paramDocEnv.getFieldDoc(varSymbol);
/*     */ 
/*     */           
/* 192 */           this.fields.prepend(fieldDocImpl);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addMethodIfExist(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, String paramString) {
/* 210 */     Names names = paramClassSymbol.name.table.names;
/*     */     
/* 212 */     for (Scope.Entry entry = paramClassSymbol.members().lookup(names.fromString(paramString)); entry.scope != null; entry = entry.next()) {
/* 213 */       if (entry.sym.kind == 16) {
/* 214 */         Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)entry.sym;
/* 215 */         if ((methodSymbol.flags() & 0x8L) == 0L)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 222 */           this.methods.append(paramDocEnv.getMethodDoc(methodSymbol));
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
/*     */   
/*     */   private void mapSerialFieldTagImplsToFieldDocImpls(FieldDocImpl paramFieldDocImpl, DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol) {
/* 236 */     Names names = paramClassSymbol.name.table.names;
/*     */     
/* 238 */     SerialFieldTag[] arrayOfSerialFieldTag = paramFieldDocImpl.serialFieldTags();
/* 239 */     for (byte b = 0; b < arrayOfSerialFieldTag.length; b++) {
/* 240 */       if (arrayOfSerialFieldTag[b].fieldName() != null && arrayOfSerialFieldTag[b].fieldType() != null) {
/*     */ 
/*     */         
/* 243 */         Name name = names.fromString(arrayOfSerialFieldTag[b].fieldName());
/*     */ 
/*     */         
/* 246 */         for (Scope.Entry entry = paramClassSymbol.members().lookup(name); entry.scope != null; entry = entry.next()) {
/* 247 */           if (entry.sym.kind == 4) {
/* 248 */             Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)entry.sym;
/* 249 */             FieldDocImpl fieldDocImpl = paramDocEnv.getFieldDoc(varSymbol);
/* 250 */             ((SerialFieldTagImpl)arrayOfSerialFieldTag[b]).mapToFieldDocImpl(fieldDocImpl);
/*     */             break;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FieldDoc[] fields() {
/* 269 */     return (FieldDoc[])this.fields.toArray((Object[])new FieldDocImpl[this.fields.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MethodDoc[] methods() {
/* 278 */     return (MethodDoc[])this.methods.toArray((Object[])new MethodDoc[this.methods.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean definesSerializableFields() {
/* 288 */     return this.definesSerializableFields;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\SerializedForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */