#Java program to implement RSA Cryptosystem
#Checks applied:
#(i)	Value of n ( n=p * q) must be greater than Message
#(ii)	Primality testing to check if random P, Q are prime
#(iii)	GCD(e,T)=1  ,where T=Totient function

import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.lang.*;
import java.math.*;
public class Algorsa3{
     public long e;
     public long d;
     public static long Computegcd(long e, long t)
     {
       if (e==0)
        return t;
       else
        return Computegcd(t%e,e);
     }
     public static long exEuclid(long a,long b)
     {
       
       long x = 0, y = 1, x1 = 1, y1 = 0, tem;
        while (b != 0)
        {
            long quo = a / b;
            long re = a % b;
 
            a = b;
            b = re;
 
            tem = x;
            x = x1 - quo * x;
            x1 = tem;
 
            tem = y;
            y = y1 - quo * y;
            y1 = tem;            
        }
        return x1;
     }
     public static long pmod(long a, long b, long t) 
     {
        BigInteger a1=BigInteger.valueOf(a);
        BigInteger t1=BigInteger.valueOf(t);
        int b1=(int)b;
        BigInteger ans=a1.pow(b1);
        BigInteger f=ans.mod(t1);
        return f.longValue();
     }
     public static long Prime_Gen( )
    {
        long n = (long)(Math.random()*9999) ;
        int iter = 5;
        long r=0;
        boolean prime = isPrime(n, iter);
        if (prime)
        {
            //System.out.println("\n"+ n +" is prime");
           
        }
        else
        {
            r = Prime_Gen();
            return r;
        }
     return n;
    }
    public static boolean isPrime(long n, int iter)

    {

        if (n == 0 || n == 1)
            return false;
            
        if (n == 2)
            return true;

        if (n % 2 == 0) /* Check for even numbers*/
            return false;
        long d = n - 1;
        while (d % 2 == 0)
            d /= 2;
        Random rand = new Random();
        for (int i = 0; i < iter; i++)
        {
            long r = Math.abs(rand.nextLong());            
            long a = r % (n - 1) + 1, t = d;
            long m = powMod(a, t, n);
            while (t != n - 1 && m != 1 && m != n - 1)
            {
                m = sqrtMod(m, m, n);
                t *= 2;
            }
            if (m != n - 1 && t % 2 == 0)
                return false;
        }
        return true;
    }

	//Modular exponentiation. 
    	static int power(int x, int y, int p) {           
        int res = 1;  
        //Update x if it is more than or 
        // equal to p 
        x = x % p;    
        while (y > 0) { 
            // If y is odd, multiply x with result 
            if ((y & 1) == 1) 
                res = (res * x) % p; 
          
            // y must be even now 
            y = y >> 1; // y = y/2 
            x = (x * x) % p; 
        } 
          
        return res; 
    } 
    public static long sqrtMod(long a, long b, long mod) 
    {
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(mod)).longValue();
    }
     public static void main(String []args){
        long p,q,n,t,gcd=1,val=0,div,mod,e,k=0,y=1,r;int z;
        long d=2,m=0,enmesg,di,demesg,dtemp;
        int s2=1,r2=1;
        String[] inp,oup;
        char space[]= " ".toCharArray();
        char revarr[];
        String str=" abcdefghijklmnopqrstuvwxyz",mesg="",cov="",finalmesg="";
        char arr[]=str.toCharArray();
        Scanner br = new Scanner(System.in);
        //------------------accepting the input from user and converting it according to BEARCATII---------------
        System.out.println("----------Enter the input message that needs to be encrypted-----------");
        String mes=br.nextLine();
        char mess[]=mes.toCharArray();
        for(int i=0;i<mess.length;i++)
        {
	          
          	for(int j=0;j<arr.length;j++)
	    	{
		       if(mess[i]==arr[j])
		      {
		     	mesg +=Integer.toString(j) + " ";
		    	break;
		      }
		}
           }
            inp=mesg.split(" ");
        //---------------------converting number input to base 27 ------------------------------------------------
        int len=inp.length;
        for (int j=0;j<inp.length;j++)
        {
          
          val+=(Integer.parseInt(inp[j]) * ((long)Math.pow(27,j)));
          
        }
        System.out.println("Input in base 27 format is :" + val); // val contains the input in base 27 format
        //-----------------------Performing primality testing to find if numbers are prime-------------------------
        
        do
        {
         p = Prime_Gen();
         q = Prime_Gen();
         if(p == q)
         {
             q = Prime_Gen();
         }
         n=p*q;
         System.out.println("Two prime numbers obtained are :" + p + "," +q "n = p * q : " + n);
         }while(val>=n);
        t=(p-1)*(q-1);//totient function;
        System.out.println("Value to totient Function is : " + t);
        //----------------------accepting public value until gcd(publickey,totientfunc) value is 1-----------------
        do
        {
          System.out.println("-----------Enter value for public key(e)-------------");
          Scanner in = new Scanner(System.in);
          e=in.nextInt();
          gcd=Computegcd(e,t);
          //System.out.println("gcd value is " + gcd);
        }while(gcd!=1);
        //---------------------encryption of input message with public key e----------------------------------------
        m=pmod(val,e,n);
        enmesg=m;//calculating encrypted text to be sent
        System.out.println("encrypted message to be sent is : "+ enmesg);
        //-----------------------ecryption the message using extended euclidean algorithm----------------------------
        dtemp = exEuclid(e,t);
        d=(dtemp+t);
        //d=BigInteger.valueOf(e).modInverse(BigInteger.valueOf(t)).longValue();
        System.out.println("********************************************************************");
       System.out.println("private key value :"+ d);
        System.out.println("Calculating decrypted message.............");
        di=pmod(enmesg,d,n);
        demesg=di; //calculating decrypted text
        System.out.println("Decrypted message is :" + demesg);
        //-----------------------converting decrypted text from base 27 to normal output------------------------------
        do
        {
         div=demesg/27;
         mod=demesg%27;
         demesg=div;
         cov=cov+Long.toString(mod)+" ";
        }while(div>27);
        cov=cov +Long.toString(demesg);
        oup=cov.split(" ");
        for(int w=0;w<oup.length;w++)
        {
            z=Integer.parseInt(oup[w]);
            finalmesg+=arr[z];
        }
        System.out.println("The final message after decryption is :" + finalmesg);
     }
}