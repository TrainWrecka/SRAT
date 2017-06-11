package mathUtilites;

/**
 * 
 * 
 * @author      黄大�?� ,胡盼，本类�?�自互�?�网
 * @Email       hhhdddwww@163.com
 * @version     1.0
 * @Time        2016年3月24日下�?�4:31:10
 * @Description 
 *
 */
public class Mat {
	
	/**chl
	 * 矩阵的�?矩阵函数
	 * @param args �?�数a是个浮点型（double）的二维数组，n是去掉的列�?�
	 * @return 返回值是一个浮点型二维数组（矩阵去掉第n列�?�的矩阵）
	 */
	public static double[][] zjz(double[][] a ,int n){
		int hang=a.length;
		int lie=a[0].length;
		double[][] result=new double[hang][lie-1];
		
		for(int i=0;i<hang;i++){
			for(int j=0;j<n;j++){
				result[i][j]=a[i][j];
			}
			for(int j=n+1;j<lie;j++){
//				System.out.println(j+"+++++");
				result[i][j-1]=a[i][j];
			}
		}
		
		return result;
	}
	
	public static int rank(double[][] b){
		int i,j,k,nn,is = 0,js = 0,l;
	    double q,d;
	    double[][] a=new double[b.length][];
	    for(int m=0;m<b.length;m++){
	    	a[m]=new double[b[m].length];
	    	for(int n=0;n<b[m].length;n++){
	    		a[m][n]=b[m][n];
	    	}
	    }
	   /* double[][] b=new double[a.length][a[0].length];
	   
	    for(int m=0;m<a.length;m++){
	    	for(int n=0;n<a[0].length;n++){
	    		b[m][n]=a[m][n].Clone;
	    	}
	    }*/
		// 秩�?于等于行列数
		nn = a.length;
	    if (a.length >= a[0].length) 
			nn =a[0].length;

	    k=0;

		// 消元求解
	    for (l=0; l<=nn-1; l++)
	    { 
			q=0.0;
	        for (i=l; i<=a.length-1; i++)
			{
				for (j=l; j<=a[0].length-1; j++)
				{ 
					 
					d=Math.abs(a[i][j]);
					if (d>q) 
					{ 
						q=d; 
						is=i; 
						js=j;
					}
				}
			}

	        if (q == 0.0) 
				return(k);

	        k=k+1;
	        if (is!=l)
	        { 
				for (j=l; j<=a[0].length-1; j++)
	            { 
				
	                d=a[l][j]; 
	                a[l][j]=a[is][j]; 
	                a[is][j]=d;
	            }
	        }
	        if (js!=l)
	        { 
				for (i=l; i<=a.length-1; i++)
	            { 
					
	                d=a[i][js]; 
	                a[i][js]=a[i][l]; 
	                a[i][l]=d;
	            }
	        }
	        
			
	        for (i=l+1; i<=a.length-1; i++)
	        { 
				d=a[i][l]/a[l][l];
	            for (j=l+1; j<=a[0].length-1; j++)
	            { 
					
	                a[i][j]=a[i][j]-d*a[l][j];
	            }
	        }
	    }
	    
		return(k);
		
	}

	/**chl
	 * 矩阵相乘的函数
	 * @param args �?�数a，b是两个浮点型（double）的二维数组
	 * @return 返回值是一个浮点型二维数组（矩阵的乘积）
	 */
	public static double[][] multi(double[][] a,double[][] b) {
		int hang=a.length;
		int lie=b[0].length;
		double sum;
		double[][] result=new double[hang][lie];
		for(int i=0;i<hang;i++){
			for(int j=0;j<lie;j++){
				sum=0;
				for(int k=0;k<b.length;k++){
				sum+=a[i][k]*b[k][j];	
				}
				result[i][j]=sum;
			}
		}
        return result;
	}
    /**zj
	 * 两个一维数组相加
	 * @param args �?�数a，b是两个浮点型（double）的一维数组
	 * @return 返回值是一个浮点型一维矩阵
	*/
    public static double[] plus(double[] a,double[] b)
    {
    	int hang=a.length;
    	double[] result=new double[hang];
    	for(int i=0;i<hang;i++){
    		
    			result[i]=a[i]+b[i];
    		
    	}
     return	result;
     }
	public static double[] multi(int[][] a,double[] b) {
		int hang=a.length;
		
		double sum;
		double[] result=new double[hang];
		for(int i=0;i<hang;i++){
				sum=0;
				for(int k=0;k<b.length;k++){
				sum+=a[i][k]*b[k];	
				}
				result[i]=sum;
			}
		
        return result;
	}
	public static int[][] multi(int[][] a,int[][] b) {
		int hang=a.length;
		int lie=b[0].length;
		int sum;
		int[][] result=new int[hang][lie];
		for(int i=0;i<hang;i++){
			for(int j=0;j<lie;j++){
				sum=0;
				for(int k=0;k<b.length;k++){
				sum+=a[i][k]*b[k][j];	
				}
				result[i][j]=sum;
			}
		}
        return result;
	}
	public static double[][] multi(int[][] a,double[][] b) {
		int hang=a.length;
		int lie=b[0].length;
		double sum;
		double[][] result=new double[hang][lie];
		for(int i=0;i<hang;i++){
			for(int j=0;j<lie;j++){
				sum=0;
				for(int k=0;k<b.length;k++){
				sum+=a[i][k]*b[k][j];	
				}
				result[i][j]=sum;
			}
		}
        return result;
	}

