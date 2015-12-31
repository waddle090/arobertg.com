package com.schwizzbot.modules;

import java.util.HashMap;
import java.util.List;

import com.schwizzbot.SchwizzBot;

public class JoinPartModule implements Module{

	@Override
	public boolean run(String channel, String sender, String login, String hostname, String message, SchwizzBot bot,
			HashMap<String, Object> daoMap, List<Object> objectList) {
		if(sender.equals("schwizz")){
			if (message.startsWith("!join ")){
				message = message.substring("!join ".length());
				System.out.println("joining " + message);
				bot.joinChannel(message);
				return true;
			} else if (message.startsWith("!part ")){
				message = message.substring("!part ".length());
				System.out.println("parting " + message);
				bot.partChannel(message);
				return true;
			} else return false;
			
		} else {
			return false;
		}
	}
}
