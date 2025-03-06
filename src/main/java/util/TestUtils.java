package util;

import org.telosys.tools.api.TelosysModelException;
import org.telosys.tools.api.TelosysProject;
import org.telosys.tools.dsl.DslModelError;
import org.telosys.tools.dsl.DslModelErrors;
import org.telosys.tools.generic.model.Model;

public class TestUtils {

	private TestUtils(){}
	
//	/**
//	 * @param pathInModels  file/folder path in "TelosysTools/models"
//	 * @param telosysToolsCfg
//	 * @return
//	 * @throws TelosysToolsException
//	 */
//	public static Model loadModelWithGenericModelLoader(String pathInModels, TelosysToolsCfg telosysToolsCfg) throws TelosysModelException { // TelosysToolsException {
//		
//		String modelAbsolutePath = FileUtil.buildFilePath( telosysToolsCfg.getModelsFolderAbsolutePath(), pathInModels);
//		File modelFile = new File(modelAbsolutePath);
//
//		GenericModelLoader loader = new GenericModelLoader();
//		try {
//			return loader.loadModel(modelFile);
//		} catch (TelosysModelException e) {
//			printErrors(e.getMessage(), e.getDslModelErrors());
//			throw e;
//		}
//	}
	
	/**
	 * @param telosysProject
	 * @param modelName
	 * @return
	 * @throws TelosysModelException
	 */
	public static Model loadModel(TelosysProject telosysProject, String modelName) throws TelosysModelException {
		try {
			return telosysProject.loadModel(modelName);
		} catch (TelosysModelException e) {
			printErrors(e.getMessage(), e.getDslModelErrors());
			throw e;
		}
	}
	
	
	public static void printErrors(String errorMessage, DslModelErrors errors) {
		println("MODEL ERRORS : ");
		println(" --> Main message : " + errorMessage);
		for ( DslModelError e : errors.getErrors() ) {
            	println(" . " + e.getReportMessage() );
		}
	}
	public static void println(String s) {
		System.out.println(s);
	}
}
