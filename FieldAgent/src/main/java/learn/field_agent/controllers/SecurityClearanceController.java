package learn.field_agent.controllers;

import learn.field_agent.domain.Result;
import learn.field_agent.domain.SecurityService;
import learn.field_agent.models.Agency;
import learn.field_agent.models.SecurityClearance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/security")
public class SecurityClearanceController {
    private final SecurityService service;

    public SecurityClearanceController(SecurityService service) {
        this.service = service;
    }

//    Find all security clearances.
    @GetMapping
    public List<SecurityClearance> findAll() {
        return service.findAll();
    }

//    Find a security clearance by its identifier.
    @GetMapping("/{securityID}")
    public ResponseEntity<SecurityClearance> findById(@PathVariable int securityID) {
        SecurityClearance security = service.findById(securityID);
        if (security == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(security);
    }

//    Add a security clearance.
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody SecurityClearance securityClearance) {
        Result<SecurityClearance> result = service.add(securityClearance);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

//    Update an existing security clearance.
    @PutMapping("/{securityID}")
    public ResponseEntity<Object> update(@PathVariable int securityID, @RequestBody SecurityClearance securityClearance) {
        if (securityID != securityClearance.getSecurityClearanceId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<SecurityClearance> result = service.update(securityClearance);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

//    Delete a security clearance. (This requires a strategy.
//    It's probably not appropriate to delete agency_agent records that depend on a security clearance.
//    Only allow deletion if a security clearance key isn't referenced.)
}
