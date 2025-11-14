package bibliotecasystem.database;

import bibliotecasystem.modelos.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    public UsuarioDAO() {
        // Construtor vazio - conexão será feita quando necessário
    }
    
    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, telefone, tipo) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getTipo());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("tipo")
                );
                usuario.setEmprestimosAtivos(rs.getInt("emprestimos_ativos"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
    
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("tipo")
                    );
                    usuario.setEmprestimosAtivos(rs.getInt("emprestimos_ativos"));
                    return usuario;
                }
            }
        }
        return null;
    }
    
    public List<Usuario> buscarPorNome(String nome) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE nome LIKE ? ORDER BY nome";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("tipo")
                    );
                    usuario.setEmprestimosAtivos(rs.getInt("emprestimos_ativos"));
                    usuarios.add(usuario);
                }
            }
        }
        return usuarios;
    }
    
    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, telefone = ?, tipo = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getTipo());
            stmt.setInt(5, usuario.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public void incrementarEmprestimos(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET emprestimos_ativos = emprestimos_ativos + 1 WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }
    }
    
    public void decrementarEmprestimos(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET emprestimos_ativos = emprestimos_ativos - 1 WHERE id = ? AND emprestimos_ativos > 0";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }
    }
    
    public int contarUsuarios() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM usuarios";
        
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