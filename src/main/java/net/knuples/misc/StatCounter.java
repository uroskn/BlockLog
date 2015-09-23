package net.knuples.misc;

public class StatCounter {

	private int rate;
	private int prevdate;
	private int crate;
	
	private int unixepoch()
	{
		return (int) (System.currentTimeMillis() / 1000L);
	}
	
	public StatCounter()
	{
		this.rate = 0;
		this.crate = 0;
		this.prevdate = 0;
	}
	
	private void CheckDate()
	{
		if (this.prevdate != this.unixepoch())
		{
			this.rate = 0;
			if ((this.unixepoch() - 1) == this.prevdate) this.rate = this.crate;
			this.crate = 0;
			this.prevdate = this.unixepoch();
		}
	}
	
	public void Count()
	{
		this.CheckDate();
		this.crate++;
	}
	
	public int GetRate()
	{
		this.CheckDate();
		return this.rate;
	}
	
}
