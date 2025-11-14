package bibliotecasystem.database;

import bibliotecasystem.modelos.Emprestimo;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {
    
    public EmprestimoDAO() {
        // Construtor vazio - conexão será feita quando necessário
    }
    
    public void inserir(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimos (id_usuario, id_livro, data_emprestimo, data_devolucao, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, emprestimo.getIdUsuario());
            stmt.setInt(2, emprestimo.getIdLivro());
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setDate(4, Date.valueOf(LocalDate.now().plusDays(15)));
            stmt.setString(5, emprestimo.getStatus());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    emprestimo.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT e.*, u.nome as usuario_nome, l.titulo as livro_titulo " +
                    "FROM emprestimos e " +
                    "JOIN usuarios u ON e.id_usuario = u.id " +
                    "JOIN livros l ON e.id_livro = l.id " +
                    "ORDER BY e.data_emprestimo DESC";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("id_livro")
                );
                emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toString());
                emprestimo.setDataDevolucao(rs.getDate("data_devolucao").toString());
                emprestimo.setStatus(rs.getString("status"));
                emprestimos.add(emprestimo);
            }
        }
        return emprestimos;
    }
    
    public void finalizarEmprestimo(int idEmprestimo) throws SQLException {
        String sql = "UPDATE emprestimos SET status = 'FINALIZADO', data_devolvida = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, idEmprestimo);
            stmt.executeUpdate();
        }
    }
    
    public List<Emprestimo> buscarEmprestimosAtivos() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM emprestimos WHERE status = 'ATIVO'";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("id_livro")
                );
                emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toString());
                emprestimo.setDataDevolucao(rs.getDate("data_devolucao").toString());
                emprestimo.setStatus(rs.getString("status"));
                emprestimos.add(emprestimo);
            }
        }
        return emprestimos;
    }
    
    public int contarEmprestimosAtivos() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM emprestimos WHERE status = 'ATIVO'";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    public int contarEmprestimosAtrasados() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM emprestimos WHERE status = 'ATIVO' AND data_devolucao < CURDATE()";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
}