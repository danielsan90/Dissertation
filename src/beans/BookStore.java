package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import disptacher.Dispatcher;

@ManagedBean(name = "bookStore")
@SessionScoped
public class BookStore implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Dispatcher dispatcher= new Dispatcher();
	private String isbn, titolo, autore, annoPubblicazione, genere;
	
	
	private  ArrayList<Book> books
    = new ArrayList<Book>(Arrays.asList(
    new Book("1234", "Brave new World", "Aldous Huxley","1932","fantascienza"),
    new Book("56743","A scanner darkly", "Philip K. Dick","1977" ,"fantascienza"),
    new Book("3245", "1984", "George Orwell","1948","fantascienza" )));

	public String insertNewBook(String isbn, String titolo, String autore, String anno, String genere){
		books.add(new Book(isbn,titolo,autore,anno,genere));
		System.out.println(books);
		return dispatcher.completeTask();
	}

	public ArrayList<Book> getBooks() {
		return books;
	}



	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}


	public String deleteBook(String isbn){
		for(int i=0; i< books.size(); i++){
			if(books.get(i).getIsbn().equals(isbn)){
				books.remove(i);
				return dispatcher.completeTask();
			}
		}
		return null;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public String getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public void setAnnoPubblicazione(String annoPubblicazione) {
		this.annoPubblicazione = annoPubblicazione;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
 	

}
