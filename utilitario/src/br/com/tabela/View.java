package br.com.tabela;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class View {
	protected int largura;
	protected int altura;
	protected int xFonte;
	protected int yFonte;
	protected int x;
	protected int y;

	public int getLargura(String string, FontMetrics fm) {
		return fm.stringWidth(string) + Constantes.DELTA_LARGURA_FONTE;
	}

	public abstract void configY(AtomicInteger acumulador);

	public abstract void configL(FontMetrics fm);

	public abstract void desenhar(Graphics g);

	public abstract void configX(int tab);

	public abstract void configA();

	public int getLargura() {
		return largura;
	}

	public int getAltura() {
		return altura;
	}
}