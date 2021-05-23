package 多线程CD;
import java.io.*;
import java.util.*;

//销售线程
class SaleThread extends Thread
{	
	//ArrayList<SaleCD> SaleList;
	MyCDshop cd;

	public SaleThread(MyCDshop cd) {
		this.cd = cd;
	}
	@Override
	public void run() {
		Random r=new Random();
		while(true)
		{
			try {
				Thread.sleep(r.nextInt(200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int id=(int)(Math.random()*10)+1;
			int count=(int)(Math.random()*5)+1;
			synchronized (cd.SaleList) {
				if (cd.saleCD(id, count)) {
					System.out.println(new Date()+" sale CD" + id + " " + count+" 张");
				} else {
					if (r.nextBoolean()) {
						System.out.println(new Date()+" CD not enough");
						cd.SaleList.notify();
						do
						{
							try {
								cd.SaleList.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						while(!cd.saleCD(id, count));
						System.out.println(new Date()+" after wait sale CD" + id + " " + count+" 张");

					}
					else
					{
						System.out.println(new Date()+" give up");
						cd.SaleList.notify();
					}
				}
			}
		}
	}
}
//租借线程
class RentThread extends Thread
{
	MyCDshop cd;
	public RentThread(MyCDshop cd) {
		super();
		this.cd = cd;
	}
	@Override
	public void run() {
		Random r = new Random();
		while(true)
		{
			try {
				Thread.sleep(r.nextInt(200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			int id=(int)(r.nextInt(10)+1);
			
//			System.out.println(id);
//			System.out.println(cd.rentCD(id));
			
			synchronized (cd.RentList) {
				if (cd.rentCD(id-1)) {
					//System.out.println(0);
					System.out.println(new Date()+" rent CD" + " "+ id  );
					try{
						Thread.sleep(r.nextInt(100)+200);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
					cd.backCD(id);
					System.out.println(new Date()+" giveback CD " +id);
				} 
				else {
					//System.out.println(1);
					if (r.nextBoolean()) {
						
						System.out.println(new Date()+" already rented,wait");
						cd.RentList.notify();
						do
						{
							try {
								cd.RentList.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						while(cd.rentCD(id-1));
						System.out.println(new Date()+" after wait,rent CD " + id  );
						try{
							Thread.sleep(r.nextInt(100)+200);
						}catch(InterruptedException e) {
							e.printStackTrace();
						}
						cd.backCD(id);
						System.out.println(new Date()+" giveback CD " +id);
					}
					else
					{
						System.out.println(new Date()+" alrady rented,giveup");
						cd.RentList.notify();
					}
				}

			}
		}
	}
}
//进货线程
class GetInThread extends Thread
{
	MyCDshop cd;

	public GetInThread(MyCDshop cd) {
		this.cd = cd;
		this.setDaemon(true);
	}

	@Override
	public void run() {
		super.run();
		synchronized(cd.SaleList)
		{
			while (true) {
				try {
					cd.SaleList.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cd.getIn();
				System.out.println(new Date()+" getin");
				cd.SaleList.notifyAll();
			}
		}
	}
	
}
//控制线程
class ControlThread extends Thread
{
	MyCDshop cd;

	public ControlThread(MyCDshop cd) {
		this.cd = cd;
		this.setDaemon(true);
	}
	public void run()
	{
		//售卖线程
		new GetInThread(cd).start();	
		new SaleThread(cd).start();		
	   new SaleThread(cd).start();
	   new SaleThread(cd).start();
	   //租赁线程
		new RentThread(cd).start();		
		new RentThread(cd).start();
		new RentThread(cd).start();
		
	}
	
}
public class Main {

	public static void main(String[] args)  throws IOException{
		FileOutputStream fs = new FileOutputStream(new File("D:\\record.txt"));
		PrintStream p=System.out;
		PrintStream ps = new PrintStream(fs);
		System.setOut(ps);
		for(int i=1;i<=2;i++){
			System.out.println("第"+i+"次模拟");
			ControlThread ct=new ControlThread(new MyCDshop());
			ct.start();
			try {
				Thread.sleep(120000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println();
		}	
		System.setOut(p);
		System.out.println("over");
	}
}
