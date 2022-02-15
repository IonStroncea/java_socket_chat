package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Persoana extends Thread  {
	private String Nick;
	private Socket s;
	public ArrayList<Persoana> persoane;
	private BufferedWriter dout;
	private BufferedReader di;
	private boolean Work;
	
	public BufferedWriter getDout()throws Exception {
		return dout;
	}

	public void setDout(BufferedWriter dout) {
		this.dout = dout;
	}

	public BufferedReader getDi() {
		return di;
	}

	public void setDi(BufferedReader di) {
		this.di = di;
	}

	public String getNick() 
	{
		return Nick;
	}
	
	public void setNick(String name) 
	{
		if(name!=null && name.length()>0)
		{
			Nick = name;
		}
	}
	
	public Socket getS() 
	{
		return s;
	}
	
	public void setS(Socket s) 
	{
		if(s!=null)
		{
			this.s = s;
		}
	}
	
	public ArrayList<Persoana> getPersoane() 
	{
		return persoane;
	}
	
	public void setPersoane(ArrayList<Persoana> persoane) 
	{
		this.persoane = persoane;
	}
	
	public Persoana(Socket s,ArrayList<Persoana> persoane) throws Exception
	{
		if(s!=null)
		{
			this.s=s;
		}
		else
			throw new Exception("Socket is null");
		if(persoane!=null)
		{
			this.persoane=persoane;
		}
		else
			throw new Exception("Persoane is null");
	}
	public void writeToAll(String str)throws Exception
	{
		System.out.println("Nr of persoane:"+ persoane.size());
		for(Persoana p:persoane)
		{	
			System.out.println("Send to:"+p.getNick());
			p.getDout().write(str);
			p.getDout().newLine();
			p.getDout().flush();
		}
	}

	
	@Override
	public void run()
	{
		try
		{
			Work=true;
			dout=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			di=new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			System.out.println("New person connected to chat");
			dout.write("Write your nickname!!");
			dout.newLine();
			dout.flush();
			
			String str;
			str=di.readLine();
			while(str==null && str.length()<1)
			{
				str=di.readLine();
			}
			setNick(str);
			System.out.println("New nickname is:"+getNick());
			writeToAll(getNick()+" entered the chat");
			while(Work)
			{
				
				str=di.readLine();
				
				if(str.equals("exit"))
				{
					System.out.println("User: "+ getNick()+" exits the chat");
					writeToAll("User: "+ getNick()+" exits the chat");
					persoane.remove(this);
					break;
				}
				else
				{
					System.out.println("User: "+getNick()+" wrote: "+ str);
					writeToAll(getNick()+":"+str);
				}
			}
			
			dout.close();
			di.close();
			s.close();
		}
		catch(Exception e)
		{
			System.out.println("Error Persoana");
			System.out.println(e.getMessage());
			System.out.println("Error Persoana");
		}
		
	}

	public boolean isWork() {
		return Work;
	}

	public void setWork(boolean work) {
		Work = work;
	}
	
}
