/*      */ package sun.rmi.rmic.iiop;
/*      */ 
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.Identifier;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class IDLNames
/*      */   implements Constants
/*      */ {
/*   55 */   public static final byte[] ASCII_HEX = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   81 */   private static final byte[] IDL_IDENTIFIER_CHARS = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getMemberOrMethodName(NameContext paramNameContext, String paramString, BatchEnvironment paramBatchEnvironment) {
/*  123 */     String str = (String)paramBatchEnvironment.namesCache.get(paramString);
/*      */     
/*  125 */     if (str == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  132 */       str = paramNameContext.get(paramString);
/*      */ 
/*      */ 
/*      */       
/*  136 */       str = convertLeadingUnderscores(str);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  143 */       str = convertIDLKeywords(str);
/*      */ 
/*      */ 
/*      */       
/*  147 */       str = convertToISOLatin1(str);
/*      */ 
/*      */ 
/*      */       
/*  151 */       paramBatchEnvironment.namesCache.put(paramString, str);
/*      */     } 
/*      */     
/*  154 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String convertToISOLatin1(String paramString) {
/*  166 */     String str = replace(paramString, "x\\u", "U");
/*  167 */     str = replace(str, "x\\U", "U");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  172 */     int i = str.length();
/*  173 */     StringBuffer stringBuffer = null;
/*      */     
/*  175 */     for (byte b = 0; b < i; b++) {
/*      */       
/*  177 */       char c = str.charAt(b);
/*      */       
/*  179 */       if (c > 'ÿ' || IDL_IDENTIFIER_CHARS[c] == 0) {
/*      */ 
/*      */ 
/*      */         
/*  183 */         if (stringBuffer == null)
/*      */         {
/*      */ 
/*      */           
/*  187 */           stringBuffer = new StringBuffer(str.substring(0, b));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  192 */         stringBuffer.append("U");
/*  193 */         stringBuffer.append((char)ASCII_HEX[(c & 0xF000) >>> 12]);
/*  194 */         stringBuffer.append((char)ASCII_HEX[(c & 0xF00) >>> 8]);
/*  195 */         stringBuffer.append((char)ASCII_HEX[(c & 0xF0) >>> 4]);
/*  196 */         stringBuffer.append((char)ASCII_HEX[c & 0xF]);
/*      */       
/*      */       }
/*  199 */       else if (stringBuffer != null) {
/*  200 */         stringBuffer.append(c);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  205 */     if (stringBuffer != null) {
/*  206 */       str = stringBuffer.toString();
/*      */     }
/*      */     
/*  209 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String convertIDLKeywords(String paramString) {
/*  219 */     for (byte b = 0; b < IDL_KEYWORDS.length; b++) {
/*  220 */       if (paramString.equalsIgnoreCase(IDL_KEYWORDS[b])) {
/*  221 */         return "_" + paramString;
/*      */       }
/*      */     } 
/*      */     
/*  225 */     return paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String convertLeadingUnderscores(String paramString) {
/*  235 */     if (paramString.startsWith("_")) {
/*  236 */       return "J" + paramString;
/*      */     }
/*      */     
/*  239 */     return paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getClassOrInterfaceName(Identifier paramIdentifier, BatchEnvironment paramBatchEnvironment) throws Exception {
/*  254 */     String str1 = paramIdentifier.getName().toString();
/*  255 */     String str2 = null;
/*      */     
/*  257 */     if (paramIdentifier.isQualified()) {
/*  258 */       str2 = paramIdentifier.getQualifier().toString();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  263 */     String str3 = (String)paramBatchEnvironment.namesCache.get(str1);
/*      */     
/*  265 */     if (str3 == null) {
/*      */ 
/*      */ 
/*      */       
/*  269 */       str3 = replace(str1, ". ", "__");
/*      */ 
/*      */ 
/*      */       
/*  273 */       str3 = convertToISOLatin1(str3);
/*      */ 
/*      */ 
/*      */       
/*  277 */       NameContext nameContext = NameContext.forName(str2, false, paramBatchEnvironment);
/*  278 */       nameContext.assertPut(str3);
/*      */ 
/*      */ 
/*      */       
/*  282 */       str3 = getTypeOrModuleName(str3);
/*      */ 
/*      */ 
/*      */       
/*  286 */       paramBatchEnvironment.namesCache.put(str1, str3);
/*      */     } 
/*      */     
/*  289 */     return str3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getExceptionName(String paramString) {
/*  299 */     String str = paramString;
/*      */     
/*  301 */     if (paramString.endsWith("Exception")) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  306 */       str = stripLeadingUnderscore(paramString.substring(0, paramString.lastIndexOf("Exception")) + "Ex");
/*      */     } else {
/*  308 */       str = paramString + "Ex";
/*      */     } 
/*      */     
/*  311 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getModuleNames(Identifier paramIdentifier, boolean paramBoolean, BatchEnvironment paramBatchEnvironment) throws Exception {
/*  324 */     String[] arrayOfString = null;
/*      */     
/*  326 */     if (paramIdentifier.isQualified()) {
/*      */ 
/*      */ 
/*      */       
/*  330 */       Identifier identifier1 = paramIdentifier.getQualifier();
/*      */ 
/*      */ 
/*      */       
/*  334 */       paramBatchEnvironment.modulesContext.assertPut(identifier1.toString());
/*      */ 
/*      */ 
/*      */       
/*  338 */       byte b1 = 1;
/*  339 */       Identifier identifier2 = identifier1;
/*  340 */       while (identifier2.isQualified()) {
/*  341 */         identifier2 = identifier2.getQualifier();
/*  342 */         b1++;
/*      */       } 
/*      */       
/*  345 */       arrayOfString = new String[b1];
/*  346 */       int i = b1 - 1;
/*  347 */       identifier2 = identifier1;
/*      */ 
/*      */ 
/*      */       
/*  351 */       for (byte b2 = 0; b2 < b1; b2++) {
/*      */         
/*  353 */         String str1 = identifier2.getName().toString();
/*      */ 
/*      */ 
/*      */         
/*  357 */         String str2 = (String)paramBatchEnvironment.namesCache.get(str1);
/*      */         
/*  359 */         if (str2 == null) {
/*      */ 
/*      */ 
/*      */           
/*  363 */           str2 = convertToISOLatin1(str1);
/*      */ 
/*      */ 
/*      */           
/*  367 */           str2 = getTypeOrModuleName(str2);
/*      */ 
/*      */ 
/*      */           
/*  371 */           paramBatchEnvironment.namesCache.put(str1, str2);
/*      */         } 
/*      */         
/*  374 */         arrayOfString[i--] = str2;
/*  375 */         identifier2 = identifier2.getQualifier();
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  383 */     if (paramBoolean) {
/*  384 */       if (arrayOfString == null) {
/*  385 */         arrayOfString = IDL_BOXEDIDL_MODULE;
/*      */       } else {
/*  387 */         String[] arrayOfString1 = new String[arrayOfString.length + IDL_BOXEDIDL_MODULE.length];
/*  388 */         System.arraycopy(IDL_BOXEDIDL_MODULE, 0, arrayOfString1, 0, IDL_BOXEDIDL_MODULE.length);
/*  389 */         System.arraycopy(arrayOfString, 0, arrayOfString1, IDL_BOXEDIDL_MODULE.length, arrayOfString.length);
/*  390 */         arrayOfString = arrayOfString1;
/*      */       } 
/*      */     }
/*      */     
/*  394 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getArrayName(Type paramType, int paramInt) {
/*  404 */     StringBuffer stringBuffer = new StringBuffer(64);
/*      */ 
/*      */ 
/*      */     
/*  408 */     stringBuffer.append("seq");
/*  409 */     stringBuffer.append(Integer.toString(paramInt));
/*  410 */     stringBuffer.append("_");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  415 */     stringBuffer.append(replace(stripLeadingUnderscore(paramType.getIDLName()), " ", "_"));
/*      */ 
/*      */ 
/*      */     
/*  419 */     return stringBuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getArrayModuleNames(Type paramType) {
/*  428 */     String[] arrayOfString1, arrayOfString2 = paramType.getIDLModuleNames();
/*  429 */     int i = arrayOfString2.length;
/*      */ 
/*      */ 
/*      */     
/*  433 */     if (i == 0) {
/*      */ 
/*      */ 
/*      */       
/*  437 */       arrayOfString1 = IDL_SEQUENCE_MODULE;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  442 */       arrayOfString1 = new String[i + IDL_SEQUENCE_MODULE.length];
/*  443 */       System.arraycopy(IDL_SEQUENCE_MODULE, 0, arrayOfString1, 0, IDL_SEQUENCE_MODULE.length);
/*  444 */       System.arraycopy(arrayOfString2, 0, arrayOfString1, IDL_SEQUENCE_MODULE.length, i);
/*      */     } 
/*      */     
/*  447 */     return arrayOfString1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getInitialAttributeKind(CompoundType.Method paramMethod, BatchEnvironment paramBatchEnvironment) throws ClassNotFound {
/*  453 */     byte b = 0;
/*      */ 
/*      */ 
/*      */     
/*  457 */     if (!paramMethod.isConstructor()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  463 */       boolean bool = true;
/*  464 */       ValueType[] arrayOfValueType = paramMethod.getExceptions();
/*      */       
/*  466 */       if (arrayOfValueType.length > 0) {
/*  467 */         for (byte b1 = 0; b1 < arrayOfValueType.length; b1++) {
/*  468 */           if (arrayOfValueType[b1].isCheckedException() && 
/*  469 */             !arrayOfValueType[b1].isRemoteExceptionOrSubclass()) {
/*  470 */             bool = false;
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } else {
/*  479 */         bool = paramMethod.getEnclosing().isType(32768);
/*      */       } 
/*      */       
/*  482 */       if (bool) {
/*  483 */         String str = paramMethod.getName();
/*  484 */         int i = str.length();
/*  485 */         int j = (paramMethod.getArguments()).length;
/*  486 */         Type type = paramMethod.getReturnType();
/*  487 */         boolean bool1 = type.isType(1);
/*  488 */         boolean bool2 = type.isType(2);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  493 */         if (str.startsWith("get") && i > 3 && j == 0 && !bool1) {
/*  494 */           b = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*  500 */         else if (str.startsWith("is") && i > 2 && j == 0 && bool2) {
/*  501 */           b = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*  507 */         else if (str.startsWith("set") && i > 3 && j == 1 && bool1) {
/*  508 */           b = 5;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  515 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void setAttributeKinds(CompoundType.Method[] paramArrayOfMethod, int[] paramArrayOfint, String[] paramArrayOfString) {
/*  522 */     int i = paramArrayOfMethod.length;
/*      */     
/*      */     byte b;
/*      */     
/*  526 */     for (b = 0; b < i; b++) {
/*  527 */       switch (paramArrayOfint[b]) { case 2:
/*  528 */           paramArrayOfString[b] = paramArrayOfString[b].substring(3); break;
/*  529 */         case 1: paramArrayOfString[b] = paramArrayOfString[b].substring(2); break;
/*  530 */         case 5: paramArrayOfString[b] = paramArrayOfString[b].substring(3);
/*      */           break; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     } 
/*  539 */     for (b = 0; b < i; b++) {
/*  540 */       if (paramArrayOfint[b] == 1) {
/*  541 */         for (byte b1 = 0; b1 < i; b1++) {
/*  542 */           if (b1 != b && (paramArrayOfint[b1] == 2 || paramArrayOfint[b1] == 5) && paramArrayOfString[b]
/*      */             
/*  544 */             .equals(paramArrayOfString[b1])) {
/*      */ 
/*      */ 
/*      */             
/*  548 */             Type type2, type1 = paramArrayOfMethod[b].getReturnType();
/*      */ 
/*      */             
/*  551 */             if (paramArrayOfint[b1] == 2) {
/*  552 */               type2 = paramArrayOfMethod[b1].getReturnType();
/*      */             } else {
/*  554 */               type2 = paramArrayOfMethod[b1].getArguments()[0];
/*      */             } 
/*      */             
/*  557 */             if (!type1.equals(type2)) {
/*      */ 
/*      */ 
/*      */               
/*  561 */               paramArrayOfint[b] = 0;
/*  562 */               paramArrayOfString[b] = paramArrayOfMethod[b].getName();
/*      */ 
/*      */ 
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  575 */     for (b = 0; b < i; b++) {
/*  576 */       if (paramArrayOfint[b] == 5) {
/*  577 */         byte b1 = -1;
/*  578 */         byte b2 = -1;
/*      */ 
/*      */         
/*  581 */         for (byte b3 = 0; b3 < i; b3++) {
/*  582 */           if (b3 != b && paramArrayOfString[b].equals(paramArrayOfString[b3])) {
/*      */ 
/*      */ 
/*      */             
/*  586 */             Type type1 = paramArrayOfMethod[b3].getReturnType();
/*  587 */             Type type2 = paramArrayOfMethod[b].getArguments()[0];
/*      */             
/*  589 */             if (type1.equals(type2)) {
/*  590 */               if (paramArrayOfint[b3] == 1) {
/*  591 */                 b2 = b3;
/*      */               }
/*  593 */               else if (paramArrayOfint[b3] == 2) {
/*  594 */                 b1 = b3;
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  601 */         if (b1 > -1) {
/*  602 */           if (b2 > -1)
/*      */           {
/*      */ 
/*      */ 
/*      */             
/*  607 */             paramArrayOfint[b2] = 3;
/*      */ 
/*      */             
/*  610 */             paramArrayOfMethod[b2].setAttributePairIndex(b);
/*  611 */             paramArrayOfMethod[b].setAttributePairIndex(b2);
/*      */ 
/*      */ 
/*      */             
/*  615 */             paramArrayOfint[b1] = 0;
/*  616 */             paramArrayOfString[b1] = paramArrayOfMethod[b1].getName();
/*      */           
/*      */           }
/*      */           else
/*      */           {
/*  621 */             paramArrayOfint[b1] = 4;
/*      */ 
/*      */             
/*  624 */             paramArrayOfMethod[b1].setAttributePairIndex(b);
/*  625 */             paramArrayOfMethod[b].setAttributePairIndex(b1);
/*      */           }
/*      */         
/*  628 */         } else if (b2 > -1) {
/*      */ 
/*      */ 
/*      */           
/*  632 */           paramArrayOfint[b2] = 3;
/*      */ 
/*      */           
/*  635 */           paramArrayOfMethod[b2].setAttributePairIndex(b);
/*  636 */           paramArrayOfMethod[b].setAttributePairIndex(b2);
/*      */         }
/*      */         else {
/*      */           
/*  640 */           paramArrayOfint[b] = 0;
/*  641 */           paramArrayOfString[b] = paramArrayOfMethod[b].getName();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  650 */     for (b = 0; b < i; b++) {
/*      */       
/*  652 */       if (paramArrayOfint[b] != 0) {
/*      */         
/*  654 */         String str = paramArrayOfString[b];
/*      */ 
/*      */ 
/*      */         
/*  658 */         if (Character.isUpperCase(str.charAt(0)))
/*      */         {
/*      */ 
/*      */           
/*  662 */           if (str.length() == 1 || Character.isLowerCase(str.charAt(1))) {
/*      */ 
/*      */ 
/*      */             
/*  666 */             StringBuffer stringBuffer = new StringBuffer(str);
/*  667 */             stringBuffer.setCharAt(0, Character.toLowerCase(str.charAt(0)));
/*  668 */             paramArrayOfString[b] = stringBuffer.toString();
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/*  673 */       paramArrayOfMethod[b].setAttributeKind(paramArrayOfint[b]);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setMethodNames(CompoundType paramCompoundType, CompoundType.Method[] paramArrayOfMethod, BatchEnvironment paramBatchEnvironment) throws Exception {
/*  711 */     int i = paramArrayOfMethod.length;
/*      */     
/*  713 */     if (i == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  717 */     String[] arrayOfString = new String[i];
/*  718 */     for (byte b1 = 0; b1 < i; b1++) {
/*  719 */       arrayOfString[b1] = paramArrayOfMethod[b1].getName();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  724 */     CompoundType compoundType = paramArrayOfMethod[0].getEnclosing();
/*  725 */     if (compoundType.isType(4096) || compoundType
/*  726 */       .isType(8192) || compoundType
/*  727 */       .isType(32768)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  732 */       int[] arrayOfInt = new int[i];
/*      */       
/*  734 */       for (byte b = 0; b < i; b++) {
/*  735 */         arrayOfInt[b] = getInitialAttributeKind(paramArrayOfMethod[b], paramBatchEnvironment);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  741 */       setAttributeKinds(paramArrayOfMethod, arrayOfInt, arrayOfString);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  746 */     NameContext nameContext = new NameContext(true);
/*      */     byte b2;
/*  748 */     for (b2 = 0; b2 < i; b2++) {
/*  749 */       nameContext.put(arrayOfString[b2]);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  754 */     b2 = 0;
/*  755 */     for (byte b3 = 0; b3 < i; b3++) {
/*  756 */       if (!paramArrayOfMethod[b3].isConstructor()) {
/*  757 */         arrayOfString[b3] = getMemberOrMethodName(nameContext, arrayOfString[b3], paramBatchEnvironment);
/*      */       } else {
/*  759 */         arrayOfString[b3] = "create";
/*  760 */         b2 = 1;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  768 */     boolean[] arrayOfBoolean = new boolean[i]; byte b4;
/*  769 */     for (b4 = 0; b4 < i; b4++) {
/*  770 */       arrayOfBoolean[b4] = (!paramArrayOfMethod[b4].isAttribute() && 
/*  771 */         !paramArrayOfMethod[b4].isConstructor() && 
/*  772 */         doesMethodCollide(arrayOfString[b4], paramArrayOfMethod[b4], paramArrayOfMethod, arrayOfString, true));
/*      */     }
/*  774 */     convertOverloadedMethods(paramArrayOfMethod, arrayOfString, arrayOfBoolean);
/*      */ 
/*      */ 
/*      */     
/*  778 */     for (b4 = 0; b4 < i; b4++) {
/*  779 */       arrayOfBoolean[b4] = (!paramArrayOfMethod[b4].isAttribute() && paramArrayOfMethod[b4]
/*  780 */         .isConstructor() && 
/*  781 */         doesConstructorCollide(arrayOfString[b4], paramArrayOfMethod[b4], paramArrayOfMethod, arrayOfString, true));
/*      */     }
/*  783 */     convertOverloadedMethods(paramArrayOfMethod, arrayOfString, arrayOfBoolean);
/*      */ 
/*      */ 
/*      */     
/*  787 */     for (b4 = 0; b4 < i; b4++) {
/*      */       
/*  789 */       CompoundType.Method method = paramArrayOfMethod[b4];
/*      */ 
/*      */ 
/*      */       
/*  793 */       if (method.isAttribute() && 
/*  794 */         doesMethodCollide(arrayOfString[b4], method, paramArrayOfMethod, arrayOfString, true))
/*      */       {
/*      */ 
/*      */         
/*  798 */         arrayOfString[b4] = arrayOfString[b4] + "__";
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  805 */     if (b2 != 0) {
/*  806 */       for (b4 = 0; b4 < i; b4++) {
/*  807 */         CompoundType.Method method = paramArrayOfMethod[b4];
/*      */ 
/*      */ 
/*      */         
/*  811 */         if (method.isConstructor() && 
/*  812 */           doesConstructorCollide(arrayOfString[b4], method, paramArrayOfMethod, arrayOfString, false))
/*      */         {
/*      */ 
/*      */           
/*  816 */           arrayOfString[b4] = arrayOfString[b4] + "__";
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  823 */     String str = paramCompoundType.getIDLName(); byte b5;
/*  824 */     for (b5 = 0; b5 < i; b5++) {
/*  825 */       if (arrayOfString[b5].equalsIgnoreCase(str))
/*      */       {
/*      */         
/*  828 */         if (!paramArrayOfMethod[b5].isAttribute()) {
/*  829 */           arrayOfString[b5] = arrayOfString[b5] + "_";
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  837 */     for (b5 = 0; b5 < i; b5++) {
/*      */ 
/*      */ 
/*      */       
/*  841 */       if (doesMethodCollide(arrayOfString[b5], paramArrayOfMethod[b5], paramArrayOfMethod, arrayOfString, false))
/*      */       {
/*      */ 
/*      */         
/*  845 */         throw new Exception(paramArrayOfMethod[b5].toString());
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  853 */     for (b5 = 0; b5 < i; b5++) {
/*      */       
/*  855 */       CompoundType.Method method = paramArrayOfMethod[b5];
/*  856 */       String str1 = arrayOfString[b5];
/*      */       
/*  858 */       if (method.isAttribute()) {
/*      */         
/*  860 */         str1 = ATTRIBUTE_WIRE_PREFIX[method.getAttributeKind()] + stripLeadingUnderscore(str1);
/*  861 */         String str2 = arrayOfString[b5];
/*  862 */         method.setAttributeName(str2);
/*      */       } 
/*  864 */       method.setIDLName(str1);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String stripLeadingUnderscore(String paramString) {
/*  869 */     if (paramString != null && paramString.length() > 1 && paramString
/*  870 */       .charAt(0) == '_')
/*      */     {
/*  872 */       return paramString.substring(1);
/*      */     }
/*  874 */     return paramString;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String stripTrailingUnderscore(String paramString) {
/*  879 */     if (paramString != null && paramString.length() > 1 && paramString
/*  880 */       .charAt(paramString.length() - 1) == '_')
/*      */     {
/*  882 */       return paramString.substring(0, paramString.length() - 1);
/*      */     }
/*  884 */     return paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void convertOverloadedMethods(CompoundType.Method[] paramArrayOfMethod, String[] paramArrayOfString, boolean[] paramArrayOfboolean) {
/*  892 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*      */ 
/*      */ 
/*      */       
/*  896 */       if (paramArrayOfboolean[b]) {
/*      */ 
/*      */ 
/*      */         
/*  900 */         CompoundType.Method method = paramArrayOfMethod[b];
/*  901 */         Type[] arrayOfType = method.getArguments();
/*      */         
/*  903 */         for (byte b1 = 0; b1 < arrayOfType.length; b1++) {
/*      */ 
/*      */ 
/*      */           
/*  907 */           paramArrayOfString[b] = paramArrayOfString[b] + "__";
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  912 */           String str = arrayOfType[b1].getQualifiedIDLName(false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  918 */           str = replace(str, "::_", "_");
/*      */ 
/*      */ 
/*      */           
/*  922 */           str = replace(str, "::", "_");
/*      */ 
/*      */ 
/*      */           
/*  926 */           str = replace(str, " ", "_");
/*      */ 
/*      */ 
/*      */           
/*  930 */           paramArrayOfString[b] = paramArrayOfString[b] + str;
/*      */         } 
/*      */         
/*  933 */         if (arrayOfType.length == 0) {
/*  934 */           paramArrayOfString[b] = paramArrayOfString[b] + "__";
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  939 */         paramArrayOfString[b] = stripLeadingUnderscore(paramArrayOfString[b]);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean doesMethodCollide(String paramString, CompoundType.Method paramMethod, CompoundType.Method[] paramArrayOfMethod, String[] paramArrayOfString, boolean paramBoolean) {
/*  952 */     for (byte b = 0; b < paramArrayOfMethod.length; b++) {
/*      */       
/*  954 */       CompoundType.Method method = paramArrayOfMethod[b];
/*      */       
/*  956 */       if (paramMethod != method && 
/*  957 */         !method.isConstructor() && (!paramBoolean || 
/*  958 */         !method.isAttribute()) && paramString
/*  959 */         .equals(paramArrayOfString[b])) {
/*      */ 
/*      */ 
/*      */         
/*  963 */         int i = paramMethod.getAttributeKind();
/*  964 */         int j = method.getAttributeKind();
/*      */         
/*  966 */         if (i == 0 || j == 0 || ((i != 5 || j == 5) && (i == 5 || j != 5) && (i != 3 || j != 2) && (i != 2 || j != 3)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  979 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  984 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean doesConstructorCollide(String paramString, CompoundType.Method paramMethod, CompoundType.Method[] paramArrayOfMethod, String[] paramArrayOfString, boolean paramBoolean) {
/*  995 */     for (byte b = 0; b < paramArrayOfMethod.length; b++) {
/*      */       
/*  997 */       CompoundType.Method method = paramArrayOfMethod[b];
/*      */       
/*  999 */       if (paramMethod != method && method
/* 1000 */         .isConstructor() == paramBoolean && paramString
/* 1001 */         .equals(paramArrayOfString[b]))
/*      */       {
/*      */ 
/*      */         
/* 1005 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 1009 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setMemberNames(CompoundType paramCompoundType, CompoundType.Member[] paramArrayOfMember, CompoundType.Method[] paramArrayOfMethod, BatchEnvironment paramBatchEnvironment) throws Exception {
/* 1027 */     NameContext nameContext = new NameContext(true);
/*      */     byte b1;
/* 1029 */     for (b1 = 0; b1 < paramArrayOfMember.length; b1++) {
/* 1030 */       nameContext.put(paramArrayOfMember[b1].getName());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1035 */     for (b1 = 0; b1 < paramArrayOfMember.length; b1++) {
/*      */       
/* 1037 */       CompoundType.Member member = paramArrayOfMember[b1];
/* 1038 */       String str1 = getMemberOrMethodName(nameContext, member.getName(), paramBatchEnvironment);
/* 1039 */       member.setIDLName(str1);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1044 */     String str = paramCompoundType.getIDLName(); byte b2;
/* 1045 */     for (b2 = 0; b2 < paramArrayOfMember.length; b2++) {
/* 1046 */       String str1 = paramArrayOfMember[b2].getIDLName();
/* 1047 */       if (str1.equalsIgnoreCase(str))
/*      */       {
/* 1049 */         paramArrayOfMember[b2].setIDLName(str1 + "_");
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1055 */     for (b2 = 0; b2 < paramArrayOfMember.length; b2++) {
/* 1056 */       String str1 = paramArrayOfMember[b2].getIDLName();
/* 1057 */       for (byte b = 0; b < paramArrayOfMember.length; b++) {
/* 1058 */         if (b2 != b && paramArrayOfMember[b].getIDLName().equals(str1))
/*      */         {
/*      */ 
/*      */           
/* 1062 */           throw new Exception(str1);
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     do {
/* 1072 */       b2 = 0;
/* 1073 */       for (byte b = 0; b < paramArrayOfMember.length; b++) {
/* 1074 */         String str1 = paramArrayOfMember[b].getIDLName();
/* 1075 */         for (byte b3 = 0; b3 < paramArrayOfMethod.length; b3++) {
/* 1076 */           if (paramArrayOfMethod[b3].getIDLName().equals(str1)) {
/*      */ 
/*      */ 
/*      */             
/* 1080 */             paramArrayOfMember[b].setIDLName(str1 + "_");
/* 1081 */             b2 = 1;
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1086 */     } while (b2 != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getTypeName(int paramInt, boolean paramBoolean) {
/* 1100 */     String str = null;
/*      */     
/* 1102 */     switch (paramInt) { case 1:
/* 1103 */         str = "void"; break;
/* 1104 */       case 2: str = "boolean"; break;
/* 1105 */       case 4: str = "octet"; break;
/* 1106 */       case 8: str = "wchar"; break;
/* 1107 */       case 16: str = "short"; break;
/* 1108 */       case 32: str = "long"; break;
/* 1109 */       case 64: str = "long long"; break;
/* 1110 */       case 128: str = "float"; break;
/* 1111 */       case 256: str = "double"; break;
/* 1112 */       case 1024: str = "any"; break;
/* 1113 */       case 2048: str = "Object";
/*      */         break;
/*      */       case 512:
/* 1116 */         if (paramBoolean) {
/* 1117 */           str = "wstring"; break;
/*      */         } 
/* 1119 */         str = "WStringValue";
/*      */         break; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1126 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getQualifiedName(String[] paramArrayOfString, String paramString) {
/* 1133 */     String str = null;
/* 1134 */     if (paramArrayOfString != null && paramArrayOfString.length > 0) {
/* 1135 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 1136 */         if (b == 0) {
/* 1137 */           str = paramArrayOfString[0];
/*      */         } else {
/* 1139 */           str = str + "::";
/* 1140 */           str = str + paramArrayOfString[b];
/*      */         } 
/*      */       } 
/* 1143 */       str = str + "::";
/* 1144 */       str = str + paramString;
/*      */     } else {
/* 1146 */       str = paramString;
/*      */     } 
/* 1148 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replace(String paramString1, String paramString2, String paramString3) {
/* 1160 */     int i = paramString1.indexOf(paramString2, 0);
/*      */     
/* 1162 */     if (i >= 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1167 */       StringBuffer stringBuffer = new StringBuffer(paramString1.length() + 16);
/* 1168 */       int j = paramString2.length();
/* 1169 */       int k = 0;
/*      */       
/* 1171 */       while (i >= 0) {
/* 1172 */         stringBuffer.append(paramString1.substring(k, i));
/* 1173 */         stringBuffer.append(paramString3);
/* 1174 */         k = i + j;
/* 1175 */         i = paramString1.indexOf(paramString2, k);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1180 */       if (k < paramString1.length()) {
/* 1181 */         stringBuffer.append(paramString1.substring(k));
/*      */       }
/*      */       
/* 1184 */       return stringBuffer.toString();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1190 */     return paramString1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getIDLRepositoryID(String paramString) {
/* 1198 */     return "IDL:" + 
/* 1199 */       replace(paramString, "::", "/") + ":1.0";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getTypeOrModuleName(String paramString) {
/* 1218 */     String str = convertLeadingUnderscores(paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1225 */     return convertIDLKeywords(str);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\IDLNames.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */