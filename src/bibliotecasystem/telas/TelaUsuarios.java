package bibliotecasystem.telas;

import bibliotecasystem.database.UsuarioDAO;
import bibliotecasystem.modelos.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class TelaUsuarios extends JFrame {
    private JTable tabelaUsuarios;
    private JTextField campoBusca;
    private JButton btnNovoUsuario, btnEditar, btnExcluir, btnVoltar;
    private DefaultTableModel modeloTabela;
    private UsuarioDAO usuarioDAO;
    
    public TelaUsuarios() {
        usuarioDAO = new UsuarioDAO();
        
        configurarJanela();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        configurarAcessibilidade();
        carregarDados();
    }
    
    private void configurarJanela() {
        setTitle("👥 Sistema Biblioteca - Gerenciar Usuários");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        campoBusca = new JTextField(20);
        campoBusca.setPreferredSize(new Dimension(200, 35));
        
        btnNovoUsuario = new JButton("➕ NOVO USUÁRIO");
        btnEditar = new JButton("✏️ EDITAR");
        btnExcluir = new JButton("🗑️ EXCLUIR");
        btnVoltar = new JButton("⬅️ VOLTAR");
        
        btnNovoUsuario.setBackground(new Color(46, 134, 171));
        btnNovoUsuario.setForeground(Color.WHITE);
        btnEditar.setBackground(new Color(241, 196, 15));
        btnExcluir.setBackground(new Color(231, 76, 60));
        btnExcluir.setForeground(Color.WHITE);
        btnVoltar.setBackground(new Color(149, 165, 166));
        
        String[] colunas = {"ID", "Nome", "Email", "Telefone", "Tipo", "Empréstimos Ativos", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaUsuarios = new JTable(modeloTabela);
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaUsuarios.getTableHeader().setReorderingAllowed(false);
        
        tabelaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(180);
        tabelaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabelaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabelaUsuarios.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabelaUsuarios.getColumnModel().getColumn(6).setPreferredWidth(120);
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setBackground(new Color(43, 87, 154));
        painelHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblTitulo = new JLabel("🏠 > 👥 USUÁRIOS");
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
        
        painelControles.add(new JLabel("🔍 Buscar usuário:"));
        painelControles.add(campoBusca);
        painelControles.add(Box.createHorizontalStrut(20));
        painelControles.add(btnNovoUsuario);
        painelControles.add(btnEditar);
        painelControles.add(btnExcluir);
        painelControles.add(Box.createHorizontalStrut(20));
        painelControles.add(btnVoltar);
        
        add(painelControles, BorderLayout.NORTH);
        
        JScrollPane scrollTabela = new JScrollPane(tabelaUsuarios);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        add(scrollTabela, BorderLayout.CENTER);
    }
    
    private void configurarEventos() {
        btnNovoUsuario.addActionListener(e -> novoUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnExcluir.addActionListener(e -> excluirUsuario());
        btnVoltar.addActionListener(e -> voltarDashboard());
        
        campoBusca.addActionListener(e -> buscarUsuarios());
        
        tabelaUsuarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarUsuario();
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
        
        campoBusca.getAccessibleContext().setAccessibleName("Campo de busca de usuários");
        tabelaUsuarios.getAccessibleContext().setAccessibleName("Tabela de usuários cadastrados");
    }
    
    private void carregarDados() {
        try {
            modeloTabela.setRowCount(0);
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            
            for (Usuario usuario : usuarios) {
                String status = usuario.podeRealizarEmprestimo() ? "✅ Pode emprestar" : "❌ Limite atingido";
                modeloTabela.addRow(new Object[]{
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getTelefone(),
                    usuario.getTipo(),
                    usuario.getEmprestimosAtivos() + "/3",
                    status
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar usuários: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            carregarDadosExemplo();
        }
    }
    
    private void carregarDadosExemplo() {
        modeloTabela.addRow(new Object[]{1, "João Silva", "joao@email.com", "(11) 99999-9999", "Estudante", "1/3", "✅ Pode emprestar"});
        modeloTabela.addRow(new Object[]{2, "Maria Santos", "maria@email.com", "(11) 88888-8888", "Professor", "0/3", "✅ Pode emprestar"});
        modeloTabela.addRow(new Object[]{3, "Carlos Oliveira", "carlos@email.com", "(11) 77777-7777", "Funcionário", "0/3", "✅ Pode emprestar"});
    }
    
    private void novoUsuario() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        JTextField campoNome = new JTextField();
        JTextField campoEmail = new JTextField();
        JTextField campoTelefone = new JTextField();
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Estudante", "Professor", "Funcionário"});
        
        panel.add(new JLabel("Nome:"));
        panel.add(campoNome);
        panel.add(new JLabel("Email:"));
        panel.add(campoEmail);
        panel.add(new JLabel("Telefone:"));
        panel.add(campoTelefone);
        panel.add(new JLabel("Tipo:"));
        panel.add(comboTipo);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "➕ NOVO USUÁRIO", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String telefone = campoTelefone.getText().trim();
            String tipo = (String) comboTipo.getSelectedItem();
            
            if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, preencha todos os campos.", 
                    "Campos obrigatórios", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Usuario novoUsuario = new Usuario(nome, email, telefone, tipo);
                usuarioDAO.inserir(novoUsuario);
                carregarDados();
                
                JOptionPane.showMessageDialog(this, 
                    "Usuário cadastrado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao cadastrar usuário: " + ex.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editarUsuario() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um usuário para editar.", 
                "Nenhum usuário selecionado", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            Usuario usuario = usuarioDAO.buscarPorId(id);
            
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, 
                    "Usuário não encontrado!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
            
            JTextField campoNome = new JTextField(usuario.getNome());
            JTextField campoEmail = new JTextField(usuario.getEmail());
            JTextField campoTelefone = new JTextField(usuario.getTelefone());
            JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Estudante", "Professor", "Funcionário"});
            comboTipo.setSelectedItem(usuario.getTipo());
            
            panel.add(new JLabel("Nome:"));
            panel.add(campoNome);
            panel.add(new JLabel("Email:"));
            panel.add(campoEmail);
            panel.add(new JLabel("Telefone:"));
            panel.add(campoTelefone);
            panel.add(new JLabel("Tipo:"));
            panel.add(comboTipo);
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "✏️ EDITAR USUÁRIO - ID: " + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String novoNome = campoNome.getText().trim();
                String novoEmail = campoEmail.getText().trim();
                String novoTelefone = campoTelefone.getText().trim();
                String novoTipo = (String) comboTipo.getSelectedItem();
                
                if (novoNome.isEmpty() || novoEmail.isEmpty() || novoTelefone.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Por favor, preencha todos os campos.", 
                        "Campos obrigatórios", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                usuario.setNome(novoNome);
                usuario.setEmail(novoEmail);
                usuario.setTelefone(novoTelefone);
                usuario.setTipo(novoTipo);
                
                usuarioDAO.atualizar(usuario);
                carregarDados();
                
                JOptionPane.showMessageDialog(this, 
                    "Usuário atualizado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao editar usuário: " + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirUsuario() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um usuário para excluir.", 
                "Nenhum usuário selecionado", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            String nome = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
            int emprestimos = Integer.parseInt(((String) modeloTabela.getValueAt(linhaSelecionada, 5)).split("/")[0]);
            
            if (emprestimos > 0) {
                JOptionPane.showMessageDialog(this,
                    "Não é possível excluir usuário com empréstimos ativos.\n" +
                    "Usuário: " + nome + "\n" +
                    "Empréstimos ativos: " + emprestimos,
                    "Usuário com empréstimos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o usuário?\n\n" +
                "ID: " + id + "\n" +
                "Nome: " + nome,
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (confirmacao == JOptionPane.YES_OPTION) {
                usuarioDAO.deletar(id);
                carregarDados();
                JOptionPane.showMessageDialog(this,
                    "Usuário excluído com sucesso!",
                    "Exclusão Concluída",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao excluir usuário: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarUsuarios() {
        String termo = campoBusca.getText().trim();
        
        if (termo.isEmpty()) {
            carregarDados();
            return;
        }
        
        try {
            modeloTabela.setRowCount(0);
            List<Usuario> usuarios = usuarioDAO.buscarPorNome(termo);
            
            for (Usuario usuario : usuarios) {
                String status = usuario.podeRealizarEmprestimo() ? "✅ Pode emprestar" : "❌ Limite atingido";
                modeloTabela.addRow(new Object[]{
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getTelefone(),
                    usuario.getTipo(),
                    usuario.getEmprestimosAtivos() + "/3",
                    status
                });
            }
            
            if (modeloTabela.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "Nenhum usuário encontrado para: " + termo,
                    "Busca sem resultados",
                    JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao buscar usuários: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarAjuda() {
        JOptionPane.showMessageDialog(this,
            "🎯 AJUDA - GERENCIAR USUÁRIOS\n\n" +
            "Funcionalidades:\n" +
            "• ➕ NOVO USUÁRIO - Cadastrar novo usuário\n" +
            "• ✏️ EDITAR - Modificar usuário selecionado\n" +
            "• 🗑️ EXCLUIR - Remover usuário selecionado\n" +
            "• 🔍 BUSCAR - Filtrar usuários por nome/email\n\n" +
            "⚠️ Restrições:\n" +
            "• Não é possível excluir usuários com empréstimos ativos\n" +
            "• Limite de 3 empréstimos por usuário\n\n" +
            "⌨️ Atalhos:\n" +
            "F1 - Esta ajuda\n" +
            "ESC - Voltar ao dashboard\n" +
            "Duplo clique - Editar usuário",
            "Ajuda - Usuários",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void voltarDashboard() {
        new TelaPrincipal().setVisible(true);
        this.dispose();
    }
}