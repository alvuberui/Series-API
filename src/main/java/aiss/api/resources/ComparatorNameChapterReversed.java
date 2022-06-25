package aiss.api.resources;

import java.util.Comparator;

import aiss.model.Chapter;

public class ComparatorNameChapterReversed implements Comparator<Chapter> {

	@Override
	public int compare(Chapter o1, Chapter o2) {
		return o2.getTitle().compareTo(o1.getTitle());
	}

}