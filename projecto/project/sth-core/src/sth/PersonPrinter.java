package sth;
import java.util.List;

class PersonPrinter implements Visitor {

	School _school;

	PersonPrinter(School school) {
		_school = school;
	}

	@Override
	public String visit(Professor prof) {
		String profInfo = prof.toString();

		List<Course> courses = _school.getProfCourses(prof);

		for (Course c : courses) {
			profInfo += c.showDisciplinesOf(prof);
		}

		return profInfo;
	}

	@Override 
	public String visit(Administrative admin) {
		return admin.toString();
	}

	@Override
	public String visit(Student student) {
		String studentInfo = (_school.representativeExists(student.getId()) ? "DELEGADO|" : "ALUNO|") + 
								student.toString();

		Course course = _school.getStudentCourse(student);

		return studentInfo + course.showDisciplinesOf(student); 


	}
}
