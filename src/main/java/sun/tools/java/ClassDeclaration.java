/*     */ package sun.tools.java;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassDeclaration
/*     */   implements Constants
/*     */ {
/*     */   int status;
/*     */   Type type;
/*     */   ClassDefinition definition;
/*     */   private boolean found;
/*     */   
/*     */   public ClassDeclaration(Identifier paramIdentifier) {
/* 117 */     this.found = false;
/*     */     this.type = Type.tClass(paramIdentifier);
/*     */   }
/*     */   
/*     */   public int getStatus() {
/*     */     return this.status;
/*     */   }
/*     */   
/*     */   public ClassDefinition getClassDefinition(Environment paramEnvironment) throws ClassNotFound {
/* 126 */     paramEnvironment.dtEvent("getClassDefinition: " + 
/* 127 */         getName() + ", status " + getStatus());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     if (this.found) {
/* 134 */       return this.definition;
/*     */     }
/*     */     
/*     */     while (true) {
/* 138 */       switch (this.status) {
/*     */         case 0:
/*     */         case 1:
/*     */         case 3:
/* 142 */           paramEnvironment.loadDefinition(this);
/*     */           continue;
/*     */ 
/*     */ 
/*     */         
/*     */         case 2:
/*     */         case 4:
/* 149 */           if (!this.definition.isInsideLocal())
/*     */           {
/*     */             
/* 152 */             this.definition.basicCheck(paramEnvironment);
/*     */           }
/*     */           
/* 155 */           this.found = true;
/* 156 */           return this.definition;
/*     */         
/*     */         case 5:
/*     */         case 6:
/* 160 */           this.found = true;
/* 161 */           return this.definition;
/*     */       }  break;
/*     */     } 
/* 164 */     throw new ClassNotFound(getName());
/*     */   }
/*     */   public Identifier getName() { return this.type.getClassName(); } public Type getType() { return this.type; } public boolean isDefined() {
/*     */     switch (this.status) {
/*     */       case 2:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */         return true;
/*     */     } 
/*     */     return false;
/*     */   } public ClassDefinition getClassDefinitionNoCheck(Environment paramEnvironment) throws ClassNotFound {
/* 176 */     paramEnvironment.dtEvent("getClassDefinition: " + 
/* 177 */         getName() + ", status " + getStatus());
/*     */     while (true) {
/* 179 */       switch (this.status) {
/*     */         case 0:
/*     */         case 1:
/*     */         case 3:
/* 183 */           paramEnvironment.loadDefinition(this);
/*     */           continue;
/*     */         
/*     */         case 2:
/*     */         case 4:
/*     */         case 5:
/*     */         case 6:
/* 190 */           return this.definition;
/*     */       }  break;
/*     */     } 
/* 193 */     throw new ClassNotFound(getName());
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
/*     */   public void setDefinition(ClassDefinition paramClassDefinition, int paramInt) {
/* 206 */     if (paramClassDefinition != null && !getName().equals(paramClassDefinition.getName())) {
/* 207 */       throw new CompilerError("setDefinition: name mismatch: " + this + ", " + paramClassDefinition);
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
/* 225 */     this.definition = paramClassDefinition;
/* 226 */     this.status = paramInt;
/*     */   }
/*     */   public ClassDefinition getClassDefinition() {
/*     */     return this.definition;
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 233 */     if (paramObject instanceof ClassDeclaration) {
/* 234 */       return this.type.equals(((ClassDeclaration)paramObject).type);
/*     */     }
/* 236 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 241 */     return this.type.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 248 */     String str1 = getName().toString();
/* 249 */     String str2 = "type ";
/* 250 */     String str3 = getName().isInner() ? "nested " : "";
/* 251 */     if (getClassDefinition() != null) {
/* 252 */       if (getClassDefinition().isInterface()) {
/* 253 */         str2 = "interface ";
/*     */       } else {
/* 255 */         str2 = "class ";
/*     */       } 
/* 257 */       if (!getClassDefinition().isTopLevel()) {
/* 258 */         str3 = "inner ";
/* 259 */         if (getClassDefinition().isLocal()) {
/* 260 */           str3 = "local ";
/* 261 */           if (!getClassDefinition().isAnonymous()) {
/* 262 */             str1 = getClassDefinition().getLocalName() + " (" + str1 + ")";
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 268 */     return str3 + str2 + str1;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\ClassDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */