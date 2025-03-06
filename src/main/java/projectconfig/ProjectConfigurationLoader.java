package projectconfig;


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.generator.task.ErrorReport;
import org.telosys.tools.generator.task.GenerationTaskResult;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Model;

import util.TestsEnv;

public class ProjectConfigurationLoader {
	
	
	private static void print(String s) {
		System.out.println(s);
	}
	
	
	
	private TelosysProject initProject(String projectFolderFullPath, String dbModelFile, String dslModelFiles, String templatesFiles) throws Exception {

		TelosysProject telosysProject = new TelosysProject(projectFolderFullPath);
		
		print("Init project...");
		String s = telosysProject.initProject();
		print(s);
		
		TestsEnv.copyDbModelFile(projectFolderFullPath,  dbModelFile);
		TestsEnv.copyDslModelFiles(projectFolderFullPath, dslModelFiles);
		TestsEnv.copyTemplatesFiles(projectFolderFullPath, templatesFiles);
		
		return telosysProject ;
	}
	
	public Model loadModel(TelosysProject telosysProject, String filePath) throws Exception {
		print("Loading model...");
		Model model = telosysProject.loadModel(filePath);
		print("Model loaded.");
		print("Name = " + model.getName() + " ( " + model.getEntities().size() + " ) entities");
		return model;
	}
	
	
	public void launchGeneration(TelosysProject telosysProject, Model model, String bundleName) throws Exception 
	{
		
		System.out.println(" --- Launch code generation for bundle '" + bundleName  + "' ..." );
		GenerationTaskResult result = telosysProject.launchGeneration(model, bundleName, true);
		
		System.out.println("Error(s) count = " + result.getNumberOfGenerationErrors() ) ;
		if ( result.getNumberOfGenerationErrors() > 0 ) {
			int n = 0 ;
			List<ErrorReport> errors = result.getErrors();
			for ( ErrorReport error : errors ) {
				n++;
				System.out.println("ERROR #" + n);
				System.out.println(" . error message : " + error.getErrorMessage() );
				Throwable ex = error.getException() ;
				System.out.println(" . exception : " + ex.getMessage() );
				Throwable cause = ex;
				while ( ( cause = cause.getCause() ) != null ) {
					System.out.println(" . cause : " + cause.getMessage() );
				}
			}
		}
		
	}
	public static void main(String args[]) throws Exception
	{
		Path projectPath= null;
		try
		{
			
			String dbModelFile = "bookstore.dbrep";
			String dslModelFiles = "employees";
			String templatesFiles = "test-bootstrap" ;
			projectPath= Files.createTempDirectory("dk2");
			
			ProjectConfigurationLoader projectConfigurationLoader = new ProjectConfigurationLoader();
			TelosysProject telosysProject = projectConfigurationLoader.initProject(projectPath.toAbsolutePath().toString(), dbModelFile, dslModelFiles, templatesFiles);
			Model model = projectConfigurationLoader.loadModel(telosysProject, dslModelFiles );			
			projectConfigurationLoader.launchGeneration(telosysProject, model, templatesFiles);
			print("Project loaded");
		}
		finally
		{
			if(Objects.nonNull(projectPath))
				FileUtils.deleteDirectory(projectPath.toFile());
			
			print(projectPath + " folder deleted.");
		}
	}

}
