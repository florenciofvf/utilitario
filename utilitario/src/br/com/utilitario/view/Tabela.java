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

	@Override
	public void desenhar(Graphics g) {
		cabecalho.desenhar(g);
		linhaColuna.desenhar(xs, g);

		for (int i = 0; i < linhas.length; i++) {
			linhas[i].desenhar(xs, g);
		}

		g.drawRect(x, y, largura, altura);
	}

	private int configColunas(FontMetrics fm) {
		int[] aux = new int[xs.length];
		int total = 0;

		for (int i = 0; i < xs.length; i++) {
			int maior = linhaColuna.getL(i, fm);
			int valor = 0;

			for (int j = 0; j < linhas.length; j++) {
				valor = linhas[j].getL(i, fm);

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

	@Override
	public void configL(FontMetrics fm) {
		largura = configColunas(fm);

		cabecalho.configL(fm);
		if (cabecalho.largura > largura) {
			largura = cabecalho.largura;
		}

		for (int i = 0; i < linhas.length; i++) {
			linhas[i].configL(fm);

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
	public void configA() {
		altura = 0;

		cabecalho.configA();
		altura += cabecalho.altura;

		linhaColuna.configA();
		altura += linhaColuna.altura;

		for (int i = 0; i < linhas.length; i++) {
			linhas[i].configA();
			altura += linhas[i].altura;
		}
	}

	@Override
	public void configY(AtomicInteger acumulador) {
		y = acumulador.get();

		cabecalho.configY(acumulador);
		linhaColuna.configY(acumulador);

		for (int i = 0; i < linhas.length; i++) {
			linhas[i].configY(acumulador);
		}
	}

	@Override
	public void configX(int tab) {
		x = tab;

		cabecalho.configX(tab);
		linhaColuna.configX(tab);

		for (int i = 0; i < linhas.length; i++) {
			linhas[i].configX(tab);
		}

		for (int i = 0; i < xs.length; i++) {
			xs[i] = xs[i] + tab;
		}
	}
}