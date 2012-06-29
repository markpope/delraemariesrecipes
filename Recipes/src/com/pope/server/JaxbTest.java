package com.pope.server;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.pope.server.jaxb.Recipes;

import junit.framework.TestCase;

public class JaxbTest extends TestCase {

	
	public void test() throws Exception {
		JAXBContext context = JAXBContext.newInstance(Recipes.class);
		Marshaller marshaller = context.createMarshaller();
		com.pope.server.jaxb.Recipe recipe = new com.pope.server.jaxb.Recipe();
		recipe.setName("hoho");
		recipe.setCategory("HHO");
		Recipes recipes = new Recipes();
		recipes.getRecipes().add(recipe);
		marshaller.marshal(recipes, System.out);
	}

}
