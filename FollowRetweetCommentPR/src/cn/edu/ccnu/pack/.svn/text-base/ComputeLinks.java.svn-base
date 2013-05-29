package cn.edu.ccnu.pack;

public class ComputeLinks {
	/**
	 * 计算每个点的入链接数
	 * 即，计算矩阵中每一列的和
	 */
	public double[] inLinks(double[][] matrix)
	{
		double[] inLinks = new double[matrix[0].length];
		for(int i = 0;i < matrix[0].length; i++)
		{
			for(int j = 0; j < matrix.length; j++)
			{
				if(matrix[j][i] != 0)
					inLinks[i] += matrix[j][i];
			}
		}
		return inLinks;
	}
	/**
	 * 
	 * 计算每一个点的链出数
	 * 
	 */
	public double[] outLinks(double[][] matrix)
	{
		double[] outLinks = new double[matrix.length];
		for(int i = 0;i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[0].length; j++)
			{
				if(matrix[i][j] != 0)
					outLinks[i] += matrix[i][j];
			}
		}
//		for(int i = 0;i < outLinks.length; i++)
//		{
//			System.out.println(outLinks[i]);
//		}
		return outLinks;
	}
	

}
