package br.com.utilitario.form;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;

import br.com.utilitario.view.Cabecalho;
import br.com.utilitario.view.Linha;
import br.com.utilitario.view.LinhaCelula;
import br.com.utilitario.view.LinhaColuna;
import br.com.utilitario.view.LinhaTabela;
import br.com.utilitario.view.Tabela;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private Painel painel = new Painel();

	public Formulario(File file) throws Exception {
		super("Util");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 500);
		montarLayout();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void montarLayout() {
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, painel);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				Tabela tabela = criarTabela();
				painel.setTabela(tabela);
				painel.configurar();
				repaint();
			}
		});
	}

	private Tabela criarTabela() {
		Cabecalho cabecalho = new Cabecalho("Cabeçalho");

		LinhaColuna colunas = new LinhaColuna(new String[] { "COLUNA-1", "COLUNA-2" });

		Linha[] linhas = new Linha[6];
		linhas[0] = new LinhaCelula(new String[] { "MARIA DA SILVA DE OLIVEIRA", "JOSÉ DA SILVA" });
		linhas[1] = new LinhaCelula(new String[] { "MARIA DA SILVA DE OLIVEIRA", "JOSÉ DA SILVA" });
		linhas[2] = new LinhaCelula(new String[] { "MARIA DA SILVA DE OLIVEIRA", "JOSÉ DA SILVA" });
		linhas[3] = criarLinhaTabela();
		linhas[4] = new LinhaCelula(new String[] { "ANTOFIO FRANCISCO", "CARLA FREIRE" });
		linhas[5] = new LinhaCelula(new String[] { "ANTOFIO FRANCISCO DE SOUSA E SILVA", "CARLA FREIRE" });

		return new Tabela(cabecalho, colunas, linhas);
	}

	private Linha criarLinhaTabela() {
		Cabecalho cabecalho = new Cabecalho("CABEÇALHO 002");

		LinhaColuna colunas = new LinhaColuna(new String[] { "COLUNA 1", "COLUNA 2" });

		Linha[] linhas = new Linha[1];
		linhas[0] = new LinhaCelula(new String[] { "REQUERIMENTO DE TESTE", "valor-2" });

		return new LinhaTabela(new Tabela(cabecalho, colunas, linhas));
	}
}