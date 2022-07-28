/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BinaryConstantPool
/*     */   implements Constants
/*     */ {
/*     */   private byte[] types;
/*     */   private Object[] cpool;
/*     */   Hashtable indexHashObject;
/*     */   Hashtable indexHashAscii;
/*     */   Vector MoreStuff;
/*     */   
/*     */   BinaryConstantPool(DataInputStream paramDataInputStream) throws IOException {
/*  52 */     this.types = new byte[paramDataInputStream.readUnsignedShort()];
/*  53 */     this.cpool = new Object[this.types.length];
/*  54 */     for (byte b = 1; b < this.cpool.length; b++) {
/*  55 */       byte b1 = b;
/*     */       
/*  57 */       this.types[b] = paramDataInputStream.readByte(); switch (paramDataInputStream.readByte()) {
/*     */         case 1:
/*  59 */           this.cpool[b] = paramDataInputStream.readUTF();
/*     */           break;
/*     */         
/*     */         case 3:
/*  63 */           this.cpool[b] = new Integer(paramDataInputStream.readInt());
/*     */           break;
/*     */         case 4:
/*  66 */           this.cpool[b] = new Float(paramDataInputStream.readFloat());
/*     */           break;
/*     */         case 5:
/*  69 */           this.cpool[b++] = new Long(paramDataInputStream.readLong());
/*     */           break;
/*     */         case 6:
/*  72 */           this.cpool[b++] = new Double(paramDataInputStream.readDouble());
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 7:
/*     */         case 8:
/*  79 */           this.cpool[b] = new Integer(paramDataInputStream.readUnsignedShort());
/*     */           break;
/*     */ 
/*     */         
/*     */         case 9:
/*     */         case 10:
/*     */         case 11:
/*     */         case 12:
/*  87 */           this.cpool[b] = new Integer(paramDataInputStream.readUnsignedShort() << 16 | paramDataInputStream.readUnsignedShort());
/*     */           break;
/*     */         
/*     */         case 15:
/*  91 */           this.cpool[b] = readBytes(paramDataInputStream, 3);
/*     */           break;
/*     */         case 16:
/*  94 */           this.cpool[b] = readBytes(paramDataInputStream, 2);
/*     */           break;
/*     */         case 18:
/*  97 */           this.cpool[b] = readBytes(paramDataInputStream, 4);
/*     */           break;
/*     */ 
/*     */         
/*     */         default:
/* 102 */           throw new ClassFormatError("invalid constant type: " + this.types[b]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private byte[] readBytes(DataInputStream paramDataInputStream, int paramInt) throws IOException {
/* 108 */     byte[] arrayOfByte = new byte[paramInt];
/* 109 */     paramDataInputStream.readFully(arrayOfByte);
/* 110 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInteger(int paramInt) {
/* 117 */     return (paramInt == 0) ? 0 : ((Number)this.cpool[paramInt]).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue(int paramInt) {
/* 124 */     return (paramInt == 0) ? null : this.cpool[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(int paramInt) {
/* 131 */     return (paramInt == 0) ? null : (String)this.cpool[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Identifier getIdentifier(int paramInt) {
/* 138 */     return (paramInt == 0) ? null : Identifier.lookup(getString(paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDeclaration getDeclarationFromName(Environment paramEnvironment, int paramInt) {
/* 145 */     return (paramInt == 0) ? null : paramEnvironment.getClassDeclaration(Identifier.lookup(getString(paramInt).replace('/', '.')));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDeclaration getDeclaration(Environment paramEnvironment, int paramInt) {
/* 152 */     return (paramInt == 0) ? null : getDeclarationFromName(paramEnvironment, getInteger(paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType(int paramInt) {
/* 159 */     return Type.tType(getString(paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getConstantType(int paramInt) {
/* 166 */     return this.types[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getConstant(int paramInt, Environment paramEnvironment) {
/* 173 */     int i = getConstantType(paramInt);
/* 174 */     switch (i) {
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 15:
/*     */       case 16:
/*     */       case 18:
/* 182 */         return getValue(paramInt);
/*     */       
/*     */       case 7:
/* 185 */         return getDeclaration(paramEnvironment, paramInt);
/*     */       
/*     */       case 8:
/* 188 */         return getString(getInteger(paramInt));
/*     */       
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */         try {
/* 194 */           int j = getInteger(paramInt);
/*     */           
/* 196 */           ClassDefinition classDefinition = getDeclaration(paramEnvironment, j >> 16).getClassDefinition(paramEnvironment);
/* 197 */           int k = getInteger(j & 0xFFFF);
/* 198 */           Identifier identifier = getIdentifier(k >> 16);
/* 199 */           Type type = getType(k & 0xFFFF);
/*     */           
/* 201 */           MemberDefinition memberDefinition = classDefinition.getFirstMatch(identifier);
/* 202 */           for (; memberDefinition != null; 
/* 203 */             memberDefinition = memberDefinition.getNextMatch()) {
/* 204 */             Type type1 = memberDefinition.getType();
/* 205 */             if ((i == 9) ? (type1 == type) : type1
/*     */               
/* 207 */               .equalArguments(type))
/* 208 */               return memberDefinition; 
/*     */           } 
/* 210 */         } catch (ClassNotFound classNotFound) {}
/*     */         
/* 212 */         return null;
/*     */     } 
/*     */     
/* 215 */     throw new ClassFormatError("invalid constant type: " + i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getDependencies(Environment paramEnvironment) {
/* 226 */     Vector<ClassDeclaration> vector = new Vector();
/* 227 */     for (byte b = 1; b < this.cpool.length; b++) {
/* 228 */       switch (this.types[b]) {
/*     */         case 7:
/* 230 */           vector.addElement(getDeclarationFromName(paramEnvironment, getInteger(b)));
/*     */           break;
/*     */       } 
/*     */     } 
/* 234 */     return vector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexObject(Object paramObject, Environment paramEnvironment) {
/* 244 */     if (this.indexHashObject == null)
/* 245 */       createIndexHash(paramEnvironment); 
/* 246 */     Integer integer = (Integer)this.indexHashObject.get(paramObject);
/* 247 */     if (integer == null)
/* 248 */       throw new IndexOutOfBoundsException("Cannot find object " + paramObject + " of type " + paramObject
/* 249 */           .getClass() + " in constant pool"); 
/* 250 */     return integer.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexString(String paramString, Environment paramEnvironment) {
/* 258 */     if (this.indexHashObject == null)
/* 259 */       createIndexHash(paramEnvironment); 
/* 260 */     Integer integer = (Integer)this.indexHashAscii.get(paramString);
/* 261 */     if (integer == null) {
/* 262 */       if (this.MoreStuff == null) this.MoreStuff = new Vector(); 
/* 263 */       integer = new Integer(this.cpool.length + this.MoreStuff.size());
/* 264 */       this.MoreStuff.addElement(paramString);
/* 265 */       this.indexHashAscii.put(paramString, integer);
/*     */     } 
/* 267 */     return integer.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createIndexHash(Environment paramEnvironment) {
/* 276 */     this.indexHashObject = new Hashtable<>();
/* 277 */     this.indexHashAscii = new Hashtable<>();
/* 278 */     for (byte b = 1; b < this.cpool.length; b++) {
/* 279 */       if (this.types[b] == 1) {
/* 280 */         this.indexHashAscii.put(this.cpool[b], new Integer(b));
/*     */       } else {
/*     */         try {
/* 283 */           this.indexHashObject.put(getConstant(b, paramEnvironment), new Integer(b));
/* 284 */         } catch (ClassFormatError classFormatError) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutputStream paramDataOutputStream, Environment paramEnvironment) throws IOException {
/* 295 */     int i = this.cpool.length;
/* 296 */     if (this.MoreStuff != null)
/* 297 */       i += this.MoreStuff.size(); 
/* 298 */     paramDataOutputStream.writeShort(i); int j;
/* 299 */     for (j = 1; j < this.cpool.length; j++) {
/* 300 */       int k; byte b = this.types[j];
/* 301 */       Object object = this.cpool[j];
/* 302 */       paramDataOutputStream.writeByte(b);
/* 303 */       switch (b) {
/*     */         case 1:
/* 305 */           paramDataOutputStream.writeUTF((String)object);
/*     */           break;
/*     */         case 3:
/* 308 */           paramDataOutputStream.writeInt(((Number)object).intValue());
/*     */           break;
/*     */         case 4:
/* 311 */           paramDataOutputStream.writeFloat(((Number)object).floatValue());
/*     */           break;
/*     */         case 5:
/* 314 */           paramDataOutputStream.writeLong(((Number)object).longValue());
/* 315 */           j++;
/*     */           break;
/*     */         case 6:
/* 318 */           paramDataOutputStream.writeDouble(((Number)object).doubleValue());
/* 319 */           j++;
/*     */           break;
/*     */         case 7:
/*     */         case 8:
/* 323 */           paramDataOutputStream.writeShort(((Number)object).intValue());
/*     */           break;
/*     */         case 9:
/*     */         case 10:
/*     */         case 11:
/*     */         case 12:
/* 329 */           k = ((Number)object).intValue();
/* 330 */           paramDataOutputStream.writeShort(k >> 16);
/* 331 */           paramDataOutputStream.writeShort(k & 0xFFFF);
/*     */           break;
/*     */         
/*     */         case 15:
/*     */         case 16:
/*     */         case 18:
/* 337 */           paramDataOutputStream.write((byte[])object, 0, ((byte[])object).length);
/*     */           break;
/*     */         default:
/* 340 */           throw new ClassFormatError("invalid constant type: " + this.types[j]);
/*     */       } 
/*     */     
/*     */     } 
/* 344 */     for (j = this.cpool.length; j < i; j++) {
/* 345 */       String str = this.MoreStuff.elementAt(j - this.cpool.length);
/* 346 */       paramDataOutputStream.writeByte(1);
/* 347 */       paramDataOutputStream.writeUTF(str);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\BinaryConstantPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */