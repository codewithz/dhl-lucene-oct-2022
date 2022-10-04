package com.dhl.example.two;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

public class LuceneFileSearch {
	
	private Directory indexDirectory;
	private Analyzer analyzer;
	
	
	public LuceneFileSearch(Directory indexDirectory, Analyzer analyzer) {
		super();
		this.indexDirectory = indexDirectory;
		this.analyzer = analyzer;
	}
	
	public void addFileToIndex(String filePath) throws IOException,URISyntaxException{
		
		Path path=Paths.get(filePath);
		File file=path.toFile();
		
		IndexWriterConfig config=new IndexWriterConfig(analyzer);
		IndexWriter writer=new IndexWriter(indexDirectory, config);
		Document document=new Document();
		
		FileReader reader=new FileReader(file);
		
		document.add(new TextField("content", reader));
		document.add(new StringField("path", file.getPath(), Store.YES));
		document.add(new StringField("name", file.getName(), Store.YES));
		
		writer.addDocument(document);
		
		writer.close();
		
		
	}
	
	public List<Document> searchIndex(String inField,String queryString){
			
			try {
				Query query=new QueryParser(inField, analyzer).parse(queryString);
				
				int hitsPerPage=10;
				IndexReader reader=DirectoryReader.open(indexDirectory);
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

	
	
	

}
