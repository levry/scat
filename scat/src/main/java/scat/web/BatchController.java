package scat.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scat.batch.address.Address;
import scat.batch.address.AddressWriter;
import scat.batch.school.SchoolData;
import scat.batch.school.SchoolWriteResult;
import scat.batch.school.SchoolWriter;

import java.util.List;

/**
 * @author levry
 */
@RestController
@RequestMapping("/batch")
public class BatchController {

    private final AddressWriter addressWriter;
    private final SchoolWriter schoolWriter;

    public BatchController(AddressWriter addressWriter, SchoolWriter schoolWriter) {
        this.addressWriter = addressWriter;
        this.schoolWriter = schoolWriter;
    }

    @PostMapping("/address")
    public void address(@RequestBody List<Address> input) {
        addressWriter.put(input);
    }

    @PostMapping("/school")
    public SchoolWriteResult school(@RequestBody List<SchoolData> input) {
        return schoolWriter.put(input);
    }
}
