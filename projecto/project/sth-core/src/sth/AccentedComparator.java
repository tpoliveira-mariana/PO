package sth;

import java.util.Comparator;
import java.text.Collator;
import java.util.Locale;
import java.io.Serializable;
import java.text.Normalizer;

class AccentedComparator implements Serializable, Comparator<String> {

	/** Serial number for serialization. */
  	private static final long serialVersionUID = 201811091442L;
  	private transient Collator collator = Collator.getInstance(Locale.getDefault());

	@Override
	public int compare(String s1, String s2) {
		//return collator.compare(s1, s2);
		String a = Normalizer.normalize(s1, Normalizer.Form.NFD);
		String b = Normalizer.normalize(s2, Normalizer.Form.NFD);
		return a.compareTo(b);
	}
}