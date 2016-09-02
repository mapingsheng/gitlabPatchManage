package com.sysware.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.archive.ArchiveFormats;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GitUtils {
    private static UsernamePasswordCredentialsProvider credentialsProvider;
    private static File repositoryPath;
    private static File fetchPath;
    private static boolean cloneFlag=false;
    public static String fetch = ReadXmlUtil.getXmlNodeValueByKey("gitInfo", "patch", "dir");
    public static String repository;
    static {
    	repository = ReadXmlUtil.getXmlNodeValueByKey("gitInfo", "repository", "dir");
    	fetchPath = new File(fetch);
        if(!fetchPath.exists()){
        	fetchPath.mkdirs();
        }
    }

    /**
     * 克隆远程仓库
     * @param url
     * @param user
     * @param passwd
     */
    public static void clone(String url,String user,String passwd){
    	int endIndex = url.lastIndexOf("/");
    	int endJvIndex = url.lastIndexOf(".");
    	repository = repository+url.substring(endIndex, endJvIndex);
    	repositoryPath = new File(repository);
    	
        if(!repositoryPath.exists()){
        	repositoryPath.mkdirs();
            cloneFlag = true;
        }
        
        if(repositoryPath.listFiles().length==0){
        	cloneFlag = true;
        }
    	
    	if(cloneFlag){
    		try {
                credentialsProvider=new UsernamePasswordCredentialsProvider(user,passwd);
                Git result = Git.cloneRepository()
                        .setURI(url)
                        .setCredentialsProvider(credentialsProvider)
                        .setDirectory(repositoryPath)
                        .call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
    	}
    }

    /**
     * 拉取最新代码
     * @param url
     * @param user
     * @param passwd
     */
    public static void pull(String url,String user,String passwd){
        Git git= null;
        try {
            git = Git.open(repositoryPath);
            credentialsProvider=new UsernamePasswordCredentialsProvider(user,passwd);
            git.pull().setCredentialsProvider(credentialsProvider).call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定ID版本的所有代码
     * @param startId
     */
    public static String fetchFilesByRepositoryId(String startId){
    	String path = "";
        try {
            Git git= Git.open(repositoryPath);
            Repository repository = git.getRepository();
            ArchiveFormats.registerAll();
            try {
            	path = write(startId,repository, "zip");
            } catch (GitAPIException e) {
                e.printStackTrace();
            } finally {
                ArchiveFormats.unregisterAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return path;
    }
    private static String write(String repositoryId, Repository repository, String format) throws IOException, GitAPIException {
        File file = new File(fetchPath+File.separator+repositoryId+".zip");
        if(!file.exists()){
        	try (OutputStream out = new FileOutputStream(file)) {
                try (Git git = new Git(repository)) {
                    git.archive()
                            .setTree(repository.resolve(repositoryId))
                            .setFormat(format)
                            .setOutputStream(out)
                            .call();
                }
            }
        }
       return file.getPath();
    }


    public static void main(String[] args) {
//        clone("http://192.168.0.200/FDM/ui.git","hemq","sysware123456");
        pull("http://192.168.0.200/FDM/ui.git", "mapsh", "sysware123456");
//        fetchFilesByRepositoryId("ca42eb45e28ce44a7a1cf5862db01df6ccb425c0");
//        fetchFilesByRepositoryId("83850ad41ff07191092961b275d40e885c697a7b");
    }
}
