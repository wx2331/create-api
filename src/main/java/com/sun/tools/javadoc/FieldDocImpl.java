/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.SerialFieldTag;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldDocImpl
/*     */   extends MemberDocImpl
/*     */   implements FieldDoc
/*     */ {
/*     */   protected final Symbol.VarSymbol sym;
/*     */   private String name;
/*     */   private String qualifiedName;
/*     */   
/*     */   public FieldDocImpl(DocEnv paramDocEnv, Symbol.VarSymbol paramVarSymbol, TreePath paramTreePath) {
/*  62 */     super(paramDocEnv, (Symbol)paramVarSymbol, paramTreePath);
/*  63 */     this.sym = paramVarSymbol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldDocImpl(DocEnv paramDocEnv, Symbol.VarSymbol paramVarSymbol) {
/*  70 */     this(paramDocEnv, paramVarSymbol, (TreePath)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getFlags() {
/*  77 */     return this.sym.flags();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Symbol.ClassSymbol getContainingClass() {
/*  84 */     return this.sym.enclClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type type() {
/*  91 */     return TypeMaker.getType(this.env, this.sym.type, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object constantValue() {
/* 102 */     Object object = this.sym.getConstValue();
/* 103 */     if (object != null && this.sym.type.hasTag(TypeTag.BOOLEAN))
/*     */     {
/* 105 */       object = Boolean.valueOf((((Integer)object).intValue() != 0)); } 
/* 106 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String constantValueExpression() {
/* 118 */     return constantValueExpression(constantValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String constantValueExpression(Object paramObject) {
/* 125 */     if (paramObject == null) return null; 
/* 126 */     if (paramObject instanceof Character) return sourceForm(((Character)paramObject).charValue()); 
/* 127 */     if (paramObject instanceof Byte) return sourceForm(((Byte)paramObject).byteValue()); 
/* 128 */     if (paramObject instanceof String) return sourceForm((String)paramObject); 
/* 129 */     if (paramObject instanceof Double) return sourceForm(((Double)paramObject).doubleValue(), 'd'); 
/* 130 */     if (paramObject instanceof Float) return sourceForm(((Float)paramObject).doubleValue(), 'f'); 
/* 131 */     if (paramObject instanceof Long) return paramObject + "L"; 
/* 132 */     return paramObject.toString();
/*     */   }
/*     */   
/*     */   private static String sourceForm(double paramDouble, char paramChar) {
/* 136 */     if (Double.isNaN(paramDouble))
/* 137 */       return "0" + paramChar + "/0" + paramChar; 
/* 138 */     if (paramDouble == Double.POSITIVE_INFINITY)
/* 139 */       return "1" + paramChar + "/0" + paramChar; 
/* 140 */     if (paramDouble == Double.NEGATIVE_INFINITY)
/* 141 */       return "-1" + paramChar + "/0" + paramChar; 
/* 142 */     return paramDouble + ((paramChar == 'f' || paramChar == 'F') ? ("" + paramChar) : "");
/*     */   }
/*     */   private static String sourceForm(char paramChar) {
/* 145 */     StringBuilder stringBuilder = new StringBuilder(8);
/* 146 */     stringBuilder.append('\'');
/* 147 */     sourceChar(paramChar, stringBuilder);
/* 148 */     stringBuilder.append('\'');
/* 149 */     return stringBuilder.toString();
/*     */   }
/*     */   private static String sourceForm(byte paramByte) {
/* 152 */     return "0x" + Integer.toString(paramByte & 0xFF, 16);
/*     */   }
/*     */   private static String sourceForm(String paramString) {
/* 155 */     StringBuilder stringBuilder = new StringBuilder(paramString.length() + 5);
/* 156 */     stringBuilder.append('"');
/* 157 */     for (byte b = 0; b < paramString.length(); b++) {
/* 158 */       char c = paramString.charAt(b);
/* 159 */       sourceChar(c, stringBuilder);
/*     */     } 
/* 161 */     stringBuilder.append('"');
/* 162 */     return stringBuilder.toString();
/*     */   }
/*     */   private static void sourceChar(char paramChar, StringBuilder paramStringBuilder) {
/* 165 */     switch (paramChar) { case '\b':
/* 166 */         paramStringBuilder.append("\\b"); return;
/* 167 */       case '\t': paramStringBuilder.append("\\t"); return;
/* 168 */       case '\n': paramStringBuilder.append("\\n"); return;
/* 169 */       case '\f': paramStringBuilder.append("\\f"); return;
/* 170 */       case '\r': paramStringBuilder.append("\\r"); return;
/* 171 */       case '"': paramStringBuilder.append("\\\""); return;
/* 172 */       case '\'': paramStringBuilder.append("\\'"); return;
/* 173 */       case '\\': paramStringBuilder.append("\\\\"); return; }
/*     */     
/* 175 */     if (isPrintableAscii(paramChar)) {
/* 176 */       paramStringBuilder.append(paramChar); return;
/*     */     } 
/* 178 */     unicodeEscape(paramChar, paramStringBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void unicodeEscape(char paramChar, StringBuilder paramStringBuilder) {
/* 184 */     paramStringBuilder.append("\\u");
/* 185 */     paramStringBuilder.append("0123456789abcdef".charAt(0xF & paramChar >> 12));
/* 186 */     paramStringBuilder.append("0123456789abcdef".charAt(0xF & paramChar >> 8));
/* 187 */     paramStringBuilder.append("0123456789abcdef".charAt(0xF & paramChar >> 4));
/* 188 */     paramStringBuilder.append("0123456789abcdef".charAt(0xF & paramChar >> 0));
/*     */   }
/*     */   private static boolean isPrintableAscii(char paramChar) {
/* 191 */     return (paramChar >= ' ' && paramChar <= '~');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncluded() {
/* 198 */     return (containingClass().isIncluded() && this.env.shouldDocument(this.sym));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isField() {
/* 206 */     return !isEnumConstant();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnumConstant() {
/* 215 */     return ((getFlags() & 0x4000L) != 0L && !this.env.legacyDoclet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTransient() {
/* 223 */     return Modifier.isTransient(getModifiers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVolatile() {
/* 230 */     return Modifier.isVolatile(getModifiers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSynthetic() {
/* 237 */     return ((getFlags() & 0x1000L) != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerialFieldTag[] serialFieldTags() {
/* 247 */     return comment().serialFieldTags();
/*     */   }
/*     */   
/*     */   public String name() {
/* 251 */     if (this.name == null) {
/* 252 */       this.name = this.sym.name.toString();
/*     */     }
/* 254 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String qualifiedName() {
/* 260 */     if (this.qualifiedName == null) {
/* 261 */       this.qualifiedName = this.sym.enclClass().getQualifiedName() + "." + name();
/*     */     }
/* 263 */     return this.qualifiedName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourcePosition position() {
/* 274 */     if ((this.sym.enclClass()).sourcefile == null) return null; 
/* 275 */     return SourcePositionImpl.make((this.sym.enclClass()).sourcefile, (this.tree == null) ? 0 : this.tree.pos, this.lineMap);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\FieldDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */