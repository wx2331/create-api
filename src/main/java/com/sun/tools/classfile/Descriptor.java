/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Descriptor
/*     */ {
/*     */   public final int index;
/*     */   private int count;
/*     */   
/*     */   public static class InvalidDescriptor
/*     */     extends DescriptorException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     public final String desc;
/*     */     public final int index;
/*     */     
/*     */     InvalidDescriptor(String param1String) {
/*  43 */       this.desc = param1String;
/*  44 */       this.index = -1;
/*     */     }
/*     */     
/*     */     InvalidDescriptor(String param1String, int param1Int) {
/*  48 */       this.desc = param1String;
/*  49 */       this.index = param1Int;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getMessage() {
/*  55 */       if (this.index == -1) {
/*  56 */         return "invalid descriptor \"" + this.desc + "\"";
/*     */       }
/*  58 */       return "descriptor is invalid at offset " + this.index + " in \"" + this.desc + "\"";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Descriptor(ClassReader paramClassReader) throws IOException {
/*  67 */     this(paramClassReader.readUnsignedShort());
/*     */   }
/*     */   
/*     */   public Descriptor(int paramInt) {
/*  71 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue(ConstantPool paramConstantPool) throws ConstantPoolException {
/*  76 */     return paramConstantPool.getUTF8Value(this.index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParameterCount(ConstantPool paramConstantPool) throws ConstantPoolException, InvalidDescriptor {
/*  81 */     String str = getValue(paramConstantPool);
/*  82 */     int i = str.indexOf(")");
/*  83 */     if (i == -1)
/*  84 */       throw new InvalidDescriptor(str); 
/*  85 */     parse(str, 0, i + 1);
/*  86 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameterTypes(ConstantPool paramConstantPool) throws ConstantPoolException, InvalidDescriptor {
/*  92 */     String str = getValue(paramConstantPool);
/*  93 */     int i = str.indexOf(")");
/*  94 */     if (i == -1)
/*  95 */       throw new InvalidDescriptor(str); 
/*  96 */     return parse(str, 0, i + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReturnType(ConstantPool paramConstantPool) throws ConstantPoolException, InvalidDescriptor {
/* 101 */     String str = getValue(paramConstantPool);
/* 102 */     int i = str.indexOf(")");
/* 103 */     if (i == -1)
/* 104 */       throw new InvalidDescriptor(str); 
/* 105 */     return parse(str, i + 1, str.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFieldType(ConstantPool paramConstantPool) throws ConstantPoolException, InvalidDescriptor {
/* 110 */     String str = getValue(paramConstantPool);
/* 111 */     return parse(str, 0, str.length());
/*     */   }
/*     */ 
/*     */   
/*     */   private String parse(String paramString, int paramInt1, int paramInt2) throws InvalidDescriptor {
/* 116 */     int i = paramInt1;
/* 117 */     StringBuilder stringBuilder = new StringBuilder();
/* 118 */     byte b = 0;
/* 119 */     this.count = 0;
/*     */     
/* 121 */     while (i < paramInt2) {
/*     */       String str; int j;
/*     */       char c;
/* 124 */       switch (c = paramString.charAt(i++)) {
/*     */         case '(':
/* 126 */           stringBuilder.append('(');
/*     */           continue;
/*     */         
/*     */         case ')':
/* 130 */           stringBuilder.append(')');
/*     */           continue;
/*     */         
/*     */         case '[':
/* 134 */           b++;
/*     */           continue;
/*     */         
/*     */         case 'B':
/* 138 */           str = "byte";
/*     */           break;
/*     */         
/*     */         case 'C':
/* 142 */           str = "char";
/*     */           break;
/*     */         
/*     */         case 'D':
/* 146 */           str = "double";
/*     */           break;
/*     */         
/*     */         case 'F':
/* 150 */           str = "float";
/*     */           break;
/*     */         
/*     */         case 'I':
/* 154 */           str = "int";
/*     */           break;
/*     */         
/*     */         case 'J':
/* 158 */           str = "long";
/*     */           break;
/*     */         
/*     */         case 'L':
/* 162 */           j = paramString.indexOf(';', i);
/* 163 */           if (j == -1)
/* 164 */             throw new InvalidDescriptor(paramString, i - 1); 
/* 165 */           str = paramString.substring(i, j).replace('/', '.');
/* 166 */           i = j + 1;
/*     */           break;
/*     */         
/*     */         case 'S':
/* 170 */           str = "short";
/*     */           break;
/*     */         
/*     */         case 'Z':
/* 174 */           str = "boolean";
/*     */           break;
/*     */         
/*     */         case 'V':
/* 178 */           str = "void";
/*     */           break;
/*     */         
/*     */         default:
/* 182 */           throw new InvalidDescriptor(paramString, i - 1);
/*     */       } 
/*     */       
/* 185 */       if (stringBuilder.length() > 1 && stringBuilder.charAt(0) == '(')
/* 186 */         stringBuilder.append(", "); 
/* 187 */       stringBuilder.append(str);
/* 188 */       for (; b > 0; b--) {
/* 189 */         stringBuilder.append("[]");
/*     */       }
/* 191 */       this.count++;
/*     */     } 
/*     */     
/* 194 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Descriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */