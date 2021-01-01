package scat.adapter.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scat.domain.batch.address.Address;
import scat.domain.batch.address.AddressWriter;
import scat.domain.batch.school.SchoolData;
import scat.domain.batch.school.SchoolWriteResult;
import scat.domain.batch.school.SchoolWriter;

import java.util.List;

/**
 * @author levry
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/batch")
public class BatchController {

    private final AddressWriter addressWriter;
    private final SchoolWriter schoolWriter;

    @PostMapping("/address")
    public void address(@RequestBody List<Address> input) {
        addressWriter.put(input);
    }

    @PostMapping("/school")
    public SchoolWriteResult school(@RequestBody List<SchoolData> input) {
        return schoolWriter.put(input);
    }
}
