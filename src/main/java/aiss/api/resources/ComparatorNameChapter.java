package aiss.api.resources;

import java.util.Comparator;

import aiss.model.Chapter;

public class ComparatorNameChapter implements Comparator<Chapter> {

	@Override
	public int compare(Chapter o1, Chapter o2) {
		return o1.getTitle().compareTo(o2.getTitle());
	}

}
