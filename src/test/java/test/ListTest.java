package test;

import java.util.ArrayList;
import java.util.List;

public class ListTest {

	public static void main(String[] args) {
		ratin();
	}
	
	public static void ratin1(){
		List<String> list = new ArrayList<String>();
		list.add("aaa");
		list.add("bbb");
		list.add("ccc");
		list.add("fff");
		
		
		List<String> list1 = new ArrayList<String>();
		list1.add("aaa");
		list1.add("bbb");
		list1.add("ddd");
		list1.add("eee");
		
		list.retainAll(list1);
		list.removeAll(list1);
	}
	
	public static void ratin(){
		Student student1 = new Student();
		student1.setName("aaa");
		student1.setAge(12);
		student1.setSex("aaa");
		
		Student student2 = new Student();
		student2.setName("bbb");	
		student2.setAge(12);
		student2.setSex("bbb");
		
		
		Student student3 = new Student();
		student3.setName("bbb");	
		student3.setAge(12);
		student3.setSex("bbb");
		
		Student student4 = new Student();
		student4.setName("ccc");	
		student4.setAge(12);
		student4.setSex("ccc");
		
		List<Student> list1 = new ArrayList<Student>();
		list1.add(student1);
		list1.add(student2);
		
		List<Student> list2 = new ArrayList<Student>();
		list2.add(student3);
		list2.add(student4);
		
		List<Student> updateStudentList = new ArrayList<Student>();
		list2.retainAll(list1);
		updateStudentList.retainAll(list2);
		
		
		List<Student> addStudentList = new ArrayList<Student>();
		list2.removeAll(list1);
		
		
	}
}


class Student{
	private String name;
	private String sex;
	private int age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public boolean equals(Object stu){
		Student stus = (Student) stu;
		if(this.name.equals(stus.getName())){
			return true;
		}else{
			return false;
		}
	}
	
}