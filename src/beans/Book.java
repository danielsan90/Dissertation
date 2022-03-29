package beans;

public class Book{

	
	
	private String isbn, titolo, autore, annoPubblicazione, genere;
	
	
	public Book(String isbn, String titolo, String autore, String annoPubblicazione, String genere){
		this.isbn=isbn;
		this.titolo=titolo;
		this.autore=autore;
		this.annoPubblicazione=annoPubblicazione;
		this.genere=genere;
		
	}
	
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
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

	public String getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public void setAnnoPubblicazione(String annoPubblicazione) {
		this.annoPubblicazione = annoPubblicazione;
	}


	

}
