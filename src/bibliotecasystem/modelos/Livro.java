package bibliotecasystem.modelos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidade;
    private int disponiveis;
    
    public Livro(int id, String titulo, String autor, String isbn, int quantidade) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidade = quantidade;
        this.disponiveis = quantidade;
    }
    
    // Construtor sem ID para novos livros
    public Livro(String titulo, String autor, String isbn, int quantidade) {
        this(0, titulo, autor, isbn, quantidade);
    }
    
    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public int getQuantidade() { return quantidade; }
    public int getDisponiveis() { return disponiveis; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setQuantidade(int quantidade) { 
        this.quantidade = quantidade; 
        if (this.disponiveis > quantidade) {
            this.disponiveis = quantidade;
        }
    }
    public void setDisponiveis(int disponiveis) { 
        if (disponiveis <= this.quantidade && disponiveis >= 0) {
            this.disponiveis = disponiveis; 
        }
    }
    
    // Métodos de negócio
    public boolean isDisponivel() {
        return disponiveis > 0;
    }
    
    public boolean emprestar() {
        if (disponiveis > 0) {
            disponiveis--;
            return true;
        }
        return false;
    }
    
    public void devolver() {
        if (disponiveis < quantidade) {
            disponiveis++;
        }
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %s - %s | Disp: %d/%d", id, titulo, autor, disponiveis, quantidade);
    }
    
    public String toDetailedString() {
        return String.format(
            "✅ %s - %s\n" +
            "   ISBN: %s\n" +
            "   Disponível: %d/%d exemplares\n" +
            "   Status: %s",
            titulo, autor, isbn, disponiveis, quantidade,
            isDisponivel() ? "✅ Disponível para empréstimo" : "❌ Indisponível"
        );
    }
}