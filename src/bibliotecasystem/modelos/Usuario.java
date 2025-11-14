package bibliotecasystem.modelos;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String tipo;
    private int emprestimosAtivos;
    
    public Usuario(int id, String nome, String email, String telefone, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.tipo = tipo;
        this.emprestimosAtivos = 0;
    }
    
    // Construtor sem ID para novos usuários
    public Usuario(String nome, String email, String telefone, String tipo) {
        this(0, nome, email, telefone, tipo);
    }
    
    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getTipo() { return tipo; }
    public int getEmprestimosAtivos() { return emprestimosAtivos; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setEmprestimosAtivos(int emprestimosAtivos) { this.emprestimosAtivos = emprestimosAtivos; }
    
    // Métodos de negócio
    public boolean podeRealizarEmprestimo() {
        return emprestimosAtivos < 3; // Máximo 3 empréstimos
    }
    
    public void adicionarEmprestimo() {
        if (podeRealizarEmprestimo()) {
            emprestimosAtivos++;
        }
    }
    
    public void removerEmprestimo() {
        if (emprestimosAtivos > 0) {
            emprestimosAtivos--;
        }
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %s (%s) | Empréstimos: %d/3", id, nome, tipo, emprestimosAtivos);
    }
    
    public String toDetailedString() {
        return String.format(
            "✅ %s (ID: %d) - %s\n" +
            "   📧 %s\n" +
            "   📞 %s\n" +
            "   Empréstimos ativos: %d/3\n" +
            "   Status: %s",
            nome, id, tipo, email, telefone, emprestimosAtivos,
            podeRealizarEmprestimo() ? "✅ Pode realizar empréstimo" : "❌ Limite de empréstimos atingido"
        );
    }
}