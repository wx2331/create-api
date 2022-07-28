/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import sun.tools.tree.BooleanExpression;
/*     */ import sun.tools.tree.DoubleExpression;
/*     */ import sun.tools.tree.Expression;
/*     */ import sun.tools.tree.FloatExpression;
/*     */ import sun.tools.tree.IntExpression;
/*     */ import sun.tools.tree.LocalMember;
/*     */ import sun.tools.tree.LongExpression;
/*     */ import sun.tools.tree.Node;
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
/*     */ public final class BinaryMember
/*     */   extends MemberDefinition
/*     */ {
/*     */   Expression value;
/*     */   BinaryAttribute atts;
/*     */   private boolean isConstantCache;
/*     */   private boolean isConstantCached;
/*     */   
/*     */   public BinaryMember(ClassDefinition paramClassDefinition, int paramInt, Type paramType, Identifier paramIdentifier, BinaryAttribute paramBinaryAttribute) {
/*  52 */     super(0L, paramClassDefinition, paramInt, paramType, paramIdentifier, (IdentifierToken[])null, (Node)null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     this.isConstantCache = false;
/* 147 */     this.isConstantCached = false; this.atts = paramBinaryAttribute; if (getAttribute(idDeprecated) != null) this.modifiers |= 0x40000;  if (getAttribute(idSynthetic) != null) this.modifiers |= 0x80000;  } public BinaryMember(ClassDefinition paramClassDefinition) { super(paramClassDefinition); this.isConstantCache = false; this.isConstantCached = false; } public boolean isInlineable(Environment paramEnvironment, boolean paramBoolean) { return (isConstructor() && getClassDefinition().getSuperClass() == null); }
/*     */   public Vector getArguments() { if (isConstructor() && getClassDefinition().getSuperClass() == null) { Vector<LocalMember> vector = new Vector(); vector.addElement(new LocalMember(0L, getClassDefinition(), 0, getClassDefinition().getType(), idThis)); return vector; }  return null; }
/* 149 */   public boolean isConstant() { if (!this.isConstantCached) {
/* 150 */       this
/*     */         
/* 152 */         .isConstantCache = (isFinal() && isVariable() && getAttribute(idConstantValue) != null);
/* 153 */       this.isConstantCached = true;
/*     */     } 
/* 155 */     return this.isConstantCache; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getValue(Environment paramEnvironment) {
/* 162 */     if (isMethod()) {
/* 163 */       return null;
/*     */     }
/* 165 */     if (!isFinal()) {
/* 166 */       return null;
/*     */     }
/* 168 */     if (getValue() != null) {
/* 169 */       return getValue();
/*     */     }
/* 171 */     byte[] arrayOfByte = getAttribute(idConstantValue);
/* 172 */     if (arrayOfByte == null) {
/* 173 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 177 */     try { BinaryConstantPool binaryConstantPool = ((BinaryClass)getClassDefinition()).getConstants();
/*     */       
/* 179 */       Object object = binaryConstantPool.getValue((new DataInputStream(new ByteArrayInputStream(arrayOfByte))).readUnsignedShort());
/* 180 */       switch (getType().getTypeCode()) {
/*     */         case 0:
/* 182 */           setValue((Node)new BooleanExpression(0L, (((Number)object).intValue() != 0)));
/*     */           break;
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/* 188 */           setValue((Node)new IntExpression(0L, ((Number)object).intValue()));
/*     */           break;
/*     */         case 5:
/* 191 */           setValue((Node)new LongExpression(0L, ((Number)object).longValue()));
/*     */           break;
/*     */         case 6:
/* 194 */           setValue((Node)new FloatExpression(0L, ((Number)object).floatValue()));
/*     */           break;
/*     */         case 7:
/* 197 */           setValue((Node)new DoubleExpression(0L, ((Number)object).doubleValue()));
/*     */           break;
/*     */         case 10:
/* 200 */           setValue((Node)new StringExpression(0L, (String)binaryConstantPool.getValue(((Number)object).intValue())));
/*     */           break;
/*     */       } 
/* 203 */       return getValue(); }
/* 204 */     catch (IOException iOException)
/* 205 */     { throw new CompilerError(iOException); } 
/*     */   }
/*     */   public ClassDeclaration[] getExceptions(Environment paramEnvironment) { if (!isMethod() || this.exp != null)
/*     */       return this.exp;  byte[] arrayOfByte = getAttribute(idExceptions); if (arrayOfByte == null)
/*     */       return new ClassDeclaration[0];  try { BinaryConstantPool binaryConstantPool = ((BinaryClass)getClassDefinition()).getConstants(); DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte)); int i = dataInputStream.readUnsignedShort(); this.exp = new ClassDeclaration[i]; for (byte b = 0; b < i; b++)
/*     */         this.exp[b] = binaryConstantPool.getDeclaration(paramEnvironment, dataInputStream.readUnsignedShort());  return this.exp; } catch (IOException iOException) { throw new CompilerError(iOException); }  } public String getDocumentation() { if (this.documentation != null)
/*     */       return this.documentation;  byte[] arrayOfByte = getAttribute(idDocumentation); if (arrayOfByte == null)
/*     */       return null;  try { return this.documentation = (new DataInputStream(new ByteArrayInputStream(arrayOfByte))).readUTF(); } catch (IOException iOException) { throw new CompilerError(iOException); }
/* 213 */      } public byte[] getAttribute(Identifier paramIdentifier) { for (BinaryAttribute binaryAttribute = this.atts; binaryAttribute != null; binaryAttribute = binaryAttribute.next) {
/* 214 */       if (binaryAttribute.name.equals(paramIdentifier)) {
/* 215 */         return binaryAttribute.data;
/*     */       }
/*     */     } 
/* 218 */     return null; }
/*     */ 
/*     */   
/*     */   public boolean deleteAttribute(Identifier paramIdentifier) {
/* 222 */     BinaryAttribute binaryAttribute1 = null, binaryAttribute2 = null;
/*     */     
/* 224 */     boolean bool = false;
/*     */     
/* 226 */     while (this.atts.name.equals(paramIdentifier)) {
/* 227 */       this.atts = this.atts.next;
/* 228 */       bool = true;
/*     */     } 
/* 230 */     for (binaryAttribute1 = this.atts; binaryAttribute1 != null; binaryAttribute1 = binaryAttribute2) {
/* 231 */       binaryAttribute2 = binaryAttribute1.next;
/* 232 */       if (binaryAttribute2 != null && 
/* 233 */         binaryAttribute2.name.equals(paramIdentifier)) {
/* 234 */         binaryAttribute1.next = binaryAttribute2.next;
/* 235 */         binaryAttribute2 = binaryAttribute2.next;
/* 236 */         bool = true;
/*     */       } 
/*     */     } 
/*     */     
/* 240 */     for (binaryAttribute1 = this.atts; binaryAttribute1 != null; binaryAttribute1 = binaryAttribute1.next) {
/* 241 */       if (binaryAttribute1.name.equals(paramIdentifier)) {
/* 242 */         throw new InternalError("Found attribute " + paramIdentifier);
/*     */       }
/*     */     } 
/*     */     
/* 246 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(Identifier paramIdentifier, byte[] paramArrayOfbyte, Environment paramEnvironment) {
/* 255 */     this.atts = new BinaryAttribute(paramIdentifier, paramArrayOfbyte, this.atts);
/*     */     
/* 257 */     ((BinaryClass)this.clazz).cpool.indexString(paramIdentifier.toString(), paramEnvironment);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\BinaryMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */