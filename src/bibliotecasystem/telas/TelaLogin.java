package bibliotecasystem.telas;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.*;

public class TelaLogin extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JCheckBox checkLembrar;
    private JButton btnEntrar;
    private JLabel lblAcessibilidade;
    
    // Usuários válidos para demonstração
    private String[][] usuariosValidos = {
        {"admin", "admin123", "Administrador"},
        {"bibliotecario", "bib123", "Bibliotecário"},
        {"usuario", "user123", "Usuário Comum"},
        {"maria", "maria123", "Maria Silva"}
    };
    
    public TelaLogin() {
        configurarJanela();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        configurarAcessibilidade();
    }
    
    private void configurarJanela() {
        setTitle("📚 Sistema Biblioteca - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void inicializarComponentes() {
        campoUsuario = new JTextField(20);
        campoSenha = new JPasswordField(20);
        checkLembrar = new JCheckBox("Lembrar credenciais");
        btnEntrar = new JButton("🚪 ENTRAR");
        lblAcessibilidade = new JLabel("F1-Ajuda | F11-Contraste | F12-Texto Grande | ESC-Sair");
        
        // Configurar tamanhos
        campoUsuario.setPreferredSize(new Dimension(200, 35));
        campoSenha.setPreferredSize(new Dimension(200, 35));
        btnEntrar.setPreferredSize(new Dimension(120, 40));
        btnEntrar.setBackground(new Color(46, 134, 171));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        // Painel principal centralizado
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        painelPrincipal.setBackground(new Color(248, 249, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Card de login
        JPanel cardLogin = new JPanel(new GridBagLayout());
        cardLogin.setBackground(Color.WHITE);
        cardLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Título
        JLabel lblTitulo = new JLabel("📚 BIBLIOTECA SYS", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(43, 87, 154));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        cardLogin.add(lblTitulo, gbc);
        
        JLabel lblSubtitulo = new JLabel("👤 ACESSO SISTEMA", JLabel.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        cardLogin.add(lblSubtitulo, gbc);
        
        // Campos
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        cardLogin.add(new JLabel("Usuário:"), gbc);
        
        gbc.gridx = 1;
        cardLogin.add(campoUsuario, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        cardLogin.add(new JLabel("Senha:"), gbc);
        
        gbc.gridx = 1;
        cardLogin.add(campoSenha, gbc);
        
        gbc.gridwidth = 2;
        gbc.gridy = 4;
        gbc.gridx = 0;
        cardLogin.add(checkLembrar, gbc);
        
        gbc.gridy = 5;
        cardLogin.add(btnEntrar, gbc);
        
        // Links
        JLabel lblLinks = new JLabel("<html><center><a href='#'>Esqueci minha senha</a><br>"
            + "<a href='#'>Acessibilidade</a></center></html>");
        lblLinks.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        cardLogin.add(lblLinks, gbc);
        
        painelPrincipal.add(cardLogin);
        add(painelPrincipal, BorderLayout.CENTER);
        
        // Rodapé com atalhos
        JPanel painelRodape = new JPanel();
        painelRodape.setBackground(new Color(243, 242, 241));
        painelRodape.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelRodape.add(lblAcessibilidade);
        add(painelRodape, BorderLayout.SOUTH);
    }
    
    private void configurarEventos() {
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        campoUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        campoSenha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        // Foco inicial
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                campoUsuario.requestFocusInWindow();
            }
        });
    }
    
    private void configurarAcessibilidade() {
        // Atalhos de teclado
        JRootPane rootPane = getRootPane();
        
        // F1 - Ajuda
        KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        rootPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarAjuda();
            }
        }, f1, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // F11 - Contraste
        KeyStroke f11 = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
        rootPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alternarContraste();
            }
        }, f11, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // F12 - Texto Grande
        KeyStroke f12 = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        rootPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alternarTextoGrande();
            }
        }, f12, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // ESC - Sair
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmarSaida();
            }
        }, esc, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Configurar descrições acessíveis
        campoUsuario.getAccessibleContext().setAccessibleName("Campo de usuário");
        campoSenha.getAccessibleContext().setAccessibleName("Campo de senha");
        btnEntrar.getAccessibleContext().setAccessibleName("Botão entrar");
    }
    
    private void realizarLogin() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword());
        
        // Validar campos vazios
        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos.", 
                "Campos obrigatórios", 
                JOptionPane.WARNING_MESSAGE);
            campoUsuario.requestFocusInWindow();
            return;
        }
        
        // Verificar credenciais
        boolean loginValido = false;
        String nomeUsuario = "";
        
        for (String[] user : usuariosValidos) {
            if (user[0].equals(usuario) && user[1].equals(senha)) {
                loginValido = true;
                nomeUsuario = user[2];
                break;
            }
        }
        
        if (loginValido) {
            JOptionPane.showMessageDialog(this, 
                "Login realizado com sucesso!\nBem-vindo, " + nomeUsuario + "!", 
                "Login OK", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir tela principal
            TelaPrincipal principal = new TelaPrincipal();
            principal.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuário ou senha incorretos!\n\n" +
                "Usuários válidos para teste:\n" +
                "• admin / admin123\n" +
                "• bibliotecario / bib123\n" +
                "• usuario / user123\n" +
                "• maria / maria123", 
                "Erro de Login", 
                JOptionPane.ERROR_MESSAGE);
            
            // Limpar campo de senha e focar no usuário
            campoSenha.setText("");
            campoUsuario.requestFocusInWindow();
            campoUsuario.selectAll();
        }
    }
    
    private void mostrarAjuda() {
        JOptionPane.showMessageDialog(this,
            "🎯 AJUDA - SISTEMA BIBLIOTECA\n\n" +
            "USUÁRIOS VÁLIDOS PARA TESTE:\n" +
            "👤 admin / admin123 (Administrador)\n" +
            "👤 bibliotecario / bib123 (Bibliotecário)\n" +
            "👤 usuario / user123 (Usuário Comum)\n" +
            "👤 maria / maria123 (Maria Silva)\n\n" +
            "⌨️ ATALHOS DE TECLADO:\n" +
            "F1 - Esta ajuda\n" +
            "F11 - Alternar contraste\n" +
            "F12 - Texto grande\n" +
            "ESC - Sair\n" +
            "ENTER - Login",
            "Ajuda do Sistema",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void alternarContraste() {
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getBackground().equals(Color.WHITE)) {
                    panel.setBackground(Color.BLACK);
                    aplicarContrasteRecursivo(panel, true);
                } else {
                    panel.setBackground(Color.WHITE);
                    aplicarContrasteRecursivo(panel, false);
                }
            }
        }
        repaint();
    }
    
    private void aplicarContrasteRecursivo(Container container, boolean altoContraste) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setForeground(altoContraste ? Color.WHITE : Color.BLACK);
            } else if (comp instanceof JTextField) {
                comp.setBackground(altoContraste ? Color.BLACK : Color.WHITE);
                comp.setForeground(altoContraste ? Color.WHITE : Color.BLACK);
            } else if (comp instanceof JButton) {
                comp.setBackground(altoContraste ? Color.YELLOW : new Color(46, 134, 171));
                comp.setForeground(altoContraste ? Color.BLACK : Color.WHITE);
            } else if (comp instanceof Container) {
                aplicarContrasteRecursivo((Container) comp, altoContraste);
            }
        }
    }
    
    private void alternarTextoGrande() {
        Font fonteAtual = campoUsuario.getFont();
        int novoTamanho = fonteAtual.getSize() == 12 ? 16 : 12;
        Font novaFonte = new Font(fonteAtual.getName(), fonteAtual.getStyle(), novoTamanho);
        
        aplicarFonteRecursivo(getContentPane(), novaFonte);
        revalidate();
        repaint();
        
        JOptionPane.showMessageDialog(this,
            "Texto " + (novoTamanho == 16 ? "GRANDE ativado" : "normal ativado"),
            "Acessibilidade",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void aplicarFonteRecursivo(Container container, Font fonte) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextComponent || comp instanceof JLabel || comp instanceof JButton) {
                comp.setFont(fonte);
            } else if (comp instanceof Container) {
                aplicarFonteRecursivo((Container) comp, fonte);
            }
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