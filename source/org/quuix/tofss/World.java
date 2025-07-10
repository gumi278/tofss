package org.quuix.tofss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class World
{
	public static final int CONFIGURE_ERROR = 0x01;
	
	static final ResourceBundle resource = ResourceBundle.getBundle(World.class.getName());

	
	public static void main(String[] args)
		throws IOException
	{
		int len = args.length;
		int i = 0;
		File v1 = null;
		File[] ls1 = null;
		Pattern allow = null;
		Pattern deny = null;
		DateTimeFormatter title = null;
		
		if(len > 0){
			for(i=0;len > i;i++){
				v1 = new File(args[i]);
				if(v1.isDirectory()){
					allow = allow(v1);
					deny = deny(v1);
					title = title(v1);
					ls1 = collect(v1, allow, deny);
					rename(ls1, title);
				}else{
					System.err.println("[args]: not found directory.");
					System.exit(CONFIGURE_ERROR);
				}
			}
		}else{
			usage();
		}
		
		return;
	}
	
	
	static DateTimeFormatter title(File path)
		throws IOException
	{
		DateTimeFormatter result = null;
		File v1 = null;
		File v2 = null;
		String s1 = null;
		
		s1 = System.getenv("TOFSS_TITLE");
		if(s1 == null){
			s1 = "yyyyMMdd+HHmmss";
		}else{
			//.
		}
		
		v1 = new File(path, ".tofss");
		v2 = new File(v1, "title.txt");
		if(v2.isFile()){
			s1 = Util.head(v2);
		}else{
			//.
		}
		result = DateTimeFormatter.ofPattern(s1);
		
		return result;
	}
	
	
	static void rename(File[] ls,DateTimeFormatter title)
	{
		int len = 0;
		int i = 0;
		long g1 = 0L;
		TimeZone tz = TimeZone.getDefault();
		LocalDateTime v1 = null;
		String s1 = null;
		int n1 = 0;
		String s2 = null;
		String s3 = null;
		String s4 = null;
		File v2 = null;
		boolean b1 = false;
		
		len = ls.length;
		for(i=0;len > i;i++){
			s1 = ls[i].getName();
			n1 = s1.lastIndexOf('.');
			if(0 > n1){
				s2 = "";
			}else{
				s2 = s1.substring(n1);
			}
			
			g1 = ls[i].lastModified();
			v1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(g1), tz.toZoneId());
			s3 = v1.format(title);
			s4 = s3 + s2;
			v2 = new File(ls[i].getParentFile(), s4);
			
			b1 = ls[i].renameTo(v2);
			if(b1){
				//.
			}else{
				System.err.println(ls[i]);
			}
		}
		
		return;
	}
	
	
	static File[] collect(File path,Pattern allow,Pattern deny)
	{
		File[] result = null;
		File[] ls1 = null;
		int len = 0;
		int i = 0;
		String s1 = null;
		ArrayList<File> ls2 = null;
		Matcher v1 = null;
		
		ls1 = path.listFiles();
		len = ls1.length;
		ls2 = new ArrayList<>(len);
		for(i=0;len > i;i++){
			if(ls1[i].isHidden()){
				continue;
			}else{
				//.
			}
			if(ls1[i].isFile()){
				//.
			}else{
				continue;
			}
			
			s1 = ls1[i].getName();
			
			v1 = allow.matcher(s1);
			if(v1.find()){
				//.
			}else{
				continue;
			}
			
			v1 = deny.matcher(s1);
			if(v1.find()){
				continue;
			}else{
				//.
			}
			
			ls2.add(ls1[i]);
		}
		
		len = ls2.size();
		result = new File[len];
		ls2.toArray(result);
		
		return result;
	}
	
	
	static Pattern allow(File path)
		throws IOException
	{
		Pattern result = null;
		File v1 = null;
		File v2 = null;
		String s1 = null;
		
		s1 = System.getenv("TOFSS_ALLOW");
		if(s1 == null){
			s1 = "^\\d{5}\\.png$";
		}else{
			//.
		}
		
		v1 = new File(path, ".tofss");
		v2 = new File(v1, "allow.txt");
		if(v2.isFile()){
			s1 = Util.head(v2);
		}else{
			//.
		}
		result = Pattern.compile(s1, Pattern.CASE_INSENSITIVE);
		
		return result;
	}
	
	
	static Pattern deny(File path)
		throws IOException
	{
		Pattern result = null;
		File v1 = null;
		File v2 = null;
		String s1 = null;
		
		s1 = System.getenv("TOFSS_DENY");
		if(s1 == null){
			s1 = "\\d{8}\\+\\d{6}";
		}else{
			//.
		}
		
		v1 = new File(path, ".tofss");
		v2 = new File(v1, "deny.txt");
		if(v2.isFile()){
			s1 = Util.head(v2);
		}else{
			//.
		}
		result = Pattern.compile(s1, Pattern.CASE_INSENSITIVE);
		
		return result;
	}
	
	
	static class Util
	{
		public static String head(File v)
			throws IOException
		{
			String result = null;
			FileInputStream in1 = null;
			InputStreamReader in2 = null;
			BufferedReader in3 = null;
			
			in1 = new FileInputStream(v);
			in2 = new InputStreamReader(in1);
			in3 = new BufferedReader(in2);
			result = in3.readLine();
			in3.close();
			in2.close();
			in1.close();
			
			return result;
		}
	}
	
	
	static void usage()
		throws IOException
	{
		String s1 = null;
		InputStream in1 = null;
		InputStreamReader in2 = null;
		OutputStreamWriter out1 = null;
		
		s1 = resource.getString("Usage");
		in1 = World.class.getResourceAsStream(s1);
		in2 = new InputStreamReader(in1, StandardCharsets.UTF_8);
		out1 = new OutputStreamWriter(System.out);
		in2.transferTo(out1);
		in2.close();
		in1.close();
		out1.close();
		
		return;
	}
}
