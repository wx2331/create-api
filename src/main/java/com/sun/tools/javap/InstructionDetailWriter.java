/*    */ package com.sun.tools.javap;
/*    */ 
/*    */ import com.sun.tools.classfile.Instruction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class InstructionDetailWriter
/*    */   extends BasicWriter
/*    */ {
/*    */   public enum Kind
/*    */   {
/* 41 */     LOCAL_VARS("localVariables"),
/* 42 */     LOCAL_VAR_TYPES("localVariableTypes"),
/* 43 */     SOURCE("source"),
/* 44 */     STACKMAPS("stackMaps"),
/* 45 */     TRY_BLOCKS("tryBlocks"),
/* 46 */     TYPE_ANNOS("typeAnnotations");
/*    */     
/*    */     Kind(String param1String1) {
/* 49 */       this.option = param1String1;
/*    */     }
/*    */     
/*    */     final String option;
/*    */   }
/*    */   
/*    */   InstructionDetailWriter(Context paramContext) {
/* 56 */     super(paramContext);
/*    */   }
/*    */   
/*    */   abstract void writeDetails(Instruction paramInstruction);
/*    */   
/*    */   void flush() {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\InstructionDetailWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */