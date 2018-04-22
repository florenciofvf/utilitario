package br.com.utilitario.form;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPanel;

import br.com.utilitario.view.Tabela;

public class Painel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Tabela tabela;

	public Painel() {
	}

	public Tabela getTabela() {
		return tabela;
	}

	public void setTabela(Tabela tabela) {
		this.tabela = tabela;
	}

	public void configurar() {
		Font font = getFont();
		FontMetrics fm = getFontMetrics(font);
		tabela.calcularLargura(fm);
		tabela.calcularAltura();
		tabela.calcularY(new AtomicInteger(100));
		tabela.calcularX(40);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (tabela == null) {
			return;
		}

		tabela.desenhar(g);
	}
}