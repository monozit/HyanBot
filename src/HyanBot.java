import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class HyanBot {

	final static int DEFAULT_NORMAL_INTERVAL = 7200000;
	final static int DEFAULT_REPLY_INTERVAL = 1800000;
	static int NORMAL_INTERVAL = DEFAULT_NORMAL_INTERVAL;	// 2 hours
	static int REPLY_INTERVAL = DEFAULT_REPLY_INTERVAL;	// 30 minutes

	public static void main(String[] args) {
		
		System.out.println("--- ひゃんbot起動 ---");
		
		try {
			setting();
		} catch (FileNotFoundException e1) {
			System.out.println("setting file not found.");
			System.out.println("Please check \"setting/setting.properties\"");
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Twitter twitter = new TwitterFactory().getInstance();
		// normal tweet(default:2 hours interval)
		Timer timer_normal = new Timer();
		timer_normal.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				try {
//					LocalDateTime now = LocalDateTime.now();
//					int hour = now.getHour();
//					if(hour < 7) return;	// do not tweet from 0 to 6
					(new NormalTweet()).post(twitter);
				} catch (TwitterException e) {
					System.out.println("maybe twitter is down.");
					e.printStackTrace();
					//timer_normal.cancel();
				} catch (FileNotFoundException e) {
					System.out.println("File not found..");
					e.printStackTrace();
					//timer_normal.cancel();
				} catch (IOException e) {
					e.printStackTrace();
					//timer_normal.cancel();
				}
			}
		}, 0, NORMAL_INTERVAL);

		// reply tweet(default:30 minutes interval)
		Timer timer_reply = new Timer();
		timer_reply.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				try {
					(new ReplyTweet()).reply(twitter);
				} catch (TwitterException e) {
					System.out.println("maybe twitter is down.");
					e.printStackTrace();
					//timer_reply.cancel();
				} catch (FileNotFoundException e) {
					System.out.println("File not found..");
					e.printStackTrace();
					//timer_reply.cancel();
				} catch (URISyntaxException e) {
					// TODO 自動生成された catch ブロック
					System.out.println("Invalid URL.");
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
					//timer_reply.cancel();
				} 
			}
		}, 0, REPLY_INTERVAL);
	}

	public static void setting() throws IOException{
		// Load properties
		Properties properties = PropertiesLoader.load("data/setting/setting.properties");
		
		// set interval
		String normalInterval = properties.getProperty("normalInterval", String.valueOf(DEFAULT_NORMAL_INTERVAL));
		NORMAL_INTERVAL = Integer.parseInt(normalInterval);
		String replyInterval = properties.getProperty("replyInterval", String.valueOf(DEFAULT_REPLY_INTERVAL));
		REPLY_INTERVAL = Integer.parseInt(replyInterval);
	}

}
