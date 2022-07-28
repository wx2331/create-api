/*     */ package com.sun.tools.javap;
/*     */
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Descriptor;
/*     */ import com.sun.tools.classfile.DescriptorException;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.LocalVariableTable_attribute;
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
/*     */ public class LocalVariableTableWriter
/*     */   extends InstructionDetailWriter
/*     */ {
/*     */   private ClassWriter classWriter;
/*     */   private Code_attribute codeAttr;
/*     */   private Map<Integer, List<LocalVariableTable_attribute.Entry>> pcMap;
/*     */
/*     */   public enum NoteKind
/*     */   {
/*  52 */     START("start") {
/*     */       public boolean match(LocalVariableTable_attribute.Entry param2Entry, int param2Int) {
/*  54 */         return (param2Int == param2Entry.start_pc);
/*     */       }
/*     */     },
/*  57 */     END("end") {
/*     */       public boolean match(LocalVariableTable_attribute.Entry param2Entry, int param2Int) {
/*  59 */         return (param2Int == param2Entry.start_pc + param2Entry.length);
/*     */       } };
/*     */
/*     */     NoteKind(String param1String1) {
/*  63 */       this.text = param1String1;
/*     */     }
/*     */     public final String text;
/*     */
/*     */     public abstract boolean match(LocalVariableTable_attribute.Entry param1Entry, int param1Int); }
/*     */
/*     */   static LocalVariableTableWriter instance(Context paramContext) {
/*  70 */     LocalVariableTableWriter localVariableTableWriter = paramContext.<LocalVariableTableWriter>get(LocalVariableTableWriter.class);
/*  71 */     if (localVariableTableWriter == null)
/*  72 */       localVariableTableWriter = new LocalVariableTableWriter(paramContext);
/*  73 */     return localVariableTableWriter;
/*     */   }
/*     */
/*     */   protected LocalVariableTableWriter(Context paramContext) {
/*  77 */     super(paramContext);
/*  78 */     paramContext.put(LocalVariableTableWriter.class, this);
/*  79 */     this.classWriter = ClassWriter.instance(paramContext);
/*     */   }
/*     */
/*     */   public void reset(Code_attribute paramCode_attribute) {
/*  83 */     this.codeAttr = paramCode_attribute;
/*  84 */     this.pcMap = new HashMap<>();
/*     */
/*  86 */     LocalVariableTable_attribute localVariableTable_attribute = (LocalVariableTable_attribute)paramCode_attribute.attributes.get("LocalVariableTable");
/*  87 */     if (localVariableTable_attribute == null) {
/*     */       return;
/*     */     }
/*  90 */     for (byte b = 0; b < localVariableTable_attribute.local_variable_table.length; b++) {
/*  91 */       LocalVariableTable_attribute.Entry entry = localVariableTable_attribute.local_variable_table[b];
/*  92 */       put(entry.start_pc, entry);
/*  93 */       put(entry.start_pc + entry.length, entry);
/*     */     }
/*     */   }
/*     */
/*     */   public void writeDetails(Instruction paramInstruction) {
/*  98 */     int i = paramInstruction.getPC();
/*  99 */     writeLocalVariables(i, NoteKind.END);
/* 100 */     writeLocalVariables(i, NoteKind.START);
/*     */   }
/*     */
/*     */
/*     */   public void flush() {
/* 105 */     int i = this.codeAttr.code_length;
/* 106 */     writeLocalVariables(i, NoteKind.END);
/*     */   }
/*     */
/*     */   public void writeLocalVariables(int paramInt, NoteKind paramNoteKind) {
/* 110 */     ConstantPool constantPool = (this.classWriter.getClassFile()).constant_pool;
/* 111 */     String str = space(2);
/* 112 */     List list = this.pcMap.get(Integer.valueOf(paramInt));
/* 113 */     if (list != null) {
/*     */
/* 115 */       ListIterator<LocalVariableTable_attribute.Entry> listIterator = list.listIterator((paramNoteKind == NoteKind.END) ? list.size() : 0);
/* 116 */       while ((paramNoteKind == NoteKind.END) ? listIterator.hasPrevious() : listIterator.hasNext()) {
/*     */
/* 118 */         LocalVariableTable_attribute.Entry entry = (paramNoteKind == NoteKind.END) ? listIterator.previous() : listIterator.next();
/* 119 */         if (paramNoteKind.match(entry, paramInt)) {
/* 120 */           print(str);
/* 121 */           print(paramNoteKind.text);
/* 122 */           print(" local ");
/* 123 */           print(Integer.valueOf(entry.index));
/* 124 */           print(" // ");
/* 125 */           Descriptor descriptor = new Descriptor(entry.descriptor_index);
/*     */           try {
/* 127 */             print(descriptor.getFieldType(constantPool));
/* 128 */           } catch (Descriptor.InvalidDescriptor invalidDescriptor) {
/* 129 */             print(report((DescriptorException)invalidDescriptor));
/* 130 */           } catch (ConstantPoolException constantPoolException) {
/* 131 */             print(report(constantPoolException));
/*     */           }
/* 133 */           print(" ");
/*     */           try {
/* 135 */             print(constantPool.getUTF8Value(entry.name_index));
/* 136 */           } catch (ConstantPoolException constantPoolException) {
/* 137 */             print(report(constantPoolException));
/*     */           }
/* 139 */           println();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   private void put(int paramInt, LocalVariableTable_attribute.Entry paramEntry) {
/* 146 */     List<LocalVariableTable_attribute.Entry> list = this.pcMap.get(Integer.valueOf(paramInt));
/* 147 */     if (list == null) {
/* 148 */       list = new ArrayList();
/* 149 */       this.pcMap.put(Integer.valueOf(paramInt), list);
/*     */     }
/* 151 */     if (!list.contains(paramEntry))
/* 152 */       list.add(paramEntry);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\LocalVariableTableWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
