package webCrawl;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BasicWebCrawl {
	
    public static void main(String[] args) throws FileNotFoundException {
    	PrintWriter writer = new PrintWriter("D:\\Ecudocs\\links.txt");
    	writer.print("");
    	writer.close();
    	Path links = Paths.get("D:\\Ecudocs\\links.txt");
        Elements start = getPageLinks("https://en.wikipedia.org/wiki/Computer_science", links, -1, 0);
    	ArrayList<Elements> allElements = new ArrayList<Elements>();
    	allElements.add(0, start);
    	
    	FileOutputStream fo = new FileOutputStream("elements.txt");
    	   PrintWriter pw = new PrintWriter(fo);

    	   for (Element elem : start){
    	       pw.println(elem);
    	   }
    	   pw.close();
    	   try {
			fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	int count = 1;
    	int i = 0;
    	while(!allElements.isEmpty()) {
    		for (Element page : allElements.get(0)) {
    			Elements temp = getPageLinks(page.attr("abs:href"), links, i, count);
    			if(!temp.isEmpty()) {
    				allElements.add(count - i, temp);
            		count++;
    			}
        	}
    		i++;
    		allElements.remove(0);
    	}
        
    }
	
	public static Elements getPageLinks(String URL, Path links, int num, int count) throws FileNotFoundException {
		
		if(checkURL(URL, links)) {
			try {
				Files.write(links, URL.getBytes(), StandardOpenOption.APPEND);
				Files.write(links, "\n".getBytes(), StandardOpenOption.APPEND);
				System.out.println(num+1);
				System.out.println(URL + "   " + count);
				//2. Fetch the HTML code
				Document document = Jsoup.connect(URL).get();
				//3. Parse the HTML to extract links to other URLs
				Elements linksOnPage = document.select("a[href]");
				return linksOnPage;
        
			} catch (IOException e) {
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}
		return new Elements();
	}
	
	public static boolean checkURL(String URL, Path links) throws FileNotFoundException {
		
		if(URL.contains("https://en.wikipedia.org/wiki/") 
				&& ((notBadURL(URL)) || (URL.equals("https://en.wikipedia.org/wiki/History_of_computer_science")|| URL.equals("https://en.wikipedia.org/wiki/BBC_Model_B")) && (!URL.equals("https://en.wikipedia.org/wiki/Apple") ))) {
			try{
				InputStream in = Files.newInputStream(links);
				Scanner reader = new Scanner(in, "UTF-8");
					    while (reader.hasNextLine()) {
					    	String line = reader.nextLine();
					        if (URL.equals(line)) {
					        	reader.close();
					        	in.close();
					        	return false;
					        }
					    }
					    reader.close();
					    in.close();
			} catch (IOException e) {
				
			}
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean notBadURL(String URL) throws FileNotFoundException {
		File urls = new File("D:\\Ecudocs\\notInclude.txt");
		Scanner myReader = new Scanner(urls);
		String data;
		while (myReader.hasNext()) {
			data = myReader.next();
			if(URL.contains(data)) {
				myReader.close();
				return false;
			}
		}
		myReader.close();
		return true;
	}
	
}
