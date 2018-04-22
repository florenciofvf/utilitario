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
		g.setColor(Constantes.COR_COLUNA);
		g.fillRect(x, y, largura, altura);

		g.setColor(Constantes.COR_FONTE);

		for (int i = 0; i < dados.length; i++) {
			g.drawString(dados[i], xs[i] + Constantes.DELTA_X_FONTE, yFonte);
			g.drawLine(xs[i], y, xs[i], y + altura);
		}
	}

	@Override
	public void configA() {
		altura = Constantes.ALTURA_LINHA_COLUNA;
	}

	@Override
	public void configY(AtomicInteger acumulador) {
		y = acumulador.getAndAdd(altura);
		yFonte = y + altura + Constantes.DELTA_Y_FONTE;
	}

	@Override
	public void configX(int tab) {
		x = tab;
	}

	@Override
	public int getL(int i, FontMetrics fm) {
		return Util.getLargura(dados[i], fm);
	}
}