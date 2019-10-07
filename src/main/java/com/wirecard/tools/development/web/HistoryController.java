package com.wirecard.tools.development.web;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Sorts;
import com.wirecard.tools.development.model.HistoryLog;
import com.wirecard.tools.development.model.HistoryLogRepository;
import com.wirecard.tools.development.model.ScriptHistory;
import com.wirecard.tools.development.model.ScriptHistoryRepository;
import org.apache.logging.log4j.util.Strings;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
class HistoryController {

    private final Logger log = LoggerFactory.getLogger(HistoryController.class);

    @Autowired
    private HistoryLogRepository historyLogRepository;

    @Autowired
    private ScriptHistoryRepository scriptHistoryRepository;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    private SimpleDateFormat displayedFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");

    @GetMapping("/histories")
    Collection<HistoryLog> histories(@RequestParam String startTime, @RequestParam String endTime, @RequestParam String uniqueFlag) throws ParseException {
        Date startTimeParsed = sdf.parse(startTime);
        Date endTimeParsed = sdf.parse(endTime);
        List historyLogList = new ArrayList<>();
        List<HistoryLog> res = (List<HistoryLog>) historyLogRepository.findAllByModifiedDateBetweenAndActionNotInAndActivityTypeNotIn(startTimeParsed,
                endTimeParsed,
                Arrays.asList("EXPORT", "IMPORT", "GENERATE", "DOWNLOAD SOURCE", "DOWNLOAD JAR", "DOWNLOAD DOCKER IMAGE", "RESET PASSWORD"),
                Arrays.asList("MENU_CONFIG", "LANGUAGE", "MONITORING_SERVICES", "PROCESS_FLOW_GENERATOR"));
        res.sort(new Comparator<HistoryLog>() {
            @Override
            public int compare(HistoryLog o1, HistoryLog o2) {
                return o1.getModifiedDate().compareTo(o2.getModifiedDate());
            }
        });

        Map<String, Set> uniqueActivityType = new HashMap<String, Set>();
        for (HistoryLog item : res) {
            String description = "";
            if("true".equals(uniqueFlag)) description = this.checkUniqueMap(uniqueActivityType, item);
            else description = item.getDescription();
            if(description == null || description.equals("")) continue;

            Map map = new HashMap();
            // map.put("id", item.get_id());
            map.put("activityType", item.getActivityType() != null ? item.getActivityType() : "");
            // map.put("comment", item.getComment());
            map.put("description", description);
            map.put("action", item.getAction());
            map.put("modifiedDate", displayedFormat.format(item.getModifiedDate()));
            map.put("modifiedBy", item.getModifiedBy());

            historyLogList.add(map);
        }

        historyLogList.sort(new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                return o1.get("activityType").toString().compareTo(o2.get("activityType").toString());
            }
        });

        return historyLogList;
    }

    private String checkUniqueMap(Map<String, Set> uniqueActivityType, HistoryLog item) {
        Set uniqueList;
        Set currentList = new HashSet();
        if(item != null && item.getDescription() != null && !"".equals(item.getDescription())) {
            String description = item.getDescription().toLowerCase().trim();
            uniqueList = uniqueActivityType.get(item.getActivityType());
            if(uniqueList == null) uniqueList = new HashSet();
            String[] splits = description.split(",");
            for(String split : splits) {
                String data = split;
                if("MODEL".equals(item.getActivityType()) && data.indexOf("_V_") == -1) {
                    ScriptHistory scriptHistory = scriptHistoryRepository.findBy_id(data);
                    if(scriptHistory != null) {
                        data = scriptHistory.getValueId().toLowerCase();
                    }
                } else if("FUNCTION_STRUCTURE".equals(item.getActivityType())) {
                    String[] structureSplitter = data.split("-");
                    if(structureSplitter != null && structureSplitter.length > 0) {
                        data = structureSplitter[0].trim();
                    }
                }
                if(uniqueList.add(data)) currentList.add(data);
            }
        } else {
            uniqueList = new HashSet();
        }
        uniqueActivityType.put(item.getActivityType(), uniqueList);
        return Strings.join(currentList, ',');
    }

    @GetMapping("/history/{id}")
    ResponseEntity<?> getGroup(@PathVariable String id) {
        Optional<HistoryLog> group = historyLogRepository.findById(id);
        return group.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/history")
    ResponseEntity<HistoryLog> createGroup(@Valid @RequestBody HistoryLog group) throws URISyntaxException {
        log.info("Request to create group: {}", group);
        HistoryLog result = historyLogRepository.save(group);
        return ResponseEntity.created(new URI("/api/group/" + result.get_id()))
                .body(result);
    }

    @PutMapping("/history/{id}")
    ResponseEntity<HistoryLog> updateGroup(@Valid @RequestBody HistoryLog group) {
        log.info("Request to update group: {}", group);
        HistoryLog result = historyLogRepository.save(group);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable String id) {
        log.info("Request to delete group: {}", id);
        historyLogRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}