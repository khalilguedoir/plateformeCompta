package com.example.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.io.InputStream;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InsertPublicData implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InsertPublicData(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        insertLanguages();
        insertCurrencies();
        insertTimezones();
        insertCountries();
    }

    private void insertLanguages() throws Exception {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM public.languages", Integer.class);
        if (count != null && count > 0) return;

        InputStream is = getClass().getResourceAsStream("/data/languages.json");
        List<Map<String, String>> languages = objectMapper.readValue(is, new TypeReference<>() {});
        for (Map<String, String> lang : languages) {
            jdbcTemplate.update(
                "INSERT INTO public.languages (code, name) VALUES (?, ?)",
                lang.get("code"), lang.get("name")
            );
        }
        System.out.println("Languages inserted");
    }

    private void insertCurrencies() throws Exception {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM public.currencies", Integer.class);
        if (count != null && count > 0) return;

        InputStream is = getClass().getResourceAsStream("/data/currencies.json");
        List<Map<String, String>> currencies = objectMapper.readValue(is, new TypeReference<>() {});
        for (Map<String, String> cur : currencies) {
            jdbcTemplate.update(
                "INSERT INTO public.currencies (code, name, symbol) VALUES (?, ?, ?)",
                cur.get("code"), cur.get("name"), cur.get("symbol")
            );
        }
        System.out.println("Currencies inserted");
    }

    private void insertTimezones() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM public.timezones", Integer.class);
        if (count != null && count > 0) return;

        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        for (String zone : zoneIds) {
            jdbcTemplate.update(
                "INSERT INTO public.timezones (tz_name, utc_offset) VALUES (?, ?)",
                zone, ZoneId.of(zone).getRules().getOffset(java.time.Instant.now()).toString()
            );
        }
        System.out.println("Timezones inserted");
    }

    private void insertCountries() throws Exception {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM public.countries", Integer.class);
        if (count != null && count > 0) return;

        InputStream is = getClass().getResourceAsStream("/data/countries.json");
        List<Map<String, String>> countries = objectMapper.readValue(is, new TypeReference<>() {});
        for (Map<String, String> c : countries) {
            jdbcTemplate.update(
                "INSERT INTO public.countries (iso2, iso3, name, phone_code, capital, region, subregion, currency_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT id FROM public.currencies WHERE code=?))",
                c.get("iso2"), c.get("iso3"), c.get("name"), c.get("phone_code"),
                c.get("capital"), c.get("region"), c.get("subregion"), c.get("currency_code")
            );
        }
        System.out.println("Countries inserted");
    }
}
