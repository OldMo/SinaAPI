package cn.edu.newccnu;

import java.util.List;

public class GetTextName {
	public static void main(String[] args){
		String filePath = "DATA/likaifu1917407643/";
		FileUtil fu = new FileUtil();
		String[] word = new String[3];
		String names = new String();
		List<String> files = fu.ReadAllFiles(filePath);
		for(int i = 0;i< files.size(); i++){
				//System.out.println(files.get(i));
				word = files.get(i).split("/");
				//System.out.println(word[2]);
				if(word[2].contains("txt")){
					names = fu.pickUp(word[2]);
					System.out.println(names);
				}
		}
//		for(int i = 0;i< word.length; i++){
//			System.out.println(word[2]);
//		}
	}

}
