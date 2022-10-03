import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class HelloLucene {
	
	public static void main(String[] args) throws IOException {
		
//		1. Specify the analyzer for tokenizing the text.
//		 -- Same Analyzer will be used for indexing and searching ---
		
		StandardAnalyzer analyzer=new StandardAnalyzer();
		
//		2. Create an Index 
		
		Directory index=new RAMDirectory();
		
		IndexWriterConfig config=new IndexWriterConfig(analyzer);
		
		IndexWriter writer=new IndexWriter(index, config);
		
		addDocToIndex(writer,"Lucene In Action","193398817");
		addDocToIndex(writer,"Lucene For Dummies","55320055Z");
		addDocToIndex(writer,"Managing Gigabytes","55063554A");
		addDocToIndex(writer,"The Art of Computer Science", "9900333X");
		
		writer.close();
		
		System.out.println("Documents are indexed");
		
//		---------------------------Accepting the Search Query----------------------------------------------
		Scanner sc=new Scanner(System.in);
		
		System.out.print("Enter the Query String:");
		String queryString=sc.nextLine();
		
		queryString=queryString.isEmpty()?"lucene":queryString;
		
		System.out.println("Query String: "+queryString);
		
//		---------------------------- Query the documents -------------------------------------
		
		Query query=null;
		
		try {
			String[] fields= {"title"};
			query=new MultiFieldQueryParser(fields, analyzer).parse(queryString);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
//	--------------- Searching the documents for Query-------------------------
		
		int hitsPerPage=10;
		
		IndexReader reader=DirectoryReader.open(index);
		IndexSearcher searcher=new IndexSearcher(reader);
		
		TopScoreDocCollector collector=TopScoreDocCollector.create(hitsPerPage);
		searcher.search(query, collector);
		
		ScoreDoc[] hits=collector.topDocs().scoreDocs;
		
//		------------------------ Display the results --------------------------
		
		System.out.println("Found:" +hits.length+" hits.");
		
		for(int i=0;i<hits.length;i++) {
			
			int documentId=hits[i].doc;
			Document d=searcher.doc(documentId);
			System.out.println((i+1)+". "+d.get("isbn") + "\t"+ d.get("title"));
		}
		
		//When the work of reader is done [when you don't need to access doc any more]
		
		reader.close();
		
	}
	
	private static void addDocToIndex(IndexWriter writer, String title,String isbn) throws IOException {
		Document doc=new Document();
		
		doc.add(new TextField("title", title, Store.YES));
		
		//Use a String Field because i don't want ISBN to be tokenized
		doc.add(new StringField("isbn", isbn, Store.YES));
		
		writer.addDocument(doc);
	}

}
