package com.dhl.example.two;

import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.RAMDirectory;

public class InMemorySearchTests {
	
	public static void main(String[] args) {
		
		searchQueryForField();
		
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

}
