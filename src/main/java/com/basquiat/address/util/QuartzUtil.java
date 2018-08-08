package com.basquiat.address.util;

import org.quartz.SimpleTrigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * Quartz Util
 * this is quartz util
 * created by basquiat
 *
 */
@Component
public class QuartzUtil {

	public static JobDetailFactoryBean createJobDetail(Class<?> jobClass) {
        return createJobDetail(jobClass, null);
    }

	public static JobDetailFactoryBean createJobDetail(Class<?> jobClass, String jobGroupName) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setGroup(jobGroupName);
        factoryBean.setDurability(true);	// job has to be durable to be stored in DB
        return factoryBean;
	}
	
	public static SimpleTriggerFactoryBean createSimpleTrigger(long pollFrequencyMs) {
    	return createSimpleTrigger(pollFrequencyMs, SimpleTrigger.REPEAT_INDEFINITELY, null);
    }

	public static SimpleTriggerFactoryBean createSimpleTrigger(long pollFrequencyMs, int repeatCount) {
    	return createSimpleTrigger(pollFrequencyMs, repeatCount, null);
    }

	public static SimpleTriggerFactoryBean createSimpleTrigger(long pollFrequencyMs, String triggerGroupName) {
    	return createSimpleTrigger(pollFrequencyMs, SimpleTrigger.REPEAT_INDEFINITELY, triggerGroupName);
    }
    
	public static SimpleTriggerFactoryBean createSimpleTrigger(long pollFrequencyMs, int repeatCount, String triggerGroupName) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setGroup(triggerGroupName);
//        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setRepeatCount(repeatCount);
        // in case of misfire, ignore all missed triggers and continue :
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }

	public static CronTriggerFactoryBean createCronTrigger(String cronExpression) {
    	return createCronTrigger(cronExpression, null);
    }
    
	public static CronTriggerFactoryBean createCronTrigger(String cronExpression, String triggerGroupName) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setGroup(triggerGroupName);
//        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        return factoryBean;
    }

}
