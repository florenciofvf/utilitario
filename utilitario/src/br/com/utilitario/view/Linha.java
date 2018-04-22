package br.com.utilitario.view;

import java.awt.FontMetrics;
import java.awt.Graphics;

public abstract class Linha extends View {
	protected final String[] dados;
	protected final Tabela tabela;
	protected boolean par;

	public Linha(String[] dados) {
		this.dados = dados;
		this.tabela = null;
	}

	public Linha(Tabela tabela) {
		this.tabela = tabela;
		this.dados = null;
	}

	public abstract int getLargura(int i, FontMetrics fm);

	public abstract void desenhar(int[] xs, Graphics g);

	@Override
	public void calcularLargura(FontMetrics fm) {
	}

	@Override
	public void desenhar(Graphics g) {
		g.drawRect(x, y, largura, altura);
	}
}