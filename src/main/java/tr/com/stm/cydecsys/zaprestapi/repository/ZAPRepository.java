package tr.com.stm.cydecsys.zaprestapi.repository;

import org.springframework.data.repository.CrudRepository;
import tr.com.stm.cydecsys.zaprestapi.model.ZAPScanResult;

public interface ZAPRepository extends CrudRepository<ZAPScanResult, Integer> {}