	/**
	 * 矩阵乘数的函数chl
	 * @param args �?�数a是个浮点型（double）的一维数组，b是浮点数；
	 * @return 返回值是一个浮点型一维数组（列�?��?a乘以数b的结果）
	 */
	public static double[] multi(double[] a,double b){
		int hang=a.length;
		double[] result=new double[hang];
		for(int i=0;i<hang;i++){
			  result[i]=a[i]*b;
		}
		return result;
	}
	/**chl
	 * 矩阵相乘的函数
	 * @param args �?�数a是个浮点型（double）的二维数组，a是一维数组
	 * @return 返回值是一个浮点型二维数组（矩阵的乘积）
	 */
	public static double[] multi(double[][] a,double[] b) {
		int hang=a.length;
		
		double sum;
		double[] result=new double[hang];
		for(int i=0;i<hang;i++){
				sum=0;
				for(int k=0;k<b.length;k++){
				sum+=a[i][k]*b[k];	
				}
				result[i]=sum;
			}
		
        return result;
	}
	/**chl
	 * 矩阵相加的函数
	 * @param args �?�数a，b是两个浮点型（double）的二维数组
	 * @return 返回值是一个浮点型二维数组（矩阵a与b的和）
	 */
	public static double[][] plus(double[][] a,double[][] b){
		int hang=a.length;
		int lie=a[0].length;
		double[][] result=new double[hang][lie];
		for(int i=0;i<hang;i++){
			for(int j=0;j<lie;j++){
			  result[i][j]=a[i][j]+b[i][j];
			}
		}
		return result;
	}
	/**chl
	 * 矩阵相�?的函数
	 * @param args �?�数a，b是两个浮点型（double）的二维数组，
	 * @return 返回值是一个浮点型二维数组（矩阵a与b的差）
	 */
	public static double[][] minus(double[][] a,double[][] b){
		int hang=a.length;
		int lie=a[0].length;
		double[][] result=new double[hang][lie];
		for(int i=0;i<hang;i++){
			for(int j=0;j<lie;j++){
			  result[i][j]=a[i][j]-b[i][j];
			}
		}
		return result;
	}
	/**chl
	 * 矩阵求�??函数
	 * @param args �?�数a是个浮点型（double）的二维数组，
	 * @return 返回值是一个浮点型二维数组（矩阵a的负矩阵）
	 */
	public static double[][] minus(double[][] a){
		int hang=a.length;
		int lie=a[0].length;
		double[][] result=new double[hang][lie];
		for(int i=0;i<hang;i++){
			for(int j=0;j<lie;j++){
			  result[i][j]=-a[i][j];
			}
		}
		return result;
	}
	/**chl
	 * 矩阵的�?矩阵函数
	 * @param args �?�数a是个浮点型（double）的二维数组，m是�?去掉的行�?�，n是去掉的列�?�
	 * @return 返回值是一个浮点型二维数组（矩阵去掉第m行和n列�?�的矩阵）
	 */
	public static double[][] zjz(double[][] a ,int m,int n){
		int hang=a.length;
		double[][] result=new double[hang-1][hang-1];
		for(int i=0;i<hang;i++){
			for(int j=0;j<hang;j++){
				if (i<m && j<n){
					result[i][j]=a[i][j];
				}
				if (i>m && j<n){
					result[i-1][j]=a[i][j];
				}
				if (i<m && j>n){
					result[i][j-1]=a[i][j];
				}
				if (i>m && j>n){
					result[i-1][j-1]=a[i][j];
				}
			}
		}
		return result;		
	} 
	/**chl
	 * 矩阵的行列�?的函数
	 * @param args �?�数a是个浮点型（double）的二维数组，
	 * @return 返回值是一个浮点数（矩阵a的行列�?）
	 */
	/*public static double det(double[][] a){
		double result=0;
		double sign=-1;
		if (a.length>1){
		for (int i=0;i<a.length;i++){
			sign*=-1;
			result=result + sign*a[0][i]*det(zjz(a,0,i));	
		}
		}
		else
		{
			result=a[0][0];
		}
		return result;
	}*/
	/**chl
	 * 矩阵的伴�?矩阵的函数
	 * @param args �?�数a是个浮点型（double）的二维数组，
	 * @return 返回值是一个浮点型二维数组（矩阵a的伴�?矩阵）
	 */
	/*public static double[][] bs(double[][] a){
		int hang = a.length;
		//double sign=-1;
		//double aa=det(a);
		double[][] result = new double[hang][hang];
		for (int i=0;i<hang;i++){
			for (int j=0;j<hang;j++){
				if ((i+j)%2==0)
			       result[j][i]=det(zjz(a,i,j));
				else
				result[j][i]=-det(zjz(a,i,j));
			}
		}
		return result;
	}*/
	/**chl
	 * 矩阵的转置矩阵的函数
	 * @param args �?�数a是个浮点型（double）的二维数组，
	 * @return 返回值是一个浮点型二维数组（矩阵a的转置矩阵）
	 */
	public static double[][] zhz(double[][] a){
		int hang=a.length;
		int lie=a[0].length;
		double[][] result=new double[lie][hang];
		for(int i=0;i<hang;i++){
			for(int j=0;j<lie;j++){
			  result[j][i]=a[i][j];
			}
		}
		return result;
	}
	/**
	 * 矩阵乘数的函数chl
	 * @param args �?�数a是个浮点型（double）的二维数组，b是浮点数；
	 * @return 返回值是一个浮点型二维数组（矩阵a乘以数b的结果）
	 */
	public static double[][] multi(double[][] a,double b){
		int hang=a.length;
		int lie=a[0].length;
		double[][] result=new double[hang][lie];
		for(int i=0;i<hang;i++){
			for(int j=0;j<lie;j++){
			  result[i][j]=a[i][j]*b;
			}
		}
		return result;
	}

