/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.AttributeException;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.DescriptorException;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicWriter
/*     */ {
/*     */   private String[] spaces;
/*     */   private LineWriter lineWriter;
/*     */   private PrintWriter out;
/*     */   protected Messages messages;
/*     */   
/*     */   protected BasicWriter(Context paramContext) {
/* 121 */     this.spaces = new String[80]; this.lineWriter = LineWriter.instance(paramContext); this.out = paramContext.<PrintWriter>get(PrintWriter.class); this.messages = paramContext.<Messages>get(Messages.class); if (this.messages == null) throw new AssertionError(); 
/*     */   }
/*     */   protected void print(String paramString) { this.lineWriter.print(paramString); }
/*     */   protected void print(Object paramObject) { this.lineWriter.print((paramObject == null) ? null : paramObject.toString()); }
/*     */   protected void println() { this.lineWriter.println(); }
/*     */   protected void println(String paramString) { this.lineWriter.print(paramString); this.lineWriter.println(); }
/*     */   protected void println(Object paramObject) { this.lineWriter.print((paramObject == null) ? null : paramObject.toString()); this.lineWriter.println(); } protected void indent(int paramInt) { this.lineWriter.indent(paramInt); } protected void tab() { this.lineWriter.tab(); } protected void setPendingNewline(boolean paramBoolean) { this.lineWriter.pendingNewline = paramBoolean; } protected String report(AttributeException paramAttributeException) { this.out.println("Error: " + paramAttributeException.getMessage()); return "???"; } protected String report(ConstantPoolException paramConstantPoolException) { this.out.println("Error: " + paramConstantPoolException.getMessage()); return "???"; } protected String report(DescriptorException paramDescriptorException) { this.out.println("Error: " + paramDescriptorException.getMessage()); return "???"; } protected String report(String paramString) { this.out.println("Error: " + paramString); return "???"; } protected String space(int paramInt) { if (paramInt < this.spaces.length && this.spaces[paramInt] != null) return this.spaces[paramInt];  StringBuilder stringBuilder = new StringBuilder(); for (byte b = 0; b < paramInt; b++) stringBuilder.append(" ");  String str = stringBuilder.toString(); if (paramInt < this.spaces.length) this.spaces[paramInt] = str;  return str; } private static class LineWriter
/*     */   {
/* 129 */     private final PrintWriter out; private final StringBuilder buffer; private int indentCount; static LineWriter instance(Context param1Context) { LineWriter lineWriter = param1Context.<LineWriter>get(LineWriter.class);
/* 130 */       if (lineWriter == null)
/* 131 */         lineWriter = new LineWriter(param1Context); 
/* 132 */       return lineWriter; }
/*     */     
/*     */     private final int indentWidth; private final int tabColumn; private boolean pendingNewline; private int pendingSpaces;
/*     */     protected LineWriter(Context param1Context) {
/* 136 */       param1Context.put(LineWriter.class, this);
/* 137 */       Options options = Options.instance(param1Context);
/* 138 */       this.indentWidth = options.indentWidth;
/* 139 */       this.tabColumn = options.tabColumn;
/* 140 */       this.out = param1Context.<PrintWriter>get(PrintWriter.class);
/* 141 */       this.buffer = new StringBuilder();
/*     */     }
/*     */     
/*     */     protected void print(String param1String) {
/* 145 */       if (this.pendingNewline) {
/* 146 */         println();
/* 147 */         this.pendingNewline = false;
/*     */       } 
/* 149 */       if (param1String == null)
/* 150 */         param1String = "null"; 
/* 151 */       for (byte b = 0; b < param1String.length(); b++) {
/* 152 */         char c = param1String.charAt(b);
/* 153 */         switch (c) {
/*     */           case ' ':
/* 155 */             this.pendingSpaces++;
/*     */             break;
/*     */           
/*     */           case '\n':
/* 159 */             println();
/*     */             break;
/*     */           
/*     */           default:
/* 163 */             if (this.buffer.length() == 0)
/* 164 */               indent(); 
/* 165 */             if (this.pendingSpaces > 0) {
/* 166 */               for (byte b1 = 0; b1 < this.pendingSpaces; b1++)
/* 167 */                 this.buffer.append(' '); 
/* 168 */               this.pendingSpaces = 0;
/*     */             } 
/* 170 */             this.buffer.append(c);
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void println() {
/* 178 */       this.pendingSpaces = 0;
/* 179 */       this.out.println(this.buffer);
/* 180 */       this.buffer.setLength(0);
/*     */     }
/*     */     
/*     */     protected void indent(int param1Int) {
/* 184 */       this.indentCount += param1Int;
/*     */     }
/*     */     
/*     */     protected void tab() {
/* 188 */       int i = this.indentCount * this.indentWidth + this.tabColumn;
/* 189 */       this.pendingSpaces += (i <= this.buffer.length()) ? 1 : (i - this.buffer.length());
/*     */     }
/*     */     
/*     */     private void indent() {
/* 193 */       this.pendingSpaces += this.indentCount * this.indentWidth;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\BasicWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */