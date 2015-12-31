package com.schwizzbot.modules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.schwizzbot.ImmutablePair;
import com.schwizzbot.InsultDao;
import com.schwizzbot.SchwizzBot;

public class InsultModule implements Module {

	@Override
	public boolean run(String channel, String sender, String login, String hostname, String message, SchwizzBot bot,
			HashMap<String, Object> daoMap, List<Object> objectList) {
		
		Map<String, String> insultSessions = (Map<String, String>) objectList.get(0);
		// check against monkey island insults
		InsultDao insultDao = (InsultDao) daoMap.get("insultDao");
		ImmutablePair<Boolean, String> pair = insultDao.checkInsult(message);

		if (pair.getLeft()) {
			bot.sendMessage(channel, pair.getRight());
			return true;
		} else {
			String keyToRemove = "";
			for(String key: insultSessions.keySet()){
				if(sender.equals(key)){
					if(insultDao.checkResponse(message, insultSessions.get(key))){
						keyToRemove = key;
						bot.sendMessage(channel, sender + ", You're good enough to fight the Sword Master!");
						break;
					}
				}
			}
			if(!keyToRemove.isEmpty()) {
				insultSessions.remove(keyToRemove);
				return true;
			} else return false;			
		} 
	}
}
