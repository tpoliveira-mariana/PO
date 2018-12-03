package sth;

import java.util.Comparator;
import java.text.Collator;
import java.util.Locale;
import java.io.Serializable;

class AccentedComparator implements Serializable, Comparator<String> {

	/** Serial number for serialization. */
  	private static final long serialVersionUID = 201811091442L;

	@Override
	public int compare(String s1, String s2) {
		Collator collator = Collator.getInstance(Locale.getDefault());
		return collator.compare(s1, s2);
	}
}