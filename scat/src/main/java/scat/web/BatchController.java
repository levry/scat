package scat.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scat.batch.address.Address;
import scat.batch.address.AddressWriter;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author levry
 */
@RestController
@RequestMapping("/batch")
public class BatchController {
    private final AddressWriter addressWriter;

    public BatchController(AddressWriter addressWriter) {
        this.addressWriter = addressWriter;
    }

    @RequestMapping(value = "/address", method = POST)
    public void address(@RequestBody List<Address> input) {
        addressWriter.put(input);
    }
}
