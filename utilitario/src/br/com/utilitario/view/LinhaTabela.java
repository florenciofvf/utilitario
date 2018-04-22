package br.com.utilitario.view;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

public class LinhaTabela extends Linha {

	public LinhaTabela(Tabela tabela) {
		super(tabela);
	}

	public void desenhar(int[] xs, Graphics g) {
		tabela.desenhar(g);
	}

	@Override
	public void calcularLargura(FontMetrics fm) {
		tabela.calcularLargura(fm);
		largura = tabela.largura;
	}

	@Override
	public void calcularAltura() {
		tabela.calcularAltura();
		altura = tabela.altura;
	}

	@Override
	public void calcularY(AtomicInteger acumulador) {
		y = acumulador.get();
		tabela.calcularY(acumulador);
	}

	@Override
	public void calcularX(int tab) {
		tabela.calcularX(tab);
		x = tab;
	}

	@Override
	public int getLargura(int i, FontMetrics fm) {
		return 0;
	}
}