package tr.com.stm.cydecsys.zaprestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.stm.cydecsys.zaprestapi.model.ZAPScanResult;
import tr.com.stm.cydecsys.zaprestapi.repository.ZAPRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ZAPScanService {

    @Autowired
    ZAPRepository zapRepository;

    public List<ZAPScanResult> findAll(){
        return (List<ZAPScanResult>) zapRepository.findAll();
    }
    public Optional<ZAPScanResult> getZAPScanResultById(int id){return zapRepository.findById(id);}
    public void saveOrUpdate(ZAPScanResult addingURL){
        zapRepository.save(addingURL);
    }
    public void delete(int id){
        zapRepository.deleteById(id);
    }
}
