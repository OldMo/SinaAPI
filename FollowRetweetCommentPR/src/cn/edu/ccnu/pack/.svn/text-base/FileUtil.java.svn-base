package cn.edu.ccnu.pack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
	static Pattern pattern = Pattern.compile("[0-9]");
	
	/**
	 * 读文件
	 * @param fromPath  读取文件的路径
	 * @return
	 * @throws IOException
	 */
	public BufferedReader readFile(String fromPath) throws IOException
	{
		FileReader reader = new FileReader(fromPath);
		BufferedReader bufferedReader = new BufferedReader(reader);   //读文件
		return bufferedReader;
	}
	
		
	/**
	 * 写文件
	 * @param toPath 写入路径
	 * @return
	 * @throws IOException
	 */
	public BufferedWriter writeFile(String toPath) throws IOException
	{
		FileWriter writer = new FileWriter(toPath);
		BufferedWriter bufferedWriter = new BufferedWriter(writer);   //写文件
		return bufferedWriter;
	}
		
	//提取数字
	public static String pickUp(String text) 
	{
		Matcher matcher = pattern.matcher(text);
		StringBuffer bf = new StringBuffer(64);
		while (matcher.find()) 
		{
		  bf.append(matcher.group()).append("");
		}
		//System.out.println(bf.toString());
		 return bf.toString();
	} 
	

	/**
	 * 将所有的子文件内容合并到一个文件中
	 * @param path  文件路径文件夹
	 * @param fromFileName 子文件名前缀
	 * @param toFileName   保存到的一个文件
	 * @param count     子文件数量
	 * @param format    文件格式
	 * @throws IOException 
	 */
	public void mergeFiles(String path,String fromFileName,String toFileName,int count,String format) throws IOException
	{
		String toFilePath = path+toFileName+format;
		BufferedWriter bw = writeFile(toFilePath);
		for(int i = 1; i <= count; i++)
		{
			String fromFilePath = path+fromFileName+i+format;
			BufferedReader br = readFile(fromFilePath);
			while(br.ready())
			{
				bw.write(br.readLine());
				bw.newLine();
			}
			br.close();
		}
		bw.close();
		System.out.println("合并完成！");
	}

	/**
	 * 获取一个文件中的前N行，返回N行的数据数组
	 * @param filePath
	 * @param lineCounts   前N行
	 * @return
	 * @throws IOException
	 */
	public List<String> getPartfromFile(String filePath,int lineCounts) throws IOException
	{
		List<String> lines = new ArrayList<String>();
		BufferedReader br = readFile(filePath);
		for(int i = 0; i < lineCounts; i++)
		{
			 lines.add(br.readLine());
		}
		br.close();
		return lines;
	}
	
	/**
	 * 删除文件中与数组lines一致的内容，同时更新文件
	 * @param filePath  文件路径
	 * @param lines     数组
	 * @throws IOException
	 */
	public void deletePartfromFile(String filePath,List<String> lines) throws IOException
	{
		List<String> allLines = new ArrayList<String>();
		String line;
		BufferedReader br = readFile(filePath);
		while((line = br.readLine())!=null)     //将文件的所有内容保存到List中
			allLines.add(line);
		br.close();
		
		for(int i = 0; i < lines.size(); i++)  //删除文件中与数组内容一致的内容
		{
			if(allLines.contains(lines.get(i)))
				allLines.remove(lines.get(i));
		}
		
		BufferedWriter bw = writeFile(filePath);  //剩余的内容重新写到原文件中
		for(String l:allLines)
		{
			bw.write(l);
			bw.newLine();
		}
		bw.close();
		
	}
	
	/**
	 * 除去没有被转发的微博，将有被转发的重新保存到新的文件中
	 * 为下一步获取转发该微博的微博做准备
	 * @param fromFile  所有的微博文件
	 * @param toFile    去除没有被转发的微博的文件
	 */
	public void delete(String fromFile,String toFile)
	{
		FileUtil fu = new FileUtil();
		BufferedReader br;
		BufferedWriter bw;
		String[] line;
		try 
		{
			br = fu.readFile(fromFile);
			bw = fu.writeFile(toFile);
			while(br.ready())
			{
				line = br.readLine().split("--");
				if(Integer.parseInt(line[2]) != 0)
				{
					System.out.println(line[0]+"--"+line[1]+"--"+line[2]);
					bw.write(line[0]+"--"+line[1]+"--"+line[2]);
					bw.newLine();
				}
			}
			br.close();
			bw.close();
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
	
}
