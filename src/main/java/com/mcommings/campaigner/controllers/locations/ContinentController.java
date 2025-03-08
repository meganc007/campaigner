package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.dtos.ContinentDTO;
import com.mcommings.campaigner.services.locations.ContinentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.UPDATE_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/locations/continents")
public class ContinentController {

    private final ContinentService continentService;

    @GetMapping
    public List<ContinentDTO> getContinents() {
        return continentService.getContinents();
    }

    @GetMapping(path = "/{continentId}")
    public ContinentDTO getContinent(@PathVariable("continentId") int continentId) {
        return continentService.getContinent(continentId).orElseThrow(() -> new IllegalArgumentException(UPDATE_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ContinentDTO> getContinentsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return continentService.getContinentsByCampaignUUID(uuid);
    }

    @PostMapping
    public ResponseEntity saveContinent(@Valid @RequestBody ContinentDTO continent) {
        continentService.saveContinent(continent);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "{continentId}")
    public ResponseEntity deleteContinent(@PathVariable("continentId") int continentId) {
        if (!continentService.deleteContinent(continentId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "{continentId}")
    public ResponseEntity updateContinent(@PathVariable("continentId") int continentId, @RequestBody ContinentDTO continent) {
        if (continentService.updateContinent(continentId, continent).isEmpty()) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        ;
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Return BadRequest with the error message
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
