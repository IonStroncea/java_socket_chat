package Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import Main.Persoana;

public class Server {
	static boolean work;
	public static ArrayList<Persoana> persoane;
	public static void main(String []args) throws Exception
	{
		work=true;
		System.out.println("Server is started");
		Thread t1=new Thread(()->{readComanad();});
		t1.start();
		ServerSocket ss=new ServerSocket(1235);
		ss.setSoTimeout(300);
		System.out.println("Server set server socket");
		persoane=new ArrayList<Persoana>();
		while(work)
		{
			Socket s=null;
			try 
			{
				s=ss.accept();
			}
			catch(java.io.InterruptedIOException e)
			{
				continue;
			}
			Persoana p=new Persoana(s,persoane);
			persoane.add(p);
			p.start();			
		}
		
		t1.join();
		ss.close();
	}
	static void readComanad()
	{
		try
		{
		Scanner in = new Scanner(System.in);
		
		String s="work";
		
		while(!s.equals("exit"))
		{
			for(Persoana p:persoane)
			{	
				System.out.println("Send to:"+p.getNick());
				p.getDout().write("Server:"+s);
				p.getDout().newLine();
				p.getDout().flush();
			}
			s=in.nextLine();
		}
		for(Persoana p:persoane)
		{	
			System.out.println("Send exit to:"+p.getNick());
			
			p.getDout().write("exit");
			p.getDout().newLine();
			p.getDout().flush();
			p.setWork(false);
		}
		
		work=false;
		
		in.close();
		}
		catch(Exception e)
		{
			System.out.println("Error Server");
			System.out.println(e.getMessage());
			System.out.println("Error Server");
		}
	}
}
