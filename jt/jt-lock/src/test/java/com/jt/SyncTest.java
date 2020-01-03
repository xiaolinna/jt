package com.jt;

import org.junit.Test;

//模拟窗口卖票业务
public class SyncTest{

	private int num = 100;

	class Windows implements Runnable{
		//定义线程卖票操作
		@Override
		public void run() {
			while(true) {
				synchronized (this) {
					if(num>0) {
						System.out.println("恭喜"+Thread.currentThread().getName()+"售出得第"+(101-num)+"张票");
						num--;
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else {
						break;//跳出循环
					}
				}
			}
		}
	}

	@Test
	public void test01() {
		Runnable runnable = new Windows();
		Thread thread1 = new Thread(runnable, "窗口A");
		Thread thread2 = new Thread(runnable, "窗口B");
		Thread thread3 = new Thread(runnable, "窗口C");
		thread1.start();
		thread2.start();
		thread3.start();
		for(;;); //防止线程提前结束
	}

}
