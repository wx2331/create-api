/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReferenceFinder
/*     */ {
/*     */   private final Filter filter;
/*     */   private final Visitor visitor;
/*     */   private ConstantPool.Visitor<Boolean, ConstantPool> cpVisitor;
/*     */   private Instruction.KindVisitor<Integer, List<Integer>> codeVisitor;
/*     */   
/*     */   public ReferenceFinder(Filter paramFilter, Visitor paramVisitor) {
/* 132 */     this.cpVisitor = new ConstantPool.Visitor<Boolean, ConstantPool>()
/*     */       {
/*     */         public Boolean visitClass(ConstantPool.CONSTANT_Class_info param1CONSTANT_Class_info, ConstantPool param1ConstantPool)
/*     */         {
/* 136 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/*     */         public Boolean visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info param1CONSTANT_InterfaceMethodref_info, ConstantPool param1ConstantPool) {
/* 140 */           return Boolean.valueOf(ReferenceFinder.this.filter.accept(param1ConstantPool, param1CONSTANT_InterfaceMethodref_info));
/*     */         }
/*     */         
/*     */         public Boolean visitMethodref(ConstantPool.CONSTANT_Methodref_info param1CONSTANT_Methodref_info, ConstantPool param1ConstantPool) {
/* 144 */           return Boolean.valueOf(ReferenceFinder.this.filter.accept(param1ConstantPool, param1CONSTANT_Methodref_info));
/*     */         }
/*     */         
/*     */         public Boolean visitFieldref(ConstantPool.CONSTANT_Fieldref_info param1CONSTANT_Fieldref_info, ConstantPool param1ConstantPool) {
/* 148 */           return Boolean.valueOf(ReferenceFinder.this.filter.accept(param1ConstantPool, param1CONSTANT_Fieldref_info));
/*     */         }
/*     */         
/*     */         public Boolean visitDouble(ConstantPool.CONSTANT_Double_info param1CONSTANT_Double_info, ConstantPool param1ConstantPool) {
/* 152 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/*     */         public Boolean visitFloat(ConstantPool.CONSTANT_Float_info param1CONSTANT_Float_info, ConstantPool param1ConstantPool) {
/* 156 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/*     */         public Boolean visitInteger(ConstantPool.CONSTANT_Integer_info param1CONSTANT_Integer_info, ConstantPool param1ConstantPool) {
/* 160 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/*     */         public Boolean visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info param1CONSTANT_InvokeDynamic_info, ConstantPool param1ConstantPool) {
/* 164 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/*     */         public Boolean visitLong(ConstantPool.CONSTANT_Long_info param1CONSTANT_Long_info, ConstantPool param1ConstantPool) {
/* 168 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/*     */         public Boolean visitNameAndType(ConstantPool.CONSTANT_NameAndType_info param1CONSTANT_NameAndType_info, ConstantPool param1ConstantPool) {
/* 172 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/*     */         public Boolean visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info param1CONSTANT_MethodHandle_info, ConstantPool param1ConstantPool) {
/* 176 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/*     */         public Boolean visitMethodType(ConstantPool.CONSTANT_MethodType_info param1CONSTANT_MethodType_info, ConstantPool param1ConstantPool) {
/* 180 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/*     */         public Boolean visitString(ConstantPool.CONSTANT_String_info param1CONSTANT_String_info, ConstantPool param1ConstantPool) {
/* 184 */           return Boolean.valueOf(false);
/*     */         }
/*     */         
/*     */         public Boolean visitUtf8(ConstantPool.CONSTANT_Utf8_info param1CONSTANT_Utf8_info, ConstantPool param1ConstantPool) {
/* 188 */           return Boolean.valueOf(false);
/*     */         }
/*     */       };
/*     */     
/* 192 */     this.codeVisitor = new Instruction.KindVisitor<Integer, List<Integer>>()
/*     */       {
/*     */         public Integer visitNoOperands(Instruction param1Instruction, List<Integer> param1List)
/*     */         {
/* 196 */           return Integer.valueOf(0);
/*     */         }
/*     */         
/*     */         public Integer visitArrayType(Instruction param1Instruction, Instruction.TypeKind param1TypeKind, List<Integer> param1List) {
/* 200 */           return Integer.valueOf(0);
/*     */         }
/*     */         
/*     */         public Integer visitBranch(Instruction param1Instruction, int param1Int, List<Integer> param1List) {
/* 204 */           return Integer.valueOf(0);
/*     */         }
/*     */         
/*     */         public Integer visitConstantPoolRef(Instruction param1Instruction, int param1Int, List<Integer> param1List) {
/* 208 */           return Integer.valueOf(param1List.contains(Integer.valueOf(param1Int)) ? param1Int : 0);
/*     */         }
/*     */         
/*     */         public Integer visitConstantPoolRefAndValue(Instruction param1Instruction, int param1Int1, int param1Int2, List<Integer> param1List) {
/* 212 */           return Integer.valueOf(param1List.contains(Integer.valueOf(param1Int1)) ? param1Int1 : 0);
/*     */         }
/*     */         
/*     */         public Integer visitLocal(Instruction param1Instruction, int param1Int, List<Integer> param1List) {
/* 216 */           return Integer.valueOf(0);
/*     */         }
/*     */         
/*     */         public Integer visitLocalAndValue(Instruction param1Instruction, int param1Int1, int param1Int2, List<Integer> param1List) {
/* 220 */           return Integer.valueOf(0);
/*     */         }
/*     */         
/*     */         public Integer visitLookupSwitch(Instruction param1Instruction, int param1Int1, int param1Int2, int[] param1ArrayOfint1, int[] param1ArrayOfint2, List<Integer> param1List) {
/* 224 */           return Integer.valueOf(0);
/*     */         }
/*     */         
/*     */         public Integer visitTableSwitch(Instruction param1Instruction, int param1Int1, int param1Int2, int param1Int3, int[] param1ArrayOfint, List<Integer> param1List) {
/* 228 */           return Integer.valueOf(0);
/*     */         }
/*     */         
/*     */         public Integer visitValue(Instruction param1Instruction, int param1Int, List<Integer> param1List) {
/* 232 */           return Integer.valueOf(0);
/*     */         }
/*     */         
/*     */         public Integer visitUnknown(Instruction param1Instruction, List<Integer> param1List) {
/* 236 */           return Integer.valueOf(0);
/*     */         }
/*     */       };
/*     */     this.filter = Objects.<Filter>requireNonNull(paramFilter);
/*     */     this.visitor = Objects.<Visitor>requireNonNull(paramVisitor);
/*     */   }
/*     */   
/*     */   public boolean parse(ClassFile paramClassFile) throws ConstantPoolException {
/*     */     ArrayList<Integer> arrayList = new ArrayList();
/*     */     int i = 1;
/*     */     for (ConstantPool.CPInfo cPInfo : paramClassFile.constant_pool.entries()) {
/*     */       if (((Boolean)cPInfo.<Boolean, ConstantPool>accept(this.cpVisitor, paramClassFile.constant_pool)).booleanValue())
/*     */         arrayList.add(Integer.valueOf(i)); 
/*     */       i += cPInfo.size();
/*     */     } 
/*     */     if (arrayList.isEmpty())
/*     */       return false; 
/*     */     for (Method method : paramClassFile.methods) {
/*     */       HashSet<Integer> hashSet = new HashSet();
/*     */       Code_attribute code_attribute = (Code_attribute)method.attributes.get("Code");
/*     */       if (code_attribute != null)
/*     */         for (Instruction instruction : code_attribute.getInstructions()) {
/*     */           int j = ((Integer)instruction.<Integer, List<Integer>>accept(this.codeVisitor, arrayList)).intValue();
/*     */           if (j > 0)
/*     */             hashSet.add(Integer.valueOf(j)); 
/*     */         }  
/*     */       if (hashSet.size() > 0) {
/*     */         ArrayList<ConstantPool.CPRefInfo> arrayList1 = new ArrayList(hashSet.size());
/*     */         for (Iterator<Integer> iterator = hashSet.iterator(); iterator.hasNext(); ) {
/*     */           int j = ((Integer)iterator.next()).intValue();
/*     */           arrayList1.add(ConstantPool.CPRefInfo.class.cast(paramClassFile.constant_pool.get(j)));
/*     */         } 
/*     */         this.visitor.visit(paramClassFile, method, arrayList1);
/*     */       } 
/*     */     } 
/*     */     return true;
/*     */   }
/*     */   
/*     */   public static interface Filter {
/*     */     boolean accept(ConstantPool param1ConstantPool, ConstantPool.CPRefInfo param1CPRefInfo);
/*     */   }
/*     */   
/*     */   public static interface Visitor {
/*     */     void visit(ClassFile param1ClassFile, Method param1Method, List<ConstantPool.CPRefInfo> param1List);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\ReferenceFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */