package bibliotecasystem.telas;

import bibliotecasystem.database.LivroDAO;
import bibliotecasystem.modelos.Livro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class TelaLivros extends JFrame {
    private JTable tabelaLivros;
    private JTextField campoBusca;
    private JButton btnNovoLivro, btnEditar, btnExcluir, btnVoltar;
    private DefaultTableModel modeloTabela;
    private LivroDAO livroDAO;
    
    public TelaLivros() {
        livroDAO = new LivroDAO();
        
        configurarJanela();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        configurarAcessibilidade();
        carregarDados();
    }
    
    private void configurarJanela() {
        setTitle("📚 Sistema Biblioteca - Gerenciar Livros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        campoBusca = new JTextField(20);
        campoBusca.setPreferredSize(new Dimension(200, 35));
        
        btnNovoLivro = new JButton("➕ NOVO LIVRO");
        btnEditar = new JButton("✏️ EDITAR");
        btnExcluir = new JButton("🗑️ EXCLUIR");
        btnVoltar = new JButton("⬅️ VOLTAR");
        
        btnNovoLivro.setBackground(new Color(46, 134, 171));
        btnNovoLivro.setForeground(Color.WHITE);
        btnEditar.setBackground(new Color(241, 196, 15));
        btnExcluir.setBackground(new Color(231, 76, 60));
        btnExcluir.setForeground(Color.WHITE);
        btnVoltar.setBackground(new Color(149, 165, 166));
        
        String[] colunas = {"ID", "Título", "Autor", "ISBN", "Quantidade", "Disponíveis", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaLivros = new JTable(modeloTabela);
        tabelaLivros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaLivros.getTableHeader().setReorderingAllowed(false);
        
        tabelaLivros.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaLivros.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelaLivros.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaLivros.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabelaLivros.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabelaLivros.getColumnModel().getColumn(5).setPreferredWidth(80);
        tabelaLivros.getColumnModel().getColumn(6).setPreferredWidth(100);
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setBackground(new Color(43, 87, 154));
        painelHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblTitulo = new JLabel("🏠 > 📚 LIVROS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel lblUsuario = new JLabel("👤 Sistema Biblioteca");
        lblUsuario.setForeground(Color.WHITE);
        
        painelHeader.add(lblTitulo, BorderLayout.WEST);
        painelHeader.add(lblUsuario, BorderLayout.EAST);
        
        add(painelHeader, BorderLayout.NORTH);
        
        JPanel painelControles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelControles.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelControles.setBackground(Color.WHITE);
        
        painelControles.add(new JLabel("🔍 Buscar livro:"));
        painelControles.add(campoBusca);
        painelControles.add(Box.createHorizontalStrut(20));
        painelControles.add(btnNovoLivro);
        painelControles.add(btnEditar);
        painelControles.add(btnExcluir);
        painelControles.add(Box.createHorizontalStrut(20));
        painelControles.add(btnVoltar);
        
        add(painelControles, BorderLayout.NORTH);
        
        JScrollPane scrollTabela = new JScrollPane(tabelaLivros);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        add(scrollTabela, BorderLayout.CENTER);
    }
    
    private void configurarEventos() {
        btnNovoLivro.addActionListener(e -> novoLivro());
        btnEditar.addActionListener(e -> editarLivro());
        btnExcluir.addActionListener(e -> excluirLivro());
        btnVoltar.addActionListener(e -> voltarDashboard());
        
        campoBusca.addActionListener(e -> buscarLivros());
        
        tabelaLivros.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarLivro();
                }
            }
        });
    }
    
    private void configurarAcessibilidade() {
        JRootPane rootPane = getRootPane();
        
        KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        rootPane.registerKeyboardAction(e -> mostrarAjuda(), f1, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(e -> voltarDashboard(), esc, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        campoBusca.getAccessibleContext().setAccessibleName("Campo de busca de livros");
        tabelaLivros.getAccessibleContext().setAccessibleName("Tabela de livros cadastrados");
    }
    
    private void carregarDados() {
        try {
            modeloTabela.setRowCount(0);
            List<Livro> livros = livroDAO.listarTodos();
            
            for (Livro livro : livros) {
                String status = livro.isDisponivel() ? "✅ Disponível" : "❌ Indisponível";
                modeloTabela.addRow(new Object[]{
                    livro.getId(),
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getIsbn(),
                    livro.getQuantidade(),
                    livro.getDisponiveis(),
                    status
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar livros: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            carregarDadosExemplo();
        }
    }
    
    private void carregarDadosExemplo() {
        modeloTabela.addRow(new Object[]{1, "Dom Casmurro", "Machado de Assis", "978-85-7232-144-9", 3, 3, "✅ Disponível"});
        modeloTabela.addRow(new Object[]{2, "O Cortiço", "Aluísio Azevedo", "978-85-7232-145-6", 2, 2, "✅ Disponível"});
        modeloTabela.addRow(new Object[]{3, "Iracema", "José de Alencar", "978-85-7232-146-3", 4, 4, "✅ Disponível"});
    }
    
    private void novoLivro() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        JTextField campoTitulo = new JTextField();
        JTextField campoAutor = new JTextField();
        JTextField campoIsbn = new JTextField();
        JTextField campoQuantidade = new JTextField();
        
        panel.add(new JLabel("Título:"));
        panel.add(campoTitulo);
        panel.add(new JLabel("Autor:"));
        panel.add(campoAutor);
        panel.add(new JLabel("ISBN:"));
        panel.add(campoIsbn);
        panel.add(new JLabel("Quantidade:"));
        panel.add(campoQuantidade);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "➕ NOVO LIVRO", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String titulo = campoTitulo.getText().trim();
            String autor = campoAutor.getText().trim();
            String isbn = campoIsbn.getText().trim();
            String quantidadeStr = campoQuantidade.getText().trim();
            
            if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty() || quantidadeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, preencha todos os campos.", 
                    "Campos obrigatórios", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                int quantidade = Integer.parseInt(quantidadeStr);
                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Quantidade deve ser maior que zero.", 
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Livro novoLivro = new Livro(titulo, autor, isbn, quantidade);
                livroDAO.inserir(novoLivro);
                carregarDados();
                
                JOptionPane.showMessageDialog(this, 
                    "Livro cadastrado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Quantidade deve ser um número válido.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao cadastrar livro: " + ex.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editarLivro() {
        int linhaSelecionada = tabelaLivros.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um livro para editar.", 
                "Nenhum livro selecionado", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            Livro livro = livroDAO.buscarPorId(id);
            
            if (livro == null) {
                JOptionPane.showMessageDialog(this, 
                    "Livro não encontrado!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
            
            JTextField campoTitulo = new JTextField(livro.getTitulo());
            JTextField campoAutor = new JTextField(livro.getAutor());
            JTextField campoIsbn = new JTextField(livro.getIsbn());
            JTextField campoQuantidade = new JTextField(String.valueOf(livro.getQuantidade()));
            JLabel lblDisponiveis = new JLabel(String.valueOf(livro.getDisponiveis()));
            
            panel.add(new JLabel("Título:"));
            panel.add(campoTitulo);
            panel.add(new JLabel("Autor:"));
            panel.add(campoAutor);
            panel.add(new JLabel("ISBN:"));
            panel.add(campoIsbn);
            panel.add(new JLabel("Quantidade:"));
            panel.add(campoQuantidade);
            panel.add(new JLabel("Disponíveis:"));
            panel.add(lblDisponiveis);
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "✏️ EDITAR LIVRO - ID: " + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String novoTitulo = campoTitulo.getText().trim();
                String novoAutor = campoAutor.getText().trim();
                String novoIsbn = campoIsbn.getText().trim();
                String novaQuantidadeStr = campoQuantidade.getText().trim();
                
                if (novoTitulo.isEmpty() || novoAutor.isEmpty() || novoIsbn.isEmpty() || novaQuantidadeStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Por favor, preencha todos os campos.", 
                        "Campos obrigatórios", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    int novaQuantidade = Integer.parseInt(novaQuantidadeStr);
                    if (novaQuantidade <= 0) {
                        JOptionPane.showMessageDialog(this, 
                            "Quantidade deve ser maior que zero.", 
                            "Erro", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    livro.setTitulo(novoTitulo);
                    livro.setAutor(novoAutor);
                    livro.setIsbn(novoIsbn);
                    livro.setQuantidade(novaQuantidade);
                    
                    livroDAO.atualizar(livro);
                    carregarDados();
                    
                    JOptionPane.showMessageDialog(this, 
                        "Livro atualizado com sucesso!", 
                        "Sucesso", 
                        JOptionPane.INFORMATION_MESSAGE);
                        
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Quantidade deve ser um número válido.", 
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao editar livro: " + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirLivro() {
        int linhaSelecionada = tabelaLivros.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um livro para excluir.", 
                "Nenhum livro selecionado", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            String titulo = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
            int disponiveis = (int) modeloTabela.getValueAt(linhaSelecionada, 5);
            
            if (disponiveis < (int) modeloTabela.getValueAt(linhaSelecionada, 4)) {
                JOptionPane.showMessageDialog(this,
                    "Não é possível excluir livro com exemplares emprestados.\n" +
                    "Livro: " + titulo + "\n" +
                    "Exemplares emprestados: " + ((int) modeloTabela.getValueAt(linhaSelecionada, 4) - disponiveis),
                    "Livro com empréstimos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o livro?\n\n" +
                "ID: " + id + "\n" +
                "Título: " + titulo,
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (confirmacao == JOptionPane.YES_OPTION) {
                livroDAO.deletar(id);
                carregarDados();
                JOptionPane.showMessageDialog(this,
                    "Livro excluído com sucesso!",
                    "Exclusão Concluída",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao excluir livro: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarLivros() {
        String termo = campoBusca.getText().trim();
        
        if (termo.isEmpty()) {
            carregarDados();
            return;
        }
        
        try {
            modeloTabela.setRowCount(0);
            List<Livro> livros = livroDAO.buscarPorTitulo(termo);
            
            for (Livro livro : livros) {
                String status = livro.isDisponivel() ? "✅ Disponível" : "❌ Indisponível";
                modeloTabela.addRow(new Object[]{
                    livro.getId(),
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getIsbn(),
                    livro.getQuantidade(),
                    livro.getDisponiveis(),
                    status
                });
            }
            
            if (modeloTabela.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "Nenhum livro encontrado para: " + termo,
                    "Busca sem resultados",
                    JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao buscar livros: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarAjuda() {
        JOptionPane.showMessageDialog(this,
            "🎯 AJUDA - GERENCIAR LIVROS\n\n" +
            "Funcionalidades:\n" +
            "• ➕ NOVO LIVRO - Cadastrar novo livro\n" +
            "• ✏️ EDITAR - Modificar livro selecionado\n" +
            "• 🗑️ EXCLUIR - Remover livro selecionado\n" +
            "• 🔍 BUSCAR - Filtrar livros por título/autor\n\n" +
            "⚠️ Restrições:\n" +
            "• Não é possível excluir livros com exemplares emprestados\n" +
            "• Quantidade deve ser maior que zero\n\n" +
            "⌨️ Atalhos:\n" +
            "F1 - Esta ajuda\n" +
            "ESC - Voltar ao dashboard\n" +
            "Duplo clique - Editar livro",
            "Ajuda - Livros",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void voltarDashboard() {
        new TelaPrincipal().setVisible(true);
        this.dispose();
    }
}