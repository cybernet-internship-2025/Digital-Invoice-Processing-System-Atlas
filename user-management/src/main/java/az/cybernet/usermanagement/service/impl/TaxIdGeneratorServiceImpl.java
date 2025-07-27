package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.mapper.UserMapper;
import az.cybernet.usermanagement.service.TaxIdGeneratorService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class TaxIdGeneratorServiceImpl implements TaxIdGeneratorService {

    private final UserMapper userMapper;

    public TaxIdGeneratorServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public String generateUniqueTaxId() {
        String taxId;
        do {
            taxId = generateTaxId();
        } while (userMapper.findByTaxId(taxId) != null);
        return taxId;
    }

    private String generateTaxId() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        int randomPart = new Random().nextInt(9000) + 1000;
        return datePart + randomPart;
    }
}
