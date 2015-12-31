package com.schwizzbot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mysql.jdbc.Connection;


public class Main {

	public static void main(String[] args) {
		ApplicationContext context = 
	             new ClassPathXmlApplicationContext("beans.xml");
		SchwizzBot bot = (SchwizzBot) context.getBean("schwizzBot");		
		connect(bot);
          
	}	
	private static void connect(SchwizzBot bot){
		try {
//        	System.out.println("Print all quotes test...");
//        	bot.printAllQuotes();
//        	bot.printRandomQuote();
			bot.connect("irc.freenode.net");
			bot.sendNotice("", "connected.");
			// Join the #pircbot channel.
			bot.sendMessage("NickServ", "identify ");
			
		} catch (NickAlreadyInUseException e) {
			// TODO Auto-generated catch block
			System.out.println("nickname already in use, trying to connect again");
			e.printStackTrace();
			bot.disconnect();
			connect(bot);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IO Exception, trying to connect again");
			e.printStackTrace();
			bot.disconnect();
			connect(bot);
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			System.out.println("IRC exception, trying to connect again");
			e.printStackTrace();
			bot.disconnect();
			connect(bot);
		}
	}
}
