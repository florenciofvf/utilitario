package br.com.utilitario.form;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPanel;

import br.com.utilitario.util.Constantes;
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
		AtomicInteger yPrincipal = new AtomicInteger(Constantes.Y_PRINCIPAL);
		FontMetrics fm = getFontMetrics(getFont());
		int xPrincipal = Constantes.X_PRINCIPAL;

		tabela.configL(fm);
		tabela.configA();
		tabela.configY(yPrincipal);
		tabela.configX(xPrincipal);
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