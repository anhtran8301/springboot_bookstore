package com.springboot.blog.controller;

import com.springboot.blog.payload.AuthorDto;
import com.springboot.blog.payload.MessageResponse;
import com.springboot.blog.payload.SaleDots.SaleReq;
import com.springboot.blog.payload.SaleDots.SaleRes;
import com.springboot.blog.service.SaleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/sales")
@Tag(
        name = "CRUD REST APIs for Sale Resource"
)
public class SaleController {
    private final SaleService saleService;

    @GetMapping
    public ResponseEntity<List<SaleRes>> getSales(){
        return ResponseEntity.ok(saleService.getSales());
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addSale(@Valid @RequestBody SaleReq request){
        try {
            return new ResponseEntity<>(saleService.addSale(request), HttpStatus.CREATED);
        } catch (Exception exception){
        return new ResponseEntity<>(new MessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateSale(@Valid @RequestBody SaleReq request){
        try {
            return new ResponseEntity<>(saleService.updateSale(request), HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>(new MessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<SaleRes> getCurrentSales(){
        return new ResponseEntity<>(saleService.getCurrentSales(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleRes> getSaleById(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(saleService.getSaleById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable(name = "id") Long id){
        saleService.deleteSale(id);
        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);

    }
}
