package bibliotecasystem.telas;

import bibliotecasystem.database.EmprestimoDAO;
import bibliotecasystem.database.LivroDAO;
import bibliotecasystem.database.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class TelaPrincipal extends JFrame {
    private JButton btnLivros, btnUsuarios, btnEmprestimos, btnRelatorios, btnSair;
    private JLabel lblEstatisticas;
    
    private LivroDAO livroDAO;
    private UsuarioDAO usuarioDAO;
    private EmprestimoDAO emprestimoDAO;
    
    public TelaPrincipal() {
        livroDAO = new LivroDAO();
        usuarioDAO = new UsuarioDAO();
        emprestimoDAO = new EmprestimoDAO();
        
        configurarJanela();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        configurarAcessibilidade();
        atualizarEstatisticas();
    }
    
    private void configurarJanela() {
        setTitle("🏠 Sistema Biblioteca - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        btnLivros = new JButton("📚 LIVROS");
        btnUsuarios = new JButton("👥 USUÁRIOS");
        btnEmprestimos = new JButton("🔄 EMPRÉSTIMOS");
        btnRelatorios = new JButton("📊 RELATÓRIOS");
        btnSair = new JButton("🚪 SAIR");
        
        lblEstatisticas = new JLabel();
        lblEstatisticas.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setBackground(new Color(43, 87, 154));
        painelHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblTitulo = new JLabel("🏠 SISTEMA BIBLIOTECA");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel lblUsuario = new JLabel("👤 Sistema Biblioteca");
        lblUsuario.setForeground(Color.WHITE);
        
        painelHeader.add(lblTitulo, BorderLayout.WEST);
        painelHeader.add(lblUsuario, BorderLayout.EAST);
        
        add(painelHeader, BorderLayout.NORTH);
        
        JPanel painelPrincipal = new JPanel(new GridLayout(1, 2));
        
        JPanel painelMenu = new JPanel(new GridLayout(6, 1, 0, 10));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelMenu.setBackground(new Color(248, 249, 250));
        
        painelMenu.add(btnLivros);
        painelMenu.add(btnUsuarios);
        painelMenu.add(btnEmprestimos);
        painelMenu.add(btnRelatorios);
        painelMenu.add(new JLabel());
        painelMenu.add(btnSair);
        
        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        painelConteudo.add(lblEstatisticas, BorderLayout.CENTER);
        
        JPanel painelAcoes = new JPanel(new FlowLayout());
        JButton btnNovoEmprestimo = new JButton("➕ NOVO EMPRÉSTIMO");
        JButton btnBuscarLivro = new JButton("🔍 BUSCAR LIVRO");
        
        btnNovoEmprestimo.addActionListener(e -> abrirTelaEmprestimos());
        btnBuscarLivro.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Use a tela de Livros para buscar livros no acervo.", 
                "Buscar Livros", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        painelAcoes.add(btnNovoEmprestimo);
        painelAcoes.add(btnBuscarLivro);
        
        painelConteudo.add(painelAcoes, BorderLayout.SOUTH);
        
        painelPrincipal.add(painelMenu);
        painelPrincipal.add(painelConteudo);
        
        add(painelPrincipal, BorderLayout.CENTER);
        
        configurarBotoesMenu();
    }
    
    private void configurarBotoesMenu() {
        JButton[] botoes = {btnLivros, btnUsuarios, btnEmprestimos, btnRelatorios, btnSair};
        
        for (JButton btn : botoes) {
            btn.setPreferredSize(new Dimension(200, 50));
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            btn.setFont(new Font("Arial", Font.PLAIN, 14));
        }
        
        btnSair.setBackground(new Color(231, 76, 60));
        btnSair.setForeground(Color.WHITE);
    }
    
    private void configurarEventos() {
        btnLivros.addActionListener(e -> abrirTelaLivros());
        btnUsuarios.addActionListener(e -> abrirTelaUsuarios());
        btnEmprestimos.addActionListener(e -> abrirTelaEmprestimos());
        btnRelatorios.addActionListener(e -> mostrarRelatorios());
        btnSair.addActionListener(e -> confirmarSaida());
    }
    
    private void configurarAcessibilidade() {
        JRootPane rootPane = getRootPane();
        
        KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        rootPane.registerKeyboardAction(e -> mostrarAjuda(), f1, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(e -> voltarLogin(), esc, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    private void atualizarEstatisticas() {
        try {
            int totalLivros = livroDAO.contarLivros();
            int totalUsuarios = usuarioDAO.contarUsuarios();
            int emprestimosAtivos = emprestimoDAO.contarEmprestimosAtivos();
            int emprestimosAtrasados = emprestimoDAO.contarEmprestimosAtrasados();
            
            lblEstatisticas.setText("<html><center>"
                + "<h2>📊 DASHBOARD</h2>"
                + "<p>Estatísticas do Sistema:</p>"
                + "<table border='1' cellpadding='10' style='margin: 0 auto; border-collapse: collapse;'>"
                + "<tr><td><b>" + totalLivros + "</b><br>Livros</td>"
                + "<td><b>" + totalUsuarios + "</b><br>Usuários</td>"
                + "<tr><td><b>" + emprestimosAtivos + "</b><br>Empréstimos</td>"
                + "<td><b>" + emprestimosAtrasados + "</b><br>Atrasos</td></tr>"
                + "</table>"
                + "</center></html>");
                
        } catch (SQLException e) {
            lblEstatisticas.setText("<html><center>"
                + "<h2>📊 DASHBOARD</h2>"
                + "<p style='color: red;'>Erro ao carregar estatísticas</p>"
                + "</center></html>");
        }
    }
    
    private void abrirTelaLivros() {
        new TelaLivros().setVisible(true);
        this.dispose();
    }
    
    private void abrirTelaUsuarios() {
        new TelaUsuarios().setVisible(true);
        this.dispose();
    }
    
    private void abrirTelaEmprestimos() {
        new TelaEmprestimos().setVisible(true);
        this.dispose();
    }
    
    private void mostrarRelatorios() {
        try {
            int totalLivros = livroDAO.contarLivros();
            int totalUsuarios = usuarioDAO.contarUsuarios();
            int emprestimosAtivos = emprestimoDAO.contarEmprestimosAtivos();
            int emprestimosAtrasados = emprestimoDAO.contarEmprestimosAtrasados();
            
            JOptionPane.showMessageDialog(this,
                "📊 RELATÓRIOS DO SISTEMA\n\n" +
                "• Total de Livros: " + totalLivros + "\n" +
                "• Total de Usuários: " + totalUsuarios + "\n" +
                "• Empréstimos Ativos: " + emprestimosAtivos + "\n" +
                "• Empréstimos Atrasados: " + emprestimosAtrasados + "\n\n" +
                "💡 Dica: Use as telas específicas para relatórios detalhados.",
                "Relatórios",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar relatórios: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarAjuda() {
        JOptionPane.showMessageDialog(this,
            "🎯 AJUDA - DASHBOARD\n\n" +
            "Navegação:\n" +
            "• 📚 LIVROS - Gerenciar acervo\n" +
            "• 👥 USUÁRIOS - Cadastrar usuários\n" +
            "• 🔄 EMPRÉSTIMOS - Realizar empréstimos\n" +
            "• 📊 RELATÓRIOS - Ver estatísticas\n\n" +
            "⌨️ Atalhos:\n" +
            "F1 - Esta ajuda\n" +
            "ESC - Voltar para login",
            "Ajuda Dashboard",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void voltarLogin() {
        int resposta = JOptionPane.showConfirmDialog(this,
            "Deseja voltar para a tela de login?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION);
            
        if (resposta == JOptionPane.YES_OPTION) {
            new TelaLogin().setVisible(true);
            this.dispose();
        }
    }
    
    private void confirmarSaida() {
        int resposta = JOptionPane.showConfirmDialog(this,
            "Deseja realmente sair do sistema?",
            "Confirmação de Saída",
            JOptionPane.YES_NO_OPTION);
            
        if (resposta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}