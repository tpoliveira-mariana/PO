package sth;

interface Visitor {

	public String show(Professor prof);
	public String show(Administrative admin);
	public String show(Student student);
}