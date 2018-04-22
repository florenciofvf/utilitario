package br.com.utilitario.view;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class View {
	protected int largura;
	protected int altura;
	protected int yFonte;
	protected int x;
	protected int y;

	public abstract void calcularY(AtomicInteger acumulador);

	public abstract void calcularLargura(FontMetrics fm);

	public abstract void desenhar(Graphics g);

	public abstract void calcularX(int tab);

	public abstract void calcularAltura();
}