package com.schwizzbot.modules;

import java.util.HashMap;
import java.util.List;

import com.schwizzbot.QuoteDao;
import com.schwizzbot.SchwizzBot;

public class QuoteModule implements Module {
	@Override
	public boolean run(String channel, String sender, String login, String hostname, String message, SchwizzBot bot,
			HashMap<String, Object> daoMap, List<Object> objectList) {

		
		if ((message.startsWith("!quote") || message.startsWith("!q"))) {
			QuoteDao quoteDao = (QuoteDao) daoMap.get("quoteDao");
			if (message.equals("!quote") || message.equals("!q")) {

				String m = "";
				// String quote = "";
				// String author = "";
				// JSONObject json = null;
				// try {
				// json =
				// JsonReader.readJsonFromUrl("http://quotesondesign.com/api/3.0/api-3.0.json");
				// quote = json.getString("quote");
				// author = json.getString("author");
				// m = quote + " - " + author;// + " - " +
				// json.getString("author");
				// quoteDao.insertQuote(author, quote, sender);
				//
				// } catch (IOException e) {
				// m = "error getting quote";
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (JSONException e) {
				// m = "json exception";
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				try {
					m = quoteDao.getRandomQuote();
				} catch (Exception e) {
					e.printStackTrace();
				}
				bot.sendMessage(channel, m);
			} else {
				if (message.startsWith("!q add ")) {
					message = message.substring("!q add ".length());
					int id = quoteDao.insertQuote("", message, sender);
					bot.sendMessage(channel, "quote added. " + id);
				} else if (message.startsWith("!quote add ")) {
					message = message.substring("!quote add ".length());
					int id = quoteDao.insertQuote("", message, sender);
					bot.sendMessage(channel, "quote added. " + id);
				} else if (sender.equals("schwizz") && message.startsWith("!q remove ")) {
					message = message.substring("!q remove ".length());
					int id = Integer.parseInt(message);
					quoteDao.removeQuoteById(id);
					bot.sendMessage(channel, "quote removed.");
				} else if (sender.equals("schwizz") && message.startsWith("!quote remove ")) {
					message = message.substring("!quote remove ".length());
					int id = Integer.parseInt(message);
					quoteDao.removeQuoteById(id);
					bot.sendMessage(channel, "quote removed.");
				} else if (message.startsWith("!q ")) {
					message = message.substring("!q ".length());
					int id = Integer.parseInt(message);
					bot.sendMessage(channel, quoteDao.getQuoteById(id));
				} else if (message.startsWith("!quote ")) {
					message = message.substring("!quote ".length());
					int id = Integer.parseInt(message);
					bot.sendMessage(channel, quoteDao.getQuoteById(id));
				}

			}
			return true;
		} else return false;
	}
}
