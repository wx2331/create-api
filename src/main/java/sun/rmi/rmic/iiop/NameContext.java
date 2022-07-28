/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NameContext
/*     */ {
/*     */   private Hashtable table;
/*     */   private boolean allowCollisions;
/*     */   
/*     */   public static synchronized NameContext forName(String paramString, boolean paramBoolean, BatchEnvironment paramBatchEnvironment) {
/*  56 */     NameContext nameContext = null;
/*     */ 
/*     */ 
/*     */     
/*  60 */     if (paramString == null)
/*     */     {
/*     */ 
/*     */       
/*  64 */       paramString = "null";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  69 */     if (paramBatchEnvironment.nameContexts == null) {
/*     */ 
/*     */ 
/*     */       
/*  73 */       paramBatchEnvironment.nameContexts = new Hashtable<>();
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  80 */       nameContext = (NameContext)paramBatchEnvironment.nameContexts.get(paramString);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  85 */     if (nameContext == null) {
/*     */ 
/*     */ 
/*     */       
/*  89 */       nameContext = new NameContext(paramBoolean);
/*     */       
/*  91 */       paramBatchEnvironment.nameContexts.put(paramString, nameContext);
/*     */     } 
/*     */     
/*  94 */     return nameContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameContext(boolean paramBoolean) {
/* 103 */     this.allowCollisions = paramBoolean;
/* 104 */     this.table = new Hashtable<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void assertPut(String paramString) throws Exception {
/* 114 */     String str = add(paramString);
/*     */     
/* 116 */     if (str != null) {
/* 117 */       throw new Exception(str);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String paramString) {
/* 126 */     if (!this.allowCollisions) {
/* 127 */       throw new Error("Must use assertPut(name)");
/*     */     }
/*     */     
/* 130 */     add(paramString);
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
/*     */   private String add(String paramString) {
/* 142 */     String str = paramString.toLowerCase();
/*     */ 
/*     */ 
/*     */     
/* 146 */     Name name = (Name)this.table.get(str);
/*     */     
/* 148 */     if (name != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 153 */       if (!paramString.equals(name.name))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 158 */         if (this.allowCollisions)
/*     */         {
/*     */ 
/*     */           
/* 162 */           name.collisions = true;
/*     */         
/*     */         }
/*     */         else
/*     */         {
/*     */           
/* 168 */           return new String("\"" + paramString + "\" and \"" + name.name + "\"");
/*     */         }
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 175 */       this.table.put(str, new Name(paramString, false));
/*     */     } 
/*     */     
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(String paramString) {
/* 187 */     Name name = (Name)this.table.get(paramString.toLowerCase());
/* 188 */     String str = paramString;
/*     */ 
/*     */ 
/*     */     
/* 192 */     if (name.collisions) {
/*     */ 
/*     */ 
/*     */       
/* 196 */       int i = paramString.length();
/* 197 */       boolean bool = true;
/*     */       
/* 199 */       for (byte b = 0; b < i; b++) {
/*     */         
/* 201 */         if (Character.isUpperCase(paramString.charAt(b))) {
/* 202 */           str = str + "_";
/* 203 */           str = str + b;
/* 204 */           bool = false;
/*     */         } 
/*     */       } 
/*     */       
/* 208 */       if (bool) {
/* 209 */         str = str + "_";
/*     */       }
/*     */     } 
/*     */     
/* 213 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 220 */     this.table.clear();
/*     */   }
/*     */   
/*     */   public class Name {
/*     */     public String name;
/*     */     public boolean collisions;
/*     */     
/*     */     public Name(String param1String, boolean param1Boolean) {
/* 228 */       this.name = param1String;
/* 229 */       this.collisions = param1Boolean;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\NameContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */