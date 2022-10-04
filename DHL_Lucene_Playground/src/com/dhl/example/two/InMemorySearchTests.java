package com.dhl.example.two;

import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;

public class InMemorySearchTests {
	
	public static void main(String[] args) {
		
//		searchQueryForField();
//		searchTermQuery();
//		searchPrefixQuery();
//		searchBooleanQuery();
//		searchPhraseQuery();
//		searchFuzzyQuery();
//		searchWildCardQuery();
//		searchWithSortQuery();
		searchAfterDeletingQuery();
		
	}
	
	public static void searchQueryForField() {
		InMemoryIndex inMemoryIndex=new InMemoryIndex(new RAMDirectory(), new StandardAnalyzer());
		inMemoryIndex.indexDocument("Hello World", "Some Hello World Text");
		
		List<Document> documents=inMemoryIndex.searchIndex("body","world");
		
		for(Document document:documents) {
			
			System.out.println(document.get("title"));
			System.out.println(document.get("body"));
		}
	}
	
	public static void searchTermQuery() {
		InMemoryIndex inMemoryIndex=new InMemoryIndex(new RAMDirectory(), new StandardAnalyzer());
		
		inMemoryIndex.indexDocument("activities", "running in track");
		inMemoryIndex.indexDocument("activities", "Cars are running on road");
		
		Term term=new Term("body","running");
		Query query=new TermQuery(term);
		
		List<Document> documents=inMemoryIndex.searchIndex(query);
		
		for(Document document:documents) {
					System.out.println("------------- Document Found----------------");
					System.out.println("Title:"+document.get("title"));
					System.out.println("Body :"+document.get("body"));
					System.out.println("--------------End-----------------------");
				}
		
		
	}
	
	public static void searchPrefixQuery() {
		InMemoryIndex inMemoryIndex=new InMemoryIndex(new RAMDirectory(), new StandardAnalyzer());
		
		inMemoryIndex.indexDocument("article", "Lucene introduction");
		inMemoryIndex.indexDocument("article", "Introduction to Lucene");

		
		Term term=new Term("body","intro");
		Query query=new PrefixQuery(term);
		
		List<Document> documents=inMemoryIndex.searchIndex(query);
		
		for(Document document:documents) {
					System.out.println("------------- Document Found----------------");
					System.out.println("Title:"+document.get("title"));
					System.out.println("Body :"+document.get("body"));
					System.out.println("--------------End-----------------------\n\n");
				}
		
		
	}
	
	public static void searchBooleanQuery() {
		InMemoryIndex inMemoryIndex=new InMemoryIndex(new RAMDirectory(), new StandardAnalyzer());
		
		
		inMemoryIndex.indexDocument("Destination", "Las Vegas singapore car");
		inMemoryIndex.indexDocument("Commutes in singapore", "Bus Car Bikes");

		Term term1=new Term("body","singapore");
		Term term2=new Term("body","car");
		
		TermQuery query1=new TermQuery(term1);
		TermQuery query2=new TermQuery(term2);
		
		BooleanQuery booleanQuery=new BooleanQuery.Builder()
									.add(query1, Occur.MUST)
									.add(query2,Occur.MUST)
									.build();
		
		
		List<Document> documents=inMemoryIndex.searchIndex(booleanQuery);
		
		for(Document document:documents) {
					System.out.println("------------- Document Found----------------");
					System.out.println("Title:"+document.get("title"));
					System.out.println("Body :"+document.get("body"));
					System.out.println("--------------End-----------------------\n\n");
				}
		
		
	}
	
	public static void searchPhraseQuery() {
		InMemoryIndex inMemoryIndex=new InMemoryIndex(new RAMDirectory(), new StandardAnalyzer());
		
		 inMemoryIndex.indexDocument("quotes", "A rose by any other name would smell as good as something sweet.");
		 
		int slop=3;
		Query query=new PhraseQuery(slop,"body",new BytesRef("smell"),new BytesRef("sweet"));
		
		List<Document> documents=inMemoryIndex.searchIndex(query);
		if(documents.isEmpty()) {
			System.out.println("No matching documents found");
		}
		else {
		for(Document document:documents) {
					System.out.println("------------- Document Found----------------");
					System.out.println("Title:"+document.get("title"));
					System.out.println("Body :"+document.get("body"));
					System.out.println("--------------End-----------------------\n\n");
				}
		}
		
		
	}
	
	
	public static void searchFuzzyQuery() {
		InMemoryIndex inMemoryIndex=new InMemoryIndex(new RAMDirectory(), new StandardAnalyzer());
		
		inMemoryIndex.indexDocument("article", "Halloween Festival");
		inMemoryIndex.indexDocument("decoration", "Decorations for Halloween");
		
		int maxEdits=2;
		Term term=new Term("body","halloween");
		Query query=new FuzzyQuery(term, maxEdits);
		
		List<Document> documents=inMemoryIndex.searchIndex(query);
		if(documents.isEmpty()) {
			System.out.println("No matching documents found");
		}
		else {
		for(Document document:documents) {
					System.out.println("------------- Document Found----------------");
					System.out.println("Title:"+document.get("title"));
					System.out.println("Body :"+document.get("body"));
					System.out.println("--------------End-----------------------\n\n");
				}
		}
		
		
	}
	
	public static void searchWildCardQuery() {
		InMemoryIndex inMemoryIndex=new InMemoryIndex(new RAMDirectory(), new StandardAnalyzer());
		
		 inMemoryIndex.indexDocument("article", "Lucene introduction");
	     inMemoryIndex.indexDocument("article", "Introducing Lucene with Spring");
	     inMemoryIndex.indexDocument("article", "Head First Java");
	
		Term term=new Term("body","*uce*"); //intro* *ing *uce*
		Query query=new WildcardQuery(term);
		
		List<Document> documents=inMemoryIndex.searchIndex(query);
		if(documents.isEmpty()) {
			System.out.println("No matching documents found");
		}
		else {
		for(Document document:documents) {
					System.out.println("------------- Document Found----------------");
					System.out.println("Title:"+document.get("title"));
					System.out.println("Body :"+document.get("body"));
					System.out.println("--------------End-----------------------\n\n");
				}
		}
		
		
	}
	
	public static void searchWithSortQuery() {
		InMemoryIndex inMemoryIndex=new InMemoryIndex(new RAMDirectory(), new StandardAnalyzer());
		
		inMemoryIndex.indexDocument("Ganges", "River in India");
		inMemoryIndex.indexDocument("Mekong", "This river flows in south Asia");
		inMemoryIndex.indexDocument("Amazon", "Rain forest river");
		inMemoryIndex.indexDocument("Rhine", "Belongs to Europe");
		inMemoryIndex.indexDocument("Nile", "Longest River");
	
		Term term=new Term("body","river"); //intro* *ing *uce*
		Query query=new WildcardQuery(term);
		boolean shouldReverseSort=false;
		SortField sortField=new SortField("title",SortField.Type.STRING_VAL,shouldReverseSort);
		Sort sortByTitle=new Sort(sortField);
		
		List<Document> documents=inMemoryIndex.searchIndex(query,sortByTitle);
		if(documents.isEmpty()) {
			System.out.println("No matching documents found");
		}
		else {
		for(Document document:documents) {
					System.out.println("------------- Document Found----------------");
					System.out.println("Title:"+document.get("title"));
					System.out.println("Body :"+document.get("body"));
					System.out.println("--------------End-----------------------\n\n");
				}
		}
		
		
	}
	
	public static void searchAfterDeletingQuery() {
		InMemoryIndex inMemoryIndex=new InMemoryIndex(new RAMDirectory(), new StandardAnalyzer());
		
		inMemoryIndex.indexDocument("Ganges", "River in India");
		inMemoryIndex.indexDocument("Mekong", "This river flows in south Asia");
		
	
		Term term=new Term("title","ganges");
		inMemoryIndex.deleteDocument(term);
		
		
		Query query=new TermQuery(term);
		
		List<Document> documents=inMemoryIndex.searchIndex(query);
		if(documents.isEmpty()) {
			System.out.println("No matching documents found");
		}
		else {
		for(Document document:documents) {
					System.out.println("------------- Document Found----------------");
					System.out.println("Title:"+document.get("title"));
					System.out.println("Body :"+document.get("body"));
					System.out.println("--------------End-----------------------\n\n");
				}
		}
		
		
	}
	
	
	
	
	
	


}
