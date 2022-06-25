package aiss.api.resources;

import java.util.Comparator;

import aiss.model.Serie;

public class ComparatorNameSerie implements Comparator<Serie> {

	@Override
	public int compare(Serie o1, Serie o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
