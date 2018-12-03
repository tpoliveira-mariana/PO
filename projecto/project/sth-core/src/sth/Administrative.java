package sth;

import java.io.Serializable;

class Administrative extends Person implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 201811081102L;

	Administrative(String name, String number, int id) {
		super(name, number, id);
	}


	//========== SHOW ===========//
	
	@Override 
	public String toString() {
		return "FUNCIONÁRIO|" + super.toString();
	}

	@Override 
	public String accept(Visitor v) {
		return v.show(this);
	}
}