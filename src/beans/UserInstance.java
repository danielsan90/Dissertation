package beans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import disptacher.Dispatcher;



@ManagedBean(name="userInstance")

public class UserInstance {


 private String utente;
 private String idProcess;
 
 private String deleteBook="1";
 private String insertBook="2";
 
 //private Dispatcher dispatcher= new Dispatcher();


public String getUtente() {
	return utente;
}
public void setUtente(String utente) {
	this.utente = utente;
}
public String getIdProcess() {
	return idProcess;
}

public void setIdProcess(String idProcess) {
	this.idProcess=idProcess;
}


public String getInsertBook() {
	return insertBook;
}
public void setInsertBook(String insertBook) {
	this.insertBook = insertBook;
}

public String getDeleteBook() {
	return deleteBook;
}
public void setDeleteBook(String deleteBook) {
	this.deleteBook = deleteBook;
}
/*
public String start(String nome, String idProcess){
	
	return dispatcher.start(nome, idProcess);
}
 */
}
