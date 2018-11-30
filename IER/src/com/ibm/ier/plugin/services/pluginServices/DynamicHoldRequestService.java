/*    */ package com.ibm.ier.plugin.services.pluginServices;
/*    */ 
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*    */ import com.ibm.ier.plugin.util.IERUtil;
/*    */ import com.ibm.jarm.api.constants.RMRefreshMode;
/*    */ import com.ibm.jarm.api.core.FilePlanRepository;
/*    */ import com.ibm.jarm.api.core.Hold;
/*    */ import com.ibm.jarm.api.core.RMFactory.Hold;
/*    */ import com.ibm.jarm.api.property.RMProperties;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DynamicHoldRequestService
/*    */   extends IERBasePluginService
/*    */ {
/*    */   public String getId()
/*    */   {
/* 21 */     return "ierDynamicHoldRequestService";
/*    */   }
/*    */   
/*    */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*    */   {
/* 26 */     Logger.logEntry(this, "serviceExecute", request);
/*    */     
/* 28 */     String id = IERUtil.getIdFromDocIdString(request.getParameter("ier_holdId"));
/* 29 */     String task = request.getParameter("ier_dynamic_hold_task");
/*    */     
/* 31 */     FilePlanRepository repository = getFilePlanRepository();
/* 32 */     Hold hold = RMFactory.Hold.fetchInstance(repository, id, null);
/*    */     
/* 34 */     RMProperties properties = hold.getProperties();
/* 35 */     properties.putIntegerValue("SweepState", Integer.valueOf(getDynamicHoldState(task)));
/* 36 */     if ("activate_sweep_hold_processing".equalsIgnoreCase(task)) {
/* 37 */       properties.putDateTimeValue("LastHoldSweepDate", null);
/*    */     }
/* 39 */     hold.save(RMRefreshMode.Refresh);
/*    */     
/* 41 */     Logger.logExit(this, "serviceExecute", request);
/*    */   }
/*    */   
/* 44 */   private int getDynamicHoldState(String task) { if ("initiate_remove_hold_request".equalsIgnoreCase(task))
/* 45 */       return 2;
/* 46 */     if ("cancel_remove_hold_request".equalsIgnoreCase(task)) {
/* 47 */       return 1;
/*    */     }
/* 49 */     return 1;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\DynamicHoldRequestService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */