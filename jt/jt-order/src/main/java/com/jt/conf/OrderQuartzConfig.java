package com.jt.conf;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import com.jt.quartz.OrderQuartz;

@Configurable
public class OrderQuartzConfig {

	//定义任务详情
	@Bean
	public JobDetail orderjobDetail() {
		
		//指定job的名称和持久化保存任务
		return JobBuilder
			   .newJob(OrderQuartz.class)	//自己的任务
			   .withIdentity("orderQuartz")	//任务的名称
			   .storeDurably()
			   .build();
		
	}
	
	//定义触发器
	@Bean
	public Trigger orderTrigger() {
		/*SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
		.withIntervalInMinutes(1)	//定义时间周期
		.repeatForever();*/
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/1 * * * ?");
		return TriggerBuilder
				.newTrigger()
				.forJob(orderjobDetail())
				.withIdentity("orderQuartz")
				.withSchedule(scheduleBuilder).build();
		
	}
	
}
