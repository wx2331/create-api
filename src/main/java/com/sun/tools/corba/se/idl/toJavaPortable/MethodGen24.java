/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.ParameterEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodGen24
/*     */   extends MethodGen
/*     */ {
/*     */   protected void writeParmList(MethodEntry paramMethodEntry, boolean paramBoolean, PrintWriter paramPrintWriter) {
/*  79 */     boolean bool = true;
/*  80 */     Enumeration<ParameterEntry> enumeration = paramMethodEntry.parameters().elements();
/*  81 */     while (enumeration.hasMoreElements()) {
/*     */       
/*  83 */       if (bool) {
/*  84 */         bool = false;
/*     */       } else {
/*  86 */         paramPrintWriter.print(", ");
/*  87 */       }  ParameterEntry parameterEntry = enumeration.nextElement();
/*  88 */       if (paramBoolean) {
/*  89 */         writeParmType(parameterEntry.type(), parameterEntry.passType());
/*  90 */         paramPrintWriter.print(' ');
/*     */       } 
/*     */       
/*  93 */       paramPrintWriter.print(parameterEntry.name());
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
/*     */   protected void helperFactoryMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 105 */     this.symbolTable = paramHashtable;
/* 106 */     this.m = paramMethodEntry;
/* 107 */     this.stream = paramPrintWriter;
/* 108 */     String str1 = paramMethodEntry.name();
/* 109 */     String str2 = Util.javaName(paramSymtabEntry);
/* 110 */     String str3 = str2 + "ValueFactory";
/*     */ 
/*     */     
/* 113 */     paramPrintWriter.print("  public static " + str2 + " " + str1 + " (org.omg.CORBA.ORB $orb");
/*     */     
/* 115 */     if (!paramMethodEntry.parameters().isEmpty()) {
/* 116 */       paramPrintWriter.print(", ");
/*     */     }
/*     */     
/* 119 */     writeParmList(paramMethodEntry, true, paramPrintWriter);
/*     */ 
/*     */     
/* 122 */     paramPrintWriter.println(")");
/* 123 */     paramPrintWriter.println("  {");
/* 124 */     paramPrintWriter.println("    try {");
/* 125 */     paramPrintWriter.println("      " + str3 + " $factory = (" + str3 + ")");
/* 126 */     paramPrintWriter.println("          ((org.omg.CORBA_2_3.ORB) $orb).lookup_value_factory(id());");
/* 127 */     paramPrintWriter.print("      return $factory." + str1 + " (");
/* 128 */     writeParmList(paramMethodEntry, false, paramPrintWriter);
/* 129 */     paramPrintWriter.println(");");
/* 130 */     paramPrintWriter.println("    } catch (ClassCastException $ex) {");
/* 131 */     paramPrintWriter.println("      throw new org.omg.CORBA.BAD_PARAM ();");
/* 132 */     paramPrintWriter.println("    }");
/* 133 */     paramPrintWriter.println("  }");
/* 134 */     paramPrintWriter.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void abstractMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter) {
/* 142 */     this.symbolTable = paramHashtable;
/* 143 */     this.m = paramMethodEntry;
/* 144 */     this.stream = paramPrintWriter;
/* 145 */     if (paramMethodEntry.comment() != null)
/* 146 */       paramMethodEntry.comment().generate("  ", paramPrintWriter); 
/* 147 */     paramPrintWriter.print("  ");
/* 148 */     paramPrintWriter.print("public abstract ");
/* 149 */     writeMethodSignature();
/* 150 */     paramPrintWriter.println(";");
/* 151 */     paramPrintWriter.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defaultFactoryMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter) {
/* 161 */     this.symbolTable = paramHashtable;
/* 162 */     this.m = paramMethodEntry;
/* 163 */     this.stream = paramPrintWriter;
/* 164 */     String str = paramMethodEntry.container().name();
/* 165 */     paramPrintWriter.println();
/* 166 */     if (paramMethodEntry.comment() != null)
/* 167 */       paramMethodEntry.comment().generate("  ", paramPrintWriter); 
/* 168 */     paramPrintWriter.print("  public " + str + " " + paramMethodEntry.name() + " (");
/* 169 */     writeParmList(paramMethodEntry, true, paramPrintWriter);
/* 170 */     paramPrintWriter.println(")");
/* 171 */     paramPrintWriter.println("  {");
/* 172 */     paramPrintWriter.print("    return new " + str + "Impl (");
/* 173 */     writeParmList(paramMethodEntry, false, paramPrintWriter);
/* 174 */     paramPrintWriter.println(");");
/* 175 */     paramPrintWriter.println("  }");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeMethodSignature() {
/* 186 */     if (this.m.type() == null) {
/*     */ 
/*     */       
/* 189 */       if (isValueInitializer()) {
/* 190 */         this.stream.print(this.m.container().name());
/*     */       } else {
/* 192 */         this.stream.print("void");
/*     */       } 
/*     */     } else {
/*     */       
/* 196 */       this.stream.print(Util.javaName(this.m.type()));
/*     */     } 
/* 198 */     this.stream.print(' ' + this.m.name() + " (");
/*     */ 
/*     */     
/* 201 */     boolean bool = true;
/* 202 */     Enumeration<ParameterEntry> enumeration = this.m.parameters().elements();
/* 203 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 205 */       if (bool) {
/* 206 */         bool = false;
/*     */       } else {
/* 208 */         this.stream.print(", ");
/* 209 */       }  ParameterEntry parameterEntry = enumeration.nextElement();
/*     */       
/* 211 */       writeParmType(parameterEntry.type(), parameterEntry.passType());
/*     */ 
/*     */       
/* 214 */       this.stream.print(' ' + parameterEntry.name());
/*     */     } 
/*     */ 
/*     */     
/* 218 */     if (this.m.contexts().size() > 0) {
/*     */       
/* 220 */       if (!bool)
/* 221 */         this.stream.print(", "); 
/* 222 */       this.stream.print("org.omg.CORBA.Context $context");
/*     */     } 
/*     */ 
/*     */     
/* 226 */     if (this.m.exceptions().size() > 0) {
/*     */       
/* 228 */       this.stream.print(") throws ");
/* 229 */       enumeration = this.m.exceptions().elements();
/* 230 */       bool = true;
/* 231 */       while (enumeration.hasMoreElements()) {
/*     */         
/* 233 */         if (bool) {
/* 234 */           bool = false;
/*     */         } else {
/* 236 */           this.stream.print(", ");
/* 237 */         }  this.stream.print(Util.javaName((SymtabEntry)enumeration.nextElement()));
/*     */       } 
/*     */     } else {
/*     */       
/* 241 */       this.stream.print(')');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void interfaceMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter) {
/* 249 */     this.symbolTable = paramHashtable;
/* 250 */     this.m = paramMethodEntry;
/* 251 */     this.stream = paramPrintWriter;
/* 252 */     if (paramMethodEntry.comment() != null)
/* 253 */       paramMethodEntry.comment().generate("  ", paramPrintWriter); 
/* 254 */     paramPrintWriter.print("  ");
/* 255 */     writeMethodSignature();
/* 256 */     paramPrintWriter.println(";");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\MethodGen24.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */