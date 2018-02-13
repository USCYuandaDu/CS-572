package HW2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import com.csvreader.CsvReader;

public class parse_data {

	public static void parse_fetches_attempt() {
		
		int fetches_attempt = 0;
		int fetches_succeed = 0;
		int fetches_failed = 0;

		HashMap<Integer, Integer> map = new HashMap<>();
		
        try {           
            CsvReader fetch_News = new CsvReader("fetch_NewsSite.csv");        
            fetch_News.readHeaders();
            while (fetch_News.readRecord())
            {
            	fetches_attempt++;
                String status = fetch_News.get("status");
                if(status.equals("200"))
                	fetches_succeed++;
                else
                	fetches_failed++;
                Integer key = Integer.valueOf(status);
                map.put(key, map.getOrDefault(key, 0) + 1);
            }
     
            fetch_News.close();
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("attemp: " + fetches_attempt);
        System.out.println("succeed: " + fetches_succeed);
        System.out.println("failed: " + fetches_failed);
        
        for(Integer key : map.keySet()) {
        	System.out.println(key + ": " + map.get(key));
        }
        
	}
	
	public static void parse_extract() {

		int total_extraction = 0;
		HashSet<String> unique_total = new HashSet<>();
		HashSet<String> unique_in = new HashSet<>();
		HashSet<String> unique_out = new HashSet<>();
        try {           
            CsvReader fetch_News = new CsvReader("urls_NewsSite.csv");        
            fetch_News.readHeaders();
            while (fetch_News.readRecord())
            {
                String url = fetch_News.get("url");
                String inside = fetch_News.get("inside");
                
                total_extraction++;
                unique_total.add(url);
                if(inside.equals("OK"))
                	unique_in.add(url);
                else
                	unique_out.add(url);
            }
     
            fetch_News.close();
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("total extraction: " + total_extraction);
        System.out.println("unique_extraction: " + unique_total.size());
        System.out.println("unique_in: " + unique_in.size());
        System.out.println("unique_out: " + unique_out.size());
	}
	
	public static void parse_visit() {

		int numberOf0_1KB = 0;
		int numberOf1KB_10KB = 0;
		int numberOf10KB_100KB = 0;
		int numberOf100KB_1MB = 0;
		int numberOf1MB = 0;
		HashMap<String, Integer> type = new HashMap<>();
        try {           
            CsvReader fetch_News = new CsvReader("visit_NewsSite.csv");        
            fetch_News.readHeaders();
            while (fetch_News.readRecord())
            {
                int size = Integer.valueOf(fetch_News.get("size"));
                String type_key = fetch_News.get("content-type");
                
                type.put(type_key, type.getOrDefault(type_key, 0) + 1);
                if(size < 1000)
                	numberOf0_1KB++;
                else if(size >= 1000 && size < 10000)
                	numberOf1KB_10KB++;
                else if(size >= 10000 && size < 100000)
                	numberOf10KB_100KB++;
                else if(size >= 100000 && size < 1024000)
                	numberOf100KB_1MB++;
                else
                	numberOf1MB++;
                
            }
     
            fetch_News.close();
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for(String key : type.keySet()) {
        	if(key.equals(""))
        		continue;
        	System.out.println(key + ": " + type.get(key));
        }
        System.out.println("0-1kb: " + numberOf0_1KB);
        System.out.println("1kb-10kb: " + numberOf1KB_10KB);
        System.out.println("10kb-100kb: " + numberOf10KB_100KB);
        System.out.println("100kb-1mb: " + numberOf100KB_1MB);
        System.out.println("1MB and more: " + numberOf1MB);
	}
	
	public static void main(String[] args) {
		parse_fetches_attempt();
		parse_extract();
		parse_visit();
	}
}
