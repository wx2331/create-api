/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Imports
/*     */   implements Constants
/*     */ {
/*  59 */   Identifier currentPackage = idNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   long currentPackageWhere = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   Hashtable classes = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   Vector packages = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   Vector singles = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int checked;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Imports(Environment paramEnvironment) {
/*  94 */     addPackage(idJavaLang);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void resolve(Environment paramEnvironment) {
/* 101 */     if (this.checked != 0) {
/*     */       return;
/*     */     }
/* 104 */     this.checked = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     Vector<IdentifierToken> vector = new Vector(); Enumeration<IdentifierToken> enumeration;
/* 138 */     for (enumeration = this.packages.elements(); enumeration.hasMoreElements(); ) {
/* 139 */       IdentifierToken identifierToken = enumeration.nextElement();
/* 140 */       Identifier identifier = identifierToken.getName();
/* 141 */       long l = identifierToken.getWhere();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 147 */       if (paramEnvironment.isExemptPackage(identifier)) {
/* 148 */         vector.addElement(identifierToken);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       try {
/* 154 */         Identifier identifier1 = paramEnvironment.resolvePackageQualifiedName(identifier);
/* 155 */         if (importable(identifier1, paramEnvironment)) {
/*     */           
/* 157 */           if (paramEnvironment.getPackage(identifier1.getTopName()).exists()) {
/* 158 */             paramEnvironment.error(l, "class.and.package", identifier1
/* 159 */                 .getTopName());
/*     */           }
/*     */           
/* 162 */           if (!identifier1.isInner())
/* 163 */             identifier1 = Identifier.lookupInner(identifier1, idNull); 
/* 164 */           identifier = identifier1;
/* 165 */         } else if (!paramEnvironment.getPackage(identifier).exists()) {
/* 166 */           paramEnvironment.error(l, "package.not.found", identifier, "import");
/* 167 */         } else if (identifier1.isInner()) {
/*     */           
/* 169 */           paramEnvironment.error(l, "class.and.package", identifier1.getTopName());
/*     */         } 
/* 171 */         vector.addElement(new IdentifierToken(l, identifier));
/* 172 */       } catch (IOException iOException) {
/* 173 */         paramEnvironment.error(l, "io.exception", "import");
/*     */       } 
/*     */     } 
/* 176 */     this.packages = vector;
/*     */     
/* 178 */     for (enumeration = this.singles.elements(); enumeration.hasMoreElements(); ) {
/* 179 */       IdentifierToken identifierToken = enumeration.nextElement();
/* 180 */       Identifier identifier1 = identifierToken.getName();
/* 181 */       long l = identifierToken.getWhere();
/* 182 */       Identifier identifier2 = identifier1.getQualifier();
/*     */ 
/*     */       
/* 185 */       identifier1 = paramEnvironment.resolvePackageQualifiedName(identifier1);
/* 186 */       if (!paramEnvironment.classExists(identifier1.getTopName())) {
/* 187 */         paramEnvironment.error(l, "class.not.found", identifier1, "import");
/*     */       }
/*     */ 
/*     */       
/* 191 */       Identifier identifier3 = identifier1.getFlatName().getName();
/*     */ 
/*     */       
/* 194 */       Identifier identifier4 = (Identifier)this.classes.get(identifier3);
/* 195 */       if (identifier4 != null) {
/* 196 */         Identifier identifier5 = Identifier.lookup(identifier4.getQualifier(), identifier4
/* 197 */             .getFlatName());
/* 198 */         Identifier identifier6 = Identifier.lookup(identifier1.getQualifier(), identifier1
/* 199 */             .getFlatName());
/* 200 */         if (!identifier5.equals(identifier6)) {
/* 201 */           paramEnvironment.error(l, "ambig.class", identifier1, identifier4);
/*     */         }
/*     */       } 
/* 204 */       this.classes.put(identifier3, identifier1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 224 */         ClassDeclaration classDeclaration = paramEnvironment.getClassDeclaration(identifier1);
/*     */ 
/*     */         
/* 227 */         ClassDefinition classDefinition = classDeclaration.getClassDefinitionNoCheck(paramEnvironment);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 232 */         Identifier identifier = classDefinition.getName().getQualifier();
/*     */ 
/*     */ 
/*     */         
/* 236 */         for (; classDefinition != null; classDefinition = classDefinition.getOuterClass()) {
/* 237 */           if (classDefinition.isPrivate() || (
/* 238 */             !classDefinition.isPublic() && 
/* 239 */             !identifier.equals(this.currentPackage))) {
/* 240 */             paramEnvironment.error(l, "cant.access.class", classDefinition);
/*     */             break;
/*     */           } 
/*     */         } 
/* 244 */       } catch (AmbiguousClass ambiguousClass) {
/* 245 */         paramEnvironment.error(l, "ambig.class", ambiguousClass.name1, ambiguousClass.name2);
/* 246 */       } catch (ClassNotFound classNotFound) {
/* 247 */         paramEnvironment.error(l, "class.not.found", classNotFound.name, "import");
/*     */       } 
/*     */     } 
/* 250 */     this.checked = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Identifier resolve(Environment paramEnvironment, Identifier paramIdentifier) throws ClassNotFound {
/* 261 */     paramEnvironment.dtEnter("Imports.resolve: " + paramIdentifier);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 267 */     if (paramIdentifier.hasAmbigPrefix()) {
/* 268 */       paramIdentifier = paramIdentifier.removeAmbigPrefix();
/*     */     }
/*     */     
/* 271 */     if (paramIdentifier.isQualified()) {
/*     */       
/* 273 */       paramEnvironment.dtExit("Imports.resolve: QUALIFIED " + paramIdentifier);
/* 274 */       return paramIdentifier;
/*     */     } 
/*     */     
/* 277 */     if (this.checked <= 0) {
/* 278 */       this.checked = 0;
/* 279 */       resolve(paramEnvironment);
/*     */     } 
/*     */ 
/*     */     
/* 283 */     Identifier identifier1 = (Identifier)this.classes.get(paramIdentifier);
/* 284 */     if (identifier1 != null) {
/* 285 */       paramEnvironment.dtExit("Imports.resolve: PREVIOUSLY IMPORTED " + paramIdentifier);
/* 286 */       return identifier1;
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
/* 300 */     Identifier identifier2 = Identifier.lookup(this.currentPackage, paramIdentifier);
/* 301 */     if (importable(identifier2, paramEnvironment)) {
/* 302 */       identifier1 = identifier2;
/*     */     }
/*     */     else {
/*     */       
/* 306 */       Enumeration<IdentifierToken> enumeration = this.packages.elements();
/* 307 */       while (enumeration.hasMoreElements()) {
/* 308 */         IdentifierToken identifierToken = enumeration.nextElement();
/* 309 */         identifier2 = Identifier.lookup(identifierToken.getName(), paramIdentifier);
/*     */         
/* 311 */         if (importable(identifier2, paramEnvironment)) {
/* 312 */           if (identifier1 == null) {
/*     */ 
/*     */ 
/*     */             
/* 316 */             identifier1 = identifier2;
/*     */             continue;
/*     */           } 
/* 319 */           paramEnvironment.dtExit("Imports.resolve: AMBIGUOUS " + paramIdentifier);
/*     */ 
/*     */           
/* 322 */           throw new AmbiguousClass(identifier1, identifier2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 329 */     if (identifier1 == null) {
/* 330 */       paramEnvironment.dtExit("Imports.resolve: NOT FOUND " + paramIdentifier);
/* 331 */       throw new ClassNotFound(paramIdentifier);
/*     */     } 
/*     */ 
/*     */     
/* 335 */     this.classes.put(paramIdentifier, identifier1);
/* 336 */     paramEnvironment.dtExit("Imports.resolve: FIRST IMPORT " + paramIdentifier);
/* 337 */     return identifier1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean importable(Identifier paramIdentifier, Environment paramEnvironment) {
/* 345 */     if (!paramIdentifier.isInner())
/* 346 */       return paramEnvironment.classExists(paramIdentifier); 
/* 347 */     if (!paramEnvironment.classExists(paramIdentifier.getTopName())) {
/* 348 */       return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 368 */       ClassDeclaration classDeclaration = paramEnvironment.getClassDeclaration(paramIdentifier.getTopName());
/*     */       
/* 370 */       ClassDefinition classDefinition = classDeclaration.getClassDefinitionNoCheck(paramEnvironment);
/*     */       
/* 372 */       return classDefinition.innerClassExists(paramIdentifier.getFlatName().getTail());
/* 373 */     } catch (ClassNotFound classNotFound) {
/* 374 */       return false;
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
/*     */   public synchronized Identifier forceResolve(Environment paramEnvironment, Identifier paramIdentifier) {
/* 386 */     if (paramIdentifier.isQualified()) {
/* 387 */       return paramIdentifier;
/*     */     }
/* 389 */     Identifier identifier = (Identifier)this.classes.get(paramIdentifier);
/* 390 */     if (identifier != null) {
/* 391 */       return identifier;
/*     */     }
/*     */     
/* 394 */     identifier = Identifier.lookup(this.currentPackage, paramIdentifier);
/*     */     
/* 396 */     this.classes.put(paramIdentifier, identifier);
/* 397 */     return identifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addClass(IdentifierToken paramIdentifierToken) {
/* 404 */     this.singles.addElement(paramIdentifierToken);
/*     */   }
/*     */   
/*     */   public void addClass(Identifier paramIdentifier) throws AmbiguousClass {
/* 408 */     addClass(new IdentifierToken(paramIdentifier));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addPackage(IdentifierToken paramIdentifierToken) {
/* 416 */     Identifier identifier = paramIdentifierToken.getName();
/*     */ 
/*     */ 
/*     */     
/* 420 */     if (identifier == this.currentPackage) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 426 */     int i = this.packages.size();
/* 427 */     for (byte b = 0; b < i; b++) {
/* 428 */       if (identifier == ((IdentifierToken)this.packages.elementAt(b)).getName()) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 434 */     this.packages.addElement(paramIdentifierToken);
/*     */   }
/*     */   
/*     */   public void addPackage(Identifier paramIdentifier) {
/* 438 */     addPackage(new IdentifierToken(paramIdentifier));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setCurrentPackage(IdentifierToken paramIdentifierToken) {
/* 445 */     this.currentPackage = paramIdentifierToken.getName();
/* 446 */     this.currentPackageWhere = paramIdentifierToken.getWhere();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setCurrentPackage(Identifier paramIdentifier) {
/* 453 */     this.currentPackage = paramIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Identifier getCurrentPackage() {
/* 460 */     return this.currentPackage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getImportedPackages() {
/* 468 */     return Collections.unmodifiableList(this.packages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getImportedClasses() {
/* 476 */     return Collections.unmodifiableList(this.singles);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Environment newEnvironment(Environment paramEnvironment) {
/* 483 */     return new ImportEnvironment(paramEnvironment, this);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\Imports.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */