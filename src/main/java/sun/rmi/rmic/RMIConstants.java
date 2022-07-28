/*    */ package sun.rmi.rmic;
/*    */ 
/*    */ import sun.tools.java.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface RMIConstants
/*    */   extends Constants
/*    */ {
/* 48 */   public static final Identifier idRemoteObject = Identifier.lookup("java.rmi.server.RemoteObject");
/*    */   
/* 50 */   public static final Identifier idRemoteStub = Identifier.lookup("java.rmi.server.RemoteStub");
/*    */   
/* 52 */   public static final Identifier idRemoteRef = Identifier.lookup("java.rmi.server.RemoteRef");
/*    */   
/* 54 */   public static final Identifier idOperation = Identifier.lookup("java.rmi.server.Operation");
/*    */   
/* 56 */   public static final Identifier idSkeleton = Identifier.lookup("java.rmi.server.Skeleton");
/*    */   
/* 58 */   public static final Identifier idSkeletonMismatchException = Identifier.lookup("java.rmi.server.SkeletonMismatchException");
/*    */   
/* 60 */   public static final Identifier idRemoteCall = Identifier.lookup("java.rmi.server.RemoteCall");
/*    */   
/* 62 */   public static final Identifier idMarshalException = Identifier.lookup("java.rmi.MarshalException");
/*    */   
/* 64 */   public static final Identifier idUnmarshalException = Identifier.lookup("java.rmi.UnmarshalException");
/*    */   
/* 66 */   public static final Identifier idUnexpectedException = Identifier.lookup("java.rmi.UnexpectedException");
/*    */   public static final int STUB_VERSION_1_1 = 1;
/*    */   public static final int STUB_VERSION_FAT = 2;
/*    */   public static final int STUB_VERSION_1_2 = 3;
/*    */   public static final long STUB_SERIAL_VERSION_UID = 2L;
/*    */   public static final int INTERFACE_HASH_STUB_VERSION = 1;
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\RMIConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */