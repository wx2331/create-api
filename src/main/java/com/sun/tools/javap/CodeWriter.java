/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.DescriptorException;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.Method;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ public class CodeWriter
/*     */   extends BasicWriter
/*     */ {
/*     */   Instruction.KindVisitor<Void, Integer> instructionPrinter;
/*     */   private AttributeWriter attrWriter;
/*     */   private ClassWriter classWriter;
/*     */   private ConstantWriter constantWriter;
/*     */   private LocalVariableTableWriter localVariableTableWriter;
/*     */   private LocalVariableTypeTableWriter localVariableTypeTableWriter;
/*     */   private TypeAnnotationWriter typeAnnotationWriter;
/*     */   private SourceWriter sourceWriter;
/*     */   private StackMapWriter stackMapWriter;
/*     */   private TryBlockWriter tryBlockWriter;
/*     */   private Options options;
/*     */   
/*     */   public static CodeWriter instance(Context paramContext) {
/*  50 */     CodeWriter codeWriter = paramContext.<CodeWriter>get(CodeWriter.class);
/*  51 */     if (codeWriter == null)
/*  52 */       codeWriter = new CodeWriter(paramContext); 
/*  53 */     return codeWriter;
/*     */   }
/*     */   
/*     */   protected CodeWriter(Context paramContext) {
/*  57 */     super(paramContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     this.instructionPrinter = new Instruction.KindVisitor<Void, Integer>()
/*     */       {
/*     */         public Void visitNoOperands(Instruction param1Instruction, Integer param1Integer)
/*     */         {
/* 134 */           return null;
/*     */         }
/*     */         
/*     */         public Void visitArrayType(Instruction param1Instruction, Instruction.TypeKind param1TypeKind, Integer param1Integer) {
/* 138 */           CodeWriter.this.print(" " + param1TypeKind.name);
/* 139 */           return null;
/*     */         }
/*     */         
/*     */         public Void visitBranch(Instruction param1Instruction, int param1Int, Integer param1Integer) {
/* 143 */           CodeWriter.this.print(Integer.valueOf(param1Instruction.getPC() + param1Int));
/* 144 */           return null;
/*     */         }
/*     */         
/*     */         public Void visitConstantPoolRef(Instruction param1Instruction, int param1Int, Integer param1Integer) {
/* 148 */           CodeWriter.this.print("#" + param1Int);
/* 149 */           CodeWriter.this.tab();
/* 150 */           CodeWriter.this.print("// ");
/* 151 */           CodeWriter.this.printConstant(param1Int);
/* 152 */           return null;
/*     */         }
/*     */         
/*     */         public Void visitConstantPoolRefAndValue(Instruction param1Instruction, int param1Int1, int param1Int2, Integer param1Integer) {
/* 156 */           CodeWriter.this.print("#" + param1Int1 + ",  " + param1Int2);
/* 157 */           CodeWriter.this.tab();
/* 158 */           CodeWriter.this.print("// ");
/* 159 */           CodeWriter.this.printConstant(param1Int1);
/* 160 */           return null;
/*     */         }
/*     */         
/*     */         public Void visitLocal(Instruction param1Instruction, int param1Int, Integer param1Integer) {
/* 164 */           CodeWriter.this.print(Integer.valueOf(param1Int));
/* 165 */           return null;
/*     */         }
/*     */         
/*     */         public Void visitLocalAndValue(Instruction param1Instruction, int param1Int1, int param1Int2, Integer param1Integer) {
/* 169 */           CodeWriter.this.print(param1Int1 + ", " + param1Int2);
/* 170 */           return null;
/*     */         }
/*     */ 
/*     */         
/*     */         public Void visitLookupSwitch(Instruction param1Instruction, int param1Int1, int param1Int2, int[] param1ArrayOfint1, int[] param1ArrayOfint2, Integer param1Integer) {
/* 175 */           int i = param1Instruction.getPC();
/* 176 */           CodeWriter.this.print("{ // " + param1Int2);
/* 177 */           CodeWriter.this.indent(param1Integer.intValue());
/* 178 */           for (byte b = 0; b < param1Int2; b++) {
/* 179 */             CodeWriter.this.print(String.format("%n%12d: %d", new Object[] { Integer.valueOf(param1ArrayOfint1[b]), Integer.valueOf(i + param1ArrayOfint2[b]) }));
/*     */           } 
/* 181 */           CodeWriter.this.print("\n     default: " + (i + param1Int1) + "\n}");
/* 182 */           CodeWriter.this.indent(-param1Integer.intValue());
/* 183 */           return null;
/*     */         }
/*     */ 
/*     */         
/*     */         public Void visitTableSwitch(Instruction param1Instruction, int param1Int1, int param1Int2, int param1Int3, int[] param1ArrayOfint, Integer param1Integer) {
/* 188 */           int i = param1Instruction.getPC();
/* 189 */           CodeWriter.this.print("{ // " + param1Int2 + " to " + param1Int3);
/* 190 */           CodeWriter.this.indent(param1Integer.intValue());
/* 191 */           for (byte b = 0; b < param1ArrayOfint.length; b++) {
/* 192 */             CodeWriter.this.print(String.format("%n%12d: %d", new Object[] { Integer.valueOf(param1Int2 + b), Integer.valueOf(i + param1ArrayOfint[b]) }));
/*     */           } 
/* 194 */           CodeWriter.this.print("\n     default: " + (i + param1Int1) + "\n}");
/* 195 */           CodeWriter.this.indent(-param1Integer.intValue());
/* 196 */           return null;
/*     */         }
/*     */         
/*     */         public Void visitValue(Instruction param1Instruction, int param1Int, Integer param1Integer) {
/* 200 */           CodeWriter.this.print(Integer.valueOf(param1Int));
/* 201 */           return null;
/*     */         }
/*     */         
/*     */         public Void visitUnknown(Instruction param1Instruction, Integer param1Integer) {
/* 205 */           return null; } }; paramContext.put(CodeWriter.class, this); this.attrWriter = AttributeWriter.instance(paramContext); this.classWriter = ClassWriter.instance(paramContext); this.constantWriter = ConstantWriter.instance(paramContext); this.sourceWriter = SourceWriter.instance(paramContext); this.tryBlockWriter = TryBlockWriter.instance(paramContext); this.stackMapWriter = StackMapWriter.instance(paramContext); this.localVariableTableWriter = LocalVariableTableWriter.instance(paramContext); this.localVariableTypeTableWriter = LocalVariableTypeTableWriter.instance(paramContext); this.typeAnnotationWriter = TypeAnnotationWriter.instance(paramContext); this.options = Options.instance(paramContext);
/*     */   } void write(Code_attribute paramCode_attribute, ConstantPool paramConstantPool) { println("Code:"); indent(1);
/*     */     writeVerboseHeader(paramCode_attribute, paramConstantPool);
/*     */     writeInstrs(paramCode_attribute);
/*     */     writeExceptionTable(paramCode_attribute);
/*     */     this.attrWriter.write(paramCode_attribute, paramCode_attribute.attributes, paramConstantPool);
/* 211 */     indent(-1); } public void writeExceptionTable(Code_attribute paramCode_attribute) { if (paramCode_attribute.exception_table_length > 0)
/* 212 */     { println("Exception table:");
/* 213 */       indent(1);
/* 214 */       println(" from    to  target type");
/* 215 */       for (byte b = 0; b < paramCode_attribute.exception_table.length; b++) {
/* 216 */         Code_attribute.Exception_data exception_data = paramCode_attribute.exception_table[b];
/* 217 */         print(String.format(" %5d %5d %5d", new Object[] {
/* 218 */                 Integer.valueOf(exception_data.start_pc), Integer.valueOf(exception_data.end_pc), Integer.valueOf(exception_data.handler_pc) }));
/* 219 */         print("   ");
/* 220 */         int i = exception_data.catch_type;
/* 221 */         if (i == 0) {
/* 222 */           println("any");
/*     */         } else {
/* 224 */           print("Class ");
/* 225 */           println(this.constantWriter.stringValue(i));
/*     */         } 
/*     */       } 
/* 228 */       indent(-1); }  }
/*     */   public void writeVerboseHeader(Code_attribute paramCode_attribute, ConstantPool paramConstantPool) { String str; Method method = this.classWriter.getMethod(); try { int i = method.descriptor.getParameterCount(paramConstantPool); if (!method.access_flags.is(8))
/*     */         i++;  str = Integer.toString(i); } catch (ConstantPoolException constantPoolException) { str = report(constantPoolException); } catch (DescriptorException descriptorException) { str = report(descriptorException); }  println("stack=" + paramCode_attribute.max_stack + ", locals=" + paramCode_attribute.max_locals + ", args_size=" + str); }
/*     */   public void writeInstrs(Code_attribute paramCode_attribute) { List<InstructionDetailWriter> list = getDetailWriters(paramCode_attribute); for (Instruction instruction : paramCode_attribute.getInstructions()) { try { for (InstructionDetailWriter instructionDetailWriter : list)
/*     */           instructionDetailWriter.writeDetails(instruction);  writeInstr(instruction); } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) { println(report("error at or after byte " + instruction.getPC())); break; }  }  for (InstructionDetailWriter instructionDetailWriter : list)
/*     */       instructionDetailWriter.flush();  }
/* 234 */   public void writeInstr(Instruction paramInstruction) { print(String.format("%4d: %-13s ", new Object[] { Integer.valueOf(paramInstruction.getPC()), paramInstruction.getMnemonic() })); int i = this.options.indentWidth; int j = (6 + i - 1) / i; paramInstruction.accept(this.instructionPrinter, Integer.valueOf(j)); println(); } private void printConstant(int paramInt) { this.constantWriter.write(paramInt); }
/*     */ 
/*     */   
/*     */   private List<InstructionDetailWriter> getDetailWriters(Code_attribute paramCode_attribute) {
/* 238 */     ArrayList<SourceWriter> arrayList = new ArrayList();
/*     */     
/* 240 */     if (this.options.details.contains(InstructionDetailWriter.Kind.SOURCE)) {
/* 241 */       this.sourceWriter.reset(this.classWriter.getClassFile(), paramCode_attribute);
/* 242 */       if (this.sourceWriter.hasSource()) {
/* 243 */         arrayList.add(this.sourceWriter);
/*     */       } else {
/* 245 */         println("(Source code not available)");
/*     */       } 
/*     */     } 
/* 248 */     if (this.options.details.contains(InstructionDetailWriter.Kind.LOCAL_VARS)) {
/* 249 */       this.localVariableTableWriter.reset(paramCode_attribute);
/* 250 */       arrayList.add(this.localVariableTableWriter);
/*     */     } 
/*     */     
/* 253 */     if (this.options.details.contains(InstructionDetailWriter.Kind.LOCAL_VAR_TYPES)) {
/* 254 */       this.localVariableTypeTableWriter.reset(paramCode_attribute);
/* 255 */       arrayList.add(this.localVariableTypeTableWriter);
/*     */     } 
/*     */     
/* 258 */     if (this.options.details.contains(InstructionDetailWriter.Kind.STACKMAPS)) {
/* 259 */       this.stackMapWriter.reset(paramCode_attribute);
/* 260 */       this.stackMapWriter.writeInitialDetails();
/* 261 */       arrayList.add(this.stackMapWriter);
/*     */     } 
/*     */     
/* 264 */     if (this.options.details.contains(InstructionDetailWriter.Kind.TRY_BLOCKS)) {
/* 265 */       this.tryBlockWriter.reset(paramCode_attribute);
/* 266 */       arrayList.add(this.tryBlockWriter);
/*     */     } 
/*     */     
/* 269 */     if (this.options.details.contains(InstructionDetailWriter.Kind.TYPE_ANNOS)) {
/* 270 */       this.typeAnnotationWriter.reset(paramCode_attribute);
/* 271 */       arrayList.add(this.typeAnnotationWriter);
/*     */     } 
/*     */     
/* 274 */     return (List)arrayList;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\CodeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */