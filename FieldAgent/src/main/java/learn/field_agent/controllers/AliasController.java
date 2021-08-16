package learn.field_agent.controllers;

import learn.field_agent.domain.AliasService;
import learn.field_agent.domain.Result;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.AliasAgent;
import learn.field_agent.models.Location;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/agent")
public class AliasController {

    private final AliasService service;

    public AliasController(AliasService service) {
        this.service = service;
    }

    @GetMapping
    public List<Alias> findAll() {
        return service.findAll();
    }

    @GetMapping("/{agentId}")
    public AliasAgent findAgentByAlias(@PathVariable int agentId) {
        return service.findAgentByAlias(agentId);
    }

//    Add an alias.
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Alias alias) {
        Result<Alias> result = service.add(alias);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

//    Update an alias.
    @PutMapping("/{aliasId}")
    public ResponseEntity<Object> update(@PathVariable int aliasId, @RequestBody Alias alias) {
        if (aliasId != alias.getAliasID()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Alias> result = service.update(alias);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

//    Delete an alias. (No strategy required. An alias is never referenced elsewhere.)
    @DeleteMapping("/{aliasId}")
    public ResponseEntity<Void> deleteById(@PathVariable int aliasId) {
        if(service.deleteById(aliasId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
