/*     */ package sun.tools.javac;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.IdentifierToken;
/*     */ import sun.tools.java.Imports;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Parser;
/*     */ import sun.tools.java.Type;
/*     */ import sun.tools.tree.Node;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class BatchParser
/*     */   extends Parser
/*     */ {
/*     */   protected Identifier pkg;
/*     */   protected Imports imports;
/*     */   protected Vector classes;
/*     */   protected SourceClass sourceClass;
/*     */   protected Environment toplevelEnv;
/*     */   
/*     */   public BatchParser(Environment paramEnvironment, InputStream paramInputStream) throws IOException {
/*  76 */     super(paramEnvironment, paramInputStream);
/*     */     
/*  78 */     this.imports = new Imports(paramEnvironment);
/*  79 */     this.classes = new Vector();
/*  80 */     this.toplevelEnv = this.imports.newEnvironment(paramEnvironment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void packageDeclaration(long paramLong, IdentifierToken paramIdentifierToken) {
/*  87 */     Identifier identifier = paramIdentifierToken.getName();
/*     */     
/*  89 */     if (this.pkg == null) {
/*     */ 
/*     */ 
/*     */       
/*  93 */       this.pkg = paramIdentifierToken.getName();
/*  94 */       this.imports.setCurrentPackage(paramIdentifierToken);
/*     */     } else {
/*  96 */       this.env.error(paramLong, "package.repeated");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void importClass(long paramLong, IdentifierToken paramIdentifierToken) {
/* 105 */     this.imports.addClass(paramIdentifierToken);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void importPackage(long paramLong, IdentifierToken paramIdentifierToken) {
/* 113 */     this.imports.addPackage(paramIdentifierToken);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDefinition beginClass(long paramLong, String paramString, int paramInt, IdentifierToken paramIdentifierToken1, IdentifierToken paramIdentifierToken2, IdentifierToken[] paramArrayOfIdentifierToken) {
/* 135 */     this.toplevelEnv.dtEnter("beginClass: " + this.sourceClass);
/*     */     
/* 137 */     SourceClass sourceClass = this.sourceClass;
/*     */     
/* 139 */     if (sourceClass == null && this.pkg != null)
/*     */     {
/* 141 */       paramIdentifierToken1 = new IdentifierToken(paramIdentifierToken1.getWhere(), Identifier.lookup(this.pkg, paramIdentifierToken1.getName()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 146 */     if ((paramInt & 0x10000) != 0) {
/* 147 */       paramInt |= 0x12;
/*     */     }
/* 149 */     if ((paramInt & 0x20000) != 0) {
/* 150 */       paramInt |= 0x2;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     if ((paramInt & 0x200) != 0) {
/*     */       
/* 166 */       paramInt |= 0x400;
/* 167 */       if (sourceClass != null)
/*     */       {
/* 169 */         paramInt |= 0x8;
/*     */       }
/*     */     } 
/*     */     
/* 173 */     if (sourceClass != null && sourceClass.isInterface()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 180 */       if ((paramInt & 0x6) == 0) {
/* 181 */         paramInt |= 0x1;
/*     */       }
/* 183 */       paramInt |= 0x8;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     this
/* 197 */       .sourceClass = (SourceClass)this.toplevelEnv.makeClassDefinition(this.toplevelEnv, paramLong, paramIdentifierToken1, paramString, paramInt, paramIdentifierToken2, paramArrayOfIdentifierToken, sourceClass);
/*     */ 
/*     */ 
/*     */     
/* 201 */     this.sourceClass.getClassDeclaration().setDefinition(this.sourceClass, 4);
/* 202 */     this.env = new Environment(this.toplevelEnv, this.sourceClass);
/*     */     
/* 204 */     this.toplevelEnv.dtEvent("beginClass: SETTING UP DEPENDENCIES");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     this.toplevelEnv.dtEvent("beginClass: ADDING TO CLASS LIST");
/*     */     
/* 212 */     this.classes.addElement(this.sourceClass);
/*     */     
/* 214 */     this.toplevelEnv.dtExit("beginClass: " + this.sourceClass);
/*     */     
/* 216 */     return this.sourceClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDefinition getCurrentClass() {
/* 223 */     return this.sourceClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endClass(long paramLong, ClassDefinition paramClassDefinition) {
/* 231 */     this.toplevelEnv.dtEnter("endClass: " + this.sourceClass);
/*     */ 
/*     */     
/* 234 */     this.sourceClass.setEndPosition(paramLong);
/* 235 */     SourceClass sourceClass = (SourceClass)this.sourceClass.getOuterClass();
/* 236 */     this.sourceClass = sourceClass;
/* 237 */     this.env = this.toplevelEnv;
/* 238 */     if (this.sourceClass != null) {
/* 239 */       this.env = new Environment(this.env, this.sourceClass);
/*     */     }
/* 241 */     this.toplevelEnv.dtExit("endClass: " + this.sourceClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void defineField(long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, IdentifierToken paramIdentifierToken, IdentifierToken[] paramArrayOfIdentifierToken1, IdentifierToken[] paramArrayOfIdentifierToken2, Node paramNode) {
/* 252 */     Identifier identifier = paramIdentifierToken.getName();
/*     */ 
/*     */     
/* 255 */     if (this.sourceClass.isInterface()) {
/*     */       
/* 257 */       if ((paramInt & 0x6) == 0)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 262 */         paramInt |= 0x1;
/*     */       }
/*     */       
/* 265 */       if (paramType.isType(12)) {
/* 266 */         paramInt |= 0x400;
/*     */       } else {
/* 268 */         paramInt |= 0x18;
/*     */       } 
/*     */     } 
/* 271 */     if (identifier.equals(idInit)) {
/*     */ 
/*     */ 
/*     */       
/* 275 */       Type type = paramType.getReturnType();
/*     */       
/* 277 */       Identifier identifier1 = !type.isType(10) ? idStar : type.getClassName();
/* 278 */       Identifier identifier2 = this.sourceClass.getLocalName();
/* 279 */       if (identifier2.equals(identifier1))
/* 280 */       { paramType = Type.tMethod(Type.tVoid, paramType.getArgumentTypes()); }
/* 281 */       else if (identifier2.equals(identifier1.getFlatName().getName()))
/*     */       
/* 283 */       { paramType = Type.tMethod(Type.tVoid, paramType.getArgumentTypes());
/* 284 */         this.env.error(paramLong, "invalid.method.decl.qual"); }
/* 285 */       else { if (identifier1.isQualified() || identifier1.equals(idStar)) {
/*     */           
/* 287 */           this.env.error(paramLong, "invalid.method.decl.name");
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 293 */         this.env.error(paramLong, "invalid.method.decl");
/*     */         
/*     */         return; }
/*     */     
/*     */     } 
/* 298 */     if (paramArrayOfIdentifierToken1 == null && paramType.isType(12)) {
/* 299 */       paramArrayOfIdentifierToken1 = new IdentifierToken[0];
/*     */     }
/*     */     
/* 302 */     if (paramArrayOfIdentifierToken2 == null && paramType.isType(12)) {
/* 303 */       paramArrayOfIdentifierToken2 = new IdentifierToken[0];
/*     */     }
/*     */     
/* 306 */     MemberDefinition memberDefinition = this.env.makeMemberDefinition(this.env, paramLong, this.sourceClass, paramString, paramInt, paramType, identifier, paramArrayOfIdentifierToken1, paramArrayOfIdentifierToken2, paramNode);
/*     */ 
/*     */     
/* 309 */     if (this.env.dump())
/* 310 */       memberDefinition.print(System.out); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\javac\BatchParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */