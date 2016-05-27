package com.baishi.db.cn;

import android.util.Log;


	
public class MyNative {
	public int TYPE_EMPLOYEID = 1;	//任务单
	public int TYPE_COMPANYID = 2;		//网点编号
	public int TYPE_BARCODE = 3;		//运单号
	public int TYPE_CARID = 4;			//车辆吗
	public int TYPE_CARLINE = 5;		//车线码
	public int TYPE_PACKET = 6;		//大包号
	public int TYPE_BAKCODE = 7;		//回单号
	public int TYPE_TAG = 8;			//封签
	public int TYPE_GEKOU = 9;			//隔口号  5位以内数字和字母组合
	public int TYPE_WORKID = 10;		//工位号ID
	public int TYPE_FACHE_PZ = 11;		//发车凭证
	public static int TYPE_HKZD = 12;			//航空主单
	public int TYPE_NULL = 255;

	

 byte[] U8;

	public MyNative() {
		// TODO Auto-generated constructor stub
	}
	public  int CheckBarcode(String pCode, int codeType){
		return OpCom_CheckBarcode(pCode, codeType);
	};
	
	
/*==============条码规则===================*/
 public String[] gc8AreaCode=
	 {
	 	"010",
	 	"020",
	 	"021",
	 	"022",
	 	"023",
	 	"024",
	 	"025",
	 	"027",
	 	"028",
	 	"029",
	 	"0471",
	 	"0351",
	 	"0311",
	 	"0431",
	 	"0451",
	 	"0551",
	 	"0531",
	 	"0571",
	 	"0791",
	 	"0591",
	 	"0731",
	 	"0371",
	 	"0898",
	 	"0771",
	 	"0851",
	 	"0871",
	 	"0931",
	 	"0951",
	 	"0971",
	 	"0991",
	 	"0891",
	 	"886",
	 	"0886",
	 	"852",
	 	"0852",
	 	"853",
	 	"0853",
	 };




public int HY_OK = 0;
public int HY_ERROR = 1;
/*------------------------------------
*函数:hy_IfDigit
*功能:判断字符串是否全是数字
*参数:pStr:待检测的字符串
*返回:HY_OK:成功   HY_ERROR:失败
*------------------------------------*/
public int hy_IfDigit(char[] pStr)
{
	int len,i;
	int ret = HY_OK;
	
	len = pStr.length;
	for(i = 0; i < len; i++)
	{
		if(pStr[i] < '0' || pStr[i] > '9')
		{
			ret = HY_ERROR;
			break;
		}
		i++;
	}
	return ret;
}

/*------------------------------------
*函数:hy_IfChar
*功能:判断字符串是否全是字母
*参数:pStr:待检测的字符串
*返回:HY_OK:成功   HY_ERROR:失败
*------------------------------------*/
int hy_IfChar(char[] pStr)
{
	int len,i;
	int ret = HY_OK;
	char ch;

	len = pStr.length;
	for(i = 0; i < len; i++)
	{
		ch = pStr[i];
		if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
		{
			i++;
		}
		else
		{
			ret = HY_ERROR;
			break;
		}
	}

	return ret;
}


/*------------------------------------
*函数:hy_IfChar_Digit
*功能:判断字符串是否字母与数字组合
*参数:pStr:待检测的字符串
*返回:HY_OK:成功   HY_ERROR:失败
*------------------------------------*/
int hy_IfChar_Digit(char[] pStr)
{
	int len,i;
	int ret = HY_OK;
	char  ch,flag=0;

	len = pStr.length;
	for(i = 0; i < len; i++)
	{
		ch = pStr[i];
		if(ch >= '0' && ch <= '9')
		{
			i++;
		}
		else if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
		{
			i++;
			flag = 1;
		}
		else
		{
			ret = HY_ERROR;
			flag = 0;
			break;
		}
	}

	if(flag == 1)
	{
		ret = HY_OK;
	}
	else
	{
		ret = HY_ERROR;
	}

	return ret;
}


//成功:HY_OK   失败HY_ERROR
int Op_Comm_CheckCarCode(char[] pCarCode, int len)
{
	int i, total, ret = HY_ERROR;
	int cmpLen;

	String char_code;
	if(len == 9)
	{
		cmpLen = 3;
		char_code = String.valueOf(pCarCode).substring(0, 3); 
	}
	else
	{
		cmpLen = 4;
		char_code = String.valueOf(pCarCode).substring(0, 4); ; 
	}
	total = gc8AreaCode.length/pCarCode.length;

	for(i = 0; i < total; i++)
	{
		
		if(char_code.equals(gc8AreaCode[i]))
		{
			ret = HY_OK;
			break;
		}
	}


	return ret;
}


/*--------------------------------------
*函数：opCom_CheckBarcodeBase
*功能：根据指定规则检查条码合法性(按基本规则判定)
*	  0x20---0x7F
*参数：pCode:带检查的条码
       codeType:条码种类
*返回：HY_OK:成功    HY_ERROR:失败     0:没有规则
*--------------------------------------*/
int opCom_CheckBarcodeBase(char[] pCode, int len)
{
	int i;
	
	for (i=0; i<len; i++)
	{
		if (pCode[i] < ' ' || pCode[i] > '~')
		{
			return HY_ERROR;
		}
	}
	return HY_OK;
}

 /*--------------------------------------
*函数：OpCom_CheckBarcode
*功能：根据指定规则检查条码合法性
*参数：pCode:带检查的条码
       codeType:条码种类
*返回：1:成功    -1:失败     0:没有规则
*--------------------------------------*/
public int OpCom_CheckBarcode(String pCode, int codeType)
{
	int ret = -1, len, i,digit=0,chara=0;
	char  ch;
	
	len = pCode.length();//条码长度
	if(len == 0)
	{
		return -1;
	}
	//判断条码基本规则(半角可显字符)
	if (opCom_CheckBarcodeBase(pCode.toCharArray(), len) == HY_ERROR)
	{
		return -1;
	}
	
	switch(codeType)
	{
	case 1://1任务单  
		if(len == 8 && (pCode.startsWith("s")|| pCode.startsWith("S")
				|| pCode.startsWith("P")|| pCode.startsWith("p")))
		{
			if(hy_IfDigit(pCode.substring(1, pCode.length()).toCharArray())== 0)
				ret = 1;
			else ret = -1;
		}else if(len == 6 && hy_IfDigit(pCode.toCharArray()) == 0 ){
			ret = 1;
		}else ret = -1;
		
		break;
	case 2://2 网点 --6位数字
		if(len == 6 && hy_IfDigit(pCode.toCharArray()) == 0)
		{
			ret = 1;
		}
		break;
	case 3://3 运单 
		if(len == 9){
			if(pCode.startsWith("Q"))
				ret = 1;
			else ret = -1;
		}else if(len == 12 && hy_IfDigit(pCode.toCharArray()) == 0){
			if(pCode.startsWith("10") 
					|| pCode.startsWith("11")|| pCode.startsWith("21")
					|| pCode.startsWith("22")|| pCode.startsWith("23")
					|| pCode.startsWith("24")|| pCode.startsWith("25")
					|| pCode.startsWith("26")|| pCode.startsWith("27")
					|| pCode.startsWith("28")|| pCode.startsWith("29")
					|| pCode.startsWith("30")|| pCode.startsWith("31")
					|| pCode.startsWith("32")|| pCode.startsWith("33")
					|| pCode.startsWith("34")|| pCode.startsWith("35")
					|| pCode.startsWith("37")|| pCode.startsWith("39")
					|| pCode.startsWith("40")|| pCode.startsWith("41")
					|| pCode.startsWith("42")|| pCode.startsWith("43")
					|| pCode.startsWith("360")|| pCode.startsWith("369")
					|| pCode.startsWith("639")|| pCode.startsWith("649")
					|| pCode.startsWith("61")|| pCode.startsWith("66")
					|| pCode.startsWith("630"))
			{
				ret = 1;
			}else ret = -1;
		}else
		if(len == 13){
			if(pCode.startsWith("0") 
					|| pCode.startsWith("2")|| pCode.startsWith("2000")
					|| pCode.startsWith("A000")|| pCode.startsWith("B000")
					|| pCode.startsWith("BXA0")|| pCode.startsWith("C0000")
					|| pCode.startsWith("D000")|| pCode.startsWith("D90000")
					|| pCode.startsWith("E000")|| pCode.startsWith("FC")
					|| pCode.startsWith("GDHD")|| pCode.startsWith("HD")
					|| pCode.startsWith("HD00000")|| pCode.startsWith("HTKY")
					|| pCode.startsWith("HTWL")|| pCode.startsWith("K8")
					|| pCode.startsWith("TEST")|| pCode.startsWith("Z0000"))
			{
				ret = 1;
			}else ret = -1;
		}else 
		if(len == 14 && hy_IfDigit(pCode.toCharArray()) == 0){
			if(pCode.startsWith("50") || pCode.startsWith("51")|| pCode.startsWith("7"))
			{
				ret = 1;
			}else ret = -1;
		}
		break;
	case 6://6 大包
		if(len == 12 && hy_IfDigit(pCode.toCharArray()) == 0)
		{
			if(pCode.startsWith("40")|| pCode.startsWith("42") || pCode.startsWith("43")){
				ret = 1;
			}else 
				ret = -1;
		}else
			ret = -1;
		break;
	case 4://4  车辆  ---9或10位字母和数字组合
		if((len == 9 || len == 10) && hy_IfChar_Digit(pCode.toCharArray()) == 0)
		{
			//判断前面的3或4位数字
			if(HY_OK == Op_Comm_CheckCarCode(pCode.toCharArray(), len))
			{
				ret = 1;
			}
		}
		break;
	case 5://5 车线 --8位数字
		if(len == 8 && hy_IfDigit(pCode.toCharArray()) == 0)
		{
			ret = 1;
		}
		break;
	case 8://8 封签  --7开头的13位数字
		if(len == 13 && pCode.startsWith("7")&& hy_IfDigit(pCode.toCharArray()) == 0)
		{
			ret = 1;
		}
		break;
	case 9://9 隔口号  两位大写字母GK+三位数字或字母或数字字母组合
		if(len == 5 && pCode.startsWith("GK"))
		{
			if(hy_IfChar_Digit(pCode.toCharArray()) == 0 || hy_IfChar(pCode.toCharArray()) == 0)
			{
				ret = 1;
			}
		}
		break;
	case 10://10 工位号 目前暂定为9-20位的数字与字母组合或纯数字
		if(len >= 9 && len <= 20)
		{
			if(hy_IfDigit(pCode.toCharArray()) == 0 || hy_IfChar_Digit(pCode.toCharArray()) == 0)
			{
				ret = 1;
			}
		}
		break;
	case 11://11  发车凭证 11位纯数字的条码
		if(len == 11 && hy_IfDigit(pCode.toCharArray()) == 0 && (pCode.startsWith("31")||pCode.startsWith("68")))
		{
			ret = 1;
		}
		break;
	case 12://12 航空主单 11纯数字条码
		if(len == 11 && hy_IfDigit(pCode.toCharArray()) == 0)
		{
			ret = 1;
		} 
		break;	
	default:
		break;
	}

	return ret;
}
}
