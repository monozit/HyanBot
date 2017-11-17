import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class ReplyTweet {
	
	Random rand = new Random();

	public void reply(Twitter twitter) throws FileNotFoundException, IOException, TwitterException, URISyntaxException{
		// get recent mentions and reply
		Paging p = new Paging();
		p.setCount(15);
		ResponseList<Status> mentions = twitter.getMentionsTimeline(p);

		for(Status mention : mentions){
			if(!mention.isFavorited()){
				String tweet = mention.getText();
				int title_idx;
				if(checkImage(mention)){
					replyRecogResult(twitter, mention);
				}else if(checKeshizumi(tweet)){
					replyAbout(twitter, mention, "keshizumi");
				}else if(checkOp(tweet)){
					replyAbout(twitter, mention, "op");
				}else if(checkEd(tweet)){
					replyAbout(twitter, mention, "ed");
				}else if(checkKawaii(tweet)){
					replyAbout(twitter, mention, "kawaii");
				}else if(checkGif(tweet)){
					replyAbout(twitter, mention, "gif");
				}else if(checkTitle(tweet)){
					replyAbout(twitter, mention, "title");
				}else if((title_idx = checkTitleAt(tweet)) != -1){
					replyAboutTitle(twitter, mention, "title", title_idx);
				}
//				String storyNum;
//				if(checkImage(mention)){
//					replyRecogResult(twitter, mention);
//				}else if(checkSyntax("data/text/syntax_keshizumi.txt", tweet) != null){
//					replyAbout(twitter, mention, "keshizumi");
//				}else if(checkSyntax("data/text/syntax_op.txt", tweet) != null){
//					replyAbout(twitter, mention, "op");
//				}else if(checkSyntax("data/text/syntax_ed.txt", tweet) != null){
//					replyAbout(twitter, mention, "ed");
//				}else if(checkSyntax("data/text/syntax_kawaii.txt", tweet) != null){
//					replyAbout(twitter, mention, "kawaii");
//				}else if(checkSyntax("data/text/syntax_gif.txt", tweet) != null){
//					replyAbout(twitter, mention, "gif");
//				}else if(checkSyntax("data/text/syntax_title.txt", tweet) != null){
//					replyAbout(twitter, mention, "title");
//				}else if((storyNum = checkSyntax("data/text/syntax_title.txt", tweet)) != null){
//					int num = Integer.parseInt(storyNum);
//					replyAboutTitle(twitter, mention, "title", num);
//				}
			}
		}
	}

	private void replyAbout(Twitter twitter, Status mention, String reply_id)
			throws TwitterException, IOException{
		String fname = "data/text/reply_" + reply_id + ".txt";
		File file = new File(fname);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "Shift-JIS"));
		ArrayList<String> rep = new ArrayList<>();
		String line;
		while((line = br.readLine()) != null){
			rep.add(line);
		}
		br.close();

		// sort images in ascending order
		File[] files = FileSort.sort(reply_id);
		
		// get reply sentence
		int random = rand.nextInt(files.length);
		String rep_message = (random < rep.size()) ? rep.get(random) : "";
		
		// post to twitter
		String reply = "@" + mention.getUser().getScreenName()
				 + " " + rep_message;
		StatusUpdate su = new StatusUpdate(reply);
		su.setInReplyToStatusId(mention.getId());
		twitter.updateStatus(su.media(files[random]));
		twitter.createFavorite(mention.getId());
		System.out.println("Reply:" + rep_message);
	}
	
	private void replyAboutTitle(Twitter twitter, Status mention, String reply_id, int title_idx)
			throws TwitterException, IOException{
		
		String fname = "data/text/reply_" + reply_id + ".txt";
		File file = new File(fname);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "Shift-JIS"));
		ArrayList<String> rep = new ArrayList<>();
		
		// get reply sentence
		String line;
		while((line = br.readLine()) != null){
			rep.add(line);
		}
		br.close();

		// sort images in ascending order
		File[] files = FileSort.sort(reply_id);
	
		// get reply sentence
		String rep_message = "";
		if((0 <= title_idx-1) && (title_idx-1 < rep.size())){
			rep_message = rep.get(title_idx-1);
		}
		
		// post to twitter
		String reply = "@" + mention.getUser().getScreenName()
				 + " " + rep_message;
		StatusUpdate su = new StatusUpdate(reply);
		su.setInReplyToStatusId(mention.getId());
		twitter.updateStatus(su.media(files[title_idx-1]));
		twitter.createFavorite(mention.getId());
		System.out.println("Reply:" + rep_message);
	}
	
	private void replyRecogResult(Twitter twitter, Status mention) throws URISyntaxException, ClientProtocolException, IOException, TwitterException{
		
		// get url and download image
		MediaEntity[] medias = mention.getMediaEntities();
		URL url = new URL(medias[0].getMediaURL());
		String imageName = (Paths.get(url.getPath())).getFileName().toString();
		System.out.println(imageName);
		InputStream in = url.openStream();
		FileOutputStream out = new FileOutputStream("data/image/recog/" + imageName);
		IOWriter.outWriter(in, out);
		in.close();
		out.close();
		
		// recognize image
		File recogImage = new File("data/image/recog/" + imageName);
		PredictInfo result = (new Recognizer()).execute(recogImage, "data/setting/customvision_claire2.properties");
		recogImage.delete();
		
		if(result.getTag().equals("error")){
			System.out.println("entity is null");
			return;
		}
		
		String rep_message = "";
		if((result.getTag().equals("Claire"))
				&& (result.getProbability() > 0.0)){
			double percent = (double)(result.getProbability() * 100);
			if(percent > 80) 
				rep_message = "【" + percent + "%】の確率で私よ！";
			else
				rep_message = "【" + percent + "%】の確率で私かもしれないわ";
		}else{
			rep_message = "この画像は私じゃないわね..";
		}
		
		// post to twitter
		String reply = "@" + mention.getUser().getScreenName()
						 + " " + rep_message;
		StatusUpdate su = new StatusUpdate(reply);
		su.setInReplyToStatusId(mention.getId());
		twitter.updateStatus(su);
		twitter.createFavorite(mention.getId());
		System.out.println("Reply:" + rep_message);
	}

	private boolean check(String regex, String target){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(target);
		boolean match = m.find();
		return match;
	}
	
	private String checkSyntax(String filePath, String target) throws IOException{
		ArrayList<String> words = new ArrayList<>();
		File file = new File(filePath);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "Shift-JIS"));
		
		// get sentences
		String line;
		while((line = br.readLine()) != null){
			words.add(line);
		}
		br.close();
		
		for(String word : words){
			if(check(word, target)) return word;
		}
		return null;
	}

	private boolean checKeshizumi(String target){
		String[] strs = {
				"まないた","まな板","マナイタ","manaita",
				"ひんにゅう","貧乳","ヒンニュウ","hinnnyu",
				"つるぺた","ツルペタ","ぺったんこ","ぺたんこ","ぺちゃんこ","ペチャンコ",
				"残念な胸"
				};
		for(String str : strs){
			if(check(str, target)) return true;
		}
		return false;
	}

	private boolean checkOp(String target){
		String[] strs = {
				"op","Op","OP"
				};
		for(String str : strs){
			if(check(str, target)) return true;
		}
		return false;
	}

	private boolean checkEd(String target){
		String[] strs = {
				"ed","Ed","ED"
				};
		for(String str : strs){
			if(check(str, target)) return true;
		}
		return false;
	}

	private boolean checkKawaii(String target){
		String[] strs = {
				"かわいい","可愛い","カワイイ"
				};
		for(String str : strs){
			if(check(str, target)) return true;
		}
		return false;
	}

	private boolean checkGif(String target){
		String[] strs = {
				"gif","GIF","Gif"
				};
		for(String str : strs){
			if(check(str, target)) return true;
		}
		return false;
	}
	
	private boolean checkTitle(String target){
		String[] strs = {
				"Title","title","TITLE","たいとる","タイトル"
				};
		for(String str : strs){
			if(check(str, target)) return true;
		}
		return false;
	}
	
	private int checkTitleAt(String target){
		String title_idx_str = target.replaceAll("[^0-9]", "");
		String[] strs = {
				"1","2","3","4","5","6",
				"7","8","9","10","11","12"
				};
		for(String str : strs){
			if(str.equals(title_idx_str)) return Integer.parseInt(title_idx_str);
		}
		return -1;
	}
	
	private boolean checkImage(Status mention) throws MalformedURLException, URISyntaxException{
		MediaEntity[] medias = mention.getMediaEntities();
		if(medias.length == 0) return false;
		
		// only first image recognize
		String url = medias[0].getMediaURL();
		
		// extension ".jpg(.JPG)" and ".png(.PNG)" and ".gif" exist
		if((url.toLowerCase().indexOf(".jpg")) != -1) return true;
		if((url.toLowerCase().indexOf(".png")) != -1) return true;
		if((url.toLowerCase().indexOf(".gif")) != -1) return true;
		
		return false;
	}
}
