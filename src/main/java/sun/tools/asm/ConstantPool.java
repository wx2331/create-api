/*     */ package sun.tools.asm;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.RuntimeConstants;
/*     */ import sun.tools.java.Type;
/*     */ import sun.tools.tree.StringExpression;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ConstantPool
/*     */   implements RuntimeConstants
/*     */ {
/*  45 */   Hashtable<Object, ConstantPoolData> hash = new Hashtable<>(101);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int index(Object paramObject) {
/*  51 */     return ((ConstantPoolData)this.hash.get(paramObject)).index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(Object paramObject) {
/*  58 */     ConstantPoolData constantPoolData = this.hash.get(paramObject);
/*  59 */     if (constantPoolData == null) {
/*  60 */       if (paramObject instanceof String) {
/*  61 */         constantPoolData = new StringConstantData(this, (String)paramObject);
/*  62 */       } else if (paramObject instanceof StringExpression) {
/*  63 */         constantPoolData = new StringExpressionConstantData(this, (StringExpression)paramObject);
/*  64 */       } else if (paramObject instanceof ClassDeclaration) {
/*  65 */         constantPoolData = new ClassConstantData(this, (ClassDeclaration)paramObject);
/*  66 */       } else if (paramObject instanceof Type) {
/*  67 */         constantPoolData = new ClassConstantData(this, (Type)paramObject);
/*  68 */       } else if (paramObject instanceof MemberDefinition) {
/*  69 */         constantPoolData = new FieldConstantData(this, (MemberDefinition)paramObject);
/*  70 */       } else if (paramObject instanceof NameAndTypeData) {
/*  71 */         constantPoolData = new NameAndTypeConstantData(this, (NameAndTypeData)paramObject);
/*  72 */       } else if (paramObject instanceof Number) {
/*  73 */         constantPoolData = new NumberConstantData(this, (Number)paramObject);
/*     */       } 
/*  75 */       this.hash.put(paramObject, constantPoolData);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream) throws IOException {
/*  83 */     ConstantPoolData[] arrayOfConstantPoolData = new ConstantPoolData[this.hash.size()];
/*  84 */     String[] arrayOfString = new String[arrayOfConstantPoolData.length];
/*  85 */     int i = 1; byte b1 = 0;
/*     */     
/*     */     byte b2;
/*  88 */     for (b2 = 0; b2 < 5; b2++) {
/*  89 */       byte b = b1;
/*  90 */       for (Enumeration<ConstantPoolData> enumeration = this.hash.elements(); enumeration.hasMoreElements(); ) {
/*  91 */         ConstantPoolData constantPoolData = enumeration.nextElement();
/*  92 */         if (constantPoolData.order() == b2) {
/*  93 */           arrayOfString[b1] = sortKey(constantPoolData);
/*  94 */           arrayOfConstantPoolData[b1++] = constantPoolData;
/*     */         } 
/*     */       } 
/*  97 */       xsort(arrayOfConstantPoolData, arrayOfString, b, b1 - 1);
/*     */     } 
/*     */ 
/*     */     
/* 101 */     for (b2 = 0; b2 < arrayOfConstantPoolData.length; b2++) {
/* 102 */       ConstantPoolData constantPoolData = arrayOfConstantPoolData[b2];
/* 103 */       constantPoolData.index = i;
/* 104 */       i += constantPoolData.width();
/*     */     } 
/*     */ 
/*     */     
/* 108 */     paramDataOutputStream.writeShort(i);
/*     */ 
/*     */     
/* 111 */     for (b2 = 0; b2 < b1; b2++) {
/* 112 */       arrayOfConstantPoolData[b2].write(paramEnvironment, paramDataOutputStream, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static String sortKey(ConstantPoolData paramConstantPoolData) {
/* 118 */     if (paramConstantPoolData instanceof NumberConstantData) {
/* 119 */       Number number = ((NumberConstantData)paramConstantPoolData).num;
/* 120 */       String str = number.toString();
/* 121 */       byte b = 3;
/* 122 */       if (number instanceof Integer) { b = 0; }
/* 123 */       else if (number instanceof Float) { b = 1; }
/* 124 */       else if (number instanceof Long) { b = 2; }
/* 125 */        return "\000" + (char)(str.length() + b << 8) + str;
/*     */     } 
/* 127 */     if (paramConstantPoolData instanceof StringExpressionConstantData)
/* 128 */       return (String)((StringExpressionConstantData)paramConstantPoolData).str.getValue(); 
/* 129 */     if (paramConstantPoolData instanceof FieldConstantData) {
/* 130 */       MemberDefinition memberDefinition = ((FieldConstantData)paramConstantPoolData).field;
/* 131 */       return memberDefinition.getName() + " " + memberDefinition.getType().getTypeSignature() + " " + memberDefinition
/* 132 */         .getClassDeclaration().getName();
/*     */     } 
/* 134 */     if (paramConstantPoolData instanceof NameAndTypeConstantData) {
/* 135 */       return ((NameAndTypeConstantData)paramConstantPoolData).name + " " + ((NameAndTypeConstantData)paramConstantPoolData).type;
/*     */     }
/* 137 */     if (paramConstantPoolData instanceof ClassConstantData)
/* 138 */       return ((ClassConstantData)paramConstantPoolData).name; 
/* 139 */     return ((StringConstantData)paramConstantPoolData).str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void xsort(ConstantPoolData[] paramArrayOfConstantPoolData, String[] paramArrayOfString, int paramInt1, int paramInt2) {
/* 148 */     if (paramInt1 >= paramInt2)
/*     */       return; 
/* 150 */     String str1 = paramArrayOfString[paramInt1];
/* 151 */     int i = paramInt1;
/* 152 */     int j = paramInt2;
/* 153 */     while (i < j) {
/* 154 */       while (i <= paramInt2 && paramArrayOfString[i].compareTo(str1) <= 0)
/* 155 */         i++; 
/* 156 */       while (j >= paramInt1 && paramArrayOfString[j].compareTo(str1) > 0)
/* 157 */         j--; 
/* 158 */       if (i < j) {
/*     */         
/* 160 */         ConstantPoolData constantPoolData1 = paramArrayOfConstantPoolData[i];
/* 161 */         String str = paramArrayOfString[i];
/* 162 */         paramArrayOfConstantPoolData[i] = paramArrayOfConstantPoolData[j]; paramArrayOfConstantPoolData[j] = constantPoolData1;
/* 163 */         paramArrayOfString[i] = paramArrayOfString[j]; paramArrayOfString[j] = str;
/*     */       } 
/*     */     } 
/* 166 */     int k = j;
/*     */     
/* 168 */     ConstantPoolData constantPoolData = paramArrayOfConstantPoolData[paramInt1];
/* 169 */     String str2 = paramArrayOfString[paramInt1];
/* 170 */     paramArrayOfConstantPoolData[paramInt1] = paramArrayOfConstantPoolData[k]; paramArrayOfConstantPoolData[k] = constantPoolData;
/* 171 */     paramArrayOfString[paramInt1] = paramArrayOfString[k]; paramArrayOfString[k] = str2;
/* 172 */     xsort(paramArrayOfConstantPoolData, paramArrayOfString, paramInt1, k - 1);
/* 173 */     xsort(paramArrayOfConstantPoolData, paramArrayOfString, k + 1, paramInt2);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\ConstantPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */