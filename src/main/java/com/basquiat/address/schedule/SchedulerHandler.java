package com.basquiat.address.schedule;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Jobs의 receiverJob들 로부터 jobBean과 jobBeanTrigger들을 빈 객체 주입하여, 
 * 스케줄러를 등록시키는 component 클래스
 *  
 * created by basquiat
 *
 */
@Component
public class SchedulerHandler {
	
	private final Logger LOG = LoggerFactory.getLogger(SchedulerHandler.class);
	
	@Autowired
	private Scheduler scheduler;

	@Autowired
	@Qualifier("BTCJobBean")
	private JobDetail receiveBTCJobDetail;
	
	@Autowired
	@Qualifier("BTCJobBeanTrigger")
	private Trigger receiveBTCJobTrigger;

	@Autowired
	@Qualifier("QTUMJobBean")
	private JobDetail receiveQTUMJobDetail;
	
	@Autowired
	@Qualifier("QTUMJobBeanTrigger")
	private Trigger receiveQTUMJobTrigger;
	
	@Autowired
	@Qualifier("ETHJobBean")
	private JobDetail receiveETHJobDetail;
	
	@Autowired
	@Qualifier("ETHJobBeanTrigger")
	private Trigger receiveETHJobTrigger;
	
	@Autowired
	@Qualifier("ETCJobBean")
	private JobDetail receiveETCJobDetail;
	
	@Autowired
	@Qualifier("ETCJobBeanTrigger")
	private Trigger receiveETCJobTrigger;
	
	@Autowired
	@Qualifier("ConfirmationsCheckJobBean")
	private JobDetail confirmationsCheckJobDetail;
	
	@Autowired
	@Qualifier("ConfirmationsCheckJobBeanTrigger")
	private Trigger confirmationsCheckJobBeanTrigger;
	
	@Value("${serverNo:-1}")
	String runServerNo;

	private static String serverNo;
		public static String getServerNo() { return serverNo; }
	
	@EventListener(ApplicationReadyEvent.class)
	public void init() throws Exception {
		
		LOG.info("Scheduler start. Server No is " + runServerNo);

		serverNo = runServerNo;

		if("-1".equals(serverNo) || "#1".equals(serverNo) || "1".equals(serverNo) || "01".equals(serverNo)) {
			LOG.info("removeAllTrigger and removeAllJob start.");
			removeAllTrigger();
			removeAllJob();
			LOG.info("removeAllTrigger and removeAllJob end.");
		}
		
		// job 등록
//		scheduler.scheduleJob(receiveBTCJobDetail, receiveBTCJobTrigger);
//		scheduler.scheduleJob(receiveQTUMJobDetail, receiveQTUMJobTrigger);
//		scheduler.scheduleJob(receiveETHJobDetail, receiveETHJobTrigger);
//		scheduler.scheduleJob(receiveETCJobDetail, receiveETCJobTrigger);
//		scheduler.scheduleJob(confirmationsCheckJobDetail, confirmationsCheckJobBeanTrigger);
	}

	/**
	 * 시작할 때 기존의 스케쥴을 destroy한다.
	 * @throws Exception
	 */
	private void removeAllTrigger() throws Exception {
		for(String triggerGroupName : scheduler.getTriggerGroupNames()) {
			for(TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroupName))) {
				String triggerName = triggerKey.getName();
				scheduler.unscheduleJob(new TriggerKey(triggerName, triggerGroupName));
			}
		}
	}

	/**
	 * 시작할 때 기존의 잡 정보들을 삭제한다.
	 * @throws Exception
	 */
	private void removeAllJob() throws Exception {
		for(String jobGroupName : scheduler.getJobGroupNames()) {
			for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroupName))) {
				String jobName = jobKey.getName();
				scheduler.deleteJob(new JobKey(jobName, jobGroupName));
			}
		}
	}
	
}
