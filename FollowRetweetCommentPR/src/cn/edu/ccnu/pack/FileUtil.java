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
	 * ���ļ�
	 * @param fromPath  ��ȡ�ļ���·��
	 * @return
	 * @throws IOException
	 */
	public BufferedReader readFile(String fromPath) throws IOException
	{
		FileReader reader = new FileReader(fromPath);
		BufferedReader bufferedReader = new BufferedReader(reader);   //���ļ�
		return bufferedReader;
	}
	
		
	/**
	 * д�ļ�
	 * @param toPath д��·��
	 * @return
	 * @throws IOException
	 */
	public BufferedWriter writeFile(String toPath) throws IOException
	{
		FileWriter writer = new FileWriter(toPath);
		BufferedWriter bufferedWriter = new BufferedWriter(writer);   //д�ļ�
		return bufferedWriter;
	}
		
	//��ȡ����
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
	 * �����е����ļ����ݺϲ���һ���ļ���
	 * @param path  �ļ�·���ļ���
	 * @param fromFileName ���ļ���ǰ׺
	 * @param toFileName   ���浽��һ���ļ�
	 * @param count     ���ļ�����
	 * @param format    �ļ���ʽ
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
		System.out.println("�ϲ���ɣ�");
	}

	/**
	 * ��ȡһ���ļ��е�ǰN�У�����N�е���������
	 * @param filePath
	 * @param lineCounts   ǰN��
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
	 * ɾ���ļ���������linesһ�µ����ݣ�ͬʱ�����ļ�
	 * @param filePath  �ļ�·��
	 * @param lines     ����
	 * @throws IOException
	 */
	public void deletePartfromFile(String filePath,List<String> lines) throws IOException
	{
		List<String> allLines = new ArrayList<String>();
		String line;
		BufferedReader br = readFile(filePath);
		while((line = br.readLine())!=null)     //���ļ����������ݱ��浽List��
			allLines.add(line);
		br.close();
		
		for(int i = 0; i < lines.size(); i++)  //ɾ���ļ�������������һ�µ�����
		{
			if(allLines.contains(lines.get(i)))
				allLines.remove(lines.get(i));
		}
		
		BufferedWriter bw = writeFile(filePath);  //ʣ�����������д��ԭ�ļ���
		for(String l:allLines)
		{
			bw.write(l);
			bw.newLine();
		}
		bw.close();
		
	}
	
	/**
	 * ��ȥû�б�ת����΢�������б�ת�������±��浽�µ��ļ���
	 * Ϊ��һ����ȡת����΢����΢����׼��
	 * @param fromFile  ���е�΢���ļ�
	 * @param toFile    ȥ��û�б�ת����΢�����ļ�
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