        public static double[][] multi(double b,double[][] a){
		int hang=a.length;
		int lie=a[0].length;
		//System.out.println(hang+"^^^^^^^^^^^^^"+lie);
		double[][] result=new double[hang][lie];
		for(int i=0;i<hang;i++){
			for(int j=0;j<lie;j++){
			  result[i][j]=a[i][j]*b;
			}
		}
		return result;
	}
	/**chl
	 * 矩阵除数的函数
	 * @param args �?�数a，是个浮点型（double）的二维数组，b是浮点数
	 * @return 返回值是一个浮点型二维数组（矩阵a除以数b的结果）
	 */
	public static double[][] divide(double[][] a,double b){
		int hang=a.length;
		int lie=a[0].length;
		double[][] result=new double[hang][lie];
		for(int i=0;i<hang;i++){
			for(int j=0;j<lie;j++){
			  result[i][j]=a[i][j]/b;
			}
		}
		return result;
	}
	/**chl
	 * 矩阵除法的函数
	 * @param args �?�数a，b是两个浮点型（double）的二维数组，
	 * @return 返回值是一个浮点型二维数组（矩阵a除以b的结果）
	 */
	public static double[][] divide(double[][] a,double[][] b){
		double[][] result = new double[a.length][b.length];
		result=multi(a,inv(b));
		return result;
	}
	/**chl
	 * 矩阵求逆的函数
	 * @param args �?�数a是个浮点型（double）的二维数组，
	 * @return 返回值是一个浮点型二维数组（矩阵a的逆矩阵）
	 */
	/*public static double[][] inv(double[][] a){
		double[][] result = new double[a.length][a.length];
		result=divide(bs(a),det(a));
		return result;
	}*/
	public static double absMax(double a[]){
		int K=a.length;
		double result=0;
		if(a.length!=0){
			if(a.length==1){
				result =Math.abs(a[0]);	
			}
			else{
				result =Math.abs(a[0]);	
		        for(int i=1;i<K;i++){	
			        if(Math.abs(a[i])>result){		
			             result=Math.abs(a[i]);		
			        }
		        }
		    }
		}		
		return result;
	}
	/**zj
	 * 行�?��?乘以列�?��?的函数
	 * @param args �?�数a，b是两个浮点型（double）的一维数组
	 * @return 返回值是一个浮点型数值
	*/
	public static double multi_data(double[] a,double[] b){
	  int lie=a.length;
	  double result=0;
	  for(int i=0;i<lie;i++){
		  result+=a[i]*b[i];
		  
	  }
	  return result;
	}
	/**zj
	 * 列�?��?乘以行�?��?的函数
	 * @param args �?�数a，b是两个浮点型（double）的一维数组
	 * @return 返回值是一个浮点型二维矩阵
	*/
    public static double[][] multi_mat(double[] a,double[] b)
    {
    	int hang=a.length;
    	double[][] result=new double[hang][hang];
    	for(int i=0;i<hang;i++){
    		for(int j=0;j<hang;j++){
    			result[i][j]=a[i]*b[j];
    		}
    	}
     return	result;
     }
    //孙波 ( 2009-03-11  16:51:29)
    /**
     * 求矩阵的逆
     * @param matrix
     * @return
     */
    public static double[][]inv(double[][]matrix){
    		int n=matrix.length;
    		double[][] matrix1=new double[n][2*n];
    		double[][] result=new double[n][n];
    		for(int i=0;i<n;i++){
    			for(int j=0;j<n;j++){
    				matrix1[i][j]=matrix[i][j];
    			}
    		}
    		for(int k=0;k<n;k++){
    			   for(int t=n;t<n*2;t++){
    			    if((t-k)==n) {
    			    	matrix1[k][t]=1.0;
    			     }
    			    else{
    			    	matrix1[k][t]=0;
    			     }
    			    }
    			   }  
//    		get the change of the array
    		for(int k=0;k<n;k++){
    		   if(matrix1[k][k]!=1){
    		    double bs = matrix1[k][k];
    		    matrix1[k][k]=1;
    		   for(int p=k;p<n*2;p++)   {
    			   matrix1[k][p]/=bs;
    		    }
    		   }
    		   for(int q=0;q<n;q++) {
    		    if(q!=k) {
    		     double bs = matrix1[q][k];
    		     for(int p=0;p<n*2;p++)   {
    		    	 matrix1[q][p]-=bs*matrix1[k][p];
    		      }
    		     }
    		    else {
    		     continue;
    		     }
    		    }
    		   }
    		for(int x=0;x<n;x++){

    			   for(int y=n;y<n*2;y++){
    				   result[x][y-n]=matrix1[x][y];
    			    }
    		}

    			
           return result;
    	}

