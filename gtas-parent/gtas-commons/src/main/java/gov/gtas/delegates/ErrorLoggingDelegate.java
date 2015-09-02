package gov.gtas.delegates;

import gov.gtas.delegates.vo.InvalidObjectInfoVo;
import gov.gtas.model.InvalidObjectInfo;
import gov.gtas.services.ErrorLoggingService;

import java.util.List;

import javax.annotation.Resource;

import javassist.bytecode.Descriptor.Iterator;

import org.springframework.stereotype.Component;

@Component
public class ErrorLoggingDelegate {
	
	@Resource
	ErrorLoggingService service;
	public String saveOrUpdate(List<InvalidObjectInfoVo> voList){
		if(voList != null && voList.size() >0){
			for(int i = 0;i <voList.size();i++){
				InvalidObjectInfoVo vo = voList.get(i);
				InvalidObjectInfo info=mapInvalidVoToObject(vo,new InvalidObjectInfo());
				if(info.getId() != null){
					service.update(info);
				}
				else{
					service.create(info);
				}
				
			}
		}
		
		
		return null;
	}
	
	private InvalidObjectInfo mapInvalidVoToObject(InvalidObjectInfoVo source, InvalidObjectInfo target){
		target.setId(source.getId());
		target.setCreatedBy(source.getCreatedBy());
		target.setCreationDate();
		target.setInvalidObjectType(source.getInvalidObjectType());
		target.setInvalidObjectValue(source.getInvalidObjectValue());
		target.setMessageKey(source.getInvalidObjectKey());
		return target;
		
	}
}
