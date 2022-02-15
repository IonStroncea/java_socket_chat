package Main;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
	static BufferedWriter dout;
	static BufferedReader di;
	static Boolean open;
	public static void main(String []args) 
	{
		try
		{
			open=true;
			Scanner in = new Scanner(System.in);
			Socket s=new Socket("localhost",1235);
		
			dout=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			di=new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			Thread t1=new Thread(()->{readServer();});
			t1.start();
			
			String str;
			
			while(open)
			{
				str=in.nextLine();
				if(!open)
				{
					break;
				}
				if(str.length()>0)
				{
					dout.write(str);
					dout.newLine();
					dout.flush();
					if(str.equals("exit"))
					{
						System.out.println("Exits the chat:");
						break;
					}
				}
			}
				
			open=false;
			dout.close();
			di.close();
			s.close();
			in.close();
			t1.join();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	static void readServer()
	{
		try
		{
			System.out.println("Start reading");
			while(open)
			{
				String str=di.readLine();
				if(str.equals("exit"))
				{
					System.out.println("Exiting from server");
					open=false;
					break;
				}
				System.out.println(str);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}


}
