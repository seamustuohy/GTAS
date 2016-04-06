package gov.gtas.services;

import gov.gtas.dto.FlightDto;
import gov.gtas.dto.PaxDto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PnrGen {

	private static String newline=System.getProperty("line.separator");
	//private static StringBuilder sb=new StringBuilder();
	private static String mNum="0"+GenUtil.getRandomNumber(999)+"A"+GenUtil.getRandomNumber(999);
	//private static List<FlightDto> flights=new ArrayList<FlightDto>();
	private static LoaderUtils lu=new LoaderUtils();

	public static void buildHeader(String code,StringBuilder sb){
		sb.append("UNA:+.?*'"+newline);
		sb.append("UNB+IATA:1+");
		sb.append(code);
		sb.append("++");
		sb.append(GenUtil.getDate());
		sb.append("+"+mNum+"'"+newline);
		sb.append("UNH+1+PNRGOV:10:1:IA+F6C2C268'");
	}
	
	public static void buildMessage(String code,StringBuilder sb){
		if(code.equals("22")){
			sb.append("MSG+:22'");
		}
		else{
			sb.append("MSG+:"+code);
			sb.append("'");
		}
	}
	
	public static void buildOrigDestinations(String carrier, String orig,String dest, String flightNumber,String date,StringBuilder sb){
		sb.append("ORG+");
		sb.append(carrier+":");
		sb.append(orig);
		sb.append("+52519950'"+newline);
		sb.append("TVL+");
		sb.append(GenUtil.getDepArlTime(6)+":"+GenUtil.getDepArlTime(2)+"+");
		sb.append(orig+"+"+dest+"+"+carrier+"+"+flightNumber+"'");
	}
	
	public static void buildEqn(int numPax,StringBuilder sb){
		sb.append("EQN+"+numPax+"'");
	}
	
	public static void buildPassenger(String carrier, String orig,String dest, String flightNumber,String date,int numPax,StringBuilder sb,FlightDto dto){
		String add=GenUtil.getSponsorAddress();
		String ph=GenUtil.getPhoneNumber();
		String lName=GenUtil.getLastName();
		List<String> ssrs=new ArrayList<>();
		for(int i =1;i<=numPax;i++){
			PaxDto pDto= new PaxDto();
			String fName=GenUtil.getFirstName();
			String id=""+GenUtil.getRandomNumber(9999);
			pDto.setFirstName(fName);
			pDto.setLastName(lName);
			pDto.setEmbark(orig);
			pDto.setDebark(dest);
			//TIF+DYE+KAYLAMS:A:43578:1'
			sb.append("TIF+"+lName+"+"+fName+":A:"+id+":"+i+"'"+newline);
			sb.append("FTI+"+carrier+":8"+GenUtil.getRandomNumber(999999)+":::ELITE'"+newline);
			sb.append("IFT+4:15:9+"+orig+" "+carrier+" X/"+dest+" "+carrier+" GBP/IT END ROE0."+GenUtil.getRandomNumber(999)+"'"+newline);
			sb.append("REF+:"+GenUtil.getRandomNumber(999999999)+"P'"+newline);
			sb.append("FAR+N+++++MIL24'"+newline);
			String country=GenUtil.getCountryCode();
			pDto.setDocNumber(GenUtil.getRandomNumber(9999999));
			sb.append("SSR+DOCS:HK::"+carrier+":::::/P/"+country+"/"+pDto.getDocNumber()+"/"+country+"/"+GenUtil.getBirthDate()+"/"+GenUtil.getGender()+"/"+GenUtil.getExpiryDate()+"/"+lName+"/"+fName+"'"+newline);
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
			dto.getPaxList().add(pDto);
		}
		dto.setStartDate(GenUtil.getDepArlTime(1));
		dto.setEndDate(GenUtil.getDepArlTime(6));
		sb.append("TVL+"+dto.getStartDate()+":"+dto.getEndDate()+"+"+orig+"+"+dest+"+"+carrier+"+"+flightNumber+":B'"+newline);
		sb.append("RPI+"+numPax+"HK'"+newline);
		sb.append("APD+7"+GenUtil.getRandomNumber(9)+"7'"+newline);

		for(String s:ssrs){
			sb.append(s);
		}
		
	}
	
	public static void buildSrc(String carrier, String orig,String dest, String flightNumber,String date,int numPax,FlightDto dto,StringBuilder sb){
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
		buildPassenger(carrier, orig,dest, flightNumber,date,numPax,sb,dto);
	}
	
	public static void buildFooter(StringBuilder sb){
		sb.append("UNT+135+1'"+newline);
		sb.append("UNZ+1+"+mNum+"'"+newline);
	}

	public static void writeToFile(int num,StringBuilder sb){
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

	public static StringBuilder buildApisHeader(StringBuilder sb,FlightDto fd){
		sb.append("UNA:+.? '"+newline);
		//UNB+UNOA:4+SHARES:UA+USCSAPIS+140201:1500+1402011500++APIS'
		sb.append("UNB+UNOA:4+SHARES:");
		sb.append(fd.getEmbarkCountry()+"+USCSAPIS+140201:1500+1402011500++APIS'"+newline);//YYMMDD
		//UNG+PAXLST+UNITED AIRLINES INC:UA+USCSAPIS+140201:1500+1402011500+UN+D:02B'
		//UNG *PAXLST*AMERICAN AIRLINES:ZZ*JMAPIS:ZZ*140211:1800*1402111800*UN*D:02B$
		sb.append("UNG+PAXLST+UNITED AIRLINES INC:UA+USCSAPIS+140201:1500+1402011500+UN+D:02B'"+newline);
		//UNH+UA0138-140201C+PAXLST:D:02B:UN:IATA++02:F'
		sb.append("UNH+1402111800+PAXLST:D:02B:UN:IATA++02:F'"+newline);
		//BGM+250+C'NAD+MS+++UNITED HELP DESK'
		//BGM*745$"""
		sb.append("BGM+745'"+newline);
		return sb;
	}

	public static StringBuilder buildApisReportingParty(StringBuilder sb){
		//NAD+MS+++UNITED HELP DESK'
		//COM*918-833-3535:TE*918-292-7090:FX$"""
		//COM+832-235-1556:TE+281-553-1491:FX'
		sb.append("NAD+MS+++UNITED HELP DESK'"+newline);
		sb.append("COM+832-235-1556:TE+281-553-1491:FX'"+newline);
		return sb;
	}

	public static StringBuilder buildApisFlight(StringBuilder sb,FlightDto fd ){
		//TDT*20*{0}{1}$
		//TDT+20+UA0138'
		sb.append("TDT+20+");
		sb.append(fd.getCarrier()+fd.getFlightNum()+"'"+newline);
		//LOC+125+NRT'
		//LOC+125+LAS'DTM+189:1401312330:201'LOC+87+YYZ'
		sb.append("LOC+125+"+fd.getEmbark()+"'");
		sb.append("DTM+189:"+GenUtil.getYYMMDD(-1)+":201'"+newline);
		//DTM+189:1401312330:201'
		//DTM+232:1401310642:201'
		sb.append("LOC+125+"+fd.getDebark()+"'"+newline);
		sb.append("DTM+232:"+GenUtil.getYYMMDD(5)+":201'"+newline);
		return sb;
	}
	
	public static StringBuilder buildPaxList(StringBuilder sb,PaxDto pd,FlightDto fd){
		//NAD NAD+FL+++DOE:JOHN:WAYNE+20 MAIN STREET+ANYCITY+VA+10053+USA’
		//NAD*FL***{0}:{1}:$
		sb.append("NAD+FL+++"+pd.getLastName()+":"+pd.getFirstName()+":"+pd.getLastName()+"+"+GenUtil.getPaxAddress()+"'"+newline);
		//ATT
		//ATT*2**M$  ATT+2++M’
		sb.append("ATT+2++"+GenUtil.getGender()+"'"+newline);
		//DTM  DTM*329:541016$
		sb.append("DTM+329:"+GenUtil.getPaxBirthday()+"'"+newline);
		//LOC LOC*178*{2}$
		//LOC*179*{3}$
		sb.append("LOC+178+"+fd.getEmbarkCountry()+"'"+newline);
		sb.append("LOC+179+"+fd.getDebarkCountry()+"'"+newline);
		//NAT NAT*2*USA$
		sb.append("NAT+2+USA'"+newline);
		//RFF*AVF:QXGYLO$
		sb.append("RFF+AVF:"+"QXGYLO"+GenUtil.getRandomNumber(999)+"'"+newline);
		//DOC DOC+P:110:111+MB140241’  DOC*P:110:111*{4}$
		sb.append("DOC+P:110:111+"+pd.getDocNumber()+"'"+newline);
		//DTM DTM*36:180221$  DTM+36:081021’
		sb.append("DTM+36:181022'"+newline);
		//LOC  LOC*91*USA$"""
		sb.append("LOC+91+"+fd.getEmbarkCountry()+"'"+newline);
		
		return sb;
	}
	
	public static void buildApisFooter(StringBuilder sb){
		sb.append("CNT+42:136'"+newline);
		sb.append("UNT+121+1402111800'"+newline);
		sb.append("UNE+1+1402111800'"+newline);
		sb.append("UNZ+1+1402111800'"+newline);
	}
	public static void buildApisMessages(List<FlightDto> flights,int counter){
		
		for(FlightDto fd : flights){
			counter++;
			
			StringBuilder sb= new StringBuilder();
			buildApisHeader(sb,fd);
			buildApisReportingParty(sb);
			buildApisFlight(sb,fd);
		
			Iterator it = fd.getPaxList().iterator();
			if(it.hasNext()){
				PaxDto pd=(PaxDto)it.next();
				buildPaxList(sb,pd,fd);
				
			}
			
			buildApisFooter(sb);
			writeToApisFile(counter,sb);
			
			sb=null;
		}
	}
	
	public static void writeToApisFile(int num,StringBuilder sb){
		String fileName="C:\\PNR"+"\\apis"+num+".txt";
		System.out.println("Writing to file"+fileName);	
        try{
            String content = sb.toString();
            File apisFile = new File(fileName);
               if (!apisFile.exists()) {
            	   apisFile.createNewFile();
               }
               FileWriter fw = new FileWriter(apisFile.getAbsoluteFile());
               BufferedWriter bw = new BufferedWriter(fw);
               bw.write(content);
               bw.close();
         }catch(Exception e){
             System.out.println(e);
         }
	}
	//run it from MessageGeneratorIT classs
//	public static void main(String[] args) {
//		for(int i=56;i <=57;i++){
//			FlightDto dto = new FlightDto();
//			StringBuilder sb = new StringBuilder();
//			String carrier=GenUtil.getCarrier();
//			String origin=GenUtil.getAirport();
//			String dest=GenUtil.getAirport();
//			String fNumber=GenUtil.getFlightNumber();
//			String dString=GenUtil.getPnrDate();
//			int numPax=GenUtil.getRandomNumber(3)+2;
//			dto.setCarrier(carrier);
//			dto.setDebark(dest);
//			dto.setEmbark(origin);
//			dto.setFlightNum(fNumber);
//			
//			buildHeader(carrier,sb);
//			buildMessage("22", sb);
//			buildOrigDestinations(carrier, origin,dest,fNumber,dString, sb);
//			buildEqn(numPax,sb);
//			buildSrc(carrier, origin,dest,fNumber,dString,numPax,dto,sb);
//			buildFooter(sb);
//			//flights.add(dto);
//			System.out.println(sb.toString());	
//			//writeToFile(i,sb);
//			sb=null;
//		}
//		//buildApisMessages();
//	}
	

}
