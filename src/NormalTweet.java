import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class NormalTweet {
	private String fileName = "data/text/normal_tweet.txt";

	public void post(Twitter twitter) throws TwitterException, FileNotFoundException, IOException{
		
		ArrayList<String> sentences = new ArrayList<>();
		File file = new File(fileName);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "Shift-JIS"));
		
		// get sentences
		String line;
		while((line = br.readLine()) != null){
			sentences.add(line);
		}
		br.close();
		
		// sort images in ascending order
		File[] imageFiles = FileSort.sort("hyan");
		int imageFileNum = imageFiles.length;

		// get my latest tweet
		ResponseList<Status> tl = twitter.getUserTimeline("claire_hyan");
		String myLatestTweet = tl.get(0).getText();
		int imageUrlIdx = myLatestTweet.indexOf(" https://");
		if(imageUrlIdx != -1)
			myLatestTweet = myLatestTweet.substring(0, imageUrlIdx);

		// decide the tweet content not to repeat the same tweet
		Random rand = new Random();
		int random = 0;
		String sentence = "";
		do{
			random = rand.nextInt(imageFileNum);
			sentence = (random < sentences.size()) ? sentences.get(random) : "";
			sentence = sentence.replace("[改行]", "\n");
		}while(myLatestTweet.equals(sentence));

		// post to twitter
//		twitter.updateStatus(
//				new StatusUpdate(sentence).media(imageFiles[random]));
//		System.out.println(sentence);
	}
}
