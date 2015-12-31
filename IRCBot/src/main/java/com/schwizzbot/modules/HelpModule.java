package com.schwizzbot.modules;

import java.util.HashMap;
import java.util.List;

import com.schwizzbot.SchwizzBot;

public class HelpModule implements Module{

	@Override
	public boolean run(String channel, String sender, String login, String hostname, String message, SchwizzBot bot,
			HashMap<String, Object> daoMap, List<Object> objectList) {
		// TODO Auto-generated method stub
		if (sender.equals("schwizz") && (message.startsWith("!help") || message.equalsIgnoreCase("!h"))){			
			bot.sendNotice("schwizz", "!help,!h");
			bot.sendNotice("schwizz", "/msg schwizzBot !add [user_name]");
			bot.sendNotice("schwizz", "/msg [user_name] !remove [user_name]");
			bot.sendNotice("schwizz", "/msg schwizzBot !send [file_name]");
			
			bot.sendNotice("schwizz", "!quote,!q");
			bot.sendNotice("schwizz", "!quote [id]");
			bot.sendNotice("schwizz", "!quote add [quote]");
			bot.sendNotice("schwizz", "!quote remove [id]");
			bot.sendNotice("schwizz", "!join [channel]");
			bot.sendNotice("schwizz", "!part [channel]");
			return true;
		} else if (message.startsWith("!help") || message.equalsIgnoreCase("!h")){
			bot.sendNotice(sender, "!help,!h");
			bot.sendNotice(sender, "!quote,!q");
			bot.sendNotice(sender, "!quote [id]");
			bot.sendNotice(sender, "!quote add [quote]");
			return true;
		} else return false;
	}

}
