package com.dhl.example.two;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;

public class InMemoryIndex {
	
	private Directory memoryIndex;
	private Analyzer analyzer;
	
	public InMemoryIndex(Directory memoryIndex, Analyzer analyzer) {
		super();
		this.memoryIndex = memoryIndex;
		this.analyzer = analyzer;
	}
	
	public void indexDocument(String title,String body) {
		
		IndexWriterConfig config=new IndexWriterConfig(analyzer);
		
		try {
			
			IndexWriter writer=new IndexWriter(memoryIndex, config);
			Document document=new Document();
			
			document.add(new TextField("title", title,Store.YES));
			document.add(new TextField("body", body,Store.YES));
			document.add(new SortedDocValuesField("title", new BytesRef(title)));
			
			writer.addDocument(document);
			writer.close();
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	
	public List<Document> searchIndex(Query query){
		
		try {
			int hitsPerPage=10;
			IndexReader reader=DirectoryReader.open(memoryIndex);
			IndexSearcher searcher=new IndexSearcher(reader);
			TopDocs topDocs=searcher.search(query, hitsPerPage);
			List<Document> documents=new ArrayList<>();
			
			for(ScoreDoc scoreDoc:topDocs.scoreDocs) {
				documents.add(searcher.doc(scoreDoc.doc));
			}
			
			return documents;
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Document> searchIndex(Query query,Sort sort){
		
		try {
			int hitsPerPage=10;
			IndexReader reader=DirectoryReader.open(memoryIndex);
			IndexSearcher searcher=new IndexSearcher(reader);
			TopDocs topDocs=searcher.search(query, hitsPerPage,sort);
			List<Document> documents=new ArrayList<>();
			
			for(ScoreDoc scoreDoc:topDocs.scoreDocs) {
				documents.add(searcher.doc(scoreDoc.doc));
			}
			
			return documents;
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
public List<Document> searchIndex(String inField,String queryString){
		
		try {
			Query query=new QueryParser(inField, analyzer).parse(queryString);
			
			int hitsPerPage=10;
			IndexReader reader=DirectoryReader.open(memoryIndex);
			IndexSearcher searcher=new IndexSearcher(reader);
			TopDocs topDocs=searcher.search(query, hitsPerPage);
			List<Document> documents=new ArrayList<>();
			
			for(ScoreDoc scoreDoc:topDocs.scoreDocs) {
				documents.add(searcher.doc(scoreDoc.doc));
			}
			
			return documents;
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public void deleteDocument(Term term) {
	    try {
	        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
	        IndexWriter writter = new IndexWriter(memoryIndex, indexWriterConfig);
	        writter.deleteDocuments(term);
	        writter.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	
	
	
	

}
