package br.com.utilitario.main;

import java.io.File;

import br.com.utilitario.form.Formulario;

public class Main {

	public static void main(String[] args) throws Exception {
		File file = new File("projeto_atual.xml");
		new Formulario(file);
	}

}