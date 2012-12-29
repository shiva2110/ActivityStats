package com.activity.main;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.index.CorruptIndexException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.activity.index.Activity;
import com.activity.index.DefaultIndexer;
import com.activity.index.Indexer;
import com.activity.index.MMapIndexer;
import com.activity.search.SearchResult;
import com.activity.tokenize.Tokenizer;
import com.activity.trends.ActivityTrendsComparator;
import com.activity.trends.Trends;
import com.activity.trends.ActivityTrendsComparator.SortOrder;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	static Trends trendTracker;
	static MMapIndexer indexer = null;
	
	public static synchronized Indexer getIndexer() {
		if(indexer==null){
			indexer = new MMapIndexer();
			indexer.setResetInterval(3600*1000); //1 hour reset interval (deletes older indices every 1 hour)
		}
		return indexer;
	}
	public synchronized static Trends getTrendTracker(){
		if(trendTracker==null){
			trendTracker = new Trends();
			try {
				trendTracker.reset(getIndexer());
				trendTracker.setResetInterval((3600*1000)+(300*1000), getIndexer()); //1 hour + 5 mins reset interval (resets trends from scratch every 1H5M)
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return trendTracker;
	}
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is "+ locale.toString() + "; ");
		
		List<Activity> popularlist = getTrendTracker().getMostPopular();
		ActivityTrendsComparator<Activity> comparator = new ActivityTrendsComparator<Activity>();
		comparator.setSortOrder(SortOrder.DESC);
		Collections.sort(popularlist, comparator);
		model.addAttribute("popularList", popularlist);
		
		List<Activity> oddlist = getTrendTracker().getMostOdd();
		comparator = new ActivityTrendsComparator<Activity>();
		comparator.setSortOrder(SortOrder.ASC);
		Collections.sort(oddlist, comparator);
		model.addAttribute("oddList", oddlist);

		
		return "home";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/record", method = RequestMethod.GET)
	public String record(Locale locale, Model model, HttpServletRequest request) {
		System.out.println("record");
		String activityText = request.getParameter("activityText");
		
		Activity activity = new Activity();
		activity.sentence = activityText;
		
		//Tokenize
		Tokenizer tokenizer = new Tokenizer();
		List<String> tokens = tokenizer.tokenize(activity.sentence);
		activity.processedTokens = tokens;
		
		System.out.println(activity.processedTokens.toString());		
		
		//Index 
		getIndexer().index(activity);
		
		//Search ; 
		SearchResult searchResult = getIndexer().search(activity);
		activity.hits = searchResult.getTotalHits();
		model.addAttribute("totalHits", searchResult.getTotalHits());
		model.addAttribute("activityText", activityText);
		
		//Record trend ;
		//singleton trendtracker is maintained to track trends.
		getTrendTracker().add(activity);
		
		List<Activity> popularlist = getTrendTracker().getMostPopular();
		ActivityTrendsComparator<Activity> comparator = new ActivityTrendsComparator<Activity>();
		comparator.setSortOrder(SortOrder.DESC);
		Collections.sort(popularlist, comparator);
		model.addAttribute("popularList", popularlist);
		
		List<Activity> oddlist = getTrendTracker().getMostOdd();
		comparator = new ActivityTrendsComparator<Activity>();
		comparator.setSortOrder(SortOrder.ASC);
		Collections.sort(oddlist, comparator);
		model.addAttribute("oddList", oddlist);
		
		return "statsResults";
	}
	
	@RequestMapping(value = "/explore", method = RequestMethod.GET)
	public String explore(Locale locale, Model model) {
		System.out.println("explore");
		return "statsResults";
	}
}
