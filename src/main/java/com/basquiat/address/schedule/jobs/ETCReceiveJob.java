package com.basquiat.address.schedule.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

import com.basquiat.address.schedule.TransactionHandler;
import com.basquiat.address.util.QuartzUtil;

/**
 * ETCReceiveJob
 * Node별 JobBean과 JobBeanTrigger bean등록을 해주는 Component
 * execute method를 통해 스케줄을 시작하게 된다.
 * 
 * created by wkimdev
 *
 */
@Component
@DisallowConcurrentExecution
public class ETCReceiveJob implements Job {

	private final Logger LOG = LoggerFactory.getLogger(ETCReceiveJob.class);
	
	@Autowired
	private TransactionHandler transactionHandler;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOG.info("ETC Scheduler execute!");
		transactionHandler.processGetTransactionTypeETH("ETC");
		LOG.info("ETC Scheduler end!");
	}
	
    @Bean(name="ETCJobBean")
	public JobDetailFactoryBean receiveETCJobDetail() {
		return QuartzUtil.createJobDetail(this.getClass());
	}
    
    @Bean(name="ETCJobBeanTrigger")
    public CronTriggerFactoryBean receiveETCJobTrigger() {
        return QuartzUtil.createCronTrigger("0/15 * * * * ?");
    }
    
}
