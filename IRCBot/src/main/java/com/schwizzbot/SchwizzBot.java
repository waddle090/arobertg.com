package com.schwizzbot;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.jibble.pircbot.*;
import org.json.JSONException;
import org.json.JSONObject;

import com.schwizzbot.modules.Module;




public class SchwizzBot extends PircBot {
	
	//DI
	private QuoteDao quoteDao;
	public void setQuoteDao(QuoteDao quoteDao){
		this.quoteDao = quoteDao;
	}
	private InsultDao insultDao;
	public void setInsultDao(InsultDao insultDao){
		this.insultDao = insultDao;
	}
	private ModuleDao moduleDao;
	public void setModuleDao(ModuleDao moduleDao){
		this.moduleDao = moduleDao;
	}
	
	private Map<String, String> insultSessions = new HashMap<>();
	private Map<String, String> byteArrayStringMap = new HashMap<>();
	private DataSource ds;
	
	//default contructer
	public SchwizzBot(String name){
		this.setName(name);
		this.setLogin(name);
	}
	
	public SchwizzBot(String name, DataSource ds) {
		this.setName(name);
		this.setLogin(name);
		this.ds = ds;
	}
	
	@Override
	public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason){
		if(getName().equals(recipientNick)){
			this.joinChannel(channel);
			this.sendMessage("ChanServ", "OP "+channel+" schwizzBot");
			String insult = insultDao.getRandomInsult();
			this.sendMessage(channel, kickerNick + ", " + insult);
			insultSessions.put(kickerNick, insult);
		}
	}
	
	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {		
		
		HashMap<String, Object> daoMap = new HashMap<>();		
		daoMap.put("insultDao", insultDao);
		daoMap.put("quoteDao", quoteDao);
		
		List<String> classList = moduleDao.getModules();//new ArrayList<>();				
		JavaClassLoader javaClassLoader = null;
		Class c = null;
		List<Object> objectList = new ArrayList<>();
		objectList.add(0,insultSessions);
		for(String className:classList){
			if(className.contains("CommandModule")){
				//skip
			} else {
				System.out.println("Loading module: " + className);
				try {
					javaClassLoader = new JavaClassLoader();
					c = javaClassLoader.loadClass(className);
					Module m = (Module) c.newInstance();
					if(m.run(channel, sender, login, hostname, message, this, daoMap, objectList))return;				
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {				
					e.printStackTrace();
				}
			}
		}
		
		if(sender.equals("schwizz")){
			if(message.startsWith("!command ")){
				if(message.startsWith("!command -i")) {
					message = message.split("!command -i ")[1]!=null?message.split("!command -i ")[1]:"";
					if(!message.isEmpty()){					
						try {
							out.write((message+"\n").getBytes());
							out.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				} else {
					message = message.split("!command ")[1]!=null?message.split("!command ")[1]:"";
					if(!message.isEmpty()){					
						try {
							runCommand(message, channel, this);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else if(message.startsWith("!msg -target ")){
				message = message.split("!msg -target ")[1]!=null?message.split("!msg -target ")[1]:"";
				String chan = message.split(" ")[0];
				message = message.substring((chan + " ").length());
				if(!message.isEmpty()){
					this.sendMessage(chan, message);					
				}
			}
		}
		
		//do this last
		if(message.contains(this.getName())){
//			String insult = insultDao.getRandomInsult();
			this.sendMessage(channel, sender + ", I don't think you know what you are talking about.");
//			insultSessions.put(sender, insult);
		}
		
		
	}
	
	private OutputStream out;
	
	private void runCommand(String command, String sender, SchwizzBot bot) throws Exception{
		Runtime rt = Runtime.getRuntime();	
		String osPrefix = (OSValidator.getOS().equals("win"))?"cmd /c ":"";
		Process proc = rt.exec(osPrefix + command);

		BufferedReader stdInput = new BufferedReader(new 
		     InputStreamReader(proc.getInputStream()));

		BufferedReader stdError = new BufferedReader(new 
		     InputStreamReader(proc.getErrorStream()));

		this.out = proc.getOutputStream();
		
		
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
	


	
	private boolean captureNextRow = false;
	private Vote currentVote;
	@Override
	public void onNotice(String sender, String login, String hostname, String target, String message){
		if(sender.equals("SPQF")){
//			-SPQF- #860: Information on vote #860: CLOSED
//			-SPQF- #860: [opine]: schwizz doesn't know what he is talking about
//			-SPQF- #860: CHANNEL  : ##politics
//			-SPQF- #860: SPEAKER  : darkprincess
//			-SPQF- #860: STARTED  : 1449001071
//			-SPQF- #860: ENDED    : 1449001191
//			-SPQF- #860: YEA      : 3 - darkprincess, junka, jzk, 
//			-SPQF- #860: YOU      : ---
//			-SPQF- #860: RESULT   : PASSED
			if(message.contains("Information on vote #1")){
				captureNextRow = true;
				currentVote = new Vote();
			} else if(captureNextRow){
				
				captureNextRow = false;
			}else if(message.contains("1: RESULT")){
				currentVote.setRESULT(message);
			}
		} else {
			if(message.contains("start capture SPQF")){
				sendMessage("SPQF", "!vote 1");
			}
		}
	}
	
	@Override
	public void onPrivateMessage(String sender, String login, String hostname, String message) {
//		System.out.println(message);
		this.sendNotice("schwizz", "schwizzBotPM: "+message);
		boolean authenticated = sender.equals("schwizz");
		if (authenticated) {
			if (message.startsWith("!add ")) {
				
				String newUser = message.split(" ")[1] != null ? message.split(" ")[1] : "";
				if (!newUser.isEmpty()) {
					this.sendNotice("schwizz", "adding bot " + newUser);
					SchwizzBot bot = new SchwizzBot(newUser, ds);
					// incase the nick as already taken, try until we find one
					// that isnt
					bot.setAutoNickChange(true);
					// Enable debugging output.
//					bot.setVerbose(true);

					// Connect to the IRC server.
					try {
						bot.connect("");
						// Join the #pircbot channel.
						bot.joinChannel("");

					} catch (NickAlreadyInUseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IrcException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					this.sendNotice("schwizz", "no user name, usage !add_username");
				}
			} else if (message.startsWith("!remove")) {
				
				this.quitServer();
				
			} else if (this.getName().equals("schwizzBot") && message.startsWith("!send ")) {//ony schwizzBot can send
				this.sendNotice("schwizz", "send started.");
				
				String fileName = "C:\\Documents and Settings\\Tier1\\My Documents\\Downloads\\" +  message.split("!send ")[1];
				String byteArray = Util.generateHexStringFromFileByteArray(fileName);
				System.out.println(byteArray);

				this.sendMessage("schwizzBot2", "!captureFile_0:Starting:Sending file " + message.split("!send ")[1]);

				boolean cont = true;
				int i = 1;
				while (cont) {
					String data = "!captureFile_" + i + ":" + byteArray;
					if (data.length() > 420) {
						String part = data.substring(0, 419);
						System.out.println("Sending part..." + part);
						this.sendMessage("schwizzBot2", part);
						byteArray = data.substring(419);
					} else {
						System.out.println("Sending last part..." + data);
						this.sendMessage("schwizzBot2", data);
						cont = false;
					}
					++i;
				}

				this.sendMessage("schwizzBot2", "!captureFile_" + i + ":Completed," + message.split("!send_")[1]);
				this.sendNotice("schwizz", "send finished.");
			}
		}
		
		//
		//
		// file sent from schizzBot to schwizzBot2
		authenticated = sender.equals("schwizzBot") && this.getName().equals("schwizzBot2");
		if (authenticated) {
			if (message.startsWith("!captureFile_")) {//ony schwizzBot2 can captureFile
				if(message.startsWith("!captureFile_0:Starting")){
					byteArrayStringMap.put(this.getName(), "");
				} else if (message.contains("Completed")) {
					// convert byte array back to BufferedImage
					System.out.println(byteArrayStringMap.get(this.getName()));
					InputStream in = new ByteArrayInputStream(Util.hexStringToByteArray(byteArrayStringMap.get(this.getName())));
					BufferedImage bImageFromConvert;
					try {
						bImageFromConvert = ImageIO.read(in);
	
						ImageIO.write(bImageFromConvert, "jpg", new File("C:\\Documents and Settings\\Tier1\\My Documents\\Downloads\\" + message.split(":Completed,")[1]));
//						ImageIO.write(bImageFromConvert, "jpg", new File("C:\\Documents and Settings\\Tier1\\My Documents\\Downloads\\test2.jpg"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					byteArrayStringMap.put(this.getName(), "");
					
				} else {
					byteArrayStringMap.put(this.getName(), byteArrayStringMap.get(this.getName()) + message.split(":")[1]);
					System.out.println("Recieving part..." + message.split(":")[1]);
				}
			}
		}
	}
	
	public void printAllQuotes(){
		quoteDao.printAllQuotes();
	}
	public void printRandomQuote(){
		System.out.println(quoteDao.getRandomQuote());
	}
//	@Override
//	public void onDisconnect(){
//		this.reconnect();
//	}
	
	
}
