/*     */ package sun.tools.asm;
/*     */ 
/*     */ import sun.tools.java.MemberDefinition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Label
/*     */   extends Instruction
/*     */ {
/*  41 */   static int labelCount = 0;
/*     */   
/*     */   int ID;
/*     */   
/*     */   int depth;
/*     */   
/*     */   MemberDefinition[] locals;
/*     */   
/*     */   public Label() {
/*  50 */     super(0L, -1, (Object)null);
/*  51 */     this.ID = ++labelCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Label getDestination() {
/*  60 */     Label label = this;
/*  61 */     if (this.next != null && this.next != this && this.depth == 0) {
/*  62 */       this.depth = 1;
/*     */       
/*  64 */       switch (this.next.opc) {
/*     */         case -1:
/*  66 */           label = ((Label)this.next).getDestination();
/*     */           break;
/*     */         
/*     */         case 167:
/*  70 */           label = ((Label)this.next.value).getDestination();
/*     */           break;
/*     */         
/*     */         case 18:
/*     */         case 19:
/*  75 */           if (this.next.value instanceof Integer) {
/*  76 */             Instruction instruction = this.next.next;
/*  77 */             if (instruction.opc == -1) {
/*  78 */               instruction = (((Label)instruction).getDestination()).next;
/*     */             }
/*     */             
/*  81 */             if (instruction.opc == 153) {
/*  82 */               if (((Integer)this.next.value).intValue() == 0) {
/*  83 */                 label = (Label)instruction.value;
/*     */               } else {
/*  85 */                 label = new Label();
/*  86 */                 label.next = instruction.next;
/*  87 */                 instruction.next = label;
/*     */               } 
/*  89 */               label = label.getDestination();
/*     */               break;
/*     */             } 
/*  92 */             if (instruction.opc == 154) {
/*  93 */               if (((Integer)this.next.value).intValue() == 0) {
/*  94 */                 label = new Label();
/*  95 */                 label.next = instruction.next;
/*  96 */                 instruction.next = label;
/*     */               } else {
/*  98 */                 label = (Label)instruction.value;
/*     */               } 
/* 100 */               label = label.getDestination();
/*     */             } 
/*     */           } 
/*     */           break;
/*     */       } 
/*     */       
/* 106 */       this.depth = 0;
/*     */     } 
/* 108 */     return label;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 112 */     String str = "$" + this.ID + ":";
/* 113 */     if (this.value != null)
/* 114 */       str = str + " stack=" + this.value; 
/* 115 */     return str;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\Label.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */