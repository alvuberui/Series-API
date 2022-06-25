package aiss.api.resources;

import java.util.Comparator;

import aiss.model.Season;

public class ComparatorNameSeasonReversed implements Comparator<Season> {

	@Override
	public int compare(Season o1, Season o2) {
		return o2.getName().compareTo(o1.getName());
	}

}