	public static double det(double[][] a){//Guass 消去
		double k=0;
	    double arrangerResult=1.0;
		for (int p = 0; p<a[0].length-1; p++) { 
		    for (int r =p+1; r<a.length; r++) {
		       k=a[r][p]/a[p][p];
		       a[r][p]=0;
		       for (int c = p+1; c<a[0].length; c++) {
		           a[r][c]=a[r][c]-k*a[p][c];
		       }
		    }
		}
		for (int i = 0; i<a.length; i++) {
		    for (int j = 0; j<a[0].length; j++) {
		       if (i==j) {arrangerResult=arrangerResult*a[i][j];}//计算主对角线相乘的结果
		    }
		}
		return arrangerResult;
	}
	public static double[] multi(double[] a,double[][] b){
		int lie=b[0].length;
		int hang=a.length;
		double result[]=new double[lie];
		for(int j=0;j<lie;j++){
			for(int i=0;i<hang;i++){
				result[j]+=a[i]*b[i][j];
			}
		}
		return result;
	}
	public static double[] zjz(double[] a ,int n){
		int hang=a.length;
		double[] result=new double[hang-1];
		for(int i=0;i<hang;i++){
			if (i<n){
				result[i]=a[i];
				}
			if (i>n){
				result[i-1]=a[i];
			}
		}
		return result;
	}
//	冒泡法对数组中的元素从大到�?排�?
	/**zj
	 * 冒泡法对数组中的元素从大到�?排�?
	 * @param args �?�数b浮点型（double）数组
	 * @return 返回值是一个浮点型二数组
	 */
	public static double[] MaxToMin(double[] b){
		double temp=0;
    	for(int i=0;i<b.length-1;i++){
    		for(int j=0;j<b.length-1;j++){
    			if(b[j+1]>b[j]){
    				temp=b[j];
    				b[j]=b[j+1];
    				b[j+1]=temp;
    			}
    		}
    	}
    	return b;
	}
//	冒泡法对数组中的元素从�?到大排�?
	/**zj
	 * 冒泡法对数组中的元素从�?到大排�?
	 * @param args �?�数b浮点型（double）数组
	 * @return 返回值是一个浮点型二数组
	 */
	public static double[] MinToMax(double[] b){
		double temp=0;
    	for(int i=0;i<b.length-1;i++){
    		for(int j=0;j<b.length-1;j++){
    			if(b[j+1]<b[j]){
    				temp=b[j];
    				b[j]=b[j+1];
    				b[j+1]=temp;
    			}
    		}
    	}
    	return b;
	}
	public static void main(String[] args) {
        double[] a={3,2,1,4,5,6,9,8};
        double[][] pp=MaxToMin1(a);
        System.out.println("新的排�?方法：");
        for(int i=0;i<pp[0].length;i++){
            System.out.println(pp[0][i]+"该元素在原数组中的�?置为："+pp[1][i]);
        }
	}
    //冒泡法对数组中的元素从大到�?排�?,并记录排�?�?�元素在数组中的�?置
	/**zj
	 * 冒泡法对数组中的元素从大到�?排�?
	 * @param args �?�数b浮点型（double）的一维数组
	 * @return 返回值a是一个浮点型（double）的二维数组，期中第一记录新的排�?，第二列记录原�?置
	 */
	public static double[][] MaxToMin1(double[] b){
		double[][] a=new double[2][b.length];
		for(int i=0;i<a[0].length;i++){
			a[0][i]=b[i];
			a[1][i]=i;
		}
		double temp=0;
		double location=0;
		for(int i=0;i<a[1].length-1;i++){
    		for(int j=0;j<a[1].length-1;j++){
    			if(a[0][j+1]>a[0][j]){
    				temp=a[1][j];
    				a[1][j]=a[1][j+1];
    				a[1][j+1]=temp;
    				location=a[0][j];
    				a[0][j]=a[0][j+1];
    				a[0][j+1]=location;
    				
    			}
    		}
    	}
		return a;
	}
	public static double[][] MaxToMin1_abs(double[] b){
		double[][] a=new double[2][b.length];
		for(int i=0;i<a[0].length;i++){
			a[0][i]=Math.abs(b[i]);
			a[1][i]=i;
		}
		double temp=0;
		double location=0;
		for(int i=0;i<a[1].length-1;i++){
    		for(int j=0;j<a[1].length-1;j++){
    			if(a[0][j+1]>a[0][j]){
    				temp=a[1][j];
    				a[1][j]=a[1][j+1];
    				a[1][j+1]=temp;
    				location=a[0][j];
    				a[0][j]=a[0][j+1];
    				a[0][j+1]=location;
    				
    			}
    		}
    	}
		return a;
	}
    // 去掉�?一行元素�?�的�?��?
	public static double[] zxl( double[] a, int place){
		double[] b=new double[a.length-1];
		for(int i=0;i<place;i++){
			b[i]=a[i];
		}
		for(int i=place;i<a.length-1;i++){
			b[i]=a[i+1];
		}
		return b;
	}
    // 去掉�?一行元素�?�的�?矩阵
	public static double[][] zjz_qh( double[][] a, int place){
		double[][] b=new double[a.length-1][a[0].length];
		for(int i=0;i<place;i++){
			for(int j=0;j<a[0].length;j++){
				b[i][j]=a[i][j];
			}
		}
		for(int i=place;i<a.length-1;i++){
			for(int j=0;j<a[0].length;j++){
				b[i][j]=a[i+1][j];
			}
		}
		return b;
	}
	/**zj
	 * 生�?�?��?阵
	 * @param args �?�数a是一个整型的数，是�?输出�?��?阵的行列数
	 * @return     I是�?��?矩阵     
	 */
	public static double[][] I(int a){
		double[][] I=new double[a][a];
		for(int i=0;i<a;i++){
			for(int j=0;j<a;j++){
				if(i==j){
					I[i][j]=1;
					}
				else{
					I[i][j]=0;
				}
			}
		}
			return I;
	}
}