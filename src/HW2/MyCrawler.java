package HW2;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.*;

import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
	                                                       + "|png|mp3|mp4|zip|gz))$");
	List<String[]> status = new LinkedList<>();
	List<String[]> pages = new LinkedList<>();
	List<String[]> oks = new LinkedList<>();
	
	HashSet<String> extractok = new HashSet<>();
	HashSet<String> extractnotok = new HashSet<>();
	
	int numberOfAttampts = 0;
	int numberOfSucceed = 0;
	int numberOfNotSucceed = 0;
	
	int numberOfTotalExtract = 0;
	int numberOfUniqueExtract = 0;
	int numberOfUniqueIn = 0;
	int numberOfUniqueOut = 0;
	 @Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String[] line = new String[2];
		String href = url.getURL().toLowerCase();
		line[0] = url.getURL();
		line[1] = href.startsWith("https://www.nbcnews.com/") ? "OK" : "N_OK";
		oks.add(line);
		return !FILTERS.matcher(href).matches() && href.startsWith("https://www.nbcnews.com/");
	}
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        // Do nothing by default
        // Sub-classed can override this to add their custom functionality
    	String[] line = new String[2];
    	line[0] = webUrl.getURL();
    	line[1] = statusCode + "";
    	status.add(line);
    	numberOfAttampts++;
    	if(statusCode == 200)
    		numberOfSucceed++;
    	else
    		numberOfNotSucceed++;
    }
    public void onBeforeExit() {
        // Do nothing by default
        // Sub-classed can override this to add their custom functionality
    	String outputFile = "fetch_NewsSite.csv";
        
        // before we open the file check to see if it already exists
        boolean alreadyExists = new File(outputFile).exists();
             
        try {
            // use FileWriter constructor that specifies open for appending
            CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
             
            // if the file didn't already exist then we need to write out the header line
            if (!alreadyExists)
            {
                csvOutput.write("url");
                csvOutput.write("status");
                csvOutput.endRecord();
            }
            // else assume that the file already has the correct header line
             
            // write out a few records
            for(String[] line : status) {
            	csvOutput.write(line[0]);
                csvOutput.write(line[1]);
                csvOutput.endRecord();
            }             
            csvOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    	String outputFile2 = "visit_NewsSite.csv";
        
        // before we open the file check to see if it already exists
        boolean alreadyExists2 = new File(outputFile2).exists();
             
        try {
            // use FileWriter constructor that specifies open for appending
            CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile2, true), ',');
             
            // if the file didn't already exist then we need to write out the header line
            if (!alreadyExists2)
            {
                csvOutput.write("url");
                csvOutput.write("size");
                csvOutput.write("number of outlinks");
                csvOutput.write("content-type");                
                csvOutput.endRecord();
            }
            // else assume that the file already has the correct header line
             
            // write out a few records
            for(String[] line : pages) {
            	for(int i = 0; i < line.length; i++)
            		csvOutput.write(line[i]);
                csvOutput.endRecord();
            }             
            csvOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
    	String outputFile3 = "urls_NewsSite.csv";
        
        // before we open the file check to see if it already exists
        boolean alreadyExists3 = new File(outputFile3).exists();
             
        try {
            // use FileWriter constructor that specifies open for appending
            CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile3, true), ',');
             
            // if the file didn't already exist then we need to write out the header line
            if (!alreadyExists3)
            {
                csvOutput.write("url");
                csvOutput.write("inside");             
                csvOutput.endRecord();
            }
            // else assume that the file already has the correct header line
             
            // write out a few records
            for(String[] line : oks) {
            	for(int i = 0; i < line.length; i++)
            		csvOutput.write(line[i]);
                csvOutput.endRecord();
            }             
            csvOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }   
        
    	

    	
    	System.out.println("numberOfAttampts:" + this.numberOfAttampts);
    	System.out.println("numberOfSucceed" + this.numberOfSucceed);
    	System.out.println("numberOfNotSucceed" + this.numberOfNotSucceed);
    	System.out.println("numberOfTotalExtract" + this.numberOfTotalExtract);
    	System.out.println("numberOfUniqueExtract" + (this.extractok.size() + this.extractnotok.size()));
    	System.out.println("numberOfUniqueIn" + this.extractok.size());
    	System.out.println("numberOfUniqueOut" + this.extractnotok.size());    	
    }

	 /**
	  * This function is called when a page is fetched and ready
	  * to be processed by your program.
	  */
	 @Override
	 public void visit(Page page) {
		 String[] line = new String[4];
	     String url = page.getWebURL().getURL();
	     System.out.println("URL: " + url);
	     line[0] = url;
	     line[1] = page.getContentData().length + "";
	     // before we open the file check to see if it already exists
	 
	     if (page.getParseData() instanceof HtmlParseData) {
	         HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	         String text = htmlParseData.getText();
	         String html = htmlParseData.getHtml();
	         Set<WebURL> links = htmlParseData.getOutgoingUrls();
	         line[2] = links.size() + "";
	         line[3] = page.getContentType();
	         this.numberOfTotalExtract += links.size();
	         for(WebURL item : links) {
	        	 String cur = item.getURL();
	        	 if(cur.startsWith("https://www.nbcnews.com/"))
	        		 this.extractok.add(cur);
	        	 else
	        		 this.extractnotok.add(cur);
	         }
	         System.out.println("Text length: " + text.length());
	         System.out.println("Html length: " + html.length());
	         System.out.println("Number of outgoing links: " + links.size());
	     
	     }
	     
	     pages.add(line);
	     
	}
}