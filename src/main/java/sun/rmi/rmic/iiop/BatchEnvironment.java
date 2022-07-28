/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import sun.rmi.rmic.BatchEnvironment;
/*     */ import sun.rmi.rmic.Main;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.ClassPath;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BatchEnvironment
/*     */   extends BatchEnvironment
/*     */   implements Constants
/*     */ {
/*     */   private boolean parseNonConforming = false;
/*     */   private boolean standardPackage;
/*  68 */   HashSet alreadyChecked = new HashSet();
/*  69 */   Hashtable allTypes = new Hashtable<>(3001, 0.5F);
/*  70 */   Hashtable invalidTypes = new Hashtable<>(256, 0.5F);
/*  71 */   DirectoryLoader loader = null;
/*  72 */   ClassPathLoader classPathLoader = null;
/*  73 */   Hashtable nameContexts = null;
/*  74 */   Hashtable namesCache = new Hashtable<>();
/*  75 */   NameContext modulesContext = new NameContext(false);
/*     */   
/*  77 */   ClassDefinition defRemote = null;
/*  78 */   ClassDefinition defError = null;
/*  79 */   ClassDefinition defException = null;
/*  80 */   ClassDefinition defRemoteException = null;
/*  81 */   ClassDefinition defCorbaObject = null;
/*  82 */   ClassDefinition defSerializable = null;
/*  83 */   ClassDefinition defExternalizable = null;
/*  84 */   ClassDefinition defThrowable = null;
/*  85 */   ClassDefinition defRuntimeException = null;
/*  86 */   ClassDefinition defIDLEntity = null;
/*  87 */   ClassDefinition defValueBase = null;
/*     */   
/*  89 */   Type typeRemoteException = null;
/*  90 */   Type typeIOException = null;
/*  91 */   Type typeException = null;
/*  92 */   Type typeThrowable = null;
/*     */   
/*  94 */   ContextStack contextStack = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath, Main paramMain) {
/* 102 */     super(paramOutputStream, paramClassPath, paramMain);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 107 */       this
/* 108 */         .defRemote = getClassDeclaration(idRemote).getClassDefinition((Environment)this);
/* 109 */       this
/* 110 */         .defError = getClassDeclaration(idJavaLangError).getClassDefinition((Environment)this);
/* 111 */       this
/* 112 */         .defException = getClassDeclaration(idJavaLangException).getClassDefinition((Environment)this);
/* 113 */       this
/* 114 */         .defRemoteException = getClassDeclaration(idRemoteException).getClassDefinition((Environment)this);
/* 115 */       this
/* 116 */         .defCorbaObject = getClassDeclaration(idCorbaObject).getClassDefinition((Environment)this);
/* 117 */       this
/* 118 */         .defSerializable = getClassDeclaration(idJavaIoSerializable).getClassDefinition((Environment)this);
/* 119 */       this
/* 120 */         .defRuntimeException = getClassDeclaration(idJavaLangRuntimeException).getClassDefinition((Environment)this);
/* 121 */       this
/* 122 */         .defExternalizable = getClassDeclaration(idJavaIoExternalizable).getClassDefinition((Environment)this);
/* 123 */       this
/* 124 */         .defThrowable = getClassDeclaration(idJavaLangThrowable).getClassDefinition((Environment)this);
/* 125 */       this
/* 126 */         .defIDLEntity = getClassDeclaration(idIDLEntity).getClassDefinition((Environment)this);
/* 127 */       this
/* 128 */         .defValueBase = getClassDeclaration(idValueBase).getClassDefinition((Environment)this);
/* 129 */       this.typeRemoteException = this.defRemoteException.getClassDeclaration().getType();
/* 130 */       this.typeException = this.defException.getClassDeclaration().getType();
/* 131 */       this.typeIOException = getClassDeclaration(idJavaIoIOException).getType();
/* 132 */       this.typeThrowable = getClassDeclaration(idJavaLangThrowable).getType();
/*     */       
/* 134 */       this.classPathLoader = new ClassPathLoader(paramClassPath);
/*     */     }
/* 136 */     catch (ClassNotFound classNotFound) {
/* 137 */       error(0L, "rmic.class.not.found", classNotFound.name);
/* 138 */       throw new Error();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getParseNonConforming() {
/* 146 */     return this.parseNonConforming;
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
/*     */   public void setParseNonConforming(boolean paramBoolean) {
/* 158 */     if (paramBoolean && !this.parseNonConforming) {
/* 159 */       reset();
/*     */     }
/*     */     
/* 162 */     this.parseNonConforming = paramBoolean;
/*     */   }
/*     */   
/*     */   void setStandardPackage(boolean paramBoolean) {
/* 166 */     this.standardPackage = paramBoolean;
/*     */   }
/*     */   
/*     */   boolean getStandardPackage() {
/* 170 */     return this.standardPackage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*     */     Enumeration<Type> enumeration;
/* 181 */     for (enumeration = this.allTypes.elements(); enumeration.hasMoreElements(); ) {
/* 182 */       Type type = enumeration.nextElement();
/* 183 */       type.destroy();
/*     */     } 
/*     */     
/* 186 */     for (enumeration = this.invalidTypes.keys(); enumeration.hasMoreElements(); ) {
/* 187 */       Type type = enumeration.nextElement();
/* 188 */       type.destroy();
/*     */     } 
/*     */     
/* 191 */     for (Type type : this.alreadyChecked)
/*     */     {
/* 193 */       type.destroy();
/*     */     }
/*     */     
/* 196 */     if (this.contextStack != null) this.contextStack.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     if (this.nameContexts != null) {
/* 202 */       for (enumeration = this.nameContexts.elements(); enumeration.hasMoreElements(); ) {
/* 203 */         NameContext nameContext = (NameContext)enumeration.nextElement();
/* 204 */         nameContext.clear();
/*     */       } 
/* 206 */       this.nameContexts.clear();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 211 */     this.allTypes.clear();
/* 212 */     this.invalidTypes.clear();
/* 213 */     this.alreadyChecked.clear();
/* 214 */     this.namesCache.clear();
/* 215 */     this.modulesContext.clear();
/*     */ 
/*     */     
/* 218 */     this.loader = null;
/* 219 */     this.parseNonConforming = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 228 */     if (this.alreadyChecked != null) {
/*     */ 
/*     */ 
/*     */       
/* 232 */       reset();
/*     */ 
/*     */       
/* 235 */       this.alreadyChecked = null;
/* 236 */       this.allTypes = null;
/* 237 */       this.invalidTypes = null;
/* 238 */       this.nameContexts = null;
/* 239 */       this.namesCache = null;
/* 240 */       this.modulesContext = null;
/* 241 */       this.defRemote = null;
/* 242 */       this.defError = null;
/* 243 */       this.defException = null;
/* 244 */       this.defRemoteException = null;
/* 245 */       this.defCorbaObject = null;
/* 246 */       this.defSerializable = null;
/* 247 */       this.defExternalizable = null;
/* 248 */       this.defThrowable = null;
/* 249 */       this.defRuntimeException = null;
/* 250 */       this.defIDLEntity = null;
/* 251 */       this.defValueBase = null;
/* 252 */       this.typeRemoteException = null;
/* 253 */       this.typeIOException = null;
/* 254 */       this.typeException = null;
/* 255 */       this.typeThrowable = null;
/*     */       
/* 257 */       super.shutdown();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\BatchEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */