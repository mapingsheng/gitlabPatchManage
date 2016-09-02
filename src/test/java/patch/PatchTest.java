package patch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sysware.entity.Files;
import com.sysware.mvn.compile.CompileFile;
import com.sysware.utils.FilesUtil;
import com.sysware.utils.GitUtils;
import com.sysware.utils.HashCodeUtil;
import com.sysware.utils.ReadXmlUtil;

public class PatchTest {

	
	@Test
	public void patch() throws FileNotFoundException{
		String separator = File.separator;
		String projectUrl = "http://192.168.0.200/FDM_XIAN/611.git";
		String userName = "mapsh";
		String password = "sysware123456";
		
		String fromCommitHash = "6c0418781630517b7085e23c4053e0d2660ee4b1";
		String toCommitHash="b0b4a7338a9368e4350cff48a3611f2be0816d61";
		
		//获取打补丁包的存放地址
		String fetch = GitUtils.fetch;
		
		String patchFileName = fromCommitHash.substring(0, 7)+"-"+toCommitHash.substring(0, 7); 
		String patchFilePath = fetch+separator+patchFileName;
		File patchFile = FilesUtil.initDeployFolder(patchFilePath);
		
		//克隆操作,如果已经克隆过则不进行克隆，只会克隆一次
		GitUtils.clone(projectUrl, userName, password);
		
		//拉取操作
		GitUtils.pull(projectUrl, userName, password);
		
		//分别把开始和结束节点对应的源代码进行压缩zip包处理
		String startPatchPath = GitUtils.fetchFilesByRepositoryId(fromCommitHash);
		String endPatchPath = GitUtils.fetchFilesByRepositoryId(toCommitHash);
		
		//解压缩源代码包
		String startUnzipFloder = fetch+separator+fromCommitHash;
		String endUnzipFloder = fetch+separator+toCommitHash;
		FilesUtil.unZip(startPatchPath, startUnzipFloder);
		FilesUtil.unZip(endPatchPath, endUnzipFloder);
		
		//根据源代码包中的pom.xml文件进行编译打包处理
		String startPomFilePath = startUnzipFloder+separator+"pom.xml";
		String endPomFilePath = endUnzipFloder+separator+"pom.xml";
		String startCompileFilePath = startUnzipFloder+separator+"target";
		File file = new File(startCompileFilePath);
		if(!file.exists()){
			CompileFile.compileFileByPom(startPomFilePath);
		}
		CompileFile.compileFileByPom(endPomFilePath);
		
		//根据pom.xml获取编译后的源代码的target/下面的项目名称对应的目录路径
		InputStream inputStream = new FileInputStream(new File(startPomFilePath));
		String[] keyNameArry = {"groupId","artifactId"};
		String[] valueNameArry = ReadXmlUtil.getXmlFilePathNodeTextByKey(inputStream, keyNameArry);
		String projectName = ReadXmlUtil.getValueFormat(valueNameArry, "-");
		
		//分别获取start和end对应的项目目录下面的自定义实体的目录
		String compileProjectFolderStart = startUnzipFloder+separator+"target"+separator+projectName;
		String compileProjectFolderEnd = endUnzipFloder+separator+"target"+separator+projectName;
		List<Files> startFileList = new ArrayList<Files>(); 
		FilesUtil.getFileListFromFolder(compileProjectFolderStart,projectName,startFileList);
		List<Files> endFileList = new ArrayList<Files>();
		FilesUtil.getFileListFromFolder(compileProjectFolderEnd,projectName,endFileList);
		
		Map<String, String> startFileCodeMap = HashCodeUtil.getHashCode(startFileList);
		Map<String, String> endFileCodeMap = HashCodeUtil.getHashCode(endFileList);
		
		List<Files> endFileListTemp = new ArrayList<Files>();
		endFileListTemp.addAll(endFileList);
		
		List<Files> updateFileList = new ArrayList<Files>();
		endFileList.retainAll(startFileList);//读取两个目录文件下面的文件交集
		updateFileList.addAll(endFileList);//存放需要比对code的文件实体数据集合
		
		List<Files> addFileList = new ArrayList<Files>();
		endFileListTemp.removeAll(startFileList);//存放新增的文件实体数据集合
		addFileList.addAll(endFileListTemp);
		for(Files files:addFileList){
			String endPath = files.getEndPath();
			String srcFileRealPath = compileProjectFolderEnd+endPath;//新增的源文件的绝对路径
			String targetFileRealPath = patchFilePath+endPath;//需要写入的补丁目录路径
			FilesUtil.writeFile(srcFileRealPath, targetFileRealPath);
		}
		
		for(Files files:updateFileList){
			String endPath = files.getEndPath();
			String endFileCode = endFileCodeMap.get(endPath);
			String startFileCode = startFileCodeMap.get(endPath);
			if(!endFileCode.equals(startFileCode)){//如果两个文件的code一样，则忽略，不一样则说明修改过
				String srcFileRealPath = compileProjectFolderEnd+endPath;//改动的源文件的绝对路径
				String targetFileRealPath = patchFilePath+endPath;//需要写入的补丁目录路径
				FilesUtil.writeFile(srcFileRealPath, targetFileRealPath);
			}
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
