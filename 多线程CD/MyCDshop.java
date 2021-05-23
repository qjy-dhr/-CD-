package ∂‡œﬂ≥ÃCD;
import java.util.ArrayList;


public class MyCDshop {

	ArrayList<RentCD> RentList =new ArrayList<RentCD>();
	ArrayList<SaleCD> SaleList =new ArrayList<SaleCD>();
	public MyCDshop() {
		for(int i=1;i<=10;i++) {
			RentList.add(new RentCD(i,false));
			SaleList.add(new SaleCD(i,"SaleCd"+i,10));
		}
		
	}
	public void getIn()
	{
		for(SaleCD sc:SaleList)
		{
			sc.count=10;
		}
	}
	public boolean saleCD(int id,int count){
		for(SaleCD sc:SaleList)
		{
			if (sc.id==id)
			{
				if(sc.count>=count)
				{
					sc.count=sc.count-count;
					return true;
				}
				else
				{
					return false;
				}
				
			}
	}
		return false;
	
	}
	public boolean rentCD(int id){
		for(RentCD rc:RentList)
		{
			if (rc.id==id)
			{
				if(rc.isRent==false)
				{
					rc.isRent=true;
				//	System.out.println(1);
					return true;
				}
				else
				{
					//System.out.println(2);
					return false;
				}
			}
			
	}
		//System.out.println(3);
		return false;
	}
	
	public void backCD(int id){
		for(RentCD rc:RentList)
		{
			if (rc.id==id)
			{
				if(rc.isRent==true)
				{
					rc.isRent=false;
				}
			}
		}
	}

}