package gov.gtas.services;

import gov.gtas.model.ApisStatistics;
import gov.gtas.model.PnrStatistics;
import gov.gtas.repository.ApisStatisticsRepository;
import gov.gtas.repository.PnrStatisticsRepository;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MessageStatisticsServiceImpl implements MessageStatisticsService {

	@Resource
	private PnrStatisticsRepository pnrStatisticsRepository;
	
	@Resource
	private ApisStatisticsRepository apisStatisticsRepository;
	
	@Override
	@Transactional
	public PnrStatistics getPnrStatistics() {
		PnrStatistics stat=new PnrStatistics();
		List<PnrStatistics> statsList=(List<PnrStatistics>)pnrStatisticsRepository.findAll();
		if(statsList != null && statsList.size() >0){
			stat=statsList.get(0);
		}
		return stat ;
	}

	@Override
	@Transactional
	public ApisStatistics getApisStatistics() {
		ApisStatistics stat = new ApisStatistics();
		List<ApisStatistics> statsList = (List<ApisStatistics>)apisStatisticsRepository.findAll();
		if(statsList != null && statsList.size() >0){
			stat=statsList.get(0);
		}
		return stat;
	}

}
