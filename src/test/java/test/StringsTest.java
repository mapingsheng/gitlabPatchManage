package test;

public class StringsTest {

	public static void main(String[] args) {
		String str = "D:/patch2b519c64598abbc4a5f2d2b111bfb759a6caba80/target/sysware-galaxy/theme/themetemplate/blue/images/01.png";
		String projectName = "sysware-galaxy";
		int index = str.indexOf(projectName)+projectName.length();
		String endStr = str.substring(index, str.length());
		System.out.println(endStr);
	}

}
