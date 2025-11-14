package bibliotecasystem.telas;

import bibliotecasystem.database.EmprestimoDAO;
import bibliotecasystem.database.LivroDAO;
import bibliotecasystem.database.UsuarioDAO;
import bibliotecasystem.modelos.Emprestimo;
import bibliotecasystem.modelos.Livro;
import bibliotecasystem.modelos.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaEmprestimos extends JFrame {
    private JTextField campoBuscaUsuario, campoBuscaLivro;
    private JTextArea areaUsuarioSelecionado, areaLivroSelecionado;
    private JButton btnConfirmar, btnCancelar, btnVoltar;
    private JLabel lblDataDevolucao;
    
    private UsuarioDAO usuarioDAO;
    private LivroDAO livroDAO;
    private EmprestimoDAO emprestimoDAO;
    
    private Usuario usuarioSelecionado;
    private Livro livroSelecionado;
    
    public TelaEmprestimos() {
        usuarioDAO = new UsuarioDAO();
        livroDAO = new LivroDAO();
        emprestimoDAO = new EmprestimoDAO();
        
        configurarJanela();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        configurarAcessibilidade();
    }
    
    private void configurarJanela() {
        setTitle("🔄 Sistema Biblioteca - Realizar Empréstimo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        campoBuscaUsuario = new JTextField(20);
        campoBuscaLivro = new JTextField(20);
        
        campoBuscaUsuario.setPreferredSize(new Dimension(250, 35));
        campoBuscaLivro.setPreferredSize(new Dimension(250, 35));
        
        areaUsuarioSelecionado = new JTextArea(3, 30);
        areaLivroSelecionado = new JTextArea(4, 30);
        
        areaUsuarioSelecionado.setEditable(false);
        areaLivroSelecionado.setEditable(false);
        areaUsuarioSelecionado.setBackground(new Color(240, 240, 240));
        areaLivroSelecionado.setBackground(new Color(240, 240, 240));
        areaUsuarioSelecionado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        areaLivroSelecionado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnConfirmar = new JButton("✅ CONFIRMAR EMPRÉSTIMO");
        btnCancelar = new JButton("❌ CANCELAR");
        btnVoltar = new JButton("⬅️ VOLTAR");
        
        btnConfirmar.setBackground(new Color(39, 174, 96));
        btnConfirmar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnVoltar.setBackground(new Color(149, 165, 166));
        
        LocalDate dataDevolucao = LocalDate.now().plusDays(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblDataDevolucao = new JLabel("📅 Data de Devolução: " + dataDevolucao.format(formatter) + " (15 dias)");
        lblDataDevolucao.setFont(new Font("Arial", Font.BOLD, 14));
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setBackground(new Color(43, 87, 154));
        painelHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblTitulo = new JLabel("🏠 > 🔄 EMPRÉSTIMO");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel lblUsuario = new JLabel("👤 Sistema Biblioteca");
        lblUsuario.setForeground(Color.WHITE);
        
        painelHeader.add(lblTitulo, BorderLayout.WEST);
        painelHeader.add(lblUsuario, BorderLayout.EAST);
        
        add(painelHeader, BorderLayout.NORTH);
        
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        painelPrincipal.setBackground(Color.WHITE);
        
        JLabel lblTituloEmprestimo = new JLabel("🔄 REALIZAR EMPRÉSTIMO");
        lblTituloEmprestimo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTituloEmprestimo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(lblTituloEmprestimo);
        painelPrincipal.add(Box.createVerticalStrut(30));
        
        painelPrincipal.add(criarPainelUsuario());
        painelPrincipal.add(Box.createVerticalStrut(20));
        
        painelPrincipal.add(criarPainelLivro());
        painelPrincipal.add(Box.createVerticalStrut(20));
        
        JPanel painelData = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelData.setBackground(Color.WHITE);
        painelData.add(lblDataDevolucao);
        painelPrincipal.add(painelData);
        painelPrincipal.add(Box.createVerticalStrut(30));
        
        painelPrincipal.add(criarPainelBotoes());
        
        add(painelPrincipal, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelUsuario() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("👤 Buscar Usuário"));
        
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.setBackground(Color.WHITE);
        painelBusca.add(new JLabel("🔍 Buscar por nome:"));
        painelBusca.add(campoBuscaUsuario);
        
        JButton btnBuscarUsuario = new JButton("🔎 Buscar");
        btnBuscarUsuario.addActionListener(e -> buscarUsuario());
        painelBusca.add(btnBuscarUsuario);
        
        painel.add(painelBusca);
        painel.add(Box.createVerticalStrut(10));
        
        JScrollPane scrollUsuario = new JScrollPane(areaUsuarioSelecionado);
        painel.add(scrollUsuario);
        
        return painel;
    }
    
    private JPanel criarPainelLivro() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("📚 Buscar Livro"));
        
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.setBackground(Color.WHITE);
        painelBusca.add(new JLabel("🔍 Buscar por título:"));
        painelBusca.add(campoBuscaLivro);
        
        JButton btnBuscarLivro = new JButton("🔎 Buscar");
        btnBuscarLivro.addActionListener(e -> buscarLivro());
        painelBusca.add(btnBuscarLivro);
        
        painel.add(painelBusca);
        painel.add(Box.createVerticalStrut(10));
        
        JScrollPane scrollLivro = new JScrollPane(areaLivroSelecionado);
        painel.add(scrollLivro);
        
        return painel;
    }
    
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        painel.setBackground(Color.WHITE);
        
        painel.add(btnCancelar);
        painel.add(btnConfirmar);
        painel.add(Box.createHorizontalStrut(50));
        painel.add(btnVoltar);
        
        return painel;
    }
    
    private void configurarEventos() {
        btnConfirmar.addActionListener(e -> confirmarEmprestimo());
        btnCancelar.addActionListener(e -> limparSelecoes());
        btnVoltar.addActionListener(e -> voltarDashboard());
        
        campoBuscaUsuario.addActionListener(e -> buscarUsuario());
        campoBuscaLivro.addActionListener(e -> buscarLivro());
    }
    
    private void configurarAcessibilidade() {
        JRootPane rootPane = getRootPane();
        
        KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        rootPane.registerKeyboardAction(e -> mostrarAjuda(), f1, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(e -> voltarDashboard(), esc, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        campoBuscaUsuario.getAccessibleContext().setAccessibleName("Campo de busca de usuário");
        campoBuscaLivro.getAccessibleContext().setAccessibleName("Campo de busca de livro");
    }
    
    private void buscarUsuario() {
        String termo = campoBuscaUsuario.getText().trim();
        
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Digite um nome para buscar o usuário.",
                "Busca vazia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            List<Usuario> usuarios = usuarioDAO.buscarPorNome(termo);
            
            if (usuarios.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nenhum usuário encontrado para: " + termo,
                    "Usuário não encontrado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Se houver múltiplos resultados, deixar o usuário escolher
            if (usuarios.size() > 1) {
                Usuario usuarioEscolhido = (Usuario) JOptionPane.showInputDialog(
                    this,
                    "Selecione o usuário:",
                    "Múltiplos usuários encontrados",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    usuarios.toArray(),
                    usuarios.get(0)
                );
                
                if (usuarioEscolhido != null) {
                    usuarioSelecionado = usuarioEscolhido;
                    areaUsuarioSelecionado.setText(usuarioSelecionado.toDetailedString());
                }
            } else {
                usuarioSelecionado = usuarios.get(0);
                areaUsuarioSelecionado.setText(usuarioSelecionado.toDetailedString());
                
                JOptionPane.showMessageDialog(this,
                    "Usuário encontrado: " + usuarioSelecionado.getNome(),
                    "Busca concluída",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao buscar usuário: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarLivro() {
        String termo = campoBuscaLivro.getText().trim();
        
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Digite um título para buscar o livro.",
                "Busca vazia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            List<Livro> livros = livroDAO.buscarPorTitulo(termo);
            
            if (livros.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nenhum livro encontrado para: " + termo,
                    "Livro não encontrado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Filtrar apenas livros disponíveis
            List<Livro> livrosDisponiveis = livros.stream()
                .filter(Livro::isDisponivel)
                .toList();
                
            if (livrosDisponiveis.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nenhum exemplar disponível para: " + termo,
                    "Livro indisponível",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Se houver múltiplos resultados, deixar o usuário escolher
            if (livrosDisponiveis.size() > 1) {
                Livro livroEscolhido = (Livro) JOptionPane.showInputDialog(
                    this,
                    "Selecione o livro:",
                    "Múltiplos livros encontrados",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    livrosDisponiveis.toArray(),
                    livrosDisponiveis.get(0)
                );
                
                if (livroEscolhido != null) {
                    livroSelecionado = livroEscolhido;
                    areaLivroSelecionado.setText(livroSelecionado.toDetailedString());
                }
            } else {
                livroSelecionado = livrosDisponiveis.get(0);
                areaLivroSelecionado.setText(livroSelecionado.toDetailedString());
                
                JOptionPane.showMessageDialog(this,
                    "Livro encontrado: " + livroSelecionado.getTitulo(),
                    "Busca concluída",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao buscar livro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void confirmarEmprestimo() {
        if (usuarioSelecionado == null || livroSelecionado == null) {
            JOptionPane.showMessageDialog(this,
                "Selecione um usuário e um livro antes de confirmar o empréstimo.",
                "Dados incompletos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar se o usuário pode realizar empréstimo
        if (!usuarioSelecionado.podeRealizarEmprestimo()) {
            JOptionPane.showMessageDialog(this,
                "Usuário atingiu o limite de empréstimos ativos!\n" +
                "Limite: 3 empréstimos simultâneos\n" +
                "Atuais: " + usuarioSelecionado.getEmprestimosAtivos() + " empréstimos",
                "Limite de empréstimos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar se o livro está disponível
        if (!livroSelecionado.isDisponivel()) {
            JOptionPane.showMessageDialog(this,
                "Livro não está disponível para empréstimo!",
                "Livro indisponível",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Confirmar empréstimo?\n\n" +
            "👤 Usuário: " + usuarioSelecionado.getNome() + " (ID: " + usuarioSelecionado.getId() + ")\n" +
            "📚 Livro: " + livroSelecionado.getTitulo() + " (ID: " + livroSelecionado.getId() + ")\n" +
            "📅 Data de devolução: " + lblDataDevolucao.getText().replace("📅 Data de Devolução: ", ""),
            "Confirmar Empréstimo",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                // Realizar o empréstimo no banco
                Emprestimo emprestimo = new Emprestimo(0, usuarioSelecionado.getId(), livroSelecionado.getId());
                emprestimoDAO.inserir(emprestimo);
                
                // Atualizar contadores
                livroDAO.emprestarLivro(livroSelecionado.getId());
                usuarioDAO.incrementarEmprestimos(usuarioSelecionado.getId());
                
                JOptionPane.showMessageDialog(this,
                    "✅ Empréstimo realizado com sucesso!\n\n" +
                    "👤 Usuário: " + usuarioSelecionado.getNome() + "\n" +
                    "📚 Livro: " + livroSelecionado.getTitulo() + "\n" +
                    "📅 Data de devolução: " + lblDataDevolucao.getText().replace("📅 Data de Devolução: ", "") + "\n\n" +
                    "Não esqueça de informar o prazo de devolução ao usuário!",
                    "Empréstimo Concluído",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                limparSelecoes();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao realizar empréstimo: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limparSelecoes() {
        campoBuscaUsuario.setText("");
        campoBuscaLivro.setText("");
        areaUsuarioSelecionado.setText("");
        areaLivroSelecionado.setText("");
        usuarioSelecionado = null;
        livroSelecionado = null;
        campoBuscaUsuario.requestFocus();
    }
    
    private void mostrarAjuda() {
        JOptionPane.showMessageDialog(this,
            "🎯 AJUDA - REALIZAR EMPRÉSTIMO\n\n" +
            "Passos para empréstimo:\n" +
            "1. 👤 Busque o usuário por nome\n" +
            "2. 📚 Busque o livro por título\n" +
            "3. ✅ Confirme os dados\n" +
            "4. 📅 Anote a data de devolução\n\n" +
            "⚠️ Restrições:\n" +
            "• Máximo de 3 empréstimos por usuário\n" +
            "• Apenas livros disponíveis podem ser emprestados\n" +
            "• Prazo de devolução: 15 dias\n\n" +
            "⌨️ Atalhos:\n" +
            "F1 - Esta ajuda\n" +
            "ESC - Voltar ao dashboard\n" +
            "ENTER - Realizar busca nos campos",
            "Ajuda - Empréstimos",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void voltarDashboard() {
        new TelaPrincipal().setVisible(true);
        this.dispose();
    }
}