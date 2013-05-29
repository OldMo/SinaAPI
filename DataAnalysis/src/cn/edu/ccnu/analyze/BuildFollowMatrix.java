package cn.edu.ccnu.analyze;

import java.io.IOException;

import cn.edu.ccnu.dataanalysis.BuildCorpus;
import cn.edu.ccnu.dataanalysis.FileUtil;

public class BuildFollowMatrix {
	
	public static void main(String[] args) throws IOException{
		String fromPath = "DATA/粉丝分析/评估的105人/uid-fid-fnickname-ffcount-ftcount-deletedumplicateline.txt";
		String toPath = "DATA/粉丝分析/评估的105人/followmatrix.txt";
		BuildCorpus bc = new BuildCorpus();
		bc.initialGraph(fromPath, toPath, 1);
		
//		String toPath1 = "DATA/粉丝分析/评估的105人/uid-fid-fnickname-ffcount-ftcount-deletedumplicateline.txt";
//		FileUtil fu = new FileUtil();
//		fu.deleteDuplicateLine(fromPath, toPath1);
	}

}
