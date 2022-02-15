package tr.com.stm.cydecsys.zaprestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.stm.cydecsys.zaprestapi.model.ZAPScanResult;
import tr.com.stm.cydecsys.zaprestapi.repository.ZAPRepository;

import java.util.List;
import java.util.Optional;


/*
    This is Service class. It helps us to save, delete, get data from our database.
    This is a gate between Controller and mongoRepository
 */
@Service
public class ZAPScanService {

    @Autowired
    ZAPRepository zapRepository;

    public List<ZAPScanResult> findAll(){
        return (List<ZAPScanResult>)zapRepository.findAll();
    }
    public void deleteAllDatabase(){
        zapRepository.deleteAll();
    }
    public Optional<ZAPScanResult> getZAPScanResultById(String id){
        return zapRepository.findById(id);}
    public void saveOrUpdate(ZAPScanResult addingZAPScanResult){
        zapRepository.save(addingZAPScanResult);
    }
    public void delete(String  id){
        zapRepository.deleteById(id);
    }
}
