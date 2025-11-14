package bibliotecasystem.modelos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Emprestimo {
    private int id;
    private int idUsuario;
    private int idLivro;
    private String dataEmprestimo;
    private String dataDevolucao;
    private String status; // "ATIVO", "FINALIZADO", "ATRASADO"
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public Emprestimo(int id, int idUsuario, int idLivro) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.dataEmprestimo = LocalDate.now().format(formatter);
        this.dataDevolucao = LocalDate.now().plusDays(15).format(formatter);
        this.status = "ATIVO";
    }
    
    // Construtor para carregar do banco
    public Emprestimo(int id, int idUsuario, int idLivro, String dataEmprestimo, String dataDevolucao, String status) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
    }
    
    // Getters
    public int getId() { return id; }
    public int getIdUsuario() { return idUsuario; }
    public int getIdLivro() { return idLivro; }
    public String getDataEmprestimo() { return dataEmprestimo; }
    public String getDataDevolucao() { return dataDevolucao; }
    public String getStatus() { return status; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public void setIdLivro(int idLivro) { this.idLivro = idLivro; }
    public void setDataEmprestimo(String dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    public void setDataDevolucao(String dataDevolucao) { this.dataDevolucao = dataDevolucao; }
    public void setStatus(String status) { this.status = status; }
    
    // Métodos de negócio
    public void finalizar() {
        this.status = "FINALIZADO";
    }
    
    public boolean isAtrasado() {
        if (!status.equals("ATIVO")) return false;
        
        LocalDate devolucao = LocalDate.parse(dataDevolucao, DateTimeFormatter.ISO_DATE);
        return LocalDate.now().isAfter(devolucao);
    }
    
    public int getDiasAtraso() {
        if (!isAtrasado()) return 0;
        
        LocalDate devolucao = LocalDate.parse(dataDevolucao, DateTimeFormatter.ISO_DATE);
        return (int) java.time.temporal.ChronoUnit.DAYS.between(devolucao, LocalDate.now());
    }
    
    @Override
    public String toString() {
        return String.format("Empréstimo #%d | User: %d | Livro: %d | Status: %s", id, idUsuario, idLivro, status);
    }
}