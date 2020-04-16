package webCrawl;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        Elements start = getPageLinks("https://en.wikipedia.org/wiki/Computer_science", links, -1, -2);

    	FileOutputStream fo = new FileOutputStream("D:\\Ecudocs\\elements.txt");
    	PrintWriter pw = new PrintWriter(fo);

    	for (Element elem : start){
    		String s = elem.attr("href");
    		if(s.charAt(0) == '/') {
    			if(s.charAt(1) == 'w' && s.charAt(2) == 'i') {
    				s = "https://en.wikipedia.org" + s;
    				if(checkURL(s, links)) {
    	    			pw.println(s + " " + -1);
    	    		}
    			}
    		}
    	}
    	
    	try {
    		FileInputStream in = new FileInputStream("D:\\Ecudocs\\elements.txt");
    		Scanner fileRead = new Scanner(in);
    		String nextLink;
    	
    		int originDoc = 0;
    		int count = 0;
    		while(fileRead.hasNext()) {
    			nextLink = fileRead.next();
    			originDoc = fileRead.nextInt();
    			Elements temp = getPageLinks(nextLink, links, count, originDoc);
    			
    					if(!temp.isEmpty()) {
    						count++;

    						for (Element elem : temp){
    							String s = elem.attr("href");
    							if(s.charAt(0) == '/') {
    				    			   if(s.charAt(1) == 'w' && s.charAt(2) == 'i') {
    				    				   s = "https://en.wikipedia.org" + s;
    				    				   if(checkURL(s, links)) {
    	    				    			   pw.println(s + " " + count);
    	    				    		   }
    				    			   }
    				    		   }
    						}
    					}
    			
    		}
    		fileRead.close();
			in.close();
    	}catch (IOException e){
    		
    	}
    	
 	   try { 
 		  pw.close();
 		  fo.close(); 
 		  
 		  
		} catch (IOException e) {
			e.printStackTrace();
		}
 	   	try {
			Files.write(links, ("https://en.wikipedia.org/wiki/Alan_Turing").getBytes(), StandardOpenOption.APPEND);
			Files.write(links, "\n".getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
    }
	
	public static Elements getPageLinks(String URL, Path links, int count, int doc) throws FileNotFoundException {
		
		if(dup(URL, links)) {
			try {
				Files.write(links, URL.getBytes(), StandardOpenOption.APPEND);
				Files.write(links, "\n".getBytes(), StandardOpenOption.APPEND);
				System.out.println(URL + "   " + count + "   " + doc);
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
		
		if((URL.contains("https://en.wikipedia.org/wiki/")) && 
				((notBadURL(URL)) || 
				(URL.equals("https://en.wikipedia.org/wiki/BBC_Model_B")) && 
			(!URL.equals("https://en.wikipedia.org/wiki/Apple"))) && !URL.equals("https://en.wikipedia.org/wiki/Code")) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean dup(String URL, Path links) throws FileNotFoundException {
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
			e.printStackTrace();
		}
		return true;
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
