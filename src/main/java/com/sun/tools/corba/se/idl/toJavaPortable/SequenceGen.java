/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.SequenceEntry;
/*     */ import com.sun.tools.corba.se.idl.SequenceGen;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SequenceGen
/*     */   implements SequenceGen, JavaGenerator
/*     */ {
/*     */   public void generate(Hashtable paramHashtable, SequenceEntry paramSequenceEntry, PrintWriter paramPrintWriter) {}
/*     */   
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  77 */     int i = paramTCOffsets.offset(paramSymtabEntry.type().fullName());
/*  78 */     if (i >= 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  86 */       paramTCOffsets.set(null);
/*  87 */       Expression expression = ((SequenceEntry)paramSymtabEntry).maxSize();
/*  88 */       if (expression == null) {
/*  89 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_recursive_sequence_tc (0, " + (i - paramTCOffsets.currentOffset()) + ");");
/*     */       } else {
/*  91 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_recursive_sequence_tc (" + Util.parseExpression(expression) + ", " + (i - paramTCOffsets.currentOffset()) + ");");
/*  92 */       }  paramTCOffsets.bumpCurrentOffset(4);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  97 */       paramTCOffsets.set(paramSymtabEntry);
/*  98 */       paramInt = ((JavaGenerator)paramSymtabEntry.type().generator()).helperType(paramInt + 1, paramString1, paramTCOffsets, paramString2, paramSymtabEntry.type(), paramPrintWriter);
/*  99 */       Expression expression = ((SequenceEntry)paramSymtabEntry).maxSize();
/* 100 */       if (expression == null) {
/* 101 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_sequence_tc (0, " + paramString2 + ");");
/*     */       } else {
/* 103 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_sequence_tc (" + Util.parseExpression(expression) + ", " + paramString2 + ");");
/*     */       } 
/* 105 */     }  paramTCOffsets.bumpCurrentOffset(4);
/* 106 */     return paramInt;
/*     */   }
/*     */   
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 110 */     int i = paramTCOffsets.offset(paramSymtabEntry.type().fullName());
/* 111 */     if (i >= 0) {
/*     */ 
/*     */       
/* 114 */       paramTCOffsets.set(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 120 */       paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_recursive_tc (\"\");");
/* 121 */       paramTCOffsets.bumpCurrentOffset(4);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 126 */       paramTCOffsets.set(paramSymtabEntry);
/* 127 */       paramInt = ((JavaGenerator)paramSymtabEntry.type().generator()).type(paramInt + 1, paramString1, paramTCOffsets, paramString2, paramSymtabEntry.type(), paramPrintWriter);
/* 128 */       Expression expression = ((SequenceEntry)paramSymtabEntry).maxSize();
/* 129 */       if (expression == null) {
/* 130 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_sequence_tc (0, " + paramString2 + ");");
/*     */       } else {
/* 132 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_sequence_tc (" + Util.parseExpression(expression) + ", " + paramString2 + ");");
/*     */       } 
/*     */     } 
/* 135 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {}
/*     */ 
/*     */   
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 148 */     SequenceEntry sequenceEntry = (SequenceEntry)paramSymtabEntry;
/* 149 */     String str1 = "_len" + paramInt++;
/* 150 */     paramPrintWriter.println(paramString1 + "int " + str1 + " = istream.read_long ();");
/* 151 */     if (sequenceEntry.maxSize() != null) {
/*     */       
/* 153 */       paramPrintWriter.println(paramString1 + "if (" + str1 + " > (" + Util.parseExpression(sequenceEntry.maxSize()) + "))");
/* 154 */       paramPrintWriter.println(paramString1 + "  throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 159 */       str2 = Util.sansArrayInfo((String)sequenceEntry.dynamicVariable(Compile.typedefInfo));
/*     */     }
/* 161 */     catch (NoSuchFieldException noSuchFieldException) {
/*     */       
/* 163 */       str2 = sequenceEntry.name();
/*     */     } 
/* 165 */     int i = str2.indexOf('[');
/* 166 */     String str3 = str2.substring(i);
/* 167 */     String str2 = str2.substring(0, i);
/*     */ 
/*     */     
/* 170 */     SymtabEntry symtabEntry = (SymtabEntry)Util.symbolTable.get(str2.replace('.', '/'));
/* 171 */     if (symtabEntry != null && symtabEntry instanceof InterfaceEntry && ((InterfaceEntry)symtabEntry).state() != null)
/*     */     {
/*     */       
/* 174 */       str2 = Util.javaName(symtabEntry);
/*     */     }
/* 176 */     str3 = str3.substring(2);
/* 177 */     paramPrintWriter.println(paramString1 + paramString2 + " = new " + str2 + '[' + str1 + ']' + str3 + ';');
/* 178 */     if (sequenceEntry.type() instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*     */ 
/*     */       
/* 181 */       if (sequenceEntry.type().name().equals("any") || sequenceEntry
/* 182 */         .type().name().equals("TypeCode") || sequenceEntry
/* 183 */         .type().name().equals("Principal")) {
/*     */         
/* 185 */         String str = "_o" + paramInt;
/* 186 */         paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 187 */         paramPrintWriter.println(paramString1 + "  " + paramString2 + '[' + str + "] = istream.read_" + sequenceEntry.type().name() + " ();");
/*     */       }
/*     */       else {
/*     */         
/* 191 */         String str = paramString2;
/* 192 */         int j = str.indexOf(' ');
/* 193 */         if (j != -1)
/* 194 */           str = str.substring(j + 1); 
/* 195 */         paramPrintWriter.println(paramString1 + "istream.read_" + Util.collapseName(paramSymtabEntry.type().name()) + "_array (" + str + ", 0, " + str1 + ");");
/*     */       } 
/* 197 */     } else if (paramSymtabEntry.type() instanceof com.sun.tools.corba.se.idl.StringEntry) {
/*     */       
/* 199 */       String str = "_o" + paramInt;
/* 200 */       paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 201 */       paramPrintWriter.println(paramString1 + "  " + paramString2 + '[' + str + "] = istream.read_" + sequenceEntry.type().name() + " ();");
/*     */     }
/* 203 */     else if (paramSymtabEntry.type() instanceof SequenceEntry) {
/*     */       
/* 205 */       String str = "_o" + paramInt;
/* 206 */       paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 207 */       paramPrintWriter.println(paramString1 + '{');
/* 208 */       paramInt = ((JavaGenerator)sequenceEntry.type().generator()).read(paramInt, paramString1 + "  ", paramString2 + '[' + str + ']', sequenceEntry.type(), paramPrintWriter);
/* 209 */       paramPrintWriter.println(paramString1 + '}');
/*     */     }
/*     */     else {
/*     */       
/* 213 */       String str4 = paramString2;
/* 214 */       int j = str4.indexOf(' ');
/* 215 */       if (j != -1)
/* 216 */         str4 = str4.substring(j + 1); 
/* 217 */       String str5 = "_o" + paramInt;
/* 218 */       paramPrintWriter.println(paramString1 + "for (int " + str5 + " = 0;" + str5 + " < " + str4 + ".length; ++" + str5 + ')');
/* 219 */       paramPrintWriter.println(paramString1 + "  " + str4 + '[' + str5 + "] = " + Util.helperName(sequenceEntry.type(), true) + ".read (istream);");
/*     */     } 
/* 221 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 226 */     SequenceEntry sequenceEntry = (SequenceEntry)paramSymtabEntry;
/* 227 */     if (sequenceEntry.maxSize() != null) {
/*     */       
/* 229 */       paramPrintWriter.println(paramString1 + "if (" + paramString2 + ".length > (" + Util.parseExpression(sequenceEntry.maxSize()) + "))");
/* 230 */       paramPrintWriter.println(paramString1 + "  throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);");
/*     */     } 
/* 232 */     paramPrintWriter.println(paramString1 + "ostream.write_long (" + paramString2 + ".length);");
/* 233 */     if (paramSymtabEntry.type() instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*     */ 
/*     */       
/* 236 */       if (paramSymtabEntry.type().name().equals("any") || paramSymtabEntry
/* 237 */         .type().name().equals("TypeCode") || paramSymtabEntry
/* 238 */         .type().name().equals("Principal"))
/*     */       
/* 240 */       { String str = "_i" + paramInt++;
/* 241 */         paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 242 */         paramPrintWriter.println(paramString1 + "  ostream.write_" + sequenceEntry.type().name() + " (" + paramString2 + '[' + str + "]);"); }
/*     */       else
/*     */       
/* 245 */       { paramPrintWriter.println(paramString1 + "ostream.write_" + Util.collapseName(paramSymtabEntry.type().name()) + "_array (" + paramString2 + ", 0, " + paramString2 + ".length);"); } 
/* 246 */     } else if (paramSymtabEntry.type() instanceof com.sun.tools.corba.se.idl.StringEntry) {
/*     */       
/* 248 */       String str = "_i" + paramInt++;
/* 249 */       paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 250 */       paramPrintWriter.println(paramString1 + "  ostream.write_" + sequenceEntry.type().name() + " (" + paramString2 + '[' + str + "]);");
/*     */     }
/* 252 */     else if (paramSymtabEntry.type() instanceof SequenceEntry) {
/*     */       
/* 254 */       String str = "_i" + paramInt++;
/* 255 */       paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 256 */       paramPrintWriter.println(paramString1 + '{');
/* 257 */       paramInt = ((JavaGenerator)sequenceEntry.type().generator()).write(paramInt, paramString1 + "  ", paramString2 + '[' + str + ']', sequenceEntry.type(), paramPrintWriter);
/* 258 */       paramPrintWriter.println(paramString1 + '}');
/*     */     }
/*     */     else {
/*     */       
/* 262 */       String str = "_i" + paramInt++;
/* 263 */       paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 264 */       paramPrintWriter.println(paramString1 + "  " + Util.helperName(sequenceEntry.type(), true) + ".write (ostream, " + paramString2 + '[' + str + "]);");
/*     */     } 
/* 266 */     return paramInt;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\SequenceGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */