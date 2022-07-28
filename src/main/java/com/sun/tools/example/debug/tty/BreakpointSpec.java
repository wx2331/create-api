/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.request.BreakpointRequest;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BreakpointSpec
/*     */   extends EventRequestSpec
/*     */ {
/*     */   String methodId;
/*     */   List<String> methodArgs;
/*     */   int lineNumber;
/*     */   
/*     */   BreakpointSpec(ReferenceTypeSpec paramReferenceTypeSpec, int paramInt) {
/*  49 */     super(paramReferenceTypeSpec);
/*  50 */     this.methodId = null;
/*  51 */     this.methodArgs = null;
/*  52 */     this.lineNumber = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   BreakpointSpec(ReferenceTypeSpec paramReferenceTypeSpec, String paramString, List<String> paramList) throws MalformedMemberNameException {
/*  57 */     super(paramReferenceTypeSpec);
/*  58 */     this.methodId = paramString;
/*  59 */     this.methodArgs = paramList;
/*  60 */     this.lineNumber = 0;
/*  61 */     if (!isValidMethodName(paramString)) {
/*  62 */       throw new MalformedMemberNameException(paramString);
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
/*     */ 
/*     */   
/*     */   EventRequest resolveEventRequest(ReferenceType paramReferenceType) throws AmbiguousMethodException, AbsentInformationException, InvalidTypeException, NoSuchMethodException, LineNotFoundException {
/*  76 */     Location location = location(paramReferenceType);
/*  77 */     if (location == null) {
/*  78 */       throw new InvalidTypeException();
/*     */     }
/*  80 */     EventRequestManager eventRequestManager = paramReferenceType.virtualMachine().eventRequestManager();
/*  81 */     BreakpointRequest breakpointRequest = eventRequestManager.createBreakpointRequest(location);
/*  82 */     breakpointRequest.setSuspendPolicy(this.suspendPolicy);
/*  83 */     breakpointRequest.enable();
/*  84 */     return (EventRequest)breakpointRequest;
/*     */   }
/*     */   
/*     */   String methodName() {
/*  88 */     return this.methodId;
/*     */   }
/*     */   
/*     */   int lineNumber() {
/*  92 */     return this.lineNumber;
/*     */   }
/*     */   
/*     */   List<String> methodArgs() {
/*  96 */     return this.methodArgs;
/*     */   }
/*     */   
/*     */   boolean isMethodBreakpoint() {
/* 100 */     return (this.methodId != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 105 */     return this.refSpec.hashCode() + this.lineNumber + ((this.methodId != null) ? this.methodId
/* 106 */       .hashCode() : 0) + ((this.methodArgs != null) ? this.methodArgs
/* 107 */       .hashCode() : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 112 */     if (paramObject instanceof BreakpointSpec) {
/* 113 */       BreakpointSpec breakpointSpec = (BreakpointSpec)paramObject;
/*     */       
/* 115 */       if ((this.methodId != null) ? this.methodId
/* 116 */         .equals(breakpointSpec.methodId) : (this.methodId == breakpointSpec.methodId)) if ((this.methodArgs != null) ? this.methodArgs
/*     */ 
/*     */           
/* 119 */           .equals(breakpointSpec.methodArgs) : (this.methodArgs == breakpointSpec.methodArgs)) if (this.refSpec
/*     */             
/* 121 */             .equals(breakpointSpec.refSpec) && this.lineNumber == breakpointSpec.lineNumber);  
/*     */       return false;
/*     */     } 
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String errorMessageFor(Exception paramException) {
/* 130 */     if (paramException instanceof AmbiguousMethodException) {
/* 131 */       return MessageOutput.format("Method is overloaded; specify arguments", 
/* 132 */           methodName());
/*     */     }
/*     */ 
/*     */     
/* 136 */     if (paramException instanceof NoSuchMethodException)
/* 137 */       return MessageOutput.format("No method in", new Object[] {
/* 138 */             methodName(), this.refSpec
/* 139 */             .toString() }); 
/* 140 */     if (paramException instanceof AbsentInformationException)
/* 141 */       return MessageOutput.format("No linenumber information for", this.refSpec
/* 142 */           .toString()); 
/* 143 */     if (paramException instanceof LineNotFoundException)
/* 144 */       return MessageOutput.format("No code at line", new Object[] { new Long(
/* 145 */               lineNumber()), this.refSpec
/* 146 */             .toString() }); 
/* 147 */     if (paramException instanceof InvalidTypeException) {
/* 148 */       return MessageOutput.format("Breakpoints can be located only in classes.", this.refSpec
/* 149 */           .toString());
/*     */     }
/* 151 */     return super.errorMessageFor(paramException);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 157 */     StringBuffer stringBuffer = new StringBuffer(this.refSpec.toString());
/* 158 */     if (isMethodBreakpoint()) {
/* 159 */       stringBuffer.append('.');
/* 160 */       stringBuffer.append(this.methodId);
/* 161 */       if (this.methodArgs != null) {
/* 162 */         boolean bool = true;
/* 163 */         stringBuffer.append('(');
/* 164 */         for (String str : this.methodArgs) {
/* 165 */           if (!bool) {
/* 166 */             stringBuffer.append(',');
/*     */           }
/* 168 */           stringBuffer.append(str);
/* 169 */           bool = false;
/*     */         } 
/* 171 */         stringBuffer.append(")");
/*     */       } 
/*     */     } else {
/* 174 */       stringBuffer.append(':');
/* 175 */       stringBuffer.append(this.lineNumber);
/*     */     } 
/* 177 */     return MessageOutput.format("breakpoint", stringBuffer.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Location location(ReferenceType paramReferenceType) throws AmbiguousMethodException, AbsentInformationException, NoSuchMethodException, LineNotFoundException {
/* 185 */     Location location = null;
/* 186 */     if (isMethodBreakpoint()) {
/* 187 */       Method method = findMatchingMethod(paramReferenceType);
/* 188 */       location = method.location();
/*     */     } else {
/*     */       
/* 191 */       List<Location> list = paramReferenceType.locationsOfLine(lineNumber());
/* 192 */       if (list.size() == 0) {
/* 193 */         throw new LineNotFoundException();
/*     */       }
/*     */       
/* 196 */       location = list.get(0);
/* 197 */       if (location.method() == null) {
/* 198 */         throw new LineNotFoundException();
/*     */       }
/*     */     } 
/* 201 */     return location;
/*     */   }
/*     */   
/*     */   private boolean isValidMethodName(String paramString) {
/* 205 */     return (isJavaIdentifier(paramString) || paramString
/* 206 */       .equals("<init>") || paramString
/* 207 */       .equals("<clinit>"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean compareArgTypes(Method paramMethod, List<String> paramList) {
/* 218 */     List<String> list = paramMethod.argumentTypeNames();
/*     */ 
/*     */     
/* 221 */     if (list.size() != paramList.size()) {
/* 222 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 226 */     int i = list.size();
/* 227 */     for (byte b = 0; b < i; b++) {
/* 228 */       String str1 = list.get(b);
/* 229 */       String str2 = paramList.get(b);
/* 230 */       if (!str1.equals(str2)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 238 */         if (b != i - 1 || 
/* 239 */           !paramMethod.isVarArgs() || 
/* 240 */           !str2.endsWith("...")) {
/* 241 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 250 */         int j = str1.length();
/* 251 */         if (j + 1 != str2.length())
/*     */         {
/* 253 */           return false;
/*     */         }
/*     */         
/* 256 */         if (!str1.regionMatches(0, str2, 0, j - 2)) {
/* 257 */           return false;
/*     */         }
/*     */         
/* 260 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 264 */     return true;
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
/*     */   private String normalizeArgTypeName(String paramString) {
/* 277 */     byte b = 0;
/* 278 */     StringBuffer stringBuffer1 = new StringBuffer();
/* 279 */     StringBuffer stringBuffer2 = new StringBuffer();
/* 280 */     paramString = paramString.trim();
/* 281 */     int i = paramString.length();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 287 */     boolean bool = paramString.endsWith("...");
/* 288 */     if (bool) {
/* 289 */       i -= 3;
/*     */     }
/* 291 */     while (b < i) {
/* 292 */       char c = paramString.charAt(b);
/* 293 */       if (Character.isWhitespace(c) || c == '[') {
/*     */         break;
/*     */       }
/* 296 */       stringBuffer1.append(c);
/* 297 */       b++;
/*     */     } 
/* 299 */     while (b < i) {
/* 300 */       char c = paramString.charAt(b);
/* 301 */       if (c == '[' || c == ']') {
/* 302 */         stringBuffer2.append(c);
/* 303 */       } else if (!Character.isWhitespace(c)) {
/* 304 */         throw new IllegalArgumentException(
/* 305 */             MessageOutput.format("Invalid argument type name"));
/*     */       } 
/* 307 */       b++;
/*     */     } 
/* 309 */     paramString = stringBuffer1.toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     if (paramString.indexOf('.') == -1 || paramString.startsWith("*.")) {
/*     */       try {
/* 317 */         ReferenceType referenceType = Env.getReferenceTypeFromToken(paramString);
/* 318 */         if (referenceType != null) {
/* 319 */           paramString = referenceType.name();
/*     */         }
/* 321 */       } catch (IllegalArgumentException illegalArgumentException) {}
/*     */     }
/*     */ 
/*     */     
/* 325 */     paramString = paramString + stringBuffer2.toString();
/* 326 */     if (bool) {
/* 327 */       paramString = paramString + "...";
/*     */     }
/* 329 */     return paramString;
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
/*     */   private Method findMatchingMethod(ReferenceType paramReferenceType) throws AmbiguousMethodException, NoSuchMethodException {
/* 343 */     ArrayList<String> arrayList = null;
/* 344 */     if (methodArgs() != null) {
/* 345 */       arrayList = new ArrayList(methodArgs().size());
/* 346 */       for (String str : methodArgs()) {
/* 347 */         str = normalizeArgTypeName(str);
/* 348 */         arrayList.add(str);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 353 */     Method method1 = null;
/* 354 */     Method method2 = null;
/* 355 */     byte b = 0;
/* 356 */     for (Method method : paramReferenceType.methods()) {
/* 357 */       if (method.name().equals(methodName())) {
/* 358 */         b++;
/*     */ 
/*     */         
/* 361 */         if (b == 1) {
/* 362 */           method1 = method;
/*     */         }
/*     */ 
/*     */         
/* 366 */         if (arrayList != null && 
/* 367 */           compareArgTypes(method, arrayList) == true) {
/* 368 */           method2 = method;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 375 */     Method method3 = null;
/* 376 */     if (method2 != null) {
/*     */       
/* 378 */       method3 = method2;
/* 379 */     } else if (arrayList == null && b > 0) {
/*     */       
/* 381 */       if (b == 1) {
/* 382 */         method3 = method1;
/*     */       } else {
/* 384 */         throw new AmbiguousMethodException();
/*     */       } 
/*     */     } else {
/* 387 */       throw new NoSuchMethodException(methodName());
/*     */     } 
/* 389 */     return method3;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\BreakpointSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */