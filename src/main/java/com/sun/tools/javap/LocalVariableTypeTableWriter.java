/*     */ package com.sun.tools.javap;
/*     */
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Descriptor;
/*     */ import com.sun.tools.classfile.DescriptorException;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.LocalVariableTypeTable_attribute;
/*     */ import com.sun.tools.classfile.Signature;
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
/*     */ public class LocalVariableTypeTableWriter
/*     */   extends InstructionDetailWriter
/*     */ {
/*     */   private ClassWriter classWriter;
/*     */   private Code_attribute codeAttr;
/*     */   private Map<Integer, List<LocalVariableTypeTable_attribute.Entry>> pcMap;
/*     */
/*     */   public enum NoteKind
/*     */   {
/*  53 */     START("start") {
/*     */       public boolean match(LocalVariableTypeTable_attribute.Entry param2Entry, int param2Int) {
/*  55 */         return (param2Int == param2Entry.start_pc);
/*     */       }
/*     */     },
/*  58 */     END("end") {
/*     */       public boolean match(LocalVariableTypeTable_attribute.Entry param2Entry, int param2Int) {
/*  60 */         return (param2Int == param2Entry.start_pc + param2Entry.length);
/*     */       } };
/*     */
/*     */     NoteKind(String param1String1) {
/*  64 */       this.text = param1String1;
/*     */     }
/*     */     public final String text;
/*     */
/*     */     public abstract boolean match(LocalVariableTypeTable_attribute.Entry param1Entry, int param1Int); }
/*     */
/*     */   static LocalVariableTypeTableWriter instance(Context paramContext) {
/*  71 */     LocalVariableTypeTableWriter localVariableTypeTableWriter = paramContext.<LocalVariableTypeTableWriter>get(LocalVariableTypeTableWriter.class);
/*  72 */     if (localVariableTypeTableWriter == null)
/*  73 */       localVariableTypeTableWriter = new LocalVariableTypeTableWriter(paramContext);
/*  74 */     return localVariableTypeTableWriter;
/*     */   }
/*     */
/*     */   protected LocalVariableTypeTableWriter(Context paramContext) {
/*  78 */     super(paramContext);
/*  79 */     paramContext.put(LocalVariableTypeTableWriter.class, this);
/*  80 */     this.classWriter = ClassWriter.instance(paramContext);
/*     */   }
/*     */
/*     */   public void reset(Code_attribute paramCode_attribute) {
/*  84 */     this.codeAttr = paramCode_attribute;
/*  85 */     this.pcMap = new HashMap<>();
/*     */
/*  87 */     LocalVariableTypeTable_attribute localVariableTypeTable_attribute = (LocalVariableTypeTable_attribute)paramCode_attribute.attributes.get("LocalVariableTypeTable");
/*  88 */     if (localVariableTypeTable_attribute == null) {
/*     */       return;
/*     */     }
/*  91 */     for (byte b = 0; b < localVariableTypeTable_attribute.local_variable_table.length; b++) {
/*  92 */       LocalVariableTypeTable_attribute.Entry entry = localVariableTypeTable_attribute.local_variable_table[b];
/*  93 */       put(entry.start_pc, entry);
/*  94 */       put(entry.start_pc + entry.length, entry);
/*     */     }
/*     */   }
/*     */
/*     */   public void writeDetails(Instruction paramInstruction) {
/*  99 */     int i = paramInstruction.getPC();
/* 100 */     writeLocalVariables(i, NoteKind.END);
/* 101 */     writeLocalVariables(i, NoteKind.START);
/*     */   }
/*     */
/*     */
/*     */   public void flush() {
/* 106 */     int i = this.codeAttr.code_length;
/* 107 */     writeLocalVariables(i, NoteKind.END);
/*     */   }
/*     */
/*     */   public void writeLocalVariables(int paramInt, NoteKind paramNoteKind) {
/* 111 */     ConstantPool constantPool = (this.classWriter.getClassFile()).constant_pool;
/* 112 */     String str = space(2);
/* 113 */     List list = this.pcMap.get(Integer.valueOf(paramInt));
/* 114 */     if (list != null) {
/*     */
/* 116 */       ListIterator<LocalVariableTypeTable_attribute.Entry> listIterator = list.listIterator((paramNoteKind == NoteKind.END) ? list.size() : 0);
/* 117 */       while ((paramNoteKind == NoteKind.END) ? listIterator.hasPrevious() : listIterator.hasNext()) {
/*     */
/* 119 */         LocalVariableTypeTable_attribute.Entry entry = (paramNoteKind == NoteKind.END) ? listIterator.previous() : listIterator.next();
/* 120 */         if (paramNoteKind.match(entry, paramInt)) {
/* 121 */           print(str);
/* 122 */           print(paramNoteKind.text);
/* 123 */           print(" generic local ");
/* 124 */           print(Integer.valueOf(entry.index));
/* 125 */           print(" // ");
/* 126 */           Signature signature = new Signature(entry.signature_index);
/*     */           try {
/* 128 */             print(signature.getFieldType(constantPool).toString().replace("/", "."));
/* 129 */           } catch (Descriptor.InvalidDescriptor invalidDescriptor) {
/* 130 */             print(report((DescriptorException)invalidDescriptor));
/* 131 */           } catch (ConstantPoolException constantPoolException) {
/* 132 */             print(report(constantPoolException));
/*     */           }
/* 134 */           print(" ");
/*     */           try {
/* 136 */             print(constantPool.getUTF8Value(entry.name_index));
/* 137 */           } catch (ConstantPoolException constantPoolException) {
/* 138 */             print(report(constantPoolException));
/*     */           }
/* 140 */           println();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   private void put(int paramInt, LocalVariableTypeTable_attribute.Entry paramEntry) {
/* 147 */     List<LocalVariableTypeTable_attribute.Entry> list = this.pcMap.get(Integer.valueOf(paramInt));
/* 148 */     if (list == null) {
/* 149 */       list = new ArrayList();
/* 150 */       this.pcMap.put(Integer.valueOf(paramInt), list);
/*     */     }
/* 152 */     if (!list.contains(paramEntry))
/* 153 */       list.add(paramEntry);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\LocalVariableTypeTableWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
