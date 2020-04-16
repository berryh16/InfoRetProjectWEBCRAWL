package webCrawl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javafx.util.Pair;

public class getData {
	
	public static void main(String[] args) {
		try {
    		FileInputStream in = new FileInputStream("D:\\Ecudocs\\links.txt");
    		Scanner fileRead = new Scanner(in);
    		String nextLink;
    		int num = 0;
    		while(fileRead.hasNext()) {
    			nextLink = fileRead.next();
    			makeDoc(nextLink, num);
    			num++;
    		}
    		fileRead.close();
			in.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void makeDoc(String URL, int num) {
		Document document;
		try {
			document = Jsoup.connect(URL).get();
			document = document.normalise();
			String data = document.text();
			String name = URL.substring(30);
			name = name.replace('/', '~');
			name = name.replace('*', '~');
			name = name.replace('~', '~');
			FileOutputStream fo = new FileOutputStream("D:\\Ecudocs\\wikiCorpus\\" + name + ".txt");
	    	PrintWriter pw = new PrintWriter(fo);
	    	pw.println(data);
	    	pw.close();
	 		fo.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
