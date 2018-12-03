package sth;

interface Visitor {

	public String visit(Professor prof);
	public String visit(Administrative admin);
	public String visit(Student student);
}