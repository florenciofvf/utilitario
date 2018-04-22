package br.com.utilitario.view;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

public class Tabela extends View {
	private final LinhaColuna linhaColuna;
	private final Cabecalho cabecalho;
	private final Linha[] linhas;
	private final int[] xs;

	public Tabela(Cabecalho cabecalho, LinhaColuna linhaColuna, Linha[] linhas) {
		xs = new int[linhaColuna.dados.length];
		this.linhaColuna = linhaColuna;
		this.cabecalho = cabecalho;
		this.linhas = linhas;
		int i = 0;
		for (Linha linha : linhas) {
			linha.par = i++ % 2 == 0;
		}
	}

	public void desenhar(Graphics g) {
		cabecalho.desenhar(g);

		linhaColuna.desenhar(xs, g);

		for (int i = 0; i < linhas.length; i++) {
			linhas[i].desenhar(xs, g);
		}

		g.drawRect(x, y, largura, altura);
	}

	private int larguraColunas(FontMetrics fm) {
		int[] aux = new int[xs.length];
		int total = 0;

		for (int i = 0; i < xs.length; i++) {
			int maior = linhaColuna.getLargura(i, fm);
			int valor = 0;

			for (int j = 0; j < linhas.length; j++) {
				valor = linhas[j].getLargura(i, fm);

				if (valor > maior) {
					maior = valor;
				}
			}

			aux[i] = maior;
			total += aux[i];
		}

		for (int i = 1; i < xs.length; i++) {
			xs[i] = aux[i - 1];
		}

		return total;
	}

	public void calcularLargura(FontMetrics fm) {
		largura = larguraColunas(fm);

		cabecalho.calcularLargura(fm);
		if (cabecalho.largura > largura) {
			largura = cabecalho.largura;
		}

		for (int i = 0; i < linhas.length; i++) {
			linhas[i].calcularLargura(fm);

			if (linhas[i].largura > largura) {
				largura = linhas[i].largura;
			}
		}

		cabecalho.largura = largura;
		linhaColuna.largura = largura;
		for (int i = 0; i < linhas.length; i++) {
			linhas[i].largura = largura;
		}
	}

	@Override
	public void calcularAltura() {
		altura = 0;

		cabecalho.calcularAltura();
		altura += cabecalho.altura;

		linhaColuna.calcularAltura();
		altura += linhaColuna.altura;

		for (int i = 0; i < linhas.length; i++) {
			linhas[i].calcularAltura();
			altura += linhas[i].altura;
		}
	}

	@Override
	public void calcularY(AtomicInteger acumulador) {
		y = acumulador.get();

		cabecalho.calcularY(acumulador);

		linhaColuna.calcularY(acumulador);

		for (int i = 0; i < linhas.length; i++) {
			linhas[i].calcularY(acumulador);
		}
	}

	@Override
	public void calcularX(int tab) {
		x = tab;

		cabecalho.calcularX(tab);

		linhaColuna.calcularX(tab);

		for (int i = 0; i < linhas.length; i++) {
			linhas[i].calcularX(tab);
		}

		for (int i = 0; i < xs.length; i++) {
			xs[i] = xs[i] + tab;
		}
	}
}