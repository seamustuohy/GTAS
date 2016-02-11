package gov.gtas.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class PnrGen {

	private static String newline=System.getProperty("line.separator");
	private static StringBuilder sb;
	private static String mNum="0"+GenUtil.getRandomNumber(999)+"A"+GenUtil.getRandomNumber(999);
	
	private static void buildHeader(String code){
		sb.append("UNA:+.?*'"+newline);
		sb.append("UNB+IATA:1+");
		sb.append(code);
		sb.append("++");
		sb.append(GenUtil.getDate());
		sb.append("+"+mNum+"'"+newline);
		sb.append("UNH+1+PNRGOV:10:1:IA+F6C2C268'");
	}
	
	private static void buildMessage(String code){
		if(code.equals("22")){
			sb.append("MSG+:22'");
		}
		else{
			sb.append("MSG+:"+code);
			sb.append("'");
		}
	}
	
	private static void buildOrigDestinations(String carrier, String orig,String dest, String flightNumber,String date){
		sb.append("ORG+");
		sb.append(carrier+":");
		sb.append(orig);
		sb.append("+52519950'"+newline);
		sb.append("TVL+");
		sb.append(GenUtil.getDepArlTime(6)+":"+GenUtil.getDepArlTime(2)+"+");
		sb.append(orig+"+"+dest+"+"+carrier+"+"+flightNumber+"'");
	}
	
	private static void buildEqn(int numPax){
		sb.append("EQN+"+numPax+"'");
	}
	
	private static void buildPassenger(String carrier, String orig,String dest, String flightNumber,String date,int numPax,StringBuilder sb){
		String add=GenUtil.getSponsorAddress();
		String ph=GenUtil.getPhoneNumber();
		String lName=GenUtil.getLastName();
		List<String> ssrs=new ArrayList<>();
		for(int i =1;i<=numPax;i++){
			String fName=GenUtil.getFirstName();
			String id=""+GenUtil.getRandomNumber(9999);
			//TIF+DYE+KAYLAMS:A:43578:1'
			sb.append("TIF+"+lName+"+"+fName+":A:"+id+":"+i+"'"+newline);
			sb.append("FTI+"+carrier+":8"+GenUtil.getRandomNumber(999999)+":::ELITE'"+newline);
			sb.append("IFT+4:15:9+"+orig+" "+carrier+" X/"+dest+" "+carrier+" GBP/IT END ROE0."+GenUtil.getRandomNumber(999)+"'"+newline);
			sb.append("REF+:"+GenUtil.getRandomNumber(999999999)+"P'"+newline);
			sb.append("FAR+N+++++MIL24'"+newline);
			String country=GenUtil.getCountryCode();
			sb.append("SSR+DOCS:HK::"+carrier+":::::/P/"+country+"/"+GenUtil.getRandomNumber(9999999)+"/"+country+"/"+GenUtil.getBirthDate()+"/"+GenUtil.getGender()+"/"+GenUtil.getExpiryDate()+"/"+lName+"/"+fName+"'"+newline);
			String s="SSR+SEAT:HK:"+i+":"+carrier+":::"+orig+":"+dest+"+"+GenUtil.getRandomNumber(99)+"A"+"::"+id+":N'"+newline;
			ssrs.add(s);
			sb.append("SSR+AVML:HK:"+i+":"+carrier+"'"+newline);
			sb.append("TKT+30"+GenUtil.getRandomNumber(999999999)+":T:1'"+newline);
			sb.append("MON+B:"+GenUtil.getRandomNumber(9999)+".00:USD+T:"+GenUtil.getRandomNumber(9999)+".94:USD'"+newline);
			sb.append("PTK+NR++"+date+"+"+carrier+"+006+"+orig+"'"+newline);
			sb.append("TXD++6.10::USD'"+newline);
			sb.append("DAT+710:"+date+"'"+newline);
			sb.append("FOP+CC:::"+GenUtil.getCcNum()+newline);
			sb.append("IFT+4:43+"+lName+" "+fName+"+"+add+"+"+ph+"'"+newline);			
		}
		sb.append("TVL+"+GenUtil.getDepArlTime(1)+":"+GenUtil.getDepArlTime(6)+"+"+orig+"+"+dest+"+"+carrier+"+"+flightNumber+":B'"+newline);
		sb.append("RPI+"+numPax+"HK'"+newline);
		sb.append("APD+7"+GenUtil.getRandomNumber(9)+"7'"+newline);

		for(String s:ssrs){
			sb.append(s);
		}
	}
	
	private static void buildSrc(String carrier, String orig,String dest, String flightNumber,String date,int numPax){
		String add=GenUtil.getAddress();
		String ph=GenUtil.getPhoneNumber();
		sb.append("SRC'"+newline);
		sb.append("RCI+"+carrier+":"+GenUtil.getRecordLocator()+"'"+newline);
		sb.append("SSR+AVML:HK:2:"+carrier+"'"+newline);
		sb.append("DAT+700:"+GenUtil.getDate()+"+710:"+GenUtil.getTicketDate()+"'"+newline);
		sb.append("IFT+4:28::"+carrier+"+THIS PASSENGER IS A VIP'"+newline);
		sb.append("IFT+4:28::"+carrier+"+CTCR "+ph+"'"+newline);
		sb.append("ORG+"+carrier+":"+orig+"+52519950:LON+++A+GB:GBP+D050517'"+newline);
		sb.append("ADD++"+add+"'"+newline);
		sb.append("EBD+USD:40.00+1::N'"+newline);
		buildPassenger(carrier, orig,dest, flightNumber,date,numPax,sb);
	}
	
	private static void buildFooter(){
		sb.append("UNT+135+1'"+newline);
		sb.append("UNZ+1+"+mNum+"'"+newline);
	}
	
	public static void main(String[] args) {
		for(int i=31;i <=35;i++){
			sb = new StringBuilder();
			String carrier=GenUtil.getCarrier();
			String origin=GenUtil.getAirport();
			String dest=GenUtil.getAirport();
			String fNumber=GenUtil.getFlightNumber();
			int numPax=GenUtil.getRandomNumber(3)+2;
		
			String dString=GenUtil.getPnrDate();
			buildHeader(carrier);
			buildMessage("22");
			buildOrigDestinations(carrier, origin,dest,fNumber,dString);
			buildEqn(numPax);
			buildSrc(carrier, origin,dest,fNumber,dString,numPax);
			buildFooter();
			System.out.println(sb.toString());	
			writeToFile(i,sb);
			sb=null;
		}
	}
	
	private static void writeToFile(int num,StringBuilder sb){
		String fileName="C:\\PNR"+"\\pnr"+num+".txt";
		System.out.println("Writing to file"+fileName);	
        try{
            String content = sb.toString();
            File pnrFile = new File(fileName);
               if (!pnrFile.exists()) {
            	   pnrFile.createNewFile();
               }
               FileWriter fw = new FileWriter(pnrFile.getAbsoluteFile());
               BufferedWriter bw = new BufferedWriter(fw);
               bw.write(content);
               bw.close();
         }catch(Exception e){
             System.out.println(e);
         }
	}

}
