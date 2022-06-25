package aiss.api.resources;

import java.util.Comparator;

import aiss.model.Season;

public class ComparatorNameSeason implements Comparator<Season> {

	@Override
	public int compare(Season o1, Season o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
