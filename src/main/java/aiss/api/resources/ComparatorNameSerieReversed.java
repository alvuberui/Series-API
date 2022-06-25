package aiss.api.resources;

import java.util.Comparator;

import aiss.model.Serie;

public class ComparatorNameSerieReversed implements Comparator<Serie> {

	@Override
	public int compare(Serie o1, Serie o2) {
		return o2.getName().compareTo(o1.getName());
	}
}
