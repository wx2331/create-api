/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TryBlockWriter
/*     */   extends InstructionDetailWriter
/*     */ {
/*     */   private Map<Integer, List<Code_attribute.Exception_data>> pcMap;
/*     */   private Map<Code_attribute.Exception_data, Integer> indexMap;
/*     */   private ConstantWriter constantWriter;
/*     */   
/*     */   public enum NoteKind
/*     */   {
/*  47 */     START("try") {
/*     */       public boolean match(Code_attribute.Exception_data param2Exception_data, int param2Int) {
/*  49 */         return (param2Int == param2Exception_data.start_pc);
/*     */       }
/*     */     },
/*  52 */     END("end try") {
/*     */       public boolean match(Code_attribute.Exception_data param2Exception_data, int param2Int) {
/*  54 */         return (param2Int == param2Exception_data.end_pc);
/*     */       }
/*     */     },
/*  57 */     HANDLER("catch") {
/*     */       public boolean match(Code_attribute.Exception_data param2Exception_data, int param2Int) {
/*  59 */         return (param2Int == param2Exception_data.handler_pc);
/*     */       } };
/*     */     
/*     */     NoteKind(String param1String1) {
/*  63 */       this.text = param1String1;
/*     */     }
/*     */     public final String text;
/*     */     
/*     */     public abstract boolean match(Code_attribute.Exception_data param1Exception_data, int param1Int); }
/*     */   
/*     */   static TryBlockWriter instance(Context paramContext) {
/*  70 */     TryBlockWriter tryBlockWriter = paramContext.<TryBlockWriter>get(TryBlockWriter.class);
/*  71 */     if (tryBlockWriter == null)
/*  72 */       tryBlockWriter = new TryBlockWriter(paramContext); 
/*  73 */     return tryBlockWriter;
/*     */   }
/*     */   
/*     */   protected TryBlockWriter(Context paramContext) {
/*  77 */     super(paramContext);
/*  78 */     paramContext.put(TryBlockWriter.class, this);
/*  79 */     this.constantWriter = ConstantWriter.instance(paramContext);
/*     */   }
/*     */   
/*     */   public void reset(Code_attribute paramCode_attribute) {
/*  83 */     this.indexMap = new HashMap<>();
/*  84 */     this.pcMap = new HashMap<>();
/*  85 */     for (byte b = 0; b < paramCode_attribute.exception_table.length; b++) {
/*  86 */       Code_attribute.Exception_data exception_data = paramCode_attribute.exception_table[b];
/*  87 */       this.indexMap.put(exception_data, Integer.valueOf(b));
/*  88 */       put(exception_data.start_pc, exception_data);
/*  89 */       put(exception_data.end_pc, exception_data);
/*  90 */       put(exception_data.handler_pc, exception_data);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeDetails(Instruction paramInstruction) {
/*  95 */     writeTrys(paramInstruction, NoteKind.END);
/*  96 */     writeTrys(paramInstruction, NoteKind.START);
/*  97 */     writeTrys(paramInstruction, NoteKind.HANDLER);
/*     */   }
/*     */   
/*     */   public void writeTrys(Instruction paramInstruction, NoteKind paramNoteKind) {
/* 101 */     String str = space(2);
/* 102 */     int i = paramInstruction.getPC();
/* 103 */     List list = this.pcMap.get(Integer.valueOf(i));
/* 104 */     if (list != null) {
/*     */       
/* 106 */       ListIterator<Code_attribute.Exception_data> listIterator = list.listIterator((paramNoteKind == NoteKind.END) ? list.size() : 0);
/* 107 */       while ((paramNoteKind == NoteKind.END) ? listIterator.hasPrevious() : listIterator.hasNext()) {
/*     */         
/* 109 */         Code_attribute.Exception_data exception_data = (paramNoteKind == NoteKind.END) ? listIterator.previous() : listIterator.next();
/* 110 */         if (paramNoteKind.match(exception_data, i)) {
/* 111 */           print(str);
/* 112 */           print(paramNoteKind.text);
/* 113 */           print("[");
/* 114 */           print(this.indexMap.get(exception_data));
/* 115 */           print("] ");
/* 116 */           if (exception_data.catch_type == 0) {
/* 117 */             print("finally");
/*     */           } else {
/* 119 */             print("#" + exception_data.catch_type);
/* 120 */             print(" // ");
/* 121 */             this.constantWriter.write(exception_data.catch_type);
/*     */           } 
/* 123 */           println();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void put(int paramInt, Code_attribute.Exception_data paramException_data) {
/* 130 */     List<Code_attribute.Exception_data> list = this.pcMap.get(Integer.valueOf(paramInt));
/* 131 */     if (list == null) {
/* 132 */       list = new ArrayList();
/* 133 */       this.pcMap.put(Integer.valueOf(paramInt), list);
/*     */     } 
/* 135 */     if (!list.contains(paramException_data))
/* 136 */       list.add(paramException_data); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\TryBlockWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */