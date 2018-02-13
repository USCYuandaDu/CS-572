package HW2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

import com.csvreader.CsvReader;

public class parse_data {

	public static void parse_fetches_attempt() {
		
		int fetches_attempt = 0;
		int fetches_succeed = 0;
		int fetches_failed = 0;
		
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
	
	public static void main(String[] args) {
		parse_fetches_attempt();
		parse_extract();
	}
}
