package com.pope.server;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import junit.framework.TestCase;

import com.pope.server.jaxb.Recipes;

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
	
	public void test2() throws Exception {
		JAXBContext context = JAXBContext.newInstance(Recipes.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Recipes recipes = (Recipes)unmarshaller.unmarshal(new File("NewFile.xml"));
		System.out.println(recipes.getRecipes().size());
		
	}

}
