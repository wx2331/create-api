/*     */ package com.sun.tools.internal.ws.processor.generator;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.tools.internal.ws.processor.model.Fault;
/*     */ import com.sun.tools.internal.ws.processor.model.Port;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaInterface;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaStructureMember;
/*     */ import com.sun.tools.internal.ws.processor.modeler.ModelerConstants;
/*     */ import com.sun.tools.internal.ws.util.ClassNameInfo;
/*     */ import com.sun.xml.internal.ws.util.StringUtils;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Names
/*     */ {
/*     */   public static String getPortName(Port port) {
/*  55 */     String javaPortName = (String)port.getProperty("com.sun.xml.internal.ws.processor.model.JavaPortName");
/*  56 */     if (javaPortName != null) {
/*  57 */       return javaPortName;
/*     */     }
/*     */     
/*  60 */     QName portName = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLPortName");
/*     */     
/*  62 */     if (portName != null) {
/*  63 */       return portName.getLocalPart();
/*     */     }
/*  65 */     String name = stripQualifier(port.getJavaInterface().getName());
/*  66 */     return ClassNameInfo.replaceInnerClassSym(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String stripQualifier(String name) {
/*  73 */     return ClassNameInfo.getName(name);
/*     */   }
/*     */   
/*     */   public static String getPackageName(String className) {
/*  77 */     String packageName = ClassNameInfo.getQualifier(className);
/*  78 */     return (packageName != null) ? packageName : "";
/*     */   }
/*     */ 
/*     */   
/*     */   public static String customJavaTypeClassName(JavaInterface intf) {
/*  83 */     return intf.getName();
/*     */   }
/*     */   
/*     */   public static String customExceptionClassName(Fault fault) {
/*  87 */     return fault.getJavaException().getName();
/*     */   }
/*     */   
/*     */   public static String getExceptionClassMemberName() {
/*  91 */     return GeneratorConstants.FAULT_CLASS_MEMBER_NAME.getValue();
/*     */   }
/*     */   
/*     */   public static boolean isJavaReservedWord(String name) {
/*  95 */     return (RESERVED_WORDS.get(name) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static String getJavaReserverVarialbeName(@NotNull String name) {
/* 102 */     return (RESERVED_WORDS.get(name) == null) ? name : RESERVED_WORDS.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getJavaMemberReadMethod(JavaStructureMember member) {
/*     */     String return_value;
/* 111 */     if (member.getType().getRealName().equals(ModelerConstants.BOOLEAN_CLASSNAME.getValue())) {
/* 112 */       return_value = GeneratorConstants.IS.getValue() + StringUtils.capitalize(member.getName());
/*     */     } else {
/* 114 */       return_value = GeneratorConstants.GET.getValue() + StringUtils.capitalize(member.getName());
/*     */     } 
/* 116 */     return return_value;
/*     */   }
/*     */   
/*     */   public static String getResponseName(String messageName) {
/* 120 */     return messageName + GeneratorConstants.RESPONSE.getValue();
/*     */   }
/*     */   
/* 123 */   private static final Map<String, String> RESERVED_WORDS = new HashMap<>(53);
/*     */   
/*     */   static {
/* 126 */     RESERVED_WORDS.put("abstract", "_abstract");
/* 127 */     RESERVED_WORDS.put("assert", "_assert");
/* 128 */     RESERVED_WORDS.put("boolean", "_boolean");
/* 129 */     RESERVED_WORDS.put("break", "_break");
/* 130 */     RESERVED_WORDS.put("byte", "_byte");
/* 131 */     RESERVED_WORDS.put("case", "_case");
/* 132 */     RESERVED_WORDS.put("catch", "_catch");
/* 133 */     RESERVED_WORDS.put("char", "_char");
/* 134 */     RESERVED_WORDS.put("class", "_class");
/* 135 */     RESERVED_WORDS.put("const", "_const");
/* 136 */     RESERVED_WORDS.put("continue", "_continue");
/* 137 */     RESERVED_WORDS.put("default", "_default");
/* 138 */     RESERVED_WORDS.put("do", "_do");
/* 139 */     RESERVED_WORDS.put("double", "_double");
/* 140 */     RESERVED_WORDS.put("else", "_else");
/* 141 */     RESERVED_WORDS.put("extends", "_extends");
/* 142 */     RESERVED_WORDS.put("false", "_false");
/* 143 */     RESERVED_WORDS.put("final", "_final");
/* 144 */     RESERVED_WORDS.put("finally", "_finally");
/* 145 */     RESERVED_WORDS.put("float", "_float");
/* 146 */     RESERVED_WORDS.put("for", "_for");
/* 147 */     RESERVED_WORDS.put("goto", "_goto");
/* 148 */     RESERVED_WORDS.put("if", "_if");
/* 149 */     RESERVED_WORDS.put("implements", "_implements");
/* 150 */     RESERVED_WORDS.put("import", "_import");
/* 151 */     RESERVED_WORDS.put("instanceof", "_instanceof");
/* 152 */     RESERVED_WORDS.put("int", "_int");
/* 153 */     RESERVED_WORDS.put("interface", "_interface");
/* 154 */     RESERVED_WORDS.put("long", "_long");
/* 155 */     RESERVED_WORDS.put("native", "_native");
/* 156 */     RESERVED_WORDS.put("new", "_new");
/* 157 */     RESERVED_WORDS.put("null", "_null");
/* 158 */     RESERVED_WORDS.put("package", "_package");
/* 159 */     RESERVED_WORDS.put("private", "_private");
/* 160 */     RESERVED_WORDS.put("protected", "_protected");
/* 161 */     RESERVED_WORDS.put("public", "_public");
/* 162 */     RESERVED_WORDS.put("return", "_return");
/* 163 */     RESERVED_WORDS.put("short", "_short");
/* 164 */     RESERVED_WORDS.put("static", "_static");
/* 165 */     RESERVED_WORDS.put("strictfp", "_strictfp");
/* 166 */     RESERVED_WORDS.put("super", "_super");
/* 167 */     RESERVED_WORDS.put("switch", "_switch");
/* 168 */     RESERVED_WORDS.put("synchronized", "_synchronized");
/* 169 */     RESERVED_WORDS.put("this", "_this");
/* 170 */     RESERVED_WORDS.put("throw", "_throw");
/* 171 */     RESERVED_WORDS.put("throws", "_throws");
/* 172 */     RESERVED_WORDS.put("transient", "_transient");
/* 173 */     RESERVED_WORDS.put("true", "_true");
/* 174 */     RESERVED_WORDS.put("try", "_try");
/* 175 */     RESERVED_WORDS.put("void", "_void");
/* 176 */     RESERVED_WORDS.put("volatile", "_volatile");
/* 177 */     RESERVED_WORDS.put("while", "_while");
/* 178 */     RESERVED_WORDS.put("enum", "_enum");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\Names.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */