package com.schwizzbot.modules;

import java.util.HashMap;
import java.util.List;

import com.schwizzbot.SchwizzBot;

public interface Module {
	public boolean run(String channel, String sender, String login, String hostname, String message, SchwizzBot bot, HashMap<String, Object> daoMap, List<Object> objectList);
}
