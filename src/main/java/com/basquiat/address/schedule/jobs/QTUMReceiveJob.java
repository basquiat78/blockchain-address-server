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
 * QTUMReceiveJob
 * Node별 JobBean과 JobBeanTrigger bean등록을 해주는 Component
 * execute method를 통해 스케줄을 시작하게 된다.
 * 
 * created by basquiat
 *
 */
@Component
@DisallowConcurrentExecution
public class QTUMReceiveJob implements Job {

	private final Logger LOG = LoggerFactory.getLogger(QTUMReceiveJob.class);
	
	@Autowired
	private TransactionHandler transactionHandler;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOG.info("QTUM Scheduler execute!");
		transactionHandler.processGetTransactionTypeBTC("QTUM");
		LOG.info("QTUM Scheduler end!");
	}
	
    @Bean(name="QTUMJobBean")
	public JobDetailFactoryBean receiveBTCJobDetail() {
		return QuartzUtil.createJobDetail(this.getClass());
	}
    
    @Bean(name="QTUMJobBeanTrigger")
    public CronTriggerFactoryBean receiveBTCJobTrigger() {
        return QuartzUtil.createCronTrigger("0/60 * * * * ?");
    }
    
}
