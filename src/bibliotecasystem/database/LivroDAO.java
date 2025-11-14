package bibliotecasystem.database;

import bibliotecasystem.modelos.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {
    
    public LivroDAO() {
        // Construtor vazio - conexão será feita quando necessário
    }
    
    public void inserir(Livro livro) throws SQLException {
        String sql = "INSERT INTO livros (titulo, autor, isbn, quantidade, disponiveis) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setInt(4, livro.getQuantidade());
            stmt.setInt(5, livro.getQuantidade()); // Inicialmente todos disponíveis
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    livro.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public List<Livro> listarTodos() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros ORDER BY titulo";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("isbn"),
                    rs.getInt("quantidade")
                );
                livro.setDisponiveis(rs.getInt("disponiveis"));
                livros.add(livro);
            }
        }
        return livros;
    }
    
    public Livro buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM livros WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getInt("quantidade")
                    );
                    livro.setDisponiveis(rs.getInt("disponiveis"));
                    return livro;
                }
            }
        }
        return null;
    }
    
    public List<Livro> buscarPorTitulo(String titulo) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE titulo LIKE ? ORDER BY titulo";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getInt("quantidade")
                    );
                    livro.setDisponiveis(rs.getInt("disponiveis"));
                    livros.add(livro);
                }
            }
        }
        return livros;
    }
    
    public void atualizar(Livro livro) throws SQLException {
        String sql = "UPDATE livros SET titulo = ?, autor = ?, isbn = ?, quantidade = ?, disponiveis = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setInt(4, livro.getQuantidade());
            stmt.setInt(5, livro.getDisponiveis());
            stmt.setInt(6, livro.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM livros WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public boolean emprestarLivro(int idLivro) throws SQLException {
        String sql = "UPDATE livros SET disponiveis = disponiveis - 1 WHERE id = ? AND disponiveis > 0";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLivro);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public void devolverLivro(int idLivro) throws SQLException {
        String sql = "UPDATE livros SET disponiveis = disponiveis + 1 WHERE id = ? AND disponiveis < quantidade";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }
    
    public int contarLivros() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM livros";
        
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