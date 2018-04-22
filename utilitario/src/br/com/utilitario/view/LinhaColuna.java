package br.com.utilitario.view;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.utilitario.util.Constantes;
import br.com.utilitario.util.Util;

public class LinhaColuna extends Linha {

	public LinhaColuna(String[] dados) {
		super(dados);
	}

	@Override
	public void desenhar(int[] xs, Graphics g) {
		g.drawRect(x, y, largura, altura);

		for (int i = 0; i < dados.length; i++) {
			g.drawString(dados[i], xs[i], yFonte);
			g.drawLine(xs[i], y, xs[i], y + altura);
		}
	}

	@Override
	public void calcularAltura() {
		altura = Constantes.ALTURA_LINHA_COLUNA;
	}

	@Override
	public void calcularY(AtomicInteger acumulador) {
		y = acumulador.getAndAdd(altura);
		yFonte = y + altura;
	}

	@Override
	public void calcularX(int tab) {
		x = tab;
	}

	@Override
	public int getLargura(int i, FontMetrics fm) {
		return Util.getLargura(dados[i], fm);
	}
}