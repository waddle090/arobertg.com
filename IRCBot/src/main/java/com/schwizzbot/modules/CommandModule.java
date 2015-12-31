package com.schwizzbot.modules;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import com.schwizzbot.OSValidator;
import com.schwizzbot.SchwizzBot;

public class CommandModule implements Module{

	@Override
	public boolean run(String channel, String sender, String login, String hostname, String message, SchwizzBot bot,
			HashMap<String, Object> daoMap, List<Object> objectList) {
		if(sender.equals("schwizz") && message.startsWith("!command ")){
			message = message.split("!command ")[1]!=null?message.split("!command ")[1]:"";
			if(!message.isEmpty()){
				try {
					runCommand(message, channel, bot);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		} else return false;
	}
	private void runCommand(String command, String sender, SchwizzBot bot) throws Exception{
		Runtime rt = Runtime.getRuntime();
		
		String osPrefix = (OSValidator.getOS().equals("win"))?"cmd /c ":"";
		Process proc = rt.exec(osPrefix + command);

		BufferedReader stdInput = new BufferedReader(new 
		     InputStreamReader(proc.getInputStream()));

		BufferedReader stdError = new BufferedReader(new 
		     InputStreamReader(proc.getErrorStream()));

//		OutputStream out = proc.getOutputStream();
//		out.write("fooUsername\n".getBytes());
//		out.flush();
		
		// read the output from the command
		System.out.println("Here is the standard output of the command:\n");
		String s = null;
		while ((s = stdInput.readLine()) != null) {
//		    System.out.println(s);
		    if(!s.isEmpty()){
		    	bot.sendMessage(sender, s);
		    }
		    
		}

		// read any errors from the attempted command
		System.out.println("Here is the standard error of the command (if any):\n");
		while ((s = stdError.readLine()) != null) {
		    System.out.println(s);
		    if(!s.isEmpty()){
		    	bot.sendMessage(sender, s);
		    }
		    
		}
	}
	

}
