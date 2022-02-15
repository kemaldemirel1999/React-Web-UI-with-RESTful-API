package tr.com.stm.cydecsys.zaprestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import tr.com.stm.cydecsys.zaprestapi.model.ZAPScanResult;

import java.util.List;

public interface ZAPRepository extends MongoRepository<ZAPScanResult,String> {}
