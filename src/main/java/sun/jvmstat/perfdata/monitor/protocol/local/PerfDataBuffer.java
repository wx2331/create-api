/*    */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.RandomAccessFile;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.MappedByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.security.AccessController;
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.VmIdentifier;
/*    */ import sun.jvmstat.perfdata.monitor.AbstractPerfDataBuffer;
/*    */ import sun.misc.Perf;
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
/*    */ public class PerfDataBuffer
/*    */   extends AbstractPerfDataBuffer
/*    */ {
/* 51 */   private static final Perf perf = AccessController.<Perf>doPrivileged(new Perf.GetPerfAction());
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
/*    */   public PerfDataBuffer(VmIdentifier paramVmIdentifier) throws MonitorException {
/*    */     try {
/* 64 */       ByteBuffer byteBuffer = perf.attach(paramVmIdentifier.getLocalVmId(), paramVmIdentifier.getMode());
/* 65 */       createPerfDataBuffer(byteBuffer, paramVmIdentifier.getLocalVmId());
/*    */     }
/* 67 */     catch (IllegalArgumentException illegalArgumentException) {
/*    */ 
/*    */       
/*    */       try {
/*    */         
/* 72 */         String str = PerfDataFile.getTempDirectory() + "hsperfdata_" + Integer.toString(paramVmIdentifier.getLocalVmId());
/*    */         
/* 74 */         File file = new File(str);
/*    */         
/* 76 */         FileChannel fileChannel = (new RandomAccessFile(file, "r")).getChannel();
/* 77 */         MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, 
/* 78 */             (int)fileChannel.size());
/* 79 */         fileChannel.close();
/* 80 */         createPerfDataBuffer(mappedByteBuffer, paramVmIdentifier.getLocalVmId());
/*    */       }
/* 82 */       catch (FileNotFoundException fileNotFoundException) {
/*    */         
/* 84 */         throw new MonitorException(paramVmIdentifier.getLocalVmId() + " not found", illegalArgumentException);
/*    */       }
/* 86 */       catch (IOException iOException) {
/* 87 */         throw new MonitorException("Could not map 1.4.1 file for " + paramVmIdentifier
/* 88 */             .getLocalVmId(), iOException);
/*    */       } 
/* 90 */     } catch (IOException iOException) {
/* 91 */       throw new MonitorException("Could not attach to " + paramVmIdentifier
/* 92 */           .getLocalVmId(), iOException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\local\PerfDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */